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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Wrapper class for the <code>Map</code> interface that provides callback
 * methods to observe the addition and removal of entries to the wrapped
 * <code>Map</code>.
 *
 * @see ObservableMap#beforePut(Object, Object, Object)
 * @see ObservableMap#afterPut(Object, Object, Object)
 * @see ObservableMap#beforeRemove(Object, Object)
 * @see ObservableMap#afterRemove(Object, Object)
 * @see Map
 */
public class ObservableMap<K,V> extends WrappedMap<K,V> {
  public ObservableMap(final Map<K,V> map) {
    super(map);
  }

  /**
   * Callback method that is invoked immediately before an entry is put into
   * the enclosed <code>Map</code>.
   *
   * @param key The key of the entry being added to the enclosed
   * <code>Map</code>.
   * @param oldValue The old value being replaced for the key in the enclosed
   * <code>Map</code>, or null if there was no existing value for the key.
   * @param newValue The new value being put for the key in the enclosed
   * <code>Map</code>.
   */
  protected void beforePut(final K key, final V oldValue, final V newValue) {
  }

  /**
   * Callback method that is invoked immediately after an entry is put into
   * the enclosed <code>Map</code>.
   *
   * @param key The key of the entry being added to the enclosed
   * <code>Map</code>.
   * @param oldValue The old value being replaced for the key in the enclosed
   * <code>Map</code>, or null if there was no existing value for the key.
   * @param newValue The new value being put for the key in the enclosed
   * <code>Map</code>.
   */
  protected void afterPut(final K key, final V oldValue, final V newValue) {
  }

  /**
   * Callback method that is invoked immediately before an entry is removed
   * from the enclosed <code>Map</code>.
   *
   * @param key The key of the entry being removed from the enclosed
   * <code>Map</code>.
   * @param value The value for the key being removed in the enclosed
   * <code>Map</code>, or null if there was no existing value for the key.
   */
  protected void beforeRemove(final Object key, final V value) {
  }

  /**
   * Callback method that is invoked immediately after an entry is removed
   * from the enclosed <code>Map</code>.
   *
   * @param key The key of the entry being removed from the enclosed
   * <code>Map</code>.
   * @param value The value for the key being removed in the enclosed
   * <code>Map</code>, or null if there was no existing value for the key.
   */
  protected void afterRemove(final Object key, final V value) {
  }

  /**
   * Associates the specified value with the specified key in this map
   * (optional operation). The callback methods <code>beforePut()</code> and
   * <code>afterPut()</code> are called immediately before and after the
   * enclosed collection is modified.
   */
  @Override
  @SuppressWarnings("unchecked")
  public V put(final K key, final V value) {
    final V oldValue = (V)source.get(key);
    beforePut(key, oldValue, value);
    source.put(key, value);
    afterPut(key, oldValue, value);
    return oldValue;
  }

  /**
   * If the specified key is not already associated with a value (or is mapped
   * to {@code null}) associates it with the given value and returns
   * {@code null}, else returns the current value. The callback methods
   * <code>beforePut()</code> and <code>afterPut()</code> are called
   * immediately before and after the enclosed collection is modified.
   */
  @Override
  public V putIfAbsent(final K key, final V value) {
    final V previous = get(key);
    if (previous == null)
      put(key, value);

    return previous;
  }

  /**
   * Copies all of the mappings from the specified map to this map
   * (optional operation). The callback methods <code>beforePut()</code> and
   * <code>afterPut()</code> are called immediately before and after the
   * enclosed collection is modified for the addition of each entry in the
   * argument map.
   */
  @Override
  public void putAll(final Map<? extends K,? extends V> m) {
    for (final Map.Entry<? extends K,? extends V> entry : m.entrySet())
      put(entry.getKey(), entry.getValue());
  }

  /**
   * Removes the mapping for a key from this map if it is present
   * (optional operation). The callback methods <code>beforeRemove()</code> and
   * <code>afterRemove()</code> are called immediately before and after the
   * enclosed collection is modified.
   */
  @Override
  @SuppressWarnings("unchecked")
  public V remove(final Object key) {
    final V value = (V)source.get(key);
    beforeRemove(key, value);
    source.remove(key);
    afterRemove(key, value);
    return value;
  }

  /**
   * Replaces the entry for the specified key only if currently
   * mapped to the specified value. The callback methods
   * <code>beforePut()</code> and <code>afterPut()</code> are called
   * immediately before and after the enclosed collection is modified.
   */
  @Override
  public boolean replace(final K key, final V oldValue, final V newValue) {
    final V previous = get(key);
    if (previous == null || oldValue == null || !oldValue.equals(previous))
      return false;

    put(key, newValue);
    return true;
  }

  /**
   * Replaces the entry for the specified key only if it is
   * currently mapped to some value. The callback methods
   * <code>beforePut()</code> and <code>afterPut()</code> are called
   * immediately before and after the enclosed collection is modified.
   */
  @Override
  public V replace(final K key, final V value) {
    final V previous = get(key);
    if (previous == null)
      return null;

    put(key, value);
    return previous;
  }

  /**
   * Removes all of the mappings from this map (optional operation).
   * The map will be empty after this call returns. The callback methods
   * <code>beforeRemove()</code> and <code>afterRemove()</code> are called
   * immediately before and after the enclosed collection is modified for the
   * removal of each entry removed.
   */
  @Override
  public void clear() {
    final Iterator<K> i = keySet().iterator();
    while (i.hasNext()) {
      i.next();
      i.remove();
    }
  }

  /**
   * Attempts to compute a mapping for the specified key and its current
   * mapped value (or {@code null} if there is no current mapping). The
   * callback methods <code>beforeRemove()</code> and
   * <code>afterRemove()</code> are called immediately before and after the
   * an entry is removed in the enclosed collection. The callback methods
   * <code>beforePut()</code> and <code>afterPut()</code> are called
   * immediately before and after the an entry is put in the enclosed
   * collection.
   */
  @Override
  @SuppressWarnings("unchecked")
  public V compute(final K key, final BiFunction<? super K,? super V,? extends V> remappingFunction) {
    return (V)source.compute(key, new BiFunction<K,V,V>() {
      @Override
      public V apply(final K t, final V u) {
        final V value = remappingFunction.apply(t, u);
        if (value == null)
          remove(t);
        else
          put(t, value);

        return value;
      }
    });
  }

  /**
   * If the specified key is not already associated with a value (or is mapped
   * to {@code null}), attempts to compute its value using the given mapping
   * function and enters it into this map unless {@code null}. The callback
   * methods <code>beforePut()</code> and <code>afterPut()</code> are called
   * immediately before and after the an entry is put in the enclosed
   * collection.
   */
  @Override
  @SuppressWarnings("unchecked")
  public V computeIfAbsent(final K key, final Function<? super K,? extends V> mappingFunction) {
    return (V)source.computeIfAbsent(key, new Function<K,V>() {
      @Override
      public V apply(final K t) {
        final V value = mappingFunction.apply(t);
        put(t, value);
        return value;
      }
    });
  }

  /**
   * If the value for the specified key is present and non-null, attempts to
   * compute a new mapping given the key and its current mapped value. The
   * callback methods <code>beforeRemove()</code> and
   * <code>afterRemove()</code> are called immediately before and after the
   * an entry is removed in the enclosed collection. The callback methods
   * <code>beforePut()</code> and <code>afterPut()</code> are called
   * immediately before and after the an entry is put in the enclosed
   * collection.
   */
  @Override
  @SuppressWarnings("unchecked")
  public V computeIfPresent(final K key, final BiFunction<? super K,? super V,? extends V> remappingFunction) {
    return (V)source.computeIfPresent(key, new BiFunction<K,V,V>() {
      @Override
      public V apply(final K t, final V u) {
        final V value = remappingFunction.apply(t, u);
        if (value == null)
          remove(t);
        else
          put(t, value);

        return value;
      }
    });
  }

  /**
   * If the specified key is not already associated with a value or is
   * associated with null, associates it with the given non-null value.
   * Otherwise, replaces the associated value with the results of the given
   * remapping function, or removes if the result is {@code null}. The
   * callback methods <code>beforeRemove()</code> and
   * <code>afterRemove()</code> are called immediately before and after the
   * an entry is removed in the enclosed collection. The callback methods
   * <code>beforePut()</code> and <code>afterPut()</code> are called
   * immediately before and after the an entry is put in the enclosed
   * collection.
   */
  @Override
  @SuppressWarnings({"unchecked", "unlikely-arg-type"})
  public V merge(final K key, final V value, final BiFunction<? super V,? super V,? extends V> remappingFunction) {
    return (V)(source.get(key) == null ? source.put(key, value) : source.merge(key, value, new BiFunction<V,V,V>() {
      @Override
      public V apply(final V t, final V u) {
        final V value = remappingFunction.apply(t, u);
        if (value == null)
          remove(t);
        else
          put(key, value);

        return value;
      }
    }));
  }

  /**
   * Replaces each entry's value with the result of invoking the given
   * function on that entry until all entries have been processed or the
   * function throws an exception. The callback methods
   * <code>beforePut()</code> and <code>afterPut()</code> are called
   * immediately before and after the an entry is put in the enclosed
   * collection.
   */
  @Override
  public void replaceAll(final BiFunction<? super K,? super V,? extends V> function) {
    source.replaceAll(new BiFunction<K,V,V>() {
      @Override
      public V apply(final K t, final V u) {
        final V value = function.apply(t, u);
        put(t, value);
        return value;
      }
    });
  }

  protected ObservableSet<Map.Entry<K,V>> entrySet;

  @Override
  public Set<Map.Entry<K,V>> entrySet() {
    return entrySet == null ? entrySet = new ObservableSet<Map.Entry<K,V>>(source.entrySet()) {
      private final ThreadLocal<K> localKey = new ThreadLocal<K>();
      private final ThreadLocal<V> localValue = new ThreadLocal<V>();

      @Override
      @SuppressWarnings("unchecked")
      protected void beforeRemove(final Object e) {
        final Map.Entry<K,V> entry = (Map.Entry<K,V>)e;
        localKey.set(entry.getKey());
        localValue.set(entry.getValue());
        ObservableMap.this.beforeRemove(entry.getKey(), entry.getValue());
      }

      @Override
      protected void afterRemove(final Object e) {
        ObservableMap.this.afterRemove(localKey.get(), localValue.get());
      }
    } : entrySet;
  }

  @Override
  public Set<K> keySet() {
    return new TransSet<Map.Entry<K,V>,K>(entrySet(), entry -> entry.getKey(), null);
  }

  @Override
  public Collection<V> values() {
    return new TransCollection<Map.Entry<K,V>,V>(entrySet(), entry -> entry.getValue(), null);
  }
}