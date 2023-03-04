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

import java.util.Collection;
import java.util.Iterator;

/**
 * An {@link Iterator} for N-dimensional {@link Collection}s of type {@code <T>} that iterates through the leaf members of the
 * {@link Collection} of type {@code <E>}.
 *
 * @param <E> The type of leaf member elements.
 * @param <T> The type of the {@link Collection}.
 */
public class FlatCollectionIterator<T,E> extends FlatIterableIterator<Collection<T>,E> {
  /**
   * Creates a new {@link FlatCollectionIterator} for {@link Collection} to be iterated.
   *
   * @param c The {@link Collection} to be iterated.
   * @throws NullPointerException If the specified {@link Collection} is null.
   */
  public FlatCollectionIterator(final Collection<T> c) {
    super(c);
  }

  @Override
  protected Iterator<?> iterator(final Collection<T> c) {
    return c.iterator();
  }

  @Override
  protected boolean isIterable(final Object obj) {
    return obj instanceof Iterable;
  }
}