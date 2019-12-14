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

public class HashByteSetTest extends PrimitiveCollectionTest {
  private static final int INITIAL_CAPACITY = 64;

  private final HashByteSet testSet = new HashByteSet(INITIAL_CAPACITY);

  @Test
  public void initiallyContainsNoElements() {
    for (byte i = 0; i < 100; ++i)
      assertFalse(testSet.contains(i));
  }

  @Test
  public void initiallyContainsNoBoxedElements() {
    for (byte i = 0; i < 100; ++i)
      assertFalse(testSet.contains(Byte.valueOf(i)));
  }

  @Test
  public void containsAddedElement() {
    assertTrue(testSet.add((byte)1));
    assertTrue(testSet.contains((byte)1));
  }

  @Test
  public void addingAnElementTwiceDoesNothing() {
    assertTrue(testSet.add((byte)1));
    assertFalse(testSet.add((byte)1));
  }

  @Test
  public void containsAddedBoxedElements() {
    assertTrue(testSet.add((byte)1));
    assertTrue(testSet.add(Byte.valueOf((byte)2)));

    assertTrue(testSet.contains(Byte.valueOf((byte)1)));
    assertTrue(testSet.contains((byte)2));
  }

  @Test
  public void removingAnElementFromAnEmptyListDoesNothing() {
    assertFalse(testSet.remove((byte)0));
  }

  @Test
  public void removingAPresentElementRemovesIt() {
    assertTrue(testSet.add((byte)1));
    assertTrue(testSet.remove((byte)1));
    assertFalse(testSet.contains((byte)1));
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
    testSet.add((byte)1);
    testSet.add((byte)1);

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
    assertFalse(testSet.contains((byte)1));
    assertFalse(testSet.contains((byte)101));
  }

  @Test
  public void differenceReturnsNullIfBothSetsEqual() {
    addTwoElements(testSet);

    final HashByteSet other = new HashByteSet(100);
    addTwoElements(other);

    other.removeAll(testSet);
    assertEquals(0, other.size());
  }

  @Test
  public void differenceReturnsSetDifference() {
    addTwoElements(testSet);

    final HashByteSet other = new HashByteSet(100);
    other.add((byte)1);

    testSet.removeAll(other);
    assertTrue(testSet.contains((byte)101));
  }

  @Test
  public void copiesOtherHashByteSet() {
    addTwoElements(testSet);

    final HashByteSet other = new HashByteSet(testSet);
    assertContainsElements(other);
  }

  @Test
  public void twoEmptySetsAreEqual() {
    Assert.assertEquals(testSet, new HashByteSet(100));
  }

  @Test
  public void setsWithTheSameValuesAreEqual() {
    final HashByteSet that = new HashByteSet(100);

    addTwoElements(testSet);
    addTwoElements(that);

    Assert.assertEquals(testSet, that);
  }

  @Test
  public void setsWithTheDifferentSizesAreNotEqual() {
    final HashByteSet that = new HashByteSet(100);

    addTwoElements(testSet);
    that.add((byte)101);

    assertNotEquals(testSet, that);
  }

  @Test
  public void setsWithTheDifferentValuesAreNotEqual() {
    final HashByteSet that = new HashByteSet(100);

    addTwoElements(testSet);
    that.add((byte)2);
    that.add((byte)101);

    assertNotEquals(testSet, that);
  }

  @Test
  public void twoEmptySetsHaveTheSameHashcode() {
    assertEquals(testSet.hashCode(), new HashByteSet(100).hashCode());
  }

  @Test
  public void setsWithTheSameValuesHaveTheSameHashcode() {
    final HashByteSet other = new HashByteSet(100);

    addTwoElements(testSet);
    addTwoElements(other);

    assertEquals(testSet.hashCode(), other.hashCode());
  }

  @Test
  public void reducesSizeWhenElementRemoved() {
    addTwoElements(testSet);
    testSet.remove((byte)101);

    assertEquals(1, testSet.size());
  }

  @Test(expected = NullPointerException.class)
  public void toArrayThrowsNullPointerExceptionForNullArgument() {
    final Byte[] a = null;
    testSet.toArray(a);
  }

  @Test
  public void toArrayCopiesElementsByteoSufficientlySizedArray() {
    addTwoElements(testSet);
    final Byte[] result = testSet.toArray(new Byte[testSet.size()]);

    assertArrayContainingElements(result);
  }

  @Test
  public void toArrayCopiesElementsByteoNewArray() {
    addTwoElements(testSet);
    final Byte[] result = testSet.toArray(new Byte[testSet.size()]);

    assertArrayContainingElements(result);
  }

  @Test
  public void toArraySupportsEmptyCollection() {
    final Byte[] result = testSet.toArray(new Byte[testSet.size()]);

    Assert.assertArrayEquals(result, new Byte[] {});
  }

  // Test case from usage bug.
  @Test
  public void chainCompactionShouldNotCauseElementsToBeMovedBeforeTheirHash() {
    final HashByteSet requiredFields = new HashByteSet(14);

    requiredFields.add((byte)8);
    requiredFields.add((byte)9);
    requiredFields.add((byte)35);
    requiredFields.add((byte)49);
    requiredFields.add((byte)56);

    assertTrue("Failed to remove 8", requiredFields.remove((byte)8));
    assertTrue("Failed to remove 9", requiredFields.remove((byte)9));

    assertTrue(requiredFields.containsAll(Arrays.asList((byte)35, (byte)49, (byte)56)));
  }

  @Test
  public void shouldResizeWhenItHitsCapacity() {
    for (byte i = 0; i < 2 * INITIAL_CAPACITY - 1; ++i)
      assertTrue(testSet.add(i));

    for (byte i = 0; i < 2 * INITIAL_CAPACITY - 1; ++i)
      assertTrue(testSet.contains(i));
  }

  @Test
  public void containsEmptySet() {
    assertTrue(testSet.containsAll(new HashByteSet(100)));
  }

  @Test
  public void containsSubset() {
    addTwoElements(testSet);

    final HashByteSet subset = new HashByteSet(100);
    subset.add((byte)1);

    assertTrue(testSet.containsAll(subset));
  }

  @Test
  public void doesNotContainDisjointSet() {
    addTwoElements(testSet);

    final HashByteSet disjoint = new HashByteSet(100);
    disjoint.add((byte)1);
    disjoint.add((byte)102);

    assertFalse(testSet.containsAll(disjoint));
  }

  @Test
  public void doesNotContainSuperset() {
    addTwoElements(testSet);

    final HashByteSet superset = new HashByteSet(100);
    addTwoElements(superset);
    superset.add((byte)15);

    assertFalse(testSet.containsAll(superset));
  }

  @Test
  public void addingEmptySetDoesNothing() {
    addTwoElements(testSet);

    assertFalse(testSet.addAll(new HashByteSet(100)));
    assertFalse(testSet.addAll(new HashSet<>()));
    assertContainsElements(testSet);
  }

  @Test
  public void containsValuesAddedFromDisjointSetPrimitive() {
    addTwoElements(testSet);

    final HashByteSet disjoint = new HashByteSet(100);

    disjoint.add((byte)2);
    disjoint.add((byte)102);

    assertTrue(testSet.addAll(disjoint));
    assertTrue(testSet.contains((byte)1));
    assertTrue(testSet.contains((byte)101));
    assertTrue(testSet.containsAll(disjoint));
  }

  @Test
  public void containsValuesAddedFromDisjointSet() {
    addTwoElements(testSet);

    final HashSet<Byte> disjoint = new HashSet<>();

    disjoint.add((byte)2);
    disjoint.add((byte)102);

    assertTrue(testSet.addAll(disjoint));
    assertTrue(testSet.contains((byte)1));
    assertTrue(testSet.contains((byte)101));
    assertTrue(testSet.containsAll(disjoint));
  }

  @Test
  public void containsValuesAddedFromByteersectingSetPrimitive() {
    addTwoElements(testSet);

    final HashByteSet intersecting = new HashByteSet(100);
    intersecting.add((byte)1);
    intersecting.add((byte)102);

    assertTrue(testSet.addAll(intersecting));
    assertTrue(testSet.contains((byte)1));
    assertTrue(testSet.contains((byte)101));
    assertTrue(testSet.containsAll(intersecting));
  }

  @Test
  public void containsValuesAddedFromByteersectingSet() {
    addTwoElements(testSet);

    final HashSet<Byte> intersecting = new HashSet<>();

    intersecting.add((byte)1);
    intersecting.add((byte)102);

    assertTrue(testSet.addAll(intersecting));
    assertTrue(testSet.contains((byte)1));
    assertTrue(testSet.contains((byte)101));
    assertTrue(testSet.containsAll(intersecting));
  }

  @Test
  public void removingEmptySetDoesNothing() {
    addTwoElements(testSet);

    assertFalse(testSet.removeAll(new HashByteSet(100)));
    assertFalse(testSet.removeAll(new HashSet<>()));
    assertContainsElements(testSet);
  }

  @Test
  public void removingDisjointSetDoesNothing() {
    addTwoElements(testSet);

    final HashByteSet disjoint = new HashByteSet(100);
    disjoint.add((byte)2);
    disjoint.add((byte)102);

    assertFalse(testSet.removeAll(disjoint));
    assertFalse(testSet.removeAll(new HashSet<>()));
    assertContainsElements(testSet);
  }

  @Test
  public void doesNotContainRemovedByteersectingSetPrimitive() {
    addTwoElements(testSet);

    final HashByteSet intersecting = new HashByteSet(100);

    intersecting.add((byte)1);
    intersecting.add((byte)102);

    assertTrue(testSet.removeAll(intersecting));
    assertTrue(testSet.contains((byte)101));
    assertFalse(testSet.containsAll(intersecting));
  }

  @Test
  public void doesNotContainRemovedByteersectingSet() {
    addTwoElements(testSet);

    final HashSet<Byte> intersecting = new HashSet<>();
    intersecting.add((byte)1);
    intersecting.add((byte)102);

    assertTrue(testSet.removeAll(intersecting));
    assertTrue(testSet.contains((byte)101));
    assertFalse(testSet.containsAll(intersecting));
  }

  @Test
  public void isEmptyAfterRemovingEqualSetPrimitive() {
    addTwoElements(testSet);

    final HashByteSet equal = new HashByteSet(100);
    addTwoElements(equal);

    assertTrue(testSet.removeAll(equal));
    assertTrue(testSet.isEmpty());
  }

  @Test
  public void isEmptyAfterRemovingEqualSet() {
    addTwoElements(testSet);

    final HashSet<Byte> equal = new HashSet<>();
    addTwoElements(equal);

    assertTrue(testSet.removeAll(equal));
    assertTrue(testSet.isEmpty());
  }

  @Test
  public void removeElementsFromIterator() {
    addTwoElements(testSet);

    final ByteIterator iterator = testSet.iterator();
    while (iterator.hasNext())
      if (iterator.next() == 1)
        iterator.remove();

    assertEquals(1, testSet.size());
    assertTrue(testSet.contains((byte)101));
  }

  @Test
  public void shouldNotContainMissingValueInitially() {
    assertFalse(testSet.contains(HashByteSet.NULL));
  }

  @Test
  public void shouldAllowMissingValue() {
    assertTrue(testSet.add(HashByteSet.NULL));
    assertTrue(testSet.contains(HashByteSet.NULL));
    assertFalse(testSet.add(HashByteSet.NULL));
  }

  @Test
  public void shouldAllowRemovalOfMissingValue() {
    assertTrue(testSet.add(HashByteSet.NULL));
    assertTrue(testSet.remove(HashByteSet.NULL));
    assertFalse(testSet.contains(HashByteSet.NULL));
    assertFalse(testSet.remove(HashByteSet.NULL));
  }

  @Test
  public void sizeAccountsForMissingValue() {
    testSet.add((byte)1);
    testSet.add(HashByteSet.NULL);

    assertEquals(2, testSet.size());
  }

  @Test
  public void toArrayCopiesElementsByteoNewArrayIncludingMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashByteSet.NULL);

    final Byte[] result = testSet.toArray(new Byte[testSet.size()]);
    assertTrue(Arrays.asList(result).containsAll(Arrays.asList((byte)1, (byte)101, HashByteSet.NULL)));
  }

  @Test
  public void toObjectArrayCopiesElementsByteoNewArrayIncludingMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashByteSet.NULL);

    final byte[] result = testSet.toArray();
    Arrays.sort(result);
    assertArrayEquals(new byte[] {HashByteSet.NULL, 1, 101}, result);
  }

  @Test
  public void equalsAccountsForMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashByteSet.NULL);

    final HashByteSet other = new HashByteSet(100);
    addTwoElements(other);

    assertNotEquals(testSet, other);

    other.add(HashByteSet.NULL);
    Assert.assertEquals(testSet, other);

    testSet.remove(HashByteSet.NULL);

    assertNotEquals(testSet, other);
  }

  @Test
  public void consecutiveValuesShouldBeCorrectlyStored() {
    for (byte i = -128; i < 127; ++i)
      testSet.add(i);

    assertEquals(255, testSet.size());

    int distinctElements = 0;
    for (final ByteIterator i = testSet.iterator(); i.hasNext(); i.next())
      ++distinctElements;

    assertEquals(distinctElements, 255);
  }

  @Test
  public void hashCodeAccountsForMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashByteSet.NULL);

    final HashByteSet other = new HashByteSet(100);
    addTwoElements(other);

    other.add(HashByteSet.NULL);
    assertEquals(testSet.hashCode(), other.hashCode());
  }

  @Test
  public void iteratorAccountsForMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashByteSet.NULL);

    int missingValueCount = 0;
    final ByteIterator iterator = testSet.iterator();
    while (iterator.hasNext())
      if (iterator.next() == HashByteSet.NULL)
        ++missingValueCount;

    assertEquals(1, missingValueCount);
  }

  @Test
  public void iteratorCanRemoveMissingValue() {
    addTwoElements(testSet);
    testSet.add(HashByteSet.NULL);

    final ByteIterator iterator = testSet.iterator();
    while (iterator.hasNext())
      if (iterator.next() == HashByteSet.NULL)
        iterator.remove();

    assertFalse(testSet.contains(HashByteSet.NULL));
  }

  @Test
  public void shouldGenerateStringRepresentation() {
    final byte[] testEntries = {3, 1, -1, 19, 7, 11, 12, 7};
    for (final byte testEntry : testEntries)
      testSet.add(testEntry);

    final String mapAsAString = "[19, 11, 7, 3, 1, -1, 12]";
    Assert.assertEquals(mapAsAString, testSet.toString());
  }

  @Test
  public void shouldRemoveMissingValueWhenCleared() {
    assertTrue(testSet.add(HashByteSet.NULL));

    testSet.clear();

    assertFalse(testSet.contains(HashByteSet.NULL));
  }

  @Test
  public void shouldHaveCompatibleEqualsAndHashcode() {
    final HashSet<Byte> compatibleSet = new HashSet<>();
    final long seed = System.nanoTime();
    final Random r = new Random(seed);
    for (int i = 0; i < 1024; ++i) {
      final byte value = (byte)r.nextInt();
      compatibleSet.add(value);
      testSet.add(value);
    }

    if (r.nextBoolean()) {
      compatibleSet.add(HashByteSet.NULL);
      testSet.add(HashByteSet.NULL);
    }

    assertTrue("Fail with seed:" + seed, testSet.size() == compatibleSet.size() && testSet.containsAll(compatibleSet));
    Assert.assertEquals("Fail with seed:" + seed, compatibleSet.hashCode(), testSet.hashCode());
  }

  private static void addTwoElements(final HashByteSet obj) {
    obj.add((byte)1);
    obj.add((byte)101);
  }

  private static void addTwoElements(final HashSet<Byte> obj) {
    obj.add((byte)1);
    obj.add((byte)101);
  }

  private void assertIteratorHasElements() {
    final ByteIterator iterator = testSet.iterator();

    final Set<Byte> values = new HashSet<>();

    assertTrue(iterator.hasNext());
    values.add(iterator.next());
    assertTrue(iterator.hasNext());
    values.add(iterator.next());
    assertFalse(iterator.hasNext());

    assertContainsElements(values);
  }

  private void assertIteratorHasElementsWithoutHasNext() {
    final ByteIterator iterator = testSet.iterator();
    final Set<Byte> values = new HashSet<>();

    values.add(iterator.next());
    values.add(iterator.next());

    assertContainsElements(values);
  }

  private static void assertArrayContainingElements(final Byte[] result) {
    assertTrue(Arrays.asList(result).containsAll(Arrays.asList((byte)1, (byte)101)));
  }

  private static void assertContainsElements(final Set<Byte> other) {
    assertTrue(other.containsAll(Arrays.asList((byte)1, (byte)101)));
  }

  private static void assertContainsElements(final ByteSet other) {
    assertTrue(other.containsAll(Arrays.asList((byte)1, (byte)101)));
  }

  private void exhaustIterator() {
    final ByteIterator iterator = testSet.iterator();
    iterator.next();
    iterator.next();
    iterator.next();
  }
}