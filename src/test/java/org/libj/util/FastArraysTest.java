/* Copyright (c) 2016 LibJ
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

import static org.junit.Assert.*;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.junit.Test;

public class FastArraysTest {
  private static Object[] createRandomNestedArray() {
    final Object[] array = new Object[4];
    for (int i = 0; i < array.length; i++)
      array[i] = Math.random() < 0.2 ? createRandomNestedArray() : Strings.getRandomAlpha(4);

    return array;
  }

  @Test
  public void testFlattenArrayRetainingReferences() {
    final Object[] array = {"a", "b", new Object[] {"c", "d"}, "e", new Object[] {"f", new Object[] {"g", "h"}}, "i"};
    final Object[] result = FastArrays.flatten(array);
    assertEquals("[" + java.util.Arrays.deepToString(array).replace("[", "").replace("]", "") + "]", java.util.Arrays.deepToString(result).toString());

    final Object[] expected = {array[0], array[1], array[2], ((Object[])array[2])[0], ((Object[])array[2])[1], array[3], array[4], ((Object[])array[4])[0], ((Object[])array[4])[1], ((Object[])((Object[])array[4])[1])[0], ((Object[])((Object[])array[4])[1])[1], array[5]};
    final Object[] resultRetainingReferences = FastArrays.flatten(array, true);
    assertArrayEquals(expected, resultRetainingReferences);
  }

  @Test
  public void testFlattenArray() {
    for (int i = 0; i < 100; i++) {
      final Object[] array = createRandomNestedArray();
      final Object[] result = FastArrays.flatten(array);
      assertEquals("[" + java.util.Arrays.deepToString(array).replace("[", "").replace("]", "") + "]", java.util.Arrays.deepToString(result).toString());
    }
  }

  @Test
  public void testBinaryClosestSearch() {
    final int[] sorted = new int[] {1, 3, 5, 9, 19};
    try {
      FastArrays.binaryClosestSearch(sorted, 4, 3, -10);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    assertEquals(0, FastArrays.binaryClosestSearch(sorted, -10));
    assertEquals(0, FastArrays.binaryClosestSearch(sorted, -1));
    assertEquals(0, FastArrays.binaryClosestSearch(sorted, 0));
    assertEquals(1, FastArrays.binaryClosestSearch(sorted, 2));
    assertEquals(2, FastArrays.binaryClosestSearch(sorted, 4));
    assertEquals(3, FastArrays.binaryClosestSearch(sorted, 6));
    assertEquals(3, FastArrays.binaryClosestSearch(sorted, 9));
    assertEquals(4, FastArrays.binaryClosestSearch(sorted, 10));
    assertEquals(4, FastArrays.binaryClosestSearch(sorted, 14));
    assertEquals(4, FastArrays.binaryClosestSearch(sorted, 18));
    assertEquals(4, FastArrays.binaryClosestSearch(sorted, 19));
    assertEquals(5, FastArrays.binaryClosestSearch(sorted, 20));
    assertEquals(5, FastArrays.binaryClosestSearch(sorted, 40));
  }

  @Test
  public void testTransform() {
    assertArrayEquals(new String[] {"ONE", "TWO", "THREE"}, FastArrays.<String>replaceAll(new UnaryOperator<String>() {
      @Override
      public String apply(final String value) {
        return value.toUpperCase();
      }
    }, new String[] {"one", "two", "three"}));

    assertArrayEquals(new String[] {}, FastArrays.<String>replaceAll(new UnaryOperator<String>() {
      @Override
      public String apply(final String value) {
        return value.toUpperCase();
      }
    }, new String[] {}));
  }

  @Test
  public void testFilter() {
    final String[] expected = new String[] {"ONE", "TWO", "THREE"};
    final String[] filtered = FastArrays.filter(new Predicate<String>() {
      @Override
      public boolean test(final String value) {
        return value != null;
      }
    }, new String[] {"ONE", null, "TWO", null, "THREE"});
    assertArrayEquals(expected, filtered);
  }

  @Test
  public void testConcat() {
    final String[] one = new String[] {"a", "b", "c"};
    final String[] two = new String[] {"d", "e", "f"};
    final String[] concat = FastArrays.concat(one, two);
    assertArrayEquals(new String[] {"a", "b", "c", "d", "e", "f"}, concat);
  }

  @Test
  public void testSubArray() {
    final String[] array = new String[] {"a", "b", "c", "d", "e", "f"};
    assertArrayEquals(new String[] {"c", "d", "e"}, FastArrays.subArray(array, 2, 5));
    assertArrayEquals(new String[] {"c", "d", "e", "f"}, FastArrays.subArray(array, 2));
  }

  @Test
  public void testSplice() {
    final String[] array = new String[] {"a", "b", "c", "d", "e", "f"};

    assertArrayEquals(new String[] {"c", "d", "e", "f"}, FastArrays.splice(array, 0, 2));
    assertArrayEquals(new String[] {"a", "d", "e", "f"}, FastArrays.splice(array, 1, 2));
    assertArrayEquals(new String[] {"a", "b", "e", "f"}, FastArrays.splice(array, 2, 2));
    assertArrayEquals(new String[] {"a", "b", "c", "f"}, FastArrays.splice(array, 3, 2));
    assertArrayEquals(new String[] {"a", "b", "c", "d"}, FastArrays.splice(array, 4, 2));
    try {
      FastArrays.splice(array, 5, 2);
      fail("Expected ArrayIndexOutOfBoundsException");
    }
    catch (final ArrayIndexOutOfBoundsException e) {
    }

    try {
      FastArrays.splice(array, -2, 3);
      fail("Expected ArrayIndexOutOfBoundsException");
    }
    catch (final ArrayIndexOutOfBoundsException e) {
    }
    assertArrayEquals(new String[] {"a", "b", "c", "d"}, FastArrays.splice(array, -2, 2));
    assertArrayEquals(new String[] {"a", "b", "c", "f"}, FastArrays.splice(array, -3, 2));
    assertArrayEquals(new String[] {"a", "b", "e", "f"}, FastArrays.splice(array, -4, 2));
    assertArrayEquals(new String[] {"a", "d", "e", "f"}, FastArrays.splice(array, -5, 2));
    assertArrayEquals(new String[] {"c", "d", "e", "f"}, FastArrays.splice(array, -6, 2));
    try {
      FastArrays.splice(array, -7, 2);
      fail("Expected ArrayIndexOutOfBoundsException");
    }
    catch (final ArrayIndexOutOfBoundsException e) {
    }

    assertArrayEquals(new String[] {}, FastArrays.splice(array, 0));
    assertArrayEquals(new String[] {"a"}, FastArrays.splice(array, 1));
    assertArrayEquals(new String[] {"a", "b"}, FastArrays.splice(array, 2));
    assertArrayEquals(new String[] {"a", "b", "c"}, FastArrays.splice(array, 3));
    assertArrayEquals(new String[] {"a", "b", "c", "d"}, FastArrays.splice(array, 4));
    assertArrayEquals(new String[] {"a", "b", "c", "d", "e"}, FastArrays.splice(array, 5));
    assertArrayEquals(new String[] {"a", "b", "c", "d", "e", "f"}, FastArrays.splice(array, 6));
    try {
      FastArrays.splice(array, 7);
      fail("Expected ArrayIndexOutOfBoundsException");
    }
    catch (final ArrayIndexOutOfBoundsException e) {
    }

    assertArrayEquals(new String[] {"a", "b", "c", "d", "e"}, FastArrays.splice(array, -1));
    assertArrayEquals(new String[] {"a", "b", "c", "d"}, FastArrays.splice(array, -2));
    assertArrayEquals(new String[] {"a", "b", "c"}, FastArrays.splice(array, -3));
    assertArrayEquals(new String[] {"a", "b"}, FastArrays.splice(array, -4));
    assertArrayEquals(new String[] {"a"}, FastArrays.splice(array, -5));
    assertArrayEquals(new String[] {}, FastArrays.splice(array, -6));
    try {
      FastArrays.splice(array, -7);
      fail("Expected NegativeArraySizeException");
    }
    catch (final NegativeArraySizeException e) {
    }

    assertArrayEquals(new String[] {"x", "y", "z", "c", "d", "e", "f"}, FastArrays.splice(array, 0, 2, "x", "y", "z"));
    assertArrayEquals(new String[] {"a", "x", "y", "z", "d", "e", "f"}, FastArrays.splice(array, 1, 2, "x", "y", "z"));
    assertArrayEquals(new String[] {"a", "b", "x", "y", "z", "e", "f"}, FastArrays.splice(array, 2, 2, "x", "y", "z"));
    assertArrayEquals(new String[] {"a", "b", "c", "x", "y", "z", "f"}, FastArrays.splice(array, 3, 2, "x", "y", "z"));
    assertArrayEquals(new String[] {"a", "b", "c", "d", "x", "y", "z"}, FastArrays.splice(array, 4, 2, "x", "y", "z"));
    try {
      FastArrays.splice(array, 5, 2, "x", "y", "z");
      fail("Expected ArrayIndexOutOfBoundsException");
    }
    catch (final ArrayIndexOutOfBoundsException e) {
    }

    assertArrayEquals(new String[] {"a", "b", "c", "d", "x", "y", "z"}, FastArrays.splice(array, -2, 2, "x", "y", "z"));
    assertArrayEquals(new String[] {"a", "b", "c", "x", "y", "z", "f"}, FastArrays.splice(array, -3, 2, "x", "y", "z"));
    assertArrayEquals(new String[] {"a", "b", "x", "y", "z", "e", "f"}, FastArrays.splice(array, -4, 2, "x", "y", "z"));
    assertArrayEquals(new String[] {"a", "x", "y", "z", "d", "e", "f"}, FastArrays.splice(array, -5, 2, "x", "y", "z"));
    assertArrayEquals(new String[] {"x", "y", "z", "c", "d", "e", "f"}, FastArrays.splice(array, -6, 2, "x", "y", "z"));
    try {
      FastArrays.splice(array, -7, 2, "x", "y", "z");
      fail("Expected ArrayIndexOutOfBoundsException");
    }
    catch (final ArrayIndexOutOfBoundsException e) {
    }
  }

  @Test
  public void testCreateRepeat() {
    final String[] array = FastArrays.createRepeat("hello", 8);
    assertArrayEquals(new String[] {"hello", "hello", "hello", "hello", "hello", "hello", "hello", "hello"}, array);
  }
}