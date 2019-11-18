/* Copyright (c) 2014 LibJ
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
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/**
 * An unsynchronized implementation of a resizable-array of int values.
 * <p>
 * The {@code size}, {@code isEmpty}, {@code get}, and {@code set} operations
 * run in constant time. The {@code add} operation runs in <i>amortized constant
 * time</i>, that is, adding n elements requires O(n) time. All of the other
 * operations run in linear time (roughly speaking).
 * <p>
 * Each {@link ArrayIntList} instance has a <i>capacity</i>. The capacity is the
 * size of the array used to store the elements in the list. It is always at
 * least as large as the list size. As elements are added to an ArrayIntList,
 * its capacity grows automatically. The details of the growth policy are not
 * specified beyond the fact that adding an element has constant amortized time
 * cost.
 * <p>
 * An application can increase the capacity of an {@link ArrayIntList} instance
 * before adding a large number of elements using the {@code ensureCapacity}
 * operation. This may reduce the amount of incremental reallocation.
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong> If
 * multiple threads access an {@link ArrayIntList} instance concurrently, and at
 * least one of the threads modifies the list structurally, it <i>must</i> be
 * synchronized externally. (A structural modification is any operation that
 * adds or deletes one or more elements, or explicitly resizes the backing
 * array; merely setting the value of an element is not a structural
 * modification.) This is typically accomplished by synchronizing on some object
 * that naturally encapsulates the list.
 */
public class ArrayIntList extends AbstractArrayList<int[]> implements Cloneable, IntList, RandomAccess, Serializable {
  private static final long serialVersionUID = 3156088399075272505L;

  private static final int[] EMPTY_VALUEDATA = {};

  /**
   * Creates an empty list with an initial capacity of five.
   */
  public ArrayIntList() {
    valueData = new int[DEFAULT_INITIAL_CAPACITY];
    fromIndex = 0;
  }

  /**
   * Creates an empty list with the specified initial capacity.
   *
   * @param initialCapacity The initial capacity of the list.
   * @throws IllegalArgumentException If the specified initial capacity is
   *           negative.
   */
  public ArrayIntList(final int initialCapacity) {
    if (initialCapacity < 0)
      throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);

    fromIndex = 0;
    valueData = initialCapacity == 0 ? EMPTY_VALUEDATA : new int[initialCapacity];
  }

  /**
   * Creates a list containing the values of the specified array.
   *
   * @param values The array whose values are to be placed into this list.
   * @param offset The index of the first value to add.
   * @param length The number of values to add.
   * @throws NullPointerException If the specified array is null.
   */
  public ArrayIntList(final int[] values, final int offset, final int length) {
    fromIndex = 0;
    valueData = new int[length];
    System.arraycopy(values, offset, valueData, 0, length);
    size = length;
  }

  /**
   * Creates a list containing the values of the specified array.
   *
   * @param values The array whose values are to be placed into this list.
   * @throws NullPointerException If the specified array is null.
   */
  public ArrayIntList(final int ... values) {
    this(values, 0, values.length);
  }

  /**
   * Creates a list containing the values of the specified collection, in the
   * order they are returned by the collection's iterator.
   *
   * @param c The collection whose values are to be placed into this list.
   * @throws NullPointerException If the specified collection is null.
   */
  public ArrayIntList(final Collection<Integer> c) {
    fromIndex = 0;
    valueData = new int[c.size()];
    for (final Iterator<Integer> i = c.iterator(); i.hasNext();)
      valueData[size++] = i.next();
  }

  /**
   * Creates a sub-list, and integrates it into the specified parent list's
   * sub-list graph. A sub-list instance shares the parent list's
   * {@link #valueData}, and modifications made to any list in the graph of
   * sub-lists are propagated with the
   * {@link AbstractArrayList#updateState(int,int)} method.
   *
   * @param parent The parent list.
   * @param fromIndex Low endpoint (inclusive) of the subList.
   * @param toIndex High endpoint (exclusive) of the subList.
   * @throws NullPointerException If the specified parent list is null.
   */
  private ArrayIntList(final ArrayIntList parent, final int fromIndex, final int toIndex) {
    super(parent, fromIndex, toIndex);
  }

  /**
   * Shifts the values in {@code valueData} right a distance of {@code dist}
   * starting from {@code index}.
   *
   * @param start Index from which to shift the values to the right.
   * @param dist Distance to shift the values by.
   */
  private void shiftRight(final int start, final int dist) {
    final int end = size + dist;
    ensureCapacity(end);
    for (int i = end - 1; i >= start + dist; --i)
      valueData[i] = valueData[i - dist];
  }

  /**
   * Shifts the values in {@code valueData} left a distance of {@code dist}
   * starting from {@code index}.
   *
   * @param start Index from which to shift the values to the left.
   * @param dist Distance to shift the values by.
   */
  private void shiftLeft(final int start, final int dist) {
    final int end = size - dist;
    for (int i = start; i < end; ++i)
      valueData[i] = valueData[i + dist];
  }

  @Override
  public int get(final int index) {
    Assertions.assertRangeList(index, size(), false);
    return valueData[fromIndex + index];
  }

  @Override
  public boolean add(final int value) {
    final int index = toIndex > -1 ? toIndex : size;
    shiftRight(index, 1);
    valueData[updateState(index, 1)] = value;
    return true;
  }

  @Override
  public boolean add(int index, final int value) {
    Assertions.assertRangeList(index, size(), true);
    index += fromIndex;
    shiftRight(index, 1);
    valueData[updateState(index, 1)] = value;
    return true;
  }

  /**
   * Appends all of the values in the specified list to the end of this list, in
   * the order that they are returned by the specified list's Iterator. The
   * behavior of this operation is undefined if the specified list is modified
   * while the operation is in progress. (This implies that the behavior of this
   * call is undefined if the specified list is this list, and this list is
   * nonempty.)
   *
   * @param list List containing values to be added to this list.
   * @return {@code true} if this collection changed as a result of the call.
   * @throws NullPointerException If the specified list is null.
   */
  public boolean addAll(final ArrayIntList list) {
    return addAll(list.valueData, list.fromIndex, list.toIndex > -1 ? list.toIndex : list.size);
  }

  /**
   * Appends all of the values in the specified list to the end of this list, in
   * the order that they are returned by the specified list's Iterator. The
   * behavior of this operation is undefined if the specified list is modified
   * while the operation is in progress. (This implies that the behavior of this
   * call is undefined if the specified list is this list, and this list is
   * nonempty.)
   *
   * @param list List containing values to be added to this list.
   * @param offset The index of the first value to add.
   * @param length The number of values to add.
   * @return {@code true} if this collection changed as a result of the call.
   * @throws IndexOutOfBoundsException If the offset or length are out of range
   *           ({@code offset < 0 || values.length < offset + length}).
   * @throws NullPointerException If the specified list is null.
   */
  public boolean addAll(final ArrayIntList list, final int offset, final int length) {
    return addAll(list.valueData, offset + list.fromIndex, length);
  }

  /**
   * Appends all of the values in the specified array to the end of this list,
   * in the order that they appear in the array.
   *
   * @param values Array containing values to be added to this list.
   * @param offset The index of the first value to add.
   * @param length The number of values to add.
   * @return {@code true} if this list changed as a result of the call.
   * @throws IndexOutOfBoundsException If the offset or length are out of range
   *           ({@code offset < 0 || values.length < offset + length}).
   * @throws NullPointerException If the specified array is null.
   */
  @Override
  public boolean addAll(final int[] values, final int offset, final int length) {
    if (values.length == 0)
      return false;

    final int index = toIndex > -1 ? toIndex : size;
    shiftRight(index, length);
    System.arraycopy(values, offset, valueData, index, length);
    updateState(index, length);
    return true;
  }

  /**
   * Appends all of the values in the specified array to the end of this list,
   * in the order that they appear in the array.
   *
   * @param values Array containing values to be added to this list.
   * @return {@code true} if this list changed as a result of the call.
   * @throws NullPointerException If the specified array is null.
   */
  @Override
  public boolean addAll(final int[] values) {
    return addAll(values, 0, values.length);
  }

  /**
   * Inserts all of the values in the specified list into this list, starting at
   * the specified position. Shifts the value currently at that position (if
   * any) and any subsequent values to the right (increases their indices). The
   * new values will appear in the list in the order that they are returned by
   * the specified list's iterator.
   *
   * @param index Index at which to insert the first value from the specified
   *          list.
   * @param list List containing values to be added to this list.
   * @param offset The index of the first value to add.
   * @param length The number of values to add.
   * @return {@code true} if this collection changed as a result of the call.
   * @throws IndexOutOfBoundsException If the index is out of range.
   *           ({@code index < 0 || size() < index}).
   * @throws NullPointerException If the specified list is null.
   */
  public boolean addAll(final int index, final ArrayIntList list, final int offset, final int length) {
    return addAll(index, list.valueData, offset + list.fromIndex, length);
  }

  /**
   * Inserts all of the values in the specified list into this list, starting at
   * the specified position. Shifts the value currently at that position (if
   * any) and any subsequent values to the right (increases their indices). The
   * new values will appear in the list in the order that they are returned by
   * the specified list's iterator.
   *
   * @param index Index at which to insert the first value from the specified
   *          list.
   * @param list List containing values to be added to this list.
   * @return {@code true} if this collection changed as a result of the call.
   * @throws IndexOutOfBoundsException If the index is out of range
   *           ({@code index < 0 || size() < index}).
   * @throws NullPointerException If the specified list is null.
   */
  public boolean addAll(final int index, final ArrayIntList list) {
    return addAll(index, list.valueData, list.fromIndex, list.toIndex > -1 ? list.toIndex : list.size);
  }

  @Override
  public boolean addAll(int index, final int[] values, final int offset, final int length) {
    Assertions.assertRangeList(index, size(), true);
    if (values.length == 0)
      return false;

    index += fromIndex;
    shiftRight(index, length);
    System.arraycopy(values, offset, valueData, index, length);
    updateState(index, length);
    return true;
  }

  @Override
  public boolean addAll(final Collection<Integer> c) {
    final int len = c.size();
    if (len == 0)
      return false;

    int index = toIndex > -1 ? toIndex : size;
    shiftRight(index, len);
    for (final Iterator<Integer> i = c.iterator(); i.hasNext(); updateState(index++, 1))
      valueData[index] = i.next();

    return true;
  }

  @Override
  public boolean addAll(final IntCollection c) {
    final int len = c.size();
    if (len == 0)
      return false;

    int index = toIndex > -1 ? toIndex : size;
    shiftRight(index, len);
    for (final IntIterator i = c.iterator(); i.hasNext(); updateState(index++, 1))
      valueData[index] = i.next();

    return true;
  }

  @Override
  public boolean addAll(int index, final Collection<Integer> c) {
    Assertions.assertRangeList(index, size(), true);
    final int len = c.size();
    if (len == 0)
      return false;

    index += fromIndex;
    shiftRight(index, len);
    for (final Iterator<Integer> i = c.iterator(); i.hasNext(); updateState(index++, 1))
      valueData[index] = i.next();

    return true;
  }

  @Override
  public boolean addAll(int index, final IntCollection c) {
    Assertions.assertRangeList(index, size(), true);
    final int len = c.size();
    if (len == 0)
      return false;

    index += fromIndex;
    shiftRight(index, len);
    for (final IntIterator i = c.iterator(); i.hasNext(); updateState(index++, 1))
      valueData[index] = i.next();

    return true;
  }

  @Override
  public int set(int index, final int value) {
    Assertions.assertRangeList(index, size(), false);
    index += fromIndex;
    final int oldValue = valueData[index];
    valueData[index] = value;
    updateState(0, 0);
    return oldValue;
  }

  @Override
  public int removeIndex(int index) {
    Assertions.assertRangeList(index, size(), false);
    index += fromIndex;
    final int value = valueData[index];
    shiftLeft(index, 1);
    updateState(index, -1);
    return value;
  }

  @Override
  public boolean retainAll(final Collection<Integer> c) {
    final int beforeSize = size;
    for (int i = toIndex > -1 ? toIndex : size; i >= fromIndex; --i) {
      if (!c.contains(valueData[i])) {
        shiftLeft(i, 1);
        updateState(i, -1);
      }
    }

    return beforeSize != size;
  }

  @Override
  public boolean retainAll(final IntCollection c) {
    final int beforeSize = size;
    for (int i = toIndex > -1 ? toIndex : size; i >= fromIndex; --i) {
      if (!c.contains(valueData[i])) {
        shiftLeft(i, 1);
        updateState(i, -1);
      }
    }

    return beforeSize != size;
  }

  @Override
  public int indexOf(final int value) {
    final int len = toIndex > -1 ? toIndex : size;
    for (int i = fromIndex; i < len; ++i)
      if (valueData[i] == value)
        return i - fromIndex;

    return -1;
  }

  @Override
  public int lastIndexOf(final int value) {
    for (int i = toIndex > -1 ? toIndex : size; i >= fromIndex; --i)
      if (valueData[i] == value)
        return i - fromIndex;

    return -1;
  }

  @Override
  public void sort() {
    updateState(0, 0);
    Arrays.sort(valueData, fromIndex, toIndex > -1 ? toIndex : size);
  }

  private class IntItr implements IntIterator {
    int cursor = ArrayIntList.this.fromIndex;
    int lastRet = -1;
    int expectedModCount = modCount;

    @Override
    public boolean hasNext() {
      return cursor != (toIndex > -1 ? toIndex : size);
    }

    @Override
    public int next() {
      checkForComodification();
      final int i = cursor;
      if (i >= (toIndex > -1 ? toIndex : size))
        throw new NoSuchElementException();

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
        ArrayIntList.this.removeIndex(lastRet - fromIndex);
        cursor = lastRet;
        lastRet = -1;
        expectedModCount = modCount;
      }
      catch (final IndexOutOfBoundsException e) {
        throw new ConcurrentModificationException();
      }
    }

    @Override
    public void forEachRemaining(final IntConsumer action) {
      Objects.requireNonNull(action);
      int i = cursor;
      if (i >= (toIndex > -1 ? toIndex : size))
        return;

      if (i >= valueData.length)
        throw new ConcurrentModificationException();

      for (; i < (toIndex > -1 ? toIndex : size) && modCount == expectedModCount; ++i)
        action.accept(valueData[i]);

      cursor = i;
      lastRet = i - 1;
      checkForComodification();
    }

    final void checkForComodification() {
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
    }
  }

  private class IntListItr extends IntItr implements IntListIterator {
    IntListItr(final int index) {
      cursor = index + fromIndex;
    }

    @Override
    public boolean hasPrevious() {
      return cursor != fromIndex;
    }

    @Override
    public int nextIndex() {
      return cursor - fromIndex;
    }

    @Override
    public int previousIndex() {
      return cursor - fromIndex - 1;
    }

    @Override
    public int previous() {
      checkForComodification();
      final int i = cursor - 1;
      if (i < fromIndex)
        throw new NoSuchElementException();

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
        ArrayIntList.this.set(lastRet - fromIndex, value);
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
        ArrayIntList.this.add(i - fromIndex, value);
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
        ArrayIntList.this.removeIndex(lastRet - fromIndex);
        cursor = lastRet;
        lastRet = -1;
        expectedModCount = modCount;
      }
      catch (final IndexOutOfBoundsException e) {
        throw new ConcurrentModificationException();
      }
    }
  }

  @Override
  public IntIterator iterator() {
    return new IntItr();
  }

  @Override
  public IntListIterator listIterator(final int index) {
    Assertions.assertRangeList(index, size(), true);
    return new IntListItr(index);
  }

  @Override
  public ArrayIntList subList(final int fromIndex, final int toIndex) {
    Assertions.assertRangeList(fromIndex, toIndex, size());
    if (this.toIndex < 0)
      this.toIndex = size;

    return new ArrayIntList(this, fromIndex + this.fromIndex, toIndex + this.fromIndex);
  }

  @Override
  public int[] toArray(int[] a) {
    if (a.length < size())
      a = new int[size()];

    System.arraycopy(valueData, fromIndex, a, 0, size());
    if (a.length > size())
      a[size()] = 0;

    return a;
  }

  @Override
  public Integer[] toArray(Integer[] a) {
    if (a.length < size())
      a = new Integer[size()];

    final int len = toIndex > -1 ? toIndex : size;
    for (int i = fromIndex; i < len; ++i)
      a[i - fromIndex] = valueData[i];

    if (a.length > size())
      a[size()] = null;

    return a;
  }

  /**
   * Trims the capacity of this {@link ArrayIntList} instance to be the list's
   * current size. An application can use this operation to minimize the storage
   * of an {@link ArrayIntList} instance.
   */
  public void trimToSize() {
    if (size < valueData.length) {
      this.valueData = size == 0 ? EMPTY_VALUEDATA : Arrays.copyOf(valueData, size);
      updateState(0, 0);
    }
  }

  /**
   * Increases the capacity of this {@link ArrayIntList} instance, if necessary,
   * to ensure that it can hold at least the number of values specified by the
   * minimum capacity argument.
   *
   * @param minCapacity The desired minimum capacity.
   */
  public void ensureCapacity(final int minCapacity) {
    if (minCapacity > valueData.length) {
      final int[] oldData = valueData;
      final int newCapacity = Math.max((valueData.length * 3) / 2 + 1, minCapacity);
      final int[] valueData = new int[newCapacity];
      System.arraycopy(oldData, 0, valueData, 0, size);
      this.valueData = valueData;
      updateState(0, 0);
    }
  }

  @Override
  public Spliterator.OfInt spliterator() {
    return Arrays.spliterator(valueData, fromIndex, toIndex > -1 ? toIndex : size);
  }

  @Override
  public IntStream stream() {
    return Arrays.stream(valueData, fromIndex, toIndex > -1 ? toIndex : size);
  }

  @Override
  public IntStream parallelStream() {
    return Arrays.stream(valueData, fromIndex, toIndex > -1 ? toIndex : size).parallel();
  }

  /**
   * Returns a clone of this {@link ArrayIntList} instance.
   *
   * @return A clone of this {@link ArrayIntList} instance.
   */
  @Override
  protected ArrayIntList clone() {
    try {
      final ArrayIntList clone = (ArrayIntList)super.clone();
      // Clones of SubList(s) retain the original valueData reference, because
      // it is shared with the ArrayIntList from which they were created
      if (toIndex < 0) {
        clone.parent = null;
        clone.sibling = null;
        clone.child = null;
        clone.valueData = new int[valueData.length];
        System.arraycopy(valueData, 0, clone.valueData, 0, valueData.length);
      }

      return clone;
    }
    catch (final CloneNotSupportedException e) {
      throw new RuntimeException(e);
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
    final int len = toIndex > -1 ? toIndex : size;
    for (int i = fromIndex; i < len; ++i)
      hashCode = 31 * hashCode + Integer.hashCode(valueData[i]);

    return hashCode;
  }

  /**
   * Compares the specified object with this list for equality. Returns
   * {@code true} if and only if the specified object is also a
   * {@link ArrayIntList}, both lists have the same size, and all corresponding
   * pairs of values in the two lists are <i>equal</i>. In other words, two
   * lists are defined to be equal if they contain the same values in the same
   * order.
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof ArrayIntList))
      return false;

    final ArrayIntList that = (ArrayIntList)obj;
    if (size() != that.size())
      return false;

    final int toIndex = this.toIndex > -1 ? this.toIndex : size;
    final int thatToIndex = that.toIndex > -1 ? that.toIndex : that.size;
    return ArrayUtil.equals(valueData, fromIndex, toIndex, that.valueData, that.fromIndex, thatToIndex);
  }

  /**
   * Returns a string representation of this list. The string representation
   * consists of a list of the list's values in the order they are stored in the
   * underlying array, enclosed in square brackets ({@code "[]"}). Adjacent
   * values are separated by the characters {@code ", "} (comma and space).
   * Values are converted to strings as by {@link String#valueOf(Object)}.
   *
   * @return A string representation of this list.
   */
  @Override
  public String toString() {
    return "[" + ArrayUtil.toString(valueData, ", ", fromIndex, size()) + "]";
  }
}