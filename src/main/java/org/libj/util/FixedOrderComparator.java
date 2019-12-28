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

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * A {@link Comparator} that accepts a fixed definition of terms specifying an
 * order. When {@link #compare(Comparable,Comparable)} is invoked, the
 * comparator first attempts to equate each argument to the order terms. If
 * either argument is matched, the term's order determines the result of the
 * comparison. If neither argument is matched, the result of the comparison is
 * determined by with {@link Comparable#compareTo(Object)}.
 *
 * @param <T> The {@link Comparable} type of element to be compared.
 */
public class FixedOrderComparator<T extends Comparable<? super T>> implements Comparator<T>, Serializable {
  private static final long serialVersionUID = 2125376584981534112L;

  private final T[] order;

  /**
   * Creates a new {@link FixedOrderComparator} with the specified order of
   * terms. The vararg array cannot be {@code null}, but member terms can be
   * {@code null}.
   *
   * @param order The terms that defined the order.
   * @throws NullPointerException If {@code order} is null.
   */
  @SafeVarargs
  public FixedOrderComparator(final T ... order) {
    this.order = Objects.requireNonNull(order);
  }

  @Override
  public int compare(final T o1, final T o2) {
    for (final T term : order) {
      if (term == null) {
        if (o1 == null)
          return o2 == null ? 0 : -1;

        if (o2 == null)
          return 1;
      }
      else {
        if (term.equals(o1))
          return term.equals(o2) ? 0 : -1;

        if (term.equals(o2))
          return 1;
      }
    }

    return o1.compareTo(o2);
  }
}