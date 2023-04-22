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
   * @param min The min value.
   * @param max The max value.
   * @throws IllegalArgumentException If {@code min} is greater than or equal to {@code max}.
   * @throws NullPointerException If {@code min} or {@code max} is null.
   */
  public Interval(final T min, final T max) {
    this.min = Objects.requireNonNull(min);
    this.max = Objects.requireNonNull(max);
    if (min.compareTo(max) >= 0)
      throw new IllegalArgumentException("Illegal interval: " + toString(min, max));
  }

  /**
   * Returns the minimum value of this {@link Interval}.
   *
   * @return The minimum value of this {@link Interval}.
   */
  public T getMin() {
    return min;
  }

  /**
   * Returns the maximum value of this {@link Interval}.
   *
   * @return The maximum value of this {@link Interval}.
   */
  public T getMax() {
    return max;
  }

  /**
   * Returns {@code true} if the provided {@link Interval} intersects this {@link Interval}, otherwise {@code false}.
   *
   * @param i The {@link Interval} to test for intersect.
   * @return {@code true} if the provided {@link Interval} intersects this {@link Interval}, otherwise {@code false}.
   */
  public boolean intersects(final Interval<T> i) {
    return min.compareTo(i.max) < 0 && max.compareTo(i.min) > 0;
  }

  /**
   * Returns {@code true} if the provided {@link Interval} is contained within this {@link Interval}, otherwise {@code false}.
   *
   * @param i The {@link Interval} to test for containment.
   * @return {@code true} if the provided {@link Interval} is contained within this {@link Interval}, otherwise {@code false}.
   */
  public boolean contains(final Interval<T> i) {
    return min.compareTo(i.min) <= 0 && max.compareTo(i.max) >= 0;
  }

  /**
   * Returns {@code true} if the provided value is contained within this {@link Interval}, otherwise {@code false}.
   *
   * @param value The value to test for containment.
   * @return {@code true} if the provided value is contained within this {@link Interval}, otherwise {@code false}.
   */
  public boolean contains(final T value) {
    return min.compareTo(value) <= 0 && max.compareTo(value) > 0;
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
   * @throws NullPointerException If the specified {@link Interval} is null.
   * {@inheritDoc #compareTo(Interval)}
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
   * @throws NullPointerException If {@code min} or {@code max} is null.
   */
  public int compareTo(final T min, final T max) {
    if (this.min.compareTo(max) > 0)
      return 2;

    if (this.max.compareTo(min) < 0)
      return -2;

    return this.min.compareTo(min);
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
    return this.min.equals(min) && this.max.equals(max);
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
    return min.equals(that.min) && max.equals(that.max);
  }

  /**
   * Returns the string representation of an interval defined by the provided {@code min} and {@code max} values.
   *
   * @param min The min value.
   * @param max The max value.
   * @return The string representation of an interval defined by the provided {@code min} and {@code max} values.
   * @implNote This method does not assert non-null values, nor whether {@code min} is less than or equal to {@code max}.
   */
  public String toString(final T min, final T max) {
    return "[" + min + "," + max + ")";
  }

  @Override
  public String toString() {
    return toString(min, max);
  }
}