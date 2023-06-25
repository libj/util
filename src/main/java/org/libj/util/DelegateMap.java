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
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A {@link DelegateMap} contains some other {@link Map}, to which it delegates its method calls, possibly transforming the data
 * along the way or providing additional functionality. The class {@link DelegateMap} itself simply overrides all methods of
 * {@link AbstractMap} with versions that pass all requests to the target {@link Map}. Subclasses of {@link DelegateMap} may further
 * override some of these methods and may also provide additional methods and fields.
 *
 * @param <K> The type of keys maintained by this map.
 * @param <V> The type of mapped values.
 */
public abstract class DelegateMap<K,V> extends AbstractMap<K,V> {
  /** The target {@link Map}. */
  @SuppressWarnings("rawtypes")
  protected Map target;

  /**
   * Creates a new {@link DelegateMap} with the specified target {@link Map}.
   *
   * @param target The target {@link Map}.
   * @throws NullPointerException If the target {@link Map} is null.
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

  /**
   * Protected method providing access to the default implementation of {@link Map#getOrDefault(Object,Object)}.
   *
   * @param key The key whose associated value is to be returned.
   * @param defaultValue The default mapping of the key.
   * @return The value to which the specified key is mapped, or {@code defaultValue} if this map contains no mapping for the key.
   * @throws ClassCastException If the key is of an inappropriate type for this map.
   * @throws NullPointerException If the specified key is null and this map does not permit null keys.
   */
  protected final V getOrDefault$(final Object key, final V defaultValue) {
    return super.getOrDefault(key, defaultValue);
  }

  @Override
  @SuppressWarnings("unchecked")
  public V getOrDefault(final Object key, final V defaultValue) {
    return (V)target.getOrDefault(key, defaultValue);
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

  /**
   * Protected method providing access to the default implementation of {@link Map#putIfAbsent(Object,Object)}.
   *
   * @param key Key with which the specified value is to be associated.
   * @param value Value to be associated with the specified key.
   * @return The previous value associated with the specified key, or {@code null} if there was no mapping for the key. (A
   *         {@code null} return can also indicate that the map previously associated {@code null} with the key, if the
   *         implementation supports null values.)
   * @throws UnsupportedOperationException If the {@code put} operation is not supported by this map.
   * @throws ClassCastException If the key or value is of an inappropriate type for this map.
   * @throws NullPointerException If the specified key or value is null, and this map does not permit null keys or values.
   * @throws IllegalArgumentException If some property of the specified key or value prevents it from being stored in this map.
   */
  protected final V putIfAbsent$(final K key, final V value) {
    return super.putIfAbsent(key, value);
  }

  @Override
  @SuppressWarnings("unchecked")
  public V putIfAbsent(final K key, final V value) {
    return (V)target.putIfAbsent(key, value);
  }

  /**
   * Protected method providing access to the default implementation of {@link Map#remove(Object,Object)}.
   *
   * @param key Key with which the specified value is associated.
   * @param value Value expected to be associated with the specified key.
   * @return {@code true} if the value was removed.
   * @throws UnsupportedOperationException If the {@code remove} operation is not supported by this map.
   * @throws ClassCastException If the key or value is of an inappropriate type for this map.
   * @throws NullPointerException If the specified key or value is null, and this map does not permit null keys or values.
   */
  protected final boolean remove$(final Object key, final Object value) {
    return super.remove(key, value);
  }

  @Override
  public boolean remove(final Object key, final Object value) {
    return target.remove(key, value);
  }

  /**
   * Protected method providing access to the default implementation of {@link Map#replace(Object,Object,Object)}.
   *
   * @param key Key with which the specified value is associated.
   * @param oldValue Value expected to be associated with the specified key.
   * @param newValue Value to be associated with the specified key.
   * @return {@code true} if the value was replaced.
   * @throws UnsupportedOperationException If the {@code put} operation is not supported by this map.
   * @throws ClassCastException If the class of a specified key or value prevents it from being stored in this map.
   * @throws NullPointerException If a specified key or newValue is null, and this map does not permit null keys or values.
   * @throws NullPointerException If oldValue is null and this map does not permit null values.
   * @throws IllegalArgumentException If some property of a specified key or value prevents it from being stored in this map.
   */
  protected final boolean replace$(final K key, final V oldValue, final V newValue) {
    return super.replace(key, oldValue, newValue);
  }

  @Override
  public boolean replace(final K key, final V oldValue, final V newValue) {
    return target.replace(key, oldValue, newValue);
  }

  /**
   * Protected method providing access to the default implementation of {@link Map#replace(Object,Object)}.
   *
   * @param key Key with which the specified value is associated.
   * @param value Value to be associated with the specified key.
   * @return The previous value associated with the specified key, or {@code null} if there was no mapping for the key. (A
   *         {@code null} return can also indicate that the map previously associated {@code null} with the key, if the
   *         implementation supports null values).
   * @throws UnsupportedOperationException If the {@code put} operation is not supported by this map.
   * @throws ClassCastException If the class of the specified key or value prevents it from being stored in this map.
   * @throws NullPointerException If the specified key or value is null, and this map does not permit null keys or values.
   * @throws IllegalArgumentException If some property of the specified key or value prevents it from being stored in this map.
   */
  protected final V replace$(final K key, final V value) {
    return super.replace(key, value);
  }

  @Override
  @SuppressWarnings("unchecked")
  public V replace(final K key, final V value) {
    return (V)target.replace(key, value);
  }

  /**
   * Protected method providing access to the default implementation of {@link Map#computeIfAbsent(Object,Function)}.
   *
   * @param key Key with which the specified value is to be associated.
   * @param mappingFunction The mapping function to compute a value.
   * @return The current (existing or computed) value associated with the specified key, or null if the computed value is null.
   * @throws NullPointerException If the specified key is null and this map does not support null keys, or the mappingFunction is
   *           null
   * @throws UnsupportedOperationException If the {@code put} operation is not supported by this map.
   * @throws ClassCastException If the class of the specified key or value prevents it from being stored in this map.
   * @throws IllegalArgumentException If some property of the specified key or value prevents it from being stored in this map.
   */
  protected final V computeIfAbsent$(final K key, final Function<? super K,? extends V> mappingFunction) {
    return super.computeIfAbsent(key, mappingFunction);
  }

  @Override
  @SuppressWarnings("unchecked")
  public V computeIfAbsent(final K key, final Function<? super K,? extends V> mappingFunction) {
    return (V)target.computeIfAbsent(key, mappingFunction);
  }

  /**
   * Protected method providing access to the default implementation of {@link Map#computeIfPresent(Object,BiFunction)}.
   *
   * @param key Key with which the specified value is to be associated.
   * @param remappingFunction The remapping function to compute a value.
   * @return The new value associated with the specified key, or null if none.
   * @throws NullPointerException If the specified key is null and this map does not support null keys, or the remappingFunction is
   *           null.
   * @throws UnsupportedOperationException If the {@code put} operation is not supported by this map.
   * @throws ClassCastException If the class of the specified key or value prevents it from being stored in this map.
   * @throws IllegalArgumentException If some property of the specified key or value prevents it from being stored in this map.
   */
  protected final V computeIfPresent$(final K key, final BiFunction<? super K,? super V,? extends V> remappingFunction) {
    return super.computeIfPresent(key, remappingFunction);
  }

  @Override
  @SuppressWarnings("unchecked")
  public V computeIfPresent(final K key, final BiFunction<? super K,? super V,? extends V> remappingFunction) {
    return (V)target.computeIfPresent(key, remappingFunction);
  }

  /**
   * Protected method providing access to the default implementation of {@link Map#compute(Object,BiFunction)}.
   *
   * @param key Key with which the specified value is to be associated.
   * @param remappingFunction The remapping function to compute a value.
   * @return The new value associated with the specified key, or null if none.
   * @throws NullPointerException If the specified key is null and this map does not support null keys, or the remappingFunction is
   *           null.
   * @throws UnsupportedOperationException If the {@code put} operation is not supported by this map.
   * @throws ClassCastException If the class of the specified key or value prevents it from being stored in this map.
   * @throws IllegalArgumentException If some property of the specified key or value prevents it from being stored in this map.
   */
  protected final V compute$(final K key, final BiFunction<? super K,? super V,? extends V> remappingFunction) {
    return super.compute(key, remappingFunction);
  }

  @Override
  @SuppressWarnings("unchecked")
  public V compute(final K key, final BiFunction<? super K,? super V,? extends V> remappingFunction) {
    return (V)target.compute(key, remappingFunction);
  }

  /**
   * Protected method providing access to the default implementation of {@link Map#merge(Object,Object,BiFunction)}.
   *
   * @param key Key with which the resulting value is to be associated.
   * @param value The non-null value to be merged with the existing value associated with the key or, if no existing value or a null
   *          value is associated with the key, to be associated with the key.
   * @param remappingFunction The remapping function to recompute a value if present.
   * @return The new value associated with the specified key, or null if no value is associated with the key.
   * @throws UnsupportedOperationException If the {@code put} operation is not supported by this map.
   * @throws ClassCastException If the class of the specified key or value prevents it from being stored in this map.
   * @throws NullPointerException If some property of the specified key or value prevents it from being stored in this map.
   * @throws IllegalArgumentException If the specified key is null and this map does not support null keys or the value or
   *           remappingFunction is null.
   */
  protected final V merge$(final K key, final V value, final BiFunction<? super V,? super V,? extends V> remappingFunction) {
    return super.merge(key, value, remappingFunction);
  }

  @Override
  @SuppressWarnings("unchecked")
  public V merge(final K key, final V value, final BiFunction<? super V,? super V,? extends V> remappingFunction) {
    return (V)target.merge(key, value, remappingFunction);
  }

  /**
   * Protected method providing access to the default implementation of {@link Map#forEach(BiConsumer)}.
   *
   * @param action The action to be performed for each entry.
   * @throws NullPointerException If the specified action is null.
   * @throws ConcurrentModificationException If an entry is found to be.
   */
  protected final void forEach$(final BiConsumer<? super K,? super V> action) {
    super.forEach(action);
  }

  @Override
  public void forEach(final BiConsumer<? super K,? super V> action) {
    target.forEach(action);
  }

  /**
   * Protected method providing access to the default implementation of {@link Map#replaceAll(BiFunction)}.
   *
   * @param function The function to apply to each entry.
   * @throws UnsupportedOperationException If the {@code set} operation is not supported by this map's entry set iterator.
   * @throws ClassCastException If the class of a replacement value prevents it from being stored in this map.
   * @throws NullPointerException If the specified function is null, or the specified replacement value is null, and this map does
   *           not permit null values.
   * @throws ClassCastException If a replacement value is of an inappropriate type for this map.
   * @throws NullPointerException If function or a replacement value is null, and this map does not permit null keys or values.
   * @throws IllegalArgumentException If some property of a replacement value prevents it from being stored in this map.
   * @throws ConcurrentModificationException If an entry is found to be removed during iteration.
   */
  protected final void replaceAll$(final BiFunction<? super K,? super V,? extends V> function) {
    super.replaceAll(function);
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
      return Objects.equals(target, obj);

    final DelegateMap<?,?> that = (DelegateMap<?,?>)obj;
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