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
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedMap;

/**
 * An extensible alternative to {@link Collections#unmodifiableSortedMap(SortedMap)} that provides an unmodifiable view of an
 * underlying {@link SortedMap}.
 * <p>
 * This class allows modules to provide users with "read-only" access to internal navigable maps. Query operations on the returned
 * navigable map "read through" to the specified navigable map. Attempts to modify the returned navigable map, whether direct, via
 * its collection views, or via its {@link SortedMap#subMap(Object,Object)}, {@link SortedMap#headMap(Object)}, or
 * {@link SortedMap#tailMap(Object)} views, result in an {@link UnsupportedOperationException}.
 * <p>
 * The navigable map is serializable if the underlying navigable map is serializable.
 *
 * @param <K> The type parameter of the map keys.
 * @param <V> The type parameter of the map values.
 */
public class UnmodifiableNavigableMap<K,V> extends UnmodifiableSortedMap<K,V> implements NavigableMap<K,V> {
  /**
   * Creates a new {@link UnmodifiableNavigableMap} with the specified target {@link NavigableMap}.
   *
   * @param target The target {@link NavigableMap}.
   * @throws NullPointerException If the target {@link NavigableMap} is null.
   */
  public UnmodifiableNavigableMap(final NavigableMap<K,? extends V> target) {
    super(target);
  }

  /**
   * Creates a new {@link UnmodifiableNavigableMap} with a null target.
   */
  protected UnmodifiableNavigableMap() {
  }

  @Override
  protected NavigableMap<K,? extends V> getTarget() {
    return (NavigableMap<K,? extends V>)super.getTarget();
  }

  @Override
  public K lowerKey(final K key) {
    return getTarget().lowerKey(key);
  }

  @Override
  public K floorKey(final K key) {
    return getTarget().floorKey(key);
  }

  @Override
  public K ceilingKey(final K key) {
    return getTarget().ceilingKey(key);
  }

  @Override
  public K higherKey(final K key) {
    return getTarget().higherKey(key);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Entry<K,V> lowerEntry(final K key) {
    final Entry<K,V> lower = (Entry<K,V>)getTarget().lowerEntry(key);
    return lower != null ? new UnmodifiableEntrySet.UnmodifiableEntry<>(lower) : null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Entry<K,V> floorEntry(final K key) {
    final Entry<K,V> floor = (Entry<K,V>)getTarget().floorEntry(key);
    return floor != null ? new UnmodifiableEntrySet.UnmodifiableEntry<>(floor) : null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Entry<K,V> ceilingEntry(final K key) {
    final Entry<K,V> ceiling = (Entry<K,V>)getTarget().ceilingEntry(key);
    return ceiling != null ? new UnmodifiableEntrySet.UnmodifiableEntry<>(ceiling) : null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Entry<K,V> higherEntry(final K key) {
    final Entry<K,V> higher = (Entry<K,V>)getTarget().higherEntry(key);
    return higher != null ? new UnmodifiableEntrySet.UnmodifiableEntry<>(higher) : null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Entry<K,V> firstEntry() {
    final Entry<K,V> first = (Entry<K,V>)getTarget().firstEntry();
    return first != null ? new UnmodifiableEntrySet.UnmodifiableEntry<>(first) : null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Entry<K,V> lastEntry() {
    final Entry<K,V> last = (Entry<K,V>)getTarget().lastEntry();
    return last != null ? new UnmodifiableEntrySet.UnmodifiableEntry<>(last) : null;
  }

  @Override
  public Entry<K,V> pollFirstEntry() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Entry<K,V> pollLastEntry() {
    throw new UnsupportedOperationException();
  }

  @Override
  public NavigableMap<K,V> descendingMap() {
    return new UnmodifiableNavigableMap<>(getTarget().descendingMap());
  }

  @Override
  public NavigableSet<K> navigableKeySet() {
    return new UnmodifiableNavigableSet<>(getTarget().navigableKeySet());
  }

  @Override
  public NavigableSet<K> descendingKeySet() {
    return new UnmodifiableNavigableSet<>(getTarget().descendingKeySet());
  }

  @Override
  public NavigableMap<K,V> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
    return new UnmodifiableNavigableMap<>(getTarget().subMap(fromKey, fromInclusive, toKey, toInclusive));
  }

  @Override
  public NavigableMap<K,V> headMap(final K toKey, final boolean inclusive) {
    return new UnmodifiableNavigableMap<>(getTarget().headMap(toKey, inclusive));
  }

  @Override
  public NavigableMap<K,V> tailMap(final K fromKey, final boolean inclusive) {
    return new UnmodifiableNavigableMap<>(getTarget().tailMap(fromKey, inclusive));
  }
}