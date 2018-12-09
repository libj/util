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

public abstract class MergedList<E,T> extends ObservableList<E> implements Cloneable {
  public class PartList<P extends E> extends ObservableList<P> implements Cloneable {
    protected T type;
    protected ArrayIntList indexes;

    protected PartList(final T type) {
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
        final MergedList<E,T> mergedList = MergedList.this;
        mergedList.indexes.add(index);
        mergedList.partLists.add(this);
        mergedList.addUnsafe(e);

        indexes.add(mergedList.size() - 1);
      }
      else {
        final int mergedIndex = index > 0 ? indexes.get(index - 1) + 1 : indexes.get(index);
        final PartList<P> partList = this;
        final MergedList<E,T> mergedList = MergedList.this;
        mergedList.indexes.add(mergedIndex, index);
        mergedList.partLists.add(mergedIndex, partList);
        mergedList.addUnsafe(mergedIndex, e);
        final IdentityHashSet<PartList<P>> visited = new IdentityHashSet<>();
        for (int i = mergedIndex + 1; i < mergedList.size(); ++i) {
          final PartList<P> nextPartList = (MergedList<E,T>.PartList<P>)mergedList.partLists.get(i);
          if (nextPartList == partList)
            mergedList.indexes.set(i, mergedList.indexes.get(i) + 1);

          if (visited.contains(nextPartList) || nextPartList == partList)
            continue;

          visited.add(nextPartList);
          incSectionIndexes(nextPartList, mergedIndex);
        }

        indexes.add(index, mergedIndex);
        for (int i = index + 1; i < indexes.size(); ++i)
          indexes.set(i, indexes.get(i) + 1);
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected boolean beforeRemove(final int index) {
      final int mergedIndex = indexes.removeIndex(index);
      final MergedList<E,T> mergedList = MergedList.this;
      mergedList.removeUnsafe(mergedIndex);
      mergedList.indexes.removeIndex(mergedIndex);
      final PartList<P> partList = (MergedList<E,T>.PartList<P>)mergedList.partLists.remove(mergedIndex);
      final IdentityHashSet<PartList<P>> visited = new IdentityHashSet<>();
      for (int i = mergedIndex; i < mergedList.size(); ++i) {
        final PartList<P> nextPartList = (MergedList<E,T>.PartList<P>)mergedList.partLists.get(i);
        if (nextPartList == partList)
          mergedList.indexes.set(i, mergedList.indexes.get(i) - 1);

        if (visited.contains(nextPartList) || nextPartList == partList)
          continue;

        visited.add(nextPartList);
        decSectionIndexes(nextPartList, mergedIndex);
      }

      for (int i = index; i < indexes.size(); ++i)
        indexes.set(i, indexes.get(i) - 1);

      return true;
    }

    @Override
    protected boolean beforeSet(final int index, final P newElement) {
      final int mergedIndex = indexes.get(index);
      partLists.set(mergedIndex, this);
      MergedList.this.setUnsafe(mergedIndex, newElement);
      return true;
    }

    protected MergedList<E,T> getMergedList() {
      return MergedList.this;
    }

    protected void print(final Logger logger) {
      final StringBuilder builder = new StringBuilder();
      builder.append("    PartList<").append(type).append("> ").append(Objects.simpleIdentity(this)).append('\n');
      builder.append("    I:");
      indexes.stream().forEach(i -> builder.append(' ').append(i));
      builder.append("\n    E:");
      forEach(e -> builder.append(' ').append(Objects.simpleIdentity(e)));
      logger.info(builder.append('\n').toString());
    }

    @Override
    @SuppressWarnings("unchecked")
    public PartList<P> clone() {
      try {
        final PartList<P> clone = (PartList<P>)super.clone();
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
  protected ArrayList<PartList<? extends E>> partLists;
  protected HashMap<T,PartList<? extends E>> typeToPartList = new HashMap<>();

  public MergedList() {
    super(new ArrayList<E>());
    this.indexes = new ArrayIntList();
    this.partLists = new ArrayList<>();
  }

  @SafeVarargs
  public MergedList(final T ... types) {
    this();
    for (final T type : types)
      typeToPartList.put(type, null);
  }

  public MergedList(final Collection<T> types) {
    this();
    if (types != null)
      for (final T type : types)
        typeToPartList.put(type, null);
  }

  protected PartList<E> newPartList(final T type) {
    return new PartList<>(type);
  }

  protected abstract PartList<E> getPartList(final Class<? extends E> type);

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

  private int add(final Integer mergedIndex, final Integer partIndex, final E element, final PartList<E> partList) {
    if (mergedIndex == null) {
      partList.add(element);
    }
    else if (partIndex == null) {
      partList.indexes.add(mergedIndex);
      partList.addUnsafe(element);
    }
    else {
      partList.addUnsafe(partIndex, element);
      partList.indexes.add(partIndex, mergedIndex);
      for (int i = partIndex + 1; i < partList.indexes.size(); ++i)
        partList.indexes.set(i, partList.indexes.get(i) + 1);
    }

    return partIndex != null ? partIndex : this.size() - 1;
  }

  private void incSectionIndexes(final PartList<? extends E> partList, final int index) {
    final ArrayIntList mergedIndexes = partList.getIndexes();
    for (int j = 0; j < mergedIndexes.size(); j++) {
      final int mergedIndex = mergedIndexes.get(j);
      if (mergedIndex >= index)
        mergedIndexes.set(j, mergedIndex + 1);
    }
  }

  private void decSectionIndexes(final PartList<? extends E> partList, final int index) {
    final ArrayIntList mergedIndexes = partList.getIndexes();
    for (int j = 0; j < mergedIndexes.size(); j++) {
      final int mergedIndex = mergedIndexes.get(j);
      if (mergedIndex > index)
        mergedIndexes.set(j, mergedIndex - 1);
    }
  }

  private int findPartIndex(final PartList<? extends E> partList, final int index) {
    if (partList.size() == 0)
      return 0;

    final ArrayIntList mergedIndexes = partList.getIndexes();
    for (int i = partList.size() - 1; i >= 0; i--) {
      final int mergedIndex = mergedIndexes.get(i);
      if (mergedIndex < index)
        return index;
    }

    return 0;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void afterAdd(final int index, final E e, final RuntimeException exception) {
    final PartList<E> partList = getPartList((Class<E>)e.getClass());
    if (partList == null)
      throw new IllegalArgumentException("Object of type " + e.getClass() + " is not allowed to appear in " + MergedList.class.getName());

    partLists.add(index, partList);
    if (index == size() - 1) {
      indexes.add(partList.size());
      add(index, null, e, partList);
    }
    else {
      final int partIndex = index == 0 ? 0 : findPartIndex(partList, index);
      add(index, partIndex, e, partList);
      if (partIndex != -1) {
        indexes.add(index, partIndex);
        final IdentityHashSet<PartList<? extends E>> visited = new IdentityHashSet<>();
        for (int i = index + 1; i < partLists.size(); ++i) {
          final PartList<? extends E> nextPartList = partLists.get(i);
          if (nextPartList == partList)
            indexes.set(i, indexes.get(i) + 1);

          if (visited.contains(nextPartList) || nextPartList == partList)
            continue;

          visited.add(nextPartList);
          incSectionIndexes(nextPartList, index);
        }
      }
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  protected boolean beforeSet(final int index, final E newElement) {
    final PartList<? extends E> partList = partLists.get(index);
    final E element = get(index);
    final int partIndex = indexes.get(index);
    if (element.getClass() == newElement.getClass()) {
      partList.setUnsafe(partIndex, newElement);
    }
    else {
      final PartList<E> newPartList = getPartList((Class<E>)newElement.getClass());
      if (newPartList == null)
        throw new IllegalArgumentException("Object of type " + newElement.getClass() + " is not allowed to appear in " + MergedList.class.getName());

      partList.removeUnsafe(partIndex);
      for (int i = index + 1; i < partLists.size(); ++i) {
        final PartList<? extends E> nextPartList = partLists.get(i);
        if (nextPartList == partList)
          indexes.set(i, indexes.get(i) - 1);
      }

      add(index, 0, newElement, newPartList);
      partLists.set(index, newPartList);
    }

    return true;
  }

  @Override
  protected boolean beforeRemove(final int index) {
    final PartList<? extends E> partList = partLists.remove(index);
    partList.removeUnsafe(indexes.removeIndex(index));
    final ArrayIntList partIndexes = partList.getIndexes();
    for (int i = index; i < partIndexes.size(); ++i)
      partIndexes.set(i, partIndexes.get(i) - 1);

    final IdentityHashSet<PartList<? extends E>> visited = new IdentityHashSet<>();
    for (int i = index; i < partLists.size(); ++i) {
      final PartList<? extends E> nextPartList = partLists.get(i);
      if (nextPartList == partList)
        indexes.set(i, indexes.get(i) - 1);

      if (visited.contains(nextPartList) || nextPartList == partList)
        continue;

      visited.add(nextPartList);
      decSectionIndexes(nextPartList, index);
    }

    return true;
  }

  protected void print(final Logger logger) {
    final StringBuilder builder = new StringBuilder();
    builder.append("  I:");
    indexes.stream().forEach(i -> builder.append(' ').append(i));
    builder.append("\n  E:");
    forEach(e -> builder.append(' ').append(Objects.simpleIdentity(e)));
    builder.append("\n  A:");
    partLists.forEach(e -> builder.append(' ').append(Objects.simpleIdentity(e)));
    logger.info(builder.append('\n').toString());
    new IdentityHashSet<>(partLists).forEach(e -> e.print(logger));
  }

  protected E clone(final E item) {
    return item;
  }

  @Override
  @SuppressWarnings("unchecked")
  public MergedList<E,T> clone() {
    try {
      final MergedList<E,T> clone = (MergedList<E,T>)super.clone();
      clone.target = (ArrayList<E>)((ArrayList<E>)target).clone();
      clone.indexes = indexes.clone();
      clone.partLists = new ArrayList<>();
      for (final PartList<? extends E> partList : partLists)
        clone.partLists.add(partList.clone());

      final IdentityHashMap<E,E> clones = new IdentityHashMap<>();
      for (int i = 0; i < clone.target.size(); ++i) {
        final E item = (E)clone.target.get(i);
        final E copy = clone(item);
        clones.put(item, copy);
        clone.target.set(i, copy);
      }

      clone.typeToPartList = new HashMap<>();
      for (final PartList<? extends E> partList : clone.partLists) {
        clone.typeToPartList.put(partList.getType(), partList);
        for (int i = 0; i < partList.target.size(); ++i)
          partList.target.set(i, clones.get(partList.target.get(i)));
      }

      return clone;
    }
    catch (final CloneNotSupportedException e) {
      throw new UnsupportedOperationException(e);
    }
  }
}