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

import java.util.ListIterator;

public abstract class PartialListIterator<E> implements ListIterator<E> {
  private final ListIterator<E> listIterator;

  public PartialListIterator(final ListIterator<E> listIterator) {
    this.listIterator = listIterator;
  }

  @Override
  public final boolean hasNext() {
    return listIterator.hasNext();
  }

  @Override
  public final E next() {
    return listIterator.next();
  }

  @Override
  public final boolean hasPrevious() {
    return listIterator.hasPrevious();
  }

  @Override
  public final E previous() {
    return listIterator.previous();
  }

  @Override
  public final int nextIndex() {
    return listIterator.nextIndex();
  }

  @Override
  public final int previousIndex() {
    return listIterator.previousIndex();
  }

  @Override
  public abstract void remove();

  @Override
  public abstract void set(final E e);

  @Override
  public abstract void add(final E e);
}
