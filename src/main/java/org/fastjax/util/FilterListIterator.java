/* Copyright (c) 2016 FastJAX
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

package org.fastjax.util;

import java.util.ListIterator;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A {@code FilterListIterator} contains some other {@code ListIterator}, which
 * it uses as its basic source of data, possibly transforming the data along the
 * way or providing additional functionality. The class
 * {@code FilterListIterator} itself simply overrides all methods of
 * {@code ListIterator} with versions that pass all requests to the source
 * {@code ListIterator}. Subclasses of {@code FilterListIterator} may further
 * override some of these methods and may also provide additional methods and
 * fields.
 */
public class FilterListIterator<E> implements ListIterator<E> {
  /**
   * The ListIterator to be filtered.
   */
  @SuppressWarnings("rawtypes")
  protected volatile ListIterator source;

  /**
   * Creates a new {@code FilterListIterator}.
   *
   * @param source The source {@code ListIterator} object.
   * @throws NullPointerException If {@code source} is {@code null}.
   */
  public FilterListIterator(final ListIterator<? extends E> source) {
    this.source = Objects.requireNonNull(source);
  }

  /**
   * Creates a new {@code FilterListIterator} with a null source.
   */
  protected FilterListIterator() {
  }

  @Override
  public boolean hasNext() {
    return source.hasNext();
  }

  @Override
  @SuppressWarnings("unchecked")
  public E next() {
    return (E)source.next();
  }

  @Override
  public boolean hasPrevious() {
    return source.hasPrevious();
  }

  @Override
  @SuppressWarnings("unchecked")
  public E previous() {
    return (E)source.previous();
  }

  @Override
  public int nextIndex() {
    return source.nextIndex();
  }

  @Override
  public int previousIndex() {
    return source.previousIndex();
  }

  @Override
  public void remove() {
    source.remove();
  }

  @Override
  public void set(final E e) {
    source.set(e);
  }

  @Override
  public void add(final E e) {
    source.add(e);
  }

  @Override
  public void forEachRemaining(final Consumer<? super E> action) {
    source.forEachRemaining(action);
  }
}