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

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

/**
 * A {@code DelegateList} contains some other {@link List}, to which it
 * delegates its method calls, possibly transforming the data along the way or
 * providing additional functionality. The class {@code DelegateList} itself
 * simply overrides all methods of {@link AbstractList} with versions that pass
 * all requests to the target {@link List}. Subclasses of {@code DelegateList}
 * may further override some of these methods and may also provide additional
 * methods and fields.
 *
 * @param <E> The type of elements in this list.
 */
public abstract class DelegateList<E> extends AbstractList<E> {
  /**
   * The target List.
   */
  @SuppressWarnings("rawtypes")
  protected volatile List target;

  /**
   * Creates a new {@code DelegateList} with the specified {@code target}.
   *
   * @param target The target {@link List} object.
   * @throws NullPointerException If {@code target} is null.
   */
  public DelegateList(final List<E> target) {
    this.target = Objects.requireNonNull(target);
  }

  /**
   * Creates a new {@code DelegateList} with a null target.
   */
  protected DelegateList() {
  }

  @Override
  public int size() {
    return target.size();
  }

  @Override
  public boolean isEmpty() {
    return target.isEmpty();
  }

  @Override
  public boolean contains(final Object o) {
    return target.contains(o);
  }

  @Override
  public Iterator<E> iterator() {
    return target.iterator();
  }

  @Override
  public Object[] toArray() {
    return target.toArray();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T>T[] toArray(final T[] a) {
    return (T[])target.toArray(a);
  }

  @Override
  public boolean add(final E e) {
    return target.add(e);
  }

  @Override
  public boolean remove(final Object o) {
    return target.remove(o);
  }

  @Override
  public boolean containsAll(final Collection<?> c) {
    return target.containsAll(c);
  }

  @Override
  public boolean addAll(final Collection<? extends E> c) {
    return target.addAll(c);
  }

  @Override
  public boolean removeAll(final Collection<?> c) {
    return target.removeAll(c);
  }

  @Override
  public boolean retainAll(final Collection<?> c) {
    return target.retainAll(c);
  }

  @Override
  public void clear() {
    target.clear();
  }

  @Override
  public boolean addAll(final int index, final Collection<? extends E> c) {
    return target.addAll(index, c);
  }

  @Override
  @SuppressWarnings("unchecked")
  public E get(final int index) {
    return (E)target.get(index);
  }

  @Override
  @SuppressWarnings("unchecked")
  public E set(final int index, final E element) {
    return (E)target.set(index, element);
  }

  @Override
  public void add(final int index, final E element) {
    target.add(index, element);
  }

  @Override
  @SuppressWarnings("unchecked")
  public E remove(final int index) {
    return (E)target.remove(index);
  }

  @Override
  public int indexOf(final Object o) {
    return target.indexOf(o);
  }

  @Override
  public int lastIndexOf(final Object o) {
    return target.lastIndexOf(o);
  }

  @Override
  public ListIterator<E> listIterator() {
    return target.listIterator();
  }

  @Override
  public ListIterator<E> listIterator(final int index) {
    return target.listIterator(index);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The class {@code DelegateList} does not itself implement
   * {@code #subList(int,int)}, so calling this method on an instance of a
   * subclass of {@code DelegateList} that does not override this method will
   * result in a {@link UnsupportedOperationException}.
   */
  @Override
  public DelegateList<E> subList(final int fromIndex, final int toIndex) {
    throw new UnsupportedOperationException();
  }
}