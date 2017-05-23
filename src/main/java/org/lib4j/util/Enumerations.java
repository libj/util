/* Copyright (c) 2016 lib4j
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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public final class Enumerations {
  @SuppressWarnings("unchecked")
  private static <T>T[] recurse(final Class<T> componentType, final Enumeration<T> enumeration, final int depth) {
    if (enumeration.hasMoreElements()) {
      final T element = enumeration.nextElement();
      final T[] array = recurse(componentType, enumeration, depth + 1);
      array[depth] = element;
      return array;
    }

    return (T[])Array.newInstance(componentType, depth);
  }

  public static <T>T[] toArray(final Class<T> componentType, final Enumeration<T> enumeration) {
    return recurse(componentType, enumeration, 0);
  }

  public static <T>List<T> toList(final Class<T> componentType, final Enumeration<T> enumeration) {
    final T[] array = recurse(componentType, enumeration, 0);
    return Arrays.asList(array);
  }

  private Enumerations() {
  }
}