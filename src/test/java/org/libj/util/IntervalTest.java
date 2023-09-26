/* Copyright (c) 2023 LibJ
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

import static org.junit.Assert.*;

import org.junit.Test;

public class IntervalTest {
  private static <T extends Comparable<? super T>> void assertCompareTo(final int expected, final T a, final T b) {
    assertEquals(expected, a.compareTo(b));
    assertEquals(-expected, b.compareTo(a));
  }

  private static <T extends Comparable<? super T>> void assertIntersects(final boolean expected, final Interval<T> a, final Interval<T> b) {
    assertEquals(expected, a.intersects(b));
    assertEquals(expected, b.intersects(a));
    assertEquals(expected, a.intersects(b.getMin(), b.getMax()));
    assertEquals(expected, b.intersects(a.getMin(), a.getMax()));
  }

  private static <T extends Comparable<? super T>> void assertEqual(final Interval<T> a, final Interval<T> b) {
    assertEquals(a, b);
    assertEquals(b, a);
  }

  private static <T extends Comparable<? super T>> void assertNotEqual(final Interval<T> a, final Interval<T> b) {
    assertNotEquals(a, b);
    assertNotEquals(b, a);
  }

  @Test
  public void testUnbounded() {
    final Interval<Integer> a = new Interval<>(null, null);
    final Interval<Integer> b = new Interval<>(null, 0);
    final Interval<Integer> c = new Interval<>(0, null);
    final Interval<Integer> d = new Interval<>(0, 1);
    assertEquals(0, a.compareTo(b));
    assertEquals(-1, a.compareTo(c));
    assertEquals(-1, a.compareTo(d));
  }

  @Test
  @SuppressWarnings("unused")
  public void testInvalid() {
    try {
      new Interval<>(1, 0);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      new Interval<>(0, 0);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }
  }

  @Test
  public void testContainsValueBounded() {
    final Interval<Integer> a = new Interval<>(2, 8);
    for (int i$ = a.getMin(), i = i$ - 10; i < i$; ++i) // [N]
      assertFalse(a.contains(i));

    for (int i = a.getMin(), i$ = a.getMax(); i < i$; ++i) // [N]
      assertTrue(a.contains(i));

    for (int i = a.getMax(), i$ = i + 10; i < i$; ++i) // [N]
      assertFalse(a.contains(i));
  }

  @Test
  public void testContainsValueUnbounded() {
    final Interval<Integer> inf = new Interval<>(null, null);
    for (int i = -100; i < 100; ++i) // [N]
      assertTrue(inf.contains(i));

    final Interval<Integer> neg = new Interval<>(null, 0);
    final Interval<Integer> neg1 = new Interval<>(null, 1);

    assertTrue(inf.contains(neg));
    assertTrue(inf.contains(neg1));
    assertFalse(neg.contains(inf));
    assertFalse(neg1.contains(inf));
    assertFalse(neg.contains(neg1));
    assertTrue(neg1.contains(neg));

    for (int i = -100; i < neg.getMax(); ++i) // [N]
      assertTrue(neg.contains(i));

    for (int i = neg.getMax(); i < 100; ++i) // [N]
      assertFalse(neg.contains(i));

    final Interval<Integer> pos = new Interval<>(0, null);
    final Interval<Integer> pos1 = new Interval<>(1, null);

    assertTrue(inf.contains(pos));
    assertTrue(inf.contains(pos1));
    assertFalse(pos.contains(inf));
    assertFalse(pos1.contains(inf));
    assertTrue(pos.contains(pos1));
    assertFalse(pos1.contains(pos));

    for (int i = -100; i < pos.getMin(); ++i) // [N]
      assertFalse(pos.contains(i));

    for (int i = pos.getMin(); i < 100; ++i) // [N]
      assertTrue(pos.contains(i));
  }

  @Test
  public void testContainsIntervalBounded() {
    final int x = 2;
    final Interval<Integer> a = new Interval<>(2, 8);
    for (int i$ = a.getMin(), i = i$ - 10; i < i$; ++i) // [N]
      assertFalse(a.contains(new Interval<>(i - x, i)));

    for (int i = a.getMin(), i$ = a.getMax() - x; i < i$; ++i) // [N]
      assertTrue(a.contains(new Interval<>(i, i + x)));

    for (int i = a.getMax(), i$ = i + 10; i < i$; ++i) // [N]
      assertFalse(a.contains(new Interval<>(i, i + x)));
  }

  @Test
  public void testContainsIntervalUnbounded() {
    final int x = 2;
    final Interval<Integer> inf = new Interval<>(null, null);
    for (int i = -100; i < 100; ++i) // [N]
      assertTrue(inf.contains(new Interval<>(i, i + 2)));

    final Interval<Integer> neg = new Interval<>(null, 0);
    for (int i = -100; i < neg.getMax(); ++i) // [N]
      assertTrue(neg.contains(new Interval<>(i - x, i)));

    for (int i = neg.getMax(); i < 100; ++i) // [N]
      assertFalse(neg.contains(new Interval<>(i, i + x)));

    final Interval<Integer> pos = new Interval<>(0, null);
    for (int i = -100; i < pos.getMin(); ++i) // [N]
      assertFalse(pos.contains(new Interval<>(i - x, i)));

    for (int i = pos.getMin(); i < 100; ++i) // [N]
      assertTrue(pos.contains(new Interval<>(i, i + x)));
  }

  @Test
  public void testIntersectsBounded() {
    final Interval<Integer> a = new Interval<>(0, 2);
    final Interval<Integer> b = new Interval<>(2, 4);
    final Interval<Integer> c = new Interval<>(4, 6);
    final Interval<Integer> d = new Interval<>(1, 2);
    final Interval<Integer> e = new Interval<>(3, 5);

    assertIntersects(false, a, b);
    assertIntersects(false, b, c);
    assertIntersects(false, a, c);

    assertIntersects(true, a, d);
    assertIntersects(false, a, e);
    assertIntersects(false, b, c);

    assertIntersects(true, b, e);
    assertIntersects(true, c, e);
    assertIntersects(false, d, e);
  }

  @Test
  public void testIntersectsUnBounded() {
    final Interval<Integer> inf = new Interval<>(null, null);
    assertIntersects(true, inf, inf);

    final Interval<Integer> neg = new Interval<>(null, 0);
    assertIntersects(true, neg, neg);

    final Interval<Integer> pos = new Interval<>(0, null);
    assertIntersects(true, pos, pos);

    assertIntersects(true, inf, neg);
    assertIntersects(true, inf, pos);
    assertIntersects(false, neg, pos);

    final Interval<Integer> neg1 = new Interval<>(null, -1);

    assertIntersects(true, neg, neg1);
    assertIntersects(false, neg1, pos);

    final Interval<Integer> pos1 = new Interval<>(1, null);

    assertIntersects(true, pos, pos1);
    assertIntersects(false, pos1, neg);

    final Interval<Integer> a = new Interval<>(-10, 0);

    assertIntersects(true, inf, a);
    assertIntersects(true, neg, a);
    assertIntersects(false, pos, a);

    final Interval<Integer> b = new Interval<>(-5, 5);

    assertIntersects(true, inf, b);
    assertIntersects(true, neg, b);
    assertIntersects(true, pos, b);

    final Interval<Integer> c = new Interval<>(0, 10);

    assertIntersects(true, inf, c);
    assertIntersects(false, neg, c);
    assertIntersects(true, pos, c);
  }

  @Test
  public void testCompareToBounded() {
    final Interval<Integer> a = new Interval<>(0, 2);
    final Interval<Integer> b = new Interval<>(2, 4);
    assertCompareTo(-1, a, b);

    final Interval<Integer> c = new Interval<>(4, 6);
    assertCompareTo(-1, b, c);
    assertCompareTo(-2, a, c);

    final Interval<Integer> d = new Interval<>(1, 2);
    assertCompareTo(-1, a, d);

    final Interval<Integer> e = new Interval<>(3, 5);
    assertCompareTo(-1, b, e);
    assertCompareTo(1, c, e);

    assertCompareTo(-2, d, e);

    final Interval<Integer> f = new Interval<>(3, 4);
    assertCompareTo(0, e, f);
  }

  @Test
  public void testCompareToUnbounded() {
    final Interval<Integer> inf = new Interval<>(null, null);
    assertCompareTo(0, inf, inf);

    final Interval<Integer> neg = new Interval<>(null, 0);
    assertCompareTo(0, neg, neg);

    final Interval<Integer> pos = new Interval<>(0, null);
    assertCompareTo(0, pos, pos);

    assertCompareTo(0, inf, neg);
    assertCompareTo(-1, inf, pos);

    assertCompareTo(-2, neg, pos);

    for (int i = -5; i < 5; ++i) { // [N]
      final Interval<Integer> a = new Interval<>(i - 10, i);
      assertCompareTo(-1, inf, a);
      assertCompareTo(-1, neg, a);
      assertCompareTo(i <= 0 ? 2 : 1, pos, a);
    }

    for (int i = 0; i < 10; ++i) { // [N]
      final Interval<Integer> a = new Interval<>(i, i + 10);
      assertCompareTo(-1, inf, a);
      assertCompareTo(-2, neg, a);
      assertCompareTo(i == 0 ? 0 : -1, pos, a);
    }

    for (int i = -10; i < 0; ++i) { // [N]
      final Interval<Integer> a = new Interval<>(i - 10, i);
      assertCompareTo(-1, inf, a);
      assertCompareTo(-1, neg, a);
      assertCompareTo(2, pos, a);
    }

    for (int i = 1; i < 10; ++i) { // [N]
      final Interval<Integer> a = new Interval<>(i, i + 10);
      assertCompareTo(-1, inf, a);
      assertCompareTo(-2, neg, a);
      assertCompareTo(-1, pos, a);

      final Interval<Integer> b = new Interval<>(0, i);
      assertCompareTo(-1, inf, b);
      assertCompareTo(-2, neg, b);
      assertCompareTo(0, pos, b);
    }
  }

  @Test
  public void testEquals() {
    final Interval<Integer> inf = new Interval<>(null, null);
    assertNotEqual(inf, null);

    assertEqual(inf, inf);
    assertEqual(inf, new Interval<>(null, null));

    final Interval<Integer> neg = new Interval<>(null, 0);
    assertEqual(neg, neg);
    assertEqual(neg, new Interval<>(null, 0));

    final Interval<Integer> pos = new Interval<>(0, null);
    assertEqual(pos, pos);
    assertEqual(pos, new Interval<>(0, null));

    assertNotEqual(inf, neg);
    assertNotEqual(inf, pos);
    assertNotEqual(neg, pos);

    final Interval<Integer> a = new Interval<>(0, 2);
    assertEqual(a, a);
    assertEqual(a, new Interval<>(0, 2));

    final Interval<Integer> b = new Interval<>(2, 4);
    assertEqual(b, b);
    assertEqual(b, new Interval<>(2, 4));

    final Interval<Integer> c = new Interval<>(4, 6);
    assertEqual(c, c);
    assertEqual(c, new Interval<>(4, 6));

    final Interval<Integer> d = new Interval<>(1, 2);
    assertEqual(d, d);
    assertEqual(d, new Interval<>(1, 2));

    final Interval<Integer> e = new Interval<>(3, 5);
    assertEqual(e, e);
    assertEqual(e, new Interval<>(3, 5));

    assertNotEqual(a, b);
    assertNotEqual(a, c);
    assertNotEqual(a, d);
    assertNotEqual(a, e);
    assertNotEqual(b, c);
    assertNotEqual(b, d);
    assertNotEqual(b, e);
    assertNotEqual(c, d);
    assertNotEqual(c, e);
    assertNotEqual(d, e);
  }
}