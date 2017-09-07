package org.lib4j.util;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Wrapper class for <code>Set</code> interface that delegates all
 * methods to the wrapped instance.
 *
 * @see Set
 */
public class WrappedSet<E> extends AbstractSet<E> implements Set<E> {
  @SuppressWarnings("rawtypes")
  protected Set source;

  public WrappedSet(final Set<E> set) {
    this.source = set;
  }

  protected WrappedSet() {
  }

  @Override
  public int size() {
    return source.size();
  }

  @Override
  public boolean isEmpty() {
    return source.isEmpty();
  }

  @Override
  public boolean contains(final Object o) {
    return source.contains(o);
  }

  @Override
  public Iterator<E> iterator() {
    return source.iterator();
  }

  @Override
  public Object[] toArray() {
    return source.toArray();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T>T[] toArray(final T[] a) {
    return (T[])source.toArray(a);
  }

  @Override
  public boolean add(final E e) {
    return source.add(e);
  }

  @Override
  public boolean remove(final Object o) {
    return source.remove(o);
  }

  @Override
  public boolean containsAll(final Collection<?> c) {
    return source.containsAll(c);
  }

  @Override
  public boolean addAll(final Collection<? extends E> c) {
    return source.addAll(c);
  }

  @Override
  public boolean removeAll(final Collection<?> c) {
    return source.removeAll(c);
  }

  @Override
  public boolean retainAll(final Collection<?> c) {
    return source.retainAll(c);
  }

  @Override
  public void clear() {
    source.clear();
  }

  @Override
  public void forEach(final Consumer<? super E> action) {
    source.forEach(action);
  }

  @Override
  public boolean removeIf(final Predicate<? super E> filter) {
    return source.removeIf(filter);
  }

  @Override
  public Spliterator<E> spliterator() {
    return source.spliterator();
  }

  @Override
  public Stream<E> stream() {
    return source.stream();
  }

  @Override
  public Stream<E> parallelStream() {
    return source.parallelStream();
  }
}