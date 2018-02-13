/* Copyright (c) 2018 lib4j
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

package org.lib4j.util.retry;

public class ExponentialBackoffRetryPolicy extends RetryPolicy {
  private static final long serialVersionUID = -6999301056780454011L;

  private final int delayMs;
  private final float backoffFactor;
  private final int maxDelayMs;
  private final boolean noDelayOnFirstRetry;

  public ExponentialBackoffRetryPolicy(final int maxRetries, final int delayMs, final float backoffFactor, final int maxDelayMs, final boolean noDelayOnFirstRetry) {
    super(maxRetries);
    this.delayMs = delayMs;
    if (delayMs <= 0)
      throw new IllegalArgumentException("baseDelayMs must be >= 0");

    this.backoffFactor = backoffFactor;
    if (backoffFactor < 1.0)
      throw new IllegalArgumentException("backoffFactor must be >= 1.0");

    this.maxDelayMs = maxDelayMs == -1 ? Integer.MAX_VALUE : maxDelayMs;
    if (maxDelayMs <= 0 && maxDelayMs != -1)
      throw new IllegalArgumentException("maxDelayMs must be > 0 or -1");

    this.noDelayOnFirstRetry = noDelayOnFirstRetry;
  }

  @Override
  public int getDelayMs(final int attemptNo) {
    return Math.min(attemptNo == 1 && noDelayOnFirstRetry ? 0 : (int)(delayMs * Math.pow(backoffFactor, attemptNo - 1)), maxDelayMs);
  }
}