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

package org.libj.util.primitive;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class HashShortSetTest extends PrimitiveCollectionTest {
  private static final int INITIAL_CAPACITY = 128;

  private final HashShortSet testSet = new HashShortSet(INITIAL_CAPACITY);

  @Test
  public void initiallyContainsNoElements() {
    for (short i = 0; i < 100; ++i)
      assertFalse(testSet.contains(i));
  }

  @Test
  public void initiallyContainsNoBoxedElements() {
    for (short i = 0; i < 100; ++i)
      assertFalse(testSet.contains(Short.valueOf(i)));
  }

  @Test
  public void containsAddedElement() {
    assertTrue(testSet.add((short)1));
    assertTrue(testSet.contains((short)1));
  }

  @Test
  public void addingAnElementTwiceDoesNothing() {
    assertTrue(testSet.add((short)1));
    assertFalse(testSet.add((short)1));
  }

  @Test
  public void containsAddedBoxedElements() {
    assertTrue(testSet.add((short)1));
    assertTrue(testSet.add(Short.valueOf((short)2)));

    assertTrue(testSet.contains(Short.valueOf((short)1)));
    assertTrue(testSet.contains((short)2));
  }

  @Test
  public void removingAnElementFromAnEmptyListDoesNothing() {
    assertFalse(testSet.remove((short)0));
  }

  @Test
  public void removingAPresentElementRemovesIt() {
    assertTrue(testSet.add((short)1));
    assertTrue(testSet.remove((short)1));
    assertFalse(testSet.contains((short)1));
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
    testSet.add((short)1);
    testSet.add((short)1);

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
    assertFalse(testSet.contains((short)1));
    assertFalse(testSet.contains((short)101));
  }

  @Test
  public void differenceReturnsNullIfBothSetsEqual() {
    addTwoElements(testSet);

    final HashShortSet other = new HashShortSet(100);
    addTwoElements(other);

    other.removeAll(testSet);
    assertEquals(0, other.size());
  }

  @Test
  public void differenceReturnsSetDifference() {
    addTwoElements(testSet);

    final HashShortSet other = new HashShortSet(100);
    other.add((short)1);

    testSet.removeAll(other);
    assertTrue(testSet.contains((short)101));
  }

  @Test
  public void copiesOtherHashShortSet() {
    addTwoElements(testSet);

    final HashShortSet other = new HashShortSet(testSet);
    assertContainsElements(other);
  }

  @Test
  public void twoEmptySetsAreEqual() {
    Assert.assertEquals(testSet, new HashShortSet(100));
  }

  @Test
  public void setsWithTheSameValuesAreEqual() {
    final HashShortSet that = new HashShortSet(100);

    addTwoElements(testSet);
    addTwoElements(that);

    Assert.assertEquals(testSet, that);
  }

  @Test
  public void setsWithTheDifferentSizesAreNotEqual() {
    final HashShortSet that = new HashShortSet(100);

    addTwoElements(testSet);
    that.add((short)101);

    assertNotEquals(testSet, that);
  }

  @Test
  public void setsWithTheDifferentValuesAreNotEqual() {
    final HashShortSet that = new HashShortSet(100);

    addTwoElements(testSet);
    that.add((short)2);
    that.add((short)101);

    assertNotEquals(testSet, that);
  }

  @Test
  public void twoEmptySetsHaveTheSameHashcode() {
    assertEquals(testSet.hashCode(), new HashShortSet(100).hashCode());
  }

  @Test
  public void setsWithTheSameValuesHaveTheSameHashcode() {
    final HashShortSet other = new HashShortSet(100);

    addTwoElements(testSet);
    addTwoElements(other);

    assertEquals(testSet.hashCode(), other.hashCode());
  }

  @Test
  public void reducesSizeWhenElementRemoved() {
    addTwoElements(testSet);
    testSet.remove((short)101);

    assertEquals(1, testSet.size());
  }

  @Test(expected = NullPointerException.class)
  public void toArrayThrowsNullPointerExceptionForNullArgument() {
    final Short[] a = null;
    testSet.toArray(a);
  }

  @Test
  public void toArrayCopiesElementsShortoSufficientlySizedArray() {
    addTwoElements(testSet);
    final Short[] result = testSet.toArray(new Short[testSet.size()]);

    assertArrayContainingElements(result);
  }

  @Test
  public void toArrayCopiesElementsShortoNewArray() {
    addTwoElements(testSet);
    final Short[] result = testSet.toArray(new Short[testSet.size()]);

    assertArrayContainingElements(result);
  }

  @Test
  public void toArraySupportsEmptyCollection() {
    final Short[] result = testSet.toArray(new Short[testSet.size()]);

    Assert.assertArrayEquals(result, new Short[] {});
  }

  // Test case from usage bug.
  @Test
  public void chainCompactionShouldNotCauseElementsToBeMovedBeforeTheirHash() {
    final HashShortSet requiredFields = new HashShortSet(14);

    requiredFields.add((short)8);
    requiredFields.add((short)9);
    requiredFields.add((short)35);
    requiredFields.add((short)49);
    requiredFields.add((short)56);

    assertTrue("Failed to remove 8", requiredFields.remove((short)8));
    assertTrue("Failed to remove 9", requiredFields.remove((short)9));

    assertTrue(requiredFields.containsAll(Arrays.asList((short)35, (short)49, (short)56)));
  }

  @Test
  public void shouldResizeWhenItHitsCapacity() {
    for (short i = 0; i < 2 * INITIAL_CAPACITY - 1; ++i)
      assertTrue(testSet.add(i));

    for (short i = 0; i < 2 * INITIAL_CAPACITY - 1; ++i)
      assertTrue(testSet.contains(i));
  }

  @Test
  public void containsEmptySet() {
    assertTrue(testSet.containsAll(new HashShortSet(100)));
  }

  @Test
  public void containsSubset() {
    addTwoElements(testSet);

    final HashShortSet subset = new HashShortSet(100);
    subset.add((short)1);

    assertTrue(testSet.containsAll(subset));
  }

  @Test
  public void doesNotContainDisjointSet() {
    addTwoElements(testSet);

    final HashShortSet disjoint = new HashShortSet(100);
    disjoint.add((short)1);
    disjoint.add((short)102);

    assertFalse(testSet.containsAll(disjoint));
  }

  @Test
  public void doesNotContainSuperset() {
    addTwoElements(testSet);

    final HashShortSet superset = new HashShortSet(100);
    addTwoElements(superset);
    superset.add((short)15);

    assertFalse(testSet.containsAll(superset));
  }

  @Test
  public void addingEmptySetDoesNothing() {
    addTwoElements(testSet);

    assertFalse(testSet.addAll(new HashShortSet(100)));
    assertFalse(testSet.addAll(new HashSet<>()));
    assertContainsElements(testSet);
  }

  @Test
  public void containsValuesAddedFromDisjointSetPrimitive() {
    addTwoElements(testSet);

    final HashShortSet disjoint = new HashShortSet(100);

    disjoint.add((short)2);
    disjoint.add((short)102);

    assertTrue(testSet.addAll(disjoint));
    assertTrue(testSet.contains((short)1));
    assertTrue(testSet.contains((short)101));
    assertTrue(testSet.containsAll(disjoint));
  }

  @Test
  public void containsValuesAddedFromDisjointSet() {
    addTwoElements(testSet);

    final HashSet<Short> disjoint = new HashSet<>();

    disjoint.add((short)2);
    disjoint.add((short)102);

    assertTrue(testSet.addAll(disjoint));
    assertTrue(testSet.contains((short)1));
    assertTrue(testSet.contains((short)101));
    assertTrue(testSet.containsAll(disjoint));
  }

  @Test
  public void containsValuesAddedFromShortersectingSetPrimitive() {
    addTwoElements(testSet);

    final HashShortSet intersecting = new HashShortSet(100);
    intersecting.add((short)1);
    intersecting.add((short)102);

    assertTrue(testSet.addAll(intersecting));
    assertTrue(testSet.contains((short)1));
    assertTrue(testSet.contains((short)101));
    assertTrue(testSet.containsAll(intersecting));
  }

  @Test
  public void containsValuesAddedFromShortersectingSet() {
    addTwoElements(testSet);

    final HashSet<Short> intersecting = new HashSet<>();

    intersecting.add((short)1);
    intersecting.add((short)102);

    assertTrue(testSet.addAll(intersecting));
    assertTrue(testSet.contains((short)1));
    assertTrue(testSet.contains((short)101));
    assertTrue(testSet.containsAll(intersecting));
  }

  @Test
  public void removingEmptySetDoesNothing() {
    addTwoElements(testSet);

    assertFalse(testSet.removeAll(new HashShortSet(100)));
    assertFalse(testSet.removeAll(new HashSet<>()));
    assertContainsElements(testSet);
  }

  @Test
  public void removingDisjointSetDoesNothing() {
    addTwoElements(testSet);

    final HashShortSet disjoint = new HashShortSet(100);
    disjoint.add((short)2);
    disjoint.add((short)102);

    assertFalse(testSet.removeAll(disjoint));
    assertFalse(testSet.removeAll(new HashSet<>()));
    assertContainsElements(testSet);
  }

  @Test
  public void doesNotContainRemovedShortersectingSetPrimitive() {
    addTwoElements(testSet);

    final HashShortSet intersecting = new HashShortSet(100);

    intersecting.add((short)1);
    intersecting.add((short)102);

    assertTrue(testSet.removeAll(intersecting));
    assertTrue(testSet.contains((short)101));
    assertFalse(testSet.containsAll(intersecting));
  }

  @Test
  public void doesNotContainRemovedShortersectingSet() {
    addTwoElements(testSet);

    final HashSet<Short> intersecting = new HashSet<>();
    intersecting.add((short)1);
    intersecting.add((short)102);

    assertTrue(testSet.removeAll(intersecting));
    assertTrue(testSet.contains((short)101));
    assertFalse(testSet.containsAll(intersecting));
  }

  @Test
  public void isEmptyAfterRemovingEqualSetPrimitive() {
    addTwoElements(testSet);

    final HashShortSet equal = new HashShortSet(100);
    addTwoElements(equal);

    assertTrue(testSet.removeAll(equal));
    assertTrue(testSet.isEmpty());
  }

  @Test
  public void isEmptyAfterRemovingEqualSet() {
    addTwoElements(testSet);

    final HashSet<Short> equal = new HashSet<>();
    addTwoElements(equal);

    assertTrue(testSet.removeAll(equal));
    assertTrue(testSet.isEmpty());
  }

  @Test
  public void removeElementsFromIterator() {
    addTwoElements(testSet);

    final ShortIterator iterator = testSet.iterator();
    while (iterator.hasNext())
      if (iterator.next() == 1)
        iterator.remove();

    assertEquals(1, testSet.size());
    assertTrue(testSet.contains((short)101));
  }

  @Test
  public void shouldNotContainMissingValueInitially() {
    assertFalse(testSet.contains(HashShortSet.NULL));
  }

  @Test
  public void shouldAllowMissingValue() {
    assertTrue(testSet.add(HashShortSet.NULL));
    assertTrue(testSet.contains(HashShortSet.NULL));
    assertFalse(testSet.add(HashShortSet.NULL));
  }

  @Test
  public void shouldAllowRemovalOfMissingValue() {
    assertTrue(testSet.add(HashShortSet.NULL));
    assertTrue(testSet.remove(HashShortSet.NULL));
    assertFalse(testSet.contains(HashShortSet.NULL));
    assertFalse(testSet.remove(HashShortSet.NULL));
  }

  @Test
  public void sizeAccountsForMissingValue() {
    testSet.add((short)1);
    testSet.add(HashShortSet.NULL);

    assertEquals(2, testSet.size());
  }

  @Test
  public void toArrayCopiesElementsShortoNewArrayIncludingMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashShortSet.NULL);

    final Short[] result = testSet.toArray(new Short[testSet.size()]);
    assertTrue(Arrays.asList(result).containsAll(Arrays.asList((short)1, (short)101, HashShortSet.NULL)));
  }

  @Test
  public void toObjectArrayCopiesElementsShortoNewArrayIncludingMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashShortSet.NULL);

    final short[] result = testSet.toArray();
    Arrays.sort(result);
    assertArrayEquals(new short[] {HashShortSet.NULL, 1, 101}, result);
  }

  @Test
  public void equalsAccountsForMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashShortSet.NULL);

    final HashShortSet other = new HashShortSet(100);
    addTwoElements(other);

    assertNotEquals(testSet, other);

    other.add(HashShortSet.NULL);
    Assert.assertEquals(testSet, other);

    testSet.remove(HashShortSet.NULL);

    assertNotEquals(testSet, other);
  }

  @Test
  public void consecutiveValuesShouldBeCorrectlyStored() {
    for (short i = -128; i < 127; ++i)
      testSet.add(i);

    assertEquals(255, testSet.size());

    int distinctElements = 0;
    for (final ShortIterator i = testSet.iterator(); i.hasNext(); i.next())
      ++distinctElements;

    assertEquals(distinctElements, 255);
  }

  @Test
  public void hashCodeAccountsForMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashShortSet.NULL);

    final HashShortSet other = new HashShortSet(100);
    addTwoElements(other);

    other.add(HashShortSet.NULL);
    assertEquals(testSet.hashCode(), other.hashCode());
  }

  @Test
  public void iteratorAccountsForMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashShortSet.NULL);

    int missingValueCount = 0;
    final ShortIterator iterator = testSet.iterator();
    while (iterator.hasNext())
      if (iterator.next() == HashShortSet.NULL)
        ++missingValueCount;

    assertEquals(1, missingValueCount);
  }

  @Test
  public void iteratorCanRemoveMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashShortSet.NULL);

    final ShortIterator iterator = testSet.iterator();
    while (iterator.hasNext())
      if (iterator.next() == HashShortSet.NULL)
        iterator.remove();

    assertFalse(testSet.contains(HashShortSet.NULL));
  }

  @Test
  public void shouldGenerateStringRepresentation() {
    final short[] testEntries = {3, 1, -1, 19, 7, 11, 12, 7};
    for (final short testEntry : testEntries)
      testSet.add(testEntry);

    final String mapAsAString = "[1, 19, 11, 7, 3, -1, 12]";
    Assert.assertEquals(mapAsAString, testSet.toString());
  }

  @Test
  public void shouldRemoveMissingValueWhenCleared() {
    assertTrue(testSet.add(HashShortSet.NULL));

    testSet.clear();

    assertFalse(testSet.contains(HashShortSet.NULL));
  }

  @Test
  public void shouldHaveCompatibleEqualsAndHashcode() {
    final HashSet<Short> compatibleSet = new HashSet<>();
    final long seed = System.nanoTime();
    final Random r = new Random(seed);
    for (int i = 0; i < 1024; ++i) {
      final short value = (short)r.nextInt();
      compatibleSet.add(value);
      testSet.add(value);
    }

    if (r.nextBoolean()) {
      compatibleSet.add(HashShortSet.NULL);
      testSet.add(HashShortSet.NULL);
    }

    assertTrue("Fail with seed:" + seed, testSet.size() == compatibleSet.size() && testSet.containsAll(compatibleSet));
    Assert.assertEquals("Fail with seed:" + seed, compatibleSet.hashCode(), testSet.hashCode());
  }

  private static void addTwoElements(final HashShortSet obj) {
    obj.add((short)1);
    obj.add((short)101);
  }

  private static void addTwoElements(final HashSet<Short> obj) {
    obj.add((short)1);
    obj.add((short)101);
  }

  private void assertIteratorHasElements() {
    final ShortIterator iterator = testSet.iterator();

    final Set<Short> values = new HashSet<>();

    assertTrue(iterator.hasNext());
    values.add(iterator.next());
    assertTrue(iterator.hasNext());
    values.add(iterator.next());
    assertFalse(iterator.hasNext());

    assertContainsElements(values);
  }

  private void assertIteratorHasElementsWithoutHasNext() {
    final ShortIterator iterator = testSet.iterator();
    final Set<Short> values = new HashSet<>();

    values.add(iterator.next());
    values.add(iterator.next());

    assertContainsElements(values);
  }

  private static void assertArrayContainingElements(final Short[] result) {
    assertTrue(Arrays.asList(result).containsAll(Arrays.asList((short)1, (short)101)));
  }

  private static void assertContainsElements(final Set<Short> other) {
    assertTrue(other.containsAll(Arrays.asList((short)1, (short)101)));
  }

  private static void assertContainsElements(final ShortSet other) {
    assertTrue(other.containsAll(Arrays.asList((short)1, (short)101)));
  }

  private void exhaustIterator() {
    final ShortIterator iterator = testSet.iterator();
    iterator.next();
    iterator.next();
    iterator.next();
  }
}