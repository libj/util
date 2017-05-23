/* Copyright (c) 2016 lib4j
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

package org.lib4j.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class PartialMap<K,V> implements Map<K,V>, Cloneable, Serializable {
  private static final long serialVersionUID = -2754766054459728078L;

  protected final Map<K,V> map;

  @SuppressWarnings("rawtypes")
  public PartialMap(final Class<? extends Map> type) {
    try {
      this.map = type.newInstance();
    }
    catch (final InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public abstract V put(final K key, final V value);

  @Override
  public final V putIfAbsent(final K key, final V value) {
    final V previous = get(key);
    if (previous == null)
      put(key, value);

    return previous;
  }

  @Override
  public final void putAll(final Map<? extends K,? extends V> m) {
    for (final Map.Entry<? extends K, ? extends V> entry : m.entrySet())
      put(entry.getKey(), entry.getValue());
  }

  @Override
  public abstract V remove(final Object key);

  @Override
  public final boolean replace(final K key, final V oldValue, final V newValue) {
    final V previous = get(key);
    if (previous == null || oldValue == null || !oldValue.equals(previous))
      return false;

    put(key, newValue);
    return true;
  }

  @Override
  public final V replace(final K key, final V value) {
    final V previous = get(key);
    if (previous == null)
      return null;

    put(key, value);
    return previous;
  }

  @Override
  public final void clear() {
    for (final K key : keySet())
      remove(key);
  }

  @Override
  public final V compute(final K key, final BiFunction<? super K,? super V,? extends V> remappingFunction) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final V computeIfAbsent(final K key, final Function<? super K,? extends V> mappingFunction) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final V computeIfPresent(final K key, final BiFunction<? super K,? super V,? extends V> remappingFunction) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final void forEach(final BiConsumer<? super K,? super V> action) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final V merge(final K key, final V value, final BiFunction<? super V,? super V,? extends V> remappingFunction) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final void replaceAll(final BiFunction<? super K,? super V,? extends V> function) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final int size() {
    return map.size();
  }

  @Override
  public final boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public final boolean containsKey(final Object key) {
    return map.containsKey(key);
  }

  @Override
  public final boolean containsValue(final Object value) {
    return map.containsValue(value);
  }

  @Override
  public final V get(final Object key) {
    return map.get(key);
  }

  @Override
  public final Set<K> keySet() {
    return map.keySet();
  }

  @Override
  public final Collection<V> values() {
    return map.values();
  }

  @Override
  public final Set<Map.Entry<K,V>> entrySet() {
    return map.entrySet();
  }

  @Override
  public abstract PartialMap<K,V> clone();
}