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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import org.libj.lang.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A directed graph of an arbitrary-sized set of arbitrary-typed vertices,
 * permitting self-loops and parallel edges.
 * <p>
 * This digraph differs from its {@link Digraph} superclass by offering a layer
 * of indirection between the object type {@code T}, and another type {@code R}
 * that is used as the linking value between edges. The required
 * {@code Function<T,R>} parameter in the constructor is used to dereference the
 * object of type {@code T} to objects by which edges are defined of type
 * {@code R}. The references are resolved prior to the {@code dfs()} method
 * call.
 * <p>
 * Upon invocation of any method that invokes {@code dfs()}, the
 * {@link RefDigraph} swaps edges of type {@code R} to their linked object
 * references of type {@code T}, based on the translation of the supplied
 * {@code Function<T,R>} function.
 * <p>
 * It is important to note that this implementation assumes that an object of
 * type {@code T} will be encountered for each reference of type {@code R}.
 * <p>
 * Vertices can be added with {@link Digraph#add(Object)}.
 * <p>
 * Edges can be added with {@link Digraph#add(Object,Object)}.
 * <p>
 * The {@link RefDigraph} implements {@code Map<K,Set<V>>}, supporting all
 * required and optional operations.
 *
 * @param <K> The type of elements in this digraph.
 * @param <V> The type of referenced values.
 * @see Digraph
 */
public class RefDigraph<K,V> extends AbstractDigraph<K,V> {
  private static final Logger logger = LoggerFactory.getLogger(RefDigraph.class);

  protected final Function<K,V> reference;
  private Digraph<Object> digraph;
  private ArrayList<K> vertices;
  private HashSet<V> references;

  /**
   * Creates an empty digraph with the specified initial capacity.
   *
   * @param keyToValue The function to obtain the referenced value of type
   *          {@code V} from a key of type {@code K}.
   * @param initialCapacity The initial capacity of the digraph.
   * @throws IllegalArgumentException If the specified initial capacity is
   *           negative.
   * @throws IllegalArgumentException If {@code keyToValue} is null.
   */
  public RefDigraph(final int initialCapacity, final Function<K,V> keyToValue) {
    super(0, true);
    digraph = new Digraph<>(initialCapacity);
    vertices = new ArrayList<>(initialCapacity);
    references = new HashSet<>(initialCapacity);
    reference = Assertions.assertNotNull(keyToValue);
  }

  /**
   * Creates an empty digraph with an initial capacity of ten.
   *
   * @param keyToValue The function to obtain the referenced value of type
   *          {@code V} from a key of type {@code K}.
   * @throws IllegalArgumentException If {@code keyToValue} is null.
   */
  public RefDigraph(final Function<K,V> keyToValue) {
    this(10, keyToValue);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected K indexToKey(final int v) {
    swapRefs();
    return (K)indexToObject.get(v);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected V indexToValue(final int v) {
    swapRefs();
    final V ref = reference.apply((K)indexToObject.get(v));
    System.err.println(ref);
    return ref;
  }

  /**
   * Swap vertex reference objects of type {@code V} with their equivalent
   * object of type {@code K}.
   *
   * @throws IllegalStateException If some vertex references have not been
   *           specified before the call of this method.
   */
  private void swapRefs() {
    if (vertices.size() == 0)
      return;

    for (final K vertex : vertices) {
      final V ref = reference.apply(vertex);
      references.remove(ref);
      final Integer index = digraph.objectToIndex.remove(ref);
      if (index != null) {
        final Integer oldIndex = digraph.objectToIndex.put(vertex, index);
        if (oldIndex != null && logger.isDebugEnabled())
          logger.debug("Remapped object at index " + oldIndex + " to " + index + ": " + vertex);
      }
    }

    vertices.clear();
    if (references.size() != 0)
      throw new IllegalStateException("Missing vertex references: " + CollectionUtil.toString(references, ", "));
  }

  @Override
  public boolean add(final K vertex) {
    vertices.add(Assertions.assertNotNull(vertex));
    return digraph.add(reference.apply(vertex));
  }

  /**
   * Add directed edge ({@code from -> to}) to this digraph. Calling this with
   * {@code to = null} is the equivalent of calling:
   *
   * <pre>
   * {@code addVertex(from)}
   * </pre>
   *
   * @implSpec This method is not thread safe.
   * @param from The tail vertex.
   * @param to The head vertex reference.
   * @return {@code true} if this digraph has been modified, and {@code false}
   *         if the specified edge already existed in the digraph.
   */
  @Override
  public boolean add(final K from, final V to) {
    if (to == null)
      return add(from);

    vertices.add(from);
    references.add(to);
    return digraph.add(reference.apply(from), to);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Set<K> keySet() {
    swapRefs();
    return (Set<K>)digraph.keySet();
  }

  @Override
  public int size() {
    return digraph.size();
  }

  /**
   * @throws IllegalStateException If some vertex references have not been
   *           specified before the call of this method.
   */
  @Override
  public int getInDegree(final K vertex) {
    swapRefs();
    return digraph.getInDegree(vertex);
  }

  /**
   * @throws IllegalStateException If some vertex references have not been
   *           specified before the call of this method.
   */
  @Override
  public int getOutDegree(final K vertex) {
    swapRefs();
    return digraph.getOutDegree(vertex);
  }

  /**
   * Returns a directed cycle (as a list of vertex {@code K} objects) if the
   * digraph has one, and null otherwise.
   *
   * @return A directed cycle (as a list of vertex {@code K} objects) if the
   *         digraph has one, and null otherwise.
   * @throws IllegalStateException If some vertex references have not been
   *           specified before the call of this method.
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<K> getCycle() {
    swapRefs();
    return (List<K>)digraph.getCycle();
  }

  /**
   * @throws IllegalStateException If some vertex references have not been
   *           specified before the call of this method.
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<K> getTopologicalOrder() {
    swapRefs();
    return (List<K>)digraph.getTopologicalOrder();
  }

  @Override
  @SuppressWarnings("unchecked")
  public RefDigraph<K,V> clone() {
    final RefDigraph<K,V> clone = (RefDigraph<K,V>)super.clone();
    clone.vertices = (ArrayList<K>)vertices.clone();
    clone.references = (HashSet<V>)references.clone();
    clone.digraph = digraph.clone();
    return clone;
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof RefDigraph))
      return false;

    final RefDigraph<K,V> that = (RefDigraph<K,V>)obj;
    if (!reference.equals(that.reference))
      return false;

    if (!Objects.equals(vertices, that.vertices))
      return false;

    if (!Objects.equals(references, that.references))
      return false;

    if (!Objects.equals(digraph, that.digraph))
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = reference.hashCode();
    hashCode = 31 * hashCode + vertices.hashCode();
    hashCode = 31 * hashCode + references.hashCode();
    hashCode = 31 * hashCode + digraph.hashCode();
    return hashCode;
  }

  @Override
  public String toString() {
    return digraph.toString();
  }
}