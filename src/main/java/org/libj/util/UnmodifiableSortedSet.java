/* Copyright (c) 2022 LibJ
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

import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;

/**
 * An extensible alternative to {@link Collections#unmodifiableSortedSet(SortedSet)} that provides an unmodifiable view of an
 * underlying {@link SortedSet}.
 * <p>
 * This class allows modules to provide users with "read-only" access to internal sorted sets. Query operations on the returned
 * sorted set "read through" to the specified sorted set. Attempts to modify the returned sorted set, whether direct, via its
 * iterator, or via its {@link SortedSet#subSet(Object,Object)}, {@link SortedSet#headSet(Object)}, or
 * {@link SortedSet#tailSet(Object)} views, result in an {@link UnsupportedOperationException}.
 * <p>
 * The returned sorted set will be serializable if the specified sorted set is serializable.
 *
 * @param <E> The type parameter of objects in the set.
 */
public class UnmodifiableSortedSet<E> extends UnmodifiableSet<E> implements SortedSet<E> {
  /**
   * Creates a new {@link UnmodifiableSortedSet} with the specified target {@link SortedSet}.
   *
   * @param target The target {@link SortedSet}.
   * @throws IllegalArgumentException If the target {@link SortedSet} is null.
   */
  public UnmodifiableSortedSet(final SortedSet<E> target) {
    super(target);
  }

  /**
   * Creates a new {@link UnmodifiableSortedSet} with a null target.
   */
  protected UnmodifiableSortedSet() {
  }

  @Override
  @SuppressWarnings("unchecked")
  protected SortedSet<E> getTarget() {
    return (SortedSet<E>)super.getTarget();
  }

  @Override
  public Comparator<? super E> comparator() {
    return getTarget().comparator();
  }

  @Override
  public SortedSet<E> subSet(final E fromElement, final E toElement) {
    return new UnmodifiableSortedSet<>(getTarget().subSet(fromElement, toElement));
  }

  @Override
  public SortedSet<E> headSet(final E toElement) {
    return new UnmodifiableSortedSet<>(getTarget().headSet(toElement));
  }

  @Override
  public SortedSet<E> tailSet(final E fromElement) {
    return new UnmodifiableSortedSet<>(getTarget().tailSet(fromElement));
  }

  @Override
  public E first() {
    return getTarget().first();
  }

  @Override
  public E last() {
    return getTarget().last();
  }
}