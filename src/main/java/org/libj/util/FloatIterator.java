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

package org.libj.util;

import java.util.NoSuchElementException;
import java.util.Objects;

import org.libj.util.function.FloatConsumer;

/**
 * A replica of the {@link java.util.Iterator} interface that defines synonymous
 * methods for the iteration over {@code float} values instead of Object
 * references.
 */
public interface FloatIterator {
  /**
   * Returns {@code true} if the iteration has more values. (In other words,
   * returns {@code true} if {@link #next} would return a value rather than
   * throwing an exception.)
   *
   * @return {@code true} if the iteration has more values.
   */
  boolean hasNext();

  /**
   * Returns the next value in the iteration.
   *
   * @return The next value in the iteration.
   * @throws NoSuchElementException If the iteration has no more values.
   */
  float next();

  /**
   * Removes from the underlying collection the last value returned by this
   * iterator (optional operation). This method can be called only once per call
   * to {@link #next}.
   * <p>
   * The behavior of an iterator is unspecified if the underlying collection is
   * modified while the iteration is in progress in any way other than by
   * calling this method, unless an overriding class has specified a concurrent
   * modification policy.
   * <p>
   * The behavior of an iterator is unspecified if this method is called after a
   * call to the {@link #forEachRemaining(FloatConsumer)} method.
   * <p>
   * The default implementation throws an instance of
   * {@link UnsupportedOperationException} and performs no other action.
   *
   * @throws UnsupportedOperationException If the {@code remove} operation is
   *           not supported by this iterator.
   * @throws IllegalStateException If the {@code next} method has not yet been
   *           called, or the {@code remove} method has already been called
   *           after the last call to the {@code next} method.
   */
  default void remove() {
    throw new UnsupportedOperationException("remove");
  }

  /**
   * Performs the given action for each remaining value until all values have
   * been processed or the action throws an exception. Actions are performed in
   * the order of iteration, if that order is specified. Exceptions thrown by
   * the action are relayed to the caller.
   * <p>
   * The behavior of an iterator is unspecified if the action modifies the
   * collection in any way (even by calling the {@link #remove()} method or
   * other mutator methods of {@link java.util.Iterator} subtypes), unless an
   * overriding class has specified a concurrent modification policy.
   * <p>
   * Subsequent behavior of an iterator is unspecified if the action throws an
   * exception.
   * <p>
   * The default implementation behaves as if:
   *
   * <pre>
   *   while (hasNext())
   *     action.accept(next());
   * </pre>
   *
   * @param action The action to be performed for each value.
   * @throws NullPointerException If the specified action is null.
   */
  default void forEachRemaining(final FloatConsumer action) {
    Objects.requireNonNull(action);
    while (hasNext())
      action.accept(next());
  }
}