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
 * A {@code DelegateListIterator} contains some other {@link ListIterator}, to
 * which it delegates its method calls, possibly transforming the data along the
 * way or providing additional functionality. The class
 * {@code DelegateListIterator} itself simply overrides all methods of
 * {@link ListIterator} with versions that pass all requests to the target
 * {@link ListIterator}. Subclasses of {@code DelegateListIterator} may further
 * override some of these methods and may also provide additional methods and
 * fields.
 *
 * @param <E> The type of elements returned by this iterator.
 */
public abstract class DelegateListIterator<E> implements ListIterator<E> {
  /**
   * The target ListIterator.
   */
  @SuppressWarnings("rawtypes")
  protected volatile ListIterator target;

  /**
   * Creates a new {@code DelegateListIterator} with the specified {@code target}.
   *
   * @param target The target {@link ListIterator} object.
   * @throws NullPointerException If {@code target} is null.
   */
  public DelegateListIterator(final ListIterator<? extends E> target) {
    this.target = Objects.requireNonNull(target);
  }

  /**
   * Creates a new {@code DelegateListIterator} with a null target.
   */
  protected DelegateListIterator() {
  }

  @Override
  public boolean hasNext() {
    return target.hasNext();
  }

  @Override
  @SuppressWarnings("unchecked")
  public E next() {
    return (E)target.next();
  }

  @Override
  public boolean hasPrevious() {
    return target.hasPrevious();
  }

  @Override
  @SuppressWarnings("unchecked")
  public E previous() {
    return (E)target.previous();
  }

  @Override
  public int nextIndex() {
    return target.nextIndex();
  }

  @Override
  public int previousIndex() {
    return target.previousIndex();
  }

  @Override
  public void remove() {
    target.remove();
  }

  @Override
  public void set(final E e) {
    target.set(e);
  }

  @Override
  public void add(final E e) {
    target.add(e);
  }

  @Override
  public void forEachRemaining(final Consumer<? super E> action) {
    target.forEachRemaining(action);
  }
}