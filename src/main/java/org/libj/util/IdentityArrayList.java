/* Copyright (c) 2008 LibJ
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

/**
 * A {@link ArrayList} that resorts to the {@code ==} operator when checking an element for equality, instead of the
 * {@link Object#equals(Object)} method. This class overrides all methods that perform a test for equality of elements in the list,
 * and supports the sub-list operation by returning an {@link IdentitySubList}.
 * <ul>
 * <li>{@link #contains(Object)}</li>
 * <li>{@link #containsAll(Collection)}</li>
 * <li>{@link #indexOf(Object)}</li>
 * <li>{@link #lastIndexOf(Object)}</li>
 * <li>{@link #remove(Object)}</li>
 * <li>{@link #removeAll(Collection)}</li>
 * <li>{@link #retainAll(Collection)}</li>
 * <li>{@link #subList(int,int)}</li>
 * <li>{@link #clone()}</li>
 * </ul>
 *
 * @param <E> The type of elements in this list.
 * @see ArrayList
 */
public class IdentityArrayList<E> extends ArrayList<E> {
  /**
   * Constructs an empty list with the specified initial capacity.
   *
   * @param initialCapacity The initial capacity of the list.
   * @throws IllegalArgumentException If the specified initial capacity is negative.
   */
  public IdentityArrayList(final int initialCapacity) {
    super(initialCapacity);
  }

  /**
   * Constructs a list containing the elements of the specified collection, in the order they are returned by the collection's
   * iterator.
   *
   * @param c The collection whose elements are to be placed into this list.
   * @throws NullPointerException If the specified collection is null.
   */
  public IdentityArrayList(final Collection<? extends E> c) {
    super(c);
  }

  /**
   * Constructs an empty list with an initial capacity of ten.
   */
  public IdentityArrayList() {
    super();
  }

  @Override
  public boolean contains(final Object o) {
    return indexOf(o) > -1;
  }

  @Override
  public int indexOf(final Object o) {
    for (int i = 0, i$ = size(); i < i$; ++i) // [RA]
      if (o == get(i))
        return i;

    return -1;
  }

  @Override
  public int lastIndexOf(final Object o) {
    for (int i = size() - 1; i >= 0; --i) // [RA]
      if (o == get(i))
        return i;

    return -1;
  }

  @Override
  public boolean remove(final Object o) {
    final int index = indexOf(o);
    if (index < 0)
      return false;

    return remove(index) != null;
  }

  @Override
  public boolean removeAll(final Collection<?> c) {
    return CollectionUtil.removeAll(this, c);
  }

  @Override
  public boolean retainAll(final Collection<?> c) {
    if (c.size() > 0) {
      final int size = size();
      OUT:
      for (int i = 0; i < size; ++i) { // [RA]
        final Object o = get(i);
        for (final Object obj : c) // [C]
          if (obj == o)
            continue OUT;

        remove(i);
      }

      return size != size();
    }

    if (size() == 0)
      return false;

    clear();
    return true;
  }

  /**
   * A {@link DelegateList} providing the same behavior of {@link IdentityArrayList} to the class returned by
   * {@link ArrayList#subList(int,int)}.
   *
   * @param <L> The type parameter of the sub-list, conforming to {@link RandomAccess} interface.
   */
  protected class IdentitySubList<L extends List<E> & RandomAccess> extends DelegateList<E,L> implements RandomAccess {
    /**
     * Creates a new {@link org.libj.util.IdentityArrayList.IdentitySubList} with the specified subList target.
     *
     * @param target The subList to which the method calls of this instance will be delegated.
     */
    public IdentitySubList(final L target) {
      super(target);
    }

    @Override
    public boolean contains(final Object o) {
      return indexOf(o) > -1;
    }

    @Override
    public int indexOf(final Object o) {
      for (int i = 0, i$ = target.size(); i < i$; ++i) // [RA]
        if (o == target.get(i))
          return i;

      return -1;
    }

    @Override
    public int lastIndexOf(final Object o) {
      for (int i = target.size() - 1; i >= 0; --i) // [RA]
        if (o == target.get(i))
          return i;

      return -1;
    }

    @Override
    public boolean remove(final Object o) {
      final int index = indexOf(o);
      return index > -1 && target.remove(index) != null;
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
      return CollectionUtil.removeAll(this, c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
      if (c == null)
        return false;

      if (c.size() == 0 && target.size() != 0) {
        target.clear();
        return true;
      }

      boolean modified = false;
      OUT:
      for (int i = 0, i$ = c.size(); i < i$; ++i) { // [RA]
        final Object o = target.get(i);
        for (final Object obj : c) // [C]
          if (obj == o)
            continue OUT;

        modified |= target.remove(i) != null;
      }

      return modified;
    }

    @Override
    public IdentitySubList<L> subList(final int fromIndex, final int toIndex) {
      assertRange("fromIndex", fromIndex, "toIndex", toIndex, "size()", size());
      return new IdentitySubList<>((L)target.subList(fromIndex, toIndex));
    }
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public List<E> subList(final int fromIndex, final int toIndex) {
    assertRange("fromIndex", fromIndex, "toIndex", toIndex, "size()", size());
    return new IdentitySubList(super.subList(fromIndex, toIndex));
  }

  @Override
  @SuppressWarnings("unchecked")
  public IdentityArrayList<E> clone() {
    return (IdentityArrayList<E>)super.clone();
  }
}