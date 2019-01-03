/* Copyright (c) 2017 OpenJAX
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

package org.openjax.standard.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.Function;

/**
 * An implementation of the List interface that transforms the elements of
 * the supplied source List based on {@code sourceToTarget} and
 * {@code targetToSource} lambda functions.
 *
 * @param <S> Type of source List.
 * @param <T> Type of target List.
 * @see List
 */
public class TransList<S,T> extends DelegateList<T> {
  protected final Function<S,T> sourceToTarget;
  protected final Function<T,S> targetToSource;

  /**
   * Creates a new {@code TransList} with the specified source List, and
   * functions defining the translation of objects types {@code S -> T} and
   * {@code T -> S}.
   * <p>
   * If {@code sourceToTarget} is null, all methods that require a translation
   * of {@code S -> T} will throw a {@link UnsupportedOperationException}.
   * <p>
   * If {@code targetToSource} is null, all methods that require a translation
   * of {@code T -> S} will throw a {@link UnsupportedOperationException}.
   *
   * @param source The source List of type {@code <S>}.
   * @param sourceToTarget The {@code Function} defining the translation from
   *          {@code S -> T}.
   * @param targetToSource The {@code Function} defining the translation from
   *          {@code T -> S}.
   * @throws NullPointerException If {@code source} is null.
   */
  public TransList(final List<S> source, final Function<S,T> sourceToTarget, final Function<T,S> targetToSource) {
    super();
    super.target = Objects.requireNonNull(source);
    this.sourceToTarget = sourceToTarget;
    this.targetToSource = targetToSource;
  }

  @Override
  public boolean contains(final Object o) {
    if (sourceToTarget == null)
      throw new UnsupportedOperationException();

    final Iterator<S> iterator = target.iterator();
    if (o != null) {
      while (iterator.hasNext())
        if (o.equals(sourceToTarget.apply(iterator.next())))
          return true;
    }
    else {
      while (iterator.hasNext())
        if (iterator.next() == null)
          return true;
    }

    return false;
  }

  @Override
  public Iterator<T> iterator() {
    final Iterator<S> iterator = target.iterator();
    return new Iterator<T>() {
      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public T next() {
        if (sourceToTarget == null)
          throw new UnsupportedOperationException();

        final S e = iterator.next();
        return e == null ? null : sourceToTarget.apply(e);
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

    final Object[] array = new Object[size()];
    final Iterator<S> iterator = target.iterator();
    for (int i = 0; i < size(); i++) {
      final S e = iterator.next();
      array[i] = e == null ? null : sourceToTarget.apply(e);
    }

    return array;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <E>E[] toArray(final E[] a) {
    if (sourceToTarget == null)
      throw new UnsupportedOperationException();

    final Iterator<S> iterator = target.iterator();
    for (int i = 0; i < size(); i++) {
      final S e = iterator.next();
      a[i] = e == null ? null : (E)sourceToTarget.apply(e);
    }

    return a;
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
      final S e = iterator.next();
      if (o != null ? o.equals(sourceToTarget.apply(e)) : sourceToTarget.apply(e) == null) {
        iterator.remove();
        return true;
      }
    }

    return false;
  }

  @Override
  @SuppressWarnings("unlikely-arg-type")
  public boolean containsAll(final Collection<?> c) {
    for (final Object e : c)
      if (contains(e))
        return true;

    return false;
  }

  @Override
  public boolean addAll(final Collection<? extends T> c) {
    boolean changed = false;
    for (final T e : c)
      changed |= add(e);

    return changed;
  }

  @Override
  @SuppressWarnings("unlikely-arg-type")
  public boolean removeAll(final Collection<?> c) {
    boolean changed = false;
    for (final Object e : c)
      changed |= remove(e);

    return changed;
  }

  @Override
  @SuppressWarnings("unlikely-arg-type")
  public boolean retainAll(final Collection<?> c) {
    boolean changed = false;
    final Iterator<S> iterator = target.iterator();
    while (iterator.hasNext()) {
      final S e = iterator.next();
      if (!c.contains(e == null ? null : sourceToTarget.apply(e))) {
        iterator.remove();
        changed = true;
      }
    }

    return changed;
  }

  @Override
  public boolean addAll(int index, final Collection<? extends T> c) {
    if (c.size() == 0)
      return false;

    for (final T e : c)
      add(index++, e);

    return true;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T get(final int index) {
    if (sourceToTarget == null)
      throw new UnsupportedOperationException();

    return sourceToTarget.apply((S)target.get(index));
  }

  @Override
  @SuppressWarnings("unchecked")
  public T set(final int index, final T element) {
    if (sourceToTarget == null || targetToSource == null)
      throw new UnsupportedOperationException();

    return sourceToTarget.apply((S)target.set(index, targetToSource.apply(element)));
  }

  @Override
  public void add(final int index, final T element) {
    if (targetToSource == null)
      throw new UnsupportedOperationException();

    target.add(index, targetToSource.apply(element));
  }

  @Override
  @SuppressWarnings("unchecked")
  public T remove(final int index) {
    if (sourceToTarget == null)
      throw new UnsupportedOperationException();

    return sourceToTarget.apply((S)target.remove(index));
  }

  @Override
  @SuppressWarnings("unchecked")
  public int indexOf(final Object o) {
    if (targetToSource == null)
      throw new UnsupportedOperationException();

    return target.indexOf(targetToSource.apply((T)o));
  }

  @Override
  @SuppressWarnings("unchecked")
  public int lastIndexOf(final Object o) {
    if (targetToSource == null)
      throw new UnsupportedOperationException();

    return target.lastIndexOf(targetToSource.apply((T)o));
  }

  @Override
  public ListIterator<T> listIterator() {
    return listIterator(0);
  }

  @Override
  public ListIterator<T> listIterator(final int index) {
    final ListIterator<S> iterator = target.listIterator();
    return new ListIterator<T>() {
      @Override
      public boolean hasPrevious() {
        return iterator.hasPrevious();
      }

      @Override
      public T previous() {
        if (sourceToTarget == null)
          throw new UnsupportedOperationException();

        return sourceToTarget.apply(iterator.previous());
      }

      @Override
      public int nextIndex() {
        return iterator.nextIndex();
      }

      @Override
      public int previousIndex() {
        return iterator.previousIndex();
      }

      @Override
      public void set(final T e) {
        if (targetToSource == null)
          throw new UnsupportedOperationException();

        iterator.set(targetToSource.apply(e));
      }

      @Override
      public void add(final T e) {
        if (targetToSource == null)
          throw new UnsupportedOperationException();

        iterator.add(targetToSource.apply(e));
      }

      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public T next() {
        if (sourceToTarget == null)
          throw new UnsupportedOperationException();

        final S e = iterator.next();
        return e == null ? null : sourceToTarget.apply(e);
      }

      @Override
      public void remove() {
        iterator.remove();
      }
    };
  }

  @Override
  public TransList<S,T> subList(final int fromIndex, final int toIndex) {
    return new TransList<S,T>(target.subList(fromIndex, toIndex), sourceToTarget, targetToSource);
  }
}