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
 * Represents an operation on a single {@code char}-valued operand that produces
 * a {@code char}-valued result. This is the primitive type specialization of
 * {@link java.util.function.UnaryOperator} for {@code char}.
 *
 * @see java.util.function.UnaryOperator
 */
@FunctionalInterface
public interface CharUnaryOperator {
  /**
   * Applies this operator to the given operand.
   *
   * @param operand The operand.
   * @return The operator result.
   */
  char applyAsChar(char operand);

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
   * @see #andThen(CharUnaryOperator)
   */
  default CharUnaryOperator compose(CharUnaryOperator before) {
    Objects.requireNonNull(before);
    return (char v) -> applyAsChar(before.applyAsChar(v));
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
   * @see #compose(CharUnaryOperator)
   */
  default CharUnaryOperator andThen(CharUnaryOperator after) {
    Objects.requireNonNull(after);
    return (char t) -> after.applyAsChar(applyAsChar(t));
  }

  /**
   * Returns a unary operator that always returns its input argument.
   *
   * @return A unary operator that always returns its input argument.
   */
  static CharUnaryOperator identity() {
    return t -> t;
  }
}