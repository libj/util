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
import org.libj.util.CollectionUtil;

@SuppressWarnings("cast")
public class Hash<X>SetTest extends PrimitiveCollectionTest {
  private static final int INITIAL_CAPACITY = 64;

  private final Hash<X>Set testSet = new Hash<X>Set(INITIAL_CAPACITY);

  @Test
  public void initiallyContainsNoElements() {
    for (<y> i = 0; i < 100; ++i)
      assertFalse(testSet.contains(i));
  }

  @Test
  public void initiallyContainsNoBoxedElements() {
    for (<y> i = 0; i < 100; ++i)
      assertFalse(testSet.contains(<Y>.valueOf(i)));
  }

  @Test
  public void containsAddedElement() {
    assertTrue(testSet.add((<y>)1));
    assertTrue(testSet.contains((<y>)1));
  }

  @Test
  public void addingAnElementTwiceDoesNothing() {
    assertTrue(testSet.add((<y>)1));
    assertFalse(testSet.add((<y>)1));
  }

  @Test
  public void containsAddedBoxedElements() {
    assertTrue(testSet.add((<y>)1));
    assertTrue(testSet.add(<Y>.valueOf((<y>)2)));

    assertTrue(testSet.contains(<Y>.valueOf((<y>)1)));
    assertTrue(testSet.contains((<y>)2));
  }

  @Test
  public void removingAnElementFromAnEmptyListDoesNothing() {
    assertFalse(testSet.remove((<y>)0));
  }

  @Test
  public void removingAPresentElementRemovesIt() {
    assertTrue(testSet.add((<y>)1));
    assertTrue(testSet.remove((<y>)1));
    assertFalse(testSet.contains((<y>)1));
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
    testSet.add((<y>)1);
    testSet.add((<y>)1);

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
    assertFalse(testSet.contains((<y>)1));
    assertFalse(testSet.contains((<y>)101));
  }

  @Test
  public void differenceReturnsNullIfBothSetsEqual() {
    addTwoElements(testSet);

    final Hash<X>Set other = new Hash<X>Set(100);
    addTwoElements(other);

    other.removeAll(testSet);
    assertEquals(0, other.size());
  }

  @Test
  public void differenceReturnsSetDifference() {
    addTwoElements(testSet);

    final Hash<X>Set other = new Hash<X>Set(100);
    other.add((<y>)1);

    testSet.removeAll(other);
    assertTrue(testSet.contains((<y>)101));
  }

  @Test
  public void copiesOtherHash<X>Set() {
    addTwoElements(testSet);

    final Hash<X>Set other = new Hash<X>Set(testSet);
    assertContainsElements(other);
  }

  @Test
  public void twoEmptySetsAreEqual() {
    Assert.assertEquals(testSet, new Hash<X>Set(100));
  }

  @Test
  public void setsWithTheSameValuesAreEqual() {
    final Hash<X>Set that = new Hash<X>Set(100);

    addTwoElements(testSet);
    addTwoElements(that);

    Assert.assertEquals(testSet, that);
  }

  @Test
  public void setsWithTheDifferentSizesAreNotEqual() {
    final Hash<X>Set that = new Hash<X>Set(100);

    addTwoElements(testSet);
    that.add((<y>)101);

    assertNotEquals(testSet, that);
  }

  @Test
  public void setsWithTheDifferentValuesAreNotEqual() {
    final Hash<X>Set that = new Hash<X>Set(100);

    addTwoElements(testSet);
    that.add((<y>)2);
    that.add((<y>)101);

    assertNotEquals(testSet, that);
  }

  @Test
  public void twoEmptySetsHaveTheSameHashcode() {
    assertEquals(testSet.hashCode(), new Hash<X>Set(100).hashCode());
  }

  @Test
  public void setsWithTheSameValuesHaveTheSameHashcode() {
    final Hash<X>Set other = new Hash<X>Set(100);

    addTwoElements(testSet);
    addTwoElements(other);

    assertEquals(testSet.hashCode(), other.hashCode());
  }

  @Test
  public void reducesSizeWhenElementRemoved() {
    addTwoElements(testSet);
    testSet.remove((<y>)101);

    assertEquals(1, testSet.size());
  }

  @Test(expected = NullPointerException.class)
  public void toArrayThrowsNullPointerExceptionForNullArgument() {
    final <Y>[] a = null;
    testSet.toArray(a);
  }

  @Test
  public void toArrayCopiesElements<Y>oSufficientlySizedArray() {
    addTwoElements(testSet);
    final <Y>[] result = testSet.toArray(new <Y>[testSet.size()]);

    assertArrayContainingElements(result);
  }

  @Test
  public void toArrayCopiesElements<Y>oNewArray() {
    addTwoElements(testSet);
    final <Y>[] result = testSet.toArray(new <Y>[testSet.size()]);

    assertArrayContainingElements(result);
  }

  @Test
  public void toArraySupportsEmptyCollection() {
    final <Y>[] result = testSet.toArray(new <Y>[testSet.size()]);

    Assert.assertArrayEquals(result, new <Y>[] {});
  }

  // Test case from usage bug.
  @Test
  public void chainCompactionShouldNotCauseElementsToBeMovedBeforeTheirHash() {
    final Hash<X>Set requiredFields = new Hash<X>Set(14);

    requiredFields.add((<y>)8);
    requiredFields.add((<y>)9);
    requiredFields.add((<y>)35);
    requiredFields.add((<y>)49);
    requiredFields.add((<y>)56);

    assertTrue("Failed to remove 8", requiredFields.remove((<y>)8));
    assertTrue("Failed to remove 9", requiredFields.remove((<y>)9));

    assertTrue(requiredFields.containsAll(Arrays.asList((<y>)35, (<y>)49, (<y>)56)));
  }

  @Test
  public void shouldResizeWhenItHitsCapacity() {
    for (<y> i = 0; i < 2 * INITIAL_CAPACITY - 1; ++i)
      assertTrue(testSet.add(i));

    for (<y> i = 0; i < 2 * INITIAL_CAPACITY - 1; ++i)
      assertTrue(testSet.contains(i));
  }

  @Test
  public void containsEmptySet() {
    assertTrue(testSet.containsAll(new Hash<X>Set(100)));
  }

  @Test
  public void containsSubset() {
    addTwoElements(testSet);

    final Hash<X>Set subset = new Hash<X>Set(100);
    subset.add((<y>)1);

    assertTrue(testSet.containsAll(subset));
  }

  @Test
  public void doesNotContainDisjointSet() {
    addTwoElements(testSet);

    final Hash<X>Set disjoint = new Hash<X>Set(100);
    disjoint.add((<y>)1);
    disjoint.add((<y>)102);

    assertFalse(testSet.containsAll(disjoint));
  }

  @Test
  public void doesNotContainSuperset() {
    addTwoElements(testSet);

    final Hash<X>Set superset = new Hash<X>Set(100);
    addTwoElements(superset);
    superset.add((<y>)15);

    assertFalse(testSet.containsAll(superset));
  }

  @Test
  public void addingEmptySetDoesNothing() {
    addTwoElements(testSet);

    assertFalse(testSet.addAll(new Hash<X>Set(100)));
    assertFalse(testSet.addAll(new HashSet<>()));
    assertContainsElements(testSet);
  }

  @Test
  public void containsValuesAddedFromDisjointSetPrimitive() {
    addTwoElements(testSet);

    final Hash<X>Set disjoint = new Hash<X>Set(100);

    disjoint.add((<y>)2);
    disjoint.add((<y>)102);

    assertTrue(testSet.addAll(disjoint));
    assertTrue(testSet.contains((<y>)1));
    assertTrue(testSet.contains((<y>)101));
    assertTrue(testSet.containsAll(disjoint));
  }

  @Test
  public void containsValuesAddedFromDisjointSet() {
    addTwoElements(testSet);

    final HashSet<<Y>> disjoint = new HashSet<>();

    disjoint.add((<y>)2);
    disjoint.add((<y>)102);

    assertTrue(testSet.addAll(disjoint));
    assertTrue(testSet.contains((<y>)1));
    assertTrue(testSet.contains((<y>)101));
    assertTrue(testSet.containsAll(disjoint));
  }

  @Test
  public void containsValuesAddedFrom<Y>ersectingSetPrimitive() {
    addTwoElements(testSet);

    final Hash<X>Set intersecting = new Hash<X>Set(100);
    intersecting.add((<y>)1);
    intersecting.add((<y>)102);

    assertTrue(testSet.addAll(intersecting));
    assertTrue(testSet.contains((<y>)1));
    assertTrue(testSet.contains((<y>)101));
    assertTrue(testSet.containsAll(intersecting));
  }

  @Test
  public void containsValuesAddedFrom<Y>ersectingSet() {
    addTwoElements(testSet);

    final HashSet<<Y>> intersecting = new HashSet<>();

    intersecting.add((<y>)1);
    intersecting.add((<y>)102);

    assertTrue(testSet.addAll(intersecting));
    assertTrue(testSet.contains((<y>)1));
    assertTrue(testSet.contains((<y>)101));
    assertTrue(testSet.containsAll(intersecting));
  }

  @Test
  public void removingEmptySetDoesNothing() {
    addTwoElements(testSet);

    assertFalse(testSet.removeAll(new Hash<X>Set(100)));
    assertFalse(testSet.removeAll(new HashSet<>()));
    assertContainsElements(testSet);
  }

  @Test
  public void removingDisjointSetDoesNothing() {
    addTwoElements(testSet);

    final Hash<X>Set disjoint = new Hash<X>Set(100);
    disjoint.add((<y>)2);
    disjoint.add((<y>)102);

    assertFalse(testSet.removeAll(disjoint));
    assertFalse(testSet.removeAll(new HashSet<>()));
    assertContainsElements(testSet);
  }

  @Test
  public void doesNotContainRemoved<Y>ersectingSetPrimitive() {
    addTwoElements(testSet);

    final Hash<X>Set intersecting = new Hash<X>Set(100);

    intersecting.add((<y>)1);
    intersecting.add((<y>)102);

    assertTrue(testSet.removeAll(intersecting));
    assertTrue(testSet.contains((<y>)101));
    assertFalse(testSet.containsAll(intersecting));
  }

  @Test
  public void doesNotContainRemoved<Y>ersectingSet() {
    addTwoElements(testSet);

    final HashSet<<Y>> intersecting = new HashSet<>();
    intersecting.add((<y>)1);
    intersecting.add((<y>)102);

    assertTrue(testSet.removeAll(intersecting));
    assertTrue(testSet.contains((<y>)101));
    assertFalse(testSet.containsAll(intersecting));
  }

  @Test
  public void isEmptyAfterRemovingEqualSetPrimitive() {
    addTwoElements(testSet);

    final Hash<X>Set equal = new Hash<X>Set(100);
    addTwoElements(equal);

    assertTrue(testSet.removeAll(equal));
    assertTrue(testSet.isEmpty());
  }

  @Test
  public void isEmptyAfterRemovingEqualSet() {
    addTwoElements(testSet);

    final HashSet<<Y>> equal = new HashSet<>();
    addTwoElements(equal);

    assertTrue(testSet.removeAll(equal));
    assertTrue(testSet.isEmpty());
  }

  @Test
  public void removeElementsFromIterator() {
    addTwoElements(testSet);

    final <X>Iterator iterator = testSet.iterator();
    while (iterator.hasNext())
      if (iterator.next() == 1)
        iterator.remove();

    assertEquals(1, testSet.size());
    assertTrue(testSet.contains((<y>)101));
  }

  @Test
  public void shouldNotContainMissingValueInitially() {
    assertFalse(testSet.contains(Hash<X>Set.NULL));
  }

  @Test
  public void shouldAllowMissingValue() {
    assertTrue(testSet.add(Hash<X>Set.NULL));
    assertTrue(testSet.contains(Hash<X>Set.NULL));
    assertFalse(testSet.add(Hash<X>Set.NULL));
  }

  @Test
  public void shouldAllowRemovalOfMissingValue() {
    assertTrue(testSet.add(Hash<X>Set.NULL));
    assertTrue(testSet.remove(Hash<X>Set.NULL));
    assertFalse(testSet.contains(Hash<X>Set.NULL));
    assertFalse(testSet.remove(Hash<X>Set.NULL));
  }

  @Test
  public void sizeAccountsForMissingValue() {
    testSet.add((<y>)1);
    testSet.add(Hash<X>Set.NULL);

    assertEquals(2, testSet.size());
  }

  @Test
  public void toArrayCopiesElements<Y>oNewArrayIncludingMissingValue() {
    addTwoElements(testSet);
    testSet.add(Hash<X>Set.NULL);

    final <Y>[] result = testSet.toArray(new <Y>[testSet.size()]);
    assertTrue(Arrays.asList(result).containsAll(Arrays.asList((<y>)1, (<y>)101, Hash<X>Set.NULL)));
  }

  @Test
  public void toObjectArrayCopiesElements<Y>oNewArrayIncludingMissingValue() {
    addTwoElements(testSet);
    testSet.add(Hash<X>Set.NULL);

    final <y>[] result = testSet.toArray();
    Arrays.sort(result);
    assertArrayEquals(new <y>[] {Hash<X>Set.NULL, 1, 101}, result);
  }

  @Test
  public void equalsAccountsForMissingValue() {
    addTwoElements(testSet);
    testSet.add(Hash<X>Set.NULL);

    final Hash<X>Set other = new Hash<X>Set(100);
    addTwoElements(other);

    assertNotEquals(testSet, other);

    other.add(Hash<X>Set.NULL);
    Assert.assertEquals(testSet, other);

    testSet.remove(Hash<X>Set.NULL);

    assertNotEquals(testSet, other);
  }

  @Test
  public void consecutiveValuesShouldBeCorrectlyStored() {
    for (<y> i = 0; i < 127; ++i)
      testSet.add(i);

    assertEquals(127, testSet.size());

    int distinctElements = 0;
    for (final <X>Iterator i = testSet.iterator(); i.hasNext(); i.next())
      ++distinctElements;

    assertEquals(distinctElements, 127);
  }

  @Test
  public void hashCodeAccountsForMissingValue() {
    addTwoElements(testSet);
    testSet.add(Hash<X>Set.NULL);

    final Hash<X>Set other = new Hash<X>Set(100);
    addTwoElements(other);

    other.add(Hash<X>Set.NULL);
    assertEquals(testSet.hashCode(), other.hashCode());
  }

  @Test
  public void iteratorAccountsForMissingValue() {
    addTwoElements(testSet);
    testSet.add(Hash<X>Set.NULL);

    int missingValueCount = 0;
    final <X>Iterator iterator = testSet.iterator();
    while (iterator.hasNext())
      if (iterator.next() == Hash<X>Set.NULL)
        ++missingValueCount;

    assertEquals(1, missingValueCount);
  }

  @Test
  public void iteratorCanRemoveMissingValue() {
    addTwoElements(testSet);
    testSet.add(Hash<X>Set.NULL);

    final <X>Iterator iterator = testSet.iterator();
    while (iterator.hasNext())
      if (iterator.next() == Hash<X>Set.NULL)
        iterator.remove();

    assertFalse(testSet.contains(Hash<X>Set.NULL));
  }

  @Test
  public void shouldGenerateStringRepresentation() {
    final <y>[] testEntries = {(<y>)65, (<y>)68, (<y>)83, (<y>)104, (<y>)111, (<y>)75, (<y>)83, (<y>)97};
    for (final <y> testEntry : testEntries)
      testSet.add(testEntry);

    final String string = testSet.toString();
    final String[] parts = string.substring(1, string.length() - 1).replace(" ", "").split(",");
    final HashSet<String> strings = CollectionUtil.asCollection(new HashSet<>(testSet.size()), parts);
    for (final <y> testEntry : testEntries)
      assertTrue(Arrays.toString(parts), strings.contains(String.valueOf(testEntry)));
  }

  @Test
  public void shouldRemoveMissingValueWhenCleared() {
    assertTrue(testSet.add(Hash<X>Set.NULL));

    testSet.clear();

    assertFalse(testSet.contains(Hash<X>Set.NULL));
  }

  @Test
  public void shouldHaveCompatibleEqualsAndHashcode() {
    final HashSet<<Y>> compatibleSet = new HashSet<>();
    final long seed = System.nanoTime();
    final Random r = new Random(seed);
    for (int i = 0; i < 1024; ++i) {
      final <y> value = (<y>)r.nextInt();
      compatibleSet.add(value);
      testSet.add(value);
    }

    if (r.nextBoolean()) {
      compatibleSet.add(Hash<X>Set.NULL);
      testSet.add(Hash<X>Set.NULL);
    }

    assertTrue("Fail with seed:" + seed, testSet.size() == compatibleSet.size() && testSet.containsAll(compatibleSet));
    Assert.assertEquals("Fail with seed:" + seed, compatibleSet.hashCode(), testSet.hashCode());
  }

  private static void addTwoElements(final Hash<X>Set obj) {
    obj.add((<y>)1);
    obj.add((<y>)101);
  }

  private static void addTwoElements(final HashSet<<Y>> obj) {
    obj.add((<y>)1);
    obj.add((<y>)101);
  }

  private void assertIteratorHasElements() {
    final <X>Iterator iterator = testSet.iterator();

    final Set<<Y>> values = new HashSet<>();

    assertTrue(iterator.hasNext());
    values.add(iterator.next());
    assertTrue(iterator.hasNext());
    values.add(iterator.next());
    assertFalse(iterator.hasNext());

    assertContainsElements(values);
  }

  private void assertIteratorHasElementsWithoutHasNext() {
    final <X>Iterator iterator = testSet.iterator();
    final Set<<Y>> values = new HashSet<>();

    values.add(iterator.next());
    values.add(iterator.next());

    assertContainsElements(values);
  }

  private static void assertArrayContainingElements(final <Y>[] result) {
    assertTrue(Arrays.asList(result).containsAll(Arrays.asList((<y>)1, (<y>)101)));
  }

  private static void assertContainsElements(final Set<<Y>> other) {
    assertTrue(other.containsAll(Arrays.asList((<y>)1, (<y>)101)));
  }

  private static void assertContainsElements(final <X>Set other) {
    assertTrue(other.containsAll(Arrays.asList((<y>)1, (<y>)101)));
  }

  private void exhaustIterator() {
    final <X>Iterator iterator = testSet.iterator();
    iterator.next();
    iterator.next();
    iterator.next();
  }
}