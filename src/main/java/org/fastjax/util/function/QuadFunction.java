/* Copyright (c) 2017 FastJAX
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

package org.fastjax.util.function;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a function that accepts three arguments and produces a result.
 * This is the four-arity specialization of {@link Function}.
 *
 * This is a functional interface.
 * whose functional method is {@link #apply(Object, Object, Object, Object)}.
 *
 * @param <T>
 *          the type of the first argument to the function
 * @param <U>
 *          the type of the second argument to the function
 * @param <V>
 *          the type of the third argument to the function
 * @param <W>
 *          the type of the fourth argument to the function
 * @param <R>
 *          the type of the result of the function
 *
 * @see Function
 */
@FunctionalInterface
public interface QuadFunction<T,U,V,W,R> {
  /**
   * Applies this function to the given arguments.
   *
   * @param t
   *          the first function argument
   * @param u
   *          the second function argument
   * @param v
   *          the third function argument
   * @param w
   *          the fourth function argument
   * @return the function result
   */
  R apply(final T t, final U u, final V v, final W w);

  /**
   * Returns a composed function that first applies this function to
   * its input, and then applies the {@code after} function to the result.
   * If evaluation of either function throws an exception, it is relayed to
   * the caller of the composed function.
   *
   * @param <X>
   *          the type of output of the {@code after} function, and of the
   *          composed function
   * @param after
   *          the function to apply after this function is applied
   * @return a composed function that first applies this function and then
   *         applies the {@code after} function
   * @throws NullPointerException
   *           if after is null
   */
  default <X>QuadFunction<T,U,V,W,X> andThen(final Function<? super R,? extends X> after) {
    Objects.requireNonNull(after);
    return (T t, U u, V v, W w) -> after.apply(apply(t, u, v, w));
  }
}