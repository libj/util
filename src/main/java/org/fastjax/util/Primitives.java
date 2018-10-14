/* Copyright (c) 2013 FastJAX
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

/**
 * Utility functions for operations pertaining to primitive types, and primitive
 * type wrapper objects.
 */
public final class Primitives {
  /**
   * Returns an object of <i>primitive type</i> specified by {@code to}, and
   * value specified by {@code from}.
   *
   * @param from The object specifying the value to cast. Supported types are:
   *          {@link Byte}, {@link Short}, {@link Integer}, {@link Long},
   *          {@link Float}, {@link Double}, {@link Character}, and
   *          {@link Boolean}.
   * @param to The target primitive type of the return object.
   * @return An object of <i>primitive type</i> specified by {@code to}, and
   *         value specified by {@code from}.
   */
  public static Object cast(final Object from, final Class<?> to) {
    if (from == null)
      return null;

    if (to.isInstance(from))
      return from;

    if (!to.isPrimitive())
      throw new IllegalArgumentException("cls is not of a primitive type");

    if (from instanceof Number) {
      if (Byte.class.isAssignableFrom(to))
        return Byte.valueOf(((Number)from).byteValue());

      if (Short.class.isAssignableFrom(to))
        return Short.valueOf(((Number)from).shortValue());

      if (Integer.class.isAssignableFrom(to))
        return Integer.valueOf(((Number)from).intValue());

      if (Long.class.isAssignableFrom(to))
        return Long.valueOf(((Number)from).longValue());

      if (Float.class.isAssignableFrom(to))
        return Float.valueOf(((Number)from).floatValue());

      if (Double.class.isAssignableFrom(to))
        return Double.valueOf(((Number)from).doubleValue());

      if (Boolean.class.isAssignableFrom(to))
        return Boolean.valueOf(((Number)from).intValue() != 0);

      if (Character.class.isAssignableFrom(to))
        return Character.valueOf((char)((Number)from).intValue());
    }
    else if (from instanceof Boolean) {
      if (Byte.class.isAssignableFrom(to))
        return Byte.valueOf((byte)((Boolean)from ? 1 : 0));

      if (Short.class.isAssignableFrom(to))
        return Short.valueOf((short)((Boolean)from ? 1 : 0));

      if (Integer.class.isAssignableFrom(to))
        return Integer.valueOf((Boolean)from ? 1 : 0);

      if (Long.class.isAssignableFrom(to))
        return Long.valueOf((Boolean)from ? 1 : 0);

      if (Float.class.isAssignableFrom(to))
        return Float.valueOf((Boolean)from ? 1 : 0);

      if (Double.class.isAssignableFrom(to))
        return Double.valueOf((Boolean)from ? 1 : 0);

      if (Character.class.isAssignableFrom(to))
        return Character.valueOf((char)((Boolean)from ? 1 : 0));
    }
    else if (from instanceof Character) {
      if (Byte.class.isAssignableFrom(to))
        return Byte.valueOf((byte)((Character)from).charValue());

      if (Short.class.isAssignableFrom(to))
        return Short.valueOf((short)((Character)from).charValue());

      if (Integer.class.isAssignableFrom(to))
        return Integer.valueOf(((Character)from).charValue());

      if (Long.class.isAssignableFrom(to))
        return Long.valueOf(((Character)from).charValue());

      if (Float.class.isAssignableFrom(to))
        return Float.valueOf(((Character)from).charValue());

      if (Double.class.isAssignableFrom(to))
        return Double.valueOf(((Character)from).charValue());

      if (Boolean.class.isAssignableFrom(to))
        return Boolean.valueOf(((Character)from).charValue() != 0);
    }

    throw new UnsupportedOperationException("Unsupported cast from " + from.getClass().getName() + " to " + to.getName());
  }

  private Primitives() {
  }
}