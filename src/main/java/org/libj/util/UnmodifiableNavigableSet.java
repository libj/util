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

import java.util.Iterator;
import java.util.NavigableSet;
import java.util.SortedSet;

/**
 * This class allows modules to provide users with "read-only" access to internal navigable sets. Query operations on the returned
 * navigable set "read through" to the specified navigable set. Attempts to modify the returned navigable set, whether direct, via
 * its iterator, or via its {@link SortedSet#subSet(Object,Object)}, {@link SortedSet#headSet(Object)}, or
 * {@link SortedSet#tailSet(Object)} views, result in an {@link UnsupportedOperationException}.
 * <p>
 * The returned navigable set will be serializable if the specified navigable set is serializable.
 *
 * @param <E> The type parameter of the objects in the set.
 */
public class UnmodifiableNavigableSet<E> extends UnmodifiableSortedSet<E> implements NavigableSet<E> {
  /**
   * Creates a new {@link UnmodifiableNavigableSet} with the specified target {@link NavigableSet}.
   *
   * @param target The target {@link NavigableSet}.
   * @throws NullPointerException If the target {@link NavigableSet} is null.
   */
  public UnmodifiableNavigableSet(final NavigableSet<E> target) {
    super(target);
  }

  /**
   * Creates a new {@link UnmodifiableNavigableSet} with a null target.
   */
  protected UnmodifiableNavigableSet() {
  }

  @Override
  protected NavigableSet<E> getTarget() {
    return (NavigableSet<E>)super.getTarget();
  }

  @Override
  public E lower(final E e) {
    return getTarget().lower(e);
  }

  @Override
  public E floor(final E e) {
    return getTarget().floor(e);
  }

  @Override
  public E ceiling(final E e) {
    return getTarget().ceiling(e);
  }

  @Override
  public E higher(final E e) {
    return getTarget().higher(e);
  }

  @Override
  public E pollFirst() {
    throw new UnsupportedOperationException();
  }

  @Override
  public E pollLast() {
    throw new UnsupportedOperationException();
  }

  @Override
  public NavigableSet<E> descendingSet() {
    return new UnmodifiableNavigableSet<>(getTarget().descendingSet());
  }

  @Override
  public Iterator<E> descendingIterator() {
    return descendingSet().iterator();
  }

  @Override
  public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
    return new UnmodifiableNavigableSet<>(getTarget().subSet(fromElement, fromInclusive, toElement, toInclusive));
  }

  @Override
  public NavigableSet<E> headSet(final E toElement, final boolean inclusive) {
    return new UnmodifiableNavigableSet<>(getTarget().headSet(toElement, inclusive));
  }

  @Override
  public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive) {
    return new UnmodifiableNavigableSet<>(getTarget().tailSet(fromElement, inclusive));
  }
}