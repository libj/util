/* Copyright (c) 2008 lib4j
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

package org.lib4j.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public final class Collections {
  public static String toString(final Collection<?> collection, final char delimiter) {
    if (collection == null)
      return null;

    if (collection.size() == 0)
      return "";

    final StringBuilder string = new StringBuilder();
    final Iterator<?> iterator = collection.iterator();
    string.append(String.valueOf(iterator.next()));
    while (iterator.hasNext())
      string.append(delimiter).append(String.valueOf(iterator.next()));

    return string.toString();
  }

  public static String toString(final Collection<?> collection, final String delimiter) {
    if (collection == null)
      return null;

    if (collection.size() == 0)
      return "";

    final StringBuilder string = new StringBuilder();
    final Iterator<?> iterator = collection.iterator();
    string.append(String.valueOf(iterator.next()));
    while (iterator.hasNext())
      string.append(delimiter).append(String.valueOf(iterator.next()));

    return string.toString();
  }

  /**
   * Searches a range of the specified list for the specified object
   * using the binary search algorithm. The range must be sorted into
   * ascending order according to the
   * {@linkplain Comparable natural ordering} of its elements (as by the
   * {@link #sort(List<T>, int, int)} method) prior to making this
   * call.  If it is not sorted, the results are undefined.
   * (If the range contains elements that are not mutually comparable (for
   * example, strings and integers), it <i>cannot</i> be sorted according
   * to the natural ordering of its elements, hence results are undefined.)
   * If the range contains multiple elements equal to the specified object,
   * there is no guarantee which one will be found.
   *
   * @param a the list to be searched
   * @param fromIndex the index of the first element (inclusive) to be
   *          searched
   * @param toIndex the index of the last element (exclusive) to be searched
   * @param key the value to be searched for
   * @return index of the search key, if it is contained in the list
   *         within the specified range;
   *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>.  The
   *         <i>insertion point</i> is defined as the point at which the
   *         key would be inserted into the list: the index of the first
   *         element in the range greater than the key,
   *         or <tt>toIndex</tt> if all
   *         elements in the range are less than the specified key.  Note
   *         that this guarantees that the return value will be &gt;= 0 if
   *         and only if the key is found.
   * @throws ClassCastException if the search key is not comparable to the
   *         elements of the list within the specified range.
   * @throws IllegalArgumentException
   *         if {@code fromIndex > toIndex}
   * @throws ArrayIndexOutOfBoundsException
   *         if {@code fromIndex < 0 or toIndex > a.length}
   */
  public static <T extends Comparable<? super T>>int binarySearch(final List<T> a, final int fromIndex, final int toIndex, final T key) {
    rangeCheck(a.size(), fromIndex, toIndex);
    return binarySearch0(a, fromIndex, toIndex, key);
  }

  /**
   * Searches the specified list for the specified object using the binary
   * search algorithm. The list must be sorted into ascending order
   * according to the {@linkplain Comparable natural ordering}
   * of its elements (as by the {@link #sort(List<T>)} method) prior to
   * making this call. If it is not sorted, the results are undefined.
   * (If the list contains elements that are not mutually comparable (for
   * example, strings and integers), it <i>cannot</i> be sorted according
   * to the natural ordering of its elements, hence results are undefined.)
   * If the list contains multiple
   * elements equal to the specified object, there is no guarantee which
   * one will be found.
   *
   * @param a the list to be searched
   * @param key the value to be searched for
   * @return index of the search key, if it is contained in the list;
   *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>.  The
   *         <i>insertion point</i> is defined as the point at which the
   *         key would be inserted into the list: the index of the first
   *         element greater than the key, or <tt>a.length</tt> if all
   *         elements in the list are less than the specified key.  Note
   *         that this guarantees that the return value will be &gt;= 0 if
   *         and only if the key is found.
   * @throws ClassCastException if the search key is not comparable to the
   *         elements of the array.
   */
  public static <T extends Comparable<? super T>>int binarySearch(final List<T> a, final T key) {
    return binarySearch0(a, 0, a.size(), key);
  }

  /**
   * Searches a range of the specified list for the specified object using
   * the binary search algorithm. The range must be sorted into ascending
   * order according to the specified comparator (as by the
   * {@link #sort(List<T>, int, int, Comparator)
   * sort(List<T>, int, int, Comparator)} method) prior to making this call.
   * If it is not sorted, the results are undefined.
   * If the range contains multiple elements equal to the specified object,
   * there is no guarantee which one will be found.
   *
   * @param <T> the class of the objects in the list
   * @param a the list to be searched
   * @param fromIndex the index of the first element (inclusive) to be
   *          searched
   * @param toIndex the index of the last element (exclusive) to be searched
   * @param key the value to be searched for
   * @param c the comparator by which the list is ordered.  A
   *        <tt>null</tt> value indicates that the elements'
   *        {@linkplain Comparable natural ordering} should be used.
   * @return index of the search key, if it is contained in the list
   *         within the specified range;
   *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>.  The
   *         <i>insertion point</i> is defined as the point at which the
   *         key would be inserted into the list: the index of the first
   *         element in the range greater than the key,
   *         or <tt>toIndex</tt> if all
   *         elements in the range are less than the specified key.  Note
   *         that this guarantees that the return value will be &gt;= 0 if
   *         and only if the key is found.
   * @throws ClassCastException if the range contains elements that are not
   *         <i>mutually comparable</i> using the specified comparator,
   *         or the search key is not comparable to the
   *         elements in the range using this comparator.
   * @throws IllegalArgumentException
   *         if {@code fromIndex > toIndex}
   * @throws ArrayIndexOutOfBoundsException
   *         if {@code fromIndex < 0 or toIndex > a.length}
   */
  public static <T extends Comparable<? super T>>int binarySearch(final List<T> a, final int fromIndex, final int toIndex, final T key, final Comparator<? super T> c) {
    rangeCheck(a.size(), fromIndex, toIndex);
    return binarySearch0(a, fromIndex, toIndex, key, c);
  }

  /**
   * Searches the specified list for the specified object using the binary
   * search algorithm.  The list must be sorted into ascending order
   * according to the specified comparator (as by the
   * {@link #sort(List<T>, Comparator) sort(List<T>, Comparator)}
   * method) prior to making this call.  If it is not sorted, the results
   * are undefined. If the list contains multiple elements equal to the
   * specified object, there is no guarantee which one will be found.
   *
   * @param <T> the class of the objects in the list
   * @param a the list to be searched
   * @param key the value to be searched for
   * @param c the comparator by which the list is ordered.  A
   *        <tt>null</tt> value indicates that the elements'
   *        {@linkplain Comparable natural ordering} should be used.
   * @return index of the search key, if it is contained in the list;
   *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>.  The
   *         <i>insertion point</i> is defined as the point at which the
   *         key would be inserted into the list: the index of the first
   *         element greater than the key, or <tt>a.length</tt> if all
   *         elements in the list are less than the specified key.  Note
   *         that this guarantees that the return value will be &gt;= 0 if
   *         and only if the key is found.
   * @throws ClassCastException if the list contains elements that are not
   *         <i>mutually comparable</i> using the specified comparator,
   *         or the search key is not comparable to the
   *         elements of the list using this comparator.
   */
  public static <T extends Comparable<? super T>>int binarySearch(final List<T> a, final T key, final Comparator<? super T> c) {
    return binarySearch0(a, 0, a.size(), key, c);
  }

  /**
   * Checks that {@code fromIndex} and {@code toIndex} are in
   * the range and throws an exception if they aren't.
   */
  private static void rangeCheck(final int arrayLength, final int fromIndex, final int toIndex) {
    if (fromIndex > toIndex)
      throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");

    if (fromIndex < 0)
      throw new ArrayIndexOutOfBoundsException(fromIndex);

    if (toIndex > arrayLength)
      throw new ArrayIndexOutOfBoundsException(toIndex);
  }

  private static <T extends Comparable<? super T>>int binarySearch0(final List<T> a, final int fromIndex, final int toIndex, final T key) {
    int low = fromIndex;
    int high = toIndex - 1;

    while (low <= high) {
      int mid = (low + high) >>> 1;
      final Comparable<? super T> midVal = a.get(mid);
      int cmp = midVal.compareTo(key);

      if (cmp < 0)
        low = mid + 1;
      else if (cmp > 0)
        high = mid - 1;
      else
        return mid; // key found
    }

    return -(low + 1); // key not found.
  }

  private static <T extends Comparable<? super T>>int binarySearch0(final List<T> a, final int fromIndex, final int toIndex, final T key, final Comparator<? super T> c) {
    if (c == null)
      return binarySearch(a, fromIndex, toIndex, key);

    int low = fromIndex;
    int high = toIndex - 1;

    while (low <= high) {
      int mid = (low + high) >>> 1;
      T midVal = a.get(mid);
      int cmp = c.compare(midVal, key);
      if (cmp < 0)
        low = mid + 1;
      else if (cmp > 0)
        high = mid - 1;
      else
        return mid; // key found
    }

    return -(low + 1); // key not found.
  }

  /**
   * Find the index of the sorted array whose value most closely matches
   * the value provided.
   *
   * @param a The sorted array.
   * @param key The value to match.
   *
   * @return The closest index of the sorted array matching the desired value.
   */
  public static <T extends Comparable<? super T>>int binaryClosestSearch(final List<T> a, final T key) {
    return binaryClosestSearch0(a, 0, a.size(), key);
  }

  /**
   * Find the index of the sorted array whose value most closely matches
   * the value provided.
   *
   * @param a The sorted array.
   * @param from The starting index of the sorted array to search from.
   * @param to The ending index of the sorted array to search to.
   * @param key The value to match.
   *
   * @return The closest index of the sorted array matching the desired value.
   */
  public static <T extends Comparable<? super T>>int binaryClosestSearch(final List<T> a, final int from, final int to, final T key) {
    rangeCheck(a.size(), from, to);
    return binaryClosestSearch0(a, from, to, key);
  }

  /**
   * Find the index of the sorted array whose value most closely matches
   * the value provided.
   *
   * @param a The sorted array.
   * @param key The value to match.
   *
   * @return The closest index of the sorted array matching the desired value.
   */
  public static <T>int binaryClosestSearch(final List<T> a, final T key, final Comparator<T> comparator) {
    return binaryClosestSearch0(a, 0, a.size(), key, comparator);
  }

  /**
   * Find the index of the sorted array whose value most closely matches
   * the value provided.
   *
   * @param a The sorted array.
   * @param from The starting index of the sorted array to search from.
   * @param to The ending index of the sorted array to search to.
   * @param key The value to match.
   *
   * @return The closest index of the sorted array matching the desired value.
   */
  public static <T>int binaryClosestSearch(final List<T> a, final int from, final int to, final T key, final Comparator<T> comparator) {
    rangeCheck(a.size(), from, to);
    return binaryClosestSearch0(a, from, to, key, comparator);
  }

  private static <T extends Comparable<? super T>>int binaryClosestSearch0(final List<T> a, final int from, final int to, final T key) {
    if (to == 0)
      return 0;

    int first = 0;
    int upto = to;
    int mid = -1;
    while (first < upto) {
      mid = (first + upto) / 2;    // Compute mid point.
      final int comparison = key.compareTo(a.get(mid));
      if (comparison < 0)
        upto = mid;        // repeat search in bottom half.
      else if (comparison > 0)
        first = mid + 1;      // Repeat search in top half.
      else
        return mid;
    }

    return first == to - 1 && key.compareTo(a.get(first)) > 0 ? first + 1 : (first + upto) / 2;
  }

  private static <T>int binaryClosestSearch0(final List<T> a, final int from, final int to, final T key, final Comparator<T> comparator) {
    if (to == 0)
      return 0;

    int first = 0;
    int upto = to;
    int mid = -1;
    while (first < upto) {
      mid = (first + upto) / 2;    // Compute mid point.
      final int comparison = comparator.compare(key, a.get(mid));
      if (comparison < 0)
        upto = mid;        // repeat search in bottom half.
      else if (comparison > 0)
        first = mid + 1;      // Repeat search in top half.
      else
        return mid;
    }

    return first == to - 1 && comparator.compare(key, a.get(first)) > 0 ? first + 1 : (first + upto) / 2;
  }

  /**
   * Sorts the specified list into ascending order, according to the
   * <i>natural ordering</i> of its elements.  This implementation differs
   * from the one in java.util.Collections in that it allows null entries to
   * be sorted, which are placed in the beginning of the list.
   *
   * @param  list the list to be sorted.
   * @throws ClassCastException if the list contains elements that are not
   *         <i>mutually comparable</i> (for example, strings and integers).
   * @throws UnsupportedOperationException if the specified list's
   *         list-iterator does not support the <tt>set</tt> operation.
   * @see java.util.Collections#sort(List)
   */
  public static <T extends Comparable<? super T>>void sort(final List<T> list) {
    if (list.remove(null)) {
      java.util.Collections.<T>sort(list);
      list.add(0, null);
    }
    else {
      java.util.Collections.<T>sort(list);
    }
  }

  /**
   * Sorts the specified list according to the order induced by the
   * specified comparator.  This implementation differs from the one in
   * java.util.Collections in that it allows null entries to be sorted, which
   * are placed in the beginning of the list.
   *
   * @param  list the list to be sorted.
   * @param  c the comparator to determine the order of the list.  A
   *        <tt>null</tt> value indicates that the elements' <i>natural
   *        ordering</i> should be used.
   * @throws ClassCastException if the list contains elements that are not
   *         <i>mutually comparable</i> using the specified comparator.
   * @throws UnsupportedOperationException if the specified list's
   *         list-iterator does not support the <tt>set</tt> operation.
   * @see java.util.Collections#sort(List, Comparator)
   */
  public static <T>void sort(final List<T> list, final Comparator<? super T> c) {
    if (list.remove(null)) {
      java.util.Collections.<T>sort(list, c);
      list.add(0, null);
    }
    else {
      java.util.Collections.<T>sort(list, c);
    }
  }

  /**
   * Returns a mutable list containing the specified object.
   *
   * @param clazz the final class type of the List.
   * @param o the object to be stored in the returned list.
   * @return a mutable list containing the specified object.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <C extends Collection<T>,T>C singletonCollection(final Class<? extends Collection> type, final T o) {
    try {
      final C collection = (C)type.newInstance();
      collection.add(o);
      return collection;
    }
    catch (final ReflectiveOperationException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  @SafeVarargs
  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <C extends Collection<T>,T>C asCollection(final Class<? extends Collection> type, final T ... a) {
    try {
      final C collection = (C)type.newInstance();
      for (int i = 0; i < a.length; i++)
        collection.add(a[i]);

      return collection;
    }
    catch (final ReflectiveOperationException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <C extends Collection<T>,T>C asCollection(final Class<? extends Collection> type, final Collection<T> c) {
    try {
      final C collection = (C)type.newInstance();
      collection.addAll(collection);
      return collection;
    }
    catch (final ReflectiveOperationException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public static <C extends Collection<T>,T>C clone(final C collection) {
    try {
      final C clone = (C)collection.getClass().newInstance();
      for (final T item : collection)
        clone.add(item);

      return clone;
    }
    catch (final ReflectiveOperationException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  @SafeVarargs
  @SuppressWarnings("rawtypes")
  public static <T>Collection<T> addAll(final Class<? extends Collection> cls, final Collection<? extends T> ... collections) {
    try {
      final Collection<T> list = cls.newInstance();
      for (final Collection<? extends T> collection : collections)
        list.addAll(collection);

      return list;
    }
    catch (final ReflectiveOperationException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public static <C extends Collection<T>,T>C[] partition(final C collection, final int size) {
    return (C[])partition(collection.getClass(), collection, size);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <C extends Collection<T>,T>C[] partition(final Class<? extends Collection> type, final Collection<T> collection, final int size) {
    final int parts = collection.size() / size;
    final int remainder = collection.size() % size;
    final C[] partitions = (C[])Array.newInstance(type, parts + (remainder != 0 ? 1 : 0));
    final Iterator<T> iterator = collection.iterator();
    try {
      for (int i = 0; i < parts; i++) {
        final C part = partitions[i] = (C)type.newInstance();
        for (int j = 0; j < size; j++)
          part.add(iterator.next());
      }

      if (remainder != 0) {
        final C part = partitions[partitions.length - 1] = (C)type.newInstance();
        for (int j = 0; j < remainder; j++)
          part.add(iterator.next());
      }
    }
    catch (final ReflectiveOperationException e) {
      throw new UnsupportedOperationException(e);
    }

    return partitions;
  }

  public static boolean equals(final Collection<?> a, final Collection<?> b) {
    if (a == null)
      return b == null;

    if (b == null)
      return false;

    if (a.size() != b.size())
      return false;

    final Iterator<?> aIterator = a.iterator();
    final Iterator<?> bIterator = b.iterator();
    while (true) {
      if (aIterator.hasNext()) {
        if (!bIterator.hasNext())
          return false;

        Object item;
        if ((item = aIterator.next()) != null ? !item.equals(bIterator.next()) : bIterator.next() != null)
          return false;
      }
      else {
        return !bIterator.hasNext();
      }
    }
  }

  public static int hashCode(final Collection<?> collection) {
    if (collection == null)
        return 0;

    int result = 1;
    for (final Object item : collection) {
      final int itemHash = item.hashCode();
      result = 31 * result + itemHash ^ (itemHash >>> 32);
    }

    return result;
  }

  private Collections() {
  }
}