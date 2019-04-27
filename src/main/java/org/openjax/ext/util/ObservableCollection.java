/* Copyright (c) 2017 OpenJAX
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

package org.openjax.ext.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A {@link DelegateCollection} that provides callback methods to observe the
 * addition and removal of elements, either due to direct method invocation on
 * the collection instance itself, or via {@link #iterator()},
 * {@link #spliterator()}, {@link #forEach(Consumer)}, and any other entrypoint
 * that facilitates modification of the elements in this list.
 *
 * @param <E> The type of elements in this collection.
 * @see #beforeAdd(Object)
 * @see #afterAdd(Object,RuntimeException)
 * @see #beforeRemove(Object)
 * @see #afterRemove(Object,RuntimeException)
 */
public abstract class ObservableCollection<E> extends DelegateCollection<E> {
  public ObservableCollection(final Collection<E> collection) {
    super(collection);
  }

  /**
   * Callback method that is invoked immediately before an element is added to
   * the enclosed {@code Collection}.
   *
   * @param e The element being added to the enclosed {@code Collection}.
   * @return If this method returns {@code false}, the subsequent add operation
   *         will not be performed; otherwise, the subsequent add
   *         operation will be performed.
   */
  protected boolean beforeAdd(final E e) {
    return true;
  }

  /**
   * Callback method that is invoked immediately after an element is added to
   * the enclosed {@code Collection}.
   *
   * @param e The element added to the enclosed {@code Collection}.
   * @param re A {@code RuntimeException} that occurred during the add
   *          operation, or null if no exception occurred.
   */
  protected void afterAdd(final E e, final RuntimeException re) {
  }

  /**
   * Callback method that is invoked immediately before an element is removed
   * from the enclosed {@code Collection}.
   *
   * @param e The element being removed from the enclosed {@code Collection}.
   * @return If this method returns {@code false}, the subsequent remove operation
   *         will not be performed; otherwise, the subsequent remove
   *         operation will be performed.
   */
  protected boolean beforeRemove(final Object e) {
    return true;
  }

  /**
   * Callback method that is invoked immediately after an element is removed
   * from the enclosed {@code Collection}.
   *
   * @param e The element removed from the enclosed {@code Collection}.
   * @param re A {@code RuntimeException} that occurred during the add
   *          operation, or null if no exception occurred.
   */
  protected void afterRemove(final Object e, final RuntimeException re) {
  }

  /**
   * Returns an iterator over the elements in this collection. Calling
   * {@link Iterator#remove()} will delegate a callback to
   * {@link #beforeRemove(Object)} and
   * {@link #afterRemove(Object,RuntimeException)} on this instance. All
   * elements for which {@link #beforeRemove(Object)} returns false will not be
   * removed from this collection.
   *
   * @return An {@code Iterator} over the elements in this collection.
   * @see Collection#iterator()
   */
  @Override
  public Iterator<E> iterator() {
    final Iterator<E> iterator = target.iterator();
    return new Iterator<E>() {
      private E current;

      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public E next() {
        return current = iterator.next();
      }

      @Override
      public void remove() {
        final E remove = current;
        if (ObservableCollection.this.beforeRemove(remove)) {
          RuntimeException re = null;
          try {
            iterator.remove();
          }
          catch (final RuntimeException t) {
            re = t;
          }

          ObservableCollection.this.afterRemove(remove, re);
          if (re != null)
            throw re;
        }
      }

      @Override
      public void forEachRemaining(final Consumer<? super E> action) {
        iterator.forEachRemaining(action);
      }
    };
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeAdd(Object)} and
   * {@link ObservableCollection#afterAdd(Object, RuntimeException)} are called
   * immediately before and after the enclosed collection is modified. If
   * {@link #beforeAdd(Object)} returns false, the element will not be added.
   */
  @Override
  public boolean add(final E e) {
    if (!beforeAdd(e))
      return false;

    boolean result = false;
    RuntimeException re = null;
    try {
      result = target.add(e);
    }
    catch (final RuntimeException t) {
      re = t;
    }

    afterAdd(e, re);
    if (re != null)
      throw re;

    return result;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeAdd(Object)} and
   * {@link #afterAdd(Object,RuntimeException)} are called immediately before
   * and after the enclosed collection is modified for the addition of each
   * element in the argument Collection. All elements for which
   * {@link #beforeAdd(Object)} returns false will not be added to this
   * collection.
   */
  @Override
  public boolean addAll(final Collection<? extends E> c) {
    boolean changed = false;
    for (final E e : c)
      changed |= add(e);

    return changed;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(Object)} and
   * {@link #afterRemove(Object,RuntimeException)} are called immediately before
   * and after the enclosed collection is modified. If
   * {@link #beforeRemove(Object)} returns false, the element will not be
   * removed.
   */
  @Override
  public boolean remove(final Object o) {
    beforeRemove(o);
    boolean result = false;
    RuntimeException re = null;
    try {
      result = target.remove(o);
    }
    catch (final RuntimeException t) {
      re = t;
    }

    afterRemove(o, re);
    if (re != null)
      throw re;

    return result;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(Object)} and
   * {@link #afterRemove(Object,RuntimeException)} are called immediately
   * before and after the enclosed collection is modified for the removal of
   * each element in the argument Collection. All elements for which
   * {@link #beforeRemove(Object)} returns false will not be removed from this
   * collection.
   */
  @Override
  @SuppressWarnings("unlikely-arg-type")
  public boolean removeAll(final Collection<?> c) {
    boolean changed = false;
    for (final Object e : c)
      changed |= remove(e);

    return changed;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(Object)} and
   * {@link #afterRemove(Object,RuntimeException)} are called immediately
   * before and after the enclosed collection is modified for the removal of
   * each element. If {@link #beforeRemove(Object)} returns false, the element
   * will not be removed.
   */
  @Override
  public boolean removeIf(final Predicate<? super E> filter) {
    boolean changed = false;
    final Iterator<E> iterator = iterator();
    while (iterator.hasNext()) {
      final E element = iterator.next();
      if (filter.test(element)) {
        beforeRemove(element);
        RuntimeException re = null;
        try {
          iterator.remove();
        }
        catch (final RuntimeException t) {
          re = t;
        }

        afterRemove(element, re);
        if (re != null)
          throw re;

        changed = true;
      }
    }

    return changed;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(Object)} and
   * {@link #afterRemove(Object,RuntimeException)} are called immediately
   * before and after the enclosed collection is modified for the removal of
   * each element not in the argument Collection.
   */
  @Override
  @SuppressWarnings("unlikely-arg-type")
  public boolean retainAll(final Collection<?> c) {
    boolean changed = false;
    final Iterator<E> iterator = iterator();
    while (iterator.hasNext()) {
      if (!c.contains(iterator.next())) {
        iterator.remove();
        changed = true;
      }
    }

    return changed;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(Object)} and
   * {@link #afterRemove(Object,RuntimeException)} are called immediately
   * before and after the enclosed collection is modified for the removal of
   * each element.
   */
  @Override
  public void clear() {
    final Iterator<E> iterator = iterator();
    while (iterator.hasNext()) {
      iterator.next();
      iterator.remove();
    }
  }
}