/* Copyright (c) 2018 LibJ
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

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A concurrent {@link HashSet} implementation backed by a {@link ConcurrentHashMap}.
 *
 * @param <E> The type of elements maintained by this set.
 */
public class ConcurrentHashSet<E> extends HashSet<E> {
  private static final class CloneableConcurrentHashMap<K,V> extends ConcurrentNullHashMap<K,V> {
    private CloneableConcurrentHashMap(final int initialCapacity) {
      super(initialCapacity);
    }

    private CloneableConcurrentHashMap() {
      super();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected CloneableConcurrentHashMap<K,V> clone() {
      try {
        return (CloneableConcurrentHashMap<K,V>)super.clone();
      }
      catch (final CloneNotSupportedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  /**
   * The default initial table capacity. Must be a power of 2 (i.e., at least 1) and at most
   * {@code ConcurrentHashMap.MAXIMUM_CAPACITY}.
   */
  private static final int DEFAULT_CAPACITY = 16;

  private transient CloneableConcurrentHashMap<E,Object> map;

  // Dummy value to associate with an Object in the backing Map
  private static final Object PRESENT = new Object();

  /**
   * Constructs a new, empty set; the backing {@link ConcurrentHashMap} instance has default expected maximum size (16).
   */
  public ConcurrentHashSet() {
    map = new CloneableConcurrentHashMap<>();
  }

  /**
   * Constructs a new set containing the elements in the specified collection. The {@link ConcurrentHashMap} is created with default
   * load factor (0.75) and an initial capacity sufficient to contain the elements in the specified collection.
   *
   * @param c The collection whose elements are to be placed into this set.
   * @throws IllegalArgumentException If the specified collection is null.
   */
  public ConcurrentHashSet(final Collection<? extends E> c) {
    map = new CloneableConcurrentHashMap<>(DEFAULT_CAPACITY);
    addAll(assertNotNull(c));
  }

  /**
   * Constructs a new, empty set; the backing {@link ConcurrentHashMap} instance has the specified initial capacity and default load
   * factor (0.75).
   *
   * @param initialCapacity The initial capacity of the hash table.
   * @throws IllegalArgumentException If the initial capacity is less than zero.
   */
  public ConcurrentHashSet(final int initialCapacity) {
    map = new CloneableConcurrentHashMap<>(initialCapacity);
  }

  @Override
  public Iterator<E> iterator() {
    return map.keySet().iterator();
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public boolean contains(final Object o) {
    return map.containsKey(o);
  }

  @Override
  public boolean add(final E e) {
    return map.put(e, PRESENT) == null;
  }

  @Override
  public boolean remove(final Object o) {
    return map.remove(o) == PRESENT;
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public Spliterator<E> spliterator() {
    return map.keySet().spliterator();
  }

  /**
   * Returns a shallow copy of this {@link ConcurrentHashSet} instance (the elements themselves are not cloned).
   *
   * @return A shallow copy of this {@link ConcurrentHashSet} instance.
   */
  @Override
  @SuppressWarnings("unchecked")
  public ConcurrentHashSet<E> clone() {
    final ConcurrentHashSet<E> newSet = (ConcurrentHashSet<E>)super.clone();
    newSet.map = map.clone();
    return newSet;
  }

  /**
   * Save the state of this {@link ConcurrentHashSet} instance to a stream (that is, serialize it).
   *
   * @serialData The capacity of the backing {@link ConcurrentHashMap} instance (int), followed by the size of the set (the number
   *             of elements it contains) (int), followed by all of its elements (each an Object) in no particular order.
   * @param s The {@link ObjectInputStream}.
   * @throws IOException If an I/O error has occurred.
   */
  private void writeObject(final ObjectOutputStream s) throws IOException {
    // Write out any hidden serialization magic
    s.defaultWriteObject();

    // Write out ConcurrentHashMap capacity and load factor
    s.writeInt(map.size());

    // Write out size
    s.writeInt(map.size());

    // Write out all elements in the proper order.
    for (final E e : map.keySet())
      s.writeObject(e);
  }

  /**
   * Reconstitute the {@link ConcurrentHashSet} instance from a stream (that is, deserialize it).
   *
   * @param s The {@link ObjectInputStream}.
   * @throws ClassNotFoundException If the class of a serialized object could not be found.
   * @throws IOException If an I/O error has occurred.
   */
  private void readObject(final ObjectInputStream s) throws ClassNotFoundException, IOException {
    // Read in any hidden serialization magic
    s.defaultReadObject();

    // Read capacity and verify non-negative.
    final int capacity = s.readInt();
    if (capacity < 0)
      throw new InvalidObjectException("Illegal capacity: " + capacity);

    // Read size and verify non-negative.
    final int size = s.readInt();
    if (size < 0)
      throw new InvalidObjectException("Illegal size: " + size);

    // Create backing ConcurrentHashMap
    map = new CloneableConcurrentHashMap<>(Math.max((int)(capacity / .75f) + 1, 16));

    // Read in all elements in the proper order.
    for (int i = 0; i < size; ++i) {
      @SuppressWarnings("unchecked")
      final E e = (E)s.readObject();
      map.put(e, PRESENT);
    }
  }
}