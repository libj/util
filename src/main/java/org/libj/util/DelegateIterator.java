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

import static org.libj.lang.Assertions.*;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A {@link DelegateIterator} contains some other {@link Iterator}, to which it delegates its method calls, possibly transforming
 * the data along the way or providing additional functionality. The class {@link DelegateIterator} itself simply overrides all
 * methods of {@link Iterator} with versions that pass all requests to the source {@link Iterator}. Subclasses of
 * {@link DelegateIterator} may further override some of these methods and may also provide additional methods and fields.
 *
 * @param <E> The type of elements returned by this iterator.
 */
public abstract class DelegateIterator<E> extends AbstractIterator<E> {
  /** The target {@link Iterator}. */
  @SuppressWarnings("rawtypes")
  protected volatile Iterator target;

  /**
   * Creates a new {@link DelegateIterator} with the specified target {@link Iterator}.
   *
   * @param target The target {@link Iterator}.
   * @throws IllegalArgumentException If the target {@link Iterator} is null.
   */
  public DelegateIterator(final Iterator<E> target) {
    this.target = assertNotNull(target);
  }

  /**
   * Creates a new {@link DelegateIterator} with a null target.
   */
  protected DelegateIterator() {
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
  public void remove() {
    target.remove();
  }

  /**
   * Protected method providing access to the default implementation of {@link Iterator#forEachRemaining(Consumer)}.
   *
   * @param action The action to be performed for each element.
   * @throws NullPointerException If the specified action is null.
   */
  protected final void superForEachRemaining(final Consumer<? super E> action) {
    super.forEachRemaining(action);
  }

  @Override
  public void forEachRemaining(final Consumer<? super E> action) {
    target.forEachRemaining(action);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof DelegateIterator))
      return Objects.equals(target, obj);

    final DelegateIterator<?> that = (DelegateIterator<?>)obj;
    return Objects.equals(target, that.target);
  }

  @Override
  public int hashCode() {
    int hashCode = 1;
    if (target != null)
      hashCode = 31 * hashCode + target.hashCode();

    return hashCode;
  }

  @Override
  public String toString() {
    return String.valueOf(target);
  }
}