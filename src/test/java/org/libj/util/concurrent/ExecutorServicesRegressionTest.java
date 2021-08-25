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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

public class ExecutorServicesRegressionTest {
  private static class Task implements Runnable {
    private final int runTime;

    private Task(final int runTime) {
      this.runTime = runTime;
    }

    @Override
    public void run() {
      try {
        Thread.sleep(runTime);
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
}