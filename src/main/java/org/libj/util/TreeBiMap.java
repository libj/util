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

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Bidirectional map ({@link BiMap}) backed by a {@link TreeMap}.
 *
 * @param <K> The type of keys maintained by this map.
 * @param <V> The type of mapped values.
 * @see BiMap
 * @see TreeMap
 */
public class TreeBiMap<K,V> extends BiMap<K,V> implements Cloneable, NavigableMap<K,V>, Serializable {
  /**
   * Constructs an empty {@link TreeBiMap} ordered according to the given comparator.
   *
   * @param keyComparator The comparator that will be used to order the forward map. If {@code null}, the {@linkplain Comparable
   *          natural ordering} of the keys will be used.
   * @param valueComparator The comparator that will be used to order the reverse map. If {@code null}, the {@linkplain Comparable
   *          natural ordering} of the keys will be used.
   */
  public TreeBiMap(final Comparator<? super K> keyComparator, final Comparator<? super V> valueComparator) {
    super(new TreeMap<>(keyComparator), new TreeMap<>(valueComparator));
  }

  /**
   * Constructs a new {@link TreeBiMap} with the same mappings as the specified {@link Map}.
   *
   * @param m The map whose mappings are to be placed in this map.
   * @throws NullPointerException If the specified map is null.
   */
  public TreeBiMap(final Map<? extends K,? extends V> m) {
    this();
    putAll(m);
  }

  /**
   * Constructs a new {@link TreeBiMap} with the same mappings as the specified {@link SortedMap}.
   *
   * @param m The map whose mappings are to be placed in this map.
   * @param valueComparator The comparator that will be used to order the reverse map. If {@code null}, the {@linkplain Comparable
   *          natural ordering} of the keys will be used.
   * @throws NullPointerException If the specified map is null.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public TreeBiMap(final SortedMap<? extends K,? extends V> m, final Comparator<? super V> valueComparator) {
    this((Comparator)m.comparator(), valueComparator);
    putAll(m);
  }

  /**
   * Constructs an empty {@link TreeBiMap}, using the natural ordering of its keys.
   */
  public TreeBiMap() {
    super(new TreeMap<>(), new TreeMap<>());
  }

  /**
   * Creates an empty instance.
   *
   * @param empty Ignored parameter.
   */
  protected TreeBiMap(final boolean empty) {
    super();
  }

  @Override
  protected BiMap<V,K> newEmptyReverseMap() {
    return new TreeBiMap<>(true);
  }

  private TreeMap<K,V> target() {
    return (TreeMap<K,V>)((ObservableMap<K,V>)target).target;
  }

  @Override
  public Comparator<? super K> comparator() {
    return target().comparator();
  }

  @Override
  public K firstKey() {
    return target().firstKey();
  }

  @Override
  public K lastKey() {
    return target().lastKey();
  }

  @Override
  public Entry<K,V> lowerEntry(final K key) {
    return target().lowerEntry(key);
  }

  @Override
  public K lowerKey(final K key) {
    return target().lowerKey(key);
  }

  @Override
  public Entry<K,V> floorEntry(final K key) {
    return target().floorEntry(key);
  }

  @Override
  public K floorKey(final K key) {
    return target().floorKey(key);
  }

  @Override
  public Entry<K,V> ceilingEntry(final K key) {
    return target().ceilingEntry(key);
  }

  @Override
  public K ceilingKey(final K key) {
    return target().ceilingKey(key);
  }

  @Override
  public Entry<K,V> higherEntry(final K key) {
    return target().higherEntry(key);
  }

  @Override
  public K higherKey(final K key) {
    return target().higherKey(key);
  }

  @Override
  public Entry<K,V> firstEntry() {
    return target().firstEntry();
  }

  @Override
  public Entry<K,V> lastEntry() {
    return target().lastEntry();
  }

  @Override
  public Entry<K,V> pollFirstEntry() {
    return target().pollFirstEntry();
  }

  @Override
  public Entry<K,V> pollLastEntry() {
    return target().pollLastEntry();
  }

  @Override
  public NavigableMap<K,V> descendingMap() {
    return target().descendingMap();
  }

  @Override
  public NavigableSet<K> navigableKeySet() {
    return target().navigableKeySet();
  }

  @Override
  public NavigableSet<K> descendingKeySet() {
    return target().descendingKeySet();
  }

  @Override
  public NavigableMap<K,V> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
    return target().subMap(fromKey, fromInclusive, toKey, toInclusive);
  }

  @Override
  public NavigableMap<K,V> headMap(final K toKey, final boolean inclusive) {
    return target().headMap(toKey, inclusive);
  }

  @Override
  public NavigableMap<K,V> tailMap(final K fromKey, final boolean inclusive) {
    return target().tailMap(fromKey, inclusive);
  }

  @Override
  public SortedMap<K,V> subMap(final K fromKey, final K toKey) {
    return target().subMap(fromKey, toKey);
  }

  @Override
  public SortedMap<K,V> headMap(final K toKey) {
    return target().headMap(toKey);
  }

  @Override
  public SortedMap<K,V> tailMap(final K fromKey) {
    return target().tailMap(fromKey);
  }

  @SuppressWarnings("unchecked")
  private TreeBiMap<K,V> superClone() {
    try {
      return (TreeBiMap<K,V>)super.clone();
    }
    catch (final CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public TreeBiMap<K,V> clone() {
    final TreeBiMap<K,V> clone = superClone();
    clone.reverse = ((TreeBiMap<V,K>)reverse).superClone();
    clone.setTarget((Map<K,V>)(((TreeMap<K,V>)((ObservableMap<K,V>)target).target).clone()));
    clone.reverse.setTarget((Map<V,K>)(((TreeMap<K,V>)((ObservableMap<K,V>)reverse.target).target).clone()));
    clone.reverse.reverse = clone;
    return clone;
  }
}