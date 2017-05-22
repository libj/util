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

package org.safris.commons.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class TopologicalSortTest {
  @Test
  public void testSort() {
    final Map<String,Set<String>> graph = new HashMap<String,Set<String>>();
    graph.put("a", Collections.asCollection(HashSet.class, "b"));
    graph.put("b", Collections.asCollection(HashSet.class, "c", "d"));
    graph.put("c", Collections.asCollection(HashSet.class, "d", "e"));
    graph.put("d", Collections.asCollection(HashSet.class, "e"));
    graph.put("e", Collections.asCollection(HashSet.class, "f", "g", "h"));
    graph.put("f", Collections.asCollection(HashSet.class, "h"));
    graph.put("g", null);
    graph.put("h", null);
    final List<String> sorted = TopologicalSort.sort(graph);
    Assert.assertArrayEquals(new String[] {"g", "h", "f", "e", "d", "c", "b", "a"}, sorted.toArray());
  }
}