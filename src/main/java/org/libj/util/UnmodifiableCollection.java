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

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * An extensible alternative to {@link Collections#unmodifiableCollection(Collection)} that provides an unmodifiable view of an
 * underlying {@link Collection}.
 * <p>
 * This class allows modules to provide users with "read-only" access to internal collections. Query operations on the returned
 * collection "read through" to the specified collection, and attempts to modify the returned collection, whether direct or via its
 * iterator, result in an {@link UnsupportedOperationException}.
 * <p>
 * The returned collection does <i>not</i> pass the {@link #hashCode()} and {@link #equals(Object)} operations through to the
 * backing collection, but relies on <tt>Object</tt>'s <tt>equals</tt> and <tt>hashCode</tt> methods. This is necessary to preserve
 * the contracts of these operations in the case that the backing collection is a set or a list.
 * <p>
 * The collection is serializable if the underlying collection is serializable.
 *
 * @param <E> The type parameter of elements in the collection.
 */
public class UnmodifiableCollection<E> implements Collection<E>, Serializable {
  /** The target {@link Collection}. */
  protected Collection<? extends E> target;

  /**
   * Creates a new {@link UnmodifiableCollection} with the specified target {@link Collection}.
   *
   * @param target The target {@link Collection}.
   * @throws NullPointerException If the target {@link Collection} is null.
   */
  public UnmodifiableCollection(final Collection<? extends E> target) {
    this.target = Objects.requireNonNull(target);
  }

  /**
   * Creates a new {@link UnmodifiableCollection} with a null target.
   */
  protected UnmodifiableCollection() {
  }

  /**
   * Returns the underlying {@link #target}.
   *
   * @return The underlying {@link #target}.
   */
  protected Collection<? extends E> getTarget() {
    return target;
  }

  @Override
  public boolean contains(final Object o) {
    return getTarget().contains(o);
  }

  @Override
  public boolean containsAll(final Collection<?> c) {
    return getTarget().containsAll(c);
  }

  @Override
  public void forEach(final Consumer<? super E> action) {
    getTarget().forEach(action);
  }

  @Override
  public boolean isEmpty() {
    return getTarget().isEmpty();
  }

  @Override
  public Iterator<E> iterator() {
    return new Iterator<E>() {
      private final Iterator<? extends E> i = getTarget().iterator();

      @Override
      public boolean hasNext() {
        return i.hasNext();
      }

      @Override
      public E next() {
        return i.next();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }

      @Override
      public void forEachRemaining(final Consumer<? super E> action) {
        i.forEachRemaining(action);
      }
    };
  }

  @Override
  public boolean add(final E e) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean addAll(final Collection<? extends E> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean remove(final Object o) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeAll(final Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean retainAll(final Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeIf(final Predicate<? super E> filter) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int size() {
    return getTarget().size();
  }

  @Override
  @SuppressWarnings("unchecked")
  public Spliterator<E> spliterator() {
    return (Spliterator<E>)getTarget().spliterator();
  }

  @Override
  @SuppressWarnings("unchecked")
  public Stream<E> stream() {
    return (Stream<E>)getTarget().stream();
  }

  @Override
  @SuppressWarnings("unchecked")
  public Stream<E> parallelStream() {
    return (Stream<E>)getTarget().parallelStream();
  }

  @Override
  public Object[] toArray() {
    return getTarget().toArray();
  }

  @Override
  public <T>T[] toArray(final T[] a) {
    return getTarget().toArray(a);
  }

  @Override
  public String toString() {
    return getTarget().toString();
  }
}