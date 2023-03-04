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

import java.util.ListIterator;

/**
 * A {@link DelegateListIterator} that keeps track of and provides cursor index information to subclasses. The cursor computation
 * algorithm in this class complies to the specification defined in {@link ListIterator}.
 *
 * @param <E> The type of elements returned by this iterator.
 * @see ListIterator#next()
 * @see ListIterator#hasNext()
 * @see ListIterator#previous()
 * @see ListIterator#hasPrevious()
 * @see ListIterator#add(Object)
 * @see ListIterator#set(Object)
 * @see ListIterator#remove()
 */
public class CursorListIterator<E> extends DelegateListIterator<E> {
  private int lastRet;

  /**
   * Creates a new {@link CursorListIterator} for the specified {@link ListIterator}.
   *
   * @param iterator The {@link ListIterator}.
   * @throws NullPointerException If the specified {@link ListIterator} is null.
   */
  protected CursorListIterator(final ListIterator<? extends E> iterator) {
    super(iterator);
  }

  /**
   * Returns the index of the element last returned by either {@link #next()} or {@link #previous()}.
   *
   * @return The index of the element last returned by either {@link #next()} or {@link #previous()}.
   */
  protected int indexOfLast() {
    return lastRet;
  }

  /**
   * Returns the index an element would have if a subsequent call to {@link #add(Object)} would be made.
   *
   * @return The index an element would have if a subsequent call to {@link #add(Object)} would be made.
   */
  protected int indexForNext() {
    return lastRet < 0 ? lastRet : nextIndex();
  }

  /**
   * Asserts that a call to a modifying operation can be made, for which only one is allowed after each call to {@link #next()} or
   * {@link #previous()}.
   *
   * @throws IllegalStateException If a modifying operation cannot be made, because one has already occurred after the last call to
   *           {@link #next()} or {@link #previous()}.
   * @see #add(Object)
   * @see #set(Object)
   * @see #remove()
   */
  protected void assertModifiable() {
    if (lastRet < 0)
      throw new IllegalStateException();
  }

  @Override
  public E next() {
    final int index = nextIndex();
    final E e = super.next();
    lastRet = index;
    return e;
  }

  @Override
  public E previous() {
    final int index = previousIndex();
    final E e = super.previous();
    lastRet = index;
    return e;
  }

  @Override
  public void remove() {
    assertModifiable();
    super.remove();
    lastRet = -1;
  }

  @Override
  public void set(final E e) {
    assertModifiable();
    super.set(e);
    lastRet = -1;
  }

  @Override
  public void add(final E e) {
    assertModifiable();
    super.add(e);
    lastRet = -1;
  }
}