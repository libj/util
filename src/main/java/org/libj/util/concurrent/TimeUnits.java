/* Copyright (c) 2024 LibJ
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

package org.libj.util.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * Utility functions for operations pertaining to the {@link java.time} package.
 */
public class TimeUnits {
  /**
   * Compares two pairs of time values represented by a {@code long} scalar at a {@link TimeUnit}. Returns a negative integer, zero,
   * or a positive integer as the first time value object is less than, equal to, or greater than the second time value.
   *
   * @param time0 The scalar of first time value.
   * @param unit0 The {@link TimeUnit} of the first time value.
   * @param time1 The scalar of second time value.
   * @param unit1 The {@link TimeUnit} of the second time value.
   * @return A negative integer, zero, or a positive integer as the first time value object is less than, equal to, or greater than
   *         the second time value.
   * @throws NullPointerException If {@code unit0} or {@code unit1} is null.
   */
  public static int compare(long time0, final TimeUnit unit0, long time1, final TimeUnit unit1) {
    final int sign;
    if (time0 < 0) {
      if (time1 >= 0)
        return -1;

      sign = -1;
    }
    else if (time0 > 0) {
      if (time1 <= 0)
        return 1;

      sign = 1;
    }
    else {
      return time1 == 0 ? 0 : time1 > 0 ? -1 : 1;
    }

    if (unit0 == unit1)
      return sign * Long.compare(time0, time1);

    if (unit0.compareTo(unit1) < 0)
      time0 /= unit0.convert(1, unit1);
    else
      time1 /= unit1.convert(1, unit0);

    return sign * Long.compare(time0, time1);
  }
}