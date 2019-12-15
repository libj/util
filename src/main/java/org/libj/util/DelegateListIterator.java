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
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A {@link DelegateListIterator} contains some other {@link ListIterator}, to
 * which it delegates its method calls, possibly transforming the data along the
 * way or providing additional functionality. The class
 * {@link DelegateListIterator} itself simply overrides all methods of
 * {@link ListIterator} with versions that pass all requests to the target
 * {@link ListIterator}. Subclasses of {@link DelegateListIterator} may further
 * override some of these methods and may also provide additional methods and
 * fields.
 *
 * @param <E> The type of elements returned by this iterator.
 */
public abstract class DelegateListIterator<E> extends AbstractIterator<E> implements ListIterator<E> {
  /** The target {@link ListIterator}. */
  @SuppressWarnings("rawtypes")
  protected volatile ListIterator target;

  /**
   * Creates a new {@link DelegateListIterator} with the specified target
   * {@link ListIterator}.
   *
   * @param target The target {@link ListIterator}.
   * @throws NullPointerException If the target {@link ListIterator} is null.
   */
  public DelegateListIterator(final ListIterator<? extends E> target) {
    this.target = Objects.requireNonNull(target);
  }

  /**
   * Creates a new {@link DelegateListIterator} with a null target.
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

  /**
   * Protected method providing access to the default implementation of
   * {@link Iterator#forEachRemaining(Consumer)}.
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

    if (!(obj instanceof DelegateListIterator))
      return target == null ? obj == null : target.equals(obj);

    final DelegateListIterator<?> that = (DelegateListIterator<?>)obj;
    return target == null ? that.target == null : target.equals(that.target);
  }

  @Override
  public int hashCode() {
    return 31 + (target == null ? 0 : target.hashCode());
  }

  @Override
  public String toString() {
    return String.valueOf(target);
  }
}