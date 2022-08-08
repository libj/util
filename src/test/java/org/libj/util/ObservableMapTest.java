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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

public class ObservableMapTest {
  private Integer expectedKey;
  private String expectedValue;
  private boolean beforePut;
  private boolean afterPut;
  private boolean beforeRemove;
  private boolean afterRemove;

  private void reset() {
    beforePut = false;
    afterPut = false;
    beforeRemove = false;
    afterRemove = false;
  }

  private void assertRemoved() {
    assertTrue(beforeRemove && afterRemove);
    assertFalse(beforePut || afterPut);
    reset();
  }

  private void assertPut() {
    assertTrue(beforePut && afterPut);
    assertFalse(beforeRemove || afterRemove);
    reset();
  }

  @Test
  public void test() {
    final ObservableMap<Integer,String> map = new ObservableMap<Integer,String>(new HashMap<>()) {
      @Override
      protected Object beforePut(final Integer key, final String oldValue, final String newValue, final Object preventDefault) {
        assertEquals(expectedKey, key);
        assertEquals(expectedValue, newValue);
        assertFalse(containsKey(key));
        beforePut = true;
        return newValue;
      }

      @Override
      protected void afterPut(final Integer key, final String oldValue, final String newValue, final RuntimeException e) {
        assertEquals(expectedKey, key);
        assertEquals(expectedValue, newValue);
        assertTrue(containsKey(key));
        afterPut = true;
      }

      @Override
      @SuppressWarnings("all")
      protected boolean beforeRemove(final Object key, final String value) {
        assertEquals(expectedKey, key);
        assertEquals(expectedValue, value);
        assertTrue(containsKey(key));
        return beforeRemove = true;
      }

      @Override
      @SuppressWarnings("all")
      protected void afterRemove(final Object key, final String value, final RuntimeException e) {
        assertEquals(expectedKey, key);
        assertEquals(expectedValue, value);
        assertFalse(containsKey(key));
        afterRemove = true;
      }
    };

    // put()
    for (int i = 0; i < 100; ++i) { // [N]
      map.put(expectedKey = i, expectedValue = String.valueOf(i));
      assertPut();
    }

    // putAll()
    map.putAll(java.util.Collections.singletonMap(expectedKey = 101, expectedValue = String.valueOf(101)));
    assertPut();

    // remove()
    for (int i = 0; i < 100; i += 7) { // [N]
      expectedValue = String.valueOf(i);
      map.remove(expectedKey = i);
      assertRemoved();
    }

    // iterator()
    final Iterator<String> iterator = map.values().iterator();
    while (iterator.hasNext()) {
      final String element = iterator.next();
      if (String.valueOf(0).equals(element) || String.valueOf(33).equals(element) || String.valueOf(100).equals(element)) {
        expectedKey = Integer.valueOf(element);
        expectedValue = element;
        iterator.remove();
        assertRemoved();
      }
    }

    final Set<Integer> keys = map.keySet();
    // removeIf()
    for (int i = 9; i < 100; i += 23) { // [N]
      final int index = i;
      expectedValue = String.valueOf(index);
      keys.removeIf(s -> (expectedKey = index).equals(s));
      assertRemoved();
    }

    // removeAll()
    expectedValue = "97";
    keys.removeAll(Arrays.asList(expectedKey = 97));
    assertRemoved();

    // clear()
    try {
      map.clear();
      fail("Expected ComparisonFailure");
    }
    catch (final AssertionError e) {
      if (!"expected:<97> but was:<1>".equals(e.getMessage()))
        throw e;
    }
  }
}