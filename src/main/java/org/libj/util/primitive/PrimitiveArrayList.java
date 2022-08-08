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
import static org.libj.lang.Assertions.*;

import java.lang.reflect.Array;

/**
 * This class provides the base implementation of an array-backed list of the {@link PrimitiveCollection} interface, specifically
 * designed to abstract the state management instructions for the instances of sub-lists.
 *
 * @param <T> The parameter representing the array type (i.e. {@code int[]} or {@code long[]}).
 */
public abstract class PrimitiveArrayList<T> implements PrimitiveCollection {
  static final int DEFAULT_INITIAL_CAPACITY = 5;

  protected int fromIndex;
  protected int toIndex = -1;

  protected PrimitiveArrayList<T> parent;
  protected PrimitiveArrayList<T> sibling;
  protected PrimitiveArrayList<T> child;

  protected T valueData;
  protected int size;
  protected transient int modCount;

  /**
   * Creates an empty list with an initial capacity of five.
   */
  protected PrimitiveArrayList() {
  }

  /**
   * Creates a sub-list, and integrates it into the specified parent list's sub-list graph. A sub-list instance shares the parent
   * list's {@link #valueData}, and modifications made to any list in the graph of sub-lists are propagated with the
   * {@link #updateState(int,int)} method.
   *
   * @param parent The parent list.
   * @param fromIndex Low endpoint (inclusive) of the subList.
   * @param toIndex High endpoint (exclusive) of the subList.
   * @throws IllegalArgumentException If {@code parent} is null.
   */
  protected PrimitiveArrayList(final PrimitiveArrayList<T> parent, final int fromIndex, final int toIndex) {
    this.parent = assertNotNull(parent);
    if (parent.child == null) {
      this.sibling = this;
      parent.child = this;
    }
    else {
      this.sibling = parent.child.sibling;
      parent.child.sibling = this;
    }

    this.valueData = parent.valueData;
    this.modCount = parent.modCount;
    this.size = parent.size;

    this.fromIndex = fromIndex;
    this.toIndex = toIndex;
  }

  /**
   * Update array store and index variables reflecting a modification at the specified index {@code i} and delta {@code d}, and
   * propagate the modification to all child and their sibling sub-lists in this instance's sub-list graph.
   *
   * @param i The index at which a modification was made.
   * @param d The delta of the modification at the index.
   */
  private void updateStateChildren(final int i, final int d) {
    for (PrimitiveArrayList<T> child = this.child; child != null; child = child.child) { // [X]
      ++child.modCount;
      child.size = this.size;
      child.valueData = this.valueData;
      child.updateStateSiblings(i, d);
      if (d == 0)
        continue;

      if (i < child.fromIndex)
        child.fromIndex += (child.fromIndex < -d ? -child.fromIndex : d);

      if (i < child.toIndex)
        child.toIndex += (child.toIndex < -d ? -child.toIndex : d);
    }
  }

  /**
   * Update array store and index variables reflecting a modification at the specified index {@code i} and delta {@code d}, and
   * propagate the modification to all sibling and their child sub-lists in this instance's sub-list graph.
   *
   * @param i The index at which a modification was made.
   * @param d The delta of the modification at the index.
   */
  private void updateStateSiblings(final int i, final int d) {
    for (PrimitiveArrayList<T> sibling = this.sibling; sibling != null && sibling != this; sibling = sibling.sibling) { // [X]
      ++sibling.modCount;
      sibling.size = this.size;
      sibling.valueData = this.valueData;
      sibling.updateStateChildren(i, d);
      if (d == 0)
        continue;

      if (i < sibling.fromIndex)
        sibling.fromIndex += (sibling.fromIndex < -d ? -sibling.fromIndex : d);

      if (i < sibling.toIndex)
        sibling.toIndex += (sibling.toIndex < -d ? -sibling.toIndex : d);
    }
  }

  /**
   * Update array store and index variables reflecting a modification at the specified index {@code i} and delta {@code d}, and
   * propagate the modification to all parent and sibling sub-lists in this instance's sub-list graph.
   *
   * @param i The index at which a modification was made.
   * @param d The delta of the modification at the index.
   */
  private void updateStateParent(final int i, final int d) {
    ++modCount;
    if (parent != null) {
      parent.valueData = this.valueData;
      parent.size = this.size;
      parent.updateStateParent(i, d);
    }

    updateStateSiblings(i, d);
    if (d != 0 && i <= toIndex)
      toIndex += d;
  }

  /**
   * Update array store and index variables reflecting a modification at the specified index {@code i} and delta {@code d}, and
   * propagate the modification to all parent, child, and sibling sub-lists in this instance's sub-list graph.
   *
   * @param i The index at which a modification was made.
   * @param d The delta of the modification at the index.
   * @return The specified index.
   */
  protected int updateState(final int i, final int d) {
    size += d;
    updateStateChildren(i, d);
    updateStateParent(i, d);
    return i;
  }

  /**
   * Returns the number of values in this list. If this list contains more than {@link Integer#MAX_VALUE} values, returns
   * {@link Integer#MAX_VALUE}.
   *
   * @return The number of values in this list.
   */
  @Override
  public int size() {
    return (toIndex > -1 ? toIndex : size) - fromIndex;
  }

  /**
   * Returns {@code true} if this list contains no values.
   *
   * @return {@code true} if this list contains no values.
   */
  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Removes all of the elements from this list. The list will be empty after this call returns.
   */
  @Override
  public void clear() {
    updateState(fromIndex, toIndex > -1 ? fromIndex - toIndex : -size);
  }

  /**
   * Creates and returns a copy of this list, containing the same value data in this list. The underlying array for the copy will
   * not the same as this list, meaning that changes made to the copy will not be reflected in this list, and vice-versa. If this
   * list has sub-lists, or is itself a sub-list of another list, neither the parent nor the children or siblings will be cloned.
   *
   * @return A copy of this list.
   */
  @Override
  @SuppressWarnings("unchecked")
  protected PrimitiveArrayList<T> clone() {
    try {
      final PrimitiveArrayList<T> clone = (PrimitiveArrayList<T>)super.clone();
      clone.parent = null;
      clone.sibling = null;
      clone.child = null;
      clone.fromIndex = 0;
      clone.toIndex = -1;
      clone.modCount = 0;

      final int size = clone.size = size();
      clone.valueData = (T)Array.newInstance(valueData.getClass().getComponentType(), size);
      System.arraycopy(valueData, fromIndex, clone.valueData, 0, size);
      return clone;
    }
    catch (final CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }
}