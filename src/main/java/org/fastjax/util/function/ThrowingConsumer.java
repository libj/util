/* Copyright (c) 2018 FastJAX
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

package org.fastjax.util.function;

import java.util.function.Consumer;

/**
 * Represents an operation that accepts a single input argument and returns no
 * result. Unlike most other functional interfaces, {@code Consumer} is expected
 * to operate via side-effects.
 * <p>
 * The {@code ThrowingConsumer} distinguishes itself from {@link Consumer} by
 * allowing the functional interface to throw an {@code Exception}. This can be
 * used to allow lambda expressions to propagate checked exceptions up the
 * expression's call stack. An example of this pattern:
 * <blockquote><pre>
 * Arrays
 *   .asList(1, 2, 3)
 *   .forEach(Throwing.rethrow(i -&gt; {
 *      if (i == 3)
 *        throw new IOException();
 *    }));
 * </pre></blockquote>
 *
 * @param <T> The type of the input to the operation.
 * @see Throwing
 */
@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {
  @Override
  default void accept(final T t) {
    try {
      acceptThrows(t);
    }
    catch (final Exception e) {
      Throwing.rethrow(e);
    }
  }

  /**
   * Performs this operation on the given argument, allowing an exception to be
   * thrown.
   *
   * @param t The input argument.
   * @throws Exception If an exception has occurred.
   */
  void acceptThrows(T t) throws Exception;
}