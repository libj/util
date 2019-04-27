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

package org.openjax.ext.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.LongUnaryOperator;

/**
 * An ordered collection (also known as a <i>sequence</i>), of {@code long}
 * values.
 * <p>
 * This interface is a replica of the {@link List} interface that defines
 * synonymous methods for a list of {@code long} values instead of Object
 * references.
 */
public interface LongList extends LongCollection {
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
  default long push(final long value) {
    add(value);
    return value;
  }

  /**
   * Removes the value at the top of this list and returns that value. This has
   * exactly the same effect as:
   *
   * <blockquote><pre>removeIndex(size() - 1)</pre></blockquote>
   *
   * @return The value at the top of this list (the last item of the
   *         {@code LongList} object).
   * @throws IndexOutOfBoundsException If this list is empty.
   */
  default long pop() {
    return removeIndex(size() - 1);
  }

  /**
   * Looks at the value at the top of this list without removing it from the
   * list. This has exactly the same effect as:
   *
   * <blockquote><pre>get(size() - 1)</pre></blockquote>
   *
   * @return The value at the top of this list (the last item of the
   *         {@code LongList} object).
   * @throws IndexOutOfBoundsException If this list is empty.
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
   * Appends the specified value to the end of this list.
   *
   * @param value Value to be appended to this list.
   * @return {@code true} if this list changed as a result of the call.
   */
  @Override
  boolean add(long value);

  /**
   * Inserts the specified value at the specified position in this list. Shifts
   * the value currently at that position (if any) and any subsequent values to
   * the right (adds one to their indices).
   *
   * @param index Index at which the specified value is to be inserted
   * @param value Value to be inserted.
   * @return {@code true} if this list changed as a result of the call.
   * @throws IndexOutOfBoundsException If the index is out of range
   *           ({@code index < 0 || size() < index}).
   */
  boolean add(int index, long value);

  /**
   * Appends all of the values in the specified array to the end of this list,
   * in the order that they appear in the array.
   *
   * @param index Index at which to insert the first value from the specified
   *          array.
   * @param values Array containing values to be added to this list.
   * @param offset The index of the first value to add.
   * @param length The number of values to add.
   * @return {@code true} if this list changed as a result of the call.
   * @throws IndexOutOfBoundsException If the index is out of range
   *           ({@code index < 0 || size() < index}), or if the offset or length
   *           are out of range
   *           ({@code offset < 0 || values.length < offset + length}).
   * @throws NullPointerException If the specified array is null.
   */
  boolean addAll(int index, long[] values, int offset, int length);

  /**
   * Appends all of the values in the specified array to the end of this list,
   * in the order that they appear in the array.
   *
   * @param index Index at which to insert the first value from the specified
   *          array.
   * @param values Array containing values to be added to this list.
   * @return {@code true} if this list changed as a result of the call.
   * @throws IndexOutOfBoundsException If the index is out of range
   *           ({@code index < 0 || size() < index}).
   * @throws NullPointerException If the specified array is null.
   */
  default boolean addAll(final int index, final long[] values) {
    return addAll(index, values, 0, values.length);
  }

  /**
   * Appends all of the values in the specified array to the end of this list,
   * in the order that they appear in the array.
   *
   * @param values Array containing values to be added to this list.
   * @param offset The index of the first value to add.
   * @param length The number of values to add.
   * @return {@code true} if this list changed as a result of the call.
   * @throws IndexOutOfBoundsException If the offset or length are out of range
   *           ({@code offset < 0 || values.length < offset + length}).
   * @throws NullPointerException If the specified array is null.
   */
  boolean addAll(long[] values, int offset, int length);

  /**
   * Appends all of the values in the specified array to the end of this
   * list, in the order that they appear in the array.
   *
   * @param values Array containing values to be added to this list.
   * @return {@code true} if this list changed as a result of the call.
   * @throws NullPointerException If the specified array is null.
   */
  default boolean addAll(final long[] values) {
    return addAll(values, 0, values.length);
  }

  /**
   * Appends all of the values in the specified collection to the end of this
   * list, in the order that they are returned by the specified collection's
   * iterator.
   *
   * @param index Index at which to insert the first value from the specified
   *          collection.
   * @param c Collection containing values to be added to this list.
   * @return {@code true} if this list changed as a result of the call.
   * @throws IndexOutOfBoundsException If the index is out of range
   *           ({@code index < 0 || size() < index}).
   * @throws ClassCastException If the class of an element of the specified
   *           collection is not {@code Long}.
   * @throws NullPointerException If the specified collection is null, or if the
   *           collection contains a null element.
   */
  boolean addAll(int index, Collection<Long> c);

  /**
   * Appends all of the values in the specified collection to the end of this
   * list, in the order that they are returned by the specified collection's
   * iterator.
   *
   * @param c Collection containing values to be added to this list.
   * @return {@code true} if this list changed as a result of the call.
   * @throws ClassCastException If the class of an element of the specified
   *           collection is not {@code Long}.
   * @throws NullPointerException If the specified collection is null, or if the
   *           collection contains a null element.
   */
  @Override
  boolean addAll(Collection<Long> c);

  /**
   * Appends all of the values in the specified collection to the end of this
   * list, in the order that they are returned by the specified collection's
   * iterator. The behavior of this operation is undefined if the specified
   * collection is modified while the operation is in progress. (Note that this
   * will occur if the specified collection is this list, and it's nonempty.)
   *
   * @param index Index at which to insert the first value from the specified
   *          collection.
   * @param c Collection containing values to be added to this list.
   * @return {@code true} if this list changed as a result of the call.
   * @throws IndexOutOfBoundsException If the index is out of range
   *           ({@code index < 0 || size() < index}).
   * @throws NullPointerException If the specified collection is null.
   */
  boolean addAll(int index, LongCollection c);

  /**
   * Appends all of the values in the specified collection to the end of this
   * list, in the order that they are returned by the specified collection's
   * iterator. The behavior of this operation is undefined if the specified
   * collection is modified while the operation is in progress. (Note that this
   * will occur if the specified collection is this list, and it's nonempty.)
   *
   * @param c Collection containing values to be added to this list.
   * @return {@code true} if this list changed as a result of the call.
   * @throws NullPointerException If the specified collection is null.
   */
  @Override
  boolean addAll(LongCollection c);

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
   * Removes the value at the specified index in this list. Shifts any
   * subsequent values to the left (subtracts one from their indices).
   *
   * @param index The index of the value to be removed.
   * @return The value that was removed from the list.
   * @throws IndexOutOfBoundsException If the index is out of range
   *           ({@code index < 0 || size() < index}).
   */
  long removeIndex(int index);

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
  default boolean remove(final long value) {
    final int index = indexOf(value);
    if (index == -1)
      return false;

    removeIndex(index);
    return true;
  }

  /**
   * Removes from this list all of its values that are contained in the
   * specified array.
   *
   * @param values Values to be removed from this list.
   * @return {@code true} if this list changed as a result of the call.
   * @see #remove(long)
   * @see #contains(long)
   */
  default boolean removeAll(final long ... values) {
    final int beforeSize = size();
    for (int i = 0; i < values.length; ++i)
      while (remove(values[i]));

    return beforeSize != size();
  }

  /**
   * Removes from this list all of its values that are contained in the
   * specified collection.
   *
   * @param c Collection containing values to be removed from this list.
   * @return {@code true} if this list changed as a result of the call.
   * @throws NullPointerException If the specified collection is null.
   * @see #remove(long)
   * @see #contains(long)
   */
  @Override
  default boolean removeAll(final LongCollection c) {
    final int beforeSize = size();
    for (final LongIterator iterator = c.iterator(); iterator.hasNext();) {
      final long value = iterator.next();
      while (remove(value));
    }

    return beforeSize != size();
  }

  /**
   * Removes from this list all of its values that are contained in the
   * specified {@code Collection}.
   *
   * @param c Collection containing values to be removed from this list.
   * @return {@code true} if this list changed as a result of the call.
   * @throws ClassCastException If the class of an element of the specified
   *           collection is not {@code Long}.
   * @throws NullPointerException If the specified collection is null, or if the
   *           collection contains a null element.
   * @see #remove(long)
   * @see #contains(long)
   */
  @Override
  default boolean removeAll(final Collection<Long> c) {
    final int beforeSize = size();
    for (final Iterator<Long> i = c.iterator(); i.hasNext();) {
      final long value = i.next();
      while (remove(value));
    }

    return beforeSize != size();
  }

  /**
   * Retains only the values in this list that are contained in the specified
   * collection. In other words, removes from this list all of its values that
   * are not contained in the specified collection.
   *
   * @param c Collection containing values to be retained in this list.
   * @return {@code true} if this list changed as a result of the call.
   * @throws ClassCastException If the class of an element of the specified
   *           collection is not {@code Long}.
   * @throws NullPointerException If the specified collection contains a null
   *           value, or if the specified collection is null.
   * @see #remove(long)
   * @see #contains(long)
   */
  @Override
  boolean retainAll(Collection<Long> c);

  /**
   * Retains only the values in this list that are contained in the specified
   * collection. In other words, removes from this list all of its values that
   * are not contained in the specified collection.
   *
   * @param c Collection containing values to be retained in this list.
   * @return {@code true} if this list changed as a result of the call.
   * @throws NullPointerException If the specified collection contains a null
   *           value, or if the specified collection is null.
   * @see #remove(long)
   * @see #contains(long)
   */
  @Override
  boolean retainAll(LongCollection c);

  /**
   * Replaces each value of this list with the result of applying the operator
   * to that value. Errors or runtime exceptions thrown by the operator are
   * relayed to the caller.
   * <p>
   * If the list's list-iterator does not support the {@code set} operation,
   * then an {@code UnsupportedOperationException} will be thrown when replacing
   * the first value.
   *
   * @param operator The operator to apply to each value.
   * @throws NullPointerException If the specified operator is null.
   */
  default void replaceAll(final LongUnaryOperator operator) {
    Objects.requireNonNull(operator);
    final LongListIterator iterator = listIterator();
    while (iterator.hasNext())
      iterator.set(operator.applyAsLong(iterator.next()));
  }

  /**
   * Returns {@code true} if this list contains the specified value. More
   * formally, returns {@code true} if and only if this list contains at least
   * one value {@code v} such that {@code Objects.equals(o, e)}.
   *
   * @param value Value whose presence in this list is to be tested.
   * @return {@code true} if this list contains the specified value.
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
    for (int i = 0; i < values.length; ++i)
      if (!contains(values[i]))
        return false;

    return true;
  }

  /**
   * Returns the index of the first occurrence of the specified value in this
   * list, or -1 if this list does not contain the value. More formally, returns
   * the lowest index {@code i} such that {@code value == get(i)}, or -1 if
   * there is no such value.
   *
   * @param value Value to search for.
   * @return The index of the first occurrence of the specified value in this
   *         list, or -1 if this list does not contain the value.
   */
  int indexOf(long value);

  /**
   * Returns the index of the last occurrence of the specified value in this
   * list, or -1 if this list does not contain the value. More formally, returns
   * the highest index {@code i} such that {@code value == get(i)}, or -1 if
   * there is no such value.
   *
   * @param value Value to search for.
   * @return The index of the last occurrence of the specified value in this
   *         list, or -1 if this list does not contain the value.
   */
  int lastIndexOf(long value);

  /**
   * Returns an iterator over the values in this list in proper sequence.
   *
   * @return An iterator over the values in this list in proper sequence.
   */
  @Override
  LongIterator iterator();

  /**
   * Returns an long list iterator over the values in this list (in proper
   * sequence).
   *
   * @return An long list iterator over the values in this list (in proper
   *         sequence).
   */
  default LongListIterator listIterator() {
    return listIterator(0);
  }

  /**
   * Returns an long list iterator over the values in this list (in proper
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
   * Returns the number of values in this list. If this list contains more than
   * {@code Long.MAX_VALUE} values, returns {@code Long.MAX_VALUE}.
   *
   * @return The number of values in this list.
   */
  @Override
  int size();

  /**
   * Returns {@code true} if this list contains no values.
   *
   * @return {@code true} if this list contains no values
   */
  @Override
  boolean isEmpty();

  /**
   * Sorts the list into ascending order.
   */
  void sort();

  /**
   * Removes all of the elements from this list. The list will be empty after
   * this call returns.
   */
  @Override
  void clear();

  /**
   * Returns a view of the portion of this list between the specified
   * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive. (If
   * {@code fromIndex} and {@code toIndex} are equal, the returned list is
   * empty). The returned list is backed by this list, so non-structural, <b>or
   * structural</b>, changes in the returned list are reflected in this list,
   * and vice-versa. The returned list supports all of the optional list
   * operations supported by this list.
   * <p>
   * This method eliminates the need for explicit range operations (of the sort
   * that commonly exist for arrays). Any operation that expects a list can be
   * used as a range operation by passing a subList view instead of a whole
   * list. For example, the following idiom removes a range of elements from a
   * list:
   *
   * <pre>
   * {@code list.subList(from, to).clear();}
   * </pre>
   *
   * Similar idioms may be constructed for {@code indexOf} and
   * {@code lastIndexOf}, and all of the algorithms in the
   * {@code LongCollections} class can be applied to a subList.
   * <p>
   * The semantics of the list returned by this method <b>are defined</b> if the
   * backing list (i.e., this list) is <i>structurally modified</i> in any way
   * other than via the returned list. Structural modifications are those that
   * change the size of this list.
   * <p>
   * <ul>
   * <li>Changes made to the returned sub-list will be reflected in its parent
   * list.
   * <ol>
   * <li>If a value is added to the sub-list, the value addition will be
   * reflected at the relevant index of the parent list, and the range and size
   * of the sub-list and its parent list will increase by 1.</li>
   * <li>If a value is removed from the sub-list, the value removal will be
   * reflected at the relevant index of the parent list, and the range and size
   * of the sub-list and its parent list will reduce by 1.</li>
   * </ol>
   * </li>
   * <li>Changes made to the parent list will be reflected in the returned
   * sub-list.
   * <ol>
   * <li>If a value is added to the parent list in the range of the sub-list,
   * the value addition will be reflected at the relevant index of the sub-list,
   * and the range and size of the parent list and the sub-list will increase by
   * 1.</li>
   * <li>If a value is added to the parent list in the range preceding the
   * {@code fromIndex} of the sub-list, the range indexes of the sub-list will
   * increase by 1.</li>
   * <li>If a value is removed from the parent list in the range of the
   * sub-list, the value removal will be reflected at the relevant index of the
   * sub-list, and the range and size of the parent list and the sub-list will
   * reduce by 1.</li>
   * <li>If a value is removed from the parent list in the range preceding the
   * {@code fromIndex} of the sub-list, the range indexes of the sub-list will
   * reduce by 1.</li>
   * </ol>
   * </li>
   * </ul>
   *
   * @param fromIndex Low endpoint (inclusive) of the subList.
   * @param toIndex High endpoint (exclusive) of the subList.
   * @return A view of the specified range within this list.
   * @throws IndexOutOfBoundsException For range parameters that are
   *           out-of-bounds ({@code fromIndex < 0 || toIndex > size ||
   *         fromIndex > toIndex}).
   */
  LongList subList(int fromIndex, int toIndex);

  /**
   * Returns an array containing all of the values in this list in proper
   * sequence (from first to last value). A new array is allocated with the size
   * of this list.
   * <p>
   * The returned array will be "safe" in that no references to it are
   * maintained by this list. (In other words, this method must allocate a new
   * array even if this list is backed by an array). The caller is thus free to
   * modify the returned array.
   *
   * @return An array containing all of the values in this list in proper
   *         sequence.
   */
  default long[] toArray() {
    return toArray(new long[size()]);
  }

  /**
   * Returns an array containing all of the values in this list in proper
   * sequence (from first to last value). If the list fits in the specified
   * array, it is returned therein. Otherwise, a new array is allocated with the
   * size of this list.
   * <p>
   * If the list fits in the specified array with room to spare (i.e., the array
   * has more values than the list), the value in the array immediately
   * following the end of the collection is set to {@code 0}. (This is useful in
   * determining the length of the list <i>only</i> if the caller knows that the
   * list does not contain any {@code 0} values.)
   *
   * @param a The array into which the values of the list are to be stored, if
   *          it is big enough; otherwise, a new array is allocated for this
   *          purpose.
   * @return An array containing the values of the list.
   * @throws NullPointerException If the specified array is null.
   */
  long[] toArray(long[] a);

  /**
   * Returns an array containing all of the values in this list in proper
   * sequence (from first to last value). If the list fits in the specified
   * array, it is returned therein. Otherwise, a new array is allocated with the
   * size of this list.
   * <p>
   * If the list fits in the specified array with room to spare (i.e., the array
   * has more values than the list), the value in the array immediately
   * following the end of the collection is set to null
   *
   * @param a The array into which the values of the list are to be stored, if
   *          it is big enough; otherwise, a new array is allocated for this
   *          purpose.
   * @return An array containing the values of the list.
   * @throws NullPointerException If the specified array is null.
   */
  Long[] toArray(Long[] a);

  /**
   * Compares the specified object with this list for equality. Returns
   * {@code true} if and only if the specified object is also a list, both lists
   * have the same size, and all corresponding pairs of values in the two lists
   * are <i>equal</i>. (Two values {@code v1} and {@code v2} are <i>equal</i> if
   * {@code v1 == v2}.) In other words, two lists are defined to be equal if
   * they contain the same values in the same order.
   *
   * @param o The object to be compared for equality with this list.
   * @return {@code true} if the specified object is equal to this list.
   */
  @Override
  boolean equals(Object o);

  /**
   * Returns the hash code value for this list. The hash code algorithm of a
   * list is defined in {@link List#hashCode()}.
   *
   * @return The hash code value for this list.
   * @see Object#equals(Object)
   * @see #equals(Object)
   */
  @Override
  int hashCode();
}