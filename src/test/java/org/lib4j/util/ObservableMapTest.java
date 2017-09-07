/* Copyright (c) 2017 lib4j
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.junit.Assert;
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
    Assert.assertTrue(beforeRemove && afterRemove);
    Assert.assertFalse(beforePut || afterPut);
    reset();
  }

  private void assertPut() {
    Assert.assertTrue(beforePut && afterPut);
    Assert.assertFalse(beforeRemove || afterRemove);
    reset();
  }

  @Test
  public void test() {
    final ObservableMap<Integer,String> map = new ObservableMap<Integer,String>(new HashMap<Integer,String>()) {
      @Override
      protected void beforePut(final Integer key, final String oldValue, final String newValue) {
        Assert.assertEquals(expectedKey, key);
        Assert.assertEquals(expectedValue, newValue);
        Assert.assertFalse(containsKey(key));
        beforePut = true;
      }

      @Override
      protected void afterPut(final Integer key, final String oldValue, final String newValue) {
        Assert.assertEquals(expectedKey, key);
        Assert.assertEquals(expectedValue, newValue);
        Assert.assertTrue(containsKey(key));
        afterPut = true;
      }

      @Override
      protected void beforeRemove(final Object key, final String value) {
        Assert.assertEquals(expectedKey, key);
        Assert.assertEquals(expectedValue, value);
        Assert.assertTrue(containsKey(key));
        beforeRemove = true;
      }

      @Override
      protected void afterRemove(final Object key, final String value) {
        Assert.assertEquals(expectedKey, key);
        Assert.assertEquals(expectedValue, value);
        Assert.assertFalse(containsKey(key));
        afterRemove = true;
      }
    };

    // put()
    for (int i = 0; i < 100; i++) {
      map.put(expectedKey = i, expectedValue = String.valueOf(i));
      assertPut();
    }

    // putAll()
    map.putAll(java.util.Collections.singletonMap(expectedKey = 101, expectedValue = String.valueOf(101)));
    assertPut();

    // remove()
    for (int i = 0; i < 100; i += 7) {
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
    for (int i = 9; i < 100; i += 23) {
      final int index = i;
      expectedValue = String.valueOf(index);
      keys.removeIf(s -> (expectedKey = index).equals(s));
      assertRemoved();
    }

    // removeAll()
    expectedValue = "97";
    keys.removeAll(Arrays.asList(new Integer[] {expectedKey = 97}));
    assertRemoved();

    // clear()
    try {
      map.clear();
      Assert.fail("Expected ComparisonFailure");
    }
    catch (final AssertionError e) {
      if (!"expected:<97> but was:<1>".equals(e.getMessage()))
        throw e;
    }
  }
}