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

import java.util.List;
import java.util.function.Function;

import org.lib4j.lang.Classes;

public class MirroredList<E,M> extends ObservableList<E> {
  private MirroredList<M,E> mirroredList;
  private final Function<E,M> mirror;

  @SuppressWarnings("rawtypes")
  public MirroredList(final Class<? extends List> type, final Function<E,M> mirror1, final Function<M,E> mirror2) {
    super(Classes.newInstance(type));
    this.mirroredList = new MirroredList<M,E>(this, type, mirror2);
    this.mirror = mirror1;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private MirroredList(final MirroredList<M,E> mirroredList, final Class<? extends List> type, final Function<E,M> mirror) {
    super(Classes.newInstance((Class<? extends List<E>>)mirroredList.source.getClass()));
    this.mirroredList = mirroredList;
    this.mirror = mirror;
  }

  public MirroredList<M,E> getMirror() {
    return mirroredList;
  }

  @Override
  protected void beforeAdd(final int index, final E e) {
    ((List<M>)mirroredList.source).add(index, mirror.apply(e));
  }

  @Override
  protected void beforeRemove(final int index) {
    ((List<M>)mirroredList.source).remove(index);
  }

  @Override
  protected void beforeSet(final int index, final E oldElement) {
    ((List<M>)mirroredList.source).set(index, mirror.apply(get(index)));
  }
}