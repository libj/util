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
 * Represents a function that accepts three object-valued argument and a
 * {@code <x>}-valued argument, and produces a result. This is the three-arity
 * specialization of {@link java.util.function.Function}.
 *
 * @param <T> The type of the object argument to the operation.
 * @param <U> The type of the second object argument to the operation.
 * @param <V> The type of the third object argument to the operation.
 * @param <R> The type of the result of the function.
 * @see org.libj.util.function.TriFunction
 */
@FunctionalInterface
public interface TriObj<X>Function<T,U,V,R> {
  /**
   * Performs this operation on the given arguments.
   *
   * @param t The first input argument.
   * @param u The second input argument.
   * @param v The third input argument.
   * @param i The fourth input argument.
   * @return The function result.
   */
  R apply(T t, U u, V v, <x> i);
}