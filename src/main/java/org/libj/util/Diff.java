/* Copyright (c) 2017 LibJ
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

package org.libj.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import name.fraser.neil.plaintext.diff_match_patch;

/**
 * This class implements an algorithm and encoding (the diff) for the
 * representation of the steps necessary to transform a {@code target} string to
 * the {@code source} string. The generated diff can thereafter be used to
 * transform the {@code source} string back to the {@code target} string. The
 * diff information (i.e. the transformation steps) is encoded with bit-level
 * packing, allowing for a super compact representation.
 * <p>
 * This class utilizes a 3rd-party library named
 * <a href="https://github.com/google/diff-match-patch">diff-match-patch</a> for
 * synchronizing plain text.
 */
public class Diff {
  private static final Charset charset = Charset.forName("UTF-8");

  /**
   * Size of "LengthSize" is 5 bits, giving it 2^5 values (0 to 31), which
   * allows Length to have 2^31 values (0 to 2147483647, which are the min and
   * max lengths allowed for a String).
   */
  private static final byte lengthSizeSize = 5;

  /**
   * Returns the "LengthSize" (5 bits) from beginning of byte.
   *
   * @param src The byte.
   * @return The "LengthSize".
   */
  private static byte getLengthSize(final byte src) {
    return (byte)((src & 0xff) >> 3);
  }

  /**
   * Writes the {@code lengthSize} into {@code dest}. 5 bits are
   * reserved for {@code lengthSize}, allowing max value of 32.
   *
   * @param dest The destination array.
   * @param lengthSize The "LengthSize".
   * @return The "LengthSize".
   */
  private static int writeLengthSize(final byte[] dest, final byte lengthSize) {
    dest[0] |= lengthSize << (8 - lengthSizeSize);
    return lengthSizeSize;
  }

  /**
   * Writes the {@code ordinal} into {@code dest} at {@code offset}. 2 bits are
   * reserved for {@code ordinal}.
   *
   * @param dest The destination array.
   * @param offset The offset at which ordinal bits will be written.
   * @param ordinal The ordinal.
   * @return The new offset adjusted by the written bits.
   */
  private static int writeOrdinal(final byte[] dest, final int offset, final byte ordinal) {
    return Bytes.writeBitsB(dest, offset, ordinal, (byte)2);
  }

  /**
   * Writes the {@code length} into {@code dest} at {@code offset}.
   * {@code lengthSize} bits will be used for {@code length}.
   *
   * @param dest The destination array.
   * @param offset The offset at which length bits will be written.
   * @param length The length value.
   * @param lengthSize The number of bits used for {@code length}.
   * @return The new offset adjusted by the written bits.
   */
  private static int writeLength(final byte[] dest, final int offset, final int length, final byte lengthSize) {
    final byte[] bytes = new byte[1 + (lengthSize - 1) / 8];
    Bytes.toBytes(length, bytes, 0, true);
    return Bytes.writeBitsB(dest, offset, bytes, lengthSize);
  }

  /**
   * Writes the {@code text} into {@code dest} at {@code offset}.
   * {@code length} * 8 bits will be used for {@code text}.
   *
   * @param dest The destination array.
   * @param offset The offset at which text bits will be written.
   * @param text The text to write.
   * @param length The length in bytes of {@code text}.
   * @return The new offset adjusted by the written bits.
   */
  private static int writeText(final byte[] dest, final int offset, final byte[] text, final int length) {
    return Bytes.writeBitsB(dest, offset, text, length * 8);
  }

  /**
   * Decodes a byte array encoding of a diff into a {@link Diff} object.
   *
   * @param bytes The {@code byte[]} array.
   * @return The {@link Diff} object decoded from the byte array.
   * @throws NullPointerException If {@code bytes} is null.
   */
  public static Diff decode(final byte[] bytes) {
    final byte lengthSize = getLengthSize(bytes[0]);
    final int limit = bytes.length * 8 - lengthSize - 2;

    final List<Mod> mods = new ArrayList<>();
    final Diff diff = new Diff(mods, lengthSize);
    for (int offset = lengthSizeSize; offset < limit;) {
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

  /**
   * Class representing an abstract modification.
   */
  protected abstract class Mod {
    protected final int length;

    /**
     * Creates a new {@link Mod} with the specified length.
     *
     * @param length The length of the modification.
     */
    protected Mod(final int length) {
      this.length = length;
    }

    /**
     * Creates a new {@link Mod} with the length encoded in the specified bytes.
     *
     * @param src The bytes from which to decode the length.
     * @param offset The offset in bits specifying the start of the length bits.
     * @param lengthSize The number of bits representing the length.
     */
    protected Mod(final byte[] src, final int offset, final byte lengthSize) {
      this.length = Bytes.toInt(Bytes.readBitsFromBytes(src, offset, lengthSize), 0, true);
    }

    /**
     * Returns the 2-bit ordinal identifier for this modification.
     *
     * @return The 2-bit ordinal identifier for this modification.
     */
    protected abstract byte ordinal();

    /**
     * Patches the specified {@link StringBuilder} at the specified position
     * with the modification represented by this instance, returning the change
     * in length due to the patch operation.
     *
     * @param builder The {@link StringBuilder} in which to perform the patch.
     * @param position The position at which to perform the patch.
     * @return The change in length due to the patch operation.
     */
    protected abstract int patch(StringBuilder builder, int position);

    /**
     * Encodes this modification into the specified byte array, at the specified
     * offset of bits.
     *
     * @param dest The byte array into which this modification will be encoded.
     * @param offset The offset of bits in the specified array at which this
     *          modification will be encoded.
     * @return The new offset adjusted by the written bits.
     */
    protected int encode(final byte[] dest, int offset) {
      offset = writeOrdinal(dest, offset, ordinal());
      offset = writeLength(dest, offset, length, lengthSize);
      return offset;
    }

    /**
     * Returns the number of bits required for the encoding of this
     * modification.
     *
     * @return The number of bits required for the encoding of this
     *         modification.
     */
    protected int getSize() {
      return 2 + lengthSize;
    }
  }

  /**
   * Class representing an insert modification.
   */
  protected class Insert extends Mod {
    private final String text;

    /**
     * Creates a new {@link Insert} modification with the specified text.
     *
     * @param text The text representing the insert modification.
     */
    protected Insert(final String text) {
      super(text.length());
      this.text = text;
    }

    /**
     * Creates a new {@link Insert} modification with the length encoded in the
     * specified bytes.
     *
     * @param src The bytes from which to decode the length.
     * @param offset The offset in bits specifying the start of the length bits.
     * @param lengthSize The number of bits representing the length.
     */
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

  protected class Delete extends Mod {
    /**
     * Creates a new {@link Delete} modification with the specified length.
     *
     * @param length The length representing the delete modification.
     */
    protected Delete(final int length) {
      super(length);
    }

    /**
     * Creates a new {@link Delete} modification with the length encoded in the
     * specified bytes.
     *
     * @param src The bytes from which to decode the length.
     * @param offset The offset in bits specifying the start of the length bits.
     * @param lengthSize The number of bits representing the length.
     */
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

  protected class Replace extends Mod {
    private final String text;

    /**
     * Creates a new {@link Replace} modification with the specified text.
     *
     * @param text The text representing the insert modification.
     */
    protected Replace(final String text) {
      super(text.length());
      this.text = text;
    }

    /**
     * Creates a new {@link Replace} modification with the length encoded in the
     * specified bytes.
     *
     * @param src The bytes from which to decode the length.
     * @param offset The offset in bits specifying the start of the length bits.
     * @param lengthSize The number of bits representing the length.
     */
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

  protected class Equal extends Mod {
    /**
     * Creates a new {@link Equal} modification with the specified length.
     *
     * @param length The length representing the equal modification.
     */
    protected Equal(final int length) {
      super(length);
    }

    /**
     * Creates a new {@link Equal} modification with the length encoded in the
     * specified bytes.
     *
     * @param src The bytes from which to decode the length.
     * @param offset The offset in bits specifying the start of the length bits.
     * @param lengthSize The number of bits representing the length.
     */
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
   * Creates a {@link Diff} that represents the steps necessary to transform a
   * {@code target} string to the {@code source} string.
   *
   * @param source The source string.
   * @param target The target string.
   */
  public Diff(final String source, final String target) {
    final List<diff_match_patch.Diff> diffs = new diff_match_patch().diff_main(source, target);
    final Iterator<diff_match_patch.Diff> iterator = diffs.iterator();
    final List<Mod> mods = new ArrayList<>();
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

    this.lengthSize = maxLength == 0 ? 0 : Bytes.getSize(maxLength);
    this.mods = mods;
  }

  /**
   * Creates a {@link Diff} from a list of {@link Diff.Mod} objects,
   * and a {@code lengthSize} (the number of bits in the length number in
   * each {@link Diff.Mod} object).
   *
   * @param mods The list of {@link Diff.Mod} objects.
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
   * Patches a string with the list of {@link Diff.Mod} objects in this
   * {@link Diff}.
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
   * Encodes this {@link Diff} object into a byte array representation.
   *
   * @return This {@link Diff} object encoded to a byte array.
   */
  public byte[] toBytes() {
    int bits = lengthSizeSize;
    for (int i = 0; i < this.mods.size(); ++i)
      bits += this.mods.get(i).getSize();

    final byte[] dest = new byte[1 + (bits - 1) / 8];
    int offset = writeLengthSize(dest, lengthSize);
    for (int i = 0; i < this.mods.size(); ++i)
      offset = this.mods.get(i).encode(dest, offset);

    return dest;
  }

  /**
   * Returns the list of {@link Diff.Mod} objects in this {@link Diff}.
   *
   * @return The list of {@link Diff.Mod} objects in this {@link Diff}.
   */
  public List<Mod> getMods() {
    return this.mods;
  }
}