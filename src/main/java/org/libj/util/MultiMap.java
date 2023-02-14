/* Copyright (c) 2021 LibJ
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
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.function.Predicate;

/**
 * A {@link Map} with keys mapped to zero or more values.
 *
 * @param <K> The type of keys maintained by this map.
 * @param <V> The type of mapped values.
 * @param <C> The type of mapped value {@link Collection}.
 */
public interface MultiMap<K,V,C extends Collection<V>> extends Map<K,C> {
  /**
   * Returns a new instance of the value {@link Collection}.
   *
   * @return A new instance of the value {@link Collection}.
   */
  C newCollection();

  /**
   * Return a non-null list of values for a given key. The returned list may be empty.
   * <p>
   * If there is no entry for the key in the map, a new empty {@link List} instance is created, registered within the map to hold
   * the values of the key and returned from the method.
   *
   * @param key The key.
   * @return value The value {@link Collection} registered with the key. The method is guaranteed to never return {@code null}.
   */
  default C getOrNew(final K key) {
    C l = get(key);
    if (l == null)
      put(key, l = newCollection());

    return l;
  }

  /**
   * Set the value for the key to be a one item {@link Collection} consisting of the provided value. Any existing values will be
   * replaced.
   *
   * @param key The key.
   * @param value The single value of the key.
   * @return The value {@link Collection} registered with the key. The method is guaranteed to never return {@code null}.
   */
  default C putSingle(final K key, final V value) {
    final C values = getOrNew(key);
    values.clear();
    values.add(value);
    return values;
  }

  /**
   * Add a value to the current {@link Collection} of values for the provided key.
   *
   * @param key The key.
   * @param value The value to be added.
   * @return The value {@link Collection} registered with the key. The method is guaranteed to never return {@code null}.
   */
  default C add(final K key, final V value) {
    final C values = getOrNew(key);
    values.add(value);
    return values;
  }

  /**
   * Add multiple values to the current list of values for the provided key. If the provided array of new values is empty, method
   * returns immediately. Method throws a {@link IllegalArgumentException} if the provided array of values is null.
   *
   * @param key The key.
   * @param newValues The values to be added.
   * @return The value {@link Collection} registered with the key. The method is guaranteed to never return {@code null}.
   * @throws NullPointerException If the provided array of new values is null.
   */
  @SuppressWarnings("unchecked")
  default C addAll(final K key, final V ... newValues) {
    final C values = getOrNew(key);
    for (final V value : newValues) // [A]
      values.add(value);

    return values;
  }

  /**
   * Add all values from the provided value {@link Collection} to the current {@link Collection} of values for the provided key. If
   * the provided value list is empty, method returns immediately. Method throws a {@link IllegalArgumentException} if the provided
   * array of values is null.
   *
   * @param key The key.
   * @param newValues The list of values to be added.
   * @return The value {@link Collection} registered with the key. The method is guaranteed to never return {@code null}.
   * @throws NullPointerException If the provided value list is null.
   */
  default C addAll(final K key, final Collection<V> newValues) {
    final C values = getOrNew(key);
    CollectionUtil.addAll(values, newValues);
    return values;
  }

  /**
   * Returns the first value for the provided key, or {@code null} if the keys does not map to a value.
   *
   * @param key The key.
   * @return The first value for the provided key, or {@code null} if the keys does not map to a value.
   */
  @SuppressWarnings("unchecked")
  default V getFirst(final K key) {
    final C values = get(key);
    if (values == null || values.size() == 0)
      return null;

    return values instanceof List ? ((List<V>)values).get(0) : values.iterator().next();
  }

  /**
   * Add a value to the first position in the current list of values for the provided key. If the type of the value
   * {@link Collection} does not extend {@link List}, an {@link UnsupportedOperationException} is thrown.
   *
   * @param key The key
   * @param value The value to be added.
   * @return The value {@link Collection} registered with the key. The method is guaranteed to never return {@code null}.
   * @throws UnsupportedOperationException If the type of the value {@link Collection} does not extend {@link List}.
   */
  @SuppressWarnings("unchecked")
  default C addFirst(final K key, final V value) {
    final C values = getOrNew(key);
    if (!(values instanceof List))
      throw new UnsupportedOperationException("addFirst is not supported by Collection type: " + values.getClass().getName());

    ((List<V>)values).add(0, value);
    return values;
  }

  /**
   * Removes the first value in the value {@link Collection} for the given key.
   *
   * @param key The key.
   * @param value The value in the value {@link Collection} to remove.
   * @return {@code true} if the method resulted in a change to the map.
   * @throws UnsupportedOperationException If the value {@link Collection} for the given key does not support the {@code remove}
   *           operation.
   */
  default boolean removeValue(final K key, final V value) {
    final C values = get(key);
    return values != null && values.remove(value);
  }

  /**
   * Removes the first value in the value {@link Collection} for the given key for which the provided {@link Predicate} returns
   * {@code true}.
   *
   * @param key The key.
   * @param test The {@link Predicate} to test whether a value is to be removed.
   * @return {@code true} if the method resulted in a change to the map.
   * @throws NullPointerException If {@code test} is null.
   * @throws UnsupportedOperationException If the value {@link Collection} for the given key does not support the {@code remove}
   *           operation.
   */
  default boolean removeIf(final K key, final Predicate<? super V> test) {
    final C values = get(key);
    if (values == null)
      return false;

    if (values instanceof List && values instanceof RandomAccess) {
      @SuppressWarnings("unchecked")
      final List<V> l = (List<V>)values;
      for (int i = 0, i$ = values.size(); i < i$; ++i) { // [RA]
        if (test.test(l.get(i))) {
          values.remove(i);
          return true;
        }
      }
    }
    else {
      for (final Iterator<V> i = values.iterator(); i.hasNext();) { // [I]
        if (test.test(i.next())) {
          i.remove();
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Removes the all values in the value {@link Collection} for the given key for which the provided {@link Predicate} returns
   * {@code true}.
   *
   * @param key The key.
   * @param test The {@link Predicate} to test whether a value is to be removed.
   * @return {@code true} if the method resulted in a change to the map.
   * @throws NullPointerException If {@code test} is null.
   * @throws UnsupportedOperationException If the value {@link Collection} for the given key does not support the {@code remove}
   *           operation.
   */
  default boolean removeAllIf(final K key, final Predicate<? super V> test) {
    final C values = get(key);
    if (values == null)
      return false;

    boolean changed = false;
    if (values instanceof List && values instanceof RandomAccess) {
      @SuppressWarnings("unchecked")
      final List<V> l = (List<V>)values;
      for (int i = 0, i$ = values.size(); i < i$; ++i) { // [RA]
        if (test.test(l.get(i))) {
          changed |= values.remove(i);
        }
      }
    }
    else {
      for (final Iterator<V> i = values.iterator(); i.hasNext();) { // [I]
        if (test.test(i.next())) {
          i.remove();
          changed = true;
        }
      }
    }

    return changed;
  }

  /**
   * Compare the specified map with this map for equality modulo the order of values for each key. Specifically, the values
   * associated with each key are compared as if they were ordered lists.
   *
   * @param otherMap Map to be compared to this one.
   * @return {@code true} if the maps are equal modulo value ordering.
   */
  default boolean equalsIgnoreValueOrder(final MultiMap<K,V,C> otherMap) {
    if (otherMap == this)
      return true;

    if (!keySet().equals(otherMap.keySet()))
      return false;

    if (size() > 0) {
      for (final Entry<K,C> entry : entrySet()) { // [S]
        final C olist = otherMap.get(entry.getKey());
        final C value = entry.getValue();
        final int size = value.size();
        if (size != olist.size() || size > 0 && !CollectionUtil.containsAll(olist, value))
          return false;
      }
    }

    return true;
  }
}