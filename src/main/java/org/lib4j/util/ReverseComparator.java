/* Copyright (c) 2012 lib4j
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

package org.lib4j.util;

import java.util.Comparator;

public final class ReverseComparator<T extends Comparable<T>> implements Comparator<T> {
  private final Comparator<? super T> comparator;

  public ReverseComparator(final Comparator<? super T> comparator) {
    this.comparator = comparator;
  }

  /**
   * Compares its two arguments for order, in reverse. Returns a negative
   * integer, zero, or a positive integer as the second argument is less than,
   * equal to, or greater than the first.
   * @param o1 the first object to be compared.
   * @param o2 the second object to be compared.
   * @return a negative integer, zero, or a positive integer as the
   * 	       second argument is less than, final equal to, or greater than the
   *	       first.
   * @throws ClassCastException if the arguments' types prevent them from
   * 	       being compared by this comparator.
   */
  @Override
  public int compare(final T o1, final T o2) {
    return comparator != null ? comparator.compare(o2, o1) : o2.compareTo(o1);
  }
}