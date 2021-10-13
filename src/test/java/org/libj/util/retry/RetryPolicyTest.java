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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetryPolicyTest {
  private static final Logger logger = LoggerFactory.getLogger(RetryPolicyTest.class);

  @Test
  public void testLinearBackoff() {
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

    assertEquals("PASS", new RetryPolicy<>(e -> true, null, (e, a, d) -> new RuntimeException(), attempts, delayMs).run((retryPolicy, attemptNo) -> {
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

    assertEquals("PASS", new RetryPolicy<>(e -> true, null, (e, a, d) -> new RetryFailureException(a, d), attempts, startDelay, 0, false, factor, maxDelay).run((retryPolicy, attemptNo) -> {
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