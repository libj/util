/* Copyright (c) 2017 FastJAX
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
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * {@link List} that guarantees sorted order of its members.
 *
 * @param <E> The type of elements in this list.
 */
public class SortedList<E extends Comparable<? super E>> extends ObservableList<E> {
  @SuppressWarnings("rawtypes")
  private static final Comparator comparator = Comparator.nullsFirst(Comparator.naturalOrder());

  /**
   * Creates a new {@code SortedList} with the provided list as the underlying
   * target.
   * <p>
   * <i><b>Note</b>: This constructor sorts the provided list.</i>
   *
   * @param list The {@code List}.
   */
  public SortedList(final List<E> list) {
    this(list, true);
  }

  private SortedList(final List<E> list, final boolean sort) {
    super(list);
    if (sort)
      list.sort(comparator);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected boolean beforeAdd(final int index, final E e) {
    target.add(FastCollections.binaryClosestSearch(target, e), e);
    return false;
  }

  @Override
  protected boolean beforeSet(final int index, final E newElement) {
    throw new UnsupportedOperationException();
  }

  /**
   * This method is not supported.
   *
   * @throws UnsupportedOperationException This method is not supported.
   * @see #add(Object)
   */
  @Override
  public void add(final int index, final E element) {
    throw new UnsupportedOperationException();
  }

  /**
   * This method is not supported.
   *
   * @throws UnsupportedOperationException This method is not supported.
   * @see #addAll(Collection)
   */
  @Override
  public boolean addAll(final int index, final Collection<? extends E> c) {
    throw new UnsupportedOperationException();
  }

  /**
   * This method is not supported.
   *
   * @throws UnsupportedOperationException This method is not supported.
   * @see #remove(int)
   * @see #add(Object)
   */
  @Override
  public E set(final int index, final E element) {
    throw new UnsupportedOperationException();
  }

  // FIXME: Implement an efficient retainAll() method that takes
  // FIXME: advantage of the sorted order of elements.

  /**
   * {@inheritDoc}
   * <p>
   * <b>Runtime performance</b>: {@code O(log2(n) * m)} if the provided
   * collection is a {@code SortedList}; otherwise {@code O(n * m)}.
   */
  @Override
  public boolean containsAll(final Collection<?> c) {
    final Iterator<?> iterator = c.iterator();
    if (c instanceof SortedList) {
      for (int i = 0; iterator.hasNext();)
        if ((i = indexOf(iterator.next(), i, size())) < 0)
          return false;
    }
    else {
      while (iterator.hasNext())
        if (!contains(iterator.next()))
          return false;
    }

    return true;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private int indexOf(final Object o, final int fromIndex, final int toIndex) {
    return o instanceof Comparable ? FastCollections.binarySearch(target, fromIndex, toIndex, (Comparable)o) : target.indexOf(o);
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>Runtime performance</b>: {@code O(log2(n))} if the provided object
   * implements {@code Comparable}; otherwise {@code O(n)}.
   */
  @Override
  public int indexOf(final Object o) {
    return indexOf(o, 0, size());
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>Runtime performance</b>: {@code O(log2(n))} if the provided object
   * implements {@code Comparable}; otherwise {@code O(n)}.
   */
  @Override
  public int lastIndexOf(final Object o) {
    int index = indexOf(o);
    if (index < 0)
      return index;

    while (target.get(++index).equals(o));
    return index;
  }

  /**
   * {@inheritDoc}
   * <p>
   * <i><b>Note</b>: The {@code ListIterator} returned by this method does not
   * support {@link ListIterator#add(Object)} or
   * {@link ListIterator#set(Object)}.</i>
   */
  @Override
  public ListIterator<E> listIterator(final int index) {
    return new DelegateListIterator<E>(super.listIterator(index)) {
      @Override
      public void set(final E e) {
        throw new UnsupportedOperationException();
      }

      @Override
      public void add(final E e) {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Override
  public SortedList<E> subList(final int fromIndex, final int toIndex) {
    return new SortedList<E>(target.subList(fromIndex, toIndex), false);
  }
}