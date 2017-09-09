/* Copyright (c) 2016 lib4j
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
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

/**
 * Wrapper class for the <code>List</code> interface that provides callback
 * methods to observe the addition and removal of elements to the wrapped
 * <code>List</code>.
 *
 * @see ObservableList#beforeAdd(Object)
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
   * Callback method that is invoked immediately before an element is added to
   * the enclosed <code>List</code>.
   *
   * @param index The index of the element being added to the enclosed
   * <code>List</code>.
   * @param e The element being added to the enclosed <code>List</code>.
   */
  protected void beforeAdd(final int index, final E e) {
  }

  /**
   * Callback method that is invoked immediately before an element is removed
   * from the enclosed <code>List</code>.
   *
   * @param index The index of the element being removed from the enclosed
   * <code>List</code>.
   */
  protected void beforeRemove(final int index) {
  }

  /**
   * Callback method that is invoked immediately before an element is set at
   * an index to the enclosed <code>List</code>.
   *
   * @param index The index of the element being set in the enclosed <code>List</code>.
   * @param newElement The element being set to the enclosed <code>List</code>.
   */
  protected void beforeSet(final int index, final E newElement) {
  }

  /**
   * Callback method that is invoked immediately after an element is added to
   * the enclosed <code>List</code>.
   *
   * @param index The index of the element added to the enclosed
   * <code>List</code>.
   * @param e The element being added to the enclosed <code>List</code>.
   */
  protected void afterAdd(final int index, final E e) {
  }

  /**
   * Callback method that is invoked immediately after an element is removed
   * from the enclosed <code>Collection</code>.
   *
   * @param e The element removed from the enclosed <code>Collection</code>.
   */
  protected void afterRemove(final Object e) {
  }

  /**
   * Callback method that is invoked immediately after an element is set at
   * an index to the enclosed <code>List</code>.
   *
   * @param index The index of the element set in the enclosed <code>List</code>.
   * @param oldElement The old element at the index of the enclosed <code>List</code>.
   */
  protected void afterSet(final int index, final E oldElement) {
  }

  /**
   * Ensures that this collection contains the specified element (optional
   * operation). The callback methods <code>beforeAdd()</code> and
   * <code>afterAdd()</code> are called immediately before and after the
   * enclosed collection is modified.
   *
   * @see Collection#add(Object)
   */
  @Override
  public boolean add(final E e) {
    add(size(), e);
    return true;
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
   * @see Collection#add(Object)
   */
  @Override
  public boolean remove(final Object o) {
    final int index = indexOf(o);
    beforeRemove(index - fromIndex);
    final boolean result = source.remove(o);
    if (toIndex != -1)
      --toIndex;

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
    final ListIterator<E> iterator = listIterator();
    while (iterator.hasNext()) {
      final E element = iterator.next();
      if (filter.test(element)) {
        beforeRemove(iterator.nextIndex() - 1 - fromIndex);
        iterator.remove();
        if (toIndex != -1)
          --toIndex;

        afterRemove(element);
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
   * Removes all of the elements from this collection (optional operation).
   * The callback methods <code>beforeRemove()</code> and
   * <code>afterRemove()</code> are called immediately before and after the
   * enclosed collection is modified for the removal of each element.
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
   * Inserts the specified element at the specified position in this list
   * (optional operation).
   */
  @Override
  public void add(final int index, final E element) {
    beforeAdd(index, element);
    source.add(index + fromIndex, element);
    if (toIndex != -1)
      ++toIndex;

    afterAdd(index, element);
  }

  /**
   * Removes the element at the specified position in this list (optional
   * operation).
   */
  @Override
  @SuppressWarnings("unchecked")
  public E remove(final int index) {
    final E element = (E)source.get(index + fromIndex);
    beforeRemove(index);
    source.remove(index + fromIndex);
    if (toIndex != -1)
      --toIndex;

    afterRemove(element);
    return element;
  }

  /**
   * Replaces the element at the specified position in this list with the
   * specified element (optional operation).
   */
  @Override
  @SuppressWarnings("unchecked")
  public E set(final int index, final E element) {
    beforeSet(index, element);
    final E oldElement = (E)source.set(index + fromIndex, element);
    afterSet(index, oldElement);
    return oldElement;
  }

  /**
   * Inserts all of the elements in the specified collection into this
   * list at the specified position (optional operation).
   */
  @Override
  public boolean addAll(int index, final Collection<? extends E> c) {
    if (c.size() == 0)
      return false;

    for (final E e : c)
      add(index++, e);

    return true;
  }

  @Override
  public int size() {
    return (toIndex != -1 ? toIndex : source.size()) - fromIndex;
  }

  /**
   * Returns an iterator over the elements in this collection. Calling
   * <code>Iterator.remove()</code> will delegate a callback to
   * <code>beforeRemove()</code> and <code>afterRemove()</code> on the instance
   * of this <code>ObservableCollection</code>.
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
   */
  @Override
  public ListIterator<E> listIterator() {
    return listIterator(0);
  }

  /**
   * Returns a list iterator over the elements in this list (in proper
   * sequence), starting at the specified position in the list.
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
        return current = listIterator.next();
      }

      @Override
      public boolean hasPrevious() {
        return previousIndex() >= 0;
      }

      @Override
      public E previous() {
        return current = listIterator.previous();
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
        beforeRemove(nextIndex() - 1);
        listIterator.remove();
        if (toIndex != -1)
          --toIndex;

        afterRemove(current);
      }

      @Override
      public void set(final E e) {
        final int index = nextIndex() - 1;
        beforeSet(index, e);
        final E remove = current;
        listIterator.set(e);
        afterSet(index, remove);
      }

      @Override
      public void add(final E e) {
        final int index = nextIndex();
        beforeAdd(index, e);
        listIterator.add(e);
        if (toIndex != -1)
          ++toIndex;

        afterAdd(index, e);
      }
    };
  }

  /**
   * Returns a view of the portion of this list between the specified
   * <tt>fromIndex</tt>, inclusive, and <tt>toIndex</tt>, exclusive.
   */
  @Override
  public ObservableList<E> subList(final int fromIndex, final int toIndex) {
    return new ObservableList<E>(source, fromIndex, toIndex) {
      @Override
      protected void beforeAdd(final int index, final E element) {
        ObservableList.this.beforeAdd(index, element);
      }

      @Override
      protected void beforeRemove(final int index) {
        ObservableList.this.beforeRemove(index);
      }

      @Override
      protected void beforeSet(final int index, final E newElement) {
        ObservableList.this.beforeSet(index, newElement);
      }

      @Override
      protected void afterAdd(final int index, final E element) {
        ObservableList.this.afterAdd(index, element);
      }

      @Override
      protected void afterRemove(final Object element) {
        ObservableList.this.afterRemove(element);
      }

      @Override
      protected void afterSet(final int index, final E oldElement) {
        ObservableList.this.afterSet(index, oldElement);
      }
    };
  }
}