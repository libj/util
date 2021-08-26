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

import java.util.concurrent.ThreadFactory;

import org.libj.lang.Assertions;

/**
 * A builder that produces {@link ThreadFactory} instances given provided
 * options.
 */
public class ThreadFactoryBuilder {
  private ThreadGroup group;

  /**
   * Sets the {@link ThreadGroup} to be provided to the {@link ThreadFactory}
   * produced by {@link #build()}, which is thereafter provided to the
   * {@link Thread} instances produced by
   * {@link ThreadFactory#newThread(Runnable)}.
   *
   * @param group The {@link ThreadGroup}.
   * @return {@code this} {@link ThreadFactoryBuilder}.
   */
  public ThreadFactoryBuilder withThreadGroup(final ThreadGroup group) {
    this.group = group;
    return this;
  }

  private String namePrefix;

  /**
   * Sets the name prefix to be provided to the {@link ThreadFactory} produced
   * by {@link #build()}, which is thereafter provided to the {@link Thread}
   * instances produced by {@link ThreadFactory#newThread(Runnable)}.
   *
   * @param namePrefix The name prefix.
   * @return {@code this} {@link ThreadFactoryBuilder}.
   */
  public ThreadFactoryBuilder withNamePrefix(final String namePrefix) {
    this.namePrefix = namePrefix;
    return this;
  }

  private int stackSize;

  /**
   * Sets the stack size to be provided to the {@link ThreadFactory}
   * produced by {@link #build()}, which is thereafter provided to the
   * {@link Thread} instances produced by
   * {@link ThreadFactory#newThread(Runnable)}.
   *
   * @param stackSize The stack size.
   * @return {@code this} {@link ThreadFactoryBuilder}.
   * @throws IllegalArgumentException If {@code stackSize} is negative.
   */
  public ThreadFactoryBuilder withStackSize(final int stackSize) {
    this.stackSize = Assertions.assertNotNegative(stackSize);
    return this;
  }

  private boolean daemon;

  /**
   * Sets the daemon boolean to be provided to the {@link ThreadFactory}
   * produced by {@link #build()}, which is thereafter provided to the
   * {@link Thread} instances produced by
   * {@link ThreadFactory#newThread(Runnable)}.
   *
   * @param daemon The daemon boolean.
   * @return {@code this} {@link ThreadFactoryBuilder}.
   */
  public ThreadFactoryBuilder setDaemon(final boolean daemon) {
    this.daemon = daemon;
    return this;
  }

  private int priority = Thread.NORM_PRIORITY;

  /**
   * Sets the priority to be provided to the {@link ThreadFactory} produced by
   * {@link #build()}, which is thereafter provided to the {@link Thread}
   * instances produced by {@link ThreadFactory#newThread(Runnable)}.
   *
   * @param priority The priority.
   * @return {@code this} {@link ThreadFactoryBuilder}.
   * @throws IllegalArgumentException If {@code priority} is outside the range
   *           of {@link Thread#MIN_PRIORITY} and {@link Thread#MAX_PRIORITY}.
   */
  public ThreadFactoryBuilder withPriority(final int priority) {
    this.priority = Assertions.assertRangeMinMax(priority, Thread.MIN_PRIORITY, Thread.MAX_PRIORITY);
    return this;
  }

  /**
   * Returns a new {@link ThreadFactory} given the provided options.
   *
   * @return A new {@link ThreadFactory} given the provided options.
   */
  public ThreadFactory build() {
    return new ConfigurableThreadFactory(group, namePrefix, stackSize, daemon, priority);
  }
}