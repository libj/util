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

package org.libj.util.retry;

/**
 * Interface that provides retry ability for a class when executed via the {@code RetryPolicy.run} functions.
 * <p>
 * This is a functional interface whose functional method is {@link #retry(RetryPolicy,int)}.
 *
 * @param <T> The type of the result of this {@link Retryable}.
 * @param <E> The type parameter of the {@link Exception} instance signifying terminal failure of the {@link RetryPolicy} execution.
 */
@FunctionalInterface
public interface Retryable<T,E extends Exception> {
  /**
   * Main run method of the {@link Retryable} that is invoked by a {@link RetryPolicy}, which defines the rules of retry invocations.
   *
   * @param retryPolicy The invoking {@link RetryPolicy}.
   * @param attemptNo The incremental sequence number of the retry attempt.
   * @return The result of the invocation.
   * @throws Exception If an exception occurs.
   */
  T retry(RetryPolicy<E> retryPolicy, int attemptNo) throws Exception;
}