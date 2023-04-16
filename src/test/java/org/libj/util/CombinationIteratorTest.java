/* Copyright (c) 2021 LibJ
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CombinationIteratorTest {
  @Test
  @SuppressWarnings({"unchecked", "unused"})
  public void testIllegalNullIterables() {
    try {
      new CombinationIterator<>((Iterable[])null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }
  }

  @Test
  @SuppressWarnings("unused")
  public void testIllegalNullIterable() {
    try {
      new CombinationIterator<>((Iterable<?>)null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }
  }

  @Test
  public void test0() {
    final ArrayList<Integer[]> expecteds = new ArrayList<>(0);

    int count = 0;
    for (final CombinationIterator<Integer> i = new CombinationIterator<>(); i.hasNext() && i.hasNext() && i.hasNext(); ++count) { // [I]
      final Integer[] expected = expecteds.get(count);
      final Object[] actual = i.next();
      assertArrayEquals(expected, actual);
    }

    assertEquals(expecteds.size(), count);
  }

  @Test
  public void test() {
    final List<Integer> as = Arrays.asList(0);
    final ArrayList<Integer[]> expecteds = new ArrayList<>(as.size());
    for (int i = 0, i$ = as.size(); i < i$; ++i) // [RA]
      expecteds.add(new Integer[] {as.get(i)});

    int count = 0;
    for (final CombinationIterator<Integer> i = new CombinationIterator<>(as); i.hasNext() && i.hasNext() && i.hasNext(); ++count) { // [I]
      final Integer[] expected = expecteds.get(count);
      final Object[] actual = i.next();
      assertArrayEquals(expected, actual);
    }

    assertEquals(expecteds.size(), count);
  }

  @Test
  public void test1() {
    final List<Integer> as = Arrays.asList(0, 1, 2);
    final ArrayList<Integer[]> expecteds = new ArrayList<>(as.size());
    for (int i = 0, i$ = as.size(); i < i$; ++i) // [RA]
      expecteds.add(new Integer[] {as.get(i)});

    int count = 0;
    for (final CombinationIterator<Integer> i = new CombinationIterator<>(as); i.hasNext() && i.hasNext() && i.hasNext(); ++count) { // [I]
      final Integer[] expected = expecteds.get(count);
      final Object[] actual = i.next();
      assertArrayEquals(expected, actual);
    }

    assertEquals(expecteds.size(), count);
  }

  @Test
  public void test2() {
    final List<Integer> as = Arrays.asList(0, 1, 2);
    final List<Integer> bs = Arrays.asList(0, 1, 2);
    final ArrayList<Integer[]> expecteds = new ArrayList<>(as.size() * bs.size());
    for (int i = 0, lenI = as.size(); i < lenI; ++i) // [RA]
      for (int j = 0, lenJ = bs.size(); j < lenJ; ++j) // [RA]
        expecteds.add(new Integer[] {as.get(i), bs.get(j)});

    int count = 0;
    for (final CombinationIterator<Integer> i = new CombinationIterator<>(as, bs); i.hasNext() && i.hasNext() && i.hasNext(); ++count) { // [I]
      final Integer[] expected = expecteds.get(count);
      final Object[] actual = i.next();
      assertArrayEquals(expected, actual);
    }

    assertEquals(expecteds.size(), count);
  }

  @Test
  public void test2_0() {
    final List<Integer> as = Arrays.asList(0, 1, 2);
    final List<Integer> bs = Arrays.asList();
    final ArrayList<Integer[]> expecteds = new ArrayList<>(as.size() * bs.size());
    for (int i = 0, lenI = as.size(); i < lenI; ++i) // [RA]
      for (int j = 0, lenJ = bs.size(); j < lenJ; ++j) // [RA]
        expecteds.add(new Integer[] {as.get(i), bs.get(j)});

    int count = 0;
    for (final CombinationIterator<Integer> i = new CombinationIterator<>(as, bs); i.hasNext() && i.hasNext() && i.hasNext(); ++count) { // [I]
      final Integer[] expected = expecteds.get(count);
      final Object[] actual = i.next();
      assertArrayEquals(expected, actual);
    }

    assertEquals(expecteds.size(), count);
  }

  @Test
  public void test0_2() {
    final List<Integer> as = Arrays.asList();
    final List<Integer> bs = Arrays.asList(0, 1, 2);
    final ArrayList<Integer[]> expecteds = new ArrayList<>(as.size() * bs.size());
    for (int i = 0, lenI = as.size(); i < lenI; ++i) // [RA]
      for (int j = 0, lenJ = bs.size(); j < lenJ; ++j) // [RA]
        expecteds.add(new Integer[] {as.get(i), bs.get(j)});

    int count = 0;
    for (final CombinationIterator<Integer> i = new CombinationIterator<>(as, bs); i.hasNext() && i.hasNext() && i.hasNext(); ++count) { // [I]
      final Integer[] expected = expecteds.get(count);
      final Object[] actual = i.next();
      assertArrayEquals(expected, actual);
    }

    assertEquals(expecteds.size(), count);
  }

  @Test
  public void test3() {
    final List<Integer> as = Arrays.asList(0, 1, 2);
    final List<Integer> bs = Arrays.asList(0, 1, 2);
    final List<Integer> cs = Arrays.asList(0, 1, 2);
    final ArrayList<Integer[]> expecteds = new ArrayList<>(as.size() * bs.size() * cs.size());
    for (int i = 0, lenI = as.size(); i < lenI; ++i) // [RA]
      for (int j = 0, lenJ = bs.size(); j < lenJ; ++j) // [RA]
        for (int k = 0, lenK = cs.size(); k < lenK; ++k) // [RA]
          expecteds.add(new Integer[] {as.get(i), bs.get(j), cs.get(k)});

    int count = 0;
    for (final CombinationIterator<Integer> i = new CombinationIterator<>(as, bs, cs); i.hasNext() && i.hasNext() && i.hasNext(); ++count) { // [I]
      final Integer[] expected = expecteds.get(count);
      final Object[] actual = i.next();
      assertArrayEquals(expected, actual);
    }

    assertEquals(expecteds.size(), count);
  }

  @Test
  public void test4() {
    final List<Integer> as = Arrays.asList(0, 1, 2);
    final List<Integer> bs = Arrays.asList(0, 1, 2);
    final List<Integer> cs = Arrays.asList(0, 1, 2);
    final List<Integer> ds = Arrays.asList(0, 1, 2);
    final ArrayList<Integer[]> expecteds = new ArrayList<>(as.size() * bs.size() * cs.size() * ds.size());
    for (int i = 0, lenI = as.size(); i < lenI; ++i) // [RA]
      for (int j = 0, lenJ = bs.size(); j < lenJ; ++j) // [RA]
        for (int k = 0, lenK = cs.size(); k < lenK; ++k) // [RA]
          for (int l = 0, lenL = ds.size(); l < lenL; ++l) // [RA]
            expecteds.add(new Integer[] {as.get(i), bs.get(j), cs.get(k), ds.get(l)});

    int count = 0;
    for (final CombinationIterator<Integer> i = new CombinationIterator<>(as, bs, cs, ds); i.hasNext() && i.hasNext() && i.hasNext(); ++count) { // [I]
      final Integer[] expected = expecteds.get(count);
      final Object[] actual = i.next();
      assertArrayEquals(expected, actual);
    }

    assertEquals(expecteds.size(), count);
  }

  @Test
  public void test2S() {
    final SequenceIterator as = new SequenceIterator(BigDecimal.valueOf(0), BigDecimal.valueOf(2), BigDecimal.valueOf(1));
    final SequenceIterator bs = new SequenceIterator(BigDecimal.valueOf(0), BigDecimal.valueOf(2), BigDecimal.valueOf(1));
    final ArrayList<BigDecimal[]> expecteds = new ArrayList<>(3 * 3);
    for (final BigDecimal a : as) // [I]
      for (final BigDecimal b : bs) // [I]
        expecteds.add(new BigDecimal[] {a, b});

    int count = 0;
    for (final CombinationIterator<BigDecimal> i = new CombinationIterator<>(as, bs); i.hasNext() && i.hasNext() && i.hasNext(); ++count) { // [I]
      final BigDecimal[] expected = expecteds.get(count);
      final Object[] actual = i.next();
      assertArrayEquals(expected, actual);
    }

    assertEquals(expecteds.size(), count);
  }

  @Test
  public void test3S() {
    final SequenceIterator as = new SequenceIterator(BigDecimal.valueOf(0), BigDecimal.valueOf(2), BigDecimal.valueOf(1));
    final SequenceIterator bs = new SequenceIterator(BigDecimal.valueOf(0), BigDecimal.valueOf(2), BigDecimal.valueOf(1));
    final SequenceIterator cs = new SequenceIterator(BigDecimal.valueOf(0), BigDecimal.valueOf(2), BigDecimal.valueOf(1));
    final ArrayList<BigDecimal[]> expecteds = new ArrayList<>(3 * 3 * 3);
    for (final BigDecimal a : as) // [I]
      for (final BigDecimal b : bs) // [I]
        for (final BigDecimal c : cs) // [I]
          expecteds.add(new BigDecimal[] {a, b, c});

    int count = 0;
    for (final CombinationIterator<BigDecimal> i = new CombinationIterator<>(as, bs, cs); i.hasNext() && i.hasNext() && i.hasNext(); ++count) { // [I]
      final BigDecimal[] expected = expecteds.get(count);
      final Object[] actual = i.next();
      assertArrayEquals(expected, actual);
    }

    assertEquals(expecteds.size(), count);
  }
}