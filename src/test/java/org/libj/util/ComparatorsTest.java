/* Copyright (c) 2018 LibJ
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

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;

import org.junit.Test;

public class ComparatorsTest {
  @Test
  public void testFixedOrderComparatorExceptions() {
    try {
      Comparators.newFixedOrderComparator((String[])null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      Comparators.newFixedOrderComparator((Comparator<Integer>)null, 1, 2, 3);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      Comparators.newFixedOrderComparator(Comparator.comparingInt(i -> i), (Integer[])null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      Comparators.newFixedOrderComparator((Function<Integer,Integer>)null, 1, 2, 3);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      Comparators.newFixedOrderComparator((Function<Integer,Integer>)null, Comparator.comparingInt((Integer i) -> i), 1, 2, 3);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }
  }

  @Test
  public void testFixedOrderComparatorComparable() {
    testFixedOrderComparator(null, null);
  }

  @Test
  public void testFixedOrderComparatorComparator() {
    testFixedOrderComparator((o1, o2) -> o1.compareTo(o2), null);
  }

  @Test
  public void testFixedOrderComparatorComparableToCompare() {
    testFixedOrderComparator(null, s -> s);
  }

  @Test
  public void testFixedOrderComparatorComparatorToCompare() {
    testFixedOrderComparator((o1, o2) -> o1.compareTo(o2), s -> s);
  }

  private static void testFixedOrderComparator(final Comparator<String> c, final Function<String,String> toCompare) {
    try {
      Comparators.newFixedOrderComparator((String[])null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    final String[] order = {"z", "a", "t", "r", "q", "b"};
    final Comparator<String> comparator = c != null ? (toCompare != null ? Comparators.newFixedOrderComparator(toCompare, c, order) : Comparators.newFixedOrderComparator(c, order)) : (toCompare != null ? Comparators.newFixedOrderComparator(toCompare, order) : Comparators.newFixedOrderComparator(order));
    assertEquals(-1, comparator.compare("z", "a"));
    assertEquals(-1, comparator.compare("z", "t"));
    assertEquals(-1, comparator.compare("z", "r"));
    assertEquals(-1, comparator.compare("r", "b"));
    assertEquals(0, comparator.compare("z", "z"));
    assertEquals(0, comparator.compare("a", "a"));
    assertEquals(0, comparator.compare("r", "r"));
    assertEquals(0, comparator.compare("b", "b"));
    assertEquals(1, comparator.compare("b", "z"));
    assertEquals(1, comparator.compare("q", "t"));
    assertEquals(1, comparator.compare("r", "a"));
    assertEquals(1, comparator.compare("t", "z"));

    assertEquals(-1, comparator.compare("1", "2"));
    assertEquals(-1, comparator.compare("z", "1"));
    assertEquals(-1, comparator.compare("z", "Z"));
    assertEquals(1, comparator.compare("Z", "t"));
    assertEquals(1, comparator.compare("0", "b"));
    assertEquals(1, comparator.compare("~", "a"));
  }

  @Test
  public void testHashCodeComparator() {
    final Object[] a1 = {new Object(), new Object(), null, new Object(), null, new Object()};
    final Object[] a2 = a1.clone();
    Arrays.sort(a1, Comparators.HASHCODE_COMPARATOR);
    Arrays.sort(a2, Comparators.IDENTITY_HASHCODE_COMPARATOR);
    assertArrayEquals(a1, a2);
  }
}