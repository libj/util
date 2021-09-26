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

import static org.libj.lang.Assertions.*;

/**
 * Represents an operation that accepts three input arguments and returns no
 * result. This is the three-arity specialization of
 * {@link java.util.function.Consumer}. Unlike most other functional interfaces,
 * {@link TriConsumer} is expected to operate via side-effects.
 * <p>
 * This is a <a href="package-summary.html">functional interface</a> whose
 * functional method is {@link #accept(Object,Object,Object)}.
 *
 * @param <T> The type of the first argument to the operation.
 * @param <U> The type of the second argument to the operation.
 * @param <V> The type of the third argument to the operation.
 * @see java.util.function.Consumer
 */
@FunctionalInterface
public interface TriConsumer<T,U,V> {
  /**
   * Performs this operation on the given arguments.
   *
   * @param t The first input argument.
   * @param u The second input argument.
   * @param v The third input argument.
   */
  void accept(T t, U u, V v);

  /**
   * Returns a composed {@link TriConsumer} that performs, in sequence, this
   * operation followed by the {@code after} operation. If performing either
   * operation throws an exception, it is relayed to the caller of the composed
   * operation. If performing this operation throws an exception, the
   * {@code after} operation will not be performed.
   *
   * @param after The operation to perform after this operation.
   * @return A composed {@link TriConsumer} that performs in sequence this
   *         operation followed by the {@code after} operation.
   * @throws IllegalArgumentException if {@code after} is null.
   */
  default TriConsumer<T,U,V> andThen(final TriConsumer<? super T,? super U,? super V> after) {
    assertNotNull(after);
    return (t, u, v) -> {
      accept(t, u, v);
      after.accept(t, u, v);
    };
  }
}