/* Copyright (c) 2023 LibJ
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

import static org.libj.lang.Assertions.*;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.SortedSet;

/**
 * An {@link ArrayList} that guarantees sorted order of its elements.
 *
 * @param <E> The type of elements in this list.
 */
public class SortedSetArrayList<E> extends ArrayList<E> implements SortedSet<E> {
  @SuppressWarnings("rawtypes")
  private static final Comparator DEFAULT_COMPARATOR = Comparator.nullsFirst(Comparator.naturalOrder());

  private final Comparator<E> comparator;

  /**
   * Constructs a list containing the elements of the specified {@link Collection collection}, sorted in the order specified by the
   * provided {@link Comparator comparator}.
   *
   * @param c The {@link Collection collection} whose elements are to be placed into this list.
   * @param comparator The {@link Comparator} used to compare list elements. A {@code null} value indicates that the elements'
   *          {@linkplain Comparable natural ordering} should be used.
   * @throws NullPointerException If the provided {@link List list} or {@link Comparator comparator} is null.
   */
  public SortedSetArrayList(final Collection<? extends E> c, final Comparator<E> comparator) {
    super();
    this.comparator = comparator != null ? comparator : DEFAULT_COMPARATOR;
    addAll(c);
    super.sort(comparator);
  }

  /**
   * Constructs a list containing the elements of the specified {@link Collection collection}, and a {@link Comparator comparator}
   * specifying the {@linkplain Comparable natural ordering} of elements.
   *
   * @param c The {@link Collection collection} whose elements are to be placed into this list.
   * @throws NullPointerException If the provided {@link List list} or {@link Comparator comparator} is null.
   */
  public SortedSetArrayList(final Collection<? extends E> c) {
    super();
    this.comparator = DEFAULT_COMPARATOR;
    addAll(c);
    super.sort(comparator);
  }

  /**
   * Constructs an empty list with the specified initial capacity and a {@link Comparator comparator} specifying the
   * {@linkplain Comparable natural ordering} of elements.
   *
   * @param initialCapacity The initial capacity of the list.
   */
  public SortedSetArrayList(final int initialCapacity) {
    super(initialCapacity);
    this.comparator = DEFAULT_COMPARATOR;
  }

  /**
   * Constructs an empty list with an initial capacity of ten and a {@link Comparator comparator} specifying the
   * {@linkplain Comparable natural ordering} of elements.
   */
  public SortedSetArrayList() {
    this.comparator = DEFAULT_COMPARATOR;
  }

  /**
   * Throws {@link UnsupportedOperationException}.
   */
  @Override
  public void add(final int index, final E element) {
    throw new UnsupportedOperationException();
  }

  /**
   * Throws {@link UnsupportedOperationException}.
   */
  @Override
  public boolean addAll(final int index, final Collection<? extends E> c) {
    throw new UnsupportedOperationException();
  }

  /**
   * Throws {@link UnsupportedOperationException}.
   */
  @Override
  public E set(final int index, final E element) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean add(final E e) {
    final int index = CollectionUtil.binaryClosestSearch(this, e, comparator);
    if (index < size() && get(index).equals(e))
      return false;

    super.add(index, e);
    return true;
  }

  @Override
  public boolean addAll(final Collection<? extends E> c) {
    return CollectionUtil.addAll(this, c);
  }

  private Class<?> comparatorType;

  private Class<?> comparatorType() {
    return comparatorType == null ? comparatorType = (Class<?>)((ParameterizedType)comparator.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0] : comparatorType;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private int indexOf(final Object o, final int fromIndex, final int toIndex) {
    if (comparator != DEFAULT_COMPARATOR && comparatorType().isInstance(o))
      return CollectionUtil.binarySearch(this, fromIndex, toIndex, o, (Comparator)comparator);

    if (o == null)
      return CollectionUtil.binarySearch(this, fromIndex, toIndex, o, DEFAULT_COMPARATOR);

    if (o instanceof Comparable) {
      final int i = CollectionUtil.binarySearch(this, fromIndex, toIndex, o, DEFAULT_COMPARATOR);
      if (i < 0)
        return i;

      Object a = get(i);
      if (o.equals(a))
        return i;

      for (int sign = -1;; sign = 1) { // [N]
        for (int offset = 1, j;; ++offset) { // [N]
          j = i + sign * offset;
          if (sign == -1 ? j < fromIndex : toIndex <= j)
            break;

          a = get(j);
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

  /**
   * {@inheritDoc}
   * <p>
   * <b>Runtime performance</b>: {@code O(log2(n) * m)} if the provided collection is a {@link SortedSetArrayList}; otherwise
   * {@code O(n * m)}.
   */
  @Override
  public boolean containsAll(final Collection<?> c) {
    if (c.size() == 0)
      return true;

    final Iterator<?> iterator = c.iterator();
    if (c instanceof SortedSetArrayList) {
      for (int i = 0; iterator.hasNext();) // [I]
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
  public boolean retainAll(final Collection<?> c) {
    final int i$ = size();
    if (i$ == 0)
      return false;

    if (c.size() > 0) {
      E elem;
      E prev = null;
      boolean removed = false;
      final int end = i$ - 1;
      int i = end;
      do { // [RA]
        elem = get(i);
        final boolean isSameAsPrev = i != end && Objects.equals(prev, elem);
        if (!isSameAsPrev) {
          if (removed = !c.contains(elem)) {
            remove(i);
          }
        }
        else if (removed) {
          remove(i);
        }

        prev = elem;
      }
      while (--i >= 0);

      return i$ != size();
    }

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

    if (!get(index).equals(o))
      return -1;

    while (--index > 0 && get(index).equals(o));
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

    if (!get(index).equals(o))
      return -1;

    final int len = size();
    while (++index < len && get(index).equals(o));
    return index - 1;
  }

  /**
   * {@inheritDoc}
   *
   * @implNote The {@link ListIterator} returned by this method does not support {@link ListIterator#add(Object)} or
   *           {@link ListIterator#set(Object)}.
   */
  @Override
  public ListIterator<E> listIterator(final int index) {
    assertRange("index", index, "size()", size(), true);
    return new CursorListIterator<E>(super.listIterator(index)) {
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
  public void sort(final Comparator<? super E> c) {
    super.sort(c != null ? c : comparator);
  }

  @Override
  public SortedSetArrayList<E> subList(final int fromIndex, final int toIndex) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Comparator<? super E> comparator() {
    return comparator;
  }

  @Override
  public SortedSet<E> subSet(final E fromElement, final E toElement) {
    throw new UnsupportedOperationException();
  }

  @Override
  public SortedSet<E> headSet(final E toElement) {
    throw new UnsupportedOperationException();
  }

  @Override
  public SortedSet<E> tailSet(final E fromElement) {
    throw new UnsupportedOperationException();
  }

  @Override
  public E first() {
    if (size() == 0)
      throw new NoSuchElementException();

    return get(0);
  }

  @Override
  public E last() {
    final int size = size();
    if (size == 0)
      throw new NoSuchElementException();

    return get(size - 1);
  }
}