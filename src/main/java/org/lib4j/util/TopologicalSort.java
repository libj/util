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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class TopologicalSort<T> {
  public static <T>List<T> sort(final Map<T,Set<T>> graph) {
    for (final Map.Entry<T,Set<T>> entry : graph.entrySet())
      if (entry.getValue() != null)
        entry.getValue().remove(entry.getKey());

    final List<T> sorted = new ArrayList<T>();
    while (true) {
      T key = null;
      for (final Map.Entry<T,Set<T>> entry : graph.entrySet()) {
        if (entry.getValue() == null || entry.getValue().size() == 0) {
          key = entry.getKey();
          break;
        }
      }

      if (key == null) {
        final Set<T> remaining = new HashSet<T>(graph.keySet());
        remaining.removeAll(sorted);
        sorted.addAll(remaining);
        return sorted;
      }

      sorted.add(key);
      graph.remove(key);
      for (final Set<T> value : graph.values())
        if (value != null)
          value.remove(key);
    }
  }

  public static <T>List<T> sort(final Collection<T> set, final Rule<T> policy) {
    final Map<Object,T> idToValue = new HashMap<Object,T>();
    final Map<T,Set<T>> graph = new HashMap<T,Set<T>>();
    for (final T t : set)
      idToValue.put(policy.getSelfId(t), t);

    for (final T t : set) {
      final Set<Object> linkIds = policy.getLinkIds(t);
      Set<T> dependents = graph.get(t);
      if (dependents == null)
        graph.put(t, dependents = new HashSet<T>());

      for (final Object linkId : linkIds) {
        final T value = idToValue.get(linkId);
        if (value != null)
          dependents.add(value);
      }
    }

    return sort(graph);
  }

  public static interface Rule<T> {
    public Set<Object> getLinkIds(final T o);
    public Object getSelfId(final T o);
  }
}