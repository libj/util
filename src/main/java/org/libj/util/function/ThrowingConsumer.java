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

import java.util.function.Consumer;

/**
 * Represents an operation that accepts a single input argument and returns no result. Unlike most other functional interfaces,
 * {@link Consumer} is expected to operate via side-effects.
 * <p>
 * The {@link ThrowingConsumer} distinguishes itself from {@link Consumer} by allowing the functional interface to throw any
 * {@link Throwable}. This can be used to allow lambda expressions to propagate checked exceptions up the expression's call stack.
 * An example of this pattern:
 *
 * <pre>
 * {@code
 * Arrays.asList(2, 1, 0).forEach(Throwing.rethrow((Integer i) -> {
 *   if (i == 0)
 *     throw new IOException("i=" + i);
 * }));
 * }
 * </pre>
 *
 * @param <T> The type of the input to the operation.
 * @param <E> The type of {@link Throwable} that can be thrown.
 * @see Throwing#rethrow(ThrowingConsumer)
 */
@FunctionalInterface
public interface ThrowingConsumer<T,E extends Throwable> extends Consumer<T> {
  @Override
  default void accept(final T t) {
    try {
      acceptThrows(t);
    }
    catch (final Throwable e) {
      Throwing.rethrow(e);
    }
  }

  /**
   * Performs this operation on the given argument, allowing a checked exception to be thrown.
   *
   * @param t The input argument.
   * @throws E If an exception has occurred.
   * @see Consumer#accept(Object)
   */
  void acceptThrows(T t) throws E;
}