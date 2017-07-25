/* Copyright (c) 2015 lib4j
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

package org.lib4j.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MaskedEnum {
  public static int toggle(int mask, final MaskedEnum ... enums) {
    for (final MaskedEnum e : enums)
      mask ^= 1 << e.ordinal;

    return mask;
  }

  public static int set(int mask, final MaskedEnum ... enums) {
    for (final MaskedEnum e : enums)
      mask |= 1 << e.ordinal;

    return mask;
  }

  public static int unset(int mask, final MaskedEnum ... enums) {
    for (final MaskedEnum e : enums)
      mask &= 1 << e.ordinal;

    return mask;
  }

  public static boolean check(final int mask, final int ordinal) {
    return (mask & (1 << ordinal)) != 0;
  }

  public static boolean check(final int mask, final MaskedEnum enums) {
    return check(mask, enums.ordinal);
  }

  @SuppressWarnings("unchecked")
  protected static <T extends MaskedEnum>T valueOf(final Class<T> callingClass, final int ordinal) {
    return ((Map<Integer,T>)enums.get(callingClass)).get(ordinal);
  }

  @SuppressWarnings("unchecked")
  protected static <T extends MaskedEnum>T[] toArray(final Class<T> callingClass, final int mask) {
    final Map<Integer,T> map = (Map<Integer,T>)enums.get(callingClass);
    final List<T> list = new ArrayList<T>();
    for (int i = 0; i < map.size(); i++)
      if ((mask & (1 << i)) != 0)
        list.add(map.get(i));

    return list.toArray((T[])Array.newInstance(callingClass, list.size()));
  }

  protected static final Map<Class<?>,Map<Integer,MaskedEnum>> enums = new HashMap<Class<?>,Map<Integer,MaskedEnum>>();

  public final int ordinal;

  protected MaskedEnum(final int ordinal) {
    this.ordinal = ordinal;
    Map<Integer,MaskedEnum> map = enums.get(getClass());
    if (map == null)
      enums.put(getClass(), map = new HashMap<Integer,MaskedEnum>());

    final MaskedEnum existing = map.get(ordinal);
    if (existing != null)
      throw new IllegalArgumentException("Ordinal [" + ordinal + "] already used by " + existing);

    map.put(ordinal, this);
  }

  public int toMask() {
    return set(0, this);
  }
}