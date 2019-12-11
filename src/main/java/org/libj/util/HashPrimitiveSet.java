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

package org.libj.util;

/**
 * An abstract class providing static methods for concrete implementations of
 * <a href="https://en.wikipedia.org/wiki/Open_addressing">open-addressing
 * (closed hashing) with linear-probing for collision resolution</a> algorithm,
 * with allocation-free operation in steady state when expanded.
 *
 * @see HashIntSet
 * @see HashLongSet
 * @see HashDoubleSet
 */
public abstract class HashPrimitiveSet {
  /**
   * Returns the hash for the specified value and mask.
   *
   * @param value The value to be hashed.
   * @param mask The mask to be applied (must be a power of 2, minus 1).
   * @return The hash of the specified value.
   */
  protected static int hash(final int value, final int mask) {
    return (value * 31) & mask;
  }

  /**
   * Returns the next index for the specified index and mask.
   *
   * @param index The index from which to calculate the next index.
   * @param mask The mask to be applied (must be a power of 2, minus 1).
   * @return The next index for the specified index.
   */
  protected static int nextIndex(final int index, final int mask) {
    return (index + 1) & mask;
  }

  /**
   * Returns the next power of 2 for the value that is greater than or equal to
   * the specified value.
   * <p>
   * If {@code value <= 0}, this method returns {@code 1}.
   * <p>
   * <b>Note:</b> This method is not suitable for {@link Integer#MIN_VALUE} or
   * numbers greater than {@code 2^30}.
   *
   * @param value Value from which to return the next power of 2.
   * @return The next power of 2 from the specified value, or the value itself
   *         if it is a power of 2.
   */
  protected static int findNextPositivePowerOfTwo(final int value) {
    return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
  }
}