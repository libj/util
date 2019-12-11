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

import java.util.function.BiFunction;

/**
 * Represents a function that accepts two arguments and produces a result.
 * <p>
 * The {@link ThrowingFunction} distinguishes itself from {@link BiFunction} by
 * allowing the functional interface to throw an {@link Exception}. This can be
 * used to allow lambda expressions to propagate checked exceptions up the
 * expression's call stack. An example of this pattern:
 *
 * <pre>
 * {@code
 * BiFunction<Integer,Integer,String> function = Throwing.<Integer,Integer,String>rethrow((i, j) -> {
 *   if (i == 0)
 *     throw new IllegalArgumentException("i=" + i);
 *   return String.valueOf(i);
 * });
 * for (int i = 3; i >= 0; --i)
 *   function.accept(i, -i);
 * }
 * </pre>
 *
 * @param <T> The type of the first input to the operation.
 * @param <U> The type of the second input to the operation.
 * @param <R> The type of the result of the function.
 * @see Throwing
 */
@FunctionalInterface
public interface ThrowingBiFunction<T,U,R> extends BiFunction<T,U,R> {
  @Override
  default R apply(final T t, final U u) {
    try {
      return applyThrows(t, u);
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
   * @param t The first input argument.
   * @param u The second function argument.
   * @return The function result.
   * @throws Exception If an exception has occurred.
   */
  R applyThrows(T t, U u) throws Exception;
}