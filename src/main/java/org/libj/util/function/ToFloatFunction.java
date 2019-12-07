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
 * Represents a function that produces a float-valued result. This is the
 * {@code float}-producing primitive specialization for
 * {@link java.util.function.Function}.
 *
 * @param <T> The type of the input to the function.
 * @see java.util.function.Function
 */
@FunctionalInterface
public interface ToFloatFunction<T> {
  /**
   * Applies this function to the given argument.
   *
   * @param value The function argument.
   * @return The function result.
   */
  float applyAsFloat(T value);
}