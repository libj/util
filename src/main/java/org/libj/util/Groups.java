/* Copyright (c) 2014 LibJ
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

import java.lang.reflect.Array;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

/**
 * Utility class providing functions related to
 * <a href="https://en.wikipedia.org/wiki/Group_theory">Group Theory</a>, such
 * as <a href="https://en.wikipedia.org/wiki/Combination">combinations</a>,
 * and <a href="https://en.wikipedia.org/wiki/Permutation">permutations</a>.
 */
public final class Groups {
  /**
   * Combines all subsets of elements in the specified 2-dimensional array,
   * where:
   * <ul>
   * <li>{@code n} = the total number of elements in the 2-dimensional
   * array.</li>
   * <li>{@code r} = the length of the first dimension of the 2-dimensional
   * array.</li>
   * </ul>
   * Time Complexity: {@code O(n choose r)}
   *
   * @param <T> The component type of the array.
   * @param a The 2-dimensional array.
   * @return A 2-dimensional array of combination sets for {@code a}.
   * @throws ArrayIndexOutOfBoundsException If {@code a.length == 0}.
   * @throws NullPointerException If {@code a} or any array member of {@code a}
   *           is null.
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

  @SafeVarargs
  public static <T>void permute(final Consumer<T[]> consumer, final T ... elements) {
    recursePermute(consumer, elements.length, elements);
  }

  private static <T>void swap(final T[] input, final int a, final int b) {
    final T tmp = input[a];
    input[a] = input[b];
    input[b] = tmp;
  }

  @SafeVarargs
  private static <T>void recursePermute(final Consumer<T[]> consumer, final int n, final T ... elements) {
    if (n == 1) {
      consumer.accept(elements);
    }
    else {
      for (int i = 0; i < n - 1; ++i) {
        recursePermute(consumer, n - 1, elements);
        swap(elements, n % 2 == 0 ? i : 0, n - 1);
      }

      recursePermute(consumer, n - 1, elements);
    }
  }

  @SafeVarargs
  public static <T>void permute(final ObjIntConsumer<T[]> consumer, final int k, final T ... elements) {
    permute(consumer, 0, k, elements);
  }

  private static <T>void permute(final ObjIntConsumer<T[]> consumer, final int n, final int k, final T[] elements) {
    if (n == k) {
      consumer.accept(elements, k);
      return;
    }

    for (int i = n; i < elements.length; ++i) {
      swap(elements, n, i);
      permute(consumer, n + 1, k, elements);
      swap(elements, n, i);
    }
  }

  private Groups() {
  }
}