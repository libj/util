/* Copyright (c) 2018 FastJAX
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

package org.fastjax.util.retry;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetryPolicyTest {
  private static final Logger logger = LoggerFactory.getLogger(RetryPolicyTest.class);

  @Test
  public void testWithPolicy() throws RetryFailureException {
    final boolean[] called = new boolean[1];

    assertEquals("PASS", new RetryPolicy(1000) {
      private static final long serialVersionUID = 811448140777577622L;

      @Override
      public int getDelayMs(final int attemptNo) {
        return 0;
      }
    }.run(new Retryable<String>() {
      @Override
      public String retry(final RetryPolicy retryPolicy, final int attemptNo) throws Exception {
        if (called[0])
          return "PASS";

        called[0] = true;
        throw new RetryException();
      }
    }));

    assertTrue(called[0]);
  }

  @Test
  public void testLinearBackoff() throws RetryFailureException {
    final int attempts = 5;
    final int delayMs = 100;

    final int[] delays = new int[attempts];
    for (int i = 1; i < attempts; ++i)
      delays[i] = delayMs;

    final int[] index = new int[] {0};
    final long[] timings = new long[attempts];
    timings[0] = System.currentTimeMillis();
    for (int i = 0; i < attempts - 1; ++i)
      timings[i + 1] = timings[i] + delays[i];

    assertEquals("PASS", new LinearDelayRetryPolicy(attempts, delayMs, true).run(new Retryable<String>() {
      @Override
      public String retry(final RetryPolicy retryPolicy, final int attemptNo) throws Exception {
        if (index[0] < attempts) {
          final int delayMs = retryPolicy.getDelayMs(attemptNo);
          assertEquals(delays[index[0]++], delayMs);
          assertTrue(0 < attemptNo && attemptNo <= attempts);
          assertEquals(timings[attemptNo - 1], System.currentTimeMillis(), 5);
          logger.info("Attempt: " + attemptNo + ", delay: " + delayMs + ", t: " + RetryException.class.getSimpleName());
          throw new RetryException();
        }

        return "PASS";
      }
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
    final int[] index = new int[] {0};
    final long[] timings = new long[attempts];
    timings[0] = System.currentTimeMillis();
    for (int i = 0; i < attempts - 1; ++i)
      timings[i + 1] = timings[i] + delays[i];

    assertEquals("PASS", new ExponentialBackoffRetryPolicy(attempts, startDelay, factor, maxDelay, true).run(new Retryable<String>() {
      @Override
      public String retry(final RetryPolicy retryPolicy, final int attemptNo) throws Exception {
        if (index[0] < attempts) {
          final int delayMs = retryPolicy.getDelayMs(attemptNo);
          assertEquals(delays[index[0]++], delayMs);
          assertTrue(0 < attemptNo && attemptNo <= attempts);
          assertEquals(timings[attemptNo - 1], System.currentTimeMillis(), 5);
          logger.info("Attempt: " + attemptNo + ", delay: " + delayMs + ", t: " + RetryException.class.getSimpleName());
          throw new RetryException();
        }

        return "PASS";
      }
    }));
  }
}