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

import java.io.Serializable;

public abstract class RetryPolicy implements Serializable {
  private static final long serialVersionUID = -8480057566592276543L;

  private final int maxRetries;

  public RetryPolicy(final int maxRetries) {
    this.maxRetries = maxRetries == -1 ? Integer.MAX_VALUE : maxRetries;
    if (maxRetries < -1 || maxRetries == 0)
      throw new IllegalArgumentException("maxRetries must positive, or -1");
  }

  protected boolean retryOn(final Throwable t) {
    return t instanceof RetryException;
  }

  public final <T>T run(final Retryable<T> retryable) throws RetryFailureException {
    for (int attemptNo = 1;; attemptNo++) {
      try {
        return retryable.retry(this, attemptNo);
      }
      catch (final Throwable t) {
        if (attemptNo > maxRetries || !retryOn(t))
          throw new RetryFailureException(t, attemptNo, getDelayMs(attemptNo));

        final int delayMs = getDelayMs(attemptNo);
        try {
          Thread.sleep(delayMs);
        }
        catch (final InterruptedException e) {
          throw new RetryFailureException(e, attemptNo, delayMs);
        }
      }
    }
  }

  public int getMaxRetries() {
    return this.maxRetries;
  }

  public abstract int getDelayMs(final int attemptNo);
}