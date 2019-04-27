/* Copyright (c) 2019 OpenJAX
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

package org.openjax.ext.util.function;

import java.util.function.Predicate;

/**
 * Represents a predicate (boolean-valued function) of one argument.
 * <p>
 * The {@code ThrowingPredicate} distinguishes itself from {@link Predicate} by
 * allowing the functional interface to throw an {@code Exception}. This can be
 * used to allow lambda expressions to propagate checked exceptions up the
 * expression's call stack. An example of this pattern:
 * <blockquote><pre>
 * Arrays
 *   .asList(1, 2, 3)
 *   .stream()
 *   .filter(Throwing.&lt;Integer&gt;rethrow(i -&gt; {
 *     if (i == 3)
 *       throw new IOException("i=" + i);
 *     return false;
 *   }))
 *   .collect(Collectors.toList());
 * </pre></blockquote>
 *
 * @param <T> The type of the first argument to the predicate.
 * @see Predicate
 */
@FunctionalInterface
public interface ThrowingPredicate<T> extends Predicate<T> {
  /**
   * Evaluates this predicate on the given argument.
   *
   * @param t The input argument.
   * @return {@code true} if the input argument matches the predicate, otherwise
   *         {@code false}.
   */
  @Override
  default boolean test(T t) {
    try {
      return testThrows(t);
    }
    catch (final Exception e) {
      Throwing.rethrow(e);
      return false;
    }
  }

  /**
   * Evaluates this predicate on the given argument, allowing an exception to be
   * thrown.
   *
   * @param t The input argument.
   * @return The function result.
   * @throws Exception If an exception has occurred.
   */
  boolean testThrows(T t) throws Exception;
}