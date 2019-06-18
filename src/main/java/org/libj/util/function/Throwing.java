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
import java.util.function.Predicate;

/**
 * Utility that allows lambda expressions to propagate checked exceptions up the
 * expression's call stack.
 */
public final class Throwing {
  /**
   * Rethrows the checked exception from the specified {@code ThrowingRunnable}.
   * <p>
   * An example of this pattern:
   * <blockquote><pre>
   * Arrays
   *   .asList(1, 2, 3)
   *   .forEach(Throwing.rethrow(() -&gt; {
   *      if (i == 3)
   *        throw new IOException();
   *    }));
   * </pre></blockquote>
   *
   * @param runnable The {@code ThrowingRunnable}.
   * @return The specified {@code Runnable} instance.
   */
  public static Runnable rethrow(final ThrowingRunnable runnable) {
    return runnable;
  }

  /**
   * Rethrows the checked exception from the specified {@code ThrowingConsumer}.
   * <p>
   * An example of this pattern:
   * <blockquote><pre>
   * Arrays
   *   .asList(2, 1, 0)
   *   .forEach(Throwing.rethrow(i -&gt; {
   *     if (i == 0)
   *       throw new IllegalArgumentException("i=" + i);
   *   }));
   * </pre></blockquote>
   * @param <T> The type of the input to the consumer's operation.
   * @param consumer The {@code ThrowingConsumer}.
   * @return The specified {@code Consumer} instance.
   */
  public static <T>Consumer<T> rethrow(final ThrowingConsumer<T> consumer) {
    return consumer;
  }

  /**
   * Rethrows the checked exception from the specified
   * {@code ThrowingBiConsumer}.
   * <p>
   * An example of this pattern:
   * <blockquote><pre>
   * BiConsumer&lt;Integer,Integer&gt; consumer = Throwing.&lt;Integer,Integer&gt;rethrow((i, j) -&gt; {
   *   if (i == 0)
   *     throw new IllegalArgumentException("i=" + i);
   * });
   * for (int i = 3; i &gt;= 0; --i)
   *   consumer.accept(i, -i);
   * </pre></blockquote>
   * @param <T> The type of the first input to the consumer's operation.
   * @param <U> The type of the second input to the consumer's operation.
   * @param consumer The {@code ThrowingBiConsumer}.
   * @return The specified {@code BiConsumer} instance.
   */
  public static <T,U>BiConsumer<T,U> rethrow(final ThrowingBiConsumer<T,U> consumer) {
    return consumer;
  }

  /**
   * Rethrows the checked exception from the specified
   * {@code ThrowingTriConsumer}.
   * <p>
   * An example of this pattern:
   * <blockquote><pre>
   * TriConsumer&lt;Integer,Integer,Integer&gt; consumer = Throwing.&lt;Integer,Integer,Integer&gt;rethrow((i, j, k) -&gt; {
   *   if (i == 0)
   *     throw new IllegalArgumentException("i=" + i);
   * });
   * for (int i = 3; i &gt;= 0; --i)
   *   consumer.accept(i, -i, i);
   * </pre></blockquote>
   * @param <T> The type of the first input to the consumer's operation.
   * @param <U> The type of the second input to the consumer's operation.
   * @param <V> The type of the third input to the consumer's operation.
   * @param consumer The {@code ThrowingTriConsumer}.
   * @return The specified {@code TriConsumer} instance.
   */
  public static <T,U,V>TriConsumer<T,U,V> rethrow(final ThrowingTriConsumer<T,U,V> consumer) {
    return consumer;
  }

  /**
   * Rethrows the checked exception from the specified {@code ThrowingConsumer}.
   * <p>
   * An example of this pattern:
   * <blockquote><pre>
   * Arrays
   *   .asList(2, 1, 0)
   *   .stream()
   *   .filter(Throwing.&lt;Integer&gt;rethrow(i -&gt; {
   *     if (i == 0)
   *       throw new IOException("i=" + i);
   *     return false;
   *   }))
   *   .collect(Collectors.toList());
   * </pre></blockquote>
   * @param <T> The type of the input to the consumer's operation.
   * @param predicate The {@code ThrowingConsumer}.
   * @return The specified {@code Consumer} instance.
   */
  public static <T>Predicate<T> rethrow(final ThrowingPredicate<T> predicate) {
    return predicate;
  }

  /**
   * Rethrows the checked exception from the specified
   * {@code ThrowingBiPredicate}.
   * <p>
   * An example of this pattern:
   * <blockquote><pre>
   * BiPredicate&lt;Integer,Integer&gt; predicate = Throwing.&lt;Integer,Integer&gt;rethrow((i, j) -&gt; {
   *   if (i == 0)
   *     throw new IllegalArgumentException("i=" + i);
   *   return false;
   * });
   * for (int i = 3; i &gt;= 0; --i)
   *   predicate.accept(i, -i);
   * </pre></blockquote>
   * @param <T> The type of the first input to the predicate's operation.
   * @param <U> The type of the second input to the predicate's operation.
   * @param predicate The {@code ThrowingBiPredicate}.
   * @return The specified {@code BiPredicate} instance.
   */
  public static <T,U>BiPredicate<T,U> rethrow(final ThrowingBiPredicate<T,U> predicate) {
    return predicate;
  }

  /**
   * Rethrows the checked exception from the specified {@code ThrowingFunction}.
   * <p>
   * An example of this pattern:
   * <blockquote><pre>
   * Arrays
   *   .asList(2, 1, 0)
   *   .stream()
   *   .map(Throwing.rethrow((Integer i) -&gt; {
   *     if (i == 0)
   *       throw new IOException("i=" + i);
   *     return String.valueOf(i);
   *   }))
   *   .forEach(f -&gt; {});
   * </pre></blockquote>
   * @param <T> The type of the input to the function's operation.
   * @param <R> The type of the result of the function's function.
   * @param function The {@code ThrowingFunction}.
   * @return The specified {@code Function} instance.
   */
  public static <T,R>Function<T,R> rethrow(final ThrowingFunction<T,R> function) {
    return function;
  }

  /**
   * Rethrows the checked exception from the specified {@code ThrowingBiFunction}.
   * <p>
   * An example of this pattern:
   * <blockquote><pre>
   * BiFunction&lt;Integer,Integer,String&gt; function = Throwing.&lt;Integer,Integer,String&gt;rethrow((i, j) -&gt; {
   *   if (i == 0)
   *     throw new IllegalArgumentException("i=" + i);
   *   return String.valueOf(i);
   * });
   * for (int i = 3; i &gt;= 0; --i)
   *   function.accept(i, -i);
   * </pre></blockquote>
   * @param <T> The type of the first input to the function's operation.
   * @param <U> The type of the second input to the function's operation.
   * @param <R> The type of the result of the function's function.
   * @param function The {@code ThrowingFunction}.
   * @return The specified {@code Function} instance.
   */
  public static <T,U,R>BiFunction<T,U,R> rethrow(final ThrowingBiFunction<T,U,R> function) {
    return function;
  }

  /**
   * Rethrows the specified throwable. This method tricks the compiler to
   * allow checked exceptions to be thrown from lambda expressions.
   *
   * @param <T> The type of the {@code Throwable}.
   * @param t The {@code Throwable} instance to rethrow.
   * @throws T The {@code Throwable} instance.
   */
  @SuppressWarnings("unchecked")
  static <T extends Throwable>void rethrow(final Throwable t) throws T {
    throw (T)t;
  }

  private Throwing() {
  }
}