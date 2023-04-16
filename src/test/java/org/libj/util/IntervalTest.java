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
  @Test
  @SuppressWarnings("unused")
  public void testOverlapsWith() {
    try {
      new Interval<Integer>(null, 0);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      new Interval<Integer>(0, null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    final Interval<Integer> a = new Interval<Integer>(0, 2);
    final Interval<Integer> b = new Interval<Integer>(2, 4);
    assertTrue(a.intersects(b));
    assertTrue(b.intersects(a));
    final Interval<Integer> c = new Interval<Integer>(4, 6);
    assertTrue(b.intersects(c));
    assertTrue(c.intersects(b));
    assertFalse(a.intersects(c));
    assertFalse(c.intersects(a));

    final Interval<Integer> d = new Interval<Integer>(1, 2);
    assertTrue(a.intersects(d));
    assertTrue(d.intersects(a));

    final Interval<Integer> e = new Interval<Integer>(3, 5);
    assertTrue(b.intersects(e));
    assertTrue(e.intersects(b));
    assertTrue(c.intersects(e));
    assertTrue(e.intersects(c));

    assertFalse(d.intersects(e));
    assertFalse(e.intersects(d));
  }

  @Test
  @SuppressWarnings("unused")
  public void testTouches() {
    try {
      new Interval<Integer>(null, 0);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      new Interval<Integer>(0, null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    final Interval<Integer> a = new Interval<Integer>(0, 2);
    final Interval<Integer> b = new Interval<Integer>(2, 4);
    assertEquals(-1, a.compareTo(b));
    assertEquals(1, b.compareTo(a));

    final Interval<Integer> c = new Interval<Integer>(4, 6);
    assertEquals(-1, b.compareTo(c));
    assertEquals(1, c.compareTo(b));
    assertEquals(-2, a.compareTo(c));
    assertEquals(2, c.compareTo(a));

    final Interval<Integer> d = new Interval<Integer>(1, 2);
    assertEquals(-1, a.compareTo(d));
    assertEquals(1, d.compareTo(a));

    final Interval<Integer> e = new Interval<Integer>(3, 5);
    assertEquals(-1, b.compareTo(e));
    assertEquals(1, e.compareTo(b));
    assertEquals(1, c.compareTo(e));
    assertEquals(-1, e.compareTo(c));

    assertEquals(-2, d.compareTo(e));
    assertEquals(2, e.compareTo(d));

    final Interval<Integer> f = new Interval<Integer>(3, 4);
    assertEquals(0, e.compareTo(f));
    assertEquals(0, f.compareTo(e));
  }
}