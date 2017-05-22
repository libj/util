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

package org.safris.commons.util;

import java.util.List;

public class MirroredDelegatedList<T,M> extends DelegatedList<T> {
  public static interface Mirror<T,M> {
    public M reflect(final T value);
  }

  private static final long serialVersionUID = 9187704221643956539L;

  private final MirroredDelegatedList<M,T> mirroredList;
  private final Mirror<T,M> mirror;

  @SuppressWarnings("rawtypes")
  public MirroredDelegatedList(final Class<? extends List> type, final Mirror<T,M> mirror1, final DelegatedList.ListDelegate<T> delegate1, final Mirror<M,T> mirror2, final DelegatedList.ListDelegate<M> delegate2) {
    super(type, delegate1);
    this.mirroredList = new MirroredDelegatedList<M,T>(this, mirror2, delegate2);
    this.mirror = mirror1;
  }

  private MirroredDelegatedList(final MirroredDelegatedList<M,T> mirroredList, final Mirror<T,M> mirror, final DelegatedList.ListDelegate<T> delegate2) {
    super(mirroredList.list.getClass(), delegate2);
    this.mirroredList = mirroredList;
    this.mirror = mirror;
  }

  public MirroredDelegatedList<M,T> getMirror() {
    return mirroredList;
  }

  private void superAdd(final int index, final T element) {
    super.add(index, element);
  }

  @Override
  public void add(final int index, final T element) {
    mirroredList.superAdd(index, mirror.reflect(element));
    super.add(index, element);
  }

  private T superRemove(final int index) {
    return super.remove(index);
  }

  @Override
  public T remove(final int index) {
    mirroredList.superRemove(index);
    return super.remove(index);
  }
}