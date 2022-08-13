/* Copyright (c) 2019 LibJ
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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * An {@link Iterator} for N-dimensional collections of type {@code <T>} that iterates through the leaf members of the collections
 * of type {@code <E>}.
 *
 * @param <E> The type of leaf member elements.
 * @param <T> The type of the collection.
 */
public abstract class FlatIterator<E,T> implements Iterator<E> {
  protected final ArrayList<T> stack = new ArrayList<>();
  protected volatile boolean hasNext;

  /**
   * Specifies whether the provided object is an iterable collection.
   *
   * @param obj The object to test.
   * @return Whether the provided object is a iterable collection.
   */
  protected abstract boolean isIterable(Object obj);
}