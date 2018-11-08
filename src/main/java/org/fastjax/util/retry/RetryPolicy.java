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

package org.fastjax.util.retry;

import java.io.Serializable;

/**
 * A policy that defines the conditions and timing of when retries should be
 * performed.
 * <p>
 * The {@link #retryOn(Exception)} method specifies the exception conditions of
 * when a retry should occur.
 * <p>
 * The {@link #run(Retryable)} method is the entrypoint for a {@link Retryable}
 * object to be executed.
 */
public abstract class RetryPolicy implements Serializable {
  private static final long serialVersionUID = -8480057566592276543L;

  private final int maxRetries;

  /**
   * Creates a new {@code RetryPolicy} with the specified {@code maxRetries}
   * value.
   *
   * @param maxRetries A positive value representing the number of retry
   *          attempts allowed by the {@code RetryPolicy}.
   */
  public RetryPolicy(final int maxRetries) {
    this.maxRetries = maxRetries;
    if (maxRetries <= 0)
      throw new IllegalArgumentException("maxRetries (" + maxRetries + ") must be a positive value");
  }

  /**
   * Specifies the exception conditions of when a retry should occur.
   *
   * @param e The exception that occurred during execution of a
   *          {@link Retryable} object.
   * @return {@code true} if a retry should occur, otherwise {@code false}.
   */
  protected boolean retryOn(final Exception e) {
    return e instanceof RetryException;
  }

  /**
   * The entrypoint for a {@link Retryable} object to be executed. Exceptions in
   * {@link Retryable#retry(RetryPolicy,int)} will be considered for retry if
   * the number of {@link #maxRetries} has not been exceeded, and
   * {@link #retryOn(Exception)} returns {@code true}.
   *
   * @param retryable The {@link Retryable} object to run.
   * @return The resulting value from {@link Retryable#retry(RetryPolicy,int)}.
   * @throws RetryFailureException If retry attempts have exceeded
   *           {@link #maxRetries}, or if {@link #retryOn(Exception)} returns
   *           {@code false}.
   * @throws NullPointerException If {@code retryable} is null.
   */
  public final <T>T run(final Retryable<T> retryable) throws RetryFailureException {
    for (int attemptNo = 1;; ++attemptNo) {
      try {
        return retryable.retry(this, attemptNo);
      }
      catch (final Exception e) {
        if (attemptNo > maxRetries || !retryOn(e))
          throw new RetryFailureException(e, attemptNo, getDelayMs(attemptNo));

        final int delayMs = getDelayMs(attemptNo);
        try {
          Thread.sleep(delayMs);
        }
        catch (final InterruptedException ie) {
          throw new RetryFailureException(ie, attemptNo, delayMs);
        }
      }
    }
  }

  /**
   * @return The number of retry attempts allowed by this {@code RetryPolicy}.
   */
  public int getMaxRetries() {
    return this.maxRetries;
  }

  /**
   * Returns the delay in milliseconds for the specified attempt number. This
   * method is intended to be implemented by a subclass to define the backoff
   * function for retry attempts.
   *
   * @param attemptNo The attempt number, starting with {@code 1}.
   * @return The delay in milliseconds for the specified attempt number.
   */
  public abstract int getDelayMs(final int attemptNo);
}