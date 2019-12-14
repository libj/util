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

import java.util.List;

import org.libj.util.primitive.IntComparator;

/**
 * Utility class providing algorithms for sorting matched lists and arrays.
 */
public final class MatchedSort {
  /** Maximum length a list or array can be for recursive swaps. */
  private static final int MAX_RECURSIONS = 5000;

  static int[] buildIndex(final int len) {
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

  static void sort0(final Object[] data, final int[] idx, final IntComparator c) {
    IntTimSort.sort(idx, 0, idx.length, c, null, 0, 0);
    swap(data, idx);
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

  static void sort0(final List<?> data, final int[] idx, final IntComparator c) {
    IntTimSort.sort(idx, 0, idx.length, c, null, 0, 0);
    swap(data, idx);
  }

  private MatchedSort() {
  }
}