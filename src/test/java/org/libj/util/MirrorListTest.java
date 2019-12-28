/* Copyright (c) 2016 LibJ
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
import java.util.ConcurrentModificationException;
import java.util.ListIterator;

import org.junit.Ignore;
import org.junit.Test;

public class MirrorListTest {
  private static MirrorList<String,Integer> newList(final ArrayList<String> values, final ArrayList<Integer> reflections) {
    return new MirrorList<>(values, reflections, new MirrorList.Mirror<String,Integer>() {
      @Override
      public Integer valueToReflection(final String value) {
        return Integer.valueOf(value);
      }

      @Override
      public String reflectionToValue(final Integer reflection) {
        return String.valueOf(reflection);
      }
    });
  }

  @Test
  public void test() {
    final MirrorList<String,Integer> list = newList(new ArrayList<>(), new ArrayList<>());

    list.add("1");
    assertTrue(list.getMirrorList().contains(1));

    list.getMirrorList().add(2);
    assertTrue(list.contains("2"));

    final ListIterator<String> stringIterator = list.listIterator();
    stringIterator.next();
    stringIterator.add("3");
    assertTrue(list.getMirrorList().contains(3));

    final ListIterator<Integer> integerIterator = list.getMirrorList().listIterator();
    integerIterator.next();
    integerIterator.next();
    integerIterator.add(7);
    assertTrue(list.contains("7"));

    assertTrue(list.remove("1"));
    assertFalse(list.getMirrorList().contains(1));

    assertEquals(Integer.valueOf(2), list.getMirrorList().remove(2));
    assertFalse(list.contains("2"));

    list.getMirrorList().clear();
    assertEquals(0, list.size());
  }

  @Test
  public void testConcurrentPut() {
    final ArrayList<String> values = new ArrayList<>();
    final ArrayList<Integer> reflections = new ArrayList<>();
    final MirrorList<String,Integer> list = newList(values, reflections);

    list.add("1");
    values.add("0");
    try {
      list.add("2");
      fail("Expected ConcurrentModificationException");
    }
    catch (final ConcurrentModificationException e) {
    }
  }

  @Test
  public void testConcurrentRemove() {
    final ArrayList<String> values = new ArrayList<>();
    final ArrayList<Integer> reflections = new ArrayList<>();
    final MirrorList<String,Integer> list = newList(values, reflections);
    list.add("1");
    list.getMirrorList();
    assertNotNull(reflections.remove(0));
    try {
      list.contains("1");
      fail("Expected ConcurrentModificationException");
    }
    catch (final ConcurrentModificationException e) {
    }
  }

  @Test
  @Ignore("List.set(...) does not increase modCount")
  public void testConcurrentReplace() {
    final ArrayList<String> values = new ArrayList<>();
    final ArrayList<Integer> reflections = new ArrayList<>();
    final MirrorList<String,Integer> list = newList(values, reflections);
    list.add("1");
    values.set(0, "2");
    try {
      list.contains("1");
      fail("Expected ConcurrentModificationException");
    }
    catch (final ConcurrentModificationException e) {
    }
  }
}