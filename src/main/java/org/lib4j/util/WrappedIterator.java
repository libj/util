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
import java.util.function.Consumer;

/**
 * Wrapper class for <code>Iterator</code> interface that delegates all
 * methods to the wrapped instance.
 *
 * @see Iterator
 */
public class WrappedIterator<E> implements Iterator<E> {
  @SuppressWarnings("rawtypes")
  protected Iterator source;

  public WrappedIterator(final Iterator<E> iterator) {
    this.source = iterator;
  }

  @Override
  public final boolean hasNext() {
    return source.hasNext();
  }

  @Override
  @SuppressWarnings("unchecked")
  public E next() {
    return (E)source.next();
  }

  @Override
  public void remove() {
    source.remove();
  }

  @Override
  public void forEachRemaining(final Consumer<? super E> action) {
    source.forEachRemaining(action);
  }
}