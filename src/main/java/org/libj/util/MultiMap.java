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

import static org.libj.lang.Assertions.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

class MultiMapSupport {
  /**
   * Return a non-null list of values for a given key. The returned list may be empty.
   * <p />
   * If there is no entry for the key in the map, a new empty {@link List} instance is created, registered within the map to hold
   * the values of the key and returned from the method.
   *
   * @param <K> The type of keys maintained by this map.
   * @param <V> The type of mapped values.
   * @param <C> The type of mapped value {@link Collection}.
   * @param map The {@link MultiMap}.
   * @param key The key.
   * @return value The value {@link Collection} registered with the key. The method is guaranteed to never return {@code null}.
   */
  static <K,V,C extends Collection<V>>C getValues(final MultiMap<K,V,C> map, final K key) {
    C l = map.get(key);
    if (l == null)
      map.put(key, l = map.newCollection());

    return l;
  }
}

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
   * Set the value for the key to be a one item {@link Collection} consisting of the supplied value. Any existing values will be
   * replaced.
   *
   * @param key The key.
   * @param value The single value of the key.
   */
  default void putSingle(final K key, final V value) {
    final C values = MultiMapSupport.getValues(this, key);
    values.clear();
    values.add(value);
  }

  /**
   * Add a value to the current {@link Collection} of values for the supplied key.
   *
   * @param key The key.
   * @param value The value to be added.
   */
  default void add(final K key, final V value) {
    final C values = MultiMapSupport.getValues(this, key);
    values.add(value);
  }

  /**
   * Add multiple values to the current list of values for the supplied key. If the supplied array of new values is empty, method
   * returns immediately. Method throws a {@link IllegalArgumentException} if the supplied array of values is null.
   *
   * @param key The key.
   * @param newValues The values to be added.
   * @throws IllegalArgumentException If the supplied array of new values is null.
   */
  @SuppressWarnings("unchecked")
  default void addAll(final K key, final V ... newValues) {
    assertNotNull(newValues, "Supplied array of values must not be null");
    if (newValues.length == 0)
      return;

    final C values = MultiMapSupport.getValues(this, key);
    for (final V value : newValues)
      values.add(value);
  }

  /**
   * Add all values from the supplied value {@link Collection} to the current {@link Collection} of values for the supplied key. If
   * the supplied value list is empty, method returns immediately. Method throws a {@link IllegalArgumentException} if the supplied
   * array of values is null.
   *
   * @param key The key.
   * @param valueList The list of values to be added.
   * @throws IllegalArgumentException If the supplied value list is null.
   */
  default void addAll(final K key, final Collection<V> valueList) {
    assertNotNull(valueList, "Supplied array of values must not be null");
    if (valueList.isEmpty())
      return;

    final C values = MultiMapSupport.getValues(this, key);
    for (V value : valueList)
      values.add(value);
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
   * Add a value to the first position in the current list of values for the supplied key. If the type of the value
   * {@link Collection} does not extend {@link List}, a {@link UnsupportedOperationException} is thrown.
   *
   * @param key The key
   * @param value The value to be added.
   * @throws UnsupportedOperationException If the type of the value {@link Collection} does not extend {@link List}.
   */
  @SuppressWarnings("unchecked")
  default void addFirst(final K key, final V value) {
    final C values = MultiMapSupport.getValues(this, key);
    if (values instanceof List)
      ((List<V>)values).add(0, value);
    else
      throw new UnsupportedOperationException("addFirst is not supported by Collection type: " + values.getClass().getName());
  }

  /**
   * Removes the first value in the value {@link Collection} for the given key.
   *
   * @param key The key.
   * @param value The value in the value {@link Collection} to remove.
   * @return {@code true} if the method resulted in a change to the map.
   */
  default boolean removeValue(final K key, final V value) {
    final C values = MultiMapSupport.getValues(this, key);
    return values != null && values.remove(value);
  }

  /**
   * Removes the first value in the value {@link Collection} for the given key for which the provided {@link Predicate} returns
   * {@code true}.
   *
   * @param key The key.
   * @param test The {@link Predicate} to test whether a value is to be removed.
   * @return {@code true} if the method resulted in a change to the map.
   */
  default boolean removeIf(final K key, final Predicate<? super V> test) {
    final C values = MultiMapSupport.getValues(this, key);
    if (values == null)
      return false;

    final Iterator<V> i = values.iterator();
    while (i.hasNext()) {
      final V v = i.next();
      if (test.test(v)) {
        i.remove();
        return true;
      }
    }

    return false;
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

    for (final Entry<K,C> e : entrySet()) {
      final C olist = otherMap.get(e.getKey());
      if (e.getValue().size() != olist.size())
        return false;

      for (final V v : e.getValue())
        if (!olist.contains(v))
          return false;
    }

    return true;
  }
}