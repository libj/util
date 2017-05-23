/* Copyright (c) 2010 lib4j
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
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public final class ArrayMap<K extends Comparable<K>,V> extends AbstractMap<K,V> implements Map<K,V>, Cloneable, Serializable {
  private static final long serialVersionUID = -7054980275015783040L;

  public static final class Entry<K extends Comparable<K>,V> implements Comparable<Entry<K,V>>, Map.Entry<K,V> {
    private final K key;
    private V value;

    protected Entry(final K key, final V value) {
      super();
      this.key = key;
      this.value = value;
    }

    @Override
    public K getKey() {
      return key;
    }

    @Override
    public V getValue() {
      return value;
    }

    @Override
    public V setValue(final V value) {
      final V oldValue = value;
      this.value = value;
      return oldValue;
    }

    @Override
    public int compareTo(final Entry<K,V> o) {
      return key.compareTo(o.key);
    }

    @Override
    public boolean equals(final Object o) {
      if (this == o)
        return true;

      if (!(o instanceof Entry))
        return false;

      final Entry<?,?> that = (Entry<?,?>)o;
      return (key != null ? key.equals(that.key) : that.value == null) && (value != null ? value.equals(that.value) : that.value == null);
    }

    @Override
    public int hashCode() {
      return (key != null ? key.hashCode() : 0) ^ (value != null ? value.hashCode() : 0);
    }
  }

  protected ArrayList<Entry<K,V>> entries;

  public ArrayMap() {
    entries = new ArrayList<Entry<K,V>>();
  }

  public ArrayMap(final Map<K,V> m) {
    this();
    putAll(m);
  }

  public int indexOf(final Object key) {
    if (key == null) {
      for (int i = 0; i < size(); i++)
        if (key == entries.get(i).getKey())
          return i;
    }
    else {
      for (int i = 0; i < size(); i++)
        if (key.equals(entries.get(i).getKey()))
          return i;
    }

    return -1;
  }

  public ArrayMap.Entry<K,V> getEntry(final int i) {
    return entries.get(i);
  }

  @Override
  public void clear() {
    entries.clear();
  }

  @Override
  public boolean containsKey(final Object key) {
    return indexOf(key) != -1;
  }

  @Override
  public boolean containsValue(final Object value) {
    for (int i = 0; i < size(); i++)
      if (value.equals(((Entry<?,?>)entries.get(i)).value))
        return true;

    return false;
  }

  @Override
  public Set<Map.Entry<K,V>> entrySet() {
    final TreeSet<Map.Entry<K,V>> set = new TreeSet<Map.Entry<K,V>>();
    for (int i = 0; i < size(); i++)
      set.add(entries.get(i));

    return set;
  }

  @Override
  public boolean equals(final Object o) {
    return o == this;
  }

  @Override
  public V get(final Object key) {
    final int index = indexOf(key);
    if (index == -1)
      return null;

    return ((Map.Entry<K,V>)entries.get(index)).getValue();
  }

  @Override
  public int hashCode() {
    int hashCode = 0;
    for (int i = 0; i < size(); i++)
      hashCode ^= ((Entry<?,?>)entries.get(i)).hashCode();

    return hashCode;
  }

  @Override
  public boolean isEmpty() {
    return entries.isEmpty();
  }

  @Override
  public Set<K> keySet() {
    final TreeSet<K> keys = new TreeSet<K>();
    for (int i = 0; i < size(); i++)
      keys.add(entries.get(i).getKey());

    return keys;
  }

  @Override
  public V put(final K key, final V value) {
    final int index = indexOf(key);
    if (index != -1)
      return entries.get(index).setValue(value);

    entries.add(new Entry<K,V>(key, value));
    return null;
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> t) {
    if (t == null)
      throw new NullPointerException("t == null");

    for (final Map.Entry<? extends K, ? extends V> entry : t.entrySet())
      put(entry.getKey(), entry.getValue());
  }

  @Override
  public V remove(final Object key) {
    final int i = indexOf(key);
    if (i == -1)
      return null;

    return entries.remove(i).getValue();
  }

  @Override
  public int size() {
    return entries.size();
  }

  @Override
  public Collection<V> values() {
    final ArrayList<V> values = new ArrayList<V>();
    for (int i = 0; i < size(); i++)
      values.add(entries.get(i).value);

    return values;
  }
}