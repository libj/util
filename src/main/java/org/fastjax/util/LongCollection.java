/* Copyright (c) 2014 FastJAX
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

package org.fastjax.util;

import java.util.Collection;
import java.util.Iterator;

public interface LongCollection extends LongIterable {
  /**
   * Appends the specified value to the end of this collection.
   *
   * @param value Value to be appended to this collection.
   */
  void add(long value);

  /**
   * Appends all of the values in the specified collection to the end of this
   * collection, in the order that they are returned by the specified
   * collection's Iterator. The behavior of this operation is undefined if the
   * specified collection is modified while the operation is in progress. (This
   * implies that the behavior of this call is undefined if the specified
   * collection is this collection, and this collection is nonempty.)
   *
   * @param c Collection containing values to be added to this collection.
   * @throws NullPointerException If the specified collection is {@code null}.
   */
  void addAll(LongCollection c);

  /**
   * Appends all of the values in the specified collection to the end of this
   * collection, in the order that they are returned by the specified
   * collection's Iterator. The behavior of this operation is undefined if the
   * specified collection is modified while the operation is in progress. (This
   * implies that the behavior of this call is undefined if the specified
   * collection is this collection, and this collection is nonempty.)
   *
   * @param c Collection containing values to be added to this collection.
   * @throws NullPointerException If the specified collection is {@code null}.
   */
  void addAll(Collection<Long> c);

  /**
   * Removes the first occurrence of the specified value from this collection,
   * if it is present. If the collection does not contain the value, it is
   * unchanged. More formally, removes the value with the lowest index {@code i}
   * such that {@code Objects.equals(o, get(i))} (if such an value exists).
   * Returns {@code true} if this collection contained the specified value (or
   * equivalently, if this collection changed as a result of the call).
   *
   * @param value Value to be removed from this collection, if present.
   * @return {@code true} if this collection contained the specified value.
   */
  boolean removeValue(long value);

  /**
   * Removes from this collection all of its values that are contained in the
   * specified {@code LongCollection}.
   *
   * @param c Collection containing values to be removed from this collection.
   * @return {@code true} if this collection changed as a result of the call.
   * @see #contains(long)
   */
  boolean removeAll(LongCollection c);

  /**
   * Removes from this collection all of its values that are contained in the
   * specified {@code Collection}.
   *
   * @param c Collection containing values to be removed from this collection.
   * @return {@code true} if this collection changed as a result of the call.
   * @see #contains(long)
   */
  default boolean removeAll(final Collection<Long> c) {
    final int size = size();
    for (final Iterator<Long> iterator = c.iterator(); iterator.hasNext();) {
      final long value = iterator.next();
      while (removeValue(value));
    }

    return size != size();
  }

  /**
   * Removes all of the values from this collection. The collection will be
   * empty after this call returns.
   */
  void clear();

  /**
   * Determines if this collection contains a given value
   *
   * @param value The value to find.
   * @return Whether this collection contains the given value.
   */
  boolean contains(long value);

  /**
   * Returns {@code true} if this collection contains all of the values in the
   * specified array.
   *
   * @param c LongCollection to be checked for containment in this collection.
   * @return {@code true} if this collection contains all of the values in the
   *         specified collection.
   * @see #contains(long)
   */
  default boolean containsAll(final LongCollection c) {
    for (final LongIterator iterator = c.iterator(); iterator.hasNext();)
      if (!contains(iterator.next()))
        return false;

    return true;
  }

  /**
   * Returns {@code true} if this collection contains all of the values in the
   * specified collection.
   *
   * @param c Collection to be checked for containment in this collection.
   * @return {@code true} if this collection contains all of the values in the
   *         specified collection.
   * @see #contains(long)
   */
  default boolean containsAll(final Collection<Long> c) {
    for (final Iterator<Long> iterator = c.iterator(); iterator.hasNext();)
      if (!contains(iterator.next()))
        return false;

    return true;
  }

  /**
   * Returns the number of values in this collection.
   *
   * @return The number of values in this collection.
   */
  int size();

  /**
   * Returns {@code true} if this collection contains no values.
   *
   * @return {@code true} if this collection contains no values.
   */
  boolean isEmpty();
}