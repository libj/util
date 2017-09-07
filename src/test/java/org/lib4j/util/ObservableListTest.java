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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.junit.Assert;
import org.junit.ComparisonFailure;
import org.junit.Test;

public class ObservableListTest {
  private String expectedString;
  private boolean beforeAdd;
  private boolean afterAdd;
  private boolean beforeRemove;
  private boolean afterRemove;
  private boolean beforeSet;
  private boolean afterSet;

  private int fromIndex;

  private void reset() {
    beforeAdd = false;
    afterAdd = false;
    beforeRemove = false;
    afterRemove = false;
    beforeSet = false;
    afterSet = false;
  }

  private void assertRemoved() {
    Assert.assertTrue(beforeRemove && afterRemove);
    Assert.assertFalse(beforeAdd || afterAdd || beforeSet || afterSet);
    reset();
  }

  private void assertAdded() {
    Assert.assertTrue(beforeAdd && afterAdd);
    Assert.assertFalse(beforeRemove || afterRemove || beforeSet || afterSet);
    reset();
  }

  private void assertSet() {
    Assert.assertTrue(beforeSet && afterSet);
    Assert.assertFalse(beforeRemove || afterRemove || beforeAdd || afterAdd);
    reset();
  }

  @Test
  public void test() {
    final ObservableList<String> list = new ObservableList<String>(new ArrayList<String>()) {
      private static final long serialVersionUID = -7731083592807446023L;

      @Override
      protected void beforeAdd(final int index, final String e) {
        Assert.assertEquals(expectedString, e);
        Assert.assertFalse(contains(e));
        beforeAdd = true;
      }

      @Override
      protected void beforeRemove(final int index) {
        final String e = ((List<String>)source).get(index + fromIndex);
        Assert.assertEquals(expectedString, e);
        Assert.assertTrue(contains(e));
        beforeRemove = true;
      }

      @Override
      protected void afterAdd(final int index, final String e) {
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

      @Override
      protected void beforeSet(final int index, final String newElement) {
        Assert.assertEquals(expectedString, newElement);
        Assert.assertFalse(contains(newElement));
        beforeSet = true;
      }

      @Override
      protected void afterSet(final int index, final String oldElement) {
        final String e = ((List<String>)source).get(index + fromIndex);
        Assert.assertEquals(expectedString, e);
        Assert.assertTrue(contains(e));
        afterSet = true;
      }
    };

    // add()
    for (int i = 0; i < 100; i++) {
      list.add(expectedString = String.valueOf(i));
      assertAdded();
    }

    // addAll()
    list.addAll(Arrays.asList(new String[] {expectedString = String.valueOf(101)}));
    assertAdded();

    final int size = list.size();
    final List<String> subList = list.subList(fromIndex = 33, 44);
    Assert.assertEquals(11, subList.size());

    subList.remove(expectedString = String.valueOf(40));
    assertRemoved();

    final Iterator<String> subListIterator = subList.iterator();
    expectedString = subListIterator.next();
    subListIterator.remove();
    assertRemoved();

    Assert.assertEquals(9, subList.size());
    Assert.assertEquals(size, list.size() + 2);

    fromIndex = 0;
    // remove()
    for (int i = 0; i < 100; i += 7) {
      list.remove(expectedString = String.valueOf(i));
      assertRemoved();
    }

    // iterator()
    final Iterator<String> iterator = list.iterator();
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
    for (int i = 9; i < 100; i += 23) {
      final int index = i;
      list.removeIf(s -> (expectedString = String.valueOf(index)).equals(s));
      assertRemoved();
    }

    // removeAll()
    list.removeAll(Arrays.asList(new String[] {expectedString = "97"}));
    assertRemoved();

    // retainAll()
    final Set<String> set = new HashSet<String>(list);
    Assert.assertTrue(set.remove(expectedString = String.valueOf(37)));
    list.retainAll(set);
    assertRemoved();

    // clear()
    try {
      list.clear();
      Assert.fail("Expected ComparisonFailure");
    }
    catch (final ComparisonFailure e) {
      if (!"expected:<[37]> but was:<[1]>".equals(e.getMessage()))
        throw e;
    }
  }
}