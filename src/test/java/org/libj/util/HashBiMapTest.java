/* Copyright (c) 2017 LibJ
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

import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

public class HashBiMapTest {
  @SuppressWarnings("unlikely-arg-type")
  public static void test(final HashBiMap<Integer,String> map, final int offset, final boolean testClone) {
    for (int i = offset; i < 100 + offset; ++i) {
      final String value = String.valueOf(i + offset);
      map.put(i + offset, value);
      assertEquals(value, map.get(i + offset));
      assertEquals(Integer.valueOf(i + offset), map.reverse().get(value));
    }

    map.remove(7 + offset);
    assertFalse(map.containsValue(7 + offset));

    map.reverse().remove(String.valueOf(8 + offset));
    assertFalse(map.containsKey(8 + offset));

    final Iterator<Map.Entry<Integer,String>> entryIterator = map.entrySet().iterator();
    while (entryIterator.hasNext()) {
      final Map.Entry<Integer,String> entry = entryIterator.next();
      if (entry.getKey() == 15 + offset) {
        entryIterator.remove();
        assertFalse(map.containsValue(String.valueOf(15 + offset)));
      }
      else if (entry.getKey() == 77 + offset) {
        entryIterator.remove();
        assertFalse(map.containsValue(String.valueOf(77 + offset)));
      }
    }

    final Iterator<String> valueIterator = map.values().iterator();
    while (valueIterator.hasNext()) {
      final String value = valueIterator.next();
      if (String.valueOf(32 + offset).equals(value)) {
        valueIterator.remove();
        assertFalse(map.containsKey(32 + offset));
      }
      else if (String.valueOf(99 + offset).equals(value)) {
        valueIterator.remove();
        assertFalse(map.containsKey(99 + offset));
      }
    }

    if (testClone) {
      final Integer xx = 17;
      map.put(xx, "xx");
      assertFalse(map.reverse.containsKey("17"));
      assertTrue(map.reverse.containsKey("xx"));

      test(map.clone(), 1000, false);
    }
  }

  @Test
  public void test() {
    test(new HashBiMap<>(), 0, true);
  }
}