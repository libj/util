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

import static org.libj.lang.Assertions.*;

/**
 * Represents an operation that accepts five input arguments and returns no result. This is the five-arity specialization of
 * {@link java.util.function.Consumer}. Unlike most other functional interfaces, {@link PentaConsumer} is expected to operate via
 * side-effects.
 * <p>
 * This is a <a href="package-summary.html">functional interface</a> whose functional method is
 * {@link #accept(Object,Object,Object,Object,Object)}.
 *
 * @param <T> The type of the first argument to the operation.
 * @param <U> The type of the second argument to the operation.
 * @param <V> The type of the third argument to the operation.
 * @param <W> The type of the fourth argument to the operation.
 * @param <X> The type of the fifth argument to the operation.
 * @see java.util.function.Consumer
 */
@FunctionalInterface
public interface PentaConsumer<T,U,V,W,X> {
  /**
   * Performs this operation on the given arguments.
   *
   * @param t The first input argument.
   * @param u The second input argument.
   * @param v The third input argument.
   * @param w The fourth input argument.
   * @param x The fifth input argument.
   */
  void accept(T t, U u, V v, W w, X x);

  /**
   * Returns a composed {@link PentaConsumer} that performs, in sequence, this operation followed by the {@code after} operation. If
   * performing either operation throws an exception, it is relayed to the caller of the composed operation. If performing this
   * operation throws an exception, the {@code after} operation will not be performed.
   *
   * @param after The operation to perform after this operation.
   * @return A composed {@link PentaConsumer} that performs in sequence this operation followed by the {@code after} operation.
   * @throws IllegalArgumentException if {@code after} is null.
   */
  default PentaConsumer<T,U,V,W,X> andThen(final PentaConsumer<? super T,? super U,? super V,? super W,? super X> after) {
    assertNotNull(after);
    return (t, u, v, w, x) -> {
      accept(t, u, v, w, x);
      after.accept(t, u, v, w, x);
    };
  }
}