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
import java.util.NoSuchElementException;

import org.libj.lang.Assertions;

/**
 * An {@link Iterator} for N-dimensional collections implementing the
 * {@link Iterable} interface of type {@code <T>} that iterates through the leaf
 * members of the collections of type {@code <E>}.
 *
 * @param <E> The type of leaf member elements.
 * @param <T> The type of the collection.
 */
public abstract class FlatIterableIterator<T,E> extends FlatIterator<E,Iterator<?>> {
  private E next;

  /**
   * Creates a new {@link FlatSequentialIterator} for collection to be iterated.
   *
   * @param c The collection to be iterated.
   * @throws IllegalArgumentException If the specified collection is null.
   */
  public FlatIterableIterator(final T c) {
    stack.add(iterator(Assertions.assertNotNull(c)));
  }

  /**
   * Returns an {@link Iterator} for the specified collection.
   *
   * @param c The collection.
   * @return An {@link Iterator} for the specified collection.
   * @throws IllegalArgumentException If the specified collection is null.
   */
  protected abstract Iterator<?> iterator(T c);

  @Override
  @SuppressWarnings("unchecked")
  public boolean hasNext() {
    if (hasNext)
      return true;

    for (Iterator<?> current;;) {
      while (!(current = stack.get(stack.size() - 1)).hasNext()) {
        stack.remove(stack.size() - 1);
        if (stack.size() == 0)
          return false;
      }

      final Object next = current.next();
      if (next == null || !isIterable(next)) {
        this.next = (E)next;
        return hasNext = true;
      }

      stack.add(iterator((T)next));
    }
  }

  @Override
  public E next() {
    if (!hasNext())
      throw new NoSuchElementException();

    hasNext = false;
    return next;
  }
}