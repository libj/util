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

import java.io.Serializable;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Bidirectional map (<code>BiMap</code>) backed by a <code>IdentityHashMap</code>.
 *
 * @see BiMap
 * @see IdentityHashMap
 */
public class IdentityHashBiMap<K,V> extends BiMap<K,V> implements Cloneable, Serializable {
  private static final long serialVersionUID = 2588014751081881163L;

  /**
   * Constructs a new, empty map with the specified expected maximum size.
   * Putting more than the expected number of key-value mappings into
   * the map may cause the internal data structure to grow, which may be
   * somewhat time-consuming.
   *
   * @param expectedMaxSize the expected maximum size of the map
   * @throws IllegalArgumentException if <tt>expectedMaxSize</tt> is negative
   * @see IdentityHashMap#IdentityHashMap(int)
   */
  public IdentityHashBiMap(final int expectedMaxSize) {
    super(new IdentityHashMap<K,V>(expectedMaxSize), new IdentityHashMap<V,K>(expectedMaxSize));
  }

  /**
   * Constructs a new bidirectional identity hash map containing the
   * keys-value mappings in the specified map.
   *
   * @param m the map whose mappings are to be placed into this map
   * @throws NullPointerException if the specified map is null
   * @see IdentityHashMap#IdentityHashMap(Map)
   */
  public IdentityHashBiMap(final Map<? extends K,? extends V> m) {
    this();
    putAll(m);
  }

  /**
   * Constructs a new, empty bidirectional identity hash map with a default
   * expected maximum size (21).
   */
  public IdentityHashBiMap() {
    super(new IdentityHashMap<K,V>(), new IdentityHashMap<V,K>());
  }

  /**
   * Constructor intended to be used to instantiate an empty instance.
   *
   * @param empty Ignored parameter.
   */
  protected IdentityHashBiMap(final boolean empty) {
    super(empty);
  }

  @Override
  protected BiMap<V,K> newEmptyInverseMap() {
    return new IdentityHashBiMap<V,K>(true);
  }


  @Override
  public Set<Map.Entry<K,V>> entrySet() {
    return entrySet == null ? entrySet = new ObservableSet<Map.Entry<K,V>>(source.entrySet()) {
      final ThreadLocal<V> value = new ThreadLocal<V>();

      @Override
      @SuppressWarnings("unchecked")
      protected void beforeRemove(final Object e) {
        value.set(((Map.Entry<K,V>)e).getValue());
      }

      @Override
      protected void afterRemove(final Object o) {
        IdentityHashBiMap.this.inverse.source.remove(value.get());
      }
    } : entrySet;
  }

  @SuppressWarnings("unchecked")
  private IdentityHashBiMap<K,V> superClone() {
    try {
      return (IdentityHashBiMap<K,V>)super.clone();
    }
    catch (final CloneNotSupportedException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public IdentityHashBiMap<K,V> clone() {
    final IdentityHashBiMap<K,V> clone = superClone();
    clone.entrySet = null;
    clone.keySet = null;
    clone.values = null;
    clone.inverse = ((IdentityHashBiMap<V,K>)inverse).superClone();
    clone.init((Map<K,V>)(((IdentityHashMap<K,V>)((ObservableMap<K,V>)source).source).clone()));
    clone.inverse.init((Map<V,K>)(((IdentityHashMap<K,V>)((ObservableMap<K,V>)inverse.source).source).clone()));
    clone.inverse.inverse = clone;
    return clone;
  }
}