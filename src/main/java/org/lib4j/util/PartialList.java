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

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public abstract class PartialList<E> implements List<E>, RandomAccess, Cloneable, Serializable {
  private static final long serialVersionUID = -2945838392443684891L;

  protected final List<E> list;

  @SuppressWarnings("rawtypes")
  public PartialList(final Class<? extends List> type) {
    try {
      this.list = type.newInstance();
    }
    catch (final InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public final boolean add(final E e) {
    add(size(), e);
    return true;
  }

  @Override
  public final E set(final int index, final E element) {
    final E item = remove(index);
    add(index, element);
    return item;
  }

  @Override
  public final boolean remove(final Object o) {
    final int index = indexOf(o);
    if (index == -1)
      return false;

    remove(index);
    return true;
  }

  @Override
  public final void clear() {
    for (int i = size() - 1; i >= 0; --i)
      remove(i);
  }

  @Override
  public final boolean addAll(final Collection<? extends E> c) {
    return addAll(size(), c);
  }

  @Override
  public final boolean addAll(int index, final Collection<? extends E> c) {
    if (c.size() == 0)
      return false;

    for (final E e : c)
      add(index++, e);

    return true;
  }

  @Override
  public final boolean removeAll(final Collection<?> c) {
    final int sizeBefore = size();
    for (final Object e : c) {
      final int index = indexOf(e);
      if (index != -1)
        remove(index);
    }

    return size() < sizeBefore;
  }

  @Override
  public final boolean retainAll(final Collection<?> c) {
    final int sizeBefore = size();
    for (int i = 0; i < size(); i++)
      if (!c.contains(get(i)))
        remove(i);

    return size() < sizeBefore;
  }

  @Override
  public final boolean removeIf(final Predicate<? super E> filter) {
    if (filter == null)
      throw new NullPointerException("filter == null");

    final int sizeBefore = size();
    final Vector<Integer> remove = new Vector<Integer>(size());
    for (int i = 0; i < size(); i++)
      if (filter.test(get(i)))
        remove.add(i);

    for (final Integer index : remove)
      remove(index);

    return size() < sizeBefore;
  }

  @Override
  public final void forEach(final Consumer<? super E> action) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final Spliterator<E> spliterator() {
    throw new UnsupportedOperationException();
  }

  @Override
  public final void replaceAll(final UnaryOperator<E> operator) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final void sort(final Comparator<? super E> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final int size() {
    return list.size();
  }

  @Override
  public final boolean isEmpty() {
    return list.isEmpty();
  }

  @Override
  public final boolean contains(final Object o) {
    return list.contains(o);
  }

  @Override
  public abstract PartialIterator<E> iterator();

  @Override
  public final Object[] toArray() {
    return list.toArray();
  }

  @Override
  public final <T>T[] toArray(final T[] a) {
    return list.toArray(a);
  }

  @Override
  public final boolean containsAll(final Collection<?> c) {
    return list.containsAll(c);
  }

  @Override
  public final E get(final int index) {
    return list.get(index);
  }

  @Override
  public final int indexOf(final Object o) {
    return list.indexOf(o);
  }

  @Override
  public final int lastIndexOf(final Object o) {
    return list.lastIndexOf(o);
  }

  @Override
  public final ListIterator<E> listIterator() {
    return listIterator(0);
  }

  @Override
  public abstract PartialListIterator<E> listIterator(final int index);

  @Override
  public abstract PartialList<E> subList(final int fromIndex, final int toIndex);

  @Override
  public abstract PartialList<E> clone();
}