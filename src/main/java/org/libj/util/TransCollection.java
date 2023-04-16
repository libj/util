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
import java.util.function.Function;

/**
 * An implementation of the Collection interface that transforms the elements of the supplied source Collection based on
 * {@code sourceToTarget} and {@code targetToSource} lambda functions.
 *
 * @param <S> Type of source Collection.
 * @param <T> Type of target Collection.
 * @see Collection
 */
public class TransCollection<S,T> extends DelegateCollection<T> {
  protected final Function<S,T> sourceToTarget;
  protected final Function<T,S> targetToSource;

  /**
   * Creates a new {@link TransCollection} with the specified source Collection, and functions defining the translation of objects
   * types {@code S -> T} and {@code T -> S}.
   * <p>
   * If {@code sourceToTarget} is null, all methods that require a translation of {@code S -> T} will throw a
   * {@link UnsupportedOperationException}.
   * <p>
   * If {@code targetToSource} is null, all methods that require a translation of {@code T -> S} will throw a
   * {@link UnsupportedOperationException}.
   *
   * @param source The source Collection of type {@code <S>}.
   * @param sourceToTarget The {@link Function} defining the translation from {@code S -> T}.
   * @param targetToSource The {@link Function} defining the translation from {@code T -> S}.
   * @throws NullPointerException If {@code source} is null.
   */
  public TransCollection(final Collection<S> source, final Function<S,T> sourceToTarget, final Function<T,S> targetToSource) {
    super.target = Objects.requireNonNull(source);
    this.sourceToTarget = sourceToTarget;
    this.targetToSource = targetToSource;
  }

  /**
   * Delegate method that is invoked for all {@link Object#equals(Object)} operations. This method is intended to be overridden to
   * support behavior that is not inherently possible with the default reliance on {@link Object#equals(Object)} for the
   * determination of object equality by this {@link ObservableSet}.
   *
   * @param o1 An object.
   * @param o2 An object to be compared with a for equality.
   * @return {@code true} if {@code o1} is equal to {@code o2}; {@code false} otherwise.
   */
  protected boolean equals(final Object o1, final Object o2) {
    return Objects.equals(o1, o2);
  }

  @Override
  public boolean contains(final Object o) {
    if (sourceToTarget == null)
      throw new UnsupportedOperationException();

    final Iterator<S> iterator = target.iterator();
    while (iterator.hasNext())
      if (equals(o, sourceToTarget.apply(iterator.next())))
        return true;

    return false;
  }

  @Override
  public Iterator<T> iterator() {
    return new Iterator<T>() {
      private final Iterator<S> iterator = target.iterator();

      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public T next() {
        if (sourceToTarget == null)
          throw new UnsupportedOperationException();

        return sourceToTarget.apply(iterator.next());
      }

      @Override
      public void remove() {
        iterator.remove();
      }
    };
  }

  @Override
  public Object[] toArray() {
    if (sourceToTarget == null)
      throw new UnsupportedOperationException();

    final int size = size();
    if (size == 0)
      return ArrayUtil.EMPTY_ARRAY;

    final Object[] a = new Object[size];
    toArray(a, size);
    return a;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <E>E[] toArray(E[] a) {
    if (sourceToTarget == null)
      throw new UnsupportedOperationException();

    final int size = size();
    if (size > 0) {
      if (a.length < size)
        a = (E[])Array.newInstance(a.getClass().getComponentType(), size);

      toArray(a, size);
    }

    if (a.length > size)
      a[size] = null;

    return a;
  }

  private void toArray(final Object[] a, final int i$) {
    final Iterator<S> iterator = target.iterator();
    for (int i = 0; i < i$; ++i) // [I]
      a[i] = sourceToTarget.apply(iterator.next());
  }

  @Override
  public boolean add(final T e) {
    if (targetToSource == null)
      throw new UnsupportedOperationException();

    return target.add(targetToSource.apply(e));
  }

  @Override
  public boolean remove(final Object o) {
    if (sourceToTarget == null)
      throw new UnsupportedOperationException();

    final Iterator<S> iterator = target.iterator();
    while (iterator.hasNext()) {
      if (equals(o, sourceToTarget.apply(iterator.next()))) {
        iterator.remove();
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean containsAll(final Collection<?> c) {
    return CollectionUtil.containsAll(this, c);
  }

  @Override
  public boolean addAll(final Collection<? extends T> c) {
    return CollectionUtil.addAll(this, c);
  }

  @Override
  public boolean removeAll(final Collection<?> c) {
    return CollectionUtil.removeAll(this, c);
  }

  @Override
  public boolean retainAll(final Collection<?> c) {
    final int size = size();
    if (c.size() > 0) {
      for (final Iterator<?> iterator = iterator(); iterator.hasNext();) // [I]
        if (!c.contains(iterator.next()))
          iterator.remove();

      return size != size();
    }

    if (size == 0)
      return false;

    clear();
    return true;
  }
}