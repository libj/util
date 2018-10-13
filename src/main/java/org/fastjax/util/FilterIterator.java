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

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A {@code FilterIterator} contains some other {@code Iterator}, which it uses
 * as its basic source of data, possibly transforming the data along the way or
 * providing additional functionality. The class {@code FilterIterator} itself
 * simply overrides all methods of {@code Iterator} with versions that pass all
 * requests to the source {@code Iterator}. Subclasses of {@code FilterIterator}
 * may further override some of these methods and may also provide additional
 * methods and fields.
 */
public abstract class FilterIterator<E> implements Iterator<E> {
  /**
   * The Iterator to be filtered.
   */
  @SuppressWarnings("rawtypes")
  protected volatile Iterator source;

  /**
   * Creates a new {@code FilterIterator}.
   *
   * @param source The source {@code Iterator} object.
   * @throws NullPointerException If {@code source} is null.
   */
  public FilterIterator(final Iterator<E> source) {
    this.source = Objects.requireNonNull(source);
  }

  /**
   * Creates a new {@code FilterIterator} with a null source.
   */
  protected FilterIterator() {
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