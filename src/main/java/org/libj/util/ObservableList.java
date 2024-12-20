/* Copyright (c) 2016 LibJ
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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * A {@link DelegateList} that provides callback methods to observe the retrieval, addition, and removal of elements, either due to
 * direct method invocation on the list instance itself, or via {@link #iterator()}, {@link #listIterator()},
 * {@link #subList(int,int)}, and any other entrypoint that facilitates access to the elements in this list, either for modification
 * or retrieval.
 *
 * @param <E> The type of elements in this list.
 * @param <L> The type of underlying list.
 * @see #beforeGet(int,ListIterator)
 * @see #afterGet(int,Object,ListIterator,RuntimeException)
 * @see #beforeAdd(int,Object,Object)
 * @see #afterAdd(int,Object,RuntimeException)
 * @see #beforeRemove(int)
 * @see #afterRemove(Object,RuntimeException)
 * @see #beforeSet(int,Object)
 * @see #afterSet(int,Object,RuntimeException)
 */
public abstract class ObservableList<E,L extends List<E>> extends DelegateList<E,L> {
  protected static final Object preventDefault = ObservableCollection.preventDefault;

  private final int fromIndex;
  private int toIndex;

  /**
   * Creates a new {@link ObservableList} with the specified target {@link List}.
   *
   * @param list The target {@link List}.
   * @throws NullPointerException If {@code list} is null.
   */
  public ObservableList(final L list) {
    this(list, 0, -1);
  }

  /**
   * Creates a new {@link ObservableList} with the specified target list, and from and to indexes to limit the scope of the target
   * list.
   *
   * @param list The target {@link List} object.
   * @param fromIndex The starting index as the lower limit of the elements in the target list, inclusive.
   * @param toIndex The starting index as the upper limit of the elements in the target list, exclusive.
   * @throws NullPointerException If {@code list} is null.
   */
  protected ObservableList(final L list, final int fromIndex, final int toIndex) {
    super(list);
    this.fromIndex = fromIndex;
    this.toIndex = toIndex;
  }

  /**
   * Callback method that is invoked immediately before an element is retrieved from the enclosed {@link List}.
   *
   * @param index The index of the element to be retrieved from the enclosed {@link List}.
   * @param iterator The {@link ListIterator} instance if the get is a result of an iterator reference, otherwise {@code null}.
   */
  protected void beforeGet(final int index, final ListIterator<E> iterator) {
  }

  /**
   * Callback method that is invoked immediately after an element is retrieved from the enclosed {@link List}.
   *
   * @param index The index of the element retrieved from the enclosed {@link List}.
   * @param element The element retrieved from the enclosed {@link List}.
   * @param iterator The {@link Iterator} instance if the get is a result of an iterator reference, otherwise {@code null}.
   * @param e A {@link RuntimeException} that occurred during the get operation, or {@code null} if no exception occurred.
   */
  protected void afterGet(final int index, final E element, final ListIterator<? super E> iterator, final RuntimeException e) {
  }

  /**
   * Callback method that is invoked immediately before an element is added to the enclosed {@link List}.
   *
   * @implNote It is possible for {@code index} to be {@code -1}, in case the <u>add</u> operation is executed from an
   *           {@link Iterator} when a prior {@link Iterator#remove()} or {@link ListIterator#add(Object)} or
   *           {@link ListIterator#set(Object)} has already been called.
   * @param index The index for the element to be added to the enclosed {@link List}.
   * @param element The element to be added to the enclosed {@link List}.
   * @param preventDefault The object to return if the subsequent {@code add} operation is to be prevented.
   * @return The element to be added to the enclosed {@link List}.
   */
  protected Object beforeAdd(final int index, final E element, final Object preventDefault) {
    return element;
  }

  /**
   * Callback method that is invoked immediately after an element is added to the enclosed {@link List}.
   *
   * @param index The index of the element added to the enclosed {@link List}.
   * @param element The element to be added to the enclosed {@link List}.
   * @param e A {@link RuntimeException} that occurred during the add operation, or {@code null} if no exception occurred.
   */
  protected void afterAdd(final int index, final E element, final RuntimeException e) {
  }

  /**
   * Callback method that is invoked immediately before an element is removed from the enclosed {@link List}.
   *
   * @implNote It is possible for {@code index} to be {@code -1}, in case the <u>remove</u> operation is executed from an
   *           {@link Iterator} when a prior {@link Iterator#remove()} or {@link ListIterator#add(Object)} or
   *           {@link ListIterator#set(Object)} has already been called.
   * @param index The index of the element to be removed from the enclosed {@link List}.
   * @return If this method returns {@code true}, the subsequent <u>remove</u> operation will be performed; if this method returns
   *         {@code false}, the subsequent <u>remove</u> operation will not be performed.
   */
  protected boolean beforeRemove(final int index) {
    return true;
  }

  /**
   * Callback method that is invoked immediately after an element is removed from the enclosed {@link List}.
   *
   * @param element The element removed from the enclosed {@link List}, or attempted to be removed from the {@link List} in case of a
   *          {@link RuntimeException}.
   * @param e A {@link RuntimeException} that occurred during the remove operation, or {@code null} if no exception occurred.
   */
  protected void afterRemove(final Object element, final RuntimeException e) {
  }

  /**
   * Callback method that is invoked immediately before an element is set at an index in the enclosed {@link List}.
   *
   * @implNote It is possible for {@code index} to be {@code -1}, in case the <u>set</u> operation is executed from an
   *           {@link Iterator} when a prior {@link Iterator#remove()} or {@link ListIterator#add(Object)} or
   *           {@link ListIterator#set(Object)} has already been called.
   * @param index The index for the element to be set in the enclosed {@link List}.
   * @param newElement The element to be set in the enclosed {@link List}.
   * @return If this method returns {@code true}, the subsequent <u>set</u> operation will be performed; if this method returns
   *         {@code false}, the subsequent <u>set</u> operation will not be performed.
   */
  protected boolean beforeSet(final int index, final E newElement) {
    return true;
  }

  /**
   * Callback method that is invoked immediately after an element is set at an index to the enclosed {@link List}.
   *
   * @param index The index of the element set in the enclosed {@link List}.
   * @param oldElement The old element at the index of the enclosed {@link List}.
   * @param e A {@link RuntimeException} that occurred during the set operation, or {@code null} if no exception occurred.
   */
  protected void afterSet(final int index, final E oldElement, final RuntimeException e) {
  }

  /**
   * Delegate method that is invoked for all {@link Object#equals(Object)} operations. This method is intended to be overridden to
   * support behavior that is not inherently possible with the default reliance on {@link Object#equals(Object)} for the determination
   * of object equality by this {@link ObservableList}.
   *
   * @param o1 An object.
   * @param o2 An object to be compared with a for equality.
   * @return {@code true} if {@code o1} is equal to {@code o2}; {@code false} otherwise.
   */
  protected boolean equals(final Object o1, final Object o2) {
    return Objects.equals(o1, o2);
  }

  @SuppressWarnings("unchecked")
  protected void addFast(final int index, E element) {
    final Object beforeAdd = beforeAdd(index, element, preventDefault);
    if (beforeAdd == preventDefault)
      return;

    element = (E)beforeAdd;
    RuntimeException exception = null;
    try {
      super.add(index + fromIndex, element);
      if (toIndex > -1)
        ++toIndex;
    }
    catch (final RuntimeException re) {
      exception = re;
    }

    afterAdd(index, element, exception);
    if (exception != null)
      throw exception;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeAdd(int,Object,Object)} and {@link #afterAdd(int,Object,RuntimeException)} are called
   * immediately before and after the enclosed {@link List} is modified for the addition of the element. If
   * {@link #beforeAdd(int,Object,Object)} returns {@code false}, the element will not be added.
   */
  @Override
  public boolean add(final E e) {
    final int size = size();
    addFast(size, e);
    return size != size();
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeAdd(int,Object,Object)} and {@link #afterAdd(int,Object,RuntimeException)} are called
   * immediately before and after the enclosed {@link List} is modified for the addition of the element. If
   * {@link #beforeAdd(int,Object,Object)} returns {@code false}, the element will not be added.
   */
  @Override
  public void add(final int index, final E element) {
    assertRange("index", index, "size()", size(), true);
    addFast(index, element);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeAdd(int,Object,Object)} and {@link #afterAdd(int,Object,RuntimeException)} are called
   * immediately before and after the enclosed {@link List} is modified for the addition of each element in the specified
   * {@link Collection}. All elements for which {@link #beforeAdd(int,Object,Object)} returns {@code false} will not be added to this
   * {@link List}.
   */
  @Override
  public boolean addAll(final Collection<? extends E> c) {
    return CollectionUtil.addAll(this, c);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeAdd(int,Object,Object)} and {@link #afterAdd(int,Object,RuntimeException)} are called
   * immediately before and after the enclosed {@link List} is modified for the addition of each element in the specified
   * {@link Collection}. All elements for which {@link #beforeAdd(int,Object,Object)} returns {@code false} will not be added to this
   * {@link List}.
   */
  @Override
  public boolean addAll(int index, final Collection<? extends E> c) {
    if (c.size() == 0)
      return false;

    for (final E e : c) // [C]
      add(index++, e);

    return true;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(int)} and {@link #afterRemove(Object,RuntimeException)} are called immediately before
   * and after the enclosed {@link List} is modified for the removal of each element. All elements for which
   * {@link #beforeRemove(int)} returns {@code false} will not be removed from this {@link List}.
   */
  @Override
  public void clear() {
    if (isRandomAccess()) {
      for (int i = size() - 1; i >= 0; --i) // [RA]
        remove(i);
    }
    else {
      for (int i = 0, i$ = size(); i < i$; ++i) // [RA]
        remove(i);
    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int,ListIterator)} and {@link #afterGet(int,Object,ListIterator,RuntimeException)} are
   * called immediately before and after each member of the enclosed list is referenced.
   */
  @Override
  public boolean contains(final Object o) {
    final int i$ = size();
    if (i$ == 0)
      return false;

    if (isRandomAccess()) {
      int i = 0;
      do // [RA]
        if (equals(o, get(i)))
          return true;
      while (++i < i$);
    }
    else {
      final Iterator<?> it = iterator(); // [I]
      if (equals(o, it.next()))
        return true;
      while (it.hasNext());
    }

    return false;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int,ListIterator)} and {@link #afterGet(int,Object,ListIterator,RuntimeException)} are
   * called immediately before and after each member of the enclosed list is referenced.
   */
  @Override
  public boolean containsAll(final Collection<?> c) {
    return CollectionUtil.containsAll(this, c);
  }

  protected E getFast(final int index) {
    beforeGet(index, null);
    E value = null;
    RuntimeException exception = null;
    try {
      value = super.get(index + fromIndex);
    }
    catch (final RuntimeException re) {
      exception = re;
    }

    afterGet(index, value, null, exception);
    if (exception != null)
      throw exception;

    return value;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int,ListIterator)} and {@link #afterGet(int,Object,ListIterator,RuntimeException)} are
   * called immediately before and after the get operation on the enclosed {@link List}.
   */
  @Override
  public E get(final int index) {
    assertRange("index", index, "size()", size(), false);
    return getFast(index);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int,ListIterator)} and {@link #afterGet(int,Object,ListIterator,RuntimeException)} are
   * called immediately before and after each member of the enclosed list is referenced.
   */
  @Override
  public int indexOf(final Object o) {
    final int i$ = size();
    if (i$ == 0)
      return -1;

    if (isRandomAccess()) {
      int i = 0;
      do // [RA]
        if (equals(o, get(i)))
          return i;
      while (++i < i$);
    }
    else {
      int i = -1;
      final Iterator<?> it = iterator();
      do { // [I]
        ++i;
        if (equals(o, it.next()))
          return i;
      }
      while (it.hasNext());
    }

    return -1;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int,ListIterator)} and {@link #afterGet(int,Object,ListIterator,RuntimeException)} are
   * called immediately before and after each member of the enclosed list is referenced.
   */
  @Override
  public int lastIndexOf(final Object o) {
    final int i$ = size();
    if (i$ == 0)
      return -1;

    if (isRandomAccess()) {
      int i = i$;
      do // [RA]
        if (equals(o, get(--i)))
          return i;
      while (i > 0);
    }
    else {
      int i = i$;
      final ListIterator<?> it = listIterator(i$);
      do { // [I]
        --i;
        if (equals(o, it.previous()))
          return i;
      }
      while (it.hasPrevious());
    }

    return -1;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Calling {@link Iterator#remove()} will delegate a callback to {@link #beforeRemove(int)} and
   * {@link #afterRemove(Object,RuntimeException)} on this instance. All elements for which {@link #beforeRemove(int)} returns
   * {@code false} will not be removed from this {@link List}.
   */
  @Override
  public Iterator<E> iterator() {
    return listIterator();
  }

  /**
   * {@inheritDoc}
   * <p>
   * Calling {@link ListIterator#remove()} will delegate a callback to {@link #beforeRemove(int)} and
   * {@link #afterRemove(Object,RuntimeException)} on this instance. All elements for which {@link #beforeRemove(int)} returns
   * {@code false} will not be removed from this {@link List}. Calling {@link ListIterator#set(Object)} will delegate a callback to
   * {@link #beforeSet(int,Object)} and {@link #afterSet(int,Object,RuntimeException)} on this instance. All elements for which
   * {@link #beforeSet(int,Object)} returns {@code false} will not be set in this {@link List}. Calling
   * {@link ListIterator#add(Object)} will delegate a callback to {@link #beforeAdd(int,Object,Object)} and
   * {@link #afterAdd(int,Object,RuntimeException)} on this instance. All elements for which {@link #beforeAdd(int,Object,Object)}
   * returns {@code false} will not be added to this {@link List}.
   */
  @Override
  public ListIterator<E> listIterator() {
    return listIterator(0);
  }

  /**
   * A {@link CursorListIterator} that delegates callback methods to the defining {@link ObservableList} instance for the retrieval,
   * addition, and removal of elements.
   */
  protected class ObservableListIterator extends CursorListIterator<E> {
    private E current;

    /**
     * Creates a new {@link CursorListIterator} for the specified {@link ListIterator}.
     *
     * @param iterator The {@link ListIterator}.
     * @throws NullPointerException If the specified {@link ListIterator} is null.
     */
    protected ObservableListIterator(final ListIterator<? extends E> iterator) {
      super(iterator);
    }

    @Override
    public boolean hasNext() {
      return nextIndex() < size();
    }

    @Override
    public E next() {
      final int index = nextIndex();
      beforeGet(index, this);
      RuntimeException exception = null;
      E value = null;
      try {
        value = super.next();
      }
      catch (final RuntimeException re) {
        exception = re;
      }

      afterGet(index, value, this, exception);
      if (exception != null)
        throw exception;

      return current = value;
    }

    @Override
    public boolean hasPrevious() {
      return previousIndex() >= 0;
    }

    @Override
    public E previous() {
      final int index = previousIndex();
      beforeGet(index, this);
      RuntimeException exception = null;
      E value = null;
      try {
        value = super.previous();
      }
      catch (final RuntimeException re) {
        exception = re;
      }

      afterGet(index, value, this, exception);
      if (exception != null)
        throw exception;

      return current = value;
    }

    @Override
    public int nextIndex() {
      return super.nextIndex() - fromIndex;
    }

    @Override
    public int previousIndex() {
      return super.previousIndex() - fromIndex;
    }

    @Override
    public void remove() {
      if (!beforeRemove(indexOfLast()))
        return;

      final E element = this.current;
      RuntimeException exception = null;
      try {
        super.remove();
        if (toIndex > -1)
          --toIndex;
      }
      catch (final RuntimeException re) {
        exception = re;
      }

      afterRemove(element, exception);
      if (exception != null)
        throw exception;
    }

    @Override
    public void set(final E e) {
      final int index = indexOfLast();
      if (!beforeSet(index, e))
        return;

      final E element = this.current;
      RuntimeException exception = null;
      try {
        super.set(e);
      }
      catch (final RuntimeException re) {
        exception = re;
      }

      afterSet(index, element, exception);
      if (exception != null)
        throw exception;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void add(E e) {
      final int index = indexForNext();
      final Object beforeAdd = beforeAdd(index, e, preventDefault);
      if (beforeAdd == preventDefault)
        return;

      e = (E)beforeAdd;
      RuntimeException exception = null;
      try {
        super.add(e);
        if (toIndex > -1)
          ++toIndex;
      }
      catch (final RuntimeException re) {
        exception = re;
      }

      afterAdd(index, e, exception);
      if (exception != null)
        throw exception;
    }
  }

  /**
   * Factory method that returns a new instance of an {@link ObservableListIterator} for the specified {@link ListIterator
   * ListIterator&lt;E&gt;}. This method is intended to be overridden by subclasses in order to provide instances of the subclass.
   *
   * @param iterator The target {@link ListIterator ListIterator&lt;E&gt;}.
   * @return A new instance of an {@link ObservableListIterator}.
   */
  protected ObservableListIterator newListIterator(final ListIterator<? extends E> iterator) {
    return new ObservableListIterator(iterator);
  }

  /**
   * {@inheritDoc}
   * <p>
   * Calling {@link ListIterator#remove()} will delegate a callback to {@link #beforeRemove(int)} and
   * {@link #afterRemove(Object,RuntimeException)} on this instance. All elements for which {@link #beforeRemove(int)} returns
   * {@code false} will not be removed from this {@link List}. Calling {@link ListIterator#set(Object)} will delegate a callback to
   * {@link #beforeSet(int,Object)} and {@link #afterSet(int,Object,RuntimeException)} on this instance. All elements for which
   * {@link #beforeSet(int,Object)} returns {@code false} will not be set in this {@link List}. Calling
   * {@link ListIterator#add(Object)} will delegate a callback to {@link #beforeAdd(int,Object,Object)} and
   * {@link #afterAdd(int,Object,RuntimeException)} on this instance. All elements for which {@link #beforeAdd(int,Object,Object)}
   * returns {@code false} will not be added to this {@link List}.
   */
  @Override
  public ListIterator<E> listIterator(final int index) {
    assertRange("index", index, "size()", size(), true);
    return newListIterator(super.listIterator(index + fromIndex));
  }

  protected E removeFast(final int index) {
    if (!beforeRemove(index))
      return null;

    E value = null;
    RuntimeException exception = null;
    try {
      value = super.remove(index + fromIndex);
      if (toIndex > -1)
        --toIndex;
    }
    catch (final RuntimeException re) {
      exception = re;
    }

    afterRemove(value, exception);
    if (exception != null)
      throw exception;

    return value;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(int)} and {@link #afterRemove(Object,RuntimeException)} are called immediately before
   * and after the enclosed {@link List} is modified for the removal of the element. If {@link #beforeRemove(int)} returns
   * {@code false}, the element will not be removed, and this method will return {@code null}.
   */
  @Override
  public E remove(final int index) {
    assertRange("index", index, "size()", size(), false);
    return removeFast(index);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(int)} and {@link #afterRemove(Object,RuntimeException)} are called immediately before
   * and after the enclosed {@link List} is modified. If {@link #beforeRemove(int)} returns {@code false}, the element will not be
   * removed.
   */
  @Override
  public boolean remove(final Object o) {
    final int index = indexOf(o);
    if (index < 0)
      return false;

    final int size = size();
    removeFast(index);
    return size != size();
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeSet(int,Object)} and {@link #afterSet(int,Object,RuntimeException)} are called immediately
   * before and after the enclosed {@link List} is modified. If {@link #beforeSet(int,Object)} returns {@code false}, the element will
   * not be set, and this method will return {@code null}.
   */
  @Override
  public E set(final int index, final E element) {
    assertRange("index", index, "size()", size(), false);
    if (!beforeSet(index, element))
      return null;

    E oldElement = null;
    RuntimeException exception = null;
    try {
      oldElement = super.set(index + fromIndex, element);
    }
    catch (final RuntimeException re) {
      exception = re;
    }

    afterSet(index, oldElement, exception);
    if (exception != null)
      throw exception;

    return oldElement;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(int)} and {@link #afterRemove(Object,RuntimeException)} are called immediately before
   * and after the enclosed {@link List} is modified for the removal of each element in the specified {@link Collection}. All elements
   * for which {@link #beforeRemove(int)} returns {@code false} will not be removed from this {@link List}.
   *
   * @see Collection#removeAll(Collection)
   */
  @Override
  public boolean removeAll(final Collection<?> c) {
    return CollectionUtil.removeAll(this, c);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(int)} and {@link #afterRemove(Object,RuntimeException)} are called immediately before
   * and after the enclosed {@link List} is modified for the removal of each element. All elements for which
   * {@link #beforeRemove(int)} returns {@code false} will not be removed from this {@link List}.
   */
  @Override
  public boolean removeIf(final Predicate<? super E> filter) {
    final int i$ = size();
    if (i$ == 0)
      return false;

    if (isRandomAccess()) {
      int i = i$;
      do // [RA]
        if (filter.test(getFast(--i)))
          removeFast(i);
      while (i > 0);
    }
    else {
      final Iterator<E> it = iterator();
      do // [I]
        if (filter.test(it.next()))
          it.remove();
      while (it.hasNext());
    }

    return i$ != size();
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeRemove(int)} and {@link #afterRemove(Object,RuntimeException)} are called immediately before
   * and after the enclosed {@link List} is modified for the removal of each element not in the specified {@link Collection}. All
   * elements for which {@link #beforeRemove(int)} returns {@code false} will not be removed from this {@link List}.
   */
  @Override
  public boolean retainAll(final Collection<?> c) {
    final int i$ = size();
    if (i$ == 0)
      return false;

    if (c.size() > 0) {
      if (isRandomAccess()) {
        int i = i$;
        do // [RA]
          if (!c.contains(getFast(--i)))
            remove(i);
        while (i > 0);
      }
      else {
        final Iterator<E> it = iterator();
        do // [I]
          if (!c.contains(it.next()))
            it.remove();
        while (it.hasNext());
      }

      return i$ != size();
    }

    clear();
    return true;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int, ListIterator)} and {@link #afterGet(int,Object,ListIterator,RuntimeException)} are
   * called immediately before and after each element of the enclosed list is retrieved.
   * <p>
   * The callback methods {@link #beforeSet(int,Object)} and {@link #afterSet(int,Object,RuntimeException)} are called immediately
   * before and after the enclosed {@link List} is modified. If {@link #beforeSet(int,Object)} returns {@code false}, the element will
   * not be set.
   */
  @Override
  public void replaceAll(final UnaryOperator<E> operator) {
    replaceAll$(operator);
  }

  @Override
  public int size() {
    return (toIndex > -1 ? toIndex : super.size()) - fromIndex;
  }

  /**
   * An {@link ObservableList} that delegates callback methods to the parent {@link ObservableList} instance for the retrieval,
   * addition, and removal of elements.
   */
  protected class ObservableSubList extends ObservableList<E,L> {
    /**
     * Creates a new {@link org.libj.util.ObservableList.ObservableSubList} for the specified {@link List List&lt;E&gt;}.
     *
     * @param list The {@link List List&lt;E&gt;}.
     * @param fromIndex The starting index as the lower limit of the elements in the target list, inclusive.
     * @param toIndex The starting index as the upper limit of the elements in the target list, exclusive.
     * @throws NullPointerException If the specified {@link List List&lt;E&gt;} is null.
     */
    protected ObservableSubList(final L list, final int fromIndex, final int toIndex) {
      super(list, fromIndex, toIndex);
    }

    @Override
    protected void beforeGet(final int index, final ListIterator<E> iterator) {
      ObservableList.this.beforeGet(index, iterator);
    }

    @Override
    protected void afterGet(final int index, final E element, final ListIterator<? super E> iterator, final RuntimeException e) {
      ObservableList.this.afterGet(index, element, iterator, e);
    }

    @Override
    protected Object beforeAdd(final int index, final E element, final Object preventDefault) {
      return ObservableList.this.beforeAdd(index, element, preventDefault);
    }

    @Override
    protected void afterAdd(final int index, final E element, final RuntimeException e) {
      ObservableList.this.afterAdd(index, element, e);
    }

    @Override
    protected boolean beforeRemove(final int index) {
      return ObservableList.this.beforeRemove(index);
    }

    @Override
    protected void afterRemove(final Object element, final RuntimeException e) {
      ObservableList.this.afterRemove(element, e);
    }

    @Override
    protected boolean beforeSet(final int index, final E newElement) {
      return ObservableList.this.beforeSet(index, newElement);
    }

    @Override
    protected void afterSet(final int index, final E oldElement, final RuntimeException e) {
      ObservableList.this.afterSet(index, oldElement, e);
    }
  }

  /**
   * Factory method that returns a new instance of an {@link ObservableSubList} for the specified {@code fromIndex} and
   * {@code toIndex}. This method is intended to be overridden by subclasses in order to provide instances of the subclass.
   *
   * @param fromIndex The starting index as the lower limit of the elements in the target list, inclusive.
   * @param toIndex The starting index as the upper limit of the elements in the target list, exclusive.
   * @return A new instance of an {@link ObservableSubList}.
   */
  protected ObservableSubList newSubList(final int fromIndex, final int toIndex) {
    return new ObservableSubList((L)target, fromIndex, toIndex);
  }

  @Override
  public ObservableList<E,L> subList(final int fromIndex, final int toIndex) {
    assertRange("fromIndex", fromIndex, "toIndex", toIndex, "size()", size());
    return newSubList(fromIndex, toIndex);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int,ListIterator)} and {@link #afterGet(int,Object,ListIterator,RuntimeException)} are
   * called immediately before and after each member of the enclosed list is referenced.
   */
  @Override
  public void sort(final Comparator<? super E> c) {
    sort$(c);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int,ListIterator)} and {@link #afterGet(int,Object,ListIterator,RuntimeException)} are
   * called immediately before and after each member of the enclosed list is referenced.
   */
  @Override
  public Object[] toArray() {
    final int size = size();
    if (size == 0)
      return ArrayUtil.EMPTY_ARRAY;

    final Object[] a = new Object[size];
    toArray(a, size);
    return a;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int,ListIterator)} and {@link #afterGet(int,Object,ListIterator,RuntimeException)} are
   * called immediately before and after each member of the enclosed list is referenced.
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> T[] toArray(T[] a) {
    final int size = size();
    if (size > 0) {
      if (a.length < size) {
        a = (T[])Array.newInstance(a.getClass().getComponentType(), size);
        toArray(a, size);
        return a;
      }

      toArray(a, size);
    }

    if (a.length > size)
      a[size] = null;

    return a;
  }

  private void toArray(final Object[] a, final int i$) {
    if (i$ == 0)
      return;

    if (isRandomAccess()) {
      int i = 0;
      do // [RA]
        a[i] = get(i);
      while (++i < i$);
    }
    else {
      int i = -1;
      final Iterator<E> it = iterator();
      do // [I]
        a[++i] = it.next();
      while (it.hasNext());
    }
  }

  // /**
  // * {@inheritDoc}
  // * <p>
  // * The callback methods {@link #beforeGet(int,ListIterator)} and
  // * {@link #afterGet(int,Object,ListIterator,RuntimeException)} are called
  // * immediately before and after each member of the enclosed list is
  // * referenced.
  // */
  // @Override
  // public <T>T[] toArray(final IntFunction<T[]> generator) {
  // return toArray$(generator);
  // }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int, ListIterator)} and {@link #afterGet(int,Object,ListIterator,RuntimeException)} are
   * called immediately before and after each element of the enclosed list is retrieved.
   */
  @Override
  public void forEach(final Consumer<? super E> action) {
    forEach$(action);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int, ListIterator)} and {@link #afterGet(int,Object,ListIterator,RuntimeException)} are
   * called immediately before and after each element of the enclosed list is retrieved.
   */
  @Override
  public Spliterator<E> spliterator() {
    return spliterator$();
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int, ListIterator)} and {@link #afterGet(int,Object,ListIterator,RuntimeException)} are
   * called immediately before and after each element of the enclosed list is retrieved.
   */
  @Override
  public Stream<E> stream() {
    return stream$();
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int, ListIterator)} and {@link #afterGet(int,Object,ListIterator,RuntimeException)} are
   * called immediately before and after each element of the enclosed list is retrieved.
   */
  @Override
  public Stream<E> parallelStream() {
    return parallelStream$();
  }

  private void touchElements() {
    final int i$ = size();
    if (i$ == 0)
      return;

    if (isRandomAccess()) {
      int i = 0;
      do // [RA]
        get(i);
      while (++i < i$);
    }
    else {
      final Iterator<E> it = iterator();
      do // [I]
        it.next();
      while (it.hasNext());
    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int, ListIterator)} and {@link #afterGet(int,Object,ListIterator,RuntimeException)} are
   * called immediately before and after each element of the enclosed list is retrieved.
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof List))
      return false;

    final List<?> that = (List<?>)obj;
    final int i$ = size();
    if (i$ != that.size())
      return false;

    if (i$ == 0)
      return true;

    int i = 0;
    if (isRandomAccess()) {
      if (CollectionUtil.isRandomAccess(that)) {
        do // [RA]
          if (!equals(get(i), that.get(i)))
            return false;
        while (++i < i$);
      }
      else {
        final Iterator<?> thatIt = that.iterator();
        do // [I]
          if (!equals(get(i), thatIt.next()))
            return false;
        while (++i < i$);
      }
    }
    else if (CollectionUtil.isRandomAccess(that)) {
      final Iterator<?> thisIt = that.iterator();
      do // [RA]
        if (!equals(thisIt.next(), that.get(i)))
          return false;
      while (++i < i$);
    }
    else {
      final Iterator<?> thisIt = that.iterator();
      final Iterator<?> thatIt = that.iterator();
      do // [I]
        if (!equals(thisIt.next(), thatIt.next()))
          return false;
      while (++i < i$);
    }

    return true;
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int, ListIterator)} and {@link #afterGet(int,Object,ListIterator,RuntimeException)} are
   * called immediately before and after each element of the enclosed list is retrieved.
   */
  @Override
  public int hashCode() {
    if (target == null)
      return 31;

    touchElements();
    return 31 + target.hashCode();
  }

  /**
   * {@inheritDoc}
   * <p>
   * The callback methods {@link #beforeGet(int, ListIterator)} and {@link #afterGet(int,Object,ListIterator,RuntimeException)} are
   * called immediately before and after each element of the enclosed list is retrieved.
   */
  @Override
  public String toString() {
    if (target == null)
      return "null";

    touchElements();
    return String.valueOf(target);
  }
}