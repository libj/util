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
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.libj.lang.Assertions;
import org.libj.lang.Throwables;
import org.libj.util.function.ThrowingRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A policy that defines the conditions and timing of when retries should be
 * performed.
 * <p>
 * The {@code #run} methods are the entrypoints for a {@link Retryable} object
 * to be executed.
 *
 * @param <E> The type parameter of the {@link Exception} instance signifying
 *          terminal failure of the {@link RetryPolicy} execution.
 */
public class RetryPolicy<E extends Exception> implements Serializable {
  private static final long serialVersionUID = -9105939315622837002L;
  private static final Logger logger = LoggerFactory.getLogger(RetryPolicy.class);

  public static class Builder {
    private final int startDelayMs;

    /**
     * @param startDelayMs A positive value representing the delay for the first
     *          retry, in milliseconds, which is also used as the multiplicative
     *          factor for subsequent backed-off delays.
     * @throws IllegalArgumentException If {@code startDelayMs} is negative.
     */
    public Builder(final int startDelayMs) {
      this.startDelayMs = Assertions.assertNotNegative(startDelayMs, "startDelayMs (%d) must be a non-negative value", startDelayMs);
    }

    private int maxRetries = Integer.MAX_VALUE;

    /**
     * @param maxRetries A positive value representing the number of retry
     *          attempts allowed by the {@link RetryPolicy}.
     * @return {@code this} builder.
     * @throws IllegalArgumentException If {@code maxRetries} is negative.
     */
    public Builder withMaxRetries(final int maxRetries) {
      this.maxRetries = Assertions.assertNotNegative(maxRetries, "maxRetries (%d) must be a positive value", maxRetries);
      return this;
    }

    private double jitter = 0;

    /**
     * @param jitter The maximum value of a random factor multiplier to be
     *          applied to {@link #getDelayMs(int)} to be added to the delay for
     *          each retry.
     * @return {@code this} builder.
     * @throws IllegalArgumentException If {@code maxDelayMs} is negative.
     */
    public Builder withJitter(final double jitter) {
      this.jitter = Assertions.assertNotNegative(jitter, "jitter (%f) must be a positive value", jitter);
      return this;
    }

    private boolean delayOnFirstRetry = false;

    /**
     * @param delayOnFirstRetry {@code true} for the first retry to be attempted
     *          after {@code startDelayMs}, otherwise {@code false} for the
     *          first retry to be attempted immediately.
     * @return {@code this} builder.
     */
    public Builder withDelayOnFirstRetry(final boolean delayOnFirstRetry) {
      this.delayOnFirstRetry = delayOnFirstRetry;
      return this;
    }

    private double backoffFactor = 1;

    /**
     * @param backoffFactor The base of the backoff exponential function, i.e. a
     *          value of {@code 2} represents a backoff function of {@code 2^a},
     *          where {@code a} is the attempt number.
     * @return {@code this} builder.
     * @throws IllegalArgumentException If {@code backoffFactor} is less than
     *           {@code 1}.
     */
    public Builder withBackoffFactor(final double backoffFactor) {
      this.backoffFactor = backoffFactor;
      if (backoffFactor < 1.0)
        throw new IllegalArgumentException("backoffFactor (" + backoffFactor + ") must be >= 1.0");

      return this;
    }

    private long maxDelayMs = Long.MAX_VALUE;

    /**
     * @param maxDelayMs The maximum delay, in milliseconds, which takes effect
     *          if the delay computed by the backoff function is a greater
     *          value.
     * @return {@code this} builder.
     * @throws IllegalArgumentException If {@code maxDelayMs} is negative.
     */
    public Builder withMaxDelayMs(final long maxDelayMs) {
      this.maxDelayMs = Assertions.assertNotNegative(maxDelayMs, "maxDelayMs (%d) must be a non-negative value", maxDelayMs);
      return this;
    }

    /**
     * Returns a new {@link RetryPolicy} with the parameters in this
     * {@link Builder}.
     *
     * @param <E> The type parameter of the {@link Exception} instance
     *          signifying terminal failure of the {@link RetryPolicy}
     *          execution.
     * @param retryOn The {@link RetryOn} specifying the conditions under which
     *          a retry should occur given the provided non-null
     *          {@link Exception}, returning {@code true} if a retry should
     *          occur, and {@code false} otherwise.
     * @param onRetryFailure The {@link OnRetryFailure} specifying the
     *          {@link Exception} instance of type {@code <E>} to be thrown in
     *          the event of terminal failure of the {@link RetryPolicy}
     *          execution.
     * @return A new {@link RetryPolicy} with the parameters in this
     *         {@link Builder}.
     * @throws IllegalArgumentException If {@code retryOn} or
     *           {@code onRetryFailure} is null.
     */
    public <E extends Exception>RetryPolicy<E> build(final RetryOn retryOn, final OnRetryFailure<E> onRetryFailure) {
      return new RetryPolicy<>(retryOn, onRetryFailure, maxRetries, startDelayMs, jitter, delayOnFirstRetry, backoffFactor, maxDelayMs);
    }
  }

  private final int maxRetries;
  private final long maxDelayMs;
  private final long startDelayMs;
  private final double jitter;
  private final double backoffFactor;
  private final boolean delayOnFirstRetry;
  private final RetryOn retryOn;
  private final OnRetryFailure<E> onRetryFailure;

  /**
   * Creates a new {@link RetryPolicy} with the specified {@code maxRetries}
   * value.
   *
   * @param retryOn The {@link RetryOn} specifying the conditions under which a
   *          retry should occur given the provided non-null {@link Exception},
   *          returning {@code true} if a retry should occur, and {@code false}
   *          otherwise.
   * @param onRetryFailure The {@link OnRetryFailure} specifying the
   *          {@link Exception} instance of type {@code <E>} to be thrown in the
   *          event of terminal failure of the {@link RetryPolicy} execution.
   * @param maxRetries A positive value representing the number of retry
   *          attempts allowed by the {@link RetryPolicy}.
   * @param startDelayMs A positive value representing the delay for the first
   *          retry, in milliseconds, which is also used as the multiplicative
   *          factor for subsequent backed-off delays.
   * @param jitter The maximum value of a random factor multiplier to be applied
   *          to {@link #getDelayMs(int)} to be added to the delay for each
   *          retry.
   * @param delayOnFirstRetry {@code true} for the first retry to be attempted
   *          after {@code startDelayMs}, otherwise {@code false} for the first
   *          retry to be attempted immediately.
   * @param backoffFactor The base of the backoff exponential function, i.e. a
   *          value of {@code 2} represents a backoff function of {@code 2^a},
   *          where {@code a} is the attempt number.
   * @param maxDelayMs The maximum delay, in milliseconds, which takes effect if
   *          the delay computed by the backoff function is a greater value.
   * @throws IllegalArgumentException If {@code retryOn} or
   *           {@code onRetryFailure} is null, or if {@code maxRetries},
   *           {@code startDelayMs}, {@code jitter} or {@code maxDelayMs} is
   *           negative, or if {@code backoffFactor} is less than {@code 1}.
   */
  public RetryPolicy(final RetryOn retryOn, final OnRetryFailure<E> onRetryFailure, final int maxRetries, final long startDelayMs, final double jitter, final boolean delayOnFirstRetry, final double backoffFactor, final long maxDelayMs) {
    this.retryOn = Assertions.assertNotNull(retryOn);
    this.onRetryFailure = Assertions.assertNotNull(onRetryFailure);
    this.maxRetries = Assertions.assertNotNegative(maxRetries, "maxRetries (%d) must be a positive value", maxRetries);
    this.startDelayMs = Assertions.assertNotNegative(startDelayMs, "startDelayMs (%d) must be a non-negative value", startDelayMs);
    this.jitter = Assertions.assertNotNegative(jitter, "jitter (%f) must be a positive value", jitter);
    this.delayOnFirstRetry = delayOnFirstRetry;
    this.backoffFactor = backoffFactor;
    if (backoffFactor < 1.0)
      throw new IllegalArgumentException("backoffFactor (" + backoffFactor + ") must be >= 1.0");

    this.maxDelayMs = Assertions.assertNotNegative(maxDelayMs, "maxDelayMs (%d) must be a non-negative value", maxDelayMs);
  }

  /**
   * Creates a new {@link RetryPolicy} with the specified {@code maxRetries}
   * value.
   *
   * @param retryOn The {@link RetryOn} specifying the conditions under which a
   *          retry should occur given the provided non-null {@link Exception},
   *          returning {@code true} if a retry should occur, and {@code false}
   *          otherwise.
   * @param onRetryFailure The {@link OnRetryFailure} specifying the
   *          {@link Exception} instance of type {@code <E>} to be thrown in the
   *          event of terminal failure of the {@link RetryPolicy} execution.
   * @param maxRetries A positive value representing the number of retry
   *          attempts allowed by the {@link RetryPolicy}.
   * @param startDelayMs A positive value representing the delay for the first
   *          retry, in milliseconds, which is also used as the multiplicative
   *          factor for subsequent backed-off delays.
   * @param jitter The maximum value of a random factor multiplier to be applied
   *          to {@link #getDelayMs(int)} to be added to the delay for each
   *          retry.
   * @param delayOnFirstRetry {@code true} for the first retry to be attempted
   *          after {@code startDelayMs}, otherwise {@code false} for the first
   *          retry to be attempted immediately.
   * @param backoffFactor The base of the backoff exponential function, i.e. a
   *          value of {@code 2} represents a backoff function of {@code 2^a},
   *          where {@code a} is the attempt number.
   * @throws IllegalArgumentException If {@code retryOn} or
   *           {@code onRetryFailure} is null, or if {@code maxRetries},
   *           {@code startDelayMs} or {@code jitter} is negative, or if
   *           {@code backoffFactor} is less than {@code 1}.
   */
  public RetryPolicy(final RetryOn retryOn, final OnRetryFailure<E> onRetryFailure, final int maxRetries, final long startDelayMs, final double jitter, final boolean delayOnFirstRetry, final double backoffFactor) {
    this(retryOn, onRetryFailure, maxRetries, startDelayMs, jitter, delayOnFirstRetry, backoffFactor, Long.MAX_VALUE);
  }

  /**
   * Creates a new {@link RetryPolicy} with the specified {@code maxRetries}
   * value.
   *
   * @param retryOn The {@link RetryOn} specifying the conditions under which a
   *          retry should occur given the provided non-null {@link Exception},
   *          returning {@code true} if a retry should occur, and {@code false}
   *          otherwise.
   * @param onRetryFailure The {@link OnRetryFailure} specifying the
   *          {@link Exception} instance of type {@code <E>} to be thrown in the
   *          event of terminal failure of the {@link RetryPolicy} execution.
   * @param maxRetries A positive value representing the number of retry
   *          attempts allowed by the {@link RetryPolicy}.
   * @param startDelayMs A positive value representing the delay for the first
   *          retry, in milliseconds, which is also used as the multiplicative
   *          factor for subsequent backed-off delays.
   * @param delayOnFirstRetry {@code true} for the first retry to be attempted
   *          after {@code startDelayMs}, otherwise {@code false} for the first
   *          retry to be attempted immediately.
   * @param jitter The maximum value of a random factor multiplier to be applied
   *          to {@link #getDelayMs(int)} to be added to the delay for each
   *          retry.
   * @throws IllegalArgumentException If {@code retryOn} or
   *           {@code onRetryFailure} is null, or if {@code maxRetries},
   *           {@code startDelayMs} or {@code jitter} is negative.
   */
  public RetryPolicy(final RetryOn retryOn, final OnRetryFailure<E> onRetryFailure, final int maxRetries, final long startDelayMs, final double jitter, final boolean delayOnFirstRetry) {
    this(retryOn, onRetryFailure, maxRetries, startDelayMs, jitter, delayOnFirstRetry, 1, Long.MAX_VALUE);
  }

  /**
   * Creates a new {@link RetryPolicy} with the specified {@code maxRetries}
   * value.
   *
   * @param retryOn The {@link RetryOn} specifying the conditions under which a
   *          retry should occur given the provided non-null {@link Exception},
   *          returning {@code true} if a retry should occur, and {@code false}
   *          otherwise.
   * @param onRetryFailure The {@link OnRetryFailure} specifying the
   *          {@link Exception} instance of type {@code <E>} to be thrown in the
   *          event of terminal failure of the {@link RetryPolicy} execution.
   * @param maxRetries A positive value representing the number of retry
   *          attempts allowed by the {@link RetryPolicy}.
   * @param startDelayMs A positive value representing the delay for the first
   *          retry, in milliseconds, which is also used as the multiplicative
   *          factor for subsequent backed-off delays.
   * @param jitter The maximum value of a random factor multiplier to be applied
   *          to {@link #getDelayMs(int)} to be added to the delay for each
   *          retry.
   * @throws IllegalArgumentException If {@code retryOn} or
   *           {@code onRetryFailure} is null, or if {@code maxRetries},
   *           {@code startDelayMs} or {@code jitter} is negative.
   */
  public RetryPolicy(final RetryOn retryOn, final OnRetryFailure<E> onRetryFailure, final int maxRetries, final long startDelayMs, final double jitter) {
    this(retryOn, onRetryFailure, maxRetries, startDelayMs, jitter, false, 1, Long.MAX_VALUE);
  }

  /**
   * Creates a new {@link RetryPolicy} with the specified {@code maxRetries}
   * value.
   *
   * @param retryOn The {@link RetryOn} specifying the conditions under which a
   *          retry should occur given the provided non-null {@link Exception},
   *          returning {@code true} if a retry should occur, and {@code false}
   *          otherwise.
   * @param onRetryFailure The {@link OnRetryFailure} specifying the
   *          {@link Exception} instance of type {@code <E>} to be thrown in the
   *          event of terminal failure of the {@link RetryPolicy} execution.
   * @param maxRetries A positive value representing the number of retry
   *          attempts allowed by the {@link RetryPolicy}.
   * @param startDelayMs A positive value representing the delay for the first
   *          retry, in milliseconds, which is also used as the multiplicative
   *          factor for subsequent backed-off delays.
   * @throws IllegalArgumentException If {@code retryOn} or
   *           {@code onRetryFailure} is null, or if {@code onRetryFailure},
   *           {@code maxRetries} or {@code startDelayMs} is negative.
   */
  public RetryPolicy(final RetryOn retryOn, final OnRetryFailure<E> onRetryFailure, final int maxRetries, final long startDelayMs) {
    this(retryOn, onRetryFailure, maxRetries, startDelayMs, 0, false, 1, Long.MAX_VALUE);
  }

  private void retryFailed(final List<Exception> exceptions, final int attemptNo, final long delayMs) throws E, RetryFailureRuntimeException {
    final E e = onRetryFailure.onRetryFailure(exceptions, attemptNo, delayMs);
    if (e != null)
      throw e;

    final int size = exceptions.size();
    if (size == 0)
      throw new RetryFailureRuntimeException(attemptNo, delayMs);

    final RetryFailureRuntimeException re = new RetryFailureRuntimeException(exceptions.get(size - 1), attemptNo, delayMs);
    throw Throwables.addSuppressed(re, exceptions, size - 2, -1);
  }

  /**
   * The entrypoint for a {@link Callable} object to be executed. Exceptions in
   * {@link Callable#call()} will be considered for retry if the number of
   * {@link #maxRetries} has not been met and {@link #retryOn} returns
   * {@code true}.
   *
   * @param <T> The type of the result object.
   * @param callable The {@link Callable} object to run.
   * @return The resulting value from {@link Callable#call()}.
   * @throws IllegalArgumentException If {@code callable} is null.
   * @throws E Exception produced by {@link #onRetryFailure} signifying terminal
   *           failure, or if the retry attempts have met {@link #maxRetries},
   *           or {@link #retryOn} returns {@code false}.
   * @throws RetryFailureRuntimeException If {@link #onRetryFailure} returns
   *           {@code null} when invoked in the event of a terminal failure, or
   *           if the retry attempts have met {@link #maxRetries}, or
   *           {@link #retryOn} returns {@code false}.
   */
  public final <T>T run(final Callable<T> callable) throws E, RetryFailureRuntimeException {
    Assertions.assertNotNull(retryOn);
    Assertions.assertNotNull(callable);
    return run0((r, a) -> callable.call(), 0);
  }

  /**
   * The entrypoint for a {@link ThrowingRunnable} object to be executed.
   * Exceptions in {@link ThrowingRunnable#run()} will be considered for retry
   * if the number of {@link #maxRetries} has not been met and {@link #retryOn}
   * returns {@code true}.
   *
   * @param <T> The type of the result object.
   * @param runnable The {@link ThrowingRunnable} object to run.
   * @return The resulting value from {@link Runnable#run()}.
   * @throws IllegalArgumentException If {@code runnable} is null.
   * @throws E Exception produced by {@link #onRetryFailure} signifying terminal
   *           failure, or if the retry attempts have met {@link #maxRetries},
   *           or {@link #retryOn} returns {@code false}.
   * @throws RetryFailureRuntimeException If {@link #onRetryFailure} returns
   *           {@code null} when invoked in the event of a terminal failure, or
   *           if the retry attempts have met {@link #maxRetries}, or
   *           {@link #retryOn} returns {@code false}.
   */
  public final <T>T run(final ThrowingRunnable<?> runnable) throws E, RetryFailureRuntimeException {
    Assertions.assertNotNull(retryOn);
    Assertions.assertNotNull(runnable);
    return run0((r, a) -> {
      runnable.run();
      return null;
    }, 0);
  }

  /**
   * The entrypoint for a {@link Retryable} object to be executed. Exceptions in
   * {@link Retryable#retry(RetryPolicy,int)} will be considered for retry if
   * the number of {@link #maxRetries} has not been met and {@link #retryOn}
   * returns {@code true}.
   *
   * @param <T> The type of the result object.
   * @param retryable The {@link Retryable} object to run.
   * @return The resulting value from {@link Retryable#retry(RetryPolicy,int)}.
   * @throws IllegalArgumentException If {@code retryable} is null.
   * @throws E Exception produced by {@link #onRetryFailure} signifying terminal
   *           failure, or if the retry attempts have met {@link #maxRetries},
   *           or {@link #retryOn} returns {@code false}.
   * @throws RetryFailureRuntimeException If {@link #onRetryFailure} returns
   *           {@code null} when invoked in the event of a terminal failure, or
   *           if the retry attempts have met {@link #maxRetries}, or
   *           {@link #retryOn} returns {@code false}.
   */
  public final <T>T run(final Retryable<T,E> retryable) throws E, RetryFailureRuntimeException {
    return run0(Assertions.assertNotNull(retryable), 0);
  }

  /**
   * The entrypoint for a {@link Retryable} object to be executed. Exceptions in
   * {@link Retryable#retry(RetryPolicy,int)} will be considered for retry if
   * the number of {@link #maxRetries} has not been met and {@link #retryOn}
   * returns {@code true}.
   *
   * @param <T> The type of the result object.
   * @param retryable The {@link Retryable} object to run.
   * @param timeout The maximum time after which this {@link RetryPolicy} is to
   *          invoke {@link #onRetryFailure}.
   * @param unit The time unit of the {@code timeout} argument.
   * @return The resulting value from {@link Retryable#retry(RetryPolicy,int)}.
   * @throws IllegalArgumentException If {@code retryable} or {@code unit} is
   *           null, or if {@code timeout} is negative.
   * @throws E Exception produced by {@link #onRetryFailure} signifying terminal
   *           failure, or if the retry attempts have met {@link #maxRetries},
   *           or {@link #retryOn} returns {@code false}.
   * @throws RetryFailureRuntimeException If {@link #onRetryFailure} returns
   *           {@code null} when invoked in the event of a terminal failure, or
   *           if the retry attempts have met {@link #maxRetries}, or
   *           {@link #retryOn} returns {@code false}.
   */
  public final <T>T run(final Retryable<T,E> retryable, final long timeout, final TimeUnit unit) throws E, RetryFailureRuntimeException {
    Assertions.assertNotNull(retryOn);
    Assertions.assertNotNull(retryable);
    Assertions.assertPositive(timeout, "timeout value (%d) must be a positive value", timeout);
    Assertions.assertNotNull(unit);
    return run0(retryable, TimeUnit.MILLISECONDS.convert(timeout, unit));
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

  private final <T>T run0(final Retryable<T,E> retryable, final long timeout) throws E, RetryFailureRuntimeException {
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

        if (attemptNo > maxRetries || !retryOn.retryOn(e))
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

  private boolean isMaxDelay;

  /**
   * Returns the delay in milliseconds for the specified attempt number. This
   * method is intended to be implemented by a subclass to define the backoff
   * function for retry attempts.
   *
   * @param attemptNo The attempt number, starting with {@code 1}.
   * @return The delay in milliseconds for the specified attempt number.
   */
  protected long getDelayMs(final int attemptNo) {
    if (attemptNo == 1 && !delayOnFirstRetry)
      return 0;

    if (isMaxDelay)
      return maxDelayMs;

    final double delay = startDelayMs * StrictMath.pow(backoffFactor, attemptNo - 1);
    if (isMaxDelay = maxDelayMs < delay)
      return maxDelayMs;

    return (long)delay;
  }
}