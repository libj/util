/* Copyright (c) 2014 OpenJAX
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

package org.openjax.standard.util;

import java.lang.reflect.Array;

/**
 * Utility class providing functions for the computation of
 * <a href="https://en.wikipedia.org/wiki/Combination">k-combinations</a>.
 */
public final class Combinations {
  /**
   * Combines all subsets of elements in the specified 2-dimensional array,
   * where:
   * <ul>
   * <li>{@code n} = the total number of elements in the 2-dimensional array.
   * <li>{@code r} = the length of the first dimension of the 2-dimensional
   * array.
   * </ul>
   * Time Complexity: {@code O(n choose r)}
   *
   * @param <T> The component type of the array.
   * @param a The 2-dimensional array.
   * @return A 2-dimensional array of combination sets for {@code a}.
   */
  @SuppressWarnings("unchecked")
  public static <T>T[][] combine(final T[][] a) {
    int total = a[0].length;
    for (int i = 1; i < a.length; ++i)
      total *= a[i].length;

    final Class<?> componentType = a[0].getClass().getComponentType();
    final T[][] combinations = (T[][])Array.newInstance(componentType, total, 0);

    for (; total > 0; --total) {
      final T[] currentSet = (T[])Array.newInstance(componentType, a.length);
      int position = total;

      // Pick the required element from each list, and add it to the set
      for (int i = 0; i < a.length; ++i) {
        final int length = a[i].length;
        currentSet[i] = a[i][position % length];
        position /= length;
      }

      combinations[total - 1] = currentSet;
    }

    return combinations;
  }

  private Combinations() {
  }
}