/* Copyright (c) 2017 LibJ
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

/**
 * A comparison function, which imposes a <i>total ordering</i> on some
 * collection of {@code byte}s.
 */
@FunctionalInterface
public interface ByteComparator {
  /**
   * Comparator that compares two {@code byte} values numerically.
   *
   * @see Byte#compare(byte,byte)
   */
  public static final ByteComparator NATURAL = Byte::compare;

  /**
   * Comparator that compares two {@code byte} values reverse numerically.
   *
   * @see #reverse()
   */
  public static final ByteComparator REVERSE = NATURAL.reverse();

  /**
   * Compares its two arguments for order. Returns a negative integer, zero, or
   * a positive integer as the first argument is less than, equal to, or greater
   * than the second.
   *
   * @param b1 The first {@code byte} to be compared.
   * @param b2 the second {@code byte} to be compared.
   * @return A negative integer, zero, or a positive integer as the first
   *         argument is less than, equal to, or greater than the second.
   * @see java.util.Comparator#compare(Object,Object)
   */
  int compare(byte b1, byte b2);

  /**
   * Returns a comparator that imposes the reverse ordering of this comparator.
   *
   * @return A comparator that imposes the reverse ordering of this comparator.
   */
  default ByteComparator reverse() {
    return (b1, b2) -> compare(b2, b1);
  }
}