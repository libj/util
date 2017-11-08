/* Copyright (c) 2017 lib4j
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.lib4j.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lib4j.lang.Bytes;

import name.fraser.neil.plaintext.diff_match_patch;

public class Diff {
  private static final Charset charset = Charset.forName("UTF-8");
  // Size of LengthSize is 5 bits, giving it 2^5 values (0 to 31), which
  // allows Length to have 2^31 values (0 to 2147483647, which are the min and
  // max lengths allowed for a String.
  private static final byte lengthSizeSize = 5;

  // (*) Get LengthSize (5 bits) from beginning of byte
  private static byte getLengthSize(final byte src) {
    return (byte)((src & 0xff) >> 3);
  }

  // 5 bits reserved for LengthSize, allowing max value of 32
  // (0) Write the lengthSize (5 bits)
  private static int writeLengthSize(final byte[] dest, final byte lengthSize) {
    dest[0] |= lengthSize << (8 - lengthSizeSize);
    return lengthSizeSize;
  }

  // (1) Write the ordinal (2 bits)
  private static int writeOrdinal(final byte[] dest, final int offset, final byte ordinal) {
    return Bytes.writeBitsB(dest, offset, ordinal, (byte)2);
  }

  // (2) Write the length (lengthSize bits)
  private static int writeLength(final byte[] dest, final int offset, final int length, final byte lengthSize) {
    final byte[] bytes = new byte[1 + (lengthSize - 1) / 8];
    Bytes.toBytes(length, bytes, 0, true);
    return Bytes.writeBitsB(dest, offset, bytes, lengthSize);
  }

  // (3) Write the text (length * 8 bits)
  private static int writeText(final byte[] dest, final int offset, final byte[] text, final int length) {
    return Bytes.writeBitsB(dest, offset, text, length * 8);
  }

  /**
   * Decode a byte array encoding of a diff into a <code>Diff</code> object.
   *
   * @param bytes The byte array.
   * @return The <code>Diff</code> object decoded from the byte array.
   */
  public static Diff decode(final byte[] bytes) {
    final byte lengthSize = getLengthSize(bytes[0]);
    final int limit = bytes.length * 8 - lengthSize - 2;

    int offset = lengthSizeSize;
    final List<Mod> mods = new ArrayList<Mod>();
    final Diff diff = new Diff(mods, lengthSize);
    while (offset < limit) {
      final byte ordinal = Bytes.readBitsFromByte(bytes, offset, (byte)2);
      offset += 2;
      final Mod mod;
      if (ordinal == 0b00) {
        mod = diff.new Insert(bytes, offset, lengthSize);
        offset += lengthSize + mod.length * 8;
      }
      else if (ordinal == 0b01) {
        mod = diff.new Delete(bytes, offset, lengthSize);
        offset += lengthSize;
      }
      else if (ordinal == 0b10) {
        mod = diff.new Replace(bytes, offset, lengthSize);
        offset += lengthSize + mod.length * 8;
      }
      else if (ordinal == 0b11) {
        mod = diff.new Equal(bytes, offset, lengthSize);
        offset += lengthSize;
      }
      else {
        throw new UnsupportedOperationException("Unsupported ordinal: " + ordinal);
      }

      // This check is needed, because it is possible that the 0+ trailing bits
      // in the last byte of the diff can be interpreted as 0b00 (I) of 0 length.
      if (mod.length > 0)
        mods.add(mod);
    }

    return diff;
  }

  public abstract class Mod {
    protected final int length;

    protected Mod(final int length) {
      this.length = length;
    }

    protected Mod(final byte[] src, final int offset, final byte lengthSize) {
      this.length = Bytes.toInt(Bytes.readBitsFromBytes(src, offset, lengthSize), 0, true);
    }

    protected abstract byte ordinal();
    protected abstract int patch(final StringBuilder builder, final int position);

    protected int encode(final byte[] dest, int offset) {
      offset = writeOrdinal(dest, offset, ordinal());
      offset = writeLength(dest, offset, length, lengthSize);
      return offset;
    }

    protected int getSize() {
      return 2 + lengthSize;
    }
  }

  public class Insert extends Mod {
    private final String text;

    protected Insert(final String text) {
      super(text.length());
      this.text = text;
    }

    protected Insert(final byte[] src, final int offset, final byte lengthSize) {
      super(src, offset, lengthSize);
      this.text = new String(Bytes.readBitsFromBytes(src, offset + lengthSize, length * 8));
    }

    @Override
    protected byte ordinal() {
      return 0b00;
    }

    @Override
    protected int patch(final StringBuilder builder, final int position) {
      builder.insert(position, text);
      return length;
    }

    @Override
    protected int encode(final byte[] dest, int offset) {
      offset = super.encode(dest, offset);
      offset = writeText(dest, offset, text.getBytes(charset), length);
      return offset;
    }

    @Override
    protected int getSize() {
      return super.getSize() + length * 8;
    }

    @Override
    public String toString() {
      return "I " + length + " " + text;
    }
  }

  public class Delete extends Mod {
    protected Delete(final int length) {
      super(length);
    }

    protected Delete(final byte[] src, final int offset, final byte lengthSize) {
      super(src, offset, lengthSize);
    }

    @Override
    protected byte ordinal() {
      return 0b01;
    }

    @Override
    protected int patch(final StringBuilder builder, final int position) {
      builder.delete(position, position + length);
      return 0;
    }

    @Override
    public String toString() {
      return "D " + length;
    }
  }

  public class Replace extends Mod {
    private final String text;

    protected Replace(final String text) {
      super(text.length());
      this.text = text;
    }

    protected Replace(final byte[] src, final int offset, final byte lengthSize) {
      super(src, offset, lengthSize);
      this.text = new String(Bytes.readBitsFromBytes(src, offset + lengthSize, length * 8));
    }

    @Override
    protected byte ordinal() {
      return 0b10;
    }

    @Override
    protected int patch(final StringBuilder builder, final int position) {
      builder.replace(position, position + length, text);
      return length;
    }

    @Override
    protected int encode(final byte[] dest, int offset) {
      offset = super.encode(dest, offset);
      offset = writeText(dest, offset, text.getBytes(charset), length);
      return offset;
    }

    @Override
    protected int getSize() {
      return super.getSize() + length * 8;
    }

    @Override
    public String toString() {
      return "R " + length + " " + text;
    }
  }

  public class Equal extends Mod {
    protected Equal(final int length) {
      super(length);
    }

    protected Equal(final byte[] src, final int offset, final byte lengthSize) {
      super(src, offset, lengthSize);
    }

    @Override
    protected byte ordinal() {
      return 0b11;
    }

    @Override
    protected int patch(final StringBuilder builder, final int position) {
      return length;
    }

    @Override
    public String toString() {
      return "E " + length;
    }
  }

  private final List<Mod> mods;
  private final byte lengthSize;

  /**
   * Create a <code>Diff</code> that represents the steps necessary to
   * transform a <code>target</code> string to the <code>source</code> string.
   *
   * @param source The source string.
   * @param target The target string.
   */
  public Diff(final String source, final String target) {
    final List<diff_match_patch.Diff> diffs = new diff_match_patch().diff_main(source, target);
    final Iterator<diff_match_patch.Diff> iterator = diffs.iterator();
    final List<Mod> mods = new ArrayList<Mod>();
    while (iterator.hasNext()) {
      final diff_match_patch.Diff diff1 = iterator.next();
      if (diff1.operation == diff_match_patch.Operation.DELETE && iterator.hasNext()) {
        final diff_match_patch.Diff diff2 = iterator.next();
        if (diff2.operation == diff_match_patch.Operation.INSERT) {
          if (diff1.text.length() > diff2.text.length()) {
            mods.add(new Replace(diff2.text));
            mods.add(new Delete(diff1.text.substring(diff2.text.length()).length()));
          }
          else if (diff1.text.length() < diff2.text.length()) {
            mods.add(new Replace(diff2.text.substring(0, diff1.text.length())));
            mods.add(new Insert(diff2.text.substring(diff1.text.length())));
          }
          else {
            mods.add(new Replace(diff2.text));
          }
        }
        else {
          mods.add(toMod(diff1));
          mods.add(toMod(diff2));
        }
      }
      else if (diff1.operation == diff_match_patch.Operation.INSERT && iterator.hasNext()) {
        final diff_match_patch.Diff diff2 = iterator.next();
        if (diff2.operation == diff_match_patch.Operation.DELETE) {
          if (diff1.text.length() > diff2.text.length()) {
            mods.add(new Replace(diff1.text.substring(0, diff2.text.length())));
            mods.add(new Insert(diff1.text.substring(diff2.text.length())));
          }
          else if (diff1.text.length() < diff2.text.length()) {
            mods.add(new Replace(diff1.text));
            mods.add(new Delete(diff2.text.substring(diff1.text.length()).length()));
          }
          else {
            mods.add(new Replace(diff1.text));
          }
        }
        else {
          mods.add(toMod(diff1));
          mods.add(toMod(diff2));
        }
      }
      else if (diff1.operation != diff_match_patch.Operation.EQUAL || iterator.hasNext()) {
        mods.add(toMod(diff1));
      }
    }

    int maxLength = 0;
    for (final Mod mod : mods)
      if (mod.length > maxLength)
        maxLength = mod.length;

    this.lengthSize = Bytes.getSize(maxLength);
    this.mods = mods;
  }

  /**
   * Create a <code>Diff</code> from a list of <code>Diff.Mod</code> objects,
   * and a <code>lengthSize</code> (the number of bits in the length number in
   * each <code>Diff.Mod</code> object).
   *
   * @param source The source string.
   * @param target The target string.
   * @param mods The list of <code>Diff.Mod</code> objects.
   * @param lengthSize The number of bits in the length number.
   */
  public Diff(final List<Mod> mods, final byte lengthSize) {
    this.mods = mods;
    this.lengthSize = lengthSize;
  }

  private Mod toMod(final diff_match_patch.Diff diff) {
    if (diff.operation == diff_match_patch.Operation.INSERT)
      return new Insert(diff.text);

    if (diff.operation == diff_match_patch.Operation.DELETE)
      return new Delete(diff.text.length());

    if (diff.operation == diff_match_patch.Operation.EQUAL)
      return new Equal(diff.text.length());

    throw new UnsupportedOperationException("Unsupported operation: " + diff.operation);
  }

  /**
   * Patch a string with the list of <code>Diff.Mod</code> objects in this
   * <code>Diff</code>.
   *
   * @param string The string.
   * @return The resulting string from applying the diff onto the argument
   *         string.
   */
  public String patch(final String string) {
    final StringBuilder builder = new StringBuilder(string);
    int position = 0;
    for (final Mod mod : mods)
      position += mod.patch(builder, position);

    return builder.toString();
  }

  /**
   * Encode this <code>Diff</code> object into a byte array representation.
   *
   * @return This <code>Diff</code> object encoded to a byte array.
   */
  public byte[] toBytes() {
    int bits = lengthSizeSize;
    for (int i = 0; i < this.mods.size(); i++)
      bits += this.mods.get(i).getSize();

    final byte[] dest = new byte[1 + (bits - 1) / 8];
    int offset = writeLengthSize(dest, lengthSize);
    for (int i = 0; i < this.mods.size(); i++)
      offset = this.mods.get(i).encode(dest, offset);

    return dest;
  }

  /**
   * Get the list of <code>Diff.Mod</code> objects in this <code>Diff</code>.
   *
   * @return The list of <code>Diff.Mod</code> objects.
   */
  public List<Mod> getMods() {
    return this.mods;
  }
}