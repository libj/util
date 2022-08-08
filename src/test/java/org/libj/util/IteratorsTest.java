/* Copyright (c) 2017 LibJ
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
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class IteratorsTest {
  private static final Integer[] expected = {null, 0, 1, 2, 3, 4, null, 5, 6, null, 7, null, 8, 9, null, 10, 11, 12, null, null};

  @Test
  public void testFlatArray() {
    final Object[] array = {null, new Object[] {}, new Object[] {0, new Object[] {new Object[] {new Object[] {}, new Object[] {}}, new Object[] {1, new Object[] {new Object[] {new Object[] {new Object[] {new Object[] {}, new Object[] {}}}}}, new Object[] {}, 2}, 3}, 4, null, new Object[] {5, 6, null}}, 7, new Object[] {null}, new Object[] {8, new Object[] {9, null}, 10}, 11, 12, new Object[] {null, null}};
    final Iterator<Integer> iterator = new FlatArrayIterator<>(array);
    for (int i = 0; i < expected.length; ++i) { // [A]
      assertTrue(iterator.hasNext());
      assertTrue(iterator.hasNext());
      final Object next = iterator.next();
      assertEquals(expected[i] == null ? null : expected[i], next);
    }
  }

  @Test
  public void testFlatList() {
    final List<?> list = Arrays.asList(null, Collections.EMPTY_LIST, Arrays.asList(0, Arrays.asList(Arrays.asList(Collections.EMPTY_LIST, Collections.EMPTY_LIST), Arrays.asList(1, Arrays.asList(Arrays.asList(Arrays.asList(Collections.EMPTY_LIST, Collections.EMPTY_LIST))), Collections.EMPTY_LIST, 2), 3), 4, null, Arrays.asList(5, 6, null)), 7, Arrays.asList((Integer)null), Arrays.asList(8, Arrays.asList(9, null), 10), 11, 12, Arrays.asList(null, null));
    final Iterator<Integer> iterator = new FlatListIterator<>(list);
    for (int i = 0; i < expected.length; ++i) { // [A]
      assertTrue(iterator.hasNext());
      assertTrue(iterator.hasNext());
      final Object next = iterator.next();
      assertEquals(expected[i] == null ? null : expected[i], next);
    }
  }

  @Test
  public void testIterableList() {
    final Collection<?> list = Arrays.asList(null, Collections.EMPTY_LIST, Arrays.asList(0, Arrays.asList(Arrays.asList(Collections.EMPTY_LIST, Collections.EMPTY_LIST), Arrays.asList(1, Arrays.asList(Arrays.asList(Arrays.asList(Collections.EMPTY_LIST, Collections.EMPTY_LIST))), Collections.EMPTY_LIST, 2), 3), 4, null, Arrays.asList(5, 6, null)), 7, Arrays.asList((Integer)null), Arrays.asList(8, Arrays.asList(9, null), 10), 11, 12, Arrays.asList(null, null));
    final Iterator<Integer> iterator = new FlatCollectionIterator<>(list);
    for (int i = 0; i < expected.length; ++i) { // [A]
      assertTrue(iterator.hasNext());
      assertTrue(iterator.hasNext());
      final Object next = iterator.next();
      assertEquals(expected[i] == null ? null : expected[i], next);
    }
  }

  @Test
  public void testFilter() {
    final List<Number> list = Arrays.asList(0, 0d, 1, 1d, 2, BigInteger.ZERO, 3.4f, BigDecimal.ONE, 3, 2d, BigInteger.ONE, 10L, 4f, (short)8, (byte)62, 4, BigInteger.valueOf(2L));

    final Iterator<? super Integer> integerIterator = Iterators.filter(list.iterator(), m -> m instanceof Integer);
    for (int i = 0; integerIterator.hasNext(); ++i) // [I]
      assertEquals(i, integerIterator.next());

    final Iterator<? super BigInteger> bigIntegerIterator = Iterators.filter(list.iterator(), m -> m instanceof BigInteger);
    for (int i = 0; bigIntegerIterator.hasNext(); ++i) // [I]
      assertEquals(BigInteger.valueOf(i), bigIntegerIterator.next());

    final Iterator<? super Double> doubleIterator = Iterators.filter(list.iterator(), m -> m instanceof Double);
    for (int i = 0; doubleIterator.hasNext(); ++i) // [I]
      assertEquals((double)i, doubleIterator.next());
  }

  @Test
  public void testLastElement() {
    final List<String> list = Arrays.asList("a", "b", "c");
    assertEquals("c", Iterators.lastElement(list.iterator()));
    assertNull(Iterators.lastElement(Arrays.asList().iterator()));
  }

  @Test
  public void testToEnumeration() {
    final List<String> list = Arrays.asList("a", "b", "c");
    final Enumeration<String> enumeration = Iterators.toEnumeration(list.iterator());
    for (int i = 0, len = list.size(); i < len; ++i) { // [L]
      assertTrue(enumeration.hasMoreElements());
      assertEquals(list.get(i), enumeration.nextElement());
    }
  }
}