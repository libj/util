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
 * Represents an operation that accepts two object-valued argument and a
 * {@code <y>}-valued argument, and returns no result. Unlike most other
 * functional interfaces, {@link BiObj<X>Consumer} is expected to operate
 * via side-effects.
 *
 * @param <T> The type of the first object argument to the operation.
 * @param <U> The type of the second object argument to the operation.
 * @see ObjBi<X>Consumer
 */
@FunctionalInterface
public interface BiObj<X>Consumer<T,U> {
  /**
   * Performs this operation on the given arguments.
   *
   * @param t The first input argument.
   * @param u The second input argument.
   * @param v The third input argument.
   */
  void accept(T t, U u, <y> v);
}