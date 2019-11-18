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

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * A directed graph of an arbitrary-sized set of arbitrary-typed vertices,
 * permitting self-loops and parallel edges.
 * <p>
 * Edges can be dynamically added with {@link Digraph#add(Object,Object)}. Cycle
 * can be found with {@link Digraph#hasCycle()} and {@link Digraph#getCycle()}.
 * If no cycle exists, a topological order can be found with
 * {@link Digraph#getTopologicalOrder()}.
 * <p>
 * This implementation uses {@link Integer}-based vertex indices as references
 * to the arbitrary-typed object vertices via {@link HashBiMap}.
 * <p>
 * The digraph is internally represented as a dynamically scalable
 * {@link ArrayList} list of index-&gt;{@link LinkedHashSet} set of adjacent
 * edges.
 * <p>
 * All operations take constant time (in the worst case) except iterating over
 * the vertices adjacent from a given vertex, which takes time proportional to
 * the number of such vertices.
 * <p>
 * The {@link Digraph} implements {@code Map<T,Set<T>>}, supporting all required
 * and optional operations.
 *
 * @param <T> The type of elements in this digraph.
 */
public class Digraph<T> extends AbstractDigraph<T,T> {
  private static final long serialVersionUID = 4470619796356029609L;

  public Digraph(final int initialCapacity) {
    super(initialCapacity);
  }

  public Digraph() {
    super();
  }

  @Override
  @SuppressWarnings("unchecked")
  protected T indexToKey(final int v) {
    return (T)indexToObject.get(v);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected T indexToValue(final int v) {
    return (T)indexToObject.get(v);
  }

  @Override
  public Digraph<T> clone() {
    return (Digraph<T>)super.clone();
  }
}