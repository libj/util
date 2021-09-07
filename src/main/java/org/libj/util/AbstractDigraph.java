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

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import org.libj.util.primitive.ArrayIntList;

/**
 * A directed graph of an arbitrary-sized set of arbitrary-typed vertices,
 * permitting self-loops and parallel edges.
 * <p>
 * Edges can be dynamically added with {@link AbstractDigraph#add(Object,Object)}.
 * Cycle can be found with {@link AbstractDigraph#hasCycle()} and
 * {@link AbstractDigraph#getCycle()}. If no cycle exists, a topological order can be
 * found with {@link AbstractDigraph#getTopologicalOrder()}.
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
 * The {@link AbstractDigraph} implements {@code Map<K,Set<V>>}, supporting all required
 * and optional operations.
 *
 * @param <K> The type of keys maintained by this digraph.
 * @param <V> The type of mapped values.
 */
abstract class AbstractDigraph<K,V> implements Map<K,Set<V>>, Cloneable {
  private final int initialCapacity;
  protected AbstractDigraph<K,V> transverse;

  protected HashBiMap<Object,Integer> objectToIndex;
  protected Map<Integer,Object> indexToObject;
  protected ArrayIntList adjRemoved;
  protected ArrayList<LinkedHashSet<Integer>> adj;
  protected TransList<LinkedHashSet<Integer>,TransSet<Integer,V>> adjEdges;
  protected ObservableMap<K,Integer> observableObjectToIndex;
  protected ArrayIntList inDegree;

  protected Object[][] flatAdj;
  protected ArrayList<K> cycle;
  protected ArrayList<K> reversePostOrder;

  /**
   * Creates an empty digraph with the specified initial capacity.
   *
   * @param initialCapacity The initial capacity of the digraph.
   * @throws IllegalArgumentException If the specified initial capacity is
   *           negative.
   */
  @SuppressWarnings("unchecked")
  AbstractDigraph(final int initialCapacity) {
    if (initialCapacity < 0)
      throw new IllegalArgumentException("Initial Capacity cannot be negative: " + initialCapacity);

    this.initialCapacity = initialCapacity;
    try {
      this.transverse = (AbstractDigraph<K,V>)super.clone();
    }
    catch (final CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
    init(this);
    init(this.transverse);
    this.transverse.transverse = this;
  }

  protected static <K,V>void init(final AbstractDigraph<K,V> digraph) {
    digraph.adj = new ArrayList<>(digraph.initialCapacity);
    digraph.inDegree = new ArrayIntList(digraph.initialCapacity);
    digraph.objectToIndex = new HashBiMap<>(digraph.initialCapacity);
    digraph.indexToObject = digraph.objectToIndex.reverse();
    digraph.adjRemoved = new ArrayIntList();
  }

  /**
   * Creates an empty digraph with an initial capacity of ten.
   */
  AbstractDigraph() {
    this(10);
  }

  /**
   * Creates an uninitialized and empty digraph with the specified initial
   * capacity.
   *
   * @param initialCapacity The initial capacity of the digraph.
   * @param ignored Ignored.
   */
  protected AbstractDigraph(final int initialCapacity, final boolean ignored) {
    this.initialCapacity = initialCapacity;
  }

  /**
   * Dereference a vertex index to the key of type {@code K}.
   *
   * @param v The vertex index.
   * @return A dereference key of type {@code K} at the specified vertex index.
   */
  protected abstract K indexToKey(int v);

  /**
   * Dereference a vertex index to the value of type {@code V}.
   *
   * @param v The vertex index.
   * @return A dereference value of type {@code K} at the specified vertex
   *         index.
   */
  protected abstract V indexToValue(int v);

  /**
   * Get if exists or create an index for the specified vertex.
   *
   * @implSpec This method is not thread safe.
   * @param vertex The vertex.
   * @return The index of the vertex.
   */
  private int getIndexCreate(final Object vertex) {
    Integer v = objectToIndex.get(vertex);
    if (v != null)
      return v;

    if (adjRemoved.isEmpty()) {
      v = objectToIndex.size();
      adj.add(null);
      inDegree.add(0);
    }
    else {
      v = adjRemoved.pop();
    }

    objectToIndex.put(vertex, v);
    return v;
  }

  /**
   * Add directed edge ({@code from} -> { @code to}) to this digraph.
   *
   * @implSpec This method is not thread safe.
   * @param from The tail vertex.
   * @param to The head vertex.
   * @return {@code true} if this digraph has been modified, and {@code false}
   *         if the specified edge already existed in the digraph.
   */
  private boolean addEdge(final Object from, final Object to) {
    final int v = getIndexCreate(from);
    final int w = getIndexCreate(to);
    LinkedHashSet<Integer> edges = adj.get(v);
    if (edges == null)
      adj.set(v, edges = new LinkedHashSet<>());
    else if (edges.contains(w))
      return false;

    edges.add(w);
    inDegree.set(w, inDegree.get(w) + 1);

    // Invalidate the previous dfs() and getFlatAdj() operations, as the
    // digraph has changed
    reversePostOrder = null;
    flatAdj = null;
    cycle = null;
    return true;
  }

  /**
   * Add directed edge ({@code from -> to}) to this digraph. Calling this with
   * {@code to = null} is the equivalent of calling
   * {@code AbstractDigraph.add(from)}.
   *
   * @implSpec This method is not thread safe.
   * @param from The tail vertex.
   * @param to The head vertex.
   * @return {@code true} if this digraph has been modified, and {@code false}
   *         if the specified edge already existed in the digraph.
   */
  public boolean add(final K from, final V to) {
    boolean modified = false;
    modified |= addEdge(from, to);
    modified |= transverse.addEdge(to, from);
    return modified;
  }

  /**
   * Add a vertex to the graph.
   *
   * @implSpec This method is not thread safe.
   * @param vertex The vertex.
   * @return {@code true} if this digraph has been modified, and {@code false}
   *         if the specified vertex already existed in the digraph.
   */
  public boolean add(final K vertex) {
    return getIndexCreate(vertex) >= size();
  }

  /**
   * Associate the set of directed {@code edges} to the {@code vertex}.
   *
   * <pre>
   * {@code vertex ->> edges}
   * </pre>
   *
   * If the digraph previously contained a mapping for the vertex, the old edges
   * are replaced by the specified value.
   *
   * @implSpec This method is not thread safe.
   */
  @Override
  public Set<V> put(final K vertex, final Set<V> edges) {
    final Set<V> previous = remove(vertex);
    for (final V edge : edges)
      add(vertex, edge);

    return previous;
  }

  /**
   * Copies all of the mappings from the specified map to this digraph. The
   * effect of this call is equivalent to that of calling
   * {@link AbstractDigraph#put(Object,Set)} on this map once for each mapping
   * from {@code key} to {@code value} in the specified map. The behavior of
   * this operation is undefined if the specified map is modified while the
   * operation is in progress.
   *
   * @implSpec This method is not thread safe.
   */
  @Override
  public void putAll(final Map<? extends K,? extends Set<V>> m) {
    for (final Map.Entry<? extends K,? extends Set<V>> entry : m.entrySet())
      put(entry.getKey(), entry.getValue());
  }

  /**
   * Returns the {@link TransList} instance that relates the edges of type
   * {@code V} to vertex indices. Changes made to this digraph are reflected in
   * this list.
   *
   * @return The {@link TransList} instance that relates the edges of type
   *         {@code V} to vertex indices.
   */
  private TransList<LinkedHashSet<Integer>,TransSet<Integer,V>> getAdjEdges() {
    if (adjEdges != null)
      return adjEdges;

    final ArrayList<TransSet<Integer,V>> transEdges = new ArrayList<>(initialCapacity);
    return adjEdges = new TransList<>(adj, (v, ws) -> {
      if (ws == null)
        return null;

      TransSet<Integer,V> edges;
      if (v >= transEdges.size()) {
        transEdges.ensureCapacity(v);
        for (int i = transEdges.size(); i <= v; ++i)
          transEdges.add(null);

        edges = null;
      }
      else {
        edges = transEdges.get(v);
      }

      if (edges == null)
        transEdges.set(v, edges = new TransSet<>(ws, this::indexToValue, o -> objectToIndex.get(o)));

      return edges;
    }, null);
  }

  /**
   * Returns a {@link TransSet} of edges for the specified vertex index
   * {@code v}, an empty {@link TransSet} if no edges exist at {@code v} and
   * {@code makeNew == true}, or {@code Collections.EMPTY_SET} if no edges exist
   * at {@code v} and {@code makeNew == false}.
   * <p>
   * If an instance of {@link TransSet} is returned, modifications made to this
   * digraph are reflected in the set, and modifications made to the set are
   * reflected in this digraph.
   *
   * @implSpec This method is not thread safe.
   * @param v The vertex index.
   * @param makeNew Whether a new empty set should be instantiated if the set of
   *          edges at the specified vertex is null.
   * @return A {@link TransSet} of edges for the specified vertex index
   *         {@code v}, an empty {@link TransSet} if no edges exist at {@code v}
   *         and {@code makeNew == true}, or {@code Collections.EMPTY_SET} if no
   *         edges exist at {@code v} and {@code makeNew == false}.
   */
  private Set<V> getEdgesAtIndex(final int v, final boolean makeNew) {
    final Set<V> edges = getAdjEdges().get(v);
    if (edges != null)
      return edges;

    if (!makeNew)
      return Collections.EMPTY_SET;

    adj.set(v, new LinkedHashSet<>());
    return adjEdges.get(v);
  }

  /**
   * Returns the set of edges mapped to the specified {@code vertex}.
   * <p>
   * If {@code withObserver == true}, an instance of {@link ObservableSet} is
   * returned, whereby modifications made to this digraph are reflected in the
   * set, and modifications made to the set are reflected in this digraph.
   * <p>
   * If {@code withObserver == false}, an instance of {@link TransSet} is
   * returned, whereby modifications made to this digraph are reflected in the
   * set.
   * <p>
   * If {@code withObserver == null}, an instance of {@link LinkedHashSet} is
   * returned.
   *
   * @param vertex The vertex.
   * @param withObserver Whether a {@link ObservableSet}, {@link TransSet}, or
   *          {@link LinkedHashSet} are to be returned.
   * @return The set of edges mapped to the specified {@code vertex}.
   */
  private Set<V> get(final Object vertex, final Boolean withObserver) {
    final Integer v = objectToIndex.get(vertex);
    if (v == null)
      return null;

    if (withObserver == null) {
      final LinkedHashSet<Integer> indices = adj.get(v);
      if (indices == null)
        return Collections.EMPTY_SET;

      final LinkedHashSet<V> edges = new LinkedHashSet<>(indices.size());
      for (final int index : indices)
        edges.add(indexToValue(index));

      return edges;
    }

    final Set<V> edges = getEdgesAtIndex(v, withObserver);
    if (!withObserver)
      return edges;

    return new ObservableSet<V>(edges) {
      @Override
      @SuppressWarnings("unchecked")
      protected Object beforeAdd(final V element, final Object preventDefault) {
        AbstractDigraph.this.add((K)vertex, element);
        return preventDefault;
      }

      @Override
      protected boolean beforeRemove(final Object element) {
        final Integer w = objectToIndex.get(element);
        if (w == null)
          return false;

        AbstractDigraph.this.removeEdge((int)v, (int)w);
        return super.beforeRemove(element);
      }
    };
  }

  /**
   * Returns the set of edges mapped to the specified {@code vertex}.
   * Modifications made to this digraph are reflected in the set, and
   * modifications made to the set are reflected in this digraph.
   *
   * @param vertex The vertex.
   * @return The set of edges mapped to the specified {@code vertex}.
   */
  @Override
  public Set<V> get(final Object vertex) {
    return get(vertex, true);
  }

  /**
   * Returns an {@link ObservableMap} of {@link #objectToIndex}, whereby
   * modifications made to this digraph are reflected in the set, and
   * modifications made to the set are reflected in this digraph.
   *
   * @return An {@link ObservableMap} of {@link #objectToIndex}, whereby
   *         modifications made to this digraph are reflected in the set, and
   *         modifications made to the set are reflected in this digraph.
   */
  @SuppressWarnings({"rawtypes", "unchecked", "unlikely-arg-type"})
  private ObservableMap<K,Integer> getObservableObjectToIndex() {
    return observableObjectToIndex == null ? observableObjectToIndex = new ObservableMap(objectToIndex) {
      @Override
      protected boolean beforeRemove(final Object key, final Object value) {
        return AbstractDigraph.this.remove(key) != null;
      }
    } : observableObjectToIndex;
  }

  /**
   * Returns {@code true} if this digraph contains the specified {@code vertex}.
   *
   * @return {@code true} if this digraph contains the specified {@code vertex}.
   * @param vertex The vertex.
   */
  @Override
  public boolean containsKey(final Object vertex) {
    return objectToIndex.containsKey(vertex);
  }

  /**
   * Returns {@code true} if at least one vertex in this digraph contains the
   * specified {@code edges}.
   *
   * @implNote If a vertex is not associated to any edges, its value is not
   *           {@code null}, but rather an empty set. Therefore, this method
   *           will return {@code null} if the specified {@code edges} is null.
   * @implNote The expected type of the {@code edges} is {@code Set<V>}, but
   *           this method accepts type {@link Object} to match the interface
   *           for {@link Map#containsValue(Object)}.
   * @return {@code true} if at least one vertex in this digraph contains the
   *         specified {@code edges}.
   * @param edges The edges.
   */
  @Override
  public boolean containsValue(final Object edges) {
    if (edges == null)
      return false;

    for (final int i : indexToObject.keySet()) {
      final Set<V> set = getEdgesAtIndex(i, false);
      if (edges.equals(set))
        return true;
    }

    return false;
  }

  /**
   * Returns the set of vertices in this digraph. Modifications made to this
   * digraph are reflected in the set, and modifications made to the set are
   * reflected in this digraph.
   *
   * @return The set of vertices in this digraph. Modifications made to this
   *         digraph are reflected in the set, and modifications made to the set
   *         are reflected in this digraph.
   */
  @Override
  public Set<K> keySet() {
    return getObservableObjectToIndex().keySet();
  }

  /**
   * Returns a collection of edges in this digraph. Modifications made to this
   * digraph are reflected in the collection, and modifications made to the
   * collection are reflected in this digraph.
   *
   * @return A collection of edges in this digraph. Modifications made to this
   *         digraph are reflected in the collection, and modifications made to
   *         the collection are reflected in this digraph.
   */
  @Override
  @SuppressWarnings("unlikely-arg-type")
  public Collection<Set<V>> values() {
    final ThreadLocal<Integer> localVertex = new ThreadLocal<>();
    return new ObservableCollection<Set<V>>(new TransSet<>(indexToObject.keySet(), v -> {
      localVertex.set(v);
      return get(indexToObject.get(v));
    }, null)) {
      @Override
      protected Object beforeAdd(final Set<V> element, final Object preventDefault) {
        throw new UnsupportedOperationException();
      }

      @Override
      protected boolean beforeRemove(final Object element) {
        if (element instanceof Set)
          AbstractDigraph.this.remove(localVertex.get());

        return false;
      }
    };
  }

  /**
   * Returns a set of {@link java.util.Map.Entry} objects representing the
   * ({@code vertex ->> edges}) mappings in this digraph.
   * Modifications made to this digraph are reflected in the set, and
   * modifications made to the set are reflected in this digraph.
   *
   * @return A set of {@link java.util.Map.Entry} objects representing the
   *         ({@code vertex ->> edges}) mappings in this digraph.
   *         Modifications made to this digraph are reflected in the set, and
   *         modifications made to the set are reflected in this digraph.
   */
  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Set<Map.Entry<K,Set<V>>> entrySet() {
    return new ObservableSet<Map.Entry<K,Set<V>>>(new TransSet<Integer,Map.Entry<K,Set<V>>>(indexToObject.keySet(), i -> new AbstractMap.SimpleEntry(indexToObject.get(i), getEdgesAtIndex(i, true)), null)) {
      @Override
      protected Object beforeAdd(final Map.Entry<K,Set<V>> element, final Object preventDefault) {
        throw new UnsupportedOperationException();
      }

      @Override
      @SuppressWarnings("unlikely-arg-type")
      protected boolean beforeRemove(final Object element) {
        if (element instanceof Map.Entry)
          AbstractDigraph.this.remove(((Map.Entry<?,?>)element).getKey());

        return false;
      }
    };
  }

  /**
   * Remove the vertex and its associated edges from this digraph.
   *
   * @param vertex The vertex to remove.
   * @return {@code true} if this digraph changed due to the method call.
   */
  private boolean removeKey(final Object vertex) {
    final Integer v = objectToIndex.remove(vertex);
    if (v == null)
      return false;

    final LinkedHashSet<Integer> ws = adj.set(v, null);
    if (ws != null)
      for (final int w : ws)
        inDegree.set(w, inDegree.get(w) - 1);

    adjRemoved.add(v);
    return true;
  }

  /**
   * Remove the association of ({@code vertex} ->> {@code edges}) from
   * this digraph.
   *
   * @param from The tail vertex.
   * @param to The head vertex.
   * @return {@code true} if this digraph changed due to the method call.
   */
  private boolean removeEdge(final Object from, final Object to) {
    final Integer v = objectToIndex.get(from);
    final Integer w = objectToIndex.get(to);
    return v != null && w != null && removeEdge((int)v, (int)w);
  }

  /**
   * Remove the association of ({@code vertex} ->> {@code edges}) from
   * this digraph.
   *
   * @param v The index of the tail vertex.
   * @param w The index of the head vertex.
   * @return {@code true} if this digraph changed due to the method call.
   */
  private boolean removeEdge(final int v, final int w) {
    inDegree.set(w, inDegree.get(w) - 1);

    // Invalidate the previous dfs() and getFlatAdj() operations, as the
    // digraph has changed
    reversePostOrder = null;
    flatAdj = null;
    cycle = null;

    return adj.get(v).remove(w);
  }

  /**
   * Remove the vertex and its associated edges from this digraph.
   *
   * @param vertex The vertex to remove.
   * @return {@code true} if this digraph changed due to the method call.
   */
  @Override
  @SuppressWarnings("unlikely-arg-type")
  public Set<V> remove(final Object vertex) {
    final Set<V> edges = get(vertex, null);
    if (edges == null && !containsKey(vertex))
      return null;

    final Set<V> transEdges = transverse.get(vertex, false);

    if (edges != null)
      for (final V edge : edges)
        transverse.removeEdge(edge, vertex);

    if (transEdges != null)
      for (final V edge : transEdges)
        removeEdge(edge, vertex);

    transverse.removeKey(vertex);
    removeKey(vertex);

    return edges;
  }

  private void clearData() {
    this.adj.clear();
    this.adjRemoved.clear();
    this.inDegree.clear();
    this.objectToIndex.clear();
    this.reversePostOrder = null;
    this.flatAdj = null;
    this.cycle = null;
  }

  /**
   * Removes all vertices and associated edges in this digraph.
   */
  @Override
  public void clear() {
    clearData();
    transverse.clearData();
  }

  /**
   * Returns the number of vertices in this digraph.
   *
   * @return The number of vertices in this digraph.
   */
  @Override
  public int size() {
    return objectToIndex.size();
  }

  /**
   * Returns {@code true} if this digraph contains no vertices.
   *
   * @return {@code true} if this digraph contains no vertices.
   */
  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Returns the index of {@code vertex} if exists, or fails with
   * {@link NoSuchElementException}.
   *
   * @param vertex The vertex.
   * @return The index of {@code vertex} if exists, or fails with
   *         {@link NoSuchElementException}.
   * @throws NoSuchElementException If {@code vertex} does not exist in this
   *           digraph.
   */
  private int getIndexFail(final K vertex) {
    final Integer v = objectToIndex.get(vertex);
    if (v == null)
      throw new NoSuchElementException("Vertex does not exist in this digraph");

    return v;
  }

  /**
   * Returns the number of directed edges incident to vertex {@code vertex}.
   *
   * @param vertex The vertex.
   * @return The number of directed edges incident to vertex {@code vertex}.
   * @throws NoSuchElementException If vertex {@code vertex} does not exist in
   *           this digraph.
   */
  public int getInDegree(final K vertex) {
    return inDegree.get(getIndexFail(vertex));
  }

  /**
   * Returns the number of directed edges incident from vertex {@code vertex}.
   *
   * @param vertex The vertex.
   * @return The number of directed edges incident from vertex {@code vertex}.
   * @throws NoSuchElementException If vertex {@code vertex} does not exist in
   *           this digraph.
   */
  public int getOutDegree(final K vertex) {
    return adj.get(getIndexFail(vertex)).size();
  }

  /**
   * Run the depth-first-search algorithm on this digraph to detect a cycle, or
   * construct the reversePostOrder list.
   *
   * @param reversePostOrder List of vertices filled in reverse post order.
   * @return A cycle list, if one was found.
   */
  private ArrayList<K> dfs(final List<? super K> reversePostOrder) {
    final int size = adj.size();
    final BitSet marked = new BitSet(size);
    final BitSet onStack = new BitSet(size);
    final int[] edgeTo = new int[size];
    for (int v = 0; v < size; ++v) {
      if (indexToObject.containsKey(v) && !marked.get(v)) {
        final ArrayList<K> cycle = dfs(marked, onStack, edgeTo, reversePostOrder, v);
        if (cycle != null)
          return cycle;
      }
    }

    return null;
  }

  private void dfs() {
    if (reversePostOrder == null && (cycle = dfs(reversePostOrder = new ArrayList<>(size()))) != null)
      reversePostOrder = null;
  }

  /**
   * Run the depth-first-search algorithm on this digraph to detect a cycle, or
   * construct the reversePostOrder list.
   *
   * @param marked {@link BitSet} maintaining marked state.
   * @param onStack {@link BitSet} maintaining on-stack state.
   * @param edgeTo Vertex index array maintaining state for cycle detection.
   * @param reversePostOrder List of vertices filled in reverse post order.
   * @param v The index of the vertex.
   * @return A cycle list, if one was found.
   */
  private ArrayList<K> dfs(final BitSet marked, final BitSet onStack, final int[] edgeTo, final List<? super K> reversePostOrder, final int v) {
    onStack.set(v);
    marked.set(v);
    final LinkedHashSet<Integer> ws = adj.get(v);
    if (ws != null) {
      for (final int w : ws) {
        if (!marked.get(w)) {
          edgeTo[w] = v;
          final ArrayList<K> cycle = dfs(marked, onStack, edgeTo, reversePostOrder, w);
          if (cycle != null)
            return cycle;
        }
        else if (v != w && onStack.get(w)) {
          final ArrayList<K> cycle = new ArrayList<>(initialCapacity / 3);
          for (int x = v; x != w; x = edgeTo[x])
            cycle.add(indexToKey(x));

          cycle.add(indexToKey(w));
          cycle.add(indexToKey(v));
          return cycle;
        }
      }
    }

    onStack.clear(v);
    reversePostOrder.add(0, indexToKey(v));
    return null;
  }

  /**
   * Returns a directed cycle if the digraph has one, and {@code null}
   * otherwise.
   *
   * @return A directed cycle if the digraph has one; otherwise {@code null}.
   */
  public List<K> getCycle() {
    dfs();
    return cycle;
  }

  /**
   * Specifies whether the digraph has a cycle.
   *
   * @return {@code true} if the digraph has a cycle; otherwise {@code false}.
   */
  public boolean hasCycle() {
    return getCycle() != null;
  }

  /**
   * Returns the reverse post order of a depth first search analysis of the
   * digraph, or {@code null} if no such order exists due to a cycle.
   *
   * @implSpec This method is not thread safe.
   * @return The reverse post order of a depth first search analysis of the
   *         digraph, or {@code null} if no such order exists due to a cycle.
   */
  public List<K> getTopologicalOrder() {
    dfs();
    return reversePostOrder;
  }

  /**
   * Returns the transverse of this digraph. Any changes made to the transverse
   * instance are reflected in this instance.
   *
   * @return The transverse of this digraph.
   */
  public AbstractDigraph<K,V> transverse() {
    return transverse;
  }

  @SuppressWarnings("unchecked")
  private AbstractDigraph<K,V> superClone() {
    try {
      final AbstractDigraph<K,V> clone = (AbstractDigraph<K,V>)super.clone();
      clone.objectToIndex = objectToIndex.clone();
      clone.indexToObject = clone.objectToIndex.reverse();
      clone.adj = (ArrayList<LinkedHashSet<Integer>>)adj.clone();
      for (int i = 0, len = clone.adj.size(); i < len; ++i) {
        final LinkedHashSet<Integer> set = clone.adj.get(i);
        clone.adj.set(i, set == null ? null : (LinkedHashSet<Integer>)set.clone());
      }

      clone.adjEdges = null;
      clone.adjRemoved = adjRemoved.clone();
      clone.observableObjectToIndex = observableObjectToIndex == null ? null : clone.getObservableObjectToIndex();
      clone.flatAdj = flatAdj == null ? null : flatAdj.clone();
      clone.inDegree = inDegree.clone();
      clone.cycle = cycle == null ? null : (ArrayList<K>)cycle.clone();
      clone.reversePostOrder = reversePostOrder == null ? null : (ArrayList<K>)reversePostOrder.clone();
      return clone;
    }
    catch (final CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Returns a clone of this digraph. The returned object does not share any
   * references to this object.
   */
  @Override
  public AbstractDigraph<K,V> clone() {
    final AbstractDigraph<K,V> clone = superClone();
    clone.transverse = transverse.superClone();
    clone.transverse.transverse = clone;
    return clone;
  }

  /**
   * Tests whether {@code this} digraph is equal to {@code obj}.
   *
   * @param obj The object to test for equality.
   * @return {@code true} if {@code this} digraph is equal to {@code obj};
   *         otherwise {@code false}.
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof AbstractDigraph))
      return false;

    final AbstractDigraph<K,V> that = (AbstractDigraph<K,V>)obj;
    if (size() != that.size())
      return false;

    if (!objectToIndex.keySet().equals(that.objectToIndex.keySet()))
      return false;

    for (final Object key : objectToIndex.keySet()) {
      final Set<V> edges = get(key, null);
      final Set<V> thatEdges = that.get(key, null);
      if (edges != null ? thatEdges == null || edges.size() != thatEdges.size() || !edges.containsAll(thatEdges) : thatEdges != null)
        return false;
    }

    return true;
  }

  /**
   * Returns the hash code value for this digraph. The hash code of a digraph is
   * computed by evaluating the hash codes of the member vertices with respect
   * to the directed edge definitions.
   *
   * @return The hash code value for this digraph. The hash code of a digraph is
   *         computed by evaluating the hash codes of the member vertices with
   *         respect to the directed edge definitions.
   */
  @Override
  public int hashCode() {
    final Set<Object> keys = objectToIndex.keySet();
    int hashCode = keys.hashCode();
    for (final Object key : keys)
      hashCode = 31 * hashCode + Objects.hashCode(get(key, null));

    return hashCode;
  }

  /**
   * Returns a string representation of this digraph, containing: the number of
   * vertices, followed by the number of edges, followed by the adjacency lists.
   *
   * @return A string representation of this digraph, containing: the number of
   *         vertices, followed by the number of edges, followed by the
   *         adjacency lists.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    for (int v = 0, len = adj.size(); v < len; ++v) {
      final Object obj = indexToObject.get(v);
      final LinkedHashSet<Integer> ws = adj.get(v);
      builder.append(obj).append(':');
      if (ws != null)
        for (final int w : ws)
          builder.append(' ').append(indexToObject.get(w));

      if (v < adj.size() - 1)
        builder.append('\n');
    }

    return builder.toString();
  }
}