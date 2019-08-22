/* Copyright (c) 2017 LibJ
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

import java.util.List;
import java.util.Objects;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An {@link ExecutorService} that allows its threads to be synchronized. When
 * {@link #synchronize()} is called, the method disallows the executor service
 * from executing any new threads, and blocks until all running threads have
 * finished, signifying a synchronized state.
 * <p>
 * If the {@code SynchronizingExecutorService} is in a {@link #synchronizing}
 * state, newly submitted tasks wait until {@link #synchronize()} returns,
 * either successfully, or due to {@link InterruptedException}. Once
 * synchronized, the {@link #onSynchronize()} method is called.
 * <p>
 * If the thread of the {@link #synchronize()} method call is interrupted while
 * the method is waiting for this instance's threads to finish, the command to
 * synchronize is aborted, and {@link #synchronize()} throws an
 * {@link InterruptedException}.
 */
public abstract class SynchronizingExecutorService extends AbstractExecutorService {
  private static final Logger logger = LoggerFactory.getLogger(SynchronizingExecutorService.class);

  private final AtomicInteger runningThreadCount = new AtomicInteger();
  private final Object startLock = new Object();
  private final Object finishLock = new Object();
  private volatile boolean synchronizing;

  /**
   * The source {@code ExecutorService}.
   */
  private final ExecutorService executorService;

  /**
   * Construct a new {@code SynchronizingExecutorService} with the specified
   * source {@code ExecutorService}.
   *
   * @param executorService The source {@code ExecutorService}.
   */
  public SynchronizingExecutorService(final ExecutorService executorService) {
    this.executorService = executorService;
  }

  /**
   * Called when this service synchronizes all executing threads to finish. When
   * this method is called, no threads in this service are pending completion.
   */
  public abstract void onSynchronize();

  /**
   * Returns the current count of running threads in this service.
   *
   * @return The current count of running threads in this service.
   */
  public int getRunningThreadCount() {
    return runningThreadCount.get();
  }

  /**
   * Stop execution of new threads, and wait for all running threads to finish.
   * Once all threads have finished, {@code onSynchronize()} is called. If this
   * method's thread is interrupted waiting for this instance's threads to
   * finish, the command to synchronize is aborted, and this method throws an
   * {@code InterruptedException}.
   *
   * @throws InterruptedException If this method's thread is interrupted waiting
   *           for this instance's threads to finish.
   */
  public void synchronize() throws InterruptedException {
    if (synchronizing)
      return;

    synchronized (startLock) {
      if (synchronizing)
        return;

      try {
        logger.debug("Starting sync....");
        if (runningThreadCount.get() > 0) {
          synchronized (finishLock) {
            synchronizing = true;
            logger.debug("wait() [1] for threads to finish...");
            finishLock.wait();
          }
        }
        else {
          synchronizing = true;
          if (runningThreadCount.get() > 0) {
            synchronized (finishLock) {
              logger.debug("wait() [2] for threads to finish...");
              finishLock.wait();
            }
          }
        }

        onSynchronize();
      }
      finally {
        synchronizing = false;
      }

      logger.debug("Sync done!");
    }
  }

  /**
   * Executes the given command at some time in the future. If the
   * {@code SynchronizingExecutor} is synchronizing, the given command will wait
   * until {@code onSynchronize()} returns.
   *
   * @param command The runnable task.
   * @throws RejectedExecutionException If this task cannot be accepted for
   *           execution.
   * @throws NullPointerException If {@code command} is null.
   */
  @Override
  public void execute(final Runnable command) {
    Objects.requireNonNull(command);
    final Runnable wrapper = () -> {
      try {
        command.run();
      }
      finally {
        logger.debug("Remaining threads: " + (runningThreadCount.get() - 1));
        if (runningThreadCount.decrementAndGet() == 0 && synchronizing) {
          synchronized (finishLock) {
            logger.debug("notify() synchronize to continue...");
            finishLock.notify();
          }
        }
      }
    };

    if (!synchronizing) {
      runningThreadCount.incrementAndGet();
      try {
        executorService.execute(wrapper);
      }
      catch (final Throwable e) {
        runningThreadCount.decrementAndGet();
        throw e;
      }

      return;
    }

    logger.debug("Waiting for unlock to exec new threads...");
    synchronized (startLock) {
      runningThreadCount.incrementAndGet();
      try {
        executorService.execute(wrapper);
      }
      catch (final Throwable e) {
        runningThreadCount.decrementAndGet();
        throw e;
      }
    }
  }

  @Override
  public void shutdown() {
    executorService.shutdown();
  }

  @Override
  public List<Runnable> shutdownNow() {
    return executorService.shutdownNow();
  }

  @Override
  public boolean isShutdown() {
    return executorService.isShutdown();
  }

  @Override
  public boolean isTerminated() {
    return executorService.isTerminated();
  }

  @Override
  public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
    return executorService.awaitTermination(timeout, unit);
  }
}