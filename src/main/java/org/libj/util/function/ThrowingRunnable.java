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
 * The {@code ThrowingRunnable} distinguishes itself from {@link Runnable} by
 * allowing the functional interface to throw an {@code Exception}. This can be
 * used to allow lambda expressions to propagate checked exceptions up the
 * expression's call stack. An example of this pattern:
 * <p>
 * <blockquote><pre>
 * Arrays
 *   .asList(1, 2, 3)
 *   .forEach(Throwing.rethrow(() -&gt; {
 *      if (i == 3)
 *        throw new IOException();
 *    }));
 * </pre></blockquote>

 * @see Runnable#run()
 */
@FunctionalInterface
public interface ThrowingRunnable extends Runnable {
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
   * @throws Exception If an exception has occurred.
   * @see Runnable#run()
   */
  void runThrows() throws Exception;
}