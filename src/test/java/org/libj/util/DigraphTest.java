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
import static org.libj.util.DigraphTestUtil.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Test;

public class DigraphTest {
  private static final Digraph<String> digraph1 = createDigraph("a", null, null, "c", null, "d", "c", "d", "c", "e", "d", "e", "e", "f", "e", "g", "e", "h", "f", "h");
  private static final Digraph<String> transverse1 = createDigraph(null, "a", "c", null, "d", null, "d", "c", "e", "c", "e", "d", "f", "e", "g", "e", "h", "e", "h", "f");

  private static final Digraph<String> digraph2 = createDigraph("c", "d", "c", "e", "d", "e", "e", "f", "e", "g", "e", "h", "f", "h", "a", null, null, "c", null, "d");
  private static final Digraph<String> transverse2 = createDigraph("d", "c", "e", "c", "e", "d", "f", "e", "g", "e", "h", "e", "h", "f", null, "a", "c", null, "d", null);

  private static final Digraph<String> digraph3 = createDigraph("2", "3", "0", "6", "0", "1", "2", "0", "11", "12", "9", "12", "9", "10", "9", "11", "3", "5", "8", "7", "5", "4", "0", "5", "6", "4", "6", "9", "7", "6");
  private static final Digraph<String> transverse3 = createDigraph("3", "2", "6", "0", "1", "0", "0", "2", "12", "11", "12", "9", "10", "9", "11", "9", "5", "3", "7", "8", "4", "5", "5", "0", "4", "6", "9", "6", "6", "7");

  @Test
  public void testClone() {
    assertEquals(digraph1, digraph1.clone());
    assertEquals(digraph2, digraph2.clone());
    assertEquals(digraph3, digraph3.clone());
    assertEquals(digraph1, digraph2.clone());
    assertEquals(digraph1.clone(), digraph2);
  }

  @Test
  public void testTransverse() {
    assertEquals(transverse1, digraph1.transverse());
    assertEquals(transverse2, digraph2.transverse());
    assertEquals(transverse3, digraph3.transverse());
  }

  @Test
  public void testTopologicalOrder() {
    assertArrayEquals(digraph1.getTopologicalOrder().toString(), new String[] {"a", null, "c", "d", "e", "g", "f", "h"}, digraph1.getTopologicalOrder().toArray());
  }

  @Test
  public void testGetEdges() {
    assertEquals(Collections.singleton(null), digraph1.get("a"));
    assertEquals(CollectionUtil.asCollection(new HashSet<>(), "c", "d"), digraph1.get(null));
    assertEquals(CollectionUtil.asCollection(new HashSet<>(), "d", "e"), digraph1.get("c"));
    assertEquals(Collections.singleton("e"), digraph1.get("d"));
    assertEquals(CollectionUtil.asCollection(new HashSet<>(), "f", "g", "h"), digraph1.get("e"));
    assertEquals(Collections.singleton("h"), digraph1.get("f"));
    assertTrue(digraph1.containsKey("g"));
    assertEquals(0, digraph1.get("g").size());
    assertTrue(digraph1.containsKey("h"));
    assertEquals(0, digraph1.get("h").size());

    assertFalse(digraph1.containsKey("i"));
  }

  private static void testRemoveAdd(final Digraph<String> digraph) {
    final Digraph<String> clone = digraph.clone();
    assertCloneEquals(digraph, clone);
    final Iterator<String> iterator = new ArrayList<>(digraph.keySet()).iterator();
    while (iterator.hasNext()) {
      final List<String> keys = new ArrayList<>();
      final List<Set<String>> forwards = new ArrayList<>();
      final List<Set<String>> reverses = new ArrayList<>();
      while (iterator.hasNext() && Math.random() < 0.5) {
        final String key = iterator.next();
        keys.add(key);
        final Set<String> reverse = digraph.transverse().get(key);
        reverses.add(reverse == null ? null : new HashSet<>(reverse));

        final Set<String> forward = digraph.remove(key);
        forwards.add(forward);
      }

      for (int i = 0, len = keys.size(); i < len; ++i) {
        final String key = keys.get(i);
        addEdges(digraph, key, forwards.get(i), true);
        addEdges(digraph, key, reverses.get(i), false);
      }

      assertEquals(clone, digraph);
    }
  }

  private static boolean addEdges(final Digraph<? super String> digraph, final String key, final Set<String> edges, final boolean forward) {
    if (edges == null || edges.size() == 0)
      return digraph.add(key);

    boolean changed = false;
    for (final String edge : edges) {
      if (forward)
        changed |= digraph.add(key, edge);
      else
        changed |= digraph.add(edge, key);
    }

    return changed;
  }

  @Test
  public void testRemoveAdd() {
    testRemoveAdd(digraph1);
    testRemoveAdd(digraph2);
    testRemoveAdd(digraph3);
    for (int i = 0; i < 1000; ++i)
      testRemoveAdd(createRandomDigraph((int)(Math.random() * 20), true));
  }

  @Test
  public void testInOutDegree() {
    final Digraph<Integer> directedAcyclicDigraph = makeDirectedAcyclicGraph();
    assertEquals(2, directedAcyclicDigraph.getInDegree(12));
    try {
      directedAcyclicDigraph.getInDegree(-1);
      fail("Expected NoSuchElementException");
    }
    catch (final NoSuchElementException e) {
    }

    assertEquals(3, directedAcyclicDigraph.getOutDegree(9));
    try {
      directedAcyclicDigraph.getOutDegree(-1);
      fail("Expected NoSuchElementException");
    }
    catch (final NoSuchElementException e) {
    }
  }

  @Test
  public void testIntegerDirectedGraph() {
    final Digraph<Integer> directedAcyclicDigraph = makeDirectedAcyclicGraph();
    assertNull(testDirectedCycle(directedAcyclicDigraph));
    assertFalse(directedAcyclicDigraph.hasCycle());
    assertArrayEquals(new Integer[] {8, 7, 2, 0, 1, 6, 9, 11, 10, 12, 3, 5, 4}, directedAcyclicDigraph.getTopologicalOrder().toArray());

    final Digraph<Integer> directedTinyDigraph = makeTinyDirectedGraph();
    assertArrayEquals(new Integer[] {3, 2, 3}, testDirectedCycle(directedTinyDigraph).toArray());
    assertTrue(directedTinyDigraph.hasCycle());
    assertNull(directedTinyDigraph.getTopologicalOrder());

    final Digraph<Integer> directedMediumDigraph = makeMediumDirectedGraph();
    assertArrayEquals(new Integer[] {13, 6, 22, 13}, testDirectedCycle(directedMediumDigraph).toArray());
    assertTrue(directedMediumDigraph.hasCycle());
    assertNull(directedMediumDigraph.getTopologicalOrder());
  }

  @Test
  @SuppressWarnings("unlikely-arg-type")
  public void testContainsValue() {
    final Digraph<Integer> digraph = makeDirectedAcyclicGraph();
    assertTrue(digraph.containsValue(Collections.singleton(12)));
    assertTrue(digraph.containsValue(Collections.EMPTY_SET));
    assertTrue(digraph.containsValue(CollectionUtil.asCollection(new HashSet<>(), 12, 11, 10)));
    assertFalse(digraph.containsValue(-1));
  }

  @Test
  public void testClear() {
    final Digraph<Integer> digraph = makeDirectedAcyclicGraph();
    final Digraph<Integer> empty = new Digraph<>();

    assertNotEquals(empty, digraph);
    digraph.clear();
    assertTrue(digraph.isEmpty());
    assertEquals(empty, digraph);
  }

  @Test
  public void testKeySetObservable() {
    final Digraph<Integer> digraph = makeDirectedAcyclicGraph();
    final Set<Integer> w6 = CollectionUtil.asCollection(new HashSet<>(), 4, 9);
    assertEquals(w6, digraph.get(6));
    final Collection<Set<Integer>> values = digraph.values();
    assertTrue(values.contains(Collections.EMPTY_SET));
    assertTrue(values.contains(w6));
    assertFalse(values.contains(Collections.singleton(9)));
    final Set<Integer> keys = digraph.keySet();
    final Set<Map.Entry<Integer,Set<Integer>>> entries = digraph.entrySet();
    assertTrue(keys.remove(4));
    for (final Iterator<Map.Entry<Integer,Set<Integer>>> iterator = entries.iterator(); iterator.hasNext();)
      if (iterator.next().getKey() == 4)
        fail("Expected 4 to be removed");

    assertFalse(keys.contains(4));
    assertFalse(values.contains(w6));
    assertTrue(values.contains(Collections.singleton(9)));
    final Set<Integer> set = digraph.get(6);
    assertEquals(Collections.singleton(9), set);
  }

  @Test
  public void testEntrySetObservable() {
    final Digraph<Integer> digraph = makeDirectedAcyclicGraph();
    final Set<Integer> w6 = CollectionUtil.asCollection(new HashSet<>(), 4, 9);
    assertEquals(w6, digraph.get(6));
    final Collection<Set<Integer>> values = digraph.values();
    assertTrue(values.contains(w6));
    assertFalse(values.contains(Collections.singleton(9)));
    final Set<Integer> keys = digraph.keySet();
    assertTrue(keys.contains(4));
    final Set<Map.Entry<Integer,Set<Integer>>> entries = digraph.entrySet();
    for (final Iterator<Map.Entry<Integer,Set<Integer>>> iterator = entries.iterator(); iterator.hasNext();)
      if (iterator.next().getKey() == 4)
        iterator.remove();

    assertFalse(keys.contains(4));
    assertFalse(values.contains(w6));
    assertTrue(values.contains(Collections.singleton(9)));
    final Set<Integer> set = digraph.get(6);
    assertEquals(Collections.singleton(9), set);
  }

  @Test
  public void testHashCode() {
    final Digraph<Integer> digraph = makeDirectedAcyclicGraph();
    final int hashCode = digraph.hashCode();
    digraph.remove(6);
    assertNotEquals(hashCode, digraph.hashCode());
    digraph.add(6, 4);
    digraph.add(6, 9);
    assertNotEquals(hashCode, digraph.hashCode());
    digraph.add(7, 6);
    assertNotEquals(hashCode, digraph.hashCode());
    digraph.add(0, 6);
    assertEquals(hashCode, digraph.hashCode());
  }
}