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

/**
 * A comparison function, which imposes a <i>total ordering</i> on some
 * collection of {@code float}s.
 */
@FunctionalInterface
public interface FloatComparator {
  /**
   * Comparator that compares two {@code float} values numerically.
   *
   * @see Float#compare(float,float)
   */
  public static final FloatComparator NATURAL = Float::compare;

  /**
   * Comparator that compares two {@code float} values reverse numerically.
   *
   * @see #reverse()
   */
  public static final FloatComparator REVERSE = NATURAL.reverse();

  /**
   * Compares its two arguments for order. Returns a negative integer, zero, or
   * a positive integer as the first argument is less than, equal to, or greater
   * than the second.
   *
   * @param f1 The first {@code float} to be compared.
   * @param f2 the second {@code float} to be compared.
   * @return A negative integer, zero, or a positive integer as the first
   *         argument is less than, equal to, or greater than the second.
   * @see java.util.Comparator#compare(Object,Object)
   */
  int compare(float f1, float f2);

  /**
   * Returns a comparator that imposes the reverse ordering of this comparator.
   *
   * @return A comparator that imposes the reverse ordering of this comparator.
   */
  default FloatComparator reverse() {
    return (f1, f2) -> compare(f2, f1);
  }
}