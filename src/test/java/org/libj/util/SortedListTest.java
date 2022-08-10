/* Copyright (c) 2012 LibJ
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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

import org.junit.Test;

@SuppressWarnings("rawtypes")
public class SortedListTest {
  @SuppressWarnings("unchecked")
  static final List<Function<List,List>> factories = Arrays.asList(ArrayList::new, LinkedList::new);

  @SafeVarargs
  private static <T> void assertListEquals(final List<T> actual, final T ... expected) {
    assertArrayEquals(actual.toString(), expected, actual.toArray());
  }

  @Test
  public void testConstructorSignature() {
    for (final Function<List,List> factory : factories) { // [L]
      // final SortedList<Object> bad = new SortedList<Object>(new ArrayList<Object>()); // Should not be allowed cause Object is not Comparable
      final SortedList<Object,?> good = new SortedList<>(factory.apply(Collections.EMPTY_LIST), (o1, o2) -> 0);
      good.add(new Object());
      good.add(new Object());
      good.add(new Object());
    }
  }

  @Test
  public void testConstructor() {
    for (final Function<List,List> factory : factories) { // [L]
      final SortedList<String,?> list = new SortedList<>(factory.apply(Arrays.asList("e", "b", "c", "f", "a", "g", "d")));
      assertListEquals(list, "a", "b", "c", "d", "e", "f", "g");
    }
  }

  @Test
  public void testAddAll() {
    for (final Function<List,List> factory : factories) { // [L]
      final SortedList<String,?> list = new SortedList<>(factory.apply(Collections.EMPTY_LIST));
      list.addAll(Arrays.asList("e", "b", "c", "f", "a", "g", "d"));
      assertListEquals(list, "a", "b", "c", "d", "e", "f", "g");
    }
  }

  @Test
  public void testIteratorRemove() {
    for (final Function<List,List> factory : factories) { // [L]
      final SortedList<String,?> list = new SortedList<>(factory.apply(Arrays.asList("e", "b", "c", "f", "a", "g", "d")));
      final ListIterator<String> iterator = list.listIterator();
      assertEquals("a", iterator.next());
      iterator.remove();
      assertListEquals(list, "b", "c", "d", "e", "f", "g");
    }
  }

  @Test
  public void testIteratorSetStartExact() {
    for (final Function<List,List> factory : factories) { // [L]
      final SortedList<String,?> list = new SortedList<>(factory.apply(Arrays.asList("e", "b", "c", "f", "a", "g", "d")));
      final ListIterator<String> iterator = list.listIterator();
      assertEquals("a", iterator.next());
      assertEquals(0, iterator.previousIndex());
      assertEquals(1, iterator.nextIndex());
      iterator.set("0");
      assertEquals(0, iterator.previousIndex());
      assertEquals(1, iterator.nextIndex());
      assertListEquals(list, "0", "b", "c", "d", "e", "f", "g");
    }
  }

  @Test
  public void testIteratorSetEndExact() {
    for (final Function<List,List> factory : factories) { // [L]
      final SortedList<String,?> list = new SortedList<>(factory.apply(Arrays.asList("e", "b", "c", "f", "a", "g", "d")));
      final ListIterator<String> iterator = list.listIterator(list.size());
      assertEquals("g", iterator.previous());
      assertEquals(5, iterator.previousIndex());
      assertEquals(6, iterator.nextIndex());
      iterator.set("z");
      assertEquals(5, iterator.previousIndex());
      assertEquals(6, iterator.nextIndex());
      assertListEquals(list, "a", "b", "c", "d", "e", "f", "z");
    }
  }

  @Test
  public void testIteratorSetStartCorrected() {
    for (final Function<List,List> factory : factories) { // [L]
      final SortedList<String,?> list = new SortedList<>(factory.apply(Arrays.asList("e", "b", "c", "f", "a", "g", "d")));
      final ListIterator<String> iterator = list.listIterator();
      assertEquals("a", iterator.next());
      assertEquals("b", iterator.next());
      assertEquals("c", iterator.next());
      assertEquals(2, iterator.previousIndex());
      assertEquals(3, iterator.nextIndex());
      iterator.set("0");
      assertEquals(2, iterator.previousIndex());
      assertEquals(3, iterator.nextIndex());
      assertListEquals(list, "0", "a", "b", "d", "e", "f", "g");
    }
  }

  @Test
  public void testIteratorSetEndCorrected() {
    for (final Function<List,List> factory : factories) { // [L]
      final SortedList<String,?> list = new SortedList<>(factory.apply(Arrays.asList("e", "b", "c", "f", "a", "g", "d")));
      final ListIterator<String> iterator = list.listIterator(list.size());
      assertEquals("g", iterator.previous());
      assertEquals("f", iterator.previous());
      assertEquals("e", iterator.previous());
      assertEquals(3, iterator.previousIndex());
      assertEquals(4, iterator.nextIndex());
      iterator.set("z");
      assertEquals(3, iterator.previousIndex());
      assertEquals(4, iterator.nextIndex());
      assertListEquals(list, "a", "b", "c", "d", "f", "g", "z");
    }
  }

  @Test
  public void testIteratorAddStartExact() {
    for (final Function<List,List> factory : factories) { // [L]
      final SortedList<String,?> list = new SortedList<>(factory.apply(Arrays.asList("e", "b", "c", "f", "a", "g", "d")));
      final ListIterator<String> iterator = list.listIterator();
      assertEquals(-1, iterator.previousIndex());
      assertEquals(0, iterator.nextIndex());
      iterator.add("0");
      assertEquals(0, iterator.previousIndex());
      assertEquals(1, iterator.nextIndex());
      assertEquals("a", iterator.next());
      assertListEquals(list, "0", "a", "b", "c", "d", "e", "f", "g");
    }
  }

  @Test
  public void testIteratorAddEndExact() {
    for (final Function<List,List> factory : factories) { // [L]
      final SortedList<String,?> list = new SortedList<>(factory.apply(Arrays.asList("e", "b", "c", "f", "a", "g", "d")));
      final ListIterator<String> iterator = list.listIterator(list.size());
      assertFalse(iterator.hasNext());
      assertEquals(6, iterator.previousIndex());
      assertEquals(7, iterator.nextIndex());
      iterator.add("z");
      assertEquals(7, iterator.previousIndex());
      assertEquals(8, iterator.nextIndex());
      assertEquals("z", iterator.previous());
      assertListEquals(list, "a", "b", "c", "d", "e", "f", "g", "z");
    }
  }

  @Test
  public void testIteratorAddStartCorrected() {
    for (final Function<List,List> factory : factories) { // [L]
      final SortedList<String,?> list = new SortedList<>(factory.apply(Arrays.asList("e", "b", "c", "f", "a", "g", "d")));
      final ListIterator<String> iterator = list.listIterator();
      assertEquals("a", iterator.next());
      assertEquals("b", iterator.next());
      assertEquals("c", iterator.next());
      assertEquals(2, iterator.previousIndex());
      assertEquals(3, iterator.nextIndex());
      iterator.add("0");
      assertEquals(3, iterator.previousIndex());
      assertEquals(4, iterator.nextIndex());
      assertListEquals(list, "0", "a", "b", "c", "d", "e", "f", "g");
    }
  }

  @Test
  public void testIteratorAddEndCorrected() {
    for (final Function<List,List> factory : factories) { // [L]
      final SortedList<String,?> list = new SortedList<>(factory.apply(Arrays.asList("e", "b", "c", "f", "a", "g", "d")));
      final ListIterator<String> iterator = list.listIterator(list.size());
      assertEquals("g", iterator.previous());
      assertEquals("f", iterator.previous());
      assertEquals("e", iterator.previous());
      assertEquals(3, iterator.previousIndex());
      assertEquals(4, iterator.nextIndex());
      iterator.add("z");
      assertEquals(4, iterator.previousIndex());
      assertEquals(5, iterator.nextIndex());
      assertListEquals(list, "a", "b", "c", "d", "e", "f", "g", "z");
    }
  }

  @Test
  public void testStory() {
    for (final Function<List,List> factory : factories) { // [L]
      final SortedList<String,?> list = new SortedList<>(factory.apply(Collections.EMPTY_LIST));
      list.add(0, "f");
      assertListEquals(list, "f");
      list.add(1, "b");
      assertListEquals(list, "b", "f");
      list.add(0, "g");
      assertListEquals(list, "b", "f", "g");
      list.set(2, "a");
      assertListEquals(list, "a", "b", "f");
      list.set(0, "g");
      assertListEquals(list, "b", "f", "g");
      list.add("c");
      assertListEquals(list, "b", "c", "f", "g");
      list.add("a");
      assertListEquals(list, "a", "b", "c", "f", "g");
      assertEquals(1, list.indexOf("b"));
      list.add("d");
      assertListEquals(list, "a", "b", "c", "d", "f", "g");
      list.add("e");
      assertListEquals(list, "a", "b", "c", "d", "e", "f", "g");
      list.remove("c");
      assertListEquals(list, "a", "b", "d", "e", "f", "g");
      list.remove(0);
      list.add("d");
      assertListEquals(list, "b", "d", "d", "e", "f", "g");
      list.remove(4);
      list.add("h");
      assertListEquals(list, "b", "d", "d", "e", "g", "h");
      assertEquals(1, list.indexOf("d"));
      assertEquals(2, list.lastIndexOf("d"));
      list.retainAll(Arrays.asList("a", "d", "f", "g", "h"));
      assertListEquals(list, "d", "d", "g", "h");
      list.retainAll(Arrays.asList("a", "d", "d", "d", "h"));
      assertListEquals(list, "d", "d", "h");
      assertEquals(-1, list.indexOf("a"));
      assertEquals(2, list.lastIndexOf("h"));
    }
  }

  private static class Foo implements Comparable<Foo> {
    private int bar;

    private Foo(final int bar) {
      this.bar = bar;
    }

    @Override
    public int compareTo(final Foo o) {
      return bar < o.bar ? -1 : 1;
    }

    @Override
    public String toString() {
      return String.valueOf(bar);
    }
  }

  @Test
  public void testComparable() {
    for (final Function<List,List> factory : factories) { // [L]
      final SortedList<Foo,?> list = new SortedList<>(factory.apply(Collections.EMPTY_LIST));
      list.add(new Foo(1));
      list.add(new Foo(0));
      list.add(new Foo(2));
      list.add(new Foo(4));
      list.add(new Foo(3));
      for (int i = 0, i$ = list.size(); i < i$; ++i) // [N]
        assertEquals(i, list.get(i).bar);
    }
  }
}