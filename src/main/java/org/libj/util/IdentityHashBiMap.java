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

import static org.libj.lang.Assertions.*;

import java.io.Serializable;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Bidirectional map ({@link BiMap}) backed by a {@link IdentityHashMap}.
 *
 * @param <K> The type of keys maintained by this map.
 * @param <V> The type of mapped values.
 * @see BiMap
 * @see IdentityHashMap
 */
public class IdentityHashBiMap<K,V> extends BiMap<K,V> implements Cloneable, Serializable {
  /**
   * Constructs a new, empty map with the specified expected maximum size. Putting more than the expected number of key-value
   * mappings into the map may cause the internal data structure to grow, which may be somewhat time-consuming.
   *
   * @param expectedMaxSize The expected maximum size of the map.
   * @throws IllegalArgumentException If {@code expectedMaxSize} is negative.
   */
  public IdentityHashBiMap(final int expectedMaxSize) {
    super(new IdentityHashMap<>(expectedMaxSize), new IdentityHashMap<>(expectedMaxSize));
  }

  /**
   * Constructs a new bidirectional identity hash map containing the keys-value mappings in the specified map.
   *
   * @param m The map whose mappings are to be placed into this map.
   * @throws IllegalArgumentException If the specified map is null.
   */
  public IdentityHashBiMap(final Map<? extends K,? extends V> m) {
    this();
    putAll(assertNotNull(m));
  }

  /**
   * Constructs a new, empty bidirectional identity hash map with a default expected maximum size (21).
   */
  public IdentityHashBiMap() {
    super(new IdentityHashMap<>(), new IdentityHashMap<>());
  }

  /**
   * Creates an empty instance.
   *
   * @param empty Ignored parameter.
   */
  protected IdentityHashBiMap(final boolean empty) {
    super();
  }

  @Override
  protected BiMap<V,K> newEmptyReverseMap() {
    return new IdentityHashBiMap<>(true);
  }

  @Override
  public Set<Map.Entry<K,V>> entrySet() {
    return entrySet == null ? entrySet = new ObservableSet<Map.Entry<K,V>>(target.entrySet()) {
      final ThreadLocal<V> value = new ThreadLocal<>();

      @Override
      protected Object beforeAdd(final Entry<K,V> element, final Object preventDefault) {
        throw new UnsupportedOperationException();
      }

      @Override
      @SuppressWarnings("unchecked")
      protected boolean beforeRemove(final Object element) {
        value.set(((Map.Entry<K,V>)element).getValue());
        return true;
      }

      @Override
      protected void afterRemove(final Object element, final RuntimeException e) {
        IdentityHashBiMap.this.reverse.target.remove(value.get());
      }
    } : entrySet;
  }

  @SuppressWarnings("unchecked")
  private IdentityHashBiMap<K,V> superClone() {
    try {
      return (IdentityHashBiMap<K,V>)super.clone();
    }
    catch (final CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public IdentityHashBiMap<K,V> clone() {
    final IdentityHashBiMap<K,V> clone = superClone();
    clone.entrySet = null;
    clone.keySet = null;
    clone.values = null;
    clone.reverse = ((IdentityHashBiMap<V,K>)reverse).superClone();
    clone.setTarget((Map<K,V>)(((IdentityHashMap<K,V>)((ObservableMap<K,V>)target).target).clone()));
    clone.reverse.setTarget((Map<V,K>)(((IdentityHashMap<K,V>)((ObservableMap<K,V>)reverse.target).target).clone()));
    clone.reverse.reverse = clone;
    return clone;
  }
}