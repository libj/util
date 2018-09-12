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
import java.util.function.Consumer;

/**
 * Wrapper class for <code>ListIterator</code> interface that delegates all
 * methods to the wrapped instance.
 *
 * @see ListIterator
 */
public class WrappedListIterator<E> implements ListIterator<E> {
  @SuppressWarnings("rawtypes")
  protected final ListIterator source;

  public WrappedListIterator(final ListIterator<? extends E> listIterator) {
    this.source = listIterator;
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