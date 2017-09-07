/* Copyright (c) 2017 lib4j
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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Bidirectional map that maintains both key-&gt;value and value-&gt;key
 * mappings. This implementation utilizes the mechanisms of the
 * <code>ObservableMap</code> to guarantee operational symmetry between the
 * <code>this</code> map, and the <code>this.inverse()</code> map. Methods
 * defined in the <code>Map</code> interface that result in a mutation to
 * the <code>this</code> instance result in reflected operations to the
 * <code>this.inverse()</code> instance. This implementation is not
 * synchronized.
 *
 * @see ObservableMap
 * @see WrappedMap
 */
public abstract class BiMap<K,V> extends WrappedMap<K,V> {
  protected BiMap<V,K> inverse;

  /**
   * Construct a new bidirectional map with the provided source maps.
   *
   * @param forward The forward source map.
   * @param reverse The reverse source map.
   */
  protected BiMap(final Map<K,V> forward, final Map<V,K> reverse) {
    inverse = newEmptyInverseMap();
    init(forward);
    inverse.init(reverse);
    inverse.inverse = this;
  }

  protected void init(final Map<K,V> map) {
    super.source = new ObservableMap<K,V>(map) {
      @Override
      protected void beforePut(final K key, final V oldValue, final V newValue) {
        ((ObservableMap<K,V>)BiMap.this.inverse.source).source.put(newValue, key);
        if (oldValue != null)
          ((ObservableMap<K,V>)BiMap.this.inverse.source).source.remove(oldValue);
      }

      @Override
      protected void afterRemove(final Object key, final V value) {
        ((ObservableMap<K,V>)BiMap.this.inverse.source).source.remove(value);
      }
    };
  }

  /**
   * @return A new instance of an empty inverse <code>BiMap</code>.
   */
  protected abstract BiMap<V,K> newEmptyInverseMap();

  /**
   * Constructor intended to be used to instantiate an empty instance.
   *
   * @param empty Ignored parameter.
   */
  protected BiMap(final boolean empty) {
  }

  /**
   * Return the inverse of this map, maintaining value-&gt;key mappings.
   * Mutations to the <code>inverse()</code> map are reflected in <code>this</code> map.
   *
   * @return The inverse map.
   */
  public Map<V,K> inverse() {
    return inverse;
  }

  @Override
  public boolean containsValue(final Object value) {
    return inverse.containsKey(value);
  }

  protected ObservableSet<K> keySet;

  @Override
  public Set<K> keySet() {
    return keySet == null ? keySet = new ObservableSet<K>(source.keySet()) {
      @Override
      @SuppressWarnings("unchecked")
      protected void afterRemove(final Object o) {
        BiMap.this.inverse.source.remove(((Map.Entry<K,V>)o).getValue());
      }
    } : keySet;
  }

  protected ObservableCollection<V> values;

  @Override
  public Collection<V> values() {
    return values == null ? values = new ObservableCollection<V>(source.values()) {
      @Override
      protected void afterRemove(final Object o) {
        BiMap.this.inverse.source.remove(o);
      }
    } : values;
  }

  protected ObservableSet<Map.Entry<K,V>> entrySet;

  @Override
  public Set<Map.Entry<K,V>> entrySet() {
    return entrySet == null ? entrySet = new ObservableSet<Map.Entry<K,V>>(source.entrySet()) {
      @Override
      @SuppressWarnings("unchecked")
      protected void afterRemove(final Object o) {
        BiMap.this.inverse.source.remove(((Map.Entry<K,V>)o).getValue());
      }
    } : entrySet;
  }
}