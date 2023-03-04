/* Copyright (c) 2016 LibJ
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

package org.libj.util;

import java.util.List;
import java.util.RandomAccess;

/**
 * A list that maintains 2 representations of its elements:
 * <ol>
 * <li>For value elements of type {@code <V>}, which is the generic type of {@code this} list.</li>
 * <li>For reflected elements of type {@code <R>}, which is the generic type of the list returned by {@link #getMirrorList()}</li>
 * </ol>
 * The {@link MirrorList.Mirror} provides the {@link MirrorList.Mirror#valueToReflection(Object) V -> R} and
 * {@link MirrorList.Mirror#reflectionToValue(Object) R -> V} methods, which are used to reflect values from one
 * {@link MirrorRandomAccessList} to the other.
 * <p>
 * Elements added to either sides of the {@link MirrorRandomAccessList} are "lazy-reflected" -- i.e. instead of reflecting elements
 * upon their addition to the list, elements are reflected upon retrieval from the list.
 * <p>
 * The {@link #getMirrorList()} method returns the {@link MirrorRandomAccessList} instance of type {@code <R>} that represents the
 * one-to-one mapping with its mirror of type {@code <V>} (i.e. {@code this} instance). Changes to the
 * {@link MirrorRandomAccessList} will be reflected in {@code this} instance, and vice-versa.
 *
 * @param <V> The type of value elements in this list.
 * @param <LV> The type of underlying value list.
 * @param <R> The type of reflected value elements in the mirror list.
 * @param <LR> The type of reflected value mirror list.
 */
public class MirrorRandomAccessList<V,LV extends List<V> & RandomAccess,R,LR extends List<R> & RandomAccess> extends MirrorList<V,LV,R,LR> implements RandomAccess {
  /**
   * Creates a new {@link MirrorRandomAccessList} with the specified target lists and {@link MirrorList.Mirror}. The specified
   * target lists are meant to be empty, as they become the underlying lists of the new {@link MirrorRandomAccessList} instance.
   * <p>
   * The specified {@link MirrorList.Mirror} provides the {@link MirrorList.Mirror#valueToReflection(Object) V -> R} and
   * {@link MirrorList.Mirror#reflectionToValue(Object) R -> V} methods, which are used to reflect object values from one
   * {@link MirrorRandomAccessList} to the other.
   *
   * @param values The underlying list of type {@code <V>}.
   * @param reflections The underlying list of type {@code <R>}.
   * @param mirror The {@link MirrorList.Mirror} specifying the {@link MirrorList.Mirror#valueToReflection(Object) V -> R} and
   *          {@link MirrorList.Mirror#reflectionToValue(Object) R -> V} methods.
   * @throws NullPointerException If any of the specified parameters is null.
   */
  public MirrorRandomAccessList(final LV values, final LR reflections, final Mirror<V,R> mirror) {
    super(values, reflections, mirror);
  }

  /**
   * Creates a new {@link MirrorRandomAccessList} with the specified lists and mirror. This method is specific for the construction
   * of a reflected {@link MirrorRandomAccessList} instance.
   *
   * @param mirrorList The {@link MirrorRandomAccessList} for which {@code this} list will be a reflection. Likewise, {@code this}
   *          list will be a reflection for {@code mirrorList}.
   * @param values The underlying list of type {@code <V>}.
   * @param mirror The {@link MirrorList.Mirror} specifying the {@link MirrorList.Mirror#valueToReflection(Object) V -> R} and
   *          {@link MirrorList.Mirror#reflectionToValue(Object) R -> V} methods.
   */
  protected MirrorRandomAccessList(final MirrorRandomAccessList<R,LR,V,LV> mirrorList, final LV values, final Mirror<V,R> mirror) {
    super(mirrorList, values, mirror);
  }
}