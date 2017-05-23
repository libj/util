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

import java.util.Map;

public class DelegatedMap<K,V> extends PartialMap<K,V> {
  private static final long serialVersionUID = -2754766054459728078L;

  private final MapDelegate<K,V> delegate;

  public interface MapDelegate<K,V> {
    public void put(final K key, final V value);
    public void remove(final Object key);
  }

  @SuppressWarnings("rawtypes")
  public DelegatedMap(final Class<? extends Map> type, final MapDelegate<K,V> delegate) {
    super(type);
    this.delegate = delegate;
    if (delegate == null)
      throw new NullPointerException("delegate == null");
  }

  @Override
  public V put(final K key, final V value) {
    final V previous = map.put(key, value);
    delegate.put(key, value);
    return previous;
  }

  @Override
  public V remove(final Object key) {
    final V value = map.remove(key);
    delegate.remove(key);
    return value;
  }

  @Override
  public DelegatedMap<K,V> clone() {
    return Maps.clone(this);
  }
}