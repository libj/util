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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A {@link DelegateCollection} that provides callback methods to observe the addition and removal of elements, either due to direct
 * method invocation on the collection instance itself, or via {@link #iterator()}, {@link #spliterator()},
 * {@link #forEach(Consumer)}, and any other entrypoint that facilitates modification of the elements in this list.
 *
 * @param <E> The type of elements in this collection.
 * @see #beforeAdd(Object,Object)
 * @see #afterAdd(Object,RuntimeException)
 * @see #beforeRemove(Object)
 * @see #afterRemove(Object,RuntimeException)
 */
public abstract class ObservableCollection<E> extends DelegateCollection<E> {
  protected static final Object preventDefault = new Object();

  /**
   * Creates a new {@link ObservableCollection} with the specified target {@link Collection}.
   *
   * @param target The target {@link Collection}.
   * @throws NullPointerException If the target {@link Collection} is null.
   */
  public ObservableCollection(final Collection<E> target) {
    super(target);
  }

  /**
   * Callback method that is invoked immediately after a value is retrieved via {@link Iterator#next()}.
   *
   * @param value The value desired to be returned by the {@link Iterator#next()} operation.
   * @param e A {@link RuntimeException} that occurred during the add operation, or {@code null} if no exception occurred.
   * @return The value to be returned by the {@link Iterator#next()} operation.
   */
  protected E afterGet(final E value, final RuntimeException e) {
    return value;
  }

  /**
   * Callback method that is invoked immediately before an element is added to the enclosed {@link Collection}.
   *
   * @param element The element to be added to the enclosed {@link Collection}.
   * @param preventDefault The object to return if the subsequent {@code add} operation should be prevented.
   * @return The element to be added to the enclosed {@link Collection}.
   */
  protected Object beforeAdd(final E element, final Object preventDefault) {
    return element;
  }

  /**
   * Callback method that is invoked immediately after an element is added to the enclosed {@link Collection}.
   *
   * @param element The element added to the enclosed {@link Collection}.
   * @param e A {@link RuntimeException} that occurred during the add operation, or {@code null} if no exception occurred.
   */
  protected void afterAdd(final E element, final RuntimeException e) {
  }

  /**
   * Callback method that is invoked immediately before an element is removed from the enclosed {@link Collection}.
   *
   * @param element The element to be removed from the enclosed {@link Collection}.
   * @return If this method returns {@code true}, the subsequent <u>remove</u> operation will be performed; if this method returns
   *         {@code false}, the subsequent <u>remove</u> operation will not be performed.
   */
  protected boolean beforeRemove(final Object element) {
    return true;
  }

  /**
   * Callback method that is invoked immediately after an element is removed from the enclosed {@link Collection}.
   *
   * @param element The element removed from the enclosed {@link Collection}, or attempted to be removed from the {@link Collection}
   *          in case of a {@link RuntimeException}.
   * @param e A {@link RuntimeException} that occurred during the add operation, or {@code null} if no exception occurred.
   */
  protected void afterRemove(final Object element, final RuntimeException e) {
  }

  /**
   * Delegate method that is invoked for all {@link Object#equals(Object)} operations. This method is intended to be overridden to
   * support behavior that is not inherently possible with the default reliance on {@link Object#equals(Object)} for the determination
   * of object equality by this {@link ObservableCollection}.
   *
   * @param o1 An object.
   * @param o2 An object to be compared with a for equality.
   * @return {@code true} if {@code o1} is equal to {@code o2}; {@code false} otherwise.
   */
  protected boolean equals(final Object o1, final Object o2) {
    return Objects.equals(o1, o2);
  }

  /**
   * A {@link DelegateIterator} that delegates callback methods to the parent {@link ObservableCollection} instance for the retrieval
   * and removal of elements.
   */
  protected class ObservableIterator extends DelegateIterator<E> {
    private E current;

    /**
     * Creates a new {@link ObservableIterator} for the specified {@link Iterator}.
     *
     * @param iterator The {@link Iterator}.
     * @throws NullPointerException If the specified {@link Iterator} is null.
     */
    protected ObservableIterator(final Iterator<E> iterator) {
      super(iterator);
    }

    @Override
    public E next() {
      RuntimeException exception = null;
      E value = null;
      try {
        value = super.next();
      }
      catch (final RuntimeException re) {
        exception = re;
      }

      value = ObservableCollection.this.afterGet(value, exception);
      if (exception != null)
        throw exception;

      return current = value;
    }

    @Override
    public void remove() {
      final E remove = current;
      if (!ObservableCollection.this.beforeRemove(remove))
        return;

      RuntimeException exception = null;
      try {
        super.remove();
      }
      catch (final RuntimeException re) {
        exception = re;
      }

      ObservableCollection.this.afterRemove(remove, exception);
      if (exception != null)
        throw exception;
    }
  }

  /**
   * Returns an iterator over the elements in this collection. Calling {@link Iterator#remove()} will delegate a callback to
   * {@link #beforeRemove(Object)} and {@link #afterRemove(Object,RuntimeException)} on this instance. All elements for which
   * {@link #beforeRemove(Object)} returns false will not be removed from this collection.
   *
   * @return An {@link Iterator} over the elements in this collection.
   * @see Collection#iterator()
   */
  @Override
  public Iterator<E> iterator() {
    return new ObservableIterator(super.iterator());
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback method {@link #afterGet(Object,RuntimeException)} is called immediately after each element of the enclosed
   * collection is retrieved to be tested for equality.
   */
  @Override
  public boolean contains(final Object o) {
    final Iterator<E> iterator = iterator();
    while (iterator.hasNext())
      if (equals(o, iterator.next()))
        return true;

    return false;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback method {@link #afterGet(Object,RuntimeException)} is called immediately after each element of the enclosed
   * collection is retrieved to be tested for equality.
   */
  @Override
  public boolean containsAll(final Collection<?> c) {
    return CollectionUtil.containsAll(this, c);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeAdd(Object,Object)} and {@link ObservableCollection#afterAdd(Object, RuntimeException)} are
   * called immediately before and after the enclosed collection is modified. If {@link #beforeAdd(Object,Object)} returns false, the
   * element will not be added.
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean add(E e) {
    final int size = size();
    final Object beforeAdd = beforeAdd(e, preventDefault);
    if (beforeAdd == preventDefault)
      return size != size();

    e = (E)beforeAdd;
    RuntimeException exception = null;
    try {
      super.add(e);
    }
    catch (final RuntimeException re) {
      exception = re;
    }

    afterAdd(e, exception);
    if (exception != null)
      throw exception;

    return size != size();
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeAdd(Object,Object)} and {@link #afterAdd(Object,RuntimeException)} are called immediately
   * before and after the enclosed collection is modified for the addition of each element in the argument Collection. All elements
   * for which {@link #beforeAdd(Object,Object)} returns false will not be added to this collection.
   */
  @Override
  public boolean addAll(final Collection<? extends E> c) {
    return CollectionUtil.addAll(this, c);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(Object)} and {@link #afterRemove(Object,RuntimeException)} are called immediately
   * before and after the enclosed collection is modified. If {@link #beforeRemove(Object)} returns false, the element will not be
   * removed.
   */
  @Override
  public boolean remove(final Object o) {
    final int size = size();
    if (!beforeRemove(o))
      return size != size();

    RuntimeException exception = null;
    try {
      super.remove(o);
    }
    catch (final RuntimeException re) {
      exception = re;
    }

    afterRemove(o, exception);
    if (exception != null)
      throw exception;

    return size != size();
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(Object)} and {@link #afterRemove(Object,RuntimeException)} are called immediately
   * before and after the enclosed collection is modified for the removal of each element in the argument Collection. All elements for
   * which {@link #beforeRemove(Object)} returns false will not be removed from this collection.
   */
  @Override
  public boolean removeAll(final Collection<?> c) {
    return CollectionUtil.removeAll(this, c);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(Object)} and {@link #afterRemove(Object,RuntimeException)} are called immediately
   * before and after the enclosed collection is modified for the removal of each element. If {@link #beforeRemove(Object)} returns
   * false, the element will not be removed.
   */
  @Override
  public boolean removeIf(final Predicate<? super E> filter) {
    return removeIf$(filter);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(Object)} and {@link #afterRemove(Object,RuntimeException)} are called immediately
   * before and after the enclosed collection is modified for the removal of each element not in the argument Collection.
   */
  @Override
  public boolean retainAll(final Collection<?> c) {
    if (size() == 0)
      return false;

    boolean changed = false;
    for (final Iterator<E> i = iterator(); i.hasNext();) { // [I]
      if (!c.contains(i.next())) {
        i.remove();
        changed = true;
      }
    }

    return changed;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(Object)} and {@link #afterRemove(Object,RuntimeException)} are called immediately
   * before and after the enclosed collection is modified for the removal of each element.
   */
  @Override
  public void clear() {
    if (size() > 0) {
      for (final Iterator<E> i = iterator(); i.hasNext();) { // [I]
        i.next();
        i.remove();
      }
    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback method {@link #afterGet(Object,RuntimeException)} is called immediately after each element of the enclosed
   * collection is retrieved.
   */
  @Override
  public Object[] toArray() {
    final int size = size();
    if (size == 0)
      return ArrayUtil.EMPTY_ARRAY;

    final Object[] a = new Object[size];
    final Iterator<E> iterator = iterator();
    for (int i = 0; iterator.hasNext(); ++i) // [I]
      a[i] = iterator.next();

    return a;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback method {@link #afterGet(Object,RuntimeException)} is called immediately after each element of the enclosed
   * collection is retrieved.
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> T[] toArray(T[] a) {
    final int size = size();
    int i = 0;
    if (size > 0) {
      if (a.length < size)
        a = (T[])Array.newInstance(a.getClass().getComponentType(), size);

      for (final Iterator<E> iterator = iterator(); iterator.hasNext(); ++i) // [I]
        a[i] = (T)iterator.next();
    }

    if (++i < a.length)
      a[i] = null;

    return a;
  }

  // /**
  // * {@inheritDoc}
  // * <p>
  // * The callback method {@link #afterGet(Object,RuntimeException)} is called
  // * immediately after each element of the enclosed collection is retrieved.
  // */
  // @Override
  // public <T>T[] toArray(final IntFunction<T[]> generator) {
  // return toArray$(generator);
  // }

  /**
   * {@inheritDoc}
   * <p>
   * The callback method {@link #afterGet(Object,RuntimeException)} is called immediately after each element of the enclosed
   * collection is retrieved.
   */
  @Override
  public void forEach(final Consumer<? super E> action) {
    forEach$(action);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback method {@link #afterGet(Object,RuntimeException)} is called immediately after each element of the enclosed
   * collection is retrieved.
   */
  @Override
  public Spliterator<E> spliterator() {
    return spliterator$();
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback method {@link #afterGet(Object,RuntimeException)} is called immediately after each element of the enclosed
   * collection is retrieved.
   */
  @Override
  public Stream<E> stream() {
    return stream$();
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback method {@link #afterGet(Object,RuntimeException)} is called immediately after each element of the enclosed
   * collection is retrieved.
   */
  @Override
  public Stream<E> parallelStream() {
    return parallelStream$();
  }

  private void touchElements() {
    if (size() > 0)
      for (final Iterator<E> i = iterator(); i.hasNext(); i.next()); // [I]
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback method {@link #afterGet(Object,RuntimeException)} is called immediately after each element of the enclosed
   * collection is retrieved.
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Collection) || size() != ((Collection<?>)obj).size())
      return false;

    touchElements();
    return super.equals(obj);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback method {@link #afterGet(Object,RuntimeException)} is called immediately after each element of the enclosed
   * collection is retrieved.
   */
  @Override
  public int hashCode() {
    if (target == null)
      return 31;

    touchElements();
    return 31 + target.hashCode();
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback method {@link #afterGet(Object,RuntimeException)} is called immediately after each element of the enclosed
   * collection is retrieved.
   */
  @Override
  public String toString() {
    if (target == null)
      return "null";

    touchElements();
    return String.valueOf(target);
  }
}