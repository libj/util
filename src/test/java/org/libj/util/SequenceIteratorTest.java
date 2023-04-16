/* Copyright (c) 2022 LibJ
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

import java.math.BigDecimal;
import java.util.Iterator;

import org.junit.Test;

@SuppressWarnings("unused")
public class SequenceIteratorTest {
  @Test
  public void testZero() {
    try {
      new SequenceIterator(BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0));
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }
  }

  @Test
  public void tetsPositive() {
    try {
      new SequenceIterator(BigDecimal.valueOf(1), BigDecimal.valueOf(0), BigDecimal.valueOf(1));
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    final SequenceIterator generator = new SequenceIterator(BigDecimal.valueOf(0), BigDecimal.valueOf(2), BigDecimal.valueOf(1));
    final Iterator<BigDecimal> iterator = generator.iterator();
    for (int i = 0; i < 10; ++i) // [N]
      assertTrue(iterator.hasNext());

    assertEquals(0, iterator.next().intValue());
    for (int i = 0; i < 10; ++i) // [N]
      assertTrue(iterator.hasNext());

    assertEquals(1, iterator.next().intValue());
    for (int i = 0; i < 10; ++i) // [N]
      assertTrue(iterator.hasNext());

    assertEquals(2, iterator.next().intValue());
    assertFalse(iterator.hasNext());
  }

  @Test
  public void tetsNegative() {
    try {
      new SequenceIterator(BigDecimal.valueOf(0), BigDecimal.valueOf(1), BigDecimal.valueOf(-1));
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    final SequenceIterator generator = new SequenceIterator(BigDecimal.valueOf(2), BigDecimal.valueOf(0), BigDecimal.valueOf(-1));
    final Iterator<BigDecimal> iterator = generator.iterator();
    for (int i = 0; i < 10; ++i) // [N]
      assertTrue(iterator.hasNext());

    assertEquals(2, iterator.next().intValue());
    for (int i = 0; i < 10; ++i) // [N]
      assertTrue(iterator.hasNext());

    assertEquals(1, iterator.next().intValue());
    for (int i = 0; i < 10; ++i) // [N]
      assertTrue(iterator.hasNext());

    assertEquals(0, iterator.next().intValue());
    assertFalse(iterator.hasNext());
  }
}