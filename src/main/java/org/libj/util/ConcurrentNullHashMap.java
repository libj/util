/* Copyright (c) 2020 LibJ
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
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A {@link ConcurrentHashMap} supporting {@code null} keys.
 *
 * @implNote This class does not properly handle
 *           {@link ConcurrentHashMap#keySet()} and
 *           {@link ConcurrentHashMap#keySet(Object)}.
 * @param <K> The type of keys maintained by this map.
 * @param <V> The type of mapped values.
 */
@SuppressWarnings("unlikely-arg-type")
public class ConcurrentNullHashMap<K,V> extends ConcurrentHashMap<K,V> {
  private static final long serialVersionUID = 6567470641062063194L;
  private static final Object NULL = new Object();

  @SuppressWarnings("unchecked")
  private static <T>T notNull(final T key) {
    return key == null ? (T)NULL : key;
  }

  /**
   * Creates a new, empty map with an initial table size accommodating the
   * specified number of elements without the need to dynamically resize.
   *
   * @param initialCapacity The implementation performs internal sizing to
   *          accommodate this many elements.
   * @throws IllegalArgumentException If the initial capacity of elements is
   *           negative.
   */
  public ConcurrentNullHashMap(final int initialCapacity) {
    super(initialCapacity);
  }

  /**
   * Creates a new map with the same mappings as the given map.
   *
   * @param m The map.
   */
  public ConcurrentNullHashMap(final Map<? extends K,? extends V> m) {
    super(m);
  }

  /**
   * Creates a new, empty map with an initial table size based on the given
   * number of elements ({@code initialCapacity}) and initial table density
   * ({@code loadFactor}).
   *
   * @param initialCapacity The initial capacity. The implementation performs
   *          internal sizing to accommodate this many elements, given the
   *          specified load factor.
   * @param loadFactor The load factor (table density) for establishing the
   *          initial table size.
   * @throws IllegalArgumentException If the initial capacity of elements is
   *           negative or the load factor is nonpositive.
   */
  public ConcurrentNullHashMap(final int initialCapacity, final float loadFactor) {
    this(initialCapacity, loadFactor, 1);
  }

  /**
   * Creates a new, empty map with an initial table size based on the given
   * number of elements ({@code initialCapacity}), table density
   * ({@code loadFactor}), and number of concurrently updating threads
   * ({@code concurrencyLevel}).
   *
   * @param initialCapacity The initial capacity. The implementation performs
   *          internal sizing to accommodate this many elements, given the
   *          specified load factor.
   * @param loadFactor The load factor (table density) for establishing the
   *          initial table size.
   * @param concurrencyLevel The estimated number of concurrently updating
   *          threads. The implementation may use this value as a sizing hint.
   * @throws IllegalArgumentException If the initial capacity is negative or the
   *           load factor or concurrencyLevel are nonpositive.
   */
  public ConcurrentNullHashMap(final int initialCapacity, final float loadFactor, final int concurrencyLevel) {
    super(initialCapacity, loadFactor, concurrencyLevel);
  }

  /**
   * Creates a new, empty map with the default initial table size (16).
   */
  public ConcurrentNullHashMap() {
    super();
  }

  @Override
  public boolean containsKey(final Object key) {
    return super.get(notNull(key)) != null;
  }

  @Override
  public void putAll(final Map<? extends K,? extends V> m) {
    // tryPresize(m.size()); // FIXME: How to call this in the super method?
    for (final Map.Entry<? extends K,? extends V> e : m.entrySet())
      put(e.getKey(), e.getValue());
  }

  @Override
  public V put(final K key, final V value) {
    return super.put(notNull(key), notNull(value));
  }

  @Override
  public V putIfAbsent(final K key, final V value) {
    return super.putIfAbsent(notNull(key), notNull(value));
  }

  @Override
  public boolean containsValue(final Object value) {
    return super.containsValue(notNull(value));
  }

  @Override
  public V get(final Object key) {
    final V value = super.get(notNull(key));
    return value == NULL ? null : value;
  }

  @Override
  public V remove(final Object key) {
    final V value = super.remove(notNull(key));
    return value == NULL ? null : value;
  }

  @Override
  public boolean replace(final K key, final V oldValue, final V newValue) {
    return super.replace(notNull(key), notNull(oldValue), notNull(newValue));
  }

  @Override
  public V replace(final K key, final V value) {
    final V oldValue = super.replace(notNull(key), notNull(value));
    return oldValue == NULL ? null : oldValue;
  }

  @Override
  public Collection<V> values() {
    return new ObservableCollection<V>(super.values()) {
      @Override
      protected V afterGet(final V value, final RuntimeException e) {
        return value == NULL ? null : value;
      }
    };
  }

  @Override
  public Set<Map.Entry<K,V>> entrySet() {
    return new DelegateSet<Map.Entry<K,V>>(super.entrySet()) {
      @Override
      public boolean add(final Map.Entry<K,V> e) {
        return super.add(new Map.Entry<K,V>() {
          @Override
          public K getKey() {
            return notNull(e.getKey());
          }

          @Override
          public V getValue() {
            return notNull(e.getValue());
          }

          @Override
          public V setValue(final V value) {
            return e.setValue(notNull(value));
          }
        });
      }
    };
  }

  private class NullEnumeration<E> implements Enumeration<E> {
    private final Enumeration<E> target;

    private NullEnumeration(final Enumeration<E> target) {
      this.target = target;
    }

    @Override
    public boolean hasMoreElements() {
      return target.hasMoreElements();
    }

    @Override
    public E nextElement() {
      final E key = target.nextElement();
      return key == NULL ? null : key;
    }
  }

  @Override
  public Enumeration<K> keys() {
    return new NullEnumeration<>(super.keys());
  }

  @Override
  public Enumeration<V> elements() {
    return new NullEnumeration<>(super.elements());
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(final Object o) {
    if (!(o instanceof Map))
      return false;

    final Map<Object,Object> m = (Map<Object,Object>)o;
    return super.equals(new ObservableMap<Object,Object>(m) {
      @Override
      protected Object beforeGet(final Object key) {
        return notNull(key);
      }

      @Override
      protected Object afterGet(final Object key, final Object value, final RuntimeException e) {
        return notNull(value);
      }
    });
  }
}