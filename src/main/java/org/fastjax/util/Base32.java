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
 * Encodes and decodes Base32.
 *
 * @see <a href="http://www.faqs.org/rfcs/rfc3548.html">RFC3548</a>
 */
public class Base32 {
  private static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
  private static final int[] lookup = {
    0xFF, 0xFF, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F,
    0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF,
    0xFF, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
    0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E,
    0x0F, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16,
    0x17, 0x18, 0x19, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF,
    0xFF, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
    0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E,
    0x0F, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16,
    0x17, 0x18, 0x19, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF
  };

  /**
   * Returns the string of the encoded {@code bytes} array.
   *
   * @param bytes The bytes to encode.
   * @return The string of the encoded {@code bytes} array.
   */
  public static String encode(final byte[] bytes) {
    final StringBuilder base32 = new StringBuilder((bytes.length + 7) * 8 / 5);
    for (int i = 0, index = 0, digit = 0, by0, by1; i < bytes.length;) {
      by0 = (bytes[i] >= 0) ? bytes[i] : (bytes[i] + 256);
      if (index > 3) {
        by1 = i + 1 < bytes.length ? bytes[i + 1] < 0 ? bytes[i + 1] + 256 : bytes[i + 1] : 0;
        digit = by0 & (0xFF >> index);
        index = (index + 5) % 8;
        digit <<= index;
        digit |= by1 >> (8 - index);
        ++i;
      }
      else {
        digit = (by0 >> (8 - (index + 5))) & 0x1F;
        index = (index + 5) % 8;
        if (index == 0)
          ++i;
      }

      base32.append(alphabet.charAt(digit));
    }

    return base32.toString();
  }

  /**
   * Returns the bytes of the decoded {@code base32} string.
   *
   * @param base32 The Base32 string.
   * @return The bytes of the decoded {@code base32} string.
   */
  static public byte[] decode(final String base32) {
    final byte[] bytes = new byte[base32.length() * 5 / 8];
    for (int i = 0, index = 0, offset = 0, ch, digit; i < base32.length(); ++i) {
      ch = base32.charAt(i) - '0';
      if (ch < 0 || lookup.length < ch)
        continue;

      digit = lookup[ch];
      if (digit == 0xFF)
        continue;

      if (index <= 3) {
        index = (index + 5) % 8;
        if (index == 0) {
          bytes[offset] |= digit;
          offset++;
          if (offset >= bytes.length)
            break;
        }
        else {
          bytes[offset] |= digit << (8 - index);
        }
      }
      else {
        index = (index + 5) % 8;
        bytes[offset] |= (digit >>> index);
        offset++;

        if (offset >= bytes.length)
          break;

        bytes[offset] |= digit << (8 - index);
      }
    }

    return bytes;
  }
}