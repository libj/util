/* Copyright (c) 2016 LibJ
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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A {@link DelegateMap} that provides callback methods to observe the addition
 * and removal of elements, either due to direct method invocation on the map
 * instance itself, or via {@link #entrySet()}, {@link #values()},
 * {@link #forEach(java.util.function.BiConsumer)}, and any other entrypoint
 * that facilitates modification of the elements in this map.
 *
 * @param <K> The type of keys maintained by this map.
 * @param <V> The type of mapped values.
 * @see #beforePut(Object,Object,Object)
 * @see #afterPut(Object,Object,Object,RuntimeException)
 * @see #beforeRemove(Object,Object)
 * @see #afterRemove(Object,Object,RuntimeException)
 */
public abstract class ObservableMap<K,V> extends DelegateMap<K,V> {
  /**
   * Creates a new {@link ObservableList} with the specified target
   * {@link Map}.
   *
   * @param map The target {@link Map}.
   * @throws NullPointerException If {@code map} is null.
   */
  public ObservableMap(final Map<K,V> map) {
    super(map);
  }

  /**
   * Callback method that is invoked immediately before a key is dereferenced
   * for inclusion via {@link #containsKey(Object)}.
   *
   * @param key The key to be dereferenced for inclusion.
   * @return If this method returns {@code false}, the subsequent
   *         {@link #containsKey(Object)} operation immediately return
   *         {@code false}; otherwise, the operation will be performed in
   *         regular fashion.
   */
  protected boolean beforeContainsKey(final Object key) {
    return true;
  }

  /**
   * Callback method that is invoked immediately after a key is dereferenced for
   * inclusion via {@link #containsKey(Object)}.
   *
   * @param key The key dereferenced for inclusion.
   * @param result The result returned by {@link #containsKey(Object)}.
   * @param re A {@link RuntimeException} that occurred during the get
   *          operation, or {@code null} if no exception occurred.
   */
  protected void afterContainsKey(final Object key, final boolean result, final RuntimeException re) {
  }

  /**
   * Callback method that is invoked immediately before a value is dereferenced
   * for inclusion via {@link #containsValue(Object)}.
   *
   * @param value The key to be dereferenced for inclusion.
   */
  protected void beforeContainsValue(final Object value) {
  }

  /**
   * Callback method that is invoked immediately after a value is dereferenced
   * for inclusion via {@link #containsValue(Object)}.
   *
   * @param value The value dereferenced for inclusion.
   * @param result The result returned by {@link #containsValue(Object)}.
   * @param re A {@link RuntimeException} that occurred during the get
   *          operation, or {@code null} if no exception occurred.
   */
  protected void afterContainsValue(final Object value, final boolean result, final RuntimeException re) {
  }

  /**
   * Callback method that is invoked immediately before a value is retrieved via
   * {@link #get(Object)}.
   *
   * @param key The key whose associated value is to be retrieved.
   */
  protected void beforeGet(final Object key) {
  }

  /**
   * Callback method that is invoked immediately after a value is retrieved from
   * the enclosed {@link Map}.
   *
   * @param key The key whose associated value is to be retrieved.
   * @param value The value retrieved via {@link #get(Object)}.
   * @param re A {@link RuntimeException} that occurred during the get
   *          operation, or {@code null} if no exception occurred.
   */
  protected void afterGet(final Object key, final V value, final RuntimeException re) {
  }

  /**
   * Callback method that is invoked immediately before an entry is put into the
   * enclosed {@link Map}.
   *
   * @param key The key of the entry to be added to the enclosed {@link Map}.
   * @param oldValue The old value to be replaced for the key in the enclosed
   *          {@link Map}, or {@code null} if there was no existing value for
   *          the key.
   * @param newValue The new value to be put for the key in the enclosed
   *          {@link Map}.
   * @return If this method returns {@code false}, the subsequent {@code put}
   *         operation will not be performed; otherwise, the operation will be
   *         performed.
   */
  protected boolean beforePut(final K key, final V oldValue, final V newValue) {
    return true;
  }

  /**
   * Callback method that is invoked immediately after an entry is put into the
   * enclosed {@link Map}.
   *
   * @param key The key of the entry to be added to the enclosed {@link Map}.
   * @param oldValue The old value to be replaced for the key in the enclosed
   *          {@link Map}, or {@code null} if there was no existing value for
   *          the key.
   * @param newValue The new value to be put for the key in the enclosed
   *          {@link Map}.
   * @param re A {@link RuntimeException} that occurred during the put
   *          operation, or {@code null} if no exception occurred.
   */
  protected void afterPut(final K key, final V oldValue, final V newValue, final RuntimeException re) {
  }

  /**
   * Callback method that is invoked immediately before an entry is removed from
   * the enclosed {@link Map}.
   *
   * @param key The key of the entry to be removed from the enclosed
   *          {@link Map}.
   * @param value The value for the key to be removed in the enclosed
   *          {@link Map}, or {@code null} if there was no existing value for
   *          the key.
   * @return If this method returns {@code false}, the subsequent {@code remove}
   *         operation will not be performed; otherwise, the operation will be
   *         performed.
   */
  protected boolean beforeRemove(final Object key, final V value) {
    return true;
  }

  /**
   * Callback method that is invoked immediately after an entry is removed from
   * the enclosed {@link Map}.
   *
   * @param key The key of the entry to be removed from the enclosed
   *          {@link Map}.
   * @param value The value for the key to be removed in the enclosed
   *          {@link Map}, or {@code null} if there was no existing value for
   *          the key.
   * @param re A {@link RuntimeException} that occurred during the remove
   *          operation, or {@code null} if no exception occurred.
   */
  protected void afterRemove(final Object key, final V value, final RuntimeException re) {
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeContainsKey(Object)} and
   * {@link #afterContainsKey(Object,boolean,RuntimeException)} are called
   * immediately before and after the get operation on the enclosed {@link Map}.
   */
  @Override
  public boolean containsKey(final Object key) {
    beforeContainsKey(key);
    boolean result = false;
    RuntimeException re = null;
    try {
      result = target.containsKey(key);
    }
    catch (final RuntimeException e) {
      re = e;
    }

    afterContainsKey(key, result, re);
    if (re != null)
      throw re;

    return result;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeContainsValue(Object)} and
   * {@link #afterContainsValue(Object,boolean,RuntimeException)} are called
   * immediately before and after the get operation on the enclosed {@link Map}.
   */
  @Override
  public boolean containsValue(final Object value) {
    beforeContainsValue(value);
    boolean result = false;
    RuntimeException re = null;
    try {
      result = target.containsValue(value);
    }
    catch (final RuntimeException e) {
      re = e;
    }

    afterContainsValue(value, result, re);
    if (re != null)
      throw re;

    return result;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(Object)} and
   * {@link #afterGet(Object,Object,RuntimeException)} are called immediately
   * before and after the get operation on the enclosed {@link Map}.
   */
  @Override
  @SuppressWarnings("unchecked")
  public V get(final Object key) {
    beforeGet(key);
    V value = null;
    RuntimeException re = null;
    try {
      value = (V)target.get(key);
    }
    catch (final RuntimeException e) {
      re = e;
    }

    afterGet(key, value, re);
    if (re != null)
      throw re;

    return value;
  }

  /**
   * Main method for the association of a value with a key in this map. This
   * method is the downstream call from {@link #put(Object,Object)},
   * {@link #putIfAbsent(Object,Object)}, {@link #replace(Object,Object)}, and
   * {@link #replace(Object,Object,Object)} for the final "put" operation into
   * the underlying {@link Map}.
   * <p>
   * The callback methods {@link #beforePut(Object,Object,Object)} and
   * {@link #afterPut(Object,Object,Object,RuntimeException)} are called
   * immediately before and after the enclosed collection is modified.
   *
   * @param key Key with which the specified value is to be associated.
   * @param oldValue Value currently associated with the specified key, or
   *          {@code null} if there was no association.
   * @param newValue Value to be associated with the specified key.
   * @return The previous value associated with {@code key}, or {@code null} if
   *         there was no mapping for {@code key}.
   */
  @SuppressWarnings("unchecked")
  protected V put(final K key, V oldValue, final V newValue) {
    if (!beforePut(key, oldValue, newValue))
      return oldValue;

    RuntimeException re = null;
    try {
      oldValue = (V)target.put(key, newValue);
    }
    catch (final RuntimeException t) {
      re = t;
    }

    afterPut(key, oldValue, newValue, re);
    if (re != null)
      throw re;

    return oldValue;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(Object)} and
   * {@link #afterGet(Object,Object,RuntimeException)} are called immediately
   * before and after the get operation on the enclosed {@link Map}.
   * <p>
   * The callback methods {@link #beforePut(Object,Object,Object)} and
   * {@link #afterPut(Object,Object,Object,RuntimeException)} are called
   * immediately before and after the enclosed collection is modified.
   */
  @Override
  public V put(final K key, final V value) {
    final V oldValue = get(key);
    return put(key, oldValue, value);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(Object)} and
   * {@link #afterGet(Object,Object,RuntimeException)} are called immediately
   * before and after the get operation on the enclosed {@link Map}.
   * <p>
   * The callback methods {@link #beforePut(Object,Object,Object)} and
   * {@link #afterPut(Object,Object,Object,RuntimeException)} are called
   * immediately before and after the enclosed collection is modified.
   */
  @Override
  public V putIfAbsent(final K key, final V value) {
    final V oldValue = get(key);
    return oldValue != null ? oldValue : put(key, oldValue, value);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforePut(Object,Object,Object)}
   * and {@link #afterPut(Object,Object,Object,RuntimeException)} are called
   * immediately before and after the enclosed collection is modified for the
   * addition of each entry in the argument map.
   */
  @Override
  public void putAll(final Map<? extends K,? extends V> m) {
    for (final Map.Entry<? extends K,? extends V> entry : m.entrySet())
      put(entry.getKey(), entry.getValue());
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(Object)} and
   * {@link #afterGet(Object,Object,RuntimeException)} are called immediately
   * before and after the get operation on the enclosed {@link Map}.
   * <p>
   * The callback methods {@link #beforeRemove(Object,Object)} and
   * {@link #afterRemove(Object,Object,RuntimeException)} are called immediately
   * before and after the enclosed collection is modified.
   */
  @Override
  @SuppressWarnings("unlikely-arg-type")
  public V remove(final Object key) {
    final V oldValue = get(key);
    if (!beforeRemove(key, oldValue))
      return oldValue;

    RuntimeException re = null;
    try {
      target.remove(key);
    }
    catch (final RuntimeException t) {
      re = t;
    }

    afterRemove(key, oldValue, re);
    if (re != null)
      throw re;

    return oldValue;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(Object)} and
   * {@link #afterGet(Object,Object,RuntimeException)} are called immediately
   * before and after the get operation on the enclosed {@link Map}.
   * <p>
   * The callback methods {@link #beforeRemove(Object,Object)} and
   * {@link #afterRemove(Object,Object,RuntimeException)} are called immediately
   * before and after the enclosed collection is modified.
   */
  @Override
  @SuppressWarnings("unlikely-arg-type")
  public boolean remove(final Object key, final Object value) {
    final V oldValue = get(key);
    if (!beforeRemove(key, oldValue))
      return false;

    boolean result = false;
    RuntimeException re = null;
    try {
      result = target.remove(key, value);
    }
    catch (final RuntimeException t) {
      re = t;
    }

    afterRemove(key, oldValue, re);
    if (re != null)
      throw re;

    return result;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(Object)} and
   * {@link #afterGet(Object,Object,RuntimeException)} are called immediately
   * before and after the get operation on the enclosed {@link Map}.
   * <p>
   * The callback methods {@link #beforePut(Object,Object,Object)} and
   * {@link #afterPut(Object,Object,Object,RuntimeException)} are called
   * immediately before and after the enclosed collection is modified.
   */
  @Override
  public boolean replace(final K key, final V oldValue, final V newValue) {
    final V previous = get(key);
    if (oldValue != null ? !oldValue.equals(previous) : previous != null)
      return false;

    put(key, previous, newValue);
    return true;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(Object)} and
   * {@link #afterGet(Object,Object,RuntimeException)} are called immediately
   * before and after the get operation on the enclosed {@link Map}.
   * <p>
   * The callback methods {@link #beforePut(Object,Object,Object)} and
   * {@link #afterPut(Object,Object,Object,RuntimeException)} are called
   * immediately before and after the enclosed collection is modified.
   */
  @Override
  public V replace(final K key, final V value) {
    final V oldValue = get(key);
    if (oldValue == null)
      return null;

    put(key, oldValue, value);
    return oldValue;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(Object,Object)} and
   * {@link #afterRemove(Object,Object,RuntimeException)} are called immediately
   * before and after the enclosed collection is modified for the removal of
   * each entry removed.
   */
  @Override
  public void clear() {
    final Iterator<K> iterator = keySet().iterator();
    while (iterator.hasNext()) {
      iterator.next();
      iterator.remove();
    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(Object,Object)} and
   * {@link #afterRemove(Object,Object,RuntimeException)} are called immediately
   * before and after the an entry is removed in the enclosed collection. The
   * callback methods {@link #beforePut(Object,Object,Object)} and
   * {@link #afterPut(Object,Object,Object,RuntimeException)} are called
   * immediately before and after the an entry is put in the enclosed
   * collection.
   */
  @Override
  @SuppressWarnings("unchecked")
  public V compute(final K key, final BiFunction<? super K,? super V,? extends V> remappingFunction) {
    return (V)target.compute(key, (BiFunction<K,V,V>)(t, u) -> {
      final V value = remappingFunction.apply(t, u);
      if (value == null)
        remove(t);
      else
        put(t, value);

      return value;
    });
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforePut(Object,Object,Object)} and
   * {@link #afterPut(Object,Object,Object,RuntimeException)} are called
   * immediately before and after the an entry is put in the enclosed
   * collection.
   */
  @Override
  @SuppressWarnings("unchecked")
  public V computeIfAbsent(final K key, final Function<? super K,? extends V> mappingFunction) {
    return (V)target.computeIfAbsent(key, (Function<K,V>)t -> {
      final V value = mappingFunction.apply(t);
      put(t, value);
      return value;
    });
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(Object,Object)} and
   * {@link #afterRemove(Object,Object,RuntimeException)} are called immediately
   * before and after the an entry is removed in the enclosed collection. The
   * callback methods {@link #beforePut(Object,Object,Object)} and
   * {@link #afterPut(Object,Object,Object,RuntimeException)} are called
   * immediately before and after the an entry is put in the enclosed
   * collection.
   */
  @Override
  @SuppressWarnings("unchecked")
  public V computeIfPresent(final K key, final BiFunction<? super K,? super V,? extends V> remappingFunction) {
    return (V)target.computeIfPresent(key, (BiFunction<K,V,V>)(t, u) -> {
      final V value = remappingFunction.apply(t, u);
      if (value == null)
        remove(t);
      else
        put(t, value);

      return value;
    });
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(Object)} and
   * {@link #afterGet(Object,Object,RuntimeException)} are called immediately
   * before and after the get operation on the enclosed {@link Map}.
   * <p>
   * The callback methods {@link #beforeRemove(Object,Object)} and
   * {@link #afterRemove(Object,Object,RuntimeException)} are called immediately
   * before and after the an entry is removed in the enclosed collection.
   * <p>
   * The callback methods {@link #beforePut(Object,Object,Object)} and
   * {@link #afterPut(Object,Object,Object,RuntimeException)} are called
   * immediately before and after the an entry is put in the enclosed
   * collection.
   */
  @Override
  @SuppressWarnings({"unchecked", "unlikely-arg-type"})
  public V merge(final K key, final V value, final BiFunction<? super V,? super V,? extends V> remappingFunction) {
    return (V)(get(key) == null ? put(key, value) : target.merge(key, value, (BiFunction<V,V,V>)(t, u) -> {
      final V value1 = remappingFunction.apply(t, u);
      if (value1 == null)
        remove(t);
      else
        put(key, value1);

      return value1;
    }));
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforePut(Object,Object,Object)} and
   * {@link #afterPut(Object,Object,Object,RuntimeException)} are called
   * immediately before and after the an entry is put in the enclosed
   * collection.
   */
  @Override
  public void replaceAll(final BiFunction<? super K,? super V,? extends V> function) {
    target.replaceAll((BiFunction<K,V,V>)(t, u) -> {
      final V value = function.apply(t, u);
      put(t, value);
      return value;
    });
  }

  protected volatile ObservableSet<Map.Entry<K,V>> entrySet;

  @Override
  public Set<Map.Entry<K,V>> entrySet() {
    return entrySet == null ? entrySet = new ObservableSet<Map.Entry<K,V>>(target.entrySet()) {
      private final ThreadLocal<K> localKey = new ThreadLocal<>();
      private final ThreadLocal<V> localOldValue = new ThreadLocal<>();
      private final ThreadLocal<V> localNewValue = new ThreadLocal<>();

      @Override
      protected boolean beforeAdd(final Entry<K,V> e) {
        localKey.set(e.getKey());
        localNewValue.set(e.getValue());
        localOldValue.set(ObservableMap.this.get(e.getKey()));
        return ObservableMap.this.beforePut(e.getKey(), localOldValue.get(), e.getValue());
      }

      @Override
      protected void afterAdd(final Entry<K,V> e, final RuntimeException re) {
        ObservableMap.this.afterPut(localKey.get(), localOldValue.get(), localNewValue.get(), re);
      }

      @Override
      @SuppressWarnings("unchecked")
      protected boolean beforeRemove(final Object e) {
        final Map.Entry<K,V> entry = (Map.Entry<K,V>)e;
        localKey.set(entry.getKey());
        localNewValue.set(entry.getValue());
        return ObservableMap.this.beforeRemove(entry.getKey(), entry.getValue());
      }

      @Override
      protected void afterRemove(final Object e, final RuntimeException re) {
        ObservableMap.this.afterRemove(localKey.get(), localNewValue.get(), re);
      }
    } : entrySet;
  }

  protected volatile ObservableSet<K> keySet;

  @Override
  public Set<K> keySet() {
    return keySet == null ? keySet = new ObservableSet<K>(target.keySet()) {
      private final ThreadLocal<Object> localKey = new ThreadLocal<>();
      private final ThreadLocal<V> localNewValue = new ThreadLocal<>();

      @Override
      protected boolean beforeAdd(final K e) {
        throw new UnsupportedOperationException();
      }

      @Override
      @SuppressWarnings("unlikely-arg-type")
      protected boolean beforeRemove(final Object e) {
        localKey.set(e);
        final V value = ObservableMap.this.get(e);
        localNewValue.set(value);
        return ObservableMap.this.beforeRemove(e, value);
      }

      @Override
      protected void afterRemove(final Object e, final RuntimeException re) {
        ObservableMap.this.afterRemove(localKey.get(), localNewValue.get(), re);
      }
    } : keySet;
  }

  protected volatile TransCollection<Map.Entry<K,V>,V> values;

  @Override
  public Collection<V> values() {
    return values == null ? values = new TransCollection<>(entrySet(), Entry::getValue, null) : values;
  }
}