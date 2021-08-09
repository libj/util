/* Copyright (c) 2019 LibJ
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

/**
 * An {@link Iterator} for N-dimensional arrays of type {@code <T>} that
 * iterates through the leaf members of the array of type {@code <E>}.
 *
 * @param <E> The type of leaf member elements.
 * @param <T> The type of the array.
 */
public class FlatArrayIterator<E,T> extends FlatSequentialIterator<E,T[]> {
  /**
   * Creates a new {@link FlatArrayIterator} for array to be iterated.
   *
   * @param a The array to be iterated.
   * @throws IllegalArgumentException If the specified array is null.
   */
  public FlatArrayIterator(final T[] a) {
    super(a);
  }

  @Override
  protected int size(final T[] c) {
    return c.length;
  }

  @Override
  protected Object get(final T[] c, final int i) {
    return c[i];
  }

  @Override
  protected boolean isIterable(final Object obj) {
    return obj.getClass().isArray();
  }
}