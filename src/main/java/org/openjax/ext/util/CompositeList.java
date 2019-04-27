/* Copyright (c) 2017 OpenJAX
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

package org.openjax.ext.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;

import org.slf4j.Logger;

/**
 * The {@code CompositeList<E,T>} is comprised of {@code ComponentList<E>}s that
 * each maintain elements (of type {@code <E>}) conforming to a particular type
 * {@code <T>}. For instance, consider:
 * <p>
 * <blockquote>
 * {@code new CompositeList<Number,Class<? extends Number>>(Integer.class, Long.class, Double.class)}
 * </blockquote>
 * <p>
 * This {@code CompositeList} is comprised of {@code ComponentList<Number>}
 * lists that each maintains elements specific to the types:
 * {@code Integer.class}, {@code Long.class}, and {@code Double.class}. This
 * {@code CompositeList} can be used to store instances of {@code Integer},
 * {@code Long} or {@code Double}, which will internally be stored in the
 * appropriate {@code ComponentList<Number>}.
 * <p>
 * Each {@code ComponentList<Number>} of the {@code CompositeList} is created
 * with the abstract method {@link #getOrCreateComponentList(Object)} in the order in
 * which it was instantiated. For instance, consider the following procedure:
 * <p>
 * <blockquote><code>
 * compositeList.add(1);<br>
 * compositeList.add(5.3);<br>
 * compositeList.add(99999L);
 * </code></blockquote>
 * <p>
 * This procedure will result in a {@code CompositeList} that has 3
 * {@code ComponentList}s:
 * <p>
 * <blockquote><code>
 * ComponentList&lt;Integer&gt;<br>
 * ComponentList&lt;Double&gt;<br>
 * ComponentList&lt;Long&gt;
 * </code></blockquote>
 * <p>
 * Thereafter, all elements of type {@code Integer} will go into the
 * {@code ComponentList<Integer>}, all elements of type {@code Double} will go
 * into the {@code ComponentList<Double>}, and all elements of type {@code Long}
 * will go into the {@code ComponentList<Long>}.
 * <p>
 * The {@code CompositeList} guarantees the following:
 * <ul>
 * <li>The order of the {@code ComponentList}s inside the {@code CompositeList}
 * is maintained.</li>
 * <li>Elements added to the individual {@code ComponentList}s will be reflected
 * in the {@code CompositeList} in proper order (i.e. all {@code Double}s will
 * always come before all {@code Integer}s).</li>
 * <li>Elements added to the {@code CompositeList} will be reflected in the
 * appropriate {@code ComponentList}s.</li>
 * <li>All list mutation operations will uphold proper order for both the
 * {@code CompositeList} and the {@code ComponentList}s within (including those
 * via {@code Iterator} and {@code ListIterator}).</li>
 * </ul>
 *
 * @param <E> The type of elements in this list and internal
 *          {@code ComponentList}s.
 * @param <T> The type for separation of {@code ComponentList}s.
 */
@SuppressWarnings("static-method")
public abstract class CompositeList<E,T> extends ObservableList<E> implements Cloneable {
  /**
   * The component list of the {@code CompositeList} that stores instances
   * extending type {@code <E>}, which is separated from other such
   * {@code ComponentList}s by objects of type {@code <T>}.
   */
  public class ComponentList extends ObservableList<E> implements Cloneable {
    protected final T type;
    protected ArrayIntList indexes;

    /**
     * Creates a new {@code ComponentList} that maintains elements for the
     * specified type.
     *
     * @param type The type this {@code ComponentList} will maintain.
     */
    protected ComponentList(final T type) {
      super(new ArrayList<E>());
      this.type = type;
      this.indexes = new ArrayIntList();
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
    protected void afterAdd(final int index, final E e, final RuntimeException exception) {
      if (index == size() - 1) {
        final CompositeList<E,T> compositeList = CompositeList.this;
        compositeList.indexes.add(index);
        compositeList.componentLists.add(this);
        compositeList.addUnsafe(e);

        indexes.add(compositeList.size() - 1);
      }
      else {
        final int compositeIndex = index > 0 ? indexes.get(index - 1) + 1 : indexes.get(index);
        final ComponentList componentList = this;
        final CompositeList<E,T> compositeList = CompositeList.this;
        compositeList.indexes.add(compositeIndex, index);
        compositeList.componentLists.add(compositeIndex, componentList);
        compositeList.addUnsafe(compositeIndex, e);
        final IdentityHashSet<ComponentList> visited = new IdentityHashSet<>();
        for (int i = compositeIndex + 1; i < compositeList.size(); ++i) {
          final ComponentList nextComponentList = compositeList.componentLists.get(i);
          if (nextComponentList == componentList)
            compositeList.indexes.set(i, compositeList.indexes.get(i) + 1);

          if (visited.contains(nextComponentList) || nextComponentList == componentList)
            continue;

          visited.add(nextComponentList);
          incSectionIndexes(nextComponentList, compositeIndex);
        }

        indexes.add(index, compositeIndex);
        for (int i = index + 1; i < indexes.size(); ++i)
          indexes.set(i, indexes.get(i) + 1);
      }
    }

    @Override
    protected boolean beforeRemove(final int index) {
      final int compositeIndex = indexes.removeIndex(index);
      final CompositeList<E,T> compositeList = CompositeList.this;
      compositeList.removeUnsafe(compositeIndex);
      compositeList.indexes.removeIndex(compositeIndex);
      final ComponentList componentList = compositeList.componentLists.remove(compositeIndex);
      final IdentityHashSet<ComponentList> visited = new IdentityHashSet<>();
      for (int i = compositeIndex; i < compositeList.size(); ++i) {
        final ComponentList nextComponentList = compositeList.componentLists.get(i);
        if (nextComponentList == componentList)
          compositeList.indexes.set(i, compositeList.indexes.get(i) - 1);

        if (visited.contains(nextComponentList) || nextComponentList == componentList)
          continue;

        visited.add(nextComponentList);
        decSectionIndexes(nextComponentList, compositeIndex);
      }

      for (int i = index; i < indexes.size(); ++i)
        indexes.set(i, indexes.get(i) - 1);

      return true;
    }

    @Override
    protected boolean beforeSet(final int index, final E newElement) {
      final int compositeIndex = indexes.get(index);
      componentLists.set(compositeIndex, this);
      CompositeList.this.setUnsafe(compositeIndex, newElement);
      return true;
    }

    /**
     * @return The {@code CompositeList} of which this list is a component.
     */
    protected CompositeList<E,T> getCompositeList() {
      return CompositeList.this;
    }

    /**
     * Print method for debugging.
     *
     * @param logger The {@link Logger} to which debug information is to be be
     *          printed.
     * @throws NullPointerException If {@code logger} is null.
     */
    protected void print(final Logger logger) {
      final StringBuilder builder = new StringBuilder();
      builder.append("    ComponentList<").append(type).append("> ").append(Objects.simpleIdentity(this)).append('\n');
      builder.append("    I:");
      indexes.stream().forEach(i -> builder.append(' ').append(i));
      builder.append("\n    E:");
      forEach(e -> builder.append(' ').append(Objects.simpleIdentity(e)));
      logger.info(builder.append('\n').toString());
    }

    @Override
    @SuppressWarnings("unchecked")
    public ComponentList clone() {
      try {
        final ComponentList clone = (ComponentList)super.clone();
        clone.target = (ArrayList<E>)((ArrayList<E>)target).clone();
        clone.indexes = indexes.clone();
        return clone;
      }
      catch (final CloneNotSupportedException e) {
        throw new UnsupportedOperationException(e);
      }
    }
  }

  private ArrayIntList indexes;
  private ArrayList<ComponentList> componentLists;
  private HashMap<T,ComponentList> typeToComponentList = new HashMap<>();

  /**
   * Creates a new {@code CompositeList} with type values unspecified.
   */
  public CompositeList() {
    super(new ArrayList<E>());
    this.indexes = new ArrayIntList();
    this.componentLists = new ArrayList<>();
  }

  /**
   * Creates a new {@code CompositeList} with the specified types for which
   * {@code ComponentList}s will be created.
   *
   * @param types The types for which {@code ComponentList}s will be created.
   */
  @SafeVarargs
  public CompositeList(final T ... types) {
    this();
    if (types != null)
      for (final T type : types)
        typeToComponentList.put(type, null);
  }

  /**
   * Creates a new {@code CompositeList} with the specified types for which
   * {@code ComponentList}s will be created.
   *
   * @param types The types for which {@code ComponentList}s will be created.
   */
  public CompositeList(final Collection<T> types) {
    this();
    if (types != null)
      for (final T type : types)
        typeToComponentList.put(type, null);
  }

  /**
   * Returns {@code true} if this {@code CompositeList} contains the specified
   * type registered for {@code ComponentList}s, regardless of whether the
   * {@code ComponentList} has been instantiated or not.
   *
   * @param type The type.
   * @return {@code true} if this {@code CompositeList} contains the specified
   *         type registered for {@code ComponentList}s.
   */
  protected boolean containsComponentType(final T type) {
    return typeToComponentList.containsKey(type);
  }

  /**
   * Returns the {@code ComponentList} for the specified type.
   * <p>
   * <i><b>Note</b>: If the specified type is registered in the
   * {@code CompositeList} but a {@code ComponentList} has not yet been
   * instantiated for the type, this method will return null. Refer to
   * {@link #containsComponentType(Object)} to determine if type is
   * registered.</i>
   *
   * @param type The type.
   * @return The {@code ComponentList} for the specified type.
   */
  protected ComponentList getComponentList(final T type) {
    return typeToComponentList.get(type);
  }

  /**
   * Returns the {@code ComponentList} at the specified index of component
   * lists.
   *
   * @param index The index.
   * @return The {@code ComponentList} at the specified index of component
   *         lists.
   * @throws IndexOutOfBoundsException If the index is out of range.
   */
  protected ComponentList getComponentList(final int index) {
    return componentLists.get(index);
  }

  /**
   * Registers the specified {@code ComponentList} for the provided type.
   *
   * @param type The type for which the {@code ComponentList} is to be
   *          registered.
   * @param componentList The {@code ComponentList}.
   */
  protected void registerComponentList(final T type, final ComponentList componentList) {
    typeToComponentList.put(type, componentList);
  }

  /**
   * Creates a new {@code ComponentList} for the specified type. This method can
   * be overridden by subclasses.
   *
   * @param type The type for which {@code ComponentList}s is to be created.
   * @return The {@code ComponentList}.
   */
  protected ComponentList newComponentList(final T type) {
    return new ComponentList(type);
  }

  /**
   * Returns the {@code ComponentList} for the specified element, creating one
   * if the {@code ComponentList} has not yet been instantiated. This method
   * must be implemented by subclasses.
   *
   * @param element The element for which {@code ComponentList}s is to be
   *          retrieved.
   * @return The {@code ComponentList} for the specified type. This method must
   *         be implemented by subclasses.
   */
  protected abstract ComponentList getOrCreateComponentList(E element);

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

  private int add(final Integer compositeIndex, final Integer comonentIndex, final E element, final ComponentList componentList) {
    if (compositeIndex == null) {
      componentList.add(element);
    }
    else if (comonentIndex == null) {
      componentList.indexes.add(compositeIndex);
      componentList.addUnsafe(element);
    }
    else {
      componentList.addUnsafe(comonentIndex, element);
      componentList.indexes.add(comonentIndex, compositeIndex);
      for (int i = comonentIndex + 1; i < componentList.indexes.size(); ++i)
        componentList.indexes.set(i, componentList.indexes.get(i) + 1);
    }

    return comonentIndex != null ? comonentIndex : this.size() - 1;
  }

  private void incSectionIndexes(final ComponentList componentList, final int index) {
    final ArrayIntList compositeIndexes = componentList.getIndexes();
    for (int j = 0; j < compositeIndexes.size(); j++) {
      final int compositeIndex = compositeIndexes.get(j);
      if (compositeIndex >= index)
        compositeIndexes.set(j, compositeIndex + 1);
    }
  }

  private void decSectionIndexes(final ComponentList componentList, final int index) {
    final ArrayIntList compositeIndexes = componentList.getIndexes();
    for (int j = 0; j < compositeIndexes.size(); j++) {
      final int compositeIndex = compositeIndexes.get(j);
      if (compositeIndex > index)
        compositeIndexes.set(j, compositeIndex - 1);
    }
  }

  private int findComonentIndex(final ComponentList componentList, final int index) {
    if (componentList.size() == 0)
      return 0;

    final ArrayIntList compositeIndexes = componentList.getIndexes();
    for (int i = componentList.size() - 1; i >= 0; --i) {
      final int compositeIndex = compositeIndexes.get(i);
      if (compositeIndex < index)
        return index;
    }

    return 0;
  }

  @Override
  protected void afterAdd(final int index, final E e, final RuntimeException exception) {
    final ComponentList componentList = getOrCreateComponentList(e);
    if (componentList == null)
      throw new IllegalArgumentException("Object of type " + e.getClass() + " is not allowed to appear in " + CompositeList.class.getName());

    componentLists.add(index, componentList);
    if (index == size() - 1) {
      indexes.add(componentList.size());
      add(index, null, e, componentList);
    }
    else {
      final int comonentIndex = index == 0 ? 0 : findComonentIndex(componentList, index);
      add(index, comonentIndex, e, componentList);
      if (comonentIndex != -1) {
        indexes.add(index, comonentIndex);
        final IdentityHashSet<ComponentList> visited = new IdentityHashSet<>();
        for (int i = index + 1; i < componentLists.size(); ++i) {
          final ComponentList nextComponentList = componentLists.get(i);
          if (nextComponentList == componentList)
            indexes.set(i, indexes.get(i) + 1);

          if (visited.contains(nextComponentList) || nextComponentList == componentList)
            continue;

          visited.add(nextComponentList);
          incSectionIndexes(nextComponentList, index);
        }
      }
    }
  }

  @Override
  protected boolean beforeSet(final int index, final E newElement) {
    final ComponentList componentList = componentLists.get(index);
    final E element = get(index);
    final int comonentIndex = indexes.get(index);
    if (element.getClass() == newElement.getClass()) {
      componentList.setUnsafe(comonentIndex, newElement);
    }
    else {
      final ComponentList newComponentList = getOrCreateComponentList(newElement);
      if (newComponentList == null)
        throw new IllegalArgumentException("Object of type " + newElement.getClass() + " is not allowed to appear in " + CompositeList.class.getName());

      componentList.removeUnsafe(comonentIndex);
      for (int i = index + 1; i < componentLists.size(); ++i) {
        final ComponentList nextComponentList = componentLists.get(i);
        if (nextComponentList == componentList)
          indexes.set(i, indexes.get(i) - 1);
      }

      add(index, 0, newElement, newComponentList);
      componentLists.set(index, newComponentList);
    }

    return true;
  }

  @Override
  protected boolean beforeRemove(final int index) {
    final ComponentList componentList = componentLists.remove(index);
    componentList.removeUnsafe(indexes.removeIndex(index));
    final ArrayIntList comonentIndexes = componentList.getIndexes();
    for (int i = index; i < comonentIndexes.size(); ++i)
      comonentIndexes.set(i, comonentIndexes.get(i) - 1);

    final IdentityHashSet<ComponentList> visited = new IdentityHashSet<>();
    for (int i = index; i < componentLists.size(); ++i) {
      final ComponentList nextComponentList = componentLists.get(i);
      if (nextComponentList == componentList)
        indexes.set(i, indexes.get(i) - 1);

      if (visited.contains(nextComponentList) || nextComponentList == componentList)
        continue;

      visited.add(nextComponentList);
      decSectionIndexes(nextComponentList, index);
    }

    return true;
  }

  /**
   * Print method for debugging.
   *
   * @param logger The {@link Logger} to which debug information is to be be
   *          printed.
   * @throws NullPointerException If {@code logger} is null.
   */
  protected void print(final Logger logger) {
    final StringBuilder builder = new StringBuilder();
    builder.append("  I:");
    indexes.stream().forEach(i -> builder.append(' ').append(i));
    builder.append("\n  E:");
    forEach(e -> builder.append(' ').append(Objects.simpleIdentity(e)));
    builder.append("\n  A:");
    componentLists.forEach(e -> builder.append(' ').append(Objects.simpleIdentity(e)));
    logger.info(builder.append('\n').toString());
    new IdentityHashSet<>(componentLists).forEach(e -> e.print(logger));
  }

  /**
   * Returns a clone of the specified item. This method is necessary for
   * {@link #clone()} to be able to properly clone elements maintained by this
   * list. The default implementation is to return the specified instance
   * reference.
   *
   * @param item The item to be cloned.
   * @return A clone of the specified item.
   */
  protected E clone(final E item) {
    return item;
  }

  @Override
  @SuppressWarnings({"unchecked", "unlikely-arg-type"})
  public CompositeList<E,T> clone() {
    try {
      final CompositeList<E,T> clone = (CompositeList<E,T>)super.clone();
      clone.target = (ArrayList<E>)((ArrayList<E>)target).clone();
      clone.indexes = indexes.clone();
      clone.componentLists = new ArrayList<>();
      for (final ComponentList componentList : componentLists)
        clone.componentLists.add(componentList.clone());

      final IdentityHashMap<E,E> clones = new IdentityHashMap<>();
      for (int i = 0; i < clone.target.size(); ++i) {
        final E item = (E)clone.target.get(i);
        final E copy = clone(item);
        clones.put(item, copy);
        clone.target.set(i, copy);
      }

      clone.typeToComponentList = new HashMap<>();
      for (final ComponentList componentList : clone.componentLists) {
        clone.typeToComponentList.put(componentList.type, componentList);
        for (int i = 0; i < componentList.target.size(); ++i)
          componentList.target.set(i, clones.get(componentList.target.get(i)));
      }

      return clone;
    }
    catch (final CloneNotSupportedException e) {
      throw new UnsupportedOperationException(e);
    }
  }
}