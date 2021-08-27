package org.libj.util.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.libj.lang.Assertions;

public final class Shutdownables {
  public static boolean awaitTermination(final long timeout, final TimeUnit unit, final Shutdownable ... shutdownables) throws InterruptedException {
    Assertions.assertNotNull(unit);
    Assertions.assertNotEmpty(shutdownables);
    final List<Callable<Boolean>> callables = new ArrayList<>(shutdownables.length);
    final AtomicReference<InterruptedException> ie = new AtomicReference<>();
    for (final Shutdownable shutdownable : shutdownables) {
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
    final List<Future<Boolean>> futures = shutdownExecutor.invokeAll(callables);
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

        return false;
      }
      catch (final ExecutionException e) {
        if (ee.get() == null)
          ee.set(e);
        else
          ee.get().addSuppressed(e);

        return false;
      }
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