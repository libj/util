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
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * A {@link DelegateList} contains some other {@link List}, to which it
 * delegates its method calls, possibly transforming the data along the way or
 * providing additional functionality. The class {@link DelegateList} itself
 * simply overrides all methods of {@link AbstractList} with versions that pass
 * all requests to the target {@link List}. Subclasses of {@link DelegateList}
 * may further override some of these methods and may also provide additional
 * methods and fields.
 *
 * @param <E> The type of elements in this list.
 */
public abstract class DelegateList<E> extends AbstractList<E> {
  /** The target List. */
  @SuppressWarnings("rawtypes")
  protected volatile List target;

  /**
   * Creates a new {@link DelegateList} with the specified target {@link List}.
   *
   * @param target The target {@link List}.
   * @throws NullPointerException If the target {@link List} is null.
   */
  public DelegateList(final List<E> target) {
    this.target = Objects.requireNonNull(target);
  }

  /**
   * Creates a new {@link DelegateList} with a null target.
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

//  /**
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
//  protected final <T>T[] superToArray(final IntFunction<T[]> generator) {
//    return super.toArray(generator);
//  }

//  @Override
//  @SuppressWarnings("unchecked")
//  public <T>T[] toArray(final IntFunction<T[]> generator) {
//    return (T[])target.toArray(generator);
//  }

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

  @Override
  public void clear() {
    target.clear();
  }

  /**
   * {@inheritDoc}
   * <p>
   * The class {@link DelegateList} does not itself implement
   * {@code #subList(int,int)}, so calling this method on an instance of a
   * subclass of {@link DelegateList} that does not override this method will
   * result in a {@link UnsupportedOperationException}.
   */
  @Override
  public DelegateList<E> subList(final int fromIndex, final int toIndex) {
    throw new UnsupportedOperationException();
  }

  /**
   * Protected method providing access to the default implementation of
   * {@link List#replaceAll(UnaryOperator)}.
   *
   * @param operator The operator to apply to each element.
   * @throws UnsupportedOperationException If this list is unmodifiable.
   *           Implementations may throw this exception if an element cannot be
   *           replaced or if, in general, modification is not supported.
   * @throws NullPointerException If the specified operator is null or if the
   *           operator result is a null value and this list does not permit
   *           null elements.
   */
  protected final void superReplaceAll(final UnaryOperator<E> operator) {
    super.replaceAll(operator);
  }

  @Override
  public void replaceAll(final UnaryOperator<E> operator) {
    target.replaceAll(operator);
  }

  /**
   * Protected method providing access to the default implementation of
   * {@link List#sort(Comparator)}.
   *
   * @param c The {@code Comparator} used to compare list elements. A
   *          {@code null} value indicates that the elements'
   *          {@linkplain Comparable natural ordering} should be used.
   * @throws ClassCastException If the list contains elements that are not
   *           <i>mutually comparable</i> using the specified comparator.
   * @throws UnsupportedOperationException If the list's list-iterator does not
   *           support the {@code set} operation.
   * @throws IllegalArgumentException If the comparator is found to violate the
   *           {@link Comparator} contract.
   */
  protected final void superSort(final Comparator<? super E> c) {
    super.sort(c);
  }

  @Override
  public void sort(final Comparator<? super E> c) {
    target.sort(c);
  }

  /**
   * Protected method providing access to the default implementation of
   * {@link Collection#forEach(Consumer)}.
   *
   * @param action The action to be performed for each element.
   * @throws NullPointerException If the specified action is null
   */
  protected final void superForEach(final Consumer<? super E> action) {
    super.forEach(action);
  }

  @Override
  public void forEach(final Consumer<? super E> action) {
    target.forEach(action);
  }

  /**
   * Protected method providing access to the default implementation of
   * {@link Collection#removeIf(Predicate)}.
   *
   * @param filter A predicate which returns {@code true} for elements to be
   *          removed.
   * @return {@code true} if any elements were removed.
   * @throws NullPointerException If the specified filter is null.
   * @throws UnsupportedOperationException If elements cannot be removed from
   *           this collection. Implementations may throw this exception if a
   *           matching element cannot be removed or if, in general, removal is
   *           not supported.
   */
  protected final boolean superRemoveIf(final Predicate<? super E> filter) {
    return super.removeIf(filter);
  }

  @Override
  public boolean removeIf(final Predicate<? super E> filter) {
    return target.removeIf(filter);
  }

  /**
   * Protected method providing access to the default implementation of
   * {@link Collection#spliterator()}.
   *
   * @return A {@code Spliterator} over the elements in this collection.
   */
  protected final Spliterator<E> superSpliterator() {
    return super.spliterator();
  }

  @Override
  public Spliterator<E> spliterator() {
    return target.spliterator();
  }

  /**
   * Protected method providing access to the default implementation of
   * {@link Collection#stream()}.
   *
   * @return A sequential {@code Stream} over the elements in this collection.
   */
  protected final Stream<E> superStream() {
    return super.stream();
  }

  @Override
  public Stream<E> stream() {
    return target.stream();
  }

  /**
   * Protected method providing access to the default implementation of
   * {@link Collection#parallelStream()}.
   *
   * @return A possibly parallel {@code Stream} over the elements in this
   *         collection.
   */
  protected final Stream<E> superParallelStream() {
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

    if (!(obj instanceof DelegateList))
      return target == null ? obj == null : target.equals(obj);

    final DelegateList<?> that = (DelegateList<?>)obj;
    return target == null ? that.target == null : target.equals(that.target);
  }

  @Override
  public int hashCode() {
    return 31 + (target == null ? 0 : target.hashCode());
  }

  @Override
  public String toString() {
    return String.valueOf(target);
  }
}