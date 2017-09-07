/* Copyright (c) 2017 lib4j
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

package org.lib4j.util;

import java.util.Collection;
import java.util.List;

/**
 * List wrapper that guarantees sorted order of its members.
 */
public class SortedList<E extends Comparable<? super E>> extends WrappedList<E> implements List<E> {
  public SortedList(final List<E> list) {
    this(list, true);
  }

  private SortedList(final List<E> list, final boolean sort) {
    super(list);
    if (sort)
      Collections.sort(list);
  }

  @Override
  public boolean contains(final Object o) {
    return indexOf(o) > -1;
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean add(final E e) {
    return addUnsafe(Collections.binaryClosestSearch(source, e), e);
  }

  protected boolean addUnsafe(final int index, final E e) {
    final int size = source.size();
    source.add(index, e);
    return size != source.size();
  }

  @Override
  public boolean remove(final Object o) {
    final int index = indexOf(o);
    if (index < 0)
      return false;

    source.remove(index);
    return true;
  }

  @Override
  public boolean containsAll(final Collection<?> c) {
    for (final Object e : c)
      if (!contains(e))
        return false;

    return true;
  }

  @Override
  public boolean addAll(final Collection<? extends E> c) {
    boolean changed = false;
    for (final E e : c)
      changed = add(e) || changed;

    return changed;
  }

  @Override
  public boolean addAll(int index, final Collection<? extends E> c) {
    final int size = source.size();
    for (final E e : c)
      add(index++, e);

    return size != source.size();
  }

  @Override
  public boolean removeAll(final Collection<?> c) {
    boolean changed = false;
    for (final Object e : c)
      changed = remove(e) || changed;

    return changed;
  }

  /**
   * TODO: doc this!
   */
  @Override
  public E set(final int index, final E element) {
    final E oldElement = remove(index);
    add(element);
    return oldElement;
  }

  /**
   * TODO: doc this!
   */
  @Override
  public void add(final int index, final E element) {
    add(element);
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public int indexOf(final Object o) {
    return o instanceof Comparable ? Collections.binarySearch(source, (Comparable)o) : source.indexOf(o);
  }

  @Override
  public int lastIndexOf(final Object o) {
    int index = indexOf(o);
    if (index < 0)
      return index;

    while (source.get(++index).equals(o));
    return index;
  }
}