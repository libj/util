/* Copyright (c) 2018 FastJAX
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
 * Utility functions for operations pertaining to {@code char} and {@link Character}.
 */
public final class Characters {
  /**
   * Tests whether the specified {@code int} is an ANSI whitespace char, which
   * is one of:
   *
   * <pre>
   * {@code ' '}, {@code '\n'}, {@code '\r'}, or {@code '\t'}
   * </pre>
   *
   * @param ch The {@code int} to test.
   * @return {@code true} if the specified {@code int} is an ANSI whitespace
   *         char; otherwise {@code false}.
   */
  public static boolean isWhiteSpace(final int ch) {
    return ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t';
  }

  private Characters() {
  }
}