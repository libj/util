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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Assert;
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
    Assert.assertTrue(beforeRemove && afterRemove);
    Assert.assertFalse(beforeAdd || afterAdd);
    reset();
  }

  private void assertAdded() {
    Assert.assertTrue(beforeAdd && afterAdd);
    Assert.assertFalse(beforeRemove || afterRemove);
    reset();
  }

  @Test
  public void test() {
    final ObservableCollection<String> collection = new ObservableCollection<String>(new HashSet<String>()) {
      @Override
      protected void beforeAdd(final String e) {
        Assert.assertEquals(expectedString, e);
        Assert.assertFalse(contains(e));
        beforeAdd = true;
      }

      @Override
      protected void beforeRemove(final Object e) {
        Assert.assertEquals(expectedString, e);
        Assert.assertTrue(contains(e));
        beforeRemove = true;
      }

      @Override
      protected void afterAdd(final String e) {
        Assert.assertEquals(expectedString, e);
        Assert.assertTrue(contains(e));
        afterAdd = true;
      }

      @Override
      protected void afterRemove(final Object e) {
        Assert.assertEquals(expectedString, e);
        Assert.assertFalse(contains(e));
        afterRemove = true;
      }
    };

    // add()
    for (int i = 0; i < 100; i++) {
      collection.add(expectedString = String.valueOf(i));
      assertAdded();
    }

    // addAll()
    collection.addAll(Arrays.asList(new String[] {expectedString = String.valueOf(101)}));
    assertAdded();

    // remove()
    for (int i = 0; i < 100; i += 7) {
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
    for (int i = 9; i < 100; i += 23) {
      final int index = i;
      collection.removeIf(s -> (expectedString = String.valueOf(index)).equals(s));
      assertRemoved();
    }

    // removeAll()
    collection.removeAll(Arrays.asList(new String[] {expectedString = "97"}));
    assertRemoved();

    // retainAll()
    final Set<String> set = new HashSet<String>(collection);
    Assert.assertTrue(set.remove(expectedString = String.valueOf(37)));
    collection.retainAll(set);
    assertRemoved();

    // clear()
    try {
      collection.clear();
      Assert.fail("Expected ComparisonFailure");
    }
    catch (final ComparisonFailure e) {
      if (!"expected:<[37]> but was:<[88]>".equals(e.getMessage()))
        throw e;
    }
  }
}