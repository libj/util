/* Copyright (c) 2017 LibJ
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

import java.util.function.Function;

/**
 * Represents a function that accepts three arguments and produces a result. This is the three-arity specialization of
 * {@link Function}. This is a functional interface. whose functional method is {@link #apply(Object,Object,Object)}.
 *
 * @param <T> The type of the first argument to the function.
 * @param <U> The type of the second argument to the function.
 * @param <V> The type of the third argument to the function.
 * @param <R> The type of the result of the function.
 * @see Function
 */
@FunctionalInterface
public interface TriFunction<T,U,V,R> {
  /**
   * Applies this function to the given arguments.
   *
   * @param t The first function argument.
   * @param u The second function argument.
   * @param v The third function argument.
   * @return The function result.
   */
  R apply(T t, U u, V v);

  /**
   * Returns a composed function that first applies this function to its input, and then applies the {@code after} function to the
   * result. If evaluation of either function throws an exception, it is relayed to the caller of the composed function.
   *
   * @param <W> The type of output of the {@code after} function, and of the composed function.
   * @param after The function to apply after this function is applied.
   * @return A composed function that first applies this function and then applies the {@code after} function.
   * @throws IllegalArgumentException If {@code after} is null.
   */
  default <W>TriFunction<T,U,V,W> andThen(final Function<? super R,? extends W> after) {
    assertNotNull(after);
    return (T t, U u, V v) -> after.apply(apply(t, u, v));
  }
}