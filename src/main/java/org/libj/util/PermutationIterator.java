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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * This iterator creates permutations of an input collection, using the
 * <a href="https://en.wikipedia.org/wiki/Steinhaus%E2%80%93Johnson%E2%80%93Trotter_algorithm">Steinhaus-Johnson-Trotter
 * algorithm</a>.
 * <p>
 * The iterator will return exactly {@code n!} permutations of the input collection. The {@link #remove()} operation is not
 * supported, and will throw an {@link UnsupportedOperationException}.
 * <p>
 * NOTE: In case of an empty provided collection, the iterator will return exactly one empty list as result, as {@code 0! = 1}.
 *
 * @param <E> The type parameter of the objects being permuted.
 */
public class PermutationIterator<E> implements Iterable<ArrayList<E>>, Iterator<ArrayList<E>> {
  private final int size, size1;

  /** Permutation is done on theses keys to handle equal objects. */
  private final Integer[] keys;

  /** Mapping between keys and objects. */
  private final Map<Integer,E> objectMap;

  /**
   * Direction table used in the algorithm:
   * <ul>
   * <li>false is left</li>
   * <li>true is right</li>
   * </ul>
   */
  private final int[] directions;

  /** Next permutation to return. When a permutation is requested this instance is provided and the next one is computed. */
  private ArrayList<E> next;

  /**
   * Creates a new {@link PermutationIterator} with the provided {@code E[]} array for which to generate permutations.
   *
   * @param a The array for which to generate permutations.
   * @throws NullPointerException If {@code a} is null.
   */
  @SafeVarargs
  public PermutationIterator(final E ... a) {
    this(a, 0, a.length);
  }

  /**
   * Creates a new {@link PermutationIterator} with the provided {@code E[]} array for which to generate permutations.
   *
   * @param a The array for which to generate permutations.
   * @param fromIndex The index of elements within the provided array from which to generate permutations (inclusive).
   * @param toIndex The index of elements within the provided array to which to generate permutations (exclusive).
   * @throws NullPointerException If {@code a} is null.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex} is less than {@code 0}, or if {@code toIndex} is greater than the
   *           length of the provided array.
   * @throws IllegalArgumentException If {@code fromIndex} is greater than {@code toIndex}.
   */
  public PermutationIterator(final E[] a, final int fromIndex, final int toIndex) {
    this(CollectionUtil.asCollection(new ArrayList<>(toIndex - fromIndex), a, fromIndex, toIndex));
    Integer k = 0;
    for (int i = fromIndex; i < toIndex; ++i)
      objectMap.put(keys[k] = ++k, a[i]);
  }

  /**
   * Creates a new {@link PermutationIterator} with the provided {@link Collection} for which to generate permutations.
   *
   * @param c The collection for which to generate permutations.
   * @throws NullPointerException If {@code c} is null.
   */
  public PermutationIterator(final Collection<? extends E> c) {
    this(new ArrayList<>(c));
    Integer k = 0;
    for (final E e : c)
      objectMap.put(keys[k] = ++k, e);
  }

  private PermutationIterator(final ArrayList<E> next) {
    this.next = next;
    this.size = next.size();
    this.size1 = size - 1;
    this.keys = new Integer[size];
    this.objectMap = new HashMap<>(size);
    this.directions = new int[size];
    Arrays.fill(directions, -1);
  }

  /**
   * Indicates if there are more permutation available.
   *
   * @return {@code true} if there are more permutations, otherwise {@code false}.
   */
  @Override
  public boolean hasNext() {
    return next != null;
  }

  /**
   * Returns the next permutation of the input collection.
   *
   * @return a List of the permutator's elements representing a permutation.
   * @throws NoSuchElementException If there are no more permutations.
   */
  @Override
  public ArrayList<E> next() throws NoSuchElementException {
    if (!hasNext())
      throw new NoSuchElementException();

    // find the largest mobile integer k
    int indexOfLargestMobileInteger = -1;
    int largestKey = -1;
    for (int i = 0; i < size; ++i) {
      final int key = keys[i];
      final int direction = directions[i];
      if (direction == 1 ? i < size1 && key > keys[i + 1] : i > 0 && key > keys[i - 1]) {
        if (key > largestKey) { // NOPMD
          largestKey = key;
          indexOfLargestMobileInteger = i;
        }
      }
    }

    if (largestKey == -1) {
      final ArrayList<E> next = this.next;
      this.next = null;
      return next;
    }

    // swap k and the adjacent integer it is looking at
    final int offset = directions[indexOfLargestMobileInteger];
    final int tmpKey = keys[indexOfLargestMobileInteger];
    keys[indexOfLargestMobileInteger] = keys[indexOfLargestMobileInteger + offset];
    keys[indexOfLargestMobileInteger + offset] = tmpKey;

    final int tmpDirection = directions[indexOfLargestMobileInteger];
    directions[indexOfLargestMobileInteger] = directions[indexOfLargestMobileInteger + offset];
    directions[indexOfLargestMobileInteger + offset] = tmpDirection;

    // reverse the direction of all integers larger than k and build the result
    final ArrayList<E> premutation = new ArrayList<>();
    for (int i = 0; i < size; ++i) {
      if (keys[i] > largestKey)
        directions[i] = -directions[i];

      premutation.add(objectMap.get(keys[i]));
    }

    final ArrayList<E> next = this.next;
    this.next = premutation;
    return next;
  }

  @Override
  public void remove() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("remove() is not supported");
  }

  @Override
  public Iterator<ArrayList<E>> iterator() {
    return this;
  }
}