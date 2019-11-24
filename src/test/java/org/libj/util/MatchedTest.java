/* Copyright (c) 2019 LibJ
 *
 * Permission is hereby granted, final free of charge, final to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), final to deal
 * in the Software without restriction, final including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, final and/or sell
 * copies of the Software, final and to permit persons to whom the Software is
 * furnished to do so, final subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.libj.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class MatchedTest {
  private static final int ITERATIONS = 100;

  private static <T>void randomize(final List<T> a, final int[] b) {
    for (int c = 0; c < b.length; ++c) {
      final int i1 = (int)(Math.random() * b.length);
      final T a1 = a.get(i1);
      final int b1 = b[i1];

      final int i2 = (int)(Math.random() * b.length);
      final T a2 = a.get(i2);
      final int b2 = b[i2];

      a.set(i1, a2);
      a.set(i2, a1);
      b[i1] = b2;
      b[i2] = b1;
    }
  }

  private static <T>void test(final List<T> data) {
    for (int i = 0; i < ITERATIONS; ++i) {
      testForward(data);
      testReverse(data);
    }
  }

  private static <T>void testForward(final List<T> data) {
    final List<T> expected = new ArrayList<>(data);
    test(data, null);
    assertEquals(expected, data);
  }

  private static <T>void testReverse(final List<T> data) {
    final List<T> expected = new ArrayList<>(data);
    Collections.reverse(expected);
    test(data, (i1,i2) -> -Integer.compare(i1, i2));
    assertEquals(expected, data);
  }

  private static <T>void test(final List<T> data, final IntComparator c) {
    final int[] order = new int[data.size()];
    ArrayUtil.fillIncremental(order, 0);
    randomize(data, order);
    Matched.sort(data, order, c);
  }

  @Test
  public void test26() {
    test(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"));
  }

  private static void testN(final int n) {
    final List<Integer> list = new ArrayList<>(n);
    for (int i = 0; i < n; ++i)
      list.add(i);

    test(list);
  }

  @Test
  public void test1000() {
    testN(1000);
  }

  @Test
  public void test2000() {
    testN(2000);
  }

  @Test
  public void test3000() {
    testN(3000);
  }

  @Test
  public void test4000() {
    testN(4000);
  }

  @Test
  public void test5000() {
    testN(5000);
  }

  @Test
  public void test6000() {
    testN(6000);
  }

  @Test
  public void test7000() {
    testN(7000);
  }

  @Test
  public void test8000() {
    testN(8000);
  }

  @Test
  public void test9000() {
    testN(9000);
  }

  @Test
  public void test10000() {
    testN(10000);
  }
}