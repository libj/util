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

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

import org.junit.Test;

@SuppressWarnings("rawtypes")
public class TransListTest {
  protected static void test(final TransList<Integer,?,String,?> trans) {
    assertTrue(trans.contains("3"));
    assertTrue(trans.target.contains(3));
    assertFalse(trans.contains("11"));
    assertFalse(trans.target.contains(11));

    trans.addAll(Arrays.asList("11", "12", "13"));
    assertTrue(trans.contains("11"));
    assertTrue(trans.target.contains(11));

    trans.remove("11");
    assertFalse(trans.contains("11"));
    assertFalse(trans.target.contains(11));

    trans.toArray(new String[trans.size()]);

    trans.remove("5");
    assertFalse(trans.contains("5"));
    assertFalse(trans.target.contains(5));

    final Iterator<String> iterator = trans.iterator();
    while (iterator.hasNext()) {
      iterator.next();
      iterator.remove();
    }

    assertEquals(0, trans.size());
    assertEquals(0, trans.target.size());
  }

  @Test
  public void test() {
    for (final Function<List,List> factory : SortedListTest.factories) { // [L]
      final List<Integer> source = factory.apply(Collections.EMPTY_LIST);
      for (int i = 0; i < 10; ++i) // [N]
        source.add(i);

      test(new TransList<>(source, (i, integer) -> String.valueOf(integer), (i, string) -> Integer.valueOf(string)));
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testList() {
    for (final Function<List,List> factory : SortedListTest.factories) { // [L]
      final List<Integer> source = factory.apply(Collections.EMPTY_LIST);
      for (int i = 0; i < 10; ++i) // [N]
        source.add(i);

      final TransList<Integer,?,String,?> trans = new TransList<>(source, (i, integer) -> String.valueOf(integer), (i, string) -> Integer.valueOf(string));
      trans.add(3, "555");
      assertEquals("555", trans.get(3));
      assertEquals(Integer.valueOf(555), ((List<Integer>)trans.target).get(3));

      trans.addAll(9, Arrays.asList("666", "777", "888"));
      assertEquals("666", trans.get(9));
      assertEquals("777", trans.get(10));
      assertEquals("888", trans.get(11));
      assertEquals(Integer.valueOf(666), ((List<Integer>)trans.target).get(9));
      assertEquals(Integer.valueOf(777), ((List<Integer>)trans.target).get(10));
      assertEquals(Integer.valueOf(888), ((List<Integer>)trans.target).get(11));

      trans.set(7, "0");
      assertEquals("0", trans.get(7));
      assertEquals(Integer.valueOf(0), ((List<Integer>)trans.target).get(7));

      trans.remove(7);
      assertNotEquals("0", trans.get(7));
      assertNotEquals(Integer.valueOf(0), ((List<Integer>)trans.target).get(7));

      final TransList<Integer,?,String,?> subList = trans.subList(4, 7);
      for (int i = 3; i < 6; ++i) // [N]
        assertEquals(String.valueOf(i), subList.get(i - 3));

      final ListIterator<String> iterator = trans.listIterator();
      while (iterator.hasNext()) {
        final String element = iterator.next();
        if ("666".equals(element)) {
          iterator.remove();
          assertFalse(trans.contains("666"));
          assertFalse(((List<Integer>)trans.target).contains(666));
        }
        else if ("777".equals(element)) {
          iterator.add("876");
          assertTrue(trans.contains("876"));
          assertTrue(((List<Integer>)trans.target).contains(876));
        }
        else if ("888".equals(element)) {
          iterator.set("123");
          assertFalse(trans.contains("888"));
          assertFalse(((List<Integer>)trans.target).contains(888));
          assertTrue(trans.contains("123"));
          assertTrue(((List<Integer>)trans.target).contains(123));
        }
      }
    }
  }
}