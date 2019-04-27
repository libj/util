/* Copyright (c) 2017 OpenJAX
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

package org.openjax.ext.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Bidirectional map that maintains both key-&gt;value and value-&gt;key
 * mappings. This implementation utilizes the mechanisms of the
 * {@link ObservableMap} to guarantee operational symmetry between the
 * {@code this} map and the {@link #inverse()} map. Methods defined in the
 * {@code Map} interface that result in a mutation to the {@code this} instance
 * will also result in reflected operations to the {@link #inverse()} instance.
 * This implementation is not synchronized.
 *
 * @param <K> The type of keys maintained by this map.
 * @param <V> The type of mapped values.
 * @see ObservableMap
 * @see DelegateMap
 */
public abstract class BiMap<K,V> extends DelegateMap<K,V> {
  protected volatile BiMap<V,K> inverse;

  /**
   * Construct a new bidirectional map with the provided source maps.
   *
   * @param forward The forward source map.
   * @param reverse The reverse source map.
   */
  protected BiMap(final Map<K,V> forward, final Map<V,K> reverse) {
    inverse = newEmptyInverseMap();
    setTarget(forward);
    inverse.setTarget(reverse);
    inverse.inverse = this;
  }

  /**
   * Creates an empty instance.
   */
  protected BiMap() {
  }

  /**
   * Sets the specified map as the underlying target map of this {@code BiMap}.
   *
   * @param map The map to set at the underlying target of this {@code BiMap}.
   */
  protected void setTarget(final Map<K,V> map) {
    super.target = new ObservableMap<K,V>(map) {
      @Override
      protected boolean beforePut(final K key, final V oldValue, final V newValue) {
        ((ObservableMap<K,V>)BiMap.this.inverse.target).target.put(newValue, key);
        if (oldValue != null)
          ((ObservableMap<K,V>)BiMap.this.inverse.target).target.remove(oldValue);

        return true;
      }

      @Override
      protected void afterRemove(final Object key, final V value, final RuntimeException re) {
        ((ObservableMap<K,V>)BiMap.this.inverse.target).target.remove(value);
      }
    };
  }

  /**
   * Returns a new instance of an empty inverse subclass of {@code BiMap}.
   *
   * @return A new instance of an empty inverse {@code BiMap}.
   */
  protected abstract BiMap<V,K> newEmptyInverseMap();

  /**
   * Returns the inverse of this map, maintaining value-&gt;key mappings.
   * Mutations to the {@code inverse()} map are reflected in {@code this} map.
   *
   * @return The inverse map.
   */
  public Map<V,K> inverse() {
    return inverse;
  }

  @Override
  @SuppressWarnings("unlikely-arg-type")
  public boolean containsValue(final Object value) {
    return inverse.containsKey(value);
  }

  protected ObservableSet<K> keySet;

  @Override
  public Set<K> keySet() {
    return keySet == null ? keySet = new ObservableSet<K>(target.keySet()) {
      @Override
      @SuppressWarnings("unchecked")
      protected void afterRemove(final Object o, final RuntimeException re) {
        BiMap.this.inverse.target.remove(((Map.Entry<K,V>)o).getValue());
      }
    } : keySet;
  }

  protected ObservableCollection<V> values;

  @Override
  public Collection<V> values() {
    return values == null ? values = new ObservableCollection<V>(target.values()) {
      @Override
      protected void afterRemove(final Object o, final RuntimeException re) {
        BiMap.this.inverse.target.remove(o);
      }
    } : values;
  }

  protected volatile ObservableSet<Map.Entry<K,V>> entrySet;

  @Override
  public Set<Map.Entry<K,V>> entrySet() {
    return entrySet == null ? entrySet = new ObservableSet<Map.Entry<K,V>>(target.entrySet()) {
      @Override
      @SuppressWarnings("unchecked")
      protected void afterRemove(final Object o, final RuntimeException re) {
        BiMap.this.inverse.target.remove(((Map.Entry<K,V>)o).getValue());
      }
    } : entrySet;
  }
}