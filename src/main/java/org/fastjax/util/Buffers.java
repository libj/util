/* Copyright (c) 2018 FastJAX
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

package org.fastjax.util;

/**
 * This utility class implements functions to {@link #set()}, {@link #clear()},
 * and {@link #get()} bit values from an argument {@code byte[]}.
 */
public final class Buffers {
  /**
   * Sets the bit at the specified index in {@code buf} to {@code 1}. If the bit
   * index is greater than the number of bits in the {@code buf} array, the
   * array will be resized based on the {@code resize} argument.
   * <p>
   * The {@code resize} argument behaves by the following rules:
   * <ul>
   * <li>If {@code resize == 0}, do not resize the array. An
   * {@code ArrayIndexOutOfBoundsException} will be thrown.</li>
   * <li>If {@code resize > 0}, set the size of the array to the value of the
   * index of the byte that {@code bit} belongs to, plus 1, multiplied by
   * {@code resize}:
   * <blockquote><pre>(int)(((bit / 8) + 1) * resize)</pre></blockquote>
   * <li>If {@code resize < 0}, set the size of the array to the value of the
   * index of the byte that {@code bit} belongs to, minus {@code (int)resize}:
   * <blockquote><pre>(bit / 8) - (int)resize</pre></blockquote>
   * </ul>
   *
   * @param buf The {@code byte[]} buffer.
   * @param bit The index of the bit to be set.
   * @param resize The resize factor.
   * @throws ArrayIndexOutOfBoundsException If the specified index is negative,
   *           or if {@code resize == 0}. and the bit index is greater than the
   *           number of bits in the {@code buf} array.
   * @throws NullPointerException If the specified array is null.
   */
  public static byte[] set(byte[] buf, final int bit, final double resize) {
    final int i = bit / Byte.SIZE;
    if (i >= buf.length) {
      if (resize == 0)
        throw new ArrayIndexOutOfBoundsException(bit);

      final byte[] resized = new byte[resize < 0 ? i - (int)resize : (int)((i + 1) * resize)];
      System.arraycopy(buf, 0, resized, 0, buf.length);
      buf = resized;
    }

    buf[i] |= 1 << (bit % Byte.SIZE);
    return buf;
  }

  /**
   * Sets the bit at the specified index in {@code buf} to {@code 1}. If the bit
   * index is greater than the number of bits in the {@code buf} array, the
   * array will be resized based on the {@code resize} argument.
   * <p>
   * The {@code resize} argument behaves by the following rules:
   * <ul>
   * <li>If {@code resize == 0}, do not resize the array. An
   * {@code ArrayIndexOutOfBoundsException} will be thrown.</li>
   * <li>If {@code resize > 0}, set the size of the array to the value of the
   * index of the long that {@code bit} belongs to, plus 1, multiplied by
   * {@code resize}:
   * <blockquote><pre>(int)(((bit / 64) + 1) * resize)</pre></blockquote>
   * <li>If {@code resize < 0}, set the size of the array to the value of the
   * index of the long that {@code bit} belongs to, minus {@code (int)resize}:
   * <blockquote><pre>(bit / 64) - (int)resize</pre></blockquote>
   * </ul>
   *
   * @param buf The {@code long[]} buffer.
   * @param bit The index of the bit to be set.
   * @param resize The resize factor.
   * @throws ArrayIndexOutOfBoundsException If the specified index is negative,
   *           or if {@code resize == 0}. and the bit index is greater than the
   *           number of bits in the {@code buf} array.
   * @throws NullPointerException If the specified array is null.
   */
  public static long[] set(long[] buf, final int bit, final double resize) {
    final int i = bit / Long.SIZE;
    if (i >= buf.length) {
      if (resize == 0)
        throw new ArrayIndexOutOfBoundsException(bit);

      final long[] resized = new long[resize < 0 ? i - (int)resize : (int)((i + 1) * resize)];
      System.arraycopy(buf, 0, resized, 0, buf.length);
      buf = resized;
    }

    buf[i] |= 1l << (bit % Long.SIZE);
    return buf;
  }

  /**
   * Sets the bit specified by the index to {@code false}.
   *
   * @param bit The index of the bit to be cleared.
   * @throws ArrayIndexOutOfBoundsException If the specified index is negative.
   * @throws NullPointerException If the specified array is null.
   */
  public static byte[] clear(final byte[] buf, final int bit) {
    final int i = bit / Byte.SIZE;
    if (i >= buf.length)
      return buf;

    buf[bit / Byte.SIZE] &= ~(1 << (bit % Byte.SIZE));
    return buf;
  }

  /**
   * Sets the bit specified by the index to {@code false}.
   *
   * @param bit The index of the bit to be cleared.
   * @throws ArrayIndexOutOfBoundsException If the specified index is negative.
   * @throws NullPointerException If the specified array is null.
   */
  public static long[] clear(final long[] buf, final int bit) {
    final int i = bit / Long.SIZE;
    if (i >= buf.length)
      return buf;

    buf[bit / Long.SIZE] &= ~(1l << (bit % Long.SIZE));
    return buf;
  }

  /**
   * Returns the value of the bit at the specified index. The value is
   * {@code true} if the bit with the index {@code bit} is currently set in
   * {@code buf}; otherwise, the result is {@code false}.
   *
   * @param buf The {@code byte[]} buffer.
   * @param bit The bit index.
   * @return The value of the bit at the specified index.
   * @throws ArrayIndexOutOfBoundsException If the specified index is negative.
   * @throws NullPointerException If the specified array is null.
   */
  public static boolean get(final byte[] buf, final int bit) {
    final int i = bit / Byte.SIZE;
    return i < buf.length && ((buf[i] >> (bit % Byte.SIZE)) & 1) != 0;
  }

  /**
   * Returns the value of the bit at the specified index. The value is
   * {@code true} if the bit with the index {@code bit} is currently set in
   * {@code buf}; otherwise, the result is {@code false}.
   *
   * @param buf The {@code long[]} buffer.
   * @param bit The bit index.
   * @return The value of the bit at the specified index.
   * @throws ArrayIndexOutOfBoundsException If the specified index is negative.
   * @throws NullPointerException If the specified array is null.
   */
  public static boolean get(final long[] buf, final int bit) {
    final int i = bit / Long.SIZE;
    return i < buf.length && ((buf[i] >> (bit % Long.SIZE)) & 1) != 0;
  }

  /**
   * Returns the "logical size" of {@code buf}: the index of the byte with the
   * highest set bit in {@code buf}, plus one. Returns zero if {@code buf}
   * contains no set bits.
   *
   * @param buf The {@code byte[]} buffer.
   * @return The logical size of the {@code buf}.
   * @throws NullPointerException If the specified array is null.
   */
  public static int length(final byte[] buf) {
    if (buf.length == 0)
      return 0;

    int i = buf.length - 1;
    while (buf[i] == 0 && --i >= 0);
    return i + 1;
  }

  /**
   * Returns the "logical size" of {@code buf}: the index of the long with the
   * highest set bit in {@code buf}, plus one. Returns zero if {@code buf}
   * contains no set bits.
   *
   * @param buf The {@code long[]} buffer.
   * @return The logical size of the {@code buf}.
   * @throws NullPointerException If the specified array is null.
   */
  public static int length(final long[] buf) {
    if (buf.length == 0)
      return 0;

    int i = buf.length - 1;
    while (buf[i] == 0 && --i >= 0);
    return i + 1;
  }

  /**
   * Returns a trimmed byte array of {@code buf}: the length of a trimmed byte
   * array is its "logical size".
   * <ul>
   * <li>If the length of {@code buf} is its "logical size", the reference to
   * {@code buf} is returned.</li>
   * <li>If the length of {@code buf} is greater than its "logical size", a
   * reference to a new byte array is returned.</li>
   * </ul>
   *
   * @param buf The {@code byte[]} buffer.
   * @return Returns a trimmed byte array of {@code buf}.
   * @throws NullPointerException If the specified array is null.
   * @see #length(byte[])
   */
  public static byte[] trimToLength(final byte[] buf) {
    final int length = length(buf);
    if (buf.length == length)
      return buf;

    final byte[] trimmed = new byte[length];
    System.arraycopy(buf, 0, trimmed, 0, length);
    return trimmed;
  }

  /**
   * Returns a trimmed long array of {@code buf}: the length of a trimmed long
   * array is its "logical size".
   * <ul>
   * <li>If the length of {@code buf} is its "logical size", the reference to
   * {@code buf} is returned.</li>
   * <li>If the length of {@code buf} is greater than its "logical size", a
   * reference to a new long array is returned.</li>
   * </ul>
   *
   * @param buf The {@code long[]} buffer.
   * @return Returns a trimmed long array of {@code buf}.
   * @throws NullPointerException If the specified array is null.
   * @see #length(long[])
   */
  public static long[] trimToLength(final long[] buf) {
    final int length = length(buf);
    if (buf.length == length)
      return buf;

    final long[] trimmed = new long[length];
    System.arraycopy(buf, 0, trimmed, 0, length);
    return trimmed;
  }

  private static final char[] digits = {
    '0', '1', '2', '3', '4', '5',
    '6', '7', '8', '9', 'a', 'b',
    'c', 'd', 'e', 'f', 'g', 'h',
    'i', 'j', 'k', 'l', 'm', 'n',
    'o', 'p', 'q', 'r', 's', 't',
    'u', 'v', 'w', 'x', 'y', 'z'
  };

  private static String toUnsignedString(long val, final int size) {
    final byte[] buf = new byte[size];
    int pos = size;
    do {
      buf[--pos] = (byte)digits[((int)val) & 1];
      val >>>= 1;
    }
    while (pos > 0);
    return new String(buf);
  }

  public static String toString(final byte ... buf) {
    final StringBuilder builder = new StringBuilder(buf.length * Byte.SIZE);
    for (int i = 0; i < buf.length; ++i)
      builder.append(toUnsignedString(buf[i], Byte.SIZE));

    return builder.toString();
  }

  public static String toString(final short ... buf) {
    final StringBuilder builder = new StringBuilder(buf.length * Short.SIZE);
    for (int i = 0; i < buf.length; ++i)
      builder.append(toUnsignedString(buf[i], Short.SIZE));

    return builder.toString();
  }

  public static String toString(final int ... buf) {
    final StringBuilder builder = new StringBuilder(buf.length * Integer.SIZE);
    for (int i = 0; i < buf.length; ++i)
      builder.append(toUnsignedString(buf[i], Integer.SIZE));

    return builder.toString();
  }

  public static String toString(final long ... buf) {
    final StringBuilder builder = new StringBuilder(buf.length * Long.SIZE);
    for (int i = 0; i < buf.length; ++i)
      builder.append(toUnsignedString(buf[i], Long.SIZE));

    return builder.toString();
  }

  private Buffers() {
  }
}