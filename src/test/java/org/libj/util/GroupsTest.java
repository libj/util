/* Copyright (c) 2014 LibJ
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

import org.junit.Test;

public class GroupsTest {
  @Test
  public void testPermute() {
    int k = 2;
    Groups.permute((a, n) -> {
//      for (int i = n; i < a.length; ++i)
//        System.out.print(a[i]);
//      ArrayUtil.toString(a, '.', n, a.length);
      System.out.println(ArrayUtil.toString(a, '.', n, a.length - n));
      // System.out.println(Arrays.toString(a, n));
    }, k, "a", "b", "c", "d", "e");
  }

  @Test
  public void testCombinations() {
    try {
      Groups.combine(null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    final String[][] test = {
      {"km", "m", "ft"},
      {"sec", "min", "hr"},
      {"kg", "lb"}
    };

    final String[][] expected = {
      {"m", "sec", "kg"},
      {"ft", "sec", "kg"},
      {"km", "min", "kg"},
      {"m", "min", "kg"},
      {"ft", "min", "kg"},
      {"km", "hr", "kg"},
      {"m", "hr", "kg"},
      {"ft", "hr", "kg"},
      {"km", "sec", "lb"},
      {"m", "sec", "lb"},
      {"ft", "sec", "lb"},
      {"km", "min", "lb"},
      {"m", "min", "lb"},
      {"ft", "min", "lb"},
      {"km", "hr", "lb"},
      {"m", "hr", "lb"},
      {"ft", "hr", "lb"},
      {"km", "sec", "kg"}
    };

    assertArrayEquals(expected, Groups.combine(test));
  }
}