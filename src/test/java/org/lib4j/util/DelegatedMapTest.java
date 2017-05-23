/* Copyright (c) 2016 lib4j
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class DelegatedMapTest {
  @Test
  public void testMapDelegate() {
    final Map<String,String> values = new HashMap<String,String>();
    for (int i = 0; i < 10; i++)
      values.put("" + ('a' + i), "" + ('a' + i));

    final DelegatedMap.MapDelegate<String,String> mapDelegate = new DelegatedMap.MapDelegate<String,String>() {
      @Override
      public void put(final String key, final String value) {
        Assert.assertTrue(!values.containsKey(key));
        Assert.assertEquals(key, value);
      }

      @Override
      public void remove(final Object key) {
        Assert.assertTrue(values.containsKey(key));
      }
    };

    final DelegatedMap<String,String> map = new DelegatedMap<String,String>(ArrayMap.class, mapDelegate);
    for (final String key : new HashSet<String>(values.keySet())) {
      final String value = values.remove(key);
      map.put(key, value);
    }

    for (final String key : new HashSet<String>(map.keySet())) {
      final String value = map.get(key);
      values.put(key, value);
      map.remove(key);
    }
  }
}