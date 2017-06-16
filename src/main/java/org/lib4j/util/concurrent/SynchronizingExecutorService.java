/* Copyright (c) 2017 lib4j
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

package org.lib4j.util.concurrent;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SynchronizingExecutorService extends AbstractExecutorService {
  private static final Logger logger = LoggerFactory.getLogger(SynchronizingExecutorService.class);

  private final Object startLock = new Object();
  private final Object finishLock = new Object();
  private volatile boolean synchronizing;
  private volatile int runningThreadCount;

  private final ExecutorService executorService;

  public SynchronizingExecutorService(final ExecutorService executorService) {
    this.executorService = executorService;
  }

  /**
   * Called when this service synchronizes all executing threads to finish.
   * When this method is called, no threads in this service are pending
   * completion.
   */
  public abstract void onSynchronize();

  /**
   * Gets the current count of running threads in this service.
   * @return current count of running threads in this service.
   */
  public int getRunningThreadCount() {
    return runningThreadCount;
  }

  /**
   * Stop execution of new threads, and merge all running threads. Once
   * merged, <code>onSynchronize()</code> is called. If this method's
   * thread is interrupted waiting for threads to finish, it throws a
   * <code>InterruptedException</code>
   * @throws InterruptedException Thrown if this method's thread is interrupted
   * waiting for its threads to finish.
   */
  public void synchronize() throws InterruptedException {
    if (synchronizing)
      return;

    synchronized (startLock) {
      if (synchronizing)
        return;

      logger.debug("Starting sync....");
      if (runningThreadCount > 0) {
        synchronized (finishLock) {
          synchronizing = true;
          logger.debug("wait() for threads to finish...");
          finishLock.wait();
        }
      }
      else {
        synchronizing = true;
      }

      onSynchronize();
      synchronizing = false;
      logger.debug("Sync done!");
    }
  }

  /**
   * Executes the given command at some time in the future. If the
   * <code>SynchronizingExecutor</code> is synchronizing, the given command
   * will wait until <code>onSynchronize()</code> returns.
   *
   * @param command the runnable task
   * @throws RejectedExecutionException if this task cannot be
   * accepted for execution
   * @throws NullPointerException if command is null
   */
  @Override
  public void execute(final Runnable command) {
    if (command == null)
      throw new NullPointerException("runnable == null");

    final Runnable wrapper = new Runnable() {
      @Override
      public void run() {
        try {
          command.run();
        }
        finally {
          logger.debug("Remaining threads: " + (runningThreadCount - 1));
          if (--runningThreadCount == 0 && synchronizing) {
            synchronized (finishLock) {
              logger.debug("notify() synchronize to continue...");
              finishLock.notify();
            }
          }
        }
      }
    };

    if (!synchronizing) {
      ++runningThreadCount;
      executorService.execute(wrapper);
      return;
    }

    logger.debug("Waiting for unlock to exec new threads...");
    synchronized (startLock) {
      ++runningThreadCount;
      executorService.execute(wrapper);
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