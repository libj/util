package org.lib4j.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

/**
 * An implementation of the Collection interface that transforms the elements
 * of the supplied source Collection based on <code>sourceToTarget</code> and
 * <code>targetToSource</code> lambda functions.
 *
 * @param <S> Type of source Collection.
 * @param <T> Type of target Collection.
 * @see Collection
 */
public class TransCollection<S,T> extends WrappedCollection<T> {
  protected final Function<S,T> sourceToTarget;
  protected final Function<T,S> targetToSource;

  public TransCollection(final Collection<S> source, final Function<S,T> sourceToTarget, final Function<T,S> targetToSource) {
    super();
    super.source = source;
    this.sourceToTarget = sourceToTarget;
    this.targetToSource = targetToSource;
  }

  @Override
  public boolean contains(final Object o) {
    if (sourceToTarget == null)
      throw new UnsupportedOperationException();

    final Iterator<S> iterator = source.iterator();
    if (o != null) {
      while (iterator.hasNext())
        if (o.equals(sourceToTarget.apply(iterator.next())))
          return true;
    }
    else {
      while (iterator.hasNext())
        if (iterator.next() == null)
          return true;
    }

    return false;
  }

  @Override
  public Iterator<T> iterator() {
    final Iterator<S> iterator = source.iterator();
    return new Iterator<T>() {
      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public T next() {
        if (sourceToTarget == null)
          throw new UnsupportedOperationException();

        return sourceToTarget.apply(iterator.next());
      }

      @Override
      public void remove() {
        iterator.remove();
      }
    };
  }

  @Override
  public Object[] toArray() {
    if (sourceToTarget == null)
      throw new UnsupportedOperationException();

    final Object[] array = new Object[size()];
    final Iterator<S> iterator = source.iterator();
    for (int i = 0; i < size(); i++)
      array[i] = sourceToTarget.apply(iterator.next());

    return array;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <E>E[] toArray(final E[] a) {
    if (sourceToTarget == null)
      throw new UnsupportedOperationException();

    final Iterator<S> iterator = source.iterator();
    for (int i = 0; i < size(); i++)
      a[i] = (E)sourceToTarget.apply(iterator.next());

    return a;
  }

  @Override
  public boolean add(final T e) {
    if (targetToSource == null)
      throw new UnsupportedOperationException();

    return source.add(targetToSource.apply(e));
  }

  @Override
  public boolean remove(final Object o) {
    if (sourceToTarget == null)
      throw new UnsupportedOperationException();

    final Iterator<S> iterator = source.iterator();
    while (iterator.hasNext()) {
      final S e = iterator.next();
      if (o != null ? o.equals(sourceToTarget.apply(e)) : sourceToTarget.apply(e) == null) {
        iterator.remove();
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean containsAll(final Collection<?> c) {
    for (final Object e : c)
      if (contains(e))
        return true;

    return false;
  }

  @Override
  public boolean addAll(final Collection<? extends T> c) {
    boolean changed = false;
    for (final T e : c)
      changed = add(e) || changed;

    return changed;
  }

  @Override
  public boolean removeAll(final Collection<?> c) {
    boolean changed = false;
    for (final Object element : c)
      changed = remove(element) || changed;

    return changed;
  }

  @Override
  public boolean retainAll(final Collection<?> c) {
    boolean changed = false;
    final Iterator<S> iterator = source.iterator();
    while (iterator.hasNext()) {
      if (!c.contains(sourceToTarget.apply(iterator.next()))) {
        iterator.remove();
        changed = true;
      }
    }

    return changed;
  }
}