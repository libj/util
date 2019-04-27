/* Copyright (c) 2016 OpenJAX
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

package org.openjax.ext.util;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * A list that maintains 2 representations of its elements:
 * <ol>
 * <li>For elements of type {@code <E>}, which is the generic type of
 * {@code this} list.</li>
 * <li>For elements of type {@code <M>}, which is the generic type of the list
 * returned by {@link #getMirror()}</li>
 * </ol>
 * Elements added to the {@code MirrorList} are added to both representations,
 * the first added directly, and the second through a mirror function that is
 * specified on the constructor.
 * <p>
 * The {@link #getMirror()} method returns a {@code MirrorList} instance of type
 * {@code <M>} that retains the one-to-one mapping with its mirror of type
 * {@code <E>}.
 *
 * @param <E> The type of elements in this list.
 * @param <M> The type of elements in the mirror list.
 */
public class MirrorList<E,M> extends ObservableList<E> {
  private final Function<E,M> reflect;
  private final MirrorList<M,E> mirror;

  /**
   * Creates a new {@code MirrorList} with the specified lists and mirror
   * functions. The specified lists are meant to be empty, as they become the
   * underlying lists of the new {@code MirrorList} instance. The specified
   * {@code Function} instances represent the {@code E -> M} mirror function for
   * the new {@code MirrorList} instance, and the and {@code M -> E} mirror
   * function for the instance returned by {@link #getMirror()}.
   *
   * @param a The underlying list of type {@code <E>}.
   * @param b The underlying list of type {@code <M>}.
   * @param forward The {@code Function} instance representing the
   *          {@code E -> M} mirror function.
   * @param reverse The {@code Function} instance representing the
   *          {@code M -> E} mirror function.
   * @throws NullPointerException If any of the specified parameters is null.
   */
  public MirrorList(final List<E> a, final List<M> b, final Function<E,M> forward, final Function<M,E> reverse) {
    super(a);
    this.reflect = Objects.requireNonNull(forward);
    this.mirror = new MirrorList<>(b, Objects.requireNonNull(reverse), this);
  }

  private MirrorList(final List<E> a, final Function<E,M> forward, final MirrorList<M,E> mirror) {
    super(a);
    this.reflect = forward;
    this.mirror = mirror;
  }

  /**
   * Returns a {@code MirrorList} instance of type {@code <M>} that retains the
   * one-to-one mapping with its mirror of type {@code <E>} (i.e. {@code this}
   * instance).
   *
   * @return A {@code MirrorList} instance of type {@code <M>} that retains the
   *         one-to-one mapping with its mirror of type {@code <E>} (i.e.
   *         {@code this} instance).
   */
  public MirrorList<M,E> getMirror() {
    return mirror;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected boolean beforeAdd(final int index, final E e) {
    ((List<M>)mirror.target).add(index, reflect.apply(e));
    return true;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected boolean beforeRemove(final int index) {
    ((List<M>)mirror.target).remove(index);
    return true;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected boolean beforeSet(final int index, final E oldElement) {
    ((List<M>)mirror.target).set(index, reflect.apply(get(index)));
    return true;
  }

  @Override
  public MirrorList<E,M> subList(final int fromIndex, final int toIndex) {
    return new MirrorList<E,M>(target.subList(fromIndex, toIndex), mirror.target.subList(fromIndex, toIndex), reflect, mirror.reflect);
  }
}