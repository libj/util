/* Copyright (c) 2022 LibJ
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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * An extensible alternative to {@link Collections#unmodifiableMap(Map)} that provides an unmodifiable view of an underlying
 * {@link Map}.
 * <p>
 * This class allows modules to provide users with "read-only" access to internal collections. Query operations on the returned
 * collection "read through" to the specified collection, and attempts to modify the returned collection, whether direct or via its
 * iterator, result in an {@link UnsupportedOperationException}.
 * <p>
 * The map is serializable if the underlying map is serializable.
 *
 * @param <K> The type of keys maintained by the map
 * @param <V> The type of mapped values in the map.
 */
public class UnmodifiableMap<K,V> implements Map<K,V>, Serializable {
  private Map<? extends K,? extends V> target;

  /**
   * Creates a new {@link UnmodifiableMap} with the specified target {@link Map}.
   *
   * @param target The target {@link Map}.
   * @throws NullPointerException If the target {@link Map} is null.
   */
  public UnmodifiableMap(final Map<? extends K,? extends V> target) {
    this.target = Objects.requireNonNull(target);
  }

  /**
   * Creates a new {@link UnmodifiableMap} with a null target.
   */
  protected UnmodifiableMap() {
  }

  /**
   * Returns the underlying {@link #target}.
   *
   * @return The underlying {@link #target}.
   */
  protected Map<? extends K,? extends V> getTarget() {
    return target;
  }

  @Override
  public int size() {
    return getTarget().size();
  }

  @Override
  public boolean isEmpty() {
    return getTarget().isEmpty();
  }

  @Override
  public boolean containsKey(final Object key) {
    return getTarget().containsKey(key);
  }

  @Override
  public boolean containsValue(final Object val) {
    return getTarget().containsValue(val);
  }

  @Override
  public V get(final Object key) {
    return getTarget().get(key);
  }

  private transient Set<K> keySet;
  private transient Set<Map.Entry<K,V>> entrySet;
  private transient Collection<V> values;

  @Override
  public Set<K> keySet() {
    return keySet == null ? keySet = new UnmodifiableSet<>(getTarget().keySet()) : keySet;
  }

  @Override
  public Set<Map.Entry<K,V>> entrySet() {
    return entrySet == null ? entrySet = new UnmodifiableEntrySet<>(getTarget().entrySet()) : entrySet;
  }

  @Override
  public Collection<V> values() {
    return values == null ? values = new UnmodifiableCollection<>(getTarget().values()) : values;
  }

  @Override
  @SuppressWarnings("unchecked")
  public V getOrDefault(final Object k, final V defaultValue) {
    return ((Map<K,V>)target).getOrDefault(k, defaultValue);
  }

  @Override
  public void forEach(final BiConsumer<? super K,? super V> action) {
    getTarget().forEach(action);
  }

  @Override
  public V put(final K key, final V value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public V remove(final Object key) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void putAll(final Map<? extends K,? extends V> m) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void replaceAll(final BiFunction<? super K,? super V,? extends V> function) {
    throw new UnsupportedOperationException();
  }

  @Override
  public V putIfAbsent(final K key, final V value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean remove(final Object key, final Object value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean replace(final K key, final V oldValue, final V newValue) {
    throw new UnsupportedOperationException();
  }

  @Override
  public V replace(final K key, final V value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public V computeIfAbsent(final K key, final Function<? super K,? extends V> mappingFunction) {
    throw new UnsupportedOperationException();
  }

  @Override
  public V computeIfPresent(final K key, final BiFunction<? super K,? super V,? extends V> remappingFunction) {
    throw new UnsupportedOperationException();
  }

  @Override
  public V compute(final K key, final BiFunction<? super K,? super V,? extends V> remappingFunction) {
    throw new UnsupportedOperationException();
  }

  @Override
  public V merge(final K key, V value, final BiFunction<? super V,? super V,? extends V> remappingFunction) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean equals(final Object o) {
    return o == this || getTarget().equals(o);
  }

  @Override
  public int hashCode() {
    return getTarget().hashCode();
  }

  @Override
  public String toString() {
    return getTarget().toString();
  }

  /**
   * We need this class in addition to {@link UnmodifiableSet} as the {@link java.util.Map.Entry Map.Entry} itself permit modification
   * of the backing {@link Map} via the {@link java.util.Map.Entry#setValue setValue()} operation. This class is subtle: there are
   * many possible attacks that must be thwarted.
   *
   * @param <K> The type of keys maintained by this entry set.
   * @param <V> The type of mapped values.
   * @serial include
   */
  protected static class UnmodifiableEntrySet<K,V> extends UnmodifiableSet<Map.Entry<K,V>> {
    /**
     * Creates a new {@link UnmodifiableEntrySet} with the specified target {@link Set Set&lt;Map.Entry&lt;? extends K,? extends
     * V&gt;&gt;}.
     *
     * @param target The target {@link Set Set&lt;Map.Entry&lt;? extends K,? extends V&gt;&gt;}.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected UnmodifiableEntrySet(final Set<? extends Map.Entry<? extends K,? extends V>> target) {
      super((Set)target);
    }

    protected static <K,V> Consumer<Map.Entry<K,V>> entryConsumer(final Consumer<? super Map.Entry<K,V>> action) {
      Objects.requireNonNull(action);
      return (final Map.Entry<K,V> e) -> action.accept(new UnmodifiableEntry<>(e));
    }

    @Override
    public void forEach(final Consumer<? super Map.Entry<K,V>> action) {
      getTarget().forEach(entryConsumer(action));
    }

    protected static class UnmodifiableEntrySetSpliterator<K,V> implements Spliterator<Map.Entry<K,V>> {
      private Spliterator<Map.Entry<K,V>> target;

      /**
       * Creates a new {@link UnmodifiableEntrySetSpliterator} with the specified target {@link Spliterator}.
       *
       * @param target The target {@link Spliterator}.
       */
      private UnmodifiableEntrySetSpliterator(final Spliterator<Map.Entry<K,V>> target) {
        this.target = target;
      }

      /**
       * Returns the underlying {@link #target}.
       *
       * @return The underlying {@link #target}.
       */
      protected Spliterator<Map.Entry<K,V>> getTarget() {
        return target;
      }

      @Override
      public boolean tryAdvance(final Consumer<? super Map.Entry<K,V>> action) {
        return getTarget().tryAdvance(entryConsumer(action));
      }

      @Override
      public void forEachRemaining(final Consumer<? super Map.Entry<K,V>> action) {
        getTarget().forEachRemaining(entryConsumer(action));
      }

      @Override
      public Spliterator<Map.Entry<K,V>> trySplit() {
        final Spliterator<Map.Entry<K,V>> split = getTarget().trySplit();
        return split == null ? null : new UnmodifiableEntrySetSpliterator<>(split);
      }

      @Override
      public long estimateSize() {
        return getTarget().estimateSize();
      }

      @Override
      public long getExactSizeIfKnown() {
        return getTarget().getExactSizeIfKnown();
      }

      @Override
      public int characteristics() {
        return getTarget().characteristics();
      }

      @Override
      public boolean hasCharacteristics(final int characteristics) {
        return getTarget().hasCharacteristics(characteristics);
      }

      @Override
      public Comparator<? super Map.Entry<K,V>> getComparator() {
        return getTarget().getComparator();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Spliterator<Map.Entry<K,V>> spliterator() {
      return new UnmodifiableEntrySetSpliterator<>((Spliterator<Map.Entry<K,V>>)getTarget().spliterator());
    }

    @Override
    public Stream<Map.Entry<K,V>> stream() {
      return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public Stream<Map.Entry<K,V>> parallelStream() {
      return StreamSupport.stream(spliterator(), true);
    }

    @Override
    public Iterator<Map.Entry<K,V>> iterator() {
      return new Iterator<Map.Entry<K,V>>() {
        private final Iterator<? extends Map.Entry<? extends K,? extends V>> i = getTarget().iterator();

        @Override
        public boolean hasNext() {
          return i.hasNext();
        }

        @Override
        public Map.Entry<K,V> next() {
          return new UnmodifiableEntry<>(i.next());
        }

        @Override
        public void remove() {
          throw new UnsupportedOperationException();
        }
      };
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object[] toArray() {
      final Object[] a = getTarget().toArray();
      for (int i = 0, i$ = a.length; i < i$; ++i) // [A]
        a[i] = new UnmodifiableEntry<>((Map.Entry<? extends K,? extends V>)a[i]);

      return a;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(final T[] a) {
      final Object[] array = getTarget().toArray(a.length == 0 ? a : Arrays.copyOf(a, 0));
      for (int i = 0, i$ = array.length; i < i$; ++i) // [A]
        array[i] = new UnmodifiableEntry<>((Map.Entry<? extends K,? extends V>)array[i]);

      if (array.length > a.length)
        return (T[])array;

      System.arraycopy(array, 0, a, 0, array.length);
      if (a.length > array.length)
        a[array.length] = null;

      return a;
    }

    /**
     * This method is overridden to protect the backing set against an object with a nefarious equals function that senses that the
     * equality-candidate is {@link java.util.Map.Entry Map.Entry} and calls its {@link java.util.Map.Entry#setValue setValue()} method.
     */
    @Override
    public boolean contains(final Object o) {
      return o instanceof Map.Entry && getTarget().contains(new UnmodifiableEntry<>((Map.Entry<?,?>)o));
    }

    /**
     * The next two methods are overridden to protect against an unscrupulous List whose {@link #contains(Object)} method senses when o
     * is a {@link java.util.Map.Entry Map.Entry}, and calls {@link java.util.Map.Entry#setValue o.setValue()}.
     */
    @Override
    public boolean containsAll(final Collection<?> c) {
      if (c.size() > 0)
        for (final Object e : c) // [C]
          if (!contains(e))
            return false;

      return true;
    }

    @Override
    public boolean equals(final Object o) {
      if (o == this)
        return true;

      if (!(o instanceof Set))
        return false;

      final Set<?> that = (Set<?>)o;
      return that.size() == getTarget().size() && containsAll(that);
    }

    /**
     * This "wrapper class" serves two purposes: it prevents the client from modifying the backing {@link Map}, by short-circuiting the
     * {@link java.util.Map.Entry#setValue setValue()} method, and it protects the backing Map against an ill-behaved
     * {@link java.util.Map.Entry Map.Entry} that attempts to modify another {@link java.util.Map.Entry Map.Entry} when asked to perform
     * an equality check.
     *
     * @param <K> The type of key of the entry.
     * @param <V> The type of values of the entry.
     */
    protected static class UnmodifiableEntry<K,V> implements Map.Entry<K,V> {
      private Map.Entry<? extends K,? extends V> target;

      /**
       * Creates a new {@link UnmodifiableEntry} with the specified target {@link java.util.Map.Entry Map.Entry}.
       *
       * @param target The target {@link java.util.Map.Entry Map.Entry}.
       */
      protected UnmodifiableEntry(final Map.Entry<? extends K,? extends V> target) {
        this.target = target;
      }

      /**
       * Returns the underlying {@link #target}.
       *
       * @return The underlying {@link #target}.
       */
      protected Map.Entry<? extends K,? extends V> getTarget() {
        return target;
      }

      @Override
      public K getKey() {
        return getTarget().getKey();
      }

      @Override
      public V getValue() {
        return getTarget().getValue();
      }

      @Override
      public V setValue(final V value) {
        throw new UnsupportedOperationException();
      }

      @Override
      public int hashCode() {
        return getTarget().hashCode();
      }

      @Override
      public boolean equals(final Object o) {
        if (this == o)
          return true;

        if (!(o instanceof Map.Entry))
          return false;

        final Map.Entry<?,?> that = (Map.Entry<?,?>)o;
        return Objects.equals(getTarget().getKey(), that.getKey()) && Objects.equals(getTarget().getValue(), that.getValue());
      }

      @Override
      public String toString() {
        return getTarget().toString();
      }
    }
  }
}