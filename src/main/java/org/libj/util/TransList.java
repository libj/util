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

import static org.libj.lang.Assertions.*;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * An implementation of the List interface that transforms the elements of the supplied source List based on {@code sourceToTarget}
 * and {@code targetToSource} lambda functions.
 *
 * @param <S> The type of the elements in the source List.
 * @param <LS> The type of source list.
 * @param <T> The type of the elements in the target List.
 * @param <LT> The type of target list.
 * @see List
 */
public class TransList<S,LS extends List<S>,T,LT extends List<T>> extends DelegateList<T,LT> {
  protected final BiFunction<Integer,S,T> sourceToTarget;
  protected final BiFunction<Integer,T,S> targetToSource;

  /**
   * Creates a new {@link TransList} with the specified source List, and functions defining the translation of objects types
   * {@code S -> T} and {@code T -> S}.
   * <p>
   * If {@code sourceToTarget} is null, all methods that require a translation of {@code S -> T} will throw a
   * {@link UnsupportedOperationException}.
   * <p>
   * If {@code targetToSource} is null, all methods that require a translation of {@code T -> S} will throw a
   * {@link UnsupportedOperationException}.
   *
   * @param source The source List of type {@code <S>}.
   * @param sourceToTarget The {@link BiFunction} defining the translation from {@code S -> T}.
   * @param targetToSource The {@link BiFunction} defining the translation from {@code T -> S}.
   * @throws IllegalArgumentException If {@code source} is null.
   */
  public TransList(final LS source, final BiFunction<Integer,S,T> sourceToTarget, final BiFunction<Integer,T,S> targetToSource) {
    super.target = assertNotNull(source);
    this.sourceToTarget = sourceToTarget;
    this.targetToSource = targetToSource;
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean contains(final Object o) {
    if (sourceToTarget == null)
      throw new UnsupportedOperationException();

    final int i$ = size();
    if (isRandomAccess()) {
      for (int i = 0; i < i$; ++i) { // [RA]
        final S e = (S)target.get(i);
        if (o.equals(sourceToTarget.apply(i, e)))
          return true;
      }
    }
    else {
      final Iterator<S> iterator = target.iterator();
      for (int i = 0; i < i$; ++i) // [I]
        if (o.equals(sourceToTarget.apply(i, iterator.next())))
          return true;
    }

    return false;
  }

  @Override
  public Iterator<T> iterator() {
    final Iterator<S> iterator = target.iterator();
    return new Iterator<T>() {
      private int index = -1;

      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public T next() {
        if (sourceToTarget == null)
          throw new UnsupportedOperationException();

        final S e = iterator.next();
        return sourceToTarget.apply(++index, e);
      }

      @Override
      public void remove() {
        iterator.remove();
        --index;
      }
    };
  }

  @Override
  public Object[] toArray() {
    final int i$ = size();
    final Object[] a = new Object[i$];
    toArray(a, i$);
    return a;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <E>E[] toArray(E[] a) {
    if (sourceToTarget == null)
      throw new UnsupportedOperationException();

    final int i$ = size();
    if (a.length < i$)
      a = (E[])Array.newInstance(a.getClass().getComponentType(), i$);

    toArray(a, i$);

    if (a.length > i$)
      a[i$] = null;

    return a;
  }

  @SuppressWarnings("unchecked")
  private void toArray(final Object[] a, final int i$) {
    if (isRandomAccess()) {
      for (int i = 0; i < i$; ++i) { // [RA]
        final S e = (S)target.get(i);
        a[i] = sourceToTarget.apply(i, e);
      }
    }
    else {
      final Iterator<S> iterator = target.iterator();
      for (int i = 0; i < i$; ++i) { // [I]
        final S e = iterator.next();
        a[i] = sourceToTarget.apply(i, e);
      }
    }
  }

  @Override
  public boolean add(final T e) {
    if (targetToSource == null)
      throw new UnsupportedOperationException();

    return target.add(targetToSource.apply(size(), e));
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean remove(final Object o) {
    if (sourceToTarget == null)
      throw new UnsupportedOperationException();

    final int i$ = size();
    if (isRandomAccess()) {
      for (int i = 0; i < i$; ++i) { // [RA]
        final S e = (S)target.get(i);
        final T t = sourceToTarget.apply(i, e);
        if (Objects.equals(o, t)) {
          target.remove(i);
          return true;
        }
      }
    }
    else {
      final Iterator<S> iterator = target.iterator();
      for (int i = 0; i < i$; ++i) { // [I]
        final S e = iterator.next();
        final T t = sourceToTarget.apply(i, e);
        if (Objects.equals(o, t)) {
          iterator.remove();
          return true;
        }
      }
    }

    return false;
  }

  @Override
  public boolean containsAll(final Collection<?> c) {
    if (c.size() == 0)
      return true;

    for (final Object e : c) // [C]
      if (contains(e))
        return true;

    return false;
  }

  @Override
  public boolean addAll(final Collection<? extends T> c) {
    boolean changed = false;
    for (final T e : c) // [C]
      changed |= add(e);

    return changed;
  }

  @Override
  public boolean removeAll(final Collection<?> c) {
    boolean changed = false;
    for (final Object e : c) // [C]
      changed |= remove(e);

    return changed;
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean retainAll(final Collection<?> c) {
    boolean changed = false;
    final int i$ = size();
    if (isRandomAccess()) {
      for (int i = 0; i < i$; ++i) { // [RA]
        final S e = (S)target.get(i);
        if (!c.contains(sourceToTarget.apply(i, e))) {
          target.remove(i);
          changed = true;
        }
      }
    }
    else {
      final Iterator<S> iterator = target.iterator();
      for (int i = 0; i < i$; ++i) { // [I]
        final S e = iterator.next();
        if (!c.contains(sourceToTarget.apply(i, e))) {
          iterator.remove();
          changed = true;
        }
      }
    }

    return changed;
  }

  @Override
  public boolean addAll(int index, final Collection<? extends T> c) {
    if (c.size() == 0)
      return false;

    for (final T e : c) // [C]
      add(index++, e);

    return true;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T get(final int index) {
    assertRange("index", index, "size()", size(), false);
    if (sourceToTarget == null)
      throw new UnsupportedOperationException();

    return sourceToTarget.apply(index, (S)target.get(index));
  }

  @Override
  @SuppressWarnings("unchecked")
  public T set(final int index, final T element) {
    assertRange("index", index, "size()", size(), false);
    if (sourceToTarget == null || targetToSource == null)
      throw new UnsupportedOperationException();

    return sourceToTarget.apply(index, (S)target.set(index, targetToSource.apply(index, element)));
  }

  @Override
  public void add(final int index, final T element) {
    assertRange("index", index, "size()", size(), true);
    if (targetToSource == null)
      throw new UnsupportedOperationException();

    target.add(index, targetToSource.apply(index, element));
  }

  @Override
  @SuppressWarnings("unchecked")
  public T remove(final int index) {
    assertRange("index", index, "size()", size(), false);
    if (sourceToTarget == null)
      throw new UnsupportedOperationException();

    return sourceToTarget.apply(index, (S)target.remove(index));
  }

  @Override
  @SuppressWarnings("unchecked")
  public int indexOf(final Object o) {
    if (targetToSource == null)
      throw new UnsupportedOperationException();

    return target.indexOf(targetToSource.apply(null, (T)o));
  }

  @Override
  @SuppressWarnings("unchecked")
  public int lastIndexOf(final Object o) {
    if (targetToSource == null)
      throw new UnsupportedOperationException();

    return target.lastIndexOf(targetToSource.apply(null, (T)o));
  }

  @Override
  public ListIterator<T> listIterator() {
    return listIterator(0);
  }

  @Override
  public ListIterator<T> listIterator(final int index) {
    assertRange("index", index, "size()", size(), true);
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

        return sourceToTarget.apply(previousIndex(), iterator.previous());
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

        iterator.set(targetToSource.apply(nextIndex() - 1, e));
      }

      @Override
      public void add(final T e) {
        if (targetToSource == null)
          throw new UnsupportedOperationException();

        iterator.add(targetToSource.apply(nextIndex(), e));
      }

      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public T next() {
        if (sourceToTarget == null)
          throw new UnsupportedOperationException();

        final int i = nextIndex();
        final S e = iterator.next();
        return sourceToTarget.apply(i, e);
      }

      @Override
      public void remove() {
        iterator.remove();
      }
    };
  }

  @Override
  public TransList<S,LS,T,LT> subList(final int fromIndex, final int toIndex) {
    assertRange("fromIndex", fromIndex, "toIndex", toIndex, "size()", size());
    return new TransList<>((LS)target.subList(fromIndex, toIndex), sourceToTarget, targetToSource);
  }

  @Override
  public void replaceAll(final UnaryOperator<T> operator) {
    superReplaceAll(operator);
  }

  @Override
  public void sort(final Comparator<? super T> c) {
    superSort(c);
  }

  @Override
  public void forEach(final Consumer<? super T> action) {
    superForEach(action);
  }

  @Override
  public boolean removeIf(final Predicate<? super T> filter) {
    return superRemoveIf(filter);
  }

  @Override
  public Spliterator<T> spliterator() {
    return superSpliterator();
  }

  @Override
  public Stream<T> stream() {
    return superStream();
  }

  @Override
  public Stream<T> parallelStream() {
    return superParallelStream();
  }

  @Override
  public boolean equals(final Object obj) {
    return superEquals(obj);
  }

  @Override
  public int hashCode() {
    return superHashCode();
  }

  @Override
  public String toString() {
    return superToString();
  }
}