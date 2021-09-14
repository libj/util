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
 * Represents an operation that accepts no input arguments and returns no
 * result.
 * <p>
 * The {@link ThrowingRunnable} distinguishes itself from {@link Runnable} by
 * allowing the functional interface to throw an {@link Exception}. This can be
 * used to allow lambda expressions to propagate checked exceptions up the
 * expression's call stack. An example of this pattern:
 *
 * <pre>
 * {@code
 * Runnable runnable = Throwing.rethrow(() -> {
 *   if (true)
 *     throw new IOException();
 * });
 * runnable.run();
 * }
 * </pre>
 *
 * @param <E> The type of the exception that can be thrown.
 * @see Throwing#rethrow(ThrowingRunnable)
 */
@FunctionalInterface
public interface ThrowingRunnable<E extends Exception> extends Runnable {
  @Override
  default void run() {
    try {
      runThrows();
    }
    catch (final Exception e) {
      Throwing.rethrow(e);
    }
  }

  /**
   * Performs this operation, allowing an exception to be thrown.
   *
   * @throws E If an exception has occurred.
   * @see Runnable#run()
   */
  void runThrows() throws E;
}