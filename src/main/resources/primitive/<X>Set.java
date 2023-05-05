/* Copyright (c) 2018 LibJ
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

/**
 * A collection of {@code <x>} values that contains no duplicates. More
 * formally, <x>-sets contain no pair of values {@code v1} and {@code v2} such
 * that {@code e1 == e2}. As implied by its name, this interface models the
 * mathematical <i>set</i> abstraction.
 * <p>
 * This interface is a replica of the {@link java.util.Set} interface that defines
 * synonymous methods for a list of {@code <x>} values instead of Object
 * references.
 */
public interface <X>Set extends <X>Collection {
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
  boolean add(<x> value);

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
   *           collection is not {@link <XX>}.
   * @throws NullPointerException If the specified collection contains one or
   *           more null values, or if the specified collection is null.
   * @see #addAll(<X>Collection)
   * @see #add(<x>)
   */
  @Override
  boolean addAll(Collection<<XX>> c);

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
   * @see #add(<x>)
   */
  @Override
  boolean addAll(<X>Collection c);

  /**
   * Returns {@code true} if this set contains the specified value. More
   * formally, returns {@code true} if and only if this set contains a value
   * {@code v} such that {@code Objects.equals(value, v)}.
   *
   * @param value Value whose presence in this set is to be tested.
   * @return {@code true} if this set contains the specified value.
   */
  @Override
  boolean contains(<x> value);

  /**
   * Returns {@code true} if this set contains all of the values of the
   * specified collection. If the specified collection is also a set, this
   * method returns {@code true} if it is a <i>subset</i> of this set.
   *
   * @param c Collection to be checked for containment in this set.
   * @return {@code true} if this set contains all of the values of the
   *         specified collection.
   * @throws ClassCastException If the class of an element of the specified
   *           collection is not {@link <XX>}.
   * @throws NullPointerException If the specified collection contains one or
   *           more null values, or if the specified collection is null.
   * @see #containsAll(<X>Collection)
   * @see #contains(<x>)
   */
  @Override
  boolean containsAll(Collection<<XX>> c);

  /**
   * Returns {@code true} if this set contains all of the values of the
   * specified collection. If the specified collection is also a set, this
   * method returns {@code true} if it is a <i>subset</i> of this set.
   *
   * @param c Collection to be checked for containment in this set.
   * @return {@code true} if this set contains all of the values of the
   *         specified collection.
   * @see #containsAll(Collection)
   * @see #contains(<x>)
   */
  @Override
  boolean containsAll(<X>Collection c);

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
  boolean remove(<x> value);

  /**
   * Removes from this set all of its values that are contained in the specified
   * collection. If the specified collection is also a set, this operation
   * effectively modifies this set so that its value is the <i>asymmetric set
   * difference</i> of the two sets.
   *
   * @param c Collection containing values to be removed from this set.
   * @return {@code true} if this set changed as a result of the call.
   * @throws ClassCastException If the class of an element of the specified
   *           collection is not {@link <XX>}.
   * @throws NullPointerException If the specified collection contains a null
   *           value, or if the specified collection is null.
   * @see #removeAll(<X>Collection)
   * @see #remove(<x>)
   * @see #contains(<x>)
   */
  @Override
  boolean removeAll(Collection<<XX>> c);

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
   * @see #remove(<x>)
   * @see #contains(<x>)
   */
  @Override
  boolean removeAll(<X>Collection c);

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
   *           collection is not {@link <XX>}.
   * @throws NullPointerException If the specified collection contains a null
   *           value, or if the specified collection is null.
   * @see #retainAll(<X>Collection)
   * @see #remove(<x>)
   * @see #contains(<x>)
   */
  @Override
  boolean retainAll(Collection<<XX>> c);

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
   * @see #remove(<x>)
   * @see #contains(<x>)
   */
  @Override
  boolean retainAll(<X>Collection c);

  /**
   * Removes all of the values from this set. The set will be empty after this
   * call returns.
   */
  @Override
  void clear();

  /**
   * Returns the number of values in this set (its cardinality). If this set
   * contains more than {@link <XX>#<M>} values, returns
   * {@link <XX>#<M>}.
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
   * Returns an iterator over the values in this set. The values are returned in
   * no particular order (unless this set is an instance of some class that
   * provides a guarantee).
   *
   * @return An iterator over the values in this set.
   */
  @Override
  <X>Iterator iterator();

<_>  /**
<_>   * Creates a {@code Spliterator.Of<X>} over the elements in this set.
<_>   * <p>
<_>   * The {@code Spliterator.Of<X>} reports {@link Spliterator#DISTINCT}.
<_>   *
<_>   * @return A {@code Spliterator.Of<X>} over the elements in this set.
<_>   * @see java.util.Spliterator.Of<X>
<_>   */
<_>  @Override
<_>  Spliterator.Of<X> spliterator();

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
   * @see <X>Set#equals(Object)
   */
  @Override
  int hashCode();
}