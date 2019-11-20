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
import java.util.Objects;

/**
 * An {@link Iterator} for N-dimensional sequentially accessible collections of
 * type {@code <T>} (arrays or lists) that iterates through the leaf members of
 * the collections of type {@code <E>}.
 *
 * @param <E> The type of leaf member elements.
 * @param <T> The type of the collection.
 */
public abstract class FlatSequentialIterator<E,T> extends FlatIterator<E,T> {
  private final ArrayIntList indices = new ArrayIntList();

  /**
   * Creates a new {@link FlatSequentialIterator} for collection to be iterated.
   *
   * @param c The collection to be iterated.
   * @throws NullPointerException If the specified collection is null.
   */
  public FlatSequentialIterator(final T c) {
    stack.add(Objects.requireNonNull(c));
    indices.add(0);
  }

  /**
   * Returns the size of the sequentially accessible collection.
   *
   * @param c The sequentially accessible collection.
   * @return The size of the sequentially accessible collection.
   */
  protected abstract int size(T c);

  /**
   * Returns the element at index {@code i} of the sequentially accessible
   * collection {@code obj}.
   *
   * @param c The sequentially accessible collection.
   * @param i The index of the element.
   * @return The element at index {@code i} of the sequentially accessible
   *         collection {@code obj}.
   */
  protected abstract Object get(T c, int i);

  @Override
  public boolean hasNext() {
    if (hasNext)
      return true;

    T current;
    for (int i = indices.peek();;) {
      while (i == size(current = stack.get(stack.size() - 1))) {
        indices.pop();
        stack.remove(stack.size() - 1);
        if (indices.size() == 0)
          return false;

        i = indices.pop() + 1;
        indices.add(i);
      }

      final Object obj = get(current, i);
      if (obj == null || !isIterable(obj))
        return hasNext = true;

      stack.add(current = (T)obj);
      indices.add(i = 0);
    }
  }

  @Override
  public E next() {
    if (!hasNext && !hasNext())
      throw new NoSuchElementException();

    final int i = indices.pop();
    indices.add(i + 1);
    final E next = (E)get(stack.get(stack.size() - 1), i);
    hasNext = false;
    return next;
  }
}