/* Copyright (c) 2014 lib4j
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

package org.safris.commons.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public final class For {
  public static interface Filter<I> {
    public boolean accept(final I item, final Object ... args);
  }

  public static interface Recurser<I,C> extends Filter<I> {
    public I[] items(final C container);
    public C next(final C container);
  }

  public static <I>I[] iterative(final I[] array, final Class<I> type, final Filter<I> filter, final Object ... args) {
    if (array == null)
      return null;

    if (type == null)
      throw new NullPointerException("type == null");

    if (filter == null)
      throw new NullPointerException("filter == null");

    final List<I> list = new ArrayList<I>(array.length);
    for (int i = 0; i < array.length; i++)
      if (filter.accept(array[i], args))
        list.add(array[i]);

    return list.toArray((I[])Array.newInstance(type, list.size()));
  }

  private static <I>I[] recursiveOrdered(final Class<I> type, final Filter<I> filter, final Object[] args, final I[] array, int index, final int depth) {
    I item;
    boolean skip = true;
    if (index >= array.length)
      return (I[])Array.newInstance(type, depth);

    while ((skip = !filter.accept(item = array[index++], args)) && index < array.length);
    final I[] ret = recursiveOrdered(type, filter, args, array, index, skip ? depth : depth + 1);
    if (!skip)
      ret[depth] = item;

    return ret;
  }

  public static <I>I[] recursiveOrdered(final I[] array, final Class<I> type, final Filter<I> filter, final Object ... args) {
    if (array == null)
      return null;

    if (type == null)
      throw new NullPointerException("type == null");

    if (filter == null)
      throw new NullPointerException("filter == null");

    return recursiveOrdered(type, filter, args, array, 0, 0);
  }

  private static <I,C>I[] recursiveOrdered(final C container, final I[] array, final Class<I> type, final Recurser<I,C> recurser, final Object[] args, int index, final int depth) {
    I item;
    boolean skip = true;
    if (index >= array.length) {
      final C parent = recurser.next(container);
      return parent == null ? (I[])Array.newInstance(type, depth) : recursiveOrdered(parent, recurser.items(parent), type, recurser, args, 0, depth);
    }

    while ((skip = !recurser.accept(item = array[index++], args)) && index < array.length);
    final I[] ret = recursiveOrdered(container, array, type, recurser, args, index, skip ? depth : depth + 1);
    if (!skip)
      ret[depth] = item;

    return ret;
  }

  public static <I,C>I[] recursiveOrdered(final C container, final I[] array, final Class<I> type, final Recurser<I,C> recurser, final Object ... args) {
    if (array == null)
      return null;

    if (type == null)
      throw new NullPointerException("type == null");

    if (recurser == null)
      throw new NullPointerException("recurser == null");

    return recursiveOrdered(container, array, type, recurser, args, 0, 0);
  }

  private static <I,C>I[] recursiveInverted(final C container, final I[] array, final Class<I> type, final Recurser<I,C> recurser, final Object[] args, int index, final int depth) {
    I item;
    boolean skip = true;
    if (index >= array.length) {
      final C parent = recurser.next(container);
      return parent == null ? (I[])Array.newInstance(type, depth) : recursiveInverted(parent, recurser.items(parent), type, recurser, args, 0, depth);
    }

    while ((skip = !recurser.accept(item = array[array.length - ++index], args)) && index < array.length);
    final I[] ret = recursiveInverted(container, array, type, recurser, args, index, skip ? depth : depth + 1);
    if (!skip)
      ret[ret.length - 1 - depth] = item;

    return ret;
  }

  public static <I,C>I[] recursiveInverted(final C container, final I[] array, final Class<I> type, final Recurser<I,C> recurser, final Object ... args) {
    if (array == null)
      return null;

    if (type == null)
      throw new NullPointerException("type == null");

    if (recurser == null)
      throw new NullPointerException("recurser == null");

    return recursiveInverted(container, array, type, recurser, args, 0, 0);
  }

  private For() {
  }
}