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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.libj.lang.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A policy that defines the conditions and timing of when retries should be
 * performed.
 * <p>
 * The {@link #retryOn(Exception)} method specifies the exception conditions of
 * when a retry should occur.
 * <p>
 * The {@link #run(Retryable)} method is the entrypoint for a {@link Retryable}
 * object to be executed.
 *
 * @param <E> The type parameter of the {@link Exception} instance signifying
 *          terminal failure of the {@link RetryPolicy} execution.
 */
public abstract class RetryPolicy<E extends Exception> implements Serializable {
  private static final long serialVersionUID = -8480057566592276543L;
  private static final Logger logger = LoggerFactory.getLogger(RetryPolicy.class);

  private final int maxRetries;
  private final double jitter;

  /**
   * Creates a new {@link RetryPolicy} with the specified {@code maxRetries}
   * value.
   *
   * @param maxRetries A positive value representing the number of retry
   *          attempts allowed by the {@link RetryPolicy}.
   * @param jitter The maximum value of a random factor multiplier to be applied
   *          to {@link #getDelayMs(int)} to be added to the delay for each
   *          retry.
   * @throws IllegalArgumentException If {@code maxRetries} or {@code jitter} is
   *           negative.
   */
  public RetryPolicy(final int maxRetries, final double jitter) {
    if (maxRetries < 0)
      throw new IllegalArgumentException("maxRetries (" + maxRetries + ") must be a positive value");

    if (jitter < 0)
      throw new IllegalArgumentException("jitter (" + jitter + ") must be a positive value");

    this.maxRetries = maxRetries;
    this.jitter = jitter;
    if (maxRetries <= 0)
      throw new IllegalArgumentException("maxRetries (" + maxRetries + ") must be a positive value");
  }

  private void retryFailed(final List<Exception> exceptions, final int attemptNo, final long delayMs) throws E, RetryFailureException {
    final E e = onRetryFailure(exceptions, attemptNo, delayMs);
    if (e != null)
      throw e;

    final int size = exceptions.size();
    if (size == 0)
      throw new RetryFailureException(attemptNo, delayMs);

    final Exception cause = exceptions.get(size - 1);
    final RetryFailureException re = new RetryFailureException(cause, attemptNo, delayMs);
    for (int i = size - 2; i >= 0; --i)
      re.addSuppressed(exceptions.get(i));

    exceptions.clear();
    throw re;
  }

  /**
   * Specifies the exception conditions of when a retry should occur.
   *
   * @param e The exception that occurred during execution of a
   *          {@link Retryable} object.
   * @return {@code true} if a retry should occur, otherwise {@code false}.
   */
  protected abstract boolean retryOn(final Exception e);

  /**
   * Callback to return the {@link Exception} instance of type {@code <E>} to be
   * thrown in the event of terminal failure of the {@link RetryPolicy}
   * execution.
   * <p>
   * If this method is not overridden, or if it returns {@code null}, a
   * {@link RetryFailureException} will be thrown instead.
   *
   * @param exceptions The exceptions that occurred during execution of a
   *          {@link Retryable} object.
   * @param attemptNo The attempt number on which the exception was thrown.
   * @param delayMs The delay (in milliseconds) from the previous invocation
   *          attempt.
   * @return The {@link Exception} instance of type {@code <E>} signifying the
   *         terminal failure of the {@link RetryPolicy} execution.
   */
  protected E onRetryFailure(final List<Exception> exceptions, final int attemptNo, final long delayMs) {
    return null;
  }

  /**
   * The entrypoint for a {@link Retryable} object to be executed. Exceptions in
   * {@link Retryable#retry(RetryPolicy,int)} will be considered for retry if
   * the number of {@link #maxRetries} has not been met and
   * {@link #retryOn(Exception)} returns {@code true}.
   *
   * @param <T> The type of the result object.
   * @param retryable The {@link Retryable} object to run.
   * @return The resulting value from {@link Retryable#retry(RetryPolicy,int)}.
   * @throws RetryFailureException If retry attempts have met
   *           {@link #maxRetries}, or if {@link #retryOn(Exception)} returns
   *           {@code false}.
   * @throws IllegalArgumentException If {@code retryable} is null.
   * @throws E Generic exception signifying terminal failure.
   */
  public final <T>T run(final Retryable<T,E> retryable) throws E, RetryFailureException {
    return run0(Assertions.assertNotNull(retryable), 0);
  }

  /**
   * The entrypoint for a {@link Retryable} object to be executed. Exceptions in
   * {@link Retryable#retry(RetryPolicy,int)} will be considered for retry if
   * the number of {@link #maxRetries} has not been met and
   * {@link #retryOn(Exception)} returns {@code true}.
   *
   * @param <T> The type of the result object.
   * @param retryable The {@link Retryable} object to run.
   * @param timeout The maximum time to retry in milliseconds.
   * @return The resulting value from {@link Retryable#retry(RetryPolicy,int)}.
   * @throws RetryFailureException If retry attempts have met
   *           {@link #maxRetries}, if {@link #retryOn(Exception)} returns
   *           {@code false}, or if {@code timeout} is exceeded.
   * @throws IllegalArgumentException If {@code retryable} is null, or if
   *           {@code timeout} is negative.
   * @throws E Generic exception signifying terminal failure.
   */
  public final <T>T run(final Retryable<T,E> retryable, final long timeout) throws E, RetryFailureException {
    if (timeout < 0)
      throw new IllegalArgumentException("timeout value (" + timeout + ") must be a positive value");

    return run0(Assertions.assertNotNull(retryable), timeout);
  }

  /**
   * Method that is called before each execution of
   * {@link Retryable#retry(RetryPolicy,int)}.
   * <p>
   * Runtime exceptions thrown from this method will result in the termination
   * of the {@link RetryPolicy} execution.
   *
   * @param attemptNo The next attempt number.
   */
  protected void onRetry(final int attemptNo) {
  }

  private final <T>T run0(final Retryable<T,E> retryable, final long timeout) throws E, RetryFailureException {
    final List<Exception> exceptions = new ArrayList<>();
    final long startTime = System.currentTimeMillis();
    long runTime = 0;
    Exception previousException = null;
    for (int attemptNo = 1;; ++attemptNo) {
      onRetry(attemptNo);
      try {
        return retryable.retry(this, attemptNo);
      }
      catch (final Exception e) {
        if (previousException == null || e.getClass() != previousException.getClass() || !Objects.equals(e.getMessage(), previousException.getMessage()))
          exceptions.add(previousException = e);

        if (attemptNo > maxRetries || !retryOn(e))
          retryFailed(exceptions, attemptNo, getDelayMs(attemptNo - 1));

        long delayMs = getDelayMs(attemptNo);
        if (jitter > 0)
          delayMs *= jitter * Math.random() + 1;

        if (timeout > 0) {
          final long remaining = timeout - runTime;
          if (remaining <= 0)
            retryFailed(exceptions, attemptNo, delayMs);

          if (remaining < delayMs)
            delayMs = remaining;
        }

        try {
          Thread.sleep(delayMs);
          runTime = System.currentTimeMillis() - startTime;
        }
        catch (final InterruptedException ie) {
          exceptions.add(ie);
          retryFailed(exceptions, attemptNo, delayMs);
        }
      }

      if (logger.isDebugEnabled())
        logger.debug("Retrying attemptNo = " + attemptNo + ", runTime = " + runTime);
    }
  }

  /**
   * Returns the number of retry attempts allowed by this {@link RetryPolicy}.
   *
   * @return The number of retry attempts allowed by this {@link RetryPolicy}.
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
  protected abstract long getDelayMs(int attemptNo);
}