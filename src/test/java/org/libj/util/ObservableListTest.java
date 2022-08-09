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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.junit.ComparisonFailure;
import org.junit.Test;

public class ObservableListTest {
  @SafeVarargs
  private static <T> void assertListEquals(final List<T> actual, final T ... expected) {
    assertArrayEquals(actual.toString(), expected, actual.toArray());
  }

  private boolean testingGetReplace;

  private String expectedString;
  private boolean beforeGet;
  private boolean afterGet;
  private boolean beforeAdd;
  private boolean afterAdd;
  private boolean beforeRemove;
  private boolean afterRemove;
  private boolean beforeSet;
  private boolean afterSet;

  private int fromIndex;

  private void reset() {
    beforeGet = false;
    afterGet = false;
    beforeAdd = false;
    afterAdd = false;
    beforeRemove = false;
    afterRemove = false;
    beforeSet = false;
    afterSet = false;
  }

  private void assertGot() {
    assertTrue(beforeGet && afterGet);
    if (!testingGetReplace)
      assertFalse(beforeRemove || afterRemove || beforeAdd || afterAdd || beforeSet || afterSet);

    reset();
  }

  private void assertRemoved() {
    assertTrue(beforeRemove);
    assertTrue(afterRemove);
    assertFalse(beforeAdd || afterAdd || beforeSet || afterSet);
    reset();
  }

  private void assertAdded() {
    assertTrue(beforeAdd && afterAdd);
    assertFalse(beforeRemove || afterRemove || beforeSet || afterSet);
    reset();
  }

  private void assertSet() {
    assertTrue(beforeSet && afterSet);
    assertFalse(beforeRemove || afterRemove || beforeAdd || afterAdd);
    reset();
  }

  @Test
  public void testStory() {
    final ObservableList<String> list = new ObservableList<String>(new ArrayList<>()) {
      @Override
      protected void beforeGet(final int index, final ListIterator<String> iterator) {
        beforeGet = true;
      }

      @Override
      protected void afterGet(final int index, final String element, final ListIterator<? super String> iterator, final RuntimeException e) {
        afterGet = true;
        if (testingGetReplace) {
          if (iterator != null)
            iterator.set(element.intern());
          else
            set(index, element.intern());
        }
      }

      @Override
      protected Object beforeAdd(final int index, final String element, final Object preventDefault) {
        assertEquals(expectedString, element);
        assertFalse(contains(element));
        beforeAdd = true;
        return element;
      }

      @Override
      protected boolean beforeRemove(final int index) {
        final String e = get(index + fromIndex);
        assertEquals(expectedString, e);
        assertTrue(contains(e));
        beforeRemove = true;
        return true;
      }

      @Override
      protected void afterAdd(final int index, final String element, final RuntimeException e) {
        assertEquals(expectedString, element);
        assertTrue(contains(element));
        afterAdd = true;
      }

      @Override
      @SuppressWarnings("all")
      protected void afterRemove(final Object element, final RuntimeException e) {
        assertEquals(expectedString, element);
        assertFalse(contains(element));
        afterRemove = true;
      }

      @Override
      protected boolean beforeSet(final int index, final String newElement) {
        if (!testingGetReplace) {
          assertEquals(expectedString, newElement);
          assertFalse(contains(newElement));
        }

        beforeSet = true;
        return true;
      }

      @Override
      protected void afterSet(final int index, final String oldElement, final RuntimeException e) {
        if (!testingGetReplace) {
          final String element = get(index + fromIndex);
          assertEquals(expectedString, element);
          assertTrue(contains(element));
        }

        afterSet = true;
      }
    };

    // add()
    for (int i = 0; i < 100; ++i) { // [N]
      list.add(expectedString = String.valueOf(i));
      assertAdded();
    }

    // get()
    for (int i = 0; i < 100; ++i) { // [N]
      list.get(i);
      assertGot();
    }

    // iterator.get()
    for (int i = 0, i$ = list.size(); i < i$; ++i) { // [L]
      assertNotNull(list.get(i));
      assertGot();
    }

    list.toArray();
    assertGot();

    list.toArray(new String[1]);
    assertGot();

    // testingGetReplace
    testingGetReplace = true;
    for (int i = 0; i < 100; ++i) { // [N]
      list.get(i);
      assertGot();
    }

    Iterator<String> iterator = list.iterator();
    while (iterator.hasNext()) {
      iterator.next();
      assertGot();
    }

    testingGetReplace = false;

    // addAll()
    list.addAll(Arrays.asList(expectedString = String.valueOf(101)));
    assertAdded();

    final int size = list.size();
    final List<String> subList = list.subList(fromIndex = 33, 44);
    assertEquals(11, subList.size());

    subList.remove(expectedString = String.valueOf(40));
    assertRemoved();

    final Iterator<String> subListIterator = subList.iterator();
    expectedString = subListIterator.next();
    subListIterator.remove();
    assertRemoved();

    assertEquals(9, subList.size());
    assertEquals(size, list.size() + 2);

    fromIndex = 0;
    // remove()
    for (int i = 0; i < 100; i += 7) { // [N]
      list.remove(expectedString = String.valueOf(i));
      assertRemoved();
    }

    // iterator()
    iterator = list.iterator();
    while (iterator.hasNext()) {
      final String element = iterator.next();
      if (String.valueOf(0).equals(element) || String.valueOf(33).equals(element) || String.valueOf(100).equals(element)) {
        expectedString = element;
        iterator.remove();
        assertRemoved();
      }
    }

    // listIterator()
    final ListIterator<String> listIterator = list.listIterator();
    while (listIterator.hasNext()) {
      final String element = listIterator.next();
      if (String.valueOf(0).equals(element) || String.valueOf(33).equals(element) || String.valueOf(100).equals(element)) {
        expectedString = element;
        listIterator.remove();
        assertRemoved();
      }
      else if (String.valueOf(8).equals(element) || String.valueOf(45).equals(element) || String.valueOf(76).equals(element)) {
        listIterator.set(expectedString = String.valueOf(100 + Math.random() * 10));
        assertSet();
      }
      else if (String.valueOf(12).equals(element) || String.valueOf(24).equals(element) || String.valueOf(73).equals(element)) {
        listIterator.add(expectedString = String.valueOf(200 + Math.random() * 10));
        assertAdded();
      }
    }

    // removeIf()
    for (int i = 9; i < 100; i += 23) { // [N]
      final int index = i;
      list.removeIf(s -> (expectedString = String.valueOf(index)).equals(s));
      assertRemoved();
    }

    // removeAll()
    list.removeAll(Arrays.asList(expectedString = "97"));
    assertRemoved();

    // retainAll()
    final Set<String> set = new HashSet<>(list);
    assertTrue(set.remove(expectedString = String.valueOf(37)));
    list.retainAll(set);
    assertRemoved();

    // clear()
    try {
      expectedString = String.valueOf(101);
      list.clear();
      fail("Expected ComparisonFailure");
    }
    catch (final ComparisonFailure e) {
      if (!"expected:<[101]> but was:<[99]>".equals(e.getMessage()))
        throw e;
    }
  }

  private static ObservableList<String> newListForSet() {
    return new ObservableList<String>(new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g"))) {
      @Override
      protected boolean beforeSet(final int index, final String newElement) {
        target.set(index, newElement);
        return false;
      }
    };
  }

  @Test
  public void testIteratorAddStart() {
    final ObservableList<String> list = newListForSet();

    final ListIterator<String> iterator = list.listIterator();
    assertFalse(iterator.hasPrevious());
    iterator.add("x");
    assertListEquals(list, "x", "a", "b", "c", "d", "e", "f", "g");
  }

  @Test
  public void testIteratorAddStart2() {
    final ObservableList<String> list = newListForSet();

    final ListIterator<String> iterator = list.listIterator(1);
    assertTrue(iterator.hasPrevious());
    assertEquals("a", iterator.previous());
    iterator.add("x");
    assertEquals("x", iterator.previous());
    assertFalse(iterator.hasPrevious());
    assertListEquals(list, "x", "a", "b", "c", "d", "e", "f", "g");
  }

  @Test
  public void testIteratorAddEnd() {
    final ObservableList<String> list = newListForSet();

    final ListIterator<String> iterator = list.listIterator(list.size() - 1);
    assertTrue(iterator.hasNext());
    assertEquals("g", iterator.next());
    iterator.add("x");
    assertEquals("x", iterator.previous());
    assertListEquals(list, "a", "b", "c", "d", "e", "f", "g", "x");
  }

  @Test
  public void testIteratorAddEnd2() {
    final ObservableList<String> list = newListForSet();

    final ListIterator<String> iterator = list.listIterator(list.size());
    assertFalse(iterator.hasNext());
    iterator.add("x");
    assertEquals("x", iterator.previous());
    assertListEquals(list, "a", "b", "c", "d", "e", "f", "g", "x");
  }

  @Test
  public void testIteratorSetStart() {
    final ObservableList<String> list = newListForSet();

    final ListIterator<String> iterator = list.listIterator();
    assertFalse(iterator.hasPrevious());
    assertEquals("a", iterator.next());
    iterator.set("x");
    assertListEquals(list, "x", "b", "c", "d", "e", "f", "g");
  }

  @Test
  public void testIteratorSetStart2() {
    final ObservableList<String> list = newListForSet();

    final ListIterator<String> iterator = list.listIterator();
    assertFalse(iterator.hasPrevious());
    assertEquals("a", iterator.next());
    assertEquals("a", iterator.previous());
    iterator.set("x");
    assertListEquals(list, "x", "b", "c", "d", "e", "f", "g");
  }

  @Test
  public void testIteratorSetEnd() {
    final ObservableList<String> list = newListForSet();

    final ListIterator<String> iterator = list.listIterator(list.size());
    assertFalse(iterator.hasNext());
    assertEquals("g", iterator.previous());
    iterator.set("x");
    assertListEquals(list, "a", "b", "c", "d", "e", "f", "x");
  }

  @Test
  public void testIteratorSetEnd2() {
    final ObservableList<String> list = newListForSet();

    final ListIterator<String> iterator = list.listIterator(list.size());
    assertFalse(iterator.hasNext());
    assertEquals("g", iterator.previous());
    assertEquals("g", iterator.next());
    iterator.set("x");
    assertListEquals(list, "a", "b", "c", "d", "e", "f", "x");
  }
}