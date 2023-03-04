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
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A {@link DelegateExecutorService} contains some other {@link ExecutorService}, to which it delegates its method calls, possibly
 * transforming the data along the way or providing additional functionality. The class {@link DelegateExecutorService} itself
 * simply overrides all methods of {@link ExecutorService} with versions that pass all requests to the target
 * {@link ExecutorService}. Subclasses of {@link DelegateExecutorService} may further override some of these methods and may also
 * provide additional methods and fields.
 */
public class DelegateExecutorService implements ExecutorService, Shutdownable<List<Runnable>> {
  /** The target {@link ExecutorService}. */
  protected ExecutorService target;

  /**
   * Creates a new {@link DelegateExecutorService} with the specified target {@link ExecutorService}.
   *
   * @param target The target {@link ExecutorService}.
   * @throws NullPointerException If the target {@link ExecutorService} is null.
   */
  public DelegateExecutorService(final ExecutorService target) {
    this.target = Objects.requireNonNull(target);
  }

  /**
   * Creates a new {@link DelegateExecutorService} with a null target.
   */
  protected DelegateExecutorService() {
    this.target = null;
  }

  @Override
  public void execute(final Runnable command) {
    target.execute(command);
  }

  @Override
  public void shutdown() {
    target.shutdown();
  }

  @Override
  public List<Runnable> shutdownNow() {
    return target.shutdownNow();
  }

  @Override
  public boolean isShutdown() {
    return target.isShutdown();
  }

  @Override
  public boolean isTerminated() {
    return target.isTerminated();
  }

  @Override
  public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
    return target.awaitTermination(timeout, unit);
  }

  @Override
  public <T>Future<T> submit(final Callable<T> task) {
    return target.submit(task);
  }

  @Override
  public <T>Future<T> submit(final Runnable task, final T result) {
    return target.submit(task, result);
  }

  @Override
  public Future<?> submit(final Runnable task) {
    return target.submit(task);
  }

  @Override
  public <T>List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
    return target.invokeAll(tasks);
  }

  @Override
  public <T>List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException {
    return target.invokeAll(tasks, timeout, unit);
  }

  @Override
  public <T>T invokeAny(final Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
    return target.invokeAny(tasks);
  }

  @Override
  public <T>T invokeAny(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    return target.invokeAny(tasks, timeout, unit);
  }
}