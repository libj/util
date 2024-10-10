package org.libj.util.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.libj.util.IdentityHashSet;

public class SynchronousExecutorService implements ExecutorService {
  static class SynchronousFuture<T> implements Future<T> {
    private final T result;

    SynchronousFuture(final T result) {
      super();
      this.result = result;
    }

    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
      return false;
    }

    @Override
    public boolean isCancelled() {
      return false;
    }

    @Override
    public boolean isDone() {
      return true;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
      return result;
    }

    @Override
    public T get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
      return result;
    }
  }

  private final IdentityHashSet<Object> tasks = new IdentityHashSet<>();
  private boolean closed = false;

  @Override
  public void execute(final Runnable command) {
    if (closed)
      return;

    tasks.add(command);
    try {
      command.run();
    }
    finally {
      tasks.remove(command);
    }
  }

  @Override
  public void shutdown() {
    closed = true;
  }

  @Override
  public List<Runnable> shutdownNow() {
    closed = true;
    return Collections.EMPTY_LIST;
  }

  @Override
  public boolean isShutdown() {
    return closed;
  }

  @Override
  public boolean isTerminated() {
    return tasks.size() == 0;
  }

  @Override
  public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
    return false;
  }

  @Override
  public <T> Future<T> submit(final Callable<T> task) {
    if (closed)
      return null;

    tasks.add(task);
    try {
      return new SynchronousFuture<>(task.call());
    }
    catch (final Exception e) {
      if (e instanceof RuntimeException)
        throw (RuntimeException)e;

      throw new RuntimeException(e);
    }
    finally {
      tasks.remove(task);
    }
  }

  @Override
  public <T> Future<T> submit(final Runnable task, final T result) {
    if (closed)
      return null;

    tasks.add(task);
    try {
      task.run();
      return new SynchronousFuture<>(result);
    }
    finally {
      tasks.remove(task);
    }
  }

  @Override
  public Future<?> submit(final Runnable task) {
    if (closed)
      return null;

    tasks.add(task);
    try {
      task.run();
      return new SynchronousFuture<>(null);
    }
    finally {
      tasks.remove(task);
    }
  }

  @Override
  public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
    if (closed)
      return null;

    final ArrayList<Future<T>> futures = new ArrayList<>();
    for (final Callable<T> task : tasks)
      futures.add(submit(task));

    return futures;
  }

  @Override
  public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T> T invokeAny(final Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
    if (closed)
      return null;

    if (tasks.size() == 0)
      return null;

    final Callable<T> task = tasks.iterator().next();
    this.tasks.add(task);
    try {
      return task.call();
    }
    catch (final Exception e) {
      if (e instanceof RuntimeException)
        throw (RuntimeException)e;

      throw new RuntimeException(e);
    }
    finally {
      this.tasks.remove(task);
    }
  }

  @Override
  public <T> T invokeAny(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    throw new UnsupportedOperationException();
  }
}