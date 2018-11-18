/* Copyright (c) 2018 FastJAX
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
import java.util.Set;
import java.util.Spliterator;

/**
 * A collection of {@code long} values that contains no duplicates. More
 * formally, long-sets contain no pair of values {@code v1} and {@code v2} such
 * that {@code e1 == e2}. As implied by its name, this interface models the
 * mathematical <i>set</i> abstraction.
 * <p>
 * This interface is a replica of the {@link Set} interface that defines
 * synonymous methods for a list of {@code long} values instead of Object
 * references.
 */
public interface LongSet extends LongCollection {
  /**
   * Adds the specified value to this set if it is not already present. More
   * formally, adds the specified value {@code value} to this set if the set
   * contains no value {@code value2} such that {@code value == value2}. If this
   * set already contains the value, the call leaves the set unchanged and
   * returns {@code false}. In combination with the restriction on constructors,
   * this ensures that sets never contain duplicate values.
   *
   * @param value Value to be added to this set.
   * @return {@code true} if this set did not already contain the specified
   *         value.
   */
  @Override
  boolean add(long value);

  /**
   * Adds all of the values in the specified collection to this set if they're
   * not already present. If the specified collection is also a set, the
   * {@code addAll} operation effectively modifies this set so that its value is
   * the <i>union</i> of the two sets. The behavior of this operation is
   * undefined if the specified collection is modified while the operation is in
   * progress.
   *
   * @param c Collection containing values to be added to this set.
   * @return {@code true} if this set changed as a result of the call.
   * @throws ClassCastException If the class of an element of the specified
   *           collection is not {@code Long}.
   * @throws NullPointerException If the specified collection contains one or
   *           more null values, or if the specified collection is null.
   * @see #addAll(LongCollection)
   * @see #add(long)
   */
  @Override
  boolean addAll(Collection<Long> c);

  /**
   * Adds all of the values in the specified collection to this set if they're
   * not already present. If the specified collection is also a set, the
   * {@code addAll} operation effectively modifies this set so that its value is
   * the <i>union</i> of the two sets. The behavior of this operation is
   * undefined if the specified collection is modified while the operation is in
   * progress.
   *
   * @param c Collection containing values to be added to this set.
   * @return {@code true} if this set changed as a result of the call.
   * @throws NullPointerException If the specified collection contains one or
   *           more null values, or if the specified collection is null.
   * @see #addAll(Collection)
   * @see #add(long)
   */
  @Override
  boolean addAll(LongCollection c);

  /**
   * Returns {@code true} if this set contains the specified value. More
   * formally, returns {@code true} if and only if this set contains a value
   * {@code v} such that {@code Objects.equals(value, v)}.
   *
   * @param value Value whose presence in this set is to be tested.
   * @return {@code true} if this set contains the specified value.
   */
  @Override
  boolean contains(long value);

  /**
   * Returns {@code true} if this set contains all of the values of the
   * specified collection. If the specified collection is also a set, this
   * method returns {@code true} if it is a <i>subset</i> of this set.
   *
   * @param c Collection to be checked for containment in this set.
   * @return {@code true} if this set contains all of the values of the
   *         specified collection.
   * @throws ClassCastException If the class of an element of the specified
   *           collection is not {@code Long}.
   * @throws NullPointerException If the specified collection contains one or
   *           more null values, or if the specified collection is null.
   * @see #containsAll(LongCollection)
   * @see #contains(long)
   */
  @Override
  boolean containsAll(Collection<Long> c);

  /**
   * Returns {@code true} if this set contains all of the values of the
   * specified collection. If the specified collection is also a set, this
   * method returns {@code true} if it is a <i>subset</i> of this set.
   *
   * @param c Collection to be checked for containment in this set.
   * @return {@code true} if this set contains all of the values of the
   *         specified collection.
   * @see #containsAll(Collection)
   * @see #contains(long)
   */
  @Override
  boolean containsAll(LongCollection c);

  /**
   * Removes the specified value from this set if it is present. More formally,
   * removes an value {@code v} such that {@code Objects.equals(value, v)}, if
   * this set contains such an value. Returns {@code true} if this set contained
   * the value (or equivalently, if this set changed as a result of the call).
   * (This set will not contain the value once the call returns).
   *
   * @param value Value to be removed from this set, if present.
   * @return {@code true} if this set contained the specified value.
   */
  @Override
  boolean remove(long value);

  /**
   * Removes from this set all of its values that are contained in the specified
   * collection. If the specified collection is also a set, this operation
   * effectively modifies this set so that its value is the <i>asymmetric set
   * difference</i> of the two sets.
   *
   * @param c Collection containing values to be removed from this set.
   * @return {@code true} if this set changed as a result of the call.
   * @throws ClassCastException If the class of an element of the specified
   *           collection is not {@code Long}.
   * @throws NullPointerException If the specified collection contains a null
   *           value, or if the specified collection is null.
   * @see #removeAll(LongCollection)
   * @see #remove(long)
   * @see #contains(long)
   */
  @Override
  boolean removeAll(Collection<Long> c);

  /**
   * Removes from this set all of its values that are contained in the specified
   * collection. If the specified collection is also a set, this operation
   * effectively modifies this set so that its value is the <i>asymmetric set
   * difference</i> of the two sets.
   *
   * @param c Collection containing values to be removed from this set.
   * @return {@code true} if this set changed as a result of the call.
   * @throws NullPointerException If the specified collection contains a null
   *           value, or if the specified collection is null.
   * @see #removeAll(Collection)
   * @see #remove(long)
   * @see #contains(long)
   */
  @Override
  boolean removeAll(LongCollection c);

  /**
   * Retains only the values in this set that are contained in the specified
   * collection. In other words, removes from this set all of its values that
   * are not contained in the specified collection. If the specified collection
   * is also a set, this operation effectively modifies this set so that its
   * value is the <i>intersection</i> of the two sets.
   *
   * @param c Collection containing values to be retained in this set.
   * @return {@code true} if this set changed as a result of the call.
   * @throws ClassCastException If the class of an element of the specified
   *           collection is not {@code Long}.
   * @throws NullPointerException If the specified collection contains a null
   *           value, or if the specified collection is null.
   * @see #retainAll(LongCollection)
   * @see #remove(long)
   * @see #contains(long)
   */
  @Override
  boolean retainAll(Collection<Long> c);

  /**
   * Retains only the values in this set that are contained in the specified
   * collection. In other words, removes from this set all of its values that
   * are not contained in the specified collection. If the specified collection
   * is also a set, this operation effectively modifies this set so that its
   * value is the <i>intersection</i> of the two sets.
   *
   * @param c Collection containing values to be retained in this set.
   * @return {@code true} if this set changed as a result of the call.
   * @throws NullPointerException If the specified collection contains a null
   *           value, or if the specified collection is null.
   * @see #retainAll(Collection)
   * @see #remove(long)
   * @see #contains(long)
   */
  @Override
  boolean retainAll(LongCollection c);

  /**
   * Removes all of the values from this set. The set will be empty after this
   * call returns.
   */
  @Override
  void clear();

  /**
   * Returns the number of values in this set (its cardinality). If this set
   * contains more than {@code Long.MAX_VALUE} values, returns
   * {@code Long.MAX_VALUE}.
   *
   * @return The number of values in this set (its cardinality).
   */
  @Override
  int size();

  /**
   * Returns {@code true} if this set contains no values.
   *
   * @return {@code true} if this set contains no values.
   */
  @Override
  boolean isEmpty();

  /**
   * Returns an array containing all of the values in this set. If this set
   * makes any guarantees as to what order its values are returned by its
   * iterator, this method must return the values in the same order.
   * <p>
   * The returned array will be "safe" in that no references to it are
   * maintained by this set. (In other words, this method must allocate a new
   * array even if this set is backed by an array). The caller is thus free to
   * modify the returned array.
   *
   * @return An array containing all the values in this set.
   */
  default long[] toArray() {
    return toArray(new long[size()]);
  }

  /**
   * Returns an array containing all of the values in this set. If the set fits
   * in the specified array, it is returned therein. Otherwise, a new array is
   * allocated with the size of this set.
   * <p>
   * If this set fits in the specified array with room to spare (i.e., the array
   * has more values than this set), the value in the array immediately
   * following the end of the set is set to {@code 0}. (This is useful in
   * determining the length of this set <i>only</i> if the caller knows that
   * this set does not contain any {@code 0} values).
   * <p>
   * If this set makes any guarantees as to what order its values are returned
   * by its iterator, this method must return the values in the same order.
   * <p>
   * Suppose {@code x} is a set known to contain only strings. The following
   * code can be used to dump the set into a newly allocated array of
   * {@code String}:
   *
   * <pre>
   * long[] y = x.toArray(new long[0]);
   * </pre>
   *
   * Note that {@code toArray(new long[0])} is identical in function to
   * {@code toArray()}.
   *
   * @param a The array into which the values of this set are to be stored, if
   *          it is big enough; otherwise, a new array is allocated for this
   *          purpose.
   * @return An array containing all the values in this set.
   * @throws NullPointerException If the specified array is null.
   */
  long[] toArray(long[] a);

  /**
   * Returns an array containing all of the values in this set; the runtime type
   * of the returned array is that of the specified array. If the set fits in
   * the specified array, it is returned therein. Otherwise, a new array is
   * allocated with the runtime type of the specified array and the size of this
   * set.
   * <p>
   * If this set fits in the specified array with room to spare (i.e., the array
   * has more values than this set), the value in the array immediately
   * following the end of the set is set to {@code null}.
   * <p>
   * If this set makes any guarantees as to what order its values are returned
   * by its iterator, this method must return the values in the same order.
   * <p>
   * Suppose {@code x} is a set known to contain only strings. The following
   * code can be used to dump the set into a newly allocated array of
   * {@code String}:
   *
   * <pre>
   * Long[] y = x.toArray(new Long[0]);
   * </pre>
   *
   * @param a The array into which the values of this set are to be stored, if
   *          it is big enough; otherwise, a new array is allocated for this
   *          purpose.
   * @return An array containing all the values in this set.
   * @throws NullPointerException If the specified array is null.
   */
  Long[] toArray(Long[] a);

  /**
   * Returns an iterator over the values in this set. The values are returned in
   * no particular order (unless this set is an instance of some class that
   * provides a guarantee).
   *
   * @return An iterator over the values in this set.
   */
  @Override
  LongIterator iterator();

  /**
   * Creates a {@code Spliterator.OfLong} over the elements in this set.
   * <p>
   * The {@code Spliterator.OfLong} reports {@link Spliterator#DISTINCT}.
   *
   * @return A {@code Spliterator.OfLong} over the elements in this set.
   * @see Spliterator.OfLong
   */
  @Override
  Spliterator.OfLong spliterator();

  /**
   * Compares the specified object with this set for equality. Returns
   * {@code true} if the specified object is also a set, the two sets have the
   * same size, and every member of the specified set is contained in this set
   * (or equivalently, every member of this set is contained in the specified
   * set). This definition ensures that the equals method works properly across
   * different implementations of the set interface.
   *
   * @param obj Object to be compared for equality with this set.
   * @return {@code true} if the specified object is equal to this set.
   */
  @Override
  boolean equals(Object obj);

  /**
   * Returns the hash code value for this set. The hash code of a set is defined
   * to be the sum of the hash codes of the values in the set. This ensures that
   * {@code s1 == s2} implies that {@code s1.hashCode() == s2.hashCode()} for
   * any two sets {@code s1} and {@code s2}, as required by the general contract
   * of {@link Object#hashCode}.
   *
   * @return The hash code value for this set.
   * @see Object#equals(Object)
   * @see LongSet#equals(Object)
   */
  @Override
  int hashCode();
}