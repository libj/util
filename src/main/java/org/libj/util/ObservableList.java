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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

/**
 * A {@link DelegateList} that provides callback methods to observe the
 * addition, removal, and retrieval of elements, either due to direct method
 * invocation on the list instance itself, or via {@link #iterator()},
 * {@link #listIterator()}, {@link #subList(int,int)}, and any other entrypoint
 * that facilitates access to the elements in this list, either for modification
 * or retrieval.
 *
 * @param <E> The type of elements in this list.
 * @see #beforeGet(int,ListIterator)
 * @see #afterGet(int,Object,ListIterator,RuntimeException)
 * @see #beforeAdd(int,Object)
 * @see #afterAdd(int,Object,RuntimeException)
 * @see #beforeRemove(int)
 * @see #afterRemove(Object,RuntimeException)
 * @see #beforeSet(int,Object)
 * @see #afterSet(int,Object,RuntimeException)
 */
public abstract class ObservableList<E> extends DelegateList<E> {
  private final int fromIndex;
  private int toIndex;

  /**
   * Creates a new {@link ObservableList} with the specified target
   * {@link List}.
   *
   * @param list The target {@link List}.
   * @throws NullPointerException If {@code list} is null.
   */
  public ObservableList(final List<E> list) {
    this(list, 0, -1);
  }

  /**
   * Creates a new {@link ObservableList} with the specified target list, and
   * from and to indexes to limit the scope of the target list.
   *
   * @param list The target {@link List} object.
   * @param fromIndex The starting index as the lower limit of the elements in
   *          the target list, inclusive.
   * @param toIndex The starting index as the upper limit of the elements in the
   *          target list, exclusive.
   * @throws NullPointerException If {@code list} is null.
   */
  protected ObservableList(final List<E> list, final int fromIndex, final int toIndex) {
    super(list);
    this.fromIndex = fromIndex;
    this.toIndex = toIndex;
  }

  /**
   * Callback method that is invoked immediately before an element is retrieved
   * from the enclosed {@link List}.
   *
   * @param index The index of the element to be retrieved from the enclosed
   *          {@link List}.
   * @param iterator The {@link ListIterator} instance if the get is a result of
   *          an iterator reference, otherwise {@code null}.
   */
  protected void beforeGet(final int index, final ListIterator<E> iterator) {
  }

  /**
   * Callback method that is invoked immediately after an element is retrieved
   * from the enclosed {@link List}.
   *
   * @param index The index of the element retrieved from the enclosed
   *          {@link List}.
   * @param e The element retrieved from the enclosed {@link List}.
   * @param iterator The {@link Iterator} instance if the get is a result of an
   *          iterator reference, otherwise {@code null}.
   * @param re A {@link RuntimeException} that occurred during the get
   *          operation, or {@code null} if no exception occurred.
   */
  protected void afterGet(final int index, final E e, final ListIterator<E> iterator, final RuntimeException re) {
  }

  /**
   * Callback method that is invoked immediately before an element is added to
   * the enclosed {@link List}.
   *
   * @param index The index for the element to be added to the enclosed
   *          {@link List}.
   * @param e The element to be added to the enclosed {@link List}.
   * @return If this method returns {@code true}, the subsequent <u>add</u>
   *         operation will be performed; if this method returns {@code false},
   *         the subsequent <u>add</u> operation will not be performed.
   */
  protected boolean beforeAdd(final int index, final E e) {
    return true;
  }

  /**
   * Callback method that is invoked immediately after an element is added to
   * the enclosed {@link List}.
   *
   * @param index The index of the element added to the enclosed {@link List}.
   * @param e The element to be added to the enclosed {@link List}.
   * @param re A {@link RuntimeException} that occurred during the add
   *          operation, or {@code null} if no exception occurred.
   */
  protected void afterAdd(final int index, final E e, final RuntimeException re) {
  }

  /**
   * Callback method that is invoked immediately before an element is removed
   * from the enclosed {@link List}.
   *
   * @param index The index of the element to be removed from the enclosed
   *          {@link List}.
   * @return If this method returns {@code true}, the subsequent <u>remove</u>
   *         operation will be performed; if this method returns {@code false},
   *         the subsequent <u>remove</u> operation will not be performed.
   */
  protected boolean beforeRemove(final int index) {
    return true;
  }

  /**
   * Callback method that is invoked immediately after an element is removed
   * from the enclosed {@link List}.
   *
   * @param e The element removed from the enclosed {@link List}, or attempted
   *          to be removed from the {@link List} in case of a
   *          {@link RuntimeException}.
   * @param re A {@link RuntimeException} that occurred during the remove
   *          operation, or {@code null} if no exception occurred.
   */
  protected void afterRemove(final Object e, final RuntimeException re) {
  }

  /**
   * Callback method that is invoked immediately before an element is set at an
   * index in the enclosed {@link List}.
   *
   * @param index The index for the element to be set in the enclosed
   *          {@link List}.
   * @param newElement The element to be set in the enclosed {@link List}.
   * @return If this method returns {@code true}, the subsequent <u>set</u>
   *         operation will be performed; if this method returns {@code false},
   *         the subsequent <u>set</u> operation will not be performed.
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
   * @param re A {@link RuntimeException} that occurred during the set
   *          operation, or {@code null} if no exception occurred.
   */
  protected void afterSet(final int index, final E oldElement, final RuntimeException re) {
  }

  private void add0(final int index, final E element) {
    if (!beforeAdd(index, element))
      return;

    RuntimeException re = null;
    try {
      target.add(index + fromIndex, element);
      if (toIndex != -1)
        ++toIndex;
    }
    catch (final RuntimeException t) {
      re = t;
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
   * before and after the enclosed {@link List} is modified for the addition of
   * the element. If {@link #beforeAdd(int,Object)} returns {@code false}, the
   * element will not be added.
   */
  @Override
  public boolean add(final E e) {
    final int size = size();
    add0(size, e);
    return size != size();
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeAdd(int,Object)} and
   * {@link #afterAdd(int,Object,RuntimeException)} are called immediately
   * before and after the enclosed {@link List} is modified for the addition of
   * the element. If {@link #beforeAdd(int,Object)} returns {@code false}, the
   * element will not be added.
   */
  @Override
  public void add(final int index, final E element) {
    Assertions.assertRangeList(index, size(), true);
    add0(index, element);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeAdd(int,Object)} and
   * {@link #afterAdd(int,Object,RuntimeException)} are called immediately
   * before and after the enclosed {@link List} is modified for the addition of
   * each element in the specified {@link Collection}. All elements for which
   * {@link #beforeAdd(int,Object)} returns {@code false} will not be added to
   * this {@link List}.
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
   * The callback methods {@link #beforeAdd(int,Object)} and
   * {@link #afterAdd(int,Object,RuntimeException)} are called immediately
   * before and after the enclosed {@link List} is modified for the addition of
   * each element in the specified {@link Collection}. All elements for which
   * {@link #beforeAdd(int,Object)} returns {@code false} will not be added to
   * this {@link List}.
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
   * and after the enclosed {@link List} is modified for the removal of each
   * element. All elements for which {@link #beforeRemove(int)} returns
   * {@code false} will not be removed from this {@link List}.
   */
  @Override
  public void clear() {
    for (int i = size() - 1; i >= 0; --i)
      remove(i);
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
    final int size = size();
    if (o == null) {
      for (int i = 0; i < size; ++i)
        if (get(i) == null)
          return true;
    }
    else {
      for (int i = 0; i < size; ++i)
        if (o.equals(get(i)))
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
  @SuppressWarnings("unlikely-arg-type")
  public boolean containsAll(final Collection<?> c) {
    if (c.size() == 0)
      return true;

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
   * immediately before and after the get operation on the enclosed {@link List}.
   */
  @Override
  @SuppressWarnings("unchecked")
  public E get(final int index) {
    Assertions.assertRangeList(index, size(), false);
    beforeGet(index, null);
    E object = null;
    RuntimeException re = null;
    try {
      object = (E)target.get(index + fromIndex);
    }
    catch (final RuntimeException t) {
      re = t;
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
    final int size = size();
    if (o == null) {
      for (int i = 0; i < size; ++i)
        if (get(i) == null)
          return i;
    }
    else {
      for (int i = 0; i < size; ++i)
        if (o.equals(get(i)))
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
    if (o == null) {
      for (int i = size() - 1; i >= 0; --i)
        if (get(i) == null)
          return i;
    }
    else {
      for (int i = size() - 1; i >= 0; --i)
        if (o.equals(get(i)))
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
   * elements for which {@link #beforeRemove(int)} returns {@code false} will
   * not be removed from this {@link List}.
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
   * elements for which {@link #beforeRemove(int)} returns {@code false} will
   * not be removed from this {@link List}. Calling
   * {@link ListIterator#set(Object)} will delegate a callback to
   * {@link #beforeSet(int,Object)} and
   * {@link #afterSet(int,Object,RuntimeException)} on this instance. All
   * elements for which {@link #beforeSet(int,Object)} returns {@code false}
   * will not be set in this {@link List}. Calling
   * {@link ListIterator#add(Object)} will delegate a callback to
   * {@link #beforeAdd(int,Object)} and
   * {@link #afterAdd(int,Object,RuntimeException)} on this instance. All
   * elements for which {@link #beforeAdd(int,Object)} returns {@code false}
   * will not be added to this {@link List}.
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
   * elements for which {@link #beforeRemove(int)} returns {@code false} will
   * not be removed from this {@link List}. Calling
   * {@link ListIterator#set(Object)} will delegate a callback to
   * {@link #beforeSet(int,Object)} and
   * {@link #afterSet(int,Object,RuntimeException)} on this instance. All
   * elements for which {@link #beforeSet(int,Object)} returns {@code false}
   * will not be set in this {@link List}. Calling
   * {@link ListIterator#add(Object)} will delegate a callback to
   * {@link #beforeAdd(int,Object)} and
   * {@link #afterAdd(int,Object,RuntimeException)} on this instance. All
   * elements for which {@link #beforeAdd(int,Object)} returns {@code false}
   * will not be added to this {@link List}.
   */
  @Override
  public ListIterator<E> listIterator(final int index) {
    Assertions.assertRangeList(index, size(), true);
    final ListIterator<E> listIterator = target.listIterator(index + fromIndex);
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
        catch (final RuntimeException t) {
          re = t;
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
        catch (final RuntimeException t) {
          re = t;
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
        final int index = nextIndex() - 1;
        if (!beforeRemove(index))
          return;

        final E element = this.current;
        RuntimeException re = null;
        try {
          listIterator.remove();
          if (toIndex != -1)
            --toIndex;
        }
        catch (final RuntimeException t) {
          re = t;
        }

        afterRemove(element, re);
        if (re != null)
          throw re;
      }

      @Override
      public void set(final E e) {
        final int index = nextIndex() - 1;
        if (!beforeSet(index, e))
          return;

        final E element = this.current;
        RuntimeException re = null;
        try {
          listIterator.set(e);
        }
        catch (final RuntimeException t) {
          re = t;
        }

        afterSet(index, element, re);
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

  @SuppressWarnings("unchecked")
  private E remove0(final int index) {
    if (!beforeRemove(index))
      return null;

    final E element = (E)target.get(index + fromIndex);
    RuntimeException re = null;
    try {
      target.remove(index + fromIndex);
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
   * and after the enclosed {@link List} is modified for the removal of the
   * element. If {@link #beforeRemove(int)} returns {@code false}, the element
   * will not be removed, and this method will return {@code null}.
   */
  @Override
  public E remove(final int index) {
    Assertions.assertRangeList(index, size(), false);
    return remove0(index);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(int)} and
   * {@link #afterRemove(Object,RuntimeException)} are called immediately before
   * and after the enclosed {@link List} is modified. If
   * {@link #beforeRemove(int)} returns {@code false}, the element will not be
   * removed.
   */
  @Override
  @SuppressWarnings("unlikely-arg-type")
  public boolean remove(final Object o) {
    final int index = indexOf(o);
    if (index == -1)
      return false;

    final int size = size();
    remove0(index);
    return size != size();
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(int)} and
   * {@link #afterRemove(Object,RuntimeException)} are called immediately before
   * and after the enclosed {@link List} is modified for the removal of each
   * element in the specified {@link Collection}. All elements for which
   * {@link #beforeRemove(int)} returns {@code false} will not be removed from
   * this {@link List}.
   *
   * @see Collection#removeAll(Collection)
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
   * The callback methods {@link #beforeRemove(int)} and
   * {@link #afterRemove(Object,RuntimeException)} are called immediately before
   * and after the enclosed {@link List} is modified for the removal of each
   * element. All elements for which {@link #beforeRemove(int)} returns
   * {@code false} will not be removed from this {@link List}.
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean removeIf(final Predicate<? super E> filter) {
    final int size = size();
    for (int i = size - 1; i >= 0; --i) {
      final E element = (E)target.get(i + fromIndex);
      if (filter.test(element) && beforeRemove(i)) {
        RuntimeException re = null;
        try {
          target.remove(i++ + fromIndex);
          if (toIndex != -1)
            --toIndex;
        }
        catch (final RuntimeException t) {
          re = t;
        }

        afterRemove(element, re);
        if (re != null)
          throw re;
      }
    }

    return size != size();
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(int)} and
   * {@link #afterRemove(Object,RuntimeException)} are called immediately before
   * and after the enclosed {@link List} is modified for the removal of each
   * element not in the specified {@link Collection}. All elements for which
   * {@link #beforeRemove(int)} returns {@code false} will not be removed from
   * this {@link List}.
   */
  @Override
  @SuppressWarnings("unlikely-arg-type")
  public boolean retainAll(final Collection<?> c) {
    if (c.size() > 0) {
      final int size = size();
      for (int i = size - 1; i >= 0; --i)
        if (!c.contains(target.get(i + fromIndex)))
          remove(i);

      return size != size();
    }

    if (size() == 0)
      return false;

    clear();
    return true;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeSet(int,Object)} and
   * {@link #afterSet(int,Object,RuntimeException)} are called immediately
   * before and after the enclosed {@link List} is modified. If
   * {@link #beforeSet(int,Object)} returns {@code false}, the element will not
   * be set, and this method will return {@code null}.
   */
  @Override
  @SuppressWarnings("unchecked")
  public E set(final int index, final E element) {
    Assertions.assertRangeList(index, size(), false);
    if (!beforeSet(index, element))
      return null;

    E oldElement = null;
    RuntimeException re = null;
    try {
      oldElement = (E)target.set(index + fromIndex, element);
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
    return (toIndex != -1 ? toIndex : target.size()) - fromIndex;
  }

  @Override
  public ObservableList<E> subList(final int fromIndex, final int toIndex) {
    Assertions.assertRangeList(fromIndex, toIndex, size());
    return new ObservableList<E>(target, fromIndex, toIndex) {
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
  public <T>T[] toArray(T[] a) {
    if (a.length < size())
      a = (T[])Array.newInstance(a.getClass().getComponentType(), size());

    final int size = size();
    for (int i = 0; i < size; ++i)
      a[i] = (T)get(i);

    if (a.length > size())
      a[size()] = null;

    return a;
  }
}