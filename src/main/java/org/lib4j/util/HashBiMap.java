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
import java.util.HashMap;
import java.util.Map;

/**
 * Bidirectional map (<code>BiMap</code>) backed by a <code>HashMap</code>.
 *
 * @see BiMap
 * @see HashMap
 */
public class HashBiMap<K,V> extends BiMap<K,V> implements Cloneable, Serializable {
  private static final long serialVersionUID = 2588014751081881163L;

  public HashBiMap(final int initialCapacity, final float loadFactor) {
    super(new HashMap<K,V>(initialCapacity, loadFactor), new HashMap<V,K>(initialCapacity, loadFactor));
  }

  public HashBiMap(final int initialCapacity) {
    super(new HashMap<K,V>(initialCapacity), new HashMap<V,K>(initialCapacity));
  }

  public HashBiMap(final Map<? extends K,? extends V> m) {
    this();
    putAll(m);
  }

  public HashBiMap() {
    super(new HashMap<K,V>(), new HashMap<V,K>());
  }

  protected HashBiMap(final boolean empty) {
    super(empty);
  }

  @Override
  protected BiMap<V,K> newEmptyInverseMap() {
    return new HashBiMap<V,K>(true);
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
    clone.init((Map<K,V>)(((HashMap<K,V>)((ObservableMap<K,V>)source).source).clone()));
    clone.inverse.init((Map<V,K>)(((HashMap<K,V>)((ObservableMap<K,V>)inverse.source).source).clone()));
    clone.inverse.inverse = clone;
    return clone;
  }
}