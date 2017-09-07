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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class TransSetTest {
  protected static void test(final TransSet<Integer,String> trans) {
    Assert.assertTrue(trans.contains("3"));
    Assert.assertTrue(trans.source.contains(3));
    Assert.assertFalse(trans.contains("11"));
    Assert.assertFalse(trans.source.contains(11));

    trans.addAll(Arrays.asList(new String[] {"11", "12", "13"}));
    Assert.assertTrue(trans.contains("11"));
    Assert.assertTrue(trans.source.contains(11));

    trans.remove("11");
    Assert.assertFalse(trans.contains("11"));
    Assert.assertFalse(trans.source.contains(11));

    trans.toArray(new String[trans.size()]);

    trans.remove("5");
    Assert.assertFalse(trans.contains("5"));
    Assert.assertFalse(trans.source.contains(5));

    final Iterator<String> iterator = trans.iterator();
    while (iterator.hasNext()) {
      iterator.next();
      iterator.remove();
    }

    Assert.assertEquals(0, trans.size());
    Assert.assertEquals(0, trans.source.size());
  }

  @Test
  public void test() {
    final Set<Integer> source = new HashSet<Integer>();
    for (int i = 0; i < 10; i++)
      source.add(i);

    test(new TransSet<Integer,String>(source, integer -> String.valueOf(integer), string -> Integer.valueOf(string)));
  }
}