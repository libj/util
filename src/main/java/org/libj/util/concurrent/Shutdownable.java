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

package org.libj.util.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * An abstraction of a lifecycle can involves the shutting down of completable
 * dependencies.
 */
public interface Shutdownable {
  /**
   * Returns {@code true} if this instance has been shut down.
   *
   * @return {@code true} if this instance has been shut down.
   */
  boolean isShutdown();

  /**
   * Returns {@code true} if all outstanding completable dependencies have
   * completed following shut down. Note that {@link #isTerminated()} is never
   * {@code true} unless either {@link #shutdown()} or {@link #shutdownNow()}
   * was called first.
   *
   * @return {@code true} if all outstanding completable dependencies have
   *         completed following shut down.
   */
  boolean isTerminated();

  /**
   * Initiates an orderly shutdown in which all active and pending completable
   * dependencies are completed, but no new completable dependencies will be
   * accepted. Invocation has no additional effect if already shut down.
   * <p>
   * This method does not wait for completable dependencies to complete
   * execution. Use {@link #awaitTermination(long,TimeUnit)} to do that.
   */
  void shutdown();

  /**
   * Attempts to stop all active completable dependencies, and dismissed all
   * pending completable dependencies.
   * <p>
   * This method does not wait for completable dependencies to terminate. Use
   * {@link #awaitTermination(long,TimeUnit)} to do that.
   * <p>
   * There are no guarantees beyond best-effort attempts to stop processing
   * completable dependencies.
   */
  void shutdownNow();

  /**
   * Blocks until all completable dependencies have completed execution after a
   * shutdown request, or the timeout occurs, or the current thread is
   * interrupted, whichever happens first.
   *
   * @param timeout The maximum time to wait.
   * @param unit The time unit of the timeout argument.
   * @return {@code true} if this instance terminated and {@code false} if the
   *         timeout elapsed before termination.
   * @throws InterruptedException If interrupted while waiting.
   */
  boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;
}