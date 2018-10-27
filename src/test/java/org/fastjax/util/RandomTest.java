/* Copyright (c) 2009 FastJAX
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

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomTest {
  private static final Logger logger = LoggerFactory.getLogger(RandomTest.class);

  private static final char[] ALPHA = "abcdefghijklmnopqrstuvwxyz".toCharArray();
  private static final char[] NUMERIC = "0123456789".toCharArray();
  private static final char[] ALPHA_NUMERIC = (new String(NUMERIC) + new String(ALPHA)).toCharArray();

  @Test
  public void testRandom() {
    try {
      Random.alpha(-1);
      fail("Expected IllegalArgumentException!");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Random.alphaNumeric(-1);
      fail("Expected IllegalArgumentException!");
    }
    catch (final IllegalArgumentException e) {
    }

    assertEquals("", Random.alphaNumeric(0));

    final String alpha = Random.alpha(16);
    logger.info(alpha);
    assertEquals(16, alpha.length());
    assertInSpace(alpha.toCharArray(), ALPHA);

    final String numeric = Random.numeric(16);
    logger.info(numeric);
    assertEquals(16, numeric.length());
    assertInSpace(numeric.toCharArray(), NUMERIC);

    final String alphaNumeric = Random.alphaNumeric(16);
    logger.info(alphaNumeric);
    assertEquals(16, alphaNumeric.length());
    assertInSpace(alphaNumeric.toCharArray(), ALPHA_NUMERIC);
  }

  private static void assertInSpace(final char[] chars, final char[] space) {
    for (final char ch : chars)
      assertFalse(Arrays.binarySearch(space, ch) == -1);
  }
}