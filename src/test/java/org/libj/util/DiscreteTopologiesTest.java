/* Copyright (c) 2023 LibJ
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

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import org.junit.Test;
import org.libj.lang.Classes;
import org.libj.lang.Strings;

public class DiscreteTopologiesTest {
  static void assertEq(final Object expected, final Object actual) {
    final Class<?> cls = expected.getClass();
    if (cls == String.class) {
      assertArrayEquals(((String)expected).toCharArray(), ((String)actual).toCharArray());
      return;
    }

    final boolean isArray = cls.isArray();
    if (!isArray) {
      assertEquals(expected, actual);
    }
    else {
      final Class<?> componentType = cls.getComponentType();
      if (!componentType.isPrimitive())
        assertArrayEquals((Object[])expected, (Object[])actual);
      else if (componentType == byte.class)
        assertArrayEquals((byte[])expected, (byte[])actual);
      else if (componentType == short.class)
        assertArrayEquals((short[])expected, (short[])actual);
      else if (componentType == int.class)
        assertArrayEquals((int[])expected, (int[])actual);
      else if (componentType == long.class)
        assertArrayEquals((long[])expected, (long[])actual);
      else if (componentType == float.class)
        assertArrayEquals((float[])expected, (float[])actual, 0);
      else if (componentType == double.class)
        assertArrayEquals((double[])expected, (double[])actual, 0);
      else if (componentType == boolean.class)
        assertArrayEquals((boolean[])expected, (boolean[])actual);
      else if (componentType == char.class)
        assertArrayEquals((char[])expected, (char[])actual);
      else
        throw new UnsupportedOperationException();
    }
  }

  @SuppressWarnings("unchecked")
  private static <N> N[] seqFromMin(final DiscreteTopology<N> t, final N min, final int len) {
    final int len1 = len - 1;
    final N[] seq = (N[])Array.newInstance(min.getClass(), len);

    seq[0] = min;
    for (int i = 0; i < len1; ++i) // [A]
      seq[i + 1] = t.nextValue(seq[i]);

    return seq;
  }

  @SuppressWarnings("unchecked")
  private static <N> N[] seqFromMax(final DiscreteTopology<N> t, final N max, final int len) {
    final int len1 = len - 1;
    final N[] seq = (N[])Array.newInstance(max.getClass(), len);

    seq[len1] = max;
    for (int i = len1; i > 0; --i) // [A]
      seq[i - 1] = t.prevValue(seq[i]);

    return seq;
  }

  public static class MinMax<N> {
    public static <N> void test(final DiscreteTopology<N> t, final N min, final N max) {
      final MinMax<N> test = new MinMax<>();
      test.testMin(t, min);
      test.testMax(t, max);
    }

    private void testMin(final DiscreteTopology<N> t, final N min) {
      assertEq(min, t.prevValue(min));

      final N[] seq = seqFromMin(t, min, 3);

      for (int i = seq.length - 1; i > 0; --i) // [A]
        assertEq(seq[i - 1], t.prevValue(seq[i]));
    }

    private void testMax(final DiscreteTopology<N> t, final N max) {
      assertEq(max, t.nextValue(max));

      final N[] seq = seqFromMax(t, max, 3);

      for (int i = 0, i$ = seq.length - 1; i < i$; ++i) // [A]
        assertEq(seq[i + 1], t.nextValue(seq[i]));
    }
  }

  @Test
  @SuppressWarnings({"rawtypes", "unchecked"})
  public void testMinMax() {
    final DiscreteTopology[] ts = {DiscreteTopologies.BYTE, DiscreteTopologies.SHORT, DiscreteTopologies.INTEGER, DiscreteTopologies.LONG, DiscreteTopologies.FLOAT, DiscreteTopologies.DOUBLE, DiscreteTopologies.CHARACTER, DiscreteTopologies.LOCAL_DATE, DiscreteTopologies.DATE};
    final Object[] mins = {Byte.MIN_VALUE, Short.MIN_VALUE, Integer.MIN_VALUE, Long.MIN_VALUE, -Float.MAX_VALUE, -Double.MAX_VALUE, Character.MIN_VALUE, LocalDate.MIN, Dates.MIN_VALUE};
    final Object[] maxs = {Byte.MAX_VALUE, Short.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE, Float.MAX_VALUE, Double.MAX_VALUE, Character.MAX_VALUE, LocalDate.MAX, Dates.MAX_VALUE};
    for (int i = 0, i$ = ts.length; i < i$; ++i) // [A]
      MinMax.test(ts[i], mins[i], maxs[i]);
  }

  @Test
  @SuppressWarnings({"rawtypes", "unchecked"})
  public void testMinMaxArrayPrimitive() {
    final DiscreteTopology[] ts = {DiscreteTopologies.BYTES, DiscreteTopologies.SHORTS, DiscreteTopologies.INTS, DiscreteTopologies.LONGS, DiscreteTopologies.FLOATS, DiscreteTopologies.DOUBLES, DiscreteTopologies.CHARS};
    final Object[] mins = {Byte.MIN_VALUE, Short.MIN_VALUE, Integer.MIN_VALUE, Long.MIN_VALUE, -Float.MAX_VALUE, -Double.MAX_VALUE, Character.MIN_VALUE};
    final Object[] maxs = {Byte.MAX_VALUE, Short.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE, Float.MAX_VALUE, Double.MAX_VALUE, Character.MAX_VALUE};
    for (int i = 0, i$ = ts.length; i < i$; ++i) { // [A]
      for (int j = 0; j < 100; j = j * 2 + 1) { // [A]
        final DiscreteTopology t = ts[i];

        final Object min = Array.newInstance(Classes.unbox(mins[i].getClass()), j);
        for (int k = 0; k < j; ++k) // [A]
          Array.set(min, k, mins[i]);

        final Object max = Array.newInstance(Classes.unbox(maxs[i].getClass()), j);
        for (int k = 0; k < j; ++k) // [A]
          Array.set(max, k, maxs[i]);

        MinMax.test(t, min, max);
      }
    }
  }

  @Test
  @SuppressWarnings({"rawtypes", "unchecked"})
  public void testMinMaxArrayObject() {
    final DiscreteTopology[] ts = {DiscreteTopologies.BYTE_OBJS, DiscreteTopologies.SHORT_OBJS, DiscreteTopologies.INTEGERS, DiscreteTopologies.LONG_OBJS, DiscreteTopologies.FLOAT_OBJS, DiscreteTopologies.DOUBLE_OBJS, DiscreteTopologies.CHARACTERS};
    final Object[] mins = {Byte.MIN_VALUE, Short.MIN_VALUE, Integer.MIN_VALUE, Long.MIN_VALUE, -Float.MAX_VALUE, -Double.MAX_VALUE, Character.MIN_VALUE};
    final Object[] maxs = {Byte.MAX_VALUE, Short.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE, Float.MAX_VALUE, Double.MAX_VALUE, Character.MAX_VALUE};
    for (int i = 0, i$ = ts.length; i < i$; ++i) { // [A]
      for (int j = 0; j < 100; j = j * 2 + 1) { // [A]
        final DiscreteTopology t = ts[i];

        final Object[] min = (Object[])Array.newInstance(mins[i].getClass(), j);
        Arrays.fill(min, mins[i]);

        final Object[] max = (Object[])Array.newInstance(maxs[i].getClass(), j);
        Arrays.fill(max, maxs[i]);

        MinMax.test(t, min, max);
      }
    }
  }

  @Test
  @SuppressWarnings({"rawtypes", "unchecked"})
  public void testMinMaxArray() {
    final DiscreteTopology[] ts = {DiscreteTopologies.LOCAL_DATES, DiscreteTopologies.DATES};
    final Object[] mins = {LocalDate.MIN, Dates.MIN_VALUE};
    final Object[] maxs = {LocalDate.MAX, Dates.MAX_VALUE};
    for (int i = 0, i$ = ts.length; i < i$; ++i) { // [A]
      for (int j = 0; j < 100; j = j * 2 + 1) { // [A]
        final DiscreteTopology t = ts[i];

        final Object[] min = (Object[])Array.newInstance(mins[i].getClass(), j);
        Arrays.fill(min, mins[i]);

        final Object[] max = (Object[])Array.newInstance(maxs[i].getClass(), j);
        Arrays.fill(max, maxs[i]);

        MinMax.test(t, min, max);
      }
    }
  }

  @Test
  public void testString() {
    for (int i = 0; i < 100; i = i * 2 + 1) // [N]
      MinMax.test(DiscreteTopologies.STRING, Strings.repeat(Character.MIN_VALUE, i).toString(), Strings.repeat(Character.MAX_VALUE, i).toString());
  }

  @Test
  public void testLocalTime() {
    for (int i = 0, i$ = DiscreteTopologies.LOCAL_TIME.length; i < i$; ++i) // [N]
      MinMax.test(DiscreteTopologies.LOCAL_TIME[i], LocalTime.MIN, LocalTime.MAX);
  }

  @Test
  public void testLocalDateTime() {
    for (int i = 0, i$ = DiscreteTopologies.LOCAL_DATE_TIME.length; i < i$; ++i) // [N]
      MinMax.test(DiscreteTopologies.LOCAL_DATE_TIME[i], LocalDateTime.MIN, LocalDateTime.MAX);
  }

  @Test
  public void testBoolean() {
    assertTrue(DiscreteTopologies.BOOLEAN.nextValue(Boolean.FALSE));
    assertTrue(DiscreteTopologies.BOOLEAN.nextValue(Boolean.TRUE));
    assertFalse(DiscreteTopologies.BOOLEAN.prevValue(Boolean.FALSE));
    assertFalse(DiscreteTopologies.BOOLEAN.prevValue(Boolean.TRUE));
  }
}