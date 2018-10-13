/* Copyright (c) 2017 FastJAX
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

package org.fastjax.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

public class TransCollectionTest {
  protected static void test(final TransCollection<Integer,String> trans) {
    assertTrue(trans.contains("3"));
    assertTrue(trans.source.contains(3));
    assertFalse(trans.contains("11"));
    assertFalse(trans.source.contains(11));

    trans.addAll(Arrays.asList("11", "12", "13"));
    assertTrue(trans.contains("11"));
    assertTrue(trans.source.contains(11));

    trans.remove("11");
    assertFalse(trans.contains("11"));
    assertFalse(trans.source.contains(11));

    trans.toArray(new String[trans.size()]);

    trans.remove("5");
    assertFalse(trans.contains("5"));
    assertFalse(trans.source.contains(5));

    final Iterator<String> iterator = trans.iterator();
    while (iterator.hasNext()) {
      iterator.next();
      iterator.remove();
    }

    assertEquals(0, trans.size());
    assertEquals(0, trans.source.size());
  }

  @Test
  public void test() {
    final Collection<Integer> source = new ArrayList<>();
    for (int i = 0; i < 10; i++)
      source.add(i);

    test(new TransCollection<Integer,String>(source, integer -> String.valueOf(integer), string -> Integer.valueOf(string)));
  }
}