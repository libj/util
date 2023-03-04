/* Copyright (c) 2022 LibJ
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

import java.util.Collections;
import java.util.Set;

/**
 * An extensible alternative to {@link Collections#unmodifiableSet(Set)} that provides an unmodifiable view of an underlying
 * {@link Set}.
 * <p>
 * This class allows modules to provide users with "read-only" access to internal collections. Query operations on the returned
 * collection "read through" to the specified collection, and attempts to modify the returned collection, whether direct or via its
 * iterator, result in an {@link UnsupportedOperationException}.
 * <p>
 * The set is serializable if the underlying set is serializable.
 *
 * @param <E> The type parameter of elements in the set.
 */
public class UnmodifiableSet<E> extends UnmodifiableCollection<E> implements Set<E> {
  /**
   * Creates a new {@link UnmodifiableSet} with the specified target {@link Set}.
   *
   * @param target The target {@link Set}.
   * @throws NullPointerException If the target {@link Set} is null.
   */
  public UnmodifiableSet(final Set<? extends E> target) {
    super(target);
  }

  /**
   * Creates a new {@link UnmodifiableSet} with a null target.
   */
  protected UnmodifiableSet() {
  }

  @Override
  public boolean equals(final Object o) {
    return o == this || getTarget().equals(o);
  }

  @Override
  public int hashCode() {
    return getTarget().hashCode();
  }
}