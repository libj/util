/* Copyright (c) 2009 lib4j
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

import java.util.Arrays;

public class Hexadecimal {
  private static char[] hexChar = {
    '0', '1', '2', '3',
    '4', '5', '6', '7',
    '8', '9', 'a', 'b',
    'c', 'd', 'e', 'f'
  };

  private static int charToNibble(final char ch) {
    if ('0' <= ch && ch <= '9')
      return ch - '0';

    if ('a' <= ch && ch <= 'f')
      return ch - 'a' + 0xa;

    if ('A' <= ch && ch <= 'F')
      return ch - 'A' + 0xa;

    throw new IllegalArgumentException("Illegal hexadecimal character: " + ch);
  }

  private static void hexToBytes0(final String hex, byte[] bytes, final int offset) {
    for (int i = 0, j = offset; i < hex.length(); i += 2, j++) {
      final int high = charToNibble(hex.charAt(i));
      final int low = charToNibble(hex.charAt(i + 1));
      bytes[j] = (byte)((high << 4) | low);
    }
  }

  public static void hexToBytes(final String hex, byte[] bytes, final int offset) {
    final int length = hex.length();
    if (length == 0)
      return;

    if (hex.length() % 2 != 0)
      throw new IllegalArgumentException("Odd hex length: " + hex.length());

    hexToBytes0(hex, bytes, offset);
  }

  public static byte[] hexToBytes(final String hex) {
    final int length = hex.length();
    if (length == 0)
      return new byte[0];

    final byte[] bytes = new byte[length / 2];
    hexToBytes(hex, bytes, 0);
    return bytes;
  }

  public static String bytesToHex(final byte[] bytes) {
    return bytesToHex(bytes, 0, bytes.length);
  }

  public static String bytesToHex(final byte[] bytes, final int offset, final int len) {
    final StringBuilder builder = new StringBuilder(len * 2);
    for (int i = offset; i < len + offset; ++i) {
      builder.append(hexChar[(bytes[i] & 0xf0) >>> 4]);
      builder.append(hexChar[bytes[i] & 0x0f]);
    }

    return builder.toString();
  }

  private String hex = null;
  private byte[] bytes = null;

  public Hexadecimal(final String hex) {
    this.hex = hex;
  }

  public Hexadecimal(final byte[] bytes) {
    this.bytes = bytes;
  }

  public byte[] getBytes() {
    return bytes == null ? bytes = hexToBytes(hex) : bytes;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Hexadecimal))
      return false;

    final Hexadecimal that = (Hexadecimal)obj;
    return hex != null && that.hex != null ? hex.equalsIgnoreCase(that.hex) : Arrays.equals(getBytes(), that.getBytes());
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(getBytes());
  }

  @Override
  public String toString() {
    return hex == null ? hex = bytesToHex(bytes) : hex;
  }
}