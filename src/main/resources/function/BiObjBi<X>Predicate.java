/* Copyright (c) 2023 LibJ
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

import java.util.Objects;

/**
 * Represents a predicate (boolean-valued function) that accepts two object-valued
 * arguments and two {@code <x>}-valued arguments. This is the {@code <x>}-consuming
 * primitive type specialization of {@link java.util.function.Predicate}.
 *
 * @param <T> The type of the first argument to the predicate.
 * @param <U> The type of the second argument to the predicate.
 *
 * @see java.util.function.Predicate
 */
@FunctionalInterface
public interface BiObjBi<X>Predicate<T,U> {
  /**
   * Evaluates this predicate on the given argument.
   *
   * @param t The first input argument.
   * @param u The second input argument.
   * @param v1 The third input argument.
   * @param v2 The fourth input argument.
   * @return {@code true} if the input argument matches the predicate, otherwise
   *         {@code false}.
   */
  boolean test(T t, U u, <x> v1, <x> v2);

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
   * @param other A predicate that will be logically-ANDed with this predicate
   * @return A composed predicate that represents the short-circuiting logical
   *         AND of this predicate and the {@code other} predicate.
   * @throws NullPointerException If {@code other} is null.
   */
  default BiObjBi<X>Predicate<T,U> and(final BiObjBi<X>Predicate<T,U> other) {
    Objects.requireNonNull(other);
    return (t, u, v1, v2) -> test(t, u, v1, v2) && other.test(t, u, v1, v2);
  }

  /**
   * Returns a predicate that represents the logical negation of this predicate.
   *
   * @return A predicate that represents the logical negation of this predicate.
   */
  default BiObjBi<X>Predicate<T,U> negate() {
    return (t, u, v1, v2) -> !test(t, u, v1, v2);
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
   * @throws NullPointerException if {@code other} is null.
   */
  default BiObjBi<X>Predicate<T,U> or(final BiObjBi<X>Predicate<T,U> other) {
    Objects.requireNonNull(other);
    return (t, u, v1, v2) -> test(t, u, v1, v2) || other.test(t, u, v1, v2);
  }
}