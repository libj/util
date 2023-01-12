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

import static org.junit.Assert.*;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TieredRangeFetcherTest {
  private static final Logger logger = LoggerFactory.getLogger(TieredRangeFetcherTest.class);

  @Test
  public void testTieredFetcher() {
    final int dbFrom = 20;
    final int dbTo = 30;
    final SortedMap<Integer,Object> db = new TreeMap<>();
    for (int i = 20; i < 30; ++i) // [N]
      db.put(i, i);

    final TieredRangeFetcher<Integer,Object> webFetcher = new TieredRangeFetcher<Integer,Object>(null) {
      private final Integer[] range = {Integer.MIN_VALUE, Integer.MAX_VALUE};

      @Override
      protected Integer[] range() {
        return range;
      }

      @Override
      protected SortedMap<Integer,Object> select(final Integer from, final Integer to) {
        if (logger.isInfoEnabled()) logger.info("WEB -> (" + from + ", " + to + "]");
        final SortedMap<Integer,Object> results = new TreeMap<>();
        for (int i = from; i < to; ++i) // [N]
          results.put(i, i);

        return results;
      }

      @Override
      protected void insert(final Integer from, final Integer to, final SortedMap<Integer,Object> data) {
      }
    };

    final TieredRangeFetcher<Integer,Object> dbFetcher = new TieredRangeFetcher<Integer,Object>(webFetcher) {
      private Integer[] range = {dbFrom, dbTo};

      @Override
      protected Integer[] range() {
        return range;
      }

      @Override
      protected SortedMap<Integer,Object> select(final Integer from, final Integer to) {
        if (logger.isInfoEnabled()) logger.info("DB -> (" + from + ", " + to + "]");
        return db.subMap(from, to);
      }

      @Override
      protected void insert(final Integer from, final Integer to, final SortedMap<Integer,Object> data) {
        if (logger.isInfoEnabled()) logger.info("DB <- (" + from + ", " + to + "]");
        if (range == null) {
          range = new Integer[] {from, to};
        }
        else {
          range[0] = from < range[0] ? from : range[0];
          range[1] = range[1] < to ? to : range[1];
        }

        if (data.size() > 0) {
          for (final Map.Entry<Integer,Object> entry : data.entrySet()) { // [S]
            if (db.containsKey(entry.getKey()))
              fail("Attempted to insert a key that already exists: " + entry.getKey());
            else
              db.put(entry.getKey(), entry.getValue());
          }
        }
      }
    };

    final TieredRangeFetcher<Integer,Object> cacheFetcher = new TieredRangeFetcher<Integer,Object>(dbFetcher) {
      private final SortedMap<Integer,Object> cache = new TreeMap<>();
      private Integer[] range;

      @Override
      protected Integer[] range() {
        return range;
      }

      @Override
      protected SortedMap<Integer,Object> select(final Integer from, final Integer to) {
        if (logger.isInfoEnabled()) logger.info("CACHE -> (" + from + ", " + to + "]");
        return cache.subMap(from, to);
      }

      @Override
      protected void insert(final Integer from, final Integer to, final SortedMap<Integer,Object> data) {
        if (logger.isInfoEnabled()) logger.info("CACHE <- (" + from + ", " + to + "]");
        if (range == null) {
          range = new Integer[] {from, to};
        }
        else {
          range[0] = from < range[0] ? from : range[0];
          range[1] = range[1] < to ? to : range[1];
        }

        cache.putAll(data);
      }
    };

//  WEB -> (10, 20]
//  DB <- (10, 20]
//  CACHE <- (10, 20]
    assertEquals(10, cacheFetcher.fetch(10, 20).size());

//  WEB -> (8, 10]
//  DB <- (8, 10]
//  DB -> (8, 10]
//  CACHE <- (8, 10]
//  CACHE -> (8, 12]
    assertEquals(4, cacheFetcher.fetch(8, 12).size());

//  WEB -> (20, 24]
//  DB <- (20, 24]
//  DB -> (20, 24]
//  CACHE <- (20, 24]
//  CACHE -> (18, 24]
    assertEquals(6, cacheFetcher.fetch(18, 24).size());

//  WEB -> (24, 28]
//  DB <- (24, 28]
//  DB -> (24, 28]
//  CACHE <- (24, 28]
//  CACHE -> (28, 30]
    assertEquals(2, cacheFetcher.fetch(28, 30).size());

//  WEB -> (4, 8]
//  DB <- (4, 8]
//  DB -> (4, 8]
//  CACHE <- (4, 8]
//  CACHE -> (4, 6]
    assertEquals(2, cacheFetcher.fetch(4, 6).size());

//  CACHE -> (8, 26]
    assertEquals(18, cacheFetcher.fetch(8, 26).size());
  }
}