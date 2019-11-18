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

import java.util.function.BiConsumer;

/**
 * Represents an operation that accepts two input arguments and returns no
 * result. Unlike most other functional interfaces, {@link TriConsumer} is
 * expected to operate via side-effects.
 * <p>
 * The {@link ThrowingBiConsumer} distinguishes itself from {@link BiConsumer} by
 * allowing the functional interface to throw an {@link Exception}. This can be
 * used to allow lambda expressions to propagate checked exceptions up the
 * expression's call stack. An example of this pattern:
 * <blockquote><pre>
 * TriConsumer&lt;Integer,Integer,Integer&gt; consumer = Throwing.&lt;Integer,Integer,Integer&gt;rethrow((i, j, k) -&gt; {
 *   if (i == 0)
 *     throw new IllegalArgumentException("i=" + i);
 * });
 * for (int i = 3; i &gt;= 0; --i)
 *   consumer.accept(i, -i, i);
 * </pre></blockquote>
 *
 * @param <T> The type of the first input to the operation.
 * @param <U> The type of the second input to the operation.
 * @param <V> The type of the third input to the operation.
 * @see Throwing
 */
@FunctionalInterface
public interface ThrowingTriConsumer<T,U,V> extends TriConsumer<T,U,V> {
  @Override
  default void accept(final T t, final U u, final V v) {
    try {
      acceptThrows(t, u, v);
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
   * @param u The second input argument.
   * @param v The third input argument.
   * @throws Exception If an exception has occurred.
   */
  void acceptThrows(T t, U u, V v) throws Exception;
}