/* Copyright (c) 2023 LibJ
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

package org.libj.util;

import java.util.Comparator;

/**
 * Interface representing the discrete topology of its type parameter.
 *
 * @param <T> The type parameter of the {@link DiscreteTopology} to represent.
 */
public interface DiscreteTopology<T> extends Comparator<T> {
  /**
   * Returns {@code true} if the provided value is the minimum, otherwise {@code false}.
   *
   * @param v The value which to determine if minimum.
   * @return {@code true} if the provided value is the minimum, otherwise {@code false}.
   */
  boolean isMinValue(T v);

  /**
   * Returns {@code true} if the provided value is the maximum, otherwise {@code false}.
   *
   * @param v The value which to determine if maximum.
   * @return {@code true} if the provided value is the maximum, otherwise {@code false}.
   */
  boolean isMaxValue(T v);

  /**
   * Returns the immediately previous discrete value from the provided argument {@code v}, or the argument itself if {@code v} is
   * {@link #isMinValue(Object)}.
   *
   * @param v The argument whose previous discrete value is to be returned.
   * @return The immediately previous discrete value from the provided argument {@code v}, or the argument itself if {@code v} is
   *         {@link #isMinValue(Object)}.
   */
  T prevValue(T v);

  /**
   * Returns the immediately next discrete value from the provided argument {@code v}, or the argument itself if {@code v} is
   * {@link #isMaxValue(Object)}.
   *
   * @param v The argument whose next discrete value is to be returned.
   * @return The immediately next discrete value from the provided argument {@code v}, or the argument itself if {@code v} is
   *         {@link #isMaxValue(Object)}.
   */
  T nextValue(T v);
}