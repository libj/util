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

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.libj.lang.Assertions;

/**
 * Utility functions for operations pertaining to {@link ExecutorService}.
 */
public final class ExecutorServices {
  /**
   * Executes the provided {@link Runnable} {@code tasks} in the given
   * {@link ExecutorService} {@code executor}, returning a {@link Future}
   * representing the aggregate status of the completion of all tasks.
   * {@link Future#isDone} is {@code true} when all {@code tasks} are completed.
   * Note that a <em>completed</em> task could have terminated either normally
   * or by throwing an exception. The results of this method are undefined if
   * the given collection is modified while this operation is in progress.
   *
   * @param executor The {@code ExecutorService} in which to invoke the provided
   *          {@code tasks}.
   * @param tasks The collection of {@link Runnable} tasks.
   * @return A {@link Future} representing the aggregate status of the
   *         completion of all tasks, whereby {@link Future#isDone} is
   *         {@code true} when all {@code tasks} are completed.
   * @throws IllegalArgumentException If {@code executor}, {@code tasks}, or any
   *           of member of {@code tasks} is null.
   * @throws RejectedExecutionException If any task cannot be scheduled for
   *           execution.
   */
  public static Future<Boolean> invokeAll(final ExecutorService executor, final Collection<? extends Runnable> tasks) {
    return invokeAll(executor, t -> t.run(), Assertions.assertNotEmpty(tasks).toArray(new Runnable[tasks.size()]));
  }

  /**
   * Executes the provided {@link Runnable} {@code tasks} in the given
   * {@link ExecutorService} {@code executor}, returning a {@link Future}
   * representing the aggregate status of the completion of all tasks.
   * {@link Future#isDone} is {@code true} when all {@code tasks} are completed.
   * Note that a <em>completed</em> task could have terminated either normally
   * or by throwing an exception. The results of this method are undefined if
   * the given collection is modified while this operation is in progress.
   *
   * @param executor The {@code ExecutorService} in which to invoke the provided
   *          {@code tasks}.
   * @param tasks The array of {@link Runnable} tasks.
   * @return A {@link Future} representing the aggregate status of the
   *         completion of all tasks, whereby {@link Future#isDone} is
   *         {@code true} when all {@code tasks} are completed.
   * @throws IllegalArgumentException If {@code executor}, {@code tasks}, or any
   *           of member of {@code tasks} is null.
   * @throws RejectedExecutionException If any task cannot be scheduled for
   *           execution.
   */
  public static Future<Boolean> invokeAll(final ExecutorService executor, final Runnable ... tasks) {
    return invokeAll(executor, t -> t.run(), Assertions.assertNotEmpty(tasks));
  }

  /**
   * Executes the provided generic {@code tasks} via the specified
   * {@link Consumer} {@code proxy} in the given {@link ExecutorService}
   * {@code executor}, returning a {@link Future} representing the aggregate
   * status of the completion of all tasks. {@link Future#isDone} is
   * {@code true} when all {@code tasks} are completed. Note that a
   * <em>completed</em> task could have terminated either normally or by
   * throwing an exception. The results of this method are undefined if the
   * given collection is modified while this operation is in progress.
   *
   * @param <T> The type parameter of the {@code tasks} to be invoked.
   * @param executor The {@code ExecutorService} in which to invoke the provided
   *          {@code tasks}.
   * @param proxy The {@link Consumer} to proxy the invocation call of each
   *          task.
   * @param tasks The array of generic tasks.
   * @return A {@link Future} representing the aggregate status of the
   *         completion of all tasks, whereby {@link Future#isDone} is
   *         {@code true} when all {@code tasks} are completed.
   * @throws IllegalArgumentException If {@code executor}, {@code tasks}, or any
   *           of member of {@code tasks} is null.
   * @throws RejectedExecutionException If any task cannot be scheduled for
   *           execution.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T>Future<Boolean> invokeAll(final ExecutorService executor, final Consumer<T> proxy, final Collection<? extends T> tasks) {
    return invokeAll(executor, (Consumer)proxy, Assertions.assertNotEmpty(tasks).toArray());
  }

  /**
   * Executes the provided generic {@code tasks} via the specified
   * {@link Consumer} {@code proxy} in the given {@link ExecutorService}
   * {@code executor}, returning a {@link Future} representing the aggregate
   * status of the completion of all tasks. {@link Future#isDone} is
   * {@code true} when all {@code tasks} are completed. Note that a
   * <em>completed</em> task could have terminated either normally or by
   * throwing an exception. The results of this method are undefined if the
   * given collection is modified while this operation is in progress.
   *
   * @param <T> The type parameter of the {@code tasks} to be invoked.
   * @param executor The {@code ExecutorService} in which to invoke the provided
   *          {@code tasks}.
   * @param proxy The {@link Consumer} to proxy the invocation call of each
   *          task.
   * @param tasks The array of generic tasks.
   * @return A {@link Future} representing the aggregate status of the
   *         completion of all tasks, whereby {@link Future#isDone} is
   *         {@code true} when all {@code tasks} are completed.
   * @throws IllegalArgumentException If {@code executor}, {@code proxy},
   *           {@code tasks}, or any of member of {@code tasks} is null.
   * @throws RejectedExecutionException If any task cannot be scheduled for
   *           execution.
   */
  @SafeVarargs
  public static <T>Future<Boolean> invokeAll(final ExecutorService executor, final Consumer<T> proxy, final T ... tasks) {
    Assertions.assertNotNull(executor);
    Assertions.assertNotNull(proxy);
    final AtomicBoolean started = new AtomicBoolean(false);
    final AtomicBoolean cancelled = new AtomicBoolean(false);
    final Thread[] threads = new Thread[tasks.length];
    final CountDownLatch latch = new CountDownLatch(tasks.length);
    for (int i = 0; i < tasks.length; ++i) {
      final T task = Assertions.assertNotNull(tasks[i]);
      final int index = i;
      executor.execute(() -> {
        if (!started.getAndSet(true)) {
          synchronized (started) {
            started.notifyAll();
          }
        }

        if (cancelled.get())
          return;

        threads[index] = Thread.currentThread();

        try {
          proxy.accept(task);
        }
        finally {
          latch.countDown();
        }
      });
    }

    return new Future<Boolean>() {
      private volatile boolean done;

      @Override
      public boolean cancel(final boolean mayInterruptIfRunning) {
        if (done)
          return false;

        done = true;
        cancelled.set(true);
        if (mayInterruptIfRunning && started.get())
          for (final Thread thread : threads)
            if (thread != null)
              thread.interrupt();

        return true;
      }

      @Override
      public boolean isCancelled() {
        return cancelled.get();
      }

      @Override
      public boolean isDone() {
        if (done)
          return true;

        if (latch.getCount() > 0)
          return false;

        return done = true;
      }

      @Override
      public Boolean get() throws InterruptedException {
        try {
          return await(0, null);
        }
        catch (final TimeoutException e) {
          throw new RuntimeException(e);
        }
      }

      @Override
      public Boolean get(final long timeout, final TimeUnit unit) throws InterruptedException, TimeoutException {
        return await(timeout, Assertions.assertNotNull(unit));
      }

      private Boolean await(final long timeout, final TimeUnit unit) throws InterruptedException, TimeoutException {
        if (cancelled.get())
          return false;

        if (!started.get()) {
          synchronized (started) {
            if (unit != null)
              started.wait(unit.toMillis(timeout));
            else
              started.wait();
          }
        }

        try {
          if (unit == null)
            latch.await();
          else if (!latch.await(timeout, unit))
            throw new TimeoutException();

          return Boolean.TRUE;
        }
        finally {
          done = true;
        }
      }
    };
  }

  private ExecutorServices() {
  }
}