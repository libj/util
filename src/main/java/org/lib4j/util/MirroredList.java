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

public class MirroredList<E,M> extends PartialList<E> {
  public static interface Mirror<T,M> {
    public M reflect(final T value);
  }

  private static final long serialVersionUID = 9187704221643956539L;

  private final MirroredList<M,E> mirroredList;
  private final Mirror<E,M> mirror;

  @SuppressWarnings("rawtypes")
  public MirroredList(final Class<? extends List> type, final Mirror<E,M> mirror1, final Mirror<M,E> mirror2) {
    super(type);
    this.mirroredList = new MirroredList<M,E>(this, type, mirror2);
    this.mirror = mirror1;
  }

  @SuppressWarnings("rawtypes")
  private MirroredList(final MirroredList<M,E> mirroredList, final Class<? extends List> type, final Mirror<E,M> mirror) {
    super(type);
    this.mirroredList = mirroredList;
    this.mirror = mirror;
  }

  public MirroredList<M,E> getMirror() {
    return mirroredList;
  }

  @Override
  public void add(final int index, final E element) {
    mirroredList.list.add(index, mirror.reflect(element));
    list.add(index, element);
  }

  @Override
  public E remove(final int index) {
    mirroredList.list.remove(index);
    return list.remove(index);
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
        mirroredList.list.remove(index);
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
        mirroredList.list.remove(listIterator.nextIndex() - 1);
      }

      @Override
      public void set(final E e) {
        listIterator.set(e);
        mirroredList.list.remove(listIterator.nextIndex() - 1);
        mirroredList.list.add(listIterator.nextIndex() - 1, mirror.reflect(e));
      }

      @Override
      public void add(final E e) {
        listIterator.add(e);
        mirroredList.list.add(listIterator.nextIndex() - 1, mirror.reflect(e));
      }
    };
  }

  @Override
  public MirroredList<E,M> subList(final int fromIndex, final int toIndex) {
    final MirroredList<E,M> subList = new MirroredList<E,M>(list.getClass(), mirror, mirroredList.mirror);
    subList.addAll(list.subList(fromIndex, toIndex));
    return subList;
  }

  @Override
  public MirroredList<E,M> clone() {
    final MirroredList<E,M> clone = new MirroredList<E,M>(list.getClass(), mirror, mirroredList.mirror);
    clone.addAll(this);
    return clone;
  }
}