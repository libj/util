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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Bidirectional map that maintains both key-&gt;value and value-&gt;key
 * mappings. This implementation utilizes the mechanisms of the
 * {@link ObservableMap} to guarantee operational symmetry between the
 * {@code this} map and the {@link #reverse()} map. Methods defined in the
 * {@link Map} interface that result in a mutation to the {@code this} instance
 * will also result in reflected operations to the {@link #reverse()} instance.
 * This implementation is not synchronized.
 *
 * @param <K> The type of keys maintained by this map.
 * @param <V> The type of mapped values.
 * @see ObservableMap
 * @see DelegateMap
 */
public abstract class BiMap<K,V> extends DelegateMap<K,V> {
  protected volatile BiMap<V,K> reverse;

  /**
   * Construct a new bidirectional map with the provided source maps.
   *
   * @param forward The forward source map.
   * @param reverse The reverse source map.
   */
  protected BiMap(final Map<K,V> forward, final Map<V,K> reverse) {
    setTarget(forward);
    this.reverse = newEmptyReverseMap();
    this.reverse.setTarget(reverse);
    this.reverse.reverse = this;
  }

  /**
   * Creates an empty instance.
   */
  protected BiMap() {
  }

  /**
   * Sets the specified map as the underlying target map of this {@link BiMap}.
   *
   * @param map The map to set at the underlying target of this {@link BiMap}.
   */
  protected void setTarget(final Map<K,V> map) {
    super.target = new ObservableMap<K,V>(map) {
      @Override
      protected boolean beforePut(final K key, final V oldValue, final V newValue) {
        ((ObservableMap<K,V>)BiMap.this.reverse.target).target.put(newValue, key);
        if (oldValue != null)
          ((ObservableMap<K,V>)BiMap.this.reverse.target).target.remove(oldValue);

        return true;
      }

      @Override
      protected void afterRemove(final Object key, final V value, final RuntimeException re) {
        ((ObservableMap<K,V>)BiMap.this.reverse.target).target.remove(value);
      }
    };
  }

  /**
   * Returns a new instance of an empty reverse subclass of {@link BiMap}.
   *
   * @return A new instance of an empty reverse subclass of {@link BiMap}.
   */
  protected abstract BiMap<V,K> newEmptyReverseMap();

  /**
   * Returns the reverse of this map, maintaining value-&gt;key mappings.
   * Mutations to the {@link #reverse()} map are reflected in {@code this} map.
   *
   * @return The reverse map.
   */
  public Map<V,K> reverse() {
    return reverse;
  }

  @Override
  @SuppressWarnings("unlikely-arg-type")
  public boolean containsValue(final Object value) {
    return reverse.containsKey(value);
  }

  protected volatile ObservableSet<K> keySet;

  @Override
  public Set<K> keySet() {
    return keySet == null ? keySet = new ObservableSet<K>(target.keySet()) {
      private final ThreadLocal<V> localValue = new ThreadLocal<>();

      @Override
      protected boolean beforeAdd(final K e) {
        throw new UnsupportedOperationException();
      }

      @Override
      @SuppressWarnings("unlikely-arg-type")
      protected boolean beforeRemove(final Object e) {
        if (!BiMap.this.containsKey(e))
          return false;

        final V value = BiMap.this.get(e);
        localValue.set(value);
        return true;
      }

      @Override
      protected void afterRemove(final Object o, final RuntimeException re) {
        BiMap.this.reverse.target.remove(localValue.get());
      }
    } : keySet;
  }

  protected volatile ObservableCollection<V> values;

  @Override
  public Collection<V> values() {
    return values == null ? values = new ObservableCollection<V>(target.values()) {
      @Override
      protected boolean beforeAdd(final V e) {
        throw new UnsupportedOperationException();
      }

      @Override
      protected void afterRemove(final Object o, final RuntimeException re) {
        BiMap.this.reverse.target.remove(o);
      }
    } : values;
  }

  protected volatile ObservableSet<Map.Entry<K,V>> entrySet;

  @Override
  public Set<Map.Entry<K,V>> entrySet() {
    return entrySet == null ? entrySet = new ObservableSet<Map.Entry<K,V>>(target.entrySet()) {
      @Override
      protected boolean beforeAdd(final Entry<K,V> e) {
        throw new UnsupportedOperationException();
      }

      @Override
      @SuppressWarnings("unchecked")
      protected void afterRemove(final Object o, final RuntimeException re) {
        BiMap.this.reverse.target.remove(((Map.Entry<K,V>)o).getValue());
      }
    } : entrySet;
  }
}