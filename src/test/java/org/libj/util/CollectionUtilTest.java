/* Copyright (c) 2014 LibJ
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;
import org.libj.lang.Strings;

public class CollectionUtilTest {
  private static List<Object> createRandomNestedList() {
    final List<Object> list = new ArrayList<>(4);
    for (int i = 0; i < 4; ++i) // [N]
      list.add(Math.random() < 0.2 ? createRandomNestedList() : Strings.getRandomAlpha(4));

    return list;
  }

  private static List<Object> createTestList() {
    return new ArrayList<>(Arrays.asList("a", "b", Arrays.asList("c", "d"), "e", Arrays.asList("f", Arrays.asList("g", "h")), "i"));
  }

  @SuppressWarnings("rawtypes")
  private static List<Object> createExpectedList(final List<Object> list) {
    return new ArrayList<>(Arrays.asList(list.get(0), list.get(1), list.get(2), ((List)list.get(2)).get(0), ((List)list.get(2)).get(1), list.get(3), list.get(4), ((List)list.get(4)).get(0), ((List)list.get(4)).get(1), ((List)((List)list.get(4)).get(1)).get(0), ((List)((List)list.get(4)).get(1)).get(1), list.get(5)));
  }

  @Test
  public void testFlattenList0() {
    final List<Object> list = createTestList();
    final String expected = "[" + list.toString().replace("[", "").replace("]", "") + "]";
    CollectionUtil.flatten(list);
    assertEquals(expected, list.toString());
  }

  @Test
  public void testFlattenListN() {
    for (int i = 0; i < 100; ++i) { // [N]
      final List<Object> list = createRandomNestedList();
      final String expected = "[" + list.toString().replace("[", "").replace("]", "") + "]";
      CollectionUtil.flatten(list);
      assertEquals(expected, list.toString());
    }
  }

  @Test
  public void testFlattenListRetainingReferences() {
    final List<Object> list = createTestList();
    final List<Object> expected = createExpectedList(list);
    CollectionUtil.flatten(list, true);
    assertEquals(expected, list);
  }

  @Test
  public void testFlattenCollection0() {
    final List<Object> list = createTestList();
    final String expected = "[" + list.toString().replace("[", "").replace("]", "") + "]";
    final List<Object> result = CollectionUtil.flatten(list, new ArrayList<>());
    assertEquals(expected, result.toString());
  }

  @Test
  public void testFlattenCollectionN() {
    for (int i = 0; i < 100; ++i) { // [N]
      final List<Object> list = createRandomNestedList();
      final List<Object> result = CollectionUtil.flatten(list, new ArrayList<>());
      assertEquals("[" + list.toString().replace("[", "").replace("]", "") + "]", result.toString());
    }
  }

  @Test
  public void testFlattenCollectionRetainingReferences() {
    final List<Object> list = createTestList();
    final List<Object> expected = createExpectedList(list);
    final List<Object> result = CollectionUtil.flatten(list, new ArrayList<>(), true);
    assertEquals(expected, result);
  }

  @Test
  public void testGetComponentType() {
    assertNull(CollectionUtil.getComponentType(Arrays.asList(null, null, null)));
    assertSame(Number.class, CollectionUtil.getComponentType(Arrays.asList(1, null, BigInteger.ONE)));
    assertSame(Number.class, CollectionUtil.getComponentType(Arrays.asList(1, 1L, BigInteger.ONE)));
  }

  @Test
  public void testPartitions() {
    final List<String> list = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k");
    for (int p = 1; p < list.size(); ++p) { // [N]
      final List<String>[] partitions = CollectionUtil.partition(list, p);
      final int parts = list.size() / p;
      final int remainder = list.size() % p;
      assertEquals(parts + (remainder != 0 ? 1 : 0), partitions.length);
      for (int i = 0; i < parts; ++i) // [L]
        for (int j = 0; j < p; ++j) // [L]
          assertEquals(list.get(i * p + j), partitions[i].get(j));

      if (remainder != 0)
        for (int j = 0, i$ = list.size(); j < i$ % p; ++j) // [L]
          assertEquals(list.get(p * parts + j), partitions[parts].get(j));
    }
  }

  private static void assertDedupe(List<Integer> list, final int len, final Comparator<? super Integer> c) {
    if (list == null) {
      try {
        CollectionUtil.dedupe(list, c);
        fail("Expected IllegalArgumentException");
      }
      catch (final IllegalArgumentException e) {
      }
    }
    else {
      assertEquals(len, CollectionUtil.dedupe(list, c));
      final List<Integer> expected = new ArrayList<>(len);
      for (int i = 0; i < len;) // [L]
        expected.add(++i);

      if (list.size() > len)
        list = list.subList(0, len);

      assertEquals(expected, list);
    }
  }

  @Test
  public void testDedupeObject() {
    final Comparator<Integer> c = (o1, o2) -> Integer.compare(o1, o2);
    assertDedupe(null, 0, c);
    assertDedupe(Arrays.asList(), 0, c);
    assertDedupe(Arrays.asList(1), 1, c);
    assertDedupe(Arrays.asList(1, 2), 2, c);
    assertDedupe(Arrays.asList(1, 2, 2, 3, 4, 5, 5, 6, 7, 7, 8, 8, 9), 9, c);
    assertDedupe(Arrays.asList(1, 1, 1, 2, 2, 3, 4, 5, 5, 6, 7, 7, 8, 8, 9, 9, 9, 9), 9, c);
  }
}