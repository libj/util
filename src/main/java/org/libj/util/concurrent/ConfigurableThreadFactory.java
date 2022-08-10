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

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import org.libj.util.function.TriObjLongFunction;

/**
 * A {@link ThreadFactory} that provides a constructor that allows configuration of parameters to be applied to new
 * {@linkplain Thread threads} returned by {@link #newThread(Runnable)}.
 */
public class ConfigurableThreadFactory implements ThreadFactory {
  private static final AtomicLong threadFactorySeq = new AtomicLong(1);
  private static final AtomicLong threadSeq = new AtomicLong(1);

  protected final String namePrefix;
  protected final long stackSize;
  protected final boolean daemon;
  protected final int priority;
  protected final ThreadGroup group;
  protected final UncaughtExceptionHandler handler;
  protected final TriObjLongFunction<ThreadGroup,Runnable,String,Thread> newThreadFunction;

  /**
   * Created a new {@link ConfigurableThreadFactory} with the provided arguments.
   *
   * @param group The {@link ThreadGroup} to be used for new threads.
   * @param namePrefix The name prefix to be used for new threads.
   * @param stackSize The stack size to be used for new threads.
   * @param daemon The daemon boolean to be used for new threads.
   * @param priority The priority to be used for new threads.
   * @param handler The handler to be invoked when a thread is abruptly terminated due to an uncaught exception.
   * @param newThreadFunction The function to be called for the creation of new {@link Thread}s.
   * @throws IllegalArgumentException If {@code stackSize} is negative, or if {@code priority} is outside the range of
   *           {@link Thread#MIN_PRIORITY} and {@link Thread#MAX_PRIORITY}.
   */
  @SuppressWarnings({"deprecation", "removal"})
  public ConfigurableThreadFactory(final ThreadGroup group, final String namePrefix, final long stackSize, final boolean daemon, final int priority, final UncaughtExceptionHandler handler, final TriObjLongFunction<ThreadGroup,Runnable,String,Thread> newThreadFunction) {
    final SecurityManager s;
    this.group = group != null ? group : (s = System.getSecurityManager()) != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
    this.namePrefix = (namePrefix != null ? namePrefix : getClass().getSimpleName()) + "-" + threadFactorySeq.getAndIncrement() + "-";
    this.stackSize = assertNotNegative(stackSize);
    this.daemon = daemon;
    this.priority = assertRangeMinMax(priority, Thread.MIN_PRIORITY, Thread.MAX_PRIORITY);
    this.handler = handler;
    this.newThreadFunction = newThreadFunction;
  }

  @Override
  public Thread newThread(final Runnable r) {
    final String name = namePrefix + threadSeq.getAndIncrement();
    final Thread thread = newThreadFunction != null ? newThreadFunction.apply(group, r, name, stackSize) : new Thread(group, r, name, stackSize);

    if (thread.isDaemon() != daemon)
      thread.setDaemon(daemon);

    if (group != null)
      thread.setUncaughtExceptionHandler(group);

    if (handler != null)
      thread.setUncaughtExceptionHandler(handler);

    if (thread.getPriority() != priority)
      thread.setPriority(priority);

    return thread;
  }
}