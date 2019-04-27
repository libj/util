/* Copyright (c) 2017 OpenJAX
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

package org.openjax.ext.util;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class IteratorsTest {
  @Test
  public void testFilter() {
    final List<Number> list = FastCollections.asCollection(new ArrayList<Number>(), 0, 0d, 1, 1d, 2, BigInteger.ZERO, 3.4f, BigDecimal.ONE, 3, 2d, BigInteger.ONE, 10l, 4f, (short)8, (byte)62, 4, BigInteger.valueOf(2l));

    final Iterator<? super Integer> integerIterator = Iterators.filter(list.iterator(), m -> m instanceof Integer);
    for (int i = 0; integerIterator.hasNext(); i++)
      assertEquals(i, integerIterator.next());

    final Iterator<? super BigInteger> bigIntegerIterator = Iterators.filter(list.iterator(), m -> m instanceof BigInteger);
    for (int i = 0; bigIntegerIterator.hasNext(); i++)
      assertEquals(BigInteger.valueOf(i), bigIntegerIterator.next());

    final Iterator<? super Double> doubleIterator = Iterators.filter(list.iterator(), m -> m instanceof Double);
    for (int i = 0; doubleIterator.hasNext(); i++)
      assertEquals(Double.valueOf(i), doubleIterator.next());
  }
}