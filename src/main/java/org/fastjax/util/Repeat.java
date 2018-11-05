/* Copyright (c) 2014 FastJAX
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

package org.fastjax.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public final class Repeat {
  public static interface Recurser<C,M,A> extends BiPredicate<M,A> {
    public M[] members(final C container);
    public C next(final C container);
  }

  private static abstract class Algorithm {
    /**
     * Default implementation of "simple" variation of abstract
     * {@code Algorithm}.
     *
     * @param <M> The type parameter for the member class of {@code array}.
     * @param <A> The type parameter of {@code arg}.
     * @param array The array to consume.
     * @param type The component type of the {@code array}.
     * @param predicate {@link BiPredicate} to be applied to each member of the
     *          {@code array}.
     * @param arg Argument to be passed to {@code predicate}.
     * @return The {@code type}-typed array of {@code predicate}-passed members
     *         of {@code array} argument.
     * @throws NullPointerException If {@code array} is not null, and
     *           {@code type} or {@code predicate} are null.
     */
    protected <M,A>M[] simple(final M[] array, final Class<M> type, final BiPredicate<M,A> predicate, final A arg) {
      if (array == null)
        return null;

      if (type == null)
        throw new NullPointerException("type == null");

      if (predicate == null)
        throw new NullPointerException("predicate == null");

      return null;
    }
  }

  public static final class Recursive {
    private static abstract class RecursiveAlgorithm extends Algorithm {
      @Override
      protected <M,A>M[] simple(final M[] array, final Class<M> type, final BiPredicate<M,A> predicate, final A arg) {
        super.simple(array, type, predicate, arg);
        return recurse(type, predicate, arg, array, 0, 0);
      }

      /**
       * Recursive implementation of "contained" variation of {@code Algorithm}.
       *
       * @param <C> The type parameter of {@code container}.
       * @param <M> The type parameter for the member class of {@code array}.
       * @param <A> The type parameter of {@code arg}.
       * @param array The array to consume.
       * @param type The component type of the {@code array}.
       * @param recurser {@link Recurser} defining the recursion logic.
       * @param arg Argument to be passed to {@code predicate}.
       * @return The {@code type}-typed array of {@code recurser}-accepted
       *         members of {@code array} argument.
       * @throws NullPointerException If {@code array} is not null, and
       *           {@code type} or {@code recurser} are null.
       */
      protected <C,M,A>M[] contained(final C container, final M[] array, final Class<M> type, final Recurser<C,M,A> recurser, final A arg) {
        if (array == null)
          return null;

        if (type == null)
          throw new IllegalArgumentException("type == null");

        if (recurser == null)
          throw new IllegalArgumentException("recurser == null");

        return recurse(container, array, type, recurser, arg, 0, 0);
      }

      protected abstract <M,A>M[] recurse(final Class<M> type, final BiPredicate<M,A> predicate, final A arg, final M[] array, int index, final int depth);
      protected abstract <C,M,A>M[] recurse(final C container, final M[] array, final Class<M> type, final Recurser<C,M,A> recurser, final A arg, int index, final int depth);
    }

    private static final RecursiveAlgorithm recursiveOrdered = new RecursiveAlgorithm() {
      @Override
      @SuppressWarnings("unchecked")
      protected <M,A>M[] recurse(final Class<M> type, final BiPredicate<M,A> predicate, final A arg, final M[] array, int index, final int depth) {
        if (index >= array.length)
          return (M[])Array.newInstance(type, depth);

        M member;
        boolean skip = true;
        while ((skip = !predicate.test(member = array[index++], arg)) && index < array.length);
        final M[] result = recurse(type, predicate, arg, array, index, skip ? depth : depth + 1);
        if (!skip)
          result[depth] = member;

        return result;
      }

      @Override
      @SuppressWarnings("unchecked")
      protected <C,M,A>M[] recurse(final C container, final M[] array, final Class<M> type, final Recurser<C,M,A> recurser, final A arg, int index, final int depth) {
        if (index == array.length) {
          final C parent = recurser.next(container);
          return parent == null ? (M[])Array.newInstance(type, depth) : recurse(parent, recurser.members(parent), type, recurser, arg, 0, depth);
        }

        M member;
        boolean skip = true;
        while ((skip = !recurser.test(member = array[index++], arg)) && index < array.length);
        final M[] result = recurse(container, array, type, recurser, arg, index, skip ? depth : depth + 1);
        if (!skip)
          result[depth] = member;

        return result;
      }
    };

    private static final RecursiveAlgorithm recursiveInverted = new RecursiveAlgorithm() {
      @Override
      @SuppressWarnings("unchecked")
      protected <M,A>M[] recurse(final Class<M> type, final BiPredicate<M,A> predicate, final A arg, final M[] array, int index, final int depth) {
        if (index == array.length)
          return (M[])Array.newInstance(type, depth);

        M member;
        boolean skip = true;
        while ((skip = !predicate.test(member = array[array.length - ++index], arg)) && index < array.length);
        final M[] result = recurse(type, predicate, arg, array, index, skip ? depth : depth + 1);
        if (!skip)
          result[result.length - 1 - depth] = member;

        return result;
      }

      @Override
      @SuppressWarnings("unchecked")
      protected <C,M,A>M[] recurse(final C container, final M[] array, final Class<M> type, final Recurser<C,M,A> recurser, final A arg, int index, final int depth) {
        if (index == array.length) {
          final C parent = recurser.next(container);
          return parent == null ? (M[])Array.newInstance(type, depth) : recurse(parent, recurser.members(parent), type, recurser, arg, 0, depth);
        }

        M member;
        boolean skip = true;
        while ((skip = !recurser.test(member = array[array.length - ++index], arg)) && index < array.length);
        final M[] result = recurse(container, array, type, recurser, arg, index, skip ? depth : depth + 1);
        if (!skip)
          result[result.length - 1 - depth] = member;

        return result;
      }
    };

    public static <M,A>M[] ordered(final M[] array, final Class<M> type, final BiPredicate<M,A> predicate, final A arg) {
      return recursiveOrdered.simple(array, type, predicate, arg);
    }

    public static <C,M,A>M[] ordered(final C container, final M[] array, final Class<M> type, final Recurser<C,M,A> recurser, final A arg) {
      return recursiveOrdered.contained(container, array, type, recurser, arg);
    }

    public static <M,A>M[] inverted(final M[] array, final Class<M> type, final BiPredicate<M,A> predicate, final A arg) {
      return recursiveInverted.simple(array, type, predicate, arg);
    }

    public static <C,M,A>M[] inverted(final C container, final M[] array, final Class<M> type, final Recurser<C,M,A> recurser, final A arg) {
      return recursiveInverted.contained(container, array, type, recurser, arg);
    }

    private Recursive() {
    }
  }

  private static final Algorithm iterative = new Algorithm() {
    @Override
    @SuppressWarnings("unchecked")
    protected <M,A>M[] simple(final M[] array, final Class<M> type, final BiPredicate<M,A> predicate, final A arg) {
      super.simple(array, type, predicate, arg);
      final List<M> list = new ArrayList<>(array.length);
      for (int i = 0; i < array.length; ++i)
        if (predicate.test(array[i], arg))
          list.add(array[i]);

      return list.toArray((M[])Array.newInstance(type, list.size()));
    }
  };

  public static <M,A>M[] iterative(final M[] array, final Class<M> type, final BiPredicate<M,A> predicate, final A arg) {
    return iterative.simple(array, type, predicate, arg);
  }

  private Repeat() {
  }
}