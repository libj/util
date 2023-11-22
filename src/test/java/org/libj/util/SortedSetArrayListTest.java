/* Copyright (c) 2023 LibJ
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
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import org.junit.Test;

public class SortedSetArrayListTest {
  private static void assertIndexOf(final SortedSetArrayList<String> list) {
    final Iterator<String> iterator = list.iterator();
    final ListIterator<String> listIterator = list.listIterator();
    for (int i = 0, i$ = list.size(); i < i$; ++i) { // [ST]
      final String str = String.valueOf((char)('a' + i));
      assertEquals(i, list.indexOf(str));
      assertEquals(i, list.lastIndexOf(str));
      assertTrue(iterator.hasNext());
      assertEquals(str, iterator.next());
      assertTrue(listIterator.hasNext());
      assertEquals(str, listIterator.next());

      try {
        listIterator.add("x");
        fail("Expected UnsupportedOperationException");
      }
      catch (final UnsupportedOperationException e) {
      }

      try {
        listIterator.set("x");
        fail("Expected UnsupportedOperationException");
      }
      catch (final UnsupportedOperationException e) {
      }
    }

    assertFalse(iterator.hasNext());
    assertFalse(listIterator.hasNext());
  }

  @Test
  public void testConstructor() {
    final Collection<String> collection = Arrays.asList("e", "b", "a", "c", "b", "f", "a", "g", "g", "d", "e");
    final SortedSetArrayList<String> list = new SortedSetArrayList<>(collection);
    SortedListTest.assertListEquals(list, "a", "b", "c", "d", "e", "f", "g");
    assertIndexOf(list);
    assertTrue(list.containsAll(collection));
  }

  @Test
  public void testAddAll() {
    final Collection<String> collection = Arrays.asList("e", "b", "a", "c", "b", "f", "a", "g", "g", "d", "e");
    final SortedSetArrayList<String> list = new SortedSetArrayList<>();
    list.addAll(collection);
    SortedListTest.assertListEquals(list, "a", "b", "c", "d", "e", "f", "g");
    assertIndexOf(list);
    assertTrue(list.containsAll(collection));
  }

  @Test
  public void testRetain() {
    final SortedSetArrayList<String> list = new SortedSetArrayList<>(Arrays.asList("e", "b", "a", "c", "b", "f", "a", "g", "g", "d", "e"));
    final Collection<String> retain = Arrays.asList("e", "c", "f", "g");
    list.retainAll(retain);
    assertEquals(retain.size(), list.size());
    assertTrue(list.containsAll(retain));
  }

  @Test
  public void testRemove() {
    final SortedSetArrayList<String> list = new SortedSetArrayList<>(Arrays.asList("e", "b", "a", "c", "b", "f", "a", "g", "g", "d", "e"));
    final Collection<String> remove = Arrays.asList("e", "c", "f", "g");
    list.removeAll(remove);
    assertEquals(3, list.size());
    assertTrue(list.containsAll(Arrays.asList("a", "b", "d")));
  }

  private static void testIteratorRemove(final SortedSetArrayList<String> list, final Iterator<String> iterator) {
    iterator.next();
    iterator.remove();
    SortedListTest.assertListEquals(list, "b", "c", "d", "e", "f", "g");

    iterator.next();
    iterator.next();
    iterator.remove();
    SortedListTest.assertListEquals(list, "b", "d", "e", "f", "g");

    iterator.next();
    iterator.next();
    iterator.next();
    iterator.remove();
    SortedListTest.assertListEquals(list, "b", "d", "e", "g");

    iterator.next();
    iterator.remove();
    SortedListTest.assertListEquals(list, "b", "d", "e");
  }

  @Test
  public void testIteratorRemove() {
    final SortedSetArrayList<String> list = new SortedSetArrayList<>(Arrays.asList("e", "b", "a", "c", "b", "f", "a", "g", "g", "d", "e"));
    final Iterator<String> iterator = list.iterator();
    testIteratorRemove(list, iterator);
  }

  @Test
  public void testListIteratorRemove() {
    final SortedSetArrayList<String> list = new SortedSetArrayList<>(Arrays.asList("e", "b", "a", "c", "b", "f", "a", "g", "g", "d", "e"));
    final Iterator<String> iterator = list.listIterator();
    testIteratorRemove(list, iterator);

    final Iterator<String> iterator2 = list.listIterator(1);
    assertEquals("d", iterator2.next());
  }
}