/* Copyright (c) 2016 LibJ
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
import java.util.ListIterator;

import org.junit.Test;

public class MirrorListTest {
  @Test
  public void test() {
    final MirrorList<String,Integer> list = new MirrorList<>(new ArrayList<String>(), new ArrayList<Integer>(), s -> Integer.valueOf(s), i -> String.valueOf(i));

    list.add("1");
    assertTrue(list.getMirror().contains(1));

    list.getMirror().add(2);
    assertTrue(list.contains("2"));

    final ListIterator<String> stringIterator = list.listIterator();
    stringIterator.next();
    stringIterator.add("3");
    assertTrue(list.getMirror().contains(3));

    final ListIterator<Integer> integerIterator = list.getMirror().listIterator();
    integerIterator.next();
    integerIterator.next();
    integerIterator.add(7);
    assertTrue(list.contains("7"));

    assertTrue(list.remove("1"));
    assertTrue(!list.getMirror().contains(1));

    assertEquals(Integer.valueOf(2), list.getMirror().remove(2));
    assertTrue(!list.contains("2"));

    list.getMirror().clear();
    assertEquals(0, list.size());
  }
}