/* Copyright (c) 2017 LibJ
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
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Utility functions for operations pertaining to {@link Iterator}.
 */
public final class Iterators {
  private static <E>void recurseNext(final Iterator<E> iterator, final Consumer<? super E> consumer) {
    if (iterator.hasNext()) {
      final E value = iterator.next();
      recurseNext(iterator, consumer);
      consumer.accept(value);
    }
  }

  /**
   * Traverses {@code iterator} and executes {@code consumer} for each element
   * in reverse order.
   * <p>
   * Note: The implementation of this method uses recursion to execute
   * {@code consumer} for each element in reverse order. If {@code iterator} is
   * expected to have more than ~8000 elements, this method may cause a
   * {@link StackOverflowError}.
   *
   * @param <E> The type of elements in {@code iterator}.
   * @param iterator The {@link Iterator} to traverse.
   * @param consumer The {@link Consumer} to execute on each element.
   * @throws NullPointerException If {@code iterator} or {@code consumer} is null.
   */
  public static <E>void forEachRemainingReverse(final Iterator<E> iterator, final Consumer<? super E> consumer) {
    recurseNext(iterator, consumer);
  }

  /**
   * Returns the number of elements in {@code iterator}. This method iterates
   * over all elements in {@code iterator} to produce the count. Any subsequent
   * use of the {@code iterator} is not possible.
   *
   * @param iterator The {@link Iterator} in which to count elements.
   * @return The number of elements in {@code iterator}.
   * @throws NullPointerException If {@code iterator} is null.
   */
  public static int getSize(final Iterator<?> iterator) {
    int i = 0;
    for (; iterator.hasNext(); ++i)
      iterator.next();

    return i;
  }

  /**
   * Returns an {@code Iterator} that iterates over the elements of
   * {@code iterator} satisfying the {@code filter} predicate.
   *
   * @param <E> The type of elements in {@code iterator}.
   * @param iterator The source {@link Iterator}.
   * @param filter The filter {@link Predicate}.
   * @return An {@code Iterator} that iterates over the elements of
   *         {@code iterator} satisfying the {@code filter} predicate.
   * @throws NullPointerException If {@code iterator} or {@code filter} is null.
   */
  public static <E>Iterator<E> filter(final Iterator<E> iterator, final Predicate<E> filter) {
    Objects.requireNonNull(iterator);
    Objects.requireNonNull(filter);
    return new Iterator<E>() {
      private boolean consumed = true;
      private E next;

      @Override
      public boolean hasNext() {
        if (!consumed)
          return true;

        if (!iterator.hasNext()) {
          next = null;
          return false;
        }

        for (E member; iterator.hasNext();) {
          if (filter.test(member = iterator.next())) {
            next = member;
            consumed = false;
            return true;
          }
        }

        next = null;
        return false;
      }

      @Override
      public E next() {
        if (!hasNext())
          throw new NoSuchElementException();

        consumed = true;
        return next;
      }
    };
  }

  /**
   * Returns a "flat" {@link Iterator} for the specified N-dimensional array of
   * type {@code <T>}, whereby all nested array members are flattened at every
   * depth.
   *
   * @param <T> The type of the array.
   * @param <E> The type of the array members.
   * @param array The input array.
   * @return A "flat" {@link Iterator} for the specified N-dimensional array of
   *         type {@code <T>}.
   * @throws NullPointerException If the specified array is null.
   */
  public static <T,E>Iterator<E> flatIterator(final T[] array) {
    return new FlatArrayIterator<>(array);
  }

  /**
   * Returns a "flat" {@link Iterator} for the specified N-dimensional list of
   * type {@code <T>}, whereby all nested list members are flattened at every
   * depth.
   *
   * @param <T> The type of the list.
   * @param <E> The type of the list elements.
   * @param list The input list.
   * @return A "flat" {@link Iterator} for the specified N-dimensional array of
   *         type {@code <T>}.
   * @throws NullPointerException If the specified list is null.
   */
  public static <T,E>Iterator<E> flatIterator(final List<T> list) {
    return new FlatListIterator<>(list);
  }

  private Iterators() {
  }
}