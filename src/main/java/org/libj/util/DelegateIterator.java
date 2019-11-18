/* Copyright (c) 2016 LibJ
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
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A {@link DelegateIterator} contains some other {@link Iterator}, to which
 * it delegates its method calls, possibly transforming the data along the way or
 * providing additional functionality. The class {@link DelegateIterator} itself
 * simply overrides all methods of {@link Iterator} with versions that pass all
 * requests to the source {@link Iterator}. Subclasses of {@link DelegateIterator}
 * may further override some of these methods and may also provide additional
 * methods and fields.
 *
 * @param <E> The type of elements returned by this iterator.
 */
public abstract class DelegateIterator<E> implements Iterator<E> {
  /**
   * The target Iterator.
   */
  @SuppressWarnings("rawtypes")
  protected volatile Iterator target;

  /**
   * Creates a new {@link DelegateIterator} with the specified {@code target}.
   *
   * @param target The target {@link Iterator} object.
   * @throws NullPointerException If {@code target} is null.
   */
  public DelegateIterator(final Iterator<E> target) {
    this.target = Objects.requireNonNull(target);
  }

  /**
   * Creates a new {@link DelegateIterator} with a null source.
   */
  protected DelegateIterator() {
  }

  @Override
  public final boolean hasNext() {
    return target.hasNext();
  }

  @Override
  @SuppressWarnings("unchecked")
  public E next() {
    return (E)target.next();
  }

  @Override
  public void remove() {
    target.remove();
  }

  @Override
  public void forEachRemaining(final Consumer<? super E> action) {
    target.forEachRemaining(action);
  }
}