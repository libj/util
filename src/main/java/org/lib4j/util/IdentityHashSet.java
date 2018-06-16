/* Copyright (c) 2017 lib4j
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

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.TreeSet;

/**
 * This class implements the <tt>Set</tt> interface, backed by a hash table
 * (actually a <tt>IdentityHashMap</tt> instance). It makes no guarantees as
 * to the iteration order of the set; in particular, it does not guarantee that
 * the order will remain constant over time. This class permits the
 * <tt>null</tt> element.
 *
 * <p>
 * This class offers constant time performance for the basic operations
 * (<tt>add</tt>, <tt>remove</tt>, <tt>contains</tt> and <tt>size</tt>),
 * assuming the hash function disperses the elements properly among the
 * buckets. Iterating over this set requires time proportional to the sum of
 * the <tt>IdentityHashSet</tt> instance's size (the number of elements) plus
 * the "capacity" of the backing <tt>IdentityHashMap</tt> instance (the number
 * of buckets). Thus, it's very important not to set the initial capacity too
 * high (or the load factor too low) if iteration performance is important.
 *
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong>
 * If multiple threads access a hash set concurrently, and at least one of
 * the threads modifies the set, it <i>must</i> be synchronized externally.
 * This is typically accomplished by synchronizing on some object that
 * naturally encapsulates the set.
 *
 * If no such object exists, the set should be "wrapped" using the
 * {@link Collections#synchronizedSet Collections.synchronizedSet}
 * method. This is best done at creation time, to prevent accidental
 * unsynchronized access to the set:
 *
 * <pre>
 *   Set s = Collections.synchronizedSet(new IdentityHashSet(...));
 * </pre>
 *
 * <p>
 * The iterators returned by this class's <tt>iterator</tt> method are
 * <i>fail-fast</i>: if the set is modified at any time after the iterator is
 * created, in any way except through the iterator's own <tt>remove</tt>
 * method, the Iterator throws a {@link ConcurrentModificationException}.
 * Thus, in the face of concurrent modification, the iterator fails quickly
 * and cleanly, rather than risking arbitrary, non-deterministic behavior at
 * an undetermined time in the future.
 *
 * <p>
 * Note that the fail-fast behavior of an iterator cannot be guaranteed
 * as it is, generally speaking, impossible to make any hard guarantees in the
 * presence of unsynchronized concurrent modification. Fail-fast iterators
 * throw <tt>ConcurrentModificationException</tt> on a best-effort basis.
 * Therefore, it would be wrong to write a program that depended on this
 * exception for its correctness: <i>the fail-fast behavior of iterators
 * should be used only to detect bugs.</i>
 *
 * @param <E>
 *          the type of elements maintained by this set
 *
 * @see Collection
 * @see Set
 * @see TreeSet
 * @see IdentityHashMap
 */
public class IdentityHashSet<E> extends HashSet<E> {
  private static final long serialVersionUID = -1418532724651347957L;

  /**
   * The maximum capacity, used if a higher value is implicitly specified
   * by either of the constructors with arguments.
   * MUST be a power of two <= 1<<30.
   */
  static final int MAXIMUM_CAPACITY = 1 << 30;

  private transient IdentityHashMap<E,Object> map;

  // Dummy value to associate with an Object in the backing Map
  private static final Object PRESENT = new Object();

  /**
   * Constructs a new, empty set; the backing <tt>IdentityHashMap</tt> instance has
   * default expected maximum size (21).
   */
  public IdentityHashSet() {
    map = new IdentityHashMap<>();
  }

  /**
   * Constructs a new set containing the elements in the specified
   * collection. The <tt>IdentityHashMap</tt> is created with default load factor
   * (0.75) and an initial capacity sufficient to contain the elements in
   * the specified collection.
   *
   * @param c
   *          the collection whose elements are to be placed into this set
   * @throws NullPointerException
   *           if the specified collection is null
   */
  public IdentityHashSet(final Collection<? extends E> c) {
    map = new IdentityHashMap<>(Math.max((int)(c.size() / .75f) + 1, 16));
    addAll(c);
  }

  /**
   * Constructs a new, empty set; the backing <tt>IdentityHashMap</tt> instance has
   * the specified initial capacity and default load factor (0.75).
   *
   * @param initialCapacity
   *          the initial capacity of the hash table
   * @throws IllegalArgumentException
   *           if the initial capacity is less
   *           than zero
   */
  public IdentityHashSet(final int initialCapacity) {
    map = new IdentityHashMap<>(initialCapacity);
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
   * Returns a shallow copy of this <tt>IdentityHashSet</tt> instance: the elements
   * themselves are not cloned.
   *
   * @return a shallow copy of this set
   */
  @Override
  @SuppressWarnings("unchecked")
  public IdentityHashSet<E> clone() {
    final IdentityHashSet<E> newSet = (IdentityHashSet<E>)super.clone();
    newSet.map = (IdentityHashMap<E,Object>)map.clone();
    return newSet;
  }

  /**
   * Save the state of this <tt>IdentityHashSet</tt> instance to a stream (that
   * is, serialize it).
   *
   * @serialData The capacity of the backing <tt>IdentityHashMap</tt> instance
   *             (int), followed by the size of the set (the number of elements it
   *             contains) (int), followed by all of its elements (each an Object)
   *             in no particular order.
   */
  private void writeObject(final ObjectOutputStream s) throws IOException {
    // Write out any hidden serialization magic
    s.defaultWriteObject();

    // Write out IdentityHashMap capacity and load factor
    s.writeInt(map.size());

    // Write out size
    s.writeInt(map.size());

    // Write out all elements in the proper order.
    for (final E e : map.keySet())
      s.writeObject(e);
  }

  /**
   * Reconstitute the <tt>IdentityHashSet</tt> instance from a stream (that is,
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

    // Create backing IdentityHashMap
    map = new IdentityHashMap<E,Object>(Math.max((int)(capacity / .75f) + 1, 16));

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