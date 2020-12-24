/* Copyright (c) 2020 LibJ
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

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class ConcurrentNullHashMapTest {
  private static final String str = "value";

  @Test
  public void testPut() {
    final ConcurrentNullHashMap<String,String> map = new ConcurrentNullHashMap<>();
    map.put(str, str);
    map.put(null, null);
  }

  @Test
  public void testValues() {
    final ConcurrentNullHashMap<String,String> map = new ConcurrentNullHashMap<>();
    map.put(str, str);
    map.put(null, null);
    final Collection<String> values = map.values();
    assertTrue(values.contains(str));
    assertTrue(values.contains(null));
  }

  @Test
  public void testPutIfAbsent() {
    final ConcurrentNullHashMap<String,String> map = new ConcurrentNullHashMap<>();
    map.putIfAbsent(str, str);
    map.putIfAbsent(null, null);
  }

  @Test
  public void testPutAll() {
    final ConcurrentNullHashMap<String,String> map = new ConcurrentNullHashMap<>();
    map.putAll(Collections.singletonMap(null, null));
  }

  @Test
  public void testContains() {
    final ConcurrentNullHashMap<String,String> map = new ConcurrentNullHashMap<>(Collections.singletonMap(null, null));
    assertTrue(map.containsKey(null));
    assertTrue(map.contains(null));
  }

  @Test
  public void testGet() {
    final ConcurrentNullHashMap<String,String> map = new ConcurrentNullHashMap<>(Collections.singletonMap(null, null));
    assertTrue(map.containsKey(null));
    assertNull(map.get(null));
  }

  @Test
  public void testContainsKey() {
    final ConcurrentNullHashMap<String,String> map = new ConcurrentNullHashMap<>(Collections.singletonMap(null, null));
    assertTrue(map.containsKey(null));
  }

  @Test
  public void testRemove() {
    final ConcurrentNullHashMap<String,String> map = new ConcurrentNullHashMap<>(Collections.singletonMap(null, null));
    assertTrue(map.containsKey(null));
    assertNull(map.get(null));
  }

  @Test
  public void testReplaceKV() {
    final ConcurrentNullHashMap<String,String> map = new ConcurrentNullHashMap<>();
    map.put(str, str);
    map.put(null, null);

    assertEquals(str, map.replace(str, null));
    assertNull(map.replace(null, null));
  }

  @Test
  public void testReplaceKVV() {
    final ConcurrentNullHashMap<String,String> map = new ConcurrentNullHashMap<>();
    map.put(str, str);
    map.put(null, null);

    assertTrue(map.replace(str, str, null));
    assertTrue(map.replace(null, null, null));
  }

  @Test
  public void testEntrySet() {
    final ConcurrentNullHashMap<String,String> map = new ConcurrentNullHashMap<>();
    final Set<Map.Entry<String,String>> entries = map.entrySet();
    entries.add(new AbstractMap.SimpleEntry<>(null, null));
    assertTrue(map.containsKey(null));
    assertNull(map.get(null));
  }

  @Test
  public void testKeys() {
    final ConcurrentNullHashMap<String,String> map = new ConcurrentNullHashMap<>();
    map.put(str, str);
    map.put(null, null);
    final Enumeration<String> keys = map.keys();
    assertTrue(keys.hasMoreElements());
    String key = keys.nextElement();
    if (key == null) {
      assertTrue(keys.hasMoreElements());
      assertEquals(str, keys.nextElement());
    }
    else if (str.equals(key)) {
      assertTrue(keys.hasMoreElements());
      assertNull(keys.nextElement());
    }
    else {
      fail();
    }

    assertFalse(keys.hasMoreElements());
  }

  @Test
  public void testElements() {
    final ConcurrentNullHashMap<String,String> map = new ConcurrentNullHashMap<>();
    map.put(str, str);
    map.put(null, null);
    final Enumeration<String> elements = map.elements();
    assertTrue(elements.hasMoreElements());
    String key = elements.nextElement();
    if (key == null) {
      assertTrue(elements.hasMoreElements());
      assertEquals(str, elements.nextElement());
    }
    else if (str.equals(key)) {
      assertTrue(elements.hasMoreElements());
      assertNull(elements.nextElement());
    }
    else {
      fail();
    }

    assertFalse(elements.hasMoreElements());
  }

  @Test
  public void testEquals() {
    final HashMap<String,String> other = new HashMap<>();
    other.put(str, str);
    other.put(null, null);

    final ConcurrentNullHashMap<String,String> map = new ConcurrentNullHashMap<>();
    map.put(str, str);
    map.put(null, null);

    assertEquals(other, map);
  }
}