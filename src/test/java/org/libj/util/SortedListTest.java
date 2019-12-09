/* Copyright (c) 2012 LibJ
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;

public class SortedListTest {
  @SafeVarargs
  private static <T>void assertListEquals(final List<T> actual, final T ... expected) {
    assertArrayEquals(expected, actual.toArray());
  }

  @Test
  public void testConstructorSignature() {
    // final SortedList<Object> bad = new SortedList<Object>(new ArrayList<Object>()); // Should not be allowed cause Object is not Comparable
    final SortedList<Object> good = new SortedList<>(new ArrayList<>(), new Comparator<Object>() {
      @Override
      public int compare(final Object o1, final Object o2) {
        return 0;
      }
    });
    good.add(new Object());
    good.add(new Object());
    good.add(new Object());
  }

  @Test
  public void testStory() {
    final SortedList<String> list = new SortedList<>(new ArrayList<String>());
    list.add(0, "f");
    assertListEquals(list, "f");
    list.add(1, "b");
    assertListEquals(list, "b", "f");
    list.add(0, "g");
    assertListEquals(list, "b", "f", "g");
    list.set(2, "a");
    assertListEquals(list, "a", "b", "f");
    list.set(0, "g");
    assertListEquals(list, "b", "f", "g");
    list.add("c");
    assertListEquals(list, "b", "c", "f", "g");
    list.add("a");
    assertListEquals(list, "a", "b", "c", "f", "g");
    assertEquals(1, list.indexOf("b"));
    list.add("d");
    assertListEquals(list, "a", "b", "c", "d", "f", "g");
    list.add("e");
    assertListEquals(list, "a", "b", "c", "d", "e", "f", "g");
    list.remove("c");
    assertListEquals(list, "a", "b", "d", "e", "f", "g");
    list.remove(0);
    list.add("d");
    assertListEquals(list, "b", "d", "d", "e", "f", "g");
    list.remove(4);
    list.add("h");
    assertListEquals(list, "b", "d", "d", "e", "g", "h");
    assertEquals(1, list.indexOf("d"));
    assertEquals(2, list.lastIndexOf("d"));
    list.retainAll(Arrays.asList("a", "d", "f", "g", "h"));
    assertListEquals(list, "d", "d", "g", "h");
    list.retainAll(Arrays.asList("a", "d", "d", "d", "h"));
    assertListEquals(list, "d", "d", "h");
    assertEquals(-1, list.indexOf("a"));
    assertEquals(2, list.lastIndexOf("h"));

    final ListIterator<String> iterator = list.listIterator();
    assertEquals("d", iterator.next());
    try {
      iterator.add("x");
      fail("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e) {
    }

    try {
      iterator.set("x");
      fail("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e) {
    }

    iterator.remove();
    assertListEquals(list, "d", "h");
  }
}