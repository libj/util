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
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.lib4j.lang.Bytes;
import org.lib4j.lang.Strings;

public class Diff {
  private static final Charset charset = Charset.forName("UTF-8");

  private Mod toMod(final DiffMatchPatch.Diff diff) {
    if (diff.operation == DiffMatchPatch.Operation.INSERT)
      return new Insert(diff.text);

    if (diff.operation == DiffMatchPatch.Operation.DELETE)
      return new Delete(diff.text.length());

    if (diff.operation == DiffMatchPatch.Operation.EQUAL)
      return new Equal(diff.text.length());

    throw new UnsupportedOperationException("Unsupported operation: " + diff.operation);
  }

  // (*) Get LengthSize (5 bits) from beginning of byte
  private static byte getLengthSize(final byte src) {
    return (byte)((src & 0xff) >> 3);
  }

  // 5 bits reserved for LengthSize, allowing max value of 32
  // (0) Write the lengthSize (5 bits)
  private static int writeLengthSize(final byte[] dest, final byte lengthSize) {
    dest[0] |= lengthSize << 3;
    return 5;
  }

  // (1) Write the ordinal (2 bits)
  private static int writeOrdinal(final byte[] dest, final int offset, final byte ordinal) {
    return Bytes.writeByte(dest, offset, ordinal, (byte)2);
  }

  // (2) Write the length (lengthSize bits)
  private static int writeLength(final byte[] dest, final int offset, final int length, final byte lengthSize) {
    final byte[] bytes = new byte[1 + (lengthSize - 1) / 8];
    Bytes.toBytes(length, bytes, 0, true);
    return Bytes.writeBytes(dest, offset, bytes, lengthSize);
  }

  // (3) Write the text (length * 8 bits)
  private static int writeText(final byte[] dest, final int offset, final byte[] text, final int length) {
    return Bytes.writeBytes(dest, offset, text, length * 8);
  }

  public abstract class Mod {
    protected final int length;

    public Mod(final int length) {
      this.length = length;
    }

    public Mod(final byte[] src, final int offset, final byte lengthSize) {
      this.length = Bytes.toInt(Bytes.readBytes(src, offset, lengthSize), 0, true);
    }

    protected abstract byte ordinal();

    protected int encode(final byte[] dest, int offset) {
      offset = writeOrdinal(dest, offset, ordinal());
      offset = writeLength(dest, offset, length, lengthSize);
      return offset;
    }

    protected int getSize() {
      return 2 + lengthSize;
    }

    public abstract int merge(final StringBuilder builder, final int position);

    public String toBitSetString() {
      return Strings.padFixed(Integer.toBinaryString(toBitSet().toByteArray()[0] & 0xFF), 4, false).replace(' ', '0');
    }

    public BitSet toBitSet() {
      final BitSet bitSet = new BitSet(4);
      bitSet.set(ordinal());
      return bitSet;
    }
  }

  public class Insert extends Mod {
    private final String text;

    public Insert(final String text) {
      super(text.length());
      this.text = text;
    }

    public Insert(final byte[] src, final int offset, final byte lengthSize) {
      super(src, offset, lengthSize);
      this.text = new String(Bytes.readBytes(src, offset + lengthSize, length * 8));
    }

    @Override
    protected byte ordinal() {
      return 0b00;
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
    public int merge(final StringBuilder builder, final int position) {
      builder.insert(position, text);
      return length;
    }

    @Override
    public String toString() {
      return "I " + length + " " + text;
    }
  }

  public class Delete extends Mod {
    public Delete(final int length) {
      super(length);
    }

    public Delete(final byte[] src, final int offset, final byte lengthSize) {
      super(src, offset, lengthSize);
    }

    @Override
    protected byte ordinal() {
      return 0b01;
    }

    @Override
    public int merge(final StringBuilder builder, final int position) {
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

    public Replace(final String text) {
      super(text.length());
      this.text = text;
    }

    public Replace(final byte[] src, final int offset, final byte lengthSize) {
      super(src, offset, lengthSize);
      this.text = new String(Bytes.readBytes(src, offset + lengthSize, length * 8));
    }

    @Override
    protected byte ordinal() {
      return 0b10;
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
    public int merge(final StringBuilder builder, final int position) {
      builder.replace(position, position + length, text);
      return length;
    }

    @Override
    public String toString() {
      return "R " + length + " " + text;
    }
  }

  public class Equal extends Mod {
    public Equal(final int length) {
      super(length);
    }

    public Equal(final byte[] src, final int offset, final byte lengthSize) {
      super(src, offset, lengthSize);
    }

    @Override
    public int merge(final StringBuilder builder, final int position) {
      return length;
    }

    @Override
    protected byte ordinal() {
      return 0b11;
    }

    @Override
    public String toString() {
      return "E " + length;
    }
  }

  public static Diff decode(final byte[] bytes) {
    final byte lengthSize = getLengthSize(bytes[0]);
    final int limit = bytes.length * 8 - lengthSize - 2;

    int offset = 5;
    final List<Mod> mods = new ArrayList<Mod>();
    final Diff diff = new Diff(mods, lengthSize);
    while (offset < limit) {
      final byte ordinal = Bytes.readByte(bytes, offset, (byte)2);
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

  private final List<Mod> mods;
  private final byte lengthSize;

  public Diff(final String source, final String target) {
    final List<DiffMatchPatch.Diff> diffs = new DiffMatchPatch().diffMain(source, target);
    final List<Mod> mods = new ArrayList<Mod>();
    final Iterator<DiffMatchPatch.Diff> iterator = diffs.iterator();
    while (iterator.hasNext()) {
      final DiffMatchPatch.Diff diff1 = iterator.next();
      if (diff1.operation == DiffMatchPatch.Operation.DELETE && iterator.hasNext()) {
        final DiffMatchPatch.Diff diff2 = iterator.next();
        if (diff2.operation == DiffMatchPatch.Operation.INSERT) {
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
      else if (diff1.operation == DiffMatchPatch.Operation.INSERT && iterator.hasNext()) {
        final DiffMatchPatch.Diff diff2 = iterator.next();
        if (diff2.operation == DiffMatchPatch.Operation.DELETE) {
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
      else if (diff1.operation != DiffMatchPatch.Operation.EQUAL || iterator.hasNext()) {
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

  public Diff(final List<Mod> mods, final byte lengthSize) {
    this.mods = mods;
    this.lengthSize = lengthSize;
  }

  public String patch(final String source) {
    final StringBuilder builder = new StringBuilder(source);
    int position = 0;
    for (final Mod mod : mods)
      position += mod.merge(builder, position);

    return builder.toString();
  }

  public byte[] toBytes() {
    int bits = 5;
    for (int i = 0; i < this.mods.size(); i++)
      bits += this.mods.get(i).getSize();

    final byte[] dest = new byte[1 + (bits - 1) / 8];
    int offset = writeLengthSize(dest, lengthSize);
    for (int i = 0; i < this.mods.size(); i++)
      offset = this.mods.get(i).encode(dest, offset);

    return dest;
  }

  public List<Mod> getMods() {
    return this.mods;
  }
}