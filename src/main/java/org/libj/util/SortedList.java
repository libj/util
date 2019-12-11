/* Copyright (c) 2017 LibJ
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

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

/**
 * A {@link List} that guarantees sorted order of its members.
 *
 * @param <E> The type of elements in this list.
 */
public class SortedList<E> extends ObservableList<E> {
  @SuppressWarnings("rawtypes")
  private static final Comparator DEFAULT_COMPARATOR = Comparator.nullsFirst(Comparator.naturalOrder());

  private final Comparator<E> comparator;

  /**
   * Creates a new {@link SortedList} with the provided {@link List list} of
   * comparable elements as the underlying target.
   * <p>
   * <b>Note</b>: This constructor sorts the provided {@link List list}.
   *
   * @param <T> The parameter requiring elements of type {@link Comparable
   *          Comparable&lt;? super E&gt;}.
   * @param list The {@link List} of comparable elements.
   * @throws NullPointerException If the provided {@link List list} is null.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public <T extends Comparable<? super E>>SortedList(final List<T> list) {
    this((List)list, DEFAULT_COMPARATOR, true);
  }

  /**
   * Creates a new {@link SortedList} with the provided {@link List list} and
   * {@link Comparator comparator} as the underlying target.
   * <p>
   * <b>Note</b>: This constructor sorts the provided {@link List list}.
   *
   * @param list The {@link List}.
   * @param comparator The {@link Comparator}.
   * @throws NullPointerException If the provided {@link List list} or
   *           {@link Comparator comparator} is null.
   */
  public SortedList(final List<E> list, final Comparator<E> comparator) {
    this(list, Objects.requireNonNull(comparator), true);
  }

  private SortedList(final List<E> list, final Comparator<E> comparator, final boolean sort) {
    super(list);
    this.comparator = comparator;
    if (sort)
      list.sort(comparator);
  }

  private Class<?> comparatorType;

  private Class<?> comparatorType() {
    return comparatorType == null ? comparatorType = (Class<?>)((ParameterizedType)comparator.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0] : comparatorType;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private int indexOf(final Object o, final int fromIndex, final int toIndex) {
    if (comparator != DEFAULT_COMPARATOR && comparatorType().isInstance(o))
      return CollectionUtil.binarySearch(target, fromIndex, toIndex, o, (Comparator)comparator);

    if (o instanceof Comparable)
      return CollectionUtil.binarySearch(target, fromIndex, toIndex, o, DEFAULT_COMPARATOR);

    return -1;
  }

  @SuppressWarnings("unchecked")
  private int getIndex(final int index, final E element, final boolean forSet) {
    final boolean isIndexOk;
    if (index == 0)
      isIndexOk = size() == 0 || comparator.compare(element, get(forSet ? 1 : 0)) <= 0;
    else if (index == size())
      isIndexOk = comparator.compare(get(index - 1), element) <= 0;
    else
      isIndexOk = comparator.compare(get(index - 1), element) <= 0 && comparator.compare(element, get(forSet ? index + 1 : index)) <= 0;

    return isIndexOk ? index : CollectionUtil.binaryClosestSearch(target, element, comparator);
  }

  @Override
  protected boolean beforeSet(final int index, final E newElement) {
    final int properIndex = getIndex(index, newElement, true);
    if (index == properIndex)
      return true;

    target.remove(index);
    target.add(index < properIndex ? properIndex - 1 : properIndex, newElement);
    return false;
  }

  @Override
  protected boolean beforeAdd(final int index, final E element) {
    final int properIndex = getIndex(index, element, false);
    if (index == properIndex)
      return true;

    target.add(properIndex, element);
    return false;
  }

  // FIXME: Implement an efficient retainAll() method that takes
  // FIXME: advantage of the sorted order of elements.

  /**
   * {@inheritDoc}
   * <p>
   * <b>Runtime performance</b>: {@code O(log2(n) * m)} if the provided
   * collection is a {@link SortedList}; otherwise {@code O(n * m)}.
   */
  @Override
  @SuppressWarnings("unlikely-arg-type")
  public boolean containsAll(final Collection<?> c) {
    if (c.size() == 0)
      return true;

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

  /**
   * {@inheritDoc}
   * <p>
   * <b>Runtime performance</b>: {@code O(log2(n))} if the provided object
   * implements {@link Comparable}; otherwise {@code O(n)}.
   */
  @Override
  public int indexOf(final Object o) {
    int index = indexOf(o, 0, size());
    if (index <= 0)
      return index;

    while (target.get(--index).equals(o));
    return index + 1;
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>Runtime performance</b>: {@code O(log2(n))} if the provided object
   * implements {@link Comparable}; otherwise {@code O(n)}.
   */
  @Override
  public int lastIndexOf(final Object o) {
    int index = indexOf(o, 0, size());
    if (index < 0 || index == size() - 1)
      return index;

    while (target.get(++index).equals(o));
    return index - 1;
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>Note</b>: The {@link ListIterator} returned by this method does not
   * support {@link ListIterator#add(Object)} or
   * {@link ListIterator#set(Object)}.
   */
  @Override
  public ListIterator<E> listIterator(final int index) {
    Assertions.assertRangeList(index, size(), false);
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
    Assertions.assertRangeList(fromIndex, toIndex, size());
    return new SortedList<E>(target.subList(fromIndex, toIndex), comparator, false);
  }
}