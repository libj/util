/* Copyright (c) 2014 LibJ
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utility functions for operations pertaining to {@link Object}.
 */
public final class ObjectUtil {
  /**
   * Returns the class name of object {@code obj}, concatenated with '@', and
   * the hexadecimal representation of its identity hash code.
   *
   * @param obj The object.
   * @return The class name of object {@code obj}, concatenated with '@', and
   *         the hexadecimal representation of its identity hash code.
   * @throws NullPointerException If {@code obj} is null.
   * @see System#identityHashCode(Object)
   */
  public static String identity(final Object obj) {
    return obj.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(obj));
  }

  /**
   * Returns the simple class name of object {@code obj}, concatenated with '@',
   * and the hexadecimal representation of its identity hash code.
   *
   * @param obj The object.
   * @return The simple class name of object {@code obj}, concatenated with '@',
   *         and the hexadecimal representation of its identity hash code.
   * @throws NullPointerException If {@code obj} is null.
   * @see System#identityHashCode(Object)
   */
  public static String simpleIdentity(final Object obj) {
    return obj.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(obj));
  }

  /**
   * Returns a clone of the specified object that implements the
   * {@link Cloneable} interface.
   *
   * @param <T> The type of the specified object.
   * @param obj The object to be cloned.
   * @return A clone of the specified object that implements the
   *         {@link Cloneable} interface.
   * @throws NullPointerException If the specified object is null.
   */
  @SuppressWarnings("unchecked")
  public static <T extends Cloneable>T clone(final T obj) {
    try {
      final Method cloneMethod = obj.getClass().getDeclaredMethod("clone");
      cloneMethod.setAccessible(true);
      final T clone = (T)cloneMethod.invoke(obj);
      cloneMethod.setAccessible(false);
      return clone;
    }
    catch (final IllegalAccessException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    catch (final InvocationTargetException e) {
      if (e.getCause() instanceof RuntimeException)
        throw (RuntimeException)e.getCause();

      throw new RuntimeException(e);
    }
  }

  private ObjectUtil() {
  }
}