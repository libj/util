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

import java.util.function.Supplier;

/**
 * Represents a supplier of results.
 * <p>
 * The {@link ThrowingSupplier} distinguishes itself from {@link Supplier} by
 * allowing the functional interface to throw an {@link Exception}. This can be
 * used to allow lambda expressions to propagate checked exceptions up the
 * expression's call stack. An example of this pattern:
 *
 * <pre>
 * {@code
 * Supplier<String> supplier = Throwing.rethrow(() -> {
 *   if (true)
 *     throw new IOException();
 *   return "hello world";
 * });
 * supplier.get();
 * }
 * </pre>
 *
 * @param <T> The type of results supplied by this supplier.
 * @see Runnable#run()
 */
@FunctionalInterface
public interface ThrowingSupplier<T> extends Supplier<T> {
  @Override
  default T get() {
    try {
      return getThrows();
    }
    catch (final Exception e) {
      Throwing.rethrow(e);
      return null;
    }
  }

  /**
   * Gets a result.
   *
   * @return A result.
   * @throws Exception If an exception has occurred.
   * @see Supplier#get()
   */
  T getThrows() throws Exception;
}