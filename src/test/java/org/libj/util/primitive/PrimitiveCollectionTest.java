/* Copyright (c) 2019 LibJ
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

package org.libj.util.primitive;

import org.junit.Assert;

abstract class PrimitiveCollectionTest {
  private static final float ef = 0.0000001f;
  private static final double ed = 0.000000000000001d;

  static void assertArrayEquals(final byte[] expected, final byte[] actual) {
    Assert.assertArrayEquals(expected, actual);
  }

  static void assertArrayEquals(final char[] expected, final char[] actual) {
    Assert.assertArrayEquals(expected, actual);
  }

  static void assertArrayEquals(final short[] expected, final short[] actual) {
    Assert.assertArrayEquals(expected, actual);
  }

  static void assertArrayEquals(final int[] expected, final int[] actual) {
    Assert.assertArrayEquals(expected, actual);
  }

  static void assertArrayEquals(final long[] expected, final long[] actual) {
    Assert.assertArrayEquals(expected, actual);
  }

  static void assertArrayEquals(final float[] expected, final float[] actual) {
    Assert.assertArrayEquals(expected, actual, ef);
  }

  static void assertArrayEquals(final double[] expected, final double[] actual) {
    Assert.assertArrayEquals(expected, actual, ed);
  }

  static void assertEquals(final byte expected, final byte actual) {
    Assert.assertEquals(expected, actual);
  }

  static void assertEquals(final short expected, final short actual) {
    Assert.assertEquals(expected, actual);
  }

  static void assertEquals(final int expected, final int actual) {
    Assert.assertEquals(expected, actual);
  }

  static void assertEquals(final float expected, final float actual) {
    Assert.assertEquals(expected, actual, ef);
  }

  static void assertEquals(final double expected, final double actual) {
    Assert.assertEquals(expected, actual, ed);
  }
}