/* Copyright (c) 2016 lib4j
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

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DelegatedList<E> extends PartialList<E> {
  private static final long serialVersionUID = -2945838392443684891L;

  public interface ListDelegate<T> {
    public void add(final int index, final T element);
    public void remove(final int index);
  }

  private final ListDelegate<E> delegate;

  @SuppressWarnings("rawtypes")
  public DelegatedList(final Class<? extends List> type, final ListDelegate<E> delegate) {
    super(type);
    this.delegate = delegate;
  }

  @Override
  public void add(final int index, final E element) {
    list.add(index, element);
    if (delegate != null)
      delegate.add(index, element);
  }

  @Override
  public E remove(final int index) {
    final E item = list.remove(index);
    if (delegate != null)
      delegate.remove(index);

    return item;
  }

  @Override
  public PartialIterator<E> iterator() {
    final Iterator<E> iterator = list.iterator();
    return new PartialIterator<E>(iterator) {
      private int index = -1;

      @Override
      public E next() {
        final E item = iterator.next();
        ++index;
        return item;
      }

      @Override
      public void remove() {
        iterator.remove();
        delegate.remove(index);
      }
    };
  }

  @Override
  public PartialListIterator<E> listIterator(final int index) {
    final ListIterator<E> listIterator = list.listIterator(index);
    return new PartialListIterator<E>(listIterator) {
      @Override
      public void remove() {
        listIterator.remove();
        delegate.remove(listIterator.nextIndex() - 1);
      }

      @Override
      public void set(final E e) {
        listIterator.set(e);
        delegate.remove(listIterator.nextIndex() - 1);
        delegate.add(listIterator.nextIndex() - 1, e);
      }

      @Override
      public void add(final E e) {
        listIterator.add(e);
        delegate.add(listIterator.nextIndex() - 1, e);
      }
    };
  }

  @Override
  public DelegatedList<E> subList(final int fromIndex, final int toIndex) {
    final DelegatedList<E> subList = new DelegatedList<E>(list.getClass(), delegate);
    subList.addAll(list.subList(fromIndex, toIndex));
    return subList;
  }

  @Override
  public DelegatedList<E> clone() {
    final DelegatedList<E> clone = new DelegatedList<E>(this.list.getClass(), delegate);
    clone.addAll(this);
    return clone;
  }
}