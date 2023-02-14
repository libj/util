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

import static org.libj.lang.Assertions.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

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
   * The empty {@code byte[]} array.
   */
  public static final byte[] EMPTY_ARRAY_BYTE = {};

  /**
   * The empty {@code char[]} array.
   */
  public static final char[] EMPTY_ARRAY_CHAR = {};

  /**
   * The empty {@code short[]} array.
   */
  public static final short[] EMPTY_ARRAY_SHORT = {};

  /**
   * The empty {@code int[]} array.
   */
  public static final int[] EMPTY_ARRAY_INT = {};

  /**
   * The empty {@code long[]} array.
   */
  public static final long[] EMPTY_ARRAY_LONG = {};

  /**
   * The empty {@code float[]} array.
   */
  public static final float[] EMPTY_ARRAY_FLOAT = {};

  /**
   * The empty {@code double[]} array.
   */
  public static final double[] EMPTY_ARRAY_DOUBLE = {};

  /**
   * The empty {@code boolean[]} array.
   */
  public static final boolean[] EMPTY_ARRAY_BOOLEAN = {};

  /**
   * Returns true if the two specified arrays of bytes, over the provided ranges, are <i>equal</i> to one another.
   * <p>
   * Two arrays are considered equal if the number of elements covered by each range is the same, and all corresponding pairs of
   * elements over the provided ranges in the two arrays are equal. In other words, two arrays are equal if they contain, over the
   * provided ranges, the same elements in the same order.
   *
   * @param a The first array to be tested for equality.
   * @param aFromIndex The index (inclusive) of the first element in the first array to be tested.
   * @param aToIndex The index (exclusive) of the last element in the first array to be tested.
   * @param b The second array to be tested from equality.
   * @param bFromIndex The index (inclusive) of the first element in the second array to be tested.
   * @param bToIndex The index (exclusive) of the last element in the second array to be tested.
   * @return {@code true} If the two arrays, over the provided ranges, are equal.
   * @throws ArrayIndexOutOfBoundsException If {@code aFromIndex < 0 or aToIndex > a.length} or if
   *           {@code bFromIndex < 0 or bToIndex > b.length}.
   * @throws NullPointerException If either array is null.
   * @throws IllegalArgumentException If {@code bFromIndex > bToIndex}.
   */
  public static boolean equals(final byte[] a, int aFromIndex, final int aToIndex, final byte[] b, int bFromIndex, final int bToIndex) {
    assertRangeArray(aFromIndex, aToIndex, a.length);
    assertRangeArray(bFromIndex, bToIndex, b.length);

    final int aLength = aToIndex - aFromIndex;
    final int bLength = bToIndex - bFromIndex;
    if (aLength != bLength)
      return false;

    for (int i = 0; i < aLength; ++i) // [A]
      if (a[aFromIndex++] != b[bFromIndex++])
        return false;

    return true;
  }

  /**
   * Returns true if the two specified arrays of {@code char}s, over the provided ranges, are <i>equal</i> to one another.
   * <p>
   * Two arrays are considered equal if the number of elements covered by each range is the same, and all corresponding pairs of
   * elements over the provided ranges in the two arrays are equal. In other words, two arrays are equal if they contain, over the
   * provided ranges, the same elements in the same order.
   *
   * @param a The first array to be tested for equality.
   * @param aFromIndex The index (inclusive) of the first element in the first array to be tested.
   * @param aToIndex The index (exclusive) of the last element in the first array to be tested.
   * @param b The second array to be tested from equality.
   * @param bFromIndex The index (inclusive) of the first element in the second array to be tested.
   * @param bToIndex The index (exclusive) of the last element in the second array to be tested.
   * @return {@code true} If the two arrays, over the provided ranges, are equal.
   * @throws ArrayIndexOutOfBoundsException If {@code aFromIndex < 0 or aToIndex > a.length} or if
   *           {@code bFromIndex < 0 or bToIndex > b.length}.
   * @throws NullPointerException If either array is null.
   * @throws IllegalArgumentException If {@code bFromIndex > bToIndex}.
   */
  public static boolean equals(final char[] a, int aFromIndex, final int aToIndex, final char[] b, int bFromIndex, final int bToIndex) {
    assertRangeArray(aFromIndex, aToIndex, a.length);
    assertRangeArray(bFromIndex, bToIndex, b.length);

    final int aLength = aToIndex - aFromIndex;
    final int bLength = bToIndex - bFromIndex;
    if (aLength != bLength)
      return false;

    for (int i = 0; i < aLength; ++i) // [A]
      if (a[aFromIndex++] != b[bFromIndex++])
        return false;

    return true;
  }

  /**
   * Returns true if the two specified arrays of {@code short}s, over the provided ranges, are <i>equal</i> to one another.
   * <p>
   * Two arrays are considered equal if the number of elements covered by each range is the same, and all corresponding pairs of
   * elements over the provided ranges in the two arrays are equal. In other words, two arrays are equal if they contain, over the
   * provided ranges, the same elements in the same order.
   *
   * @param a The first array to be tested for equality.
   * @param aFromIndex The index (inclusive) of the first element in the first array to be tested.
   * @param aToIndex The index (exclusive) of the last element in the first array to be tested.
   * @param b The second array to be tested from equality.
   * @param bFromIndex The index (inclusive) of the first element in the second array to be tested.
   * @param bToIndex The index (exclusive) of the last element in the second array to be tested.
   * @return {@code true} If the two arrays, over the provided ranges, are equal.
   * @throws ArrayIndexOutOfBoundsException If {@code aFromIndex < 0 or aToIndex > a.length} or if
   *           {@code bFromIndex < 0 or bToIndex > b.length}.
   * @throws NullPointerException If either array is null.
   * @throws IllegalArgumentException If {@code bFromIndex > bToIndex}.
   */
  public static boolean equals(final short[] a, int aFromIndex, final int aToIndex, final short[] b, int bFromIndex, final int bToIndex) {
    assertRangeArray(aFromIndex, aToIndex, a.length);
    assertRangeArray(bFromIndex, bToIndex, b.length);

    final int aLength = aToIndex - aFromIndex;
    final int bLength = bToIndex - bFromIndex;
    if (aLength != bLength)
      return false;

    for (int i = 0; i < aLength; ++i) // [A]
      if (a[aFromIndex++] != b[bFromIndex++])
        return false;

    return true;
  }

  /**
   * Returns true if the two specified arrays of {@code int}s, over the provided ranges, are <i>equal</i> to one another.
   * <p>
   * Two arrays are considered equal if the number of elements covered by each range is the same, and all corresponding pairs of
   * elements over the provided ranges in the two arrays are equal. In other words, two arrays are equal if they contain, over the
   * provided ranges, the same elements in the same order.
   *
   * @param a The first array to be tested for equality.
   * @param aFromIndex The index (inclusive) of the first element in the first array to be tested.
   * @param aToIndex The index (exclusive) of the last element in the first array to be tested.
   * @param b The second array to be tested from equality.
   * @param bFromIndex The index (inclusive) of the first element in the second array to be tested.
   * @param bToIndex The index (exclusive) of the last element in the second array to be tested.
   * @return {@code true} If the two arrays, over the provided ranges, are equal.
   * @throws ArrayIndexOutOfBoundsException If {@code aFromIndex < 0 or aToIndex > a.length} or if
   *           {@code bFromIndex < 0 or bToIndex > b.length}.
   * @throws NullPointerException If either array is null.
   * @throws IllegalArgumentException If {@code bFromIndex > bToIndex}.
   */
  public static boolean equals(final int[] a, int aFromIndex, final int aToIndex, final int[] b, int bFromIndex, final int bToIndex) {
    assertRangeArray(aFromIndex, aToIndex, a.length);
    assertRangeArray(bFromIndex, bToIndex, b.length);

    final int aLength = aToIndex - aFromIndex;
    final int bLength = bToIndex - bFromIndex;
    if (aLength != bLength)
      return false;

    for (int i = 0; i < aLength; ++i) // [A]
      if (a[aFromIndex++] != b[bFromIndex++])
        return false;

    return true;
  }

  /**
   * Returns true if the two specified arrays of {@code long}s, over the provided ranges, are <i>equal</i> to one another.
   * <p>
   * Two arrays are considered equal if the number of elements covered by each range is the same, and all corresponding pairs of
   * elements over the provided ranges in the two arrays are equal. In other words, two arrays are equal if they contain, over the
   * provided ranges, the same elements in the same order.
   *
   * @param a The first array to be tested for equality.
   * @param aFromIndex The index (inclusive) of the first element in the first array to be tested.
   * @param aToIndex The index (exclusive) of the last element in the first array to be tested.
   * @param b The second array to be tested from equality.
   * @param bFromIndex The index (inclusive) of the first element in the second array to be tested.
   * @param bToIndex The index (exclusive) of the last element in the second array to be tested.
   * @return {@code true} If the two arrays, over the provided ranges, are equal.
   * @throws ArrayIndexOutOfBoundsException If {@code aFromIndex < 0 or aToIndex > a.length} or if
   *           {@code bFromIndex < 0 or bToIndex > b.length}.
   * @throws NullPointerException If either array is null.
   * @throws IllegalArgumentException If {@code bFromIndex > bToIndex}.
   */
  public static boolean equals(final long[] a, int aFromIndex, final int aToIndex, final long[] b, int bFromIndex, final int bToIndex) {
    assertRangeArray(aFromIndex, aToIndex, a.length);
    assertRangeArray(bFromIndex, bToIndex, b.length);

    final int aLength = aToIndex - aFromIndex;
    final int bLength = bToIndex - bFromIndex;
    if (aLength != bLength)
      return false;

    for (int i = 0; i < aLength; ++i) // [A]
      if (a[aFromIndex++] != b[bFromIndex++])
        return false;

    return true;
  }

  /**
   * Returns true if the two specified arrays of {@code float}s, over the provided ranges, are <i>equal</i> to one another.
   * <p>
   * Two arrays are considered equal if the number of elements covered by each range is the same, and all corresponding pairs of
   * elements over the provided ranges in the two arrays are equal. In other words, two arrays are equal if they contain, over the
   * provided ranges, the same elements in the same order.
   *
   * @param a The first array to be tested for equality.
   * @param aFromIndex The index (inclusive) of the first element in the first array to be tested.
   * @param aToIndex The index (exclusive) of the last element in the first array to be tested.
   * @param b The second array to be tested from equality.
   * @param bFromIndex The index (inclusive) of the first element in the second array to be tested.
   * @param bToIndex The index (exclusive) of the last element in the second array to be tested.
   * @return {@code true} If the two arrays, over the provided ranges, are equal.
   * @throws ArrayIndexOutOfBoundsException If {@code aFromIndex < 0 or aToIndex > a.length} or if
   *           {@code bFromIndex < 0 or bToIndex > b.length}.
   * @throws NullPointerException If either array is null.
   * @throws IllegalArgumentException If {@code bFromIndex > bToIndex}.
   */
  public static boolean equals(final float[] a, int aFromIndex, final int aToIndex, final float[] b, int bFromIndex, final int bToIndex) {
    assertRangeArray(aFromIndex, aToIndex, a.length);
    assertRangeArray(bFromIndex, bToIndex, b.length);

    final int aLength = aToIndex - aFromIndex;
    final int bLength = bToIndex - bFromIndex;
    if (aLength != bLength)
      return false;

    for (int i = 0; i < aLength; ++i) // [A]
      if (a[aFromIndex++] != b[bFromIndex++])
        return false;

    return true;
  }

  /**
   * Returns true if the two specified arrays of {@code double}s, over the provided ranges, are <i>equal</i> to one another.
   * <p>
   * Two arrays are considered equal if the number of elements covered by each range is the same, and all corresponding pairs of
   * elements over the provided ranges in the two arrays are equal. In other words, two arrays are equal if they contain, over the
   * provided ranges, the same elements in the same order.
   *
   * @param a The first array to be tested for equality.
   * @param aFromIndex The index (inclusive) of the first element in the first array to be tested.
   * @param aToIndex The index (exclusive) of the last element in the first array to be tested.
   * @param b The second array to be tested from equality.
   * @param bFromIndex The index (inclusive) of the first element in the second array to be tested.
   * @param bToIndex The index (exclusive) of the last element in the second array to be tested.
   * @return {@code true} If the two arrays, over the provided ranges, are equal.
   * @throws ArrayIndexOutOfBoundsException If {@code aFromIndex < 0 or aToIndex > a.length} or if
   *           {@code bFromIndex < 0 or bToIndex > b.length}.
   * @throws NullPointerException If either array is null.
   * @throws IllegalArgumentException If {@code bFromIndex > bToIndex}.
   */
  public static boolean equals(final double[] a, int aFromIndex, final int aToIndex, final double[] b, int bFromIndex, final int bToIndex) {
    assertRangeArray(aFromIndex, aToIndex, a.length);
    assertRangeArray(bFromIndex, bToIndex, b.length);

    final int aLength = aToIndex - aFromIndex;
    final int bLength = bToIndex - bFromIndex;
    if (aLength != bLength)
      return false;

    for (int i = 0; i < aLength; ++i) // [A]
      if (a[aFromIndex++] != b[bFromIndex++])
        return false;

    return true;
  }

  /**
   * Returns true if the two specified arrays of objects, over the provided ranges, are <i>equal</i> to one another.
   * <p>
   * Two arrays are considered equal if the number of elements covered by each range is the same, and all corresponding pairs of
   * elements over the provided ranges in the two arrays are equal. In other words, two arrays are equal if they contain, over the
   * provided ranges, the same elements in the same order.
   *
   * @param a The first array to be tested for equality.
   * @param aFromIndex The index (inclusive) of the first element in the first array to be tested.
   * @param aToIndex The index (exclusive) of the last element in the first array to be tested.
   * @param b The second array to be tested from equality.
   * @param bFromIndex The index (inclusive) of the first element in the second array to be tested.
   * @param bToIndex The index (exclusive) of the last element in the second array to be tested.
   * @return {@code true} If the two arrays, over the provided ranges, are equal.
   * @throws ArrayIndexOutOfBoundsException If {@code aFromIndex < 0 or aToIndex > a.length} or if
   *           {@code bFromIndex < 0 or bToIndex > b.length}.
   * @throws NullPointerException If either array is null.
   * @throws IllegalArgumentException If {@code bFromIndex > bToIndex}.
   */
  public static boolean equals(final Object[] a, int aFromIndex, final int aToIndex, final Object[] b, int bFromIndex, final int bToIndex) {
    assertRangeArray(aFromIndex, aToIndex, a.length);
    assertRangeArray(bFromIndex, bToIndex, b.length);

    final int aLength = aToIndex - aFromIndex;
    final int bLength = bToIndex - bFromIndex;
    if (aLength != bLength)
      return false;

    for (int i = 0; i < aLength; ++i) { // [A]
      final Object oa = a[aFromIndex++];
      final Object ob = b[bFromIndex++];
      if (!Objects.equals(oa, ob))
        return false;
    }

    return true;
  }

  /**
   * Returns the length of the specified array, summed with the lengths of all nested arrays at every depth. The value of
   * {@code member.getClass().isArray()} is used to determine whether an array member represents an array for further recursion.
   * <p>
   * Array members that reference an array are <i>not included</i> in the count. This is the equivalent of calling:
   *
   * <pre>
   * {@code lengthDeep(Object[],false)}
   * </pre>
   *
   * @param a The array.
   * @return The length of the specified array, summed with the lengths of all nested arrays at every depth.
   * @throws NullPointerException If the provided array is null.
   */
  public static int lengthDeep(final Object[] a) {
    return lengthDeep(a, false);
  }

  /**
   * Returns the length of the specified array, summed with the lengths of all nested arrays at every depth. The value of
   * {@code member.getClass().isArray()} is used to determine whether an array member represents an array for further recursion.
   *
   * @param <T> The component type of the specified array.
   * @param a The array.
   * @param countArrayReferences If {@code true}, array members that reference an array are included in the count; if {@code false},
   *          they are not included in the count.
   * @return The length of the specified array, summed with the lengths of all nested arrays at every depth.
   * @throws NullPointerException If the provided array is null.
   */
  public static <T>int lengthDeep(final T[] a, final boolean countArrayReferences) {
    return lengthDeep(a, null, countArrayReferences);
  }

  /**
   * Returns the length of the specified array, summed with the lengths of all nested arrays at every depth. The specified resolver
   * {@link Function} provides a layer of indirection between an array member, and a higher-layer value. This is useful in the
   * situation where the array contains symbolic references to other arrays. The {@code resolver} parameter is provided to
   * dereference such a symbolic references.
   *
   * @param <T> The component type of the specified array.
   * @param a The array.
   * @param resolver A {@link Function} to provide a layer of indirection between an array member, and a higher-layer value. If
   *          {@code resolver} is null, {@code member.getClass().isArray()} is used to determine whether the member value represents
   *          an array.
   * @param countArrayReferences If {@code true}, array members that reference an array are included in the count; if {@code false},
   *          they are not included in the count.
   * @return The length of the specified array, summed with the lengths of all nested arrays at every depth.
   * @throws NullPointerException If the provided array is null.
   */
  @SuppressWarnings("unchecked")
  public static <T>int lengthDeep(final T[] a, final Function<? super T,T[]> resolver, final boolean countArrayReferences) {
    int size = 0;
    for (int i = 0, i$ = a.length; i < i$; ++i) { // [A]
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
   * Returns a one-dimensional array with the members of the specified array, and the members of all nested arrays at every depth.
   * The value of {@code member.getClass().isArray()} is used to determine whether an array member represents an array for further
   * recursion.
   * <p>
   * Array members that reference an array are <i>not included</i> in the resulting array. This is the equivalent of calling
   * {@code flatten(Object[],false)}.
   *
   * @param a The array.
   * @return A one-dimensional array with the members of the specified array, and the members of all nested arrays at every depth.
   * @throws NullPointerException If the provided array is null.
   */
  public static Object[] flatten(final Object[] a) {
    return flatten(a, null, false);
  }

  /**
   * Returns a one-dimensional array with the members of the specified array, and the members of all nested arrays at every depth.
   * The value of {@code member.getClass().isArray()} is used to determine whether an array member represents an array for further
   * recursion.
   *
   * @param a The array.
   * @param retainArrayReferences If {@code true}, array members that reference an array are included in the resulting array; if
   *          {@code false}, they are not included in the resulting array.
   * @return A one-dimensional array with the members of the specified array, and the members of all nested arrays at every depth.
   * @throws NullPointerException If the provided array is null.
   */
  public static Object[] flatten(final Object[] a, final boolean retainArrayReferences) {
    return flatten(a, null, retainArrayReferences);
  }

  /**
   * Returns a one-dimensional array with the members of the specified array, and the members of all nested arrays at every depth.
   * The specified resolver {@link Function} provides a layer of indirection between an array member, and a higher-layer value. This
   * is useful in the situation where the array contains symbolic references to other arrays. The {@code resolver} parameter is
   * provided to dereference such a symbolic references.
   *
   * @param <T> The component type of the specified array.
   * @param a The array.
   * @param resolver A {@link Function} to provide a layer of indirection between an array member, and a higher-layer value. If
   *          {@code resolver} is null, {@code member.getClass().isArray()} is used to determine whether the member value represents
   *          an array.
   * @param retainArrayReferences If {@code true}, array members that reference an array are included in the resulting array; if
   *          {@code false}, they are not included in the resulting array.
   * @return A one-dimensional array with the members of the specified array, and the members of all nested arrays at every depth.
   * @throws NullPointerException If the provided array is null.
   */
  @SuppressWarnings("unchecked")
  public static <T>T[] flatten(final T[] a, final Function<? super T,T[]> resolver, final boolean retainArrayReferences) {
    final T[] out = (T[])Array.newInstance(a.getClass().getComponentType(), lengthDeep(a, resolver, retainArrayReferences));
    flatten0(a, out, resolver, retainArrayReferences, -1);
    return out;
  }

  @SuppressWarnings("unchecked")
  private static <T>int flatten0(final T[] in, final Object[] out, final Function<? super T,T[]> resolver, final boolean retainArrayReferences, int index) {
    for (int i = 0, i$ = in.length; i < i$; ++i) { // [A]
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
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match. The returned index will be less than or equal to an exact match.
   *
   * @param <T> Type parameter of {@link Comparable} object.
   * @param a The sorted array.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws NullPointerException If the provided array is null.
   */
  public static <T extends Comparable<? super T>>int binaryClosestSearch(final T[] a, final T key) {
    return binaryClosestSearch0(a, 0, a.length, key);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match. The returned index will be less than or equal to an exact match.
   *
   * @param <T> Type parameter of {@link Comparable} object.
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws NullPointerException If the provided array is null.
   */
  public static <T extends Comparable<? super T>>int binaryClosestSearch(final T[] a, final int fromIndex, final int toIndex, final T key) {
    assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key);
  }

  private static <T extends Comparable<? super T>>int binaryClosestSearch0(final T[] a, int fromIndex, int toIndex, final T key) {
    for (int mid, com; fromIndex < toIndex;) { // [A]
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
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match. The returned index will be less than or equal to an exact match.
   *
   * @param <T> Type parameter of {@link Comparable} object.
   * @param <K> Type parameter of key.
   * @param a The sorted array.
   * @param key The value to match.
   * @param objectToKey {@link Function} to dereference the key of an object.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws NullPointerException If the provided array or {@code objectToKey} is null.
   */
  public static <T,K extends Comparable<? super K>>int binaryClosestSearch(final T[] a, final K key, final Function<T,K> objectToKey) {
    return binaryClosestSearch0(a, 0, a.length, key, objectToKey);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match. The returned index will be less than or equal to an exact match.
   *
   * @param <T> Type parameter of {@link Comparable} object.
   * @param <K> Type parameter of key.
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @param objectToKey {@link Function} to dereference the key of an object.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws NullPointerException If the provided array or {@code objectToKey} is null.
   */
  public static <T,K extends Comparable<? super K>>int binaryClosestSearch(final T[] a, final int fromIndex, final int toIndex, final K key, final Function<T,K> objectToKey) {
    assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, objectToKey);
  }

  private static <T,K extends Comparable<? super K>>int binaryClosestSearch0(final T[] a, int fromIndex, int toIndex, final K key, final Function<T,K> objectToKey) {
    for (int mid, com; fromIndex < toIndex;) { // [A]
      mid = (fromIndex + toIndex) / 2;
      com = key.compareTo(objectToKey.apply(a[mid]));
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
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param <T> Type parameter of object.
   * @param a The sorted array.
   * @param key The value to match.
   * @param c The {@link Comparator} for objects of type {@code <T>}.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws NullPointerException If the provided array is null.
   */
  public static <T>int binaryClosestSearch(final T[] a, final T key, final Comparator<? super T> c) {
    return binaryClosestSearch0(a, 0, a.length, key, c);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param <T> Type parameter of object.
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @param c The {@link Comparator} for objects of type {@code <T>}.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws NullPointerException If the provided array is null.
   */
  public static <T>int binaryClosestSearch(final T[] a, final int fromIndex, final int toIndex, final T key, final Comparator<? super T> c) {
    assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, c);
  }

  private static <T>int binaryClosestSearch0(final T[] a, int fromIndex, int toIndex, final T key, final Comparator<? super T> c) {
    for (int mid, com; fromIndex < toIndex;) { // [A]
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
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param <T> Type parameter of object.
   * @param <K> Type parameter of key.
   * @param a The sorted array.
   * @param key The value to match.
   * @param objectToKey {@link Function} to dereference the key of an object.
   * @param c The {@link Comparator} for objects of type {@code <T>}.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws NullPointerException If the provided array or {@code objectToKey} is null.
   */
  public static <T,K>int binaryClosestSearch(final T[] a, final K key, final Function<T,K> objectToKey, final Comparator<K> c) {
    return binaryClosestSearch0(a, 0, a.length, key, objectToKey, c);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param <T> Type parameter of object.
   * @param <K> Type parameter of key.
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @param objectToKey {@link Function} to dereference the key of an object.
   * @param c The {@link Comparator} for objects of type {@code <T>}.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws NullPointerException If the provided array or {@code objectToKey} is null.
   */
  public static <T,K>int binaryClosestSearch(final T[] a, final int fromIndex, final int toIndex, final K key, final Function<T,K> objectToKey, final Comparator<K> c) {
    assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, objectToKey, c);
  }

  private static <T,K>int binaryClosestSearch0(final T[] a, int fromIndex, int toIndex, final K key, final Function<T,K> objectToKey, final Comparator<K> c) {
    for (int mid, com; fromIndex < toIndex;) { // [A]
      mid = (fromIndex + toIndex) / 2;
      com = c.compare(key, objectToKey.apply(a[mid]));
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
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param a The sorted array.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws NullPointerException If the provided array is null.
   */
  public static int binaryClosestSearch(final byte[] a, final byte key) {
    return binaryClosestSearch0(a, 0, a.length, key, ByteComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws NullPointerException If the provided array is null.
   */
  public static int binaryClosestSearch(final byte[] a, final int fromIndex, final int toIndex, final byte key) {
    assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, ByteComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @param c The comparator to use.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws NullPointerException If the provided array is null.
   */
  public static int binaryClosestSearch(final byte[] a, final int fromIndex, final int toIndex, final byte key, final ByteComparator c) {
    assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, c);
  }

  private static int binaryClosestSearch0(final byte[] a, int fromIndex, int toIndex, final byte key, final ByteComparator c) {
    for (int mid, com; fromIndex < toIndex;) { // [A]
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
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param a The sorted array.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws NullPointerException If the provided array is null.
   */
  public static int binaryClosestSearch(final short[] a, final short key) {
    return binaryClosestSearch0(a, 0, a.length, key, ShortComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws NullPointerException If the provided array is null.
   */
  public static int binaryClosestSearch(final short[] a, final int fromIndex, final int toIndex, final short key) {
    assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, ShortComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @param c The comparator to use.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws NullPointerException If the provided array is null.
   */
  public static int binaryClosestSearch(final short[] a, final int fromIndex, final int toIndex, final short key, final ShortComparator c) {
    assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, c);
  }

  private static int binaryClosestSearch0(final short[] a, int fromIndex, int toIndex, final short key, final ShortComparator c) {
    for (int mid, com; fromIndex < toIndex;) { // [A]
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
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param a The sorted array.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws NullPointerException If the provided array is null.
   */
  public static int binaryClosestSearch(final int[] a, final int key) {
    return binaryClosestSearch0(a, 0, a.length, key, IntComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws NullPointerException If the provided array is null.
   */
  public static int binaryClosestSearch(final int[] a, final int fromIndex, final int toIndex, final int key) {
    assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, IntComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @param c The comparator to use.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws NullPointerException If the provided array is null.
   */
  public static int binaryClosestSearch(final int[] a, final int fromIndex, final int toIndex, final int key, final IntComparator c) {
    assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, c);
  }

  private static int binaryClosestSearch0(final int[] a, int fromIndex, int toIndex, final int key, final IntComparator c) {
    for (int mid, com; fromIndex < toIndex;) { // [A]
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
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param a The sorted array.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws NullPointerException If the provided array is null.
   */
  public static int binaryClosestSearch(final float[] a, final float key) {
    return binaryClosestSearch0(a, 0, a.length, key, FloatComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws NullPointerException If the provided array is null.
   */
  public static int binaryClosestSearch(final float[] a, final int fromIndex, final int toIndex, final float key) {
    assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, FloatComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @param c The comparator to use.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws NullPointerException If the provided array is null.
   */
  public static int binaryClosestSearch(final float[] a, final int fromIndex, final int toIndex, final float key, final FloatComparator c) {
    assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, c);
  }

  private static int binaryClosestSearch0(final float[] a, int fromIndex, int toIndex, final float key, final FloatComparator c) {
    for (int mid, com; fromIndex < toIndex;) { // [A]
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
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param a The sorted array.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws NullPointerException If the provided array is null.
   */
  public static int binaryClosestSearch(final double[] a, final double key) {
    return binaryClosestSearch0(a, 0, a.length, key, DoubleComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws NullPointerException If the provided array is null.
   */
  public static int binaryClosestSearch(final double[] a, final int fromIndex, final int toIndex, final double key) {
    assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, DoubleComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @param c The comparator to use.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws NullPointerException If the provided array is null.
   */
  public static int binaryClosestSearch(final double[] a, final int fromIndex, final int toIndex, final double key, final DoubleComparator c) {
    assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, c);
  }

  private static int binaryClosestSearch0(final double[] a, int fromIndex, int toIndex, final double key, final DoubleComparator c) {
    for (int mid, com; fromIndex < toIndex;) { // [A]
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
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param a The sorted array.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws NullPointerException If the provided array is null.
   */
  public static int binaryClosestSearch(final long[] a, final long key) {
    return binaryClosestSearch0(a, 0, a.length, key, LongComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws NullPointerException If the provided array is null.
   */
  public static int binaryClosestSearch(final long[] a, final int fromIndex, final int toIndex, final long key) {
    assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, LongComparator.NATURAL);
  }

  /**
   * Find the index of the sorted array whose value most closely matches the value provided. The returned index will be less than or
   * equal to an exact match.
   *
   * @param a The sorted array.
   * @param fromIndex The starting index of the sorted array to search from.
   * @param toIndex The ending index of the sorted array to search to.
   * @param key The value to match.
   * @param c The comparator to use.
   * @return The closest index of the sorted array matching the desired value. The returned index will be less than or equal to an
   *         exact match.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws NullPointerException If the provided array is null.
   */
  public static int binaryClosestSearch(final long[] a, final int fromIndex, final int toIndex, final long key, final LongComparator c) {
    assertRangeArray(fromIndex, toIndex, a.length);
    return binaryClosestSearch0(a, fromIndex, toIndex, key, c);
  }

  private static int binaryClosestSearch0(final long[] a, int fromIndex, int toIndex, final long key, final LongComparator c) {
    for (int mid, com; fromIndex < toIndex;) { // [A]
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
   * Replace all members of the provided array with the provided {@link UnaryOperator}.
   *
   * @param <T> Type parameter of object.
   * @param operator The {@link UnaryOperator} that defines the replacement operation.
   * @param array The array whose members are to be replaced.
   * @return The the original array instance with its members replaced by the operator.
   * @throws NullPointerException If {@code operator} or {@code array} is null.
   */
  @SafeVarargs
  public static <T>T[] replaceAll(final UnaryOperator<T> operator, final T ... array) {
    for (int i = 0, i$ = array.length; i < i$; ++i) // [A]
      array[i] = operator.apply(array[i]);

    return array;
  }

  /**
   * Filter the provided array with the specified {@link Predicate}. This method is recursive.
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
  private static <T>T[] concat0(final T[] array1, final T[] array2, final T[] ... arrays) {
    int length = array1.length + array2.length;
    for (int i = 0, i$ = arrays.length; i < i$; ++i) // [A]
      length += arrays[i].length;

    final T[] concat = Arrays.copyOf(array1, length);
    System.arraycopy(array2, 0, concat, array1.length, array2.length);
    for (int i = 0, l = array1.length + array2.length; i < arrays.length; l += arrays[i].length, ++i) // [A]
      System.arraycopy(arrays[i], 0, concat, l, arrays[i].length);

    return concat;
  }

  /**
   * Returns a new array containing the members of the given 2-dimensional array as a 1-dimensional array.
   *
   * @param <T> Type parameter of object.
   * @param arrays The 2-dimensional array, whose array members are to be concatenated into a 1-dimensional array.
   * @return A new array containing the members of the given 2-dimensional array as a 1-dimensional array.
   * @throws NullPointerException If {@code arrays} is null.
   */
  @SuppressWarnings("unchecked")
  public static <T>T[] concat(final T[][] arrays) {
    int length = 0;
    for (int i = 0, i$ = arrays.length; i < i$; ++i) // [A]
      length += arrays[i].length;

    final T[] concat = (T[])Array.newInstance(arrays[0].getClass().getComponentType(), length);
    for (int i = 0, l = 0; i < arrays.length; l += arrays[i].length, ++i) // [A]
      System.arraycopy(arrays[i], 0, concat, l, arrays[i].length);

    return concat;
  }

  /**
   * Returns a new array containing the members of the given arrays concatenated in the provided order.
   *
   * @param <T> Type parameter of object.
   * @param array1 The first array to be concatenated.
   * @param array2 The second array to be concatenated.
   * @return A new array containing the members of the given arrays concatenated in the provided order.
   * @throws NullPointerException If {@code array1} or {code array2} is null.
   */
  public static <T>T[] concat(final T[] array1, final T[] array2) {
    final int length = array1.length + array2.length;
    final T[] concat = Arrays.copyOf(array1, length);
    System.arraycopy(array2, 0, concat, array1.length, array2.length);
    return concat;
  }

  /**
   * Returns a new array containing the members of the given arrays concatenated in the provided order.
   *
   * @param <T> Type parameter of object.
   * @param array1 The first array to be concatenated.
   * @param array2 The second array to be concatenated.
   * @param arrays The remaining arrays to be concatenated.
   * @return A new array containing the members of the given arrays concatenated in the provided order.
   * @throws NullPointerException If {@code array1}, {code array2} or {@code arrays} is null.
   */
  @SafeVarargs
  public static <T>T[] concat(final T[] array1, final T[] array2, final T[] ... arrays) {
    return concat0(array1, array2, arrays);
  }

  /**
   * Returns a new array containing the members of the given arrays concatenated in the provided order.
   *
   * @param <T> Type parameter of object.
   * @param array The array of first elements to be concatenated.
   * @param element1 The next element to be concatenated.
   * @param elements The additional elements to be concatenated.
   * @return A new array containing the members of the given arrays concatenated in the provided order.
   * @throws NullPointerException If {@code array} or {@code elements} is null.
   */
  @SafeVarargs
  public static <T>T[] concat(final T[] array, final T element1, final T ... elements) {
    Arrays.copyOf(array, array.length + 1 + elements.length);
    final T[] concat = Arrays.copyOf(array, array.length + 1 + elements.length);
    concat[array.length] = element1;
    System.arraycopy(elements, 0, concat, array.length + 1, elements.length);
    return concat;
  }

  /**
   * Returns a new array containing the members of the given {@code element} and provided {@code array}.
   *
   * @param <T> Type parameter of object.
   * @param element1 The first element to be concatenated.
   * @param array The additional elements to be concatenated.
   * @return A new array containing the members of the given {@code element} and provided {@code array}.
   * @throws NullPointerException If {@code array} is null.
   */
  @SuppressWarnings("unchecked")
  public static <T>T[] concat(final T element1, final T[] array) {
    final T[] concat = (T[])Array.newInstance(array.getClass().getComponentType(), array.length + 1);
    concat[0] = element1;
    System.arraycopy(array, 0, concat, 1, array.length);
    return concat;
  }

  /**
   * Returns a new array containing the members of the given {@code element} and provided {@code array}.
   *
   * @param <T> Type parameter of object.
   * @param element1 The first element to be concatenated.
   * @param element2 The second element to be concatenated.
   * @param elements The additional elements to be concatenated.
   * @return A new array containing the members of the given {@code element} and provided {@code array}.
   * @throws NullPointerException If {@code elements} is null.
   */
  @SuppressWarnings("unchecked")
  public static <T>T[] concat(final T element1, final T element2, final T ... elements) {
    final T[] concat = (T[])Array.newInstance(elements.getClass().getComponentType(), elements.length + 2);
    concat[0] = element1;
    concat[1] = element2;
    System.arraycopy(elements, 0, concat, 2, elements.length);
    return concat;
  }

  /**
   * Returns a new array by removing existing elements from the provided array.
   *
   * @param <T> Type parameter of object.
   * @param array The array to be spliced.
   * @param start Index at the greater of which to remove all elements in the array (with origin 0). If negative, index will be set
   *          to its calculated value from the end of the array (with origin 1).
   * @return A new array with elements removed from the provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static <T>T[] splice(final T[] array, int start) {
    if (start < 0)
      start += array.length;

    return splice(array, start, array.length - start);
  }

  /**
   * Returns a new array by removing existing elements from the provided array.
   *
   * @param <T> Type parameter of object.
   * @param array The array to be spliced.
   * @param start Index at which to begin changing the array (with origin 0). If negative, index will be set to its calculated value
   *          from the end of the array (with origin 1).
   * @param deleteCount An integer indicating the number of array elements to remove. If deleteCount is 0, no elements are removed,
   *          but a new reference to the array is returned.
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
   * Returns a new array by removing existing elements from the provided array.
   *
   * @param <T> Type parameter of object.
   * @param array The array to be spliced.
   * @param start Index at which to begin changing the array (with origin 0). If negative, index will be set to its calculated value
   *          from the end of the array (with origin 1).
   * @param deleteCount An integer indicating the number of array elements to remove. If deleteCount is 0, no elements are removed,
   *          but a new reference to the array is returned.
   * @param items The elements to add to the array, beginning at the start index.
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
   * Find the first index of a value in an array.
   *
   * @param array The array to search.
   * @param value The value to locate.
   * @return The index of the value if it is found, otherwise {@code -1}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static int indexOf(final byte[] array, final byte value) {
    return indexOf0(array, 0, array.length, value);
  }

  /**
   * Find the first index of a value in an array.
   *
   * @param array The array to search.
   * @param off The offset at which to start searching.
   * @param len The number of elements to search.
   * @param value The value to locate.
   * @return The index of the value if it is found, otherwise {@code -1}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static int indexOf(final byte[] array, final int off, int len, final byte value) {
    return indexOf0(array, off, Math.min(len, array.length), value);
  }

  private static <T>int indexOf0(final byte[] array, final int off, final int len, final byte value) {
    for (int i = off; i < len; ++i) // [A]
      if (value == array[i])
        return i;

    return -1;
  }

  /**
   * Find the first index of a value in an array.
   *
   * @param array The array to search.
   * @param value The value to locate.
   * @return The index of the value if it is found, otherwise {@code -1}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static int indexOf(final char[] array, final char value) {
    return indexOf0(array, 0, array.length, value);
  }

  /**
   * Find the first index of a value in an array.
   *
   * @param array The array to search.
   * @param off The offset at which to start searching.
   * @param len The number of elements to search.
   * @param value The value to locate.
   * @return The index of the value if it is found, otherwise {@code -1}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static int indexOf(final char[] array, final int off, int len, final char value) {
    return indexOf0(array, off, Math.min(len, array.length), value);
  }

  private static <T>int indexOf0(final char[] array, final int off, final int len, final char value) {
    for (int i = off; i < len; ++i) // [A]
      if (value == array[i])
        return i;

    return -1;
  }

  /**
   * Find the first index of a value in an array.
   *
   * @param array The array to search.
   * @param value The value to locate.
   * @return The index of the value if it is found, otherwise {@code -1}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static int indexOf(final short[] array, final short value) {
    return indexOf0(array, 0, array.length, value);
  }

  /**
   * Find the first index of a value in an array.
   *
   * @param array The array to search.
   * @param off The offset at which to start searching.
   * @param len The number of elements to search.
   * @param value The value to locate.
   * @return The index of the value if it is found, otherwise {@code -1}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static int indexOf(final short[] array, final int off, int len, final short value) {
    return indexOf0(array, off, Math.min(len, array.length), value);
  }

  private static <T>int indexOf0(final short[] array, final int off, final int len, final short value) {
    for (int i = off; i < len; ++i) // [A]
      if (value == array[i])
        return i;

    return -1;
  }

  /**
   * Find the first index of a value in an array.
   *
   * @param array The array to search.
   * @param value The value to locate.
   * @return The index of the value if it is found, otherwise {@code -1}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static int indexOf(final int[] array, final int value) {
    return indexOf0(array, 0, array.length, value);
  }

  /**
   * Find the first index of a value in an array.
   *
   * @param array The array to search.
   * @param off The offset at which to start searching.
   * @param len The number of elements to search.
   * @param value The value to locate.
   * @return The index of the value if it is found, otherwise {@code -1}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static int indexOf(final int[] array, final int off, int len, final int value) {
    return indexOf0(array, off, Math.min(len, array.length), value);
  }

  private static <T>int indexOf0(final int[] array, final int off, final int len, final int value) {
    for (int i = off; i < len; ++i) // [A]
      if (value == array[i])
        return i;

    return -1;
  }

  /**
   * Find the first index of a value in an array.
   *
   * @param array The array to search.
   * @param value The value to locate.
   * @return The index of the value if it is found, otherwise {@code -1}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static int indexOf(final long[] array, final long value) {
    return indexOf0(array, 0, array.length, value);
  }

  /**
   * Find the first index of a value in an array.
   *
   * @param array The array to search.
   * @param off The offset at which to start searching.
   * @param len The number of elements to search.
   * @param value The value to locate.
   * @return The index of the value if it is found, otherwise {@code -1}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static int indexOf(final long[] array, final int off, int len, final long value) {
    return indexOf0(array, off, Math.min(len, array.length), value);
  }

  private static <T>int indexOf0(final long[] array, final int off, final int len, final long value) {
    for (int i = off; i < len; ++i) // [A]
      if (value == array[i])
        return i;

    return -1;
  }

  /**
   * Find the first index of a value in an array.
   *
   * @param array The array to search.
   * @param value The value to locate.
   * @return The index of the value if it is found, otherwise {@code -1}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static int indexOf(final float[] array, final float value) {
    return indexOf0(array, 0, array.length, value);
  }

  /**
   * Find the first index of a value in an array.
   *
   * @param array The array to search.
   * @param off The offset at which to start searching.
   * @param len The number of elements to search.
   * @param value The value to locate.
   * @return The index of the value if it is found, otherwise {@code -1}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static int indexOf(final float[] array, final int off, int len, final float value) {
    return indexOf0(array, off, Math.min(len, array.length), value);
  }

  private static <T>int indexOf0(final float[] array, final int off, final int len, final float value) {
    for (int i = off; i < len; ++i) // [A]
      if (value == array[i])
        return i;

    return -1;
  }

  /**
   * Find the first index of a value in an array.
   *
   * @param array The array to search.
   * @param value The value to locate.
   * @return The index of the value if it is found, otherwise {@code -1}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static int indexOf(final double[] array, final double value) {
    return indexOf0(array, 0, array.length, value);
  }

  /**
   * Find the first index of a value in an array.
   *
   * @param array The array to search.
   * @param off The offset at which to start searching.
   * @param len The number of elements to search.
   * @param value The value to locate.
   * @return The index of the value if it is found, otherwise {@code -1}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static int indexOf(final double[] array, final int off, int len, final double value) {
    return indexOf0(array, off, Math.min(len, array.length), value);
  }

  private static <T>int indexOf0(final double[] array, final int off, final int len, final double value) {
    for (int i = off; i < len; ++i) // [A]
      if (value == array[i])
        return i;

    return -1;
  }

  /**
   * Find the first index of a value in an array.
   *
   * @param <T> Type parameter of {@code obj}.
   * @param array The array to search.
   * @param value The value to locate.
   * @return The index of the value if it is found, otherwise {@code -1}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static <T>int indexOf(final T[] array, final T value) {
    return indexOf0(array, 0, array.length, value);
  }

  /**
   * Find the first index of a value in an array.
   *
   * @param <T> Type parameter of {@code obj}.
   * @param array The array to search.
   * @param off The offset at which to start searching.
   * @param len The number of elements to search.
   * @param value The value to locate.
   * @return The index of the value if it is found, otherwise {@code -1}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static <T>int indexOf(final T[] array, final int off, int len, final T value) {
    return indexOf0(array, off, Math.min(len, array.length), value);
  }

  private static <T>int indexOf0(final T[] array, final int off, final int len, final T value) {
    for (int i = off; i < len; ++i) // [A]
      if (value.equals(array[i]))
        return i;

    return -1;
  }

  /**
   * Linear search for the existence of a value in an array.
   *
   * @param array The array to search.
   * @param value The value to locate.
   * @return {@code true} if the value exists, {@code false} otherwise.
   * @throws NullPointerException If {@code array} is null.
   */
  public static boolean contains(final byte[] array, final byte value) {
    return indexOf(array, value) >= 0;
  }

  /**
   * Linear search for the existence of a value in an array.
   *
   * @param array The array to search.
   * @param off The offset at which to start searching.
   * @param len The number of elements to search.
   * @param value The value to locate.
   * @return {@code true} if the value exists, {@code false} otherwise.
   * @throws NullPointerException If {@code array} is null.
   */
  public static boolean contains(final byte[] array, final int off, final int len, final byte value) {
    return indexOf(array, off, len, value) >= 0;
  }

  /**
   * Linear search for the existence of a value in an array.
   *
   * @param array The array to search.
   * @param value The value to locate.
   * @return {@code true} if the value exists, {@code false} otherwise.
   * @throws NullPointerException If {@code array} is null.
   */
  public static boolean contains(final char[] array, final char value) {
    return indexOf(array, value) >= 0;
  }

  /**
   * Linear search for the existence of a value in an array.
   *
   * @param array The array to search.
   * @param off The offset at which to start searching.
   * @param len The number of elements to search.
   * @param value The value to locate.
   * @return {@code true} if the value exists, {@code false} otherwise.
   * @throws NullPointerException If {@code array} is null.
   */
  public static boolean contains(final char[] array, final int off, final int len, final char value) {
    return indexOf(array, off, len, value) >= 0;
  }

  /**
   * Linear search for the existence of a value in an array.
   *
   * @param array The array to search.
   * @param value The value to locate.
   * @return {@code true} if the value exists, {@code false} otherwise.
   * @throws NullPointerException If {@code array} is null.
   */
  public static boolean contains(final short[] array, final short value) {
    return indexOf(array, value) >= 0;
  }

  /**
   * Linear search for the existence of a value in an array.
   *
   * @param array The array to search.
   * @param off The offset at which to start searching.
   * @param len The number of elements to search.
   * @param value The value to locate.
   * @return {@code true} if the value exists, {@code false} otherwise.
   * @throws NullPointerException If {@code array} is null.
   */
  public static boolean contains(final short[] array, final int off, final int len, final short value) {
    return indexOf(array, off, len, value) >= 0;
  }

  /**
   * Linear search for the existence of a value in an array.
   *
   * @param array The array to search.
   * @param value The value to locate.
   * @return {@code true} if the value exists, {@code false} otherwise.
   * @throws NullPointerException If {@code array} is null.
   */
  public static boolean contains(final int[] array, final int value) {
    return indexOf(array, value) >= 0;
  }

  /**
   * Linear search for the existence of a value in an array.
   *
   * @param array The array to search.
   * @param off The offset at which to start searching.
   * @param len The number of elements to search.
   * @param value The value to locate.
   * @return {@code true} if the value exists, {@code false} otherwise.
   * @throws NullPointerException If {@code array} is null.
   */
  public static boolean contains(final int[] array, final int off, final int len, final int value) {
    return indexOf(array, off, len, value) >= 0;
  }

  /**
   * Linear search for the existence of a value in an array.
   *
   * @param array The array to search.
   * @param value The value to locate.
   * @return {@code true} if the value exists, {@code false} otherwise.
   * @throws NullPointerException If {@code array} is null.
   */
  public static boolean contains(final long[] array, final long value) {
    return indexOf(array, value) >= 0;
  }

  /**
   * Linear search for the existence of a value in an array.
   *
   * @param array The array to search.
   * @param off The offset at which to start searching.
   * @param len The number of elements to search.
   * @param value The value to locate.
   * @return {@code true} if the value exists, {@code false} otherwise.
   * @throws NullPointerException If {@code array} is null.
   */
  public static boolean contains(final long[] array, final int off, final int len, final long value) {
    return indexOf(array, off, len, value) >= 0;
  }

  /**
   * Linear search for the existence of a value in an array.
   *
   * @param array The array to search.
   * @param value The value to locate.
   * @return {@code true} if the value exists, {@code false} otherwise.
   * @throws NullPointerException If {@code array} is null.
   */
  public static boolean contains(final float[] array, final float value) {
    return indexOf(array, value) >= 0;
  }

  /**
   * Linear search for the existence of a value in an array.
   *
   * @param array The array to search.
   * @param off The offset at which to start searching.
   * @param len The number of elements to search.
   * @param value The value to locate.
   * @return {@code true} if the value exists, {@code false} otherwise.
   * @throws NullPointerException If {@code array} is null.
   */
  public static boolean contains(final float[] array, final int off, final int len, final float value) {
    return indexOf(array, off, len, value) >= 0;
  }

  /**
   * Linear search for the existence of a value in an array.
   *
   * @param array The array to search.
   * @param value The value to locate.
   * @return {@code true} if the value exists, {@code false} otherwise.
   * @throws NullPointerException If {@code array} is null.
   */
  public static boolean contains(final double[] array, final double value) {
    return indexOf(array, value) >= 0;
  }

  /**
   * Linear search for the existence of a value in an array.
   *
   * @param array The array to search.
   * @param off The offset at which to start searching.
   * @param len The number of elements to search.
   * @param value The value to locate.
   * @return {@code true} if the value exists, {@code false} otherwise.
   * @throws NullPointerException If {@code array} is null.
   */
  public static boolean contains(final double[] array, final int off, final int len, final double value) {
    return indexOf(array, off, len, value) >= 0;
  }

  /**
   * Linear search for the existence of a value in an array.
   *
   * @param <T> Type parameter of {@code obj}.
   * @param array The array to search.
   * @param value The object to locate.
   * @return {@code true} if the value exists, {@code false} otherwise.
   * @throws NullPointerException If {@code array} is null.
   */
  public static <T>boolean contains(final T[] array, final T value) {
    return indexOf(array, value) >= 0;
  }

  /**
   * Linear search for the existence of a value in an array.
   *
   * @param <T> Type parameter of {@code obj}.
   * @param array The array to search.
   * @param off The offset at which to start searching.
   * @param len The number of elements to search.
   * @param value The object to locate.
   * @return {@code true} if the value exists, {@code false} otherwise.
   * @throws NullPointerException If {@code array} is null.
   */
  public static <T>boolean contains(final T[] array, final int off, final int len, final T value) {
    return indexOf(array, off, len, value) >= 0;
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final byte[] array, final char delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final byte[] array, final char delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final byte[] array, final char delimiter, final int offset, int length) {
    if (array == null)
      return "null";

    if (length == 0 || array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    length += offset;
    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length; ++i) // [A]
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final byte[] array, final String delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final byte[] array, final String delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final byte[] array, final String delimiter, final int offset, int length) {
    if (array == null)
      return "null";

    if (length == 0 || array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    length += offset;
    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length; ++i) // [A]
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final char[] array, final char delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final char[] array, final char delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final char[] array, final char delimiter, final int offset, int length) {
    if (array == null)
      return "null";

    if (length == 0 || array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    length += offset;
    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length; ++i) // [A]
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final char[] array, final String delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final char[] array, final String delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final char[] array, final String delimiter, final int offset, int length) {
    if (array == null)
      return "null";

    if (length == 0 || array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    length += offset;
    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length; ++i) // [A]
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final short[] array, final char delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final short[] array, final char delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final short[] array, final char delimiter, final int offset, int length) {
    if (array == null)
      return "null";

    if (length == 0 || array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    length += offset;
    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length; ++i) // [A]
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final short[] array, final String delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final short[] array, final String delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final short[] array, final String delimiter, final int offset, int length) {
    if (array == null)
      return "null";

    if (length == 0 || array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    length += offset;
    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length; ++i) // [A]
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final int[] array, final char delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final int[] array, final char delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final int[] array, final char delimiter, final int offset, int length) {
    if (array == null)
      return "null";

    if (length == 0 || array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    length += offset;
    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length; ++i) // [A]
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final int[] array, final String delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final int[] array, final String delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final int[] array, final String delimiter, final int offset, int length) {
    if (array == null)
      return "null";

    if (length == 0 || array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    length += offset;
    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length; ++i) // [A]
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final long[] array, final char delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final long[] array, final char delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final long[] array, final char delimiter, final int offset, int length) {
    if (array == null)
      return "null";

    if (array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    length += offset;
    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length; ++i) // [A]
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final long[] array, final String delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final long[] array, final String delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final long[] array, final String delimiter, final int offset, int length) {
    if (array == null)
      return "null";

    if (array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    length += offset;
    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length; ++i) // [A]
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final float[] array, final char delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final float[] array, final char delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final float[] array, final char delimiter, final int offset, int length) {
    if (array == null)
      return "null";

    if (array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    length += offset;
    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length; ++i) // [A]
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final float[] array, final String delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final float[] array, final String delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final float[] array, final String delimiter, final int offset, int length) {
    if (array == null)
      return "null";

    if (array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    length += offset;
    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length; ++i) // [A]
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final double[] array, final char delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final double[] array, final char delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final double[] array, final char delimiter, final int offset, int length) {
    if (array == null)
      return "null";

    if (array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    length += offset;
    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length; ++i) // [A]
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final double[] array, final String delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final double[] array, final String delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final double[] array, final String delimiter, final int offset, int length) {
    if (array == null)
      return "null";

    if (array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    length += offset;
    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length; ++i) // [A]
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final Object[] array, final char delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling the provided function on each member, delimited by the
   * provided delimiter {@code char}.
   *
   * @param <T> The component type of the specified array.
   * @param array The array.
   * @param delimiter The delimiter.
   * @param function The {@code toString} function.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   * @throws NullPointerException If the provided array is not null and the function is null.
   */
  public static <T>String toString(final T[] array, final char delimiter, final Function<? super T,String> function) {
    return array == null ? null : toString(array, delimiter, 0, array.length, function);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final Object[] array, final char delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling the provided function on each member, delimited by the
   * provided delimiter {@code char}.
   *
   * @param <T> The component type of the specified array.
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param function The {@code toString} function.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   * @throws NullPointerException If the provided array is not null and the function is null.
   */
  public static <T>String toString(final T[] array, final char delimiter, final int offset, final Function<? super T,String> function) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset, function);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter {@code char}.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final Object[] array, final char delimiter, final int offset, int length) {
    if (array == null)
      return "null";

    if (array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    length += offset;
    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length; ++i) // [A]
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling the provided function on each member, delimited by the
   * provided delimiter {@code char}.
   *
   * @param <T> The component type of the specified array.
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @param function The {@code toString} function.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   * @throws NullPointerException If the provided array is not null and the function is null.
   */
  public static <T>String toString(final T[] array, final char delimiter, final int offset, int length, final Function<? super T,String> function) {
    if (array == null)
      return "null";

    if (array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return function.apply(array[offset]);

    length += offset;
    final StringBuilder builder = new StringBuilder(function.apply(array[offset]));
    for (int i = offset + 1; i < length; ++i) // [A]
      builder.append(delimiter).append(function.apply(array[i]));

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final Object[] array, final String delimiter) {
    return array == null ? null : toString(array, delimiter, 0, array.length);
  }

  /**
   * Create a string representation of the specified array by calling the provided function on each member, delimited by the
   * provided delimiter {@code char}.
   *
   * @param <T> The component type of the specified array.
   * @param array The array.
   * @param delimiter The delimiter.
   * @param function The {@code toString} function.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   * @throws NullPointerException If the provided array is not null and the function is null.
   */
  public static <T>String toString(final T[] array, final String delimiter, final Function<? super T,String> function) {
    return array == null ? null : toString(array, delimiter, 0, array.length, function);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final Object[] array, final String delimiter, final int offset) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset);
  }

  /**
   * Create a string representation of the specified array by calling the provided function on each member, delimited by the
   * provided delimiter string.
   *
   * @param <T> The component type of the specified array.
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param function The {@code toString} function.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   * @throws NullPointerException If the provided array is not null and the function is null.
   */
  public static <T>String toString(final T[] array, final String delimiter, final int offset, final Function<? super T,String> function) {
    return array == null ? null : toString(array, delimiter, offset, array.length - offset, function);
  }

  /**
   * Create a string representation of the specified array by calling each member's {@link #toString()} method, delimited by the
   * provided delimiter string.
   *
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   */
  public static String toString(final Object[] array, final String delimiter, final int offset, int length) {
    if (array == null)
      return "null";

    if (array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return String.valueOf(array[offset]);

    length += offset;
    final StringBuilder builder = new StringBuilder(String.valueOf(array[offset]));
    for (int i = offset + 1; i < length; ++i) // [A]
      builder.append(delimiter).append(array[i]);

    return builder.toString();
  }

  /**
   * Create a string representation of the specified array by calling the provided function on each member, delimited by the
   * provided delimiter string.
   *
   * @param <T> The component type of the specified array.
   * @param array The array.
   * @param delimiter The delimiter.
   * @param offset The starting offset in the array.
   * @param length The number of array elements to be included.
   * @param function The {@code toString} function.
   * @return The delimiter delimited {@link #toString()} representation of the array, or {@code null} If the provided array is null.
   * @throws NullPointerException If the provided array is not null and the function is null.
   */
  public static <T>String toString(final T[] array, final String delimiter, final int offset, int length, final Function<? super T,String> function) {
    if (array == null)
      return "null";

    if (array.length <= offset)
      return "";

    if (array.length == offset + 1)
      return function.apply(array[offset]);

    length += offset;
    final StringBuilder builder = new StringBuilder(function.apply(array[offset]));
    for (int i = offset + 1; i < length; ++i) // [A]
      builder.append(delimiter).append(function.apply(array[i]));

    return builder.toString();
  }

  /**
   * Fill the provided array with a sequence of values starting with the provided {@code start} value.
   *
   * @param array The target array.
   * @param start The starting value.
   * @return The provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static byte[] fillSequence(final byte[] array, byte start) {
    for (int i = 0, i$ = array.length; i < i$; ++i) // [A]
      array[i] = start++;

    return array;
  }

  /**
   * Fill the provided array with a sequence of values starting with the provided {@code start} value.
   *
   * @param array The target array.
   * @param start The starting value.
   * @return The provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static Byte[] fillSequence(final Byte[] array, byte start) {
    for (int i = 0, i$ = array.length; i < i$; ++i) // [A]
      array[i] = start++;

    return array;
  }

  /**
   * Fill the provided array with a sequence of values starting with the provided {@code start} value.
   *
   * @param array The target array.
   * @param start The starting value.
   * @return The provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static char[] fillSequence(final char[] array, char start) {
    for (int i = 0, i$ = array.length; i < i$; ++i) // [A]
      array[i] = start++;

    return array;
  }

  /**
   * Fill the provided array with a sequence of values starting with the provided {@code start} value.
   *
   * @param array The target array.
   * @param start The starting value.
   * @return The provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static Character[] fillSequence(final Character[] array, char start) {
    for (int i = 0, i$ = array.length; i < i$; ++i) // [A]
      array[i] = start++;

    return array;
  }

  /**
   * Fill the provided array with a sequence of values starting with the provided {@code start} value.
   *
   * @param array The target array.
   * @param start The starting value.
   * @return The provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static short[] fillSequence(final short[] array, short start) {
    for (int i = 0, i$ = array.length; i < i$; ++i) // [A]
      array[i] = start++;

    return array;
  }

  /**
   * Fill the provided array with a sequence of values starting with the provided {@code start} value.
   *
   * @param array The target array.
   * @param start The starting value.
   * @return The provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static Short[] fillSequence(final Short[] array, short start) {
    for (int i = 0, i$ = array.length; i < i$; ++i) // [A]
      array[i] = start++;

    return array;
  }

  /**
   * Fill the provided array with a sequence of values starting with the provided {@code start} value.
   *
   * @param array The target array.
   * @param start The starting value.
   * @return The provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static int[] fillSequence(final int[] array, int start) {
    for (int i = 0, i$ = array.length; i < i$; ++i) // [A]
      array[i] = start++;

    return array;
  }

  /**
   * Fill the provided array with a sequence of values starting with the provided {@code start} value.
   *
   * @param array The target array.
   * @param start The starting value.
   * @return The provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static Integer[] fillSequence(final Integer[] array, int start) {
    for (int i = 0, i$ = array.length; i < i$; ++i) // [A]
      array[i] = start++;

    return array;
  }

  /**
   * Fill the provided array with a sequence of values starting with the provided {@code start} value.
   *
   * @param array The target array.
   * @param start The starting value.
   * @return The provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static long[] fillSequence(final long[] array, long start) {
    for (int i = 0, i$ = array.length; i < i$; ++i) // [A]
      array[i] = start++;

    return array;
  }

  /**
   * Fill the provided array with a sequence of values starting with the provided {@code start} value.
   *
   * @param array The target array.
   * @param start The starting value.
   * @return The provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static Long[] fillSequence(final Long[] array, long start) {
    for (int i = 0, i$ = array.length; i < i$; ++i) // [A]
      array[i] = start++;

    return array;
  }

  /**
   * Fill the provided array with a sequence of values starting with the provided {@code start} value.
   *
   * @param array The target array.
   * @param start The starting value.
   * @return The provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static float[] fillSequence(final float[] array, float start) {
    for (int i = 0, i$ = array.length; i < i$; ++i) // [A]
      array[i] = start++;

    return array;
  }

  /**
   * Fill the provided array with a sequence of values starting with the provided {@code start} value.
   *
   * @param array The target array.
   * @param start The starting value.
   * @return The provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static Float[] fillSequence(final Float[] array, float start) {
    for (int i = 0, i$ = array.length; i < i$; ++i) // [A]
      array[i] = start++;

    return array;
  }

  /**
   * Fill the provided array with a sequence of values starting with the provided {@code start} value.
   *
   * @param array The target array.
   * @param start The starting value.
   * @return The provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static double[] fillSequence(final double[] array, double start) {
    for (int i = 0, i$ = array.length; i < i$; ++i) // [A]
      array[i] = start++;

    return array;
  }

  /**
   * Fill the provided array with a sequence of values starting with the provided {@code start} value.
   *
   * @param array The target array.
   * @param start The starting value.
   * @return The provided array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static Double[] fillSequence(final Double[] array, double start) {
    for (int i = 0, i$ = array.length; i < i$; ++i) // [A]
      array[i] = start++;

    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value} members.
   */
  public static boolean[] createRepeat(final boolean value, final int length) {
    final boolean[] array = new boolean[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value} members.
   */
  public static Boolean[] createRepeat(final Boolean value, final int length) {
    final Boolean[] array = new Boolean[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value} members.
   */
  public static byte[] createRepeat(final byte value, final int length) {
    final byte[] array = new byte[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value} members.
   */
  public static Byte[] createRepeat(final Byte value, final int length) {
    final Byte[] array = new Byte[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value} members.
   */
  public static char[] createRepeat(final char value, final int length) {
    final char[] array = new char[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value} members.
   */
  public static Character[] createRepeat(final Character value, final int length) {
    final Character[] array = new Character[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value} members.
   */
  public static short[] createRepeat(final short value, final int length) {
    final short[] array = new short[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value} members.
   */
  public static Short[] createRepeat(final Short value, final int length) {
    final Short[] array = new Short[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value} members.
   */
  public static int[] createRepeat(final int value, final int length) {
    final int[] array = new int[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value} members.
   */
  public static Integer[] createRepeat(final Integer value, final int length) {
    final Integer[] array = new Integer[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value} members.
   */
  public static long[] createRepeat(final long value, final int length) {
    final long[] array = new long[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value} members.
   */
  public static Long[] createRepeat(final Long value, final int length) {
    final Long[] array = new Long[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value} members.
   */
  public static float[] createRepeat(final float value, final int length) {
    final float[] array = new float[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value} members.
   */
  public static Float[] createRepeat(final Float value, final int length) {
    final Float[] array = new Float[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value} members.
   */
  public static double[] createRepeat(final double value, final int length) {
    final double[] array = new double[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided {@code length} number of times.
   *
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value} members.
   */
  public static Double[] createRepeat(final Double value, final int length) {
    final Double[] array = new Double[length];
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Create a new array by repeating the provided {@code value} by the provided {@code length} number of times.
   *
   * @param <T> Type parameter of object.
   * @param value The value to repeat.
   * @param length The number of times to repeat the value.
   * @return A new array with {@code length} number of repeated {@code value} members.
   */
  @SuppressWarnings("unchecked")
  public static <T>T[] createRepeat(final T value, final int length) {
    final T[] array = (T[])Array.newInstance(value.getClass(), length);
    Arrays.fill(array, value);
    return array;
  }

  /**
   * Returns an array that is the subArray of the provided array. Calling this method is the equivalent of
   * {@link #subArray(byte[],int,int) Arrays.subArray(array, offset, array.length)}.
   *
   * @param array The provided {@code array}.
   * @param offset Starting offset in the array.
   * @return The subArray of the provided {@code array}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static byte[] subArray(final byte[] array, final int offset) {
    return subArray(array, offset, Math.max(0, array.length - offset));
  }

  /**
   * Returns an array that is the subArray of the provided array.
   *
   * @param array The provided {@code array}.
   * @param offset Starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The subArray of the provided {@code array}.
   * @throws NullPointerException If {@code array} is null.
   * @throws ArrayIndexOutOfBoundsException If {@code length} is negative.
   */
  public static byte[] subArray(final byte[] array, final int offset, final int length) {
    assertOffsetLength("offset", offset, "length", length);
    final byte[] subArray = new byte[length];
    if (length == 0)
      return subArray;

    System.arraycopy(array, offset, subArray, 0, length);
    return subArray;
  }

  /**
   * Returns an array that is the subArray of the provided array. Calling this method is the equivalent of
   * {@link #subArray(short[],int,int) Arrays.subArray(array, offset, array.length)}.
   *
   * @param array The provided {@code array}.
   * @param offset Starting offset in the array.
   * @return The subArray of the provided {@code array}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static short[] subArray(final short[] array, final int offset) {
    return subArray(array, offset, Math.max(0, array.length - offset));
  }

  /**
   * Returns an array that is the subArray of the provided array.
   *
   * @param array The provided {@code array}.
   * @param offset Starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The subArray of the provided {@code array}.
   * @throws NullPointerException If {@code array} is null.
   * @throws ArrayIndexOutOfBoundsException If {@code length} is negative.
   */
  public static short[] subArray(final short[] array, final int offset, final int length) {
    assertOffsetLength("offset", offset, "length", length);
    final short[] subArray = new short[length];
    if (length == 0)
      return subArray;

    System.arraycopy(array, offset, subArray, 0, length);
    return subArray;
  }

  /**
   * Returns an array that is the subArray of the provided array. Calling this method is the equivalent of
   * {@link #subArray(int[],int,int) Arrays.subArray(array, offset, array.length)}.
   *
   * @param array The provided {@code array}.
   * @param offset Starting offset in the array.
   * @return The subArray of the provided {@code array}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static int[] subArray(final int[] array, final int offset) {
    return subArray(array, offset, Math.max(0, array.length - offset));
  }

  /**
   * Returns an array that is the subArray of the provided array.
   *
   * @param array The provided {@code array}.
   * @param offset Starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The subArray of the provided {@code array}.
   * @throws NullPointerException If {@code array} is null.
   * @throws ArrayIndexOutOfBoundsException If {@code length} is negative.
   */
  public static int[] subArray(final int[] array, final int offset, final int length) {
    assertOffsetLength("offset", offset, "length", length);
    final int[] subArray = new int[length];
    if (length == 0)
      return subArray;

    System.arraycopy(array, offset, subArray, 0, length);
    return subArray;
  }

  /**
   * Returns an array that is the subArray of the provided array. Calling this method is the equivalent of
   * {@link #subArray(long[],int,int) Arrays.subArray(array, offset, array.length)}.
   *
   * @param array The provided {@code array}.
   * @param offset Starting offset in the array.
   * @return The subArray of the provided {@code array}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static long[] subArray(final long[] array, final int offset) {
    return subArray(array, offset, Math.max(0, array.length - offset));
  }

  /**
   * Returns an array that is the subArray of the provided array.
   *
   * @param array The provided {@code array}.
   * @param offset Starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The subArray of the provided {@code array}.
   * @throws NullPointerException If {@code array} is null.
   * @throws ArrayIndexOutOfBoundsException If {@code length} is negative.
   */
  public static long[] subArray(final long[] array, final int offset, final int length) {
    assertOffsetLength("offset", offset, "length", length);
    final long[] subArray = new long[length];
    if (length == 0)
      return subArray;

    System.arraycopy(array, offset, subArray, 0, length);
    return subArray;
  }

  /**
   * Returns an array that is the subArray of the provided array. Calling this method is the equivalent of
   * {@link #subArray(float[],int,int) Arrays.subArray(array, offset, array.length)}.
   *
   * @param array The provided {@code array}.
   * @param offset Starting offset in the array.
   * @return The subArray of the provided {@code array}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static float[] subArray(final float[] array, final int offset) {
    return subArray(array, offset, Math.max(0, array.length - offset));
  }

  /**
   * Returns an array that is the subArray of the provided array.
   *
   * @param array The provided {@code array}.
   * @param offset Starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The subArray of the provided {@code array}.
   * @throws NullPointerException If {@code array} is null.
   * @throws ArrayIndexOutOfBoundsException If {@code length} is negative.
   */
  public static float[] subArray(final float[] array, final int offset, final int length) {
    assertOffsetLength("offset", offset, "length", length);
    final float[] subArray = new float[length];
    if (length == 0)
      return subArray;

    System.arraycopy(array, offset, subArray, 0, length);
    return subArray;
  }

  /**
   * Returns an array that is the subArray of the provided array. Calling this method is the equivalent of
   * {@link #subArray(double[],int,int) Arrays.subArray(array, offset, array.length)}.
   *
   * @param array The provided {@code array}.
   * @param offset Starting offset in the array.
   * @return The subArray of the provided {@code array}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static double[] subArray(final double[] array, final int offset) {
    return subArray(array, offset, Math.max(0, array.length - offset));
  }

  /**
   * Returns an array that is the subArray of the provided array.
   *
   * @param array The provided {@code array}.
   * @param offset Starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The subArray of the provided {@code array}.
   * @throws NullPointerException If {@code array} is null.
   * @throws ArrayIndexOutOfBoundsException If {@code length} is negative.
   */
  public static double[] subArray(final double[] array, final int offset, final int length) {
    assertOffsetLength("offset", offset, "length", length);
    final double[] subArray = new double[length];
    if (length == 0)
      return subArray;

    System.arraycopy(array, offset, subArray, 0, length);
    return subArray;
  }

  /**
   * Returns an array that is the subArray of the provided array. Calling this method is the equivalent of
   * {@link #subArray(boolean[],int,int) Arrays.subArray(array, offset, array.length)}.
   *
   * @param array The provided {@code array}.
   * @param offset Starting offset in the array.
   * @return The subArray of the provided {@code array}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static boolean[] subArray(final boolean[] array, final int offset) {
    return subArray(array, offset, Math.max(0, array.length - offset));
  }

  /**
   * Returns an array that is the subArray of the provided array.
   *
   * @param array The provided {@code array}.
   * @param offset Starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The subArray of the provided {@code array}.
   * @throws NullPointerException If {@code array} is null.
   * @throws ArrayIndexOutOfBoundsException If {@code length} is negative.
   */
  public static boolean[] subArray(final boolean[] array, final int offset, final int length) {
    assertOffsetLength("offset", offset, "length", length);
    final boolean[] subArray = new boolean[length];
    if (length == 0)
      return subArray;

    System.arraycopy(array, offset, subArray, 0, length);
    return subArray;
  }

  /**
   * Returns an array that is the subArray of the provided array. Calling this method is the equivalent of
   * {@link #subArray(char[],int,int) Arrays.subArray(array, offset, array.length)}.
   *
   * @param array The provided {@code array}.
   * @param offset Starting offset in the array.
   * @return The subArray of the provided {@code array}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static char[] subArray(final char[] array, final int offset) {
    return subArray(array, offset, Math.max(0, array.length - offset));
  }

  /**
   * Returns an array that is the subArray of the provided array.
   *
   * @param array The provided {@code array}.
   * @param offset Starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The subArray of the provided {@code array}.
   * @throws NullPointerException If {@code array} is null.
   * @throws ArrayIndexOutOfBoundsException If {@code length} is negative.
   */
  public static char[] subArray(final char[] array, final int offset, final int length) {
    assertOffsetLength("offset", offset, "length", length);
    final char[] subArray = new char[length];
    if (length == 0)
      return subArray;

    System.arraycopy(array, offset, subArray, 0, length);
    return subArray;
  }

  /**
   * Returns an array that is the subArray of the provided array. Calling this method is the equivalent of
   * {@link #subArray(Object[],int,int) Arrays.subArray(array, offset, array.length)}.
   *
   * @param <T> Type parameter of object.
   * @param array The provided {@code array}.
   * @param offset Starting offset in the array.
   * @return The subArray of the provided {@code array}.
   * @throws NullPointerException If {@code array} is null.
   */
  public static <T>T[] subArray(final T[] array, final int offset) {
    return subArray(array, offset, Math.max(0, array.length - offset));
  }

  /**
   * Returns an array that is the subArray of the provided array.
   *
   * @param <T> Type parameter of object.
   * @param array The provided {@code array}.
   * @param offset Starting offset in the array.
   * @param length The number of array elements to be included.
   * @return The subArray of the provided {@code array}.
   * @throws NullPointerException If {@code array} is null.
   * @throws ArrayIndexOutOfBoundsException If {@code length} is negative.
   */
  @SuppressWarnings("unchecked")
  public static <T>T[] subArray(final T[] array, final int offset, final int length) {
    assertOffsetLength("offset", offset, "length", length);
    final Class<?> componentType = array.getClass().getComponentType();
    final T[] subArray = (T[])Array.newInstance(componentType, length);
    if (length == 0)
      return subArray;

    System.arraycopy(array, offset, subArray, 0, length);
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
   * Randomly permutes the specified array using a default source of randomness. All permutations occur with approximately equal
   * likelihood.
   * <p>
   * The hedge "approximately" is used in the foregoing description because default source of randomness is only approximately an
   * unbiased source of independently chosen bits. If it were a perfect source of randomly chosen bits, then the algorithm would
   * choose permutations with perfect uniformity.
   * <p>
   * This implementation traverses the array backwards, from the last element up to the second, repeatedly swapping a randomly
   * selected element into the "current position". Elements are randomly selected from the portion of the array that runs from the
   * first element to the current position, inclusive.
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
   * Randomly permutes the specified array using a default source of randomness. All permutations occur with approximately equal
   * likelihood.
   * <p>
   * The hedge "approximately" is used in the foregoing description because default source of randomness is only approximately an
   * unbiased source of independently chosen bits. If it were a perfect source of randomly chosen bits, then the algorithm would
   * choose permutations with perfect uniformity.
   * <p>
   * This implementation traverses the array backwards, from the last element up to the second, repeatedly swapping a randomly
   * selected element into the "current position". Elements are randomly selected from the portion of the array that runs from the
   * first element to the current position, inclusive.
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
   * Randomly permutes the specified array using a default source of randomness. All permutations occur with approximately equal
   * likelihood.
   * <p>
   * The hedge "approximately" is used in the foregoing description because default source of randomness is only approximately an
   * unbiased source of independently chosen bits. If it were a perfect source of randomly chosen bits, then the algorithm would
   * choose permutations with perfect uniformity.
   * <p>
   * This implementation traverses the array backwards, from the last element up to the second, repeatedly swapping a randomly
   * selected element into the "current position". Elements are randomly selected from the portion of the array that runs from the
   * first element to the current position, inclusive.
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
   * Randomly permutes the specified array using a default source of randomness. All permutations occur with approximately equal
   * likelihood.
   * <p>
   * The hedge "approximately" is used in the foregoing description because default source of randomness is only approximately an
   * unbiased source of independently chosen bits. If it were a perfect source of randomly chosen bits, then the algorithm would
   * choose permutations with perfect uniformity.
   * <p>
   * This implementation traverses the array backwards, from the last element up to the second, repeatedly swapping a randomly
   * selected element into the "current position". Elements are randomly selected from the portion of the array that runs from the
   * first element to the current position, inclusive.
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
   * Randomly permutes the specified array using a default source of randomness. All permutations occur with approximately equal
   * likelihood.
   * <p>
   * The hedge "approximately" is used in the foregoing description because default source of randomness is only approximately an
   * unbiased source of independently chosen bits. If it were a perfect source of randomly chosen bits, then the algorithm would
   * choose permutations with perfect uniformity.
   * <p>
   * This implementation traverses the array backwards, from the last element up to the second, repeatedly swapping a randomly
   * selected element into the "current position". Elements are randomly selected from the portion of the array that runs from the
   * first element to the current position, inclusive.
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
   * Randomly permutes the specified array using a default source of randomness. All permutations occur with approximately equal
   * likelihood.
   * <p>
   * The hedge "approximately" is used in the foregoing description because default source of randomness is only approximately an
   * unbiased source of independently chosen bits. If it were a perfect source of randomly chosen bits, then the algorithm would
   * choose permutations with perfect uniformity.
   * <p>
   * This implementation traverses the array backwards, from the last element up to the second, repeatedly swapping a randomly
   * selected element into the "current position". Elements are randomly selected from the portion of the array that runs from the
   * first element to the current position, inclusive.
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
   * Randomly permutes the specified array using a default source of randomness. All permutations occur with approximately equal
   * likelihood.
   * <p>
   * The hedge "approximately" is used in the foregoing description because default source of randomness is only approximately an
   * unbiased source of independently chosen bits. If it were a perfect source of randomly chosen bits, then the algorithm would
   * choose permutations with perfect uniformity.
   * <p>
   * This implementation traverses the array backwards, from the last element up to the second, repeatedly swapping a randomly
   * selected element into the "current position". Elements are randomly selected from the portion of the array that runs from the
   * first element to the current position, inclusive.
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
   * Randomly permutes the specified array using a default source of randomness. All permutations occur with approximately equal
   * likelihood.
   * <p>
   * The hedge "approximately" is used in the foregoing description because default source of randomness is only approximately an
   * unbiased source of independently chosen bits. If it were a perfect source of randomly chosen bits, then the algorithm would
   * choose permutations with perfect uniformity.
   * <p>
   * This implementation traverses the array backwards, from the last element up to the second, repeatedly swapping a randomly
   * selected element into the "current position". Elements are randomly selected from the portion of the array that runs from the
   * first element to the current position, inclusive.
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
   * Randomly permute the specified array using the specified source of randomness. All permutations occur with equal likelihood
   * assuming that the source of randomness is fair.
   * <p>
   * This implementation traverses the array backwards, from the last element up to the second, repeatedly swapping a randomly
   * selected element into the "current position". Elements are randomly selected from the portion of the array that runs from the
   * first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The array to be shuffled.
   * @param random The source of randomness to use to shuffle the array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final boolean[] array, final Random random) {
    for (int i = array.length; i > 1; --i) // [A]
      swap(array, i - 1, random.nextInt(i));
  }

  /**
   * Randomly permute the specified array using the provided source of randomness. All permutations occur with equal likelihood
   * assuming that the source of randomness is fair.
   * <p>
   * This implementation traverses the array backwards, from the last element up to the second, repeatedly swapping a randomly
   * selected element into the "current position". Elements are randomly selected from the portion of the array that runs from the
   * first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array the array to be shuffled.
   * @param random The source of randomness to use to shuffle the array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final byte[] array, final Random random) {
    for (int i = array.length; i > 1; --i) // [A]
      swap(array, i - 1, random.nextInt(i));
  }

  /**
   * Randomly permute the specified array using the provided source of randomness. All permutations occur with equal likelihood
   * assuming that the source of randomness is fair.
   * <p>
   * This implementation traverses the array backwards, from the last element up to the second, repeatedly swapping a randomly
   * selected element into the "current position". Elements are randomly selected from the portion of the array that runs from the
   * first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The array to be shuffled.
   * @param random The source of randomness to use to shuffle the array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final char[] array, final Random random) {
    for (int i = array.length; i > 1; --i) // [A]
      swap(array, i - 1, random.nextInt(i));
  }

  /**
   * Randomly permute the specified array using the provided source of randomness. All permutations occur with equal likelihood
   * assuming that the source of randomness is fair.
   * <p>
   * This implementation traverses the array backwards, from the last element up to the second, repeatedly swapping a randomly
   * selected element into the "current position". Elements are randomly selected from the portion of the array that runs from the
   * first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The array to be shuffled.
   * @param random The source of randomness to use to shuffle the array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final short[] array, final Random random) {
    for (int i = array.length; i > 1; --i) // [A]
      swap(array, i - 1, random.nextInt(i));
  }

  /**
   * Randomly permute the specified array using the provided source of randomness. All permutations occur with equal likelihood
   * assuming that the source of randomness is fair.
   * <p>
   * This implementation traverses the array backwards, from the last element up to the second, repeatedly swapping a randomly
   * selected element into the "current position". Elements are randomly selected from the portion of the array that runs from the
   * first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The array to be shuffled.
   * @param random The source of randomness to use to shuffle the array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final int[] array, final Random random) {
    for (int i = array.length; i > 1; --i) // [A]
      swap(array, i - 1, random.nextInt(i));
  }

  /**
   * Randomly permute the specified array using the provided source of randomness. All permutations occur with equal likelihood
   * assuming that the source of randomness is fair.
   * <p>
   * This implementation traverses the array backwards, from the last element up to the second, repeatedly swapping a randomly
   * selected element into the "current position". Elements are randomly selected from the portion of the array that runs from the
   * first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The array to be shuffled.
   * @param random The source of randomness to use to shuffle the array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final long[] array, final Random random) {
    for (int i = array.length; i > 1; --i) // [A]
      swap(array, i - 1, random.nextInt(i));
  }

  /**
   * Randomly permute the specified array using the provided source of randomness. All permutations occur with equal likelihood
   * assuming that the source of randomness is fair.
   * <p>
   * This implementation traverses the array backwards, from the last element up to the second, repeatedly swapping a randomly
   * selected element into the "current position". Elements are randomly selected from the portion of the array that runs from the
   * first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The array to be shuffled.
   * @param random The source of randomness to use to shuffle the array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final float[] array, final Random random) {
    for (int i = array.length; i > 1; --i) // [A]
      swap(array, i - 1, random.nextInt(i));
  }

  /**
   * Randomly permute the specified array using the provided source of randomness. All permutations occur with equal likelihood
   * assuming that the source of randomness is fair.
   * <p>
   * This implementation traverses the array backwards, from the last element up to the second, repeatedly swapping a randomly
   * selected element into the "current position". Elements are randomly selected from the portion of the array that runs from the
   * first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The array to be shuffled.
   * @param random The source of randomness to use to shuffle the array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final double[] array, final Random random) {
    for (int i = array.length; i > 1; --i) // [A]
      swap(array, i - 1, random.nextInt(i));
  }

  /**
   * Randomly permute the specified array using the provided source of randomness. All permutations occur with equal likelihood
   * assuming that the source of randomness is fair.
   * <p>
   * This implementation traverses the array backwards, from the last element up to the second, repeatedly swapping a randomly
   * selected element into the "current position". Elements are randomly selected from the portion of the array that runs from the
   * first element to the current position, inclusive.
   * <p>
   * This method runs in linear time.
   *
   * @param array The array to be shuffled.
   * @param random The source of randomness to use to shuffle the array.
   * @throws NullPointerException If {@code array} is null.
   */
  public static void shuffle(final Object[] array, final Random random) {
    for (int i = array.length; i > 1; --i) // [A]
      swap(array, i - 1, random.nextInt(i));
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the array in the second argument.
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
   * Sorts the array in the first argument matching the sorted order of the array in the second argument.
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
   * @throws NullPointerException If {@code data}, {@code order} or {@code comparator} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final byte[] order, final ByteComparator comparator) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    PrimitiveSort.sortPaired(data, order, 0, order.length, comparator);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the array in the second argument.
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
   * Sorts the array in the first argument matching the sorted order of the array in the second argument.
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
   * @throws NullPointerException If {@code data}, {@code order} or {@code comparator} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final char[] order, final CharComparator comparator) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    PrimitiveSort.sortPaired(data, order, 0, order.length, comparator);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the array in the second argument.
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
   * Sorts the array in the first argument matching the sorted order of the array in the second argument.
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
   * @throws NullPointerException If {@code data}, {@code order} or {@code comparator} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final short[] order, final ShortComparator comparator) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    PrimitiveSort.sortPaired(data, order, 0, order.length, comparator);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the array in the second argument.
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
   * Sorts the array in the first argument matching the sorted order of the array in the second argument.
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
   * @throws NullPointerException If {@code data}, {@code order} or {@code comparator} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final int[] order, final IntComparator comparator) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    PrimitiveSort.sortPaired(data, order, 0, order.length, comparator);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the array in the second argument.
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
   * Sorts the array in the first argument matching the sorted order of the array in the second argument.
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
   * @throws NullPointerException If {@code data}, {@code order} or {@code comparator} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final long[] order, final LongComparator comparator) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    PrimitiveSort.sortPaired(data, order, 0, order.length, comparator);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the array in the second argument.
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
   * Sorts the array in the first argument matching the sorted order of the array in the second argument.
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
   * @throws NullPointerException If {@code data}, {@code order} or {@code comparator} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final float[] order, final FloatComparator comparator) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    PrimitiveSort.sortPaired(data, order, 0, order.length, comparator);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the array in the second argument.
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
   * Sorts the array in the first argument matching the sorted order of the array in the second argument.
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
   * @throws NullPointerException If {@code data}, {@code order} or {@code comparator} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static void sort(final Object[] data, final double[] order, final DoubleComparator comparator) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    PrimitiveSort.sortPaired(data, order, 0, order.length, comparator);
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the {@link List} of {@link Comparable} objects in the second
   * argument.
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
   * @param <T> The type parameter for the {@link Comparable} objects of {@code order}.
   * @param data The array providing the data.
   * @param order The {@link List} of {@link Comparable} objects providing the order of indices to sort {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static <T extends Comparable<? super T>>void sort(final Object[] data, final T[] order) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    final int[] idx = PrimitiveSort.buildIndex(order.length);
    PrimitiveSort.sortIndexed(data, order, idx, (o1, o2) -> {
      final T c1 = order[o1];
      final T c2 = order[o2];
      return c1 == null ? c2 == null ? 0 : -1 : c2 == null ? 1 : c1.compareTo(c2);
    });
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the {@link List} in the second argument.
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
   * @param <T> The type parameter for the {@link Comparable} objects of {@code order}.
   * @param data The array providing the data.
   * @param order The {@link List} providing the order of indices to sort {@code data}.
   * @param comparator The {@link Comparator} for members of {@code order}.
   * @throws NullPointerException If {@code data}, {@code order} or {@code comparator} is null.
   * @throws IllegalArgumentException If {@code data.length != order.length}.
   */
  public static <T>void sort(final Object[] data, final T[] order, final Comparator<? super T> comparator) {
    if (data.length != order.length)
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.length [" + order.length + "] must be equal");

    final int[] idx = PrimitiveSort.buildIndex(order.length);
    PrimitiveSort.sortIndexed(data, order, idx, (o1, o2) -> comparator.compare(order[o1], order[o2]));
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the {@link List} of {@link Comparable} objects in the second
   * argument.
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
   * @param <T> The type parameter for the {@link Comparable} objects of {@code order}.
   * @param data The array providing the data.
   * @param order The {@link List} of {@link Comparable} objects providing the order of indices to sort {@code data}.
   * @throws NullPointerException If {@code data} or {@code order} is null.
   * @throws IllegalArgumentException If {@code data.length != order.size()}.
   */
  public static <T extends Comparable<? super T>>void sort(final Object[] data, final List<T> order) {
    if (data.length != order.size())
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.size() [" + order.size() + "] must be equal");

    final int[] idx = PrimitiveSort.buildIndex(order.size());
    PrimitiveSort.sortIndexed(data, order, idx, (o1, o2) -> {
      final T c1 = order.get(o1);
      final T c2 = order.get(o2);
      return c1 == null ? c2 == null ? 0 : -1 : c2 == null ? 1 : c1.compareTo(c2);
    });
  }

  /**
   * Sorts the array in the first argument matching the sorted order of the {@link List} in the second argument.
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
   * @param <T> The type parameter for the {@link Comparable} objects of {@code order}.
   * @param data The array providing the data.
   * @param order The {@link List} providing the order of indices to sort {@code data}.
   * @param comparator The {@link Comparator} for members of {@code order}.
   * @throws NullPointerException If {@code data}, {@code order} or {@code comparator} is null.
   * @throws IllegalArgumentException If {@code data.length != order.size()}.
   */
  public static <T>void sort(final Object[] data, final List<? extends T> order, final Comparator<? super T> comparator) {
    if (data.length != order.size())
      throw new IllegalArgumentException("data.length [" + data.length + "] and order.size() [" + order.size() + "] must be equal");

    final int[] idx = PrimitiveSort.buildIndex(order.size());
    PrimitiveSort.sortIndexed(data, order, idx, (o1, o2) -> comparator.compare(order.get(o1), order.get(o2)));
  }

  /**
   * Sorts the specified array of {@code byte}s, according to the specified {@link ByteComparator}.
   *
   * @param a The array of {@code byte}s.
   * @param c The {@link ByteComparator}.
   * @throws IllegalArgumentException If {@code a} is null.
   */
  public static void sort(final byte[] a, final ByteComparator c) {
    sort(a, 0, a.length, c);
  }

  /**
   * Sorts the specified array of {@code byte}s, according to the specified {@link ByteComparator}.
   *
   * @param a The array of {@code byte}s.
   * @param fromIndex The index of the first element, inclusive, to be sorted.
   * @param toIndex The index of the last element, exclusive, to be sorted.
   * @param c The {@link ByteComparator}.
   * @throws IllegalArgumentException If {@code a} is null.
   */
  public static void sort(final byte[] a, final int fromIndex, final int toIndex, final ByteComparator c) {
    if (c != null)
      PrimitiveSort.sort(a, fromIndex, toIndex, c);
    else
      Arrays.sort(a, fromIndex, toIndex);
  }

  /**
   * Sorts the specified array of {@code char}s, according to the specified {@link CharComparator}.
   *
   * @param a The array of {@code char}s.
   * @param c The {@link CharComparator}.
   * @throws IllegalArgumentException If {@code a} is null.
   */
  public static void sort(final char[] a, final CharComparator c) {
    sort(a, 0, a.length, c);
  }

  /**
   * Sorts the specified array of {@code char}s, according to the specified {@link CharComparator}.
   *
   * @param a The array of {@code char}s.
   * @param fromIndex The index of the first element, inclusive, to be sorted.
   * @param toIndex The index of the last element, exclusive, to be sorted.
   * @param c The {@link CharComparator}.
   * @throws IllegalArgumentException If {@code a} is null.
   */
  public static void sort(final char[] a, final int fromIndex, final int toIndex, final CharComparator c) {
    if (c != null)
      PrimitiveSort.sort(a, fromIndex, toIndex, c);
    else
      Arrays.sort(a, fromIndex, toIndex);
  }

  /**
   * Sorts the specified array of {@code short}s, according to the specified {@link ShortComparator}.
   *
   * @param a The array of {@code short}s.
   * @param c The {@link ShortComparator}.
   * @throws IllegalArgumentException If {@code a} is null.
   */
  public static void sort(final short[] a, final ShortComparator c) {
    sort(a, 0, a.length, c);
  }

  /**
   * Sorts the specified array of {@code short}s, according to the specified {@link ShortComparator}.
   *
   * @param a The array of {@code short}s.
   * @param fromIndex The index of the first element, inclusive, to be sorted.
   * @param toIndex The index of the last element, exclusive, to be sorted.
   * @param c The {@link ShortComparator}.
   * @throws IllegalArgumentException If {@code a} is null.
   */
  public static void sort(final short[] a, final int fromIndex, final int toIndex, final ShortComparator c) {
    if (c != null)
      PrimitiveSort.sort(a, fromIndex, toIndex, c);
    else
      Arrays.sort(a, fromIndex, toIndex);
  }

  /**
   * Sorts the specified array of {@code int}s, according to the specified {@link IntComparator}.
   *
   * @param a The array of {@code int}s.
   * @param c The {@link IntComparator}.
   * @throws IllegalArgumentException If {@code a} is null.
   */
  public static void sort(final int[] a, final IntComparator c) {
    sort(a, 0, a.length, c);
  }

  /**
   * Sorts the specified array of {@code int}s, according to the specified {@link IntComparator}.
   *
   * @param a The array of {@code int}s.
   * @param fromIndex The index of the first element, inclusive, to be sorted.
   * @param toIndex The index of the last element, exclusive, to be sorted.
   * @param c The {@link IntComparator}.
   * @throws IllegalArgumentException If {@code a} is null.
   */
  public static void sort(final int[] a, final int fromIndex, final int toIndex, final IntComparator c) {
    if (c != null)
      PrimitiveSort.sort(a, fromIndex, toIndex, c);
    else
      Arrays.sort(a, fromIndex, toIndex);
  }

  /**
   * Sorts the specified array of {@code long}s, according to the specified {@link LongComparator}.
   *
   * @param a The array of {@code long}s.
   * @param c The {@link LongComparator}.
   * @throws IllegalArgumentException If {@code a} is null.
   */
  public static void sort(final long[] a, final LongComparator c) {
    sort(a, 0, a.length, c);
  }

  /**
   * Sorts the specified array of {@code long}s, according to the specified {@link LongComparator}.
   *
   * @param a The array of {@code long}s.
   * @param fromIndex The index of the first element, inclusive, to be sorted.
   * @param toIndex The index of the last element, exclusive, to be sorted.
   * @param c The {@link LongComparator}.
   * @throws IllegalArgumentException If {@code a} is null.
   */
  public static void sort(final long[] a, final int fromIndex, final int toIndex, final LongComparator c) {
    if (c != null)
      PrimitiveSort.sort(a, fromIndex, toIndex, c);
    else
      Arrays.sort(a, fromIndex, toIndex);
  }

  /**
   * Sorts the specified array of {@code float}s, according to the specified {@link FloatComparator}.
   *
   * @param a The array of {@code float}s.
   * @param c The {@link FloatComparator}.
   * @throws IllegalArgumentException If {@code a} is null.
   */
  public static void sort(final float[] a, final FloatComparator c) {
    sort(a, 0, a.length, c);
  }

  /**
   * Sorts the specified array of {@code float}s, according to the specified {@link FloatComparator}.
   *
   * @param a The array of {@code float}s.
   * @param fromIndex The index of the first element, inclusive, to be sorted.
   * @param toIndex The index of the last element, exclusive, to be sorted.
   * @param c The {@link FloatComparator}.
   * @throws IllegalArgumentException If {@code a} is null.
   */
  public static void sort(final float[] a, final int fromIndex, final int toIndex, final FloatComparator c) {
    if (c != null)
      PrimitiveSort.sort(a, fromIndex, toIndex, c);
    else
      Arrays.sort(a, fromIndex, toIndex);
  }

  /**
   * Sorts the specified array of {@code double}s, according to the specified {@link DoubleComparator}.
   *
   * @param a The array of {@code double}s.
   * @param c The {@link DoubleComparator}.
   * @throws IllegalArgumentException If {@code a} is null.
   */
  public static void sort(final double[] a, final DoubleComparator c) {
    sort(a, 0, a.length, c);
  }

  /**
   * Sorts the specified array of {@code double}s, according to the specified {@link DoubleComparator}.
   *
   * @param a The array of {@code double}s.
   * @param fromIndex The index of the first element, inclusive, to be sorted.
   * @param toIndex The index of the last element, exclusive, to be sorted.
   * @param c The {@link DoubleComparator}.
   * @throws IllegalArgumentException If {@code a} is null.
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
   * @param fromIndex The index of the first element, inclusive, to be reversed.
   * @param toIndex The index of the last element, exclusive, to be reversed.
   * @return The provided array with its members reversed.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   */
  public static byte[] reverse(final byte[] array, final int fromIndex, final int toIndex) {
    if (array == null || array.length <= 1)
      return array;

    assertRangeArray(fromIndex, toIndex, array.length);
    for (int i = fromIndex, j = toIndex - 1, i$ = array.length / 2; i < i$; ++i, --j) { // [A]
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
  public static byte[] reverse(final byte[] array) {
    return array == null ? null : reverse(array, 0, array.length);
  }

  /**
   * Reverses the order of the members in the provided array.
   *
   * @param array The array of members to be reversed.
   * @param fromIndex The index of the first element, inclusive, to be reversed.
   * @param toIndex The index of the last element, exclusive, to be reversed.
   * @return The provided array with its members reversed.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   */
  public static short[] reverse(final short[] array, final int fromIndex, final int toIndex) {
    if (array == null || array.length <= 1)
      return array;

    assertRangeArray(fromIndex, toIndex, array.length);
    for (int i = fromIndex, j = toIndex - 1, i$ = array.length / 2; i < i$; ++i, --j) { // [A]
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
    return array == null ? null : reverse(array, 0, array.length);
  }

  /**
   * Reverses the order of the members in the provided array.
   *
   * @param array The array of members to be reversed.
   * @param fromIndex The index of the first element, inclusive, to be reversed.
   * @param toIndex The index of the last element, exclusive, to be reversed.
   * @return The provided array with its members reversed.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   */
  public static int[] reverse(final int[] array, final int fromIndex, final int toIndex) {
    if (array == null || array.length <= 1)
      return array;

    assertRangeArray(fromIndex, toIndex, array.length);
    for (int i = fromIndex, j = toIndex - 1, i$ = array.length / 2; i < i$; ++i, --j) { // [A]
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
    return array == null ? null : reverse(array, 0, array.length);
  }

  /**
   * Reverses the order of the members in the provided array.
   *
   * @param array The array of members to be reversed.
   * @param fromIndex The index of the first element, inclusive, to be reversed.
   * @param toIndex The index of the last element, exclusive, to be reversed.
   * @return The provided array with its members reversed.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   */
  public static long[] reverse(final long[] array, final int fromIndex, final int toIndex) {
    if (array == null || array.length <= 1)
      return array;

    assertRangeArray(fromIndex, toIndex, array.length);
    for (int i = fromIndex, j = toIndex - 1, i$ = array.length / 2; i < i$; ++i, --j) { // [A]
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
    return array == null ? null : reverse(array, 0, array.length);
  }

  /**
   * Reverses the order of the members in the provided array.
   *
   * @param array The array of members to be reversed.
   * @param fromIndex The index of the first element, inclusive, to be reversed.
   * @param toIndex The index of the last element, exclusive, to be reversed.
   * @return The provided array with its members reversed.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   */
  public static float[] reverse(final float[] array, final int fromIndex, final int toIndex) {
    if (array == null || array.length <= 1)
      return array;

    assertRangeArray(fromIndex, toIndex, array.length);
    float tmp;
    for (int i = fromIndex, j = toIndex - 1, i$ = array.length / 2; i < i$; ++i, --j) { // [A]
      tmp = array[i];
      array[i] = array[j];
      array[j] = tmp;
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
    return array == null ? null : reverse(array, 0, array.length);
  }

  /**
   * Reverses the order of the members in the provided array.
   *
   * @param array The array of members to be reversed.
   * @param fromIndex The index of the first element, inclusive, to be reversed.
   * @param toIndex The index of the last element, exclusive, to be reversed.
   * @return The provided array with its members reversed.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   */
  public static double[] reverse(final double[] array, final int fromIndex, final int toIndex) {
    if (array == null || array.length <= 1)
      return array;

    assertRangeArray(fromIndex, toIndex, array.length);
    double tmp;
    for (int i = fromIndex, j = toIndex - 1, i$ = array.length / 2; i < i$; ++i, --j) { // [A]
      tmp = array[i];
      array[i] = array[j];
      array[j] = tmp;
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
    return array == null ? null : reverse(array, 0, array.length);
  }

  /**
   * Reverses the order of the members in the provided array.
   *
   * @param array The array of members to be reversed.
   * @param fromIndex The index of the first element, inclusive, to be reversed.
   * @param toIndex The index of the last element, exclusive, to be reversed.
   * @return The provided array with its members reversed.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   */
  public static boolean[] reverse(final boolean[] array, final int fromIndex, final int toIndex) {
    if (array == null || array.length <= 1)
      return array;

    assertRangeArray(fromIndex, toIndex, array.length);
    for (int i = fromIndex, j = toIndex - 1, i$ = array.length / 2; i < i$; ++i, --j) { // [A]
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
    return array == null ? null : reverse(array, 0, array.length);
  }

  /**
   * Reverses the order of the members in the provided array.
   *
   * @param array The array of members to be reversed.
   * @param fromIndex The index of the first element, inclusive, to be reversed.
   * @param toIndex The index of the last element, exclusive, to be reversed.
   * @return The provided array with its members reversed.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   */
  public static char[] reverse(final char[] array, final int fromIndex, final int toIndex) {
    if (array == null || array.length <= 1)
      return array;

    assertRangeArray(fromIndex, toIndex, array.length);
    for (int i = fromIndex, j = toIndex - 1, i$ = array.length / 2; i < i$; ++i, --j) { // [A]
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
  public static char[] reverse(final char[] array) {
    return array == null ? null : reverse(array, 0, array.length);
  }

  /**
   * Reverses the order of the members in the provided array.
   *
   * @param <T> The component type of the array.
   * @param array The array of members to be reversed.
   * @param fromIndex The index of the first element, inclusive, to be reversed.
   * @param toIndex The index of the last element, exclusive, to be reversed.
   * @return The provided array with its members reversed.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   */
  public static <T>T[] reverse(final T[] array, final int fromIndex, final int toIndex) {
    if (array == null || array.length < 2)
      return array;

    assertRangeArray(fromIndex, toIndex, array.length);
    T tmp;
    for (int i = 0, j = array.length - 1, i$ = array.length / 2; i < i$; ++i, --j) { // [A]
      tmp = array[i];
      array[i] = array[j];
      array[j] = tmp;
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
  public static <T>T[] reverse(final T[] array) {
    return array == null ? null : reverse(array, 0, array.length);
  }

  /**
   * Circularly shifts the members in the provided array by the specified {@code offset}
   *
   * @param array The array of members to circularly shift.
   * @param fromIndex The index of the first element, inclusive, to be reversed.
   * @param toIndex The index of the last element, exclusive, to be reversed.
   * @param offset The offset.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws IllegalArgumentException If {@code array} is null, or if {@code offset} is negative or greater than
   *           {@code array.length}.
   */
  public static void shift(final byte[] array, final int fromIndex, final int toIndex, final int offset) {
    if (offset < 0)
      throw new IllegalArgumentException("offset (" + offset + ") must be non-negative");

    if (array.length < offset)
      throw new IllegalArgumentException("offset (" + offset + ") must less than array.length (" + array.length + ")");

    assertRangeArray(fromIndex, toIndex, array.length);
    if (offset != 0 && array.length != offset)
      shift(array, offset, array.length, array.length, offset);
  }

  /**
   * Circularly shifts the members in the provided array by the specified {@code offset}
   *
   * @param array The array of members to circularly shift.
   * @param offset The offset.
   * @throws IllegalArgumentException If {@code array} is null, or if {@code offset} is negative or greater than
   *           {@code array.length}.
   */
  public static void shift(final byte[] array, final int offset) {
    shift(array, 0, array.length, offset);
  }

  private static void shift(final byte[] array, final int from, final int to, final int len, final int off) {
    if (from == to) {
      if (to != off)
        shift(array, 0, off, len, off);

      return;
    }

    final byte value = array[from];
    shift(array, from + 1, to, len, off);
    final int i = from - off;
    array[i < 0 ? len + i : i] = value;
  }

  /**
   * Circularly shifts the members in the provided array by the specified {@code offset}
   *
   * @param array The array of members to circularly shift.
   * @param fromIndex The index of the first element, inclusive, to be reversed.
   * @param toIndex The index of the last element, exclusive, to be reversed.
   * @param offset The offset.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws IllegalArgumentException If {@code array} is null, or if {@code offset} is negative or greater than
   *           {@code array.length}.
   */
  public static void shift(final short[] array, final int fromIndex, final int toIndex, final int offset) {
    if (offset < 0)
      throw new IllegalArgumentException("offset (" + offset + ") must be non-negative");

    if (array.length < offset)
      throw new IllegalArgumentException("offset (" + offset + ") must less than array.length (" + array.length + ")");

    assertRangeArray(fromIndex, toIndex, array.length);
    if (offset != 0 && array.length != offset)
      shift(array, offset, array.length, array.length, offset);
  }

  /**
   * Circularly shifts the members in the provided array by the specified {@code offset}
   *
   * @param array The array of members to circularly shift.
   * @param offset The offset.
   * @throws IllegalArgumentException If {@code array} is null, or if {@code offset} is negative or greater than
   *           {@code array.length}.
   */
  public static void shift(final short[] array, final int offset) {
    shift(array, 0, array.length, offset);
  }

  private static void shift(final short[] array, final int from, final int to, final int len, final int off) {
    if (from == to) {
      if (to != off)
        shift(array, 0, off, len, off);

      return;
    }

    final short value = array[from];
    shift(array, from + 1, to, len, off);
    final int i = from - off;
    array[i < 0 ? len + i : i] = value;
  }

  /**
   * Circularly shifts the members in the provided array by the specified {@code offset}
   *
   * @param array The array of members to circularly shift.
   * @param fromIndex The index of the first element, inclusive, to be reversed.
   * @param toIndex The index of the last element, exclusive, to be reversed.
   * @param offset The offset.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws IllegalArgumentException If {@code array} is null, or if {@code offset} is negative or greater than
   *           {@code array.length}.
   */
  public static void shift(final int[] array, final int fromIndex, final int toIndex, final int offset) {
    if (offset < 0)
      throw new IllegalArgumentException("offset (" + offset + ") must be non-negative");

    if (array.length < offset)
      throw new IllegalArgumentException("offset (" + offset + ") must less than array.length (" + array.length + ")");

    assertRangeArray(fromIndex, toIndex, array.length);
    if (offset != 0 && array.length != offset)
      shift(array, offset, array.length, array.length, offset);
  }

  /**
   * Circularly shifts the members in the provided array by the specified {@code offset}
   *
   * @param array The array of members to circularly shift.
   * @param offset The offset.
   * @throws IllegalArgumentException If {@code array} is null, or if {@code offset} is negative or greater than
   *           {@code array.length}.
   */
  public static void shift(final int[] array, final int offset) {
    shift(array, 0, array.length, offset);
  }

  private static void shift(final int[] array, final int from, final int to, final int len, final int off) {
    if (from == to) {
      if (to != off)
        shift(array, 0, off, len, off);

      return;
    }

    final int value = array[from];
    shift(array, from + 1, to, len, off);
    final int i = from - off;
    array[i < 0 ? len + i : i] = value;
  }

  /**
   * Circularly shifts the members in the provided array by the specified {@code offset}
   *
   * @param array The array of members to circularly shift.
   * @param fromIndex The index of the first element, inclusive, to be reversed.
   * @param toIndex The index of the last element, exclusive, to be reversed.
   * @param offset The offset.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws IllegalArgumentException If {@code array} is null, or if {@code offset} is negative or greater than
   *           {@code array.length}.
   */
  public static void shift(final long[] array, final int fromIndex, final int toIndex, final int offset) {
    if (offset < 0)
      throw new IllegalArgumentException("offset (" + offset + ") must be non-negative");

    if (array.length < offset)
      throw new IllegalArgumentException("offset (" + offset + ") must less than array.length (" + array.length + ")");

    assertRangeArray(fromIndex, toIndex, array.length);
    if (offset != 0 && array.length != offset)
      shift(array, offset, array.length, array.length, offset);
  }

  /**
   * Circularly shifts the members in the provided array by the specified {@code offset}
   *
   * @param array The array of members to circularly shift.
   * @param offset The offset.
   * @throws IllegalArgumentException If {@code array} is null, or if {@code offset} is negative or greater than
   *           {@code array.length}.
   */
  public static void shift(final long[] array, final int offset) {
    shift(array, 0, array.length, offset);
  }

  private static void shift(final long[] array, final int from, final int to, final int len, final int off) {
    if (from == to) {
      if (to != off)
        shift(array, 0, off, len, off);

      return;
    }

    final long value = array[from];
    shift(array, from + 1, to, len, off);
    final int i = from - off;
    array[i < 0 ? len + i : i] = value;
  }

  /**
   * Circularly shifts the members in the provided array by the specified {@code offset}
   *
   * @param array The array of members to circularly shift.
   * @param fromIndex The index of the first element, inclusive, to be reversed.
   * @param toIndex The index of the last element, exclusive, to be reversed.
   * @param offset The offset.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws IllegalArgumentException If {@code array} is null, or if {@code offset} is negative or greater than
   *           {@code array.length}.
   */
  public static void shift(final float[] array, final int fromIndex, final int toIndex, final int offset) {
    if (offset < 0)
      throw new IllegalArgumentException("offset (" + offset + ") must be non-negative");

    if (array.length < offset)
      throw new IllegalArgumentException("offset (" + offset + ") must less than array.length (" + array.length + ")");

    assertRangeArray(fromIndex, toIndex, array.length);
    if (offset != 0 && array.length != offset)
      shift(array, offset, array.length, array.length, offset);
  }

  /**
   * Circularly shifts the members in the provided array by the specified {@code offset}
   *
   * @param array The array of members to circularly shift.
   * @param offset The offset.
   * @throws IllegalArgumentException If {@code array} is null, or if {@code offset} is negative or greater than
   *           {@code array.length}.
   */
  public static void shift(final float[] array, final int offset) {
    shift(array, 0, array.length, offset);
  }

  private static void shift(final float[] array, final int from, final int to, final int len, final int off) {
    if (from == to) {
      if (to != off)
        shift(array, 0, off, len, off);

      return;
    }

    final float value = array[from];
    shift(array, from + 1, to, len, off);
    final int i = from - off;
    array[i < 0 ? len + i : i] = value;
  }

  /**
   * Circularly shifts the members in the provided array by the specified {@code offset}
   *
   * @param array The array of members to circularly shift.
   * @param fromIndex The index of the first element, inclusive, to be reversed.
   * @param toIndex The index of the last element, exclusive, to be reversed.
   * @param offset The offset.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws IllegalArgumentException If {@code array} is null, or if {@code offset} is negative or greater than
   *           {@code array.length}.
   */
  public static void shift(final double[] array, final int fromIndex, final int toIndex, final int offset) {
    if (offset < 0)
      throw new IllegalArgumentException("offset (" + offset + ") must be non-negative");

    if (array.length < offset)
      throw new IllegalArgumentException("offset (" + offset + ") must less than array.length (" + array.length + ")");

    assertRangeArray(fromIndex, toIndex, array.length);
    if (offset != 0 && array.length != offset)
      shift(array, offset, array.length, array.length, offset);
  }

  /**
   * Circularly shifts the members in the provided array by the specified {@code offset}
   *
   * @param array The array of members to circularly shift.
   * @param offset The offset.
   * @throws IllegalArgumentException If {@code array} is null, or if {@code offset} is negative or greater than
   *           {@code array.length}.
   */
  public static void shift(final double[] array, final int offset) {
    shift(array, 0, array.length, offset);
  }

  private static void shift(final double[] array, final int from, final int to, final int len, final int off) {
    if (from == to) {
      if (to != off)
        shift(array, 0, off, len, off);

      return;
    }

    final double value = array[from];
    shift(array, from + 1, to, len, off);
    final int i = from - off;
    array[i < 0 ? len + i : i] = value;
  }

  /**
   * Circularly shifts the members in the provided array by the specified {@code offset}
   *
   * @param array The array of members to circularly shift.
   * @param fromIndex The index of the first element, inclusive, to be reversed.
   * @param toIndex The index of the last element, exclusive, to be reversed.
   * @param offset The offset.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws IllegalArgumentException If {@code array} is null, or if {@code offset} is negative or greater than
   *           {@code array.length}.
   */
  public static void shift(final boolean[] array, final int fromIndex, final int toIndex, final int offset) {
    if (offset < 0)
      throw new IllegalArgumentException("offset (" + offset + ") must be non-negative");

    if (array.length < offset)
      throw new IllegalArgumentException("offset (" + offset + ") must less than array.length (" + array.length + ")");

    assertRangeArray(fromIndex, toIndex, array.length);
    if (offset != 0 && array.length != offset)
      shift(array, offset, array.length, array.length, offset);
  }

  /**
   * Circularly shifts the members in the provided array by the specified {@code offset}
   *
   * @param array The array of members to circularly shift.
   * @param offset The offset.
   * @throws IllegalArgumentException If {@code array} is null, or if {@code offset} is negative or greater than
   *           {@code array.length}.
   */
  public static void shift(final boolean[] array, final int offset) {
    shift(array, 0, array.length, offset);
  }

  private static void shift(final boolean[] array, final int from, final int to, final int len, final int off) {
    if (from == to) {
      if (to != off)
        shift(array, 0, off, len, off);

      return;
    }

    final boolean value = array[from];
    shift(array, from + 1, to, len, off);
    final int i = from - off;
    array[i < 0 ? len + i : i] = value;
  }

  /**
   * Circularly shifts the members in the provided array by the specified {@code offset}
   *
   * @param array The array of members to circularly shift.
   * @param fromIndex The index of the first element, inclusive, to be reversed.
   * @param toIndex The index of the last element, exclusive, to be reversed.
   * @param offset The offset.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws IllegalArgumentException If {@code array} is null, or if {@code offset} is negative or greater than
   *           {@code array.length}.
   */
  public static void shift(final char[] array, final int fromIndex, final int toIndex, final int offset) {
    if (offset < 0)
      throw new IllegalArgumentException("offset (" + offset + ") must be non-negative");

    if (array.length < offset)
      throw new IllegalArgumentException("offset (" + offset + ") must less than array.length (" + array.length + ")");

    assertRangeArray(fromIndex, toIndex, array.length);
    if (offset != 0 && array.length != offset)
      shift(array, offset, array.length, array.length, offset);
  }

  /**
   * Circularly shifts the members in the provided array by the specified {@code offset}
   *
   * @param array The array of members to circularly shift.
   * @param offset The offset.
   * @throws IllegalArgumentException If {@code array} is null, or if {@code offset} is negative or greater than
   *           {@code array.length}.
   */
  public static void shift(final char[] array, final int offset) {
    shift(array, 0, array.length, offset);
  }

  private static void shift(final char[] array, final int from, final int to, final int len, final int off) {
    if (from == to) {
      if (to != off)
        shift(array, 0, off, len, off);

      return;
    }

    final char value = array[from];
    shift(array, from + 1, to, len, off);
    final int i = from - off;
    array[i < 0 ? len + i : i] = value;
  }

  /**
   * Circularly shifts the members in the provided array by the specified {@code offset}
   *
   * @param array The array of members to circularly shift.
   * @param fromIndex The index of the first element, inclusive, to be reversed.
   * @param toIndex The index of the last element, exclusive, to be reversed.
   * @param offset The offset.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0 or toIndex > a.length}.
   * @throws IllegalArgumentException If {@code array} is null, or if {@code offset} is negative or greater than
   *           {@code array.length}.
   */
  public static void shift(final Object[] array, final int fromIndex, final int toIndex, final int offset) {
    if (offset < 0)
      throw new IllegalArgumentException("offset (" + offset + ") must be non-negative");

    if (array.length < offset)
      throw new IllegalArgumentException("offset (" + offset + ") must less than array.length (" + array.length + ")");

    assertRangeArray(fromIndex, toIndex, array.length);
    if (offset != 0 && array.length != offset)
      shift(array, offset, array.length, array.length, offset);
  }

  /**
   * Circularly shifts the members in the provided array by the specified {@code offset}
   *
   * @param array The array of members to circularly shift.
   * @param offset The offset.
   * @throws IllegalArgumentException If {@code array} is null, or if {@code offset} is negative or greater than
   *           {@code array.length}.
   */
  public static void shift(final Object[] array, final int offset) {
    shift(array, 0, array.length, offset);
  }

  private static void shift(final Object[] array, final int from, final int to, final int len, final int off) {
    if (from == to) {
      if (to != off)
        shift(array, 0, off, len, off);

      return;
    }

    final Object value = array[from];
    shift(array, from + 1, to, len, off);
    final int i = from - off;
    array[i < 0 ? len + i : i] = value;
  }

  private static int dedupe(final byte[] tables, final int len, final int index, final int depth) {
    if (index == len)
      return depth;

    final byte element = tables[index];
    if (tables[index - 1] == element)
      return dedupe(tables, len, index + 1, depth);

    final int length = dedupe(tables, len, index + 1, depth + 1);
    tables[depth] = element;
    return length;
  }

  /**
   * Deduplicates the provided array by reordering the unique values in ascending order, returning the number of unique values.
   *
   * @param a The array to dedupe.
   * @return The number of unique values after having reordering the unique values in ascending order.
   * @throws IllegalArgumentException If the provided array is null.
   */
  public static int dedupe(final byte[] a) {
    return a.length <= 1 ? a.length : dedupe(a, a.length, 1, 1);
  }

  private static int dedupe(final char[] tables, final int len, final int index, final int depth) {
    if (index == len)
      return depth;

    final char element = tables[index];
    if (tables[index - 1] == element)
      return dedupe(tables, len, index + 1, depth);

    final int length = dedupe(tables, len, index + 1, depth + 1);
    tables[depth] = element;
    return length;
  }

  /**
   * Deduplicates the provided array by reordering the unique values in ascending order, returning the number of unique values.
   *
   * @param a The array to dedupe.
   * @return The number of unique values after having reordering the unique values in ascending order.
   * @throws IllegalArgumentException If the provided array is null.
   */
  public static int dedupe(final char[] a) {
    return a.length <= 1 ? a.length : dedupe(a, a.length, 1, 1);
  }

  private static int dedupe(final short[] tables, final int len, final int index, final int depth) {
    if (index == len)
      return depth;

    final short element = tables[index];
    if (tables[index - 1] == element)
      return dedupe(tables, len, index + 1, depth);

    final int length = dedupe(tables, len, index + 1, depth + 1);
    tables[depth] = element;
    return length;
  }

  /**
   * Deduplicates the provided array by reordering the unique values in ascending order, returning the number of unique values.
   *
   * @param a The array to dedupe.
   * @return The number of unique values after having reordering the unique values in ascending order.
   * @throws IllegalArgumentException If the provided array is null.
   */
  public static int dedupe(final short[] a) {
    return a.length <= 1 ? a.length : dedupe(a, a.length, 1, 1);
  }

  private static int dedupe(final int[] tables, final int len, final int index, final int depth) {
    if (index == len)
      return depth;

    final int element = tables[index];
    if (tables[index - 1] == element)
      return dedupe(tables, len, index + 1, depth);

    final int length = dedupe(tables, len, index + 1, depth + 1);
    tables[depth] = element;
    return length;
  }

  /**
   * Deduplicates the provided array by reordering the unique values in ascending order, returning the number of unique values.
   *
   * @param a The array to dedupe.
   * @return The number of unique values after having reordering the unique values in ascending order.
   * @throws IllegalArgumentException If the provided array is null.
   */
  public static int dedupe(final int[] a) {
    return a.length <= 1 ? a.length : dedupe(a, a.length, 1, 1);
  }

  private static int dedupe(final long[] tables, final int len, final int index, final int depth) {
    if (index == len)
      return depth;

    final long element = tables[index];
    if (tables[index - 1] == element)
      return dedupe(tables, len, index + 1, depth);

    final int length = dedupe(tables, len, index + 1, depth + 1);
    tables[depth] = element;
    return length;
  }

  /**
   * Deduplicates the provided array by reordering the unique values in ascending order, returning the number of unique values.
   *
   * @param a The array to dedupe.
   * @return The number of unique values after having reordering the unique values in ascending order.
   * @throws IllegalArgumentException If the provided array is null.
   */
  public static int dedupe(final long[] a) {
    return a.length <= 1 ? a.length : dedupe(a, a.length, 1, 1);
  }

  private static int dedupe(final float[] tables, final int len, final int index, final int depth) {
    if (index == len)
      return depth;

    final float element = tables[index];
    if (tables[index - 1] == element)
      return dedupe(tables, len, index + 1, depth);

    final int length = dedupe(tables, len, index + 1, depth + 1);
    tables[depth] = element;
    return length;
  }

  /**
   * Deduplicates the provided array by reordering the unique values in ascending order, returning the number of unique values.
   *
   * @param a The array to dedupe.
   * @return The number of unique values after having reordering the unique values in ascending order.
   * @throws IllegalArgumentException If the provided array is null.
   */
  public static int dedupe(final float[] a) {
    return a.length <= 1 ? a.length : dedupe(a, a.length, 1, 1);
  }

  private static int dedupe(final double[] tables, final int len, final int index, final int depth) {
    if (index == len)
      return depth;

    final double element = tables[index];
    if (tables[index - 1] == element)
      return dedupe(tables, len, index + 1, depth);

    final int length = dedupe(tables, len, index + 1, depth + 1);
    tables[depth] = element;
    return length;
  }

  /**
   * Deduplicates the provided array by reordering the unique values in ascending order, returning the number of unique values.
   *
   * @param a The array to dedupe.
   * @return The number of unique values after having reordering the unique values in ascending order.
   * @throws IllegalArgumentException If the provided array is null.
   */
  public static int dedupe(final double[] a) {
    return a.length <= 1 ? a.length : dedupe(a, a.length, 1, 1);
  }

  private static <T>int dedupe(final T[] a, final int len, final int index, final int depth, final Comparator<? super T> c) {
    if (index == len)
      return depth;

    final T element = a[index];
    if (c.compare(a[index - 1], element) == 0)
      return dedupe(a, len, index + 1, depth, c);

    final int length = dedupe(a, len, index + 1, depth + 1, c);
    a[depth] = element;
    return length;
  }

  /**
   * Deduplicates the provided array by reordering the unique elements in ascending order specified by the given {@link Comparator},
   * returning the number of unique elements.
   *
   * @param <T> The type parameter of the provided array.
   * @param a The array to dedupe.
   * @param c The {@link Comparator}.
   * @return The number of unique elements after having reordering the unique elements in ascending order specified by the given
   *         {@link Comparator}.
   * @throws IllegalArgumentException If the provided array or {@link Comparator} is null.
   */
  public static <T>int dedupe(final T[] a, final Comparator<? super T> c) {
    return a.length <= 1 ? a.length : dedupe(a, a.length, 1, 1, c);
  }

  /**
   * Compares two {@code Object} arrays lexicographically using a specified comparator.
   * <p>
   * If the two arrays share a common prefix then the lexicographic comparison is the result of comparing with the specified
   * comparator two elements at an index within the respective arrays that is the prefix length. Otherwise, one array is a proper
   * prefix of the other and, lexicographic comparison is the result of comparing the two array lengths.
   * <p>
   * A {@code null} array reference is considered lexicographically less than a non-{@code null} array reference. Two {@code null}
   * array references are considered equal.
   *
   * @param a The first array to compare.
   * @param b The second array to compare.
   * @param cmp The comparator to compare array elements.
   * @param <T> The type of array elements.
   * @return The value {@code 0} if the first and second array are equal and contain the same elements in the same order; a value
   *         less than {@code 0} if the first array is lexicographically less than the second array; and a value greater than
   *         {@code 0} if the first array is lexicographically greater than the second array.
   * @throws IllegalArgumentException If the comparator is null.
   */
  public static <T>int compare(final T[] a, final T[] b, final Comparator<? super T> cmp) {
    if (a == b)
      return 0;

    if (a == null)
      return -1;

    if (b == null)
      return 1;

    for (int i = 0, i$ = Math.min(a.length, b.length); i < i$; ++i) { // [A]
      final T oa = a[i];
      final T ob = b[i];
      if (oa != ob) {
        final int v = cmp.compare(oa, ob);
        if (v != 0)
          return v;
      }
    }

    return a.length - b.length;
  }

  private ArrayUtil() {
  }
}