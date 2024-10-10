/* Copyright (c) 2024 LibJ
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

import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * A {@link ScheduledExecutorService} that executes its submitted tasks synchronously in the current thread.
 */
public class SynchronousScheduledExecutorService extends SynchronousExecutorService implements ScheduledExecutorService {
  static class SynchronousScheduledFuture<T> extends SynchronousFuture<T> implements ScheduledFuture<T> {
    private final long delay;
    private final TimeUnit timeUnit;

    SynchronousScheduledFuture(final T result, final long delay, final TimeUnit timeUnit) {
      super(result);
      this.delay = delay;
      this.timeUnit = timeUnit;
    }

    @Override
    public int compareTo(final Delayed o) {
      int c = TimeUnits.compare(delay, timeUnit, o.getDelay(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
      if (c != 0)
        return c;

      c = TimeUnits.compare(delay, timeUnit, o.getDelay(TimeUnit.MICROSECONDS), TimeUnit.MICROSECONDS);
      if (c != 0)
        return c;

      return TimeUnits.compare(delay, timeUnit, o.getDelay(TimeUnit.NANOSECONDS), TimeUnit.NANOSECONDS);
    }

    @Override
    public long getDelay(final TimeUnit unit) {
      return unit.convert(delay, timeUnit);
    }
  }

  @Override
  public ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
    try {
      Thread.sleep(TimeUnit.MILLISECONDS.convert(delay, unit));
    }
    catch (final InterruptedException e) {
      throw new RuntimeException(e);
    }

    command.run();
    return new SynchronousScheduledFuture<>(null, delay, unit);
  }

  @Override
  public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit) {
    try {
      Thread.sleep(TimeUnit.MILLISECONDS.convert(delay, unit));
      return new SynchronousScheduledFuture<>(callable.call(), delay, unit);
    }
    catch (final Exception e) {
      if (e instanceof RuntimeException)
        throw (RuntimeException)e;

      throw new RuntimeException(e);
    }
  }

  @Override
  public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
    try {
      Thread.sleep(TimeUnit.MILLISECONDS.convert(initialDelay, unit));
      final long rateMillis = TimeUnit.MILLISECONDS.convert(period, unit);
      for (long ts = System.currentTimeMillis(), sleep;;) {
        command.run();
        sleep = rateMillis - (System.currentTimeMillis() - ts);
        ts = System.currentTimeMillis();
        if (sleep > 0)
          Thread.sleep(sleep);
      }
    }
    catch (final Exception e) {
      if (e instanceof RuntimeException)
        throw (RuntimeException)e;

      throw new RuntimeException(e);
    }
  }

  @Override
  public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
    try {
      Thread.sleep(TimeUnit.MILLISECONDS.convert(initialDelay, unit));
      final long delayMillis = TimeUnit.MILLISECONDS.convert(delay, unit);
      while (true) {
        Thread.sleep(delayMillis);
        command.run();
      }
    }
    catch (final Exception e) {
      if (e instanceof RuntimeException)
        throw (RuntimeException)e;

      throw new RuntimeException(e);
    }
  }
}