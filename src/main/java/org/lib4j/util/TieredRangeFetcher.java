/* Copyright (c) 2015 lib4j
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

import java.util.SortedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TieredRangeFetcher<A extends Comparable<A>,B> {
  private static final Logger logger = LoggerFactory.getLogger(TieredRangeFetcher.class);

  private final TieredRangeFetcher<A,B> next;

  public TieredRangeFetcher(final TieredRangeFetcher<A,B> next) {
    this.next = next;
  }

  /**
   * fetch data for keys (A, B]
   */
  public SortedMap<A,B> fetch(final A from, final A to)  {
    return fetch(from, to, null);
  }

  /**
   * fetch data for keys (A, B]
   */
  public SortedMap<A,B> fetch(final A from, final A to, final TieredRangeFetcher<A,B> last)  {
    final A[] range = range();
    if (range == null || range[0] == range[1]) {
      if (next != null) {
        final SortedMap<A,B> data = next.fetch(from, to, last);
        insert(from, to, data);
        return data;
      }

      return null;
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
   * Returns the range of the keys present in this TieredFetcher, as an array of length 2. Must not be null, and must be of length 2.
   */
  protected abstract A[] range();
  protected abstract SortedMap<A,B> select(final A from, final A to);
  protected abstract void insert(final A from, final A to, final SortedMap<A,B> data);
}