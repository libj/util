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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator.OfInt;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * An {@link IntSet} implementing
 * <a href="https://en.wikipedia.org/wiki/Open_addressing">open-addressing
 * (closed hashing) with linear-probing for collision resolution</a> algorithm,
 * with allocation-free operation in steady state when expanded.
 * <p>
 * This class replicates the API of the {@link HashSet} class by defining
 * synonymous methods for a set of {@code int} values instead of Object
 * references.
 */
public class HashIntSet implements Cloneable, IntSet, Serializable {
  private static final long serialVersionUID = -2903767291531144447L;

  /**
   * Returns the hash for the specified value and mask.
   *
   * @param value The value to be hashed.
   * @param mask The mask to be applied (must be a power of 2, minus 1).
   * @return The hash of the specified value.
   */
  private static int hash(final int value, final int mask) {
    return (value * 31) & mask;
  }

  /**
   * Returns the next index for the specified index and mask.
   *
   * @param index The index from which to calculate the next index.
   * @param mask The mask to be applied (must be a power of 2, minus 1).
   * @return The next index for the specified index.
   */
  private static int nextIndex(final int index, final int mask) {
    return (index + 1) & mask;
  }

  /**
   * Returns the next power of 2 for the value that is greater than or equal to
   * the specified value.
   * <p>
   * If {@code value <= 0}, this method returns {@code 1}.
   * <p>
   * <i><b>Note:</b> This method is not suitable for {@link Integer#MIN_VALUE}
   * or numbers greater than {@code 2^30}.</i>
   *
   * @param value Value from which to return the next power of 2.
   * @return The next power of 2 from the specified value, or the value itself
   *         if it is a power of 2.
   */
  private static int findNextPositivePowerOfTwo(final int value) {
    return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
  }

  /**
   * The load factor used when none specified in constructor.
   */
  static final float DEFAULT_LOAD_FACTOR = 0.55f;

  /**
   * Value that represents null in {@link #valueData}.
   */
  static final int NULL = 0;

  private final float loadFactor;
  private int resizeThreshold;

  /**
   * Whether this set contains the value representing {@link #NULL}.
   */
  private boolean containsNull;
  private int[] valueData;
  private int size;
  private transient int modCount;

  /**
   * Creates an empty {@code HashIntSet} with the default initial capacity
   * (16) and the default load factor (0.55).
   */
  public HashIntSet() {
    this(16);
  }

  /**
   * Creates an empty {@code HashIntSet} with the specified initial capacity and
   * load factor.
   *
   * @param initialCapacity The initial capacity.
   * @param loadFactor The load factor.
   * @throws IllegalArgumentException If the initial capacity is negative or the
   *           load factor less than {@code .1} or greater than {@code .9}.
   */
  public HashIntSet(final int initialCapacity, final float loadFactor) {
    if (loadFactor < .1f || Float.isNaN(loadFactor) || .9f < loadFactor)
      throw new IllegalArgumentException("Illegal load factor: " + loadFactor);

    this.loadFactor = loadFactor;
    this.size = 0;

    final int capacity = findNextPositivePowerOfTwo(initialCapacity);
    this.resizeThreshold = (int)(capacity * loadFactor);
    this.valueData = new int[capacity];
  }

  /**
   * Creates an empty {@code HashIntSet} with the specified initial capacity and
   * the default load factor (0.55).
   *
   * @param initialCapacity The initial capacity.
   * @throws IllegalArgumentException If the initial capacity is negative.
   */
  public HashIntSet(final int initialCapacity) {
    this(initialCapacity, DEFAULT_LOAD_FACTOR);
  }

  /**
   * Creates a new {@code HashIntSet} with the same values as the specified
   * collection. The {@code HashIntSet} is created with default load factor
   * (0.55) and an initial capacity sufficient to hold the mappings in the
   * specified collection.
   *
   * @param c The collection whose values are to be added to this set.
   * @throws NullPointerException If the specified set is null.
   */
  public HashIntSet(final IntCollection c) {
    this(c.size());
    addAll(c);
  }

  /**
   * Creates a new {@code HashIntSet} with the same values as the specified
   * collection. The {@code HashIntSet} is created with default load factor
   * (0.55) and an initial capacity sufficient to hold the mappings in the
   * specified collection.
   *
   * @param c The collection whose values are to be added to this set.
   * @throws NullPointerException If the specified set is null.
   */
  public HashIntSet(final Collection<Integer> c) {
    this(c.size());
    addAll(c);
  }

  @Override
  public boolean add(final int value) {
    if (value == NULL) {
      if (containsNull)
        return false;

      ++modCount;
      return containsNull = true;
    }

    final int mask = valueData.length - 1;
    int index = hash(value, mask);
    for (; valueData[index] != NULL; index = nextIndex(index, mask))
      if (valueData[index] == value)
        return false;

    ++modCount;
    valueData[index] = value;
    if (++size > resizeThreshold)
      rehash(valueData.length * 2);

    return true;
  }

  @Override
  public boolean addAll(final IntCollection c) {
    if (c.size() == 0)
      return false;

    boolean changed = false;
    for (final IntIterator i = c.iterator(); i.hasNext(); changed |= add(i.next()));
    return changed;
  }

  @Override
  public boolean addAll(final Collection<Integer> c) {
    if (c.size() == 0)
      return false;

    boolean changed = false;
    for (final Iterator<Integer> i = c.iterator(); i.hasNext(); changed |= add(i.next()));
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
   * @see #addAll(IntCollection)
   * @see #add(int)
   */
  public boolean addAll(final HashIntSet s) {
    if (s.size() == 0)
      return false;

    boolean changed = false;
    for (final int value : s.valueData)
      if (value != NULL)
        changed |= add(value);

    if (s.containsNull)
      changed |= add(NULL);

    return changed;
  }

  @Override
  public boolean contains(final int value) {
    if (value == NULL)
      return containsNull;

    final int mask = valueData.length - 1;
    for (int index = hash(value, mask); valueData[index] != NULL; index = nextIndex(index, mask))
      if (valueData[index] == value)
        return true;

    return false;
  }

  @Override
  public boolean containsAll(final IntCollection c) {
    if (c.size() == 0)
      return true;

    for (final IntIterator i = c.iterator(); i.hasNext();)
      if (!contains(i.next()))
        return false;

    return true;
  }

  @Override
  public boolean containsAll(final Collection<Integer> c) {
    if (c.size() == 0)
      return true;

    for (final Iterator<Integer> i = c.iterator(); i.hasNext();)
      if (!contains(i.next()))
        return false;

    return true;
  }

  @Override
  public boolean remove(final int value) {
    if (value == NULL) {
      if (!containsNull)
        return false;

      ++modCount;
      containsNull = false;
      return true;
    }

    final int mask = valueData.length - 1;
    for (int index = hash(value, mask); valueData[index] != NULL; index = nextIndex(index, mask)) {
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
  public boolean removeAll(final IntCollection c) {
    boolean changed = false;
    for (final IntIterator i = c.iterator(); i.hasNext(); changed |= remove(i.next()));
    return changed;
  }

  @Override
  public boolean removeAll(final Collection<Integer> c) {
    boolean changed = false;
    for (final Iterator<Integer> i = c.iterator(); i.hasNext(); changed |= remove(i.next()));
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
   * @see #removeAll(IntCollection)
   * @see #remove(int)
   * @see #contains(int)
   */
  public boolean removeAll(final HashIntSet s) {
    boolean changed = false;
    for (final int value : s.valueData)
      if (value != NULL)
        changed |= remove(value);

    if (s.containsNull)
      changed |= remove(NULL);

    return changed;
  }

  @Override
  public boolean retainAll(final IntCollection c) {
    final int[] values = new int[this.valueData.length];
    System.arraycopy(this.valueData, 0, values, 0, values.length);

    boolean changed = false;
    for (final int value : values)
      if (!c.contains(value))
        changed |= remove(value);

    return changed;
  }

  @Override
  public boolean retainAll(final Collection<Integer> c) {
    final int[] values = new int[this.valueData.length];
    System.arraycopy(this.valueData, 0, values, 0, values.length);

    boolean changed = false;
    for (final int value : values)
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
  public int[] toArray(int[] a) {
    if (a.length < size())
      a = new int[size()];

    int i = 0;
    final int[] values = this.valueData;
    for (final int value : values)
      if (NULL != value)
        a[i++] = value;

    if (containsNull)
      a[size] = NULL;

    return a;
  }

  @Override
  public Integer[] toArray(Integer[] a) {
    if (a.length < size())
      a = new Integer[size()];

    int i = 0;
    final int[] values = this.valueData;
    for (final int value : values)
      if (NULL != value)
        a[i++] = value;

    if (containsNull)
      a[size] = NULL;

    return a;
  }

  @Override
  public IntIterator iterator() {
    return new IntItr();
  }

  final class IntItr implements IntIterator, Serializable {
    private static final long serialVersionUID = -7682246612354831208L;

    private int remaining;
    private int positionCounter;
    private int stopCounter;
    private boolean isPositionValid = false;
    private int expectedModCount = modCount;

    IntItr() {
      final int[] valueData = HashIntSet.this.valueData;
      final int length = valueData.length;
      int i = length;
      if (valueData[length - 1] != NULL)
        for (i = 0; i < length; ++i)
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
    public int next() {
      checkForComodification();
      if (remaining == 1 && containsNull) {
        remaining = 0;
        isPositionValid = true;

        return NULL;
      }

      findNext();
      final int[] values = HashIntSet.this.valueData;
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
        final int[] values = HashIntSet.this.valueData;
        final int position = getPosition(values);
        values[position] = NULL;
        --size;
        compactChain(position);
        expectedModCount = modCount;
      }

      isPositionValid = false;
    }

    private void findNext() {
      final int[] valueData = HashIntSet.this.valueData;
      final int mask = valueData.length - 1;
      isPositionValid = true;
      for (int i = positionCounter - 1; i >= stopCounter; --i) {
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

    private int getPosition(final int[] values) {
      return positionCounter & (values.length - 1);
    }

    final void checkForComodification() {
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
    }
  }

  @Override
  public OfInt spliterator() {
    throw new UnsupportedOperationException();
  }

  @Override
  public IntStream stream() {
    return StreamSupport.intStream(spliterator(), false);
  }

  @Override
  public IntStream parallelStream() {
    return StreamSupport.intStream(spliterator(), true);
  }

  private void compactChain(int deleteIndex) {
    ++modCount;
    final int[] values = this.valueData;
    final int mask = values.length - 1;
    int index = deleteIndex;
    while (true) {
      index = nextIndex(index, mask);
      if (values[index] == NULL)
        return;

      final int hash = hash(values[index], mask);
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
    final int[] valueData = new int[newCapacity];
    for (final int value : this.valueData) {
      if (value != NULL) {
        int newHash = hash(value, mask);
        for (; valueData[newHash] != NULL; newHash = ++newHash & mask);
        valueData[newHash] = value;
      }
    }

    this.valueData = valueData;
  }

  /**
   * Compact the backing arrays by rehashing with a capacity just larger than
   * current size and giving consideration to the load factor.
   */
  public void compact() {
    final int idealCapacity = (int)Math.round(size() * (1.0 / loadFactor));
    rehash(findNextPositivePowerOfTwo(idealCapacity));
  }

  @Override
  public Object clone() {
    try {
      final HashIntSet clone = (HashIntSet)super.clone();
      clone.valueData = new int[valueData.length];
      System.arraycopy(valueData, 0, clone.valueData, 0, valueData.length);
      return clone;
    }
    catch (final CloneNotSupportedException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof HashIntSet))
      return false;

    final HashIntSet that = (HashIntSet)obj;
    return size == that.size && containsNull == that.containsNull && containsAll(that);
  }

  @Override
  public int hashCode() {
    int hashCode = 0;
    for (final int value : valueData)
      if (value != NULL)
        hashCode += Integer.hashCode(value);

    if (containsNull)
      hashCode += Integer.hashCode(NULL);

    return hashCode;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append('[');
    for (int i = 0; i < valueData.length; ++i)
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