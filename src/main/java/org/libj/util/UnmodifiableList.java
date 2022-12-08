/* Copyright (c) 2022 LibJ
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

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * An extensible alternative to {@link Collections#unmodifiableList(List)} that provides an unmodifiable view of an underlying
 * {@link List}.
 * <p>
 * This class allows modules to provide users with "read-only" access to internal collections. Query operations on the returned
 * collection "read through" to the specified collection, and attempts to modify the returned collection, whether direct or via its
 * iterator, result in an {@link UnsupportedOperationException}.
 * <p>
 * The list is serializable if the underlying list is serializable.
 *
 * @param <E> The type parameter of elements in the list.
 */
public class UnmodifiableList<E> extends UnmodifiableCollection<E> implements List<E> {
  /**
   * Creates a new {@link UnmodifiableList} with the specified target {@link List}.
   *
   * @param target The target {@link List}.
   * @throws IllegalArgumentException If the target {@link List} is null.
   */
  public UnmodifiableList(final List<? extends E> target) {
    super(target);
  }

  /**
   * Creates a new {@link UnmodifiableList} with a null target.
   */
  protected UnmodifiableList() {
  }

  @Override
  protected List<? extends E> getTarget() {
    return (List<? extends E>)super.target;
  }

  @Override
  public E get(final int index) {
    return getTarget().get(index);
  }

  @Override
  public E set(final int index, final E element) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void add(final int index, final E element) {
    throw new UnsupportedOperationException();
  }

  @Override
  public E remove(final int index) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int indexOf(final Object o) {
    return getTarget().indexOf(o);
  }

  @Override
  public int lastIndexOf(final Object o) {
    return getTarget().lastIndexOf(o);
  }

  @Override
  public boolean addAll(final int index, final Collection<? extends E> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void replaceAll(final UnaryOperator<E> operator) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void sort(final Comparator<? super E> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListIterator<E> listIterator() {
    return listIterator(0);
  }

  @Override
  public ListIterator<E> listIterator(final int index) {
    return new ListIterator<E>() {
      private final ListIterator<? extends E> i = getTarget().listIterator(index);

      @Override
      public boolean hasNext() {
        return i.hasNext();
      }

      @Override
      public E next() {
        return i.next();
      }

      @Override
      public boolean hasPrevious() {
        return i.hasPrevious();
      }

      @Override
      public E previous() {
        return i.previous();
      }

      @Override
      public int nextIndex() {
        return i.nextIndex();
      }

      @Override
      public int previousIndex() {
        return i.previousIndex();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }

      @Override
      public void set(final E e) {
        throw new UnsupportedOperationException();
      }

      @Override
      public void add(final E e) {
        throw new UnsupportedOperationException();
      }

      @Override
      public void forEachRemaining(final Consumer<? super E> action) {
        i.forEachRemaining(action);
      }
    };
  }

  @Override
  public List<E> subList(final int fromIndex, final int toIndex) {
    return new UnmodifiableList<>(getTarget().subList(fromIndex, toIndex));
  }

  @Override
  public boolean equals(final Object o) {
    return o == this || getTarget().equals(o);
  }

  @Override
  public int hashCode() {
    return getTarget().hashCode();
  }
}