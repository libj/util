/* Copyright (c) 2023 LibJ
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
 * Thrown to indicate that a retry should occur.
 */
public class RetryException extends Exception {
  /**
   * Creates a new {@link RetryException}.
   */
  public RetryException() {
    super();
  }

  /**
   * Creates a new {@link RetryException}.
   *
   * @param message The detail message.
   */
  public RetryException(final String message) {
    super(message);
  }

  /**
   * Creates a new {@link RetryException}.
   *
   * @param cause The cause.
   */
  public RetryException(final Exception cause) {
    super(cause);
  }

  /**
   * Creates a new {@link RetryException}.
   *
   * @param message The detail message.
   * @param cause The cause.
   */
  public RetryException(final String message, final Exception cause) {
    super(message, cause);
  }
}