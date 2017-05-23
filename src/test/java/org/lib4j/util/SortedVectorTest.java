/* Copyright (c) 2012 lib4j
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
import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;

public class SortedVectorTest {
  @Test
  public void testSortedVector() {
    final Vector<String> vector = new SortedVector<String>();
    vector.addElement("f");
    Assert.assertArrayEquals(new String[] {"f"}, vector.toArray());
    vector.addElement("b");
    Assert.assertArrayEquals(new String[] {"b", "f"}, vector.toArray());
    vector.addElement("g");
    Assert.assertArrayEquals(new String[] {"b", "f", "g"}, vector.toArray());
    vector.addElement("c");
    Assert.assertArrayEquals(new String[] {"b", "c", "f", "g"}, vector.toArray());
    vector.addElement("a");
    Assert.assertArrayEquals(new String[] {"a", "b", "c", "f", "g"}, vector.toArray());
    vector.addElement("d");
    Assert.assertArrayEquals(new String[] {"a", "b", "c", "d", "f", "g"}, vector.toArray());
    vector.addElement("e");
    Assert.assertArrayEquals(new String[] {"a", "b", "c", "d", "e", "f", "g"}, vector.toArray());
    vector.remove("c");
    Assert.assertArrayEquals(new String[] {"a", "b", "d", "e", "f", "g"}, vector.toArray());
    vector.set(0, "d");
    Assert.assertArrayEquals(new String[] {"b", "d", "d", "e", "f", "g"}, vector.toArray());
    vector.set(4, "h");
    Assert.assertArrayEquals(new String[] {"b", "d", "d", "e", "g", "h"}, vector.toArray());
    vector.retainAll(Arrays.<String>asList(new String[] {"a", "d", "f", "g", "h"}));
    Assert.assertArrayEquals(new String[] {"d", "d", "g", "h"}, vector.toArray());
    vector.retainAll(Arrays.<String>asList(new String[] {"a", "d", "d", "d", "h"}));
    Assert.assertArrayEquals(new String[] {"d", "d", "h"}, vector.toArray());
  }
}