/* Copyright (c) 2017 lib4j
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

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Iterators {
  public static int getSize(final Iterator<?> iterator) {
    int i = 0;
    for (; iterator.hasNext(); i++)
      iterator.next();

    return i;
  }

  public static <T>Iterator<? extends T> filter(final Iterator<?> iterator, final Class<T> type) {
    return new Iterator<T>() {
      private boolean consumed = true;
      private T next;

      @Override
      @SuppressWarnings("unchecked")
      public boolean hasNext() {
        if (!consumed)
          return true;

        if (!iterator.hasNext()) {
          next = null;
          return false;
        }

        Object member;
        while (iterator.hasNext()) {
          if (type.isInstance(member = iterator.next())) {
            next = (T)member;
            consumed = false;
            return true;
          }
        }

        next = null;
        return false;
      }

      @Override
      public T next() {
        if (!hasNext())
          throw new NoSuchElementException();

        consumed = true;
        return next;
      }
    };
  }

  private Iterators() {
  }
}