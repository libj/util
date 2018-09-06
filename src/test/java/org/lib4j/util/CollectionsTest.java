/* Copyright (c) 2014 lib4j
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

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class CollectionsTest {
  private static List<Object> createRandomNestedList() {
    final List<Object> list = new ArrayList<>(4);
    for (int i = 0; i < 4; i++)
      list.add(Math.random() < 0.2 ? createRandomNestedList() : Strings.getRandomAlphaString(4));

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
    Collections.flatten(list);
    assertEquals(expected, list.toString());
  }

  @Test
  public void testFlattenListN() {
    for (int i = 0; i < 100; i++) {
      final List<Object> list = createRandomNestedList();
      final String expected = "[" + list.toString().replace("[", "").replace("]", "") + "]";
      Collections.flatten(list);
      assertEquals(expected, list.toString());
    }
  }

  @Test
  public void testFlattenListRetainingReferences() {
    final List<Object> list = createTestList();
    final List<Object> expected = createExpectedList(list);
    Collections.flatten(list, true);
    assertEquals(expected, list);
  }

  @Test
  public void testFlattenCollection0() {
    final List<Object> list = createTestList();
    final String expected = "[" + list.toString().replace("[", "").replace("]", "") + "]";
    final List<Object> result = Collections.flatten(list, new ArrayList<>());
    assertEquals(expected, result.toString());
  }

  @Test
  public void testFlattenCollectionN() {
    for (int i = 0; i < 100; i++) {
      final List<Object> list = createRandomNestedList();
      final List<Object> result = Collections.flatten(list, new ArrayList<>());
      assertEquals("[" + list.toString().replace("[", "").replace("]", "") + "]", result.toString());
    }
  }

  @Test
  public void testFlattenCollectionRetainingReferences() {
    final List<Object> list = createTestList();
    final List<Object> expected = createExpectedList(list);
    final List<Object> result = Collections.flatten(list, new ArrayList<>(), true);
    assertEquals(expected, result);
  }

  @Test
  public void testGetComponentType() {
    Assert.assertEquals(null, Collections.getComponentType(Arrays.asList(null, null, null)));
    Assert.assertEquals(Number.class, Collections.getComponentType(Arrays.asList(Integer.valueOf(1), null, BigInteger.ONE)));
    Assert.assertEquals(Number.class, Collections.getComponentType(Arrays.asList(Integer.valueOf(1), Long.valueOf(1), BigInteger.ONE)));
  }

  @Test
  public void testPartitions() {
    final List<String> list = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k");
    for (int p = 1; p < list.size(); p++) {
      final List<String>[] partitions = Collections.partition(list, p);
      final int parts = list.size() / p;
      final int remainder = list.size() % p;
      Assert.assertEquals(parts + (remainder != 0 ? 1 : 0), partitions.length);
      for (int i = 0; i < parts; i++)
        for (int j = 0; j < p; j++)
          Assert.assertEquals(list.get(i * p + j), partitions[i].get(j));

      if (remainder != 0)
        for (int j = 0; j < list.size() % p; j++)
          Assert.assertEquals(list.get(p * parts + j), partitions[parts].get(j));
    }
  }

}