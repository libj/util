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

public final class Random {
  private static final char[] ALPHA = "abcdefghijklmnopqrstuvwxyz".toCharArray();
  private static final char[] NUMERIC = "0123456789".toCharArray();
  private static final char[] ALPHA_NUMERIC = (new String(NUMERIC) + new String(ALPHA)).toCharArray();

  private static String random(final int length, final char[] chars) {
    if (length <= 0)
      throw new IllegalArgumentException("length <= 0");

    final char[] random = new char[length];
    for (int i = 0; i < length; i++)
      random[i] = chars[(int)(Math.random() * chars.length)];

    return new String(random);
  }

  public static String alpha(final int length) {
    return random(length, ALPHA);
  }

  public static String numeric(final int length) {
    return random(length, NUMERIC);
  }

  public static String alphaNumeric(final int length) {
    return random(length, ALPHA_NUMERIC);
  }

  private Random() {
  }
}