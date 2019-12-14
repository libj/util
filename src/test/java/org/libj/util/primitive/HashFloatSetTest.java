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

public class HashFloatSetTest extends PrimitiveCollectionTest {
  private static final int INITIAL_CAPACITY = 100;

  private final HashFloatSet testSet = new HashFloatSet(INITIAL_CAPACITY);

  @Test
  public void initiallyContainsNoElements() {
    for (float i = 0; i < 10000; ++i)
      assertFalse(testSet.contains(i));
  }

  @Test
  public void initiallyContainsNoBoxedElements() {
    for (float i = 0; i < 10000; ++i)
      assertFalse(testSet.contains(Float.valueOf(i)));
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
    assertTrue(testSet.add(Float.valueOf(2)));

    assertTrue(testSet.contains(Float.valueOf(1)));
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

    final HashFloatSet other = new HashFloatSet(100);
    addTwoElements(other);

    other.removeAll(testSet);
    assertEquals(0, other.size());
  }

  @Test
  public void differenceReturnsSetDifference() {
    addTwoElements(testSet);

    final HashFloatSet other = new HashFloatSet(100);
    other.add(1);

    testSet.removeAll(other);
    assertTrue(testSet.contains(1001f));
  }

  @Test
  public void copiesOtherHashFloatSet() {
    addTwoElements(testSet);

    final HashFloatSet other = new HashFloatSet(testSet);
    assertContainsElements(other);
  }

  @Test
  public void twoEmptySetsAreEqual() {
    Assert.assertEquals(testSet, new HashFloatSet(100));
  }

  @Test
  public void setsWithTheSameValuesAreEqual() {
    final HashFloatSet that = new HashFloatSet(100);

    addTwoElements(testSet);
    addTwoElements(that);

    Assert.assertEquals(testSet, that);
  }

  @Test
  public void setsWithTheDifferentSizesAreNotEqual() {
    final HashFloatSet that = new HashFloatSet(100);

    addTwoElements(testSet);
    that.add(1001);

    assertNotEquals(testSet, that);
  }

  @Test
  public void setsWithTheDifferentValuesAreNotEqual() {
    final HashFloatSet that = new HashFloatSet(100);

    addTwoElements(testSet);
    that.add(2);
    that.add(1001);

    assertNotEquals(testSet, that);
  }

  @Test
  public void twoEmptySetsHaveTheSameHashcode() {
    assertEquals(testSet.hashCode(), new HashFloatSet(100).hashCode());
  }

  @Test
  public void setsWithTheSameValuesHaveTheSameHashcode() {
    final HashFloatSet other = new HashFloatSet(100);

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
    final Float[] a = null;
    testSet.toArray(a);
  }

  @Test
  public void toArrayCopiesElementsFloatoSufficientlySizedArray() {
    addTwoElements(testSet);
    final Float[] result = testSet.toArray(new Float[testSet.size()]);

    assertArrayContainingElements(result);
  }

  @Test
  public void toArrayCopiesElementsFloatoNewArray() {
    addTwoElements(testSet);
    final Float[] result = testSet.toArray(new Float[testSet.size()]);

    assertArrayContainingElements(result);
  }

  @Test
  public void toArraySupportsEmptyCollection() {
    final Float[] result = testSet.toArray(new Float[testSet.size()]);

    Assert.assertArrayEquals(result, new Float[] {});
  }

  // Test case from usage bug.
  @Test
  public void chainCompactionShouldNotCauseElementsToBeMovedBeforeTheirHash() {
    final HashFloatSet requiredFields = new HashFloatSet(14);

    requiredFields.add(8);
    requiredFields.add(9);
    requiredFields.add(35);
    requiredFields.add(49);
    requiredFields.add(56);

    assertTrue("Failed to remove 8", requiredFields.remove(8));
    assertTrue("Failed to remove 9", requiredFields.remove(9));

    assertTrue(requiredFields.containsAll(Arrays.asList(35f, 49f, 56f)));
  }

  @Test
  public void shouldResizeWhenItHitsCapacity() {
    for (float i = 0; i < 2 * INITIAL_CAPACITY; ++i)
      assertTrue(testSet.add(i));

    for (float i = 0; i < 2 * INITIAL_CAPACITY; ++i)
      assertTrue(testSet.contains(i));
  }

  @Test
  public void containsEmptySet() {
    assertTrue(testSet.containsAll(new HashFloatSet(100)));
  }

  @Test
  public void containsSubset() {
    addTwoElements(testSet);

    final HashFloatSet subset = new HashFloatSet(100);
    subset.add(1);

    assertTrue(testSet.containsAll(subset));
  }

  @Test
  public void doesNotContainDisjointSet() {
    addTwoElements(testSet);

    final HashFloatSet disjoint = new HashFloatSet(100);
    disjoint.add(1);
    disjoint.add(1002);

    assertFalse(testSet.containsAll(disjoint));
  }

  @Test
  public void doesNotContainSuperset() {
    addTwoElements(testSet);

    final HashFloatSet superset = new HashFloatSet(100);
    addTwoElements(superset);
    superset.add(15);

    assertFalse(testSet.containsAll(superset));
  }

  @Test
  public void addingEmptySetDoesNothing() {
    addTwoElements(testSet);

    assertFalse(testSet.addAll(new HashFloatSet(100)));
    assertFalse(testSet.addAll(new HashSet<>()));
    assertContainsElements(testSet);
  }

  @Test
  public void containsValuesAddedFromDisjointSetPrimitive() {
    addTwoElements(testSet);

    final HashFloatSet disjoint = new HashFloatSet(100);

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

    final HashSet<Float> disjoint = new HashSet<>();

    disjoint.add(2f);
    disjoint.add(1002f);

    assertTrue(testSet.addAll(disjoint));
    assertTrue(testSet.contains(1));
    assertTrue(testSet.contains(1001));
    assertTrue(testSet.containsAll(disjoint));
  }

  @Test
  public void containsValuesAddedFromFloatersectingSetPrimitive() {
    addTwoElements(testSet);

    final HashFloatSet intersecting = new HashFloatSet(100);
    intersecting.add(1);
    intersecting.add(1002);

    assertTrue(testSet.addAll(intersecting));
    assertTrue(testSet.contains(1));
    assertTrue(testSet.contains(1001));
    assertTrue(testSet.containsAll(intersecting));
  }

  @Test
  public void containsValuesAddedFromFloatersectingSet() {
    addTwoElements(testSet);

    final HashSet<Float> intersecting = new HashSet<>();

    intersecting.add(1f);
    intersecting.add(1002f);

    assertTrue(testSet.addAll(intersecting));
    assertTrue(testSet.contains(1));
    assertTrue(testSet.contains(1001));
    assertTrue(testSet.containsAll(intersecting));
  }

  @Test
  public void removingEmptySetDoesNothing() {
    addTwoElements(testSet);

    assertFalse(testSet.removeAll(new HashFloatSet(100)));
    assertFalse(testSet.removeAll(new HashSet<>()));
    assertContainsElements(testSet);
  }

  @Test
  public void removingDisjointSetDoesNothing() {
    addTwoElements(testSet);

    final HashFloatSet disjoint = new HashFloatSet(100);
    disjoint.add(2);
    disjoint.add(1002);

    assertFalse(testSet.removeAll(disjoint));
    assertFalse(testSet.removeAll(new HashSet<>()));
    assertContainsElements(testSet);
  }

  @Test
  public void doesNotContainRemovedFloatersectingSetPrimitive() {
    addTwoElements(testSet);

    final HashFloatSet intersecting = new HashFloatSet(100);

    intersecting.add(1);
    intersecting.add(1002);

    assertTrue(testSet.removeAll(intersecting));
    assertTrue(testSet.contains(1001));
    assertFalse(testSet.containsAll(intersecting));
  }

  @Test
  public void doesNotContainRemovedFloatersectingSet() {
    addTwoElements(testSet);

    final HashSet<Float> intersecting = new HashSet<>();
    intersecting.add(1f);
    intersecting.add(1002f);

    assertTrue(testSet.removeAll(intersecting));
    assertTrue(testSet.contains(1001));
    assertFalse(testSet.containsAll(intersecting));
  }

  @Test
  public void isEmptyAfterRemovingEqualSetPrimitive() {
    addTwoElements(testSet);

    final HashFloatSet equal = new HashFloatSet(100);
    addTwoElements(equal);

    assertTrue(testSet.removeAll(equal));
    assertTrue(testSet.isEmpty());
  }

  @Test
  public void isEmptyAfterRemovingEqualSet() {
    addTwoElements(testSet);

    final HashSet<Float> equal = new HashSet<>();
    addTwoElements(equal);

    assertTrue(testSet.removeAll(equal));
    assertTrue(testSet.isEmpty());
  }

  @Test
  public void removeElementsFromIterator() {
    addTwoElements(testSet);

    final FloatIterator iterator = testSet.iterator();
    while (iterator.hasNext())
      if (iterator.next() == 1)
        iterator.remove();

    assertEquals(1, testSet.size());
    assertTrue(testSet.contains(1001f));
  }

  @Test
  public void shouldNotContainMissingValueInitially() {
    assertFalse(testSet.contains(HashFloatSet.NULL));
  }

  @Test
  public void shouldAllowMissingValue() {
    assertTrue(testSet.add(HashFloatSet.NULL));
    assertTrue(testSet.contains(HashFloatSet.NULL));
    assertFalse(testSet.add(HashFloatSet.NULL));
  }

  @Test
  public void shouldAllowRemovalOfMissingValue() {
    assertTrue(testSet.add(HashFloatSet.NULL));
    assertTrue(testSet.remove(HashFloatSet.NULL));
    assertFalse(testSet.contains(HashFloatSet.NULL));
    assertFalse(testSet.remove(HashFloatSet.NULL));
  }

  @Test
  public void sizeAccountsForMissingValue() {
    testSet.add(1);
    testSet.add(HashFloatSet.NULL);

    assertEquals(2, testSet.size());
  }

  @Test
  public void toArrayCopiesElementsFloatoNewArrayIncludingMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashFloatSet.NULL);

    final Float[] result = testSet.toArray(new Float[testSet.size()]);
    assertTrue(Arrays.asList(result).containsAll(Arrays.asList(1f, 1001f, HashFloatSet.NULL)));
  }

  @Test
  public void toObjectArrayCopiesElementsFloatoNewArrayIncludingMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashFloatSet.NULL);

    final float[] result = testSet.toArray();
    Arrays.sort(result);
    assertArrayEquals(new float[] {HashFloatSet.NULL, 1, 1001}, result);
  }

  @Test
  public void equalsAccountsForMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashFloatSet.NULL);

    final HashFloatSet other = new HashFloatSet(100);
    addTwoElements(other);

    assertNotEquals(testSet, other);

    other.add(HashFloatSet.NULL);
    Assert.assertEquals(testSet, other);

    testSet.remove(HashFloatSet.NULL);

    assertNotEquals(testSet, other);
  }

  @Test
  public void consecutiveValuesShouldBeCorrectlyStored() {
    for (int i = 0; i < 10000; ++i)
      testSet.add(i);

    assertEquals(10000, testSet.size());

    int distinctElements = 0;
    for (final FloatIterator i = testSet.iterator(); i.hasNext(); i.next())
      ++distinctElements;

    assertEquals(distinctElements, 10000);
  }

  @Test
  public void hashCodeAccountsForMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashFloatSet.NULL);

    final HashFloatSet other = new HashFloatSet(100);
    addTwoElements(other);

    other.add(HashFloatSet.NULL);
    assertEquals(testSet.hashCode(), other.hashCode());
  }

  @Test
  public void iteratorAccountsForMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashFloatSet.NULL);

    int missingValueCount = 0;
    final FloatIterator iterator = testSet.iterator();
    while (iterator.hasNext())
      if (iterator.next() == HashFloatSet.NULL)
        ++missingValueCount;

    assertEquals(1, missingValueCount);
  }

  @Test
  public void iteratorCanRemoveMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashFloatSet.NULL);

    final FloatIterator iterator = testSet.iterator();
    while (iterator.hasNext())
      if (iterator.next() == HashFloatSet.NULL)
        iterator.remove();

    assertFalse(testSet.contains(HashFloatSet.NULL));
  }

  @Test
  public void shouldGenerateStringRepresentation() {
    final float[] testEntries = {3, 1, -1, 19, 7, 11, 12, 7};
    for (final float testEntry : testEntries)
      testSet.add(testEntry);

    final String mapAsAString = "[3.0, 1.0, -1.0, 19.0, 7.0, 11.0, 12.0]";
    Assert.assertEquals(mapAsAString, testSet.toString());
  }

  @Test
  public void shouldRemoveMissingValueWhenCleared() {
    assertTrue(testSet.add(HashFloatSet.NULL));

    testSet.clear();

    assertFalse(testSet.contains(HashFloatSet.NULL));
  }

  @Test
  public void shouldHaveCompatibleEqualsAndHashcode() {
    final HashSet<Float> compatibleSet = new HashSet<>();
    final long seed = System.nanoTime();
    final Random r = new Random(seed);
    for (int i = 0; i < 1024; ++i) {
      final float value = r.nextFloat();
      compatibleSet.add(value);
      testSet.add(value);
    }

    if (r.nextBoolean()) {
      compatibleSet.add(HashFloatSet.NULL);
      testSet.add(HashFloatSet.NULL);
    }

    assertTrue("Fail with seed:" + seed, testSet.size() == compatibleSet.size() && testSet.containsAll(compatibleSet));
    Assert.assertEquals("Fail with seed:" + seed, compatibleSet.hashCode(), testSet.hashCode());
  }

  private static void addTwoElements(final HashFloatSet obj) {
    obj.add(1);
    obj.add(1001);
  }

  private static void addTwoElements(final HashSet<Float> obj) {
    obj.add(1f);
    obj.add(1001f);
  }

  private void assertIteratorHasElements() {
    final FloatIterator iterator = testSet.iterator();

    final Set<Float> values = new HashSet<>();

    assertTrue(iterator.hasNext());
    values.add(iterator.next());
    assertTrue(iterator.hasNext());
    values.add(iterator.next());
    assertFalse(iterator.hasNext());

    assertContainsElements(values);
  }

  private void assertIteratorHasElementsWithoutHasNext() {
    final FloatIterator iterator = testSet.iterator();
    final Set<Float> values = new HashSet<>();

    values.add(iterator.next());
    values.add(iterator.next());

    assertContainsElements(values);
  }

  private static void assertArrayContainingElements(final Float[] result) {
    assertTrue(Arrays.asList(result).containsAll(Arrays.asList(1f, 1001f)));
  }

  private static void assertContainsElements(final Set<Float> other) {
    assertTrue(other.containsAll(Arrays.asList(1f, 1001f)));
  }

  private static void assertContainsElements(final FloatSet other) {
    assertTrue(other.containsAll(Arrays.asList(1f, 1001f)));
  }

  private void exhaustIterator() {
    final FloatIterator iterator = testSet.iterator();
    iterator.next();
    iterator.next();
    iterator.next();
  }
}