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
 * collection of {@code char}s.
 */
@FunctionalInterface
public interface CharComparator {
  /**
   * Comparator that compares two {@code char} values numerically.
   *
   * @see Character#compare(char,char)
   */
  public static final CharComparator NATURAL = Character::compare;

  /**
   * Comparator that compares two {@code char} values reverse numerically.
   *
   * @see #reverse()
   */
  public static final CharComparator REVERSE = NATURAL.reverse();

  /**
   * Compares its two arguments for order. Returns a negative integer, zero, or
   * a positive integer as the first argument is less than, equal to, or greater
   * than the second.
   *
   * @param c1 The first {@code char} to be compared.
   * @param c2 the second {@code char} to be compared.
   * @return A negative integer, zero, or a positive integer as the first
   *         argument is less than, equal to, or greater than the second.
   * @see java.util.Comparator#compare(Object,Object)
   */
  int compare(char c1, char c2);

  /**
   * Returns a comparator that imposes the reverse ordering of this comparator.
   *
   * @return A comparator that imposes the reverse ordering of this comparator.
   */
  default CharComparator reverse() {
    return (c1, c2) -> compare(c2, c1);
  }
}