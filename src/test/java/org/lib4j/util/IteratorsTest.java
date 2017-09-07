/* Copyright (c) 2017 lib4j
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

package org.lib4j.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class IteratorsTest {
  @Test
  public void testFilter() {
    final List<Number> list = Collections.asCollection(new ArrayList<Number>(), 0, 0d, 1, 1d, 2, BigInteger.ZERO, 3.4f, BigDecimal.ONE, 3, 2d, BigInteger.ONE, 10l, 4f, (short)8, (byte)62, 4, BigInteger.valueOf(2l));

    final Iterator<? extends Integer> integerIterator = Iterators.filter(list.iterator(), Integer.class);
    int i = 0;
    while (integerIterator.hasNext())
      Assert.assertEquals((Integer)i++, integerIterator.next());

    final Iterator<? extends BigInteger> bigIntegerIterator = Iterators.filter(list.iterator(), BigInteger.class);
    int j = 0;
    while (bigIntegerIterator.hasNext())
      Assert.assertEquals(BigInteger.valueOf(j++), bigIntegerIterator.next());

    final Iterator<? extends Double> doubleIterator = Iterators.filter(list.iterator(), Double.class);
    int k = 0;
    while (doubleIterator.hasNext())
      Assert.assertEquals(Double.valueOf(k++), doubleIterator.next());
  }
}