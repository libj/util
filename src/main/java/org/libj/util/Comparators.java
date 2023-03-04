/* Copyright (c) 2018 LibJ
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

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

/**
 * Factory and utility methods for {@link Comparable} classes.
 */
public final class Comparators {
  /**
   * Creates a new {@link Comparator} that accepts a fixed declaration of terms defining an order.
   * <p>
   * When {@link Comparator#compare(Object,Object)} is invoked, the comparator first attempts to equate each argument to the order
   * terms. If either argument is matched, the term's order determines the result of the comparison. If neither argument is matched,
   * the result of the comparison is determined by with {@link Comparable#compareTo(Object)}.
   * <p>
   * The {@code order} vararg array cannot be {@code null}, but member terms can be {@code null}.
   *
   * @param <T> The type of the {@link Comparable} order arguments.
   * @param order The terms that defined the order.
   * @return A new {@link Comparators} with the specified order of terms.
   * @throws NullPointerException If {@code order} is null.
   */
  @SafeVarargs
  public static <T extends Comparable<? super T>>Comparator<T> newFixedOrderComparator(final T ... order) {
    Objects.requireNonNull(order);
    return new Comparator<T>() {
      @Override
      public int compare(final T o1, final T o2) {
        for (final T term : order) { // [A]
          if (term == null) {
            if (o1 == null)
              return o2 == null ? 0 : -1;

            if (o2 == null)
              return 1;
          }
          else {
            final int r1 = term.compareTo(o1);
            final int r2 = term.compareTo(o2);
            if (r1 == 0)
              return r2 == 0 ? 0 : -1;

            if (r2 == 0)
              return 1;
          }
        }

        return o1.compareTo(o2);
      }
    };
  }

  /**
   * Creates a new {@link Comparator} that accepts a fixed declaration of terms defining an order.
   * <p>
   * When {@link Comparator#compare(Object,Object)} is invoked, the comparator first attempts to equate each argument to the order
   * terms. If either argument is matched, the term's order determines the result of the comparison. If neither argument is matched,
   * the result of the comparison is determined by with {@link Comparable#compareTo(Object)}.
   * <p>
   * The {@code order} vararg array cannot be {@code null}, but member terms can be {@code null}.
   *
   * @param <T> The type of the object of this {@link Comparator}.
   * @param <C> The type of the {@link Comparable} order arguments.
   * @param toComparable The {@link Function} to convert a {@code <T>} object to a {@link Comparable} {@code <C>}.
   * @param order The terms that defined the order.
   * @return A new {@link Comparators} with the specified order of terms.
   * @throws NullPointerException If {@code toComparable} or {@code order} is null.
   */
  @SafeVarargs
  public static <T,C extends Comparable<? super C>>Comparator<T> newFixedOrderComparator(final Function<T,C> toComparable, final C ... order) {
    Objects.requireNonNull(toComparable);
    Objects.requireNonNull(order);
    return new Comparator<T>() {
      @Override
      public int compare(final T o1, final T o2) {
        final C c1 = toComparable.apply(o1);
        final C c2 = toComparable.apply(o2);
        for (final C term : order) { // [A]
          if (term == null) {
            if (c1 == null)
              return c2 == null ? 0 : -1;

            if (c2 == null)
              return 1;
          }
          else {
            final int r1 = term.compareTo(c1);
            final int r2 = term.compareTo(c2);
            if (r1 == 0)
              return r2 == 0 ? 0 : -1;

            if (r2 == 0)
              return 1;
          }
        }

        return c1.compareTo(c2);
      }
    };
  }

  /**
   * Creates a new {@link Comparator} that accepts a fixed declaration of terms defining an order.
   * <p>
   * When {@link Comparator#compare(Object,Object)} is invoked, the comparator first attempts to equate each argument to the order
   * terms. If either argument is matched, the term's order determines the result of the comparison. If neither argument is matched,
   * the result of the comparison is determined by with {@link Comparable#compareTo(Object)}.
   * <p>
   * The {@code order} vararg array cannot be {@code null}, but member terms can be {@code null}.
   *
   * @param <T> The type of the order arguments.
   * @param comparator The {@link Comparator}.
   * @param order The terms that defined the order.
   * @return A new {@link Comparators} with the specified order of terms.
   * @throws NullPointerException If {@code comparator} or {@code order} is null.
   */
  @SafeVarargs
  public static <T>Comparator<T> newFixedOrderComparator(final Comparator<T> comparator, final T ... order) {
    Objects.requireNonNull(comparator);
    Objects.requireNonNull(order);
    return new Comparator<T>() {
      @Override
      public int compare(final T o1, final T o2) {
        for (final T term : order) { // [A]
          if (term == null) {
            if (o1 == null)
              return o2 == null ? 0 : -1;

            if (o2 == null)
              return 1;
          }
          else {
            final int r1 = comparator.compare(term, o1);
            final int r2 = comparator.compare(term, o2);
            if (r1 == 0)
              return r2 == 0 ? 0 : -1;

            if (r2 == 0)
              return 1;
          }
        }

        return comparator.compare(o1, o2);
      }
    };
  }

  /**
   * Creates a new {@link Comparator} that accepts a fixed declaration of terms defining an order.
   * <p>
   * When {@link Comparator#compare(Object,Object)} is invoked, the comparator first attempts to equate each argument to the order
   * terms. If either argument is matched, the term's order determines the result of the comparison. If neither argument is matched,
   * the result of the comparison is determined by with {@link Comparable#compareTo(Object)}.
   * <p>
   * The {@code order} vararg array cannot be {@code null}, but member terms can be {@code null}.
   *
   * @param <T> The type of the object of this {@link Comparator}.
   * @param <C> The type of the {@link Comparable} order arguments.
   * @param toComparable The {@link Function} to convert a {@code <T>} object to a {@link Comparable} {@code <C>}.
   * @param comparator The {@link Comparator}.
   * @param order The terms that defined the order.
   * @return A new {@link Comparators} with the specified order of terms.
   * @throws NullPointerException If {@code toComparable}, {@code comparator} or {@code order} is null.
   */
  @SafeVarargs
  public static <T,C extends Comparable<? super C>>Comparator<T> newFixedOrderComparator(final Function<T,C> toComparable, final Comparator<C> comparator, final C ... order) {
    Objects.requireNonNull(comparator);
    Objects.requireNonNull(toComparable);
    Objects.requireNonNull(order);
    return new Comparator<T>() {
      @Override
      public int compare(final T o1, final T o2) {
        final C c1 = toComparable.apply(o1);
        final C c2 = toComparable.apply(o2);
        for (final C term : order) { // [A]
          if (term == null) {
            if (c1 == null)
              return c2 == null ? 0 : -1;

            if (c2 == null)
              return 1;
          }
          else {
            final int r1 = comparator.compare(term, c1);
            final int r2 = comparator.compare(term, c2);
            if (r1 == 0)
              return r2 == 0 ? 0 : -1;

            if (r2 == 0)
              return 1;
          }
        }

        return comparator.compare(c1, c2);
      }
    };
  }

  private Comparators() {
  }
}