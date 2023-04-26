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

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * A {@link Comparable} and {@link Serializable} two-dimensional semi-closed interval defined by the {@link #getMin() min}
 * (inclusive) and {@link #getMax() max} (exclusive) values. The implementations of {@link #compareTo(Interval)} and
 * {@link #compareTo(Comparable,Comparable)} {@link #getMin() min} as belonging to the interval (i.e. closed), and {@link #getMax()
 * max} as absent from the interval (i.e. open).
 *
 * @param <T> The {@link Comparable} and {@link Serializable} type parameter of the {@link #getMin() min} and {@link #getMax() max}
 *          values.
 */
public class Interval<T extends Comparable<? super T>> implements Comparable<Interval<T>>, Serializable {
  @SuppressWarnings({"rawtypes", "unchecked"})
  public static final Comparator<? super Interval> COMPARATOR = (final Interval o1, final Interval o2) -> o1 == null ? o2 == null ? 0 : -1 : o2 == null ? 1 : o1.compareTo(o2);

  protected final T min;
  protected final T max;

  /**
   * Creates a new {@link Interval} with the provided {@code min} and {@code max} parameters.
   *
   * @param min The min value, or {@code null} to represent negative infinity.
   * @param max The max value, or {@code null} to represent positive infinity.
   * @throws IllegalArgumentException If a non-null {@code min} is greater than or equal to a non-null {@code max}.
   */
  public Interval(final T min, final T max) {
    this.min = min;
    this.max = max;
    if (min != null && max != null && min.compareTo(max) >= 0)
      throw new IllegalArgumentException("Illegal interval: " + toString(min, max));
  }

  /**
   * Returns the minimum value, where {@code null} represents negative infinity.
   *
   * @return The minimum value, where {@code null} represents negative infinity.
   */
  public T getMin() {
    return min;
  }

  /**
   * Returns the maximum value, where {@code null} represents positive infinity.
   *
   * @return The maximum value, where {@code null} represents positive infinity.
   */
  public T getMax() {
    return max;
  }

  /**
   * Returns {@code true} if the provided {@link Interval} intersects this {@link Interval}, otherwise {@code false}.
   *
   * @param i The {@link Interval} to test for intersect.
   * @return {@code true} if the provided {@link Interval} intersects this {@link Interval}, otherwise {@code false}.
   * @throws NullPointerException If the provided {@link Interval} is null.
   */
  public boolean intersects(final Interval<T> i) {
    return intersectLeft(i.min, max) && intersectRight(min, i.max);
  }

  private boolean intersectLeft(final T min1, final T max0) {
    return min1 == null || max0 == null || max0.compareTo(min1) > 0;
  }

  private boolean intersectRight(final T min0, final T max1) {
    return min0 == null || max1 == null || max1.compareTo(min0) > 0;
  }

  /**
   * Returns {@code true} if the provided {@link Interval} is contained within this {@link Interval}, otherwise {@code false}.
   *
   * @param i The {@link Interval} to test for containment.
   * @return {@code true} if the provided {@link Interval} is contained within this {@link Interval}, otherwise {@code false}.
   * @throws NullPointerException If the provided {@link Interval} is null.
   */
  public boolean contains(final Interval<T> i) {
    final T min0 = min;
    final T max0 = max;

    if (min0 == null) {
      final T max1;
      return max0 == null || (max1 = i.max) != null && max1.compareTo(max0) <= 0;
    }

    final T min1 = i.min;
    if (max0 == null)
      return min1 != null && min1.compareTo(min0) >= 0;

    return min1.compareTo(min0) >= 0 && i.max.compareTo(max0) <= 0;
  }

  /**
   * Returns {@code true} if the provided value is contained within this {@link Interval}, otherwise {@code false}.
   *
   * @param value The value to test for containment.
   * @return {@code true} if the provided value is contained within this {@link Interval}, otherwise {@code false}.
   * @throws NullPointerException If the provided {@code value} is null.
   */
  public boolean contains(final T value) {
    final T min = this.min;
    final T max = this.max;
    return (min == null || value.compareTo(min) >= 0) && (max == null || value.compareTo(max) < 0);
  }

  /**
   * Compares this {@link Interval} with the provided {@link Interval} for order, and returns an integer value of {@code -2},
   * {@code -1}, {@code 0}, {@code 1} or {@code 2}, based on the following logic:
   *
   * <ul>
   * <li>
   * No intersect: {@code -2}
   * <pre>
   *                ___________
   *                |   that  |
   *                -----------
   *   ___________
   *   |   this  |
   *   -----------
   * </pre>
   * </li>
   * <li>
   * Yes intersect: {@code -1}
   * <pre>
   *            ___________
   *            |   that  |
   *            -----------
   *   ___________
   *   |   this  |
   *   -----------
   * </pre>
   * </li>
   * <li>
   * Yes intersect: {@code 0}
   * <pre>
   *   ___________
   *   |   that  |
   *   -----------
   *   ________
   *   | this |
   *   --------
   * </pre>
   * </li>
   * <li>
   * Yes intersect: {@code 0}
   * <pre>
   *   ________
   *   | that |
   *   --------
   *   ___________
   *   |   this  |
   *   -----------
   * </pre>
   * </li>
   * <li>
   * Yes intersect: {@code 1}
   * <pre>
   *   ___________
   *   |   that  |
   *   -----------
   *            ___________
   *            |   this  |
   *            -----------
   * </pre>
   * </li>
   * <li>
   * No intersect: {@code 2}
   * <pre>
   *   ___________
   *   |   that  |
   *   -----------
   *                ___________
   *                |   this  |
   *                -----------
   * </pre>
   * </li>
   * </ul>
   *
   * @param o The {@link Interval} with which to compare.
   * @return The value as described above.
   * @throws NullPointerException If the provided {@link Interval} is null.
   */
  @Override
  public int compareTo(final Interval<T> o) {
    return compareTo(o.min, o.max);
  }

  /**
   * Compares this {@link Interval} with the provided {@code min} and {@code max} values for order, and returns an integer value of
   * {@code -2}, {@code -1}, {@code 0}, {@code 1} or {@code 2}, based on the following logic:
   *
   * <ul>
   * <li>
   * No intersect: {@code -2}
   * <pre>
   *                ___________
   *                |   that  |
   *                -----------
   *   ___________
   *   |   this  |
   *   -----------
   * </pre>
   * </li>
   * <li>
   * Yes intersect: {@code -1}
   * <pre>
   *            ___________
   *            |   that  |
   *            -----------
   *   ___________
   *   |   this  |
   *   -----------
   * </pre>
   * </li>
   * <li>
   * Yes intersect: {@code 0}
   * <pre>
   *   ___________
   *   |   that  |
   *   -----------
   *   ________
   *   | this |
   *   --------
   * </pre>
   * </li>
   * <li>
   * Yes intersect: {@code 0}
   * <pre>
   *   ________
   *   | that |
   *   --------
   *   ___________
   *   |   this  |
   *   -----------
   * </pre>
   * </li>
   * <li>
   * Yes intersect: {@code 1}
   * <pre>
   *   ___________
   *   |   that  |
   *   -----------
   *            ___________
   *            |   this  |
   *            -----------
   * </pre>
   * </li>
   * <li>
   * No intersect: {@code 2}
   * <pre>
   *   ___________
   *   |   that  |
   *   -----------
   *                ___________
   *                |   this  |
   *                -----------
   * </pre>
   * </li>
   * </ul>
   * @param min The min value to compare with.
   * @param max The max value to compare with.
   * @return The value as described above.
   */
  public int compareTo(final T min, final T max) {
    final T min0 = this.min;
    final T max0 = this.max;

    if (min0 == null) {
      if (min == null)
        return 0;

      return max0 == null || max0.compareTo(min) > 0 ? -1 : -2;
    }

    if (min == null)
      return max == null || max.compareTo(min0) > 0 ? 1 : 2;

    if (max0 == null) {
      if (max == null || min0.compareTo(max) < 0)
        return min0.compareTo(min);

      return 2;
    }

    if (max == null)
      return min.compareTo(max0) >= 0 ? -2 : min0.compareTo(min);

    if (min0.compareTo(max) > 0)
      return 2;

    if (max0.compareTo(min) < 0)
      return -2;

    return min0.compareTo(min);
  }

  /**
   * Returns {@code true} if the provided {@code min} value equals the {@link #getMin() min} value, and the provided {@code max}
   * value equals the {@link #getMax() max} value of this {@link Interval}, otherwise {@code false}.
   *
   * @param min The min value to test for equality.
   * @param max The max value to test for equality.
   * @return {@code true} if the provided {@code min} value equals the {@link #getMin() min} value, and the provided {@code max}
   *         value equals the {@link #getMax() max} value of this {@link Interval}, otherwise {@code false}.
   */
  public boolean equals(final T min, final T max) {
    return Objects.equals(this.min, min) && Objects.equals(this.max, max);
  }

  @Override
  public int hashCode() {
    return min.hashCode() ^ max.hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof Interval))
      return false;

    final Interval<?> that = (Interval<?>)obj;
    return Objects.equals(min, that.min) && Objects.equals(max, that.max);
  }

  /**
   * Returns the string representation of an interval defined by the provided {@code min} and {@code max} values.
   *
   * @param min The min value.
   * @param max The max value.
   * @return The string representation of an interval defined by the provided {@code min} and {@code max} values.
   * @implNote This method does not assert whether {@code min} is less than {@code max}.
   */
  public String toString(final T min, final T max) {
    return "[" + min + "," + max + ")";
  }

  @Override
  public String toString() {
    return toString(min, max);
  }
}