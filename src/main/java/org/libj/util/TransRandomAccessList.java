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

import java.util.List;
import java.util.RandomAccess;
import java.util.function.BiFunction;

/**
 * An implementation of the List interface that transforms the elements of the supplied source List based on {@code sourceToTarget}
 * and {@code targetToSource} lambda functions.
 *
 * @param <S> The type of the elements in the source List.
 * @param <LS> The type of source list.
 * @param <T> The type of the elements in the target List.
 * @param <LT> The type of target list.
 * @see List
 * @see RandomAccess
 */
public class TransRandomAccessList<S,LS extends List<S> & RandomAccess,T,LT extends List<T> & RandomAccess> extends TransList<S,LS,T,LT> implements RandomAccess {
  /**
   * Creates a new {@link TransRandomAccessList} with the specified source List, and functions defining the translation of objects
   * types {@code S -> T} and {@code T -> S}.
   * <p>
   * If {@code sourceToTarget} is null, all methods that require a translation of {@code S -> T} will throw a
   * {@link UnsupportedOperationException}.
   * <p>
   * If {@code targetToSource} is null, all methods that require a translation of {@code T -> S} will throw a
   * {@link UnsupportedOperationException}.
   *
   * @param source The source List of type {@code <S>}.
   * @param sourceToTarget The {@link BiFunction} defining the translation from {@code S -> T}.
   * @param targetToSource The {@link BiFunction} defining the translation from {@code T -> S}.
   * @throws IllegalArgumentException If {@code source} is null.
   */
  public TransRandomAccessList(final LS source, final BiFunction<Integer,S,T> sourceToTarget, final BiFunction<Integer,T,S> targetToSource) {
    super(source, sourceToTarget, targetToSource);
  }
}