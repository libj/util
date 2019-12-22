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

/**
 * Represents an operation that accepts an object-valued argument and two
 * {@code <y>}-valued arguments, and returns no result. This is the
 * {@code (reference, <y>, <y>)} specialization of
 * {@link Bi<X>Consumer}. Unlike most other functional interfaces,
 * {@link ObjBi<X>Consumer} is expected to operate via side-effects.
 *
 * @param <T> The type of the object argument to the operation.
 * @see Bi<X>Consumer
 */
@FunctionalInterface
public interface ObjBi<X>Consumer<T> {
  /**
   * Performs this operation on the given arguments.
   *
   * @param t The first input argument.
   * @param v1 The second input argument.
   * @param v2 The third input argument.
   */
  void accept(T t, <y> v1, <y> v2);
}