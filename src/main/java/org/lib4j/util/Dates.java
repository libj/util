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

package org.lib4j.util;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public final class Dates {
  public static final byte DAYS_IN_WEEK = 7;
  public static final byte HOURS_IN_DAY = 24;
  public static final byte MINUTES_IN_HOUR = 60;
  public static final byte SECONDS_IN_MINUTE = 60;
  public static final short MILLISECONDS_IN_SECOND = 1000;

  public static final short HOURS_IN_WEEK = HOURS_IN_DAY * DAYS_IN_WEEK;
  public static final short MINUTES_IN_WEEK = MINUTES_IN_HOUR * HOURS_IN_WEEK;
  public static final int SECONDS_IN_WEEK = SECONDS_IN_MINUTE * MINUTES_IN_WEEK;
  public static final int MILLISECONDS_IN_WEEK = MILLISECONDS_IN_SECOND * SECONDS_IN_WEEK;

  public static final short MINUTES_IN_DAY = MINUTES_IN_HOUR * HOURS_IN_DAY;
  public static final int SECONDS_IN_DAY = SECONDS_IN_MINUTE * MINUTES_IN_DAY;
  public static final int MILLISECONDS_IN_DAY = MILLISECONDS_IN_SECOND * SECONDS_IN_DAY;

  public static final int SECONDS_IN_HOUR = SECONDS_IN_MINUTE * MINUTES_IN_HOUR;
  public static final int MILLISECONDS_IN_HOUR = MILLISECONDS_IN_SECOND * SECONDS_IN_HOUR;

  public static final int MILLISECONDS_IN_MINUTE = MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE;

  private static class Date2 extends Date {
    private static final long serialVersionUID = -3516661689900839721L;
    protected Date2(final long time) {
      super(time);
    }

    @Override
    public String toString() {
      return DateTimeFormatter.ISO_INSTANT.format(toInstant());
    }
  }

  public static Date newDate(final long time) {
    return new Date2(time);
  }

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
    date.setTime((date.getTime() / MILLISECONDS_IN_SECOND) * MILLISECONDS_IN_SECOND + milliseconds);
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

  public static short getMilliOfSecond(final Date dateTime) {
    return (short)(dateTime.getTime() % 1000);
  }

  public static Date dropDatePart(final Date dateTime) {
    return new Date(dropDatePart(dateTime.getTime()));
  }

  public static long dropDatePart(long dateTime) {
    if (dateTime < 0)
      dateTime = MILLISECONDS_IN_DAY + dateTime % MILLISECONDS_IN_DAY;

    return dateTime % MILLISECONDS_IN_DAY;
  }

  public static Date dropTimePart(final Date dateTime) {
    return new Date(dropTimePart(dateTime.getTime()));
  }

  public static long dropTimePart(final long dateTime) {
    return dateTime - dropDatePart(dateTime);
  }

  public static long dropMilliseconds(final long time) {
    return MILLISECONDS_IN_SECOND * (time / MILLISECONDS_IN_SECOND);
  }

  public static Date dropMilliseconds(final Date dateTime) {
    dateTime.setTime(dropMilliseconds(dateTime.getTime()));
    return dateTime;
  }

  public static String getTimeZoneISO8601(final TimeZone timeZone) {
    final int absOffset = Math.abs(timeZone.getRawOffset());
    final int hours = absOffset / MILLISECONDS_IN_HOUR;
    final int minutes = absOffset / MILLISECONDS_IN_MINUTE % MINUTES_IN_HOUR;
    final String sign = timeZone.getRawOffset() >= 0 ? "+" : "-";
    return sign + (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes);
  }

  private Dates() {
  }
}