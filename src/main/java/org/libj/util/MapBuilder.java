/* Copyright (c) 2024 LibJ
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

import java.util.Map;

/**
 * Class providing the builder pattern for the population of key/value pairs into arbitrary {@link Map}s.
 *
 * @param <K> The type of keys maintained by the map.
 * @param <V> The type of mapped values in the map.
 * @param <M> The type of the map.
 */
public class MapBuilder<K,V,M extends Map<K,V>> {
  /**
   * Associates the specified value with the specified key in the target map, and returns the target map.
   *
   * @param <K> The type of keys maintained by the map.
   * @param <V> The type of mapped values in the map.
   * @param <M> The type of the map.
   * @param map The target {@link Map}.
   * @param key Key with which the specified value is to be associated in the target map of this {@link MapBuilder}.
   * @param value Value to be associated with the specified key in the target map of this {@link MapBuilder}.
   * @return The target {@link Map}.
   * @throws UnsupportedOperationException if the {@link Map#put} operation is not supported by the target map of this
   *           {@link MapBuilder}.
   * @throws ClassCastException If the class of the specified key or value prevents it from being stored in the target map of this
   *           {@link MapBuilder}.
   * @throws NullPointerException If the specified key or value is null and the target map of this {@link MapBuilder} does not permit
   *           null keys or values.
   * @throws IllegalArgumentException If some property of the specified key or value prevents it from being stored in the target map
   *           of this {@link MapBuilder}.
   * @throws NullPointerException If the target map of this {@link MapBuilder} is null.
   * @see Map#put(Object,Object)
   */
  public static <K,V,M extends Map<K,V>> M put(final M map, final K key, final V value) {
    map.put(key, value);
    return map;
  }

  /**
   * Associates the specified variable array of alternating key and value arguments in the target map, and returns the target map.
   *
   * @param <K> The type of keys maintained by the map.
   * @param <V> The type of mapped values in the map.
   * @param <M> The type of the map.
   * @param map The target {@link Map}.
   * @param keyValue Variable array of alternating key and value arguments.
   * @return The target {@link Map}.
   * @throws UnsupportedOperationException if the {@link Map#put} operation is not supported by the target map of this
   *           {@link MapBuilder}.
   * @throws ClassCastException If the class of the specified key or value prevents it from being stored in the target map of this
   *           {@link MapBuilder}.
   * @throws NullPointerException If the specified key or value is null and the target map of this {@link MapBuilder} does not permit
   *           null keys or values.
   * @throws IllegalArgumentException If the number of provided key and value arguments is not even, or if some property of the
   *           specified key or value prevents it from being stored in the target map of this {@link MapBuilder}.
   * @throws NullPointerException If the target map of this {@link MapBuilder} is null.
   * @see Map#put(Object,Object)
   */
  @SuppressWarnings("unchecked")
  public static <K,V,M extends Map<K,V>> M put(final M map, final Object ... keyValue) {
    final Map m = map;
    final int len = keyValue.length;
    if (len % 2 != 0)
      throw new IllegalArgumentException("Expected an even number of alternating key and value arguments");

    for (int i = 0; i < len; m.put(keyValue[i++], keyValue[i++])); // [A]
    return map;
  }

  private final M map;

  /**
   * Creates a new {@link MapBuilder} with the provided {@link Map} as the target map of the builder pattern.
   *
   * @param map The target {@link Map} of the builder pattern.
   */
  public MapBuilder(final M map) {
    this.map = map;
  }

  /**
   * Associates the specified value with the specified key in the target map of this {@link MapBuilder}, and returns {@code this}
   * {@link MapBuilder}.
   *
   * @param key Key with which the specified value is to be associated in the target map of this {@link MapBuilder}.
   * @param value Value to be associated with the specified key in the target map of this {@link MapBuilder}.
   * @return {@code this} {@link MapBuilder}.
   * @throws UnsupportedOperationException if the {@link Map#put} operation is not supported by the target map of this
   *           {@link MapBuilder}.
   * @throws ClassCastException If the class of the specified key or value prevents it from being stored in the target map of this
   *           {@link MapBuilder}.
   * @throws NullPointerException If the specified key or value is null and the target map of this {@link MapBuilder} does not permit
   *           null keys or values.
   * @throws IllegalArgumentException If some property of the specified key or value prevents it from being stored in the target map
   *           of this {@link MapBuilder}.
   * @throws NullPointerException If the target map of this {@link MapBuilder} is null.
   * @see Map#put(Object,Object)
   */
  public MapBuilder<K,V,M> put(final K key, final V value) {
    map.put(key, value);
    return this;
  }

  /**
   * Copies all of the mappings from the specified map to the target map of this {@link MapBuilder}, and returns {@code this}
   * {@link MapBuilder}. The effect of this call is equivalent to that of calling {@link #put(Object,Object) put(k, v)} on this
   * {@link MapBuilder} once for each mapping from key {@code k} to value {@code v} in the specified map. The behavior of this
   * operation is undefined if the specified map is modified while the operation is in progress.
   *
   * @return {@code this} {@link MapBuilder}.
   * @param m Mappings to be stored in the target map of this {@link MapBuilder}.
   * @throws UnsupportedOperationException If the {@link Map#putAll} operation is not supported by the target map of this
   *           {@link MapBuilder}.
   * @throws ClassCastException If the class of a key or value in the specified map prevents it from being stored in the target map of
   *           this {@link MapBuilder}.
   * @throws NullPointerException If the specified map is null, or if the target map of this {@link MapBuilder} does not permit null
   *           keys or values, and the specified map contains null keys or values.
   * @throws IllegalArgumentException If some property of a key or value in the specified map prevents it from being stored in the
   *           target map of this {@link MapBuilder}.
   * @throws NullPointerException If the target map of this {@link MapBuilder} is null.
   * @see Map#putAll(Map)
   */
  public MapBuilder<K,V,M> putAll(final Map<? extends K,? extends V> m) {
    map.putAll(m);
    return this;
  }

  /**
   * If the specified key is not already associated with a value in the target map of this {@link MapBuilder} (or is mapped to
   * {@code null}), this method associates the specified key with the given value, and returns {@code this} {@link MapBuilder}.
   *
   * @param key Key with which the specified value is to be associated in the target map of this {@link MapBuilder}.
   * @param value Value to be associated with the specified key in the target map of this {@link MapBuilder}.
   * @return {@code this} {@link MapBuilder}.
   * @throws UnsupportedOperationException If the {@link Map#put} operation is not supported by the target map of this
   *           {@link MapBuilder}.
   * @throws ClassCastException If the key or value is of an inappropriate type for the target map of this {@link MapBuilder}.
   * @throws NullPointerException If the specified key or value is null, and the target map of this {@link MapBuilder} does not permit
   *           null keys or values.
   * @throws IllegalArgumentException If some property of the specified key or value prevents it from being stored in the target map
   *           of this {@link MapBuilder}.
   * @throws NullPointerException If the target map of this {@link MapBuilder} is null.
   * @see Map#putIfAbsent(Object,Object)
   */
  public MapBuilder<K,V,M> putIfAbsent(final K key, final V value) {
    map.putIfAbsent(key, value);
    return this;
  }

  public M build() {
    return map;
  }
}