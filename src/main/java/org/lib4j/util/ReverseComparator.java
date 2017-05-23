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
  private final Comparator<? super T> comparatorInstance;

  public ReverseComparator(final Class<? extends Comparator<? super T>> comparatorClass) {
    try {
      this.comparatorInstance = comparatorClass.newInstance();
    }
    catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Compares its two arguments for order.  Returns a negative integer,
   * zero, or a positive integer as the first argument is less than, equal
   * to, or greater than the second.<p>
   * In the foregoing description, the notation
   * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
   * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
   * <tt>0</tt>, or <tt>1</tt> according to whether the value of
   * <i>expression</i> is negative, zero or positive.<p>
   * The implementor must ensure that <tt>sgn(compare(x, y)) ==
   * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
   * implies that <tt>compare(x, y)</tt> must throw an exception if and only
   * if <tt>compare(y, x)</tt> throws an exception.)<p>
   * The implementor must also ensure that the relation is transitive:
   * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies
   * <tt>compare(x, z)&gt;0</tt>.<p>
   * Finally, the implementor must ensure that <tt>compare(x, y)==0</tt>
   * implies that <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all
   * <tt>z</tt>.<p>
   * It is generally the case, but <i>not</i> strictly required that
   * <tt>(compare(x, y)==0) == (x.equals(y))</tt>.  Generally speaking,
   * any comparator that violates this condition should clearly indicate
   * this fact.  The recommended language is "Note: this comparator
   * imposes orderings that are inconsistent with equals."
   * @param o1 the first object to be compared.
   * @param o2 the second object to be compared.
   * @return a negative integer, zero, or a positive integer as the
   * 	       first argument is less than, final equal to, or greater than the
   *	       second.
   * @throws ClassCastException if the arguments' types prevent them from
   * 	       being compared by this comparator.
   */
  @Override
  public int compare(final T o1, final T o2) {
    return comparatorInstance != null ? comparatorInstance.compare(o2, o1) : o2.compareTo(o1);
  }
}