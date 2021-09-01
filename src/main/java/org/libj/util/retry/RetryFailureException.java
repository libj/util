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
 * Thrown to indicate the ultimate failure of retried invocation(s) by a
 * {@link RetryPolicy}.
 */
public class RetryFailureException extends Exception {
  private static final long serialVersionUID = 4067260656337660435L;

  private final int attemptNo;
  private final long delayMs;

  /**
   * Constructs a {@link RetryFailureException} with the specified exception
   * that was the cause of this exception.
   *
   * @param cause The exception that was the cause of this exception.
   * @param attemptNo The attempt number on which the exception was thrown.
   * @param delayMs The delay (in milliseconds) from the previous invocation
   *          attempt.
   */
  public RetryFailureException(final Throwable cause, final int attemptNo, final long delayMs) {
    super("attemptNo = " + attemptNo + ", delayMs = " + delayMs, cause);
    this.attemptNo = attemptNo;
    this.delayMs = delayMs;
  }

  /**
   * Constructs a {@link RetryFailureException} without a specified exception
   * cause.
   *
   * @param attemptNo The attempt number on which the exception was thrown.
   * @param delayMs The delay (in milliseconds) from the previous invocation
   *          attempt.
   */
  public RetryFailureException(final int attemptNo, final long delayMs) {
    this(null, attemptNo, delayMs);
  }

  /**
   * Returns the attempt number on which the exception was thrown.
   *
   * @return The attempt number on which the exception was thrown.
   */
  public int getAttemptNo() {
    return this.attemptNo;
  }

  /**
   * Returns the delay (in milliseconds) from the previous invocation attempt.
   *
   * @return The delay (in milliseconds) from the previous invocation attempt.
   */
  public long getDelayMs() {
    return this.delayMs;
  }
}