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
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Utility functions for operations pertaining to {@link Shutdownable}.
 */
public final class Shutdownables {
  /**
   * Blocks until all provided {@link Shutdownable shutdownables} have {@linkplain Shutdownable#awaitTermination(long,TimeUnit)
   * terminated} after a shutdown request, or the timeout occurs, or the current thread is interrupted, whichever happens first.
   *
   * @implNote
   * @param timeout The maximum time to wait.
   * @param unit The time unit of the timeout argument.
   * @param shutdownables The {@link Shutdownable}s on which to {@linkplain Shutdownable#awaitTermination(long,TimeUnit) await
   *          termination}.
   * @return {@code true} if all provided {@link Shutdownable shutdownables} have successfully
   *         {@linkplain Shutdownable#awaitTermination(long,TimeUnit) terminated}, and {@code false} if any of the provided
   *         {@link Shutdownable shutdownables} have failed to {@linkplain Shutdownable#awaitTermination(long,TimeUnit) terminate}
   *         or the timeout elapsed before termination.
   * @throws InterruptedException If the current thread is interrupted while waiting, an {@link InterruptedException} is raised
   *           immediately. If any thread of {@code shutdownables} is interrupted while waiting, an {@link InterruptedException} is
   *           raised after the call to {@link Shutdownable#awaitTermination(long,TimeUnit)} of all {@code shutdownables} has
   *           returned. If multiple {@link InterruptedException}s occur, they are added as
   *           {@linkplain Throwable#addSuppressed(Throwable) suppressed} exceptions on the {@link InterruptedException} to be
   *           thrown.
   * @throws IllegalArgumentException If {@code unit} or {@code shutdownables} is null, or if any member of {@code shutdownables} is
   *           null.
   */
  public static boolean awaitTermination(final long timeout, final TimeUnit unit, final Shutdownable<?> ... shutdownables) throws InterruptedException {
    assertNotNull(unit);
    assertNotEmpty(shutdownables);
    final List<Callable<Boolean>> callables = new ArrayList<>(shutdownables.length);
    final AtomicReference<InterruptedException> ie = new AtomicReference<>();
    for (final Shutdownable<?> shutdownable : shutdownables) {
      assertNotNull(shutdownable);
      callables.add(() -> {
        try {
          return shutdownable.awaitTermination(timeout, unit);
        }
        catch (final InterruptedException e) {
          if (ie.get() == null)
            ie.set(e);
          else
            ie.get().addSuppressed(e);

          return false;
        }
      });
    }

    final ExecutorService shutdownExecutor = Executors.newCachedThreadPool(new ThreadFactoryBuilder().withNamePrefix("awaitTermination").build());
    final List<Future<Boolean>> futures = shutdownExecutor.invokeAll(callables, timeout, unit);
    shutdownExecutor.shutdown();
    final AtomicReference<ExecutionException> ee = new AtomicReference<>();
    final boolean success = futures.stream().allMatch(f -> {
      try {
        return f.get();
      }
      catch (final InterruptedException e) {
        if (ie.get() == null)
          ie.set(e);
        else
          ie.get().addSuppressed(e);
      }
      catch (final ExecutionException e) {
        if (ee.get() == null)
          ee.set(e);
        else
          ee.get().addSuppressed(e);
      }

      return false;
    });

    if (ee.get() != null)
      throw new RuntimeException(ee.get());

    if (ie.get() != null)
      throw ie.get();

    return success;
  }

  private Shutdownables() {
  }
}