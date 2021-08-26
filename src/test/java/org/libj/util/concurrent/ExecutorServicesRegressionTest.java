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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.libj.lang.ThreadsTest;
import org.libj.util.function.BiObjBiLongConsumer;

public class ExecutorServicesRegressionTest {
  private static class Task implements Runnable {
    private final long sleepTime;

    private Task(final long sleepTime) {
      this.sleepTime = sleepTime;
    }

    @Override
    public void run() {
      try {
        Thread.sleep(sleepTime);
      }
      catch (final InterruptedException e) {
      }
    }
  }

  private static final int numTests = 10;

  @Test
  public void testExecutorNotNull() {
    try {
      ExecutorServices.invokeAll(null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }
  }

  @Test
  public void testTasksNotEmpty() {
    try {
      ExecutorServices.invokeAll(Executors.newCachedThreadPool());
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }
  }

  @Test
  public void testTasksNotNull() {
    try {
      ExecutorServices.invokeAll(Executors.newCachedThreadPool(), (Runnable[])null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }
  }

  @Test
  public void testTaskNotNull() {
    try {
      ExecutorServices.invokeAll(Executors.newCachedThreadPool(), new Task(500), null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }
  }

  @Test
  public void testIsDone() throws InterruptedException {
    final Future<Boolean> future = ExecutorServices.invokeAll(Executors.newCachedThreadPool(), new Task(200), new Task(800));
    assertFalse(future.isDone());
    Thread.sleep(400);
    assertFalse(future.isDone());
    Thread.sleep(1200);
    assertTrue(future.isDone());
  }

  @Test
  public void testIsCancelled() throws InterruptedException {
    final Future<Boolean> future = ExecutorServices.invokeAll(Executors.newCachedThreadPool(), new Task(200), new Task(800));
    assertFalse(future.isCancelled());
    Thread.sleep(400);
    assertFalse(future.isCancelled());
    Thread.sleep(1200);
    assertFalse(future.isCancelled());
  }

  @Test
  public void testCancelCompleteNormally() throws InterruptedException {
    for (int i = 0; i < numTests; ++i) {
      final Future<Boolean> future = ExecutorServices.invokeAll(Executors.newCachedThreadPool(), new Task(100), new Task(400));
      assertFalse(future.isDone());
      Thread.sleep(500);
      assertTrue(future.isDone());
      assertFalse(future.cancel(false));
      assertFalse(future.cancel(true));
    }
  }

  @Test
  public void testGet() throws ExecutionException, InterruptedException {
    for (int i = 0; i < numTests; ++i) {
      final Future<Boolean> future = ExecutorServices.invokeAll(Executors.newCachedThreadPool(), new Task(100), new Task(400));
      assertFalse(future.isDone());
      assertTrue(future.get());
    }
  }

  @Test
  public void testGetTimeout() throws ExecutionException, InterruptedException {
    for (int i = 0; i < numTests; ++i) {
      final Future<Boolean> future = ExecutorServices.invokeAll(Executors.newCachedThreadPool(), new Task(100), new Task(1000));
      assertFalse(future.isDone());
      try {
        assertFalse(future.get(200, TimeUnit.MILLISECONDS));
        fail("Expected TimeoutException");
      }
      catch (final TimeoutException e) {
      }
    }
  }

  @Test
  public void testCancelComplete() throws ExecutionException, InterruptedException {
    for (int i = 0; i < numTests; ++i) {
      final Future<Boolean> future = ExecutorServices.invokeAll(Executors.newCachedThreadPool(), new Task(100), new Task(2000));
      assertFalse(future.isDone());
      Thread.sleep(400);
      assertFalse(future.isDone());
      assertTrue(future.cancel(true));
      assertTrue(future.isDone());
      assertFalse(future.get());
    }
  }

  private static final Random r = new Random();
  private static final int numInterruptTests = 100;

  private static void test(final int numTests, final BiObjBiLongConsumer<ExecutorService,CountDownLatch> consumer) throws InterruptedException {
    final CountDownLatch latch = new CountDownLatch(numTests);
    final long timeout = 200;
    final long sleepTime = timeout + (r.nextBoolean() ? 1 : -1) * 50;
    final ExecutorService executor = ExecutorServices.interruptAfterTimeout(Executors.newCachedThreadPool(), timeout, TimeUnit.MILLISECONDS);
    for (int i = 0; i < numTests; ++i)
      consumer.accept(executor, latch, sleepTime, timeout);

    latch.await();
  }

  private static <V>Callable<V> newCallable(final CountDownLatch latch, final long sleep, final long timeout) {
    final Runnable r = ThreadsTest.newRunnable(latch, sleep, timeout);
    return () -> {
      r.run();
      return null;
    };
  }

  private static <V>Collection<Callable<V>> newCallables(final CountDownLatch latch, final long sleep, final long timeout, final int len) {
    final ArrayList<Callable<V>> c = new ArrayList<>(len);
    for (int i = 0; i < len; ++i)
      c.add(newCallable(latch, sleep, timeout));

    return c;
  }

  @Test
  public void testInterruptAfterTimeoutExecute() throws InterruptedException {
    test(numInterruptTests, (e, l, s, t) -> e.execute(ThreadsTest.newRunnable(l, s, t)));
  }

  @Test
  public void testInterruptAfterTimeoutSubmit() throws InterruptedException {
    test(numInterruptTests, (e, l, s, t) -> e.submit(ThreadsTest.newRunnable(l, s, t)));
  }

  @Test
  public void testInterruptAfterTimeoutSubmitResult() throws InterruptedException {
    test(numInterruptTests, (e, l, s, t) -> e.submit(ThreadsTest.newRunnable(l, s, t), null));
  }

  @Test
  public void testInterruptAfterTimeoutSubmitCallable() throws InterruptedException {
    test(numInterruptTests, (e, l, s, t) -> e.submit(newCallable(l, s, t)));
  }

  @Test
  public void testInterruptAfterTimeoutInvokeAll() throws InterruptedException {
    test(20, (e, l, s, t) -> {
      try {
        e.invokeAll(newCallables(l, s, t, r.nextInt(20)));
      }
      catch (final InterruptedException x) {
        throw new RuntimeException(x);
      }
    });
  }

  @Test
  public void testInterruptAfterTimeoutInvokeAllTimeout() throws InterruptedException {
    test(20, (e, l, s, t) -> {
      try {
        e.invokeAll(newCallables(l, s, t, r.nextInt(20)), 100, TimeUnit.MILLISECONDS);
      }
      catch (final InterruptedException x) {
        throw new RuntimeException(x);
      }
    });
  }

  @Test
  public void testInterruptAfterTimeoutInvokeAny() throws InterruptedException {
    test(1, (e, l, s, t) -> {
      try {
        e.invokeAny(newCallables(l, s, t, r.nextInt(numInterruptTests)));
      }
      catch (final ExecutionException | InterruptedException x) {
        throw new RuntimeException(x);
      }
    });
  }

  @Test
  public void testInterruptAfterTimeoutInvokeAnyTimeout() throws InterruptedException {
    test(1, (e, l, s, t) -> {
      try {
        e.invokeAny(newCallables(l, s, t, r.nextInt(numInterruptTests)), 200, TimeUnit.MILLISECONDS);
      }
      catch (final TimeoutException x) {
      }
      catch (final ExecutionException | InterruptedException x) {
        throw new RuntimeException(x);
      }
    });
  }
}