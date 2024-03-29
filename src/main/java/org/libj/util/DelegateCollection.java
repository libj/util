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
 * A {@link DelegateCollection} contains some other {@link Collection}, to which it delegates its method calls, possibly
 * transforming the data along the way or providing additional functionality. The class {@link DelegateCollection} itself simply
 * overrides all methods of {@link AbstractCollection} with versions that pass all requests to the target {@link Collection}.
 * Subclasses of {@link DelegateCollection} may further override some of these methods and may also provide additional methods and
 * fields.
 *
 * @param <E> The type of elements in this collection.
 */
public abstract class DelegateCollection<E> extends AbstractCollection<E> {
  /** The target {@link Collection}. */
  @SuppressWarnings("rawtypes")
  protected Collection target;

  /**
   * Creates a new {@link DelegateCollection} with the specified target {@link Collection}.
   *
   * @param target The target {@link Collection}.
   * @throws NullPointerException If the target {@link Collection} is null.
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
  public <T> T[] toArray(final T[] a) {
    return (T[])target.toArray(a);
  }

//@formatter:off
  // /**
//   * Protected method providing access to the default implementation of
//   * {@link List#toArray(IntFunction)}.
//   *
//   * @param <T> The component type of the array to contain the collection.
//   * @param generator A function which produces a new array of the desired type
//   *          and the provided length.
//   * @return An array containing all of the elements in this collection.
//   * @throws ArrayStoreException If the runtime type of any element in this
//   *           collection is not assignable to the
//   *           {@linkplain Class#getComponentType runtime component type} of the
//   *           generated array.
//   * @throws NullPointerException If the generator function is null.
//   */
//  protected final <T>T[] toArray$(final IntFunction<T[]> generator) {
//    return super.toArray(generator);
//  }

//  @Override
//  @SuppressWarnings("unchecked")
//  public <T>T[] toArray(final IntFunction<T[]> generator) {
//    return (T[])target.toArray(generator);
//  }
//@formatter:on

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

  /**
   * Protected method providing access to the default implementation of {@link Collection#forEach(Consumer)}.
   *
   * @param action The action to be performed for each element.
   * @throws NullPointerException If the specified action is null.
   */
  protected final void forEach$(final Consumer<? super E> action) {
    super.forEach(action);
  }

  @Override
  public void forEach(final Consumer<? super E> action) {
    target.forEach(action);
  }

  /**
   * Protected method providing access to the default implementation of {@link Collection#removeIf(Predicate)}.
   *
   * @param filter A predicate which returns {@code true} for elements to be removed.
   * @return {@code true} if any elements were removed.
   * @throws NullPointerException If the specified filter is null.
   * @throws UnsupportedOperationException If elements cannot be removed from this collection. Implementations may throw this
   *           exception if a matching element cannot be removed or if, in general, removal is not supported.
   */
  protected final boolean removeIf$(final Predicate<? super E> filter) {
    return super.removeIf(filter);
  }

  @Override
  public boolean removeIf(final Predicate<? super E> filter) {
    return target.removeIf(filter);
  }

  /**
   * Protected method providing access to the default implementation of {@link Collection#spliterator()}.
   *
   * @return A {@code Spliterator} over the elements in this collection.
   */
  protected final Spliterator<E> spliterator$() {
    return super.spliterator();
  }

  @Override
  public Spliterator<E> spliterator() {
    return target.spliterator();
  }

  /**
   * Protected method providing access to the default implementation of {@link Collection#stream()}.
   *
   * @return A sequential {@code Stream} over the elements in this collection.
   */
  protected final Stream<E> stream$() {
    return super.stream();
  }

  @Override
  public Stream<E> stream() {
    return target.stream();
  }

  /**
   * Protected method providing access to the default implementation of {@link Collection#parallelStream()}.
   *
   * @return A possibly parallel {@code Stream} over the elements in this collection.
   */
  protected final Stream<E> parallelStream$() {
    return super.parallelStream();
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
      return Objects.equals(target, obj);

    final DelegateCollection<?> that = (DelegateCollection<?>)obj;
    return Objects.equals(target, that.target);
  }

  @Override
  public int hashCode() {
    int hashCode = 1;
    if (target != null)
      hashCode = 31 * hashCode + target.hashCode();

    return hashCode;
  }

  @Override
  public String toString() {
    return String.valueOf(target);
  }
}