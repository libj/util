/* Copyright (c) 2012 OpenJAX
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

package org.openjax.classic.util;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Utility functions for operations pertaining to {@link Calendar}.
 */
public final class CalendarUtil {
  /**
   * Creates a new {@link Calendar} at the specified {@link TimeZone}, with its
   * current time set to {@code time}.
   *
   * @param time The milliseconds value to which the time of the returned
   *          {@link Calendar} should be set.
   * @param timeZone The {@link TimeZone} to be used by the returned
   *          {@link Calendar}.
   * @return A new {@link Calendar} at the specified {@link TimeZone}, with its
   *         current time set to {@code time}.
   */
  public static Calendar newCalendar(final long time, final TimeZone timeZone) {
    if (timeZone == null)
      throw new IllegalArgumentException("timeZone == null");

    final Calendar calendar = Calendar.getInstance(timeZone);
    calendar.setTimeInMillis(time);
    return calendar;
  }

  /**
   * Creates a new {@link Calendar} at the default time zone, with its current
   * time set to {@code time}.
   *
   * @param time The milliseconds value to which the time of the returned
   *          {@link Calendar} should be set.
   * @return A new {@link Calendar} at the default time zone, with its current
   *         time set to {@code time}.
   */
  public static Calendar newCalendar(final long time) {
    final Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(time);
    return calendar;
  }

  private CalendarUtil() {
  }
}