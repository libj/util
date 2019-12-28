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

package org.libj.util.primitive;

import java.util.List;

/**
 * Utility class providing algorithms for sorting paired lists and arrays.
 */
public abstract class PrimitiveSort {
  /** Maximum length a list or array can be for recursive swaps. */
  private static final int MAX_RECURSIONS = 5000;

  protected static int[] buildIndex(final int len) {
    final int[] idx = new int[len];
    for (int i = 0; i < len; ++i)
      idx[i] = i;

    return idx;
  }

  private static <T>void recurse(final T[] data, final int[] idx, final int i) {
    if (i == idx.length)
      return;

    final T obj = data[idx[i]];
    recurse(data, idx, i + 1);
    data[i] = obj;
  }

  @SuppressWarnings("unchecked")
  private static <T>void swap(final T[] data, final int[] idx) {
    final int len = idx.length;
    if (len < MAX_RECURSIONS) {
      recurse(data, idx, 0);
    }
    else {
      final Object[] tmp = new Object[len];
      for (int i = 0; i < len; ++i) {
        tmp[i] = data[idx[i]];
      }

      for (int i = 0; i < len; ++i) {
        data[i] = (T)tmp[i];
        tmp[i] = null;
      }
    }
  }

  private static <T>void recurse(final List<T> data, final int[] idx, final int i) {
    if (i == idx.length)
      return;

    final T obj = data.get(idx[i]);
    recurse(data, idx, i + 1);
    data.set(i, obj);
  }

  @SuppressWarnings("unchecked")
  private static <T>void swap(final List<T> data, final int[] idx) {
    final int len = idx.length;
    if (len < MAX_RECURSIONS) {
      recurse(data, idx, 0);
    }
    else {
      final Object[] tmp = new Object[len];
      for (int i = 0; i < len; ++i) {
        tmp[i] = data.get(idx[i]);
      }

      for (int i = 0; i < len; ++i) {
        data.set(i, (T)tmp[i]);
        tmp[i] = null;
      }
    }
  }

  /**
   * Sorts the specified array of {@code byte}s, according to the provided
   * {@link ByteComparator}.
   *
   * @param a The array of {@code byte}s.
   * @param fromIndex The index of the first element, inclusive, to be sorted.
   * @param toIndex The index of the last element, exclusive, to be sorted.
   * @param c The comparator to use.
   * @throws NullPointerException If {@code a} or {@code c} is null.
   */
  protected static void sort(final byte[] a, final int fromIndex, final int toIndex, final ByteComparator c) {
    ByteTimSort.sort(a, fromIndex, toIndex, c, null, 0, 0);
  }

  /**
   * Sorts the specified array of {@code char}s, according to the provided
   * {@link CharComparator}.
   *
   * @param a The array of {@code char}s.
   * @param fromIndex The index of the first element, inclusive, to be sorted.
   * @param toIndex The index of the last element, exclusive, to be sorted.
   * @param c The comparator to use.
   * @throws NullPointerException If {@code a} or {@code c} is null.
   */
  protected static void sort(final char[] a, final int fromIndex, final int toIndex, final CharComparator c) {
    CharTimSort.sort(a, fromIndex, toIndex, c, null, 0, 0);
  }

  /**
   * Sorts the specified array of {@code short}s, according to the provided
   * {@link ShortComparator}.
   *
   * @param a The array of {@code short}s.
   * @param fromIndex The index of the first element, inclusive, to be sorted.
   * @param toIndex The index of the last element, exclusive, to be sorted.
   * @param c The comparator to use.
   * @throws NullPointerException If {@code a} or {@code c} is null.
   */
  protected static void sort(final short[] a, final int fromIndex, final int toIndex, final ShortComparator c) {
    ShortTimSort.sort(a, fromIndex, toIndex, c, null, 0, 0);
  }

  /**
   * Sorts the specified array of {@code int}s, according to the provided
   * {@link IntComparator}.
   *
   * @param a The array of {@code int}s.
   * @param fromIndex The index of the first element, inclusive, to be sorted.
   * @param toIndex The index of the last element, exclusive, to be sorted.
   * @param c The comparator to use.
   * @throws NullPointerException If {@code a} or {@code c} is null.
   */
  protected static void sort(final int[] a, final int fromIndex, final int toIndex, final IntComparator c) {
    IntTimSort.sort(a, fromIndex, toIndex, c, null, 0, 0);
  }

  /**
   * Sorts the specified array of {@code long}s, according to the provided
   * {@link LongComparator}.
   *
   * @param a The array of {@code long}s.
   * @param fromIndex The index of the first element, inclusive, to be sorted.
   * @param toIndex The index of the last element, exclusive, to be sorted.
   * @param c The comparator to use.
   * @throws NullPointerException If {@code a} or {@code c} is null.
   */
  protected static void sort(final long[] a, final int fromIndex, final int toIndex, final LongComparator c) {
    LongTimSort.sort(a, fromIndex, toIndex, c, null, 0, 0);
  }

  /**
   * Sorts the specified array of {@code float}s, according to the provided
   * {@link FloatComparator}.
   *
   * @param a The array of {@code float}s.
   * @param fromIndex The index of the first element, inclusive, to be sorted.
   * @param toIndex The index of the last element, exclusive, to be sorted.
   * @param c The comparator to use.
   * @throws NullPointerException If {@code a} or {@code c} is null.
   */
  protected static void sort(final float[] a, final int fromIndex, final int toIndex, final FloatComparator c) {
    FloatTimSort.sort(a, fromIndex, toIndex, c, null, 0, 0);
  }

  /**
   * Sorts the specified array of {@code double}s, according to the provided
   * {@link DoubleComparator}.
   *
   * @param a The array of {@code double}s.
   * @param fromIndex The index of the first element, inclusive, to be sorted.
   * @param toIndex The index of the last element, exclusive, to be sorted.
   * @param c The comparator to use.
   * @throws NullPointerException If {@code a} or {@code c} is null.
   */
  protected static void sort(final double[] a, final int fromIndex, final int toIndex, final DoubleComparator c) {
    DoubleTimSort.sort(a, fromIndex, toIndex, c, null, 0, 0);
  }

  protected static void sortIndexed(final Object[] data, final int[] idx, final IntComparator c) {
    IntTimSort.sort(idx, 0, idx.length, c, null, 0, 0);
    swap(data, idx);
  }

  protected static void sortIndexed(final List<?> data, final int[] idx, final IntComparator c) {
    IntTimSort.sort(idx, 0, idx.length, c, null, 0, 0);
    swap(data, idx);
  }

  protected static void sortPaired(final Object[] data, final byte[] order, final int fromIndex, final int toIndex, final ByteComparator comparator) {
    BytePairedTimSort.sort(order, data, fromIndex, toIndex, comparator, null, 0, 0);
  }

  protected static void sortPaired(final Object[] data, final char[] order, final int fromIndex, final int toIndex, final CharComparator comparator) {
    CharPairedTimSort.sort(order, data, fromIndex, toIndex, comparator, null, 0, 0);
  }

  protected static void sortPaired(final Object[] data, final short[] order, final int fromIndex, final int toIndex, final ShortComparator comparator) {
    ShortPairedTimSort.sort(order, data, fromIndex, toIndex, comparator, null, 0, 0);
  }

  protected static void sortPaired(final Object[] data, final int[] order, final int fromIndex, final int toIndex, final IntComparator comparator) {
    IntPairedTimSort.sort(order, data, fromIndex, toIndex, comparator, null, 0, 0);
  }

  protected static void sortPaired(final Object[] data, final long[] order, final int fromIndex, final int toIndex, final LongComparator comparator) {
    LongPairedTimSort.sort(order, data, fromIndex, toIndex, comparator, null, 0, 0);
  }

  protected static void sortPaired(final Object[] data, final float[] order, final int fromIndex, final int toIndex, final FloatComparator comparator) {
    FloatPairedTimSort.sort(order, data, fromIndex, toIndex, comparator, null, 0, 0);
  }

  protected static void sortPaired(final Object[] data, final double[] order, final int fromIndex, final int toIndex, final DoubleComparator comparator) {
    DoublePairedTimSort.sort(order, data, fromIndex, toIndex, comparator, null, 0, 0);
  }

  protected PrimitiveSort() {
  }
}