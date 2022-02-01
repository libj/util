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

import static org.libj.lang.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

import org.libj.lang.Threads;

/**
 * Utility functions for operations pertaining to {@link ExecutorService}.
 */
public final class ExecutorServices {
  static class InterruptExecutorService extends DelegateExecutorService {
    final long timeout;
    final TimeUnit unit;

    InterruptExecutorService(final ExecutorService target, final long timeout, final TimeUnit unit) {
      super(target);
      this.timeout = timeout;
      this.unit = unit;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If {@code task} is null.
     */
    @Override
    public void execute(final Runnable command) {
      super.execute(Threads.interruptAfterTimeout(command, timeout, unit));
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If {@code task} is null.
     */
    @Override
    public <T>Future<T> submit(final Callable<T> task) {
      return super.submit(Threads.interruptAfterTimeout(task, timeout, unit));
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If {@code task} is null.
     */
    @Override
    public <T>Future<T> submit(final Runnable task, final T result) {
      return super.submit(Threads.interruptAfterTimeout(task, timeout, unit), result);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If {@code task} is null.
     */
    @Override
    public Future<?> submit(final Runnable task) {
      return super.submit(Threads.interruptAfterTimeout(task, timeout, unit));
    }

    private <T>List<Callable<T>> interruptAfterTimeout(final Collection<? extends Callable<T>> tasks) {
      final List<Callable<T>> interruptableTasks = new ArrayList<>(assertNotNull(tasks).size());
      final Iterator<? extends Callable<T>> iterator = tasks.iterator();
      while (iterator.hasNext())
        interruptableTasks.add(Threads.interruptAfterTimeout(assertNotNull(iterator.next()), timeout, unit));

      return interruptableTasks;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If {@code tasks} or any member of
     *           {@code tasks} is null.
     */
    @Override
    public <T>List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
      return super.invokeAll(interruptAfterTimeout(tasks));
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If {@code tasks} or any member of
     *           {@code tasks} is null.
     */
    @Override
    public <T>List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException {
      return super.invokeAll(interruptAfterTimeout(tasks), timeout, unit);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If {@code tasks} or any member of
     *           {@code tasks} is null.
     */
    @Override
    public <T>T invokeAny(final Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
      return super.invokeAny(interruptAfterTimeout(tasks));
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If {@code tasks} or any member of
     *           {@code tasks} is null.
     */
    @Override
    public <T>T invokeAny(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
      return super.invokeAny(interruptAfterTimeout(tasks), timeout, unit);
    }
  }

  static class InterruptScheduledExecutorService extends InterruptExecutorService implements ScheduledExecutorService {
    private final ScheduledExecutorService target;

    InterruptScheduledExecutorService(final ScheduledExecutorService target, final long timeout, final TimeUnit unit) {
      super(target, timeout, unit);
      this.target = target;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If {@code command} is null.
     */
    @Override
    public ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
      return target.schedule(Threads.interruptAfterTimeout(command, this.timeout, this.unit), delay, unit);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If {@code callable} is null.
     */
    @Override
    public <V>ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit) {
      return target.schedule(Threads.interruptAfterTimeout(callable, this.timeout, this.unit), delay, unit);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If {@code command} is null.
     */
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
      return target.scheduleAtFixedRate(Threads.interruptAfterTimeout(command, this.timeout, this.unit), initialDelay, period, unit);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If {@code command} is null.
     */
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
      return target.scheduleWithFixedDelay(Threads.interruptAfterTimeout(command, this.timeout, this.unit), initialDelay, delay, unit);
    }
  }

  /**
   * Returns a new {@link ExecutorService} instance that wraps the provided
   * {@code executor}, and is configured to schedule all executed or submitted
   * {@link Runnable} or {@link Callable} tasks to be
   * {@linkplain Thread#interrupt() interrupted} once the provided
   * {@code timeout} of {@link TimeUnit unit} expires.
   *
   * @param executor The {@link ExecutorService} to be wrapped.
   * @param timeout The maximum time to wait.
   * @param unit The {@link TimeUnit} of the {@code timeout} argument.
   * @return A new {@link ExecutorService} instance that wraps the provided
   *         {@code executor}, and is configured to schedule all executed or
   *         submitted {@link Runnable} or {@link Callable} tasks to be
   *         {@linkplain Thread#interrupt() interrupted} once the provided
   *         {@code timeout} of {@link TimeUnit unit} expires.
   * @throws IllegalArgumentException If {@code executor} or {@code unit} is
   *           null, or if {@code timeout} is negative.
   */
  public static ExecutorService interruptAfterTimeout(final ExecutorService executor, final long timeout, final TimeUnit unit) {
    return new InterruptExecutorService(assertNotNull(executor), assertNotNegative(timeout), assertNotNull(unit));
  }

  /**
   * Returns a new {@link ScheduledExecutorService} instance that wraps the
   * provided {@code executor}, and is configured to schedule all executed or
   * submitted {@link Runnable} or {@link Callable} tasks to be
   * {@linkplain Thread#interrupt() interrupted} once the provided
   * {@code timeout} of {@link TimeUnit unit} expires.
   *
   * @param executor The {@link ScheduledExecutorService} to be wrapped.
   * @param timeout The maximum time to wait.
   * @param unit The {@link TimeUnit} of the {@code timeout} argument.
   * @return A new {@link ScheduledExecutorService} instance that wraps the
   *         provided {@code executor}, and is configured to schedule all
   *         executed or submitted {@link Runnable} or {@link Callable} tasks to
   *         be {@linkplain Thread#interrupt() interrupted} once the provided
   *         {@code timeout} of {@link TimeUnit unit} expires.
   * @throws IllegalArgumentException If {@code executor} or {@code unit} is
   *           null, or if {@code timeout} is negative.
   */
  public static ScheduledExecutorService interruptAfterTimeout(final ScheduledExecutorService executor, final long timeout, final TimeUnit unit) {
    return new InterruptScheduledExecutorService(assertNotNull(executor), assertNotNegative(timeout), assertNotNull(unit));
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
   * @param executor The {@link ExecutorService} in which to invoke the provided
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
    return invokeAll(executor, Runnable::run, assertNotNull(tasks).toArray(new Runnable[tasks.size()]));
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
   * @param executor The {@link ExecutorService} in which to invoke the provided
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
    return invokeAll(executor, Runnable::run, assertNotNull(tasks));
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
   * @param executor The {@link ExecutorService} in which to invoke the provided
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
    return invokeAll(executor, (Consumer)proxy, assertNotNull(tasks).toArray());
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
   * @param executor The {@link ExecutorService} in which to invoke the provided
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
    assertNotNull(executor);
    assertNotNull(proxy);
    assertNotNull(tasks);
    final AtomicBoolean started = new AtomicBoolean(false);
    final AtomicBoolean canceled = new AtomicBoolean(false);
    final Thread[] threads = new Thread[tasks.length];
    final CountDownLatch latch = new CountDownLatch(tasks.length);
    for (int i = 0; i < tasks.length; ++i) {
      final T task = assertNotNull(tasks[i]);
      final int index = i;
      executor.execute(() -> {
        if (!started.getAndSet(true)) {
          synchronized (started) {
            started.notifyAll();
          }
        }

        if (canceled.get())
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
        canceled.set(true);
        if (mayInterruptIfRunning && started.get())
          for (final Thread thread : threads)
            if (thread != null)
              thread.interrupt();

        return true;
      }

      @Override
      public boolean isCancelled() {
        return canceled.get();
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
        return await(timeout, assertNotNull(unit));
      }

      private Boolean await(final long timeout, final TimeUnit unit) throws InterruptedException, TimeoutException {
        if (canceled.get())
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

  /**
   * Executes the provided generic {@code tasks} via the specified
   * {@link Consumer} {@code proxy} in the given {@link ExecutorService}
   * {@code executor}, returning a list of {@link Future}s holding their status
   * and results when all complete. {@link Future#isDone} is {@code true} for
   * each element of the returned list. Note that a <em>completed</em> task
   * could have terminated either normally or by throwing an exception. The
   * results of this method are undefined if the given collection is modified
   * while this operation is in progress.
   *
   * @param tasks the collection of tasks
   * @param <T> The type of the tasks.
   * @param <R> The type of the values returned from the tasks.
   * @param executor The {@link ExecutorService} in which to invoke the provided
   *          {@code tasks}.
   * @param proxy The {@link Function} to proxy the invocation call of each
   *          task.
   * @return A list of {@link Future}s representing the tasks, in the same
   *         sequential order as produced by the iterator for the given task
   *         list, each of which has completed.
   * @throws InterruptedException If interrupted while waiting, in which case
   *           unfinished tasks are cancelled.
   * @throws IllegalArgumentException If {@code tasks} or any member of
   *           {@code tasks} is null.
   * @throws RejectedExecutionException If any task cannot be scheduled for
   *           execution.
   */
  @SafeVarargs
  @SuppressWarnings("unchecked")
  public static <T,R>List<Future<R>> invokeAll(final ExecutorService executor, final Function<T,R> proxy, final T ... tasks) throws InterruptedException {
    final Callable<R>[] callables = new Callable[assertNotNull(tasks).length];
    for (int i = 0; i < tasks.length; ++i) {
      final T task = assertNotNull(tasks[i]);
      callables[i] = () -> proxy.apply(task);
    }

    return executor.invokeAll(Arrays.asList(callables));
  }

  /**
   * Executes the provided generic {@code tasks} via the specified
   * {@link Consumer} {@code proxy} in the given {@link ExecutorService}
   * {@code executor}, returning a list of {@link Future}s holding their status
   * and results when all complete. {@link Future#isDone} is {@code true} for
   * each element of the returned list. Note that a <em>completed</em> task
   * could have terminated either normally or by throwing an exception. The
   * results of this method are undefined if the given collection is modified
   * while this operation is in progress.
   *
   * @param tasks the collection of tasks
   * @param <T> The type of the tasks.
   * @param <R> The type of the values returned from the tasks.
   * @param executor The {@link ExecutorService} in which to invoke the provided
   *          {@code tasks}.
   * @param proxy The {@link Function} to proxy the invocation call of each
   *          task.
   * @return A list of {@link Future}s representing the tasks, in the same
   *         sequential order as produced by the iterator for the given task
   *         list, each of which has completed.
   * @throws InterruptedException If interrupted while waiting, in which case
   *           unfinished tasks are cancelled.
   * @throws IllegalArgumentException If {@code tasks} or any member of
   *           {@code tasks} is null.
   * @throws RejectedExecutionException If any task cannot be scheduled for
   *           execution.
   */
  @SuppressWarnings("unchecked")
  public static <T,R>List<Future<R>> invokeAll(final ExecutorService executor, final Function<T,R> proxy, final Collection<T> tasks) throws InterruptedException {
    final Callable<R>[] callables = new Callable[assertNotNull(tasks).size()];
    final Iterator<T> iterator = tasks.iterator();
    for (int i = 0; iterator.hasNext(); ++i) {
      final T task = assertNotNull(iterator.next());
      callables[i] = () -> proxy.apply(task);
    }

    return executor.invokeAll(Arrays.asList(callables));
  }

  private ExecutorServices() {
  }
}