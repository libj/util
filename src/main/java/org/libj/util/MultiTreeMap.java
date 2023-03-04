/* Copyright (c) 2023 LibJ
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
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Supplier;

/**
 * A {@link TreeMap} with keys mapped to zero or more values.
 *
 * @implNote This class does not properly handle {@link TreeMap#keySet()}.
 * @param <K> The type of keys maintained by this map.
 * @param <V> The type of mapped values.
 * @param <C> The type of mapped value {@link Collection}.
 */
public class MultiTreeMap<K,V,C extends Collection<V>> extends TreeMap<K,C> implements MultiMap<K,V,C> {
  private final Supplier<C> multiSupplier;

  /**
   * Creates a new map with the same mappings as the given map, and the provided {@code multiSupplier} for the underlying map
   * instance.
   *
   * @param m The map.
   * @param multiSupplier The {@link Supplier} for value {@link Collection}s of type {@code <C>}.
   * @throws NullPointerException If the specified map is null or the specified map contains a null key and this map does not permit
   *           null keys.
   * @throws NullPointerException If {@code multiSupplier} is null.
   */
  public MultiTreeMap(final Map<? extends K,? extends C> m, final Supplier<C> multiSupplier) {
    super(m);
    this.multiSupplier = Objects.requireNonNull(multiSupplier);
  }

  /**
   * Creates a new map with the same mappings as the given map, and the provided {@code multiSupplier} for the underlying sorted map
   * instance.
   *
   * @param m The sorted map.
   * @param multiSupplier The {@link Supplier} for value {@link Collection}s of type {@code <C>}.
   * @throws NullPointerException If the specified map is null or the specified map contains a null key and this map does not permit
   *           null keys.
   * @throws NullPointerException If {@code multiSupplier} is null.
   */
  public MultiTreeMap(final SortedMap<K,? extends C> m, final Supplier<C> multiSupplier) {
    super(m);
    this.multiSupplier = Objects.requireNonNull(multiSupplier);
  }

  /**
   * Creates a new, empty map with the default initial table size (16), and the provided {@code multiSupplier} for the underlying
   * map instance.
   *
   * @param multiSupplier The {@link Supplier} for value {@link Collection}s of type {@code <C>}.
   * @throws NullPointerException If {@code multiSupplier} is null.
   */
  public MultiTreeMap(final Supplier<C> multiSupplier) {
    super();
    this.multiSupplier = Objects.requireNonNull(multiSupplier);
  }

  /**
   * Creates a new, empty map with an initial table size accommodating the specified number of elements without the need to
   * dynamically resize, and the provided {@code multiSupplier} for the underlying map instance.
   *
   * @param comparator the comparator that will be used to order this map. If {@code null}, the {@linkplain Comparable natural
   *          ordering} of the keys will be used.
   * @param multiSupplier The {@link Supplier} for value {@link Collection}s of type {@code <C>}.
   * @throws IllegalArgumentException If the initial capacity of elements is negative.
   * @throws NullPointerException If {@code multiSupplier} is null.
   */
  public MultiTreeMap(final Comparator<? super K> comparator, final Supplier<C> multiSupplier) {
    super(comparator);
    this.multiSupplier = Objects.requireNonNull(multiSupplier);
  }

  /**
   * Creates a new, empty map with an initial table size accommodating the specified number of elements without the need to
   * dynamically resize, and a null {@code multiSupplier}.
   *
   * @param comparator the comparator that will be used to order this map. If {@code null}, the {@linkplain Comparable natural
   *          ordering} of the keys will be used.
   * @throws IllegalArgumentException If the initial capacity of elements is negative.
   */
  protected MultiTreeMap(final Comparator<? super K> comparator) {
    super(comparator);
    this.multiSupplier = null;
  }

  @Override
  public C newCollection() {
    return multiSupplier.get();
  }
}