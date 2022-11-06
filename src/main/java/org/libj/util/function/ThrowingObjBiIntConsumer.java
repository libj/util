/* Copyright (c) 2022 LibJ
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
 * Represents an operation that accepts an object-valued and two {@code int}-valued arguments, and returns no result. Unlike most
 * other functional interfaces, {@link ObjBiIntConsumer} is expected to operate via side-effects.
 * <p>
 * The {@link ThrowingObjBiIntConsumer} distinguishes itself from {@link ObjBiIntConsumer} by allowing the functional interface to
 * throw any {@link Throwable}. This can be used to allow lambda expressions to propagate checked exceptions up the expression's
 * call stack. An example of this pattern:
 *
 * <pre>
 * {@code
 * ObjBiIntConsumer<String> consumer = Throwing.rethrow((String s, int i, int j) -> {
 *   if (i == 0)
 *     throw new IOException("i=" + i);
 * });
 * for (int i = 3; i >= 0; --i)
 *   consumer.accept(i, -i, i * 2);
 * }
 * </pre>
 *
 * @param <T> The type of the object argument to the operation.
 * @param <E> The type of {@link Throwable} that can be thrown.
 * @see Throwing#rethrow(ThrowingObjBiIntConsumer)
 */
@FunctionalInterface
public interface ThrowingObjBiIntConsumer<T,E extends Throwable> extends ObjBiIntConsumer<T> {
  @Override
  default void accept(final T t, final int v1, final int v2) {
    try {
      acceptThrows(t, v1, v2);
    }
    catch (final Throwable e) {
      Throwing.rethrow(e);
    }
  }

  /**
   * Performs this operation on the given argument, allowing a checked exception to be thrown.
   *
   * @param t The first input argument.
   * @param v1 The second input argument.
   * @param v2 The third input argument.
   * @throws E If an exception has occurred.
   * @see ObjBiIntConsumer#accept(Object,int,int)
   */
  void acceptThrows(T t, int v1, int v2) throws E;
}