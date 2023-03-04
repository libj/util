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
import java.util.List;

/**
 * An {@link Iterator} for N-dimensional {@link List}s of type {@code <T>} that iterates through the leaf members of the
 * {@link List} of type {@code <E>}.
 *
 * @param <E> The type of leaf member elements.
 * @param <T> The type of the {@link List}.
 */
public class FlatListIterator<E,T> extends FlatSequentialIterator<E,List<T>> {
  /**
   * Creates a new {@link FlatListIterator} for {@link List} to be iterated.
   *
   * @param l The {@link List} to be iterated.
   * @throws NullPointerException If the specified {@link List} is null.
   */
  public FlatListIterator(final List<T> l) {
    super(l);
  }

  @Override
  protected int size(final List<T> c) {
    return c.size();
  }

  @Override
  protected Object get(final List<T> c, final int i) {
    return c.get(i);
  }

  @Override
  protected boolean isIterable(final Object obj) {
    return obj instanceof List;
  }
}