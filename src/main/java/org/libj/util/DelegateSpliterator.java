/* Copyright (c) 2019 LibJ
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

package org.libj.util;

import static org.libj.lang.Assertions.*;

import java.util.Comparator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * A {@link DelegateSpliterator} contains some other {@link Spliterator}, to
 * which it delegates its method calls, possibly transforming the data along the
 * way or providing additional functionality. The class
 * {@link DelegateSpliterator} itself simply overrides all methods of
 * {@link Spliterator} with versions that pass all requests to the target
 * {@link Spliterator}. Subclasses of {@link DelegateSpliterator} may further
 * override some of these methods and may also provide additional methods and
 * fields.
 *
 * @param <T> The type of elements returned by this {@link Spliterator}.
 */
public abstract class DelegateSpliterator<T> implements Spliterator<T> {
  /** The target {@link Spliterator}. */
  @SuppressWarnings("rawtypes")
  protected volatile Spliterator target;

  /**
   * Creates a new {@link DelegateSpliterator} with the specified target
   * {@link Spliterator}.
   *
   * @param target The target {@link Spliterator}.
   * @throws IllegalArgumentException If the target {@link Spliterator} is null.
   */
  public DelegateSpliterator(final Spliterator<T> target) {
    this.target = assertNotNull(target);
  }

  /**
   * Creates a new {@link DelegateSpliterator} with a null target.
   */
  protected DelegateSpliterator() {
  }

  @Override
  public boolean tryAdvance(final Consumer<? super T> action) {
    return target.tryAdvance(action);
  }

  @Override
  public Spliterator<T> trySplit() {
    return target.trySplit();
  }

  @Override
  public long estimateSize() {
    return target.estimateSize();
  }

  @Override
  public int characteristics() {
    return target.characteristics();
  }

  @Override
  public Comparator<? super T> getComparator() {
    return target.getComparator();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof DelegateSpliterator))
      return Objects.equals(target, obj);

    final DelegateSpliterator<?> that = (DelegateSpliterator<?>)obj;
    return Objects.equals(target, that.target);
  }

  @Override
  public int hashCode() {
    int hashCode = 1;
    if (target != null)
      hashCode = 31 * hashCode + target.hashCode();

    return hashCode;
  }

  @Override
  public String toString() {
    return String.valueOf(target);
  }
}