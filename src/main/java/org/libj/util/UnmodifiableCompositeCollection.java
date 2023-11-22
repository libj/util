/* Copyright (c) 2023 LibJ
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
import java.util.Iterator;
import java.util.NoSuchElementException;

public class UnmodifiableCompositeCollection<E> implements Collection<E> {
  private final Collection<? extends E>[] targets;

  @SafeVarargs
  public UnmodifiableCompositeCollection(final Collection<? extends E> ... targets) {
    this.targets = assertNotNull(targets);
  }

  protected UnmodifiableCompositeCollection() {
    this.targets = null;
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
  public boolean contains(final Object o) {
    for (final Collection<? extends E> target : targets) // [A]
      if (target.contains(o))
        return true;

    return false;
  }

  @Override
  public boolean containsAll(final Collection<?> c) {
    if (c.size() > 0)
      for (final Object o : c) // [C]
        if (!contains(o))
          return false;

    return true;
  }

  @Override
  public boolean remove(final Object o) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean retainAll(final Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeAll(final Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int size() {
    int size = 0;
    for (final Collection<? extends E> target : targets) // [A]
      size += target.size();

    return size;
  }

  @Override
  public boolean isEmpty() {
    for (final Collection<? extends E> target : targets) // [A]
      if (!target.isEmpty())
        return false;

    return true;
  }

  @Override
  public Iterator<E> iterator() {
    final int length = targets.length;
    if (length == 0)
      return Iterators.empty();

    return new Iterator<E>() {
      private int index = 0;
      private Iterator<? extends E> iterator = targets[0].iterator();

      @Override
      public boolean hasNext() {
        while (!iterator.hasNext()) {
          if (++index >= length)
            return false;

          iterator = targets[index].iterator();
        }

        return true;
      }

      @Override
      public E next() {
        if (hasNext())
          return iterator.next();

        throw new NoSuchElementException();
      }
    };
  }

  @Override
  public Object[] toArray() {
    int pos = size();
    final Object[] a = new Object[pos];
    for (int i = targets.length - 1; i >= 0; --i) { // [A]
      final Collection<? extends E> target = targets[i];
      final int size = target.size();
      if (size == 0)
        continue;

      pos -= size;
      if (pos > 0) {
        target.toArray(a);
        System.arraycopy(a, 0, a, pos, size);
      }
      else if (size < a.length) {
        final Object last = a[size];
        target.toArray(a);
        a[size] = last;
      }
      else {
        target.toArray(a);
      }
    }

    return a;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T[] toArray(T[] a) {
    int pos = size();
    final int length = a.length;
    if (length < pos)
      a = (T[])Array.newInstance(a.getClass().getComponentType(), pos);
    else if (length >= pos)
      a[pos] = null;

    for (int i = targets.length - 1; i >= 0; --i) { // [A]
      final Collection<? extends E> target = targets[i];
      final int size = target.size();
      if (size == 0)
        continue;

      pos -= size;
      if (pos > 0) {
        target.toArray(a);
        System.arraycopy(a, 0, a, pos, size);
      }
      else if (size < a.length) {
        final T last = a[size];
        target.toArray(a);
        a[size] = last;
      }
      else {
        target.toArray(a);
      }
    }

    return a;
  }
}