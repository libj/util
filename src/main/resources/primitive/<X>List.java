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

package org.libj.util.primitive;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * An ordered collection (also known as a <i>sequence</i>), of {@code <x>}
 * values.
 * <p>
 * This interface is a replica of the {@link java.util.List} interface that
 * defines synonymous methods for a list of {@code <x>} values instead of
 * Object references.
 */
public interface <X>List extends <X>Collection {
  /**
   * Pushes an item onto the top of this list. This has exactly the same effect
   * as:
   *
   * <pre>
   * {@code add(value)}
   * </pre>
   *
   * @param value The value to be pushed onto this list.
   * @return The {@code value} argument.
   * @see #add(<x>)
   */
  default <x> push(final <x> value) {
    add(value);
    return value;
  }

  /**
   * Removes the value at the top of this list and returns that value. This has
   * exactly the same effect as:
   *
   * <pre>
   * {@code removeIndex(size() - 1)}
   * </pre>
   *
   * @return The value at the top of this list (the last item of the
   *         {@link <X>List} object).
   * @throws IndexOutOfBoundsException If this list is empty.
   */
  default <x> pop() {
    return removeIndex(size() - 1);
  }

  /**
   * Looks at the value at the top of this list without removing it from the
   * list. This has exactly the same effect as:
   *
   * <pre>
   * {@code get(size() - 1)}
   * </pre>
   *
   * @return The value at the top of this list (the last item of the
   *         {@link <X>List} object).
   * @throws IndexOutOfBoundsException If this list is empty.
   */
  default <x> peek() {
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
  <x> get(int index);

  /**
   * Appends the specified value to the end of this list.
   *
   * @param value Value to be appended to this list.
   * @return {@code true} if this list changed as a result of the call.
   */
  @Override
  boolean add(<x> value);

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
  boolean add(int index, <x> value);

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
  boolean addAll(int index, <x>[] values, int offset, int length);

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
  default boolean addAll(final int index, final <x>[] values) {
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
  boolean addAll(<x>[] values, int offset, int length);

  /**
   * Appends all of the values in the specified array to the end of this list,
   * in the order that they appear in the array.
   *
   * @param values Array containing values to be added to this list.
   * @return {@code true} if this list changed as a result of the call.
   * @throws NullPointerException If the specified array is null.
   */
  @Override
  default boolean addAll(final <x> ... values) {
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
   *           collection is not {@link <XX>}.
   * @throws NullPointerException If the specified collection is null, or if the
   *           collection contains a null element.
   */
  boolean addAll(int index, Collection<<XX>> c);

  /**
   * Appends all of the values in the specified collection to the end of this
   * list, in the order that they are returned by the specified collection's
   * iterator.
   *
   * @param c Collection containing values to be added to this list.
   * @return {@code true} if this list changed as a result of the call.
   * @throws ClassCastException If the class of an element of the specified
   *           collection is not {@link <XX>}.
   * @throws NullPointerException If the specified collection is null, or if the
   *           collection contains a null element.
   */
  @Override
  boolean addAll(Collection<<XX>> c);

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
  boolean addAll(int index, <X>Collection c);

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
  boolean addAll(<X>Collection c);

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
  <x> set(int index, <x> value);

  /**
   * Removes the value at the specified index in this list. Shifts any
   * subsequent values to the left (subtracts one from their indices).
   *
   * @param index The index of the value to be removed.
   * @return The value that was removed from the list.
   * @throws IndexOutOfBoundsException If the index is out of range
   *           ({@code index < 0 || size() < index}).
   */
  <x> removeIndex(int index);

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
  default boolean remove(final <x> value) {
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
   * @see #remove(<x>)
   * @see #contains(<x>)
   */
  @Override
  default boolean removeAll(final <x> ... values) {
    final int beforeSize = size();
    for (int i = 0; i < values.length; ++i) // [A]
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
   * @see #remove(<x>)
   * @see #contains(<x>)
   */
  @Override
  default boolean removeAll(final <X>Collection c) {
    final int beforeSize = size();
    for (final <X>Iterator iterator = c.iterator(); iterator.hasNext();) { // [X]
      final <x> value = iterator.next();
      while (remove(value));
    }

    return beforeSize != size();
  }

  /**
   * Removes from this list all of its values that are contained in the
   * specified {@link Collection}.
   *
   * @param c Collection containing values to be removed from this list.
   * @return {@code true} if this list changed as a result of the call.
   * @throws ClassCastException If the class of an element of the specified
   *           collection is not {@link <XX>}.
   * @throws NullPointerException If the specified collection is null, or if the
   *           collection contains a null element.
   * @see #remove(<x>)
   * @see #contains(<x>)
   */
  @Override
  default boolean removeAll(final Collection<<XX>> c) {
    final int beforeSize = size();
    for (final Iterator<<XX>> i = c.iterator(); i.hasNext();) { // [X]
      final <x> value = i.next();
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
   *           collection is not {@link <XX>}.
   * @throws NullPointerException If the specified collection contains a null
   *           value, or if the specified collection is null.
   * @see #remove(<x>)
   * @see #contains(<x>)
   */
  @Override
  boolean retainAll(Collection<<XX>> c);

  /**
   * Retains only the values in this list that are contained in the specified
   * collection. In other words, removes from this list all of its values that
   * are not contained in the specified collection.
   *
   * @param c Collection containing values to be retained in this list.
   * @return {@code true} if this list changed as a result of the call.
   * @throws NullPointerException If the specified collection contains a null
   *           value, or if the specified collection is null.
   * @see #remove(<x>)
   * @see #contains(<x>)
   */
  @Override
  boolean retainAll(<X>Collection c);

  /**
   * Replaces each value of this list with the result of applying the operator
   * to that value. Errors or runtime exceptions thrown by the operator are
   * relayed to the caller.
   * <p>
   * If the list's list-iterator does not support the {@code set} operation,
   * then an {@link UnsupportedOperationException} will be thrown when replacing
   * the first value.
   *
   * @param operator The operator to apply to each value.
   * @throws NullPointerException If the specified operator is null.
   */
  default void replaceAll(final <X>UnaryOperator operator) {
    Objects.requireNonNull(operator);
    final <X>ListIterator iterator = listIterator();
    while (iterator.hasNext())
      iterator.set(operator.applyAs<X>(iterator.next()));
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
  default boolean contains(final <x> value) {
    return indexOf(value) != -1;
  }

  /**
   * Returns {@code true} if this collection contains all of the values in the
   * specified array.
   *
   * @param values Array to be checked for containment in this collection.
   * @return {@code true} if this collection contains all of the values in the
   *         specified collection.
   * @see #contains(<x>)
   */
  default boolean containsAll(final <x> ... values) {
    for (int i = 0; i < values.length; ++i) // [A]
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
  int indexOf(<x> value);

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
  int lastIndexOf(<x> value);

  /**
   * Returns an iterator over the values in this list in proper sequence.
   *
   * @return An iterator over the values in this list in proper sequence.
   */
  @Override
  <X>Iterator iterator();

  /**
   * Returns a <x> list iterator over the values in this list (in proper
   * sequence).
   *
   * @return An <x> list iterator over the values in this list (in proper
   *         sequence).
   */
  default <X>ListIterator listIterator() {
    return listIterator(0);
  }

  /**
   * Returns a <x> list iterator over the values in this list (in proper
   * sequence), starting at the specified position in the list. The specified
   * index indicates the first value that would be returned by an initial call
   * to {@link <X>ListIterator#next next}. An initial call to
   * {@link <X>ListIterator#previous previous} would return the value with the
   * specified index minus one.
   *
   * @param index Index of the first value to be returned from the <x> list
   *          iterator (by a call to {@link <X>ListIterator#next next}).
   * @return A <x> list iterator over the values in this list (in proper
   *         sequence), starting at the specified position in the list.
   * @throws IndexOutOfBoundsException If the index is out of range
   *           ({@code index < 0 || index > size()}).
   */
  <X>ListIterator listIterator(int index);

  /**
   * Returns the number of values in this list. If this list contains more than
   * {@link <XX>#MAX_VALUE} values, returns {@link <XX>#MAX_VALUE}.
   *
   * @return The number of values in this list.
   */
  @Override
  int size();

  /**
   * Returns {@code true} if this list contains no values.
   *
   * @return {@code true} if this list contains no values.
   */
  @Override
  boolean isEmpty();

  /**
   * Sorts the list into ascending order.
   */
  default void sort() {
    sort((<X>Comparator)null);
  }

  /**
   * Sorts the specified list according to the order induced by the specified
   * comparator.
   *
   * @param c The comparator to determine the order of the list.
   */
  void sort(<X>Comparator c);

  /**
   * Sorts the specified paired array according to the order induced by the
   * specified comparator applied to the values in this list.
   * <p>
   * This method will result in this and the specified paired list to be sorted
   * in tandem.
   * <p>
   * For example, {@code this} and {@code p} are initialized to:
   *
   * <pre>
   * this: 6 8 9 7 4 0 2 3 1 5
   *    p: g i j h e a c d b f
   * </pre>
   *
   * After {@code sort(p)} is called:
   *
   * <pre>
   * this: 0 1 2 3 4 5 6 7 8 9
   *    p: a b c d e f g h i j
   * </pre>
   *
   * @param p The paired array that is to be sorted in tandem with this list.
   */
  default void sort(final Object[] p) {
    sort(p, null);
  }

  /**
   * Sorts this specified paired array according to the order induced by the
   * specified comparator applied to the values in this list.
   * <p>
   * This method will result in this and the specified paired list to be sorted
   * in tandem.
   * <p>
   * For example, {@code this} and {@code p} are initialized to:
   *
   * <pre>
   * this: 6 8 9 7 4 0 2 3 1 5
   *    p: g i j h e a c d b f
   * </pre>
   *
   * After {@code sort(p)} is called:
   *
   * <pre>
   * this: 0 1 2 3 4 5 6 7 8 9
   *    p: a b c d e f g h i j
   * </pre>
   *
   * @param p The paired array that is to be sorted in tandem with this list.
   * @param c The comparator to determine the order of this and the paired array.
   * @throws IllegalArgumentException If the length of the paired list does not
   *           match that of this list.
   */
  default void sort(final Object[] p, final <X>Comparator c) {
    if (p.length != size())
      throw new IllegalArgumentException("The length of the paired array (" + p.length + ") does not match that of this list (" + size() + ")");

    final int[] idx = PrimitiveSort.buildIndex(size());
    final <X>Comparator comparator = c != null ? c : <X>Comparator.NATURAL;
    PrimitiveSort.sortIndexed(p, this, idx, new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return comparator.compare(get(o1), get(o2));
      }
    });
  }

  /**
   * Sorts the specified paired list according to the order induced by the
   * specified comparator applied to the values in this list.
   * <p>
   * This method will result in this and the specified paired list to be sorted
   * in tandem.
   *
   * @param p The paired list that is to be sorted in tandem with this list.
   */
  default void sort(final List<?> p) {
    sort(p, null);
  }

  /**
   * Sorts the specified paired list according to the order induced by the
   * specified comparator applied to the values in this list.
   * <p>
   * This method will result in this and the specified paired list to be sorted
   * in tandem.
   *
   * @param p The paired list that is to be sorted in tandem with this list.
   * @param c The comparator to determine the order of this and the paired list.
   * @throws IllegalArgumentException If the length of the paired list does not
   *           match that of this list.
   */
  default void sort(final List<?> p, final <X>Comparator c) {
    if (p.size() != size())
      throw new IllegalArgumentException("The size of the paired list (" + p.size() + ") does not match that of this list (" + size() + ")");

    final int[] idx = PrimitiveSort.buildIndex(size());
    final <X>Comparator comparator = c != null ? c : <X>Comparator.NATURAL;
    PrimitiveSort.sortIndexed(p, this, idx, new IntComparator() {
      @Override
      public int compare(final int o1, final int o2) {
        return comparator.compare(get(o1), get(o2));
      }
    });
  }

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
   * {@code lastIndexOf}, and all of the algorithms in the {@link <X>List}
   * class can be applied to a subList.
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
  <X>List subList(int fromIndex, int toIndex);

  /**
   * Compares the specified object with this list for equality. Returns
   * {@code true} if and only if the specified object is also a list, both lists
   * have the same size, and all corresponding pairs of values in the two lists
   * are <i>equal</i>. (Two values {@code v1} and {@code v2} are <i>equal</i> if
   * {@code v1 == v2}.) In other words, two lists are defined to be equal if
   * they contain the same values in the same order.
   *
   * @param obj The object to be compared for equality with this list.
   * @return {@code true} if the specified object is equal to this list.
   */
  @Override
  boolean equals(Object obj);

  /**
   * Returns the hash code value for this list. The hash code algorithm of a
   * list is defined in {@link java.util.List#hashCode()}.
   *
   * @return The hash code value for this list.
   * @see Object#equals(Object)
   * @see #equals(Object)
   */
  @Override
  int hashCode();
}