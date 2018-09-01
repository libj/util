/* Copyright (c) 2012 lib4j
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

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.junit.Test;

public class ArrayIntListTest {
  @Test
  public void test() {
    final ArrayIntList list = new ArrayIntList();
    assertTrue(list.isEmpty());
    list.add(1);
    list.add(2);
    list.add(3);
    list.add(4);
    list.add(5);
    assertEquals(1, list.get(0));
    assertEquals(2, list.get(1));
    assertEquals(3, list.get(2));
    assertEquals(4, list.get(3));
    assertEquals(5, list.get(4));

    try {
      list.get(5);
      fail("Expected IndexOutOfBoundsException");
    }
    catch (final IndexOutOfBoundsException e) {
    }

    list.addAll(new int[] {6, 7, 8, 9, 10});
    assertEquals(6, list.get(5));
    assertEquals(7, list.get(6));
    assertEquals(8, list.get(7));
    assertEquals(9, list.get(8));
    assertEquals(10, list.get(9));

    list.addAll(3, new int[] {11, 12, 13});
    assertEquals(2, list.get(1));
    assertEquals(3, list.get(2));
    assertEquals(11, list.get(3));
    assertEquals(12, list.get(4));
    assertEquals(13, list.get(5));
    assertEquals(4, list.get(6));

    list.set(1, 7);
    assertEquals(7, list.get(1));

    assertEquals(1, list.indexOf(7));
    assertEquals(9, list.lastIndexOf(7));

    list.add(2, 99);
    list.add(7, 99);
    list.add(13, 99);
    assertArrayEquals(new int[] {1, 7, 99, 3, 11, 12, 13, 99, 4, 5, 6, 7, 8, 99, 9, 10}, list.toArray(new int[list.size]));
    assertEquals(list, list.clone());

    list.remove(3);
    assertArrayEquals(new int[] {1, 7, 99, 11, 12, 13, 99, 4, 5, 6, 7, 8, 99, 9, 10}, list.toArray(new int[0]));

    list.removeValue(99);
    assertArrayEquals(new int[] {1, 7, 11, 12, 13, 99, 4, 5, 6, 7, 8, 99, 9, 10}, list.toArray(new int[list.size]));

    assertFalse(list.contains(0));
    list.sort();
    assertArrayEquals(new Integer[] {1, 4, 5, 6, 7, 7, 8, 9, 10, 11, 12, 13, 99, 99}, list.toArray(new Integer[list.size]));

    assertTrue(list.removeAll(new int[] {5, 9, 12}));
    assertArrayEquals(new int[] {1, 4, 6, 7, 7, 8, 10, 11, 13, 99, 99}, list.toArray(new int[list.size]));

    assertTrue(list.removeAll(Arrays.asList(1, 8, 13)));
    assertArrayEquals(new int[] {4, 6, 7, 7, 10, 11, 99, 99}, list.toArray(new int[0]));

    assertTrue(list.containsAll(4, 10, 99));
    assertTrue(list.containsAll(Arrays.asList(4, 10, 99)));
    assertTrue(list.containsAll(new ArrayIntList(4, 10, 99)));
  }

  @Test
  public void testIterator() {
    final int[] values = {7, 3, 5, 4, 6, 9, 1, 8, 0};
    final ArrayIntList list = new ArrayIntList(values);
    IntIterator iterator = list.iterator();
    for (int i = 0; i < values.length; ++i)
      assertEquals(values[i], iterator.next());

    assertFalse(iterator.hasNext());
    try {
      iterator.next();
      fail("Expected NoSuchElementException");
    }
    catch (final NoSuchElementException e) {
    }

    iterator = list.iterator();
    iterator.next();
    iterator.next();
    iterator.remove();
    iterator.next();
    iterator.next();
    iterator.remove();
    iterator.next();
    iterator.next();
    iterator.remove();
    iterator.next();
    iterator.next();
    iterator.remove();
    assertArrayEquals(new int[] {7, 5, 6, 1, 0}, list.toArray(new int[0]));
  }

  @Test
  public void testListIterator() {
    final int[] values = {7, 3, 5, 4, 6, 9, 1, 8, 0};
    final ArrayIntList list = new ArrayIntList(values);
    IntListIterator iterator = list.listIterator();
    for (int i = 0; i < values.length; ++i) {
      assertEquals(i, iterator.nextIndex());
      assertEquals(values[i], iterator.next());
    }

    assertFalse(iterator.hasNext());
    try {
      iterator.next();
      fail("Expected NoSuchElementException");
    }
    catch (final NoSuchElementException e) {
    }

    iterator = list.listIterator(values.length);
    for (int i = values.length - 1; i >= 0; --i) {
      assertEquals(i, iterator.previousIndex());
      assertEquals(values[i], iterator.previous());
    }

    assertFalse(iterator.hasPrevious());
    iterator = list.listIterator(4);
    iterator.next();
    iterator.remove();
    try {
      iterator.remove();
      fail("Expected IllegalStateException");
    }
    catch (final IllegalStateException e) {
    }
    iterator.previous();
    iterator.remove();
    try {
      iterator.remove();
      fail("Expected IllegalStateException");
    }
    catch (final IllegalStateException e) {
    }
    iterator.previous();
    iterator.previous();
    iterator.remove();
    iterator.next();
    iterator.next();
    iterator.next();
    iterator.next();
    iterator.remove();
    assertArrayEquals(new int[] {7, 5, 9, 1, 0}, list.toArray(new int[0]));

    iterator.add(3);
    assertArrayEquals(new int[] {7, 5, 9, 1, 3, 0}, list.toArray(new int[0]));
    try {
      iterator.remove();
      fail("Expected IllegalStateException");
    }
    catch (final IllegalStateException e) {
    }
    iterator.next();
    iterator.remove();
    assertArrayEquals(new int[] {7, 5, 9, 1, 3}, list.toArray(new int[0]));
  }
}