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
  private static String bytesToHex(final byte[] bytes) {
    if (bytes == null)
      throw new NullPointerException("bytes == null");

    final StringBuilder builder = new StringBuilder(bytes.length * 2);
    for (int i = 0; i < bytes.length; i++) {
      // look up high nibble char
      builder.append(hexChar[(bytes[i] & 0xf0) >>> 4]);

      // look up low nibble char
      builder.append(hexChar[bytes[i] & 0x0f]);
    }

    return builder.toString();
  }

  private static byte[] hexToBytes(final String hex) {
    if (hex == null)
      return null;

    final int stringLength = hex.length();
    if (stringLength == 0)
      return new byte[0];

    if ((stringLength & 0x1) != 0)
      throw new IllegalArgumentException("fromHexString requires an even number of hex characters");

    final byte[] bytes = new byte[stringLength / 2];
    for (int i = 0, j = 0; i < stringLength; i += 2, j++) {
      final int high = charToNibble(hex.charAt(i));
      final int low = charToNibble(hex.charAt(i + 1));
      bytes[j] = (byte)((high << 4) | low);
    }

    return bytes;
  }

  private static int charToNibble(final char character) {
    if ('0' <= character && character <= '9')
      return character - '0';

    if ('a' <= character && character <= 'f')
      return character - 'a' + 0xa;

    if ('A' <= character && character <= 'F')
      return character - 'A' + 0xa;

    throw new IllegalArgumentException("Illegal hexadecimal character: " + character);
  }

  private static char[] hexChar = {
    '0', '1', '2', '3',
    '4', '5', '6', '7',
    '8', '9', 'a', 'b',
    'c', 'd', 'e', 'f'
  };
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