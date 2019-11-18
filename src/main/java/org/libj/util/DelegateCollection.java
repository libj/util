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

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A {@link DelegateCollection} contains some other {@link Collection}, to which
 * it delegates its method calls, possibly transforming the data along the way
 * or providing additional functionality. The class {@link DelegateCollection}
 * itself simply overrides all methods of {@link AbstractCollection} with
 * versions that pass all requests to the target {@link Collection}. Subclasses
 * of {@link DelegateCollection} may further override some of these methods and
 * may also provide additional methods and fields.
 *
 * @param <E> The type of elements in this collection.
 */
public abstract class DelegateCollection<E> extends AbstractCollection<E> {
  /**
   * The target Collection.
   */
  @SuppressWarnings("rawtypes")
  protected volatile Collection target;

  /**
   * Creates a new {@link DelegateCollection} with the specified {@code target}.
   *
   * @param target The target {@link Collection} object.
   * @throws NullPointerException If {@code target} is null.
   */
  public DelegateCollection(final Collection<E> target) {
    this.target = Objects.requireNonNull(target);
  }

  /**
   * Creates a new {@link DelegateCollection} with a null target.
   */
  protected DelegateCollection() {
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
  public void forEach(final Consumer<? super E> action) {
    target.forEach(action);
  }

  @Override
  public boolean removeIf(final Predicate<? super E> filter) {
    return target.removeIf(filter);
  }

  @Override
  public Spliterator<E> spliterator() {
    return target.spliterator();
  }

  @Override
  public Stream<E> stream() {
    return target.stream();
  }

  @Override
  public Stream<E> parallelStream() {
    return target.parallelStream();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof DelegateCollection))
      return false;

    final DelegateCollection<?> that = (DelegateCollection<?>)obj;
    return target != null ? target.equals(that.target) : that.target == null;
  }

  @Override
  public int hashCode() {
    return target == null ? 733 : target.hashCode();
  }

  @Override
  public String toString() {
    return String.valueOf(target);
  }
}