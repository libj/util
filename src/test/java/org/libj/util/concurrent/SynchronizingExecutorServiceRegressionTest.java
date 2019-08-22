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

import static org.junit.Assert.*;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.libj.logging.DeferredLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class SynchronizingExecutorServiceRegressionTest {
  private static final Logger logger = DeferredLogger.defer(LoggerFactory.getLogger(SynchronizingExecutorServiceRegressionTest.class), Level.DEBUG);

  private static final int threadRuntime = 500;

  private final int testConsumerThreads = 3000;
  private volatile int testConsumerCounter = 0;
  private volatile Integer testLastConsumerCountAllowed = null;
  private long lastSync = System.currentTimeMillis();
  private long syncPeriod = 500;
  private volatile long syncTs;
  private volatile long consumerTs;

  private volatile String error;

  @Test
  public void test() throws InterruptedException {
    try {
      runTest();
    }
    catch (final InterruptedException e) {
      DeferredLogger.flush();
      throw e;
    }
  }

  private void runTest() throws InterruptedException {
    final SynchronizingExecutorService executorService = new SynchronizingExecutorService(Executors.newFixedThreadPool(testConsumerThreads / 4)) {
      @Override
      public void onSynchronize() {
        testLastConsumerCountAllowed = testConsumerCounter;

        try {
          // Check against the executor's count...
          if (getRunningThreadCount() != 0) {
            SynchronizingExecutorServiceRegressionTest.this.error = "getRunningThreadCount() = " + getRunningThreadCount();
            return;
          }

          // Check against our own count...
          if (consumerCount.get() != 0) {
            SynchronizingExecutorServiceRegressionTest.this.error = "consumerCount = " + consumerCount + ", testLastConsumerCountAllowed = " + testLastConsumerCountAllowed + ", (syncTs - consumerTs) = " + (syncTs - consumerTs);
            return;
          }

          logger.debug("Syncing with " + consumerCount + " running consumers...");
          Thread.sleep(10);
        }
        catch (final InterruptedException e) {
          error = e.getMessage();
        }

        testLastConsumerCountAllowed = null;
        lastSync = System.currentTimeMillis();
      }
    };

    for (int j = 0; j < testConsumerThreads; j++) {
      if (this.error != null)
        fail(this.error);

      executorService.submit(new Consumer(testConsumerCounter++));
      if (lastSync + syncPeriod <= System.currentTimeMillis()) {
        lastSync = System.currentTimeMillis();
        new Thread(() -> {
          logger.debug("Starting sync with " + consumerCount + " running consumers");
          try {
            syncTs = System.currentTimeMillis();
            executorService.synchronize();
          }
          catch (final InterruptedException e) {
            error = e.getMessage();
          }
        }).start();
      }

      Thread.sleep((long)(Math.random() * 10));
    }
  }

  private final AtomicInteger consumerCount = new AtomicInteger();

  private class Consumer implements Runnable {
    private final int id;

    public Consumer(final int id) {
      this.id = id;
    }

    @Override
    public void run() {
      consumerCount.incrementAndGet();
      consumerTs = System.currentTimeMillis();
      logger.debug("    Starting new Consumer(" + id + ")");
      try {
        // Check whether this thread should have been executed
        if (testLastConsumerCountAllowed != null && testLastConsumerCountAllowed < id) {
          error = "Should not start thread while syncing";
          return;
        }

        if (Math.random() < .1)
          throw new UnsupportedOperationException();

        try {
          Thread.sleep((long)(Math.random() * threadRuntime));
        }
        catch (final InterruptedException e) {
          throw new IllegalStateException(e);
        }

        logger.debug("    Finished " + id);
      }
      finally {
        consumerCount.decrementAndGet();
      }
    }
  }
}