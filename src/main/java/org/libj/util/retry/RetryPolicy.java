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

import static org.libj.lang.Assertions.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.IntConsumer;

import org.libj.lang.Throwables;
import org.libj.util.function.ThrowingRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A policy that defines the conditions and timing of when retries should be performed.
 * <p>
 * The {@code #run} methods are the entrypoints for a {@link Retryable} object to be executed.
 *
 * @param <E> The type parameter of the {@link Exception} instance signifying terminal failure of the {@link RetryPolicy} execution.
 */
public class RetryPolicy<E extends Exception> implements Serializable {
  private static final Logger logger = LoggerFactory.getLogger(RetryPolicy.class);

  /**
   * A builder for {@link RetryPolicy RetryPolicy&lt;E&gt;} instances.
   *
   * @param <E> The type parameter of the {@link Exception} instance signifying terminal failure of the {@link RetryPolicy}
   *          execution.
   */
  public static class Builder<E extends Exception> implements Cloneable {
    private final OnRetryFailure<E> onRetryFailure;

    /**
     * Creates a new {@link Builder Builder&lt;E&gt;} with the provided {@link OnRetryFailure OnRetryFailure&lt;E&gt;}.
     *
     * @param onRetryFailure The {@link OnRetryFailure} specifying the {@link Exception} instance of type {@code <E>} to be thrown
     *          in the event of terminal failure of the {@link RetryPolicy} execution.
     * @throws NullPointerException If {@code onRetryFailure} is null.
     */
    public Builder(final OnRetryFailure<E> onRetryFailure) {
      this.onRetryFailure = Objects.requireNonNull(onRetryFailure);
    }

    private int startDelayMs = 0;

    /**
     * @param startDelayMs A positive value representing the delay for the first retry, in milliseconds, which is also used as the
     *          multiplicative factor for subsequent backed-off delays.
     * @return {@code this} builder.
     * @throws IllegalArgumentException If {@code startDelayMs} is negative.
     */
    public Builder<E> withStartDelay(final int startDelayMs) {
      this.startDelayMs = assertNotNegative(startDelayMs, () -> "startDelayMs (" + startDelayMs + ") must be a non-negative value");
      return this;
    }

    private int maxRetries = Integer.MAX_VALUE;

    /**
     * @param maxRetries A positive value representing the number of retry attempts allowed by the {@link RetryPolicy}.
     * @return {@code this} builder.
     * @throws IllegalArgumentException If {@code maxRetries} is negative.
     */
    public Builder<E> withMaxRetries(final int maxRetries) {
      this.maxRetries = assertNotNegative(maxRetries, () -> "maxRetries (" + maxRetries + ") must be a positive value");
      return this;
    }

    private double jitter = 0;

    /**
     * @param jitter The maximum value of a random factor multiplier to be applied to {@link #getDelayMs(int)} to be added to the
     *          delay for each retry.
     * @return {@code this} builder.
     * @throws IllegalArgumentException If {@code maxDelayMs} is negative.
     */
    public Builder<E> withJitter(final double jitter) {
      this.jitter = assertNotNegative(jitter, () -> "jitter " + jitter + ") must be a positive value");
      return this;
    }

    private boolean delayOnFirstRetry = false;

    /**
     * @param delayOnFirstRetry {@code true} for the first retry to be attempted after {@code startDelayMs}, otherwise {@code false}
     *          for the first retry to be attempted immediately.
     * @return {@code this} builder.
     */
    public Builder<E> withDelayOnFirstRetry(final boolean delayOnFirstRetry) {
      this.delayOnFirstRetry = delayOnFirstRetry;
      return this;
    }

    private double backoffFactor = 1;

    /**
     * @param backoffFactor The base of the backoff exponential function, i.e. a value of {@code 2} represents a backoff function of
     *          {@code 2^a}, where {@code a} is the attempt number.
     * @return {@code this} builder.
     * @throws IllegalArgumentException If {@code backoffFactor} is less than {@code 1}.
     */
    public Builder<E> withBackoffFactor(final double backoffFactor) {
      this.backoffFactor = backoffFactor;
      if (backoffFactor < 1.0)
        throw new IllegalArgumentException("backoffFactor (" + backoffFactor + ") must be >= 1.0");

      return this;
    }

    private long maxDelayMs = Long.MAX_VALUE;

    /**
     * @param maxDelayMs The maximum delay, in milliseconds, which takes effect if the delay computed by the backoff function is a
     *          greater value.
     * @return {@code this} builder.
     * @throws IllegalArgumentException If {@code maxDelayMs} is negative.
     */
    public Builder<E> withMaxDelayMs(final long maxDelayMs) {
      this.maxDelayMs = assertNotNegative(maxDelayMs, () -> "maxDelayMs (" + maxDelayMs + ") must be a non-negative value");
      return this;
    }

    /**
     * Returns a new {@link RetryPolicy} with the parameters in this {@link Builder}.
     *
     * @param retryOn The {@link RetryOn} specifying the conditions under which a retry should occur given the provided non-null
     *          {@link Exception}, returning {@code true} if a retry should occur, and {@code false} otherwise.
     * @return A new {@link RetryPolicy} with the parameters in this {@link Builder}.
     * @throws NullPointerException If {@code retryOn} is null.
     */
    public RetryPolicy<E> build(final RetryOn retryOn) {
      return build(retryOn, null);
    }

    /**
     * Returns a new {@link RetryPolicy} with the parameters in this {@link Builder}.
     *
     * @param retryOn The {@link RetryOn} specifying the conditions under which a retry should occur given the provided non-null
     *          {@link Exception}, returning {@code true} if a retry should occur, and {@code false} otherwise.
     * @param onRetry {@link IntConsumer} providing the {@code attemptNo} that is called before each execution of
     *          {@link Retryable#retry(RetryPolicy,int)}. Runtime exceptions thrown from this method will result in the termination
     *          of the {@link RetryPolicy}'s execution.
     * @return A new {@link RetryPolicy} with the parameters in this {@link Builder}.
     * @throws NullPointerException If {@code retryOn} is null.
     */
    public RetryPolicy<E> build(final RetryOn retryOn, final IntConsumer onRetry) {
      return new RetryPolicy<>(retryOn, onRetry, onRetryFailure, maxRetries, startDelayMs, jitter, delayOnFirstRetry, backoffFactor, maxDelayMs);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Builder<E> clone() {
      try {
        return (Builder<E>)super.clone();
      }
      catch (final CloneNotSupportedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private static List<Exception> testExceptions = Arrays.asList(new Exception());

  private final int maxRetries;
  private final long maxDelayMs;
  private final long startDelayMs;
  private final double jitter;
  private final double backoffFactor;
  private final boolean delayOnFirstRetry;
  private final RetryOn retryOn;
  private final IntConsumer onRetry;
  private final OnRetryFailure<E> onRetryFailure;

  /**
   * Creates a new {@link RetryPolicy} with the specified {@code maxRetries} value.
   *
   * @param retryOn The {@link RetryOn} specifying the conditions under which a retry should occur given the provided non-null
   *          {@link Exception}, returning {@code true} if a retry should occur, and {@code false} otherwise.
   * @param onRetry {@link IntConsumer} providing the {@code attemptNo} that is called before each execution of
   *          {@link Retryable#retry(RetryPolicy,int)}. Runtime exceptions thrown from this method will result in the termination of
   *          the {@link RetryPolicy}'s execution.
   * @param onRetryFailure The {@link OnRetryFailure} specifying the {@link Exception} instance of type {@code <E>} to be thrown in
   *          the event of terminal failure of the {@link RetryPolicy} execution.
   * @param maxRetries A positive value representing the number of retry attempts allowed by the {@link RetryPolicy}.
   * @param startDelayMs A positive value representing the delay for the first retry, in milliseconds, which is also used as the
   *          multiplicative factor for subsequent backed-off delays.
   * @param jitter The maximum value of a random factor multiplier to be applied to {@link #getDelayMs(int)} to be added to the
   *          delay for each retry.
   * @param delayOnFirstRetry {@code true} for the first retry to be attempted after {@code startDelayMs}, otherwise {@code false}
   *          for the first retry to be attempted immediately.
   * @param backoffFactor The base of the backoff exponential function, i.e. a value of {@code 2} represents a backoff function of
   *          {@code 2^a}, where {@code a} is the attempt number.
   * @param maxDelayMs The maximum delay, in milliseconds, which takes effect if the delay computed by the backoff function is a
   *          greater value.
   * @throws IllegalArgumentException If {@code maxRetries}, {@code startDelayMs}, {@code jitter}, or {@code maxDelayMs} is
   *           negative, or if {@code backoffFactor} is less than {@code 1}.
   * @throws NullPointerException If {@code retryOn} or {@code onRetryFailure} is null, or if {@code onRetryFailure} returns a null
   *           value.
   */
  public RetryPolicy(final RetryOn retryOn, final IntConsumer onRetry, final OnRetryFailure<E> onRetryFailure, final int maxRetries, final long startDelayMs, final double jitter, final boolean delayOnFirstRetry, final double backoffFactor, final long maxDelayMs) {
    this.retryOn = Objects.requireNonNull(retryOn);
    this.onRetry = onRetry;
    this.onRetryFailure = Objects.requireNonNull(onRetryFailure);
    this.maxRetries = assertNotNegative(maxRetries, () -> "maxRetries (" + maxRetries + ") must be a positive value");
    this.startDelayMs = assertNotNegative(startDelayMs, () -> "startDelayMs (" + startDelayMs + ") must be a non-negative value");
    this.jitter = assertNotNegative(jitter, () -> "jitter (" + jitter + ") must be a positive value");
    this.delayOnFirstRetry = delayOnFirstRetry;
    this.backoffFactor = backoffFactor;
    if (backoffFactor < 1.0)
      throw new IllegalArgumentException("backoffFactor (" + backoffFactor + ") must be >= 1.0");

    this.maxDelayMs = assertNotNegative(maxDelayMs, () -> "maxDelayMs (" + maxDelayMs + ") must be a non-negative value");

    Objects.requireNonNull(onRetryFailure.onRetryFailure(testExceptions.get(0), testExceptions, 0, 0), "onRetryFailure must return a non-null instance of type <E>");
  }

  /**
   * Creates a new {@link RetryPolicy} with the specified {@code maxRetries} value.
   *
   * @param retryOn The {@link RetryOn} specifying the conditions under which a retry should occur given the provided non-null
   *          {@link Exception}, returning {@code true} if a retry should occur, and {@code false} otherwise.
   * @param onRetry {@link IntConsumer} providing the {@code attemptNo} that is called before each execution of
   *          {@link Retryable#retry(RetryPolicy,int)}. Runtime exceptions thrown from this method will result in the termination of
   *          the {@link RetryPolicy}'s execution.
   * @param onRetryFailure The {@link OnRetryFailure} specifying the {@link Exception} instance of type {@code <E>} to be thrown in
   *          the event of terminal failure of the {@link RetryPolicy} execution.
   * @param maxRetries A positive value representing the number of retry attempts allowed by the {@link RetryPolicy}.
   * @param startDelayMs A positive value representing the delay for the first retry, in milliseconds, which is also used as the
   *          multiplicative factor for subsequent backed-off delays.
   * @param jitter The maximum value of a random factor multiplier to be applied to {@link #getDelayMs(int)} to be added to the
   *          delay for each retry.
   * @param delayOnFirstRetry {@code true} for the first retry to be attempted after {@code startDelayMs}, otherwise {@code false}
   *          for the first retry to be attempted immediately.
   * @param backoffFactor The base of the backoff exponential function, i.e. a value of {@code 2} represents a backoff function of
   *          {@code 2^a}, where {@code a} is the attempt number.
   * @throws IllegalArgumentException If {@code maxRetries}, {@code startDelayMs}, or {@code jitter} is negative, or if
   *           {@code backoffFactor} is less than {@code 1}.
   * @throws NullPointerException If {@code retryOn} or {@code onRetryFailure} is null, or if {@code onRetryFailure} returns a null
   *           value.
   */
  public RetryPolicy(final RetryOn retryOn, final IntConsumer onRetry, final OnRetryFailure<E> onRetryFailure, final int maxRetries, final long startDelayMs, final double jitter, final boolean delayOnFirstRetry, final double backoffFactor) {
    this(retryOn, onRetry, onRetryFailure, maxRetries, startDelayMs, jitter, delayOnFirstRetry, backoffFactor, Long.MAX_VALUE);
  }

  /**
   * Creates a new {@link RetryPolicy} with the specified {@code maxRetries} value.
   *
   * @param retryOn The {@link RetryOn} specifying the conditions under which a retry should occur given the provided non-null
   *          {@link Exception}, returning {@code true} if a retry should occur, and {@code false} otherwise.
   * @param onRetry {@link IntConsumer} providing the {@code attemptNo} that is called before each execution of
   *          {@link Retryable#retry(RetryPolicy,int)}. Runtime exceptions thrown from this method will result in the termination of
   *          the {@link RetryPolicy}'s execution.
   * @param onRetryFailure The {@link OnRetryFailure} specifying the {@link Exception} instance of type {@code <E>} to be thrown in
   *          the event of terminal failure of the {@link RetryPolicy} execution.
   * @param maxRetries A positive value representing the number of retry attempts allowed by the {@link RetryPolicy}.
   * @param startDelayMs A positive value representing the delay for the first retry, in milliseconds, which is also used as the
   *          multiplicative factor for subsequent backed-off delays.
   * @param delayOnFirstRetry {@code true} for the first retry to be attempted after {@code startDelayMs}, otherwise {@code false}
   *          for the first retry to be attempted immediately.
   * @param jitter The maximum value of a random factor multiplier to be applied to {@link #getDelayMs(int)} to be added to the
   *          delay for each retry.
   * @throws IllegalArgumentException If {@code maxRetries}, {@code startDelayMs}, or {@code jitter} is negative.
   * @throws NullPointerException If {@code retryOn} or {@code onRetryFailure} is null, or if {@code onRetryFailure} returns a null
   *           value.
   */
  public RetryPolicy(final RetryOn retryOn, final IntConsumer onRetry, final OnRetryFailure<E> onRetryFailure, final int maxRetries, final long startDelayMs, final double jitter, final boolean delayOnFirstRetry) {
    this(retryOn, onRetry, onRetryFailure, maxRetries, startDelayMs, jitter, delayOnFirstRetry, 1, Long.MAX_VALUE);
  }

  /**
   * Creates a new {@link RetryPolicy} with the specified {@code maxRetries} value.
   *
   * @param retryOn The {@link RetryOn} specifying the conditions under which a retry should occur given the provided non-null
   *          {@link Exception}, returning {@code true} if a retry should occur, and {@code false} otherwise.
   * @param onRetry {@link IntConsumer} providing the {@code attemptNo} that is called before each execution of
   *          {@link Retryable#retry(RetryPolicy,int)}. Runtime exceptions thrown from this method will result in the termination of
   *          the {@link RetryPolicy}'s execution.
   * @param onRetryFailure The {@link OnRetryFailure} specifying the {@link Exception} instance of type {@code <E>} to be thrown in
   *          the event of terminal failure of the {@link RetryPolicy} execution.
   * @param maxRetries A positive value representing the number of retry attempts allowed by the {@link RetryPolicy}.
   * @param startDelayMs A positive value representing the delay for the first retry, in milliseconds, which is also used as the
   *          multiplicative factor for subsequent backed-off delays.
   * @param jitter The maximum value of a random factor multiplier to be applied to {@link #getDelayMs(int)} to be added to the
   *          delay for each retry.
   * @throws IllegalArgumentException If {@code maxRetries}, {@code startDelayMs}, or {@code jitter} is negative.
   * @throws NullPointerException If {@code retryOn} or {@code onRetryFailure} is null, or if {@code onRetryFailure} returns a null
   *           value.
   */
  public RetryPolicy(final RetryOn retryOn, final IntConsumer onRetry, final OnRetryFailure<E> onRetryFailure, final int maxRetries, final long startDelayMs, final double jitter) {
    this(retryOn, onRetry, onRetryFailure, maxRetries, startDelayMs, jitter, false, 1, Long.MAX_VALUE);
  }

  /**
   * Creates a new {@link RetryPolicy} with the specified {@code maxRetries} value.
   *
   * @param retryOn The {@link RetryOn} specifying the conditions under which a retry should occur given the provided non-null
   *          {@link Exception}, returning {@code true} if a retry should occur, and {@code false} otherwise.
   * @param onRetry {@link IntConsumer} providing the {@code attemptNo} that is called before each execution of
   *          {@link Retryable#retry(RetryPolicy,int)}. Runtime exceptions thrown from this method will result in the termination of
   *          the {@link RetryPolicy}'s execution.
   * @param onRetryFailure The {@link OnRetryFailure} specifying the {@link Exception} instance of type {@code <E>} to be thrown in
   *          the event of terminal failure of the {@link RetryPolicy} execution.
   * @param maxRetries A positive value representing the number of retry attempts allowed by the {@link RetryPolicy}.
   * @param startDelayMs A positive value representing the delay for the first retry, in milliseconds, which is also used as the
   *          multiplicative factor for subsequent backed-off delays.
   * @throws IllegalArgumentException If {@code maxRetries} or {@code startDelayMs} is negative.
   * @throws NullPointerException If {@code retryOn} or {@code onRetryFailure} is null, or if {@code onRetryFailure} returns a null
   *           value.
   */
  public RetryPolicy(final RetryOn retryOn, final IntConsumer onRetry, final OnRetryFailure<E> onRetryFailure, final int maxRetries, final long startDelayMs) {
    this(retryOn, onRetry, onRetryFailure, maxRetries, startDelayMs, 0, false, 1, Long.MAX_VALUE);
  }

  private void retryFailed(final List<Exception> exceptions, final int attemptNo, final long delayMs) throws E, RetryFailureRuntimeException {
    int size = exceptions.size();
    final Exception lastException = size == 0 ? null : exceptions.remove(--size);
    final E e = onRetryFailure.onRetryFailure(lastException, exceptions, attemptNo, delayMs);
    if (e != null)
      throw size == 0 ? e : Throwables.addSuppressed(e, exceptions, size - 1, 0);

    if (lastException == null)
      throw new RetryFailureRuntimeException(attemptNo, delayMs);

    final RetryFailureRuntimeException re = new RetryFailureRuntimeException(lastException, attemptNo, delayMs);
    throw size == 0 ? re : Throwables.addSuppressed(re, exceptions, size - 1, 0);
  }

  /**
   * The entrypoint for a {@link Callable} object to be executed. Exceptions in {@link Callable#call()} will be considered for retry
   * if the number of {@link #maxRetries} has not been met and {@link #retryOn} returns {@code true}.
   *
   * @param <T> The type of the result object.
   * @param callable The {@link Callable} object to run.
   * @return The resulting value from {@link Callable#call()}.
   * @throws NullPointerException If {@code callable} is null.
   * @throws E Exception produced by {@link #onRetryFailure} signifying terminal failure, or if the retry attempts have met
   *           {@link #maxRetries}, or {@link #retryOn} returns {@code false}.
   * @throws RetryFailureRuntimeException If {@link #onRetryFailure} returns {@code null} when invoked in the event of a terminal
   *           failure, or if the retry attempts have met {@link #maxRetries}, or {@link #retryOn} returns {@code false}.
   */
  public final <T>T run(final Callable<T> callable) throws E, RetryFailureRuntimeException {
    Objects.requireNonNull(callable);
    return run0((r, a) -> callable.call(), 0);
  }

  /**
   * The entrypoint for a {@link ThrowingRunnable} object to be executed. Exceptions in {@link ThrowingRunnable#run()} will be
   * considered for retry if the number of {@link #maxRetries} has not been met and {@link #retryOn} returns {@code true}.
   *
   * @param <T> The type of the result object.
   * @param runnable The {@link ThrowingRunnable} object to run.
   * @return The resulting value from {@link Runnable#run()}.
   * @throws NullPointerException If {@code runnable} is null.
   * @throws E Exception produced by {@link #onRetryFailure} signifying terminal failure, or if the retry attempts have met
   *           {@link #maxRetries}, or {@link #retryOn} returns {@code false}.
   * @throws RetryFailureRuntimeException If {@link #onRetryFailure} returns {@code null} when invoked in the event of a terminal
   *           failure, or if the retry attempts have met {@link #maxRetries}, or {@link #retryOn} returns {@code false}.
   */
  public final <T>T run(final ThrowingRunnable<?> runnable) throws E, RetryFailureRuntimeException {
    Objects.requireNonNull(runnable);
    return run0((r, a) -> {
      runnable.run();
      return null;
    }, 0);
  }

  /**
   * The entrypoint for a {@link Retryable} object to be executed. Exceptions in {@link Retryable#retry(RetryPolicy,int)} will be
   * considered for retry if the number of {@link #maxRetries} has not been met and {@link #retryOn} returns {@code true}.
   *
   * @param <T> The type of the result object.
   * @param retryable The {@link Retryable} object to run.
   * @return The resulting value from {@link Retryable#retry(RetryPolicy,int)}.
   * @throws NullPointerException If {@code retryable} is null.
   * @throws E Exception produced by {@link #onRetryFailure} signifying terminal failure, or if the retry attempts have met
   *           {@link #maxRetries}, or {@link #retryOn} returns {@code false}.
   * @throws RetryFailureRuntimeException If {@link #onRetryFailure} returns {@code null} when invoked in the event of a terminal
   *           failure, or if the retry attempts have met {@link #maxRetries}, or {@link #retryOn} returns {@code false}.
   */
  public final <T>T run(final Retryable<T,E> retryable) throws E, RetryFailureRuntimeException {
    return run0(Objects.requireNonNull(retryable), 0);
  }

  /**
   * The entrypoint for a {@link Retryable} object to be executed. Exceptions in {@link Retryable#retry(RetryPolicy,int)} will be
   * considered for retry if the number of {@link #maxRetries} has not been met and {@link #retryOn} returns {@code true}.
   *
   * @param <T> The type of the result object.
   * @param retryable The {@link Retryable} object to run.
   * @param timeout The maximum time after which this {@link RetryPolicy} is to invoke {@link #onRetryFailure}.
   * @param unit The time unit of the {@code timeout} argument.
   * @return The resulting value from {@link Retryable#retry(RetryPolicy,int)}.
   * @throws NullPointerException If {@code retryable} or {@code unit} is null.
   * @throws IllegalArgumentException If {@code timeout} is negative.
   * @throws E Exception produced by {@link #onRetryFailure} signifying terminal failure, or if the retry attempts have met
   *           {@link #maxRetries}, or {@link #retryOn} returns {@code false}.
   * @throws RetryFailureRuntimeException If {@link #onRetryFailure} returns {@code null} when invoked in the event of a terminal
   *           failure, or if the retry attempts have met {@link #maxRetries}, or {@link #retryOn} returns {@code false}.
   */
  public final <T>T run(final Retryable<T,E> retryable, final long timeout, final TimeUnit unit) throws E, RetryFailureRuntimeException {
    assertPositive(timeout, () -> "timeout value (" + timeout + ") must be a positive value");
    return run0(retryable, TimeUnit.MILLISECONDS.convert(timeout, unit));
  }

  private final <T>T run0(final Retryable<T,E> retryable, final long timeout) throws E, RetryFailureRuntimeException {
    final List<Exception> exceptions = new ArrayList<>();
    final long startTime = System.currentTimeMillis();
    long runTime = 0;
    Exception previousException = null;
    for (int attemptNo = 1;; ++attemptNo) { // [N]
      if (onRetry != null)
        onRetry.accept(attemptNo);

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

      if (logger.isDebugEnabled()) logger.debug("Retrying attemptNo = " + attemptNo + ", runTime = " + runTime);
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
   * Returns the delay in milliseconds for the specified attempt number. This method is intended to be implemented by a subclass to
   * define the backoff function for retry attempts.
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