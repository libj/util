/* Copyright (c) 2017 lib4j
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

package org.lib4j.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Wrapper class for the <code>Set</code> interface that provides callback
 * methods to observe the addition and removal of elements to the wrapped
 * <code>Set</code>.
 *
 * @see ObservableSet#beforeAdd(Object)
 * @see ObservableSet#afterAdd(Object)
 * @see ObservableSet#beforeRemove(Object)
 * @see ObservableSet#afterRemove(Object)
 * @see Set
 */
public class ObservableSet<E> extends WrappedSet<E> {
  public ObservableSet(final Set<E> set) {
    super(set);
  }

  /**
   * Callback method that is invoked immediately before an element is added to
   * the enclosed <code>Set</code>.
   *
   * @param e The element being added to the enclosed <code>Set</code>.
   */
  protected void beforeAdd(final E e) {
  }

  /**
   * Callback method that is invoked immediately before an element is removed
   * from the enclosed <code>Set</code>.
   *
   * @param e The element being removed from the enclosed <code>Set</code>.
   */
  protected void beforeRemove(final Object e) {
  }

  /**
   * Callback method that is invoked immediately after an element is added to
   * the enclosed <code>Set</code>.
   *
   * @param e The element added to the enclosed <code>Set</code>.
   */
  protected void afterAdd(final E e) {
  }

  /**
   * Callback method that is invoked immediately after an element is removed
   * from the enclosed <code>Set</code>.
   *
   * @param e The element removed from the enclosed <code>Set</code>.
   */
  protected void afterRemove(final Object e) {
  }

  /**
   * Returns an iterator over the elements in this collection. Calling
   * <code>Iterator.remove()</code> will delegate a callback to
   * <code>beforeRemove()</code> and <code>afterRemove()</code> on the instance
   * of this <code>ObservableCollection</code>.
   *
   * @see Set#iterator()
   * @return an <tt>Iterator</tt> over the elements in this collection
   */
  @Override
  public Iterator<E> iterator() {
    final Iterator<E> i = source.iterator();
    return new Iterator<E>() {
      private E current;

      @Override
      public boolean hasNext() {
        return i.hasNext();
      }

      @Override
      public E next() {
        return current = i.next();
      }

      @Override
      public void remove() {
        final E remove = current;
        ObservableSet.this.beforeRemove(remove);
        i.remove();
        ObservableSet.this.afterRemove(remove);
      }

      @Override
      public void forEachRemaining(final Consumer<? super E> action) {
        i.forEachRemaining(action);
      }
    };
  }

  /**
   * Ensures that this collection contains the specified element (optional
   * operation). The callback methods <code>beforeAdd()</code> and
   * <code>afterAdd()</code> are called immediately before and after the
   * enclosed collection is modified.
   *
   * @see Set#add(Object)
   */
  @Override
  public boolean add(final E e) {
    beforeAdd(e);
    final boolean result = source.add(e);
    afterAdd(e);
    return result;
  }

  /**
   * Adds all of the elements in the specified collection to this collection
   * (optional operation). The callback methods <code>beforeAdd()</code> and
   * <code>afterAdd()</code> are called immediately before and after the
   * enclosed collection is modified for the addition of each element
   * in the argument Collection.
   */
  @Override
  public boolean addAll(final Collection<? extends E> c) {
    boolean changed = false;
    for (final E element : c)
      changed = add(element) || changed;

    return changed;
  }

  /**
   * Removes a single instance of the specified element from this collection,
   * if it is present (optional operation). The callback methods
   * <code>beforeRemove()</code> and <code>afterRemove()</code> are called
   * immediately before and after the enclosed collection is
   * modified.
   *
   * @see Set#add(Object)
   */
  @Override
  public boolean remove(final Object o) {
    beforeRemove(o);
    final boolean result = source.remove(o);
    afterRemove(o);
    return result;
  }

  /**
   * Removes all of this collection's elements that are also contained in the
   * specified collection (optional operation). The callback methods
   * <code>beforeRemove()</code> and <code>afterRemove()</code> are called
   * immediately before and after the enclosed collection is
   * modified for the removal of each element in the argument Collection.
   */
  @Override
  public boolean removeAll(final Collection<?> c) {
    boolean changed = false;
    for (final Object element : c)
      changed = remove(element) || changed;

    return changed;
  }

  /**
   * Removes all of the elements of this collection that satisfy the given
   * predicate. The callback methods <code>beforeRemove()</code> and
   * <code>afterRemove()</code> are called immediately before and after the
   * enclosed collection is modified for the removal of each element.
   */
  @Override
  public boolean removeIf(final Predicate<? super E> filter) {
    boolean changed = false;
    final Iterator<E> i = iterator();
    while (i.hasNext()) {
      final E e = i.next();
      if (filter.test(e)) {
        beforeRemove(e);
        i.remove();
        afterRemove(e);
        changed = true;
      }
    }

    return changed;
  }

  /**
   * Retains only the elements in this collection that are contained in the
   * specified collection (optional operation). The callback methods
   * <code>beforeRemove()</code> and <code>afterRemove()</code> are called
   * immediately before and after the enclosed collection is
   * modified for the removal of each element not in the argument Collection.
   */
  @Override
  public boolean retainAll(final Collection<?> c) {
    boolean changed = false;
    final Iterator<E> i = iterator();
    while (i.hasNext()) {
      if (!c.contains(i.next())) {
        i.remove();
        changed = true;
      }
    }

    return changed;
  }

  /**
   * Removes all of the elements from this collection (optional operation).
   * The callback methods <code>beforeRemove()</code> and
   * <code>afterRemove()</code> are called immediately before and after the
   * enclosed collection is modified for the removal of each element.
   */
  @Override
  public void clear() {
    final Iterator<E> i = iterator();
    while (i.hasNext()) {
      i.next();
      i.remove();
    }
  }
}