/* Copyright (c) 2022 LibJ
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

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Generate <a href="https://en.wikipedia.org/wiki/Combination">combinations</a> of {@code n} members (of type {@code T}) of
 * {@code k} {@link Iterable}s in lexicographic order.
 *
 * @param <T> Type parameter of elements for which combinations are generated.
 */
public class CombinationIterator<T> implements Iterable<T[]>, Iterator<T[]> {
  private final Iterator<T>[] iterators;
  private final Iterable<T>[] iterables;
  private final int len;
  private final int end;

  private T[] next;
  private int index = 0;
  private Boolean hasNext;

  /**
   * Creates a new {@link CombinationIterator} with the provided {@link Iterable}s.
   *
   * @param iterables The {@link Iterable}s to be used for generation of combinations.
   * @throws NullPointerException If {@code iterables} is null.
   */
  @SafeVarargs
  @SuppressWarnings("unchecked")
  public CombinationIterator(final Iterable<T> ... iterables) {
    this.len = iterables.length;
    this.iterables = iterables;
    this.iterators = new Iterator[len];
    this.end = len - 1;

    if (len == 0)
      hasNext = false;
    else
      for (int i = index; i < len; ++i) // [A]
        iterators[i] = iterables[i].iterator();
  }

  @Override
  public boolean hasNext() {
    if (hasNext != null)
      return hasNext;

    Iterator<?> iterator;
    for (int i = end; i >= index; --i) { // [A]
      iterator = iterators[i];
      if (!iterator.hasNext()) {
        if (i == 0)
          return hasNext = false;

        index = i - 1;
        iterator = iterators[i] = iterables[i].iterator();
        if (!iterator.hasNext())
          return hasNext = false;
      }
    }

    return hasNext = true;
  }

  /**
   * Returns the array representing the next combination.
   *
   * @return The array representing the next combination.
   * @implNote Upon invocation, this method adjusts the order of the members of the same array instance, and the same array instance
   *           is returned each time.
   */
  @Override
  @SuppressWarnings("unchecked")
  public T[] next() {
    if (!hasNext())
      throw new NoSuchElementException();

    hasNext = null;
    int i = end;
    final T obj = iterators[i].next();
    if (next == null)
      next = (T[])Array.newInstance(obj.getClass(), len);

    next[i] = obj;
    while (--i >= index) // [A]
      next[i] = iterators[i].next();

    index = end;
    return next;
  }

  @Override
  public Iterator<T[]> iterator() {
    return this;
  }
}