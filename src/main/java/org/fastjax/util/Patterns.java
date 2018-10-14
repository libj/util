/* Copyright (c) 2016 FastJAX
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

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Utility functions for operations pertaining to {@link Pattern}.
 */
public final class Patterns {
  /**
   * Returns a string array of the group names of the specified {@code pattern}.
   * <p>
   * This implementation expects the group name encoding as:
   *
   * <pre>
   * {@code (?<name>regex)}
   * </pre>
   *
   * @param pattern The {@link Pattern}.
   * @return A string array of the group names of the specified {@code pattern}.
   */
  public static String[] getGroupNames(final Pattern pattern) {
    return pattern == null ? null : getGroupNames(pattern.toString(), 0, 0);
  }

  private static String[] getGroupNames(final String regex, final int index, final int depth) {
    final int start = regex.indexOf("(?<", index);
    if (start < 0)
      return depth == 0 ? null : new String[depth];

    final int end = regex.indexOf('>', start + 3);
    if (end < 0)
      throw new PatternSyntaxException("Malformed pattern", regex, start + 3);

    final String name = regex.substring(start + 3, end);
    final String[] names = getGroupNames(regex, end + 1, depth + 1);
    names[depth] = name;
    return names;
  }

  private Patterns() {
  }
}