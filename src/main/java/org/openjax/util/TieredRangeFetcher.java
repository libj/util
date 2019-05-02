/* Copyright (c) 2015 OpenJAX
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

package org.openjax.util;

import java.util.SortedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A "data fetcher" that facilitates the retrieval of data representing
 * information pertaining to a dimension that can be denoted as a "range" (such
 * as time or distance). The {@code TieredRangeFetcher} abstracts the concept of
 * tiered data sources. When data is fetched (via the
 * {@link #fetch(Comparable,Comparable)} method), the {@code TieredRangeFetcher}
 * attempts to retrieve the requested range from the "top" tier of the stack. If
 * all or a part of the range is not present in the tier, the
 * {@code TieredRangeFetcher} attempts to fetch the missing portion of the range
 * from the next tier. Upon successfully fetching the data, the
 * {@code TieredRangeFetcher} thereafter inserts the fetched data into the top
 * tier, thus resembling a caching mechanism. The {@code TieredRangeFetcher} is
 * intended to provide an abstraction for the caching of range data belonging to
 * a data source that is expensive to call, such as a remote database.
 *
 * @param <A> Type parameter of the "range" data.
 * @param <B> Type parameter of the data.
 */
public abstract class TieredRangeFetcher<A extends Comparable<A>,B> {
  private static final Logger logger = LoggerFactory.getLogger(TieredRangeFetcher.class);

  private final TieredRangeFetcher<A,B> next;

  /**
   * Creates a {@code TieredRangeFetcher} with the specified next
   * {@code TieredRangeFetcher} that represents the next tier.
   *
   * @param next The {@code TieredRangeFetcher} that represents the next tier.
   */
  public TieredRangeFetcher(final TieredRangeFetcher<A,B> next) {
    this.next = next;
  }

  /**
   * Returns a {@code SortedMap} of data from {@code from} (inclusive) to
   * {@code to} (exclusive).
   *
   * @param from The lower bound of the range, inclusive.
   * @param to The upper bound of the range, exclusive.
   * @return A {@code SortedMap} of data from {@code from} (inclusive) to
   *         {@code to} (exclusive).
   */
  public SortedMap<A,B> fetch(final A from, final A to) {
    return fetch(from, to, null);
  }

  /**
   * Returns a {@code SortedMap} of data from {@code from} (inclusive) to
   * {@code to} (exclusive).
   *
   * @param from The lower bound of the range, inclusive.
   * @param to The upper bound of the range, exclusive.
   * @param last The {@code TieredRangeFetcher} representing the previous tier.
   * @return A {@code SortedMap} of data from {@code from} (inclusive) to
   *         {@code to} (exclusive).
   */
  public SortedMap<A,B> fetch(final A from, final A to, final TieredRangeFetcher<A,B> last) {
    final A[] range = range();
    if (range == null || range[0] == range[1]) {
      if (next == null)
        return null;

      final SortedMap<A,B> data = next.fetch(from, to, last);
      insert(from, to, data);
      return data;
    }

    if (this != last) {
      if (to.compareTo(range[0]) <= 0) {
        logger.trace(toString() + "{1} (" + from + ", " + range[0] + "]");
        insert(from, range[0], next.fetch(from, range[0], last));
      }
      else if (range[1].compareTo(from) <= 0) {
        logger.trace(toString() + " {2} (" + range[1] + ", " + to + "]");
        insert(range[1], to, next.fetch(range[1], to, last));
      }
      else {
        if (from.compareTo(range[0]) < 0) {
          logger.trace(toString() + " {3} (" + from + ", " + range[0] + "]");
          insert(from, range[0], next.fetch(from, range[0], last));
        }

        if (range[1].compareTo(to) < 0) {
          logger.trace(toString() + " {3} (" + range[1] + ", " + to + "]");
          insert(range[1], to, next.fetch(range[1], to, last));
        }
      }
    }

    return select(from, to);
  }

  /**
   * Returns the range of the keys present in this TieredFetcher, as an array of
   * length 2. Must not be null, and must be of length 2.
   *
   * @return The not-null range of the keys present in this TieredFetcher, as an
   *         array of length 2.
   */
  protected abstract A[] range();

  /**
   * Returns a {@link SortedMap} of data in this {@code TieredRangeFetcher} for
   * the range between {@code from} and {@code to}.
   *
   * @param from The start of the range, inclusive.
   * @param to The end of the range, exclusive.
   * @return A {@link SortedMap} of data in this {@code TieredRangeFetcher} for
   *         the range between {@code from} and {@code to}.
   */
  protected abstract SortedMap<A,B> select(A from, A to);

  /**
   * Inserts a {@link SortedMap} of {@code data} into this
   * {@code TieredRangeFetcher} for the range between {@code from} and
   * {@code to}.
   *
   * @param from The start of the range, inclusive.
   * @param to The end of the range, exclusive.
   * @param data The {@link SortedMap}.
   */
  protected abstract void insert(A from, A to, SortedMap<A,B> data);
}