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

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a function that accepts five arguments and produces a result. This is the five-arity specialization of
 * {@link Function}. This is a functional interface. whose functional method is {@link #apply(Object,Object,Object,Object,Object)}.
 *
 * @param <T> The type of the first argument to the function.
 * @param <U> The type of the second argument to the function.
 * @param <V> The type of the third argument to the function.
 * @param <W> The type of the fourth argument to the function.
 * @param <X> The type of the fifth argument to the function.
 * @param <R> The type of the result of the function.
 * @see Function
 */
@FunctionalInterface
public interface PentaFunction<T,U,V,W,X,R> {
  /**
   * Applies this function to the given arguments.
   *
   * @param t The first function argument.
   * @param u The second function argument.
   * @param v The third function argument.
   * @param w The fourth input argument.
   * @param x The fifth input argument.
   * @return The function result.
   */
  R apply(T t, U u, V v, W w, X x);

  /**
   * Returns a composed function that first applies this function to its input, and then applies the {@code after} function to the
   * result. If evaluation of either function throws an exception, it is relayed to the caller of the composed function.
   *
   * @param <S> The type of output of the {@code after} function, and of the composed function.
   * @param after The function to apply after this function is applied.
   * @return A composed function that first applies this function and then applies the {@code after} function.
   * @throws NullPointerException If {@code after} is null.
   */
  default <S> PentaFunction<T,U,V,W,X,S> andThen(final Function<? super R,? extends S> after) {
    Objects.requireNonNull(after);
    return (T t, U u, V v, W w, X x) -> after.apply(apply(t, u, v, w, x));
  }
}