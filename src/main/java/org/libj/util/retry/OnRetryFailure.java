/* Copyright (c) 2021 LibJ
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

import java.util.List;

@FunctionalInterface
public interface OnRetryFailure<E extends Exception> {
  /**
   * Callback to return the {@link Exception} instance of type {@code <E>} to be
   * thrown in the event of terminal failure of the {@link RetryPolicy}
   * execution.
   * <p>
   * If this method is not overridden, or if it returns {@code null}, a
   * {@link RetryFailureException} will be thrown instead.
   *
   * @param exceptions The exceptions that occurred during execution of a
   *          {@link Retryable} object in the order of their occurrence.
   * @param attemptNo The attempt number on which the exception was thrown.
   * @param delayMs The delay (in milliseconds) from the previous invocation
   *          attempt.
   * @return The {@link Exception} instance of type {@code <E>} signifying the
   *         terminal failure of the {@link RetryPolicy} execution.
   */
  E onRetryFailure(List<Exception> exceptions, int attemptNo, long delayMs);
}