/* Copyright (c) 2012 lib4j
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

package org.safris.commons.util;

import java.util.Date;
import java.util.TimeZone;

public final class DateUtil {
  public static final int NANOSECONDS_IN_MILLISECONDS = 1000000;

  public static final short MINUTES_IN_DAY = 24 * 60;
  public static final int MILLISECONDS_IN_MINUTE = 60 * 1000;
  public static final int MILLISECONDS_IN_HOUR = 60 * MILLISECONDS_IN_MINUTE;
  public static final int MILLISECONDS_IN_DAY = 24 * MILLISECONDS_IN_HOUR;

  public static String getTimeZoneShortName(final String id, final Date date) {
    final TimeZone timezone = TimeZone.getTimeZone(id);
    return timezone.getDisplayName(timezone.inDaylightTime(date), TimeZone.SHORT);
  }

  public static Date setTimeInPlace(final Date date, final int hours) {
    return setTimeInPlace(date, hours, 0, 0, 0);
  }

  public static Date setTimeInPlace(final Date date, final int hours, final int minutes) {
    return setTimeInPlace(date, hours, minutes, 0, 0);
  }

  public static Date setTimeInPlace(final Date date, final int hours, final int minutes, final int seconds) {
    return setTimeInPlace(date, hours, minutes, seconds, 0);
  }

  @SuppressWarnings("deprecation")
  public static Date setTimeInPlace(final Date date, final int hours, final int minutes, final int seconds, final int milliseconds) {
    final int timeZoneOffset = date.getTimezoneOffset();
    date.setTime((date.getTime() / 1000) * 1000 + milliseconds);
    date.setHours(hours);
    date.setMinutes(minutes);
    date.setSeconds(seconds);
    date.setTime(date.getTime() + (timeZoneOffset - date.getTimezoneOffset()) * MILLISECONDS_IN_MINUTE);
    return date;
  }

  public static Date setTime(final Date date, final int hours) {
    return setTime(date, hours, 0, 0, 0);
  }

  public static Date setTime(final Date date, final int hours, final int minutes) {
    return setTime(date, hours, minutes, 0, 0);
  }

  public static Date setTime(final Date date, final int hours, final int minutes, final int seconds) {
    return setTime(date, hours, minutes, seconds, 0);
  }

  public static Date setTime(final Date date, final int hours, final int minutes, final int seconds, final int milliseconds) {
    final Date update = new Date(date.getTime());
    setTimeInPlace(update, hours, minutes, seconds, milliseconds);
    return update;
  }

  public static Date addTimeInPlace(final Date date, final int hours) {
    return addTimeInPlace(date, hours, 0, 0, 0);
  }

  public static Date addTimeInPlace(final Date date, final int hours, final int minutes) {
    return addTimeInPlace(date, hours, minutes, 0, 0);
  }

  public static Date addTimeInPlace(final Date date, final int hours, final int minutes, final int seconds) {
    return addTimeInPlace(date, hours, minutes, seconds, 0);
  }

  @SuppressWarnings("deprecation")
  public static Date addTimeInPlace(final Date date, final int hours, final int minutes, final int seconds, final int milliseconds) {
    final int timeZoneOffset = date.getTimezoneOffset();
    date.setTime(date.getTime() + hours * MILLISECONDS_IN_HOUR + minutes * MILLISECONDS_IN_MINUTE + milliseconds);
    date.setTime(date.getTime() + (timeZoneOffset - date.getTimezoneOffset()) * MILLISECONDS_IN_MINUTE);
    return date;
  }

  public static Date addTime(final Date date, final int hours) {
    return addTime(date, hours, 0, 0, 0);
  }

  public static Date addTime(final Date date, final int hours, final int minutes) {
    return addTime(date, hours, minutes, 0, 0);
  }

  public static Date addTime(final Date date, final int hours, final int minutes, final int seconds) {
    return addTime(date, hours, minutes, seconds, 0);
  }

  public static Date addTime(final Date date, final int hours, final int minutes, final int seconds, final int milliseconds) {
    final Date update = new Date(date.getTime());
    addTimeInPlace(update, hours, minutes, seconds, milliseconds);
    return update;
  }

  @SuppressWarnings("deprecation")
  public static Date dropTimePart(final Date dateTime) {
    return dateTime != null ? new Date(dateTime.getYear(), dateTime.getMonth(), dateTime.getDate()) : null;
  }

  public static long dropMilliseconds(final long time) {
    return 1000 * (time / 1000);
  }

  public static Date dropMilliseconds(final Date dateTime) {
    dateTime.setTime(dropMilliseconds(dateTime.getTime()));
    return dateTime;
  }

  public static Date setTimeZone(final Date date, final TimeZone timeZone) {
    return setTimeZoneInPlace(new Date(date.getTime()), timeZone);
  }

  @SuppressWarnings("deprecation")
  public static Date setTimeZoneInPlace(final Date date, final TimeZone timeZone) {
    date.setTime(date.getTime() + (timeZone.getOffset(date.getTime()) - date.getTimezoneOffset()) * 60000);
    return date;
  }

  private DateUtil() {
  }
}