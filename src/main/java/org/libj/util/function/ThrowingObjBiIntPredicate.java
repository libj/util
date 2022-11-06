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
 * Represents a predicate (boolean-valued function) that accepts an object-valued and two {@code int}-valued arguments.
 * <p>
 * The {@link ThrowingObjBiIntPredicate} distinguishes itself from {@link ObjBiIntPredicate} by allowing the functional interface to
 * throw any {@link Throwable}. This can be used to allow lambda expressions to propagate checked exceptions up the expression's
 * call stack. An example of this pattern:
 *
 * <pre>
 * {@code
 * ObjBiIntPredicate<Integer,Integer> predicate = Throwing.rethrow((Integer i, int j, int k) -> {
 *   if (i == 0)
 *     throw new IOException("i=" + i);
 *   return false;
 * });
 * for (int i = 3; i >= 0; --i)
 *   predicate.accept(i, -i, i * 2);
 * }
 * </pre>
 *
 * @param <T> The type of the first argument to the predicate.
 * @param <E> The type of {@link Throwable} that can be thrown.
 * @see Throwing#rethrow(ThrowingObjBiIntPredicate)
 */
@FunctionalInterface
public interface ThrowingObjBiIntPredicate<T,E extends Throwable> extends ObjBiIntPredicate<T> {
  /**
   * Evaluates this predicate on the given arguments.
   *
   * @param t The first input argument.
   * @param v1 The second input argument.
   * @return {@code true} if the input argument matches the predicate, otherwise {@code false}.
   */
  @Override
  default boolean test(final T t, final int v1, final int v2) {
    try {
      return testThrows(t, v1, v2);
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
   * @param v1 The second input argument.
   * @param v2 The third input argument.
   * @return The function result.
   * @throws E If an exception has occurred.
   * @see BiPredicate#test(Object,Object)
   */
  boolean testThrows(T t, int v1, int v2) throws E;
}