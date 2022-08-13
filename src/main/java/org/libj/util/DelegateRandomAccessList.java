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

import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

/**
 * A {@link DelegateRandomAccessList} contains some other {@link List} that also implements {@link RandomAccess}, to which it
 * delegates its method calls, possibly transforming the data along the way or providing additional functionality. The class
 * {@link DelegateRandomAccessList} itself simply overrides all methods of {@link AbstractList} with versions that pass all requests
 * to the target {@link List}. Subclasses of {@link DelegateRandomAccessList} may further override some of these methods and may
 * also provide additional methods and fields.
 *
 * @param <E> The type of elements in this list.
 * @param <L> The type of underlying list.
 */
public abstract class DelegateRandomAccessList<E,L extends List<E> & RandomAccess> extends DelegateList<E,List<E>> implements RandomAccess {
  /**
   * Creates a new {@link DelegateRandomAccessList} with the specified target {@link List}.
   *
   * @param target The target {@link List}.
   * @throws IllegalArgumentException If the target {@link List} is null.
   */
  public DelegateRandomAccessList(final L target) {
    super(target);
  }

  /**
   * Creates a new {@link DelegateRandomAccessList} with a null target.
   */
  protected DelegateRandomAccessList() {
    super();
  }
}