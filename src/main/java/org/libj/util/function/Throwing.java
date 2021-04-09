/* Copyright (c) 2018 LibJ
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

package org.libj.util.function;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Utility that allows lambda expressions to propagate checked exceptions up the
 * expression's call stack.
 */
public final class Throwing {
  /**
   * Rethrows the checked exception from the specified {@link ThrowingRunnable}.
   * <p>
   * An example of this pattern:
   *
   * <pre>
   * {@code
   * Runnable runnable = Throwing.rethrow(() -> {
   *   if (true)
   *     throw new IOException();
   * });
   * runnable.run();
   * }
   * </pre>
   *
   * @param runnable The {@link ThrowingRunnable}.
   * @return The specified {@link Runnable} instance.
   */
  public static Runnable rethrow(final ThrowingRunnable<?> runnable) {
    return runnable;
  }

  /**
   * Rethrows the checked exception from the specified {@link ThrowingSupplier}.
   * <p>
   * An example of this pattern:
   *
   * <pre>
   * {@code
   * Supplier<String> supplier = Throwing.rethrow(() -> {
   *   if (true)
   *     throw new IOException();
   *
   *   return "hello world";
   * });
   * }
   * supplier.get();
   * </pre>
   *
   * @param <T> The type of results supplied by this supplier.
   * @param supplier The {@link ThrowingSupplier}.
   * @return The specified {@link Supplier} instance.
   */
  public static <T>Supplier<T> rethrow(final ThrowingSupplier<T,?> supplier) {
    return supplier;
  }

  /**
   * Rethrows the checked exception from the specified {@link ThrowingConsumer}.
   * <p>
   * An example of this pattern:
   *
   * <pre>
   * {@code
   * Arrays.asList(2, 1, 0).forEach(Throwing.rethrow(i -> {
   *   if (i == 0)
   *     throw new IllegalArgumentException("i=" + i);
   * }));
   * }
   * </pre>
   *
   * @param <T> The type of the input to the consumer's operation.
   * @param consumer The {@link ThrowingConsumer}.
   * @return The specified {@link Consumer} instance.
   */
  public static <T>Consumer<T> rethrow(final ThrowingConsumer<T,?> consumer) {
    return consumer;
  }

  /**
   * Rethrows the checked exception from the specified
   * {@link ThrowingBiConsumer}.
   * <p>
   * An example of this pattern:
   *
   * <pre>
   * {@code
   * BiConsumer<Integer,Integer> consumer = Throwing.<Integer,Integer>rethrow((i, j) -> {
   *   if (i == 0)
   *     throw new IllegalArgumentException("i=" + i);
   * });
   * for (int i = 3; i >= 0; --i)
   *   consumer.accept(i, -i);
   * }
   * </pre>
   *
   * @param <T> The type of the first input to the consumer's operation.
   * @param <U> The type of the second input to the consumer's operation.
   * @param consumer The {@link ThrowingBiConsumer}.
   * @return The specified {@link BiConsumer} instance.
   */
  public static <T,U>BiConsumer<T,U> rethrow(final ThrowingBiConsumer<T,U,?> consumer) {
    return consumer;
  }

  /**
   * Rethrows the checked exception from the specified
   * {@link ThrowingBiConsumer}.
   * <p>
   * An example of this pattern:
   *
   * <pre>
   * {@code
   * ObjIntConsumer<String> consumer = Throwing.<Integer,Integer>rethrow((s,i) -> {
   *   if (i == 0)
   *     throw new IllegalArgumentException("i=" + i);
   * });
   * for (int i = 3; i >= 0; --i)
   *   consumer.accept(i, -i);
   * }
   * </pre>
   *
   * @param <T> The type of the first input to the consumer's operation.
   * @param consumer The {@link ThrowingBiConsumer}.
   * @return The specified {@link BiConsumer} instance.
   */
  public static <T>ObjIntConsumer<T> rethrow(final ThrowingObjIntConsumer<T,?> consumer) {
    return consumer;
  }

  /**
   * Rethrows the checked exception from the specified
   * {@link ThrowingTriConsumer}.
   * <p>
   * An example of this pattern:
   *
   * <pre>
   * {@code
   * TriConsumer<Integer,Integer,Integer> consumer = Throwing.<Integer,Integer,Integer>rethrow((i, j, k) -> {
   *   if (i == 0)
   *     throw new IllegalArgumentException("i=" + i);
   * });
   * for (int i = 3; i >= 0; --i)
   *   consumer.accept(i, -i, i);
   * }
   * </pre>
   *
   * @param <T> The type of the first input to the consumer's operation.
   * @param <U> The type of the second input to the consumer's operation.
   * @param <V> The type of the third input to the consumer's operation.
   * @param consumer The {@link ThrowingTriConsumer}.
   * @return The specified {@link TriConsumer} instance.
   */
  public static <T,U,V>TriConsumer<T,U,V> rethrow(final ThrowingTriConsumer<T,U,V,?> consumer) {
    return consumer;
  }

  /**
   * Rethrows the checked exception from the specified {@link ThrowingConsumer}.
   * <p>
   * An example of this pattern:
   *
   * <pre>
   * {@code
   * Arrays
   *   .asList(2, 1, 0)
   *   .stream()
   *   .filter(Throwing.<Integer>rethrow(i -> {
   *     if (i == 0)
   *       throw new IOException("i=" + i);
   *     return false;
   *   }))
   *   .collect(Collectors.toList());
   * }
   * </pre>
   *
   * @param <T> The type of the input to the consumer's operation.
   * @param predicate The {@link ThrowingConsumer}.
   * @return The specified {@link Consumer} instance.
   */
  public static <T>Predicate<T> rethrow(final ThrowingPredicate<T,?> predicate) {
    return predicate;
  }

  /**
   * Rethrows the checked exception from the specified
   * {@link ThrowingBiPredicate}.
   * <p>
   * An example of this pattern:
   *
   * <pre>
   * {@code
   * BiPredicate<Integer,Integer> predicate = Throwing.<Integer,Integer>rethrow((i, j) -> {
   *   if (i == 0)
   *     throw new IllegalArgumentException("i=" + i);
   *   return false;
   * });
   * for (int i = 3; i >= 0; --i)
   *   predicate.accept(i, -i);
   * }
   * </pre>
   *
   * @param <T> The type of the first input to the predicate's operation.
   * @param <U> The type of the second input to the predicate's operation.
   * @param predicate The {@link ThrowingBiPredicate}.
   * @return The specified {@link BiPredicate} instance.
   */
  public static <T,U>BiPredicate<T,U> rethrow(final ThrowingBiPredicate<T,U,?> predicate) {
    return predicate;
  }

  /**
   * Rethrows the checked exception from the specified {@link ThrowingFunction}.
   * <p>
   * An example of this pattern:
   *
   * <pre>
   * {@code
   * Arrays
   *   .asList(2, 1, 0)
   *   .stream()
   *   .map(Throwing.rethrow((Integer i) -> {
   *     if (i == 0)
   *       throw new IOException("i=" + i);
   *     return String.valueOf(i);
   *   }))
   *   .forEach(f -> {});
   * }
   * </pre>
   *
   * @param <T> The type of the input to the function's operation.
   * @param <R> The type of the result of the function's function.
   * @param function The {@link ThrowingFunction}.
   * @return The specified {@link Function} instance.
   */
  public static <T,R>Function<T,R> rethrow(final ThrowingFunction<T,R,?> function) {
    return function;
  }

  /**
   * Rethrows the checked exception from the specified
   * {@link ThrowingBiFunction}.
   * <p>
   * An example of this pattern:
   *
   * <pre>
   * {@code
   * BiFunction<Integer,Integer,String> function = Throwing.<Integer,Integer,String>rethrow((i, j) -> {
   *   if (i == 0)
   *     throw new IllegalArgumentException("i=" + i);
   *   return String.valueOf(i);
   * });
   * for (int i = 3; i >= 0; --i)
   *   function.accept(i, -i);
   * }
   * </pre>
   *
   * @param <T> The type of the first input to the function's operation.
   * @param <U> The type of the second input to the function's operation.
   * @param <R> The type of the result of the function's function.
   * @param function The {@link ThrowingFunction}.
   * @return The specified {@link Function} instance.
   */
  public static <T,U,R>BiFunction<T,U,R> rethrow(final ThrowingBiFunction<T,U,R,?> function) {
    return function;
  }

  /**
   * Rethrows the specified throwable. This method tricks the compiler to
   * allow checked exceptions to be thrown from lambda expressions.
   *
   * @param <T> The type of the {@link Throwable}.
   * @param t The {@link Throwable} instance to rethrow.
   * @throws T The {@link Throwable} instance.
   */
  @SuppressWarnings("unchecked")
  public static <T extends Throwable>void rethrow(final Throwable t) throws T {
    throw (T)t;
  }

  private Throwing() {
  }
}