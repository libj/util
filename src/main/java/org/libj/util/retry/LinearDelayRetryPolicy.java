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
 * A {@link RetryPolicy} that defines a maximum number of retries, and a
 * constant delay for retry attempts.
 */
public class LinearDelayRetryPolicy extends RetryPolicy {
  private static final long serialVersionUID = -6090799745376513743L;

  private final int delayMs;
  private final boolean noDelayOnFirstRetry;

  /**
   * Creates a new {@link LinearDelayRetryPolicy} with the specified maximum
   * number of retries, and a constant delay for retry attempts.
   *
   * @param maxRetries A positive value representing the number of retry
   *          attempts allowed by the {@link LinearDelayRetryPolicy}.
   * @param delayMs A positive value representing the constant delay between
   *          retry attempts, in milliseconds.
   * @param noDelayOnFirstRetry {@code true} for the first retry to be attempted
   *          immediately, otherwise {@code false} for the first retry to be
   *          attempted after {@code delayMs}.
   * @param jitter The factor multiplier to be applied to {@code delayMs} to
   *          thereafter be added to the delay for each retry.
   * @throws IllegalArgumentException If {@code delayMs}, {@code maxRetries} or
   *           {@code jitter} is negative.
   */
  public LinearDelayRetryPolicy(final int maxRetries, final int delayMs, final boolean noDelayOnFirstRetry, final double jitter) {
    super(maxRetries, jitter);
    this.noDelayOnFirstRetry = noDelayOnFirstRetry;
    this.delayMs = delayMs;
    if (delayMs <= 0)
      throw new IllegalArgumentException("delayMs (" + delayMs + ") must be a positive value");
  }

  @Override
  public long getDelayMs(final int attemptNo) {
    return attemptNo == 1 && noDelayOnFirstRetry ? 0 : delayMs;
  }
}