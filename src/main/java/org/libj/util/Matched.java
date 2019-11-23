/* Copyright (c) 2019 LibJ
 *
 * Permission is hereby granted, final free of charge, final to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), final to deal
 * in the Software without restriction, final including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, final and/or sell
 * copies of the Software, final and to permit persons to whom the Software is
 * furnished to do so, final subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.libj.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Utility class providing algorithms for sorting matched lists and arrays.
 */
public final class Matched {
  private static final int MAX_RECURSIONS = 5000;

  private static int[] buildIndex(final int len) {
    final int[] idx = new int[len];
    for (int i = 0; i < len; ++i)
      idx[i] = i;

    return idx;
  }

  @SuppressWarnings("unchecked")
  private static <T>void swap(final T[] data, final int[] idx, final int i) {
    if (idx.length < MAX_RECURSIONS) {
      recurse(data, idx, i);
    }
    else {
      final T[] tmp = (T[])Array.newInstance(data.getClass().getComponentType(), idx.length);
      for (int j = 0; j < idx.length; ++j)
        tmp[j] = data[idx[j]];

      for (int j = 0; j < idx.length; ++j)
        data[j] = tmp[j];
    }
  }

  private static <T>void recurse(final T[] data, final int[] idx, final int i) {
    if (i == idx.length)
      return;

    final T o = data[idx[i]];
    recurse(data, idx, i + 1);
    data[i] = o;
  }

  private static <T>void swap(final List<T> data, final int[] idx, final int i) {
    if (idx.length < MAX_RECURSIONS) {
      recurse(data, idx, i);
    }
    else {
      final List<T> tmp = new ArrayList<>(idx.length);
      for (int j = 0; j < idx.length; ++j)
        tmp.add(data.get(idx[j]));

      for (int j = 0; j < idx.length; ++j)
        data.set(j, tmp.get(j));
    }
  }

  private static <T>void recurse(final List<T> data, final int[] idx, final int i) {
    if (i == idx.length)
      return;

    final T o = data.get(idx[i]);
    recurse(data, idx, i + 1);
    data.set(i, o);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * array in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The array providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final int[] order) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    final int[] idx = buildIndex(order.length);
    IntTimSort.sort(idx, 0, idx.length, new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return Integer.compare(order[o1], order[o2]);
      }
    }, null, 0, 0);

    swap(data, idx, 0);
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of
   * the array in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The {@link List} providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.size() != order.length}.
   */
  public static void sort(final List<?> data, final int[] order) {
    if (data.size() != order.length)
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.length [" + order.length + "] must be equal");

    final int[] idx = buildIndex(order.length);
    IntTimSort.sort(idx, 0, idx.length, new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return Integer.compare(order[o1], order[o2]);
      }
    }, null, 0, 0);

    swap(data, idx, 0);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * {@link IntList} in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The array providing the data.
   * @param order The {@link IntList} providing the order of indices to sort
   *          {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.size()}.
   */
  public static void sort(final Object[] data, final IntList order) {
    if (data.length != order.size())
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.size() [" + order.size() + "] must be equal");

    final int[] idx = buildIndex(order.size());
    IntTimSort.sort(idx, 0, idx.length, new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return Integer.compare(order.get(o1), order.get(o2));
      }
    }, null, 0, 0);

    swap(data, idx, 0);
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of
   * the {@link IntList} in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The {@link List} providing the data.
   * @param order The {@link IntList} providing the order of indices to sort {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.size() != order.size()}.
   */
  public static void sort(final List<?> data, final IntList order) {
    if (data.size() != order.size())
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.size() [" + order.size() + "] must be equal");

    final int[] idx = buildIndex(order.size());
    IntTimSort.sort(idx, 0, idx.length, new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return Integer.compare(order.get(o1), order.get(o2));
      }
    }, null, 0, 0);

    swap(data, idx, 0);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * array in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The array providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final long[] order) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    final int[] idx = buildIndex(order.length);
    IntTimSort.sort(idx, 0, idx.length, new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return Long.compare(order[o1], order[o2]);
      }
    }, null, 0, 0);

    swap(data, idx, 0);
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of
   * the array in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The {@link List} providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.size() != order.length}.
   */
  public static void sort(final List<?> data, final long[] order) {
    if (data.size() != order.length)
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.length [" + order.length + "] must be equal");

    final int[] idx = buildIndex(order.length);
    IntTimSort.sort(idx, 0, idx.length, new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return Long.compare(order[o1], order[o2]);
      }
    }, null, 0, 0);

    swap(data, idx, 0);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * {@link LongList} in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The array providing the data.
   * @param order The {@link LongList} providing the order of indices to sort
   *          {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.size()}.
   */
  public static void sort(final Object[] data, final LongList order) {
    if (data.length != order.size())
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.size() [" + order.size() + "] must be equal");

    final int[] idx = buildIndex(order.size());
    IntTimSort.sort(idx, 0, idx.length, new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return Long.compare(order.get(o1), order.get(o2));
      }
    }, null, 0, 0);

    swap(data, idx, 0);
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of
   * the {@link LongList} in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The {@link List} providing the data.
   * @param order The {@link LongList} providing the order of indices to sort
   *          {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.size() != order.size()}.
   */
  public static void sort(final List<?> data, final LongList order) {
    if (data.size() != order.size())
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.size() [" + order.size() + "] must be equal");

    final int[] idx = buildIndex(order.size());
    IntTimSort.sort(idx, 0, idx.length, new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return Long.compare(order.get(o1), order.get(o2));
      }
    }, null, 0, 0);

    swap(data, idx, 0);
  }


  /**
   * Sorts the array in the first argument matching the sorted order of the
   * array in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The array providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final double[] order) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    final int[] idx = buildIndex(order.length);
    IntTimSort.sort(idx, 0, idx.length, new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return Double.compare(order[o1], order[o2]);
      }
    }, null, 0, 0);

    swap(data, idx, 0);
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of
   * the array in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The {@link List} providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.size() != order.length}.
   */
  public static void sort(final List<?> data, final double[] order) {
    if (data.size() != order.length)
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.length [" + order.length + "] must be equal");

    final int[] idx = buildIndex(order.length);
    IntTimSort.sort(idx, 0, idx.length, new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return Double.compare(order[o1], order[o2]);
      }
    }, null, 0, 0);

    swap(data, idx, 0);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * {@link DoubleList} in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The array providing the data.
   * @param order The {@link DoubleList} providing the order of indices to sort
   *          {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.size()}.
   */
  public static void sort(final Object[] data, final DoubleList order) {
    if (data.length != order.size())
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.size() [" + order.size() + "] must be equal");

    final int[] idx = buildIndex(order.size());
    IntTimSort.sort(idx, 0, idx.length, new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return Double.compare(order.get(o1), order.get(o2));
      }
    }, null, 0, 0);

    swap(data, idx, 0);
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of
   * the {@link DoubleList} in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The {@link List} providing the data.
   * @param order The {@link DoubleList} providing the order of indices to sort
   *          {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.size() != order.size()}.
   */
  public static void sort(final List<?> data, final DoubleList order) {
    if (data.size() != order.size())
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.size() [" + order.size() + "] must be equal");

    final int[] idx = buildIndex(order.size());
    IntTimSort.sort(idx, 0, idx.length, new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return Double.compare(order.get(o1), order.get(o2));
      }
    }, null, 0, 0);

    swap(data, idx, 0);
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of
   * the {@link List} of {@link Comparable} objects in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param <T> The type parameter for the {@link Comparable} objects of
   *          {@code order}.
   * @param data The {@link List} providing the data.
   * @param order The {@link List} of {@link Comparable} objects providing the
   *          order of indices to sort {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.size() != order.size()}.
   */
  public static <T extends Comparable<? super T>>void sort(final List<?> data, final List<T> order) {
    if (data.size() != order.size())
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.size() [" + order.size() + "] must be equal");

    final int[] idx = buildIndex(order.size());
    IntTimSort.sort(idx, 0, idx.length, new IntComparator() {
      @Override
      @SuppressWarnings({"rawtypes", "unchecked"})
      public int compare(final int o1, final int o2) {
        final Comparable c1 = order.get(o1);
        final Comparable c2 = order.get(o2);
        return c1 == null ? (c2 == null ? 0 : -1) : (c2 == null ? 1 : c1.compareTo(c2));
      }
    }, null, 0, 0);

    swap(data, idx, 0);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * {@link List} of {@link Comparable} objects in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param <T> The type parameter for the {@link Comparable} objects of
   *          {@code order}.
   * @param data The array providing the data.
   * @param order The {@link List} of {@link Comparable} objects providing the
   *          order of indices to sort {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.size()}.
   */
  public static <T extends Comparable<? super T>>void sort(final Object[] data, final List<T> order) {
    if (data.length != order.size())
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.size() [" + order.size() + "] must be equal");

    final int[] idx = buildIndex(order.size());
    IntTimSort.sort(idx, 0, idx.length, new IntComparator() {
      @Override
      @SuppressWarnings({"rawtypes", "unchecked"})
      public int compare(final int o1, final int o2) {
        final Comparable c1 = order.get(o1);
        final Comparable c2 = order.get(o2);
        return c1 == null ? (c2 == null ? 0 : -1) : (c2 == null ? 1 : c1.compareTo(c2));
      }
    }, null, 0, 0);

    swap(data, idx, 0);
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of
   * the {@link List} in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param <T> The type parameter for the {@link Comparable} objects of
   *          {@code order}.
   * @param data The {@link List} providing the data.
   * @param order The {@link List} providing the order of indices to sort
   *          {@code data}.
   * @param comparator The {@link Comparator} for members of {@code order}.
   * @throws NullPointerException If {@code data}, {@code order}, or
   *           {@code comparator} is null.
   * @throws IllegalArgumentException If {@code data.size() != order.size()}.
   */
  public static <T>void sort(final List<?> data, final List<T> order, final Comparator<T> comparator) {
    if (data.size() != order.size())
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.size() [" + order.size() + "] must be equal");

    final int[] idx = buildIndex(order.size());
    IntTimSort.sort(idx, 0, idx.length, new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return comparator.compare(order.get(o1), order.get(o2));
      }
    }, null, 0, 0);

    swap(data, idx, 0);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * {@link List} in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param <T> The type parameter for the {@link Comparable} objects of
   *          {@code order}.
   * @param data The array providing the data.
   * @param order The {@link List} providing the order of indices to sort
   *          {@code data}.
   * @param comparator The {@link Comparator} for members of {@code order}.
   * @throws NullPointerException If {@code data}, {@code order}, or
   *           {@code comparator} is null.
   * @throws IllegalArgumentException If {@code data.length != order.size()}.
   */
  public static <T>void sort(final Object[] data, final List<T> order, final Comparator<T> comparator) {
    if (data.length != order.size())
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.size() [" + order.size() + "] must be equal");

    final int[] idx = buildIndex(order.size());
    IntTimSort.sort(idx, 0, idx.length, new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return comparator.compare(order.get(o1), order.get(o2));
      }
    }, null, 0, 0);

    swap(data, idx, 0);
  }

  private Matched() {
  }
}