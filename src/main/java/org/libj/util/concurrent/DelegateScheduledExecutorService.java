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

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * A {@link DelegateScheduledExecutorService} contains some other {@link ScheduledExecutorService}, to which it delegates its method
 * calls, possibly transforming the data along the way or providing additional functionality. The class
 * {@link DelegateScheduledExecutorService} itself simply overrides all methods of {@link ScheduledExecutorService} with versions
 * that pass all requests to the target {@link ScheduledExecutorService}. Subclasses of {@link DelegateScheduledExecutorService} may
 * further override some of these methods and may also provide additional methods and fields.
 */
public class DelegateScheduledExecutorService extends DelegateExecutorService implements ScheduledExecutorService {
  /** The target {@link ScheduledExecutorService}. */
  protected final ScheduledExecutorService target;

  /**
   * Creates a new {@link DelegateScheduledExecutorService} with the specified target {@link ScheduledExecutorService}.
   *
   * @param target The target {@link ScheduledExecutorService}.
   * @throws NullPointerException If the target {@link ScheduledExecutorService} is null.
   */
  public DelegateScheduledExecutorService(final ScheduledExecutorService target) {
    // FIXME: Double null check.
    super(target);
    this.target = Objects.requireNonNull(target);
  }

  /**
   * Creates a new {@link DelegateScheduledExecutorService} with a null target.
   */
  protected DelegateScheduledExecutorService() {
    super();
    this.target = null;
  }

  @Override
  public ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
    return target.schedule(command, delay, unit);
  }

  @Override
  public <V>ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit) {
    return target.schedule(callable, delay, unit);
  }

  @Override
  public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
    return target.scheduleAtFixedRate(command, initialDelay, period, unit);
  }

  @Override
  public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
    return target.scheduleWithFixedDelay(command, initialDelay, delay, unit);
  }
}