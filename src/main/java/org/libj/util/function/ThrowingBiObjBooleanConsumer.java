/* Copyright (c) 2024 LibJ
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
 * Represents a consumer (boolean-valued function) that accepts two object-valued and a {@code boolean}-valued argument.
 * <p>
 * The {@link ThrowingBiObjBooleanConsumer} distinguishes itself from {@link ObjBiIntPredicate} by allowing the functional interface
 * to throw any {@link Throwable}. This can be used to allow lambda expressions to propagate checked exceptions up the expression's
 * call stack. An example of this pattern:
 *
 * <pre>
 * {@code
 * BiObjBooleanConsumer<Integer,Integer> consumer = Throwing.rethrow((Integer s, Integer t, boolean b) -> {
 *   if (!b)
 *     throw new IOException("i=" + i);
 *   return false;
 * });
 * consumer.accept(i, -i, true);
 * consumer.accept(i, -i, false);
 * }
 * </pre>
 *
 * @param <T> The type of the first argument to the consumer.
 * @param <U> The type of the second argument to the consumer.
 * @param <E> The type of {@link Throwable} that can be thrown.
 * @see Throwing#rethrow(ThrowingBiObjBooleanConsumer)
 */
@FunctionalInterface
public interface ThrowingBiObjBooleanConsumer<T,U,E extends Throwable> extends BiObjBooleanConsumer<T,U> {
  /**
   * Evaluates this consumer on the given arguments.
   *
   * @param t The first input argument.
   * @param u The second input argument.
   * @param b The third input argument.
   */
  @Override
  default void accept(final T t, final U u, final boolean b) {
    try {
      acceptThrows(t, u, b);
    }
    catch (final Throwable e) {
      Throwing.rethrow(e);
    }
  }

  /**
   * Evaluates this consumer on the given arguments, allowing a checked exception to be thrown.
   *
   * @param t The first input argument.
   * @param u The second input argument.
   * @param b The third input argument.
   * @throws E If an exception has occurred.
   * @see BiPredicate#test(Object,Object)
   */
  void acceptThrows(T t, U u, boolean b) throws E;
}