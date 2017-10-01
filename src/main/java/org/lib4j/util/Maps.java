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

import java.util.Map;

public final class Maps {
  /**
   * Clone a <code>Map</code>, containing the same entries as the original. The
   * class type of the <code>map</code> argument must have a default
   * constructor.
   * @param map The <code>Map</code> to clone.
   * @return A clone of the map containing the same entries as the original.
   * @throws UnsupportedOperationException If the class of the <code>map</code>
   *         argument cannot be instantiated due to an
   *         <code>IllegalAccessException</code> or
   *         <code>InstantiationException</code>.
   * @see Map
   */
  @SuppressWarnings("unchecked")
  public static <M extends Map<K,V>,K,V>M clone(final Map<K,V> map) {
    try {
      final M clone = (M)map.getClass().newInstance();
      for (final Map.Entry<K,V> entry : map.entrySet())
        clone.put(entry.getKey(), entry.getValue());

      return clone;
    }
    catch (final IllegalAccessException | InstantiationException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  private Maps() {
  }
}