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

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetryPolicyTest {
  private static final Logger logger = LoggerFactory.getLogger(RetryPolicyTest.class);

  private abstract class Level1RetryPolicy<E extends Exception> extends RetryPolicy<E> {
    private static final long serialVersionUID = 811448140777577622L;

    public Level1RetryPolicy(final int maxRetries, final double jitter) {
      super(maxRetries, jitter);
    }

    @Override
    protected long getDelayMs(final int attemptNo) {
      return 0;
    }

    @Override
    protected boolean retryOn(final Exception e) {
      return false;
    }
  }

  private abstract class Level2RetryPolicy<E extends Exception> extends Level1RetryPolicy<E> {
    private static final long serialVersionUID = 6106256145228319206L;

    public Level2RetryPolicy(final int maxRetries, final double jitter) {
      super(maxRetries, jitter);
    }
  }

  @Test
  public void testWithPolicy() throws RetryFailureException {
    final boolean[] called = new boolean[1];

    try {
      new Level2RetryPolicy<IOException>(1000, 0) {
        private static final long serialVersionUID = 3973463128089152060L;

        @Override
        protected IOException onRetryFailure(final Exception e, final int attemptNo, final long delayMs) {
          return new IOException(e);
        }
      }.run((retryPolicy, attemptNo) -> {
        if (called[0])
          return "PASS";

        called[0] = true;
        throw new IllegalStateException();
      });

      fail("Expected IOException");
    }
    catch (final IOException e) {
    }

    assertTrue(called[0]);
  }

  @Test
  public void testLinearBackoff() throws RetryFailureException {
    final int attempts = 5;
    final int delayMs = 100;

    final int[] delays = new int[attempts];
    for (int i = 1; i < attempts; ++i)
      delays[i] = delayMs;

    final int[] index = {0};
    final long[] timings = new long[attempts];
    timings[0] = System.currentTimeMillis();
    for (int i = 0; i < attempts - 1; ++i)
      timings[i + 1] = timings[i] + delays[i];

    assertEquals("PASS", new LinearDelayRetryPolicy<RuntimeException>(attempts, delayMs, true, 0) {
      private static final long serialVersionUID = 5786003126480516387L;

      @Override
      protected boolean retryOn(final Exception e) {
        return true;
      }

      @Override
      protected RuntimeException onRetryFailure(final Exception e, final int attemptNo, final long delayMs) {
        return null;
      }
    }.run((retryPolicy, attemptNo) -> {
      if (index[0] < attempts) {
        final long delayMs1 = retryPolicy.getDelayMs(attemptNo);
        assertEquals(delays[index[0]++], delayMs1);
        assertTrue(0 < attemptNo && attemptNo <= attempts);
        assertEquals(timings[attemptNo - 1], System.currentTimeMillis(), 5);
        logger.info("Attempt: " + attemptNo + ", delay: " + delayMs1 + ", t: " + IllegalStateException.class.getSimpleName());
        throw new IllegalStateException();
      }

      return "PASS";
    }));
  }

  @Test
  public void testExponentialBackoff() throws RetryFailureException {
    final int attempts = 5;
    final float factor = 1.5f;
    final int startDelay = 100;
    final int maxDelay = 300;

    final int[] delays = new int[attempts];
    delays[0] = startDelay;
    for (int i = 0; i < attempts - 1; ++i)
      delays[i + 1] = (int)Math.min(delays[i] * factor, maxDelay);

    delays[0] = 0;
    final int[] index = {0};
    final long[] timings = new long[attempts];
    timings[0] = System.currentTimeMillis();
    for (int i = 0; i < attempts - 1; ++i)
      timings[i + 1] = timings[i] + delays[i];

    assertEquals("PASS", new ExponentialBackoffRetryPolicy<RuntimeException>(attempts, startDelay, factor, maxDelay, true, 0) {
      private static final long serialVersionUID = -4440457306185447865L;

      @Override
      protected boolean retryOn(final Exception e) {
        return true;
      }

      @Override
      protected RuntimeException onRetryFailure(final Exception e, final int attemptNo, final long delayMs) {
        return null;
      }
    }.run((retryPolicy, attemptNo) -> {
      if (index[0] < attempts) {
        final long delayMs = retryPolicy.getDelayMs(attemptNo);
        assertEquals(delays[index[0]++], delayMs);
        assertTrue(0 < attemptNo && attemptNo <= attempts);
        assertEquals(timings[attemptNo - 1], System.currentTimeMillis(), 5);
        logger.info("Attempt: " + attemptNo + ", delay: " + delayMs + ", t: " + IllegalStateException.class.getSimpleName());
        throw new IllegalStateException();
      }

      return "PASS";
    }));
  }
}