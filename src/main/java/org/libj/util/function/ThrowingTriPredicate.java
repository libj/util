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

/**
 * Represents a predicate (boolean-valued function) that accepts three arguments.
 * <p>
 * The {@link ThrowingTriPredicate} distinguishes itself from {@link TriPredicate} by allowing the functional interface to throw any
 * {@link Throwable}. This can be used to allow lambda expressions to propagate checked exceptions up the expression's call stack.
 * An example of this pattern:
 *
 * <pre>
 * {@code
 * TriPredicate<Integer,Integer,Integer> predicate = Throwing.rethrow((Integer i, Integer j, Integer k) -> {
 *   if (i == 0)
 *     throw new IOException("i=" + i);
 *   return false;
 * });
 * for (int i = 3; i >= 0; --i)
 *   predicate.accept(i, -i, i);
 * }
 * </pre>
 *
 * @param <T> The type of the first argument to the predicate.
 * @param <U> The type of the second argument to the predicate.
 * @param <V> The type of the third argument to the predicate.
 * @param <E> The type of {@link Throwable} that can be thrown.
 * @see Throwing#rethrow(ThrowingTriPredicate)
 */
@FunctionalInterface
public interface ThrowingTriPredicate<T,U,V,E extends Throwable> extends TriPredicate<T,U,V> {
  /**
   * Evaluates this predicate on the given arguments.
   *
   * @param t The first input argument.
   * @param u The second input argument.
   * @return {@code true} if the input argument matches the predicate, otherwise {@code false}.
   */
  @Override
  default boolean test(final T t, final U u, final V v) {
    try {
      return testThrows(t, u, v);
    }
    catch (final Throwable e) {
      Throwing.rethrow(e);
      return false;
    }
  }

  /**
   * Evaluates this predicate on the given arguments, allowing a checked exception to be thrown.
   *
   * @param t The first input argument.
   * @param u The second input argument.
   * @param v The third input argument.
   * @return The function result.
   * @throws E If an exception has occurred.
   * @see TriPredicate#test(Object,Object,Object)
   */
  boolean testThrows(T t, U u, V v) throws E;
}