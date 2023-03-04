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
import java.util.SortedMap;

/**
 * An extensible alternative to {@link Collections#unmodifiableSortedMap(SortedMap)} that provides an unmodifiable view of an
 * underlying {@link SortedMap}.
 * <p>
 * This class allows modules to provide users with "read-only" access to internal sorted maps. Query operations on the returned
 * sorted map "read through" to the specified sorted map. Attempts to modify the returned sorted map, whether direct, via its
 * collection views, or via its {@link SortedMap#subMap(Object,Object)}, {@link SortedMap#headMap(Object)}, or
 * {@link SortedMap#tailMap(Object)} views, result in an {@link UnsupportedOperationException}.
 * <p>
 * The sorted map is serializable if the underlying sorted map is serializable.
 *
 * @param <K> The type parameter of the map keys.
 * @param <V> The type parameter of the map values.
 */
public class UnmodifiableSortedMap<K,V> extends UnmodifiableMap<K,V> implements SortedMap<K,V> {
  /**
   * Creates a new {@link UnmodifiableSortedMap} with the specified target {@link SortedMap}.
   *
   * @param target The target {@link SortedMap}.
   * @throws NullPointerException If the target {@link SortedMap} is null.
   */
  public UnmodifiableSortedMap(final SortedMap<K,? extends V> target) {
    super(target);
  }

  /**
   * Creates a new {@link UnmodifiableSortedMap} with a null target.
   */
  protected UnmodifiableSortedMap() {
  }

  @Override
  @SuppressWarnings("unchecked")
  protected SortedMap<K,? extends V> getTarget() {
    return (SortedMap<K,? extends V>)super.getTarget();
  }

  @Override
  public Comparator<? super K> comparator() {
    return getTarget().comparator();
  }

  @Override
  public SortedMap<K,V> subMap(final K fromKey, final K toKey) {
    return new UnmodifiableSortedMap<>(getTarget().subMap(fromKey, toKey));
  }

  @Override
  public SortedMap<K,V> headMap(final K toKey) {
    return new UnmodifiableSortedMap<>(getTarget().headMap(toKey));
  }

  @Override
  public SortedMap<K,V> tailMap(final K fromKey) {
    return new UnmodifiableSortedMap<>(getTarget().tailMap(fromKey));
  }

  @Override
  public K firstKey() {
    return getTarget().firstKey();
  }

  @Override
  public K lastKey() {
    return getTarget().lastKey();
  }
}