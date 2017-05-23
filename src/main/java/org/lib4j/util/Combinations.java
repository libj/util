/* Copyright (c) 2014 lib4j
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

public final class Combinations {
  @SuppressWarnings("unchecked")
  public static <T>T[][] combine(final T[][] arrays) {
    if (arrays == null)
      return null;

    int total = arrays[0].length;
    for (int i = 1; i < arrays.length; i++)
      total *= arrays[i].length;

    final Class<?> componentType = arrays[0].getClass().getComponentType();
    final T[][] combinations = (T[][])Array.newInstance(componentType, total, 0);

    // Generate this combination
    for (; total > 0; total--) {
      final T[] currentSet = (T[])Array.newInstance(componentType, arrays.length);
      int position = total;

      // Pick the required element from each list, and add it to the set.
      for (int i = 0; i < arrays.length; i++) {
        final int length = arrays[i].length;
        currentSet[i] = arrays[i][position % length];
        position /= length;
      }

      combinations[total - 1] = currentSet;
    }

    return combinations;
  }

  private Combinations() {
  }
}