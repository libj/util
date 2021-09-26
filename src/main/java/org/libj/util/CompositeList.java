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

import static org.libj.lang.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;

import org.libj.lang.ObjectUtil;
import org.libj.util.primitive.ArrayIntList;
import org.slf4j.Logger;

/**
 * The {@code CompositeList<E,T>} is comprised of {@code ComponentList<E>}s that
 * each maintain elements (of type {@code <E>}) conforming to a particular type
 * {@code <T>}. For instance, consider:
 *
 * <pre>
 * {@code new CompositeList<Number,Class<? extends Number>>(Integer.class, Long.class, Double.class)}
 * </pre>
 *
 * This {@link CompositeList} is comprised of {@code ComponentList<Number>}
 * lists that each maintains elements specific to the types:
 * {@code Integer.class}, {@code Long.class}, and {@code Double.class}. This
 * {@link CompositeList} can be used to store instances of {@link Integer},
 * {@link Long} or {@link Double}, which will internally be stored in the
 * appropriate {@code ComponentList<Number>}.
 * <p>
 * Each {@code ComponentList<Number>} of the {@link CompositeList} is created
 * with the abstract method {@link #getOrCreateComponentList(Object)} in the
 * order in which it was instantiated. For instance, consider the following
 * procedure:<br>
 * <blockquote>{@code
 * compositeList.add(1);
 * compositeList.add(5.3);
 * compositeList.add(99999L);
 * }</blockquote><br>
 * This procedure will result in a {@link CompositeList} that has 3
 * {@link ComponentList}s:<br>
 * <blockquote>{@code
 * ComponentList<Integer>
 * ComponentList<Double>
 * ComponentList<Long>
 * }</blockquote><br>
 * Thereafter, all elements of type {@link Integer} will go into the
 * {@code ComponentList<Integer>}, all elements of type {@link Double} will go
 * into the {@code ComponentList<Double>}, and all elements of type {@link Long}
 * will go into the {@code ComponentList<Long>}.
 * <p>
 * The {@link CompositeList} guarantees the following:
 * <ul>
 * <li>The order of the {@link ComponentList}s inside the {@link CompositeList}
 * is maintained.</li>
 * <li>Elements added to the individual {@link ComponentList}s will be reflected
 * in the {@link CompositeList} in proper order (i.e. all {@link Double}s will
 * always come before all {@link Integer}s).</li>
 * <li>Elements added to the {@link CompositeList} will be reflected in the
 * appropriate {@link ComponentList}s.</li>
 * <li>All list mutation operations will uphold proper order for both the
 * {@link CompositeList} and the {@link ComponentList}s within (including those
 * via {@link java.util.Iterator} and {@link java.util.ListIterator}).</li>
 * </ul>
 *
 * @param <E> The type of elements in this list and internal
 *          {@link ComponentList}s.
 * @param <T> The type for separation of {@link ComponentList}s.
 */
@SuppressWarnings("static-method")
public abstract class CompositeList<E,T> extends ObservableList<E> implements Cloneable {
  /**
   * The component list of the {@link CompositeList} that stores instances
   * extending type {@code <E>}, which is separated from other such
   * {@link ComponentList}s by objects of type {@code <T>}.
   */
  public class ComponentList extends ObservableList<E> implements Cloneable {
    protected final T type;
    protected ArrayIntList indexes;

    /**
     * Creates a new {@link ComponentList} that maintains elements for the
     * specified type.
     *
     * @param type The type this {@link ComponentList} will maintain.
     */
    protected ComponentList(final T type) {
      super(new ArrayList<>());
      this.type = type;
      this.indexes = new ArrayIntList();
    }

    private ArrayIntList getIndexes() {
      return this.indexes;
    }

    private void addUnsafe(final E element) {
      super.target.add(element);
    }

    private void addUnsafe(final int index, final E element) {
      super.target.add(index, element);
    }

    @SuppressWarnings("unchecked")
    private E setUnsafe(final int index, final E element) {
      return (E)super.target.set(index, element);
    }

    @SuppressWarnings("unchecked")
    private E removeUnsafe(final int index) {
      this.indexes.removeIndex(index);
      return (E)super.target.remove(index);
    }

    @Override
    protected void afterAdd(final int index, final E element, final RuntimeException e) {
      if (index == size() - 1) {
        final CompositeList<E,T> compositeList = CompositeList.this;
        compositeList.indexes.add(index);
        compositeList.componentLists.add(this);
        compositeList.addUnsafe(element);

        indexes.add(compositeList.size() - 1);
      }
      else {
        final int compositeIndex = index > 0 ? indexes.get(index - 1) + 1 : indexes.get(index);
        final ComponentList componentList = this;
        final CompositeList<E,T> compositeList = CompositeList.this;
        compositeList.indexes.add(compositeIndex, index);
        compositeList.componentLists.add(compositeIndex, componentList);
        compositeList.addUnsafe(compositeIndex, element);
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
     * Returns the {@link CompositeList} of which this list is a component.
     *
     * @return The {@link CompositeList} of which this list is a component.
     */
    protected CompositeList<E,T> getCompositeList() {
      return CompositeList.this;
    }

    /**
     * Print method for debugging.
     *
     * @param logger The {@link Logger} to which debug information is to be be
     *          printed.
     * @throws IllegalArgumentException If {@code logger} is null.
     */
    protected void print(final Logger logger) {
      final StringBuilder builder = new StringBuilder();
      builder.append("    ComponentList<").append(type).append("> ").append(ObjectUtil.simpleIdentityString(this)).append('\n');
      builder.append("    I:");
      indexes.stream().forEach(i -> builder.append(' ').append(i));
      builder.append("\n    E:");
      forEach(e -> builder.append(' ').append(ObjectUtil.simpleIdentityString(e)));
      assertNotNull(logger).info(builder.append('\n').toString());
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
        throw new RuntimeException(e);
      }
    }
  }

  private ArrayIntList indexes;
  private ArrayList<ComponentList> componentLists;
  private HashMap<T,ComponentList> typeToComponentList = new HashMap<>();

  /**
   * Creates a new {@link CompositeList} with type values unspecified.
   */
  public CompositeList() {
    super(new ArrayList<>());
    this.indexes = new ArrayIntList();
    this.componentLists = new ArrayList<>();
  }

  /**
   * Creates a new {@link CompositeList} with the specified types for which
   * {@link ComponentList}s will be created.
   *
   * @param types The types for which {@link ComponentList}s will be created.
   */
  @SafeVarargs
  public CompositeList(final T ... types) {
    this();
    if (types != null)
      for (final T type : types)
        typeToComponentList.put(type, null);
  }

  /**
   * Creates a new {@link CompositeList} with the specified types for which
   * {@link ComponentList}s will be created.
   *
   * @param types The types for which {@link ComponentList}s will be created.
   */
  public CompositeList(final Collection<? extends T> types) {
    this();
    if (types != null)
      for (final T type : types)
        typeToComponentList.put(type, null);
  }

  /**
   * Returns {@code true} if this {@link CompositeList} contains the specified
   * type registered for {@link ComponentList}s, regardless of whether the
   * {@link ComponentList} has been instantiated or not.
   *
   * @param type The type.
   * @return {@code true} if this {@link CompositeList} contains the specified
   *         type registered for {@link ComponentList}s.
   */
  protected boolean containsComponentType(final T type) {
    return typeToComponentList.containsKey(type);
  }

  /**
   * Returns the {@link ComponentList} for the specified type.
   *
   * @implNote If the specified type is registered in the {@link CompositeList}
   *           but a {@link ComponentList} has not yet been instantiated for the
   *           type, this method will return {@code null}. Refer to
   *           {@link #containsComponentType(Object)} to determine if type is
   *           registered.
   * @param type The type.
   * @return The {@link ComponentList} for the specified type.
   */
  protected ComponentList getComponentList(final T type) {
    return typeToComponentList.get(type);
  }

  /**
   * Returns the {@link ComponentList} at the specified index of component
   * lists.
   *
   * @param index The index.
   * @return The {@link ComponentList} at the specified index of component
   *         lists.
   * @throws IndexOutOfBoundsException If the index is out of range.
   */
  protected ComponentList getComponentList(final int index) {
    return componentLists.get(index);
  }

  /**
   * Registers the specified {@link ComponentList} for the provided type.
   *
   * @param type The type for which the {@link ComponentList} is to be
   *          registered.
   * @param componentList The {@link ComponentList}.
   */
  protected void registerComponentList(final T type, final ComponentList componentList) {
    typeToComponentList.put(type, componentList);
  }

  /**
   * Creates a new {@link ComponentList} for the specified type. This method can
   * be overridden by subclasses.
   *
   * @param type The type for which {@link ComponentList}s is to be created.
   * @return The {@link ComponentList}.
   */
  protected ComponentList newComponentList(final T type) {
    return new ComponentList(type);
  }

  /**
   * Returns the {@link ComponentList} for the specified element, creating one
   * if the {@link ComponentList} has not yet been instantiated. This method
   * must be implemented by subclasses.
   *
   * @param element The element for which {@link ComponentList}s is to be
   *          retrieved.
   * @return The {@link ComponentList} for the specified type. This method must
   *         be implemented by subclasses.
   */
  protected abstract ComponentList getOrCreateComponentList(E element);

  private void addUnsafe(final E element) {
    super.target.add(element);
  }

  private void addUnsafe(final int index, final E element) {
    super.target.add(index, element);
  }

  @SuppressWarnings("unchecked")
  private E setUnsafe(final int index, final E element) {
    return (E)super.target.set(index, element);
  }

  @SuppressWarnings("unchecked")
  private E removeUnsafe(final int index) {
    return (E)super.target.remove(index);
  }

  private int add(final Integer compositeIndex, final Integer componentIndex, final E element, final ComponentList componentList) {
    if (compositeIndex == null) {
      componentList.add(element);
    }
    else if (componentIndex == null) {
      componentList.indexes.add(compositeIndex);
      componentList.addUnsafe(element);
    }
    else {
      componentList.addUnsafe(componentIndex, element);
      componentList.indexes.add(componentIndex, compositeIndex);
      for (int i = componentIndex + 1; i < componentList.indexes.size(); ++i)
        componentList.indexes.set(i, componentList.indexes.get(i) + 1);
    }

    return componentIndex != null ? componentIndex : this.size() - 1;
  }

  private void incSectionIndexes(final ComponentList componentList, final int index) {
    final ArrayIntList compositeIndexes = componentList.getIndexes();
    for (int j = 0, len = compositeIndexes.size(); j < len; j++) {
      final int compositeIndex = compositeIndexes.get(j);
      if (compositeIndex >= index)
        compositeIndexes.set(j, compositeIndex + 1);
    }
  }

  private void decSectionIndexes(final ComponentList componentList, final int index) {
    final ArrayIntList compositeIndexes = componentList.getIndexes();
    for (int j = 0, len = compositeIndexes.size(); j < len; j++) {
      final int compositeIndex = compositeIndexes.get(j);
      if (compositeIndex > index)
        compositeIndexes.set(j, compositeIndex - 1);
    }
  }

  private int findComponentIndex(final ComponentList componentList, final int index) {
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
  protected void afterAdd(final int index, final E element, final RuntimeException e) {
    final ComponentList componentList = assertNotNull(getOrCreateComponentList(element), "Object of type " + element.getClass() + " is not allowed to appear in " + CompositeList.class.getName());
    componentLists.add(index, componentList);
    if (index == size() - 1) {
      indexes.add(componentList.size());
      add(index, null, element, componentList);
    }
    else {
      final int componentIndex = index == 0 ? 0 : findComponentIndex(componentList, index);
      add(index, componentIndex, element, componentList);
      if (componentIndex > -1) {
        indexes.add(index, componentIndex);
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
    final int componentIndex = indexes.get(index);
    if (element.getClass() == newElement.getClass()) {
      componentList.setUnsafe(componentIndex, newElement);
    }
    else {
      final ComponentList newComponentList = assertNotNull(getOrCreateComponentList(newElement), "Object of type " + newElement.getClass() + " is not allowed to appear in " + CompositeList.class.getName());
      componentList.removeUnsafe(componentIndex);
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
    final ArrayIntList componentIndexes = componentList.getIndexes();
    for (int i = index; i < componentIndexes.size(); ++i)
      componentIndexes.set(i, componentIndexes.get(i) - 1);

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
   * @throws IllegalArgumentException If {@code logger} is null.
   */
  protected void print(final Logger logger) {
    final StringBuilder builder = new StringBuilder();
    builder.append("  I:");
    indexes.stream().forEach(i -> builder.append(' ').append(i));
    builder.append("\n  E:");
    forEach(e -> builder.append(' ').append(ObjectUtil.simpleIdentityString(e)));
    builder.append("\n  A:");
    componentLists.forEach(e -> builder.append(' ').append(ObjectUtil.simpleIdentityString(e)));
    assertNotNull(logger).info(builder.append('\n').toString());
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
      for (int i = 0, len = clone.target.size(); i < len; ++i) {
        final E item = (E)clone.target.get(i);
        final E copy = clone(item);
        clones.put(item, copy);
        clone.target.set(i, copy);
      }

      clone.typeToComponentList = new HashMap<>();
      for (final ComponentList componentList : clone.componentLists) {
        clone.typeToComponentList.put(componentList.type, componentList);
        for (int i = 0, len = componentList.target.size(); i < len; ++i)
          componentList.target.set(i, clones.get(componentList.target.get(i)));
      }

      return clone;
    }
    catch (final CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }
}