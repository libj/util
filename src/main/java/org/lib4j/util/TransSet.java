package org.lib4j.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An implementation of the <code>Set</code> interface that transforms the
 * elements of the supplied source <code>Set</code> based on
 * <code>sourceToTarget</code> and <code>targetToSource</code> lambda
 * functions.
 *
 * @param <S> Type of source <code>Set</code>.
 * @param <T> Type of target <code>Set</code>.
 * @see Set
 */
public class TransSet<S,T> extends WrappedSet<T> {
  protected final Function<S,T> sourceToTarget;
  protected final Function<T,S> targetToSource;

  public TransSet(final Set<S> source, final Function<S,T> sourceToTarget, final Function<T,S> targetToSource) {
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

        final S e = iterator.next();
        return e == null ? null : sourceToTarget.apply(e);
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
    for (int i = 0; i < size(); i++) {
      final S e = iterator.next();
      array[i] = e == null ? null : sourceToTarget.apply(e);
    }

    return array;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <E>E[] toArray(final E[] a) {
    if (sourceToTarget == null)
      throw new UnsupportedOperationException();

    final Iterator<S> iterator = source.iterator();
    for (int i = 0; i < size(); i++) {
      final S e = iterator.next();
      a[i] = e == null ? null : (E)sourceToTarget.apply(e);
    }

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
      final S e = iterator.next();
      if (!c.contains(e == null ? null : sourceToTarget.apply(e))) {
        iterator.remove();
        changed = true;
      }
    }

    return changed;
  }

  @Override
  public boolean removeIf(final Predicate<? super T> filter) {
    if (sourceToTarget == null)
      throw new UnsupportedOperationException();

    return source.removeIf(new Predicate<S>() {
      @Override
      public boolean test(final S t) {
        return filter.test(sourceToTarget.apply(t));
      }
    });
  }
}