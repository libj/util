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

/**
 * The root interface in the <i>primitive collection hierarchy</i>. A primitive
 * collection represents a group of primitive values, such as of types
 * {@code byte}, {@code short}, {@code char}, {@code int}, {@code long},
 * {@code float}, {@code double}, and {@code boolean}. Some collections allow
 * duplicate elements and others do not. Some are ordered and others unordered.
 * <p>
 * The architecture of the <i>primitive collection hierarchy</i> is designed to
 * mimic that of the <i>collection hierarchy</i> in
 * {@link java.util.Collection}, in order to provide a nearly identical API for
 * ease of use and interoperability.
 *
 * @see IntList
 * @see LongList
 * @see ArrayIntList
 * @see ArrayLongList
 * @see IntSet
 * @see HashIntSet
 * @see LongSet
 * @see HashLongSet
 */
public interface PrimitiveCollection {
  /**
   * Removes all of the values from this collection. The collection will be
   * empty after this call returns.
   */
  void clear();

  /**
   * Returns the number of values in this collection. If this collection
   * contains more than {@code Integer.MAX_VALUE} values, returns
   * {@code Integer.MAX_VALUE}.
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

  /**
   * Compares the specified object with this collection for equality.
   * <p>
   * While the {@code IntCollection} interface adds no stipulations to the
   * general contract for the {@code Object.equals}, programmers who implement
   * the {@code IntCollection} interface "directly" (in other words, create a
   * class that is a {@code IntCollection} but is not a {@code IntSet} or a
   * {@code IntList}) must exercise care if they choose to override the
   * {@code Object.equals}. It is not necessary to do so, and the simplest
   * course of action is to rely on {@code Object}'s implementation, but the
   * implementor may wish to implement a "value comparison" in place of the
   * default "reference comparison." (The {@code IntList} and {@code IntSet}
   * interfaces mandate such value comparisons.)
   * <p>
   * The general contract for the {@code Object.equals} method states that
   * equals must be symmetric (in other words, {@code a.equals(b)} if and only
   * if {@code b.equals(a)}). The contracts for {@code IntList.equals} and
   * {@code IntSet.equals} state that lists are only equal to other lists, and
   * sets to other sets. Thus, a custom {@code equals} method for a collection
   * class that implements neither the {@code IntList} nor {@code IntSet}
   * interface must return {@code false} when this collection is compared to any
   * list or set. (By the same logic, it is not possible to write a class that
   * correctly implements both the {@code IntSet} and {@code IntList}
   * interfaces.)
   *
   * @param obj Object to be compared for equality with this collection.
   * @return {@code true} if the specified object is equal to this collection.
   * @see Object#equals(Object)
   * @see IntSet#equals(Object)
   * @see IntList#equals(Object)
   */
  @Override
  boolean equals(Object obj);

  /**
   * Returns the hash code value for this collection. While the
   * {@code IntCollection} interface adds no stipulations to the general
   * contract for the {@code Object.hashCode} method, programmers should take
   * note that any class that overrides the {@code Object.equals} method must
   * also override the {@code Object.hashCode} method in order to satisfy the
   * general contract for the {@code Object.hashCode} method. In particular,
   * {@code c1.equals(c2)} implies that {@code c1.hashCode()==c2.hashCode()}.
   *
   * @return The hash code value for this collection.
   * @see Object#hashCode()
   * @see Object#equals(Object)
   */
  @Override
  int hashCode();
}