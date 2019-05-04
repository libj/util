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
 * Represents an operation that accepts three object-valued and two
 * {@code int}-valued arguments, and returns no result. Unlike most other
 * functional interfaces, {@code TriObjBiIntConsumer} is expected to operate via
 * side-effects.
 *
 * @param <T> The type of the first object argument to the operation.
 * @param <U> The type of the second object argument to the operation.
 * @param <V> The type of the third object argument to the operation.
 * @see ObjBiIntConsumer
 */
@FunctionalInterface
public interface TriObjBiIntConsumer<T,U,V> {
  /**
   * Performs this operation on the given arguments.
   *
   * @param t The first input argument.
   * @param u The second input argument.
   * @param v The third input argument.
   * @param w1 The third input argument.
   * @param w2 The fourth input argument.
   */
  void accept(T t, U u, V v, int w1, int w2);
}