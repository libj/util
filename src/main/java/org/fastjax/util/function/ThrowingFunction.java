/* Copyright (c) 2018 FastJAX
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

import java.util.function.Function;

/**
 * Represents a function that accepts one argument and produces a result.
 * <p>
 * The {@code ThrowingFunction} distinguishes itself from {@link Function} by
 * allowing the functional interface to throw an {@code Exception}. This can be
 * used to allow lambda expressions to propagate checked exceptions up the
 * expression's call stack. An example of this pattern:
 * <blockquote><pre>
 * Arrays
 *   .asList(1, 2, 3)
 *   .stream()
 *   .forEach(Throwing.rethrow(i -&gt; {
 *      if (i == 3)
 *        throw new IOException();
 *      return String.valueOf(i);
 *    }))
 *    .forEach(f -&gt; {});
 * </pre></blockquote>
 *
 * @param <T> The type of the input to the operation.
 * @param <R> The type of the result of the function.
 * @see Throwing
 */
@FunctionalInterface
public interface ThrowingFunction<T,R> extends Function<T,R> {
  @Override
  default R apply(final T t) {
    try {
      return applyThrows(t);
    }
    catch (final Exception e) {
      Throwing.rethrow(e);
      return null;
    }
  }

  /**
   * Performs this operation on the given argument, allowing an exception to be
   * thrown.
   *
   * @param t The input argument.
   * @return The function result.
   * @throws Exception If an exception has occurred.
   */
  R applyThrows(T t) throws Exception;
}