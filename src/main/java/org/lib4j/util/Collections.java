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