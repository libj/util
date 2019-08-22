/* Copyright (c) 2012 LibJ
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

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class SortedListTest {
  @Test
  public void test() {
    final SortedList<String> list = new SortedList<>(new ArrayList<String>());
    list.add("f");
    assertArrayEquals(new String[] {"f"}, list.toArray());
    list.add("b");
    assertArrayEquals(new String[] {"b", "f"}, list.toArray());
    list.add("g");
    assertArrayEquals(new String[] {"b", "f", "g"}, list.toArray());
    list.add("c");
    assertArrayEquals(new String[] {"b", "c", "f", "g"}, list.toArray());
    list.add("a");
    assertArrayEquals(new String[] {"a", "b", "c", "f", "g"}, list.toArray());
    list.add("d");
    assertArrayEquals(new String[] {"a", "b", "c", "d", "f", "g"}, list.toArray());
    list.add("e");
    assertArrayEquals(new String[] {"a", "b", "c", "d", "e", "f", "g"}, list.toArray());
    list.remove("c");
    assertArrayEquals(new String[] {"a", "b", "d", "e", "f", "g"}, list.toArray());
    list.remove(0);
    list.add("d");
    assertArrayEquals(new String[] {"b", "d", "d", "e", "f", "g"}, list.toArray());
    list.remove(4);
    list.add("h");
    assertArrayEquals(new String[] {"b", "d", "d", "e", "g", "h"}, list.toArray());
    list.retainAll(Arrays.asList(new String[] {"a", "d", "f", "g", "h"}));
    assertArrayEquals(new String[] {"d", "d", "g", "h"}, list.toArray());
    list.retainAll(Arrays.asList(new String[] {"a", "d", "d", "d", "h"}));
    assertArrayEquals(new String[] {"d", "d", "h"}, list.toArray());
  }
}