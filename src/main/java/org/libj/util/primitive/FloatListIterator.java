/* Copyright (c) 2014 LibJ
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

package org.libj.util.primitive;

import java.util.NoSuchElementException;

/**
 * A replica of the {@link java.util.ListIterator} interface that defines
 * synonymous methods for the iteration over {@code float} values instead of
 * Object references.
 */
public interface FloatListIterator extends FloatIterator {
  /**
   * Returns {@code true} if this list iterator has more values when
   * traversing the list in the forward direction. (In other words,
   * returns {@code true} if {@link #next} would return an value rather
   * than throwing an exception.)
   *
   * @return {@code true} if the list iterator has more values when
   *         traversing the list in the forward direction.
   */
  @Override
  boolean hasNext();

  /**
   * Returns the next value in the list and advances the cursor position.
   * This method may be called repeatedly to iterate through the list,
   * or intermixed with calls to {@link #previous} to go back and forth.
   * (Note that alternating calls to {@code next} and {@code previous}
   * will return the same value repeatedly.)
   *
   * @return The next value in the list.
   * @throws NoSuchElementException If the iteration has no next value.
   */
  @Override
  float next();

  /**
   * Returns {@code true} if this list iterator has more values when
   * traversing the list in the reverse direction. (In other words,
   * returns {@code true} if {@link #previous} would return an value
   * rather than throwing an exception.)
   *
   * @return {@code true} if the list iterator has more values when
   *         traversing the list in the reverse direction.
   */
  boolean hasPrevious();

  /**
   * Returns the previous value in the list and moves the cursor
   * position backwards. This method may be called repeatedly to
   * iterate through the list backwards, or intermixed with calls to
   * {@link #next} to go back and forth. (Note that alternating calls
   * to {@code next} and {@code previous} will return the same
   * value repeatedly.)
   *
   * @return The previous value in the list.
   * @throws NoSuchElementException If the iteration has no previous
   *         value.
   */
  float previous();

  /**
   * Returns the index of the value that would be returned by a
   * subsequent call to {@link #next}. (Returns list size if the list
   * iterator is at the end of the list.)
   *
   * @return The index of the value that would be returned by a
   *         subsequent call to {@code next}, or list size if the list
   *         iterator is at the end of the list.
   */
  int nextIndex();

  /**
   * Returns the index of the value that would be returned by a
   * subsequent call to {@link #previous}. (Returns -1 if the list
   * iterator is at the beginning of the list.)
   *
   * @return The index of the value that would be returned by a
   *         subsequent call to {@code previous}, or -1 if the list
   *         iterator is at the beginning of the list.
   */
  int previousIndex();

  /**
   * Removes from the list the last value that was returned by {@link
   * #next} or {@link #previous} (optional operation). This call can
   * only be made once per call to {@code next} or {@code previous}.
   * It can be made only if {@link #add} has not been
   * called after the last call to {@code next} or {@code previous}.
   *
   * @throws UnsupportedOperationException If the {@code remove}
   *         operation is not supported by this list iterator.
   * @throws IllegalStateException If neither {@code next} nor
   *         {@code previous} have been called, or {@code remove} or
   *         {@code add} have been called after the last call to
   *         {@code next} or {@code previous}.
   */
  @Override
  void remove();

  /**
   * Replaces the last value returned by {@link #next} or
   * {@link #previous} with the specified value (optional operation).
   * This call can be made only if neither {@link #remove} nor {@link
   * #add} have been called after the last call to {@code next} or
   * {@code previous}.
   *
   * @param value The value with which to replace the last value returned by
   *          {@code next} or {@code previous}.
   * @throws UnsupportedOperationException If the {@code set} operation
   *         is not supported by this list iterator.
   * @throws IllegalArgumentException If some aspect of the specified
   *         value prevents it from being added to this list.
   * @throws IllegalStateException If neither {@code next} nor
   *         {@code previous} have been called, or {@code remove} or
   *         {@code add} have been called after the last call to
   *         {@code next} or {@code previous}.
   */
  void set(float value);

  /**
   * Inserts the specified value into the list (optional operation).
   * The value is inserted immediately before the value that
   * would be returned by {@link #next}, if any, and after the value
   * that would be returned by {@link #previous}, if any. (If the
   * list contains no values, the new value becomes the sole value
   * on the list.)  The new value is inserted before the implicit
   * cursor: a subsequent call to {@code next} would be unaffected, and a
   * subsequent call to {@code previous} would return the new value.
   * (This call increases by one the value that would be returned by a
   * call to {@code nextIndex} or {@code previousIndex}.)
   *
   * @param value The value to insert.
   * @throws UnsupportedOperationException If the {@code add} method is
   *         not supported by this list iterator.
   * @throws IllegalArgumentException If some aspect of this value
   *         prevents it from being added to this list.
   */
  void add(float value);
}