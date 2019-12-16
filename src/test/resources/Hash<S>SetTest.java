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
public class Hash<S>SetTest extends PrimitiveCollectionTest {
  private static final int INITIAL_CAPACITY = 64;

  private final Hash<S>Set testSet = new Hash<S>Set(INITIAL_CAPACITY);

  @Test
  public void initiallyContainsNoElements() {
    for (<t> i = 0; i < 100; ++i)
      assertFalse(testSet.contains(i));
  }

  @Test
  public void initiallyContainsNoBoxedElements() {
    for (<t> i = 0; i < 100; ++i)
      assertFalse(testSet.contains(<T>.valueOf(i)));
  }

  @Test
  public void containsAddedElement() {
    assertTrue(testSet.add((<t>)1));
    assertTrue(testSet.contains((<t>)1));
  }

  @Test
  public void addingAnElementTwiceDoesNothing() {
    assertTrue(testSet.add((<t>)1));
    assertFalse(testSet.add((<t>)1));
  }

  @Test
  public void containsAddedBoxedElements() {
    assertTrue(testSet.add((<t>)1));
    assertTrue(testSet.add(<T>.valueOf((<t>)2)));

    assertTrue(testSet.contains(<T>.valueOf((<t>)1)));
    assertTrue(testSet.contains((<t>)2));
  }

  @Test
  public void removingAnElementFromAnEmptyListDoesNothing() {
    assertFalse(testSet.remove((<t>)0));
  }

  @Test
  public void removingAPresentElementRemovesIt() {
    assertTrue(testSet.add((<t>)1));
    assertTrue(testSet.remove((<t>)1));
    assertFalse(testSet.contains((<t>)1));
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
    testSet.add((<t>)1);
    testSet.add((<t>)1);

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
    assertFalse(testSet.contains((<t>)1));
    assertFalse(testSet.contains((<t>)101));
  }

  @Test
  public void differenceReturnsNullIfBothSetsEqual() {
    addTwoElements(testSet);

    final Hash<S>Set other = new Hash<S>Set(100);
    addTwoElements(other);

    other.removeAll(testSet);
    assertEquals(0, other.size());
  }

  @Test
  public void differenceReturnsSetDifference() {
    addTwoElements(testSet);

    final Hash<S>Set other = new Hash<S>Set(100);
    other.add((<t>)1);

    testSet.removeAll(other);
    assertTrue(testSet.contains((<t>)101));
  }

  @Test
  public void copiesOtherHash<S>Set() {
    addTwoElements(testSet);

    final Hash<S>Set other = new Hash<S>Set(testSet);
    assertContainsElements(other);
  }

  @Test
  public void twoEmptySetsAreEqual() {
    Assert.assertEquals(testSet, new Hash<S>Set(100));
  }

  @Test
  public void setsWithTheSameValuesAreEqual() {
    final Hash<S>Set that = new Hash<S>Set(100);

    addTwoElements(testSet);
    addTwoElements(that);

    Assert.assertEquals(testSet, that);
  }

  @Test
  public void setsWithTheDifferentSizesAreNotEqual() {
    final Hash<S>Set that = new Hash<S>Set(100);

    addTwoElements(testSet);
    that.add((<t>)101);

    assertNotEquals(testSet, that);
  }

  @Test
  public void setsWithTheDifferentValuesAreNotEqual() {
    final Hash<S>Set that = new Hash<S>Set(100);

    addTwoElements(testSet);
    that.add((<t>)2);
    that.add((<t>)101);

    assertNotEquals(testSet, that);
  }

  @Test
  public void twoEmptySetsHaveTheSameHashcode() {
    assertEquals(testSet.hashCode(), new Hash<S>Set(100).hashCode());
  }

  @Test
  public void setsWithTheSameValuesHaveTheSameHashcode() {
    final Hash<S>Set other = new Hash<S>Set(100);

    addTwoElements(testSet);
    addTwoElements(other);

    assertEquals(testSet.hashCode(), other.hashCode());
  }

  @Test
  public void reducesSizeWhenElementRemoved() {
    addTwoElements(testSet);
    testSet.remove((<t>)101);

    assertEquals(1, testSet.size());
  }

  @Test(expected = NullPointerException.class)
  public void toArrayThrowsNullPointerExceptionForNullArgument() {
    final <T>[] a = null;
    testSet.toArray(a);
  }

  @Test
  public void toArrayCopiesElements<T>oSufficientlySizedArray() {
    addTwoElements(testSet);
    final <T>[] result = testSet.toArray(new <T>[testSet.size()]);

    assertArrayContainingElements(result);
  }

  @Test
  public void toArrayCopiesElements<T>oNewArray() {
    addTwoElements(testSet);
    final <T>[] result = testSet.toArray(new <T>[testSet.size()]);

    assertArrayContainingElements(result);
  }

  @Test
  public void toArraySupportsEmptyCollection() {
    final <T>[] result = testSet.toArray(new <T>[testSet.size()]);

    Assert.assertArrayEquals(result, new <T>[] {});
  }

  // Test case from usage bug.
  @Test
  public void chainCompactionShouldNotCauseElementsToBeMovedBeforeTheirHash() {
    final Hash<S>Set requiredFields = new Hash<S>Set(14);

    requiredFields.add((<t>)8);
    requiredFields.add((<t>)9);
    requiredFields.add((<t>)35);
    requiredFields.add((<t>)49);
    requiredFields.add((<t>)56);

    assertTrue("Failed to remove 8", requiredFields.remove((<t>)8));
    assertTrue("Failed to remove 9", requiredFields.remove((<t>)9));

    assertTrue(requiredFields.containsAll(Arrays.asList((<t>)35, (<t>)49, (<t>)56)));
  }

  @Test
  public void shouldResizeWhenItHitsCapacity() {
    for (<t> i = 0; i < 2 * INITIAL_CAPACITY - 1; ++i)
      assertTrue(testSet.add(i));

    for (<t> i = 0; i < 2 * INITIAL_CAPACITY - 1; ++i)
      assertTrue(testSet.contains(i));
  }

  @Test
  public void containsEmptySet() {
    assertTrue(testSet.containsAll(new Hash<S>Set(100)));
  }

  @Test
  public void containsSubset() {
    addTwoElements(testSet);

    final Hash<S>Set subset = new Hash<S>Set(100);
    subset.add((<t>)1);

    assertTrue(testSet.containsAll(subset));
  }

  @Test
  public void doesNotContainDisjointSet() {
    addTwoElements(testSet);

    final Hash<S>Set disjoint = new Hash<S>Set(100);
    disjoint.add((<t>)1);
    disjoint.add((<t>)102);

    assertFalse(testSet.containsAll(disjoint));
  }

  @Test
  public void doesNotContainSuperset() {
    addTwoElements(testSet);

    final Hash<S>Set superset = new Hash<S>Set(100);
    addTwoElements(superset);
    superset.add((<t>)15);

    assertFalse(testSet.containsAll(superset));
  }

  @Test
  public void addingEmptySetDoesNothing() {
    addTwoElements(testSet);

    assertFalse(testSet.addAll(new Hash<S>Set(100)));
    assertFalse(testSet.addAll(new HashSet<>()));
    assertContainsElements(testSet);
  }

  @Test
  public void containsValuesAddedFromDisjointSetPrimitive() {
    addTwoElements(testSet);

    final Hash<S>Set disjoint = new Hash<S>Set(100);

    disjoint.add((<t>)2);
    disjoint.add((<t>)102);

    assertTrue(testSet.addAll(disjoint));
    assertTrue(testSet.contains((<t>)1));
    assertTrue(testSet.contains((<t>)101));
    assertTrue(testSet.containsAll(disjoint));
  }

  @Test
  public void containsValuesAddedFromDisjointSet() {
    addTwoElements(testSet);

    final HashSet<<T>> disjoint = new HashSet<>();

    disjoint.add((<t>)2);
    disjoint.add((<t>)102);

    assertTrue(testSet.addAll(disjoint));
    assertTrue(testSet.contains((<t>)1));
    assertTrue(testSet.contains((<t>)101));
    assertTrue(testSet.containsAll(disjoint));
  }

  @Test
  public void containsValuesAddedFrom<T>ersectingSetPrimitive() {
    addTwoElements(testSet);

    final Hash<S>Set intersecting = new Hash<S>Set(100);
    intersecting.add((<t>)1);
    intersecting.add((<t>)102);

    assertTrue(testSet.addAll(intersecting));
    assertTrue(testSet.contains((<t>)1));
    assertTrue(testSet.contains((<t>)101));
    assertTrue(testSet.containsAll(intersecting));
  }

  @Test
  public void containsValuesAddedFrom<T>ersectingSet() {
    addTwoElements(testSet);

    final HashSet<<T>> intersecting = new HashSet<>();

    intersecting.add((<t>)1);
    intersecting.add((<t>)102);

    assertTrue(testSet.addAll(intersecting));
    assertTrue(testSet.contains((<t>)1));
    assertTrue(testSet.contains((<t>)101));
    assertTrue(testSet.containsAll(intersecting));
  }

  @Test
  public void removingEmptySetDoesNothing() {
    addTwoElements(testSet);

    assertFalse(testSet.removeAll(new Hash<S>Set(100)));
    assertFalse(testSet.removeAll(new HashSet<>()));
    assertContainsElements(testSet);
  }

  @Test
  public void removingDisjointSetDoesNothing() {
    addTwoElements(testSet);

    final Hash<S>Set disjoint = new Hash<S>Set(100);
    disjoint.add((<t>)2);
    disjoint.add((<t>)102);

    assertFalse(testSet.removeAll(disjoint));
    assertFalse(testSet.removeAll(new HashSet<>()));
    assertContainsElements(testSet);
  }

  @Test
  public void doesNotContainRemoved<T>ersectingSetPrimitive() {
    addTwoElements(testSet);

    final Hash<S>Set intersecting = new Hash<S>Set(100);

    intersecting.add((<t>)1);
    intersecting.add((<t>)102);

    assertTrue(testSet.removeAll(intersecting));
    assertTrue(testSet.contains((<t>)101));
    assertFalse(testSet.containsAll(intersecting));
  }

  @Test
  public void doesNotContainRemoved<T>ersectingSet() {
    addTwoElements(testSet);

    final HashSet<<T>> intersecting = new HashSet<>();
    intersecting.add((<t>)1);
    intersecting.add((<t>)102);

    assertTrue(testSet.removeAll(intersecting));
    assertTrue(testSet.contains((<t>)101));
    assertFalse(testSet.containsAll(intersecting));
  }

  @Test
  public void isEmptyAfterRemovingEqualSetPrimitive() {
    addTwoElements(testSet);

    final Hash<S>Set equal = new Hash<S>Set(100);
    addTwoElements(equal);

    assertTrue(testSet.removeAll(equal));
    assertTrue(testSet.isEmpty());
  }

  @Test
  public void isEmptyAfterRemovingEqualSet() {
    addTwoElements(testSet);

    final HashSet<<T>> equal = new HashSet<>();
    addTwoElements(equal);

    assertTrue(testSet.removeAll(equal));
    assertTrue(testSet.isEmpty());
  }

  @Test
  public void removeElementsFromIterator() {
    addTwoElements(testSet);

    final <S>Iterator iterator = testSet.iterator();
    while (iterator.hasNext())
      if (iterator.next() == 1)
        iterator.remove();

    assertEquals(1, testSet.size());
    assertTrue(testSet.contains((<t>)101));
  }

  @Test
  public void shouldNotContainMissingValueInitially() {
    assertFalse(testSet.contains(Hash<S>Set.NULL));
  }

  @Test
  public void shouldAllowMissingValue() {
    assertTrue(testSet.add(Hash<S>Set.NULL));
    assertTrue(testSet.contains(Hash<S>Set.NULL));
    assertFalse(testSet.add(Hash<S>Set.NULL));
  }

  @Test
  public void shouldAllowRemovalOfMissingValue() {
    assertTrue(testSet.add(Hash<S>Set.NULL));
    assertTrue(testSet.remove(Hash<S>Set.NULL));
    assertFalse(testSet.contains(Hash<S>Set.NULL));
    assertFalse(testSet.remove(Hash<S>Set.NULL));
  }

  @Test
  public void sizeAccountsForMissingValue() {
    testSet.add((<t>)1);
    testSet.add(Hash<S>Set.NULL);

    assertEquals(2, testSet.size());
  }

  @Test
  public void toArrayCopiesElements<T>oNewArrayIncludingMissingValue() {
    addTwoElements(testSet);
    testSet.add(Hash<S>Set.NULL);

    final <T>[] result = testSet.toArray(new <T>[testSet.size()]);
    assertTrue(Arrays.asList(result).containsAll(Arrays.asList((<t>)1, (<t>)101, Hash<S>Set.NULL)));
  }

  @Test
  public void toObjectArrayCopiesElements<T>oNewArrayIncludingMissingValue() {
    addTwoElements(testSet);
    testSet.add(Hash<S>Set.NULL);

    final <t>[] result = testSet.toArray();
    Arrays.sort(result);
    assertArrayEquals(new <t>[] {Hash<S>Set.NULL, 1, 101}, result);
  }

  @Test
  public void equalsAccountsForMissingValue() {
    addTwoElements(testSet);
    testSet.add(Hash<S>Set.NULL);

    final Hash<S>Set other = new Hash<S>Set(100);
    addTwoElements(other);

    assertNotEquals(testSet, other);

    other.add(Hash<S>Set.NULL);
    Assert.assertEquals(testSet, other);

    testSet.remove(Hash<S>Set.NULL);

    assertNotEquals(testSet, other);
  }

  @Test
  public void consecutiveValuesShouldBeCorrectlyStored() {
    for (<t> i = 0; i < 127; ++i)
      testSet.add(i);

    assertEquals(127, testSet.size());

    int distinctElements = 0;
    for (final <S>Iterator i = testSet.iterator(); i.hasNext(); i.next())
      ++distinctElements;

    assertEquals(distinctElements, 127);
  }

  @Test
  public void hashCodeAccountsForMissingValue() {
    addTwoElements(testSet);
    testSet.add(Hash<S>Set.NULL);

    final Hash<S>Set other = new Hash<S>Set(100);
    addTwoElements(other);

    other.add(Hash<S>Set.NULL);
    assertEquals(testSet.hashCode(), other.hashCode());
  }

  @Test
  public void iteratorAccountsForMissingValue() {
    addTwoElements(testSet);
    testSet.add(Hash<S>Set.NULL);

    int missingValueCount = 0;
    final <S>Iterator iterator = testSet.iterator();
    while (iterator.hasNext())
      if (iterator.next() == Hash<S>Set.NULL)
        ++missingValueCount;

    assertEquals(1, missingValueCount);
  }

  @Test
  public void iteratorCanRemoveMissingValue() {
    addTwoElements(testSet);
    testSet.add(Hash<S>Set.NULL);

    final <S>Iterator iterator = testSet.iterator();
    while (iterator.hasNext())
      if (iterator.next() == Hash<S>Set.NULL)
        iterator.remove();

    assertFalse(testSet.contains(Hash<S>Set.NULL));
  }

  @Test
  public void shouldGenerateStringRepresentation() {
    final <t>[] testEntries = {(<t>)65, (<t>)68, (<t>)83, (<t>)104, (<t>)111, (<t>)75, (<t>)83, (<t>)97};
    for (final <t> testEntry : testEntries)
      testSet.add(testEntry);

    final String string = testSet.toString();
    final String[] parts = string.substring(1, string.length() - 1).replace(" ", "").split(",");
    final HashSet<String> strings = CollectionUtil.asCollection(new HashSet<>(testSet.size()), parts);
    for (final <t> testEntry : testEntries)
      assertTrue(Arrays.toString(parts), strings.contains(String.valueOf(testEntry)));
  }

  @Test
  public void shouldRemoveMissingValueWhenCleared() {
    assertTrue(testSet.add(Hash<S>Set.NULL));

    testSet.clear();

    assertFalse(testSet.contains(Hash<S>Set.NULL));
  }

  @Test
  public void shouldHaveCompatibleEqualsAndHashcode() {
    final HashSet<<T>> compatibleSet = new HashSet<>();
    final long seed = System.nanoTime();
    final Random r = new Random(seed);
    for (int i = 0; i < 1024; ++i) {
      final <t> value = (<t>)r.nextInt();
      compatibleSet.add(value);
      testSet.add(value);
    }

    if (r.nextBoolean()) {
      compatibleSet.add(Hash<S>Set.NULL);
      testSet.add(Hash<S>Set.NULL);
    }

    assertTrue("Fail with seed:" + seed, testSet.size() == compatibleSet.size() && testSet.containsAll(compatibleSet));
    Assert.assertEquals("Fail with seed:" + seed, compatibleSet.hashCode(), testSet.hashCode());
  }

  private static void addTwoElements(final Hash<S>Set obj) {
    obj.add((<t>)1);
    obj.add((<t>)101);
  }

  private static void addTwoElements(final HashSet<<T>> obj) {
    obj.add((<t>)1);
    obj.add((<t>)101);
  }

  private void assertIteratorHasElements() {
    final <S>Iterator iterator = testSet.iterator();

    final Set<<T>> values = new HashSet<>();

    assertTrue(iterator.hasNext());
    values.add(iterator.next());
    assertTrue(iterator.hasNext());
    values.add(iterator.next());
    assertFalse(iterator.hasNext());

    assertContainsElements(values);
  }

  private void assertIteratorHasElementsWithoutHasNext() {
    final <S>Iterator iterator = testSet.iterator();
    final Set<<T>> values = new HashSet<>();

    values.add(iterator.next());
    values.add(iterator.next());

    assertContainsElements(values);
  }

  private static void assertArrayContainingElements(final <T>[] result) {
    assertTrue(Arrays.asList(result).containsAll(Arrays.asList((<t>)1, (<t>)101)));
  }

  private static void assertContainsElements(final Set<<T>> other) {
    assertTrue(other.containsAll(Arrays.asList((<t>)1, (<t>)101)));
  }

  private static void assertContainsElements(final <S>Set other) {
    assertTrue(other.containsAll(Arrays.asList((<t>)1, (<t>)101)));
  }

  private void exhaustIterator() {
    final <S>Iterator iterator = testSet.iterator();
    iterator.next();
    iterator.next();
    iterator.next();
  }
}