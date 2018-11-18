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

/**
 * This class provides a skeletal implementation of an array-backed list of the
 * {@link PrimitiveCollection} interface, specifically designed to abstract the
 * state management instructions for the instances of a graph of sub-lists.
 *
 * @param <T> The parameter representing the array type (i.e. {@code int[]} or
 *          {@code long[]}).
 */
public class AbstractArrayList<T> implements PrimitiveCollection {
  static final int DEFAULT_INITIAL_CAPACITY = 5;

  protected volatile int fromIndex;
  protected volatile int toIndex = -1;

  protected AbstractArrayList<T> parent;
  protected AbstractArrayList<T> sibling;
  protected AbstractArrayList<T> child;

  protected T valueData;
  protected volatile int size;
  protected transient volatile int modCount;

  /**
   * Creates an empty list with an initial capacity of five.
   */
  protected AbstractArrayList() {
  }

  /**
   * Creates a sub-list, and integrates it into the specified parent list's
   * sub-list graph. A sub-list instance shares the parent list's
   * {@link #valueData}, and modifications made to any list in the graph of
   * sub-lists are propagated with the {@link #updateState(int,int)} method.
   *
   * @param parent The parent list.
   * @param fromIndex Low endpoint (inclusive) of the subList.
   * @param toIndex High endpoint (exclusive) of the subList.
   * @throws NullPointerException If the specified parent list is null.
   */
  protected AbstractArrayList(final AbstractArrayList<T> parent, final int fromIndex, final int toIndex) {
    this.parent = parent;
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
   * Update array store and index variables reflecting a modification at the
   * specified index {@code i} and delta {@code d}, and propagate the
   * modification to all child and their sibling sub-lists in this instance's
   * sub-list graph.
   *
   * @param i The index at which a modification was made.
   * @param d The delta of the modification at the index.
   */
  private void updateStateChildren(final int i, final int d) {
    for (AbstractArrayList<T> child = this.child; child != null; child = child.child) {
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
   * Update array store and index variables reflecting a modification at the
   * specified index {@code i} and delta {@code d}, and propagate the
   * modification to all sibling and their child sub-lists in this instance's
   * sub-list graph.
   *
   * @param i The index at which a modification was made.
   * @param d The delta of the modification at the index.
   */
  private void updateStateSiblings(final int i, final int d) {
    for (AbstractArrayList<T> sibling = this.sibling; sibling != null && sibling != this; sibling = sibling.sibling) {
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
   * Update array store and index variables reflecting a modification at the
   * specified index {@code i} and delta {@code d}, and propagate the
   * modification to all parent and sibling sub-lists in this instance's
   * sub-list graph.
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
   * Update array store and index variables reflecting a modification at the
   * specified index {@code i} and delta {@code d}, and propagate the
   * modification to all parent, child, and sibling sub-lists in this instance's
   * sub-list graph.
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
   * Returns the number of values in this list. If this list contains more than
   * {@code Integer.MAX_VALUE} values, returns {@code Integer.MAX_VALUE}.
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
   * @return {@code true} if this list contains no values
   */
  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Removes all of the elements from this list. The list will be empty after
   * this call returns.
   */
  @Override
  public void clear() {
    updateState(fromIndex, toIndex > -1 ? fromIndex - toIndex : -size);
  }
}