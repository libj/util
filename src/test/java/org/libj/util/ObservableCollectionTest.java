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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.ComparisonFailure;
import org.junit.Test;

public class ObservableCollectionTest {
  private String expectedString;
  private boolean beforeAdd;
  private boolean afterAdd;
  private boolean beforeRemove;
  private boolean afterRemove;

  private void reset() {
    beforeAdd = false;
    afterAdd = false;
    beforeRemove = false;
    afterRemove = false;
  }

  private void assertRemoved() {
    assertTrue(beforeRemove && afterRemove);
    assertFalse(beforeAdd || afterAdd);
    reset();
  }

  private void assertAdded() {
    assertTrue(beforeAdd && afterAdd);
    assertFalse(beforeRemove || afterRemove);
    reset();
  }

  @Test
  public void test() {
    final ObservableCollection<String> collection = new ObservableCollection<String>(new HashSet<>()) {
      @Override
      protected Object beforeAdd(final String element, final Object preventDefault) {
        assertEquals(expectedString, element);
        assertFalse(contains(element));
        beforeAdd = true;
        return element;
      }

      @Override
      protected boolean beforeRemove(final Object element) {
        assertEquals(expectedString, element);
        assertTrue(contains(element));
        return beforeRemove = true;
      }

      @Override
      protected void afterAdd(final String element, final RuntimeException e) {
        assertEquals(expectedString, element);
        assertTrue(contains(element));
        afterAdd = true;
      }

      @Override
      protected void afterRemove(final Object element, final RuntimeException e) {
        assertEquals(expectedString, element);
        assertFalse(contains(element));
        afterRemove = true;
      }
    };

    // add()
    for (int i = 0; i < 100; ++i) { // [N]
      collection.add(expectedString = String.valueOf(i));
      assertAdded();
    }

    // addAll()
    collection.addAll(Arrays.asList(expectedString = String.valueOf(101)));
    assertAdded();

    // remove()
    for (int i = 0; i < 100; i += 7) { // [N]
      collection.remove(expectedString = String.valueOf(i));
      assertRemoved();
    }

    // iterator()
    final Iterator<String> iterator = collection.iterator();
    while (iterator.hasNext()) {
      final String element = iterator.next();
      if (String.valueOf(0).equals(element) || String.valueOf(33).equals(element) || String.valueOf(100).equals(element)) {
        expectedString = element;
        iterator.remove();
        assertRemoved();
      }
    }

    // removeIf()
    for (int i = 9; i < 100; i += 23) { // [N]
      final int index = i;
      collection.removeIf(s -> (expectedString = String.valueOf(index)).equals(s));
      assertRemoved();
    }

    // removeAll()
    collection.removeAll(Arrays.asList(expectedString = "97"));
    assertRemoved();

    // retainAll()
    final Set<String> set = new HashSet<>(collection);
    assertTrue(set.remove(expectedString = String.valueOf(37)));
    collection.retainAll(set);
    assertRemoved();

    // clear()
    try {
      collection.clear();
      fail("Expected ComparisonFailure");
    }
    catch (final ComparisonFailure e) {
      if (!"expected:<[37]> but was:<[88]>".equals(e.getMessage()))
        throw e;
    }
  }
}