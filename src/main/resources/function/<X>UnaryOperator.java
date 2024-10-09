/* Copyright (c) 2019 LibJ
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
 * Represents an operation on a single {@code <x>}-valued operand that
 * produces a {@code <x>}-valued result. This is the primitive type
 * specialization of {@link java.util.function.UnaryOperator} for {@code <x>}.
 *
 * @see java.util.function.UnaryOperator
 */
@FunctionalInterface
public interface <X>UnaryOperator {
  /**
   * Applies this operator to the given operand.
   *
   * @param operand The operand.
   * @return The operator result.
   */
  <x> applyAs<X>(<x> operand);

  /**
   * Returns a composed operator that first applies the {@code before} operator
   * to its input, and then applies this operator to the result. If evaluation
   * of either operator throws an exception, it is relayed to the caller of the
   * composed operator.
   *
   * @param before The operator to apply before this operator is applied.
   * @return A composed operator that first applies the {@code before} operator
   *         and then applies this operator.
   * @throws NullPointerException If {@code before} is null.
   * @see #andThen(<X>UnaryOperator)
   */
  default <X>UnaryOperator compose(final <X>UnaryOperator before) {
    Objects.requireNonNull(before);
    return (final <x> v) -> applyAs<X>(before.applyAs<X>(v));
  }

  /**
   * Returns a composed operator that first applies this operator to its input,
   * and then applies the {@code after} operator to the result. If evaluation of
   * either operator throws an exception, it is relayed to the caller of the
   * composed operator.
   *
   * @param after The operator to apply after this operator is applied.
   * @return A composed operator that first applies this operator and then
   *         applies the {@code after} operator.
   * @throws NullPointerException If {@code after} is null.
   * @see #compose(<X>UnaryOperator)
   */
  default <X>UnaryOperator andThen(final <X>UnaryOperator after) {
    Objects.requireNonNull(after);
    return (final <x> t) -> after.applyAs<X>(applyAs<X>(t));
  }

  /**
   * Returns a unary operator that always returns its input argument.
   *
   * @return A unary operator that always returns its input argument.
   */
  static <X>UnaryOperator identity() {
    return (final <x> t) -> t;
  }
}