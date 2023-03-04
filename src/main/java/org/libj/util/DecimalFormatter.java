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

import java.text.DecimalFormat;

/**
 * Utility functions for operations pertaining to {@link DecimalFormat}.
 */
public final class DecimalFormatter {
  /**
   * Returns a {@link ThreadLocal} of a {@link DecimalFormat} object using the given {@code pattern} and the symbols for the default
   * {@link java.util.Locale.Category#FORMAT FORMAT} locale.
   *
   * @param pattern A non-localized pattern string.
   * @return A {@link ThreadLocal} of a {@link DecimalFormat} object using the given {@code pattern}.
   * @throws NullPointerException If the given pattern is null or invalid.
   */
  public static ThreadLocal<DecimalFormat> createDecimalFormat(final String pattern) {
    final DecimalFormat format = new DecimalFormat(pattern);
    return ThreadLocal.withInitial(() -> format);
  }

  private DecimalFormatter() {
  }
}