/* Copyright (c) 2017 OpenJAX
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

package org.openjax.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A directed graph of an arbitrary-sized set of arbitrary-typed vertices,
 * permitting self-loops and parallel edges.
 * <p>
 * Edges can be dynamically added with {@link Digraph#addEdge(Object,Object)}.
 * Cycle can be found with {@link Digraph#hasCycle()} and
 * {@link Digraph#getCycle()}. If no cycle exists, a topological order can be
 * found with {@link Digraph#getTopologicalOrder()}.
 * <p>
 * This implementation uses {@code Integer}-based vertex indices as references
 * to the arbitrary-typed object vertices via {@link HashBiMap}.
 * <p>
 * The digraph is internally represented as a dynamically scalable
 * {@link ArrayList} list of index-&gt;{@link LinkedHashSet} set of adjacent
 * edges.
 * <p>
 * All operations take constant time (in the worst case) except iterating over
 * the vertices adjacent from a given vertex, which takes time proportional to
 * the number of such vertices.
 *
 * @param <T> The type of elements in this digraph.
 */
public class Digraph<T> implements Cloneable, Serializable {
  private static final long serialVersionUID = -1725638737276587152L;

  private static final Comparator<Object[]> arrayComparator = new Comparator<Object[]>() {
    @Override
    public int compare(final Object[] o1, final Object[] o2) {
      return o1 == null ? (o2 == null ? 0 : 1) : o2 == null ? -1 : Integer.compare(Arrays.hashCode(o1), Arrays.hashCode(o2));
    }
  };

  protected HashBiMap<T,Integer> objectToIndex = new HashBiMap<>();

  private final int initialCapacity;
  private ArrayList<LinkedHashSet<Integer>> adj;
  private Object[][] flatAdj;

  private ArrayIntList indegree;
  private ArrayList<T> edges;

  private ArrayList<T> cycle;
  private ArrayList<T> reversePostOrder;

  /**
   * Creates an empty digraph with the specified initial capacity.
   *
   * @param initialCapacity The initial capacity of the digraph.
   * @throws IllegalArgumentException If the specified initial capacity is
   *           negative.
   */
  public Digraph(final int initialCapacity) {
    if (initialCapacity < 0)
      throw new IllegalArgumentException("Initial Capacity cannot be negative: " + initialCapacity);

    this.initialCapacity = initialCapacity;
    this.adj = new ArrayList<>(initialCapacity);
    this.indegree = new ArrayIntList(initialCapacity);
    this.edges = new ArrayList<>(initialCapacity);
  }

  /**
   * Creates an empty digraph with an initial capacity of ten.
   */
  public Digraph() {
    this(10);
  }

  /**
   * Returns the number of vertices in this digraph.
   *
   * @return The number of vertices in this digraph.
   */
  public int getSize() {
    return adj.size();
  }

  /**
   * Returns heads of all directed edges.
   *
   * @return Heads of all directed edges.
   */
  public List<T> getEdges() {
    return edges;
  }

  /**
   * Add a vertex to the graph.
   *
   * @param vertex The vertex.
   * @return {@code true} if this digraph has been modified, and {@code false}
   *         if the specified vertex already existed in the digraph.
   * @throws NullPointerException If {@code vertex} is null.
   */
  public boolean addVertex(final T vertex) {
    return addVertex(getCreateIndex(vertex));
  }

  /**
   * Returns the set of vertices in this digraph.
   *
   * @return The set of vertices in this digraph.
   */
  public Set<T> getVertices() {
    return objectToIndex.keySet();
  }

  /**
   * Add a vertex to the graph.
   *
   * @param vertex The vertex index.
   * @return {@code true} if this digraph has been modified, and {@code false}
   *         if the specified vertex already existed in the digraph.
   */
  private boolean addVertex(final int vertex) {
    if (vertex < adj.size())
      return false;

    for (int i = adj.size(); i <= vertex; ++i)
      adj.add(null);

    return true;
  }

  /**
   * Get if exists or create an index for the specified vertex
   * <p>
   * <i><b>Note:</b> This method is not synchronized.</i>
   *
   * @param vertex The vertex.
   * @return The index of the vertex.
   * @throws NullPointerException If {@code vertex} is null.
   */
  private int getCreateIndex(final T vertex) {
    Integer index = objectToIndex.get(Objects.requireNonNull(vertex));
    if (index == null)
      objectToIndex.put(vertex, index = objectToIndex.size());

    return index;
  }

  /**
   * Returns the index of {@code vertex} if exists, or fails with
   * {@link IllegalArgumentException}.
   *
   * @param vertex The vertex.
   * @return The index of {@code vertex} if exists, or fails with
   *         {@link IllegalArgumentException}.
   * @throws IllegalArgumentException If {@code vertex} does not exist in this
   *           digraph.
   * @throws NullPointerException If {@code vertex} is null.
   */
  private int getFailIndex(final T vertex) {
    final Integer index = objectToIndex.get(Objects.requireNonNull(vertex));
    if (index == null)
      throw new IllegalArgumentException("Vertex does not exist in this digraph");

    return index;
  }

  /**
   * Add directed edge ({@code from} -&gt; {@code to}) to this digraph. Calling
   * this with {@code to = null} is the equivalent of calling
   * {@code Digraph.addVertex(from)}.
   * <p>
   * <i><b>Note:</b> This method is not synchronized.</i>
   *
   * @param from The tail vertex.
   * @param to The head vertex.
   * @return {@code true} if this digraph has been modified, and {@code false}
   *         if the specified edge already existed in the digraph.
   * @throws NullPointerException If {@code from} is null.
   */
  public boolean addEdge(final T from, final T to) {
    if (to == null)
      return addVertex(getCreateIndex(from));

    if (!addEdge(getCreateIndex(from), getCreateIndex(to)))
      return false;

    edges.add(to);
    return true;
  }

  /**
   * Add directed edge (from -&gt; to) to this digraph.
   * <p>
   * <i><b>Note:</b> This method is not synchronized.</i>
   *
   * @param v The index of the tail vertex.
   * @param w The index of the head vertex.
   * @return {@code true} if this digraph has been modified, and {@code false}
   *         if the specified edge already existed in the digraph.
   */
  private boolean addEdge(final int v, final int w) {
    LinkedHashSet<Integer> edges;
    if (v < adj.size()) {
      edges = adj.get(v);
      if (edges == null)
        adj.set(v, edges = new LinkedHashSet<>());
      else if (edges.contains(w))
        return false;
    }
    else {
      for (int i = adj.size(); i < v; ++i)
        adj.add(null);

      adj.add(edges = new LinkedHashSet<>());
    }

    for (int i = adj.size(); i <= w; ++i)
      adj.add(null);

    edges.add(w);

    if (w < indegree.size()) {
      indegree.set(w, indegree.get(w) + 1);
    }
    else {
      for (int i = indegree.size(); i < w; ++i)
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
   * @param vertex The vertex.
   * @return The number of directed edges incident to vertex {@code vertex}.
   * @throws IllegalArgumentException If vertex {@code vertex} does not exist in
   *           this digraph.
   * @throws NullPointerException If {@code vertex} is null.
   */
  public int getInDegree(final T vertex) {
    return indegree.get(getFailIndex(vertex));
  }

  /**
   * @param vertex The vertex.
   * @return The number of directed edges incident from vertex {@code vertex}.
   * @throws IllegalArgumentException If vertex {@code vertex} does not exist in
   *           this digraph.
   * @throws NullPointerException If {@code vertex} is null.
   */
  public int getOutDegree(final T vertex) {
    return adj.get(getFailIndex(vertex)).size();
  }

  /**
   * Returns a new {@code Digraph} instance with all edges of this digraph
   * reversed.
   *
   * @return A new {@code Digraph} instance with all edges of this digraph
   *         reversed.
   */
  public Digraph<T> reverse() {
    final Digraph<T> reverse = new Digraph<>();
    reverse.objectToIndex = objectToIndex.clone();
    for (int v = 0; v < adj.size(); ++v) {
      final LinkedHashSet<Integer> edges = adj.get(v);
      if (edges != null) {
        for (final int w : edges) {
          reverse.addEdge(w, v);
          reverse.edges.add(objectToIndex.inverse().get(v));
        }
      }
    }

    return reverse;
  }

  private void dfs() {
    if (reversePostOrder == null && (cycle = dfs(reversePostOrder = new ArrayList<>(edges.size()))) != null)
      reversePostOrder = null;
  }

  /**
   * Run the depth-first-search algorithm on this digraph to detect a cycle, or
   * construct the reversePostOrder list.
   *
   * @param reversePostOrder List of vertices filled in reverse post order.
   * @return A cycle list, if one was found.
   */
  private ArrayList<T> dfs(final List<T> reversePostOrder) {
    final BitSet marked = new BitSet(adj.size());
    final BitSet onStack = new BitSet(adj.size());
    final int[] edgeTo = new int[adj.size()];
    for (int v = 0; v < adj.size(); ++v) {
      if (!marked.get(v)) {
        final ArrayList<T> cycle = dfs(marked, onStack, edgeTo, reversePostOrder, v);
        if (cycle != null)
          return cycle;
      }
    }

    return null;
  }

  /**
   * Run the depth-first-search algorithm on this digraph to detect a cycle, or
   * construct the reversePostOrder list.
   *
   * @param marked {@code BitSet} maintaining marked state.
   * @param onStack {@code BitSet} maintaining on-stack state.
   * @param edgeTo Vertex index array maintaining state for cycle detection.
   * @param reversePostOrder List of vertices filled in reverse post order.
   * @param v The index of the vertex.
   * @return A cycle list, if one was found.
   */
  private ArrayList<T> dfs(final BitSet marked, final BitSet onStack, final int[] edgeTo, final List<T> reversePostOrder, final int v) {
    onStack.set(v);
    marked.set(v);
    final LinkedHashSet<Integer> edges = adj.get(v);
    if (edges != null) {
      for (final int w : edges) {
        if (!marked.get(w)) {
          edgeTo[w] = v;
          final ArrayList<T> cycle = dfs(marked, onStack, edgeTo, reversePostOrder, w);
          if (cycle != null)
            return cycle;
        }
        else if (v != w && onStack.get(w)) {
          final ArrayList<T> cycle = new ArrayList<>(initialCapacity / 3);
          for (int x = v; x != w; x = edgeTo[x])
            cycle.add(objectToIndex.inverse().get(x));

          cycle.add(objectToIndex.inverse().get(w));
          cycle.add(objectToIndex.inverse().get(v));
          return cycle;
        }
      }
    }

    onStack.clear(v);
    reversePostOrder.add(0, objectToIndex.inverse().get(v));
    return null;
  }

  /**
   * Specifies whether the digraph has a cycle.
   * <p>
   * <i><b>Note:</b> This method is not synchronized.</i>
   *
   * @return {@code true} if the digraph has a cycle; otherwise {@code false}.
   */
  public boolean hasCycle() {
    return getCycle() != null;
  }

  /**
   * Returns a directed cycle if the digraph has one, and {@code null}
   * otherwise.
   * <p>
   * <i><b>Note:</b> This method is not synchronized.</i>
   *
   * @return A directed cycle if the digraph has one; otherwise {@code null}.
   */
  public List<T> getCycle() {
    dfs();
    return cycle;
  }

  /**
   * Returns the reverse post order of a depth first search analysis of the
   * digraph, or null if no such order exists due to a cycle.
   * <p>
   * <i><b>Note:</b> This method is not synchronized.</i>
   *
   * @return The reverse post order of a depth first search analysis of the
   *         digraph, or {@code null} if no such order exists due to a cycle..
   */
  public List<T> getTopologicalOrder() {
    dfs();
    return reversePostOrder;
  }

  /**
   * Returns a flat representation of the {@code adj} data structure, where each
   * member of the flat representation is the java reference to the
   * arbitrary-typed vertex.
   * <p>
   * <i><b>Note:</b> This method is not synchronized.</i>
   *
   * @return A flat representation of the {@code adj} data structure, where each
   *         member of the flat representation is the java reference to the
   *         arbitrary-typed vertex.
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
        vertices[j++] = objectToIndex.inverse().get(index);
    }

    Arrays.sort(flatAdj, arrayComparator);
    return this.flatAdj = flatAdj;
  }

  /**
   * Returns the hash code value for this digraph. The hash code of a digraph is
   * computed by evaluating the hash codes of the member vertices with respect
   * to the directed edge definitions.
   * <p>
   * <i><b>Note:</b> This method is not synchronized.</i>
   *
   * @return The hash code value for this digraph. The hash code of a digraph is
   *         computed by evaluating the hash codes of the member vertices with
   *         respect to the directed edge definitions.
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
   * Tests whether {@code this} digraph is equal to {@code obj}.
   * <p>
   * <i><b>Note:</b> This method is not synchronized.</i>
   *
   * @param obj The object to test for equality.
   * @return {@code true} if {@code this} digraph is equal to {@code obj};
   *         otherwise {@code false}.
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
    for (int i = 0; i < thisFlat.length; ++i)
      if (!Arrays.equals(thisFlat[i], thatFlat[i]))
        return false;

    return true;
  }

  /**
   * Returns a string representation of this digraph, containing: the number of
   * vertices, followed by the number of edges, followed by the adjacency lists.
   * <p>
   * <i><b>Note:</b> This method is not synchronized.</i>
   *
   * @return A string representation of this digraph, containing: the number of
   *         vertices, followed by the number of edges, followed by the
   *         adjacency lists.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder().append(adj.size()).append(" vertices, ").append(edges).append(" edges").append('\n');
    for (int v = 0; v < adj.size(); ++v) {
      builder.append(objectToIndex.inverse().get(v)).append(':');
      final LinkedHashSet<Integer> edges = adj.get(v);
      if (edges != null)
        for (final int w : edges)
          builder.append(' ').append(objectToIndex.inverse().get(w));

      if (v < adj.size() - 1)
        builder.append('\n');
    }

    return builder.toString();
  }

  @Override
  @SuppressWarnings("unchecked")
  public Digraph<T> clone() {
    try {
      final Digraph<T> clone = (Digraph<T>)super.clone();
      clone.objectToIndex = objectToIndex.clone();
      clone.adj = (ArrayList<LinkedHashSet<Integer>>)adj.clone();
      clone.flatAdj = flatAdj == null ? null : flatAdj.clone();
      clone.indegree = indegree.clone();
      clone.edges = (ArrayList<T>)edges.clone();
      clone.cycle = cycle == null ? null : (ArrayList<T>)cycle.clone();
      clone.reversePostOrder = reversePostOrder == null ? null : (ArrayList<T>)reversePostOrder.clone();
      return clone;
    }
    catch (final CloneNotSupportedException e) {
      throw new UnsupportedOperationException(e);
    }
  }
}