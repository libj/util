/* Copyright (c) 2008 LibJ
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.function.Function;

import org.libj.lang.Classes;
import org.libj.lang.WrappedArrayList;
import org.libj.util.primitive.ArrayDoubleList;
import org.libj.util.primitive.ArrayFloatList;
import org.libj.util.primitive.ArrayIntList;
import org.libj.util.primitive.ArrayLongList;
import org.libj.util.primitive.ByteComparator;
import org.libj.util.primitive.CharComparator;
import org.libj.util.primitive.DoubleComparator;
import org.libj.util.primitive.FloatComparator;
import org.libj.util.primitive.IntComparator;
import org.libj.util.primitive.LongComparator;
import org.libj.util.primitive.PrimitiveSort;
import org.libj.util.primitive.ShortComparator;

/**
 * Utility functions for operations pertaining to {@link Collection} and {@link List}.
 */
public final class CollectionUtil extends PrimitiveSort {
  /**
   * The empty {@link ArrayList} (immutable). This list is serializable.
   *
   * @see WrappedArrayList
   */
  @SuppressWarnings("rawtypes")
  public static final ArrayList EMPTY_ARRAY_LIST = new WrappedArrayList<>();

  /**
   * Adds all of the elements in the {@code subset} collection to the {@code set} collection. The behavior of this operation is
   * undefined if the specified collection is modified while the operation is in progress.
   *
   * @param <E> The type of elements in {@code set}.
   * @param set Collection to which elements from {@code subset} are to be added.
   * @param subset Collection containing elements to be added to {@code set}.
   * @return {@code true} if {@code set} changed as a result of the call.
   * @throws IllegalArgumentException If {@code set} or {@code subset} is null.
   */
  public static <E>boolean addAll(final Collection<E> set, final Collection<? extends E> subset) {
    assertNotNull(set);
    assertNotNull(subset);

    if (subset.size() == 0)
      return false;

    boolean changed = false;
    final List<? extends E> sublist;
    if (subset instanceof List && isRandomAccess(sublist = (List<? extends E>)subset)) {
      for (int i = 0, i$ = sublist.size(); i < i$; ++i) // [RA]
        changed |= set.add(sublist.get(i));
    }
    else {
      for (final E e : subset) // [C]
        changed |= set.add(e);
    }

    return changed;
  }

  /**
   * Returns {@code true} if {@code set} contains all the elements specified in {@code subset}; otherwise {@code false}.
   *
   * @param set Collection to check for containment of {@code subset}.
   * @param subset Collection to be checked for containment in {@code set}.
   * @return {@code true} if {@code set} contains all the elements specified in {@code subset}; otherwise {@code false}.
   * @throws IllegalArgumentException If {@code set} or {@code subset} is null.
   */
  public static boolean containsAll(final Collection<?> set, final Collection<?> subset) {
    assertNotNull(set);
    assertNotNull(subset);

    if (subset.size() == 0)
      return true;

    final List<?> sublist;
    if (subset instanceof List && isRandomAccess(sublist = (List<?>)subset)) {
      for (int i = 0, i$ = sublist.size(); i < i$; ++i) // [RA]
        if (!set.contains(sublist.get(i)))
          return false;

      return true;
    }

    for (final Object o : subset) // [C]
      if (!set.contains(o))
        return false;

    return true;
  }

  /**
   * Removes all of elements in {@code set} that are also contained in {@code subset}. After this call returns, {@code set} will
   * contain no elements in common with {@code subset}.
   *
   * @param set Collection from which elements in {@code subset} are to be removed.
   * @param subset Collection containing elements to be removed from {@code set}.
   * @return {@code true} if {@code set} changed as a result of the call.
   * @throws IllegalArgumentException If {@code set} or {@code subset} is null.
   */
  public static boolean removeAll(final Collection<?> set, final Collection<?> subset) {
    assertNotNull(set);
    assertNotNull(subset);

    if (subset.size() == 0)
      return false;

    final int size = set.size();
    final List<?> sublist;
    if (subset instanceof List && isRandomAccess(sublist = (List<?>)subset))
      for (int i = 0, i$ = sublist.size(); i < i$; ++i) // [RA]
        set.remove(sublist.get(i));
    else
      for (final Object e : subset) // [C]
        set.remove(e);

    return size != set.size();
  }

  /**
   * Retains only the elements in {@code set} that are contained in {@code subset}. In other words, removes from {@code set} all of
   * its elements that are not contained in {@code subset}.
   *
   * @param set Collection in which elements from {@code subset} are to be retained.
   * @param subset Collection containing elements to be retained in this {@code set}.
   * @return {@code true} if {@code set} changed as a result of the call.
   * @throws IllegalArgumentException If {@code set} or {@code subset} is null.
   */
  public static boolean retainAll(final Collection<?> set, final Collection<?> subset) {
    assertNotNull(set);
    assertNotNull(subset);

    final int size = set.size();
    final int subSize = subset.size();
    if (subSize == 0) {
      if (size == 0)
        return false;

      set.clear();
      return true;
    }

    final List<?> list;
    if (set instanceof List && isRandomAccess(list = (List<?>)set)) {
      for (int i = 0; i < subSize; ++i) // [RA]
        if (!subset.contains(list.get(i)))
          list.remove(i);
    }
    else {
      for (final Iterator<?> i = set.iterator(); i.hasNext();) // [X]
        if (!subset.contains(i.next()))
          i.remove();
    }

    return size != set.size();
  }

  /**
   * Returns {@code true} if the provided {@link List} either directly implements the {@link RandomAccess} interface, or is a
   * {@link DelegateList} wrapping another list that implements the {@link RandomAccess} interface; otherwise {@code false}.
   *
   * @param l The list to test.
   * @return {@code true} if the provided {@link List} either directly implements the {@link RandomAccess} interface, or is a
   *         {@link DelegateList} wrapping another list that implements the {@link RandomAccess} interface; otherwise {@code false}.
   */
  public static boolean isRandomAccess(final List<?> l) {
    return l instanceof RandomAccess || l instanceof DelegateList && ((DelegateList<?,?>)l).isRandomAccess();
  }

  /**
   * Inserts the one-dimensional representation of the input collection into the specified output collection, whereby all nested
   * {@link Collection} members are flattened at every depth. The value of {@code member instanceof Collection} is used to determine
   * whether a member represents a {@link Collection} for further recursion.
   * <p>
   * Collection members that reference a {@link Collection} are <i>not included</i> in the resulting collection. This is the
   * equivalent of calling {@code flatten(Collection,Collection,false)}.
   *
   * @param <C> The type of the output collection.
   * @param <T> The type of collection elements.
   * @param in The input collection.
   * @param out The output collection.
   * @return The specified output collection, filled with the one-dimensional representation of the input collection.
   * @throws IllegalArgumentException If {@code in} or {@code out} is null.
   */
  public static <C extends Collection<T>,T>C flatten(final Collection<T> in, final C out) {
    return flatten(in, out, null, false);
  }

  /**
   * Inserts the one-dimensional representation of the input collection into the specified output collection, whereby all nested
   * {@link Collection} members are flattened at every depth.
   *
   * @param <C> The type of the output collection.
   * @param <T> The type of collection elements.
   * @param in The input collection.
   * @param out The output collection.
   * @param retainCollectionReferences If {@code true}, members that reference a {@link Collection} are included in the output
   *          collection; if {@code false}, they are not included in the output collection.
   * @return The specified output collection, filled with the one-dimensional representation of the input collection.
   * @throws IllegalArgumentException If {@code in} or {@code out} is null.
   */
  public static <C extends Collection<T>,T>C flatten(final Collection<T> in, final C out, final boolean retainCollectionReferences) {
    return flatten(in, out, null, retainCollectionReferences);
  }

  /**
   * Inserts the one-dimensional representation of the input collection into the specified output collection, whereby all nested
   * {@link Collection} members are flattened at every depth. The specified resolver {@link Function} provides a layer of
   * indirection between a member, and a higher-layer value. This is useful in the situation where the collection contains symbolic
   * references to other collections. The {@code resolver} parameter is provided to dereference such a symbolic references.
   *
   * @param <C> The type of the output collection.
   * @param <T> The type of collection elements.
   * @param in The input collection.
   * @param out The output collection.
   * @param resolver A {@link Function} to provide a layer of indirection between a member, and a higher-layer value. If
   *          {@code resolver} is null, {@code member instanceof Collection} is used to determine whether the member value
   *          represents a collection.
   * @param retainCollectionReferences If {@code true}, members that reference a {@link Collection} are included in the output
   *          collection; if {@code false}, they are not included in the output collection.
   * @return The specified output collection, filled with the one-dimensional representation of the input collection.
   * @throws IllegalArgumentException If {@code in} or {@code out} is null.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <C extends Collection<T>,T>C flatten(final Collection<? extends T> in, final C out, final Function<T,? extends Collection<T>> resolver, final boolean retainCollectionReferences) {
    assertNotNull(in);
    assertNotNull(out);
    for (final T member : in) { // [C]
      final Collection inner = resolver != null ? resolver.apply(member) : member instanceof Collection ? (Collection)member : null;
      if (inner != null) {
        if (retainCollectionReferences)
          out.add(member);

        flatten(inner, out, resolver, retainCollectionReferences);
      }
      else {
        out.add(member);
      }
    }

    return out;
  }

  /**
   * Flattens the specified list into a one-dimensional representation, in-place, replacing all nested {@link List} members with
   * their expanded form, at every depth. The value of {@code member instanceof List} is used to determine whether a member
   * represents a {@link List} for further recursion.
   * <p>
   * List members that reference a {@link List} are <i>not included</i> in the resulting array. This is the equivalent of calling
   * {@code flatten(List,false)}.
   *
   * @param <L> The type of the list.
   * @param <T> The type of list elements.
   * @param list The list.
   * @return The specified list.
   * @throws IllegalArgumentException If {@code list} is null.
   */
  public static <L extends List<T>,T>L flatten(final L list) {
    flatten(list, (Function<T,List<T>>)null, false);
    return list;
  }

  /**
   * Flattens the specified list into a one-dimensional representation, in-place, replacing all nested {@link List} members with
   * their expanded form, at every depth.
   *
   * @param <L> The type of the list.
   * @param <T> The type of list elements.
   * @param list The list.
   * @param retainListReferences If {@code true}, members that reference a {@link List} are retained; if {@code false}, they are
   *          removed.
   * @return The specified list.
   * @throws IllegalArgumentException If {@code list} is null.
   */
  public static <L extends List<T>,T>L flatten(final L list, final boolean retainListReferences) {
    flatten(list, (Function<T,List<T>>)null, retainListReferences);
    return list;
  }

  /**
   * Flattens the specified list into a one-dimensional representation, in-place, replacing all nested {@link List} members with
   * their expanded form, at every depth. The specified resolver {@link Function} provides a layer of indirection between a member,
   * and a higher-layer value. This is useful in the situation where the collection contains symbolic references to other list. The
   * {@code resolver} parameter is provided to dereference such a symbolic references.
   *
   * @param <L> The type of the list.
   * @param <T> The type of list elements.
   * @param list The list.
   * @param retainListReferences If {@code true}, members that reference a {@link List} are retained; if {@code false}, they are
   *          removed.
   * @param resolver A {@link Function} to provide a layer of indirection between a member, and a higher-layer value. If
   *          {@code resolver} is null, {@code member instanceof List} is used to determine whether the member value represents a
   *          list.
   * @return The specified list.
   * @throws IllegalArgumentException If {@code list} is null.
   */
  @SuppressWarnings("unchecked")
  public static <L extends List<T>,T>L flatten(final L list, final Function<T,? extends List<T>> resolver, final boolean retainListReferences) {
    final ListIterator<T> iterator = assertNotNull(list).listIterator();
    int i = 0;
    while (iterator.hasNext()) {
      final T member = iterator.next();
      final List<T> inner = resolver != null ? resolver.apply(member) : member instanceof List ? (List<T>)member : null;
      if (inner != null) {
        if (retainListReferences)
          ++i;
        else
          iterator.remove();

        if (CollectionUtil.isRandomAccess(inner)) {
          for (int j = 0, i$ = inner.size(); j < i$; ++j) // [RA]
            iterator.add(inner.get(j));
        }
        else {
          for (final T item : inner) // [L]
            iterator.add(item);
        }

        while (iterator.nextIndex() > i)
          iterator.previous();
      }
      else {
        ++i;
      }
    }

    return list;
  }

  /**
   * Returns a class representing the component type of the specified collection. Each member of the specified array is
   * instance-assignable to the returned class.
   *
   * @param c The collection.
   * @return A class representing the component type of the specified collection, or {@code null} if the collection has no member
   *         objects.
   * @throws IllegalArgumentException If the specified collection is null.
   */
  public static Class<?> getComponentType(final Collection<?> c) {
    if (assertNotNull(c).size() == 0)
      return null;

    final Iterator<?> iterator = c.iterator();
    final Class<?>[] types = getNotNullMembers(iterator, 0);
    return types.length == 0 ? null : Classes.getGreatestCommonSuperclass(types);
  }

  private static Class<?>[] getNotNullMembers(final Iterator<?> iterator, final int depth) {
    while (iterator.hasNext()) {
      final Object member = iterator.next();
      if (member != null) {
        final Class<?>[] types = getNotNullMembers(iterator, depth + 1);
        types[depth] = member.getClass();
        return types;
      }
    }

    return new Class<?>[depth];
  }

  /**
   * Returns {@code true} if the specified class is instance-assignable for each member object of the specified collection,
   * otherwise {@code false}.
   *
   * @param c The collection.
   * @param type The class.
   * @return {@code true} if the specified class is instance-assignable for each member object of the specified collection,
   *         otherwise {@code false}.
   * @throws IllegalArgumentException If {@code c} or {@code type} is null.
   */
  public static boolean isComponentType(final Collection<?> c, final Class<?> type) {
    for (final Object member : assertNotNull(c)) // [C]
      if (member != null && !type.isInstance(member))
        return false;

    return true;
  }

  /**
   * Returns a string representation of the specified collection, using the specified delimiter between the string representation of
   * each element. If the specified collection is null, this method returns the string {@code "null"}. If the specified collection
   * is empty, this method returns {@code ""}.
   *
   * @param c The collection.
   * @param del The delimiter.
   * @return A string representation of the specified collection, using the specified delimiter between the string representation of
   *         each element.
   */
  public static String toString(final Collection<?> c, final char del) {
    return c == null ? "null" : c.size() == 0 ? "" : c instanceof List ? toStringTryRandomAccess((List<?>)c, del) : toStringIterator(c, del);
  }

  static String toStringIterator(final Collection<?> c, final char del) {
    final StringBuilder b = new StringBuilder();
    final Iterator<?> i = c.iterator();
    b.append(i.next());
    while (i.hasNext()) {
      final Object e = i.next();
      b.append(del).append(e == c ? "(this Collection)" : e);
    }

    return b.toString();
  }

  static String toStringTryRandomAccess(final List<?> c, final char del) {
    if (!isRandomAccess(c))
      return toStringIterator(c, del);

    final StringBuilder b = new StringBuilder();
    b.append(c.get(0));
    for (int i = 1, i$ = c.size(); i < i$; ++i) { // [RA]
      final Object e = c.get(i);
      b.append(del).append(e == c ? "(this Collection)" : e);
    }

    return b.toString();
  }

  /**
   * Returns a string representation of the specified collection, using the specified delimiter between the string representation of
   * each element. If the specified collection is null, this method returns the string {@code "null"}. If the specified collection
   * is empty, this method returns {@code ""}.
   *
   * @param c The collection.
   * @param del The delimiter.
   * @return A string representation of the specified collection, using the specified delimiter between the string representation of
   *         each element.
   */
  public static String toString(final Collection<?> c, final String del) {
    return c == null ? "null" : c.size() == 0 ? "" : c instanceof List ? toStringTryRandomAccess((List<?>)c, del) : toStringIterator(c, del);
  }

  static String toStringIterator(final Collection<?> c, final String del) {
    final StringBuilder b = new StringBuilder();
    final Iterator<?> i = c.iterator();
    b.append(i.next());
    while (i.hasNext()) {
      final Object e = i.next();
      b.append(del).append(e == c ? "(this Collection)" : e);
    }

    return b.toString();
  }

  static String toStringTryRandomAccess(final List<?> c, final String del) {
    if (!isRandomAccess(c))
      return toStringIterator(c, del);

    final StringBuilder b = new StringBuilder();
    b.append(c.get(0));
    for (int i = 1, i$ = c.size(); i < i$; ++i) { // [RA]
      final Object e = c.get(i);
      b.append(del).append(e == c ? "(this Collection)" : e);
    }

    return b.toString();
  }

  /**
   * Searches a range of the specified list for the specified object using the binary search algorithm. The range must be sorted in
   * ascending order according to the {@linkplain Comparable natural ordering} of its elements (as by the
   * {@link List#sort(Comparator)} method) prior to making this call. If it is not sorted, the results are undefined. (If the range
   * contains elements that are not mutually comparable (for example, strings and integers), it <i>cannot</i> be sorted according to
   * the natural ordering of its elements, hence results are undefined.) If the range contains multiple elements equal to the
   * specified object, there is no guarantee which one will be found.
   *
   * @param <T> Type parameter of Comparable key object.
   * @param a The list to be searched.
   * @param fromIndex The index of the first element (inclusive) to be searched.
   * @param toIndex The index of the last element (exclusive) to be searched.
   * @param key The value to be searched for.
   * @return Index of the search key if it is contained in the list within the specified range, otherwise
   *         {@code (-(<i>insertion point</i>) - 1)}. The <i>insertion point</i> is defined as the point at which the key would be
   *         inserted into the list: the index of the first element in the range greater than the key, or {@code toIndex} if all
   *         elements in the range are less than the specified key. Note that this guarantees that the return value will be
   *         {@code >= 0} if and only if the key is found.
   * @throws ClassCastException If the search key is not comparable to the elements of the list within the specified range.
   * @throws IllegalArgumentException If {@code fromIndex > toIndex}
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0} or {@code toIndex > a.length}
   * @throws IllegalArgumentException If {@code a} is null.
   */
  public static <T extends Comparable<? super T>>int binarySearch(final List<? extends T> a, final int fromIndex, final int toIndex, final T key) {
    assertNotNull(a);
    assertRangeArray(fromIndex, toIndex, a.size());
    return binarySearch0(a, fromIndex, toIndex, key);
  }

  /**
   * Searches the specified list for the specified object using the binary search algorithm. The list must be sorted in ascending
   * order according to the {@linkplain Comparable natural ordering} of its elements (as by the {@link List#sort(Comparator)}
   * method) prior to making this call. If it is not sorted, the results are undefined. (If the list contains elements that are not
   * mutually comparable (for example, strings and integers), it <i>cannot</i> be sorted according to the natural ordering of its
   * elements, hence results are undefined.) If the list contains multiple elements equal to the specified object, there is no
   * guarantee which one will be found.
   *
   * @param <T> Type parameter of Comparable key object.
   * @param a The list to be searched.
   * @param key The value to be searched for.
   * @return Index of the search key if it is contained in the list, otherwise {@code (-(<i>insertion point</i>) - 1)}. The
   *         <i>insertion point</i> is defined as the point at which the key would be inserted into the list: the index of the first
   *         element greater than the key, or {@code a.length} if all elements in the list are less than the specified key. Note
   *         that this guarantees that the return value will be {@code >= 0} if and only if the key is found.
   * @throws ClassCastException If the search key is not comparable to the elements of the array.
   * @throws IllegalArgumentException If {@code a} is null.
   */
  public static <T extends Comparable<? super T>>int binarySearch(final List<? extends T> a, final T key) {
    assertNotNull(a);
    return binarySearch0(a, 0, a.size(), key);
  }

  /**
   * Searches a range of the specified list for the specified object using the binary search algorithm. The range must be sorted in
   * ascending order according to the specified comparator (as by the {@link List#sort(Comparator)} method) prior to making this
   * call. If it is not sorted, the results are undefined. If the range contains multiple elements equal to the specified object,
   * there is no guarantee which one will be found.
   *
   * @param <T> The type parameter of the Comparable key object.
   * @param a The list to be searched.
   * @param fromIndex The index of the first element (inclusive) to be searched.
   * @param toIndex The index of the last element (exclusive) to be searched.
   * @param key The value to be searched for.
   * @param c The comparator by which the list is ordered. A {@code null} value indicates that the elements' {@linkplain Comparable
   *          natural ordering} should be used.
   * @return Index of the search key if it is contained in the list within the specified range, otherwise
   *         {@code (-(<i>insertion point</i>) - 1)}. The <i>insertion point</i> is defined as the point at which the key would be
   *         inserted into the list: the index of the first element in the range greater than the key, or {@code toIndex} if all
   *         elements in the range are less than the specified key. Note that this guarantees that the return value will be
   *         {@code >= 0} if and only if the key is found.
   * @throws ClassCastException If the range contains elements that are not <i>mutually comparable</i> using the specified
   *           comparator, or the search key is not comparable to the elements in the range using this comparator.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex < 0} or {@code toIndex > a.length}
   * @throws IllegalArgumentException If {@code a} or {@code c} is null, or if {@code fromIndex > toIndex}.
   */
  public static <T>int binarySearch(final List<? extends T> a, final int fromIndex, final int toIndex, final T key, final Comparator<? super T> c) {
    assertNotNull(a);
    assertRangeArray(fromIndex, toIndex, a.size());
    return binarySearch0(a, fromIndex, toIndex, key, c);
  }

  /**
   * Searches the specified list for the specified object using the binary search algorithm. The list must be sorted in ascending
   * order according to the specified comparator (as by the {@link List#sort(Comparator)} method) prior to making this call. If it
   * is not sorted, the results are undefined. If the list contains multiple elements equal to the specified object, there is no
   * guarantee which one will be found.
   *
   * @param <T> The type parameter of the Comparable key object.
   * @param a The list to be searched.
   * @param key The value to be searched for.
   * @param c The comparator by which the list is ordered. A {@code null} value indicates that the elements' {@linkplain Comparable
   *          natural ordering} should be used.
   * @return Index of the search key if it is contained in the list, otherwise {@code (-(<i>insertion point</i>) - 1)}. The
   *         <i>insertion point</i> is defined as the point at which the key would be inserted into the list: the index of the first
   *         element greater than the key, or {@code a.length} if all elements in the list are less than the specified key. Note
   *         that this guarantees that the return value will be {@code >= 0} if and only if the key is found.
   * @throws ClassCastException If the list contains elements that are not <i>mutually comparable</i> using the specified
   *           comparator, or the search key is not comparable to the elements of the list using this comparator.
   * @throws IllegalArgumentException If {@code a} or {@code c} is null.
   */
  public static <T>int binarySearch(final List<? extends T> a, final T key, final Comparator<? super T> c) {
    assertNotNull(a);
    return binarySearch0(a, 0, a.size(), key, c);
  }

  private static <T extends Comparable<? super T>>int binarySearch0(final List<? extends T> a, final int fromIndex, final int toIndex, final T key) {
    int low = fromIndex;
    int high = toIndex - 1;
    while (low <= high) {
      final int mid = (low + high) >>> 1;
      final Comparable<? super T> midVal = a.get(mid);
      final int cmp = midVal.compareTo(key);

      if (cmp < 0)
        low = mid + 1;
      else if (cmp > 0)
        high = mid - 1;
      else
        return mid;
    }

    return -(low + 1);
  }

  private static <T>int binarySearch0(final List<? extends T> a, final int fromIndex, final int toIndex, final T key, final Comparator<? super T> c) {
    int low = fromIndex;
    int high = toIndex - 1;
    while (low <= high) {
      final int mid = (low + high) >>> 1;
      final T midVal = a.get(mid);
      final int cmp = c.compare(midVal, key);
      if (cmp < 0)
        low = mid + 1;
      else if (cmp > 0)
        high = mid - 1;
      else
        return mid;
    }

    return -(low + 1);
  }

  /**
   * Find the index of the sorted {@link List} whose value most closely matches the value provided.
   *
   * @param <T> The type parameter of the Comparable key object.
   * @param a The sorted {@link List}.
   * @param key The value to match.
   * @return The closest index of the sorted {@link List} matching the desired value.
   * @throws IllegalArgumentException If {@code a} is null.
   */
  public static <T extends Comparable<? super T>>int binaryClosestSearch(final List<? extends T> a, final T key) {
    assertNotNull(a);
    return binaryClosestSearch0(a, 0, a.size(), key);
  }

  /**
   * Find the index of the sorted {@link List} whose value most closely matches the value provided.
   *
   * @param <T> The type parameter of the Comparable key object.
   * @param a The sorted {@link List}.
   * @param fromIndex The starting index of the sorted {@link List} to search from.
   * @param toIndex The ending index of the sorted {@link List} to search to.
   * @param key The value to match.
   * @return The closest index of the sorted {@link List} matching the desired value.
   * @throws IllegalArgumentException If {@code a} is null.
   */
  public static <T extends Comparable<? super T>>int binaryClosestSearch(final List<? extends T> a, final int fromIndex, final int toIndex, final T key) {
    assertNotNull(a);
    assertRangeArray(fromIndex, toIndex, a.size());
    return binaryClosestSearch0(a, fromIndex, toIndex, key);
  }

  /**
   * Find the index of the sorted {@link List} whose value most closely matches the value provided.
   *
   * @param <T> The type parameter of the key object.
   * @param a The sorted {@link List}.
   * @param key The value to match.
   * @param c The {@link Comparator} for {@code key} of type {@code <T>}.
   * @return The closest index of the sorted {@link List} matching the desired value.
   * @throws IllegalArgumentException If {@code a} or {@code c} is null.
   */
  public static <T>int binaryClosestSearch(final List<? extends T> a, final T key, final Comparator<? super T> c) {
    assertNotNull(a);
    return binaryClosestSearch0(a, 0, a.size(), key, c);
  }

  /**
   * Find the index of the sorted {@link List} whose value most closely matches the value provided.
   *
   * @param <T> The type parameter of the key object.
   * @param a The sorted {@link List}.
   * @param fromIndex The starting index of the sorted {@link List} to search from.
   * @param toIndex The ending index of the sorted {@link List} to search to.
   * @param key The value to match.
   * @param c The {@link Comparator} for {@code key} of type {@code <T>}.
   * @return The closest index of the sorted {@link List} matching the desired value.
   * @throws IllegalArgumentException If {@code a} or {@code c} is null.
   */
  public static <T>int binaryClosestSearch(final List<? extends T> a, final int fromIndex, final int toIndex, final T key, final Comparator<? super T> c) {
    assertNotNull(a);
    assertRangeArray(fromIndex, toIndex, a.size());
    return binaryClosestSearch0(a, fromIndex, toIndex, key, c);
  }

  private static <T extends Comparable<? super T>>int binaryClosestSearch0(final List<? extends T> a, int from, int to, final T key) {
    for (int mid; from < to;) { // [N]
      mid = (from + to) / 2;
      final int c = key.compareTo(a.get(mid));
      if (c < 0)
        to = mid;
      else if (c > 0)
        from = mid + 1;
      else
        return mid;
    }

    return (from + to) / 2;
  }

  private static <T>int binaryClosestSearch0(final List<? extends T> a, int from, int to, final T key, final Comparator<? super T> comparator) {
    for (int mid; from < to;) { // [N]
      mid = (from + to) / 2;
      final int c = comparator.compare(key, a.get(mid));
      if (c < 0)
        to = mid;
      else if (c > 0)
        from = mid + 1;
      else
        return mid;
    }

    return (from + to) / 2;
  }

  /**
   * Find the index of the sorted {@link ArrayIntList} whose value most closely matches the value provided. The returned index will
   * be less than or equal to an exact match.
   *
   * @param a The sorted {@link ArrayIntList}.
   * @param key The value to match.
   * @return The closest index of the sorted {@link ArrayIntList} matching the desired value. The returned index will be less than
   *         or equal to an exact match.
   * @throws IllegalArgumentException If the specified {@link ArrayIntList} is null.
   */
  public static int binaryClosestSearch(final ArrayIntList a, final int key) {
    assertNotNull(a);
    return binaryClosestSearch0(a, 0, a.size(), key, Integer::compare);
  }

  /**
   * Find the index of the sorted {@link ArrayIntList} whose value most closely matches the value provided. The returned index will
   * be less than or equal to an exact match.
   *
   * @param a The sorted {@link ArrayIntList}.
   * @param key The value to match.
   * @param c The comparator to use.
   * @return The closest index of the sorted {@link ArrayIntList} matching the desired value. The returned index will be less than
   *         or equal to an exact match.
   * @throws IllegalArgumentException If the specified {@link ArrayIntList} is null.
   */
  public static int binaryClosestSearch(final ArrayIntList a, final int key, final IntComparator c) {
    assertNotNull(a);
    return binaryClosestSearch0(a, 0, a.size(), key, c);
  }

  /**
   * Find the index of the sorted {@link ArrayIntList} whose value most closely matches the value provided. The returned index will
   * be less than or equal to an exact match.
   *
   * @param a The sorted {@link ArrayIntList}.
   * @param fromIndex The starting index of the {@link ArrayIntList} to search from.
   * @param toIndex The ending index of the {@link ArrayIntList} to search to.
   * @param key The value to match.
   * @return The closest index of the {@link ArrayIntList} matching the desired value. The returned index will be less than or equal
   *         to an exact match.
   * @throws IllegalArgumentException If the specified {@link ArrayIntList} is null.
   */
  public static int binaryClosestSearch(final ArrayIntList a, final int fromIndex, final int toIndex, final int key) {
    assertNotNull(a);
    assertRangeArray(fromIndex, toIndex, a.size());
    return binaryClosestSearch0(a, fromIndex, toIndex, key, Integer::compare);
  }

  /**
   * Find the index of the sorted {@link ArrayIntList} whose value most closely matches the value provided. The returned index will
   * be less than or equal to an exact match.
   *
   * @param a The sorted {@link ArrayIntList}.
   * @param fromIndex The starting index of the {@link ArrayIntList} to search from.
   * @param toIndex The ending index of the {@link ArrayIntList} to search to.
   * @param key The value to match.
   * @param c The comparator to use.
   * @return The closest index of the {@link ArrayIntList} matching the desired value. The returned index will be less than or equal
   *         to an exact match.
   * @throws IllegalArgumentException If the specified array is null.
   */
  public static int binaryClosestSearch(final ArrayIntList a, final int fromIndex, final int toIndex, final int key, final IntComparator c) {
    assertNotNull(a);
    assertRangeArray(fromIndex, toIndex, a.size());
    return binaryClosestSearch0(a, fromIndex, toIndex, key, c);
  }

  private static int binaryClosestSearch0(final ArrayIntList a, int fromIndex, int toIndex, final int key, final IntComparator c) {
    for (int mid, com; fromIndex < toIndex;) { // [N]
      mid = (fromIndex + toIndex) / 2;
      com = c.compare(key, a.get(mid));
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
   * Find the index of the sorted {@link ArrayLongList} whose value most closely matches the value provided. The returned index will
   * be less than or equal to an exact match.
   *
   * @param a The sorted {@link ArrayLongList}.
   * @param key The value to match.
   * @return The closest index of the sorted {@link ArrayLongList} matching the desired value. The returned index will be less than
   *         or equal to an exact match.
   * @throws IllegalArgumentException If the specified {@link ArrayLongList} is null.
   */
  public static int binaryClosestSearch(final ArrayLongList a, final long key) {
    assertNotNull(a);
    return binaryClosestSearch0(a, 0, a.size(), key, Long::compare);
  }

  /**
   * Find the index of the sorted {@link ArrayLongList} whose value most closely matches the value provided. The returned index will
   * be less than or equal to an exact match.
   *
   * @param a The sorted {@link ArrayLongList}.
   * @param key The value to match.
   * @param c The comparator to use.
   * @return The closest index of the sorted {@link ArrayLongList} matching the desired value. The returned index will be less than
   *         or equal to an exact match.
   * @throws IllegalArgumentException If the specified {@link ArrayLongList} is null.
   */
  public static int binaryClosestSearch(final ArrayLongList a, final long key, final LongComparator c) {
    assertNotNull(a);
    return binaryClosestSearch0(a, 0, a.size(), key, c);
  }

  /**
   * Find the index of the sorted {@link ArrayLongList} whose value most closely matches the value provided. The returned index will
   * be less than or equal to an exact match. The returned index will be less than or equal to an exact match.
   *
   * @param a The sorted {@link ArrayLongList}.
   * @param fromIndex The starting index of the {@link ArrayLongList} to search from.
   * @param toIndex The ending index of the {@link ArrayLongList} to search to.
   * @param key The value to match.
   * @return The closest index of the {@link ArrayLongList} matching the desired value. The returned index will be less than or
   *         equal to an exact match.
   * @throws IllegalArgumentException If the specified {@link ArrayLongList} is null.
   */
  public static int binaryClosestSearch(final ArrayLongList a, final int fromIndex, final int toIndex, final long key) {
    assertNotNull(a);
    assertRangeArray(fromIndex, toIndex, a.size());
    return binaryClosestSearch0(a, fromIndex, toIndex, key, Long::compare);
  }

  /**
   * Find the index of the sorted {@link ArrayLongList} whose value most closely matches the value provided. The returned index will
   * be less than or equal to an exact match.
   *
   * @param a The sorted {@link ArrayLongList}.
   * @param fromIndex The starting index of the {@link ArrayLongList} to search from.
   * @param toIndex The ending index of the {@link ArrayLongList} to search to.
   * @param key The value to match.
   * @param c The comparator to use.
   * @return The closest index of the {@link ArrayLongList} matching the desired value. The returned index will be less than or
   *         equal to an exact match.
   * @throws IllegalArgumentException If the specified array is null.
   */
  public static int binaryClosestSearch(final ArrayLongList a, final int fromIndex, final int toIndex, final long key, final LongComparator c) {
    assertNotNull(a);
    assertRangeArray(fromIndex, toIndex, a.size());
    return binaryClosestSearch0(a, fromIndex, toIndex, key, c);
  }

  private static int binaryClosestSearch0(final ArrayLongList a, int fromIndex, int toIndex, final long key, final LongComparator c) {
    for (int mid, com; fromIndex < toIndex;) { // [N]
      mid = (fromIndex + toIndex) / 2;
      com = c.compare(key, a.get(mid));
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
   * Find the index of the sorted {@link ArrayFloatList} whose value most closely matches the value provided. The returned index
   * will be less than or equal to an exact match.
   *
   * @param a The sorted {@link ArrayFloatList}.
   * @param key The value to match.
   * @return The closest index of the sorted {@link ArrayFloatList} matching the desired value. The returned index will be less than
   *         or equal to an exact match.
   * @throws IllegalArgumentException If the specified {@link ArrayFloatList} is null.
   */
  public static int binaryClosestSearch(final ArrayFloatList a, final float key) {
    assertNotNull(a);
    return binaryClosestSearch0(a, 0, a.size(), key, Float::compare);
  }

  /**
   * Find the index of the sorted {@link ArrayFloatList} whose value most closely matches the value provided. The returned index
   * will be less than or equal to an exact match.
   *
   * @param a The sorted {@link ArrayFloatList}.
   * @param key The value to match.
   * @param c The comparator to use.
   * @return The closest index of the sorted {@link ArrayFloatList} matching the desired value. The returned index will be less than
   *         or equal to an exact match.
   * @throws IllegalArgumentException If the specified {@link ArrayFloatList} is null.
   */
  public static int binaryClosestSearch(final ArrayFloatList a, final float key, final FloatComparator c) {
    assertNotNull(a);
    return binaryClosestSearch0(a, 0, a.size(), key, c);
  }

  /**
   * Find the index of the sorted {@link ArrayFloatList} whose value most closely matches the value provided. The returned index
   * will be less than or equal to an exact match.
   *
   * @param a The sorted {@link ArrayFloatList}.
   * @param fromIndex The starting index of the {@link ArrayFloatList} to search from.
   * @param toIndex The ending index of the {@link ArrayFloatList} to search to.
   * @param key The value to match.
   * @return The closest index of the {@link ArrayFloatList} matching the desired value. The returned index will be less than or
   *         equal to an exact match.
   * @throws IllegalArgumentException If the specified {@link ArrayFloatList} is null.
   */
  public static int binaryClosestSearch(final ArrayFloatList a, final int fromIndex, final int toIndex, final float key) {
    assertNotNull(a);
    assertRangeArray(fromIndex, toIndex, a.size());
    return binaryClosestSearch0(a, fromIndex, toIndex, key, Float::compare);
  }

  /**
   * Find the index of the sorted {@link ArrayFloatList} whose value most closely matches the value provided. The returned index
   * will be less than or equal to an exact match.
   *
   * @param a The sorted {@link ArrayFloatList}.
   * @param fromIndex The starting index of the {@link ArrayFloatList} to search from.
   * @param toIndex The ending index of the {@link ArrayFloatList} to search to.
   * @param key The value to match.
   * @param c The comparator to use.
   * @return The closest index of the {@link ArrayFloatList} matching the desired value. The returned index will be less than or
   *         equal to an exact match.
   * @throws IllegalArgumentException If the specified array is null.
   */
  public static int binaryClosestSearch(final ArrayFloatList a, final int fromIndex, final int toIndex, final float key, final FloatComparator c) {
    assertNotNull(a);
    assertRangeArray(fromIndex, toIndex, a.size());
    return binaryClosestSearch0(a, fromIndex, toIndex, key, c);
  }

  private static int binaryClosestSearch0(final ArrayFloatList a, int fromIndex, int toIndex, final float key, final FloatComparator c) {
    for (int mid, com; fromIndex < toIndex;) { // [N]
      mid = (fromIndex + toIndex) / 2;
      com = c.compare(key, a.get(mid));
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
   * Find the index of the sorted {@link ArrayDoubleList} whose value most closely matches the value provided. The returned index
   * will be less than or equal to an exact match.
   *
   * @param a The sorted {@link ArrayDoubleList}.
   * @param key The value to match.
   * @return The closest index of the sorted {@link ArrayDoubleList} matching the desired value. The returned index will be less
   *         than or equal to an exact match.
   * @throws IllegalArgumentException If the specified {@link ArrayDoubleList} is null.
   */
  public static int binaryClosestSearch(final ArrayDoubleList a, final double key) {
    assertNotNull(a);
    return binaryClosestSearch0(a, 0, a.size(), key, Double::compare);
  }

  /**
   * Find the index of the sorted {@link ArrayDoubleList} whose value most closely matches the value provided. The returned index
   * will be less than or equal to an exact match.
   *
   * @param a The sorted {@link ArrayDoubleList}.
   * @param key The value to match.
   * @param c The comparator to use.
   * @return The closest index of the sorted {@link ArrayDoubleList} matching the desired value. The returned index will be less
   *         than or equal to an exact match.
   * @throws IllegalArgumentException If the specified {@link ArrayDoubleList} is null.
   */
  public static int binaryClosestSearch(final ArrayDoubleList a, final double key, final DoubleComparator c) {
    assertNotNull(a);
    return binaryClosestSearch0(a, 0, a.size(), key, c);
  }

  /**
   * Find the index of the sorted {@link ArrayDoubleList} whose value most closely matches the value provided. The returned index
   * will be less than or equal to an exact match.
   *
   * @param a The sorted {@link ArrayDoubleList}.
   * @param fromIndex The starting index of the {@link ArrayDoubleList} to search from.
   * @param toIndex The ending index of the {@link ArrayDoubleList} to search to.
   * @param key The value to match.
   * @return The closest index of the {@link ArrayDoubleList} matching the desired value. The returned index will be less than or
   *         equal to an exact match.
   * @throws IllegalArgumentException If the specified {@link ArrayDoubleList} is null.
   */
  public static int binaryClosestSearch(final ArrayDoubleList a, final int fromIndex, final int toIndex, final double key) {
    assertNotNull(a);
    assertRangeArray(fromIndex, toIndex, a.size());
    return binaryClosestSearch0(a, fromIndex, toIndex, key, Double::compare);
  }

  /**
   * Find the index of the sorted {@link ArrayDoubleList} whose value most closely matches the value provided. The returned index
   * will be less than or equal to an exact match.
   *
   * @param a The sorted {@link ArrayDoubleList}.
   * @param fromIndex The starting index of the {@link ArrayDoubleList} to search from.
   * @param toIndex The ending index of the {@link ArrayDoubleList} to search to.
   * @param key The value to match.
   * @param c The comparator to use.
   * @return The closest index of the {@link ArrayDoubleList} matching the desired value. The returned index will be less than or
   *         equal to an exact match.
   * @throws IllegalArgumentException If the specified array is null.
   */
  public static int binaryClosestSearch(final ArrayDoubleList a, final int fromIndex, final int toIndex, final double key, final DoubleComparator c) {
    assertNotNull(a);
    assertRangeArray(fromIndex, toIndex, a.size());
    return binaryClosestSearch0(a, fromIndex, toIndex, key, c);
  }

  private static int binaryClosestSearch0(final ArrayDoubleList a, int fromIndex, int toIndex, final double key, final DoubleComparator c) {
    for (int mid, com; fromIndex < toIndex;) { // [N]
      mid = (fromIndex + toIndex) / 2;
      com = c.compare(key, a.get(mid));
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
   * Returns the specified collection with the provided parameters array added as members to the collection.
   *
   * @param <C> The type of the collection.
   * @param <T> The type of collection elements.
   * @param c The collection.
   * @param a The members to add to the collection.
   * @return The specified collection with the specified vararg parameters added as members to the collection.
   * @throws IllegalArgumentException If {@code c} or {@code a} is null.
   */
  public static <C extends Collection<T>,T>C asCollection(final C c, final T[] a) {
    assertNotNull(a);
    for (int i = 0, i$ = a.length; i < i$; ++i) // [A]
      c.add(a[i]);

    return c;
  }

  /**
   * Returns the specified collection with the provided vararg parameters added as members to the collection.
   *
   * @param <C> The type of the collection.
   * @param <T> The type of collection elements.
   * @param c The collection.
   * @param a0 The first member to add to the collection.
   * @param aN The remaining members to add to the collection.
   * @return The specified collection with the specified vararg parameters added as members to the collection.
   * @throws IllegalArgumentException If {@code c}, {@code a0}, or {@code aN} is null.
   */
  @SafeVarargs
  public static <C extends Collection<T>,T>C asCollection(final C c, final T a0, final T ... aN) {
    assertNotNull(c);
    assertNotNull(a0);
    assertNotNull(aN);
    c.add(a0);
    Collections.addAll(c, aN);
    return c;
  }

  /**
   * Returns the specified collection with the provided array of members added to the collection.
   *
   * @param <C> The type of the collection.
   * @param <T> The type of collection elements.
   * @param c The collection.
   * @param a The members to add to the collection.
   * @param fromIndex The index of the first member (inclusive) to be added.
   * @param toIndex The index of the last member (exclusive) to be added.
   * @return The specified collection with the specified vararg parameters added as members to the collection.
   * @throws ArrayIndexOutOfBoundsException If {@code fromIndex} is less than 0, or if {@code toIndex} is greater than the length of
   *           the specified array.
   * @throws IllegalArgumentException If {@code c} or {@code a} is null, or if {@code fromIndex} is greater than {@code toIndex}.
   */
  public static <C extends Collection<T>,T>C asCollection(final C c, final T[] a, final int fromIndex, final int toIndex) {
    assertNotNull(a);
    assertRangeArray(fromIndex, toIndex, a.length);
    for (int i = fromIndex; i < toIndex; ++i) // [A]
      c.add(a[i]);

    return c;
  }

  /**
   * Returns the specified target collection with the elements of the specified vararg parameter collections concatenated as members
   * to the target collection.
   *
   * @param <C> The type of the collection.
   * @param <T> The type of collection elements.
   * @param target The target collection.
   * @param collections The collections of members to concatenate to the target collection.
   * @return The specified target collection with the elements of the specified vararg parameter collections concatenated as members
   *         to the target collection.
   * @throws IllegalArgumentException If {@code target} or {@code collections} is null.
   */
  @SafeVarargs
  public static <C extends Collection<T>,T>C concat(final C target, final Collection<? extends T> ... collections) {
    assertNotNull(target);
    assertNotNull(collections);
    for (final Collection<? extends T> collection : collections) // [A]
      target.addAll(collection);

    return target;
  }

  /**
   * Returns an array of sublists of the specified list partitioned to the specified size. The last sublist member of the resulting
   * array will contain the divisor remainder of elements ranging from size of {@code 1} to {@code size}.
   *
   * @param <T> The type of list elements.
   * @param list The list to partition.
   * @param size The size of each partition.
   * @return An array of sublists of the specified list partitioned to the specified size.
   * @throws IllegalArgumentException If {@code list} is null, or if {@code size} is less than or equal to 0.
   */
  @SuppressWarnings("unchecked")
  public static <T>List<T>[] partition(final List<T> list, final int size) {
    assertNotNull(list);
    if (size <= 0)
      throw new IllegalArgumentException("Size must be positive: " + size);

    final int parts = list.size() / size;
    final int remainder = list.size() % size;
    final List<T>[] partitions = new List[remainder != 0 ? parts + 1 : parts];
    for (int i = 0; i < parts; ++i) // [A]
      partitions[i] = list.subList(i * size, (i + 1) * size);

    if (remainder != 0)
      partitions[partitions.length - 1] = list.subList(parts * size, list.size());

    return partitions;
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of the array in the second argument.
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
   * @param data The {@link List} providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @throws IllegalArgumentException If {@code data} or {@code order} is null, or if {@code data.size() != order.length}.
   */
  public static void sort(final List<?> data, final byte[] order) {
    sort(data, order, ByteComparator.NATURAL);
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of the array in the second argument.
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
   * @param data The {@link List} providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @param comparator The comparator to use.
   * @throws IllegalArgumentException If {@code data} or {@code order} is null, or if {@code data.size() != order.length}.
   */
  public static void sort(final List<?> data, final byte[] order, final ByteComparator comparator) {
    if (assertNotNull(data).size() != assertNotNull(order).length)
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.length [" + order.length + "] must be equal");

    assertNotNull(comparator);
    final int[] idx = PrimitiveSort.buildIndex(order.length);
    PrimitiveSort.sortIndexed(data, order, idx, (o1, o2) -> comparator.compare(order[o1], order[o2]));
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of the array in the second argument.
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
   * @param data The {@link List} providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @throws IllegalArgumentException If {@code data} or {@code order} is null, or if {@code data.size() != order.length}.
   */
  public static void sort(final List<?> data, final char[] order) {
    sort(data, order, CharComparator.NATURAL);
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of the array in the second argument.
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
   * @param data The {@link List} providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @param comparator The comparator to use.
   * @throws IllegalArgumentException If {@code data} or {@code order} is null, or if {@code data.size() != order.length}.
   */
  public static void sort(final List<?> data, final char[] order, final CharComparator comparator) {
    if (assertNotNull(data).size() != assertNotNull(order).length)
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.length [" + order.length + "] must be equal");

    assertNotNull(comparator);
    final int[] idx = PrimitiveSort.buildIndex(order.length);
    PrimitiveSort.sortIndexed(data, order, idx, (o1, o2) -> comparator.compare(order[o1], order[o2]));
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of the array in the second argument.
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
   * @param data The {@link List} providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @throws IllegalArgumentException If {@code data} or {@code order} is null, or if {@code data.size() != order.length}.
   */
  public static void sort(final List<?> data, final short[] order) {
    sort(data, order, ShortComparator.NATURAL);
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of the array in the second argument.
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
   * @param data The {@link List} providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @param comparator The comparator to use.
   * @throws IllegalArgumentException If {@code data} or {@code order} is null, or if {@code data.size() != order.length}.
   */
  public static void sort(final List<?> data, final short[] order, final ShortComparator comparator) {
    if (assertNotNull(data).size() != assertNotNull(order).length)
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.length [" + order.length + "] must be equal");

    assertNotNull(comparator);
    final int[] idx = PrimitiveSort.buildIndex(order.length);
    PrimitiveSort.sortIndexed(data, order, idx, (o1, o2) -> comparator.compare(order[o1], order[o2]));
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of the array in the second argument.
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
   * @param data The {@link List} providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @throws IllegalArgumentException If {@code data} or {@code order} is null, or if {@code data.size() != order.length}.
   */
  public static void sort(final List<?> data, final int[] order) {
    sort(data, order, IntComparator.NATURAL);
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of the array in the second argument.
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
   * @param data The {@link List} providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @param comparator The comparator to use.
   * @throws IllegalArgumentException If {@code data} or {@code order} is null, or if {@code data.size() != order.length}.
   */
  public static void sort(final List<?> data, final int[] order, final IntComparator comparator) {
    if (assertNotNull(data).size() != assertNotNull(order).length)
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.length [" + order.length + "] must be equal");

    assertNotNull(comparator);
    final int[] idx = PrimitiveSort.buildIndex(order.length);
    PrimitiveSort.sortIndexed(data, order, idx, (o1, o2) -> comparator.compare(order[o1], order[o2]));
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of the array in the second argument.
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
   * @param data The {@link List} providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @throws IllegalArgumentException If {@code data} or {@code order} is null, or if {@code data.size() != order.length}.
   */
  public static void sort(final List<?> data, final long[] order) {
    sort(data, order, LongComparator.NATURAL);
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of the array in the second argument.
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
   * @param data The {@link List} providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @param comparator The comparator to use.
   * @throws IllegalArgumentException If {@code data} or {@code order} is null, or if {@code data.size() != order.length}.
   */
  public static void sort(final List<?> data, final long[] order, final LongComparator comparator) {
    if (assertNotNull(data).size() != assertNotNull(order).length)
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.length [" + order.length + "] must be equal");

    assertNotNull(comparator);
    final int[] idx = PrimitiveSort.buildIndex(order.length);
    PrimitiveSort.sortIndexed(data, order, idx, (o1, o2) -> comparator.compare(order[o1], order[o2]));
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of the array in the second argument.
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
   * @param data The {@link List} providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @throws IllegalArgumentException If {@code data} or {@code order} is null, or if {@code data.size() != order.length}.
   */
  public static void sort(final List<?> data, final float[] order) {
    sort(data, order, FloatComparator.NATURAL);
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of the array in the second argument.
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
   * @param data The {@link List} providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @param comparator The comparator to use.
   * @throws IllegalArgumentException If {@code data} or {@code order} is null, or if {@code data.size() != order.length}.
   */
  public static void sort(final List<?> data, final float[] order, final FloatComparator comparator) {
    if (assertNotNull(data).size() != assertNotNull(order).length)
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.length [" + order.length + "] must be equal");

    assertNotNull(comparator);
    final int[] idx = PrimitiveSort.buildIndex(order.length);
    PrimitiveSort.sortIndexed(data, order, idx, (o1, o2) -> comparator.compare(order[o1], order[o2]));
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of the array in the second argument.
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
   * @param data The {@link List} providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @throws IllegalArgumentException If {@code data} or {@code order} is null, or if {@code data.size() != order.length}.
   */
  public static void sort(final List<?> data, final double[] order) {
    sort(data, order, DoubleComparator.NATURAL);
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of the array in the second argument.
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
   * @param data The {@link List} providing the data.
   * @param order The array providing the order of indices to sort {@code data}.
   * @param comparator The comparator to use.
   * @throws IllegalArgumentException If {@code data} or {@code order} is null, or if {@code data.size() != order.length}.
   */
  public static void sort(final List<?> data, final double[] order, final DoubleComparator comparator) {
    if (assertNotNull(data).size() != assertNotNull(order).length)
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.length [" + order.length + "] must be equal");

    assertNotNull(comparator);
    final int[] idx = PrimitiveSort.buildIndex(order.length);
    PrimitiveSort.sortIndexed(data, order, idx, (o1, o2) -> comparator.compare(order[o1], order[o2]));
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of the {@link List} of {@link Comparable} objects in the
   * second argument.
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
   * @param data The {@link List} providing the data.
   * @param order The {@link List} of {@link Comparable} objects providing the order of indices to sort {@code data}.
   * @throws IllegalArgumentException If {@code data} or {@code order} is null, or if {@code data.size() != order.length}.
   */
  public static <T extends Comparable<? super T>>void sort(final List<?> data, final T[] order) {
    sort(data, order, (o1, o2) -> o1 == null ? o2 == null ? 0 : -1 : o2 == null ? 1 : o1.compareTo(o2));
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of the {@link List} in the second argument.
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
   * @param data The {@link List} providing the data.
   * @param order The {@link List} providing the order of indices to sort {@code data}.
   * @param comparator The {@link Comparator} for members of {@code order}.
   * @throws IllegalArgumentException If {@code data}, {@code order} or {@code comparator} is null, or if
   *           {@code data.size() != order.length}.
   */
  public static <T>void sort(final List<?> data, final T[] order, final Comparator<? super T> comparator) {
    if (assertNotNull(data).size() != assertNotNull(order).length)
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.length [" + order.length + "] must be equal");

    sortIndexed(data, order, buildIndex(order.length), (o1, o2) -> comparator.compare(order[o1], order[o2]));
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of the {@link List} of {@link Comparable} objects in the
   * second argument.
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
   * @param data The {@link List} providing the data.
   * @param order The {@link List} of {@link Comparable} objects providing the order of indices to sort {@code data}.
   * @throws IllegalArgumentException If {@code data} or {@code order} is null, or if {@code data.size() != order.size()}.
   */
  public static <T extends Comparable<? super T>>void sort(final List<?> data, final List<T> order) {
    sort(data, order, (o1, o2) -> o1 == null ? o2 == null ? 0 : -1 : o2 == null ? 1 : o1.compareTo(o2));
  }

  /**
   * Sorts the {@link List} in the first argument matching the sorted order of the {@link List} in the second argument.
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
   * @param data The {@link List} providing the data.
   * @param order The {@link List} providing the order of indices to sort {@code data}.
   * @param comparator The {@link Comparator} for members of {@code order}.
   * @throws IllegalArgumentException If {@code data}, {@code order} or {@code comparator} is null, or if
   *           {@code data.size() != order.size()}.
   */
  public static <T>void sort(final List<?> data, final List<? extends T> order, final Comparator<? super T> comparator) {
    if (assertNotNull(data).size() != assertNotNull(order).size())
      throw new IllegalArgumentException("data.size() [" + data.size() + "] and order.size() [" + order.size() + "] must be equal");

    sortIndexed(data, order, buildIndex(order.size()), (o1, o2) -> comparator.compare(order.get(o1), order.get(o2)));
  }

  private static final <T>int dedupe(final List<T> a, final int len, final int index, final int depth, final Comparator<? super T> c) {
    if (index == len)
      return depth;

    final T element = a.get(index);
    if (c.compare(a.get(index - 1), element) == 0)
      return dedupe(a, len, index + 1, depth, c);

    final int length = dedupe(a, len, index + 1, depth + 1, c);
    a.set(depth, element);
    return length;
  }

  /**
   * Deduplicates the provided {@link List} by reordering the unique elements in ascending order specified by the given
   * {@link Comparator}, returning the number of unique elements.
   *
   * @param <T> The type parameter of the provided {@link List}.
   * @param l The list to dedupe.
   * @param c The {@link Comparator}.
   * @return The number of unique elements after having reordering the unique elements in ascending order specified by the given
   *         {@link Comparator}.
   * @throws IllegalArgumentException If the provided {@link List} or {@link Comparator} is null.
   */
  public static final <T>int dedupe(final List<T> l, final Comparator<? super T> c) {
    return assertNotNull(l).size() <= 1 ? l.size() : dedupe(l, l.size(), 1, 1, assertNotNull(c));
  }

  private CollectionUtil() {
  }
}