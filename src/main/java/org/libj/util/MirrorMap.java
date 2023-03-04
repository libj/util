/* Copyright (c) 2019 LibJ
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

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 * A map that maintains 2 representations of its key-value entries:
 * <ol>
 * <li>For value elements of type {@code <V>}, which is the generic type of {@code this} map.</li>
 * <li>For reflected elements of type {@code <R>}, which is the generic type of the map returned by {@link #getMirrorMap()}</li>
 * </ol>
 * The {@link Mirror} provides the {@link Mirror#valueToReflection(Object,Object) V -> R} and
 * {@link Mirror#reflectionToValue(Object,Object) R -> V} methods, which are used to reflect values from one {@link MirrorMap} to
 * the other.
 * <p>
 * Entries added to either sides of the {@link MirrorMap} are "lazy-reflected" -- i.e. instead of reflecting entries upon their
 * addition to the map, entries are reflected upon retrieval from the map.
 * <p>
 * The {@link #getMirrorMap()} method returns the {@link MirrorMap} instance of type {@code <K,R>} that represents the one-to-one
 * mapping with its mirror of type {@code <K,V>} (i.e. {@code this} instance). Changes to the {@link MirrorMap} will be reflected in
 * {@code this} instance, and vice-versa.
 *
 * @param <K> The type of keys maintained by this map.
 * @param <V> The type of value elements in this map.
 * @param <R> The type of reflected value elements in the mirror map.
 */
public class MirrorMap<K,V,R> extends ObservableMap<K,V> {
  /**
   * Interface providing methods for the reflection of one type of object to another, and vice-versa.
   *
   * @param <K> The type of keys maintained by this map.
   * @param <V> The type of value object of this mirror.
   * @param <R> The type of reflected value object of this mirror.
   */
  public interface Mirror<K,V,R> {
    /**
     * Reflects a value object of type {@code <V>} to a reflected value object of type {@code <R>}.
     *
     * @param key The key.
     * @param value The value object of type {@code <V>}.
     * @return The reflected value object of type {@code <R>}.
     */
    R valueToReflection(K key, V value);

    /**
     * Reflects a reflected value object of type {@code <R>} to a value object of type {@code <V>}.
     *
     * @param key The key.
     * @param reflection The reflected value object of type {@code <R>}.
     * @return The value object of type {@code <V>}.
     */
    V reflectionToValue(K key, R reflection);

    /**
     * Returns the reverse representation of this {@link Mirror}, whereby the value object type {@code <V>} and reflected value
     * object of type {@code <R>} are swapped.
     *
     * @return The reverse representation of this {@link Mirror}.
     */
    default Mirror<K,R,V> reverse() {
      return new Mirror<K,R,V>() {
        @Override
        public V valueToReflection(final K key, final R value) {
          return Mirror.this.reflectionToValue(key, value);
        }

        @Override
        public R reflectionToValue(final K key, final V reflection) {
          return Mirror.this.valueToReflection(key, reflection);
        }
      };
    }
  }

  /**
   * Object to be used in the underlying maps to represent that a value is pending reflection via the methods in {@link #mirror}
   * upon the invocation of {@link #beforeGet(Object)}.
   */
  protected static final Object PENDING = new Object();

  private final Mirror<K,V,R> mirror;
  private Mirror<K,R,V> reverse;
  protected MirrorMap<K,R,V> mirrorMap;
  protected Iterator<?> targetLock;
  protected boolean inited;

  /**
   * Creates a new {@link MirrorMap} with the specified target maps and {@link Mirror}. The specified target maps are meant to be
   * empty, as they become the underlying maps of the new {@link MirrorMap} instance. The specified {@link Mirror} provides the
   * {@link Mirror#valueToReflection(Object,Object) V -> R} and {@link Mirror#reflectionToValue(Object,Object) R -> V} methods,
   * which are used to reflect object values from one {@link MirrorMap} to the other.
   *
   * @param values The underlying map of type {@code <K,V>}.
   * @param reflections The underlying map of type {@code <K,R>}.
   * @param mirror The {@link Mirror} specifying the {@link Mirror#valueToReflection(Object,Object) V -> R} and
   *          {@link Mirror#reflectionToValue(Object,Object) R -> V} methods.
   * @throws NullPointerException If any of the specified parameters is null.
   */
  public MirrorMap(final Map<K,V> values, final Map<K,R> reflections, final Mirror<K,V,R> mirror) {
    super(values);
    this.mirror = Objects.requireNonNull(mirror);
    this.reflections = Objects.requireNonNull(reflections);
  }

  /**
   * Creates a new {@link MirrorMap} with the specified maps and mirror. This method is specific for the construction of a reflected
   * {@link MirrorMap} instance.
   *
   * @param mirrorMap The {@link MirrorMap} for which {@code this} map will be a reflection. Likewise, {@code this} map will be a
   *          reflection for {@code mirrorMap}.
   * @param values The underlying map of type {@code <K,V>}.
   * @param mirror The {@link Mirror} specifying the {@link Mirror#valueToReflection(Object,Object) V -> R} and
   *          {@link Mirror#reflectionToValue(Object,Object) R -> V} methods.
   */
  protected MirrorMap(final MirrorMap<K,R,V> mirrorMap, final Map<K,V> values, final Mirror<K,V,R> mirror) {
    super(values);
    this.mirror = mirror;
    this.mirrorMap = mirrorMap;
  }

  /**
   * Factory method that returns a new instance of a {@link MirrorMap} with the specified target maps. This method is intended to be
   * overridden by subclasses in order to provide instances of the subclass.
   *
   * @param values The underlying map of type {@code <K,V>}.
   * @param reflections The underlying map of type {@code <K,R>}.
   * @return A new instance of a {@link MirrorMap} with the specified target maps.
   */
  protected MirrorMap<K,V,R> newInstance(final Map<K,V> values, final Map<K,R> reflections) {
    return new MirrorMap<>(values, reflections, mirror);
  }

  /**
   * Factory method that returns a new <b>mirror</b> instance of a {@link MirrorMap} with the specified target map. This method is
   * intended to be overridden by subclasses in order to provide instances of the subclass.
   *
   * @param values The underlying map of type {@code <K,V>}.
   * @return A new instance of a {@link MirrorMap} with the specified target maps.
   */
  protected MirrorMap<K,R,V> newMirrorInstance(final Map<K,R> values) {
    return new MirrorMap<>(this, values, getReverseMirror());
  }

  protected class ObservableMirrorEntrySet extends ObservableMap<K,V>.ObservableEntrySet {
    protected ObservableMirrorEntrySet(final Set<Entry<K,V>> set) {
      super(set);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Entry<K,V> afterGet(final Entry<K,V> value, final RuntimeException e) {
      if (value.getValue() == PENDING) {
        unlocked = unlock();
        final K key = value.getKey();
        value.setValue(mirror.reflectionToValue(key, (R)mirrorMap.target.get(key)));
        lock(unlocked);
      }

      return value;
    }
  }

  @Override
  protected ObservableMap<K,V>.ObservableEntrySet newEntrySet() {
    return new ObservableMirrorEntrySet(target.entrySet());
  }

  /**
   * Underlying map to be used for {@link #mirrorMap}, or {@code null} if {@link #mirrorMap} is already instantiated.
   */
  private Map<K,R> reflections;

  /**
   * Returns the instantiated {@link #mirrorMap}. If the {@link #mirrorMap} is not instantiated, this method uses the
   * {@link #reflections} as the underlying map to be consumed by {@link #newMirrorInstance(Map)}.
   *
   * @param require If {@code true}, the {@link #mirrorMap} will be initialized given that {@link #reflections} is not null; if
   *          {@code false}, the {@link #mirrorMap} will be initialized given that {@link #reflections} is not null and not empty.
   * @return The instantiated {@link #mirrorMap}.
   */
  @SuppressWarnings("unchecked")
  private MirrorMap<K,R,V> mirrorMap(final boolean require) {
    if (reflections != null && (require || reflections.size() > 0)) {
      mirrorMap = newMirrorInstance(reflections);
      reflections = null;

      final int comparison = Integer.compare(target.size(), mirrorMap.target.size());
      if (comparison == 0)
        return mirrorMap;

      final Map<Object,Object> less;
      final Map<Object,Object> more;
      if (comparison < 1) {
        less = target;
        more = mirrorMap.target;
      }
      else {
        less = mirrorMap.target;
        more = target;
      }

      boolean unlocked = false;
      if (more.size() > 0) {
        for (final Object key : more.keySet()) { // [S]
          if (!less.containsKey(key)) {
            unlocked |= unlock();
            less.put(key, PENDING);
          }
        }
      }

      lock(unlocked);
    }

    return mirrorMap;
  }

  /**
   * Returns the {@link MirrorMap} instance of type {@code <K,R>} that represents the one-to-one mapping with its mirror of type
   * {@code <K,V>} (i.e. {@code this} instance). Changes to the {@link MirrorMap} will be reflected in {@code this} instance, and
   * vice-versa.
   *
   * @return The {@link MirrorMap} instance of type {@code <K,R>} that retains the one-to-one mapping with its mirror of type
   *         {@code <K,V>} (i.e. {@code this} instance).
   */
  public MirrorMap<K,R,V> getMirrorMap() {
    return mirrorMap(true);
  }

  /**
   * Returns the {@link Mirror} for this {@link MirrorMap}.
   *
   * @return The {@link Mirror} for this {@link MirrorMap}.
   */
  public Mirror<K,V,R> getMirror() {
    return this.mirror;
  }

  /**
   * Returns the reverse {@link Mirror} for this {@link MirrorMap}, and caches it for subsequent retrieval, avoiding
   * reinstantiation.
   *
   * @return The reverse {@link Mirror} for this {@link MirrorMap}.
   */
  protected Mirror<K,R,V> getReverseMirror() {
    return reverse == null ? reverse = mirror.reverse() : reverse;
  }

  /**
   * Locks the {@link #target underlying map} to detect concurrent modification. Specifically, this method calls
   * {@link Set#iterator() target.entrySet().iterator()} and saves its reference for examination during the next method call that is
   * made to the {@link MirrorMap}. If the call to {@link Iterator#next()} on the {@link #targetLock saved iterator} results in a
   * {@link ConcurrentModificationException}, it means that the {@link #target underlying map} was modified outside of the
   * {@link MirrorMap}. Such modifications are not allowed, because they risk compromising the data integrity of the this map and
   * its {@link #mirrorMap}.
   *
   * @param unlocked If {@code true}, this map is unlocked and will be locked; if {@code false}, this map is already locked and will
   *          not be relocked.
   */
  protected void lock(final boolean unlocked) {
    if (unlocked) {
      targetLock = target.entrySet().iterator();
      if (mirrorMap != null)
        getMirrorMap().targetLock = mirrorMap.target.entrySet().iterator();
    }
  }

  /**
   * Unlocks the {@link #target underlying map} and checks for concurrent modification. Specifically, this method calls
   * {@link Iterator#next()} on {@link #targetLock}, which may result in a {@link ConcurrentModificationException} if the
   * {@link #target underlying map} was modified outside of this {@link MirrorMap}.
   *
   * @return Whether this map was locked prior to the execution of this method.
   * @throws ConcurrentModificationException If the {@link #target underlying map} was modified outside of the {@link MirrorMap}
   */
  protected boolean unlock() throws ConcurrentModificationException {
    if (inited && targetLock == null)
      return false;

    if (targetLock != null) {
      try {
        targetLock.next();
        if (mirrorMap != null && getMirrorMap().targetLock != null)
          getMirrorMap().targetLock.next();
      }
      catch (final NoSuchElementException ignored) {
      }

      targetLock = null;
    }
    else {
      inited = true;
    }

    return true;
  }

  @Override
  public int size() {
    return reflections != null && reflections.size() > 0 ? reflections.size() : super.size();
  }

  private boolean unlocked;

  @Override
  @SuppressWarnings("unchecked")
  protected V beforePut(final K key, final V oldValue, final V newValue, final Object preventDefault) {
    if (mirrorMap(false) != null) {
      unlocked = unlock();
      mirrorMap.target.put(key, PENDING);
    }

    return newValue;
  }

  @Override
  protected void afterPut(final K key, final V oldValue, final V newValue, final RuntimeException e) {
    lock(unlocked);
  }

  @Override
  protected boolean beforeRemove(final Object key, final V value) {
    if (mirrorMap(false) != null) {
      unlocked = unlock();
      mirrorMap.target.remove(key);
      lock(unlocked);
    }

    return true;
  }

  @Override
  protected void afterRemove(final Object key, final V value, final RuntimeException e) {
    lock(unlocked);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected boolean beforeContainsKey(final Object key) {
    unlocked = unlock();
    mirrorMap(true);
    final Object obj = target.get(key);
    if (obj == PENDING)
      target.put(key, mirror.reflectionToValue((K)key, (R)mirrorMap.target.get(key)));

    return true;
  }

  @Override
  protected void afterContainsKey(final Object key, final boolean result, final RuntimeException e) {
    lock(unlocked);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void beforeContainsValue(final Object value) {
    boolean unlocked = false;
    if (target.size() > 0) {
      for (final Map.Entry<Object,Object> entry : (Set<Map.Entry<Object,Object>>)target.entrySet()) { // [S]
        if (entry.getValue() == PENDING) {
          unlocked |= unlock();
          final Object reflection = mirror.reflectionToValue((K)entry.getKey(), (R)mirrorMap.target.get(entry.getKey()));
          entry.setValue(reflection);
          if (Objects.equals(value, reflection))
            break;
        }
      }
    }

    lock(unlocked);
  }

  @Override
  protected void afterContainsValue(final Object value, final boolean result, final RuntimeException e) {
    lock(unlocked);
  }

  @Override
  protected Object beforeGet(final Object key) {
    beforeContainsKey(key);
    return key;
  }

  @Override
  protected V afterGet(final Object key, final V value, final RuntimeException e) {
    lock(unlocked);
    return value;
  }
}