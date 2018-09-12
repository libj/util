/* Copyright (c) 2018 FastJAX
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

package org.fastjax.util;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A concurrent HashSet implementation backed by a <tt>ConcurrentHashMap</tt>.
 */
public class ConcurrentHashSet<E> extends HashSet<E> {
  private static class ClonableConcurrentHashMap<K,V> extends ConcurrentHashMap<K,V> {
    private static final long serialVersionUID = -3259218346584869178L;

    public ClonableConcurrentHashMap(final int initialCapacity) {
      super(initialCapacity);
    }

    public ClonableConcurrentHashMap() {
      super();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ConcurrentHashMap<K,V> clone() {
      try {
        return (ConcurrentHashMap<K,V>)super.clone();
      }
      catch (final CloneNotSupportedException e) {
        throw new UnsupportedOperationException(e);
      }
    }
  }

  private static final long serialVersionUID = -2016698281843655212L;

  /**
   * The default initial table capacity. Must be a power of 2
   * (i.e., at least 1) and at most MAXIMUM_CAPACITY.
   */
  private static final int DEFAULT_CAPACITY = 16;

  private transient ClonableConcurrentHashMap<E,Object> map;

  // Dummy value to associate with an Object in the backing Map
  private static final Object PRESENT = new Object();

  /**
   * Constructs a new, empty set; the backing <tt>ConcurrentHashMap</tt> instance has
   * default expected maximum size (16).
   */
  public ConcurrentHashSet() {
    map = new ClonableConcurrentHashMap<>();
  }

  /**
   * Constructs a new set containing the elements in the specified
   * collection. The <tt>ConcurrentHashMap</tt> is created with default load factor
   * (0.75) and an initial capacity sufficient to contain the elements in
   * the specified collection.
   *
   * @param c
   *          the collection whose elements are to be placed into this set
   * @throws NullPointerException
   *           if the specified collection is null
   */
  public ConcurrentHashSet(final Collection<? extends E> c) {
    map = new ClonableConcurrentHashMap<>(DEFAULT_CAPACITY);
    addAll(c);
  }

  /**
   * Constructs a new, empty set; the backing <tt>ConcurrentHashMap</tt> instance has
   * the specified initial capacity and default load factor (0.75).
   *
   * @param initialCapacity
   *          the initial capacity of the hash table
   * @throws IllegalArgumentException
   *           if the initial capacity is less
   *           than zero
   */
  public ConcurrentHashSet(final int initialCapacity) {
    map = new ClonableConcurrentHashMap<>(initialCapacity);
  }

  /**
   * Returns an iterator over the elements in this set. The elements
   * are returned in no particular order.
   *
   * @return an Iterator over the elements in this set
   * @see ConcurrentModificationException
   */
  @Override
  public Iterator<E> iterator() {
    return map.keySet().iterator();
  }

  /**
   * @return the number of elements in this set (its cardinality)
   */
  @Override
  public int size() {
    return map.size();
  }

  /**
   * @return <tt>true</tt> if this set contains no elements
   */
  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  /**
   * Returns <tt>true</tt> if this set contains the specified element.
   * More formally, returns <tt>true</tt> if and only if this set
   * contains an element <tt>e</tt> such that <tt>(o == e)</tt>.
   *
   * @param o
   *          element whose presence in this set is to be tested
   * @return <tt>true</tt> if this set contains the specified element
   */
  @Override
  public boolean contains(final Object o) {
    return map.containsKey(o);
  }

  /**
   * Adds the specified element to this set if it is not already present.
   * More formally, adds the specified element <tt>e</tt> to this set if
   * this set contains no element <tt>e2</tt> such that <tt>(e == e2)</tt>.
   * If this set already contains the element, the call leaves the set
   * unchanged and returns <tt>false</tt>.
   *
   * @param e
   *          element to be added to this set
   * @return <tt>true</tt> if this set did not already contain the specified
   *         element
   */
  @Override
  public boolean add(final E e) {
    return map.put(e, PRESENT) == null;
  }

  /**
   * Removes the specified element from this set if it is present.
   * More formally, removes an element <tt>e</tt> such that <tt>(o == e)</tt>,
   * if this set contains such an element. Returns <tt>true</tt> if
   * this set contained the element (or equivalently, if this set
   * changed as a result of the call). (This set will not contain the
   * element once the call returns.)
   *
   * @param o
   *          object to be removed from this set, if present
   * @return <tt>true</tt> if the set contained the specified element
   */
  @Override
  public boolean remove(final Object o) {
    return map.remove(o) == PRESENT;
  }

  /**
   * Removes all of the elements from this set.
   * The set will be empty after this call returns.
   */
  @Override
  public void clear() {
    map.clear();
  }

  /**
   * Returns a shallow copy of this <tt>ConcurrentHashSet</tt> instance: the elements
   * themselves are not cloned.
   *
   * @return a shallow copy of this set
   */
  @Override
  @SuppressWarnings("unchecked")
  public ConcurrentHashSet<E> clone() {
    final ConcurrentHashSet<E> newSet = (ConcurrentHashSet<E>)super.clone();
    newSet.map = (ClonableConcurrentHashMap<E,Object>)map.clone();
    return newSet;
  }

  /**
   * Save the state of this <tt>ConcurrentHashSet</tt> instance to a stream (that
   * is, serialize it).
   *
   * @serialData The capacity of the backing <tt>ConcurrentHashMap</tt> instance
   *             (int), followed by the size of the set (the number of elements it
   *             contains) (int), followed by all of its elements (each an Object)
   *             in no particular order.
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
   * Reconstitute the <tt>ConcurrentHashSet</tt> instance from a stream (that is,
   * deserialize it).
   */
  private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
    // Read in any hidden serialization magic
    s.defaultReadObject();

    // Read capacity and verify non-negative.
    int capacity = s.readInt();
    if (capacity < 0)
      throw new InvalidObjectException("Illegal capacity: " + capacity);

    // Read size and verify non-negative.
    final int size = s.readInt();
    if (size < 0)
      throw new InvalidObjectException("Illegal size: " + size);

    // Create backing ConcurrentHashMap
    map = new ClonableConcurrentHashMap<>(Math.max((int)(capacity / .75f) + 1, 16));

    // Read in all elements in the proper order.
    for (int i = 0; i < size; i++) {
      @SuppressWarnings("unchecked")
      final E e = (E)s.readObject();
      map.put(e, PRESENT);
    }
  }

  /**
   * Creates a <em><a href="Spliterator.html#binding">late-binding</a></em>
   * and <em>fail-fast</em> {@link Spliterator} over the elements in this
   * set.
   *
   * <p>The {@code Spliterator} reports {@link Spliterator#SIZED} and
   * {@link Spliterator#DISTINCT}.  Overriding implementations should document
   * the reporting of additional characteristic values.
   *
   * @return a {@code Spliterator} over the elements in this set
   */
  @Override
  public Spliterator<E> spliterator() {
    return map.keySet().spliterator();
  }
}