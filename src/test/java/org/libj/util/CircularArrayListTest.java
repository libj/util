/* Copyright (c) 2020 LibJ
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

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CircularArrayListTest {
  private static final Logger logger = LoggerFactory.getLogger(CircularArrayListTest.class);

  @Test
  public void test() {
    final List<Integer> c = new CircularArrayList<>(5);
    for (int i = 8; i >= 0; --i) {
      c.add(0, i);
      logger.info(c.toString());
    }
  }

  @Test
  public void testAddAndRemove() {
    final Integer[] array = new Integer[5];
    for (int i = 0; i < array.length; ++i)
      array[i] = Integer.valueOf(i);

    final List<Integer> list = new CircularArrayList<>();

    for (Integer i = 0; i < 5; ++i) {
      list.add(i);
      assertEquals(i, array[i]);
      assertTrue(list.contains(i));
      assertEquals(i, list.get(i));
    }

    assertFalse(list.contains(6));

    assertEquals((Integer)4, list.get(list.size() - 1));
    assertEquals((Integer)0, list.get(0));
    assertEquals(5, list.size());

    final Integer v = list.remove(1);
    assertEquals((Integer)1, v);
    assertEquals(4, list.size());
    assertEquals((Integer)0, list.get(0));
    assertEquals((Integer)4, list.get(list.size() - 1));
    list.add(5);
    assertEquals(5, list.size());
    assertEquals((Integer)0, list.get(0));
    assertEquals((Integer)5, list.get(list.size() - 1));
    assertEquals((Integer)0, list.remove(0));
    assertEquals((Integer)2, list.get(0));
    assertEquals((Integer)5, list.get(list.size() - 1));
    assertEquals((Integer)2, list.remove(0));
    assertEquals((Integer)3, list.get(0));
    assertEquals((Integer)5, list.get(list.size() - 1));
    assertEquals((Integer)3, list.remove(0));
    assertEquals((Integer)4, list.get(0));
    assertEquals((Integer)5, list.get(list.size() - 1));
    assertEquals((Integer)4, list.remove(0));
    assertEquals((Integer)5, list.get(0));
    assertEquals((Integer)5, list.get(list.size() - 1));
    assertEquals((Integer)5, list.remove(0));
    assertEquals(0, list.size());
    assertTrue(list.isEmpty());
  }
}