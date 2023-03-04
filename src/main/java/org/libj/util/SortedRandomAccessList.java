/* Copyright (c) 2017 LibJ
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

import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

/**
 * A {@link List} that also implements {@link RandomAccess} that guarantees sorted order of its elements.
 *
 * @param <E> The type of elements in this list.
 * @param <L> The type of underlying list.
 */
public class SortedRandomAccessList<E,L extends List<E> & RandomAccess> extends SortedList<E,L> implements RandomAccess {
  /**
   * Creates a new {@link SortedRandomAccessList} with the provided {@link List list} of comparable elements as the underlying
   * target.
   *
   * @implNote This constructor sorts the provided {@link List list}.
   * @param list The {@link List} of comparable elements.
   * @throws NullPointerException If the provided {@link List list} is null.
   */
  public SortedRandomAccessList(final L list) {
    super(list);
  }

  /**
   * Creates a new {@link SortedRandomAccessList} with the provided {@link List list} and {@link Comparator comparator} as the
   * underlying target.
   *
   * @implNote This constructor sorts the provided {@link List list}.
   * @param list The {@link List}.
   * @param comparator The {@link Comparator}.
   * @throws NullPointerException If the provided {@link List list} or {@link Comparator comparator} is null.
   */
  public SortedRandomAccessList(final L list, final Comparator<E> comparator) {
    super(list, comparator);
  }
}