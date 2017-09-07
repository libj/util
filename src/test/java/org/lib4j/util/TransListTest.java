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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.Assert;
import org.junit.Test;

public class TransListTest {
  protected static void test(final TransList<Integer,String> trans) {
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
    final List<Integer> source = new ArrayList<Integer>();
    for (int i = 0; i < 10; i++)
      source.add(i);

    test(new TransList<Integer,String>(source, integer -> String.valueOf(integer), string -> Integer.valueOf(string)));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testList() {
    final List<Integer> source = new ArrayList<Integer>();
    for (int i = 0; i < 10; i++)
      source.add(i);

    final TransList<Integer,String> trans = new TransList<Integer,String>(source, integer -> String.valueOf(integer), string -> Integer.valueOf(string));
    trans.add(3, "555");
    Assert.assertEquals("555", trans.get(3));
    Assert.assertEquals(new Integer(555), ((List<Integer>)trans.source).get(3));

    trans.addAll(9, Arrays.asList(new String[] {"666", "777", "888"}));
    Assert.assertEquals("666", trans.get(9));
    Assert.assertEquals("777", trans.get(10));
    Assert.assertEquals("888", trans.get(11));
    Assert.assertEquals(new Integer(666), ((List<Integer>)trans.source).get(9));
    Assert.assertEquals(new Integer(777), ((List<Integer>)trans.source).get(10));
    Assert.assertEquals(new Integer(888), ((List<Integer>)trans.source).get(11));

    trans.set(7, "0");
    Assert.assertEquals("0", trans.get(7));
    Assert.assertEquals(new Integer(000), ((List<Integer>)trans.source).get(7));

    trans.remove(7);
    Assert.assertNotEquals("0", trans.get(7));
    Assert.assertNotEquals(new Integer(000), ((List<Integer>)trans.source).get(7));

    final TransList<Integer,String> subList = trans.subList(4, 7);
    for (int i = 3; i < 6; i++)
      Assert.assertEquals(String.valueOf(i), subList.get(i - 3));

    final ListIterator<String> iterator = trans.listIterator();
    while (iterator.hasNext()) {
      final String element = iterator.next();
      if ("666".equals(element)) {
        iterator.remove();
        Assert.assertFalse(trans.contains("666"));
        Assert.assertFalse(((List<Integer>)trans.source).contains(666));
      }
      else if ("777".equals(element)) {
        iterator.add("876");
        Assert.assertTrue(trans.contains("876"));
        Assert.assertTrue(((List<Integer>)trans.source).contains(876));
      }
      else if ("888".equals(element)) {
        iterator.set("123");
        Assert.assertFalse(trans.contains("888"));
        Assert.assertFalse(((List<Integer>)trans.source).contains(888));
        Assert.assertTrue(trans.contains("123"));
        Assert.assertTrue(((List<Integer>)trans.source).contains(123));
      }
    }
  }
}