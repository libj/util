/* Copyright (c) 2017 FastJAX
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

package org.fastjax.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;

import org.slf4j.Logger;

public abstract class PartitionedList<E,T> extends ObservableList<E> implements Cloneable {
  public class PartitionList<P extends E> extends ObservableList<P> implements Cloneable {
    protected T type;
    protected ArrayIntList indexes;

    protected PartitionList(final T type) {
      super(new ArrayList<P>());
      this.type = type;
      this.indexes = new ArrayIntList();
    }

    public T getType() {
      return this.type;
    }

    private ArrayIntList getIndexes() {
      return this.indexes;
    }

    private void addUnsafe(final E e) {
      super.target.add(e);
    }

    private void addUnsafe(final int index, final E e) {
      super.target.add(index, e);
    }

    @SuppressWarnings("unchecked")
    private E setUnsafe(final int index, final E e) {
      return (E)super.target.set(index, e);
    }

    @SuppressWarnings("unchecked")
    private E removeUnsafe(final int index) {
      this.indexes.removeIndex(index);
      return (E)super.target.remove(index);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void afterAdd(final int index, final P e, final RuntimeException exception) {
      if (index == size() - 1) {
        final PartitionedList<E,T> superList = PartitionedList.this;
        superList.indexes.add(index);
        superList.subLists.add(this);
        superList.addUnsafe(e);

        indexes.add(superList.size() - 1);
      }
      else {
        final int superIndex = index > 0 ? indexes.get(index - 1) + 1 : indexes.get(index);
        final PartitionList<P> subList = this;
        final PartitionedList<E,T> superList = PartitionedList.this;
        superList.indexes.add(superIndex, index);
        superList.subLists.add(superIndex, subList);
        superList.addUnsafe(superIndex, e);
        final IdentityHashSet<PartitionList<P>> visited = new IdentityHashSet<>();
        for (int i = superIndex + 1; i < superList.size(); ++i) {
          final PartitionList<P> nextSubList = (PartitionedList<E,T>.PartitionList<P>)superList.subLists.get(i);
          if (nextSubList == subList)
            superList.indexes.set(i, superList.indexes.get(i) + 1);

          if (visited.contains(nextSubList) || nextSubList == subList)
            continue;

          visited.add(nextSubList);
          incSuperIndexes(nextSubList, superIndex);
        }

        indexes.add(index, superIndex);
        for (int i = index + 1; i < indexes.size(); ++i)
          indexes.set(i, indexes.get(i) + 1);
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected boolean beforeRemove(final int index) {
      final int superIndex = indexes.removeIndex(index);
      final PartitionedList<E,T> superList = PartitionedList.this;
      superList.removeUnsafe(superIndex);
      superList.indexes.removeIndex(superIndex);
      final PartitionList<P> subList = (PartitionedList<E,T>.PartitionList<P>)superList.subLists.remove(superIndex);
      final IdentityHashSet<PartitionList<P>> visited = new IdentityHashSet<>();
      for (int i = superIndex; i < superList.size(); ++i) {
        final PartitionList<P> nextSubList = (PartitionedList<E,T>.PartitionList<P>)superList.subLists.get(i);
        if (nextSubList == subList)
          superList.indexes.set(i, superList.indexes.get(i) - 1);

        if (visited.contains(nextSubList) || nextSubList == subList)
          continue;

        visited.add(nextSubList);
        decSuperIndexes(nextSubList, superIndex);
      }

      for (int i = index; i < indexes.size(); ++i)
        indexes.set(i, indexes.get(i) - 1);

      return true;
    }

    @Override
    protected boolean beforeSet(final int index, final P newElement) {
      final int superIndex = indexes.get(index);
      subLists.set(superIndex, this);
      PartitionedList.this.setUnsafe(superIndex, newElement);
      return true;
    }

    protected PartitionedList<E,T> getSuperList() {
      return PartitionedList.this;
    }

    protected void print(final Logger logger) {
      final StringBuilder builder = new StringBuilder();
      builder.append("    SubList<" + type + "> ").append(Objects.simpleIdentity(this)).append('\n');
      builder.append("    I:");
      indexes.stream().forEach(i -> builder.append(' ').append(i));
      builder.append("\n    E:");
      stream().forEach(e -> builder.append(' ').append(Objects.simpleIdentity(e)));
      logger.info(builder.append('\n').toString());
    }

    @Override
    @SuppressWarnings("unchecked")
    public PartitionList<P> clone() {
      try {
        final PartitionList<P> clone = (PartitionList<P>)super.clone();
        clone.target = (ArrayList<E>)((ArrayList<E>)target).clone();
        clone.type = type;
        clone.indexes = indexes.clone();
        return clone;
      }
      catch (final CloneNotSupportedException e) {
        throw new UnsupportedOperationException(e);
      }
    }
  }

  protected ArrayIntList indexes;
  protected ArrayList<PartitionList<? extends E>> subLists;
  protected HashMap<T,PartitionList<? extends E>> typeToSubList = new HashMap<>();

  public PartitionedList() {
    super(new ArrayList<E>());
    this.indexes = new ArrayIntList();
    this.subLists = new ArrayList<>();
  }

  @SafeVarargs
  public PartitionedList(final T ... types) {
    this();
    for (final T type : types)
      typeToSubList.put(type, null);
  }

  public PartitionedList(final Collection<T> types) {
    this();
    if (types != null)
      for (final T type : types)
        typeToSubList.put(type, null);
  }

  protected PartitionList<E> newPartition(final T type) {
    return new PartitionList<>(type);
  }

  protected abstract PartitionList<E> getPartition(final Class<? extends E> type);

  private void addUnsafe(final E e) {
    super.target.add(e);
  }

  private void addUnsafe(final int index, final E e) {
    super.target.add(index, e);
  }

  @SuppressWarnings("unchecked")
  private E setUnsafe(final int index, final E e) {
    return (E)super.target.set(index, e);
  }

  @SuppressWarnings("unchecked")
  private E removeUnsafe(final int index) {
    return (E)super.target.remove(index);
  }

  private int add(final Integer superIndex, final Integer subIndex, final E element, final PartitionList<E> subList) {
    if (superIndex == null) {
      subList.add(element);
    }
    else if (subIndex == null) {
      subList.indexes.add(superIndex);
      subList.addUnsafe(element);
    }
    else {
      subList.addUnsafe(subIndex, element);
      subList.indexes.add(subIndex, superIndex);
      for (int i = subIndex + 1; i < subList.indexes.size(); ++i)
        subList.indexes.set(i, subList.indexes.get(i) + 1);
    }

    return subIndex != null ? subIndex : this.size() - 1;
  }

  private void incSuperIndexes(final PartitionList<? extends E> subList, final int index) {
    final ArrayIntList superIndexes = subList.getIndexes();
    for (int j = 0; j < superIndexes.size(); j++) {
      final int superIndex = superIndexes.get(j);
      if (superIndex >= index)
        superIndexes.set(j, superIndex + 1);
    }
  }

  private void decSuperIndexes(final PartitionList<? extends E> subList, final int index) {
    final ArrayIntList superIndexes = subList.getIndexes();
    for (int j = 0; j < superIndexes.size(); j++) {
      final int superIndex = superIndexes.get(j);
      if (superIndex > index)
        superIndexes.set(j, superIndex - 1);
    }
  }

  private int findSubIndex(final PartitionList<? extends E> subList, final int index) {
    if (subList.size() == 0)
      return 0;

    final ArrayIntList superIndexes = subList.getIndexes();
    for (int i = subList.size() - 1; i >= 0; i--) {
      final int superIndex = superIndexes.get(i);
      if (superIndex < index)
        return index;
    }

    return 0;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void afterAdd(final int index, final E e, final RuntimeException exception) {
    final PartitionList<E> subList = getPartition((Class<E>)e.getClass());
    if (subList == null)
      throw new IllegalArgumentException("Object of type " + e.getClass() + " is not allowed to appear in " + PartitionedList.class.getName());

    subLists.add(index, subList);
    if (index == size() - 1) {
      indexes.add(subList.size());
      add(index, null, e, subList);
    }
    else {
      final int subIndex = index == 0 ? 0 : findSubIndex(subList, index);
      add(index, subIndex, e, subList);
      if (subIndex != -1) {
        indexes.add(index, subIndex);
        final IdentityHashSet<PartitionList<? extends E>> visited = new IdentityHashSet<>();
        for (int i = index + 1; i < subLists.size(); ++i) {
          final PartitionList<? extends E> nextSubList = subLists.get(i);
          if (nextSubList == subList)
            indexes.set(i, indexes.get(i) + 1);

          if (visited.contains(nextSubList) || nextSubList == subList)
            continue;

          visited.add(nextSubList);
          incSuperIndexes(nextSubList, index);
        }
      }
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  protected boolean beforeSet(final int index, final E newElement) {
    final PartitionList<? extends E> subList = subLists.get(index);
    final E element = get(index);
    final int subIndex = indexes.get(index);
    if (element.getClass() == newElement.getClass()) {
      subList.setUnsafe(subIndex, newElement);
    }
    else {
      final PartitionList<E> newSubList = getPartition((Class<E>)newElement.getClass());
      if (newSubList == null)
        throw new IllegalArgumentException("Object of type " + newElement.getClass() + " is not allowed to appear in " + PartitionedList.class.getName());

      subList.removeUnsafe(subIndex);
      for (int i = index + 1; i < subLists.size(); ++i) {
        final PartitionList<? extends E> nextSubList = subLists.get(i);
        if (nextSubList == subList)
          indexes.set(i, indexes.get(i) - 1);
      }

      add(index, 0, newElement, newSubList);
      subLists.set(index, newSubList);
    }

    return true;
  }

  @Override
  protected boolean beforeRemove(final int index) {
    final PartitionList<? extends E> subList = subLists.remove(index);
    subList.removeUnsafe(indexes.removeIndex(index));
    final ArrayIntList subIndexes = subList.getIndexes();
    for (int i = index; i < subIndexes.size(); ++i)
      subIndexes.set(i, subIndexes.get(i) - 1);

    final IdentityHashSet<PartitionList<? extends E>> visited = new IdentityHashSet<>();
    for (int i = index; i < subLists.size(); ++i) {
      final PartitionList<? extends E> nextSubList = subLists.get(i);
      if (nextSubList == subList)
        indexes.set(i, indexes.get(i) - 1);

      if (visited.contains(nextSubList) || nextSubList == subList)
        continue;

      visited.add(nextSubList);
      decSuperIndexes(nextSubList, index);
    }

    return true;
  }

  protected void print(final Logger logger) {
    final StringBuilder builder = new StringBuilder();
    builder.append("  I:");
    indexes.stream().forEach(i -> builder.append(' ').append(i));
    builder.append("\n  E:");
    stream().forEach(e -> builder.append(' ').append(Objects.simpleIdentity(e)));
    builder.append("\n  A:");
    subLists.stream().forEach(e -> builder.append(' ').append(Objects.simpleIdentity(e)));
    logger.info(builder.append('\n').toString());
    new IdentityHashSet<>(subLists).stream().forEach(e -> e.print(logger));
  }

  protected E clone(final E item) {
    return item;
  }

  @Override
  @SuppressWarnings("unchecked")
  public PartitionedList<E,T> clone() {
    try {
      final PartitionedList<E,T> clone = (PartitionedList<E,T>)super.clone();
      clone.target = (ArrayList<E>)((ArrayList<E>)target).clone();
      clone.indexes = indexes.clone();
      clone.subLists = new ArrayList<>();
      for (final PartitionList<? extends E> subList : subLists)
        clone.subLists.add(subList.clone());

      final IdentityHashMap<E,E> clones = new IdentityHashMap<>();
      for (int i = 0; i < clone.target.size(); ++i) {
        final E item = (E)clone.target.get(i);
        final E copy = clone(item);
        clones.put(item, copy);
        clone.target.set(i, copy);
      }

      clone.typeToSubList = new HashMap<>();
      for (final PartitionList<? extends E> subList : clone.subLists) {
        clone.typeToSubList.put(subList.getType(), subList);
        for (int i = 0; i < subList.target.size(); ++i)
          subList.target.set(i, clones.get(subList.target.get(i)));
      }

      return clone;
    }
    catch (final CloneNotSupportedException e) {
      throw new UnsupportedOperationException(e);
    }
  }
}