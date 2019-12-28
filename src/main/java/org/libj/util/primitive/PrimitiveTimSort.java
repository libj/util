/* Copyright (c) 2019 LibJ
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

abstract class PrimitiveTimSort {
  /**
   * This is the minimum sized sequence that will be merged. Shorter sequences
   * will be lengthened by calling binarySort. If the entire array is less than
   * this length, no merges will be performed. This constant should be a power
   * of two. It was 64 in Tim Peter's C implementation, but 32 was empirically
   * determined to work better in this implementation. In the unlikely event
   * that you set this constant to be a number that's not a power of two, you'll
   * need to change the {@link #minRunLength} computation. If you decrease this
   * constant, you must change the stackLen computation in the IntTrimSort
   * constructor, or you risk an ArrayOutOfBounds exception. See listsort.txt
   * for a discussion of the minimum stack length required as a function of the
   * length of the array being sorted and the minimum merge sequence length.
   */
  static final int MIN_MERGE = 32;

  /**
   * When we get into galloping mode, we stay there until both runs win less
   * often than MIN_GALLOP consecutive times.
   */
  static final int MIN_GALLOP = 7;

  /**
   * Maximum initial size of tmp array, which is used for merging. The array can
   * grow to accommodate demand. Unlike Tim's original C version, we do not
   * allocate this much storage when sorting smaller arrays. This change was
   * required for performance.
   */
  static final int INITIAL_TMP_STORAGE_LENGTH = 256;

  int tmpBase; // base of tmp array slice
  int tmpLen; // length of tmp array slice

  /**
   * A stack of pending runs yet to be merged. Run i starts at address base[i]
   * and extends for len[i] elements. It's always true (so long as the indices
   * are in bounds) that: runBase[i] + runLen[i] == runBase[i + 1] so we could
   * cut the storage for this, but it's a minor amount, and keeping all the info
   * explicit simplifies the code.
   */
  int stackSize; // Number of pending runs on stack
  int[] runBase;
  int[] runLen;

  /**
   * Returns the minimum acceptable run length for an array of the specified
   * length. Natural runs shorter than this will be extended with
   * {@code binarySort()}. Roughly speaking, the computation is: If n <
   * MIN_MERGE, return n (it's too small to bother with fancy stuff). Else if n
   * is an exact power of 2, return MIN_MERGE/2. Else return an int k,
   * MIN_MERGE/2 <= k <= MIN_MERGE, such that n/k is close to, but strictly less
   * than, an exact power of 2. For the rationale, see listsort.txt.
   *
   * @param n the length of the array to be sorted
   * @return the length of the minimum run to be merged
   */
  static int minRunLength(int n) {
    assert n >= 0;
    int r = 0; // Becomes 1 if any 1 bits are shifted off
    while (n >= MIN_MERGE) {
      r |= (n & 1);
      n >>= 1;
    }

    return n + r;
  }

  /**
   * Pushes the specified run onto the pending-run stack.
   *
   * @param runBase index of the first element in the run
   * @param runLen the number of elements in the run
   */
  void pushRun(final int runBase, final int runLen) {
    this.runBase[stackSize] = runBase;
    this.runLen[stackSize] = runLen;
    ++stackSize;
  }

  /**
   * Examines the stack of runs waiting to be merged and merges adjacent runs
   * until the stack invariants are reestablished: 1. runLen[i - 3] > runLen[i -
   * 2] + runLen[i - 1] 2. runLen[i - 2] > runLen[i - 1] This method is called
   * each time a new run is pushed onto the stack, so the invariants are
   * guaranteed to hold for i < stackSize upon entry to the method. Thanks to
   * Stijn de Gouw, Jurriaan Rot, Frank S. de Boer, Richard Bubel and Reiner
   * Hahnle, this is fixed with respect to the analysis in "On the Worst-Case
   * Complexity of IntTrimSort" by Nicolas Auger, Vincent Jug, Cyril Nicaud, and
   * Carine Pivoteau.
   */
  void mergeCollapse() {
    while (stackSize > 1) {
      int n = stackSize - 2;
      if (n > 0 && runLen[n - 1] <= runLen[n] + runLen[n + 1] || n > 1 && runLen[n - 2] <= runLen[n] + runLen[n - 1]) {
        if (runLen[n - 1] < runLen[n + 1])
          --n;
      }
      else if (n < 0 || runLen[n] > runLen[n + 1]) {
        break; // Invariant is established
      }

      mergeAt(n);
    }
  }

  /**
   * Merges all runs on the stack until only one remains. This method is called
   * once, to complete the sort.
   */
  void mergeForceCollapse() {
    while (stackSize > 1) {
      int n = stackSize - 2;
      if (n > 0 && runLen[n - 1] < runLen[n + 1])
        --n;

      mergeAt(n);
    }
  }

  abstract void mergeAt(int i);
}