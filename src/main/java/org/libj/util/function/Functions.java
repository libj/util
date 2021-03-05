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

package org.libj.util.function;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Utility functions for operations pertaining to {@link Function}.
 */
public class Functions {
  /* {@link Function} */

  /**
   * Returns an "and"-composed function that applies its output to the the
   * inputs of the given functions in the provided order. The return of the
   * composed function is a {@link Stream Stream<R>} of results of the argument
   * functions in the provided order. If evaluation of any function throws an
   * exception, it is relayed to the caller of the composed function.
   *
   * @param <T> The type of the input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @return An "and"-composed function that applies its output to the the
   *         inputs of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  public static <T,R>Function<T,Stream<R>> and(final Function<T,? extends R> f0, final Function<T,? extends R> f1) {
    return new Function<T,Stream<R>>() {
      @Override
      public Stream<R> apply(final T t) {
        final Stream.Builder<R> stream = Stream.<R>builder();
        stream.accept(f0.apply(t));
        stream.accept(f1.apply(t));
        return stream.build();
      }
    };
  }

  /**
   * Returns an "and"-composed function that applies its output to the the
   * inputs of the given functions in the provided order. The return of the
   * composed function is a {@link Stream Stream<R>} of results of the argument
   * functions in the provided order. If evaluation of any function throws an
   * exception, it is relayed to the caller of the composed function.
   *
   * @param <T> The type of the input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @param f2 The third function to which the output of the composed function
   *          is to be applied.
   * @return An "and"-composed function that applies its output to the the
   *         inputs of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  public static <T,R>Function<T,Stream<R>> and(final Function<T,? extends R> f0, final Function<T,? extends R> f1, final Function<T,? extends R> f2) {
    return new Function<T,Stream<R>>() {
      @Override
      public Stream<R> apply(final T t) {
        final Stream.Builder<R> stream = Stream.<R>builder();
        stream.accept(f0.apply(t));
        stream.accept(f1.apply(t));
        stream.accept(f2.apply(t));
        return stream.build();
      }
    };
  }

  /**
   * Returns an "and"-composed function that applies its output to the the
   * inputs of the given functions in the provided order. The return of the
   * composed function is a {@link Stream Stream<R>} of results of the argument
   * functions in the provided order. If evaluation of any function throws an
   * exception, it is relayed to the caller of the composed function.
   *
   * @param <T> The type of the input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @param f2 The third function to which the output of the composed function
   *          is to be applied.
   * @param f3 The fourth function to which the output of the composed function
   *          is to be applied.
   * @return An "and"-composed function that applies its output to the the
   *         inputs of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  public static <T,R>Function<T,Stream<R>> and(final Function<T,? extends R> f0, final Function<T,? extends R> f1, final Function<T,? extends R> f2, final Function<T,? extends R> f3) {
    return new Function<T,Stream<R>>() {
      @Override
      public Stream<R> apply(final T t) {
        final Stream.Builder<R> stream = Stream.<R>builder();
        stream.accept(f0.apply(t));
        stream.accept(f1.apply(t));
        stream.accept(f2.apply(t));
        stream.accept(f3.apply(t));
        return stream.build();
      }
    };
  }

  /**
   * Returns an "and"-composed function that applies its output to the the
   * inputs of the given functions in the provided order. The return of the
   * composed function is a {@link Stream Stream<R>} of results of the argument
   * functions in the provided order. If evaluation of any function throws an
   * exception, it is relayed to the caller of the composed function.
   *
   * @param <T> The type of the input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @param f2 The third function to which the output of the composed function
   *          is to be applied.
   * @param f3 The fourth function to which the output of the composed function
   *          is to be applied.
   * @param fN The rest of the functions to which the output of the composed
   *          function is to be applied.
   * @return An "and"-composed function that applies its output to the the
   *         inputs of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  @SafeVarargs
  public static <T,R>Function<T,Stream<R>> and(final Function<T,? extends R> f0, final Function<T,? extends R> f1, final Function<T,? extends R> f2, final Function<T,? extends R> f3, final Function<T,? extends R> ... fN) {
    return new Function<T,Stream<R>>() {
      @Override
      public Stream<R> apply(final T t) {
        final Stream.Builder<R> stream = Stream.<R>builder();
        stream.accept(f0.apply(t));
        stream.accept(f1.apply(t));
        stream.accept(f2.apply(t));
        stream.accept(f3.apply(t));
        for (final Function<T,? extends R> f : fN)
          stream.accept(f.apply(t));

        return stream.build();
      }
    };
  }

  /**
   * Returns an "or"-composed function that applies its output to the the inputs
   * of the given functions in the provided order. The output of the first
   * successful evaluation of the given functions will be returned, thus
   * shortcutting the evaluation of the remaining functions. If evaluation of
   * any function throws an exception, it is relayed to the caller of the
   * composed function.
   *
   * @param <T> The type of the first input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @return An "or"-composed function that applies its output to the the inputs
   *         of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  public static <T,R>Function<T,R> or(final Function<T,? extends R> f0, final Function<T,? extends R> f1) {
    return new Function<T,R>() {
      @Override
      public R apply(final T t) {
        R o;
        if ((o = f0.apply(t)) != null)
          return o;

        if ((o = f1.apply(t)) != null)
          return o;

        return null;
      }
    };
  }

  /**
   * Returns an "or"-composed function that applies its output to the the inputs
   * of the given functions in the provided order. The output of the first
   * successful evaluation of the given functions will be returned, thus
   * shortcutting the evaluation of the remaining functions. If evaluation of
   * any function throws an exception, it is relayed to the caller of the
   * composed function.
   *
   * @param <T> The type of the first input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @param f2 The third function to which the output of the composed function
   *          is to be applied.
   * @return An "or"-composed function that applies its output to the the inputs
   *         of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  public static <T,R>Function<T,R> or(final Function<T,? extends R> f0, final Function<T,? extends R> f1, final Function<T,? extends R> f2) {
    return new Function<T,R>() {
      @Override
      public R apply(final T t) {
        R o;
        if ((o = f0.apply(t)) != null)
          return o;

        if ((o = f1.apply(t)) != null)
          return o;

        if ((o = f2.apply(t)) != null)
          return o;

        return null;
      }
    };
  }

  /**
   * Returns an "or"-composed function that applies its output to the the inputs
   * of the given functions in the provided order. The output of the first
   * successful evaluation of the given functions will be returned, thus
   * shortcutting the evaluation of the remaining functions. If evaluation of
   * any function throws an exception, it is relayed to the caller of the
   * composed function.
   *
   * @param <T> The type of the first input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @param f2 The third function to which the output of the composed function
   *          is to be applied.
   * @param f3 The fourth function to which the output of the composed function
   *          is to be applied.
   * @return An "or"-composed function that applies its output to the the inputs
   *         of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  public static <T,R>Function<T,R> or(final Function<T,? extends R> f0, final Function<T,? extends R> f1, final Function<T,? extends R> f2, final Function<T,? extends R> f3) {
    return new Function<T,R>() {
      @Override
      public R apply(final T t) {
        R o;
        if ((o = f0.apply(t)) != null)
          return o;

        if ((o = f1.apply(t)) != null)
          return o;

        if ((o = f2.apply(t)) != null)
          return o;

        if ((o = f3.apply(t)) != null)
          return o;

        return null;
      }
    };
  }

  /**
   * Returns an "or"-composed function that applies its output to the the inputs
   * of the given functions in the provided order. The output of the first
   * successful evaluation of the given functions will be returned, thus
   * shortcutting the evaluation of the remaining functions. If evaluation of
   * any function throws an exception, it is relayed to the caller of the
   * composed function.
   *
   * @param <T> The type of the first input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @param f2 The third function to which the output of the composed function
   *          is to be applied.
   * @param f3 The fourth function to which the output of the composed function
   *          is to be applied.
   * @param fN The rest of the functions to which the output of the composed
   *          function is to be applied.
   * @return An "or"-composed function that applies its output to the the inputs
   *         of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  @SafeVarargs
  public static <T,R>Function<T,R> or(final Function<T,? extends R> f0, final Function<T,? extends R> f1, final Function<T,? extends R> f2, final Function<T,? extends R> f3, final Function<T,? extends R> ... fN) {
    return new Function<T,R>() {
      @Override
      public R apply(final T t) {
        R o;
        if ((o = f0.apply(t)) != null)
          return o;

        if ((o = f1.apply(t)) != null)
          return o;

        if ((o = f2.apply(t)) != null)
          return o;

        if ((o = f3.apply(t)) != null)
          return o;

        for (final Function<T,? extends R> f : fN)
          if ((o = f.apply(t)) != null)
            return o;

        return null;
      }
    };
  }

  /* {@link BiFunction} */

  /**
   * Returns an "and"-composed function that applies its output to the the
   * inputs of the given functions in the provided order. The return of the
   * composed function is a {@link Stream Stream<R>} of results of the argument
   * functions in the provided order. If evaluation of any function throws an
   * exception, it is relayed to the caller of the composed function.
   *
   * @param <T> The type of the first input to the functions.
   * @param <U> The type of the second input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @return An "and"-composed function that applies its output to the the
   *         inputs of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  public static <T,U,R>BiFunction<T,U,Stream<R>> and(final BiFunction<T,U,? extends R> f0, final BiFunction<T,U,? extends R> f1) {
    return new BiFunction<T,U,Stream<R>>() {
      @Override
      public Stream<R> apply(final T t, final U u) {
        final Stream.Builder<R> stream = Stream.<R>builder();
        stream.accept(f0.apply(t, u));
        stream.accept(f1.apply(t, u));
        return stream.build();
      }
    };
  }

  /**
   * Returns an "and"-composed function that applies its output to the the
   * inputs of the given functions in the provided order. The return of the
   * composed function is a {@link Stream Stream<R>} of results of the argument
   * functions in the provided order. If evaluation of any function throws an
   * exception, it is relayed to the caller of the composed function.
   *
   * @param <T> The type of the first input to the functions.
   * @param <U> The type of the second input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @param f2 The third function to which the output of the composed function
   *          is to be applied.
   * @return An "and"-composed function that applies its output to the the
   *         inputs of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  public static <T,U,R>BiFunction<T,U,Stream<R>> and(final BiFunction<T,U,? extends R> f0, final BiFunction<T,U,? extends R> f1, final BiFunction<T,U,? extends R> f2) {
    return new BiFunction<T,U,Stream<R>>() {
      @Override
      public Stream<R> apply(final T t, final U u) {
        final Stream.Builder<R> stream = Stream.<R>builder();
        stream.accept(f0.apply(t, u));
        stream.accept(f1.apply(t, u));
        stream.accept(f2.apply(t, u));
        return stream.build();
      }
    };
  }

  /**
   * Returns an "and"-composed function that applies its output to the the
   * inputs of the given functions in the provided order. The return of the
   * composed function is a {@link Stream Stream<R>} of results of the argument
   * functions in the provided order. If evaluation of any function throws an
   * exception, it is relayed to the caller of the composed function.
   *
   * @param <T> The type of the first input to the functions.
   * @param <U> The type of the second input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @param f2 The third function to which the output of the composed function
   *          is to be applied.
   * @param f3 The fourth function to which the output of the composed function
   *          is to be applied.
   * @return An "and"-composed function that applies its output to the the
   *         inputs of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  public static <T,U,R>BiFunction<T,U,Stream<R>> and(final BiFunction<T,U,? extends R> f0, final BiFunction<T,U,? extends R> f1, final BiFunction<T,U,? extends R> f2, final BiFunction<T,U,? extends R> f3) {
    return new BiFunction<T,U,Stream<R>>() {
      @Override
      public Stream<R> apply(final T t, final U u) {
        final Stream.Builder<R> stream = Stream.<R>builder();
        stream.accept(f0.apply(t, u));
        stream.accept(f1.apply(t, u));
        stream.accept(f2.apply(t, u));
        stream.accept(f3.apply(t, u));
        return stream.build();
      }
    };
  }

  /**
   * Returns an "and"-composed function that applies its output to the the
   * inputs of the given functions in the provided order. The return of the
   * composed function is a {@link Stream Stream<R>} of results of the argument
   * functions in the provided order. If evaluation of any function throws an
   * exception, it is relayed to the caller of the composed function.
   *
   * @param <T> The type of the first input to the functions.
   * @param <U> The type of the second input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @param f2 The third function to which the output of the composed function
   *          is to be applied.
   * @param f3 The fourth function to which the output of the composed function
   *          is to be applied.
   * @param fN The rest of the functions to which the output of the composed
   *          function is to be applied.
   * @return An "and"-composed function that applies its output to the the
   *         inputs of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  @SafeVarargs
  public static <T,U,R>BiFunction<T,U,Stream<R>> and(final BiFunction<T,U,? extends R> f0, final BiFunction<T,U,? extends R> f1, final BiFunction<T,U,? extends R> f2, final BiFunction<T,U,? extends R> f3, final BiFunction<T,U,? extends R> ... fN) {
    return new BiFunction<T,U,Stream<R>>() {
      @Override
      public Stream<R> apply(final T t, final U u) {
        final Stream.Builder<R> stream = Stream.<R>builder();
        stream.accept(f0.apply(t, u));
        stream.accept(f1.apply(t, u));
        stream.accept(f2.apply(t, u));
        stream.accept(f3.apply(t, u));
        for (final BiFunction<T,U,? extends R> f : fN)
          stream.accept(f.apply(t, u));

        return stream.build();
      }
    };
  }

  /**
   * Returns an "or"-composed function that applies its output to the the inputs
   * of the given functions in the provided order. The output of the first
   * successful evaluation of the given functions will be returned, thus
   * shortcutting the evaluation of the remaining functions. If evaluation of
   * any function throws an exception, it is relayed to the caller of the
   * composed function.
   *
   * @param <T> The type of the first input to the functions.
   * @param <U> The type of the second input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @return An "or"-composed function that applies its output to the the inputs
   *         of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  public static <T,U,R>BiFunction<T,U,R> or(final BiFunction<T,U,? extends R> f0, final BiFunction<T,U,? extends R> f1) {
    return new BiFunction<T,U,R>() {
      @Override
      public R apply(final T t, final U u) {
        R o;
        if ((o = f0.apply(t, u)) != null)
          return o;

        if ((o = f1.apply(t, u)) != null)
          return o;

        return null;
      }
    };
  }

  /**
   * Returns an "or"-composed function that applies its output to the the inputs
   * of the given functions in the provided order. The output of the first
   * successful evaluation of the given functions will be returned, thus
   * shortcutting the evaluation of the remaining functions. If evaluation of
   * any function throws an exception, it is relayed to the caller of the
   * composed function.
   *
   * @param <T> The type of the first input to the functions.
   * @param <U> The type of the second input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @param f2 The third function to which the output of the composed function
   *          is to be applied.
   * @return An "or"-composed function that applies its output to the the inputs
   *         of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  public static <T,U,R>BiFunction<T,U,R> or(final BiFunction<T,U,? extends R> f0, final BiFunction<T,U,? extends R> f1, final BiFunction<T,U,? extends R> f2) {
    return new BiFunction<T,U,R>() {
      @Override
      public R apply(final T t, final U u) {
        R o;
        if ((o = f0.apply(t, u)) != null)
          return o;

        if ((o = f1.apply(t, u)) != null)
          return o;

        if ((o = f2.apply(t, u)) != null)
          return o;

        return null;
      }
    };
  }

  /**
   * Returns an "or"-composed function that applies its output to the the inputs
   * of the given functions in the provided order. The output of the first
   * successful evaluation of the given functions will be returned, thus
   * shortcutting the evaluation of the remaining functions. If evaluation of
   * any function throws an exception, it is relayed to the caller of the
   * composed function.
   *
   * @param <T> The type of the first input to the functions.
   * @param <U> The type of the second input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @param f2 The third function to which the output of the composed function
   *          is to be applied.
   * @param f3 The fourth function to which the output of the composed function
   *          is to be applied.
   * @return An "or"-composed function that applies its output to the the inputs
   *         of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  public static <T,U,R>BiFunction<T,U,R> or(final BiFunction<T,U,? extends R> f0, final BiFunction<T,U,? extends R> f1, final BiFunction<T,U,? extends R> f2, final BiFunction<T,U,? extends R> f3) {
    return new BiFunction<T,U,R>() {
      @Override
      public R apply(final T t, final U u) {
        R o;
        if ((o = f0.apply(t, u)) != null)
          return o;

        if ((o = f1.apply(t, u)) != null)
          return o;

        if ((o = f2.apply(t, u)) != null)
          return o;

        if ((o = f3.apply(t, u)) != null)
          return o;

        return null;
      }
    };
  }

  /**
   * Returns an "or"-composed function that applies its output to the the inputs
   * of the given functions in the provided order. The output of the first
   * successful evaluation of the given functions will be returned, thus
   * shortcutting the evaluation of the remaining functions. If evaluation of
   * any function throws an exception, it is relayed to the caller of the
   * composed function.
   *
   * @param <T> The type of the first input to the functions.
   * @param <U> The type of the second input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @param f2 The third function to which the output of the composed function
   *          is to be applied.
   * @param f3 The fourth function to which the output of the composed function
   *          is to be applied.
   * @param fN The rest of the functions to which the output of the composed
   *          function is to be applied.
   * @return An "or"-composed function that applies its output to the the inputs
   *         of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  @SafeVarargs
  public static <T,U,R>BiFunction<T,U,R> or(final BiFunction<T,U,? extends R> f0, final BiFunction<T,U,? extends R> f1, final BiFunction<T,U,? extends R> f2, final BiFunction<T,U,? extends R> f3, final BiFunction<T,U,? extends R> ... fN) {
    return new BiFunction<T,U,R>() {
      @Override
      public R apply(final T t, final U u) {
        R o;
        if ((o = f0.apply(t, u)) != null)
          return o;

        if ((o = f1.apply(t, u)) != null)
          return o;

        if ((o = f2.apply(t, u)) != null)
          return o;

        if ((o = f3.apply(t, u)) != null)
          return o;

        for (final BiFunction<T,U,? extends R> f : fN)
          if ((o = f.apply(t, u)) != null)
            return o;

        return null;
      }
    };
  }

  /* {@link TriFunction} */

  /**
   * Returns an "and"-composed function that applies its output to the the
   * inputs of the given functions in the provided order. The return of the
   * composed function is a {@link Stream Stream<R>} of results of the argument
   * functions in the provided order. If evaluation of any function throws an
   * exception, it is relayed to the caller of the composed function.
   *
   * @param <T> The type of the first input to the functions.
   * @param <U> The type of the second input to the functions.
   * @param <V> The type of the third input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @return An "and"-composed function that applies its output to the the
   *         inputs of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  public static <T,U,V,R>TriFunction<T,U,V,Stream<R>> and(final TriFunction<T,U,V,? extends R> f0, final TriFunction<T,U,V,? extends R> f1) {
    return new TriFunction<T,U,V,Stream<R>>() {
      @Override
      public Stream<R> apply(final T t, final U u, final V v) {
        final Stream.Builder<R> stream = Stream.<R>builder();
        stream.accept(f0.apply(t, u, v));
        stream.accept(f1.apply(t, u, v));
        return stream.build();
      }
    };
  }

  /**
   * Returns an "and"-composed function that applies its output to the the
   * inputs of the given functions in the provided order. The return of the
   * composed function is a {@link Stream Stream<R>} of results of the argument
   * functions in the provided order. If evaluation of any function throws an
   * exception, it is relayed to the caller of the composed function.
   *
   * @param <T> The type of the first input to the functions.
   * @param <U> The type of the second input to the functions.
   * @param <V> The type of the third input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @param f2 The third function to which the output of the composed function
   *          is to be applied.
   * @return An "and"-composed function that applies its output to the the
   *         inputs of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  public static <T,U,V,R>TriFunction<T,U,V,Stream<R>> and(final TriFunction<T,U,V,? extends R> f0, final TriFunction<T,U,V,? extends R> f1, final TriFunction<T,U,V,? extends R> f2) {
    return new TriFunction<T,U,V,Stream<R>>() {
      @Override
      public Stream<R> apply(final T t, final U u, final V v) {
        final Stream.Builder<R> stream = Stream.<R>builder();
        stream.accept(f0.apply(t, u, v));
        stream.accept(f1.apply(t, u, v));
        stream.accept(f2.apply(t, u, v));
        return stream.build();
      }
    };
  }

  /**
   * Returns an "and"-composed function that applies its output to the the
   * inputs of the given functions in the provided order. The return of the
   * composed function is a {@link Stream Stream<R>} of results of the argument
   * functions in the provided order. If evaluation of any function throws an
   * exception, it is relayed to the caller of the composed function.
   *
   * @param <T> The type of the first input to the functions.
   * @param <U> The type of the second input to the functions.
   * @param <V> The type of the third input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @param f2 The third function to which the output of the composed function
   *          is to be applied.
   * @param f3 The fourth function to which the output of the composed function
   *          is to be applied.
   * @return An "and"-composed function that applies its output to the the
   *         inputs of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  public static <T,U,V,R>TriFunction<T,U,V,Stream<R>> and(final TriFunction<T,U,V,? extends R> f0, final TriFunction<T,U,V,? extends R> f1, final TriFunction<T,U,V,? extends R> f2, final TriFunction<T,U,V,? extends R> f3) {
    return new TriFunction<T,U,V,Stream<R>>() {
      @Override
      public Stream<R> apply(final T t, final U u, final V v) {
        final Stream.Builder<R> stream = Stream.<R>builder();
        stream.accept(f0.apply(t, u, v));
        stream.accept(f1.apply(t, u, v));
        stream.accept(f2.apply(t, u, v));
        stream.accept(f3.apply(t, u, v));
        return stream.build();
      }
    };
  }

  /**
   * Returns an "and"-composed function that applies its output to the the
   * inputs of the given functions in the provided order. The return of the
   * composed function is a {@link Stream Stream<R>} of results of the argument
   * functions in the provided order. If evaluation of any function throws an
   * exception, it is relayed to the caller of the composed function.
   *
   * @param <T> The type of the first input to the functions.
   * @param <U> The type of the second input to the functions.
   * @param <V> The type of the third input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @param f2 The third function to which the output of the composed function
   *          is to be applied.
   * @param f3 The fourth function to which the output of the composed function
   *          is to be applied.
   * @param fN The rest of the functions to which the output of the composed
   *          function is to be applied.
   * @return An "or"-composed function that applies its output to the the inputs
   *         of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  @SafeVarargs
  public static <T,U,V,R>TriFunction<T,U,V,Stream<R>> and(final TriFunction<T,U,V,? extends R> f0, final TriFunction<T,U,V,? extends R> f1, final TriFunction<T,U,V,? extends R> f2, final TriFunction<T,U,V,? extends R> f3, final TriFunction<T,U,V,? extends R> ... fN) {
    return new TriFunction<T,U,V,Stream<R>>() {
      @Override
      public Stream<R> apply(final T t, final U u, final V v) {
        final Stream.Builder<R> stream = Stream.<R>builder();
        stream.accept(f0.apply(t, u, v));
        stream.accept(f1.apply(t, u, v));
        stream.accept(f2.apply(t, u, v));
        stream.accept(f3.apply(t, u, v));
        for (final TriFunction<T,U,V,? extends R> f : fN)
          stream.accept(f.apply(t, u, v));

        return stream.build();
      }
    };
  }

  /**
   * Returns an "or"-composed function that applies its output to the the inputs
   * of the given functions in the provided order. The output of the first
   * successful evaluation of the given functions will be returned, thus
   * shortcutting the evaluation of the remaining functions. If evaluation of
   * any function throws an exception, it is relayed to the caller of the
   * composed function.
   *
   * @param <T> The type of the first input to the functions.
   * @param <U> The type of the second input to the functions.
   * @param <V> The type of the third input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @return An "or"-composed function that applies its output to the the inputs
   *         of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  public static <T,U,V,R>TriFunction<T,U,V,R> or(final TriFunction<T,U,V,? extends R> f0, final TriFunction<T,U,V,? extends R> f1) {
    return new TriFunction<T,U,V,R>() {
      @Override
      public R apply(final T t, final U u, final V v) {
        R o;
        if ((o = f0.apply(t, u, v)) != null)
          return o;

        if ((o = f1.apply(t, u, v)) != null)
          return o;

        return null;
      }
    };
  }

  /**
   * Returns an "or"-composed function that applies its output to the the inputs
   * of the given functions in the provided order. The output of the first
   * successful evaluation of the given functions will be returned, thus
   * shortcutting the evaluation of the remaining functions. If evaluation of
   * any function throws an exception, it is relayed to the caller of the
   * composed function.
   *
   * @param <T> The type of the first input to the functions.
   * @param <U> The type of the second input to the functions.
   * @param <V> The type of the third input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @param f2 The third function to which the output of the composed function
   *          is to be applied.
   * @return An "or"-composed function that applies its output to the the inputs
   *         of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  public static <T,U,V,R>TriFunction<T,U,V,R> or(final TriFunction<T,U,V,? extends R> f0, final TriFunction<T,U,V,? extends R> f1, final TriFunction<T,U,V,? extends R> f2) {
    return new TriFunction<T,U,V,R>() {
      @Override
      public R apply(final T t, final U u, final V v) {
        R o;
        if ((o = f0.apply(t, u, v)) != null)
          return o;

        if ((o = f1.apply(t, u, v)) != null)
          return o;

        if ((o = f2.apply(t, u, v)) != null)
          return o;

        return null;
      }
    };
  }

  /**
   * Returns an "or"-composed function that applies its output to the the inputs
   * of the given functions in the provided order. The output of the first
   * successful evaluation of the given functions will be returned, thus
   * shortcutting the evaluation of the remaining functions. If evaluation of
   * any function throws an exception, it is relayed to the caller of the
   * composed function.
   *
   * @param <T> The type of the first input to the functions.
   * @param <U> The type of the second input to the functions.
   * @param <V> The type of the third input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @param f2 The third function to which the output of the composed function
   *          is to be applied.
   * @param f3 The fourth function to which the output of the composed function
   *          is to be applied.
   * @return An "or"-composed function that applies its output to the the inputs
   *         of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  public static <T,U,V,R>TriFunction<T,U,V,R> or(final TriFunction<T,U,V,? extends R> f0, final TriFunction<T,U,V,? extends R> f1, final TriFunction<T,U,V,? extends R> f2, final TriFunction<T,U,V,? extends R> f3) {
    return new TriFunction<T,U,V,R>() {
      @Override
      public R apply(final T t, final U u, final V v) {
        R o;
        if ((o = f0.apply(t, u, v)) != null)
          return o;

        if ((o = f1.apply(t, u, v)) != null)
          return o;

        if ((o = f2.apply(t, u, v)) != null)
          return o;

        if ((o = f3.apply(t, u, v)) != null)
          return o;

        return null;
      }
    };
  }

  /**
   * Returns an "or"-composed function that applies its output to the the inputs
   * of the given functions in the provided order. The output of the first
   * successful evaluation of the given functions will be returned, thus
   * shortcutting the evaluation of the remaining functions. If evaluation of
   * any function throws an exception, it is relayed to the caller of the
   * composed function.
   *
   * @param <T> The type of the first input to the functions.
   * @param <U> The type of the second input to the functions.
   * @param <V> The type of the third input to the functions.
   * @param <R> The type of the result of the functions.
   * @param f0 The first function to which the output of the composed function
   *          is to be applied.
   * @param f1 The second function to which the output of the composed function
   *          is to be applied.
   * @param f2 The third function to which the output of the composed function
   *          is to be applied.
   * @param f3 The fourth function to which the output of the composed function
   *          is to be applied.
   * @param fN The rest of the functions to which the output of the composed
   *          function is to be applied.
   * @return An "or"-composed function that applies its output to the the inputs
   *         of the given functions in the provided order.
   * @throws NullPointerException If any provided function is null.
   */
  @SafeVarargs
  public static <T,U,V,R>TriFunction<T,U,V,R> or(final TriFunction<T,U,V,? extends R> f0, final TriFunction<T,U,V,? extends R> f1, final TriFunction<T,U,V,? extends R> f2, final TriFunction<T,U,V,? extends R> f3, final TriFunction<T,U,V,? extends R> ... fN) {
    return new TriFunction<T,U,V,R>() {
      @Override
      public R apply(final T t, final U u, final V v) {
        R o;
        if ((o = f0.apply(t, u, v)) != null)
          return o;

        if ((o = f1.apply(t, u, v)) != null)
          return o;

        if ((o = f2.apply(t, u, v)) != null)
          return o;

        if ((o = f3.apply(t, u, v)) != null)
          return o;

        for (final TriFunction<T,U,V,? extends R> f : fN)
          if ((o = f.apply(t, u, v)) != null)
            return o;

        return null;
      }
    };
  }
}