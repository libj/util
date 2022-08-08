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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;
import org.libj.lang.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"rawtypes", "unchecked"})
public class CompositeListTest {
  private static final Logger logger = LoggerFactory.getLogger(CompositeListTest.class);

  private static void assertElementCount(final String description, final List expected, final int expectedLength, final CompositeList.ComponentList elements) {
    if (description != null) {
      logger.info(description + " " + Strings.repeat("=", 34 - description.length()));
      elements.getCompositeList().print(logger);
    }

    assertEquals(expectedLength, elements.size());
    final Iterator iterator = elements.getCompositeList().iterator();
    for (int i = 0; iterator.hasNext(); ++i) { // [I]
      final Object next = iterator.next();
      assertEquals("Index " + i, expected.get(i), next);
    }
  }

  private static final class TestCompositeList extends CompositeList<Object,Class<?>> {
    private TestCompositeList(final Class<?> ... types) {
      super(types);
    }

    @Override
    protected CompositeList<Object,Class<?>>.ComponentList getOrCreateComponentList(final Object element) {
      return getOrCreateComponentList(element.getClass());
    }

    protected CompositeList<Object,Class<?>>.ComponentList getOrCreateComponentList(Class<?> type) {
      do {
        if (containsComponentType(type)) {
          ComponentList componentList = getComponentList(type);
          if (componentList == null)
            registerComponentList(type, componentList = newComponentList(type));

          return componentList;
        }
      }
      while ((type = type.getSuperclass()) != null);
      return null;
    }

    @Override
    protected Object clone(final Object item) {
      if (item instanceof Integer)
        return new Integer((Integer)item);

      if (item instanceof Double)
        return new Double((Double)item);

      if (item instanceof Boolean)
        return new Boolean((Boolean)item);

      return new String((String)item);
    }

    @Override
    public TestCompositeList clone() {
      return (TestCompositeList)super.clone();
    }
  }

  @Test
  public void testElementLists() {
    final TestCompositeList compositeList = new TestCompositeList(String.class, Integer.class, Double.class, Boolean.class);

    final List<Object> expected = new ArrayList<>();

    final String p1 = Strings.getRandomAlpha(6);
    expected.add(p1);
    compositeList.getOrCreateComponentList(String.class).add(p1);
    assertElementCount("Add " + System.identityHashCode(p1), expected, 1, compositeList.getOrCreateComponentList(String.class));

    final String p2 = Strings.getRandomAlpha(6);
    expected.add(p2);
    compositeList.getOrCreateComponentList(String.class).add(p2);
    assertElementCount("Add " + System.identityHashCode(p2), expected, 2, compositeList.getOrCreateComponentList(String.class));

    expected.add(p2);
    ListIterator listIterator = compositeList.listIterator(2);
    listIterator.add(p2);
    assertElementCount("Add " + System.identityHashCode(p2), expected, 3, compositeList.getOrCreateComponentList(String.class));

    listIterator = compositeList.listIterator(1);
    expected.remove(1);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(p1) + " at 0S", expected, 2, compositeList.getOrCreateComponentList(String.class));

    expected.add(0, p2);
    listIterator = compositeList.listIterator(0);
    listIterator.add(p2);
    assertElementCount("Add " + System.identityHashCode(p2) + " at 0S", expected, 3, compositeList.getOrCreateComponentList(String.class));

    final Integer div1 = (int)(Math.random() * 100);
    expected.add(2, div1);
    listIterator = compositeList.listIterator(2);
    listIterator.add(div1);
    assertElementCount("Add " + System.identityHashCode(div1) + " at 2S", expected, 3, compositeList.getOrCreateComponentList(String.class));
    assertElementCount(null, expected, 1, compositeList.getOrCreateComponentList(Integer.class));

    listIterator = compositeList.listIterator(1);
    expected.remove(1);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(p2) + " at 1S", expected, 2, compositeList.getOrCreateComponentList(String.class));
    assertElementCount(null, expected, 1, compositeList.getOrCreateComponentList(Integer.class));

    listIterator = compositeList.listIterator(1);
    expected.add(1, p2);
    listIterator.add(p2);
    assertElementCount("Add " + System.identityHashCode(p2) + " at 1S", expected, 3, compositeList.getOrCreateComponentList(String.class));
    assertElementCount(null, expected, 1, compositeList.getOrCreateComponentList(Integer.class));

    testClone(compositeList);
    final Integer div2 = (int)(Math.random() * 100);
    expected.add(1, div2);
    listIterator = compositeList.listIterator();
    listIterator.next();
    listIterator.add(div2);
    assertElementCount("Add " + System.identityHashCode(div2) + " at 1S", expected, 3, compositeList.getOrCreateComponentList(String.class));
    assertElementCount(null, expected, 2, compositeList.getOrCreateComponentList(Integer.class));

    final Integer div3 = (int)(Math.random() * 100);
    expected.add(2, div3);
    listIterator = compositeList.getOrCreateComponentList(Integer.class).listIterator();
    listIterator.next();
    listIterator.add(div3);
    assertElementCount("Add " + System.identityHashCode(div3) + " at 1E", expected, 3, compositeList.getOrCreateComponentList(String.class));
    assertElementCount(null, expected, 3, compositeList.getOrCreateComponentList(Integer.class));

    final Double pre1 = Math.random();
    expected.set(3, pre1);
    listIterator = compositeList.listIterator(3);
    listIterator.next();
    listIterator.set(pre1);
    assertElementCount("Set " + System.identityHashCode(pre1) + " at 3S", expected, 2, compositeList.getOrCreateComponentList(String.class));
    assertElementCount(null, expected, 3, compositeList.getOrCreateComponentList(Integer.class));
    assertElementCount(null, expected, 1, compositeList.getOrCreateComponentList(Double.class));

    final Boolean hr1 = Boolean.TRUE;
    expected.set(1, hr1);
    listIterator = compositeList.listIterator(1);
    listIterator.next();
    listIterator.set(hr1);
    assertElementCount("Set " + System.identityHashCode(hr1) + " at 1S", expected, 2, compositeList.getOrCreateComponentList(String.class));
    assertElementCount(null, expected, 2, compositeList.getOrCreateComponentList(Integer.class));
    assertElementCount(null, expected, 1, compositeList.getOrCreateComponentList(Double.class));
    assertElementCount(null, expected, 1, compositeList.getOrCreateComponentList(Boolean.class));

    listIterator = compositeList.listIterator(1);
    expected.remove(1);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(hr1) + " at 1S", expected, 2, compositeList.getOrCreateComponentList(String.class));
    assertElementCount(null, expected, 2, compositeList.getOrCreateComponentList(Integer.class));
    assertElementCount(null, expected, 1, compositeList.getOrCreateComponentList(Double.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Boolean.class));

    listIterator = compositeList.listIterator(4);
    expected.remove(4);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(p2) + " at 4S", expected, 1, compositeList.getOrCreateComponentList(String.class));
    assertElementCount(null, expected, 2, compositeList.getOrCreateComponentList(Integer.class));
    assertElementCount(null, expected, 1, compositeList.getOrCreateComponentList(Double.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Boolean.class));

    listIterator = compositeList.getOrCreateComponentList(Double.class).listIterator();
    expected.remove(2);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(pre1) + " at 0E", expected, 1, compositeList.getOrCreateComponentList(String.class));
    assertElementCount(null, expected, 2, compositeList.getOrCreateComponentList(Integer.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Double.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Boolean.class));

    listIterator = compositeList.getOrCreateComponentList(Integer.class).listIterator(1);
    expected.remove(2);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(div1) + " at 1E", expected, 1, compositeList.getOrCreateComponentList(String.class));
    assertElementCount(null, expected, 1, compositeList.getOrCreateComponentList(Integer.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Double.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Boolean.class));

    expected.add(0, p2);
    compositeList.getOrCreateComponentList(String.class).add(0, p2);
    assertElementCount("Add " + System.identityHashCode(p2) + " at 0E", expected, 2, compositeList.getOrCreateComponentList(String.class));
    assertElementCount(null, expected, 1, compositeList.getOrCreateComponentList(Integer.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Double.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Boolean.class));

    listIterator = compositeList.getOrCreateComponentList(Integer.class).listIterator();
    expected.remove(2);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(div3) + " at 0E", expected, 2, compositeList.getOrCreateComponentList(String.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Integer.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Double.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Boolean.class));

    listIterator = compositeList.getOrCreateComponentList(String.class).listIterator();
    expected.remove(0);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(p2) + " at 0E", expected, 1, compositeList.getOrCreateComponentList(String.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Integer.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Double.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Boolean.class));

    expected.add(0, p1);
    compositeList.getOrCreateComponentList(String.class).add(0, p1);
    assertElementCount("Add " + System.identityHashCode(p1) + " at 0E", expected, 2, compositeList.getOrCreateComponentList(String.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Integer.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Double.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Boolean.class));

    final Boolean hr2 = Boolean.FALSE;
    expected.set(1, hr2);
    listIterator = compositeList.listIterator(1);
    listIterator.next();
    listIterator.set(hr2);
    assertElementCount("Set " + System.identityHashCode(hr2) + " at 1S", expected, 1, compositeList.getOrCreateComponentList(String.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Integer.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Double.class));
    assertElementCount(null, expected, 1, compositeList.getOrCreateComponentList(Boolean.class));

    final String p3 = Strings.getRandomAlpha(6);
    expected.add(p3);
    compositeList.getOrCreateComponentList(String.class).add(p3);
    assertElementCount("Add " + System.identityHashCode(p3), expected, 2, compositeList.getOrCreateComponentList(String.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Integer.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Double.class));
    assertElementCount(null, expected, 1, compositeList.getOrCreateComponentList(Boolean.class));

    final Double pre2 = Math.random();
    expected.add(1, pre2);
    listIterator = compositeList.listIterator(1);
    listIterator.add(pre2);
    assertElementCount("Remove " + System.identityHashCode(pre2) + " at 1S", expected, 2, compositeList.getOrCreateComponentList(String.class));
    assertElementCount(null, expected, 0, compositeList.getOrCreateComponentList(Integer.class));
    assertElementCount(null, expected, 1, compositeList.getOrCreateComponentList(Double.class));
    assertElementCount(null, expected, 1, compositeList.getOrCreateComponentList(Boolean.class));
  }

  public void testClone(final CompositeList<Object,Class<?>> list) {
    final CompositeList<Object,Class<?>> clone = list.clone();
    assertEquals(list.size(), clone.size());
    final ListIterator listIterator = list.listIterator();
    final ListIterator cloneIterator = clone.listIterator();
    while (listIterator.hasNext()) {
      final Object listItem = listIterator.next();
      final Object cloneItem = cloneIterator.next();
      assertEquals(listItem, cloneItem);
      assertNotSame(listItem, cloneItem);
    }
  }
}