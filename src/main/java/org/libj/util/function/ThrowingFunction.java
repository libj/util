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

import java.util.function.Function;

/**
 * Represents a function that accepts one argument and produces a result.
 * <p>
 * The {@link ThrowingFunction} distinguishes itself from {@link Function} by
 * allowing the functional interface to throw an {@link Exception}. This can be
 * used to allow lambda expressions to propagate checked exceptions up the
 * expression's call stack. An example of this pattern:
 *
 * <pre>
 * {@code
 * Arrays
 *   .asList(2, 1, 0)
 *   .stream()
 *   .map(Throwing.rethrow((Integer i) -> {
 *     if (i == 0)
 *       throw new IOException("i=" + i);
 *     return String.valueOf(i);
 *   }))
 *   .forEach(f -> {});
 * }
 * </pre>
 *
 * @param <T> The type of the input to the operation.
 * @param <R> The type of the result of the function.
 * @param <E> The type of the exception that can be thrown.
 * @see Throwing#rethrow(ThrowingFunction)
 */
@FunctionalInterface
public interface ThrowingFunction<T,R,E extends Exception> extends Function<T,R> {
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
   * @throws E If an exception has occurred.
   * @see Function#apply(Object)
   */
  R applyThrows(T t) throws E;
}