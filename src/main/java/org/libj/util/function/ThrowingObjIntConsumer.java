/* Copyright (c) 2021 LibJ
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

import java.util.function.ObjIntConsumer;

/**
 * Represents an operation that accepts an object-valued and a
 * {@code int}-valued argument, and returns no result. Unlike most other
 * functional interfaces, {@link ObjIntConsumer} is expected to operate via
 * side-effects.
 * <p>
 * The {@link ThrowingObjIntConsumer} distinguishes itself from
 * {@link ObjIntConsumer} by allowing the functional interface to throw an
 * {@link Exception}. This can be used to allow lambda expressions to propagate
 * checked exceptions up the expression's call stack. An example of this
 * pattern:
 *
 * <pre>
 * {
 *   &#64;code
 *   ObjIntConsumer<String> consumer = Throwing.<Integer,Integer>rethrow((s,i) -> {
 *     if (i == 0)
 *       throw new IllegalArgumentException("i=" + i);
 *   });
 *   for (int i = 3; i >= 0; --i)
 *     consumer.accept(i, -i);
 * }
 * </pre>
 *
 * @param <T> The type of the object argument to the operation.
 * @param <E> The type of the exception that can be thrown.
 * @see Throwing
 */
@FunctionalInterface
public interface ThrowingObjIntConsumer<T,E extends Exception> extends ObjIntConsumer<T> {
  @Override
  default void accept(final T t, final int value) {
    try {
      acceptThrows(t, value);
    }
    catch (final Exception e) {
      Throwing.rethrow(e);
    }
  }

  /**
   * Performs this operation on the given argument, allowing an exception to be
   * thrown.
   *
   * @param t The first input argument.
   * @param value The second input argument.
   * @throws E If an exception has occurred.
   */
  void acceptThrows(T t, int value) throws E;
}