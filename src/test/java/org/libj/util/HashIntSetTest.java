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

import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.*;

import org.junit.Test;

public class HashIntSetTest {
  private static final int INITIAL_CAPACITY = 100;

  private final HashIntSet testSet = new HashIntSet(INITIAL_CAPACITY);

  @Test
  public void initiallyContainsNoElements() {
    for (int i = 0; i < 10000; ++i)
      assertFalse(testSet.contains(i));
  }

  @Test
  public void initiallyContainsNoBoxedElements() {
    for (int i = 0; i < 10000; ++i)
      assertFalse(testSet.contains(Integer.valueOf(i)));
  }

  @Test
  public void containsAddedElement() {
    assertTrue(testSet.add(1));
    assertTrue(testSet.contains(1));
  }

  @Test
  public void addingAnElementTwiceDoesNothing() {
    assertTrue(testSet.add(1));
    assertFalse(testSet.add(1));
  }

  @Test
  public void containsAddedBoxedElements() {
    assertTrue(testSet.add(1));
    assertTrue(testSet.add(Integer.valueOf(2)));

    assertTrue(testSet.contains(Integer.valueOf(1)));
    assertTrue(testSet.contains(2));
  }

  @Test
  public void removingAnElementFromAnEmptyListDoesNothing() {
    assertFalse(testSet.remove(0));
  }

  @Test
  public void removingAPresentElementRemovesIt() {
    assertTrue(testSet.add(1));
    assertTrue(testSet.remove(1));
    assertFalse(testSet.contains(1));
  }

  @Test
  public void sizeIsInitiallyZero() {
    assertEquals(0, testSet.size());
  }

  @Test
  public void sizeIncrementsWithNumberOfAddedElements() {
    addTwoElements(testSet);

    assertEquals(2, testSet.size());
  }

  @Test
  public void sizeContainsNumberOfNewElements() {
    testSet.add(1);
    testSet.add(1);

    assertEquals(1, testSet.size());
  }

  @Test
  public void iteratorsListElements() {
    addTwoElements(testSet);

    assertIteratorHasElements();
  }

  @Test
  public void iteratorsStartFromTheBeginningEveryTime() {
    iteratorsListElements();

    assertIteratorHasElements();
  }

  @Test
  public void iteratorsListElementsWithoutHasNext() {
    addTwoElements(testSet);

    assertIteratorHasElementsWithoutHasNext();
  }

  @Test
  public void iteratorsStartFromTheBeginningEveryTimeWithoutHasNext() {
    iteratorsListElementsWithoutHasNext();

    assertIteratorHasElementsWithoutHasNext();
  }

  @Test(expected = NoSuchElementException.class)
  public void iteratorsThrowNoSuchElementException() {
    addTwoElements(testSet);

    exhaustIterator();
  }

  @Test(expected = NoSuchElementException.class)
  public void iteratorsThrowNoSuchElementExceptionFromTheBeginningEveryTime() {
    addTwoElements(testSet);

    try {
      exhaustIterator();
    }
    catch (final NoSuchElementException ignore) {
    }

    exhaustIterator();
  }

  @Test
  public void iteratorHasNoElements() {
    assertFalse(testSet.iterator().hasNext());
  }

  @Test(expected = NoSuchElementException.class)
  public void iteratorThrowExceptionForEmptySet() {
    testSet.iterator().next();
  }

  @Test
  public void clearRemovesAllElementsOfTheSet() {
    addTwoElements(testSet);

    testSet.clear();

    assertEquals(0, testSet.size());
    assertFalse(testSet.contains(1));
    assertFalse(testSet.contains(1001));
  }

  @Test
  public void differenceReturnsNullIfBothSetsEqual() {
    addTwoElements(testSet);

    final HashIntSet other = new HashIntSet(100);
    addTwoElements(other);

    other.removeAll(testSet);
    assertEquals(0, other.size());
  }

  @Test
  public void differenceReturnsSetDifference() {
    addTwoElements(testSet);

    final HashIntSet other = new HashIntSet(100);
    other.add(1);

    testSet.removeAll(other);
    assertTrue(testSet.contains(1001));
  }

  @Test
  public void copiesOtherHashIntSet() {
    addTwoElements(testSet);

    final HashIntSet other = new HashIntSet(testSet);
    assertContainsElements(other);
  }

  @Test
  public void twoEmptySetsAreEqual() {
    assertEquals(testSet, new HashIntSet(100));
  }

  @Test
  public void setsWithTheSameValuesAreEqual() {
    final HashIntSet that = new HashIntSet(100);

    addTwoElements(testSet);
    addTwoElements(that);

    assertEquals(testSet, that);
  }

  @Test
  public void setsWithTheDifferentSizesAreNotEqual() {
    final HashIntSet that = new HashIntSet(100);

    addTwoElements(testSet);
    that.add(1001);

    assertNotEquals(testSet, that);
  }

  @Test
  public void setsWithTheDifferentValuesAreNotEqual() {
    final HashIntSet that = new HashIntSet(100);

    addTwoElements(testSet);
    that.add(2);
    that.add(1001);

    assertNotEquals(testSet, that);
  }

  @Test
  public void twoEmptySetsHaveTheSameHashcode() {
    assertEquals(testSet.hashCode(), new HashIntSet(100).hashCode());
  }

  @Test
  public void setsWithTheSameValuesHaveTheSameHashcode() {
    final HashIntSet other = new HashIntSet(100);

    addTwoElements(testSet);
    addTwoElements(other);

    assertEquals(testSet.hashCode(), other.hashCode());
  }

  @Test
  public void reducesSizeWhenElementRemoved() {
    addTwoElements(testSet);
    testSet.remove(1001);

    assertEquals(1, testSet.size());
  }

  @Test(expected = NullPointerException.class)
  public void toArrayThrowsNullPointerExceptionForNullArgument() {
    final Integer[] a = null;
    testSet.toArray(a);
  }

  @Test
  public void toArrayCopiesElementsIntoSufficientlySizedArray() {
    addTwoElements(testSet);
    final Integer[] result = testSet.toArray(new Integer[testSet.size()]);

    assertArrayContainingElements(result);
  }

  @Test
  public void toArrayCopiesElementsIntoNewArray() {
    addTwoElements(testSet);
    final Integer[] result = testSet.toArray(new Integer[testSet.size()]);

    assertArrayContainingElements(result);
  }

  @Test
  public void toArraySupportsEmptyCollection() {
    final Integer[] result = testSet.toArray(new Integer[testSet.size()]);

    assertArrayEquals(result, new Integer[] {});
  }

  @Test
  public void chainCompactionShouldNotCauseElementsToBeMovedBeforeTheirHash() {
    final HashIntSet requiredFields = new HashIntSet(14);

    requiredFields.add(8);
    requiredFields.add(9);
    requiredFields.add(35);
    requiredFields.add(49);
    requiredFields.add(56);

    assertTrue("Failed to remove 8", requiredFields.remove(8));
    assertTrue("Failed to remove 9", requiredFields.remove(9));

    assertTrue(requiredFields.containsAll(Arrays.asList(35, 49, 56)));
  }

  @Test
  public void shouldResizeWhenItHitsCapacity() {
    for (int i = 0; i < 2 * INITIAL_CAPACITY; ++i)
      assertTrue(testSet.add(i));

    for (int i = 0; i < 2 * INITIAL_CAPACITY; ++i)
      assertTrue(testSet.contains(i));
  }

  @Test
  public void containsEmptySet() {
    assertTrue(testSet.containsAll(new HashIntSet(100)));
  }

  @Test
  public void containsSubset() {
    addTwoElements(testSet);

    final HashIntSet subset = new HashIntSet(100);
    subset.add(1);

    assertTrue(testSet.containsAll(subset));
  }

  @Test
  public void doesNotContainDisjointSet() {
    addTwoElements(testSet);

    final HashIntSet disjoint = new HashIntSet(100);
    disjoint.add(1);
    disjoint.add(1002);

    assertFalse(testSet.containsAll(disjoint));
  }

  @Test
  public void doesNotContainSuperset() {
    addTwoElements(testSet);

    final HashIntSet superset = new HashIntSet(100);
    addTwoElements(superset);
    superset.add(15);

    assertFalse(testSet.containsAll(superset));
  }

  @Test
  public void addingEmptySetDoesNothing() {
    addTwoElements(testSet);

    assertFalse(testSet.addAll(new HashIntSet(100)));
    assertFalse(testSet.addAll(new HashSet<>()));
    assertContainsElements(testSet);
  }

  @Test
  public void containsValuesAddedFromDisjointSetPrimitive() {
    addTwoElements(testSet);

    final HashIntSet disjoint = new HashIntSet(100);

    disjoint.add(2);
    disjoint.add(1002);

    assertTrue(testSet.addAll(disjoint));
    assertTrue(testSet.contains(1));
    assertTrue(testSet.contains(1001));
    assertTrue(testSet.containsAll(disjoint));
  }

  @Test
  public void containsValuesAddedFromDisjointSet() {
    addTwoElements(testSet);

    final HashSet<Integer> disjoint = new HashSet<>();

    disjoint.add(2);
    disjoint.add(1002);

    assertTrue(testSet.addAll(disjoint));
    assertTrue(testSet.contains(1));
    assertTrue(testSet.contains(1001));
    assertTrue(testSet.containsAll(disjoint));
  }

  @Test
  public void containsValuesAddedFromIntersectingSetPrimitive() {
    addTwoElements(testSet);

    final HashIntSet intersecting = new HashIntSet(100);
    intersecting.add(1);
    intersecting.add(1002);

    assertTrue(testSet.addAll(intersecting));
    assertTrue(testSet.contains(1));
    assertTrue(testSet.contains(1001));
    assertTrue(testSet.containsAll(intersecting));
  }

  @Test
  public void containsValuesAddedFromIntersectingSet() {
    addTwoElements(testSet);

    final HashSet<Integer> intersecting = new HashSet<>();

    intersecting.add(1);
    intersecting.add(1002);

    assertTrue(testSet.addAll(intersecting));
    assertTrue(testSet.contains(1));
    assertTrue(testSet.contains(1001));
    assertTrue(testSet.containsAll(intersecting));
  }

  @Test
  public void removingEmptySetDoesNothing() {
    addTwoElements(testSet);

    assertFalse(testSet.removeAll(new HashIntSet(100)));
    assertFalse(testSet.removeAll(new HashSet<>()));
    assertContainsElements(testSet);
  }

  @Test
  public void removingDisjointSetDoesNothing() {
    addTwoElements(testSet);

    final HashIntSet disjoint = new HashIntSet(100);
    disjoint.add(2);
    disjoint.add(1002);

    assertFalse(testSet.removeAll(disjoint));
    assertFalse(testSet.removeAll(new HashSet<>()));
    assertContainsElements(testSet);
  }

  @Test
  public void doesNotContainRemovedIntersectingSetPrimitive() {
    addTwoElements(testSet);

    final HashIntSet intersecting = new HashIntSet(100);

    intersecting.add(1);
    intersecting.add(1002);

    assertTrue(testSet.removeAll(intersecting));
    assertTrue(testSet.contains(1001));
    assertFalse(testSet.containsAll(intersecting));
  }

  @Test
  public void doesNotContainRemovedIntersectingSet() {
    addTwoElements(testSet);

    final HashSet<Integer> intersecting = new HashSet<>();
    intersecting.add(1);
    intersecting.add(1002);

    assertTrue(testSet.removeAll(intersecting));
    assertTrue(testSet.contains(1001));
    assertFalse(testSet.containsAll(intersecting));
  }

  @Test
  public void isEmptyAfterRemovingEqualSetPrimitive() {
    addTwoElements(testSet);

    final HashIntSet equal = new HashIntSet(100);
    addTwoElements(equal);

    assertTrue(testSet.removeAll(equal));
    assertTrue(testSet.isEmpty());
  }

  @Test
  public void isEmptyAfterRemovingEqualSet() {
    addTwoElements(testSet);

    final HashSet<Integer> equal = new HashSet<>();
    addTwoElements(equal);

    assertTrue(testSet.removeAll(equal));
    assertTrue(testSet.isEmpty());
  }

  @Test
  public void removeElementsFromIterator() {
    addTwoElements(testSet);

    final IntIterator iterator = testSet.iterator();
    while (iterator.hasNext())
      if (iterator.next() == 1)
        iterator.remove();

    assertEquals(1, testSet.size());
    assertTrue(testSet.contains(1001));
  }

  @Test
  public void shouldNotContainMissingValueInitially() {
    assertFalse(testSet.contains(HashIntSet.NULL));
  }

  @Test
  public void shouldAllowMissingValue() {
    assertTrue(testSet.add(HashIntSet.NULL));
    assertTrue(testSet.contains(HashIntSet.NULL));
    assertFalse(testSet.add(HashIntSet.NULL));
  }

  @Test
  public void shouldAllowRemovalOfMissingValue() {
    assertTrue(testSet.add(HashIntSet.NULL));
    assertTrue(testSet.remove(HashIntSet.NULL));
    assertFalse(testSet.contains(HashIntSet.NULL));
    assertFalse(testSet.remove(HashIntSet.NULL));
  }

  @Test
  public void sizeAccountsForMissingValue() {
    testSet.add(1);
    testSet.add(HashIntSet.NULL);

    assertEquals(2, testSet.size());
  }

  @Test
  public void toArrayCopiesElementsIntoNewArrayIncludingMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashIntSet.NULL);

    final Integer[] result = testSet.toArray(new Integer[testSet.size()]);
    assertTrue(Arrays.asList(result).containsAll(Arrays.asList(1, 1001, HashIntSet.NULL)));
  }

  @Test
  public void toObjectArrayCopiesElementsIntoNewArrayIncludingMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashIntSet.NULL);

    final int[] result = testSet.toArray();
    Arrays.sort(result);
    assertArrayEquals(new int[] {HashIntSet.NULL, 1, 1001}, result);
  }

  @Test
  public void equalsAccountsForMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashIntSet.NULL);

    final HashIntSet other = new HashIntSet(100);
    addTwoElements(other);

    assertNotEquals(testSet, other);

    other.add(HashIntSet.NULL);
    assertEquals(testSet, other);

    testSet.remove(HashIntSet.NULL);

    assertNotEquals(testSet, other);
  }

  @Test
  public void consecutiveValuesShouldBeCorrectlyStored() {
    for (int i = 0; i < 10000; ++i)
      testSet.add(i);

    assertEquals(10000, testSet.size());

    int distinctElements = 0;
    for (final IntIterator i = testSet.iterator(); i.hasNext(); i.next())
      ++distinctElements;

    assertEquals(distinctElements, 10000);
  }

  @Test
  public void hashCodeAccountsForMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashIntSet.NULL);

    final HashIntSet other = new HashIntSet(100);
    addTwoElements(other);

    other.add(HashIntSet.NULL);
    assertEquals(testSet.hashCode(), other.hashCode());
  }

  @Test
  public void iteratorAccountsForMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashIntSet.NULL);

    int missingValueCount = 0;
    final IntIterator iterator = testSet.iterator();
    while (iterator.hasNext())
      if (iterator.next() == HashIntSet.NULL)
        ++missingValueCount;

    assertEquals(1, missingValueCount);
  }

  @Test
  public void iteratorCanRemoveMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashIntSet.NULL);

    final IntIterator iterator = testSet.iterator();
    while (iterator.hasNext())
      if (iterator.next() == HashIntSet.NULL)
        iterator.remove();

    assertFalse(testSet.contains(HashIntSet.NULL));
  }

  @Test
  public void shouldGenerateStringRepresentation() {
    final int[] testEntries = {3, 1, -1, 19, 7, 11, 12, 7};
    for (final int testEntry : testEntries)
      testSet.add(testEntry);

    final String mapAsAString = "[1, 19, 11, 7, 3, -1, 12]";
    assertEquals(mapAsAString, testSet.toString());
  }

  @Test
  public void shouldRemoveMissingValueWhenCleared() {
    assertTrue(testSet.add(HashIntSet.NULL));

    testSet.clear();

    assertFalse(testSet.contains(HashIntSet.NULL));
  }

  @Test
  public void shouldHaveCompatibleEqualsAndHashcode() {
    final HashSet<Integer> compatibleSet = new HashSet<>();
    final long seed = System.nanoTime();
    final Random r = new Random(seed);
    for (int i = 0; i < 1024; ++i) {
      final int value = r.nextInt();
      compatibleSet.add(value);
      testSet.add(value);
    }

    if (r.nextBoolean()) {
      compatibleSet.add(HashIntSet.NULL);
      testSet.add(HashIntSet.NULL);
    }

    assertTrue("Fail with seed:" + seed, testSet.size() == compatibleSet.size() && testSet.containsAll(compatibleSet));
    assertEquals("Fail with seed:" + seed, compatibleSet.hashCode(), testSet.hashCode());
  }

  private static void addTwoElements(final HashIntSet obj) {
    obj.add(1);
    obj.add(1001);
  }

  private static void addTwoElements(final HashSet<Integer> obj) {
    obj.add(1);
    obj.add(1001);
  }

  private void assertIteratorHasElements() {
    final IntIterator iterator = testSet.iterator();

    final Set<Integer> values = new HashSet<>();

    assertTrue(iterator.hasNext());
    values.add(iterator.next());
    assertTrue(iterator.hasNext());
    values.add(iterator.next());
    assertFalse(iterator.hasNext());

    assertContainsElements(values);
  }

  private void assertIteratorHasElementsWithoutHasNext() {
    final IntIterator iterator = testSet.iterator();
    final Set<Integer> values = new HashSet<>();

    values.add(iterator.next());
    values.add(iterator.next());

    assertContainsElements(values);
  }

  private static void assertArrayContainingElements(final Integer[] result) {
    assertTrue(Arrays.asList(result).containsAll(Arrays.asList(1, 1001)));
  }

  private static void assertContainsElements(final Set<Integer> other) {
    assertTrue(other.containsAll(Arrays.asList(1, 1001)));
  }

  private static void assertContainsElements(final IntSet other) {
    assertTrue(other.containsAll(Arrays.asList(1, 1001)));
  }

  private void exhaustIterator() {
    final IntIterator iterator = testSet.iterator();
    iterator.next();
    iterator.next();
    iterator.next();
  }
}