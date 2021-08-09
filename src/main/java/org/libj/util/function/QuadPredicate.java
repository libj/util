/* Copyright (c) 2020 LibJ
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

package org.libj.util.function;

import org.libj.lang.Assertions;

/**
 * Represents a predicate (boolean-valued function) of four arguments. This is
 * the four-arity specialization of {@link java.util.function.Predicate}.
 *
 * @param <T> The type of the first argument to the predicate.
 * @param <U> The type of the second argument the predicate.
 * @param <V> The type of the third argument the predicate.
 * @param <W> The type of the fourth argument the predicate.
 * @see java.util.function.Predicate
 */
@FunctionalInterface
public interface QuadPredicate<T,U,V,W> {
  /**
   * Evaluates this predicate on the given arguments.
   *
   * @param t The first input argument.
   * @param u The second input argument.
   * @param v The third input argument.
   * @param w The fourth input argument.
   * @return {@code true} if the input arguments match the predicate, otherwise
   *         {@code false}.
   */
  boolean test(T t, U u, V v, W w);

  /**
   * Returns a composed predicate that represents a short-circuiting logical AND
   * of this predicate and another. When evaluating the composed predicate, if
   * this predicate is {@code false}, then the {@code other} predicate is not
   * evaluated.
   * <p>
   * Any exceptions thrown during evaluation of either predicate are relayed to
   * the caller; if evaluation of this predicate throws an exception, the
   * {@code other} predicate will not be evaluated.
   *
   * @param other A predicate that will be logically-ANDed with this predicate.
   * @return A composed predicate that represents the short-circuiting logical
   *         AND of this predicate and the {@code other} predicate.
   * @throws IllegalArgumentException If {@code other} is null.
   */
  default QuadPredicate<T,U,V,W> and(final QuadPredicate<? super T,? super U,? super V,? super W> other) {
    Assertions.assertNotNull(other);
    return (T t, U u, V v, W w) -> test(t, u, v, w) && other.test(t, u, v, w);
  }

  /**
   * Returns a predicate that represents the logical negation of this predicate.
   *
   * @return A predicate that represents the logical negation of this predicate.
   */
  default QuadPredicate<T,U,V,W> negate() {
    return (T t, U u, V v, W w) -> !test(t, u, v, w);
  }

  /**
   * Returns a composed predicate that represents a short-circuiting logical OR
   * of this predicate and another. When evaluating the composed predicate, if
   * this predicate is {@code true}, then the {@code other} predicate is not
   * evaluated.
   * <p>
   * Any exceptions thrown during evaluation of either predicate are relayed to
   * the caller; if evaluation of this predicate throws an exception, the
   * {@code other} predicate will not be evaluated.
   *
   * @param other A predicate that will be logically-ORed with this predicate.
   * @return A composed predicate that represents the short-circuiting logical
   *         OR of this predicate and the {@code other} predicate.
   * @throws IllegalArgumentException If other is null.
   */
  default QuadPredicate<T,U,V,W> or(final QuadPredicate<? super T,? super U,? super V,? super W> other) {
    Assertions.assertNotNull(other);
    return (T t, U u, V v, W w) -> test(t, u, v, w) || other.test(t, u, v, w);
  }
}