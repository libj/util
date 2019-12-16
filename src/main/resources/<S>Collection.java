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
import java.util.Objects;

/**
 * The root interface in the <i>{@code <t>} collection hierarchy</i>. A
 * primitive collection represents a group of {@code <t>} values. Some
 * collections allow duplicate elements and others do not. Some are ordered and
 * others unordered.
 * <p>
 * The architecture of the <i>{@code <t>} collection hierarchy</i> is designed
 * to mimic that of the <i>collection hierarchy</i> in {@link Collection}, in
 * order to provide a nearly identical API for ease of use and interoperability.
 *
 * @see <S>List
 * @see Array<S>List
 * @see <S>Set
 * @see Hash<S>Set
 */
public interface <S>Collection extends <S>Iterable, PrimitiveCollection {
  /**
   * Ensures that this collection contains the specified value (optional
   * operation). Returns {@code true} if this collection changed as a result of
   * the call. (Returns {@code false} if this collection does not permit
   * duplicates and already contains the specified value).
   *
   * @param value Value whose presence in this collection is to be ensured.
   * @return {@code true} if this collection changed as a result of the call.
   */
  boolean add(<t> value);

  /**
   * Appends all of the values in the specified array to the end of this
   * collection, in the order that they appear in the array.
   *
   * @param a Array containing values to be added to this collection.
   * @return {@code true} if this collection changed as a result of the call.
   * @throws NullPointerException If the specified array is null.
   */
  default boolean addAll(final <t> ... a) {
    boolean changed = false;
    for (int i = 0; i < a.length; ++i)
      changed |= add(a[i]);

    return changed;
  }

  /**
   * Appends all of the values in the specified collection to the end of this
   * collection, in the order that they are returned by the specified
   * collection's {@link <S>Iterator}. The behavior of this operation is
   * undefined if the specified collection is modified while the operation is in
   * progress. (This implies that the behavior of this call is undefined if the
   * specified collection is this collection, and this collection is nonempty).
   *
   * @param c Collection containing values to be added to this collection.
   * @return {@code true} if this collection changed as a result of the call.
   * @throws NullPointerException If the specified collection is null.
   */
  boolean addAll(<S>Collection c);

  /**
   * Appends all of the values in the specified collection to the end of this
   * collection, in the order that they are returned by the specified
   * collection's {@link Iterator}. The behavior of this operation is undefined
   * if the specified collection is modified while the operation is in progress.
   *
   * @param c Collection containing values to be added to this collection.
   * @return {@code true} if this collection changed as a result of the call.
   * @throws ClassCastException If the class of an element of the specified
   *           collection is not {@link <T>}.
   * @throws NullPointerException If the specified collection is null, or if the
   *           specified collection contains a null value.
   */
  boolean addAll(Collection<<T>> c);

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
  boolean remove(<t> value);

  /**
   * Removes all of this collection's values that are also contained in the
   * specified vararg array. After this call returns, this collection will
   * contain no values in common with the specified array.
   *
   * @param a Array containing values to be removed from this collection.
   * @return {@code true} if this collection changed as a result of the call.
   * @throws NullPointerException If the specified array is null.
   * @see #remove(<t>)
   * @see #contains(<t>)
   */
  boolean removeAll(<t> ... a);

  /**
   * Removes all of this collection's values that are also contained in the
   * specified collection. After this call returns, this collection will contain
   * no values in common with the specified collection.
   *
   * @param c Collection containing values to be removed from this collection.
   * @return {@code true} if this collection changed as a result of the call.
   * @throws NullPointerException If the specified collection is null.
   * @see #remove(<t>)
   * @see #contains(<t>)
   */
  boolean removeAll(<S>Collection c);

  /**
   * Removes all of this collection's values that are also contained in the
   * specified collection. After this call returns, this collection will contain
   * no values in common with the specified collection.
   *
   * @param c Collection containing values to be removed from this collection.
   * @return {@code true} if this collection changed as a result of the call.
   * @throws ClassCastException If the class of an element of the specified
   *           collection is not an {@link <T>}.
   * @throws NullPointerException If the specified collection is null, or if the
   *           specified collection contains a null value.
   * @see #remove(<t>)
   * @see #contains(<t>)
   */
  default boolean removeAll(final Collection<<T>> c) {
    final int beforeSize = size();
    for (final Iterator<<T>> i = c.iterator(); i.hasNext();) {
      final <t> value = i.next();
      while (remove(value));
    }

    return beforeSize != size();
  }

  /**
   * Removes all of the values of this collection that satisfy the given
   * predicate. Errors or runtime exceptions thrown during iteration or by the
   * predicate are relayed to the caller.
   * <p>
   * The default implementation traverses all values of the collection using its
   * {@link #iterator()}. Each matching value is removed using
   * {@link IntIterator#remove()}. If the collection's iterator does not support
   * removal then an {@link UnsupportedOperationException} will be thrown on the
   * first matching value.
   *
   * @param filter A predicate which returns {@code true} for values to be
   *          removed.
   * @return {@code true} if any values were removed.
   * @throws NullPointerException If the specified filter is null.
   * @throws UnsupportedOperationException If values cannot be removed from
   *           this collection. Implementations may throw this exception if a
   *           matching value cannot be removed or if, in general, removal is
   *           not supported.
   */
  default boolean removeIf(final <S>Predicate filter) {
    Objects.requireNonNull(filter);
    boolean removed = false;
    final <S>Iterator each = iterator();
    while (each.hasNext()) {
      if (filter.test(each.next())) {
        each.remove();
        removed = true;
      }
    }

    return removed;
  }

  /**
   * Retains only the values in this collection that are contained in the
   * specified collection. In other words, removes from this collection all of
   * its values that are not contained in the specified collection.
   *
   * @param c Collection containing values to be retained in this collection.
   * @return {@code true} if this collection changed as a result of the call.
   * @throws ClassCastException If the class of an element of the specified
   *           collection is not {@link <T>}.
   * @throws NullPointerException If the specified collection is null, or if the
   *           specified collection contains a null value.
   * @see #remove(<t>)
   * @see #contains(<t>)
   */
  boolean retainAll(Collection<<T>> c);

  /**
   * Retains only the values in this collection that are contained in the
   * specified collection. In other words, removes from this collection all of
   * its values that are not contained in the specified collection.
   *
   * @param c Collection containing values to be retained in this collection.
   * @return {@code true} if this collection changed as a result of the call.
   * @throws NullPointerException If the specified collection is null.
   * @see #remove(<t>)
   * @see #contains(<t>)
   */
  boolean retainAll(<S>Collection c);

  /**
   * Returns {@code true} if this collection contains the specified value. More
   * formally, returns {@code true} if and only if this collection contains at
   * least one value {@code v} such that {@code Objects.equals(o, e)}.
   *
   * @param value Value whose presence in this collection is to be tested
   * @return {@code true} if this collection contains the specified value.
   */
  boolean contains(<t> value);

  /**
   * Returns {@code true} if this collection contains all of the values in the
   * specified collection.
   *
   * @param c Collection to be checked for containment in this collection.
   * @return {@code true} if this collection contains all of the values in the
   *         specified collection.
   * @throws NullPointerException If the specified collection is null.
   * @see #contains(<t>)
   */
  default boolean containsAll(final <S>Collection c) {
    for (final <S>Iterator i = c.iterator(); i.hasNext();)
      if (!contains(i.next()))
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
   * @throws ClassCastException If the class of an element of the specified
   *           collection is not an {@link <T>}.
   * @throws NullPointerException If the specified collection is null, or if the
   *           specified collection contains a null value.
   * @see #contains(<t>)
   */
  default boolean containsAll(final Collection<<T>> c) {
    for (final Iterator<<T>> i = c.iterator(); i.hasNext();)
      if (!contains(i.next()))
        return false;

    return true;
  }

  /**
   * Returns an array containing all of the values in this collection in proper
   * sequence (from first to last value). A new array is allocated with the size
   * of this collection.
   * <p>
   * The returned array will be "safe" in that no references to it are
   * maintained by this collection. (In other words, this method must allocate a
   * new array even if this collection is backed by an array). The caller is
   * thus free to modify the returned array.
   *
   * @return An array containing all of the values in this collection in proper
   *         sequence.
   */
  default <t>[] toArray() {
    return toArray(new <t>[size()]);
  }

  /**
   * Returns an array containing all of the values in this collection in proper
   * sequence (from first to last value). If the collection fits in the
   * specified array, it is returned therein. Otherwise, a new array is
   * allocated with the size of this collection.
   * <p>
   * If the collection fits in the specified array with room to spare (i.e., the
   * array has more values than the collection), the value in the array
   * immediately following the end of the collection is set to {@code 0}. (This
   * is useful in determining the length of the collection <i>only</i> if the
   * caller knows that the collection does not contain any {@code 0} values).
   *
   * @param a The array into which the values of the collection are to be
   *          stored. If the array is not big enough to store the values, a new
   *          array is allocated.
   * @return An array containing the values of the collection.
   * @throws NullPointerException If the specified array is null.
   */
  <t>[] toArray(<t>[] a);

  /**
   * Returns an array containing all of the values in this collection in proper
   * sequence (from first to last value). If the collection fits in the
   * specified array, it is returned therein. Otherwise, a new array is
   * allocated with the size of this collection.
   * <p>
   * If the collection fits in the specified array with room to spare (i.e., the
   * array has more values than the collection), the value in the array
   * immediately following the end of the collection is set to null.
   *
   * @param a The array into which the values of the collection are to be
   *          stored. If the array is not big enough to store the values, a new
   *          array is allocated.
   * @return An array containing the values of the collection.
   * @throws NullPointerException If the specified array is null.
   */
  <T>[] toArray(<T>[] a);

  // FIXME: <S>Stream
<_>  /**
<_>   * Creates a {@link java.util.Spliterator.Of<S>} over the values in this collection.
<_>   *
<_>   * @return A {@code Spliterator.Of<S>} over the values in this collection.
<_>   */
<_>  Spliterator.Of<S> spliterator();
<_>
<_>  /**
<_>   * Returns a sequential {@link <S>Stream} over the values in this collection.
<_>   *
<_>   * @return A sequential {@link <S>Stream} over the values in this collection.
<_>   */
<_>  <S>Stream stream();
<_>
<_>  /**
<_>   * Returns a possibly parallel {@link <S>Stream} over the values in this
<_>   * collection.
<_>   *
<_>   * @return A possibly parallel {@link <S>Stream} over the values in this
<_>   *         collection.
<_>   */
<_>  <S>Stream parallelStream();
}