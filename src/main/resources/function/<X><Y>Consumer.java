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

package org.libj.util.function;

import java.util.Objects;

/**
 * Represents an operation that accepts a {@code <x>}-valued argument and a
 * {@code <y>}-valued argument and returns no result. Unlike most other functional
 * interfaces, {@link <X><Y>Consumer} is expected to operate via side-effects.
 */
@FunctionalInterface
public interface <X><Y>Consumer {
  /**
   * Performs this operation on the given argument.
   *
   * @param v1 The first input argument.
   * @param v2 The second input argument.
   */
  void accept(<x> v1, <y> v2);

  /**
   * Returns a composed {@link <X><Y>Consumer} that performs, in sequence, this
   * operation followed by the {@code after} operation. If performing either
   * operation throws an exception, it is relayed to the caller of the composed
   * operation. If performing this operation throws an exception, the
   * {@code after} operation will not be performed.
   *
   * @param after The operation to perform after this operation.
   * @return A composed {@link <X><Y>Consumer} that performs in sequence this
   *         operation followed by the {@code after} operation.
   * @throws NullPointerException If {@code after} is null.
   */
  default <X><Y>Consumer andThen(final <X><Y>Consumer after) {
    Objects.requireNonNull(after);
    return (v1, v2) -> {
      accept(v1, v2);
      after.accept(v1, v2);
    };
  }
}