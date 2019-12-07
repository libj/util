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

package org.libj.util;

/**
 * A comparison function, which imposes a <i>total ordering</i> on some
 * collection of {@code long}s.
 */
@FunctionalInterface
public interface LongComparator {
  /**
   * Comparator that compares two {@code long} values numerically.
   *
   * @see Long#compare(long,long)
   */
  public static final LongComparator NATURAL = Long::compare;

  /**
   * Comparator that compares two {@code long} values reverse numerically.
   *
   * @see #reverse()
   */
  public static final LongComparator REVERSE = NATURAL.reverse();

  /**
   * Compares its two arguments for order. Returns a negative integer, zero, or
   * a positive integer as the first argument is less than, equal to, or greater
   * than the second.
   *
   * @param l1 The first {@code long} to be compared.
   * @param l2 the second {@code long} to be compared.
   * @return A negative integer, zero, or a positive integer as the first
   *         argument is less than, equal to, or greater than the second.
   * @see java.util.Comparator#compare(Object,Object)
   */
  int compare(long l1, long l2);

  /**
   * Returns a comparator that imposes the reverse ordering of this comparator.
   *
   * @return A comparator that imposes the reverse ordering of this comparator.
   */
  default LongComparator reverse() {
    return (l1, l2) -> compare(l2, l1);
  }
}