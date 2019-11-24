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

import java.util.Comparator;
import java.util.List;

/**
 * Utility class providing algorithms for sorting matched lists and arrays.
 */
public final class Matched {
  /** Maximum length a list or array can be for recursive swaps. */
  private static final int MAX_RECURSIONS = 5000;

  private static int[] buildIndex(final int len) {
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

  private static void sort0(final Object[] data, final int[] idx, final IntComparator c) {
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

  private static void sort0(final List<?> data, final int[] idx, final IntComparator c) {
    IntTimSort.sort(idx, 0, idx.length, c, null, 0, 0);
    swap(data, idx);
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
    sort(data, order, null);
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
   * @param comparator The comparator to use.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final int[] order, final IntComparator comparator) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    final IntComparator c = comparator != null ? comparator : Integer::compare;
    sort0(data, buildIndex(order.length), new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return c.compare(order[o1], order[o2]);
      }
    });
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
    sort(data, order, null);
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
   * @param comparator The comparator to use.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.size() != order.length}.
   */
  public static void sort(final List<?> data, final int[] order, final IntComparator comparator) {
    if (data.size() != order.length)
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.length [" + order.length + "] must be equal");

    final IntComparator c = comparator != null ? comparator : Integer::compare;
    sort0(data, buildIndex(order.length), new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return c.compare(order[o1], order[o2]);
      }
    });
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
    sort(data, order, null);
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
   * @param comparator The comparator to use.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.size()}.
   */
  public static void sort(final Object[] data, final IntList order, final IntComparator comparator) {
    if (data.length != order.size())
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.size() [" + order.size() + "] must be equal");

    final IntComparator c = comparator != null ? comparator : Integer::compare;
    sort0(data, buildIndex(order.size()), new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return c.compare(order.get(o1), order.get(o2));
      }
    });
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
    sort(data, order, null);
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
   * @param comparator The comparator to use.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.size() != order.size()}.
   */
  public static void sort(final List<?> data, final IntList order, final IntComparator comparator) {
    if (data.size() != order.size())
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.size() [" + order.size() + "] must be equal");

    final IntComparator c = comparator != null ? comparator : Integer::compare;
    sort0(data, buildIndex(order.size()), new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return c.compare(order.get(o1), order.get(o2));
      }
    });
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
    sort(data, order, null);
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
   * @param comparator The comparator to use.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final long[] order, final LongComparator comparator) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    final LongComparator c = comparator != null ? comparator : Long::compare;
    sort0(data, buildIndex(order.length), new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return c.compare(order[o1], order[o2]);
      }
    });
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
    sort(data, order, null);
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
   * @param comparator The comparator to use.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.size() != order.length}.
   */
  public static void sort(final List<?> data, final long[] order, final LongComparator comparator) {
    if (data.size() != order.length)
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.length [" + order.length + "] must be equal");

    final LongComparator c = comparator != null ? comparator : Long::compare;
    sort0(data, buildIndex(order.length), new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return c.compare(order[o1], order[o2]);
      }
    });
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
    sort(data, order, null);
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
   * @param comparator The comparator to use.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.size()}.
   */
  public static void sort(final Object[] data, final LongList order, final LongComparator comparator) {
    if (data.length != order.size())
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.size() [" + order.size() + "] must be equal");

    final LongComparator c = comparator != null ? comparator : Long::compare;
    sort0(data, buildIndex(order.size()), new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return c.compare(order.get(o1), order.get(o2));
      }
    });
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
    sort(data, order, null);
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
   * @param comparator The comparator to use.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.size() != order.size()}.
   */
  public static void sort(final List<?> data, final LongList order, final LongComparator comparator) {
    if (data.size() != order.size())
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.size() [" + order.size() + "] must be equal");

    final LongComparator c = comparator != null ? comparator : Long::compare;
    sort0(data, buildIndex(order.size()), new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return c.compare(order.get(o1), order.get(o2));
      }
    });
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
    sort(data, order, null);
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
   * @param comparator The comparator to use.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final double[] order, final DoubleComparator comparator) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    final DoubleComparator c = comparator != null ? comparator : Double::compare;
    sort0(data, buildIndex(order.length), new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return c.compare(order[o1], order[o2]);
      }
    });
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
    sort(data, order, null);
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
   * @param comparator The comparator to use.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.size() != order.length}.
   */
  public static void sort(final List<?> data, final double[] order, final DoubleComparator comparator) {
    if (data.size() != order.length)
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.length [" + order.length + "] must be equal");

    final DoubleComparator c = comparator != null ? comparator : Double::compare;
    sort0(data, buildIndex(order.length), new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return c.compare(order[o1], order[o2]);
      }
    });
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
    sort(data, order, null);
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
   * @param comparator The comparator to use.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.size()}.
   */
  public static void sort(final Object[] data, final DoubleList order, final DoubleComparator comparator) {
    if (data.length != order.size())
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.size() [" + order.size() + "] must be equal");

    final DoubleComparator c = comparator != null ? comparator : Double::compare;
    sort0(data, buildIndex(order.size()), new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return c.compare(order.get(o1), order.get(o2));
      }
    });
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
    sort(data, order, null);
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
   * @param comparator The comparator to use.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.size() != order.size()}.
   */
  public static void sort(final List<?> data, final DoubleList order, final DoubleComparator comparator) {
    if (data.size() != order.size())
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.size() [" + order.size() + "] must be equal");

    final DoubleComparator c = comparator != null ? comparator : Double::compare;
    sort0(data, buildIndex(order.size()), new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return c.compare(order.get(o1), order.get(o2));
      }
    });
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

    sort0(data, buildIndex(order.size()), new IntComparator() {
      @Override
      @SuppressWarnings({"rawtypes", "unchecked"})
      public int compare(final int o1, final int o2) {
        final Comparable c1 = order.get(o1);
        final Comparable c2 = order.get(o2);
        return c1 == null ? (c2 == null ? 0 : -1) : (c2 == null ? 1 : c1.compareTo(c2));
      }
    });
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

    sort0(data, buildIndex(order.size()), new IntComparator() {
      @Override
      @SuppressWarnings({"rawtypes", "unchecked"})
      public int compare(final int o1, final int o2) {
        final Comparable c1 = order.get(o1);
        final Comparable c2 = order.get(o2);
        return c1 == null ? (c2 == null ? 0 : -1) : (c2 == null ? 1 : c1.compareTo(c2));
      }
    });
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

    sort0(data, buildIndex(order.size()), new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return comparator.compare(order.get(o1), order.get(o2));
      }
    });
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

    sort0(data, buildIndex(order.size()), new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return comparator.compare(order.get(o1), order.get(o2));
      }
    });
  }

  private Matched() {
  }
}