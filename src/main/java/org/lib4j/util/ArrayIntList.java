/* Copyright (c) 2014 lib4j
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

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.IntConsumer;

/**
 * An unsynchronized implementation of a resizable-array of int values.
 *
 * <p>The {@code size}, {@code isEmpty}, {@code get}, and {@code set}
 * operations run in constant time. The {@code add} operation runs in
 * <i>amortized constant time</i>, that is, adding n elements requires O(n)
 * time. All of the other operations run in linear time (roughly speaking).
 *
 * <p>Each {@code ArrayIntList} instance has a <i>capacity</i>. The capacity is
 * the size of the array used to store the elements in the list. It is always
 * at least as large as the list size. As elements are added to an ArrayIntList,
 * its capacity grows automatically. The details of the growth policy are not
 * specified beyond the fact that adding an element has constant amortized
 * time cost.
 *
 * <p>An application can increase the capacity of an {@code ArrayIntList} instance
 * before adding a large number of elements using the {@code ensureCapacity}
 * operation. This may reduce the amount of incremental reallocation.
 *
 * <p><strong>Note that this implementation is not synchronized.</strong>
 * If multiple threads access an {@code ArrayIntList} instance concurrently,
 * and at least one of the threads modifies the list structurally, it
 * <i>must</i> be synchronized externally. (A structural modification is
 * any operation that adds or deletes one or more elements, or explicitly
 * resizes the backing array; merely setting the value of an element is not
 * a structural modification.) This is typically accomplished by
 * synchronizing on some object that naturally encapsulates the list.
 */
public class ArrayIntList implements Cloneable, IntList, RandomAccess, Serializable {
  private static final long serialVersionUID = 3156088399075272505L;
  private static final int DEFAULT_INITIAL_CAPACITY = 5;
  private static final int[] EMPTY_ELEMENTDATA = {};

  protected int[] valueData;
  protected int size;
  protected transient int modCount = 0;

  /**
   * Constructs an empty list with an initial capacity of five.
   */
  public ArrayIntList() {
    valueData = new int[DEFAULT_INITIAL_CAPACITY];
  }

  /**
   * Constructs an empty list with the specified initial capacity.
   *
   * @param initialCapacity The initial capacity of the list.
   * @throws IllegalArgumentException If the specified initial capacity
   *           is negative.
   */
  public ArrayIntList(final int initialCapacity) {
    if (initialCapacity < 0)
      throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);

    valueData = initialCapacity == 0 ? EMPTY_ELEMENTDATA : new int[initialCapacity];
  }

  /**
   * Constructs a list containing the values of the specified
   * array.
   *
   * @param values The array whose values are to be placed into this list.
   * @param offset The index of the first value to add.
   * @param length The number of values to add.
   * @throws NullPointerException If the specified array is null.
   */
  public ArrayIntList(final int[] values, final int offset, final int length) {
    valueData = new int[length];
    System.arraycopy(values, offset, valueData, 0, length);
    size = length;
  }

  /**
   * Constructs a list containing the values of the specified
   * array.
   *
   * @param values The array whose values are to be placed into this list.
   * @throws NullPointerException If the specified array is null.
   */
  public ArrayIntList(final int ... values) {
    this(values, 0, values.length);
  }

  /**
   * Constructs a list containing the values of the specified
   * collection, in the order they are returned by the collection's
   * iterator.
   *
   * @param c The collection whose values are to be placed into this list.
   * @throws NullPointerException If the specified collection is null.
   */
  public ArrayIntList(final Collection<Integer> c) {
    valueData = new int[c.size()];
    for (final Iterator<Integer> iterator = c.iterator(); iterator.hasNext();)
      valueData[size++] = iterator.next();
  }

  /**
   * Shifts the values in {@code valueData} right a distance of
   * {@code length} starting from {@code index}.
   *
   * @param index Index from which to shift the values.
   * @param length Distance to shift the values by.
   */
  private void shiftRight(final int index, final int length) {
    ensureCapacity(size + length);
    for (int i = size - 1; i >= index; --i)
      valueData[i + length] = valueData[i];
  }

  /**
   * Shifts the values in {@code valueData} left a distance of
   * {@code length} starting from {@code index}.
   *
   * @param index Index from which to shift the values.
   * @param length Distance to shift the values by.
   */
  private void shiftLeft(final int index, final int length) {
    for (int i = index; i < size - 1; ++i)
      valueData[i] = valueData[i + length];
  }

  @Override
  public int get(final int index) {
    if (index < 0 || size <= index)
      throw new ArrayIndexOutOfBoundsException(index);

    return valueData[index];
  }

  @Override
  public void add(final int value) {
    ++modCount;
    ensureCapacity(size + 1);
    valueData[size++] = value;
  }

  @Override
  public void add(final int index, final int value) {
    if (index < 0 || size < index)
      throw new ArrayIndexOutOfBoundsException(index);

    ++modCount;
    shiftRight(index, 1);
    valueData[index] = value;
    ++size;
  }

  /**
   * Inserts all of the values in the specified list into this
   * list, starting at the specified position. Shifts the value
   * currently at that position (if any) and any subsequent values to
   * the right (increases their indices). The new values will appear
   * in the list in the order that they are returned by the
   * specified list's iterator.
   *
   * @param index Index at which to insert the first value from the
   *              specified list.
   * @param list List containing values to be added to this list.
   * @param offset The index of the first value to add.
   * @param length The number of values to add.
   * @return {@code true} If this list changed as a result of the call.
   * @throws IndexOutOfBoundsException If the index is out of range.
   *         ({@code index < 0 || size() < index}).
   * @throws NullPointerException If the specified list is null.
   */
  public void addAll(final int index, final ArrayIntList list, final int offset, final int length) {
    addAll(index, list.valueData, offset, length);
  }

  /**
   * Inserts all of the values in the specified list into this
   * list, starting at the specified position. Shifts the value
   * currently at that position (if any) and any subsequent values to
   * the right (increases their indices). The new values will appear
   * in the list in the order that they are returned by the
   * specified list's iterator.
   *
   * @param index Index at which to insert the first value from the
   *              specified list.
   * @param list List containing values to be added to this list.
   * @return {@code true} If this list changed as a result of the call.
   * @throws IndexOutOfBoundsException If the index is out of range
   *         ({@code index < 0 || size() < index}).
   * @throws NullPointerException If the specified list is null.
   */
  public void addAll(final int index, final ArrayIntList list) {
    addAll(index, list.valueData, 0, list.size);
  }

  /**
   * Appends all of the values in the specified list to the end of
   * this list, in the order that they are returned by the
   * specified list's Iterator. The behavior of this operation is
   * undefined if the specified list is modified while the operation
   * is in progress. (This implies that the behavior of this call is
   * undefined if the specified list is this list, and this
   * list is nonempty.)
   *
   * @param list List containing values to be added to this list.
   * @param offset The index of the first value to add.
   * @param length The number of values to add.
   * @return {@code true} If this list changed as a result of the call.
   * @throws NullPointerException If the specified list is null.
   */
  public void addAll(final ArrayIntList list, final int offset, final int length) {
    addAll(size(), list.valueData, offset, length);
  }

  /**
   * Appends all of the values in the specified list to the end of
   * this list, in the order that they are returned by the
   * specified list's Iterator. The behavior of this operation is
   * undefined if the specified list is modified while the operation
   * is in progress. (This implies that the behavior of this call is
   * undefined if the specified list is this list, and this
   * list is nonempty.)
   *
   * @param list List containing values to be added to this list.
   * @return {@code true} If this list changed as a result of the call.
   * @throws NullPointerException If the specified list is null.
   */
  public void addAll(final ArrayIntList list) {
    addAll(size(), list.valueData, 0, list.size);
  }

  @Override
  public void addAll(final int index, final int[] values, final int offset, final int length) {
    ++modCount;
    shiftRight(index, length);
    System.arraycopy(values, offset, valueData, index, length);
    size += length;
  }

  @Override
  public void addAll(final int index, final Collection<Integer> c) {
    ++modCount;
    shiftRight(index, c.size());
    for (final Iterator<Integer> iterator = c.iterator(); iterator.hasNext();)
      valueData[size++] = iterator.next();
  }

  @Override
  public void addAll(final int index, IntCollection c) {
    ++modCount;
    shiftRight(index, c.size());
    for (final IntIterator iterator = c.iterator(); iterator.hasNext();)
      valueData[size++] = iterator.next();
  }

  @Override
  public int set(final int index, final int value) {
    if (index < 0 || size <= index)
      throw new ArrayIndexOutOfBoundsException(index);

    ++modCount;
    final int oldValue = valueData[index];
    valueData[index] = value;
    return oldValue;
  }

  @Override
  public int remove(final int index) {
    if (index < 0 || size <= index)
      throw new ArrayIndexOutOfBoundsException(index);

    ++modCount;
    final int value = valueData[index];
    shiftLeft(index, 1);
    --size;
    return value;
  }

  @Override
  public void clear() {
    ++modCount;
    size = 0;
  }

  @Override
  public int indexOf(final int value) {
    for (int i = 0; i < size; ++i)
      if (valueData[i] == value)
        return i;

    return -1;
  }

  @Override
  public int lastIndexOf(final int value) {
    for (int i = size - 1; i >= 0; --i)
      if (valueData[i] == value)
        return i;

    return -1;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public void sort() {
    ++modCount;
    java.util.Arrays.sort(valueData, 0, size);
  }

  private class IntItr implements IntIterator {
    int cursor;
    int lastRet = -1;
    int expectedModCount = modCount;

    @Override
    public boolean hasNext() {
      return cursor != size;
    }

    @Override
    public int next() {
      checkForComodification();
      final int i = cursor;
      if (i >= size)
        throw new NoSuchElementException();

      final int[] valueData = ArrayIntList.this.valueData;
      if (i >= valueData.length)
        throw new ConcurrentModificationException();

      cursor = i + 1;
      return valueData[lastRet = i];
    }

    @Override
    public void remove() {
      if (lastRet < 0)
        throw new IllegalStateException();

      checkForComodification();
      try {
        ArrayIntList.this.remove(lastRet);
        cursor = lastRet;
        lastRet = -1;
        expectedModCount = modCount;
      }
      catch (IndexOutOfBoundsException e) {
        throw new ConcurrentModificationException();
      }
    }

    @Override
    public void forEachRemaining(final IntConsumer action) {
      Objects.requireNonNull(action);
      final int size = ArrayIntList.this.size;
      int i = cursor;
      if (i >= size)
        return;

      final int[] es = valueData;
      if (i >= es.length)
        throw new ConcurrentModificationException();

      for (; i < size && modCount == expectedModCount; i++)
        action.accept(es[i]);

      cursor = i;
      lastRet = i - 1;
      checkForComodification();
    }

    final void checkForComodification() {
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
    }
  };

  private class IntListItr extends IntItr implements IntListIterator {
    IntListItr(final int index) {
      cursor = index;
    }

    @Override
    public boolean hasPrevious() {
      return cursor != 0;
    }

    @Override
    public int nextIndex() {
      return cursor;
    }

    @Override
    public int previousIndex() {
      return cursor - 1;
    }

    @Override
    public int previous() {
      checkForComodification();
      final int i = cursor - 1;
      if (i < 0)
        throw new NoSuchElementException();

      final int[] valueData = ArrayIntList.this.valueData;
      if (i >= valueData.length)
        throw new ConcurrentModificationException();

      cursor = i;
      return valueData[lastRet = i];
    }

    @Override
    public void set(final int value) {
      if (lastRet < 0)
        throw new IllegalStateException();

      checkForComodification();
      try {
        ArrayIntList.this.set(lastRet, value);
      }
      catch (final IndexOutOfBoundsException e) {
        throw new ConcurrentModificationException();
      }
    }

    @Override
    public void add(final int value) {
      checkForComodification();

      try {
        final int i = cursor;
        ArrayIntList.this.add(i, value);
        cursor = i + 1;
        lastRet = -1;
        expectedModCount = modCount;
      }
      catch (final IndexOutOfBoundsException e) {
        throw new ConcurrentModificationException();
      }
    }

    @Override
    public void remove() {
      if (lastRet < 0)
        throw new IllegalStateException();

      checkForComodification();
      try {
        ArrayIntList.this.remove(lastRet);
        cursor = lastRet;
        lastRet = -1;
        expectedModCount = modCount;
      }
      catch (final IndexOutOfBoundsException e) {
        throw new ConcurrentModificationException();
      }
    }
  };

  @Override
  public IntIterator iterator() {
    return new IntItr();
  }

  @Override
  public IntListIterator listIterator(final int index) {
    return new IntListItr(index);
  }

  @Override
  public int[] toArray(int[] a) {
    if (a.length < size)
      a = new int[size];

    System.arraycopy(valueData, 0, a, 0, size);
    if (a.length > size)
      a[size] = 0;

    return a;
  }

  @Override
  public Integer[] toArray(Integer[] a) {
    if (a.length < size)
      a = (Integer[])Array.newInstance(a.getClass().getComponentType(), size);

    for (int i = 0; i < size; ++i)
      a[i] = Integer.valueOf(valueData[i]);

    if (a.length > size)
      a[size] = null;

    return a;
  }

  /**
   * Trims the capacity of this {@code ArrayIntList} instance to be the
   * list's current size.  An application can use this operation to minimize
   * the storage of an {@code ArrayIntList} instance.
   */
  public void trimToSize() {
    ++modCount;
    if (size < valueData.length)
      valueData = size == 0 ? EMPTY_ELEMENTDATA : Arrays.copyOf(valueData, size);
  }

  /**
   * Increases the capacity of this {@code ArrayIntList} instance, if
   * necessary, to ensure that it can hold at least the number of values
   * specified by the minimum capacity argument.
   *
   * @param minCapacity The desired minimum capacity.
   */
  public void ensureCapacity(final int minCapacity) {
    if (minCapacity > valueData.length) {
      ++modCount;
      final int[] oldData = valueData;
      final int newCapacity = Math.max((valueData.length * 3) / 2 + 1, minCapacity);
      valueData = new int[newCapacity];
      System.arraycopy(oldData, 0, valueData, 0, size);
    }
  }

  /**
   * Returns a clone of this {@code ArrayIntList} instance.
   *
   * @return A clone of this {@code ArrayIntList} instance.
   */
  @Override
  protected ArrayIntList clone() {
    try {
      final ArrayIntList clone = (ArrayIntList)super.clone();
      clone.size = size;
      clone.valueData = new int[valueData.length];
      System.arraycopy(valueData, 0, clone.valueData, 0, valueData.length);
      return clone;
    }
    catch (final CloneNotSupportedException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  /**
   * Returns the hash code value for this list.
   *
   * @return The hash code value for this list.
   */
  @Override
  public int hashCode() {
    int hashCode = 1;
    for (int i = 0; i < size; ++i)
      hashCode = 31 * hashCode + valueData[i];

    return hashCode;
  }

  /**
   * Compares the specified object with this list for equality. Returns
   * {@code true} if and only if the specified object is also a
   * {@code ArrayIntList}, both lists have the same size, and all corresponding
   * pairs of values in the two lists are <i>equal</i>. In other words, two
   * lists are defined to be equal if they contain the same values in the
   * same order.
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof ArrayIntList))
      return false;

    final ArrayIntList that = (ArrayIntList)obj;
    return size == that.size && java.util.Arrays.equals(valueData, 0, size, that.valueData, 0, size);
  }

  /**
   * Returns a string representation of this list. The string representation
   * consists of a list of the list's values in the order they are stored in
   * the underlying array, enclosed in square brackets ({@code "[]"}). Adjacent
   * values are separated by the characters {@code ", "} (comma and space).
   * Values are converted to strings as by {@link String#valueOf(Object)}.
   *
   * @return A string representation of this list.
   */
  @Override
  public String toString() {
    return "[" + org.lib4j.util.Arrays.toString(valueData, ", ", 0, size) + "]";
  }
}