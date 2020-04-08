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

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.libj.lang.Assertions;

/**
 * A list that maintains 2 representations of its elements:
 * <ol>
 * <li>For value elements of type {@code <V>}, which is the generic type of
 * {@code this} list.</li>
 * <li>For reflected elements of type {@code <R>}, which is the generic type of
 * the list returned by {@link #getMirrorList()}</li>
 * </ol>
 * The {@link Mirror} provides the {@link Mirror#valueToReflection(Object) V ->
 * R} and {@link Mirror#reflectionToValue(Object) R -> V} methods, which are
 * used to reflect values from one {@link MirrorList} to the other.
 * <p>
 * Elements added to either sides of the {@link MirrorList} are "lazy-reflected"
 * -- i.e. instead of reflecting elements upon their addition to the list,
 * elements are reflected upon retrieval from the list.
 * <p>
 * The {@link #getMirrorList()} method returns the {@link MirrorList} instance
 * of type {@code <R>} that represents the one-to-one mapping with its mirror of
 * type {@code <V>} (i.e. {@code this} instance). Changes to the
 * {@link MirrorList} will be reflected in {@code this} instance, and
 * vice-versa.
 *
 * @param <V> The type of value elements in this list.
 * @param <R> The type of reflected value elements in the mirror list.
 */
public class MirrorList<V,R> extends ObservableList<V> {
  /**
   * Interface providing methods for the reflection of one type of object to
   * another, and vice-versa.
   *
   * @param <V> The type of value object of this {@link Mirror}.
   * @param <R> The type of reflected value object of this {@link Mirror}.
   */
  public interface Mirror<V,R> {
    /**
     * Reflects a value object of type {@code <V>} to a reflected value object
     * of type {@code <R>}.
     *
     * @param value The value object of type {@code <V>}.
     * @return The reflected value object of type {@code <R>}.
     */
    R valueToReflection(V value);

    /**
     * Reflects a reflected value object of type {@code <R>} to a value object
     * of type {@code <V>}.
     *
     * @param reflection The reflected value object of type {@code <R>}.
     * @return The value object of type {@code <V>}.
     */
    V reflectionToValue(R reflection);

    /**
     * Returns the reverse representation of this {@link Mirror}, whereby the
     * value object type {@code <V>} and reflected value object of type
     * {@code <R>} are swapped.
     *
     * @return The reverse representation of this {@link Mirror}.
     */
    default Mirror<R,V> reverse() {
      return new Mirror<R,V>() {
        @Override
        public V valueToReflection(final R value) {
          return Mirror.this.reflectionToValue(value);
        }

        @Override
        public R reflectionToValue(final V reflection) {
          return Mirror.this.valueToReflection(reflection);
        }
      };
    }
  }

  /**
   * Object to be used in the underlying lists to represent that a value is
   * pending reflection via the methods in {@link #mirror} upon the invocation
   * of {@link #beforeGet(int,ListIterator)}.
   */
  protected static final Object PENDING = new Object();

  private Mirror<V,R> mirror;
  private Mirror<R,V> reverse;
  protected MirrorList<R,V> mirrorList;
  protected Iterator<?> targetLock;
  protected boolean inited;
  protected boolean unlocked;

  /**
   * Underlying map to be used for {@link #mirrorList}, or {@code null} if
   * {@link #mirrorList} is already instantiated.
   */
  private List<R> reflections;

  /**
   * Creates a new {@link MirrorList} with the specified target lists and
   * {@link Mirror}. The specified target lists are meant to be empty, as they
   * become the underlying lists of the new {@link MirrorList} instance.
   * <p>
   * The specified {@link Mirror} provides the
   * {@link Mirror#valueToReflection(Object) V -> R} and
   * {@link Mirror#reflectionToValue(Object) R -> V} methods, which are used to
   * reflect object values from one {@link MirrorList} to the other.
   *
   * @param values The underlying list of type {@code <V>}.
   * @param reflections The underlying list of type {@code <R>}.
   * @param mirror The {@link Mirror} specifying the
   *          {@link Mirror#valueToReflection(Object) V -> R} and
   *          {@link Mirror#reflectionToValue(Object) R -> V} methods.
   * @throws NullPointerException If any of the specified parameters is null.
   */
  public MirrorList(final List<V> values, final List<R> reflections, final Mirror<V,R> mirror) {
    super(values);
    this.mirror = Objects.requireNonNull(mirror);
    this.reflections = reflections;
  }

  /**
   * Creates a new {@link MirrorList} with the specified lists and mirror. This
   * method is specific for the construction of a reflected {@link MirrorList}
   * instance.
   *
   * @param mirrorList The {@link MirrorList} for which {@code this} list will
   *          be a reflection. Likewise, {@code this} list will be a reflection
   *          for {@code mirrorList}.
   * @param values The underlying list of type {@code <V>}.
   * @param mirror The {@link Mirror} specifying the
   *          {@link Mirror#valueToReflection(Object) V -> R} and
   *          {@link Mirror#reflectionToValue(Object) R -> V} methods.
   */
  protected MirrorList(final MirrorList<R,V> mirrorList, final List<V> values, final Mirror<V,R> mirror) {
    super(values);
    this.mirror = mirror;
    this.mirrorList = mirrorList;
  }

  /**
   * Factory method that returns a new instance of a {@link MirrorList} with the
   * specified target lists. This method is intended to be overridden by
   * subclasses in order to provide instances of the subclass.
   *
   * @param values The underlying list of type {@code <V>}.
   * @param reflections The underlying list of type {@code <R>}.
   * @return A new instance of a {@link MirrorList} with the specified target
   *         lists.
   */
  protected MirrorList<V,R> newInstance(final List<V> values, final List<R> reflections) {
    return new MirrorList<>(values, reflections, mirror);
  }

  /**
   * Factory method that returns a new <b>mirror</b> instance of a
   * {@link MirrorList} with the specified target list. This method is intended
   * to be overridden by subclasses in order to provide instances of the
   * subclass.
   *
   * @param values The underlying list of type {@code <V>}.
   * @return A new instance of a {@link MirrorList} with the specified target
   *         lists.
   */
  protected MirrorList<R,V> newMirrorInstance(final List<R> values) {
    return new MirrorList<>(this, values, getReverseMirror());
  }

  /**
   * Returns the instantiated {@link #mirrorList}. If the {@link #mirrorList} is
   * not instantiated, this method uses the {@link #reflections} as the
   * underlying list to be consumed by {@link #newMirrorInstance(List)}.
   *
   * @param require If {@code true}, the {@link #mirrorList} will be initialized
   *          given that {@link #reflections} is not null; if {@code false}, the
   *          {@link #mirrorList} will be initialized given that
   *          {@link #reflections} is not null and not empty.
   * @return The instantiated {@link #mirrorList}.
   */
  @SuppressWarnings("unchecked")
  private MirrorList<R,V> mirrorList(final boolean require) {
    if (reflections != null && (require || reflections.size() > 0)) {
      mirrorList = newMirrorInstance(reflections);
      reflections = null;

      final int comparison = Integer.compare(target.size(), mirrorList.target.size());
      if (comparison == 0)
        return mirrorList;

      final List<Object> less;
      final List<Object> more;
      if (comparison < 1) {
        less = target;
        more = mirrorList.target;
      }
      else {
        less = mirrorList.target;
        more = target;
      }

      final boolean unlocked = unlock();
      for (int i = less.size(), len = more.size(); i < len; ++i)
        less.add(PENDING);

      lock(unlocked);
    }

    return mirrorList;
  }

  /**
   * Returns the {@link MirrorList} instance of type {@code <R>} that represents
   * the one-to-one mapping with its mirror of type {@code <V>} (i.e.
   * {@code this} instance). Changes to the {@link MirrorList} will be reflected
   * in {@code this} instance, and vice-versa.
   *
   * @return The {@link MirrorList} instance of type {@code <R>} that retains
   *         the one-to-one mapping with its mirror of type {@code <V>} (i.e.
   *         {@code this} instance).
   */
  public MirrorList<R,V> getMirrorList() {
    return mirrorList(true);
  }

  /**
   * Reverses this {@link MirrorList}, effectively switching {@code <V>} to
   * {@code <R>}, and vice-versa. This method is the in-place version of
   * {@link #getMirrorList()}.
   * <p>
   * If this {@link MirrorList} has a not-null {@link #mirrorList}, the internal
   * states of the two lists are swapped, thus reversing the {@link #mirrorList}
   * as well.
   *
   * @return This list, with its {@code <V>} and {@code <R>} types reversed.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public MirrorList<R,V> reverse() {
    if (this.reflections != null) {
      final List tempList = this.target;
      this.target = this.reflections;
      this.reflections = tempList;

      final Mirror tempMirror = mirror;
      mirror = (Mirror)(reverse != null ? reverse : mirror.reverse());
      reverse = tempMirror;
    }
    else if (mirrorList != null) {
      final List tempList = this.target;
      this.target = this.mirrorList.target;
      this.mirrorList.target = tempList;

      final Mirror tempMirror = mirrorList.mirror;
      mirrorList.mirror = reverse = (Mirror)mirror;
      mirrorList.reverse = mirror = tempMirror;

      mirrorList.lock(true);
    }
    else {
      throw new IllegalStateException();
    }

    lock(true);
    return (MirrorList<R,V>)this;
  }

  /**
   * Returns the {@link Mirror}.
   *
   * @return The {@link Mirror}.
   */
  public Mirror<V,R> getMirror() {
    return this.mirror;
  }

  /**
   * Returns the reverse {@link Mirror}, and caches it for subsequent retrieval,
   * avoiding reinstantiation.
   *
   * @return The reverse {@link Mirror}.
   */
  protected Mirror<R,V> getReverseMirror() {
    return reverse == null ? reverse = mirror.reverse() : reverse;
  }

  /**
   * Locks the {@link #target underlying list} to detect concurrent
   * modification. Specifically, this method calls {@link #iterator()
   * target.iterator()} and saves its reference for examination during the next
   * method call that is made to the {@link MirrorList}. If the call to
   * {@link Iterator#next()} on the {@link #targetLock saved iterator} results
   * in a {@link ConcurrentModificationException}, it means that the
   * {@link #target underlying list} was modified outside of the
   * {@link MirrorList}. Such modifications are not allowed, because they risk
   * compromising the data integrity of the this list and its
   * {@link #mirrorList}.
   *
   * @param unlocked If {@code true}, this list is unlocked and will be locked;
   *          if {@code false}, this list is already locked and will not be
   *          relocked.
   */
  protected void lock(final boolean unlocked) {
    if (unlocked) {
      targetLock = target.iterator();
      if (mirrorList != null)
        getMirrorList().targetLock = mirrorList.target.iterator();
    }
  }

  /**
   * Unlocks the {@link #target underlying list} and checks for concurrent
   * modification. Specifically, this method calls {@link Iterator#next()} on
   * {@link #targetLock}, which may result in a
   * {@link ConcurrentModificationException} if the {@link #target underlying
   * list} was modified outside of this {@link MirrorList}.
   *
   * @return Whether this list was locked prior to the execution of this method.
   * @throws ConcurrentModificationException If the {@link #target underlying
   *           list} was modified outside of the {@link MirrorList}
   */
  protected boolean unlock() throws ConcurrentModificationException {
    if (inited && targetLock == null)
      return false;

    if (targetLock != null) {
      try {
        targetLock.next();
        if (mirrorList != null && getMirrorList().targetLock != null)
          getMirrorList().targetLock.next();
      }
      catch (final NoSuchElementException ignored) {
      }

      targetLock = null;
    }
    else {
      inited = true;
    }

    return true;
  }

  @Override
  public int size() {
    return reflections != null && reflections.size() > 0 ? reflections.size() : super.size();
  }

  @Override
  @SuppressWarnings("unchecked")
  protected boolean beforeAdd(final int index, final V element) {
    unlocked = unlock();
    if (mirrorList(false) != null)
      mirrorList.target.add(index, PENDING);

    lock(unlocked);
    return true;
  }

  @Override
  protected void afterAdd(final int index, final V element, final RuntimeException e) {
    lock(unlocked);
  }

  @Override
  protected boolean beforeRemove(final int index) {
    unlocked = unlock();
    if (mirrorList(false) != null)
      mirrorList.target.remove(index);

    lock(unlocked);
    return true;
  }

  @Override
  protected void afterRemove(final Object element, final RuntimeException e) {
    lock(unlocked);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected boolean beforeSet(final int index, final V newElement) {
    unlocked = unlock();
    if (mirrorList(false) != null)
      mirrorList.target.set(index, PENDING);

    lock(unlocked);
    return true;
  }

  @Override
  protected void afterSet(final int index, final V oldElement, final RuntimeException e) {
    lock(unlocked);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void beforeGet(final int index, final ListIterator<V> iterator) {
    unlocked = unlock();
    mirrorList(true);
    final Object obj = target.get(index);
    if (obj == PENDING) {
      final V value = mirror.reflectionToValue((R)mirrorList.target.get(index));
      target.set(index, value);
    }
  }

  @Override
  protected void afterGet(final int index, final V element, final ListIterator<? super V> iterator, final RuntimeException e) {
    lock(unlocked);
  }

  @Override
  @SuppressWarnings("unchecked")
  public MirrorList<V,R> subList(final int fromIndex, final int toIndex) {
    Assertions.assertRange(fromIndex, toIndex, size());
    return newInstance(target.subList(fromIndex, toIndex), (reflections != null ? reflections : mirrorList.target).subList(fromIndex, toIndex));
  }
}