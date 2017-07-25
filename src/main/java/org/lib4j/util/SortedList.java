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

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SortedList<E extends Comparable<? super E>> implements List<E>, Cloneable, Serializable {
  private static final long serialVersionUID = 2529942933588293260L;
  private final List<E> list;

  public SortedList(final List<E> list) {
    this(list, true);
  }

  private SortedList(final List<E> list, final boolean sort) {
    this.list = list;
    if (sort)
      Collections.sort(list);
  }

  @Override
  public int size() {
    return list.size();
  }

  @Override
  public boolean isEmpty() {
    return list.isEmpty();
  }

  @Override
  public boolean contains(final Object o) {
    return indexOf(o) > -1;
  }

  @Override
  public Iterator<E> iterator() {
    return list.iterator();
  }

  @Override
  public Object[] toArray() {
    return list.toArray();
  }

  @Override
  public <T>T[] toArray(final T[] a) {
    return list.toArray(a);
  }

  @Override
  public boolean add(final E e) {
    final int index = Collections.binaryClosestSearch(list, e);
    return addUnsafe(index, e);
  }

  protected boolean addUnsafe(final int index, final E e) {
    int size = list.size();
    list.add(index, e);
    return size != list.size();
  }

  @Override
  public boolean remove(final Object o) {
    final int index = indexOf(o);
    if (index < 0)
      return false;

    list.remove(index);
    return true;
  }

  @Override
  public boolean containsAll(final Collection<?> c) {
    for (final Object item : c)
      if (!contains(item))
        return false;

    return true;
  }

  @Override
  public boolean addAll(final Collection<? extends E> c) {
    boolean changed = false;
    for (final E item : c)
      changed = add(item) || changed;

    return changed;
  }

  @Override
  public boolean addAll(int index, final Collection<? extends E> c) {
    final int size = list.size();
    for (final E item : c)
      add(index++, item);

    return size != list.size();
  }

  @Override
  public boolean removeAll(final Collection<?> c) {
    boolean changed = false;
    for (final Object item : c)
      changed = remove(item) || changed;

    return changed;
  }

  @Override
  public boolean retainAll(final Collection<?> c) {
    return list.retainAll(c);
  }

  @Override
  public void clear() {
    list.clear();
  }

  @Override
  public E get(final int index) {
    return list.get(index);
  }

  @Override
  public E set(final int index, final E element) {
    final int correct = Collections.binaryClosestSearch(list, element);
    if (index != correct)
      throw new IllegalArgumentException("Incorrect index will corrupt sorted list [" + index + " != " + correct + "]");

    return list.set(index, element);
  }

  @Override
  public void add(final int index, final E element) {
    final int correct = Collections.binaryClosestSearch(list, element);
    if (index != correct)
      throw new IllegalArgumentException("Incorrect index will corrupt sorted list [" + index + " != " + correct + "]");

    addUnsafe(index, element);
  }

  @Override
  public E remove(final int index) {
    return list.remove(index);
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public int indexOf(final Object o) {
    return o instanceof Comparable ? Collections.binarySearch((List)list, (Comparable)o) : list.indexOf(o);
  }

  @Override
  public int lastIndexOf(final Object o) {
    int index = indexOf(o);
    if (index < 0)
      return index;

    while (list.get(++index).equals(o));
    return index;
  }

  @Override
  public ListIterator<E> listIterator() {
    return list.listIterator();
  }

  @Override
  public ListIterator<E> listIterator(final int index) {
    return list.listIterator(index);
  }

  @Override
  public List<E> subList(final int fromIndex, final int toIndex) {
    return list.subList(fromIndex, toIndex);
  }

  @Override
  protected SortedList<E> clone() {
    return new SortedList<E>(Collections.clone(list), false);
  }
}