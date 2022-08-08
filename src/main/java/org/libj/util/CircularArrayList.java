/* Copyright (c) 2020 LibJ
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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * Resizable-array implementation of the {@link java.util.List} interface with circular indexing. This class is roughly equivalent
 * to {@link java.util.ArrayList}, except that it is optimized for removing elements at the front and back of the list to facilitate
 * use as a queue or deque.
 * <p>
 * The {@link #size()}, {@link #isEmpty()}, {@link #get(int)}, {@link #set(int,Object)}, {@link #iterator()},
 * {@link #listIterator()}, and {@link #listIterator(int)} operations run in constant time. The {@link #add(Object)} and
 * {@link #add(int,Object)} operations run in <i>amortized constant time</i>, that is, adding {@code n} elements requires O(n) time.
 * All of the other operations run in linear time (roughly speaking). The constant factor is low compared to that for the
 * {@link java.util.LinkedList} implementation.
 * <p>
 * Each {@link CircularArrayList} instance has a capacity, which is the size of the array used to store the elements in the list.
 * The capacity is always at least as large as the list size. As elements are added to an {@link CircularArrayList}, the capacity
 * grows automatically via {@link #ensureCapacity(int)}, and incurs a constant amortized time cost for each addition.
 * <p>
 * An application can increase the capacity of an {@link CircularArrayList} instance before adding a large number of elements using
 * the {@link #ensureCapacity(int)} operation. This may reduce the amount of incremental reallocation.
 * <p>
 * <b>This implementation is not synchronized</b>
 * <p>
 * If multiple threads access a {@link CircularArrayList} concurrently, and at least one of the threads modifies the list
 * structurally, it must be synchronized externally. (A structural modification is any operation that adds or deletes one or more
 * elements, or explicitly resizes the backing array; merely setting the value of an element is not a structural modification.) This
 * is typically accomplished by synchronizing on some object that naturally encapsulates the list. If no object exists, the list
 * should be "wrapped" using the {@link java.util.Collections#synchronizedList(java.util.List)} method. This is best done at
 * creation time, to prevent accidental unsynchronized access to the list:
 *
 * <pre>
 * List list = Collections.synchronizedList(new CircularArrayList(...));
 * </pre>
 *
 * The iterators returned by this class's {@link #iterator()} and {@link #listIterator()} methods are fail-fast: if list is
 * structurally modified at any time after the iterator is created, in any way except through the iterator's own remove or add
 * methods, the iterator will throw a {@link ConcurrentModificationException}. Thus, in the face of concurrent modification, the
 * iterator fails quickly and cleanly, rather than risking arbitrary, non-deterministic behavior at an undetermined time in the
 * future.
 * <p>
 * Note that the fail-fast behavior of an iterator cannot be guaranteed as it is, generally speaking, impossible to make any hard
 * guarantees in the presence of unsynchronized concurrent modification. Fail-fast iterators throw
 * {@link ConcurrentModificationException} on a best-effort basis. Therefore, it would be wrong to write a program that depended on
 * this exception for its correctness: <i>the fail-fast behavior of iterators should be used only to detect bugs</i>.
 *
 * @param <E> The type of elements in this list.
 */
public class CircularArrayList<E> extends AbstractList<E> implements Deque<E>, RandomAccess, Serializable {
  /** Default initial capacity. */
  private static final int DEFAULT_CAPACITY = 10;

  protected transient Object[] elementData;

  // head points to the first logical element in the array, and tail points to the element following the last. This means
  // that the list is empty when head == tail. It also means that the array array has to have an extra space in it.
  protected int head = 0;

  // head points to the first logical element in the array, and tail points to the element following the last. This means
  // that the list is empty when head == tail. It also means that the array array has to have an extra space in it.
  protected int tail = 0;

  // Strictly speaking, we don't need to keep a handle to size, as it can be calculated programmatically, but keeping it
  // makes the algorithms faster.
  protected int size = 0;

  /**
   * Constructs an empty list with an initial capacity of ten.
   */
  public CircularArrayList() {
    this(DEFAULT_CAPACITY);
  }

  /**
   * Constructs an empty list with the specified initial capacity.
   *
   * @param initialCapacity The initial capacity of the list.
   * @throws NegativeArraySizeException If the specified initial capacity is negative.
   */
  public CircularArrayList(final int initialCapacity) {
    elementData = new Object[initialCapacity];
  }

  /**
   * Constructs a list containing the elements of the specified collection, in the order they are returned by the collection's
   * iterator.
   *
   * @param c The collection whose elements are to be placed into this list.
   * @throws IllegalArgumentException If the specified collection is null.
   */
  public CircularArrayList(final Collection<? extends E> c) {
    tail = c.size();
    elementData = new Object[tail];
    c.toArray(elementData);
  }

  /**
   * Increases the capacity of this {@link CircularArrayList} instance, if necessary, to ensure that it can hold at least the number
   * of elements specified by the minimum capacity argument.
   *
   * @param minCapacity The desired minimum capacity.
   */
  public void ensureCapacity(final int minCapacity) {
    final int oldCapacity = elementData.length;
    if (minCapacity > oldCapacity) {
      int newCapacity = ((oldCapacity * 3) / 2) + 1;
      if (newCapacity < minCapacity)
        newCapacity = minCapacity;

      final Object[] newData = new Object[newCapacity];
      toArray(newData);
      tail = size;
      head = 0;
      elementData = newData;
    }
  }

  // Takes a logical index (as if head was always 0), and dereferences the index
  private int deref(final int index) {
    return (index + head) % elementData.length;
  }

  private void writeObject(final ObjectOutputStream s) throws IOException {
    s.writeInt(size);
    for (int i = 0; i != size; ++i) // [A]
      s.writeObject(elementData[deref(i)]);
  }

  private void readObject(final ObjectInputStream s) throws ClassNotFoundException, IOException {
    // Read in size of list and allocate array
    head = 0;
    size = tail = s.readInt();
    elementData = tail < DEFAULT_CAPACITY ? new Object[DEFAULT_CAPACITY] : new Object[tail];

    // Read in all elements in the proper order
    for (int i = 0; i < tail; ++i) // [A]
      elementData[i] = s.readObject();
  }

  @Override
  public boolean contains(final Object o) {
    return indexOf(o) >= 0;
  }

  @Override
  public int indexOf(final Object o) {
    if (o == null) {
      for (int i = 0; i < size; ++i) // [A]
        if (elementData[deref(i)] == null)
          return i;
    }
    else {
      for (int i = 0; i < size; ++i) // [A]
        if (o.equals(elementData[deref(i)]))
          return i;
    }

    return -1;
  }

  @Override
  public int lastIndexOf(final Object o) {
    if (o == null) {
      for (int i = size - 1; i >= 0; --i) // [A]
        if (elementData[deref(i)] == null)
          return i;
    }
    else {
      for (int i = size - 1; i >= 0; --i) // [A]
        if (o.equals(elementData[deref(i)]))
          return i;
    }

    return -1;
  }

  @Override
  @SuppressWarnings("unchecked")
  public E get(final int index) {
    assertRange("index", index, "size", size);
    return (E)elementData[deref(index)];
  }

  @Override
  public boolean add(final Object o) {
    ++modCount;
    ensureCapacity(size + 1 + 1);
    elementData[tail] = o;
    tail = (tail + 1) % elementData.length;
    ++size;
    return true;
  }

  @Override
  public void add(final int index, final Object element) {
    if (index == size) {
      add(element);
      return;
    }

    ++modCount;
    assertRange("index", index, "size", size);
    ensureCapacity(size + 1 + 1);
    final int pos = deref(index);
    if (pos == head) {
      head = (head - 1 + elementData.length) % elementData.length;
      elementData[head] = element;
    }
    else if (pos == tail) {
      elementData[tail] = element;
      tail = (tail + 1) % elementData.length;
    }
    else {
      if (pos > head && pos > tail) { // tail/head/pos
        System.arraycopy(elementData, pos, elementData, head - 1, pos - head + 1);
        head = (head - 1 + elementData.length) % elementData.length;
      }
      else { // head/pos/tail
        System.arraycopy(elementData, pos, elementData, pos + 1, tail - pos);
        tail = (tail + 1) % elementData.length;
      }

      elementData[pos] = element;
    }

    ++size;
  }

  @Override
  public boolean addAll(final Collection<? extends E> c) {
    final int addedSize = c.size();
    if (addedSize == 0)
      return false;

    ++modCount;
    ensureCapacity(size + addedSize + 1);
    final Iterator<? extends E> it = c.iterator();
    for (int i = 0; i < addedSize; ++i, ++size) { // [I]
      elementData[tail] = it.next();
      tail = (tail + 1) % elementData.length;
    }

    return true;
  }

  @Override
  public boolean addAll(final int index, final Collection<? extends E> c) {
    final int addedSize = c.size();
    if (addedSize == 0)
      return false;

    ++modCount;
    ensureCapacity(size + addedSize + 1);
    // FIXME: This is a very inefficient algorithm!
    for (final E e : c) // [C]
      add(index, e);

    return true;
  }

  @Override
  @SuppressWarnings("unchecked")
  public E set(final int index, final E element) {
    ++modCount;
    assertRange("index", index, "size", size);
    final int realIndex = deref(index);
    final E oldValue = (E)elementData[realIndex];
    elementData[realIndex] = element;
    return oldValue;
  }

  // This method is the main reason we re-wrote the class. It is optimized for removing first and last elements
  // but also allows you to remove in the middle of the list.
  @Override
  @SuppressWarnings("unchecked")
  public E remove(final int index) {
    ++modCount;
    assertRange("index", index, "size", size);
    final int pos = deref(index);

    final E e = (E)elementData[pos];

    elementData[pos] = null;
    // optimized for FIFO access, i.e. adding to back and removing from front
    if (pos == head) {
      head = (head + 1) % elementData.length;
    }
    else if (pos == tail) {
      tail = (tail - 1 + elementData.length) % elementData.length;
    }
    else if (pos > head && pos > tail) { // tail/head/pos
      System.arraycopy(elementData, head, elementData, head + 1, pos - head);
      head = (head + 1) % elementData.length;
    }
    else {
      System.arraycopy(elementData, pos + 1, elementData, pos, tail - pos - 1);
      tail = (tail - 1 + elementData.length) % elementData.length;
    }

    --size;
    return e;
  }

  @Override
  public boolean remove(final Object o) {
    final int index = indexOf(o);
    if (index < 0)
      return false;

    remove(index);
    return true;
  }

  @Override
  protected void removeRange(final int fromIndex, final int toIndex) {
    for (int i = fromIndex; i < toIndex; ++i) // [A]
      remove(i);
  }

  @Override
  public void addFirst(final E e) {
    add(0, e);
  }

  @Override
  public void addLast(final E e) {
    add(e);
  }

  @Override
  public boolean offerFirst(final E e) {
    add(0, e);
    return true;
  }

  @Override
  public boolean offerLast(final E e) {
    add(e);
    return true;
  }

  @Override
  public E removeFirst() {
    if (size == 0)
      throw new NoSuchElementException();

    return remove(0);
  }

  @Override
  public E removeLast() {
    if (size == 0)
      throw new NoSuchElementException();

    return remove(size - 1);
  }

  @Override
  public E pollFirst() {
    return size == 0 ? null : remove(0);
  }

  @Override
  public E pollLast() {
    return size == 0 ? null : remove(size - 1);
  }

  @Override
  public E getFirst() {
    if (size == 0)
      throw new NoSuchElementException();

    return get(0);
  }

  @Override
  public E getLast() {
    if (size == 0)
      throw new NoSuchElementException();

    return get(size - 1);
  }

  @Override
  public E peekFirst() {
    return size == 0 ? null : get(0);
  }

  @Override
  public E peekLast() {
    return size == 0 ? null : get(size - 1);
  }

  @Override
  public boolean removeFirstOccurrence(final Object o) {
    return remove(o);
  }

  @Override
  public boolean removeLastOccurrence(final Object o) {
    final int index = lastIndexOf(o);
    if (index < 0)
      return false;

    remove(index);
    return true;
  }

  @Override
  public boolean offer(final E e) {
    return add(e);
  }

  @Override
  public E remove() {
    if (size == 0)
      throw new NoSuchElementException();

    return removeFirst();
  }

  @Override
  public E poll() {
    return size == 0 ? null : removeFirst();
  }

  @Override
  public E element() {
    if (size == 0)
      throw new NoSuchElementException();

    return getFirst();
  }

  @Override
  public E peek() {
    return size == 0 ? null : getFirst();
  }

  @Override
  public void push(final E e) {
    offerFirst(e);
  }

  @Override
  public E pop() {
    if (size == 0)
      throw new NoSuchElementException();

    return removeFirst();
  }

  @Override
  public Iterator<E> descendingIterator() {
    return new Iterator<E>() {
      private int expectedModCount = modCount;
      private int index = size;

      @Override
      public boolean hasNext() {
        return index > 0;
      }

      @Override
      @SuppressWarnings("unchecked")
      public E next() {
        if (expectedModCount != modCount)
          throw new ConcurrentModificationException();

        return (E)elementData[deref(--index)];
      }
    };
  }

  @Override
  public int size() {
    // the size can also be worked out each time as: (tail + array.length - head) % array.length
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public void clear() {
    ++modCount;
    for (int i = 0; i != size; ++i) // [A]
      elementData[deref(i)] = null;

    head = tail = size = 0;
  }

  @Override
  public Object[] toArray() {
    return toArray(new Object[size]);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T>T[] toArray(T[] a) {
    if (size == 0)
      return a;

    if (a.length < size)
      a = (T[])Array.newInstance(a.getClass().getComponentType(), size);

    if (head < tail) {
      System.arraycopy(elementData, head, a, 0, tail - head);
    }
    else {
      System.arraycopy(elementData, head, a, 0, elementData.length - head);
      System.arraycopy(elementData, 0, a, elementData.length - head, tail);
    }

    if (a.length > size)
      a[size] = null;

    return a;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder("[");
    for (int i = 0; i < size; ++i) { // [A]
      if (i > 0)
        builder.append(", ");

      builder.append(elementData[deref(i)]);
    }

    return builder.append(']').toString();
  }
}