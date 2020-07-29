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

package org.libj.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.libj.lang.Assertions;
import org.libj.util.primitive.ByteComparator;
import org.libj.util.primitive.CharComparator;
import org.libj.util.primitive.DoubleComparator;
import org.libj.util.primitive.FloatComparator;
import org.libj.util.primitive.IntComparator;
import org.libj.util.primitive.LongComparator;
import org.libj.util.primitive.PrimitiveSort;
import org.libj.util.primitive.ShortComparator;

/**
 * Utility functions for operations pertaining to arrays.
 */
public final class ArrayUtil extends PrimitiveSort {
  /**
   * The empty {@code Object[]} array.
   */
  public static final Object[] EMPTY_ARRAY = {};

  /**
   * Returns true if the two specified arrays of bytes, over the provided
   * ranges, are <i>equal</i> to one another.
   * <p>
   * Two arrays are considered equal if the number of elements covered by each
   * range is the same, and all corresponding pairs of elements over the
   * provided ranges in the two arrays are equal. In other words, two arrays are
   * equal if they contain, over the provided ranges, the same elements in the
   * same order.
   *
   * @param a The first array to be tested for equality.
   * @param aFromIndex The index (inclusive) of the first element in the first
   *          array to be tested.
   * @param aToIndex The index (exclusive) of the last element in the first
   *          array to be tested.
   * @param b The second array to be tested from equality.
   * @param bFromIndex The index (inclusive) of the first element in the second
   *          array to be tested.
   * @param bToIndex The index (exclusive) of the last element in the second
   *          array to be tested.
   * @return {@code true} If the two arrays, over the provided ranges, are
   *         equal.
   * @throws IllegalArgumentException If {@code aFromIndex > aToIndex} or if
   *           {@code bFromIndex > bToIndex}.
   * @throws ArrayIndexOutOfBoundsException If
   *           {@code aFromIndex < 0 or aToIndex > a.length} or if
   *           {@code bFromIndex < 0 or bToIndex > b.length}.
   * @throws NullPointerException If either array is null.
   */
  public static boolean equals(final byte[] a, int aFromIndex, final int aToIndex, final byte[] b, int bFromIndex, final int bToIndex) {
    Assertions.assertRangeArray(aFromIndex, aToIndex, a.length);
    Assertions.assertRangeArray(bFromIndex, bToIndex, b.length);

    final int aLength = aToIndex - aFromIndex;
    final int bLength = bToIndex - bFromIndex;
    if (aLength != bLength)
      return false;

    for (int i = 0; i < aLength; ++i)
      if (a[aFromIndex++] != b[bFromIndex++])
        return false;

    return true;
  }

  /**
   * Returns true if the two specified arrays of {@code char}s, over the
   * provided ranges, are <i>equal</i> to one another.
   * <p>
   * Two arrays are considered equal if the number of elements covered by each
   * range is the same, and all corresponding pairs of elements over the
   * provided ranges in the two arrays are equal. In other words, two arrays are
   * equal if they contain, over the provided ranges, the same elements in the
   * same order.
   *
   * @param a The first array to be tested for equality.
   * @param aFromIndex The index (inclusive) of the first element in the first
   *          array to be tested.
   * @param aToIndex The index (exclusive) of the last element in the first
   *          array to be tested.
   * @param b The second array to be tested from equality.
   * @param bFromIndex The index (inclusive) of the first element in the second
   *          array to be tested.
   * @param bToIndex The index (exclusive) of the last element in the second
   *          array to be tested.
   * @return {@code true} If the two arrays, over the provided ranges, are
   *         equal.
   * @throws IllegalArgumentException If {@code aFromIndex > aToIndex} or if
   *           {@code bFromIndex > bToIndex}.
   * @throws ArrayIndexOutOfBoundsException If
   *           {@code aFromIndex < 0 or aToIndex > a.length} or if
   *           {@code bFromIndex < 0 or bToIndex > b.length}.
   * @throws NullPointerException If either array is null.
   */
  public static boolean equals(final char[] a, int aFromIndex, final int aToIndex, final char[] b, int bFromIndex, final int bToIndex) {
    Assertions.assertRangeArray(aFromIndex, aToIndex, a.length);
    Assertions.assertRangeArray(bFromIndex, bToIndex, b.length);

    final int aLength = aToIndex - aFromIndex;
    final int bLength = bToIndex - bFromIndex;
    if (aLength != bLength)
      return false;

    for (int i = 0; i < aLength; ++i)
      if (a[aFromIndex++] != b[bFromIndex++])
        return false;

    return true;
  }

  /**
   * Returns true if the two specified arrays of {@code short}s, over the
   * provided ranges, are <i>equal</i> to one another.
   * <p>
   * Two arrays are considered equal if the number of elements covered by each
   * range is the same, and all corresponding pairs of elements over the
   * provided ranges in the two arrays are equal. In other words, two arrays are
   * equal if they contain, over the provided ranges, the same elements in the
   * same order.
   *
   * @param a The first array to be tested for equality.
   * @param aFromIndex The index (inclusive) of the first element in the first
   *          array to be tested.
   * @param aToIndex The index (exclusive) of the last element in the first
   *          array to be tested.
   * @param b The second array to be tested from equality.
   * @param bFromIndex The index (inclusive) of the first element in the second
   *          array to be tested.
   * @param bToIndex The index (exclusive) of the last element in the second
   *          array to be tested.
   * @return {@code true} If the two arrays, over the provided ranges, are
   *         equal.
   * @throws IllegalArgumentException If {@code aFromIndex > aToIndex} or if
   *           {@code bFromIndex > bToIndex}.
   * @throws ArrayIndexOutOfBoundsException If
   *           {@code aFromIndex < 0 or aToIndex > a.length} or if
   *           {@code bFromIndex < 0 or bToIndex > b.length}.
   * @throws NullPointerException If either array is null.
   */
  public static boolean equals(final short[] a, int aFromIndex, final int aToIndex, final short[] b, int bFromIndex, final int bToIndex) {
    Assertions.assertRangeArray(aFromIndex, aToIndex, a.length);
    Assertions.assertRangeArray(bFromIndex, bToIndex, b.length);

    final int aLength = aToIndex - aFromIndex;
    final int bLength = bToIndex - bFromIndex;
    if (aLength != bLength)
      return false;

    for (int i = 0; i < aLength; ++i)
      if (a[aFromIndex++] != b[bFromIndex++])
        return false;

    return true;
  }

  /**
   * Returns true if the two specified arrays of {@code int}s, over the provided
   * ranges, are <i>equal</i> to one another.
   * <p>
   * Two arrays are considered equal if the number of elements covered by each
   * range is the same, and all corresponding pairs of elements over the
   * provided ranges in the two arrays are equal. In other words, two arrays are
   * equal if they contain, over the provided ranges, the same elements in the
   * same order.
   *
   * @param a The first array to be tested for equality.
   * @param aFromIndex The index (inclusive) of the first element in the first
   *          array to be tested.
   * @param aToIndex The index (exclusive) of the last element in the first
   *          array to be tested.
   * @param b The second array to be tested from equality.
   * @param bFromIndex The index (inclusive) of the first element in the second
   *          array to be tested.
   * @param bToIndex The index (exclusive) of the last element in the second
   *          array to be tested.
   * @return {@code true} If the two arrays, over the provided ranges, are
   *         equal.
   * @throws IllegalArgumentException If {@code aFromIndex > aToIndex} or if
   *           {@code bFromIndex > bToIndex}.
   * @throws ArrayIndexOutOfBoundsException If
   *           {@code aFromIndex < 0 or aToIndex > a.length} or if
   *           {@code bFromIndex < 0 or bToIndex > b.length}.
   * @throws NullPointerException If either array is null.
   */
  public static boolean equals(final int[] a, int aFromIndex, final int aToIndex, final int[] b, int bFromIndex, final int bToIndex) {
    Assertions.assertRangeArray(aFromIndex, aToIndex, a.length);
    Assertions.assertRangeArray(bFromIndex, bToIndex, b.length);

    final int aLength = aToIndex - aFromIndex;
    final int bLength = bToIndex - bFromIndex;
    if (aLength != bLength)
      return false;

    for (int i = 0; i < aLength; ++i)
      if (a[aFromIndex++] != b[bFromIndex++])
        return false;

    return true;
  }

  /**
   * Returns true if the two specified arrays of {@code long}s, over the
   * provided ranges, are <i>equal</i> to one another.
   * <p>
   * Two arrays are considered equal if the number of elements covered by each
   * range is the same, and all corresponding pairs of elements over the
   * provided ranges in the two arrays are equal. In other words, two arrays are
   * equal if they contain, over the provided ranges, the same elements in the
   * same order.
   *
   * @param a The first array to be tested for equality.
   * @param aFromIndex The index (inclusive) of the first element in the first
   *          array to be tested.
   * @param aToIndex The index (exclusive) of the last element in the first
   *          array to be tested.
   * @param b The second array to be tested from equality.
   * @param bFromIndex The index (inclusive) of the first element in the second
   *          array to be tested.
   * @param bToIndex The index (exclusive) of the last element in the second
   *          array to be tested.
   * @return {@code true} If the two arrays, over the provided ranges, are
   *         equal.
   * @throws IllegalArgumentException If {@code aFromIndex > aToIndex} or if
   *           {@code bFromIndex > bToIndex}.
   * @throws ArrayIndexOutOfBoundsException If
   *           {@code aFromIndex < 0 or aToIndex > a.length} or if
   *           {@code bFromIndex < 0 or bToIndex > b.length}.
   * @throws NullPointerException If either array is null.
   */
  public static boolean equals(final long[] a, int aFromIndex, final int aToIndex, final long[] b, int bFromIndex, final int bToIndex) {
    Assertions.assertRangeArray(aFromIndex, aToIndex, a.length);
    Assertions.assertRangeArray(bFromIndex, bToIndex, b.length);

    final int aLength = aToIndex - aFromIndex;
    final int bLength = bToIndex - bFromIndex;
    if (aLength != bLength)
      return false;

    for (int i = 0; i < aLength; ++i)
      if (a[aFromIndex++] != b[bFromIndex++])
        return false;

    return true;
  }

  /**
   * Returns true if the two specified arrays of {@code float}s, over the
   * provided ranges, are <i>equal</i> to one another.
   * <p>
   * Two arrays are considered equal if the number of elements covered by each
   * range is the same, and all corresponding pairs of elements over the
   * provided ranges in the two arrays are equal. In other words, two arrays are
   * equal if they contain, over the provided ranges, the same elements in the
   * same order.
   *
   * @param a The first array to be tested for equality.
   * @param aFromIndex The index (inclusive) of the first element in the first
   *          array to be tested.
   * @param aToIndex The index (exclusive) of the last element in the first
   *          array to be tested.
   * @param b The second array to be tested from equality.
   * @param bFromIndex The index (inclusive) of the first element in the second
   *          array to be tested.
   * @param bToIndex The index (exclusive) of the last element in the second
   *          array to be tested.
   * @return {@code true} If the two arrays, over the provided ranges, are
   *         equal.
   * @throws IllegalArgumentException If {@code aFromIndex > aToIndex} or if
   *           {@code bFromIndex > bToIndex}.
   * @throws ArrayIndexOutOfBoundsException If
   *           {@code aFromIndex < 0 or aToIndex > a.length} or if
   *           {@code bFromIndex < 0 or bToIndex > b.length}.
   * @throws NullPointerException If either array is null.
   */
  public static boolean equals(final float[] a, int aFromIndex, final int aToIndex, final float[] b, int bFromIndex, final int bToIndex) {
    Assertions.assertRangeArray(aFromIndex, aToIndex, a.length);
    Assertions.assertRangeArray(bFromIndex, bToIndex, b.length);

    final int aLength = aToIndex - aFromIndex;
    final int bLength = bToIndex - bFromIndex;
    if (aLength != bLength)
      return false;

    for (int i = 0; i < aLength; ++i)
      if (a[aFromIndex++] != b[bFromIndex++])
        return false;

    return true;
  }

  /**
   * Returns true if the two specified arrays of {@code double}s, over the
   * provided ranges, are <i>equal</i> to one another.
   * <p>
   * Two arrays are considered equal if the number of elements covered by each
   * range is the same, and all corresponding pairs of elements over the
   * provided ranges in the two arrays are equal. In other words, two arrays are
   * equal if they contain, over the provided ranges, the same elements in the
   * same order.
   *
   * @param a The first array to be tested for equality.
   * @param aFromIndex The index (inclusive) of the first element in the first
   *          array to be tested.
   * @param aToIndex The index (exclusive) of the last element in the first
   *          array to be tested.
   * @param b The second array to be tested from equality.
   * @param bFromIndex The index (inclusive) of the first element in the second
   *          array to be tested.
   * @param bToIndex The index (exclusive) of the last element in the second
   *          array to be tested.
   * @return {@code true} If the two arrays, over the provided ranges, are
   *         equal.
   * @throws IllegalArgumentException If {@code aFromIndex > aToIndex} or if
   *           {@code bFromIndex > bToIndex}.
   * @throws ArrayIndexOutOfBoundsException If
   *           {@code aFromIndex < 0 or aToIndex > a.length} or if
   *           {@code bFromIndex < 0 or bToIndex > b.length}.
   * @throws NullPointerException If either array is null.
   */
  public static boolean equals(final double[] a, int aFromIndex, final int aToIndex, final double[] b, int bFromIndex, final int bToIndex) {
    Assertions.assertRangeArray(aFromIndex, aToIndex, a.length);
    Assertions.assertRangeArray(bFromIndex, bToIndex, b.length);

    final int aLength = aToIndex - aFromIndex;
    final int bLength = bToIndex - bFromIndex;
    if (aLength != bLength)
      return false;

    for (int i = 0; i < aLength; ++i)
      if (a[aFromIndex++] != b[bFromIndex++])
        return false;

    return true;
  }

  /**
   * Returns true if the two specified arrays of objects, over the provided
   * ranges, are <i>equal</i> to one another.
   * <p>
   * Two arrays are considered equal if the number of elements covered by each
   * range is the same, and all corresponding pairs of elements over the
   * provided ranges in the two arrays are equal. In other words, two arrays are
   * equal if they contain, over the provided ranges, the same elements in the
   * same order.
   *
   * @param a The first array to be tested for equality.
   * @param aFromIndex The index (inclusive) of the first element in the first
   *          array to be tested.
   * @param aToIndex The index (exclusive) of the last element in the first
   *          array to be tested.
   * @param b The second array to be tested from equality.
   * @param bFromIndex The index (inclusive) of the first element in the second
   *          array to be tested.
   * @param bToIndex The index (exclusive) of the last element in the second
   *          array to be tested.
   * @return {@code true} If the two arrays, over the provided ranges, are
   *         equal.
   * @throws IllegalArgumentException If {@code aFromIndex > aToIndex} or if
   *           {@code bFromIndex > bToIndex}.
   * @throws ArrayIndexOutOfBoundsException If
   *           {@code aFromIndex < 0 or aToIndex > a.length} or if
   *           {@code bFromIndex < 0 or bToIndex > b.length}.
   * @throws NullPointerException If either array is null.
   */
  public static boolean equals(final Object[] a, int aFromIndex, final int aToIndex, final Object[] b, int bFromIndex, final int bToIndex) {
    Assertions.assertRangeArray(aFromIndex, aToIndex, a.length);
    Assertions.assertRangeArray(bFromIndex, bToIndex, b.length);

    final int aLength = aToIndex - aFromIndex;
    final int bLength = bToIndex - bFromIndex;
    if (aLength != bLength)
      return false;

    for (int i = 0; i < aLength; ++i) {
      final Object oa = a[aFromIndex++];
      final Object ob = b[bFromIndex++];
      if (!Objects.equals(oa, ob))
        return false;
    }

    return true;
  }

  /**
   * Returns the length of the specified array, summed with the lengths of all
   * nested arrays at every depth. The value of
   * {@code member.getClass().isArray()} is used to determine whether an array
   * member represents an array for further recursion.
   * <p>
   * Array members that reference an array are <i>not included</i> in the count.
   * This is the equivalent of calling:
   *
   * <pre>
   * {@code lengthDeep(Object[],false)}
   * </pre>
   *
   * @param a The array.
   * @return The length of the specified array, summed with the lengths of all
   *         nested arrays at every depth.
   * @throws NullPointerException If the specified array is null.
   */
  public static int lengthDeep(final Object[] a) {
    return lengthDeep(a, false);
  }

  /**
   * Returns the length of the specified array, summed with the lengths of all
   * nested arrays at every depth. The value of
   * {@code member.getClass().isArray()} is used to determine whether an array
   * member represents an array for further recursion.
   *
   * @param <T> The component type of the specified array.
   * @param a The array.
   * @param countArrayReferences If {@code true}, array members that reference
   *          an array are included in the count; if {@code false}, they are not
   *          included in the count.
   * @return The length of the specified array, summed with the lengths of all
   *         nested arrays at every depth.
   * @throws NullPointerException If the specified array is null.
   */
  public static <T>int lengthDeep(final T[] a, final boolean countArrayReferences) {
    return lengthDeep(a, null, countArrayReferences);
  }

  /**
   * Returns the length of the specified array, summed with the lengths of all
   * nested arrays at every depth. The specified resolver {@link Function}
   * provides a layer of indirection between an array member, and a higher-layer
   * value. This is useful in the situation where the array contains symbolic
   * references to other arrays. The {@code resolver} parameter is provided to
   * dereference such a symbolic references.
   *
   * @param <T> The component type of the specified array.
   * @param a The array.
   * @param resolver A {@link Function} to provide a layer of indirection
   *          between an array member, and a higher-layer value. If
   *          {@code resolver} is null, {@code member.getClass().isArray()} is
   *          used to determine whether the member value represents an array.
   * @param countArrayReferences If {@code true}, array members that reference
   *          an array are included in the count; if {@code false}, they are not
   *          included in the count.
   * @return The length of the specified array, summed with the lengths of all
   *         nested arrays at every depth.
   * @throws NullPointerException If the specified array is null.
   */
  @SuppressWarnings("unchecked")
  public static <T>int lengthDeep(final T[] a, final Function<? super T,T[]> resolver, final boolean countArrayReferences) {
    int size = 0;
    for (int i = 0; i < a.length; ++i) {
      final T member = a[i];
      final T[] inner = member == null ? null : resolver != null ? resolver.apply(member) : member.getClass().isArray() ? (T[])member : null;
      if (inner != null) {
        size += lengthDeep(inner, resolver, countArrayReferences);
        if (countArrayReferences)
          ++size;
      }
      else {
        ++size;
      }
    }

    return size;
  }

  /**
   * Returns a one-dimensional array with the members of the specified array,
   * and the members of all nested arrays at every depth. The value of
   * {@code member.getClass().isArray()} is used to determine whether an array
   * member represents an array for further recursion.
   * <p>
   * Array members that reference an array are <i>not included</i> in the
   * resulting array. This is the equivalent of calling
   * {@code flatten(Object[],false)}.
   *
   * @param a The array.
   * @return A one-dimensional array with the members of the specified array,
   *         and the members of all nested arrays at every depth.
   * @throws NullPointerException If the specified array is null.
   */
  public static Object[] flatten(final Object[] a) {
    return flatten(a, null, false);
  }

  /**
   * Returns a one-dimensional array with the members of the specified array,
   * and the members of all nested arrays at every depth. The value of
   * {@code member.getClass().isArray()} is used to determine whether an array
   * member represents an array for further recursion.
   *
   * @param a The array.
   * @param retainArrayReferences If {@code true}, array members that reference
   *          an array are included in the resulting array; if {@code false},
   *          they are not included in the resulting array.
   * @return A one-dimensional array with the members of the specified array,
   *         and the members of all nested arrays at every depth.
   * @throws NullPointerException If the specified array is null.
   */
  public static Object[] flatten(final Object[] a, final boolean retainArrayReferences) {
    return flatten(a, null, retainArrayReferences);
  }

  /**
   * Returns a one-dimensional array with the members of the specified array,
   * and the members of all nested arrays at every depth. The specified resolver
   * {@link Function} provides a layer of indirection between an array member,
   * and a higher-layer value. This is useful in the situation where the array
   * contains symbolic references to other arrays. The {@code resolver}
   * parameter is provided to dereference such a symbolic references.
   *
   * @param <T> The component type of the specified array.
   * @param a The array.
   * @param resolver A {@link Function} to provide a layer of indirection
   *          between an array member, and a higher-layer value. If
   *          {@code resolver} is null, {@code member.getClass().isArray()} is
   *          used to determine whether the member value represents an array.
   * @param retainArrayReferences If {@code true}, array members that reference
   *          an array are included in the resulting array; if {@code false},
   *          they are not included in the resulting array.
   * @return A one-dimensional array with the members of the specified array,
   *         and the members of all nested arrays at every depth.
   * @throws NullPointerException If the specified array is null.
   */
  @SuppressWarnings("unchecked")
  public static <T>T[] flatten(final T[] a, final Function<? super T,T[]> resolver, final boolean retainArrayReferences) {
    final T[] out = (T[])Array.newInstance(a.getClass().getComponentType(), lengthDeep(a, resolver, retainArrayReferences));
    flatten0(a, out, resolver, retainArrayReferences, -1);
    return out;
  }

  @SuppressWarnings("unchecked")
  private static <T>int flatten0(final T[] in, final Object[] out, final Function<? super T,T[]> resolver, final boolean retainArrayReferences, int index) {
    for (int i = 0; i < in.length; ++i) {
      final T member = in[i];
      final T[] inner = member == null ? null : resolver != null ? resolver.apply(member) : member.getClass().isArray() ? (T[])member : null;
      if (inner != null) {
        if (retainArrayReferences)
          out[++index] = member;

        index = flatten0(inner, out, resolver, retainArrayReferences, index);
      }
      else {
        out[++index] = member;
      }
    }

    return index;
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match. The returned index will be less than or equal to an exact match.
   *
   * @param <T> Type parameter of {@link Comparable} object.
   * @param a The sorted array.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static <T extends Comparable<? super T>> int binaryClosestSearch(final T[] a, final T key) {
    return binaryClosestSearch0(a, 0, a.length, key);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match. The returned index will be less than or equal to an exact match.
   *
   * @param <T> Type parameter of {@link Comparable} object.
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static <T extends Comparable<? super T>> int binaryClosestSearch(final T[] a, final int fromIndex, final int toIndex, final T key) {
    Assertions.assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key);
  }

  private static <T extends Comparable<? super T>> int binaryClosestSearch0(final T[] a, int fromIndex, int toIndex, final T key) {
    for (int mid, com; fromIndex < toIndex;) {
      mid = (fromIndex + toIndex) / 2;
      com = key.compareTo(a[mid]);
      if (com < 0)
        toIndex = mid;
      else if (com > 0)
        fromIndex = mid + 1;
      else
        return mid;
    }

    return (fromIndex + toIndex) / 2;
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match.
   *
   * @param <T> Type parameter of object.
   * @param a The sorted array.
   * @param key The value to match.
   * @param c The {@link Comparator} for objects of type {@code <T>}.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static <T>int binaryClosestSearch(final T[] a, final T key, final Comparator<? super T> c) {
    return binaryClosestSearch0(a, 0, a.length, key, c);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match.
   *
   * @param <T> Type parameter of object.
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @param c The {@link Comparator} for objects of type {@code <T>}.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static <T>int binaryClosestSearch(final T[] a, final int fromIndex, final int toIndex, final T key, final Comparator<? super T> c) {
    Assertions.assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, c);
  }

  private static <T>int binaryClosestSearch0(final T[] a, int fromIndex, int toIndex, final T key, final Comparator<? super T> c) {
    for (int mid, com; fromIndex < toIndex;) {
      mid = (fromIndex + toIndex) / 2;
      com = c.compare(key, a[mid]);
      if (com < 0)
        toIndex = mid;
      else if (com > 0)
        fromIndex = mid + 1;
      else
        return mid;
    }

    return (fromIndex + toIndex) / 2;
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match.
   *
   * @param a The sorted array.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static int binaryClosestSearch(final byte[] a, final byte key) {
    return binaryClosestSearch0(a, 0, a.length, key, ByteComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static int binaryClosestSearch(final byte[] a, final int fromIndex, final int toIndex, final byte key) {
    Assertions.assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, ByteComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @param c The comparator to use.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static int binaryClosestSearch(final byte[] a, final int fromIndex, final int toIndex, final byte key, final ByteComparator c) {
    Assertions.assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, c);
  }

  private static int binaryClosestSearch0(final byte[] a, int fromIndex, int toIndex, final byte key, final ByteComparator c) {
    for (int mid, com; fromIndex < toIndex;) {
      mid = (fromIndex + toIndex) / 2;
      com = c.compare(key, a[mid]);
      if (com < 0)
        toIndex = mid;
      else if (com > 0)
        fromIndex = mid + 1;
      else
        return mid;
    }

    return (fromIndex + toIndex) / 2;
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match.
   *
   * @param a The sorted array.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static int binaryClosestSearch(final short[] a, final short key) {
    return binaryClosestSearch0(a, 0, a.length, key, ShortComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static int binaryClosestSearch(final short[] a, final int fromIndex, final int toIndex, final short key) {
    Assertions.assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, ShortComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @param c The comparator to use.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static int binaryClosestSearch(final short[] a, final int fromIndex, final int toIndex, final short key, final ShortComparator c) {
    Assertions.assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, c);
  }

  private static int binaryClosestSearch0(final short[] a, int fromIndex, int toIndex, final short key, final ShortComparator c) {
    for (int mid, com; fromIndex < toIndex;) {
      mid = (fromIndex + toIndex) / 2;
      com = c.compare(key, a[mid]);
      if (com < 0)
        toIndex = mid;
      else if (com > 0)
        fromIndex = mid + 1;
      else
        return mid;
    }

    return (fromIndex + toIndex) / 2;
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match.
   *
   * @param a The sorted array.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static int binaryClosestSearch(final int[] a, final int key) {
    return binaryClosestSearch0(a, 0, a.length, key, IntComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static int binaryClosestSearch(final int[] a, final int fromIndex, final int toIndex, final int key) {
    Assertions.assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, IntComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @param c The comparator to use.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static int binaryClosestSearch(final int[] a, final int fromIndex, final int toIndex, final int key, final IntComparator c) {
    Assertions.assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, c);
  }

  private static int binaryClosestSearch0(final int[] a, int fromIndex, int toIndex, final int key, final IntComparator c) {
    for (int mid, com; fromIndex < toIndex;) {
      mid = (fromIndex + toIndex) / 2;
      com = c.compare(key, a[mid]);
      if (com < 0)
        toIndex = mid;
      else if (com > 0)
        fromIndex = mid + 1;
      else
        return mid;
    }

    return (fromIndex + toIndex) / 2;
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match.
   *
   * @param a The sorted array.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static int binaryClosestSearch(final float[] a, final float key) {
    return binaryClosestSearch0(a, 0, a.length, key, FloatComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static int binaryClosestSearch(final float[] a, final int fromIndex, final int toIndex, final float key) {
    Assertions.assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, FloatComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @param c The comparator to use.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static int binaryClosestSearch(final float[] a, final int fromIndex, final int toIndex, final float key, final FloatComparator c) {
    Assertions.assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, c);
  }

  private static int binaryClosestSearch0(final float[] a, int fromIndex, int toIndex, final float key, final FloatComparator c) {
    for (int mid, com; fromIndex < toIndex;) {
      mid = (fromIndex + toIndex) / 2;
      com = c.compare(key, a[mid]);
      if (com < 0)
        toIndex = mid;
      else if (com > 0)
        fromIndex = mid + 1;
      else
        return mid;
    }

    return (fromIndex + toIndex) / 2;
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match.
   *
   * @param a The sorted array.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static int binaryClosestSearch(final double[] a, final double key) {
    return binaryClosestSearch0(a, 0, a.length, key, DoubleComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static int binaryClosestSearch(final double[] a, final int fromIndex, final int toIndex, final double key) {
    Assertions.assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, DoubleComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @param c The comparator to use.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static int binaryClosestSearch(final double[] a, final int fromIndex, final int toIndex, final double key, final DoubleComparator c) {
    Assertions.assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, c);
  }

  private static int binaryClosestSearch0(final double[] a, int fromIndex, int toIndex, final double key, final DoubleComparator c) {
    for (int mid, com; fromIndex < toIndex;) {
      mid = (fromIndex + toIndex) / 2;
      com = c.compare(key, a[mid]);
      if (com < 0)
        toIndex = mid;
      else if (com > 0)
        fromIndex = mid + 1;
      else
        return mid;
    }

    return (fromIndex + toIndex) / 2;
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match.
   *
   * @param a The sorted array.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static int binaryClosestSearch(final long[] a, final long key) {
    return binaryClosestSearch0(a, 0, a.length, key, LongComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static int binaryClosestSearch(final long[] a, final int fromIndex, final int toIndex, final long key) {
    Assertions.assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, LongComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the
   * value provided. The returned index will be less than or equal to an exact
   * match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @param c The comparator to use.
   * @return The closest index of the sorted array matching the desired value.
   *         The returned index will be less than or equal to an exact match.
   * @throws NullPointerException If the specified array is null.
   */
  public static int binaryClosestSearch(final long[] a, final int fromIndex, final int toIndex, final long key, final LongComparator c) {
    Assertions.assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, c);
  }

  private static int binaryClosestSearch0(final long[] a, int fromIndex, int toIndex, final long key, final LongComparator c) {
    for (int mid, com; fromIndex < toIndex;) {
      mid = (fromIndex + toIndex) / 2;
      com = c.compare(key, a[mid]);
      if (com < 0)
        toIndex = mid;
      else if (com > 0)
        fromIndex = mid + 1;
      else
        return mid;
    }

    return (fromIndex + toIndex) / 2;
  }

  /**
   * Replace all members of the provided array with the provided
   * {@link UnaryOperator}.
   *
   * @param <T> Type parameter of object.
   * @param operator The {@link UnaryOperator} that defines the replacement
   *          operation.
   * @param array The array whose members are to be replaced.
   * @return The the original array instance with its members replaced by the
   *         operator.
   * @throws NullPointerException If {@code operator} or {@code array} is null.
   */
  @SafeVarargs
  public static <T>T[] replaceAll(final UnaryOperator<T> operator, final T ... array) {
    for (int i = 0; i < array.length; ++i)
      array[i] = operator.apply(array[i]);

    return array;
  }

  /**
   * Filter the provided array with the specified {@link Predicate}. This method
   * is recursive.
   *
   * @param <T> Type parameter of object.
   * @param predicate The {@link Predicate} that defines the filter.
   * @param array The array whose members are to be filtered.
   * @return A new array instance with members that pass the filter.
   * @throws NullPointerException If {@code predicate} or {@code array} is null.
   */
  @SafeVarargs
  public static <T>T[] filter(final Predicate<? super T> predicate, final T ... array) {
    return filter0(predicate, 0, 0, array);
  }

  @SuppressWarnings("unchecked")
  private static <T>T[] filter0(final Predicate<? super T> predicate, final int index, final int depth, final T ... array) {
    if (index == array.length)
      return (T[])Array.newInstance(array.getClass().getComponentType(), depth);

    final boolean accept = predicate.test(array[index]);
    final T[] filtered = filter0(predicate, index + 1, accept ? depth + 1 : depth, array);
    if (accept)
      filtered[depth] = array[index];

    return filtered;
  }

  @SafeVarargs
  @SuppressWarnings("unchecked")
  private static <T>T[] concat0(final T[] ... arrays) {
    int length = 0;
    for (final T[] array : arrays)
      length += array.length;

    final T[] concat = (T[])Array.newInstance(arrays[0].getClass().getComponentType(), length);
    for (int i = 0, l = 0; i < arrays.length; l += arrays[i].length, ++i)
      System.arraycopy(arrays[i], 0, concat, l, arrays[i].length);

    return concat;
  }

  /**
   * Concatenate the provided arrays of a common type into a single array.
   *
   * @param <T> Type parameter of object.
   * @param arrays The arrays to be concatenated.
   * @return The concatenated array.
   * @throws NullPointerException If {@code arrays} is null.
   */
  @SafeVarargs
  public static <T>T[] concat(final T[] ... arrays) {
    return concat0(arrays);
  }

  /**
   * Concatenate the provided arrays of a common type into a single array.
   *
   * @param <T> Type parameter of object.
   * @param array The base array to be concatenated.
   * @param elements The additional elements to be concatenated.
   * @return The concatenated array.
   * @throws NullPointerException If {@code array} or {@code elements} is null.
   */
  @SafeVarargs
  public static <T>T[] concat(final T[] array, final T ... elements) {
    return concat0(array, elements);
  }

  /**
   * Concatenate the provided arrays of a common type into a single array.
   *
   * @param <T> Type parameter of object.
   * @param array The base array to be concatenated.
   * @param element The additional element to be concatenated.
   * @return The concatenated array.
   * @throws NullPointerException If {@code array} is null.
   */
  @SuppressWarnings("unchecked")
  public static <T>T[] concat(final T[] array, final T element) {
    final T[] concat = (T[])Array.newInstance(array.getClass().getComponentType(), array.length + 1);
    System.arraycopy(array, 0, concat, 0, array.length);
    concat[array.length] = element;
    return concat;
  }

  /**
   * Create a new array by removing existing elements from the provided array.
   *
   * @param <T> Type parameter of object.
   * @param array The array to be spliced.
   * @param start Index at the greater of which to remove all elements in the
   *          array (with origin 0). If negative, index will be set to its
   *          calculated value from the end of the array (with origin 1).
   * @return A new array with elements removed from the provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static <T>T[] splice(final T[] array, int start) {
    if (start < 0)
      start += array.length;

    return splice(array, start, array.length - start);
  }

  /**
   * Create a new array by removing existing elements from the provided array.
   *
   * @param <T> Type parameter of object.
   * @param array The array to be spliced.
   * @param start Index at which to begin changing the array (with origin 0). If
   *          negative, index will be set to its calculated value from the end
   *          of the array (with origin 1).
   * @param deleteCount An integer indicating the number of array elements to
   *          remove. If deleteCount is 0, no elements are removed, but a new
   *          reference to the array is returned.
   * @return A new array with elements removed from the provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  @SuppressWarnings("unchecked")
  public static <T>T[] splice(final T[] array, int start, final int deleteCount) {
    if (deleteCount == 0)
      return array.clone();

    if (start < 0)
      start += array.length;

    final T[] spliced = (T[])Array.newInstance(array.getClass().getComponentType(), array.length - deleteCount);
    if (start != 0)
      System.arraycopy(array, 0, spliced, 0, start);

    if (start + deleteCount != array.length)
      System.arraycopy(array, start + deleteCount, spliced, start, array.length - start - deleteCount);

    return spliced;
  }

  /**
   * Create a new array by removing existing elements from the provided array.
   *
   * @param <T> Type parameter of object.
   * @param array The array to be spliced.
   * @param start Index at which to begin changing the array (with origin 0). If
   *          negative, index will be set to its calculated value from the end
   *          of the array (with origin 1).
   * @param deleteCount An integer indicating the number of array elements to
   *          remove. If deleteCount is 0, no elements are removed, but a new
   *          reference to the array is returned.
   * @param items The elements to add to the array, beginning at the start
   *          index.
   * @return A new array with elements removed from the provided array.
   * @throws NullPointerException If {@code array} or {@code items} is null.
   */
  @SuppressWarnings("unchecked")
  public static <T>T[] splice(final T[] array, int start, final int deleteCount, final T ... items) {
    if (items.length == 0)
      return splice(array, start, deleteCount);

    if (start < 0)
      start += array.length;

    final T[] spliced = (T[])Array.newInstance(array.getClass().getComponentType(), array.length - deleteCount + items.length);
    if (start != 0)
      System.arraycopy(array, 0, spliced, 0, start);

    System.arraycopy(items, 0, spliced, start, items.length);
    if (start + deleteCount != array.length)
      System.arraycopy(array, start + deleteCount, spliced, start + items.length, array.length - start - deleteCount);

    return spliced;
  }

  /**
   * Find the index of an object in an array.
   *
   * @param <T> Type parameter of object.
   * @param array The array to search.
   * @param obj The object to locate.
   * @return The index of the object if it is found, or -1 otherwise.
   * @throws NullPointerException If {@code array} is null.
   */
  public static <T>int indexOf(final T[] array, final T obj) {
    return indexOf0(array, 0, array.length, obj);
  }

  /**
   * Find the index of an object in an array.
   *
   * @param <T> Type parameter of object.
   * @param array The array to search.
   * @param obj The object to locate.
   * @return The index of the object if it is found, or -1 otherwise.
   * @throws NullPointerException If {@code array} is null.
   */
  public static <T>int indexOf(final T[] array, final int off, int len, final T obj) {
    return indexOf0(array, off, Math.min(len, array.length), obj);
  }

  private static <T>int indexOf0(final T[] array, final int off, final int len, final T obj) {
    for (int i = off; i < len; ++i)
      if (obj.equals(array[i]))
        return i;

    return -1;
  }

  /**
   * Check for the existence of an object in an array.
   *
   * @param <T> Type parameter of object.
   * @param array The array to search.
   * @param obj The object to locate.
   * @return {@code true} if the object exists, {@code false} otherwise.
   * @throws NullPointerException If {@code array} is null.
   */
  public static <T>boolean contains(final T[] array, final T obj) {
    return indexOf(array, obj) >= 0;
  }

  /**
   * Check for the existence of an object in an array.
   *
   * @param <T> Type parameter of object.
   * @param array The array to search.
   * @param obj The object to locate.
   * @return {@code true} if the object exists, {@code false} otherwise.
   * @throws NullPointerException If {@code array} is null.
   */
  public static <T>boolean contains(final T[] array, final int off, final int len, final T obj) {
    return indexOf(array, off, len, obj) >= 0;
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final byte[] array, final char delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final byte[] array, final char delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final byte[] array, final char delimiter, final int offset, final int length) {
    if (array == null)
      return "null";

    Assertions.assertRangeArray(offset, array.length);
    if (array.length == offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length + offset; ++i)
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final byte[] array, final String delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final byte[] array, final String delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final byte[] array, final String delimiter, final int offset, final int length) {
    if (array == null)
      return "null";

    Assertions.assertRangeArray(offset, array.length);
    if (array.length == offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length + offset; ++i)
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final char[] array, final char delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final char[] array, final char delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final char[] array, final char delimiter, final int offset, final int length) {
    if (array == null)
      return "null";

    Assertions.assertRangeArray(offset, array.length);
    if (array.length == offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length + offset; ++i)
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final char[] array, final String delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final char[] array, final String delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final char[] array, final String delimiter, final int offset, final int length) {
    if (array == null)
      return "null";

    Assertions.assertRangeArray(offset, array.length);
    if (array.length == offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length + offset; ++i)
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final short[] array, final char delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final short[] array, final char delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final short[] array, final char delimiter, final int offset, final int length) {
    if (array == null)
      return "null";

    Assertions.assertRangeArray(offset, array.length);

    Assertions.assertRangeArray(offset, array.length);
    if (array.length == offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length + offset; ++i)
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final short[] array, final String delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final short[] array, final String delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final short[] array, final String delimiter, final int offset, final int length) {
    if (array == null)
      return "null";

    Assertions.assertRangeArray(offset, array.length);
    if (array.length == offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length + offset; ++i)
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final int[] array, final char delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final int[] array, final char delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final int[] array, final char delimiter, final int offset, final int length) {
    if (array == null)
      return "null";

    Assertions.assertRangeArray(offset, array.length);
    if (array.length == offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length + offset; ++i)
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final int[] array, final String delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final int[] array, final String delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final int[] array, final String delimiter, final int offset, final int length) {
    if (array == null)
      return "null";

    Assertions.assertRangeArray(offset, array.length);
    if (length == 0 || array.length == offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length + offset; ++i)
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final long[] array, final char delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final long[] array, final char delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final long[] array, final char delimiter, final int offset, final int length) {
    if (array == null)
      return "null";

    if (array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length + offset; ++i)
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final long[] array, final String delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final long[] array, final String delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final long[] array, final String delimiter, final int offset, final int length) {
    if (array == null)
      return "null";

    if (array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length + offset; ++i)
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final float[] array, final char delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final float[] array, final char delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final float[] array, final char delimiter, final int offset, final int length) {
    if (array == null)
      return "null";

    if (array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length + offset; ++i)
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final float[] array, final String delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final float[] array, final String delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final float[] array, final String delimiter, final int offset, final int length) {
    if (array == null)
      return "null";

    if (array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length + offset; ++i)
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final double[] array, final char delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final double[] array, final char delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final double[] array, final char delimiter, final int offset, final int length) {
    if (array == null)
      return "null";

    if (array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length + offset; ++i)
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final double[] array, final String delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final double[] array, final String delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final double[] array, final String delimiter, final int offset, final int length) {
    if (array == null)
      return "null";

    if (array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length + offset; ++i)
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final Object[] array, final char delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling the
   * provided function on each member, delimited by the provided delimiter
   * {@code char}.
   *
   * @param <T> The component type of the specified array.
   * @param array The array.
   * @param delimiter The delimiter.
   * @param function The {@code toString} function.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   * @throws NullPointerException If the specified array is not null and the
   *           function is null.
   */
  public static <T>String toString(final T[] array, final char delimiter, final Function<? super T,String> function) {
    return array == null ? null : toString(array, delimiter, 0, array.length, function);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final Object[] array, final char delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling the
   * provided function on each member, delimited by the provided delimiter
   * {@code char}.
   *
   * @param <T> The component type of the specified array.
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param function The {@code toString} function.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   * @throws NullPointerException If the specified array is not null and the
   *           function is null.
   */
  public static <T>String toString(final T[] array, final char delimiter, final int offset, final Function<? super T,String> function) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset, function);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final Object[] array, final char delimiter, final int offset, final int length) {
    if (array == null)
      return "null";

    if (array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length + offset; ++i)
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling the
   * provided function on each member, delimited by the provided delimiter
   * {@code char}.
   *
   * @param <T> The component type of the specified array.
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @param function The {@code toString} function.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   * @throws NullPointerException If the specified array is not null and the
   *           function is null.
   */
  public static <T>String toString(final T[] array, final char delimiter, final int offset, final int length, final Function<? super T,String> function) {
    if (array == null)
      return "null";

    if (array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return function.apply(array[offset]);

    final StringBuilder builder = new StringBuilder(function.apply(array[offset]));
    for (int i = offset + 1; i < length + offset; ++i)
      builder.append(delimiter).append(function.apply(array[i]));

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final Object[] array, final String delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling the
   * provided function on each member, delimited by the provided delimiter
   * {@code char}.
   *
   * @param <T> The component type of the specified array.
   * @param array The array.
   * @param delimiter The delimiter.
   * @param function The {@code toString} function.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   * @throws NullPointerException If the specified array is not null and the
   *           function is null.
   */
  public static <T>String toString(final T[] array, final String delimiter, final Function<? super T,String> function) {
    return array == null ? null : toString(array, delimiter, 0, array.length, function);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final Object[] array, final String delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling the
   * provided function on each member, delimited by the provided delimiter
   * string.
   *
   * @param <T> The component type of the specified array.
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param function The {@code toString} function.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   * @throws NullPointerException If the specified array is not null and the
   *           function is null.
   */
  public static <T>String toString(final T[] array, final String delimiter, final int offset, final Function<? super T,String> function) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset, function);
  }

  /**
   * Create a string representation of the specified array by calling each
   * member's {@link #toString()} method, delimited by the provided delimiter
   * string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   */
  public static String toString(final Object[] array, final String delimiter, final int offset, final int length) {
    if (array == null)
      return "null";

    if (array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length + offset; ++i)
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling the
   * provided function on each member, delimited by the provided delimiter
   * string.
   *
   * @param <T> The component type of the specified array.
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @param function The {@code toString} function.
   * @return The delimiter delimited {@link #toString()} representation of the
   *         array, or {@code null} if the specified array is null.
   * @throws NullPointerException If the specified array is not null and the
   *           function is null.
   */
  public static <T>String toString(final T[] array, final String delimiter, final int offset, final int length, final Function<? super T,String> function) {
    if (array == null)
      return "null";

    if (array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return function.apply(array[offset]);

    final StringBuilder builder = new StringBuilder(function.apply(array[offset]));
    for (int i = offset + 1; i < length + offset; ++i)
      builder.append(delimiter).append(function.apply(array[i]));

    return builder.toString();
  }

  /**
   * Fill the provided array with +1 incremental values starting at the provided
   * {@code start} value from {@code array[0]}, to
   * {@code array[array.length - 1]}.
   *
   * @param array The target array.
   * @param start The starting value.
   * @return The provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static byte[] fillIncremental(final byte[] array, byte start) {
    for (int i = 0; i < array.length; ++i)
      array[i] = start++;

    return array;
  }

  /**
   * Fill the provided array with +1 incremental values starting at the provided
   * {@code start} value from {@code array[0]}, to
   * {@code array[array.length - 1]}.
   *
   * @param array The target array.
   * @param start The starting value.
   * @return The provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static char[] fillIncremental(final char[] array, char start) {
    for (int i = 0; i < array.length; ++i)
      array[i] = start++;

    return array;
  }

  /**
   * Fill the provided array with +1 incremental values starting at the provided
   * {@code start} value from {@code array[0]}, to
   * {@code array[array.length - 1]}.
   *
   * @param array The target array.
   * @param start The starting value.
   * @return The provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static short[] fillIncremental(final short[] array, short start) {
    for (int i = 0; i < array.length; ++i)
      array[i] = start++;

    return array;
  }

  /**
   * Fill the provided array with +1 incremental values starting at the provided
   * {@code start} value from {@code array[0]}, to
   * {@code array[array.length - 1]}.
   *
   * @param array The target array.
   * @param start The starting value.
   * @return The provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static int[] fillIncremental(final int[] array, int start) {
    for (int i = 0; i < array.length; ++i)
      array[i] = start++;

    return array;
  }

  /**
   * Fill the provided array with +1 incremental values starting at the provided
   * {@code start} value from {@code array[0]}, to
   * {@code array[array.length - 1]}.
   *
   * @param array The target array.
   * @param start The starting value.
   * @return The provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static long[] fillIncremental(final long[] array, long start) {
    for (int i = 0; i < array.length; ++i)
      array[i] = start++;

    return array;
  }

  /**
   * Fill the provided array with +1 incremental values starting at the provided
   * {@code start} value from {@code array[0]}, to
   * {@code array[array.length - 1]}.
   *
   * @param array The target array.
   * @param start The starting value.
   * @return The provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static float[] fillIncremental(final float[] array, float start) {
    for (int i = 0; i < array.length; ++i)
      array[i] = start++;

    return array;
  }

  /**
   * Fill the provided array with +1 incremental values starting at the provided
   * {@code start} value from {@code array[0]}, to
   * {@code array[array.length - 1]}.
   *
   * @param array The target array.
   * @param start The starting value.
   * @return The provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static double[] fillIncremental(final double[] array, double start) {
    for (int i = 0; i < array.length; ++i)
      array[i] = start++;

    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided
   * {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value}
   *         members.
   */
  public static boolean[] createRepeat(final boolean value, final int length) {
    final boolean[] array = new boolean[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided
   * {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value}
   *         members.
   */
  public static byte[] createRepeat(final byte value, final int length) {
    final byte[] array = new byte[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided
   * {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value}
   *         members.
   */
  public static char[] createRepeat(final char value, final int length) {
    final char[] array = new char[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided
   * {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value}
   *         members.
   */
  public static double[] createRepeat(final double value, final int length) {
    final double[] array = new double[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided
   * {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value}
   *         members.
   */
  public static float[] createRepeat(final float value, final int length) {
    final float[] array = new float[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided
   * {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value}
   *         members.
   */
  public static int[] createRepeat(final int value, final int length) {
    final int[] array = new int[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided
   * {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value}
   *         members.
   */
  public static long[] createRepeat(final long value, final int length) {
    final long[] array = new long[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided
   * {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value}
   *         members.
   */
  public static short[] createRepeat(final short value, final int length) {
    final short[] array = new short[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided
   * {@code length} number of times.
   *
   * @param <T> Type parameter of object.
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value}
   *         members.
   */
  @SuppressWarnings("unchecked")
  public static <T>T[] createRepeat(final T value, final int length) {
    final T[] array = (T[])Array.newInstance(value.getClass(), length);
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Returns an array that is the subArray of the provided array. Calling this
   * method is the equivalent of calling Arrays.subArray(array, beginIndex,
   * array.length).
   *
   * @param <T> Type parameter of object.
   * @param array The specified {@code array}.
   * @param beginIndex The index to become the start of the new array.
   * @return The subArray of the specified {@code array}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static <T>T[] subArray(final T[] array, final int beginIndex) {
    return subArray(array, beginIndex, array.length);
  }

  /**
   * Returns an array that is the subArray of the provided array.
   *
   * @param <T> Type parameter of object.
   * @param array The specified {@code array}.
   * @param beginIndex The index to become the start of the new array.
   * @param endIndex The index to become the end of the new array.
   * @return The subArray of the specified {@code array}.
   * @throws NullPointerException If {@code array} is null.
   */
  @SuppressWarnings("unchecked")
  public static <T>T[] subArray(final T[] array, final int beginIndex, final int endIndex) {
    if (endIndex < beginIndex)
      throw new IllegalArgumentException("endIndex < beginIndex");

    final Class<?> componentType = array.getClass().getComponentType();
    final T[] subArray = (T[])Array.newInstance(componentType, endIndex - beginIndex);
    if (beginIndex == endIndex)
      return subArray;

    System.arraycopy(array, beginIndex, subArray, 0, endIndex - beginIndex);
    return subArray;
  }

  /**
   * Swaps the two specified elements in the provided array.
   */
  private static void swap(final boolean[] arr, final int i, final int j) {
    final boolean tmp = arr[i];
    arr[i] = arr[j];
    arr[j] = tmp;
  }

  /**
   * Swaps the two specified elements in the provided array.
   */
  private static void swap(final byte[] arr, final int i, final int j) {
    final byte tmp = arr[i];
    arr[i] = arr[j];
    arr[j] = tmp;
  }

  /**
   * Swaps the two specified elements in the provided array.
   */
  private static void swap(final char[] arr, final int i, final int j) {
    final char tmp = arr[i];
    arr[i] = arr[j];
    arr[j] = tmp;
  }

  /**
   * Swaps the two specified elements in the provided array.
   */
  private static void swap(final short[] arr, final int i, final int j) {
    final short tmp = arr[i];
    arr[i] = arr[j];
    arr[j] = tmp;
  }

  /**
   * Swaps the two specified elements in the provided array.
   */
  private static void swap(final int[] arr, final int i, final int j) {
    final int tmp = arr[i];
    arr[i] = arr[j];
    arr[j] = tmp;
  }

  /**
   * Swaps the two specified elements in the provided array.
   */
  private static void swap(final long[] arr, final int i, final int j) {
    final long tmp = arr[i];
    arr[i] = arr[j];
    arr[j] = tmp;
  }

  /**
   * Swaps the two specified elements in the provided array.
   */
  private static void swap(final float[] arr, final int i, final int j) {
    final float tmp = arr[i];
    arr[i] = arr[j];
    arr[j] = tmp;
  }

  /**
   * Swaps the two specified elements in the provided array.
   */
  private static void swap(final double[] arr, final int i, final int j) {
    final double tmp = arr[i];
    arr[i] = arr[j];
    arr[j] = tmp;
  }

  /**
   * Swaps the two specified elements in the provided array.
   */
  private static void swap(final Object[] arr, final int i, final int j) {
    final Object tmp = arr[i];
    arr[i] = arr[j];
    arr[j] = tmp;
  }

  private static Random DEFAULT_RANDOM;

  private static Random getRandom() {
    return DEFAULT_RANDOM == null ? DEFAULT_RANDOM = new Random() : DEFAULT_RANDOM;
  }

  /**
   * Randomly permutes the specified array using a default source of randomness.
   * All permutations occur with approximately equal likelihood.
   * <p>
   * The hedge "approximately" is used in the foregoing description because
   * default source of randomness is only approximately an unbiased source of
   * independently chosen bits. If it were a perfect source of randomly chosen
   * bits, then the algorithm would choose permutations with perfect uniformity.
   * <p>
   * This implementation traverses the array backwards, from the last element up
   * to the second, repeatedly swapping a randomly selected element into the
   * "current position". Elements are randomly selected from the portion of the
   * array that runs from the first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The list to be shuffled.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final boolean[] array) {
    shuffle(array, getRandom());
  }

  /**
   * Randomly permutes the specified array using a default source of randomness.
   * All permutations occur with approximately equal likelihood.
   * <p>
   * The hedge "approximately" is used in the foregoing description because
   * default source of randomness is only approximately an unbiased source of
   * independently chosen bits. If it were a perfect source of randomly chosen
   * bits, then the algorithm would choose permutations with perfect uniformity.
   * <p>
   * This implementation traverses the array backwards, from the last element up
   * to the second, repeatedly swapping a randomly selected element into the
   * "current position". Elements are randomly selected from the portion of the
   * array that runs from the first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The list to be shuffled.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final byte[] array) {
    shuffle(array, getRandom());
  }

  /**
   * Randomly permutes the specified array using a default source of randomness.
   * All permutations occur with approximately equal likelihood.
   * <p>
   * The hedge "approximately" is used in the foregoing description because
   * default source of randomness is only approximately an unbiased source of
   * independently chosen bits. If it were a perfect source of randomly chosen
   * bits, then the algorithm would choose permutations with perfect uniformity.
   * <p>
   * This implementation traverses the array backwards, from the last element up
   * to the second, repeatedly swapping a randomly selected element into the
   * "current position". Elements are randomly selected from the portion of the
   * array that runs from the first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The list to be shuffled.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final short[] array) {
    shuffle(array, getRandom());
  }

  /**
   * Randomly permutes the specified array using a default source of randomness.
   * All permutations occur with approximately equal likelihood.
   * <p>
   * The hedge "approximately" is used in the foregoing description because
   * default source of randomness is only approximately an unbiased source of
   * independently chosen bits. If it were a perfect source of randomly chosen
   * bits, then the algorithm would choose permutations with perfect uniformity.
   * <p>
   * This implementation traverses the array backwards, from the last element up
   * to the second, repeatedly swapping a randomly selected element into the
   * "current position". Elements are randomly selected from the portion of the
   * array that runs from the first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The list to be shuffled.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final int[] array) {
    shuffle(array, getRandom());
  }

  /**
   * Randomly permutes the specified array using a default source of randomness.
   * All permutations occur with approximately equal likelihood.
   * <p>
   * The hedge "approximately" is used in the foregoing description because
   * default source of randomness is only approximately an unbiased source of
   * independently chosen bits. If it were a perfect source of randomly chosen
   * bits, then the algorithm would choose permutations with perfect uniformity.
   * <p>
   * This implementation traverses the array backwards, from the last element up
   * to the second, repeatedly swapping a randomly selected element into the
   * "current position". Elements are randomly selected from the portion of the
   * array that runs from the first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The list to be shuffled.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final long[] array) {
    shuffle(array, getRandom());
  }

  /**
   * Randomly permutes the specified array using a default source of randomness.
   * All permutations occur with approximately equal likelihood.
   * <p>
   * The hedge "approximately" is used in the foregoing description because
   * default source of randomness is only approximately an unbiased source of
   * independently chosen bits. If it were a perfect source of randomly chosen
   * bits, then the algorithm would choose permutations with perfect uniformity.
   * <p>
   * This implementation traverses the array backwards, from the last element up
   * to the second, repeatedly swapping a randomly selected element into the
   * "current position". Elements are randomly selected from the portion of the
   * array that runs from the first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The list to be shuffled.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final float[] array) {
    shuffle(array, getRandom());
  }

  /**
   * Randomly permutes the specified array using a default source of randomness.
   * All permutations occur with approximately equal likelihood.
   * <p>
   * The hedge "approximately" is used in the foregoing description because
   * default source of randomness is only approximately an unbiased source of
   * independently chosen bits. If it were a perfect source of randomly chosen
   * bits, then the algorithm would choose permutations with perfect uniformity.
   * <p>
   * This implementation traverses the array backwards, from the last element up
   * to the second, repeatedly swapping a randomly selected element into the
   * "current position". Elements are randomly selected from the portion of the
   * array that runs from the first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The list to be shuffled.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final double[] array) {
    shuffle(array, getRandom());
  }

  /**
   * Randomly permutes the specified array using a default source of randomness.
   * All permutations occur with approximately equal likelihood.
   * <p>
   * The hedge "approximately" is used in the foregoing description because
   * default source of randomness is only approximately an unbiased source of
   * independently chosen bits. If it were a perfect source of randomly chosen
   * bits, then the algorithm would choose permutations with perfect uniformity.
   * <p>
   * This implementation traverses the array backwards, from the last element up
   * to the second, repeatedly swapping a randomly selected element into the
   * "current position". Elements are randomly selected from the portion of the
   * array that runs from the first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The list to be shuffled.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final Object[] array) {
    shuffle(array, getRandom());
  }

  /**
   * Randomly permute the specified array using the specified source of
   * randomness. All permutations occur with equal likelihood assuming that the
   * source of randomness is fair.
   * <p>
   * This implementation traverses the array backwards, from the last element up
   * to the second, repeatedly swapping a randomly selected element into the
   * "current position". Elements are randomly selected from the portion of the
   * array that runs from the first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The array to be shuffled.
   * @param random The source of randomness to use to shuffle the array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final boolean[] array, final Random random) {
    for (int i = array.length; i > 1; --i)
      swap(array, i - 1, random.nextInt(i));
  }

  /**
   * Randomly permute the specified array using the provided source of
   * randomness. All permutations occur with equal likelihood assuming that the
   * source of randomness is fair.
   * <p>
   * This implementation traverses the array backwards, from the last element up
   * to the second, repeatedly swapping a randomly selected element into the
   * "current position". Elements are randomly selected from the portion of the
   * array that runs from the first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array the array to be shuffled.
   * @param random The source of randomness to use to shuffle the array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final byte[] array, final Random random) {
    for (int i = array.length; i > 1; --i)
      swap(array, i - 1, random.nextInt(i));
  }

  /**
   * Randomly permute the specified array using the provided source of
   * randomness. All permutations occur with equal likelihood assuming that the
   * source of randomness is fair.
   * <p>
   * This implementation traverses the array backwards, from the last element up
   * to the second, repeatedly swapping a randomly selected element into the
   * "current position". Elements are randomly selected from the portion of the
   * array that runs from the first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The array to be shuffled.
   * @param random The source of randomness to use to shuffle the array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final char[] array, final Random random) {
    for (int i = array.length; i > 1; --i)
      swap(array, i - 1, random.nextInt(i));
  }

  /**
   * Randomly permute the specified array using the provided source of
   * randomness. All permutations occur with equal likelihood assuming that the
   * source of randomness is fair.
   * <p>
   * This implementation traverses the array backwards, from the last element up
   * to the second, repeatedly swapping a randomly selected element into the
   * "current position". Elements are randomly selected from the portion of the
   * array that runs from the first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The array to be shuffled.
   * @param random The source of randomness to use to shuffle the array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final short[] array, final Random random) {
    for (int i = array.length; i > 1; --i)
      swap(array, i - 1, random.nextInt(i));
  }

  /**
   * Randomly permute the specified array using the provided source of
   * randomness. All permutations occur with equal likelihood assuming that the
   * source of randomness is fair.
   * <p>
   * This implementation traverses the array backwards, from the last element up
   * to the second, repeatedly swapping a randomly selected element into the
   * "current position". Elements are randomly selected from the portion of the
   * array that runs from the first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The array to be shuffled.
   * @param random The source of randomness to use to shuffle the array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final int[] array, final Random random) {
    for (int i = array.length; i > 1; --i)
      swap(array, i - 1, random.nextInt(i));
  }

  /**
   * Randomly permute the specified array using the provided source of
   * randomness. All permutations occur with equal likelihood assuming that the
   * source of randomness is fair.
   * <p>
   * This implementation traverses the array backwards, from the last element up
   * to the second, repeatedly swapping a randomly selected element into the
   * "current position". Elements are randomly selected from the portion of the
   * array that runs from the first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The array to be shuffled.
   * @param random The source of randomness to use to shuffle the array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final long[] array, final Random random) {
    for (int i = array.length; i > 1; --i)
      swap(array, i - 1, random.nextInt(i));
  }

  /**
   * Randomly permute the specified array using the provided source of
   * randomness. All permutations occur with equal likelihood assuming that the
   * source of randomness is fair.
   * <p>
   * This implementation traverses the array backwards, from the last element up
   * to the second, repeatedly swapping a randomly selected element into the
   * "current position". Elements are randomly selected from the portion of the
   * array that runs from the first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The array to be shuffled.
   * @param random The source of randomness to use to shuffle the array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final float[] array, final Random random) {
    for (int i = array.length; i > 1; --i)
      swap(array, i - 1, random.nextInt(i));
  }

  /**
   * Randomly permute the specified array using the provided source of
   * randomness. All permutations occur with equal likelihood assuming that the
   * source of randomness is fair.
   * <p>
   * This implementation traverses the array backwards, from the last element up
   * to the second, repeatedly swapping a randomly selected element into the
   * "current position". Elements are randomly selected from the portion of the
   * array that runs from the first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The array to be shuffled.
   * @param random The source of randomness to use to shuffle the array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final double[] array, final Random random) {
    for (int i = array.length; i > 1; --i)
      swap(array, i - 1, random.nextInt(i));
  }

  /**
   * Randomly permute the specified array using the provided source of
   * randomness. All permutations occur with equal likelihood assuming that the
   * source of randomness is fair.
   * <p>
   * This implementation traverses the array backwards, from the last element up
   * to the second, repeatedly swapping a randomly selected element into the
   * "current position". Elements are randomly selected from the portion of the
   * array that runs from the first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The array to be shuffled.
   * @param random The source of randomness to use to shuffle the array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final Object[] array, final Random random) {
    for (int i = array.length; i > 1; --i)
      swap(array, i - 1, random.nextInt(i));
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * array in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The array providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final byte[] order) {
    sort(data, order, ByteComparator.NATURAL);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * array in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The array providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @param comparator The comparator to use.
   * @throws NullPointerException If {@code data}, {@code order}, or
   *           {@code comparator} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final byte[] order, final ByteComparator comparator) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    PrimitiveSort.sortPaired(data, order, 0, order.length, comparator);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * array in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The array providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final char[] order) {
    sort(data, order, CharComparator.NATURAL);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * array in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The array providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @param comparator The comparator to use.
   * @throws NullPointerException If {@code data}, {@code order}, or
   *           {@code comparator} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final char[] order, final CharComparator comparator) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    PrimitiveSort.sortPaired(data, order, 0, order.length, comparator);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * array in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The array providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final short[] order) {
    sort(data, order, ShortComparator.NATURAL);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * array in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The array providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @param comparator The comparator to use.
   * @throws NullPointerException If {@code data}, {@code order}, or
   *           {@code comparator} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final short[] order, final ShortComparator comparator) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    PrimitiveSort.sortPaired(data, order, 0, order.length, comparator);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * array in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The array providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final int[] order) {
    sort(data, order, IntComparator.NATURAL);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * array in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The array providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @param comparator The comparator to use.
   * @throws NullPointerException If {@code data}, {@code order}, or
   *           {@code comparator} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final int[] order, final IntComparator comparator) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    PrimitiveSort.sortPaired(data, order, 0, order.length, comparator);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * array in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The array providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final long[] order) {
    sort(data, order, LongComparator.NATURAL);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * array in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The array providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @param comparator The comparator to use.
   * @throws NullPointerException If {@code data}, {@code order}, or
   *           {@code comparator} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final long[] order, final LongComparator comparator) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    PrimitiveSort.sortPaired(data, order, 0, order.length, comparator);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * array in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The array providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final float[] order) {
    sort(data, order, FloatComparator.NATURAL);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * array in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The array providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @param comparator The comparator to use.
   * @throws NullPointerException If {@code data}, {@code order}, or
   *           {@code comparator} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final float[] order, final FloatComparator comparator) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    PrimitiveSort.sortPaired(data, order, 0, order.length, comparator);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * array in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The array providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final double[] order) {
    sort(data, order, DoubleComparator.NATURAL);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * array in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param data The array providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @param comparator The comparator to use.
   * @throws NullPointerException If {@code data}, {@code order}, or
   *           {@code comparator} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final double[] order, final DoubleComparator comparator) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    PrimitiveSort.sortPaired(data, order, 0, order.length, comparator);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * {@link List} of {@link Comparable} objects in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param <T> The type parameter for the {@link Comparable} objects of
   *          {@code order}.
   * @param data The array providing the data.
   * @param order The {@link List} of {@link Comparable} objects providing the
   *          order of indices to sort {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.size()}.
   */
  public static <T extends Comparable<? super T>>void sort(final Object[] data, final List<T> order) {
    if (data.length != order.size())
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.size() [" + order.size() + "] must be equal");

    final int[] idx = PrimitiveSort.buildIndex(order.size());
    PrimitiveSort.sortIndexed(data, idx, (o1, o2) -> {
      final T c1 = order.get(o1);
      final T c2 = order.get(o2);
      return c1 == null ? c2 == null ? 0 : -1 : c2 == null ? 1 : c1.compareTo(c2);
    });
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the
   * {@link List} in the second argument.
   * <p>
   * For example, {@code data} and {@code order} are initialized to:
   *
   * <pre>
   *  data: g i j h e a c d b f
   * order: 6 8 9 7 4 0 2 3 1 5
   * </pre>
   *
   * After {@code sort(data, order)} is called:
   *
   * <pre>
   *  data: a b c d e f g h i j
   * order: 0 1 2 3 4 5 6 7 8 9
   * </pre>
   *
   * @param <T> The type parameter for the {@link Comparable} objects of
   *          {@code order}.
   * @param data The array providing the data.
   * @param order The {@link List} providing the order of indices to sort
   *          {@code data}.
   * @param comparator The {@link Comparator} for members of {@code order}.
   * @throws NullPointerException If {@code data}, {@code order}, or
   *           {@code comparator} is null.
   * @throws IllegalArgumentException If {@code data.length != order.size()}.
   */
  public static <T>void sort(final Object[] data, final List<? extends T> order, final Comparator<? super T> comparator) {
    if (data.length != order.size())
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.size() [" + order.size() + "] must be equal");

    Objects.requireNonNull(comparator);
    final int[] idx = PrimitiveSort.buildIndex(order.size());
    PrimitiveSort.sortIndexed(data, idx, (o1, o2) -> comparator.compare(order.get(o1), order.get(o2)));
  }

  /**
   * Sorts the specified array of {@code byte}s, according to the specified
   * {@link ByteComparator}.
   *
   * @param a The array of {@code byte}s.
   * @param c The {@link ByteComparator}.
   * @throws NullPointerException If {@code a} is null.
   */
  public static void sort(final byte[] a, final ByteComparator c) {
    sort(a, 0, a.length, c);
  }

  /**
   * Sorts the specified array of {@code byte}s, according to the specified
   * {@link ByteComparator}.
   *
   * @param a The array of {@code byte}s.
   * @param fromIndex The index of the first element, inclusive, to be sorted.
   * @param toIndex The index of the last element, exclusive, to be sorted.
   * @param c The {@link ByteComparator}.
   * @throws NullPointerException If {@code a} is null.
   */
  public static void sort(final byte[] a, final int fromIndex, final int toIndex, final ByteComparator c) {
    if (c != null)
      PrimitiveSort.sort(a, fromIndex, toIndex, c);
    else
      Arrays.sort(a, fromIndex, toIndex);
  }

  /**
   * Sorts the specified array of {@code char}s, according to the specified
   * {@link CharComparator}.
   *
   * @param a The array of {@code char}s.
   * @param c The {@link CharComparator}.
   * @throws NullPointerException If {@code a} is null.
   */
  public static void sort(final char[] a, final CharComparator c) {
    sort(a, 0, a.length, c);
  }

  /**
   * Sorts the specified array of {@code char}s, according to the specified
   * {@link CharComparator}.
   *
   * @param a The array of {@code char}s.
   * @param fromIndex The index of the first element, inclusive, to be sorted.
   * @param toIndex The index of the last element, exclusive, to be sorted.
   * @param c The {@link CharComparator}.
   * @throws NullPointerException If {@code a} is null.
   */
  public static void sort(final char[] a, final int fromIndex, final int toIndex, final CharComparator c) {
    if (c != null)
      PrimitiveSort.sort(a, fromIndex, toIndex, c);
    else
      Arrays.sort(a, fromIndex, toIndex);
  }

  /**
   * Sorts the specified array of {@code short}s, according to the specified
   * {@link ShortComparator}.
   *
   * @param a The array of {@code short}s.
   * @param c The {@link ShortComparator}.
   * @throws NullPointerException If {@code a} is null.
   */
  public static void sort(final short[] a, final ShortComparator c) {
    sort(a, 0, a.length, c);
  }

  /**
   * Sorts the specified array of {@code short}s, according to the specified
   * {@link ShortComparator}.
   *
   * @param a The array of {@code short}s.
   * @param fromIndex The index of the first element, inclusive, to be sorted.
   * @param toIndex The index of the last element, exclusive, to be sorted.
   * @param c The {@link ShortComparator}.
   * @throws NullPointerException If {@code a} is null.
   */
  public static void sort(final short[] a, final int fromIndex, final int toIndex, final ShortComparator c) {
    if (c != null)
      PrimitiveSort.sort(a, fromIndex, toIndex, c);
    else
      Arrays.sort(a, fromIndex, toIndex);
  }

  /**
   * Sorts the specified array of {@code int}s, according to the specified
   * {@link IntComparator}.
   *
   * @param a The array of {@code int}s.
   * @param c The {@link IntComparator}.
   * @throws NullPointerException If {@code a} is null.
   */
  public static void sort(final int[] a, final IntComparator c) {
    sort(a, 0, a.length, c);
  }

  /**
   * Sorts the specified array of {@code int}s, according to the specified
   * {@link IntComparator}.
   *
   * @param a The array of {@code int}s.
   * @param fromIndex The index of the first element, inclusive, to be sorted.
   * @param toIndex The index of the last element, exclusive, to be sorted.
   * @param c The {@link IntComparator}.
   * @throws NullPointerException If {@code a} is null.
   */
  public static void sort(final int[] a, final int fromIndex, final int toIndex, final IntComparator c) {
    if (c != null)
      PrimitiveSort.sort(a, fromIndex, toIndex, c);
    else
      Arrays.sort(a, fromIndex, toIndex);
  }

  /**
   * Sorts the specified array of {@code long}s, according to the specified
   * {@link LongComparator}.
   *
   * @param a The array of {@code long}s.
   * @param c The {@link LongComparator}.
   * @throws NullPointerException If {@code a} is null.
   */
  public static void sort(final long[] a, final LongComparator c) {
    sort(a, 0, a.length, c);
  }

  /**
   * Sorts the specified array of {@code long}s, according to the specified
   * {@link LongComparator}.
   *
   * @param a The array of {@code long}s.
   * @param fromIndex The index of the first element, inclusive, to be sorted.
   * @param toIndex The index of the last element, exclusive, to be sorted.
   * @param c The {@link LongComparator}.
   * @throws NullPointerException If {@code a} is null.
   */
  public static void sort(final long[] a, final int fromIndex, final int toIndex, final LongComparator c) {
    if (c != null)
      PrimitiveSort.sort(a, fromIndex, toIndex, c);
    else
      Arrays.sort(a, fromIndex, toIndex);
  }

  /**
   * Sorts the specified array of {@code float}s, according to the specified
   * {@link FloatComparator}.
   *
   * @param a The array of {@code float}s.
   * @param c The {@link FloatComparator}.
   * @throws NullPointerException If {@code a} is null.
   */
  public static void sort(final float[] a, final FloatComparator c) {
    sort(a, 0, a.length, c);
  }

  /**
   * Sorts the specified array of {@code float}s, according to the specified
   * {@link FloatComparator}.
   *
   * @param a The array of {@code float}s.
   * @param fromIndex The index of the first element, inclusive, to be sorted.
   * @param toIndex The index of the last element, exclusive, to be sorted.
   * @param c The {@link FloatComparator}.
   * @throws NullPointerException If {@code a} is null.
   */
  public static void sort(final float[] a, final int fromIndex, final int toIndex, final FloatComparator c) {
    if (c != null)
      PrimitiveSort.sort(a, fromIndex, toIndex, c);
    else
      Arrays.sort(a, fromIndex, toIndex);
  }

  /**
   * Sorts the specified array of {@code double}s, according to the specified
   * {@link DoubleComparator}.
   *
   * @param a The array of {@code double}s.
   * @param c The {@link DoubleComparator}.
   * @throws NullPointerException If {@code a} is null.
   */
  public static void sort(final double[] a, final DoubleComparator c) {
    sort(a, 0, a.length, c);
  }

  /**
   * Sorts the specified array of {@code double}s, according to the specified
   * {@link DoubleComparator}.
   *
   * @param a The array of {@code double}s.
   * @param fromIndex The index of the first element, inclusive, to be sorted.
   * @param toIndex The index of the last element, exclusive, to be sorted.
   * @param c The {@link DoubleComparator}.
   * @throws NullPointerException If {@code a} is null.
   */
  public static void sort(final double[] a, final int fromIndex, final int toIndex, final DoubleComparator c) {
    if (c != null)
      PrimitiveSort.sort(a, fromIndex, toIndex, c);
    else
      Arrays.sort(a, fromIndex, toIndex);
  }

  /**
   * Reverses the order of the members in the provided array.
   *
   * @param array The array of members to be reversed.
   * @return The provided array with its members reversed.
   */
  public static byte[] reverse(final byte[] array) {
    if (array == null || array.length <= 1)
      return array;

    for (int i = 0, j = array.length - 1, len = array.length / 2; i < len; ++i, --j) {
      array[i] ^= array[j];
      array[j] ^= array[i];
      array[i] ^= array[j];
    }

    return array;
  }

  /**
   * Reverses the order of the members in the provided array.
   *
   * @param array The array of members to be reversed.
   * @return The provided array with its members reversed.
   */
  public static short[] reverse(final short[] array) {
    if (array == null || array.length <= 1)
      return array;

    for (int i = 0, j = array.length - 1, len = array.length / 2; i < len; ++i, --j) {
      array[i] ^= array[j];
      array[j] ^= array[i];
      array[i] ^= array[j];
    }

    return array;
  }

  /**
   * Reverses the order of the members in the provided array.
   *
   * @param array The array of members to be reversed.
   * @return The provided array with its members reversed.
   */
  public static int[] reverse(final int[] array) {
    if (array == null || array.length <= 1)
      return array;

    for (int i = 0, j = array.length - 1, len = array.length / 2; i < len; ++i, --j) {
      array[i] ^= array[j];
      array[j] ^= array[i];
      array[i] ^= array[j];
    }

    return array;
  }

  /**
   * Reverses the order of the members in the provided array.
   *
   * @param array The array of members to be reversed.
   * @return The provided array with its members reversed.
   */
  public static long[] reverse(final long[] array) {
    if (array == null || array.length <= 1)
      return array;

    for (int i = 0, j = array.length - 1, len = array.length / 2; i < len; ++i, --j) {
      array[i] ^= array[j];
      array[j] ^= array[i];
      array[i] ^= array[j];
    }

    return array;
  }

  /**
   * Reverses the order of the members in the provided array.
   *
   * @param array The array of members to be reversed.
   * @return The provided array with its members reversed.
   */
  public static boolean[] reverse(final boolean[] array) {
    if (array == null || array.length <= 1)
      return array;

    for (int i = 0, j = array.length - 1, len = array.length / 2; i < len; ++i, --j) {
      array[i] ^= array[j];
      array[j] ^= array[i];
      array[i] ^= array[j];
    }

    return array;
  }

  /**
   * Reverses the order of the members in the provided array.
   *
   * @param array The array of members to be reversed.
   * @return The provided array with its members reversed.
   */
  public static float[] reverse(final float[] array) {
    if (array == null || array.length <= 1)
      return array;

    float temp;
    for (int i = 0, j = array.length - 1, len = array.length / 2; i < len; ++i, --j) {
      temp = array[i];
      array[i] = array[j];
      array[j] = temp;
    }

    return array;
  }

  /**
   * Reverses the order of the members in the provided array.
   *
   * @param array The array of members to be reversed.
   * @return The provided array with its members reversed.
   */
  public static double[] reverse(final double[] array) {
    if (array == null || array.length <= 1)
      return array;

    double temp;
    for (int i = 0, j = array.length - 1, len = array.length / 2; i < len; ++i, --j) {
      temp = array[i];
      array[i] = array[j];
      array[j] = temp;
    }

    return array;
  }

  /**
   * Reverses the order of the members in the provided array.
   *
   * @param <T> The component type of the array.
   * @param array The array of members to be reversed.
   * @return The provided array with its members reversed.
   */
  @SafeVarargs
  public static <T>T[] reverse(final T ... array) {
    if (array == null || array.length < 2)
      return array;

    T temp;
    for (int i = 0, j = array.length - 1, len = array.length / 2; i < len; ++i, --j) {
      temp = array[i];
      array[i] = array[j];
      array[j] = temp;
    }

    return array;
  }

  private ArrayUtil() {
  }
}