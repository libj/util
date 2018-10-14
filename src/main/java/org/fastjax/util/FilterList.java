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

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

/**
 * A {@code FilterList} contains some other {@link List}, which it uses as its
 * basic source of data, possibly transforming the data along the way or
 * providing additional functionality. The class {@code FilterList} itself
 * simply overrides all methods of {@link AbstractList} with versions that pass
 * all requests to the source {@link List}. Subclasses of {@code FilterList} may
 * further override some of these methods and may also provide additional
 * methods and fields.
 */
public abstract class FilterList<E> extends AbstractList<E> {
  /**
   * The List to be filtered.
   */
  @SuppressWarnings("rawtypes")
  protected volatile List source;

  /**
   * Creates a new {@code FilterList} with the specified {@code source}.
   *
   * @param source The source {@link List} object.
   * @throws NullPointerException If {@code source} is null.
   */
  public FilterList(final List<E> source) {
    this.source = Objects.requireNonNull(source);
  }

  /**
   * Creates a new {@code FilterList} with a null source.
   */
  protected FilterList() {
  }

  /**
   * Returns a new instance of the subclass of {@code FilterList}.
   *
   * @param source The source {@link List} object.
   * @return A new instance of the subclass of {@code FilterList}.
   */
  @SuppressWarnings("rawtypes")
  protected abstract FilterList<E> newInstance(final List source);

  @Override
  public int size() {
    return source.size();
  }

  @Override
  public boolean isEmpty() {
    return source.isEmpty();
  }

  @Override
  public boolean contains(final Object o) {
    return source.contains(o);
  }

  @Override
  public Iterator<E> iterator() {
    return source.iterator();
  }

  @Override
  public Object[] toArray() {
    return source.toArray();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T>T[] toArray(final T[] a) {
    return (T[])source.toArray(a);
  }

  @Override
  public boolean add(final E e) {
    return source.add(e);
  }

  @Override
  public boolean remove(final Object o) {
    return source.remove(o);
  }

  @Override
  public boolean containsAll(final Collection<?> c) {
    return source.containsAll(c);
  }

  @Override
  public boolean addAll(final Collection<? extends E> c) {
    return source.addAll(c);
  }

  @Override
  public boolean removeAll(final Collection<?> c) {
    return source.removeAll(c);
  }

  @Override
  public boolean retainAll(final Collection<?> c) {
    return source.retainAll(c);
  }

  @Override
  public void clear() {
    source.clear();
  }

  @Override
  public boolean addAll(final int index, final Collection<? extends E> c) {
    return source.addAll(index, c);
  }

  @Override
  @SuppressWarnings("unchecked")
  public E get(final int index) {
    return (E)source.get(index);
  }

  @Override
  @SuppressWarnings("unchecked")
  public E set(final int index, final E element) {
    return (E)source.set(index, element);
  }

  @Override
  public void add(final int index, final E element) {
    source.add(index, element);
  }

  @Override
  @SuppressWarnings("unchecked")
  public E remove(final int index) {
    return (E)source.remove(index);
  }

  @Override
  public int indexOf(final Object o) {
    return source.indexOf(o);
  }

  @Override
  public int lastIndexOf(final Object o) {
    return source.lastIndexOf(o);
  }

  @Override
  public ListIterator<E> listIterator() {
    return source.listIterator();
  }

  @Override
  public ListIterator<E> listIterator(final int index) {
    return source.listIterator(index);
  }

  @Override
  public FilterList<E> subList(final int fromIndex, final int toIndex) {
    return newInstance(source.subList(fromIndex, toIndex));
  }
}