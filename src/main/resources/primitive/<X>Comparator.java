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

package org.libj.util.primitive;

import java.util.List;

/**
 * A comparison function, which imposes a <i>total ordering</i> on some
 * collection of {@code <x>}s.
 */
@FunctionalInterface
public interface <X>Comparator {
  /**
   * Comparator that compares two {@code <x>} values numerically.
   *
   * @see <XX>#compare(<x>,<x>)
   */
  public static final <X>Comparator NATURAL = <XX>::compare;

  /**
   * Comparator that compares two {@code <x>} values reverse numerically.
   *
   * @see #reverse()
   */
  public static final <X>Comparator REVERSE = NATURAL.reverse();

  /**
   * Compares its two arguments for order. Returns a negative integer, zero, or
   * a positive integer as the first argument is less than, equal to, or greater
   * than the second.
   *
   * @param f1 The first {@code <x>} to be compared.
   * @param f2 the second {@code <x>} to be compared.
   * @return A negative integer, zero, or a positive integer as the first
   *         argument is less than, equal to, or greater than the second.
   * @see java.util.Comparator#compare(Object,Object)
   */
  int compare(<x> f1, <x> f2);

  /**
   * Returns a comparator that imposes the reverse ordering of this comparator.
   *
   * @return A comparator that imposes the reverse ordering of this comparator.
   */
  default <X>Comparator reverse() {
    return (f1, f2) -> compare(f2, f1);
  }
}