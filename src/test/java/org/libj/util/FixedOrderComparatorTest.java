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

import static org.junit.Assert.*;

import org.junit.Test;

public class FixedOrderComparatorTest {
  @Test
  @SuppressWarnings("unused")
  public void test() {
    try {
      new FixedOrderComparator<>((String[])null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    final FixedOrderComparator<String> comparator = new FixedOrderComparator<>("z", "a", "t", "r", "q", "b");
    assertEquals(-1, comparator.compare("z", "a"));
    assertEquals(-1, comparator.compare("z", "t"));
    assertEquals(-1, comparator.compare("z", "r"));
    assertEquals(-1, comparator.compare("r", "b"));
    assertEquals(0, comparator.compare("z", "z"));
    assertEquals(0, comparator.compare("a", "a"));
    assertEquals(0, comparator.compare("r", "r"));
    assertEquals(0, comparator.compare("b", "b"));
    assertEquals(1, comparator.compare("b", "z"));
    assertEquals(1, comparator.compare("q", "t"));
    assertEquals(1, comparator.compare("r", "a"));
    assertEquals(1, comparator.compare("t", "z"));

    assertEquals(-1, comparator.compare("1", "2"));
    assertEquals(-1, comparator.compare("z", "1"));
    assertEquals(-1, comparator.compare("z", "Z"));
    assertEquals(1, comparator.compare("Z", "t"));
    assertEquals(1, comparator.compare("0", "b"));
    assertEquals(1, comparator.compare("~", "a"));
  }
}