/* Copyright (c) 2023 LibJ
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Test;

public class PermutationIteratorTest {
  protected Character[] testArray = {'A', 'B', 'C'};
  protected ArrayList<Character> testList = CollectionUtil.asCollection(new ArrayList<>(), testArray);

  public PermutationIterator<Character> makeEmptyIterator() {
    return new PermutationIterator<>(new ArrayList<Character>());
  }

  public PermutationIterator<Character> makeObject() {
    return new PermutationIterator<>(testList);
  }

  @Test
  public void testPermutationResultSize() {
    int factorial = 1;
    for (int i = 0; i < 8; ++i, factorial *= i) { // [N]
      final ArrayList<Integer> list = new ArrayList<>();
      for (int j = 0; j < i; ++j) // [N]
        list.add(j);

      int count = 0;
      for (final Iterator<ArrayList<Integer>> it = new PermutationIterator<>(list); it.hasNext(); ++count) // [I]
        it.next();

      assertEquals(factorial, count);
    }
  }

  /**
   * Test checking that all the permutations are returned
   */
  @Test
  public void testPermutationExhaustivity() {
    final ArrayList<Character> perm1 = new ArrayList<>();
    final ArrayList<Character> perm2 = new ArrayList<>();
    final ArrayList<Character> perm3 = new ArrayList<>();
    final ArrayList<Character> perm4 = new ArrayList<>();
    final ArrayList<Character> perm5 = new ArrayList<>();
    final ArrayList<Character> perm6 = new ArrayList<>();

    perm1.add('A');
    perm2.add('A');
    perm3.add('B');
    perm4.add('B');
    perm5.add('C');
    perm6.add('C');

    perm1.add('B');
    perm2.add('C');
    perm3.add('A');
    perm4.add('C');
    perm5.add('A');
    perm6.add('B');

    perm1.add('C');
    perm2.add('B');
    perm3.add('C');
    perm4.add('A');
    perm5.add('B');
    perm6.add('A');

    final ArrayList<List<Character>> results = new ArrayList<>();
    for (final PermutationIterator<Character> it = makeObject(); it.hasNext(); results.add(it.next())); // [I]

    // 3! permutation for 3 elements
    assertEquals(6, results.size());
    assertTrue(results.contains(perm1));
    assertTrue(results.contains(perm2));
    assertTrue(results.contains(perm3));
    assertTrue(results.contains(perm4));
    assertTrue(results.contains(perm5));
    assertTrue(results.contains(perm6));
  }

  /**
   * Test checking that all the permutations are returned only once.
   */
  @Test
  public void testPermutationUnicity() {
    final ArrayList<List<Character>> resultsList = new ArrayList<>();
    final Set<List<Character>> resultsSet = new HashSet<>();

    for (final PermutationIterator<Character> it = makeObject(); it.hasNext();) { // [I]
      final ArrayList<Character> permutation = it.next();
      resultsList.add(permutation);
      resultsSet.add(permutation);
    }

    // 3! permutation for 3 elements
    assertEquals(6, resultsList.size());
    assertEquals(6, resultsSet.size());
  }

  @Test
  public void testPermutationException() {
    final ArrayList<List<Character>> resultsList = new ArrayList<>();

    final PermutationIterator<Character> it = makeObject();
    while (it.hasNext())
      resultsList.add(it.next());

    // asking for another permutation should throw an exception
    try {
      it.next();
      fail("Expected NoSuchElementException");
    }
    catch (final NoSuchElementException e) {
    }
  }

  @Test
  public void testPermutatorHasMore() {
    final PermutationIterator<Character> it = makeObject();
    for (int i = 0; i < 6; ++i) { // [N]
      assertTrue(it.hasNext());
      it.next();
    }

    assertFalse(it.hasNext());
  }

  @Test
  public void testEmptyCollection() {
    final PermutationIterator<Character> it = makeEmptyIterator();
    // There is one permutation for an empty set: 0! = 1
    assertTrue(it.hasNext());
    assertEquals(0, it.next().size());
    assertFalse(it.hasNext());
  }
}