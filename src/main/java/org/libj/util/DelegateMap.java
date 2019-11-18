/* Copyright (c) 2017 LibJ
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

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A {@link DelegateMap} contains some other {@link Map}, to which it delegates
 * its method calls, possibly transforming the data along the way or providing
 * additional functionality. The class {@link DelegateMap} itself simply
 * overrides all methods of {@link AbstractMap} with versions that pass all
 * requests to the target {@link Map}. Subclasses of {@link DelegateMap} may
 * further override some of these methods and may also provide additional
 * methods and fields.
 *
 * @param <K> The type of keys maintained by this map.
 * @param <V> The type of mapped values.
 */
public abstract class DelegateMap<K,V> extends AbstractMap<K,V> {
  /** The target Map. */
  @SuppressWarnings("rawtypes")
  protected volatile Map target;

  /**
   * Creates a new {@link DelegateMap} with the specified {@code target}.
   *
   * @param target The target {@link Map} object.
   * @throws NullPointerException If {@code target} is null.
   */
  public DelegateMap(final Map<K,V> target) {
    this.target = Objects.requireNonNull(target);
  }

  /**
   * Creates a new {@link DelegateMap} with a null target.
   */
  protected DelegateMap() {
  }

  @Override
  public int size() {
    return target.size();
  }

  @Override
  public boolean isEmpty() {
    return target.isEmpty();
  }

  @Override
  @SuppressWarnings("unchecked")
  public V get(final Object key) {
    return (V)target.get(key);
  }

  @Override
  public boolean containsKey(final Object key) {
    return target.containsKey(key);
  }

  @Override
  @SuppressWarnings("unchecked")
  public V put(final K key, final V value) {
    return (V)target.put(key, value);
  }

  @Override
  public void putAll(final Map<? extends K,? extends V> m) {
    target.putAll(m);
  }

  @Override
  @SuppressWarnings("unchecked")
  public V remove(final Object key) {
    return (V)target.remove(key);
  }

  @Override
  public void clear() {
    target.clear();
  }

  @Override
  public boolean containsValue(final Object value) {
    return target.containsKey(value);
  }

  @Override
  public Set<K> keySet() {
    return target.keySet();
  }

  @Override
  public Collection<V> values() {
    return target.values();
  }

  @Override
  public Set<Map.Entry<K,V>> entrySet() {
    return target.entrySet();
  }

  @Override
  @SuppressWarnings("unchecked")
  public V getOrDefault(final Object key, final V defaultValue) {
    return (V)target.getOrDefault(key, defaultValue);
  }

  @Override
  @SuppressWarnings("unchecked")
  public V putIfAbsent(final K key, final V value) {
    return (V)target.putIfAbsent(key, value);
  }

  @Override
  public boolean remove(final Object key, final Object value) {
    return target.remove(key, value);
  }

  @Override
  public boolean replace(final K key, final V oldValue, final V newValue) {
    return target.replace(key, oldValue, newValue);
  }

  @Override
  @SuppressWarnings("unchecked")
  public V replace(final K key, final V value) {
    return (V)target.replace(key, value);
  }

  @Override
  @SuppressWarnings("unchecked")
  public V computeIfAbsent(final K key, final Function<? super K,? extends V> mappingFunction) {
    return (V)target.computeIfAbsent(key, mappingFunction);
  }

  @Override
  @SuppressWarnings("unchecked")
  public V computeIfPresent(final K key, final BiFunction<? super K,? super V,? extends V> remappingFunction) {
    return (V)target.computeIfPresent(key, remappingFunction);
  }

  @Override
  @SuppressWarnings("unchecked")
  public V compute(final K key, final BiFunction<? super K,? super V,? extends V> remappingFunction) {
    return (V)target.compute(key, remappingFunction);
  }

  @Override
  @SuppressWarnings("unchecked")
  public V merge(final K key, final V value, final BiFunction<? super V,? super V,? extends V> remappingFunction) {
    return (V)target.merge(key, value, remappingFunction);
  }

  @Override
  public void forEach(final BiConsumer<? super K,? super V> action) {
    target.forEach(action);
  }

  @Override
  public void replaceAll(final BiFunction<? super K,? super V,? extends V> function) {
    target.replaceAll(function);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof DelegateMap))
      return false;

    final DelegateMap<?,?> that = (DelegateMap<?,?>)obj;
    return target != null ? target.equals(that.target) : that.target == null;
  }

  @Override
  public int hashCode() {
    return target == null ? 733 : target.hashCode();
  }

  @Override
  public String toString() {
    return String.valueOf(target);
  }
}