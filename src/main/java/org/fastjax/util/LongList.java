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

interface LongList extends LongCollection {
  /**
   * Pushes an item onto the top of this list. This has exactly the same effect
   * as:
   *
   * <blockquote><pre>add(value)</pre></blockquote>
   *
   * @param value The value to be pushed onto this list.
   * @return The {@code value} argument.
   * @see #add(long)
   */
  default long push(final int value) {
    add(value);
    return value;
  }

  /**
   * Removes the value at the top of this list and returns that value. This has
   * exactly the same effect as:
   *
   * <blockquote><pre>remove(size() - 1)</pre></blockquote>
   *
   * @return The value at the top of this list (the last item of the
   *         {@code LongList} object).
   * @throws ArrayIndexOutOfBoundsException If this list is empty.
   */
  default long pop() {
    return remove(size() - 1);
  }

  /**
   * Looks at the value at the top of this list without removing it from the
   * list. This has exactly the same effect as:
   *
   * <blockquote><pre>get(size() - 1)</pre></blockquote>
   *
   * @return The value at the top of this list (the last item of the
   *         {@code LongList} object).
   * @throws ArrayIndexOutOfBoundsException if this list is empty.
   */
  default long peek() {
    return get(size() - 1);
  }

  /**
   * Returns the value at the specified position in this list.
   *
   * @param index Index of the value to return.
   * @return The value at the specified position in this list.
   * @throws IndexOutOfBoundsException If the index is out of range
   *           ({@code index < 0 || size() < index}).
   */
  long get(int index);

  /**
   * Inserts the specified value at the specified position in this list. Shifts
   * the value currently at that position (if any) and any subsequent values to
   * the right (adds one to their indices).
   *
   * @param index Index at which the specified value is to be inserted
   * @param value Value to be inserted.
   * @throws IndexOutOfBoundsException If the index is out of range
   *           ({@code index < 0 || size() < index}).
   */
  void add(int index, long value);

  /**
   * Inserts all of the values in the specified array into this list, starting
   * at the specified position. Shifts the value currently at that position (if
   * any) and any subsequent values to the right (increases their indices). The
   * new values will appear in the list in the order that they are returned by
   * the specified array's iterator.
   *
   * @param index Index at which to insert the first value from the specified
   *          array.
   * @param values Array containing values to be added to this list.
   * @param offset The index of the first value to add.
   * @param length The number of values to add.
   * @return {@code true} if this list changed as a result of the call.
   * @throws IndexOutOfBoundsException if the index is out of range
   *           ({@code index < 0 || size() < index}).
   * @throws NullPointerException If the specified array is {@code null}.
   */
  void addAll(int index, long[] values, int offset, int length);

  /**
   * Inserts all of the values in the specified array into this list, starting
   * at the specified position. Shifts the value currently at that position (if
   * any) and any subsequent values to the right (increases their indices). The
   * new values will appear in the list in the order that they are returned by
   * the specified array's iterator.
   *
   * @param index Index at which to insert the first value from the specified
   *          array.
   * @param values Array containing values to be added to this list.
   * @throws IndexOutOfBoundsException If the index is out of range
   *           ({@code index < 0 || size() < index}).
   * @throws NullPointerException If the specified array is {@code null}.
   */
  default void addAll(final int index, final long[] values) {
    addAll(index, values, 0, values.length);
  }

  /**
   * Appends all of the values in the specified array to the end of this list,
   * in the order that they are returned by the specified array's Iterator. The
   * behavior of this operation is undefined if the specified array is modified
   * while the operation is in progress. (This implies that the behavior of this
   * call is undefined if the specified array is this list, and this list is
   * nonempty.)
   *
   * @param values Array containing values to be added to this list.
   * @param offset The index of the first value to add.
   * @param length The number of values to add.
   * @throws NullPointerException If the specified array is {@code null}.
   */
  default void addAll(final long[] values, final int offset, final int length) {
    addAll(size(), values, offset, length);
  }

  /**
   * Appends all of the values in the specified array to the end of this list,
   * in the order that they are returned by the specified array's Iterator. The
   * behavior of this operation is undefined if the specified array is modified
   * while the operation is in progress. (This implies that the behavior of this
   * call is undefined if the specified array is this list, and this list is
   * nonempty.)
   *
   * @param values Array containing values to be added to this list.
   * @throws NullPointerException If the specified array is {@code null}.
   */
  default void addAll(final long[] values) {
    addAll(size(), values, 0, values.length);
  }

  /**
   * Inserts all of the values in the specified {@code Collection} into this
   * list, starting at the specified position. Shifts the value currently at
   * that position (if any) and any subsequent values to the right (increases
   * their indices). The new values will appear in the list in the order that
   * they are returned by the specified collection's iterator.
   *
   * @param index Index at which to insert the first value from the specified
   *          collection.
   * @param c Collection containing values to be added to this list.
   * @throws IndexOutOfBoundsException If the index is out of range
   *           ({@code index < 0 || size() < index}).
   * @throws NullPointerException If the specified collection is {@code null}.
   */
  void addAll(int index, Collection<Long> c);

  /**
   * Appends all of the values in the specified {@code Collection} to the end of
   * this list, in the order that they are returned by the specified
   * collection's Iterator. The behavior of this operation is undefined if the
   * specified collection is modified while the operation is in progress. (This
   * implies that the behavior of this call is undefined if the specified
   * collection is this list, and this list is nonempty.)
   *
   * @param c Collection containing values to be added to this list.
   * @throws NullPointerException If the specified collection is {@code null}.
   */
  @Override
  default void addAll(final Collection<Long> c) {
    addAll(size(), c);
  }

  /**
   * Inserts all of the values in the specified {@code LongCollection} into this
   * list, starting at the specified position. Shifts the value currently at
   * that position (if any) and any subsequent values to the right (increases
   * their indices). The new values will appear in the list in the order that
   * they are returned by the specified collection's iterator.
   *
   * @param index Index at which to insert the first value from the specified
   *          collection.
   * @param c Collection containing values to be added to this list.
   * @throws IndexOutOfBoundsException If the index is out of range
   *           ({@code index < 0 || size() < index}).
   * @throws NullPointerException If the specified collection is {@code null}.
   */
  void addAll(int index, LongCollection c);

  @Override
  default void addAll(final LongCollection c) {
    addAll(size(), c);
  }

  /**
   * Replaces the value at the specified position in this list with the
   * specified value.
   *
   * @param index Index of the value to replace.
   * @param value Value to be stored at the specified position.
   * @return The value previously at the specified position.
   * @throws IndexOutOfBoundsException If the index is out of range
   *           ({@code index < 0 || size() < index}).
   */
  long set(int index, long value);

  /**
   * Removes the value at the specified position in this list. Shifts any
   * subsequent values to the left (subtracts one from their indices).
   *
   * @param index The index of the value to be removed.
   * @return The value that was removed from the list.
   * @throws IndexOutOfBoundsException If the index is out of range
   *           ({@code index < 0 || size() < index}).
   */
  long remove(int index);

  /**
   * Removes the first occurrence of the specified value from this list, if it
   * is present. If the list does not contain the value, it is unchanged. More
   * formally, removes the value with the lowest index {@code i} such that
   * {@code Objects.equals(o, get(i))} (if such an value exists). Returns
   * {@code true} if this list contained the specified value (or equivalently,
   * if this list changed as a result of the call).
   *
   * @param value Value to be removed from this list, if present.
   * @return {@code true} if this list contained the specified value.
   */
  @Override
  default boolean removeValue(final long value) {
    final int index = indexOf(value);
    if (index == -1)
      return false;

    remove(index);
    return true;
  }

  /**
   * Removes from this list all of its values that are contained in the
   * specified {@code LongCollection}.
   *
   * @param values Values to be removed from this list.
   * @return {@code true} if this list changed as a result of the call.
   * @see #contains(long)
   */
  default boolean removeAll(final long ... values) {
    final int size = size();
    for (int i = 0; i < values.length; i++)
      while (removeValue(values[i]));

    return size != size();
  }

  /**
   * Removes from this list all of its values that are contained in the
   * specified {@code LongCollection}.
   *
   * @param c LongCollection containing values to be removed from this list.
   * @return {@code true} if this list changed as a result of the call.
   * @see #contains(long)
   */
  @Override
  default boolean removeAll(final LongCollection c) {
    final int size = size();
    for (final LongIterator iterator = c.iterator(); iterator.hasNext();) {
      final long value = iterator.next();
      while (removeValue(value));
    }

    return size != size();
  }

  /**
   * Removes from this list all of its values that are contained in the
   * specified {@code Collection}.
   *
   * @param c Collection containing values to be removed from this list.
   * @return {@code true} if this list changed as a result of the call.
   * @see #contains(long)
   */
  @Override
  default boolean removeAll(final Collection<Long> c) {
    final int size = size();
    for (final Iterator<Long> iterator = c.iterator(); iterator.hasNext();) {
      final long value = iterator.next();
      while (removeValue(value));
    }

    return size != size();
  }

  /**
   * Determines if this list contains a given value
   *
   * @param value The value to find.
   * @return Whether this list contains the given value.
   */
  @Override
  default boolean contains(final long value) {
    return indexOf(value) != -1;
  }

  /**
   * Returns {@code true} if this collection contains all of the values in the
   * specified array.
   *
   * @param values Array to be checked for containment in this collection.
   * @return {@code true} if this collection contains all of the values in the
   *         specified collection.
   * @see #contains(long)
   */
  default boolean containsAll(final long ... values) {
    for (int i = 0; i < values.length; i++)
      if (!contains(values[i]))
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
  @Override
  default boolean containsAll(final Collection<Long> c) {
    for (final Iterator<Long> iterator = c.iterator(); iterator.hasNext();)
      if (!contains(iterator.next()))
        return false;

    return true;
  }

  /**
   * Returns the index of the first occurrence of the specified value in this
   * list, or -1 if this list does not contain the value. More formally, returns
   * the lowest index {@code i} such that {@code value == get(i)}, or -1 if
   * there is no such value.
   */
  int indexOf(long value);

  /**
   * Returns the index of the last occurrence of the specified value in this
   * list, or -1 if this list does not contain the value. More formally, returns
   * the highest index {@code i} such that {@code value == get(i)}, or -1 if
   * there is no such value.
   */
  int lastIndexOf(long value);

  /**
   * Returns a long list iterator over the values in this list (in proper
   * sequence).
   *
   * @return A long list iterator over the values in this list (in proper
   *         sequence).
   */
  default LongListIterator listIterator() {
    return listIterator(0);
  }

  /**
   * Returns a long list iterator over the values in this list (in proper
   * sequence), starting at the specified position in the list. The specified
   * index indicates the first value that would be returned by an initial call
   * to {@link LongListIterator#next next}. An initial call to
   * {@link LongListIterator#previous previous} would return the value with the
   * specified index minus one.
   *
   * @param index Index of the first value to be returned from the long list
   *          iterator (by a call to {@link LongListIterator#next next}).
   * @return A long list iterator over the values in this list (in proper
   *         sequence), starting at the specified position in the list.
   * @throws IndexOutOfBoundsException If the index is out of range
   *           ({@code index < 0 || index > size()}).
   */
  LongListIterator listIterator(int index);

  /**
   * Sorts the list into ascending order.
   */
  void sort();

  /**
   * Returns an array containing all of the values in this list in proper
   * sequence (from first to last value); the runtime type of the returned array
   * is that of the specified array. If the list fits in the specified array, it
   * is returned therein. Otherwise, a new array is allocated with the runtime
   * type of the specified array and the size of this list.
   * <p>
   * If the list fits in the specified array with room to spare (i.e., the array
   * has more values than the list), the value in the array immediately
   * following the end of the collection is set to {@code 0}. (This is useful in
   * determining the length of the list <i>only</i> if the caller knows that the
   * list does not contain any null values.)
   *
   * @param a The array into which the values of the list are to be stored, if
   *          it is big enough; otherwise, a new array of the same runtime type
   *          is allocated for this purpose.
   * @return An array containing the values of the list.
   * @throws NullPointerException If the specified array is {@code null}.
   */
  long[] toArray(long[] a);

  /**
   * Returns an array containing all of the values in this list in proper
   * sequence (from first to last value); the runtime type of the returned array
   * is that of the specified array. If the list fits in the specified array, it
   * is returned therein. Otherwise, a new array is allocated with the runtime
   * type of the specified array and the size of this list.
   * <p>
   * If the list fits in the specified array with room to spare (i.e., the array
   * has more values than the list), the value in the array immediately
   * following the end of the collection is set to {@code null}. (This is useful
   * in determining the length of the list <i>only</i> if the caller knows that
   * the list does not contain any null values.)
   *
   * @param a The array into which the values of the list are to be stored, if
   *          it is big enough; otherwise, a new array of the same runtime type
   *          is allocated for this purpose.
   * @return An array containing the values of the list.
   * @throws NullPointerException If the specified array is {@code null}.
   */
  Long[] toArray(Long[] a);
}