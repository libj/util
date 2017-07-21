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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *  A directed graph of an arbitrary-sized set of arbitrary-typed vertices,
 *  permitting self-loops and parallel edges.
 *
 *  Edges can be dynamically added with <code>Digraph.addEdge()</code>.
 *  Cycle can be found with <code>Digraph.hasCycle()</code> and
 *  <code>Digraph.getCycle()</code>.
 *  If no cycle exists, a topological order can be found with
 *  <code>Digraph.getTopologicalOrder()</code>.
 *
 *  This implementation uses <code>Integer</code>-based vertex indices
 *  as references to the arbitrary-typed object vertices via
 *  <code>HashMap</code>.
 *
 *  The digraph is internally represented as a dynamically scalable
 *  <code>LinkedList</code> list of index-&gt;<code>LinkedHashSet</code> set
 *  of adjacent edges.
 *
 *  All operations take constant time (in the worst case) except
 *  iterating over the vertices adjacent from a given vertex, which takes
 *  time proportional to the number of such vertices.
 */
public class Digraph<T extends Serializable> implements Serializable {
  private static final long serialVersionUID = 139950028376580231L;

  private static final Comparator<Object[]> arrayComparator = new Comparator<Object[]>() {
    @Override
    public int compare(final Object[] o1, final Object[] o2) {
      return o1 == null ? (o2 == null ? 0 : 1) : o2 == null ? -1 : Integer.compare(Arrays.hashCode(o1), Arrays.hashCode(o2));
    }
  };

  private final Map<T,Integer> objectToIndex = new HashMap<T,Integer>();
  private final Map<Integer,T> indexToObject = new HashMap<Integer,T>();

  private final List<LinkedHashSet<Integer>> adj;
  private Object[][] flatAdj;

  private final List<Integer> indegree;
  private final List<T> edges;

  private List<T> cycle;
  private List<T> reversePostOrder;

  public Digraph() {
    this.adj = new ArrayList<LinkedHashSet<Integer>>();;
    this.indegree = new ArrayList<Integer>();
    this.edges = new LinkedList<T>();
  }

  /**
   * @return The number of vertices in this digraph.
   */
  public int getVertices() {
    return adj.size();
  }

  /**
   * @return Heads of all directed edges.
   */
  public List<T> getEdges() {
    return edges;
  }

  /**
   * Add a vertex to the graph.
   *
   * @param vertex The typed vertex.
   * @return <code>true</code> if this digraph has been modified, and
   *         <code>false</code> if the specified vertex already existed in the
   *         digraph.
   */
  public boolean addVertex(final T vertex) {
    if (vertex == null)
      throw new NullPointerException("vertex == null");

    return addVertex(getCreateIndex(vertex));
  }

  /**
   * Add a vertex to the graph.
   *
   * @param v The vertex index.
   * @return <code>true</code> if this digraph has been modified, and
   *         <code>false</code> if the specified vertex already existed in the
   *         digraph.
   */
  private boolean addVertex(final int v) {
    if (v < adj.size())
      return false;

    for (int i = adj.size(); i <= v; i++)
      adj.add(null);

    return true;
  }

  /**
   * Get if exists or create an index for the specified vertex (not synchronized).
   *
   * @param vertex The typed vertex.
   * @return The index of the vertex.
   */
  private int getCreateIndex(final T vertex) {
    Integer index = objectToIndex.get(vertex);
    if (index == null) {
      objectToIndex.put(vertex, index = objectToIndex.size());
      indexToObject.put(index, vertex);
    }

    return index;
  }

  /**
   * Get if exists or fail with IllegalArgumentException for the specified
   * vertex.
   *
   * @param vertex The typed vertex.
   * @return The index of the vertex.
   * @throws IllegalArgumentException If <code>v</code> is not a member of this
   *         digraph.
   */
  private int getFailIndex(final T vertex) {
    final Integer index = objectToIndex.get(vertex);
    if (index == null)
      throw new IllegalArgumentException("Vertex does not exist in this digraph");

    return index;
  }

  /**
   * Add directed edge (<code>from</code> -&gt; <code>to</code>) to this
   * digraph. Calling this with <code>to = null</code> is the equivalent of
   * calling <code>Digraph.addVertex(from)</code> (not synchronized).
   *
   * @param from The tail vertex.
   * @param to The head vertex.
   * @return <code>true</code> if this digraph has been modified, and
   *         <code>false</code> if the specified edge already existed in the
   *         digraph.
   * @throws NullPointerException If <code>from</code> is null.
   */
  public boolean addEdge(final T from, final T to) {
    if (from == null)
      throw new NullPointerException("from == null");

    if (to == null)
      return addVertex(getCreateIndex(from));

    if (!addEdge(getCreateIndex(from), getCreateIndex(to)))
      return false;

    edges.add(to);
    return true;
  }

  /**
   * Add directed edge (from -&gt; to) to this digraph (not synchronized).
   *
   * @param v The index of the tail vertex.
   * @param w The index of the head vertex.
   * @return <code>true</code> if this digraph has been modified, and
   *         <code>false</code> if the specified edge already existed in the
   *         digraph.
   */
  private boolean addEdge(final int v, final int w) {
    LinkedHashSet<Integer> edges;
    if (v < adj.size()) {
      edges = adj.get(v);
      if (edges == null)
        adj.set(v, edges = new LinkedHashSet<Integer>());
      else if (edges.contains(w))
        return false;
    }
    else {
      for (int i = adj.size(); i < v; i++)
        adj.add(null);

      adj.add(edges = new LinkedHashSet<Integer>());
    }

    for (int i = adj.size(); i <= w; i++)
      adj.add(null);

    edges.add(w);

    if (w < indegree.size()) {
      indegree.set(w, indegree.get(w) + 1);
    }
    else {
      for (int i = indegree.size(); i < w; i++)
        indegree.add(0);

      indegree.add(1);
    }

    // Invalidate the previous dfs() and getFlatAdj() operations, as the
    // digraph has changed
    reversePostOrder = null;
    flatAdj = null;
    return true;
  }

  /**
   * @param v The vertex.
   * @return The number of directed edges incident to vertex <code>v</code>.
   * @throws IllegalArgumentException If vertex <code>v</code> is not a member
   *         of this digraph.
   */
  public int getInDegree(final T v) {
    return indegree.get(getFailIndex(v));
  }

  /**
   * @param v The vertex.
   * @return The number of directed edges incident from vertex <code>v</code>.
   * @throws IllegalArgumentException If vertex <code>v</code> is not a member
   *         of this digraph.
   */
  public int getOutDegree(final T v) {
    return adj.get(getFailIndex(v)).size();
  }

  /**
   * @return A new digraph instance with all edges of this digraph reversed.
   */
  public Digraph<T> reverse() {
    final Digraph<T> reverse = new Digraph<T>();
    reverse.indexToObject.putAll(indexToObject);
    reverse.objectToIndex.putAll(objectToIndex);
    for (int v = 0; v < adj.size(); v++) {
      final LinkedHashSet<Integer> edges = adj.get(v);
      if (edges != null) {
        for (final int w : edges) {
          reverse.addEdge(w, v);
          reverse.edges.add(indexToObject.get(v));
        }
      }
    }

    return reverse;
  }

  private void dfs() {
    if (reversePostOrder == null && (cycle = dfs(reversePostOrder = new LinkedList<T>())) != null)
      reversePostOrder = null;
  }

  /**
   * Run the depth-first-search algorithm on this digraph to detect a cycle,
   * or construct the reversePostOrder list.
   *
   * @param reversePostOrder List of vertices filled in reverse post order.
   * @return A cycle list, if one was found.
   */
  private List<T> dfs(final List<T> reversePostOrder) {
    final BitSet marked = new BitSet(adj.size());
    final BitSet onStack = new BitSet(adj.size());
    final int[] edgeTo = new int[adj.size()];
    for (int v = 0; v < adj.size(); v++) {
      if (!marked.get(v)) {
        final List<T> cycle = dfs(marked, onStack, edgeTo, reversePostOrder, v);
        if (cycle != null)
          return cycle;
      }
    }

    return null;
  }

  /**
   * Run the depth-first-search algorithm on this digraph to detect a cycle,
   * or construct the reversePostOrder list.
   *
   * @param marked <code>BitSet</code> maintaining marked state.
   * @param onStack <code>BitSet</code> maintaining on-stack state.
   * @param edgeTo Vertex index array maintaining state for cycle detection.
   * @param reversePostOrder List of vertices filled in reverse post order.
   * @param v The vertex index.
   * @return A cycle list, if one was found.
   */
  private List<T> dfs(final BitSet marked, final BitSet onStack, final int[] edgeTo, final List<T> reversePostOrder, final int v) {
    onStack.set(v);
    marked.set(v);
    final LinkedHashSet<Integer> edges = adj.get(v);
    if (edges != null) {
      for (final int w : edges) {
        if (!marked.get(w)) {
          edgeTo[w] = v;
          final List<T> cycle = dfs(marked, onStack, edgeTo, reversePostOrder, w);
          if (cycle != null)
            return cycle;
        }
        else if (v != w && onStack.get(w)) {
          final List<T> cycle = new LinkedList<T>();
          for (int x = v; x != w; x = edgeTo[x])
            cycle.add(indexToObject.get(x));

          cycle.add(indexToObject.get(w));
          cycle.add(indexToObject.get(v));
          return cycle;
        }
      }
    }

    onStack.clear(v);
    reversePostOrder.add(0, indexToObject.get(v));
    return null;
  }

  /**
   * @return Whether the digraph has a cycle (not synchronized).
   */
  public boolean hasCycle() {
    return getCycle() != null;
  }

  /**
   * @return A directed cycle if the digraph has one, and <code>null</code>
   *         otherwise (not synchronized).
   */
  public List<T> getCycle() {
    dfs();
    return cycle;
  }

  /**
   * @return The reverse post order of a depth first search analysis of the
   *         digraph, or <code>null</code> if no such order exists due to a
   *         cycle (not synchronized).
   */
  public List<T> getTopologicalOrder() {
    dfs();
    return reversePostOrder;
  }

  /**
   * @return A flat representation of the <code>adj</code> data structure,
   * where each member of the flat representation is the java reference to the
   * arbitrary-typed vertex (not synchronized).
   */
  private Object[][] getFlatAdj() {
    if (flatAdj != null)
      return flatAdj;

    final Object[][] flatAdj = new Object[adj.size()][];
    int i = 0;
    for (final LinkedHashSet<Integer> edges : adj) {
      if (edges == null)
        continue;

      final Object[] vertices = flatAdj[i++] = new Object[edges.size()];
      int j = 0;
      for (final Integer index : edges)
        vertices[j++] = indexToObject.get(index);
    }

    Arrays.sort(flatAdj, arrayComparator);
    return this.flatAdj = flatAdj;
  }

  /**
   * @return The hash code value for this digraph. The hash code of a digraph
   *         is computed by evaluating the hash codes of the member vertices with
   *         respect to the directed edge definitions (not synchronized).
   */
  @Override
  public int hashCode() {
    int hashCode = objectToIndex.keySet().hashCode();
    final Object[][] thisFlat = getFlatAdj();
    for (final Object[] vertices : thisFlat)
      hashCode ^= vertices.hashCode() * 31;

    return hashCode;
  }

  /**
   * @param obj The object to test for equality.
   * @return Whether <code>this</code> digraph is equal to <code>obj</code>
   *         (not synchronized).
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Digraph))
      return false;

    final Digraph<?> that = (Digraph<?>)obj;
    if (adj.size() != that.adj.size())
      return false;

    if (!objectToIndex.keySet().equals(that.objectToIndex.keySet()))
      return false;

    final Object[][] thisFlat = getFlatAdj();
    final Object[][] thatFlat = that.getFlatAdj();
    for (int i = 0; i < thisFlat.length; i++)
      if (!Arrays.equals(thisFlat[i], thatFlat[i]))
        return false;

    return true;
  }

  /**
   * @return A string representation of the digraph, containing: the number of
   *         vertices, followed by the number of edges, followed by the
   *         adjacency lists (not synchronized).
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder().append(adj.size()).append(" vertices, ").append(edges).append(" edges").append('\n');
    for (int v = 0; v < adj.size(); v++) {
      builder.append(indexToObject.get(v)).append(":");
      final LinkedHashSet<Integer> edges = adj.get(v);
      if (edges != null)
        for (final int w : edges)
          builder.append(" ").append(indexToObject.get(w));

      if (v < adj.size() - 1)
        builder.append('\n');
    }

    return builder.toString();
  }
}