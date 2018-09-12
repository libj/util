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
 * Wrapper class for the <code>List</code> interface that provides callback
 * methods to observe the addition and removal of elements to the wrapped
 * <code>List</code>.
 *
 * @see ObservableList#beforeGet(int,Iterator<E>)
 * @see ObservableList#afterGet(int,E,Iterator<E>)
 * @see ObservableList#beforeAdd(int,Object)
 * @see ObservableList#afterAdd(int)
 * @see ObservableList#beforeRemove(int)
 * @see ObservableList#afterRemove(Object)
 * @see ObservableList#beforeSet(int,E)
 * @see ObservableList#afterSet(int,E)
 * @see List
 */
public class ObservableList<E> extends WrappedList<E> {
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

  /**
   * Callback method that is invoked immediately before an element is retrieved
   * from the enclosed <code>List</code>.
   *
   * @param index The index of the element being retrieved from the enclosed
   * <code>List</code>.
   * @param iterator The <code>ListIterator</code> instance if the get is a
   *                 result of an iterator reference, or null if otherwise.
   * <code>List</code>.
   */
  protected void beforeGet(final int index, final ListIterator<E> iterator) {
  }

  /**
   * Callback method that is invoked immediately after an element is retrieved
   * from the enclosed <code>List</code>.
   *
   * @param index The index of the element being retrieved from the enclosed
   * <code>List</code>.
   * @param e The element being retrieved from the enclosed <code>List</code>.
   * @param iterator The <code>Iterator</code> instance if the get is a result
   *                 of an iterator reference, or null if otherwise.
   */
  @SuppressWarnings("unused")
  protected void afterGet(final int index, final E e, final ListIterator<E> iterator, final RuntimeException re) {
  }

  /**
   * Callback method that is invoked immediately before an element is added to
   * the enclosed <code>List</code>.
   *
   * @param index The index of the element being added to the enclosed
   * <code>List</code>.
   * @param e The element being added to the enclosed <code>List</code>.
   * @return Whether the element should be added to the collection.
   */
  protected boolean beforeAdd(final int index, final E e) {
    return true;
  }

  /**
   * Callback method that is invoked immediately after an element is added to
   * the enclosed <code>List</code>.
   *
   * @param index The index of the element added to the enclosed
   * <code>List</code>.
   * @param e The element being added to the enclosed <code>List</code>.
   */
  @SuppressWarnings("unused")
  protected void afterAdd(final int index, final E e, final RuntimeException re) {
  }

  /**
   * Callback method that is invoked immediately before an element is removed
   * from the enclosed <code>List</code>.
   *
   * @param index The index of the element being removed from the enclosed
   * <code>List</code>.
   * @return Whether the element should be removed from the collection.
   */
  protected boolean beforeRemove(final int index) {
    return true;
  }

  /**
   * Callback method that is invoked immediately after an element is removed
   * from the enclosed <code>Collection</code>.
   *
   * @param e The element removed from the enclosed <code>Collection</code>.
   */
  @SuppressWarnings("unused")
  protected void afterRemove(final Object e, final RuntimeException re) {
  }

  /**
   * Callback method that is invoked immediately before an element is set at
   * an index to the enclosed <code>List</code>.
   *
   * @param index The index of the element being set in the enclosed <code>List</code>.
   * @param newElement The element being set to the enclosed <code>List</code>.
   * @return Whether the element should be set in the collection.
   */
  protected boolean beforeSet(final int index, final E newElement) {
    return true;
  }

  /**
   * Callback method that is invoked immediately after an element is set at
   * an index to the enclosed <code>List</code>.
   *
   * @param index The index of the element set in the enclosed <code>List</code>.
   * @param oldElement The old element at the index of the enclosed <code>List</code>.
   */
  @SuppressWarnings("unused")
  protected void afterSet(final int index, final E oldElement, final RuntimeException re) {
  }

  /**
   * Ensures that this collection contains the specified element (optional
   * operation). The callback methods <code>beforeAdd()</code> and
   * <code>afterAdd()</code> are called immediately before and after the
   * enclosed collection is modified. If <code>beforeAdd()</code> returns
   * false, the element will not be added.
   *
   * @see Collection#add(Object)
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
   * Inserts the specified element at the specified position in this list
   * (optional operation). The callback methods <code>beforeAdd()</code> and
   * <code>afterAdd()</code> are called immediately before and after the
   * enclosed collection is modified for the addition of each element
   * in the argument Collection. If <code>beforeAdd()</code> returns false,
   * the element will not be added.
   *
   * @see List#add(int,Object)
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
   * Adds all of the elements in the specified collection to this collection
   * (optional operation). The callback methods <code>beforeAdd()</code> and
   * <code>afterAdd()</code> are called immediately before and after the
   * enclosed collection is modified for the addition of each element
   * in the argument Collection. All elements for which <code>beforeAdd()</code>
   * returns false will not be added to this collection.
   *
   * @see Collection#addAll(Collection)
   */
  @Override
  public boolean addAll(final Collection<? extends E> c) {
    boolean changed = false;
    for (final E element : c)
      changed = add(element) || changed;

    return changed;
  }

  /**
   * Inserts all of the elements in the specified collection into this
   * list at the specified position (optional operation).
   *
   * @see List#addAll(int,Collection)
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
   * Removes all of the elements from this collection (optional operation).
   * The callback methods <code>beforeRemove()</code> and
   * <code>afterRemove()</code> are called immediately before and after the
   * enclosed collection is modified for the removal of each element.
   * All elements for which <code>beforeRemove()</code> returns false will not
   * be removed from this collection.
   *
   * @see Collection#clear()
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
   * Returns {@code true} if this list contains the specified element.
   * The callback methods <code>beforeGet()</code> and
   * <code>afterGet()</code> are called immediately before and after each
   * member of the enclosed list is referenced.
   *
   * @see List#contains(Object)
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
   * Returns {@code true} if this list contains all of the elements of the
   * specified collection. The callback methods <code>beforeGet()</code> and
   * <code>afterGet()</code> are called immediately before and after each
   * member of the enclosed list is referenced.
   *
   * @see List#containsAll(Object)
   */
  @Override
  public boolean containsAll(final Collection<?> c) {
    for (final Object o : c)
      if (!contains(o))
        return false;

    return true;
  }

  /**
   * Returns the element at the specified position in this list. The callback
   * methods <code>beforeGet()</code> and <code>afterGet()</code> are called
   * immediately before and after the get operation on the enclosed collection.
   *
   * @param index index of the element to return
   * @return the element at the specified position in this list
   * @throws IndexOutOfBoundsException if the index is out of range
   *         ({@code index < 0 || index >= size()})
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
   * Returns the index of the first occurrence of the specified element
   * in this list, or -1 if this list does not contain the element. The
   * callback methods <code>beforeGet()</code> and
   * <code>afterGet()</code> are called immediately before and after each
   * member of the enclosed list is referenced.
   *
   * @see List#indexOf(Object)
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
   * Returns the index of the last occurrence of the specified element
   * in this list, or -1 if this list does not contain the element. The
   * callback methods <code>beforeGet()</code> and
   * <code>afterGet()</code> are called immediately before and after each
   * member of the enclosed list is referenced.
   *
   * @see List#lastIndexOf(Object)
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
   * Returns an iterator over the elements in this collection. Calling
   * <code>Iterator.remove()</code> will delegate a callback to
   * <code>beforeRemove()</code> and <code>afterRemove()</code> on the instance
   * of this <code>ObservableCollection</code>. All elements for which
   * <code>beforeRemove()</code> returns false will not be removed from this
   * collection.
   *
   * @see Collection#iterator()
   * @return an <tt>Iterator</tt> over the elements in this collection
   */
  @Override
  public Iterator<E> iterator() {
    return listIterator();
  }

  /**
   * Returns a list iterator over the elements in this list (in proper
   * sequence).
   *
   * @see List#listIterator()
   */
  @Override
  public ListIterator<E> listIterator() {
    return listIterator(0);
  }

  /**
   * Returns a list iterator over the elements in this list (in proper
   * sequence), starting at the specified position in the list.
   *
   * @see List#listIterator(int)
   */
  @Override
  public ListIterator<E> listIterator(final int index) {
    if (index > size())
      throw new IndexOutOfBoundsException("Invalid index " + index + ", size is " + size());

    final ListIterator<E> listIterator = source.listIterator(index + fromIndex);
    return new ListIterator<>() {
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
   * Removes the element at the specified position in this list (optional
   * operation).
   *
   * @see List#remove(int)
   * @return the element previously at the specified position
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
   * Removes a single instance of the specified element from this collection,
   * if it is present (optional operation). The callback methods
   * <code>beforeRemove()</code> and <code>afterRemove()</code> are called
   * immediately before and after the enclosed collection is
   * modified. If <code>beforeRemove()</code> returns false, the element will
   * not be removed.
   *
   * @see Collection#remove(Object)
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
   * Removes all of this collection's elements that are also contained in the
   * specified collection (optional operation). The callback methods
   * <code>beforeRemove()</code> and <code>afterRemove()</code> are called
   * immediately before and after the enclosed collection is
   * modified for the removal of each element in the argument Collection.
   * All elements for which <code>beforeRemove()</code> returns false will not
   * be removed from this collection.
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
   * Removes all of the elements of this collection that satisfy the given
   * predicate. The callback methods <code>beforeRemove()</code> and
   * <code>afterRemove()</code> are called immediately before and after the
   * enclosed collection is modified for the removal of each element. All
   * elements for which <code>beforeRemove()</code> returns false will not be
   * removed from this collection.
   *
   * @see Collection#removeIf(Predicate)
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
   * Retains only the elements in this collection that are contained in the
   * specified collection (optional operation). The callback methods
   * <code>beforeRemove()</code> and <code>afterRemove()</code> are called
   * immediately before and after the enclosed collection is
   * modified for the removal of each element not in the argument Collection.
   * All elements for which <code>beforeRemove()</code> returns false will not
   * be removed from this collection.
   *
   * @see Collection#retainAll(Collection)
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
   * Replaces the element at the specified position in this list with the
   * specified element (optional operation). The callback methods
   * <code>beforeSet()</code> and <code>afterSet()</code> are called
   * immediately before and after the enclosed collection is
   * modified. All elements for which <code>beforeSet()</code> returns false
   * will be skipped.
   *
   * @see List#set(int,Object)
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

  /**
   * Returns a view of the portion of this list between the specified
   * <tt>fromIndex</tt>, inclusive, and <tt>toIndex</tt>, exclusive.
   *
   * @see List#subList(int,int)
   */
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
   * Returns an array containing all of the elements in this list in proper
   * sequence (from first to last element). The callback methods
   * <code>beforeGet()</code> and <code>afterGet()</code> are called
   * immediately before and after each member of the enclosed list is
   * referenced.
   *
   * @see List#toArray()
   */
  @Override
  public Object[] toArray() {
    return toArray(new Object[size()]);
  }

  /**
   * Returns an array containing all of the elements in this list in
   * proper sequence (from first to last element); the runtime type of
   * the returned array is that of the specified array. The callback methods
   * <code>beforeGet()</code> and <code>afterGet()</code> are called
   * immediately before and after each member of the enclosed list is
   * referenced.
   *
   * @see List#toArray(Object[])
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