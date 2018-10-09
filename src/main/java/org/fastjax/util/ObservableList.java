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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

/**
 * Wrapper class for the {@link List} interface that provides callback methods
 * to observe the addition and removal of elements to the wrapped {@link List}.
 * <ul>
 * <li>{@link #beforeGet(int,ListIterator)}</li>
 * <li>{@link #afterGet(int,Object,ListIterator,RuntimeException)}</li>
 * <li>{@link #beforeAdd(int,Object)}</li>
 * <li>{@link #afterAdd(int,Object,RuntimeException)}</li>
 * <li>{@link #beforeRemove(int)}</li>
 * <li>{@link #afterRemove(Object,RuntimeException)}</li>
 * <li>{@link #beforeSet(int,Object)}</li>
 * <li>{@link #afterSet(int,Object,RuntimeException)}</li>
 * <li>{@link List}</li>
 * </ul>
 */
public class ObservableList<E> extends FilterList<E> {
  private final int fromIndex;
  private int toIndex;

  public ObservableList(final List<E> list) {
    this(list, 0, -1);
  }

  private ObservableList(final List<E> list, final int fromIndex, final int toIndex) {
    super(list);
    this.fromIndex = fromIndex;
    this.toIndex = toIndex;
  }

  @Override
  protected FilterList<E> newInstance(final List source) {
    return new ObservableList<E>(source);
  }

  /**
   * Callback method that is invoked immediately before an element is retrieved
   * from the enclosed {@link List}.
   *
   * @param index The index of the element being retrieved from the enclosed
   *          {@link List}.
   * @param iterator The {@link ListIterator} instance if the get is a result of
   *          an iterator reference, or null if otherwise. {@link List}.
   */
  protected void beforeGet(final int index, final ListIterator<E> iterator) {
  }

  /**
   * Callback method that is invoked immediately after an element is retrieved
   * from the enclosed {@link List}.
   *
   * @param index The index of the element being retrieved from the enclosed
   *          {@link List}.
   * @param e The element being retrieved from the enclosed {@link List}.
   * @param iterator The {@link Iterator} instance if the get is a result of an
   *          iterator reference, or null if otherwise.
   * @param re A {@code RuntimeException} that occurred during the get
   *          operation, or {@code null} if no exception occurred.
   */
  protected void afterGet(final int index, final E e, final ListIterator<E> iterator, final RuntimeException re) {
  }

  /**
   * Callback method that is invoked immediately before an element is added to
   * the enclosed {@link List}.
   *
   * @param index The index of the element being added to the enclosed
   *          {@link List}.
   * @param e The element being added to the enclosed {@link List}.
   * @return If this method returns {@code false}, the subsequent add operation
   *         will not be performed; otherwise, the subsequent add
   *         operation will be performed.
   */
  protected boolean beforeAdd(final int index, final E e) {
    return true;
  }

  /**
   * Callback method that is invoked immediately after an element is added to
   * the enclosed {@link List}.
   *
   * @param index The index of the element added to the enclosed {@link List}.
   * @param e The element being added to the enclosed {@link List}.
   * @param re A {@code RuntimeException} that occurred during the add
   *          operation, or {@code null} if no exception occurred.
   */
  protected void afterAdd(final int index, final E e, final RuntimeException re) {
  }

  /**
   * Callback method that is invoked immediately before an element is removed
   * from the enclosed {@link List}.
   *
   * @param index The index of the element being removed from the enclosed
   *          {@link List}.
   * @return If this method returns {@code false}, the subsequent remove operation
   *         will not be performed; otherwise, the subsequent remove
   *         operation will be performed.
   */
  protected boolean beforeRemove(final int index) {
    return true;
  }

  /**
   * Callback method that is invoked immediately after an element is removed
   * from the enclosed {@link List}.
   *
   * @param e The element removed from the enclosed {@link List}.
   * @param re A {@code RuntimeException} that occurred during the remove
   *          operation, or {@code null} if no exception occurred.
   */
  protected void afterRemove(final Object e, final RuntimeException re) {
  }

  /**
   * Callback method that is invoked immediately before an element is set at an
   * index to the enclosed {@link List}.
   *
   * @param index The index of the element being set in the enclosed
   *          {@link List}.
   * @param newElement The element being set to the enclosed {@link List}.
   * @return If this method returns {@code false}, the subsequent set operation
   *         will not be performed; otherwise, the subsequent set
   *         operation will be performed.
   */
  protected boolean beforeSet(final int index, final E newElement) {
    return true;
  }

  /**
   * Callback method that is invoked immediately after an element is set at an
   * index to the enclosed {@link List}.
   *
   * @param index The index of the element set in the enclosed {@link List}.
   * @param oldElement The old element at the index of the enclosed
   *          {@link List}.
   * @param re A {@code RuntimeException} that occurred during the set
   *          operation, or {@code null} if no exception occurred.
   */
  protected void afterSet(final int index, final E oldElement, final RuntimeException re) {
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeAdd(int,Object)} and
   * {@link #afterAdd(int,Object,RuntimeException)} are called immediately
   * before and after the enclosed collection is modified. If
   * {@link #beforeAdd(int,Object)} returns false, the element will not be
   * added.
   */
  @Override
  public boolean add(final E e) {
    final int index = size();
    if (!beforeAdd(index, e))
      return false;

    RuntimeException re = null;
    try {
      source.add(index + fromIndex, e);
      if (toIndex != -1)
        ++toIndex;
    }
    catch (final RuntimeException t) {
      re = t;
    }

    afterAdd(index, e, re);
    if (re != null)
      throw re;

    return true;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeAdd(int,Object)} and
   * {@link #afterAdd(int,Object,RuntimeException)} are called immediately
   * before and after the enclosed collection is modified for the addition of
   * each element in the argument Collection. If {@link #beforeAdd(int,Object)}
   * returns false, the element will not be added.
   */
  @Override
  public void add(final int index, final E element) {
    if (!beforeAdd(index, element))
      return;

    RuntimeException re = null;
    try {
      source.add(index + fromIndex, element);
      if (toIndex != -1)
        ++toIndex;
    }
    catch (final RuntimeException e) {
      re = e;
    }

    afterAdd(index, element, re);
    if (re != null)
      throw re;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeAdd(int,Object)} and
   * {@link #afterAdd(int,Object,RuntimeException)} are called immediately
   * before and after the enclosed collection is modified for the addition of
   * each element in the argument Collection. All elements for which
   * {@link #beforeAdd(int,Object)} returns false will not be added to this
   * collection.
   */
  @Override
  public boolean addAll(final Collection<? extends E> c) {
    boolean changed = false;
    for (final E element : c)
      changed = add(element) || changed;

    return changed;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeAdd(int,Object)} and
   * {@link #afterAdd(int,Object,RuntimeException)} are called immediately
   * before and after the enclosed collection is modified for the addition of
   * each element in the argument Collection. All elements for which
   * {@link #beforeAdd(int,Object)} returns false will not be added to this
   * collection.
   */
  @Override
  public boolean addAll(int index, final Collection<? extends E> c) {
    if (c.size() == 0)
      return false;

    for (final E e : c)
      add(index++, e);

    return true;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(int)} and
   * {@link #afterRemove(Object,RuntimeException)} are called immediately before
   * and after the enclosed collection is modified for the removal of each
   * element. All elements for which {@link #beforeRemove(int)} returns false
   * will not be removed from this collection.
   */
  @Override
  public void clear() {
    final Iterator<E> iterator = iterator();
    while (iterator.hasNext()) {
      iterator.next();
      iterator.remove();
    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int,ListIterator)} and
   * {@link #afterGet(int,Object,ListIterator,RuntimeException)} are called
   * immediately before and after each member of the enclosed list is
   * referenced.
   */
  @Override
  public boolean contains(final Object o) {
    final ListIterator<E> iterator = listIterator();
    if (o == null) {
      while (iterator.hasNext())
        if (iterator.next() == null)
          return true;
    }
    else {
      while (iterator.hasNext())
        if (o.equals(iterator.next()))
          return true;
    }

    return false;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int,ListIterator)} and
   * {@link #afterGet(int,Object,ListIterator,RuntimeException)} are called
   * immediately before and after each member of the enclosed list is
   * referenced.
   */
  @Override
  public boolean containsAll(final Collection<?> c) {
    for (final Object o : c)
      if (!contains(o))
        return false;

    return true;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int,ListIterator)} and
   * {@link #afterGet(int,Object,ListIterator,RuntimeException)} are called
   * immediately before and after the get operation on the enclosed collection.
   */
  @Override
  public E get(final int index) {
    beforeGet(index, null);
    E object = null;
    RuntimeException re = null;
    try {
      object = super.get(index);
    }
    catch (final RuntimeException e) {
      re = e;
    }

    afterGet(index, object, null, re);
    if (re != null)
      throw re;

    return object;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int,ListIterator)} and
   * {@link #afterGet(int,Object,ListIterator,RuntimeException)} are called
   * immediately before and after each member of the enclosed list is
   * referenced.
   */
  @Override
  public int indexOf(final Object o) {
    final ListIterator<E> iterator = listIterator();
    if (o == null) {
      for (int i = 0; iterator.hasNext(); i++)
        if (iterator.next() == null)
          return i;
    }
    else {
      for (int i = 0; iterator.hasNext(); i++)
        if (o.equals(iterator.next()))
          return i;
    }

    return -1;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int,ListIterator)} and
   * {@link #afterGet(int,Object,ListIterator,RuntimeException)} are called
   * immediately before and after each member of the enclosed list is
   * referenced.
   */
  @Override
  public int lastIndexOf(final Object o) {
    final ListIterator<E> iterator = listIterator(size());
    if (o == null) {
      for (int i = 0; iterator.hasPrevious(); i++)
        if (iterator.previous() == null)
          return i;
    }
    else {
      for (int i = 0; iterator.hasPrevious(); i++)
        if (o.equals(iterator.previous()))
          return i;
    }

    return -1;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Calling {@link Iterator#remove()} will delegate a callback to
   * {@link #beforeRemove(int)} and
   * {@link #afterRemove(Object,RuntimeException)} on this instance. All
   * elements for which {@link #beforeRemove(int)} returns false will not be
   * removed from this collection.
   */
  @Override
  public Iterator<E> iterator() {
    return listIterator();
  }

  /**
   * {@inheritDoc}
   * <p>
   * Calling {@link ListIterator#remove()} will delegate a callback to
   * {@link #beforeRemove(int)} and
   * {@link #afterRemove(Object,RuntimeException)} on this instance. All
   * elements for which {@link #beforeRemove(int)} returns false will not be
   * removed from this collection. Calling {@link ListIterator#set(Object)} will
   * delegate a callback to {@link #beforeSet(int,Object)} and
   * {@link #afterSet(int,Object,RuntimeException)} on this instance. All
   * elements for which {@link #beforeSet(int,Object)} returns false will not be
   * set in this collection. Calling {@link ListIterator#add(Object)} will
   * delegate a callback to {@link #beforeAdd(int,Object)} and
   * {@link #afterAdd(int,Object,RuntimeException)} on this instance. All
   * elements for which {@link #beforeAdd(int,Object)} returns false will not be
   * added to this collection.
   */
  @Override
  public ListIterator<E> listIterator() {
    return listIterator(0);
  }

  /**
   * {@inheritDoc}
   * <p>
   * Calling {@link ListIterator#remove()} will delegate a callback to
   * {@link #beforeRemove(int)} and
   * {@link #afterRemove(Object,RuntimeException)} on this instance. All
   * elements for which {@link #beforeRemove(int)} returns false will not be
   * removed from this collection. Calling {@link ListIterator#set(Object)} will
   * delegate a callback to {@link #beforeSet(int,Object)} and
   * {@link #afterSet(int,Object,RuntimeException)} on this instance. All
   * elements for which {@link #beforeSet(int,Object)} returns false will not be
   * set in this collection. Calling {@link ListIterator#add(Object)} will
   * delegate a callback to {@link #beforeAdd(int,Object)} and
   * {@link #afterAdd(int,Object,RuntimeException)} on this instance. All
   * elements for which {@link #beforeAdd(int,Object)} returns false will not be
   * added to this collection.
   */
  @Override
  public ListIterator<E> listIterator(final int index) {
    if (index > size())
      throw new IndexOutOfBoundsException("Invalid index " + index + ", size is " + size());

    final ListIterator<E> listIterator = source.listIterator(index + fromIndex);
    return new ListIterator<E>() {
      private E current;

      @Override
      public boolean hasNext() {
        return nextIndex() < size();
      }

      @Override
      public E next() {
        final int index = listIterator.nextIndex() - fromIndex;
        beforeGet(index, this);
        RuntimeException re = null;
        try {
          current = listIterator.next();
        }
        catch (final RuntimeException e) {
          re = e;
        }

        afterGet(index, current, this, re);
        if (re != null)
          throw re;

        return current;
      }

      @Override
      public boolean hasPrevious() {
        return previousIndex() >= 0;
      }

      @Override
      public E previous() {
        final int index = listIterator.previousIndex() - fromIndex;
        beforeGet(index, this);
        RuntimeException re = null;
        try {
          current = listIterator.previous();
        }
        catch (final RuntimeException e) {
          re = e;
        }

        afterGet(index, current, this, re);
        if (re != null)
          throw re;

        return current;
      }

      @Override
      public int nextIndex() {
        return listIterator.nextIndex() - fromIndex;
      }

      @Override
      public int previousIndex() {
        return listIterator.previousIndex() - fromIndex;
      }

      @Override
      public void remove() {
        if (!beforeRemove(nextIndex() - 1))
          return;

        RuntimeException re = null;
        try {
          listIterator.remove();
          if (toIndex != -1)
            --toIndex;
        }
        catch (final RuntimeException e) {
          re = e;
        }

        afterRemove(current, re);
        if (re != null)
          throw re;
      }

      @Override
      public void set(final E e) {
        final int index = nextIndex() - 1;
        if (!beforeSet(index, e))
          return;

        final E remove = current;
        RuntimeException re = null;
        try {
          listIterator.set(e);
        }
        catch (final RuntimeException t) {
          re = t;
        }

        afterSet(index, remove, re);
        if (re != null)
          throw re;
      }

      @Override
      public void add(final E e) {
        final int index = nextIndex();
        if (!beforeAdd(index, e))
          return;

        RuntimeException re = null;
        try {
          listIterator.add(e);
          if (toIndex != -1)
            ++toIndex;
        }
        catch (final RuntimeException t) {
          re = t;
        }

        afterAdd(index, e, re);
        if (re != null)
          throw re;
      }
    };
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(int)} and
   * {@link #afterRemove(Object,RuntimeException)} are called immediately before
   * and after the enclosed collection is modified for the removal of the
   * element. If {@link #beforeRemove(int)} returns false, the element will not
   * be removed.
   */
  @Override
  @SuppressWarnings("unchecked")
  public E remove(final int index) {
    final E element = (E)source.get(index + fromIndex);
    if (!beforeRemove(index))
      return element;

    RuntimeException re = null;
    try {
      source.remove(index + fromIndex);
      if (toIndex != -1)
        --toIndex;
    }
    catch (final RuntimeException t) {
      re = t;
    }

    afterRemove(element, re);
    if (re != null)
      throw re;

    return element;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(int)} and
   * {@link #afterRemove(Object,RuntimeException)} are called immediately before
   * and after the enclosed collection is modified. If
   * {@link #beforeRemove(int)} returns false, the element will not be removed.
   */
  @Override
  public boolean remove(final Object o) {
    final int index = indexOf(o);
    if (index == -1)
      return false;

    if (!beforeRemove(index))
      return false;

    RuntimeException re = null;
    try {
      source.remove(index + fromIndex);
      if (toIndex != -1)
        --toIndex;
    }
    catch (final RuntimeException t) {
      re = t;
    }

    afterRemove(o, re);
    if (re != null)
      throw re;

    return true;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(int)} and
   * {@link #afterRemove(Object,RuntimeException)} are called immediately before
   * and after the enclosed collection is modified for the removal of each
   * element in the argument Collection. All elements for which
   * {@link #beforeRemove(int)} returns false will not be removed from this
   * collection.
   *
   * @see Collection#removeAll(Collection)
   */
  @Override
  public boolean removeAll(final Collection<?> c) {
    boolean changed = false;
    for (final Object element : c)
      changed = remove(element) || changed;

    return changed;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(int)} and
   * {@link #afterRemove(Object,RuntimeException)} are called immediately before
   * and after the enclosed collection is modified for the removal of each
   * element. All elements for which {@link #beforeRemove(int)} returns false
   * will not be removed from this collection.
   */
  @Override
  public boolean removeIf(final Predicate<? super E> filter) {
    boolean changed = false;
    final ListIterator<E> iterator = listIterator();
    while (iterator.hasNext()) {
      final E element = iterator.next();
      if (filter.test(element) && beforeRemove(iterator.nextIndex() - 1 - fromIndex)) {
        RuntimeException re = null;
        try {
          iterator.remove();
          if (toIndex != -1)
            --toIndex;
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
   * The callback methods {@link #beforeRemove(int)} and
   * {@link #afterRemove(Object,RuntimeException)} are called immediately before
   * and after the enclosed collection is modified for the removal of each
   * element not in the argument Collection. All elements for which
   * {@link #beforeRemove(int)} returns false will not be removed from this
   * collection.
   */
  @Override
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
   * The callback methods {@link #beforeSet(int,Object)} and
   * {@link #afterSet(int,Object,RuntimeException)} are called immediately
   * before and after the enclosed collection is modified. All elements for
   * which {@link #beforeSet(int,Object)} returns false will be skipped.
   */
  @Override
  @SuppressWarnings("unchecked")
  public E set(final int index, final E element) {
    if (!beforeSet(index, element))
      return (E)source.get(index + fromIndex);

    E oldElement = null;
    RuntimeException re = null;
    try {
      oldElement = (E)source.set(index + fromIndex, element);
    }
    catch (final RuntimeException t) {
      re = t;
    }

    afterSet(index, oldElement, re);
    if (re != null)
      throw re;

    return oldElement;
  }

  @Override
  public int size() {
    return (toIndex != -1 ? toIndex : source.size()) - fromIndex;
  }

  @Override
  public ObservableList<E> subList(final int fromIndex, final int toIndex) {
    return new ObservableList<E>(source, fromIndex, toIndex) {
      @Override
      protected void beforeGet(final int index, final ListIterator<E> iterator) {
        ObservableList.this.beforeGet(index, iterator);
      }

      @Override
      protected void afterGet(final int index, final E e, final ListIterator<E> iterator, final RuntimeException re) {
        ObservableList.this.afterGet(index, e, iterator, re);
      }

      @Override
      protected boolean beforeAdd(final int index, final E element) {
        return ObservableList.this.beforeAdd(index, element);
      }

      @Override
      protected void afterAdd(final int index, final E element, final RuntimeException re) {
        ObservableList.this.afterAdd(index, element, re);
      }

      @Override
      protected boolean beforeRemove(final int index) {
        return ObservableList.this.beforeRemove(index);
      }

      @Override
      protected void afterRemove(final Object element, final RuntimeException re) {
        ObservableList.this.afterRemove(element, re);
      }

      @Override
      protected boolean beforeSet(final int index, final E newElement) {
        return ObservableList.this.beforeSet(index, newElement);
      }

      @Override
      protected void afterSet(final int index, final E oldElement, final RuntimeException re) {
        ObservableList.this.afterSet(index, oldElement, re);
      }
    };
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int,ListIterator)} and
   * {@link #afterGet(int,Object,ListIterator,RuntimeException)} are called
   * immediately before and after each member of the enclosed list is
   * referenced.
   */
  @Override
  public Object[] toArray() {
    return toArray(new Object[size()]);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int,ListIterator)} and
   * {@link #afterGet(int,Object,ListIterator,RuntimeException)} are called
   * immediately before and after each member of the enclosed list is
   * referenced.
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T>T[] toArray(final T[] a) {
    final ListIterator<E> iterator = listIterator();
    for (int i = 0; i < a.length && iterator.hasNext(); i++)
      a[i] = (T)iterator.next();

    return a;
  }
}