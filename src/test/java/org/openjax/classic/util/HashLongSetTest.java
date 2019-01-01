/* Copyright (c) 2018 OpenJAX
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

package org.openjax.classic.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

public class HashLongSetTest {
  private static final int INITIAL_CAPACITY = 100;

  private final HashLongSet testSet = new HashLongSet(INITIAL_CAPACITY);

  @Test
  public void initiallyContainsNoElements() {
    for (int i = 0; i < 10000; ++i)
      assertFalse(testSet.contains(i));
  }

  @Test
  public void initiallyContainsNoBoxedElements() {
    for (int i = 0; i < 10000; ++i)
      assertFalse(testSet.contains(Long.valueOf(i)));
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
    assertTrue(testSet.add(Long.valueOf(2)));

    assertTrue(testSet.contains(Long.valueOf(1)));
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

    final HashLongSet other = new HashLongSet(100);
    addTwoElements(other);

    other.removeAll(testSet);
    assertEquals(0, other.size());
  }

  @Test
  public void differenceReturnsSetDifference() {
    addTwoElements(testSet);

    final HashLongSet other = new HashLongSet(100);
    other.add(1);

    testSet.removeAll(other);
    assertTrue(testSet.containsAll(Arrays.asList(1001l)));
  }

  @Test
  public void copiesOtherHashLongSet() {
    addTwoElements(testSet);

    final HashLongSet other = new HashLongSet(testSet);
    assertContainsElements(other);
  }

  @Test
  public void twoEmptySetsAreEqual() {
    assertEquals(testSet, new HashLongSet(100));
  }

  @Test
  public void setsWithTheSameValuesAreEqual() {
    final HashLongSet that = new HashLongSet(100);

    addTwoElements(testSet);
    addTwoElements(that);

    assertEquals(testSet, that);
  }

  @Test
  public void setsWithTheDifferentSizesAreNotEqual() {
    final HashLongSet that = new HashLongSet(100);

    addTwoElements(testSet);
    that.add(1001);

    assertNotEquals(testSet, that);
  }

  @Test
  public void setsWithTheDifferentValuesAreNotEqual() {
    final HashLongSet that = new HashLongSet(100);

    addTwoElements(testSet);
    that.add(2);
    that.add(1001);

    assertNotEquals(testSet, that);
  }

  @Test
  public void twoEmptySetsHaveTheSameHashcode() {
    assertEquals(testSet.hashCode(), new HashLongSet(100).hashCode());
  }

  @Test
  public void setsWithTheSameValuesHaveTheSameHashcode() {
    final HashLongSet other = new HashLongSet(100);

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
    final Long[] a = null;
    testSet.toArray(a);
  }

  @Test
  public void toArrayCopiesElementsLongoSufficientlySizedArray() {
    addTwoElements(testSet);
    final Long[] result = testSet.toArray(new Long[testSet.size()]);

    assertArrayContainingElements(result);
  }

  @Test
  public void toArrayCopiesElementsLongoNewArray() {
    addTwoElements(testSet);
    final Long[] result = testSet.toArray(new Long[testSet.size()]);

    assertArrayContainingElements(result);
  }

  @Test
  public void toArraySupportsEmptyCollection() {
    final Long[] result = testSet.toArray(new Long[testSet.size()]);

    assertArrayEquals(result, new Long[] {});
  }

  // Test case from usage bug.
  @Test
  public void chainCompactionShouldNotCauseElementsToBeMovedBeforeTheirHash() {
    final HashLongSet requiredFields = new HashLongSet(14);

    requiredFields.add(8);
    requiredFields.add(9);
    requiredFields.add(35);
    requiredFields.add(49);
    requiredFields.add(56);

    assertTrue("Failed to remove 8", requiredFields.remove(8));
    assertTrue("Failed to remove 9", requiredFields.remove(9));

    assertTrue(requiredFields.containsAll(Arrays.asList(35l, 49l, 56l)));
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
    assertTrue(testSet.containsAll(new HashLongSet(100)));
  }

  @Test
  public void containsSubset() {
    addTwoElements(testSet);

    final HashLongSet subset = new HashLongSet(100);
    subset.add(1);

    assertTrue(testSet.containsAll(subset));
  }

  @Test
  public void doesNotContainDisjointSet() {
    addTwoElements(testSet);

    final HashLongSet disjoint = new HashLongSet(100);
    disjoint.add(1);
    disjoint.add(1002);

    assertFalse(testSet.containsAll(disjoint));
  }

  @Test
  public void doesNotContainSuperset() {
    addTwoElements(testSet);

    final HashLongSet superset = new HashLongSet(100);
    addTwoElements(superset);
    superset.add(15);

    assertFalse(testSet.containsAll(superset));
  }

  @Test
  public void addingEmptySetDoesNothing() {
    addTwoElements(testSet);

    assertFalse(testSet.addAll(new HashLongSet(100)));
    assertFalse(testSet.addAll(new HashSet<>()));
    assertContainsElements(testSet);
  }

  @Test
  public void containsValuesAddedFromDisjointSetPrimitive() {
    addTwoElements(testSet);

    final HashLongSet disjoint = new HashLongSet(100);

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

    final HashSet<Long> disjoint = new HashSet<>();

    disjoint.add(2l);
    disjoint.add(1002l);

    assertTrue(testSet.addAll(disjoint));
    assertTrue(testSet.contains(1));
    assertTrue(testSet.contains(1001));
    assertTrue(testSet.containsAll(disjoint));
  }

  @Test
  public void containsValuesAddedFromLongersectingSetPrimitive() {
    addTwoElements(testSet);

    final HashLongSet intersecting = new HashLongSet(100);
    intersecting.add(1);
    intersecting.add(1002);

    assertTrue(testSet.addAll(intersecting));
    assertTrue(testSet.contains(1));
    assertTrue(testSet.contains(1001));
    assertTrue(testSet.containsAll(intersecting));
  }

  @Test
  public void containsValuesAddedFromLongersectingSet() {
    addTwoElements(testSet);

    final HashSet<Long> intersecting = new HashSet<>();

    intersecting.add(1l);
    intersecting.add(1002l);

    assertTrue(testSet.addAll(intersecting));
    assertTrue(testSet.contains(1));
    assertTrue(testSet.contains(1001));
    assertTrue(testSet.containsAll(intersecting));
  }

  @Test
  public void removingEmptySetDoesNothing() {
    addTwoElements(testSet);

    assertFalse(testSet.removeAll(new HashLongSet(100)));
    assertFalse(testSet.removeAll(new HashSet<>()));
    assertContainsElements(testSet);
  }

  @Test
  public void removingDisjointSetDoesNothing() {
    addTwoElements(testSet);

    final HashLongSet disjoint = new HashLongSet(100);
    disjoint.add(2);
    disjoint.add(1002);

    assertFalse(testSet.removeAll(disjoint));
    assertFalse(testSet.removeAll(new HashSet<>()));
    assertContainsElements(testSet);
  }

  @Test
  public void doesNotContainRemovedLongersectingSetPrimitive() {
    addTwoElements(testSet);

    final HashLongSet intersecting = new HashLongSet(100);

    intersecting.add(1);
    intersecting.add(1002);

    assertTrue(testSet.removeAll(intersecting));
    assertTrue(testSet.contains(1001));
    assertFalse(testSet.containsAll(intersecting));
  }

  @Test
  public void doesNotContainRemovedLongersectingSet() {
    addTwoElements(testSet);

    final HashSet<Long> intersecting = new HashSet<>();
    intersecting.add(1l);
    intersecting.add(1002l);

    assertTrue(testSet.removeAll(intersecting));
    assertTrue(testSet.contains(1001));
    assertFalse(testSet.containsAll(intersecting));
  }

  @Test
  public void isEmptyAfterRemovingEqualSetPrimitive() {
    addTwoElements(testSet);

    final HashLongSet equal = new HashLongSet(100);
    addTwoElements(equal);

    assertTrue(testSet.removeAll(equal));
    assertTrue(testSet.isEmpty());
  }

  @Test
  public void isEmptyAfterRemovingEqualSet() {
    addTwoElements(testSet);

    final HashSet<Long> equal = new HashSet<>();
    addTwoElements(equal);

    assertTrue(testSet.removeAll(equal));
    assertTrue(testSet.isEmpty());
  }

  @Test
  public void removeElementsFromIterator() {
    addTwoElements(testSet);

    final LongIterator iterator = testSet.iterator();
    while (iterator.hasNext())
      if (iterator.next() == 1)
        iterator.remove();

    assertEquals(1, testSet.size());
    assertTrue(testSet.containsAll(Arrays.asList(1001l)));
  }

  @Test
  public void shouldNotContainMissingValueInitially() {
    assertFalse(testSet.contains(HashLongSet.NULL));
  }

  @Test
  public void shouldAllowMissingValue() {
    assertTrue(testSet.add(HashLongSet.NULL));
    assertTrue(testSet.contains(HashLongSet.NULL));
    assertFalse(testSet.add(HashLongSet.NULL));
  }

  @Test
  public void shouldAllowRemovalOfMissingValue() {
    assertTrue(testSet.add(HashLongSet.NULL));
    assertTrue(testSet.remove(HashLongSet.NULL));
    assertFalse(testSet.contains(HashLongSet.NULL));
    assertFalse(testSet.remove(HashLongSet.NULL));
  }

  @Test
  public void sizeAccountsForMissingValue() {
    testSet.add(1);
    testSet.add(HashLongSet.NULL);

    assertEquals(2, testSet.size());
  }

  @Test
  public void toArrayCopiesElementsLongoNewArrayIncludingMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashLongSet.NULL);

    final Long[] result = testSet.toArray(new Long[testSet.size()]);
    assertTrue(Arrays.asList(result).containsAll(Arrays.asList(1l, 1001l, HashLongSet.NULL)));
  }

  @Test
  public void toObjectArrayCopiesElementsLongoNewArrayIncludingMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashLongSet.NULL);

    final long[] result = testSet.toArray();
    Arrays.sort(result);
    assertArrayEquals(new long[] {HashLongSet.NULL, 1, 1001}, result);
  }

  @Test
  public void equalsAccountsForMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashLongSet.NULL);

    final HashLongSet other = new HashLongSet(100);
    addTwoElements(other);

    assertNotEquals(testSet, other);

    other.add(HashLongSet.NULL);
    assertEquals(testSet, other);

    testSet.remove(HashLongSet.NULL);

    assertNotEquals(testSet, other);
  }

  @Test
  public void consecutiveValuesShouldBeCorrectlyStored() {
    for (int i = 0; i < 10000; ++i)
      testSet.add(i);

    assertEquals(10000, testSet.size());

    int distinctElements = 0;
    for (final LongIterator i = testSet.iterator(); i.hasNext(); i.next())
      distinctElements++;

    assertEquals(distinctElements, 10000);
  }

  @Test
  public void hashCodeAccountsForMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashLongSet.NULL);

    final HashLongSet other = new HashLongSet(100);
    addTwoElements(other);

    other.add(HashLongSet.NULL);
    assertEquals(testSet.hashCode(), other.hashCode());
  }

  @Test
  public void iteratorAccountsForMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashLongSet.NULL);

    int missingValueCount = 0;
    final LongIterator iterator = testSet.iterator();
    while (iterator.hasNext())
      if (iterator.next() == HashLongSet.NULL)
        missingValueCount++;

    assertEquals(1, missingValueCount);
  }

  @Test
  public void iteratorCanRemoveMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashLongSet.NULL);

    final LongIterator iterator = testSet.iterator();
    while (iterator.hasNext())
      if (iterator.next() == HashLongSet.NULL)
        iterator.remove();

    assertFalse(testSet.contains(HashLongSet.NULL));
  }

  @Test
  public void shouldGenerateStringRepresentation() {
    final int[] testEntries = {3, 1, -1, 19, 7, 11, 12, 7};

    for (final int testEntry : testEntries)
      testSet.add(testEntry);

    final String mapAsAString = "[1, 19, 11, 7, 3, -1, 12]";
    assertEquals(testSet.toString(), mapAsAString);
  }

  @Test
  public void shouldRemoveMissingValueWhenCleared() {
    assertTrue(testSet.add(HashLongSet.NULL));

    testSet.clear();

    assertFalse(testSet.contains(HashLongSet.NULL));
  }

  @Test
  public void shouldHaveCompatibleEqualsAndHashcode() {
    final HashSet<Long> compatibleSet = new HashSet<>();
    final long seed = System.nanoTime();
    final Random r = new Random(seed);
    for (int i = 0; i < 1024; ++i) {
      final long value = r.nextLong();
      compatibleSet.add(value);
      testSet.add(value);
    }

    if (r.nextBoolean()) {
      compatibleSet.add(HashLongSet.NULL);
      testSet.add(HashLongSet.NULL);
    }

    assertTrue("Fail with seed:" + seed, testSet.size() == compatibleSet.size() && testSet.containsAll(compatibleSet));
    assertEquals("Fail with seed:" + seed, compatibleSet.hashCode(), testSet.hashCode());
  }

  private static void addTwoElements(final HashLongSet obj) {
    obj.add(1);
    obj.add(1001);
  }

  private static void addTwoElements(final HashSet<Long> obj) {
    obj.add(1l);
    obj.add(1001l);
  }

  private void assertIteratorHasElements() {
    final LongIterator iterator = testSet.iterator();

    final Set<Long> values = new HashSet<>();

    assertTrue(iterator.hasNext());
    values.add(iterator.next());
    assertTrue(iterator.hasNext());
    values.add(iterator.next());
    assertFalse(iterator.hasNext());

    assertContainsElements(values);
  }

  private void assertIteratorHasElementsWithoutHasNext() {
    final LongIterator iterator = testSet.iterator();
    final Set<Long> values = new HashSet<>();

    values.add(iterator.next());
    values.add(iterator.next());

    assertContainsElements(values);
  }

  private static void assertArrayContainingElements(final Long[] result) {
    assertTrue(Arrays.asList(result).containsAll(Arrays.asList(1l, 1001l)));
  }

  private static void assertContainsElements(final Set<Long> other) {
    assertTrue(other.containsAll(Arrays.asList(1l, 1001l)));
  }

  private static void assertContainsElements(final LongSet other) {
    assertTrue(other.containsAll(Arrays.asList(1l, 1001l)));
  }

  private void exhaustIterator() {
    final LongIterator iterator = testSet.iterator();
    iterator.next();
    iterator.next();
    iterator.next();
  }
}