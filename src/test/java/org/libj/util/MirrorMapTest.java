/* Copyright (c) 2019 LibJ
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

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

public class MirrorMapTest {
  private static MirrorMap<String,String,Integer> newMap(final HashMap<String,String> values, final HashMap<String,Integer> reflections) {
    return new MirrorMap<>(values, reflections, new MirrorMap.Mirror<String,String,Integer>() {
      @Override
      public Integer valueToReflection(final String key, final String value) {
        return Integer.valueOf(value);
      }

      @Override
      public String reflectionToValue(final String key, final Integer reflection) {
        return String.valueOf(reflection);
      }
    });
  }

  @Test
  public void test() {
    final MirrorMap<String,String,Integer> map = newMap(new HashMap<>(), new HashMap<>());

    map.put("1", "1");
    assertTrue(map.getMirrorMap().containsValue(1));

    map.getMirrorMap().put("2", 2);
    assertTrue(map.containsValue("2"));

    final Iterator<Map.Entry<String,String>> stringIterator = map.entrySet().iterator();
    final Map.Entry<String,String> stringNext = stringIterator.next();
    stringIterator.remove();
    assertFalse(map.getMirrorMap().containsKey(stringNext.getKey()));
    assertFalse(map.getMirrorMap().containsValue(Integer.valueOf(stringNext.getValue())));

    final Iterator<Map.Entry<String,Integer>> integerIterator = map.getMirrorMap().entrySet().iterator();
    final Map.Entry<String,Integer> integerNext = integerIterator.next();
    integerIterator.remove();
    assertFalse(map.containsKey(integerNext.getKey()));
    assertFalse(map.containsValue(String.valueOf(integerNext.getValue())));
  }

  @Test
  public void testConcurrentPut() {
    final HashMap<String,String> values = new HashMap<>();
    final HashMap<String,Integer> reflections = new HashMap<>();
    final MirrorMap<String,String,Integer> map = newMap(values, reflections);
    map.put("1", "1");
    values.put("0", "0");
    try {
      map.put("2", "2");
      fail("Expected ConcurrentModificationException");
    }
    catch (final ConcurrentModificationException e) {
    }
  }

  @Test
  public void testConcurrentRemove() {
    final HashMap<String,String> values = new HashMap<>();
    final HashMap<String,Integer> reflections = new HashMap<>();
    final MirrorMap<String,String,Integer> map = newMap(values, reflections);
    map.put("1", "1");
    reflections.remove("1");
    try {
      map.get("1");
      fail("Expected ConcurrentModificationException");
    }
    catch (final ConcurrentModificationException e) {
    }
  }

  @Test
  @Ignore("Map.replace(...) does not increase modCount")
  public void testConcurrentReplace() {
    final HashMap<String,String> values = new HashMap<>();
    final HashMap<String,Integer> reflections = new HashMap<>();
    final MirrorMap<String,String,Integer> map = newMap(values, reflections);
    map.put("1", "1");
    values.replace("1", "2");
    try {
      map.containsKey("1");
      fail("Expected ConcurrentModificationException");
    }
    catch (final ConcurrentModificationException e) {
    }
  }
}