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

import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

import org.junit.Test;

public class IdentityHashBiMapTest {
  @SuppressWarnings("unlikely-arg-type")
  public static void test(final IdentityHashBiMap<Integer,String> map, final int offset, final boolean testClone) {
    for (int i = offset; i < 100 + offset; ++i) {
      final Integer index = i + offset;
      final String value = String.valueOf(index).intern();
      map.put(index, value);
      assertSame(value + " != " + map.get(index), value, map.get(index));
      assertSame(index + " != " + map.inverse().get(value), index, map.inverse().get(value));
    }

    map.remove(7 + offset);
    assertFalse(map.containsValue(7 + offset));

    map.inverse().remove(String.valueOf(8 + offset).intern());
    assertFalse(map.containsKey(8 + offset));

    final Iterator<Map.Entry<Integer,String>> entryIterator = map.entrySet().iterator();
    while (entryIterator.hasNext()) {
      final Map.Entry<Integer,String> entry = entryIterator.next();
      if (entry.getKey() == 15 + offset) {
        entryIterator.remove();
        assertFalse(map.containsValue(String.valueOf(15 + offset).intern()));
      }
      else if (entry.getKey() == 77 + offset) {
        entryIterator.remove();
        assertFalse(map.containsValue(String.valueOf(77 + offset).intern()));
      }
    }

    final Iterator<String> valueIterator = map.values().iterator();
    while (valueIterator.hasNext()) {
      final String value = valueIterator.next();
      if (String.valueOf(32 + offset).intern() == value) {
        valueIterator.remove();
        assertFalse(map.containsKey(32 + offset));
      }
      else if (String.valueOf(99 + offset).intern() == value) {
        valueIterator.remove();
        assertFalse(map.containsKey(99 + offset));
      }
    }

    if (testClone) {
      final Integer xx = 17;
      map.put(xx, "xx".intern());
      assertFalse(map.inverse.containsKey("17".intern()));
      assertTrue(map.inverse.containsKey("xx".intern()));

      test(map.clone(), 1000, false);
    }
  }

  @Test
  public void test() {
    test(new IdentityHashBiMap<Integer,String>(), 0, true);
  }
}