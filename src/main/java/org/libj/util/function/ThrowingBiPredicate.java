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

import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) that accepts two arguments.
 * <p>
 * The {@link ThrowingBiPredicate} distinguishes itself from {@link BiPredicate} by allowing the functional interface to throw an
 * {@link Exception}. This can be used to allow lambda expressions to propagate checked exceptions up the expression's call stack.
 * An example of this pattern:
 *
 * <pre>
 * {@code
 * BiPredicate<Integer,Integer> predicate = Throwing.<Integer,Integer>rethrow((i, j) -> {
 *   if (i == 0)
 *     throw new IllegalArgumentException("i=" + i);
 *   return false;
 * });
 * for (int i = 3; i >= 0; --i)
 *   predicate.accept(i, -i);
 * }
 * </pre>
 *
 * @param <T> The type of the first argument to the predicate.
 * @param <U> The type of the second argument to the predicate.
 * @param <E> The type of the exception that can be thrown.
 * @see Throwing#rethrow(ThrowingBiPredicate)
 */
@FunctionalInterface
public interface ThrowingBiPredicate<T,U,E extends Exception> extends BiPredicate<T,U> {
  /**
   * Evaluates this predicate on the given arguments.
   *
   * @param t The first input argument.
   * @param u The second input argument.
   * @return {@code true} if the input argument matches the predicate, otherwise {@code false}.
   */
  @Override
  default boolean test(final T t, final U u) {
    try {
      return testThrows(t, u);
    }
    catch (final Exception e) {
      Throwing.rethrow(e);
      return false;
    }
  }

  /**
   * Evaluates this predicate on the given arguments, allowing an exception to be thrown.
   *
   * @param t The first input argument.
   * @param u The second input argument.
   * @return The function result.
   * @throws E If an exception has occurred.
   * @see BiPredicate#test(Object,Object)
   */
  boolean testThrows(T t, U u) throws E;
}