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

import static org.libj.lang.Assertions.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A {@link DelegateMap} that provides callback methods to observe the
 * retrieval, addition, and removal of elements, either due to direct method
 * invocation on the map instance itself, or via {@link #entrySet()},
 * {@link #values()}, {@link #forEach(java.util.function.BiConsumer)}, and any
 * other entrypoint that facilitates modification of the elements in this map.
 *
 * @param <K> The type of keys maintained by this map.
 * @param <V> The type of mapped values.
 * @see #beforeGet(Object)
 * @see #afterGet(Object,Object,RuntimeException)
 * @see #beforePut(Object,Object,Object,Object)
 * @see #afterPut(Object,Object,Object,RuntimeException)
 * @see #beforeRemove(Object,Object)
 * @see #afterRemove(Object,Object,RuntimeException)
 */
public abstract class ObservableMap<K,V> extends DelegateMap<K,V> {
  protected static final Object preventDefault = ObservableCollection.preventDefault;

  /**
   * Creates a new {@link ObservableMap} with the specified target {@link Map}.
   *
   * @param map The target {@link Map}.
   * @throws IllegalArgumentException If {@code map} is null.
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
   * @param e A {@link RuntimeException} that occurred during the get operation,
   *          or {@code null} if no exception occurred.
   */
  protected void afterContainsKey(final Object key, final boolean result, final RuntimeException e) {
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
   * @param e A {@link RuntimeException} that occurred during the get operation,
   *          or {@code null} if no exception occurred.
   */
  protected void afterContainsValue(final Object value, final boolean result, final RuntimeException e) {
  }

  /**
   * Callback method that is invoked immediately before a value is retrieved via
   * {@link #get(Object)}.
   *
   * @param key The key whose associated value is to be retrieved.
   * @return The key whose associated value is to be retrieved.
   */
  protected Object beforeGet(final Object key) {
    return key;
  }

  /**
   * Callback method that is invoked immediately after a value is retrieved from
   * the enclosed {@link Map}.
   *
   * @param key The key whose associated value is to be retrieved.
   * @param value The value retrieved via {@link #get(Object)}.
   * @param e A {@link RuntimeException} that occurred during the get operation,
   *          or {@code null} if no exception occurred.
   * @return The value to be retrieved via {@link #get(Object)}.
   */
  protected V afterGet(final Object key, final V value, final RuntimeException e) {
    return value;
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
   * @param preventDefault The object to return if the subsequent {@code put}
   *          operation is to be prevented.
   * @return The new value to be put for the key in the enclosed {@link Map}.
   */
  protected Object beforePut(final K key, final V oldValue, final V newValue, final Object preventDefault) {
    return newValue;
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
   * @param e A {@link RuntimeException} that occurred during the put operation,
   *          or {@code null} if no exception occurred.
   */
  protected void afterPut(final K key, final V oldValue, final V newValue, final RuntimeException e) {
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
   * @param e A {@link RuntimeException} that occurred during the remove
   *          operation, or {@code null} if no exception occurred.
   */
  protected void afterRemove(final Object key, final V value, final RuntimeException e) {
  }

  /**
   * An {@link ObservableSet ObservableSet&lt;Map.Entry&lt;K,V&gt;&gt;} that
   * delegates callback methods to the parent {@link ObservableMap} instance for
   * the retrieval and removal of entries.
   */
  protected class ObservableEntrySet extends ObservableSet<Map.Entry<K,V>> {
    private class ObservableEntry implements Map.Entry<K,V> {
      private final Map.Entry<K,V> target;

      private ObservableEntry(final Entry<K,V> target) {
        this.target = target;
      }

      @SuppressWarnings("unchecked")
      @Override
      public K getKey() {
        return (K)beforeGet(target.getKey());
      }

      @Override
      public V getValue() {
        return ObservableMap.this.afterGet(target.getKey(), target.getValue(), null);
      }

      @Override
      @SuppressWarnings("unchecked")
      public V setValue(final V newValue) {
        final V oldValue = getValue();
        final Object beforePut = ObservableMap.this.beforePut(getKey(), oldValue, newValue, preventDefault);
        if (beforePut != preventDefault)
          target.setValue((V)beforePut);

        return oldValue;
      }

      @Override
      public int hashCode() {
        return target.hashCode();
      }

      @Override
      public boolean equals(final Object obj) {
        return target.equals(obj);
      }

      @Override
      public String toString() {
        return target.toString();
      }
    }

    private final ThreadLocal<K> localKey = new ThreadLocal<>();
    private final ThreadLocal<V> localOldValue = new ThreadLocal<>();
    private final ThreadLocal<V> localNewValue = new ThreadLocal<>();

    /**
     * Creates a new {@link ObservableEntrySet} for the specified {@link Set
     * Set&lt;Map.Entry&lt;K,V&gt;&gt;}.
     *
     * @param set The {@link Iterator}.
     * @throws IllegalArgumentException If the specified {@link Set
     *           Set&lt;Map.Entry&lt;K,V&gt;&gt;}} is null.
     */
    protected ObservableEntrySet(final Set<Map.Entry<K,V>> set) {
      super(set);
    }

    @Override
    protected Map.Entry<K,V> afterGet(final Map.Entry<K,V> value, final RuntimeException e) {
      return new ObservableEntry(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Object beforeAdd(Map.Entry<K,V> element, final Object preventDefault) {
      final K key = element.getKey();
      final V oldValue = ObservableMap.this.get(key);
      final V newValue = element.getValue();
      final Object beforePut = ObservableMap.this.beforePut(key, oldValue, newValue, ObservableCollection.preventDefault);
      if (beforePut == ObservableCollection.preventDefault)
        return preventDefault;

      localKey.set(key);
      localNewValue.set((V)beforePut);
      localOldValue.set(oldValue);

      return beforePut == newValue ? element : new ObservableEntry(element) {
        @Override
        public V getValue() {
          return (V)beforePut;
        }
      };
    }

    @Override
    protected void afterAdd(final Entry<K,V> element, final RuntimeException e) {
      ObservableMap.this.afterPut(localKey.get(), localOldValue.get(), localNewValue.get(), e);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected boolean beforeRemove(final Object element) {
      final Map.Entry<K,V> entry = (Map.Entry<K,V>)element;
      final K key = entry.getKey();
      final V value = entry.getValue();
      if (!ObservableMap.this.beforeRemove(key, value))
        return false;

      localKey.set(key);
      localNewValue.set(value);
      return true;
    }

    @Override
    protected void afterRemove(final Object element, final RuntimeException e) {
      ObservableMap.this.afterRemove(localKey.get(), localNewValue.get(), e);
    }
  }

  /**
   * Factory method that returns a new instance of an
   * {@link ObservableEntrySet}. This method is intended to be overridden by
   * subclasses in order to provide instances of the subclass.
   *
   * @return A new instance of an {@link ObservableEntrySet}.
   */
  protected ObservableEntrySet newEntrySet() {
    return new ObservableEntrySet(target.entrySet());
  }

  /**
   * An {@link ObservableSet ObservableSet&lt;K&gt;} that delegates callback
   * methods to the parent {@link ObservableMap} instance for the retrieval and
   * removal of keys.
   */
  protected class ObservableKeySet extends ObservableSet<K> {
    /**
     * Creates a new {@link ObservableEntrySet} for the specified {@link Set
     * Set&lt;K&gt;}.
     *
     * @param set The {@link Iterator}.
     * @throws IllegalArgumentException If the specified {@link Set
     *           Set&lt;K&gt;}} is null.
     */
    protected ObservableKeySet(final Set<K> set) {
      super(set);
    }

    private final ThreadLocal<Object> localKey = new ThreadLocal<>();
    private final ThreadLocal<V> localNewValue = new ThreadLocal<>();

    @Override
    protected K beforeAdd(final K element, final Object preventDefault) {
      throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unlikely-arg-type")
    protected boolean beforeRemove(final Object element) {
      localKey.set(element);
      final V value = ObservableMap.this.get(element);
      localNewValue.set(value);
      return ObservableMap.this.beforeRemove(element, value);
    }

    @Override
    protected void afterRemove(final Object element, final RuntimeException e) {
      ObservableMap.this.afterRemove(localKey.get(), localNewValue.get(), e);
    }
  }

  /**
   * Factory method that returns a new instance of an {@link ObservableKeySet}.
   * This method is intended to be overridden by subclasses in order to provide
   * instances of the subclass.
   *
   * @return A new instance of an {@link ObservableKeySet}.
   */
  protected ObservableKeySet newKeySet() {
    return new ObservableKeySet(target.keySet());
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
    RuntimeException exception = null;
    try {
      result = target.containsKey(key);
    }
    catch (final RuntimeException re) {
      exception = re;
    }

    afterContainsKey(key, result, exception);
    if (exception != null)
      throw exception;

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
    RuntimeException exception = null;
    try {
      result = target.containsValue(value);
    }
    catch (final RuntimeException re) {
      exception = re;
    }

    afterContainsValue(value, result, exception);
    if (exception != null)
      throw exception;

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
  public V get(Object key) {
    key = beforeGet(key);
    V value = null;
    RuntimeException exception = null;
    try {
      value = (V)target.get(key);
    }
    catch (final RuntimeException re) {
      exception = re;
    }

    afterGet(key, value, exception);
    if (exception != null)
      throw exception;

    return value;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeContainsKey(Object)} and
   * {@link #afterContainsKey(Object,boolean,RuntimeException)} are called
   * immediately before and after the get operation on the enclosed {@link Map}.
   * <p>
   * The callback methods {@link #beforeGet(Object)} and
   * {@link #afterGet(Object,Object,RuntimeException)} are called immediately
   * before and after the get operation on the enclosed {@link Map}.
   */
  @Override
  public V getOrDefault(final Object key, final V defaultValue) {
    return superGetOrDefault(key, defaultValue);
  }

  /**
   * Main method for the association of a value with a key in this map. This
   * method is the downstream call from {@link #put(Object,Object)},
   * {@link #putIfAbsent(Object,Object)}, {@link #replace(Object,Object)}, and
   * {@link #replace(Object,Object,Object)} for the final "put" operation into
   * the underlying {@link Map}.
   * <p>
   * The callback methods {@link #beforePut(Object,Object,Object,Object)} and
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
  protected V put(final K key, V oldValue, V newValue) {
    final Object beforePut = beforePut(key, oldValue, newValue, preventDefault);
    if (beforePut == preventDefault)
      return oldValue;

    newValue = (V)beforePut;
    RuntimeException exception = null;
    try {
      oldValue = (V)target.put(key, newValue);
    }
    catch (final RuntimeException re) {
      exception = re;
    }

    afterPut(key, oldValue, newValue, exception);
    if (exception != null)
      throw exception;

    return oldValue;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(Object)} and
   * {@link #afterGet(Object,Object,RuntimeException)} are called immediately
   * before and after the get operation on the enclosed {@link Map}.
   * <p>
   * The callback methods {@link #beforePut(Object,Object,Object,Object)} and
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
   * The callback methods {@link #beforePut(Object,Object,Object,Object)} and
   * {@link #afterPut(Object,Object,Object,RuntimeException)} are called
   * immediately before and after the enclosed collection is modified.
   */
  @Override
  public V putIfAbsent(final K key, final V value) {
    return superPutIfAbsent(key, value);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforePut(Object,Object,Object,Object)} and
   * {@link #afterPut(Object,Object,Object,RuntimeException)} are called
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

    RuntimeException exception = null;
    try {
      target.remove(key);
    }
    catch (final RuntimeException re) {
      exception = re;
    }

    afterRemove(key, oldValue, exception);
    if (exception != null)
      throw exception;

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
  public boolean remove(final Object key, final Object value) {
    return superRemove(key, value);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(Object)} and
   * {@link #afterGet(Object,Object,RuntimeException)} are called immediately
   * before and after the get operation on the enclosed {@link Map}.
   * <p>
   * The callback methods {@link #beforePut(Object,Object,Object,Object)} and
   * {@link #afterPut(Object,Object,Object,RuntimeException)} are called
   * immediately before and after the enclosed collection is modified.
   */
  @Override
  public boolean replace(final K key, final V oldValue, final V newValue) {
    return superReplace(key, oldValue, newValue);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(Object)} and
   * {@link #afterGet(Object,Object,RuntimeException)} are called immediately
   * before and after the get operation on the enclosed {@link Map}.
   * <p>
   * The callback methods {@link #beforePut(Object,Object,Object,Object)} and
   * {@link #afterPut(Object,Object,Object,RuntimeException)} are called
   * immediately before and after the enclosed collection is modified.
   */
  @Override
  public V replace(final K key, final V value) {
    return superReplace(key, value);
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
    for (final Iterator<K> i = keySet().iterator(); i.hasNext();) {
      i.next();
      i.remove();
    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(Object)} and
   * {@link #afterGet(Object,Object,RuntimeException)} are called immediately
   * before and after the get operation on the enclosed {@link Map}.
   */
  @Override
  public void forEach(final BiConsumer<? super K,? super V> action) {
    superForEach(action);
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
   * The callback methods {@link #beforePut(Object,Object,Object,Object)} and
   * {@link #afterPut(Object,Object,Object,RuntimeException)} are called
   * immediately before and after the an entry is put in the enclosed
   * collection.
   */
  @Override
  public V compute(final K key, final BiFunction<? super K,? super V,? extends V> remappingFunction) {
    return superCompute(key, remappingFunction);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(Object)} and
   * {@link #afterGet(Object,Object,RuntimeException)} are called immediately
   * before and after the get operation on the enclosed {@link Map}.
   * <p>
   * The callback methods {@link #beforePut(Object,Object,Object,Object)} and
   * {@link #afterPut(Object,Object,Object,RuntimeException)} are called
   * immediately before and after the an entry is put in the enclosed
   * collection.
   */
  @Override
  public V computeIfAbsent(final K key, final Function<? super K,? extends V> mappingFunction) {
    return superComputeIfAbsent(key, mappingFunction);
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
   * The callback methods {@link #beforePut(Object,Object,Object,Object)} and
   * {@link #afterPut(Object,Object,Object,RuntimeException)} are called
   * immediately before and after the an entry is put in the enclosed
   * collection.
   */
  @Override
  public V computeIfPresent(final K key, final BiFunction<? super K,? super V,? extends V> remappingFunction) {
    return superComputeIfPresent(key, remappingFunction);
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
   * The callback methods {@link #beforePut(Object,Object,Object,Object)} and
   * {@link #afterPut(Object,Object,Object,RuntimeException)} are called
   * immediately before and after the an entry is put in the enclosed
   * collection.
   */
  @Override
  public V merge(final K key, final V value, final BiFunction<? super V,? super V,? extends V> remappingFunction) {
    return superMerge(key, value, remappingFunction);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforePut(Object,Object,Object,Object)} and
   * {@link #afterPut(Object,Object,Object,RuntimeException)} are called
   * immediately before and after the an entry is put in the enclosed
   * collection.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void replaceAll(final BiFunction<? super K,? super V,? extends V> function) {
    assertNotNull(function);
    for (final Map.Entry<K,V> entry : entrySet()) {
      final K key = entry.getKey();
      final V oldValue = entry.getValue();
      final Object beforePut = beforePut(key, oldValue, function.apply(key, oldValue), preventDefault);
      if (beforePut == preventDefault)
        continue;

      final V newValue = (V)beforePut;
      RuntimeException exception = null;
      try {
        entry.setValue(newValue);
      }
      catch (final RuntimeException re) {
        exception = re;
      }

      afterPut(key, oldValue, newValue, exception);
    }
  }

  protected volatile ObservableSet<Map.Entry<K,V>> entrySet;

  @Override
  public Set<Map.Entry<K,V>> entrySet() {
    return entrySet == null ? entrySet = newEntrySet() : entrySet;
  }

  protected volatile ObservableSet<K> keySet;

  @Override
  public Set<K> keySet() {
    return keySet == null ? keySet = newKeySet() : keySet;
  }

  protected volatile TransCollection<Map.Entry<K,V>,V> values;

  @Override
  public Collection<V> values() {
    return values == null ? values = new TransCollection<>(entrySet(), Map.Entry::getValue, null) : values;
  }

  private void touchEntries() {
    for (final Iterator<?> i = entrySet().iterator(); i.hasNext(); i.next())
      ;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(Object)} and
   * {@link #afterGet(Object,Object,RuntimeException)} are called immediately
   * before and after each element of the enclosed map is retrieved.
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Map) || size() != ((Map<?,?>)obj).size())
      return false;

    touchEntries();
    return target.equals(obj);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(Object)} and
   * {@link #afterGet(Object,Object,RuntimeException)} are called immediately
   * before and after each element of the enclosed map is retrieved.
   */
  @Override
  public int hashCode() {
    if (target == null)
      return 31;

    touchEntries();
    return 31 + target.hashCode();
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(Object)} and
   * {@link #afterGet(Object,Object,RuntimeException)} are called immediately
   * before and after each element of the enclosed map is retrieved.
   */
  @Override
  public String toString() {
    if (target == null)
      return "null";

    touchEntries();
    return String.valueOf(target);
  }
}