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
 * The {@code ThrowingBiPredicate} distinguishes itself from {@link BiPredicate}
 * by allowing the functional interface to throw an {@code Exception}. This can
 * be used to allow lambda expressions to propagate checked exceptions up the
 * expression's call stack. An example of this pattern:
 * <blockquote><pre>
 * BiPredicate&lt;Integer,Integer&gt; predicate = Throwing.&lt;Integer,Integer&gt;rethrow((i, j) -&gt; {
 *   if (i == 0)
 *     throw new IllegalArgumentException("i=" + i);
 *   return false;
 * });
 * for (int i = 3; i &gt;= 0; --i)
 *   predicate.accept(i, -i);
 * </pre></blockquote>
 *
 * @param <T> The type of the first argument to the predicate.
 * @param <U> The type of the second argument to the predicate.
 * @see BiPredicate
 */
@FunctionalInterface
public interface ThrowingBiPredicate<T,U> extends BiPredicate<T,U> {
  /**
   * Evaluates this predicate on the given arguments.
   *
   * @param t The first input argument.
   * @param u The second input argument.
   * @return {@code true} if the input argument matches the predicate, otherwise
   *         {@code false}.
   */
  @Override
  default boolean test(T t, U u) {
    try {
      return testThrows(t, u);
    }
    catch (final Exception e) {
      Throwing.rethrow(e);
      return false;
    }
  }

  /**
   * Evaluates this predicate on the given arguments, allowing an exception to
   * be thrown.
   *
   * @param t The first input argument.
   * @param u The second input argument.
   * @return The function result.
   * @throws Exception If an exception has occurred.
   */
  boolean testThrows(T t, U u) throws Exception;
}