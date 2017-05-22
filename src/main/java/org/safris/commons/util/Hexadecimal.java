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

package org.safris.commons.util;

public final class Hexadecimal {
  private static char[] hexChar = {
    '0', '1', '2', '3',
    '4', '5', '6', '7',
    '8', '9', 'a', 'b',
    'c', 'd', 'e', 'f'
  };
  private String hexadecimal = null;
  private byte[] bytes = null;

  public Hexadecimal(final String hexadecimal) {
    this.hexadecimal = hexadecimal;
  }

  public Hexadecimal(final byte[] bytes) {
    this.bytes = bytes;
  }

  @Override
  public String toString() {
    if (hexadecimal == null)
      hexadecimal = bytesToHex(bytes);

    return hexadecimal;
  }

  public byte[] getBytes() {
    if (bytes == null)
      bytes = hexToBytes(hexadecimal);

    return bytes;
  }

  private static String bytesToHex(final byte[] bytes) {
    if (bytes == null)
      throw new NullPointerException("bytes == null");

    final StringBuffer stringBuffer = new StringBuffer(bytes.length * 2);
    for (int i = 0; i < bytes.length; i++) {
      // look up high nibble char
      stringBuffer.append(hexChar[(bytes[i] & 0xf0) >>> 4]);

      // look up low nibble char
      stringBuffer.append(hexChar[bytes[i] & 0x0f]);
    }

    return stringBuffer.toString();
  }

  private static byte[] hexToBytes(final String hexadecimal) {
    if (hexadecimal == null)
      throw new NullPointerException("hexadecimal == null");

    final int stringLength = hexadecimal.length();
    if (stringLength == 0)
      throw new IllegalArgumentException("hexadecimal.length() == 0");

    if ((stringLength & 0x1) != 0)
      throw new IllegalArgumentException("fromHexString requires an even number of hex characters");

    final byte[] bytes = new byte[stringLength / 2];
    for (int i = 0, j = 0; i < stringLength; i += 2, j++) {
      final int high = charToNibble(hexadecimal.charAt(i));
      final int low = charToNibble(hexadecimal.charAt(i + 1));
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

    throw new IllegalArgumentException("Invalid hexadecimal character: " + character);
  }
}