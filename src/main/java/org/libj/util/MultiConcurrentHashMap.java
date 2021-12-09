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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * A {@link ConcurrentHashMap} with keys mapped to zero or more values.
 *
 * @implNote This class does not properly handle {@link ConcurrentHashMap#keySet()} and {@link ConcurrentHashMap#keySet(Object)}.
 * @param <K> The type of keys maintained by this map.
 * @param <V> The type of mapped values.
 * @param <C> The type of mapped value {@link Collection}.
 */
public class MultiConcurrentHashMap<K,V,C extends Collection<V>> extends ConcurrentHashMap<K,C> implements MultiMap<K,V,C> {
  private final Supplier<C> multiSupplier;

  /**
   * Creates a new, empty map with an initial table size accommodating the specified number of elements without the need to
   * dynamically resize, and the provided {@code multiSupplier}.
   *
   * @param initialCapacity The implementation performs internal sizing to accommodate this many elements.
   * @param multiSupplier The {@link Supplier} for value {@link Collection}s of type {@code <C>}.
   * @throws IllegalArgumentException If the initial capacity of elements is negative.
   * @throws IllegalArgumentException If {@code multiSupplier} is null.
   */
  public MultiConcurrentHashMap(final int initialCapacity, final Supplier<C> multiSupplier) {
    super(initialCapacity);
    this.multiSupplier = assertNotNull(multiSupplier);
  }

  /**
   * Creates a new map with the same mappings as the given map, and the provided {@code multiSupplier}.
   *
   * @param m The map.
   * @param multiSupplier The {@link Supplier} for value {@link Collection}s of type {@code <C>}.
   * @throws IllegalArgumentException If {@code multiSupplier} is null.
   */
  public MultiConcurrentHashMap(final Map<? extends K,? extends C> m, final Supplier<C> multiSupplier) {
    super(m);
    this.multiSupplier = assertNotNull(multiSupplier);
  }

  /**
   * Creates a new, empty map with an initial table size based on the given number of elements ({@code initialCapacity}), initial
   * table density ({@code loadFactor}), and the provided {@code multiSupplier}.
   *
   * @param initialCapacity The initial capacity. The implementation performs internal sizing to accommodate this many elements,
   *          given the specified load factor.
   * @param loadFactor The load factor (table density) for establishing the initial table size.
   * @param multiSupplier The {@link Supplier} for value {@link Collection}s of type {@code <C>}.
   * @throws IllegalArgumentException If the initial capacity of elements is negative or the load factor is nonpositive.
   * @throws IllegalArgumentException If {@code multiSupplier} is null.
   */
  public MultiConcurrentHashMap(final int initialCapacity, final float loadFactor, final Supplier<C> multiSupplier) {
    this(initialCapacity, loadFactor, 1, multiSupplier);
  }

  /**
   * Creates a new, empty map with an initial table size based on the given number of elements ({@code initialCapacity}), table
   * density ({@code loadFactor}), number of concurrently updating threads ({@code concurrencyLevel}), and the provided
   * {@code multiSupplier}.
   *
   * @param initialCapacity The initial capacity. The implementation performs internal sizing to accommodate this many elements,
   *          given the specified load factor.
   * @param loadFactor The load factor (table density) for establishing the initial table size.
   * @param concurrencyLevel The estimated number of concurrently updating threads. The implementation may use this value as a
   *          sizing hint.
   * @param multiSupplier The {@link Supplier} for value {@link Collection}s of type {@code <C>}.
   * @throws IllegalArgumentException If the initial capacity is negative or the load factor or concurrencyLevel are nonpositive.
   * @throws IllegalArgumentException If {@code multiSupplier} is null.
   */
  public MultiConcurrentHashMap(final int initialCapacity, final float loadFactor, final int concurrencyLevel, final Supplier<C> multiSupplier) {
    super(initialCapacity, loadFactor, concurrencyLevel);
    this.multiSupplier = assertNotNull(multiSupplier);
  }

  /**
   * Creates a new, empty map with the default initial table size (16).
   *
   * @param multiSupplier The {@link Supplier} for value {@link Collection}s of type {@code <C>}.
   * @throws IllegalArgumentException If {@code multiSupplier} is null.
   */
  public MultiConcurrentHashMap(final Supplier<C> multiSupplier) {
    super();
    this.multiSupplier = assertNotNull(multiSupplier);
  }

  /**
   * Creates a new, empty map with an initial table size based on the given number of elements ({@code initialCapacity}), table
   * density ({@code loadFactor}), number of concurrently updating threads ({@code concurrencyLevel}), and a null
   * {@code multiSupplier}.
   *
   * @param initialCapacity The initial capacity. The implementation performs internal sizing to accommodate this many elements,
   *          given the specified load factor.
   * @param loadFactor The load factor (table density) for establishing the initial table size.
   * @param concurrencyLevel The estimated number of concurrently updating threads. The implementation may use this value as a
   *          sizing hint.
   * @throws IllegalArgumentException If the initial capacity is negative or the load factor or concurrencyLevel are nonpositive.
   */
  protected MultiConcurrentHashMap(final int initialCapacity, final float loadFactor, final int concurrencyLevel) {
    super(initialCapacity, loadFactor, concurrencyLevel);
    this.multiSupplier = null;
  }

  @Override
  public C newCollection() {
    return multiSupplier.get();
  }
}