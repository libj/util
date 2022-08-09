/* Copyright (c) 2019 LibJ
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

import java.io.Serializable;
import java.util.List;

import org.libj.lang.Strings;

final class DigraphTestUtil {
  /**
   * Create a {@code Digraph<T>} of the type {@code type} with the specified number of vertices and edges in (v, w) sequential
   * linear order.
   *
   * @param <T> The type of elements in the digraph.
   * @param edges The (v, w) pairs of edges in sequential order.
   * @throws IllegalArgumentException If the endpoints of any edge are not in prescribed range.
   * @throws IllegalArgumentException If edges.length is not divisible by 2, or if the input stream is in the wrong format
   * @return The {@code Digraph<T>}.
   */
  @SafeVarargs
  static <T extends Serializable>Digraph<T> createDigraph(final T ... edges) {
    final Digraph<T> digraph = new Digraph<>();
    if (edges.length % 2 != 0)
      throw new IllegalArgumentException("edges array must be (v, w) vertex pairs");

    for (int i = 0; i < edges.length;) // [A]
      digraph.add(edges[i++], edges[i++]);

    assertNotEquals(0, digraph.size());
    return digraph;
  }

  static Digraph<String> createRandomDigraph(final int size, final boolean withNulls) {
    final Digraph<String> digraph = new Digraph<>();
    for (int i = 0; i < size; ++i) // [L]
      digraph.add(withNulls && Math.random() < .2 ? null : Strings.getRandomAlpha(1), Math.random() < .1 ? null : Strings.getRandomAlpha(1));

    return digraph;
  }

  static Digraph<Integer> makeDirectedAcyclicGraph() {
    return createDigraph(2, 3, 0, 6, 0, 1, 2, 0, 11, 12, 9, 12, 9, 10, 9, 11, 3, 5, 8, 7, 5, 4, 0, 5, 6, 4, 6, 9, 7, 6);
  }

  static Digraph<Integer> makeTinyDirectedGraph() {
    return createDigraph(4, 2, 2, 3, 3, 2, 6, 0, 0, 1, 2, 0, 11, 12, 12, 9, 9, 10, 9, 11, 7, 9, 10, 12, 11, 4, 4, 3, 3, 5, 6, 8, 8, 6, 5, 4, 0, 5, 6, 4, 6, 9, 7, 6);
  }

  static Digraph<Integer> makeMediumDirectedGraph() {
    return createDigraph(0, 7, 0, 34, 1, 14, 1, 45, 1, 21, 1, 22, 1, 22, 1, 49, 2, 19, 2, 25, 2, 33, 3, 4, 3, 17, 3, 27, 3, 36, 3, 42, 4, 17, 4, 17, 4, 27, 5, 43, 6, 13, 6, 13, 6, 28, 6, 28, 7, 41, 7, 44, 8, 19, 8, 48, 9, 9, 9, 11, 9, 30, 9, 46, 10, 0, 10, 7, 10, 28, 10, 28, 10, 28, 10, 29, 10, 29, 10, 34, 10, 41, 11, 21, 11, 30, 12, 9, 12, 11, 12, 21, 12, 21, 12, 26, 13, 22, 13, 23, 13, 47, 14, 8, 14, 21, 14, 48, 15, 8, 15, 34, 15, 49, 16, 9, 17, 20, 17, 24, 17, 38, 18, 6, 18, 28, 18, 32, 18, 42, 19, 15, 19, 40, 20, 3, 20, 35, 20, 38, 20, 46, 22, 6, 23, 11, 23, 21, 23, 22, 24, 4, 24, 5, 24, 38, 24, 43, 25, 2, 25, 34, 26, 9, 26, 12, 26, 16, 27, 5, 27, 24, 27, 32, 27, 31, 27, 42, 28, 22, 28, 29, 28, 39, 28, 44, 29, 22, 29, 49, 30, 23, 30, 37, 31, 18, 31, 32, 32, 5, 32, 6, 32, 13, 32, 37, 32, 47, 33, 2, 33, 8, 33, 19, 34, 2, 34, 19, 34, 40, 35, 9, 35, 37, 35, 46, 36, 20, 36, 42, 37, 5, 37, 9, 37, 35, 37, 47, 37, 47, 38, 35, 38, 37, 38, 38, 39, 18, 39, 42, 40, 15, 41, 28, 41, 44, 42, 31, 43, 37, 43, 38, 44, 39, 45, 8, 45, 14, 45, 14, 45, 15, 45, 49, 46, 16, 47, 23, 47, 30, 48, 12, 48, 21, 48, 33, 48, 33, 49, 34, 49, 22, 49, 49);
  }

  static List<Integer> testDirectedCycle(final Digraph<Integer> digraph) {
    final List<Integer> cycle = digraph.getCycle();
    if (cycle != null) {
      verifyCycle(cycle);
      return digraph.getCycle();
    }

    return null;
  }

  // verify the digraph has a directed cycle if it reports one
  static void verifyCycle(final List<Integer> cycle) {
    // verify cycle
    Object first = null;
    Object last = null;
    for (int i = 0, i$ = cycle.size(); i < i$; ++i) { // [L]
      final Object v = cycle.get(i);
      if (first == null)
        first = v;

      last = v;
    }

    if (first != last)
      throw new IllegalStateException("cycle begins with " + first + " and ends with " + last);
  }

  static void assertNotSameOrNull(final Object expected, final Object actual) {
    if (expected == null) {
      assertNull(actual);
    }
    else {
      assertNotNull(actual);
      assertNotSame(expected, actual);
    }
  }

  static void assertEqualClone(final Object expected, final Object actual) {
    assertNotSameOrNull(expected, actual);
    assertEquals(expected, actual);
  }

  static <T>void assertCloneData(final AbstractDigraph<?,?> expected, final AbstractDigraph<?,?> actual) {
    assertEqualClone(expected, actual);
    assertEqualClone(expected.objectToIndex, actual.objectToIndex);
    assertEqualClone(expected.indexToObject, actual.indexToObject);
    assertEqualClone(expected.adjRemoved, actual.adjRemoved);
    assertEqualClone(expected.adj, actual.adj);
    assertEqualClone(expected.inDegree, actual.inDegree);
  }

  static <T>void assertCloneEquals(final Digraph<T> expected, final Digraph<T> actual) {
    assertEqualClone(expected, actual);
    assertCloneData(expected.transverse, actual.transverse);
  }

  private DigraphTestUtil() {
  }
}