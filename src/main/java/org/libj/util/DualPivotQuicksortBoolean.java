/* Copyright (c) 2023 LibJ
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
 * Copied from {@link java.util.DualPivotQuicksort}.
 */
class DualPivotQuicksortBoolean {
  /**
   * The maximum number of runs in merge sort.
   */
  private static final int MAX_RUN_COUNT = 67;

  /**
   * The maximum length of run in merge sort.
   */
  private static final int MAX_RUN_LENGTH = 33;

  /**
   * If the length of an array to be sorted is less than this constant, Quicksort is used in preference to merge sort.
   */
  private static final int QUICKSORT_THRESHOLD = 286;

  /**
   * If the length of an array to be sorted is less than this constant, insertion sort is used in preference to Quicksort.
   */
  private static final int INSERTION_SORT_THRESHOLD = 47;

  /**
   * Checks that {@code fromIndex} and {@code toIndex} are in the range and throws an exception if they aren't.
   */
  private static void rangeCheck(int arrayLength, int fromIndex, int toIndex) {
    if (fromIndex > toIndex) {
      throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
    }
    if (fromIndex < 0) {
      throw new ArrayIndexOutOfBoundsException(fromIndex);
    }
    if (toIndex > arrayLength) {
      throw new ArrayIndexOutOfBoundsException(toIndex);
    }
  }

  /**
   * Sorts the specified range of the array into ascending order. The range to be sorted extends from the index {@code fromIndex},
   * inclusive, to the index {@code toIndex}, exclusive. If {@code fromIndex == toIndex}, the range to be sorted is empty.
   * <p>
   * Implementation note: The sorting algorithm is a Dual-Pivot Quicksort by Vladimir Yaroslavskiy, Jon Bentley, and Joshua Bloch.
   * This algorithm offers O(n log(n)) performance on many data sets that cause other quicksorts to degrade to quadratic performance,
   * and is typically faster than traditional (one-pivot) Quicksort implementations.
   *
   * @param a the array to be sorted
   * @param fromIndex the index of the first element, inclusive, to be sorted
   * @param toIndex the index of the last element, exclusive, to be sorted
   * @throws IllegalArgumentException if {@code fromIndex > toIndex}
   * @throws ArrayIndexOutOfBoundsException if {@code fromIndex < 0} or {@code toIndex > a.length}
   */
  public static void sort(boolean[] a, int fromIndex, int toIndex) {
    rangeCheck(a.length, fromIndex, toIndex);
    DualPivotQuicksortBoolean.sort(a, fromIndex, toIndex - 1, null, 0, 0);
  }

  /**
   * Sorts the specified range of the array using the given workspace array slice if possible for merging
   *
   * @param a the array to be sorted
   * @param left the index of the first element, inclusive, to be sorted
   * @param right the index of the last element, inclusive, to be sorted
   * @param work a workspace array (slice)
   * @param workBase origin of usable space in work array
   * @param workLen usable size of work array
   */
  static void sort(boolean[] a, int left, int right, boolean[] work, int workBase, int workLen) {
    // Use Quicksort on small arrays
    if (right - left < QUICKSORT_THRESHOLD) {
      sort(a, left, right, true);
      return;
    }

    /*
     * Index run[i] is the start of i-th run (ascending or descending sequence).
     */
    int[] run = new int[MAX_RUN_COUNT + 1];
    int count = 0;
    run[0] = left;

    // Check if the array is nearly sorted
    for (int k = left; k < right; run[count] = k) { // [A]
      if (!a[k] && a[k + 1]) { // ascending
        while (++k <= right && (!a[k - 1] || a[k]));
      }
      else if (a[k] && !a[k + 1]) { // descending
        while (++k <= right && (a[k - 1] || !a[k]));
        for (int lo = run[count] - 1, hi = k; ++lo < --hi;) { // [A]
          boolean t = a[lo];
          a[lo] = a[hi];
          a[hi] = t;
        }
      }
      else { // equal
        for (int m = MAX_RUN_LENGTH; ++k <= right && a[k - 1] == a[k];) { // [A]
          if (--m == 0) {
            sort(a, left, right, true);
            return;
          }
        }
      }

      /*
       * The array is not highly structured, use Quicksort instead of merge sort.
       */
      if (++count == MAX_RUN_COUNT) {
        sort(a, left, right, true);
        return;
      }
    }

    // Check special cases
    // Implementation note: variable "right" is increased by 1.
    if (run[count] == right++) { // The last run contains one element
      run[++count] = right;
    }
    else if (count == 1) { // The array is already sorted
      return;
    }

    // Determine alternation base for merge
    byte odd = 0;
    for (int n = 1; (n <<= 1) < count; odd ^= 1); // [X]

    // Use or create temporary array b for merging
    boolean[] b;         // temp array; alternates with a
    int ao, bo;        // array offsets from 'left'
    int blen = right - left; // space needed for b
    if (work == null || workLen < blen || workBase + blen > work.length) {
      work = new boolean[blen];
      workBase = 0;
    }
    if (odd == 0) {
      System.arraycopy(a, left, work, workBase, blen);
      b = a;
      bo = 0;
      a = work;
      ao = workBase - left;
    }
    else {
      b = work;
      ao = 0;
      bo = workBase - left;
    }

    // Merging
    for (int last; count > 1; count = last) { // [N]
      for (int k = (last = 0) + 2; k <= count; k += 2) { // [A]
        int hi = run[k], mi = run[k - 1];
        for (int i = run[k - 2], p = i, q = mi; i < hi; ++i) { // [X]
          if (q >= hi || p < mi && (!a[p + ao] || a[q + ao])) {
            b[i + bo] = a[p++ + ao];
          }
          else {
            b[i + bo] = a[q++ + ao];
          }
        }
        run[++last] = hi;
      }
      if ((count & 1) != 0) {
        for (int i = right, lo = run[count - 1]; --i >= lo; b[i + bo] = a[i + ao]); // [A]
        run[++last] = right;
      }
      boolean[] t = a;
      a = b;
      b = t;
      int o = ao;
      ao = bo;
      bo = o;
    }
  }

  /**
   * Sorts the specified range of the array by Dual-Pivot Quicksort.
   *
   * @param a the array to be sorted
   * @param left the index of the first element, inclusive, to be sorted
   * @param right the index of the last element, inclusive, to be sorted
   * @param leftmost indicates if this part is the leftmost in the range
   */
  private static void sort(boolean[] a, int left, int right, boolean leftmost) {
    int length = right - left + 1;

    // Use insertion sort on tiny arrays
    if (length < INSERTION_SORT_THRESHOLD) {
      if (leftmost) {
        /*
         * Traditional (without sentinel) insertion sort, optimized for server VM, is used in case of the leftmost part.
         */
        for (int i = left, j = i; i < right; j = ++i) { // [A]
          boolean ai = a[i + 1];
          while (!ai && a[j]) {
            a[j + 1] = a[j];
            if (j-- == left) {
              break;
            }
          }
          a[j + 1] = ai;
        }
      }
      else {
        /*
         * Skip the booleanest ascending sequence.
         */
        do {
          if (left >= right) {
            return;
          }
        }
        while (a[++left] || !a[left - 1]);

        /*
         * Every element from adjoining part plays the role of sentinel, therefore this allows us to avoid the left range check on each
         * iteration. Moreover, we use the more optimized algorithm, so called pair insertion sort, which is faster (in the context of
         * Quicksort) than traditional implementation of insertion sort.
         */
        for (int k = left; ++left <= right; k = ++left) { // [A]
          boolean a1 = a[k], a2 = a[left];

          if (!a1 && a2) {
            a2 = a1;
            a1 = a[left];
          }
          while (!a1 && a[--k]) {
            a[k + 2] = a[k];
          }
          a[++k + 1] = a1;

          while (!a2 && a[--k]) {
            a[k + 1] = a[k];
          }
          a[k + 1] = a2;
        }
        boolean last = a[right];

        while (!last && a[--right]) {
          a[right + 1] = a[right];
        }
        a[right + 1] = last;
      }
      return;
    }

    // Inexpensive approximation of length / 7
    int seventh = (length >> 3) + (length >> 6) + 1;

    /*
     * Sort five evenly spaced elements around (and including) the center element in the range. These elements will be used for pivot
     * selection as described below. The choice for spacing these elements was empirically determined to work well on a wide variety of
     * inputs.
     */
    int e3 = (left + right) >>> 1; // The midpoint
    int e2 = e3 - seventh;
    int e1 = e2 - seventh;
    int e4 = e3 + seventh;
    int e5 = e4 + seventh;

    // Sort these elements using insertion sort
    if (!a[e2] && a[e1]) { boolean t = a[e2]; a[e2] = a[e1]; a[e1] = t; }

    if (!a[e3] && a[e2]) {
      boolean t = a[e3];
      a[e3] = a[e2];
      a[e2] = t;
      if (!t && a[e1]) { a[e2] = a[e1]; a[e1] = t; }
    }
    if (!a[e4] && a[e3]) {
      boolean t = a[e4];
      a[e4] = a[e3];
      a[e3] = t;
      if (!t && a[e2]) {
        a[e3] = a[e2];
        a[e2] = t;
        if (!t && a[e1]) { a[e2] = a[e1]; a[e1] = t; }
      }
    }
    if (!a[e5] && a[e4]) {
      boolean t = a[e5];
      a[e5] = a[e4];
      a[e4] = t;
      if (!t && a[e3]) {
        a[e4] = a[e3];
        a[e3] = t;
        if (!t && a[e2]) {
          a[e3] = a[e2];
          a[e2] = t;
          if (!t && a[e1]) { a[e2] = a[e1]; a[e1] = t; }
        }
      }
    }

    // Pointers
    int less = left;  // The index of the first element of center part
    int great = right; // The index before the first element of right part

    if (a[e1] != a[e2] && a[e2] != a[e3] && a[e3] != a[e4] && a[e4] != a[e5]) {
      /*
       * Use the second and fourth of the five sorted elements as pivots. These values are inexpensive approximations of the first and
       * second terciles of the array. Note that pivot1 <= pivot2.
       */
      boolean pivot1 = a[e2];
      boolean pivot2 = a[e4];

      /*
       * The first and the last elements to be sorted are moved to the locations formerly occupied by the pivots. When partitioning is
       * complete, the pivots are swapped back into their final positions, and excluded from subsequent sorting.
       */
      a[e2] = a[left];
      a[e4] = a[right];

      /*
       * Skip elements, which are less or greater than pivot values.
       */
      while (!a[++less] && pivot1);
      while (a[--great] && !pivot2);

      /*
       * Partitioning: left part center part right part +--------------------------------------------------------------+ | < pivot1 |
       * pivot1 <= && <= pivot2 | ? | > pivot2 | +--------------------------------------------------------------+ ^ ^ ^ | | | less k great
       * Invariants: all in (left, less) < pivot1 pivot1 <= all in [less, k) <= pivot2 all in (great, right) > pivot2 Pointer k is the
       * first index of ?-part.
       */
      outer:
      for (int k = less - 1; ++k <= great;) { // [A]
        boolean ak = a[k];
        if (!ak && pivot1) { // Move a[k] to left part
          a[k] = a[less];
          /*
           * Here and below we use "a[i] = b; i++;" instead of "a[i++] = b;" due to performance issue.
           */
          a[less] = ak;
          ++less;
        }
        else if (ak && !pivot2) { // Move a[k] to right part
          while (a[great] && !pivot2) {
            if (great-- == k) {
              break outer;
            }
          }
          if (!a[great] && pivot1) { // a[great] <= pivot2
            a[k] = a[less];
            a[less] = a[great];
            ++less;
          }
          else { // pivot1 <= a[great] <= pivot2
            a[k] = a[great];
          }
          /*
           * Here and below we use "a[i] = b; i--;" instead of "a[i--] = b;" due to performance issue.
           */
          a[great] = ak;
          --great;
        }
      }

      // Swap pivots into their final positions
      a[left] = a[less - 1];
      a[less - 1] = pivot1;
      a[right] = a[great + 1];
      a[great + 1] = pivot2;

      // Sort left and right parts recursively, excluding known pivots
      sort(a, left, less - 2, leftmost);
      sort(a, great + 2, right, false);

      /*
       * If center part is too large (comprises > 4/7 of the array), swap internal pivot values to ends.
       */
      if (less < e1 && e5 < great) {
        /*
         * Skip elements, which are equal to pivot values.
         */
        while (a[less] == pivot1) {
          ++less;
        }

        while (a[great] == pivot2) {
          --great;
        }

        /*
         * Partitioning: left part center part right part +----------------------------------------------------------+ | == pivot1 | pivot1
         * < && < pivot2 | ? | == pivot2 | +----------------------------------------------------------+ ^ ^ ^ | | | less k great Invariants:
         * all in (*, less) == pivot1 pivot1 < all in [less, k) < pivot2 all in (great, *) == pivot2 Pointer k is the first index of ?-part.
         */
        outer:
        for (int k = less - 1; ++k <= great;) { // [A]
          boolean ak = a[k];
          if (ak == pivot1) { // Move a[k] to left part
            a[k] = a[less];
            a[less] = ak;
            ++less;
          }
          else if (ak == pivot2) { // Move a[k] to right part
            while (a[great] == pivot2) {
              if (great-- == k) {
                break outer;
              }
            }
            if (a[great] == pivot1) { // a[great] < pivot2
              a[k] = a[less];
              /*
               * Even though a[great] equals to pivot1, the assignment a[less] = pivot1 may be incorrect, if a[great] and pivot1 are
               * floating-point zeros of different signs. Therefore in float and double sorting methods we have to use more accurate assignment
               * a[less] = a[great].
               */
              a[less] = pivot1;
              ++less;
            }
            else { // pivot1 < a[great] < pivot2
              a[k] = a[great];
            }
            a[great] = ak;
            --great;
          }
        }
      }

      // Sort center part recursively
      sort(a, less, great, false);

    }
    else { // Partitioning with one pivot
      /*
       * Use the third of the five sorted elements as pivot. This value is inexpensive approximation of the median.
       */
      boolean pivot = a[e3];

      /*
       * Partitioning degenerates to the traditional 3-way (or "Dutch National Flag") schema: left part center part right part
       * +-------------------------------------------------+ | < pivot | == pivot | ? | > pivot |
       * +-------------------------------------------------+ ^ ^ ^ | | | less k great Invariants: all in (left, less) < pivot all in
       * [less, k) == pivot all in (great, right) > pivot Pointer k is the first index of ?-part.
       */
      for (int k = less; k <= great; ++k) { // [A]
        if (a[k] == pivot) {
          continue;
        }
        boolean ak = a[k];
        if (!ak && pivot) { // Move a[k] to left part
          a[k] = a[less];
          a[less] = ak;
          ++less;
        }
        else { // a[k] > pivot - Move a[k] to right part
          while (a[great] && !pivot) {
            --great;
          }
          if (!a[great] && pivot) { // a[great] <= pivot
            a[k] = a[less];
            a[less] = a[great];
            ++less;
          }
          else { // a[great] == pivot
            /*
             * Even though a[great] equals to pivot, the assignment a[k] = pivot may be incorrect, if a[great] and pivot are floating-point
             * zeros of different signs. Therefore in float and double sorting methods we have to use more accurate assignment a[k] = a[great].
             */
            a[k] = pivot;
          }
          a[great] = ak;
          --great;
        }
      }

      /*
       * Sort left and right parts recursively. All elements from center part are equal and, therefore, already sorted.
       */
      sort(a, left, less - 1, leftmost);
      sort(a, great + 1, right, false);
    }
  }
}