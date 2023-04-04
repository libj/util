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

package org.libj.util.primitive;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

import org.libj.util.CollectionUtil;

/**
 * An {@link <X>Set} implementing <a href="https://en.wikipedia.org/wiki/Open_addressing">open-addressing (closed hashing) with
 * linear-probing for collision resolution</a> algorithm, with allocation-free operation in steady state when expanded.
 * <p>
 * This class replicates the API of the {@link java.util.HashSet} class by defining synonymous methods for a set of {@code <x>}
 * values instead of Object references.
 */
public class Hash<X>Set extends HashPrimitiveSet implements <X>Set {
  /**
   * The load factor used when none specified in constructor.
   */
  static final float DEFAULT_LOAD_FACTOR = 0.55f;

  /**
   * Value that represents null in {@link #valueData}.
   */
  static final <x> NULL = 0;

  private final float loadFactor;
  private int resizeThreshold;

  /**
   * Whether this set contains the value representing {@link #NULL}.
   */
  private boolean containsNull;
  private <x>[] valueData;
  private int size;
  private transient int modCount;

  /**
   * Creates an empty {@link Hash<X>Set} with the default initial capacity (16) and the default load factor (0.55).
   */
  public Hash<X>Set() {
    this(16);
  }

  /**
   * Creates an empty {@link Hash<X>Set} with the specified initial capacity and
   * load factor.
   *
   * @param initialCapacity The initial capacity.
   * @param loadFactor The load factor.
   * @throws IllegalArgumentException If the initial capacity is negative or the
   *           load factor less than {@code .1} or greater than {@code .9}.
   */
  public Hash<X>Set(final int initialCapacity, final float loadFactor) {
    if (loadFactor < .1f || Float.isNaN(loadFactor) || .9f < loadFactor)
      throw new IllegalArgumentException("Illegal load factor: " + loadFactor);

    this.loadFactor = loadFactor;
    this.size = 0;

    final int capacity = findNextPositivePowerOfTwo(initialCapacity);
    this.resizeThreshold = (int)(capacity * loadFactor);
    this.valueData = new <x>[capacity];
  }

  /**
   * Creates an empty {@link Hash<X>Set} with the specified initial capacity and the default load factor (0.55).
   *
   * @param initialCapacity The initial capacity.
   * @throws IllegalArgumentException If the initial capacity is negative.
   */
  public Hash<X>Set(final int initialCapacity) {
    this(initialCapacity, DEFAULT_LOAD_FACTOR);
  }

  /**
   * Creates a new {@link Hash<X>Set} with the same values as the specified
   * collection. The {@link Hash<X>Set} is created with default load factor
   * (0.55) and an initial capacity sufficient to hold the mappings in the
   * specified collection.
   *
   * @param c The collection whose values are to be added to this set.
   * @throws NullPointerException If the specified set is null.
   */
  public Hash<X>Set(final <X>Collection c) {
    this(c.size());
    addAll(c);
  }

  /**
   * Creates a new {@link Hash<X>Set} with the same values as the specified
   * collection. The {@link Hash<X>Set} is created with default load factor
   * (0.55) and an initial capacity sufficient to hold the mappings in the
   * specified collection.
   *
   * @param c The collection whose values are to be added to this set.
   * @throws NullPointerException If the specified set is null.
   */
  public Hash<X>Set(final Collection<<XX>> c) {
    this(c.size());
    addAll(c);
  }

  @Override
  public boolean add(final <x> value) {
    if (value == NULL) {
      if (containsNull)
        return false;

      ++modCount;
      return containsNull = true;
    }

    final int mask = valueData.length - 1;
    int index = hash(<XX>.hashCode(value), mask);
    for (; valueData[index] != NULL; index = nextIndex(index, mask)) // [A]
      if (valueData[index] == value)
        return false;

    ++modCount;
    valueData[index] = value;
    if (++size > resizeThreshold)
      rehash(valueData.length * 2);

    return true;
  }

  @Override
  public boolean addAll(final <X>Collection c) {
    final int i$ = c.size();
    if (i$ == 0)
      return false;

    boolean changed = false;
    if (c instanceof <X>List && c instanceof RandomAccess) {
      final <X>List l = (<X>List)c;
      int i = 0; do // [RA]
        changed |= add(l.get(i));
      while (++i < i$);
    }
    else {
      final <X>Iterator i = c.iterator(); do // [I]
        changed |= add(i.next());
      while (i.hasNext());
    }

    return changed;
  }

  @Override
  public boolean addAll(final Collection<<XX>> c) {
    final int i$ = c.size();
    if (i$ == 0)
      return false;

    boolean changed = false;
    final List<<XX>> l;
    if (c instanceof List && CollectionUtil.isRandomAccess(l = (List<<XX>>)c)) {
      int i = 0; do // [RA]
        changed |= add(l.get(i));
      while (++i < i$);
    }
    else {
      final Iterator<<XX>> it = c.iterator(); do // [I]
        changed |= add(it.next());
      while (it.hasNext());
    }

    return changed;
  }

  /**
   * Adds all of the values in the specified set to this set if they're not
   * already present. The {@code addAll} operation effectively modifies this set
   * so that its value is the <i>union</i> of the two sets. The behavior of this
   * operation is undefined if the specified collection is modified while the
   * operation is in progress.
   *
   * @param s Set containing values to be added to this set.
   * @return {@code true} if this set changed as a result of the call.
   * @throws NullPointerException If the specified set is null.
   * @see #addAll(<X>Collection)
   * @see #add(<x>)
   */
  public boolean addAll(final Hash<X>Set s) {
    if (s.size() == 0)
      return false;

    boolean changed = false;
    for (final <x> value : s.valueData) // [A]
      if (value != NULL)
        changed |= add(value);

    if (s.containsNull)
      changed |= add(NULL);

    return changed;
  }

  @Override
  public boolean contains(final <x> value) {
    if (value == NULL)
      return containsNull;

    final int mask = valueData.length - 1;
    for (int index = hash(<XX>.hashCode(value), mask); valueData[index] != NULL; index = nextIndex(index, mask)) // [A]
      if (valueData[index] == value)
        return true;

    return false;
  }

  @Override
  public boolean containsAll(final <X>Collection c) {
    final int i$ = c.size();
    if (i$ == 0)
      return true;

    if (c instanceof <X>List && c instanceof RandomAccess) {
      final <X>List l = (<X>List)c;
      int i = 0; do // [RA]
        if (!contains(l.get(i)))
          return false;
      while (++i < i$);
    }
    else {
      final <X>Iterator i = c.iterator(); do // [I]
        if (!contains(i.next()))
          return false;
      while(i.hasNext());
    }

    return true;
  }

  @Override
  public boolean containsAll(final Collection<<XX>> c) {
    final int i$ = c.size();
    if (i$ == 0)
      return true;

    final List<<XX>> l;
    if (c instanceof List && CollectionUtil.isRandomAccess(l = (List<<XX>>)c)) {
      int i = 0; do // [RA]
        if (!contains(l.get(i)))
          return false;
      while (++i < i$);
    }
    else {
      final Iterator<<XX>> it = c.iterator(); do // [I]
        if (!contains(it.next()))
          return false;
      while (it.hasNext());
    }

    return true;
  }

  @Override
  public boolean remove(final <x> value) {
    if (value == NULL) {
      if (!containsNull)
        return false;

      ++modCount;
      containsNull = false;
      return true;
    }

    final int mask = valueData.length - 1;
    for (int index = hash(<XX>.hashCode(value), mask); valueData[index] != NULL; index = nextIndex(index, mask)) { // [A]
      if (valueData[index] == value) {
        ++modCount;
        valueData[index] = NULL;
        compactChain(index);
        --size;
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean removeAll(final <x> ... a) {
    boolean changed = false;
    for (int i = 0, i$ = a.length; i < i$; ++i) // [A]
      changed |= remove(a[i]);

    return changed;
  }

  @Override
  public boolean removeAll(final <X>Collection c) {
    final int i$ = c.size();
    if (i$ == 0)
      return false;

    boolean changed = false;
    if (c instanceof <X>List && c instanceof RandomAccess) {
      final <X>List l = (<X>List)c;
      int i = 0; do // [RA]
        changed |= remove(l.get(i));
      while (++i < i$);
    }
    else {
      final <X>Iterator i = c.iterator(); do // [I]
        changed |= remove(i.next());
      while (i.hasNext());
    }

    return changed;
  }

  @Override
  public boolean removeAll(final Collection<<XX>> c) {
    final int i$ = c.size();
    if (i$ == 0)
      return false;

    boolean changed = false;
    final List<<XX>> l;
    if (c instanceof List && CollectionUtil.isRandomAccess(l = (List<<XX>>)c)) {
      int i = 0; do // [RA]
        changed |= remove(l.get(i));
      while (++i < i$);
    }
    else {
      final Iterator<<XX>> it = c.iterator(); do // [I]
        changed |= remove(it.next());
      while (it.hasNext());
    }

    return changed;
  }

  /**
   * Removes from this set all of its values that are contained in the specified
   * set. This operation effectively modifies this set so that its value is the
   * <i>asymmetric set difference</i> of the two sets.
   *
   * @param s Set containing values to be removed from this set.
   * @return {@code true} if this set changed as a result of the call.
   * @throws NullPointerException If the specified collection is null.
   * @see #removeAll(<X>Collection)
   * @see #remove(<x>)
   * @see #contains(<x>)
   */
  public boolean removeAll(final Hash<X>Set s) {
    boolean changed = false;
    for (final <x> value : s.valueData) // [A]
      if (value != NULL)
        changed |= remove(value);

    if (s.containsNull)
      changed |= remove(NULL);

    return changed;
  }

  @Override
  public boolean retainAll(final <X>Collection c) {
    final <x>[] values = new <x>[this.valueData.length];
    System.arraycopy(this.valueData, 0, values, 0, values.length);

    boolean changed = false;
    for (final <x> value : values) // [A]
      if (!c.contains(value))
        changed |= remove(value);

    return changed;
  }

  @Override
  public boolean retainAll(final Collection<<XX>> c) {
    final <x>[] values = new <x>[this.valueData.length];
    System.arraycopy(this.valueData, 0, values, 0, values.length);

    boolean changed = false;
    for (final <x> value : values) // [A]
      if (!c.contains(value))
        changed |= remove(value);

    return changed;
  }

  @Override
  public void clear() {
    if (size() > 0) {
      ++modCount;
      Arrays.fill(valueData, NULL);
      containsNull = false;
      size = 0;
    }
  }

  @Override
  public int size() {
    return containsNull ? size + 1 : size;
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public <x>[] toArray(<x>[] a) {
    final int size = size();
    if (a.length < size)
      a = new <x>[size];

    int i = 0;
    final <x>[] values = this.valueData;
    for (final <x> value : values) // [A]
      if (value != NULL)
        a[i++] = value;

    if (containsNull)
      a[size - 1] = NULL;

    if (a.length > size)
      a[size] = NULL;

    return a;
  }

  @Override
  public <XX>[] toArray(<XX>[] a) {
    final int size = size();
    if (a.length < size)
      a = new <XX>[size];

    int i = 0;
    final <x>[] values = this.valueData;
    for (final <x> value : values) // [A]
      if (value != NULL)
        a[i++] = value;

    if (containsNull)
      a[size - 1] = NULL;

    if (a.length > size)
      a[size] = null;

    return a;
  }

  @Override
  public <X>Iterator iterator() {
    return new <X>Itr();
  }

  final class <X>Itr implements <X>Iterator {
    private int remaining;
    private int positionCounter;
    private int stopCounter;
    private boolean isPositionValid = false;
    private int expectedModCount = modCount;

    <X>Itr() {
      final <x>[] valueData = Hash<X>Set.this.valueData;
      final int length = valueData.length;
      int i = length;
      if (valueData[length - 1] != NULL)
        for (i = 0; i < length; ++i) // [A]
          if (valueData[i] == NULL)
            break;

      this.remaining = size();
      this.stopCounter = i;
      this.positionCounter = i + length;
      this.isPositionValid = false;
    }

    @Override
    public boolean hasNext() {
      return remaining > 0;
    }

    @Override
    public <x> next() {
      checkForComodification();
      if (remaining == 1 && containsNull) {
        remaining = 0;
        isPositionValid = true;

        return NULL;
      }

      findNext();
      final <x>[] values = Hash<X>Set.this.valueData;
      return values[getPosition(values)];
    }

    @Override
    public void remove() {
      if (!isPositionValid)
        throw new IllegalStateException();

      checkForComodification();
      if (0 == remaining && containsNull) {
        containsNull = false;
      }
      else {
        final <x>[] values = Hash<X>Set.this.valueData;
        final int position = getPosition(values);
        values[position] = NULL;
        --size;
        compactChain(position);
        expectedModCount = modCount;
      }

      isPositionValid = false;
    }

    private void findNext() {
      final <x>[] valueData = Hash<X>Set.this.valueData;
      final int mask = valueData.length - 1;
      isPositionValid = true;
      for (int i = positionCounter - 1; i >= stopCounter; --i) { // [A]
        final int index = i & mask;
        if (valueData[index] != NULL) {
          positionCounter = i;
          --remaining;
          return;
        }
      }

      isPositionValid = false;
      throw new NoSuchElementException();
    }

    private int getPosition(final <x>[] values) {
      return positionCounter & (values.length - 1);
    }

    final void checkForComodification() {
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
    }
  }

<_>  @Override
<_>  public Spliterator.Of<X> spliterator() {
<_>    throw new UnsupportedOperationException();
<_>  }
<_>
<_>  @Override
<_>  public <X>Stream stream() {
<_>    return StreamSupport.<x>Stream(spliterator(), false);
<_>  }
<_>
<_>  @Override
<_>  public <X>Stream parallelStream() {
<_>    return StreamSupport.<x>Stream(spliterator(), true);
<_>  }

  private void compactChain(int deleteIndex) {
    ++modCount;
    final <x>[] values = this.valueData;
    final int mask = values.length - 1;
    int index = deleteIndex;
    while (true) {
      index = nextIndex(index, mask);
      if (values[index] == NULL)
        return;

      final int hash = hash(<XX>.hashCode(values[index]), mask);
      if (index < hash && (hash <= deleteIndex || deleteIndex <= index) || hash <= deleteIndex && deleteIndex <= index) {
        values[deleteIndex] = values[index];
        values[index] = NULL;
        deleteIndex = index;
      }
    }
  }

  private void rehash(final int newCapacity) {
    ++modCount;
    final int mask = newCapacity - 1;
    this.resizeThreshold = (int)(newCapacity * loadFactor);
    final <x>[] valueData = new <x>[newCapacity];
    for (final <x> value : this.valueData) { // [A]
      if (value != NULL) {
        int newHash = hash(<XX>.hashCode(value), mask);
        for (; valueData[newHash] != NULL; newHash = ++newHash & mask); // [N]
        valueData[newHash] = value;
      }
    }

    this.valueData = valueData;
  }

  /**
   * Compact the backing arrays by rehashing with a capacity just larger than current size and giving consideration to the load
   * factor.
   */
  public void compact() {
    final int idealCapacity = (int)Math.round(size() * (1.0 / loadFactor));
    rehash(findNextPositivePowerOfTwo(idealCapacity));
  }

  @Override
  public Hash<X>Set clone() {
    try {
      final Hash<X>Set clone = (Hash<X>Set)super.clone();
      clone.valueData = new <x>[valueData.length];
      System.arraycopy(valueData, 0, clone.valueData, 0, valueData.length);
      return clone;
    }
    catch (final CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Hash<X>Set))
      return false;

    final Hash<X>Set that = (Hash<X>Set)obj;
    return size == that.size && containsNull == that.containsNull && containsAll(that);
  }

  @Override
  public int hashCode() {
    int hashCode = containsNull ? <XX>.hashCode(NULL) : 0;
    for (int i = 0, i$ = valueData.length; i < i$; ++i) // [A]
      hashCode += <XX>.hashCode(valueData[i]);

    return hashCode;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append('[');
    for (int i = 0, i$ = valueData.length; i < i$; ++i) // [A]
      if (valueData[i] != NULL)
        builder.append(valueData[i]).append(", ");

    if (containsNull)
      builder.append(NULL).append(", ");

    if (builder.length() > 1)
      builder.setLength(builder.length() - 2);

    builder.append(']');
    return builder.toString();
  }
}