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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

/**
 *  A directed graph of an arbitrary-sized set of arbitrary-typed vertices,
 *  permitting self-loops and parallel edges.
 *
 *  This digraph differs from its <code>Digraph</code> superclass by offering
 *  a layer of indirection between the object type <code>T</code>, and another
 *  type <code>R</code> that is used as the linking value between edges. The
 *  required <code>Function<T,I></code> parameter in the constructor is used
 *  to dereference the object of type <code>T</code> to objects by which edges
 *  are defined of type <code>R</code>.
 *
 *  Upon invocation of any method that invokes <code>digraph.dfs()</code>, the
 *  <code>IndirectDigraph</code> swaps edges of type <code>R</code> to their
 *  linked object references of type <code>T</code>, based on the translation
 *  of the supplied <code>Function<T,I></code> function.
 *
 *  It is important to note that this implementation assumes that an object of
 *  type <code>T</code> will be encountered for each reference of type
 *  <code>R</code>.
 *
 *  Vertices can be dynamically added with the additional
 *  <code>Digraph.addIndirectVertex()</code> method.
 *
 *  Edges can be dynamically added with the additional
 *  <code>Digraph.addIndirectEdge()</code> method.
 *
 *  @see org.lib4j.util.Digraph
 */
public class IndirectDigraph<T,R> extends Digraph<T> {
  private static final long serialVersionUID = -8038282541169001107L;

  private ArrayList<T> vertices = new ArrayList<T>();
  private HashSet<R> references = new HashSet<R>();
  private Digraph<Object> digraph = new Digraph<Object>();
  protected final Function<T,R> reference;

  /**
   * Constructs an empty digraph with the specified initial capacity.
   *
   * @param  reference the function to obtain the reference of type
   *         <code>R</code> from an object of type <code>T</code>.
   * @param  initialCapacity the initial capacity of the digraph.
   * @throws IllegalArgumentException if the specified initial capacity
   *         is negative
   */
  public IndirectDigraph(final int initialCapacity, final Function<T,R> reference) {
    super(initialCapacity);
    this.reference = reference;
  }

  /**
   * Constructs an empty digraph with an initial capacity of ten.
   *
   * @param  reference the function to obtain the reference of type
   *         <code>R</code> from an object of type <code>T</code>.
   */
  public IndirectDigraph(final Function<T,R> reference) {
    super();
    this.reference = reference;
  }

  /**
   * Swap vertex reference object of type R with their equivalent object of
   * type T.
   * @throws IllegalStateException I some vertex references have not been
   *         specified before the time this method is called.
   */
  private void swapRefs() {
    for (final T vertex : vertices) {
      final R ref = reference.apply(vertex);
      references.remove(ref);
      final Integer index = digraph.objectToIndex.remove(ref);
      if (index != null)
        digraph.objectToIndex.put(vertex, index);
    }

    vertices.clear();
    if (references.size() != 0)
      throw new IllegalStateException("Vertices with the following vertex references have not been specified: " + Collections.toString(references, ", "));
  }

  @Override
  public boolean addVertex(final T vertex) {
    vertices.add(vertex);
    return digraph.addVertex(reference.apply(vertex));
  }

  /**
   * Add a vertex reference to the graph.
   *
   * @param vertex The vertex reference.
   * @return <code>true</code> if this digraph has been modified, and
   *         <code>false</code> if the specified vertex already existed in the
   *         digraph.
   */
  public boolean addVertexRef(final R vertex) {
    references.add(vertex);
    return digraph.addVertex(vertex);
  }

  @Override
  public boolean addEdge(final T from, final T to) {
    if (from == null)
      throw new NullPointerException("from == null");

    if (to == null)
      return addVertex(from);

    vertices.add(from);
    vertices.add(to);
    return digraph.addEdge(reference.apply(from), reference.apply(to));
  }

  /**
   * Add directed edge (<code>from</code> -&gt; <code>to</code>) to this
   * digraph. Calling this with <code>to = null</code> is the equivalent of
   * calling <code>Digraph.addVertex(from)</code> (not synchronized).
   *
   * @param from The tail vertex.
   * @param to The head vertex reference.
   * @return <code>true</code> if this digraph has been modified, and
   *         <code>false</code> if the specified edge already existed in the
   *         digraph.
   * @throws NullPointerException If <code>from</code> is null.
   */
  public boolean addEdgeRef(final T from, final R to) {
    if (from == null)
      throw new NullPointerException("from == null");

    if (to == null)
      return addVertex(from);

    vertices.add(from);
    references.add(to);
    return digraph.addEdge(reference.apply(from), to);
  }

  @Override
  public int getSize() {
    return digraph.getSize();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<T> getEdges() {
    swapRefs();
    return (List<T>)digraph.getEdges();
  }

  @Override
  public int getInDegree(final T v) {
    swapRefs();
    return digraph.getInDegree(v);
  }

  @Override
  public int getOutDegree(final T v) {
    swapRefs();
    return digraph.getOutDegree(v);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<T> getCycle() {
    swapRefs();
    return (List<T>)digraph.getCycle();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<T> getTopologicalOrder() {
    swapRefs();
    return (List<T>)digraph.getTopologicalOrder();
  }

  @Override
  @SuppressWarnings("unchecked")
  public IndirectDigraph<T,R> clone() {
    final IndirectDigraph<T,R> clone = (IndirectDigraph<T,R>)super.clone();
    clone.vertices = (ArrayList<T>)vertices.clone();
    clone.references = (HashSet<R>)references.clone();
    clone.digraph = digraph.clone();
    return clone;
  }
}