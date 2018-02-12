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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.Assert;
import org.junit.Test;
import org.lib4j.lang.Strings;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PartitionedListTest {
  private static void assertElementCount(final String description, final List expected, final int expectedLength, final PartitionedList.PartitionList elements) {
    if (description != null) {
      System.err.println(description + " " + Strings.repeat("=", 34 - description.length()));
      elements.getSuperList().print();
    }

    Assert.assertEquals(expectedLength, elements.size());
    final Iterator iterator = elements.getSuperList().iterator();
    for (int i = 0; iterator.hasNext(); i++) {
      final Object next = iterator.next();
      Assert.assertEquals("Index " + i, expected.get(i), next);
    }
  }

  @Test
  public void testElementLists() {
    final PartitionedList<Object,Class<? extends Object>> superList = new PartitionedList<Object,Class<? extends Object>>(String.class, Integer.class, Double.class, Boolean.class) {
      @Override
      protected PartitionedList<Object,Class<? extends Object>>.PartitionList<Object> getPartition(Class<? extends Object> type) {
        do {
          if (typeToSubList.containsKey(type)) {
            PartitionList partitionList = typeToSubList.get(type);
            if (partitionList == null)
              typeToSubList.put(type, partitionList = newPartition(type));

            return partitionList;
          }
        }
        while ((type = type.getSuperclass()) != null);
        return null;
      }

      @Override
      @SuppressWarnings("deprecation")
      protected Object clone(final Object item) {
        if (item instanceof Integer)
          return new Integer((Integer)item);

        if (item instanceof Double)
          return new Double((Double)item);

        if (item instanceof Boolean)
          return new Boolean((Boolean)item);

        return new String((String)item);
      }
    };

    final List<Object> expected = new ArrayList<Object>();

    final String p1 = Random.alpha(6);
    expected.add(p1);
    superList.getPartition(String.class).add(p1);
    assertElementCount("Add " + System.identityHashCode(p1), expected, 1, superList.getPartition(String.class));

    final String p2 = Random.alpha(6);
    expected.add(p2);
    superList.getPartition(String.class).add(p2);
    assertElementCount("Add " + System.identityHashCode(p2), expected, 2, superList.getPartition(String.class));

    expected.add(p2);
    ListIterator listIterator = superList.listIterator(2);
    listIterator.add(p2);
    assertElementCount("Add " + System.identityHashCode(p2), expected, 3, superList.getPartition(String.class));

    listIterator = superList.listIterator(1);
    expected.remove(1);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(p1) + " at 0S", expected, 2, superList.getPartition(String.class));

    expected.add(0, p2);
    listIterator = superList.listIterator(0);
    listIterator.add(p2);
    assertElementCount("Add " + System.identityHashCode(p2) + " at 0S", expected, 3, superList.getPartition(String.class));

    final Integer div1 = Integer.valueOf((int)(Math.random() * 100));
    expected.add(2, div1);
    listIterator = superList.listIterator(2);
    listIterator.add(div1);
    assertElementCount("Add " + System.identityHashCode(div1) + " at 2S", expected, 3, superList.getPartition(String.class));
    assertElementCount(null, expected, 1, superList.getPartition(Integer.class));

    listIterator = superList.listIterator(1);
    expected.remove(1);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(p2) + " at 1S", expected, 2, superList.getPartition(String.class));
    assertElementCount(null, expected, 1, superList.getPartition(Integer.class));

    listIterator = superList.listIterator(1);
    expected.add(1, p2);
    listIterator.add(p2);
    assertElementCount("Add " + System.identityHashCode(p2) + " at 1S", expected, 3, superList.getPartition(String.class));
    assertElementCount(null, expected, 1, superList.getPartition(Integer.class));

    testClone(superList);
    final Integer div2 = Integer.valueOf((int)(Math.random() * 100));
    expected.add(1, div2);
    listIterator = superList.listIterator();
    listIterator.next();
    listIterator.add(div2);
    assertElementCount("Add " + System.identityHashCode(div2) + " at 1S", expected, 3, superList.getPartition(String.class));
    assertElementCount(null, expected, 2, superList.getPartition(Integer.class));

    final Integer div3 = Integer.valueOf((int)(Math.random() * 100));
    expected.add(2, div3);
    listIterator = superList.getPartition(Integer.class).listIterator();
    listIterator.next();
    listIterator.add(div3);
    assertElementCount("Add " + System.identityHashCode(div3) + " at 1E", expected, 3, superList.getPartition(String.class));
    assertElementCount(null, expected, 3, superList.getPartition(Integer.class));

    final Double pre1 = Math.random();
    expected.set(3, pre1);
    listIterator = superList.listIterator(3);
    listIterator.next();
    listIterator.set(pre1);
    assertElementCount("Set " + System.identityHashCode(pre1) + " at 3S", expected, 2, superList.getPartition(String.class));
    assertElementCount(null, expected, 3, superList.getPartition(Integer.class));
    assertElementCount(null, expected, 1, superList.getPartition(Double.class));

    final Boolean hr1 = Boolean.TRUE;
    expected.set(1, hr1);
    listIterator = superList.listIterator(1);
    listIterator.next();
    listIterator.set(hr1);
    assertElementCount("Set " + System.identityHashCode(hr1) + " at 1S", expected, 2, superList.getPartition(String.class));
    assertElementCount(null, expected, 2, superList.getPartition(Integer.class));
    assertElementCount(null, expected, 1, superList.getPartition(Double.class));
    assertElementCount(null, expected, 1, superList.getPartition(Boolean.class));

    listIterator = superList.listIterator(1);
    expected.remove(1);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(hr1) + " at 1S", expected, 2, superList.getPartition(String.class));
    assertElementCount(null, expected, 2, superList.getPartition(Integer.class));
    assertElementCount(null, expected, 1, superList.getPartition(Double.class));
    assertElementCount(null, expected, 0, superList.getPartition(Boolean.class));

    listIterator = superList.listIterator(4);
    expected.remove(4);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(p2) + " at 4S", expected, 1, superList.getPartition(String.class));
    assertElementCount(null, expected, 2, superList.getPartition(Integer.class));
    assertElementCount(null, expected, 1, superList.getPartition(Double.class));
    assertElementCount(null, expected, 0, superList.getPartition(Boolean.class));

    listIterator = superList.getPartition(Double.class).listIterator();
    expected.remove(2);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(pre1) + " at 0E", expected, 1, superList.getPartition(String.class));
    assertElementCount(null, expected, 2, superList.getPartition(Integer.class));
    assertElementCount(null, expected, 0, superList.getPartition(Double.class));
    assertElementCount(null, expected, 0, superList.getPartition(Boolean.class));

    listIterator = superList.getPartition(Integer.class).listIterator(1);
    expected.remove(2);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(div1) + " at 1E", expected, 1, superList.getPartition(String.class));
    assertElementCount(null, expected, 1, superList.getPartition(Integer.class));
    assertElementCount(null, expected, 0, superList.getPartition(Double.class));
    assertElementCount(null, expected, 0, superList.getPartition(Boolean.class));

    expected.add(0, p2);
    superList.getPartition(String.class).add(0, p2);
    assertElementCount("Add " + System.identityHashCode(p2) + " at 0E", expected, 2, superList.getPartition(String.class));
    assertElementCount(null, expected, 1, superList.getPartition(Integer.class));
    assertElementCount(null, expected, 0, superList.getPartition(Double.class));
    assertElementCount(null, expected, 0, superList.getPartition(Boolean.class));

    listIterator = superList.getPartition(Integer.class).listIterator();
    expected.remove(2);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(div3) + " at 0E", expected, 2, superList.getPartition(String.class));
    assertElementCount(null, expected, 0, superList.getPartition(Integer.class));
    assertElementCount(null, expected, 0, superList.getPartition(Double.class));
    assertElementCount(null, expected, 0, superList.getPartition(Boolean.class));

    listIterator = superList.getPartition(String.class).listIterator();
    expected.remove(0);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(p2) + " at 0E", expected, 1, superList.getPartition(String.class));
    assertElementCount(null, expected, 0, superList.getPartition(Integer.class));
    assertElementCount(null, expected, 0, superList.getPartition(Double.class));
    assertElementCount(null, expected, 0, superList.getPartition(Boolean.class));

    expected.add(0, p1);
    superList.getPartition(String.class).add(0, p1);
    assertElementCount("Add " + System.identityHashCode(p1) + " at 0E", expected, 2, superList.getPartition(String.class));
    assertElementCount(null, expected, 0, superList.getPartition(Integer.class));
    assertElementCount(null, expected, 0, superList.getPartition(Double.class));
    assertElementCount(null, expected, 0, superList.getPartition(Boolean.class));

    final Boolean hr2 = Boolean.FALSE;
    expected.set(1, hr2);
    listIterator = superList.listIterator(1);
    listIterator.next();
    listIterator.set(hr2);
    assertElementCount("Set " + System.identityHashCode(hr2) + " at 1S", expected, 1, superList.getPartition(String.class));
    assertElementCount(null, expected, 0, superList.getPartition(Integer.class));
    assertElementCount(null, expected, 0, superList.getPartition(Double.class));
    assertElementCount(null, expected, 1, superList.getPartition(Boolean.class));

    final String p3 = Random.alpha(6);
    expected.add(p3);
    superList.getPartition(String.class).add(p3);
    assertElementCount("Add " + System.identityHashCode(p3), expected, 2, superList.getPartition(String.class));
    assertElementCount(null, expected, 0, superList.getPartition(Integer.class));
    assertElementCount(null, expected, 0, superList.getPartition(Double.class));
    assertElementCount(null, expected, 1, superList.getPartition(Boolean.class));

    final Double pre2 = Math.random();
    expected.add(1, pre2);
    listIterator = superList.listIterator(1);
    listIterator.add(pre2);
    assertElementCount("Remove " + System.identityHashCode(pre2) + " at 1S", expected, 2, superList.getPartition(String.class));
    assertElementCount(null, expected, 0, superList.getPartition(Integer.class));
    assertElementCount(null, expected, 1, superList.getPartition(Double.class));
    assertElementCount(null, expected, 1, superList.getPartition(Boolean.class));
  }

  public void testClone(final PartitionedList<Object,Class<? extends Object>> list) {
    final PartitionedList<Object,Class<? extends Object>> clone = list.clone();
    Assert.assertEquals(list.size(), clone.size());
    final ListIterator listIterator = list.listIterator();
    final ListIterator cloneIterator = clone.listIterator();
    while (listIterator.hasNext()) {
      final Object listItem = listIterator.next();
      final Object cloneItem = cloneIterator.next();
      Assert.assertEquals(listItem, cloneItem);
      Assert.assertFalse(listItem == cloneItem);
    }
  }
}