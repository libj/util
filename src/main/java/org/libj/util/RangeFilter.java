/* Copyright (c) 2015 LibJ
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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract "API proxy" that facilitates the selection and insertion of data that is sorted by a dimension that can be denoted as
 * a "range" (such as time or distance). A developer can chain separate implementations of {@link RangeFilter}, allowing
 * higher-level patterns to manage selected or inserted data in a memory cache (fastest), vs database calls (slower), vs API calls
 * (slowest). When data is fetched (via the {@link #fetch(Comparable,Comparable)} method), the {@link RangeFilter} attempts to
 * retrieve the requested range from the "top" tier of the stack. If all or a part of the range is not present in the tier, the
 * {@link RangeFilter} attempts to fetch the missing portion of the range from the next tier. Upon successfully fetching the data,
 * the {@link RangeFilter} thereafter inserts the fetched data into the top tier, thus resembling a caching mechanism. The
 * {@link RangeFilter} is intended to provide an abstraction for the caching of range data belonging to a data source that is
 * expensive to call, such as a remote database.
 *
 * @param <A> Type parameter of the "range" data.
 * @param <B> Type parameter of the data.
 */
public abstract class RangeFilter<A extends Comparable<A>,B> {
  private static final Logger logger = LoggerFactory.getLogger(RangeFilter.class);

  private final RangeFilter<A,B> next;

  /**
   * Creates a {@link RangeFilter} with the specified next {@link RangeFilter} that represents the next tier.
   *
   * @param next The {@link RangeFilter} that represents the next tier.
   */
  public RangeFilter(final RangeFilter<A,B> next) {
    this.next = next;
  }

  /**
   * Returns a {@link Map} of data from {@code from} (inclusive) to {@code to} (exclusive).
   *
   * @param from The lower bound of the range, inclusive.
   * @param to The upper bound of the range, exclusive.
   * @return A {@link Map} of data from {@code from} (inclusive) to {@code to} (exclusive).
   */
  public Map<A,B> fetch(final A from, final A to) {
    return fetch(from, to, null);
  }

  /**
   * Returns a {@link Map} of data from {@code from} (inclusive) to {@code to} (exclusive).
   *
   * @param from The lower bound of the range, inclusive.
   * @param to The upper bound of the range, exclusive.
   * @param prev The {@link RangeFilter} representing the previous tier.
   * @return A {@link Map} of data from {@code from} (inclusive) to {@code to} (exclusive).
   */
  public Map<A,B> fetch(final A from, final A to, final RangeFilter<A,B> prev) {
    final A[] range = range();
    final A r0;
    final A r1;
    if (range == null || (r0 = range[0]) == (r1 = range[1])) {
      if (next == null)
        return null;

      final Map<A,B> data = next.fetch(from, to, prev);
      insert(from, to, data);
      return data;
    }

    if (this != prev) {
      if (to.compareTo(r0) <= 0) {
        if (logger.isTraceEnabled()) { logger.trace(this + "{1} (" + from + ", " + r0 + "]"); }
        insert(from, r0, next.fetch(from, r0, prev));
      }
      else if (r1.compareTo(from) <= 0) {
        if (logger.isTraceEnabled()) { logger.trace(this + " {2} (" + r1 + ", " + to + "]"); }
        insert(r1, to, next.fetch(r1, to, prev));
      }
      else {
        if (from.compareTo(r0) < 0) {
          if (logger.isTraceEnabled()) { logger.trace(this + " {3} (" + from + ", " + r0 + "]"); }
          insert(from, r0, next.fetch(from, r0, prev));
        }

        if (r1.compareTo(to) < 0) {
          if (logger.isTraceEnabled()) { logger.trace(this + " {3} (" + r1 + ", " + to + "]"); }
          insert(r1, to, next.fetch(r1, to, prev));
        }
      }
    }

    return select(from, to);
  }

  /**
   * Returns the range of the keys present in this {@link RangeFilter}, as an array of length 2. Must not be null, and must be of
   * length 2.
   *
   * @return The not-null range of the keys present in this {@link RangeFilter}, as an array of length 2.
   */
  protected abstract A[] range();

  /**
   * Returns a {@link Map} of data in this {@link RangeFilter} for the range between {@code from} and {@code to}.
   *
   * @param from The start of the range, inclusive.
   * @param to The end of the range, exclusive.
   * @return A {@link Map} of data in this {@link RangeFilter} for the range between {@code from} and {@code to}.
   */
  protected abstract Map<A,B> select(A from, A to);

  /**
   * Inserts a {@link Map} of {@code data} into this {@link RangeFilter} for the range between {@code from} and {@code to}.
   *
   * @param from The start of the range, inclusive.
   * @param to The end of the range, exclusive.
   * @param data The {@link Map}.
   */
  protected abstract void insert(A from, A to, Map<A,B> data);
}