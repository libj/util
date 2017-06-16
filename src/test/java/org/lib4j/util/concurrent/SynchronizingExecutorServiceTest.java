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

import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

public class SynchronizingExecutorServiceTest {
  private static final Logger logger = LoggerFactory.getLogger(SynchronizingExecutorServiceTest.class);

  private static final int threadRuntime = 2000;

  private final int testConsumerThreads = 200;
  private volatile int testConsumerCounter = 0;
  private volatile Integer testLastConsumerCountAllowed = null;
  private long lastSync = System.currentTimeMillis();
  private long syncPeriod = 3000;

  private volatile String error;

  @Test
  public void test() throws InterruptedException {
    final ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
    root.setLevel(Level.ALL);

    final SynchronizingExecutorService executorService = new SynchronizingExecutorService(Executors.newFixedThreadPool(testConsumerThreads / 4)) {
      @Override
      public void onSynchronize() {
        testLastConsumerCountAllowed = testConsumerCounter;
        lastSync = System.currentTimeMillis();

        try {
          // Check against the executor's count...
          if (getRunningThreadCount() != 0) {
            SynchronizingExecutorServiceTest.this.error = "getRunningThreadCount() = " + getRunningThreadCount();
            return;
          }

          // Check against our own count...
          if (consumerCount != 0) {
            SynchronizingExecutorServiceTest.this.error = "consumerCount = " + consumerCount;
            return;
          }

          logger.info("Syncing with " + consumerCount + " running consumers...");
          Thread.sleep(1000);
        }
        catch (final InterruptedException e) {
          error = e.getMessage();
        }

        testLastConsumerCountAllowed = null;
      }
    };

    for (int j = 0; j < testConsumerThreads; j++) {
      if (this.error != null)
        Assert.fail(this.error);

      executorService.submit(new Consumer(testConsumerCounter++));
      if (lastSync + syncPeriod <= System.currentTimeMillis()) {
        lastSync = System.currentTimeMillis();
        new Thread() {
          @Override
          public void run() {
            logger.info("Starting sync with " + consumerCount + " running consumers");
            try {
              executorService.synchronize();
            }
            catch (final InterruptedException e) {
              error = e.getMessage();
            }
          }
        }.start();
      }

      Thread.sleep((long)(Math.random() * 100));
    }
  }

  private volatile int consumerCount = 0;

  private class Consumer implements Runnable {
    private final int id;

    public Consumer(final int id) {
      this.id = id;
    }

    @Override
    public void run() {
      ++consumerCount;
      logger.info("    Starting " + Thread.currentThread().getName());
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
          throw new RuntimeException(e);
        }

        logger.info("    Finished " + Thread.currentThread().getName());
      }
      finally {
        --consumerCount;
      }
    }
  }
}