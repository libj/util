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

import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

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
 * @see RandomAccess
 */
public abstract class ObservableRandomAccessList<E,L extends List<E> & RandomAccess> extends ObservableList<E,L> implements RandomAccess {
  /**
   * Creates a new {@link ObservableRandomAccessList} with the specified target {@link List}.
   *
   * @param list The target {@link List}.
   * @throws IllegalArgumentException If {@code list} is null.
   */
  public ObservableRandomAccessList(final L list) {
    super(list);
  }

  /**
   * Creates a new {@link ObservableRandomAccessList} with the specified target list, and from and to indexes to limit the scope of
   * the target list.
   *
   * @param list The target {@link List} object.
   * @param fromIndex The starting index as the lower limit of the elements in the target list, inclusive.
   * @param toIndex The starting index as the upper limit of the elements in the target list, exclusive.
   * @throws IllegalArgumentException If {@code list} is null.
   */
  protected ObservableRandomAccessList(final L list, final int fromIndex, final int toIndex) {
    super(list, fromIndex, toIndex);
  }
}