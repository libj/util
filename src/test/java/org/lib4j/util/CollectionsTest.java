/* Copyright (c) 2014 lib4j
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class CollectionsTest {
  @Test
  public void testGetComponentType() {
    Assert.assertEquals(null, Collections.getComponentType(Arrays.asList(new Object[] {null, null, null})));
    Assert.assertEquals(Number.class, Collections.getComponentType(Arrays.asList(new Object[] {new Integer(1), null, BigInteger.ONE})));
    Assert.assertEquals(Number.class, Collections.getComponentType(Arrays.asList(new Object[] {new Integer(1), new Long(1), BigInteger.ONE})));
  }

  @Test
  public void testPartitions() {
    final List<String> list = Arrays.asList(new String[] {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"});
    for (int p = 1; p < list.size(); p++) {
      final List<String>[] partitions = Collections.partition(ArrayList.class, list, p);
      final int parts = list.size() / p;
      final int remainder = list.size() % p;
      Assert.assertEquals(parts + (remainder != 0 ? 1 : 0), partitions.length);
      for (int i = 0; i < parts; i++)
        for (int j = 0; j < p; j++)
          Assert.assertEquals(list.get(i * p + j), partitions[i].get(j));

      if (remainder != 0)
        for (int j = 0; j < list.size() % p; j++)
          Assert.assertEquals(list.get(p * parts + j), partitions[parts].get(j));
    }
  }
}
