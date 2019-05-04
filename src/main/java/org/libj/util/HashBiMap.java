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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Bidirectional map ({@link BiMap}) backed by a {@link HashMap}.
 *
 * @param <K> The type of keys maintained by this map.
 * @param <V> The type of mapped values.
 * @see BiMap
 * @see HashMap
 */
public class HashBiMap<K,V> extends BiMap<K,V> implements Cloneable, Serializable {
  private static final long serialVersionUID = 2588014751081881163L;

  /**
   * Constructs an empty {@code HashBiMap} with the specified initial capacity
   * and load factor.
   *
   * @param initialCapacity The initial capacity.
   * @param loadFactor The load factor.
   * @throws IllegalArgumentException If the initial capacity is negative or the
   *           load factor is nonpositive.
   */
  public HashBiMap(final int initialCapacity, final float loadFactor) {
    super(new HashMap<K,V>(initialCapacity, loadFactor), new HashMap<V,K>(initialCapacity, loadFactor));
  }

  /**
   * Constructs an empty {@code HashBiMap} with the specified initial capacity
   * and the default load factor (0.75).
   *
   * @param initialCapacity The initial capacity.
   * @throws IllegalArgumentException If the initial capacity is negative.
   */
  public HashBiMap(final int initialCapacity) {
    super(new HashMap<K,V>(initialCapacity), new HashMap<V,K>(initialCapacity));
  }

  /**
   * Constructs a new {@code HashBiMap} with the same mappings as the specified
   * {@code Map}. The {@code HashBiMap} is created with default load factor
   * (0.75) and an initial capacity sufficient to hold the mappings in the
   * specified {@code Map}.
   *
   * @param m The map whose mappings are to be placed in this map.
   * @throws NullPointerException If the specified map is null.
   */
  public HashBiMap(final Map<? extends K,? extends V> m) {
    this();
    putAll(m);
  }

  /**
   * Constructs an empty {@code HashBiMap} with the default initial capacity
   * (16) and the default load factor (0.75).
   */
  public HashBiMap() {
    super(new HashMap<K,V>(), new HashMap<V,K>());
  }

  /**
   * Creates an empty instance.
   *
   * @param empty Ignored parameter.
   */
  protected HashBiMap(final boolean empty) {
    super();
  }

  @Override
  protected BiMap<V,K> newEmptyInverseMap() {
    return new HashBiMap<>(true);
  }

  @SuppressWarnings("unchecked")
  private HashBiMap<K,V> superClone() {
    try {
      return (HashBiMap<K,V>)super.clone();
    }
    catch (final CloneNotSupportedException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public HashBiMap<K,V> clone() {
    final HashBiMap<K,V> clone = superClone();
    clone.inverse = ((HashBiMap<V,K>)inverse).superClone();
    clone.setTarget((Map<K,V>)(((HashMap<K,V>)((ObservableMap<K,V>)target).target).clone()));
    clone.inverse.setTarget((Map<V,K>)(((HashMap<K,V>)((ObservableMap<K,V>)inverse.target).target).clone()));
    clone.inverse.inverse = clone;
    return clone;
  }
}