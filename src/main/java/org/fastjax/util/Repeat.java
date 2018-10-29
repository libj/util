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

public final class Repeat {
  public static interface Filter<M,A> {
    public boolean accept(final M member, final A arg);
  }

  public static interface Recurser<M,C,A> extends Filter<M,A> {
    public M[] members(final C container);
    public C next(final C container);
  }

  private static abstract class Algorithm {
    /**
     * Default implementation of simple variation of abstract algorithm.
     *
     * @param array The array to consume.
     * @param type The component type of the {@code array}.
     * @param filter Filter to be applied to each member of the {@code array}.
     * @param arg Type-agnostic argument to be passed to the {@code filter}.
     *
     * @return The {@code type}-typed array of {@code filter}-accepted members of {@code array} argument.
     */
    protected <M,A>M[] simple(final M[] array, final Class<M> type, final Filter<M,A> filter, final A arg) {
      if (array == null)
        return null;

      if (type == null)
        throw new IllegalArgumentException("type == null");

      if (filter == null)
        throw new IllegalArgumentException("filter == null");

      return null;
    }
  }

  public static final class Recursive {
    private static abstract class RecursiveAlgorithm extends Algorithm {
      @Override
      protected <M,A>M[] simple(final M[] array, final Class<M> type, final Filter<M,A> filter, final A arg) {
        super.simple(array, type, filter, arg);
        return recurse(type, filter, arg, array, 0, 0);
      }

      protected <M,C,A>M[] contained(final C container, final M[] array, final Class<M> type, final Recurser<M,C,A> recurser, final A arg) {
        if (array == null)
          return null;

        if (type == null)
          throw new IllegalArgumentException("type == null");

        if (recurser == null)
          throw new IllegalArgumentException("recurser == null");

        return recurse(container, array, type, recurser, arg, 0, 0);
      }

      protected abstract <M,A>M[] recurse(final Class<M> type, final Filter<M,A> filter, final A arg, final M[] array, int index, final int depth);
      protected abstract <M,C,A>M[] recurse(final C container, final M[] array, final Class<M> type, final Recurser<M,C,A> recurser, final A arg, int index, final int depth);
    }

    private static final RecursiveAlgorithm recursiveOrdered = new RecursiveAlgorithm() {
      @Override
      @SuppressWarnings("unchecked")
      protected <M,A>M[] recurse(final Class<M> type, final Filter<M,A> filter, final A arg, final M[] array, int index, final int depth) {
        if (index >= array.length)
          return (M[])Array.newInstance(type, depth);

        M member;
        boolean skip = true;
        while ((skip = !filter.accept(member = array[index++], arg)) && index < array.length);
        final M[] result = recurse(type, filter, arg, array, index, skip ? depth : depth + 1);
        if (!skip)
          result[depth] = member;

        return result;
      }

      @Override
      @SuppressWarnings("unchecked")
      protected <M,C,A>M[] recurse(final C container, final M[] array, final Class<M> type, final Recurser<M,C,A> recurser, final A arg, int index, final int depth) {
        if (index >= array.length) {
          final C parent = recurser.next(container);
          return parent == null ? (M[])Array.newInstance(type, depth) : recurse(parent, recurser.members(parent), type, recurser, arg, 0, depth);
        }

        M member;
        boolean skip = true;
        while ((skip = !recurser.accept(member = array[index++], arg)) && index < array.length);
        final M[] result = recurse(container, array, type, recurser, arg, index, skip ? depth : depth + 1);
        if (!skip)
          result[depth] = member;

        return result;
      }
    };

    private static final RecursiveAlgorithm recursiveInverted = new RecursiveAlgorithm() {
      @Override
      @SuppressWarnings("unchecked")
      protected <M,A>M[] recurse(final Class<M> type, final Filter<M,A> filter, final A arg, final M[] array, int index, final int depth) {
        if (index >= array.length)
          return (M[])Array.newInstance(type, depth);

        M member;
        boolean skip = true;
        while ((skip = !filter.accept(member = array[array.length - ++index], arg)) && index < array.length);
        final M[] result = recurse(type, filter, arg, array, index, skip ? depth : depth + 1);
        if (!skip)
          result[result.length - 1 - depth] = member;

        return result;
      }

      @Override
      @SuppressWarnings("unchecked")
      protected <M,C,A>M[] recurse(final C container, final M[] array, final Class<M> type, final Recurser<M,C,A> recurser, final A arg, int index, final int depth) {
        if (index >= array.length) {
          final C parent = recurser.next(container);
          return parent == null ? (M[])Array.newInstance(type, depth) : recurse(parent, recurser.members(parent), type, recurser, arg, 0, depth);
        }

        M member;
        boolean skip = true;
        while ((skip = !recurser.accept(member = array[array.length - ++index], arg)) && index < array.length);
        final M[] result = recurse(container, array, type, recurser, arg, index, skip ? depth : depth + 1);
        if (!skip)
          result[result.length - 1 - depth] = member;

        return result;
      }
    };

    public static <M,A>M[] ordered(final M[] array, final Class<M> type, final Filter<M,A> filter, final A arg) {
      return recursiveOrdered.simple(array, type, filter, arg);
    }

    public static <M,C,A>M[] ordered(final C container, final M[] array, final Class<M> type, final Recurser<M,C,A> recurser, final A arg) {
      return recursiveOrdered.contained(container, array, type, recurser, arg);
    }

    public static <M,A>M[] inverted(final M[] array, final Class<M> type, final Filter<M,A> filter, final A arg) {
      return recursiveInverted.simple(array, type, filter, arg);
    }

    public static <M,C,A>M[] inverted(final C container, final M[] array, final Class<M> type, final Recurser<M,C,A> recurser, final A arg) {
      return recursiveInverted.contained(container, array, type, recurser, arg);
    }

    private Recursive() {
    }
  }

  private static final Algorithm iterative = new Algorithm() {
    @Override
    @SuppressWarnings("unchecked")
    protected <M,A>M[] simple(final M[] array, final Class<M> type, final Filter<M,A> filter, final A arg) {
      super.simple(array, type, filter, arg);
      final List<M> list = new ArrayList<>(array.length);
      for (int i = 0; i < array.length; ++i)
        if (filter.accept(array[i], arg))
          list.add(array[i]);

      return list.toArray((M[])Array.newInstance(type, list.size()));
    }
  };

  public static <M,A>M[] iterative(final M[] array, final Class<M> type, final Filter<M,A> filter, final A arg) {
    return iterative.simple(array, type, filter, arg);
  }

  private Repeat() {
  }
}