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

import java.util.function.Predicate;

/**
 * Represents a predicate (boolean-valued function) of one argument.
 * <p>
 * The {@link ThrowingPredicate} distinguishes itself from {@link Predicate} by allowing the functional interface to throw any
 * {@link Throwable}. This can be used to allow lambda expressions to propagate checked exceptions up the expression's call stack.
 * An example of this pattern:
 *
 * <pre>
 * {@code
 * Arrays.asList(2, 1, 0).stream().filter(Throwing.rethrow((Integer i) -> {
 *   if (i == 0)
 *     throw new IOException("i=" + i);
 *   return false;
 * })).collect(Collectors.toList());
 * }
 * </pre>
 *
 * @param <T> The type of the input to the predicate.
 * @param <E> The type of {@link Throwable} that can be thrown.
 * @see Throwing#rethrow(ThrowingPredicate)
 */
@FunctionalInterface
public interface ThrowingPredicate<T,E extends Throwable> extends Predicate<T> {
  /**
   * Evaluates this predicate on the given argument.
   *
   * @param t The input argument.
   * @return {@code true} if the input argument matches the predicate, otherwise {@code false}.
   */
  @Override
  default boolean test(final T t) {
    try {
      return testThrows(t);
    }
    catch (final Throwable e) {
      Throwing.rethrow(e);
      return false;
    }
  }

  /**
   * Evaluates this predicate on the given argument, allowing a checked exception to be thrown.
   *
   * @param t The input argument.
   * @return The function result.
   * @throws E If an exception has occurred.
   * @see Predicate#test(Object)
   */
  boolean testThrows(T t) throws E;
}