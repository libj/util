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

import org.libj.lang.Assertions;

/**
 * A {@link List} that guarantees sorted order of its elements.
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
   * <b>Note:</b> This constructor sorts the provided {@link List list}.
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
   * <b>Note:</b> This constructor sorts the provided {@link List list}.
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

    if (o == null)
      return CollectionUtil.binarySearch(target, fromIndex, toIndex, o, DEFAULT_COMPARATOR);

    if (o instanceof Comparable) {
      final int i = CollectionUtil.binarySearch(target, fromIndex, toIndex, o, DEFAULT_COMPARATOR);
      if (i < 0)
        return i;

      Object a = target.get(i);
      if (o.equals(a))
        return i;

      for (int sign = -1;; sign = 1) {
        for (int offset = 1, j;; ++offset) {
          j = i + sign * offset;
          if (sign == -1 ? j < fromIndex : toIndex <= j)
            break;

          a = target.get(j);
          if (((Comparable)o).compareTo(a) != 0)
            break;

          if (o.equals(a))
            return j;
        }

        if (sign == 1)
          return -1;
      }
    }

    return -1;
  }

  @SuppressWarnings("unchecked")
  private int getIndex(final int index, final E element, final boolean forSet) {
    final int size = size();
    final boolean isIndexOk;
    if (index == 0) {
      final int testIndex = forSet ? 1 : 0;
      isIndexOk = size <= testIndex || comparator.compare(element, get(testIndex)) <= 0;
    }
    else if (index == (forSet ? size - 1 : size)) {
      isIndexOk = comparator.compare(get(index - 1), element) <= 0;
    }
    else {
      isIndexOk = comparator.compare(get(index - 1), element) <= 0 && comparator.compare(element, get(forSet ? index + 1 : index)) <= 0;
    }

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
  protected Object beforeAdd(final int index, final E element, final Object preventDefault) {
    final int properIndex = getIndex(index, element, false);
    if (index == properIndex)
      return element;

    target.add(properIndex, element);
    return preventDefault;
  }

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

  @Override
  @SuppressWarnings("unlikely-arg-type")
  public boolean retainAll(final Collection<?> c) {
    if (c.size() > 0) {
      final int size = size();
      final int last = size - 1;
      E elem;
      E prev = null;
      boolean removed = false;
      for (int i = last; i >= 0; --i, prev = elem) {
        elem = getFast(i);
        final boolean isSameAsPrev = i != last && (Objects.equals(prev, elem));
        if (!isSameAsPrev) {
          if (removed = !c.contains(elem)) {
            remove(i);
          }
        }
        else if (removed) {
          remove(i);
        }
      }

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
   * <b>Runtime performance</b>: {@code O(log2(n))}.
   */
  @Override
  public int indexOf(final Object o) {
    int index = indexOf(o, 0, size());
    if (index <= 0)
      return index;

    if (!target.get(index).equals(o))
      return -1;

    while (--index > 0 && target.get(index).equals(o));
    return index + 1;
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>Runtime performance</b>: {@code O(log2(n))}.
   */
  @Override
  public int lastIndexOf(final Object o) {
    int index = indexOf(o, 0, size());
    if (index < 0 || index == size() - 1)
      return index;

    if (!target.get(index).equals(o))
      return -1;

    final int len = target.size();
    while (++index < len && target.get(index).equals(o));
    return index - 1;
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>Note:</b> The {@link ListIterator} returned by this method does not
   * support {@link ListIterator#add(Object)} or
   * {@link ListIterator#set(Object)}.
   */
  @Override
  public ListIterator<E> listIterator(final int index) {
    Assertions.assertRange("index", index, "size()", size(), true);
    return new CursorListIterator<E>(target.listIterator(index)) {
      private void shift(int dist) {
        if (dist < 0) {
          while (++dist <= 0) {
            previous();
          }
        }
        else {
          while (--dist > 0) {
            next();
          }
        }
      }

      @Override
      public void set(final E e) {
        assertModifiable();
        final int index = indexOfLast();
        final int properIndex = getIndex(index, e, true);
        if (index == properIndex) {
          super.set(e);
        }
        else {
          remove();
          final int dist = properIndex - index;
          shift(dist);
          try {
            super.add(e);
          }
          finally {
            shift((dist < 0 ? 1 : 0) - dist);
          }
        }
      }

      @Override
      public void add(final E e) {
        assertModifiable();
        final int index = indexForNext();
        final int properIndex = getIndex(index, e, false);
        if (index == properIndex) {
          super.add(e);
        }
        else {
          int dist = properIndex - index;
          if (dist > 0)
            ++dist;

          shift(dist);
          try {
            super.add(e);
          }
          finally {
            shift(1 - dist);
          }
        }
      }
    };
  }

  @Override
  public SortedList<E> subList(final int fromIndex, final int toIndex) {
    Assertions.assertRange("fromIndex", fromIndex, "toIndex", toIndex, "size()", size());
    return new SortedList<E>(target.subList(fromIndex, toIndex), comparator, false);
  }
}