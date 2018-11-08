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

/**
 * A {@link RetryPolicy} that defines a maximum number of retries, and a
 * delay for retry attempts that increases exponentially based on a backoff factor.
 */
public class ExponentialBackoffRetryPolicy extends RetryPolicy {
  private static final long serialVersionUID = -6999301056780454011L;

  private final int delayMs;
  private final float backoffFactor;
  private final int maxDelayMs;
  private final boolean noDelayOnFirstRetry;

  /**
   * Creates a new {@code ExponentialBackoffRetryPolicy} with the specified
   * maximum number of retries, and a delay for retry attempts that increases
   * exponentially based on a backoff factor.
   *
   * @param maxRetries A positive value representing the number of retry
   *          attempts allowed by the {@code ExponentialBackoffRetryPolicy}.
   * @param delayMs A positive value representing the delay for the first retry,
   *          in milliseconds, which is also used as the multiplicative factor
   *          for subsequent backed-off delays.
   * @param backoffFactor The base of the backoff exponential function, i.e. a
   *          value of {@code 2} represents a backoff function of {@code 2^a},
   *          where {@code a} is the attempt number.
   * @param maxDelayMs The maximum delay, in milliseconds, which takes effect if
   *          the delay computed by the backoff function is a greater value.
   * @param noDelayOnFirstRetry {@code true} for the first retry to be attempted
   *          immediately, otherwise {@code false} for the first retry to be
   *          attempted after {@code delayMs}.
   */
  public ExponentialBackoffRetryPolicy(final int maxRetries, final int delayMs, final float backoffFactor, final int maxDelayMs, final boolean noDelayOnFirstRetry) {
    super(maxRetries);
    this.delayMs = delayMs;
    if (delayMs <= 0)
      throw new IllegalArgumentException("delayMs (" + delayMs + ") must be a positive value");

    this.backoffFactor = backoffFactor;
    if (backoffFactor < 1.0)
      throw new IllegalArgumentException("backoffFactor (" + backoffFactor + ") must be >= 1.0");

    this.maxDelayMs = maxDelayMs;
    if (maxDelayMs <= 0)
      throw new IllegalArgumentException("maxDelayMs (" + maxDelayMs + ") must be a positive value");

    this.noDelayOnFirstRetry = noDelayOnFirstRetry;
  }

  @Override
  public int getDelayMs(final int attemptNo) {
    return Math.min(attemptNo == 1 && noDelayOnFirstRetry ? 0 : (int)(delayMs * Math.pow(backoffFactor, attemptNo - 1)), maxDelayMs);
  }
}