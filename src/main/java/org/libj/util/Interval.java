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
import java.math.BigInteger;
import java.util.Comparator;
import java.util.Objects;

/**
 * A {@link Comparable} and {@link Serializable} two-dimensional interval defined by {@link #getMin() min} and {@link #getMax() max}
 * values that are themselves {@link Comparable} and {@link Serializable}. The implementations of {@link #compareTo(Interval)} and
 * {@link #compareTo(Comparable,Comparable)} consider both the {@link #getMin() min} and {@link #getMax() max} values as belonging
 * to the interval (i.e. a closed interval).
 *
 * @param <T> The {@link Comparable} and {@link Serializable} type parameter of the {@link #getMin() min} and {@link #getMax() max}
 *          values.
 */
public class Interval<T extends Comparable<T>> implements Comparable<Interval<T>>, Serializable {
  @SuppressWarnings({"rawtypes", "unchecked"})
  public static final Comparator<? super Interval> COMPARATOR = (final Interval o1, final Interval o2) -> o1 == null ? o2 == null ? 0 : -1 : o2 == null ? 1 : o1.compareTo(o2);

  private interface Spec<T extends Comparable<T>> {
    public static final Spec<Byte> BYTE = new Spec<Byte>() {
      @Override
      public Byte maxValue() {
        return Byte.MAX_VALUE;
      }

      @Override
      public Byte minValue() {
        return Byte.MIN_VALUE;
      }

      @Override
      public Byte nextValue(Byte v) {
        return ++v;
      }

      @Override
      public Byte prevValue(Byte v) {
        return --v;
      }
    };

    public static final Spec<Short> SHORT = new Spec<Short>() {
      @Override
      public Short maxValue() {
        return Short.MAX_VALUE;
      }

      @Override
      public Short minValue() {
        return Short.MIN_VALUE;
      }

      @Override
      public Short nextValue(Short v) {
        return ++v;
      }

      @Override
      public Short prevValue(Short v) {
        return --v;
      }
    };

    public static final Spec<Integer> INTEGER = new Spec<Integer>() {
      @Override
      public Integer maxValue() {
        return Integer.MAX_VALUE;
      }

      @Override
      public Integer minValue() {
        return Integer.MIN_VALUE;
      }

      @Override
      public Integer nextValue(Integer v) {
        return ++v;
      }

      @Override
      public Integer prevValue(Integer v) {
        return --v;
      }
    };

    public static final Spec<Long> LONG = new Spec<Long>() {
      @Override
      public Long maxValue() {
        return Long.MAX_VALUE;
      }

      @Override
      public Long minValue() {
        return Long.MIN_VALUE;
      }

      @Override
      public Long nextValue(Long v) {
        return ++v;
      }

      @Override
      public Long prevValue(Long v) {
        return --v;
      }
    };

    public static final Spec<Float> FLOAT = new Spec<Float>() {
      @Override
      public Float maxValue() {
        return Float.MAX_VALUE;
      }

      @Override
      public Float minValue() {
        return Float.MIN_VALUE;
      }

      @Override
      public Float nextValue(final Float v) {
        return Math.nextUp(v);
      }

      @Override
      public Float prevValue(final Float v) {
        return Math.nextDown(v);
      }
    };

    public static final Spec<Double> DOUBLE = new Spec<Double>() {
      @Override
      public Double maxValue() {
        return Double.MAX_VALUE;
      }

      @Override
      public Double minValue() {
        return Double.MIN_VALUE;
      }

      @Override
      public Double nextValue(final Double v) {
        return Math.nextUp(v);
      }

      @Override
      public Double prevValue(final Double v) {
        return Math.nextDown(v);
      }
    };

    public static final Spec<Character> CHAR = new Spec<Character>() {
      @Override
      public Character maxValue() {
        return Character.MAX_VALUE;
      }

      @Override
      public Character minValue() {
        return Character.MIN_VALUE;
      }

      @Override
      public Character nextValue(Character v) {
        return ++v;
      }

      @Override
      public Character prevValue(Character v) {
        return --v;
      }
    };

    public static final Spec<BigInteger> BIG_INTEGER = new Spec<BigInteger>() {
      @Override
      public BigInteger maxValue() {
        return null;
      }

      @Override
      public BigInteger minValue() {
        return null;
      }

      @Override
      public BigInteger nextValue(final BigInteger v) {
        return v.add(BigInteger.ONE);
      }

      @Override
      public BigInteger prevValue(final BigInteger v) {
        return v.subtract(BigInteger.ONE);
      }
    };

    public T maxValue();
    public T minValue();
    public T nextValue(T v);
    public T prevValue(T v);
  }

  @SuppressWarnings("rawtypes")
  private static Spec getSpec(final Class<?> cls) {
    if (Integer.class.equals(cls))
      return Spec.INTEGER;

    if (Long.class.equals(cls))
      return Spec.LONG;

    if (Double.class.equals(cls))
      return Spec.DOUBLE;

    if (Short.class.equals(cls))
      return Spec.SHORT;

    if (Byte.class.equals(cls))
      return Spec.BYTE;

    if (Float.class.equals(cls))
      return Spec.FLOAT;

    if (BigInteger.class.equals(cls))
      return Spec.BIG_INTEGER;

    throw new IllegalArgumentException("Class " + cls.getName() + " is not supported");
  }

  protected final T min;
  protected final T max;
  protected final Spec<T> spec;

  /**
   * Creates a new {@link Interval} with the provided {@code min} and {@code max} values of the {@link Byte} class.
   *
   * @param min The value of the minimum coordinate.
   * @param max The value of the maximum coordinate.
   * @throws IllegalArgumentException If {@code min} is greater than {@code max}.
   * @throws NullPointerException If {@code min} or {@code max} is null.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Interval(final Byte min, final Byte max) {
    this((Spec)Spec.BYTE, (T)Objects.requireNonNull(min), (T)Objects.requireNonNull(max));
  }

  /**
   * Creates a new {@link Interval} with the provided {@code min} and {@code max} values of the {@link Short} class.
   *
   * @param min The value of the minimum coordinate.
   * @param max The value of the maximum coordinate.
   * @throws IllegalArgumentException If {@code min} is greater than {@code max}.
   * @throws NullPointerException If {@code min} or {@code max} is null.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Interval(final Short min, final Short max) {
    this((Spec)Spec.SHORT, (T)Objects.requireNonNull(min), (T)Objects.requireNonNull(max));
  }

  /**
   * Creates a new {@link Interval} with the provided {@code min} and {@code max} values of the {@link Integer} class.
   *
   * @param min The value of the minimum coordinate.
   * @param max The value of the maximum coordinate.
   * @throws IllegalArgumentException If {@code min} is greater than {@code max}.
   * @throws NullPointerException If {@code min} or {@code max} is null.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Interval(final Integer min, final Integer max) {
    this((Spec)Spec.INTEGER, (T)Objects.requireNonNull(min), (T)Objects.requireNonNull(max));
  }

  /**
   * Creates a new {@link Interval} with the provided {@code min} and {@code max} values of the {@link Long} class.
   *
   * @param min The value of the minimum coordinate.
   * @param max The value of the maximum coordinate.
   * @throws IllegalArgumentException If {@code min} is greater than {@code max}.
   * @throws NullPointerException If {@code min} or {@code max} is null.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Interval(final Long min, final Long max) {
    this((Spec)Spec.LONG, (T)Objects.requireNonNull(min), (T)Objects.requireNonNull(max));
  }

  /**
   * Creates a new {@link Interval} with the provided {@code min} and {@code max} values of the {@link Float} class.
   *
   * @param min The value of the minimum coordinate.
   * @param max The value of the maximum coordinate.
   * @throws IllegalArgumentException If {@code min} is greater than {@code max}.
   * @throws NullPointerException If {@code min} or {@code max} is null.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Interval(final Float min, final Float max) {
    this((Spec)Spec.FLOAT, (T)Objects.requireNonNull(min), (T)Objects.requireNonNull(max));
  }

  /**
   * Creates a new {@link Interval} with the provided {@code min} and {@code max} values of the {@link Double} class.
   *
   * @param min The value of the minimum coordinate.
   * @param max The value of the maximum coordinate.
   * @throws IllegalArgumentException If {@code min} is greater than {@code max}.
   * @throws NullPointerException If {@code min} or {@code max} is null.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Interval(final Double min, final Double max) {
    this((Spec)Spec.DOUBLE, (T)Objects.requireNonNull(min), (T)Objects.requireNonNull(max));
  }

  /**
   * Creates a new {@link Interval} with the provided {@code min} and {@code max} values of the {@link Character} class.
   *
   * @param min The value of the minimum coordinate.
   * @param max The value of the maximum coordinate.
   * @throws IllegalArgumentException If {@code min} is greater than {@code max}.
   * @throws NullPointerException If {@code min} or {@code max} is null.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Interval(final Character min, final Character max) {
    this((Spec)Spec.CHAR, (T)Objects.requireNonNull(min), (T)Objects.requireNonNull(max));
  }

  /**
   * Creates a new {@link Interval} with the provided {@code min} and {@code max} values of the {@link BigInteger} class.
   *
   * @param min The value of the minimum coordinate.
   * @param max The value of the maximum coordinate.
   * @throws IllegalArgumentException If {@code min} is greater than {@code max}.
   * @throws NullPointerException If {@code min} or {@code max} is null.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Interval(final BigInteger min, final BigInteger max) {
    this((Spec)Spec.BIG_INTEGER, (T)Objects.requireNonNull(min), (T)Objects.requireNonNull(max));
  }

  /**
   * Creates a new {@link Interval} with the provided {@code min} and {@code max} values of the {@link Number} sub-class.
   *
   * @param <N> The type parameter of the {@code min} and {@code max} {@link Number} sub-class.
   * @param min The value of the minimum coordinate.
   * @param max The value of the maximum coordinate.
   * @throws IllegalArgumentException If {@code min} is greater than {@code max}.
   * @throws IllegalArgumentException If the class of the {@code min} or {@code max} values is not supported.
   * @throws NullPointerException If {@code min} or {@code max} is null.
   */
  @SuppressWarnings("unchecked")
  public <N extends Number>Interval(final N min, final N max) {
    this(getSpec(min.getClass()), (T)min, (T)max);
  }

  private Interval(final Spec<T> spec, final T min, final T max) {
    this.spec = spec;
    this.min = min;
    this.max = max;
    if (min.compareTo(max) > 0)
      throw new IllegalArgumentException("Illegal interval: " + toString(min, max));
  }

  /**
   * Returns the minimum coordinate of this {@link Interval}.
   *
   * @return The minimum coordinate of this {@link Interval}.
   */
  public T getMin() {
    return min;
  }

  /**
   * Returns the maximum coordinate of this {@link Interval}.
   *
   * @return The maximum coordinate of this {@link Interval}.
   */
  public T getMax() {
    return max;
  }

  /**
   * Returns a new {@link Interval} instance with the provided {@code min} and {@code max} values.
   *
   * @param min The value of the minimum coordinate.
   * @param max The value of the maximum coordinate.
   * @return A new {@link Interval} instance with the provided {@code min} and {@code max} values.
   * @throws IllegalArgumentException If {@code min} is greater than {@code max}.
   * @throws NullPointerException If {@code min} or {@code max} is null.
   */
  public Interval<T> newInterval(final T min, final T max) {
    return new Interval<>(spec, min, max);
  }

  /**
   * Returns {@code true} if the provided {@link Interval} intersects this {@link Interval}, otherwise {@code false}.
   *
   * @param interval The {@link Interval} to test for intersect.
   * @return {@code true} if the provided {@link Interval} intersects this {@link Interval}, otherwise {@code false}.
   */
  public boolean intersects(final Interval<T> interval) {
    return min.compareTo(interval.max) <= 0 && max.compareTo(interval.min) >= 0;
  }

  /**
   * Returns {@code true} if the provided {@link Interval} is contained within this {@link Interval}, otherwise {@code false}.
   *
   * @param interval The {@link Interval} to test for containment.
   * @return {@code true} if the provided {@link Interval} is contained within this {@link Interval}, otherwise {@code false}.
   */
  public boolean contains(final Interval<T> interval) {
    return min.compareTo(interval.min) <= 0 && max.compareTo(interval.max) >= 0;
  }

  /**
   * Returns {@code true} if the provided value is contained within this {@link Interval}, otherwise {@code false}.
   *
   * @param value The value to test for containment.
   * @return {@code true} if the provided value is contained within this {@link Interval}, otherwise {@code false}.
   */
  public boolean contains(final T value) {
    return min.compareTo(value) <= 0 && max.compareTo(value) >= 0;
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
   * Compares this {@link Interval} with the provided {@code min} and {@code max} coordinates for order, and returns an integer
   * value of {@code -2}, {@code -1}, {@code 0}, {@code 1} or {@code 2}, based on the following logic:
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
   * @param min The value of the minimum coordinate to compare with.
   * @param max The value of the maximum coordinate to compare with.
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
   * @param min The value of the minimum coordinate to test for equality.
   * @param max The value of the maximum coordinate to test for equality.
   * @return {@code true} if the provided {@code min} value equals the {@link #getMin() min} value, and the provided {@code max}
   *         value equals the {@link #getMax() max} value of this {@link Interval}, otherwise {@code false}.
   */
  public boolean equals(final T min, final T max) {
    return this.min.equals(min) && this.max.equals(max);
  }

  /**
   * Returns the immediately next adjacent increment of the provided value, or the same value if the provided value equals the
   * maximum value of the value's class.
   *
   * @param v The value for which to determine the immediately next adjacent increment.
   * @return The immediately next adjacent increment of the provided value, or the same value if the provided value equals the
   *         maximum value of the value's class.
   */
  public T nextValue(final T v) {
    return v.equals(spec.maxValue()) ? v : spec.nextValue(v);
  }

  /**
   * Returns the immediately previous adjacent increment of the provided value, or the same value if the provided value equals the
   * minimum value of the value's class.
   *
   * @param v The value for which to determine the immediately previous adjacent increment.
   * @return The immediately previous adjacent increment of the provided value, or the same value if the provided value equals the
   *         minimum value of the value's class.
   */
  public T prevValue(final T v) {
    return v.equals(spec.minValue()) ? v : spec.prevValue(v);
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
   * Returns the string representation of an interval defined by the provided {@code min} and {@code max} coordinates.
   *
   * @param min The value of the minimum coordinate.
   * @param max The value of the maximum coordinate.
   * @return The string representation of an interval defined by the provided {@code min} and {@code max} coordinates.
   * @implNote This method does not assert non-null values, nor whether {@code min} is less than or equal to {@code max}.
   */
  public String toString(final T min, final T max) {
    return "[" + min + "," + max + "]";
  }

  @Override
  public String toString() {
    return toString(min, max);
  }
}