/* Copyright (c) 2020 LibJ
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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Utility functions for operations pertaining to {@link Iterable}.
 */
public final class Iterables {
  /**
   * Creates an {@link Iterable} with the specified object.
   *
   * @param <T> The type of the specified object.
   * @param obj The object.
   * @return An {@link Iterable} with the specified object.
   */
  public static final <T>Iterable<T> singleton(final T obj) {
    return new Iterable<T>() {
      @Override
      public Iterator<T> iterator() {
        return new Iterator<T>() {
          private volatile boolean hasNext = true;

          @Override
          public boolean hasNext() {
            return hasNext;
          }

          @Override
          public T next() {
            if (!hasNext)
              throw new NoSuchElementException();

            hasNext = false;
            return obj;
          }
        };
      }
    };
  }

  private Iterables() {
  }
}