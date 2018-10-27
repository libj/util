/* Copyright (c) 2016 FastJAX
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

package org.fastjax.util;

import java.util.List;
import java.util.function.Function;

public class MirrorList<E,M> extends ObservableList<E> {
  private final MirrorList<M,E> mirroredList;
  private final Function<E,M> mirror;

  public MirrorList(final List<E> list1, final List<M> list2, final Function<E,M> mirror1, final Function<M,E> mirror2) {
    super(list1);
    this.mirroredList = new MirrorList<>(this, list2, mirror2);
    this.mirror = mirror1;
  }

  private MirrorList(final MirrorList<M,E> mirroredList, final List<E> list2, final Function<E,M> mirror) {
    super(list2);
    this.mirroredList = mirroredList;
    this.mirror = mirror;
  }

  public MirrorList<M,E> getMirror() {
    return mirroredList;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected boolean beforeAdd(final int index, final E e) {
    ((List<M>)mirroredList.source).add(index, mirror.apply(e));
    return true;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected boolean beforeRemove(final int index) {
    ((List<M>)mirroredList.source).remove(index);
    return true;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected boolean beforeSet(final int index, final E oldElement) {
    ((List<M>)mirroredList.source).set(index, mirror.apply(get(index)));
    return true;
  }
}