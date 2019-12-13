/* Copyright (c) 2019 LibJ
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
import java.util.ListIterator;

import org.junit.Test;

public class CursorListIteratorTest {
  public static CursorListIterator<Integer> newListIterator() {
    return new CursorListIterator<>(new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5)).listIterator());
  }

  @Test
  public void testIndexOfLast() {
    final CursorListIterator<Integer> iterator = newListIterator();
    assertEquals(Integer.valueOf(0), iterator.next());
    assertEquals(0, iterator.indexOfLast());
    assertEquals(Integer.valueOf(1), iterator.next());
    assertEquals(1, iterator.indexOfLast());
    assertEquals(Integer.valueOf(1), iterator.previous());
    assertEquals(1, iterator.indexOfLast());
    assertEquals(Integer.valueOf(0), iterator.previous());
    assertEquals(0, iterator.indexOfLast());
    iterator.remove();
    assertEquals(-1, iterator.indexOfLast());
    try {
      iterator.assertModifiable();
      fail("Expected IllegalStateException");
    }
    catch (final IllegalStateException e) {
    }
  }

  @Test
  public void testIndexForNext() {
    final CursorListIterator<Integer> iterator = newListIterator();
    assertEquals(Integer.valueOf(0), iterator.next());
    assertEquals(1, iterator.indexForNext());
    assertEquals(Integer.valueOf(1), iterator.next());
    assertEquals(2, iterator.indexForNext());
    assertEquals(Integer.valueOf(1), iterator.previous());
    assertEquals(1, iterator.indexForNext());
    assertEquals(Integer.valueOf(0), iterator.previous());
    assertEquals(0, iterator.indexForNext());
    iterator.remove();
    assertEquals(-1, iterator.indexOfLast());
    try {
      iterator.assertModifiable();
      fail("Expected IllegalStateException");
    }
    catch (final IllegalStateException e) {
    }
  }

  @Test
  public void testAssertRemove() {
    final ListIterator<Integer> iterator = newListIterator();
    assertEquals(Integer.valueOf(0), iterator.next());
    iterator.remove();
    try {
      iterator.remove();
      fail("Expected IllegalStateException");
    }
    catch (final IllegalStateException e) {
    }
  }

  @Test
  public void testAssertSet() {
    final ListIterator<Integer> iterator = newListIterator();
    assertEquals(Integer.valueOf(0), iterator.next());
    iterator.set(9);
    try {
      iterator.set(4);
      fail("Expected IllegalStateException");
    }
    catch (final IllegalStateException e) {
    }
  }

  @Test
  public void testAssertAdd() {
    final ListIterator<Integer> iterator = newListIterator();
    assertEquals(Integer.valueOf(0), iterator.next());
    iterator.add(9);
    try {
      iterator.add(4);
      fail("Expected IllegalStateException");
    }
    catch (final IllegalStateException e) {
    }
  }
}