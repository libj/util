/* Copyright (c) 2012 LibJ
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

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.libj.lang.Numbers;
import org.libj.lang.ParseException;

/**
 * Utility functions for operations pertaining to {@link Date}.
 */
public final class Dates {
  public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");

  /** Minimum allowable value for {@link Date} */
  public static final Date MIN_VALUE = new Date(Long.MIN_VALUE);

  /** Maximum allowable value for {@link Date} */
  public static final Date MAX_VALUE = new Date(Long.MAX_VALUE);

  /** Number of days in a week. */
  public static final byte DAYS_IN_WEEK = 7;
  /** Number of hours in a day. */
  public static final byte HOURS_IN_DAY = 24;
  /** Number of minutes in an hour. */
  public static final byte MINUTES_IN_HOUR = 60;
  /** Number of seconds in a minute. */
  public static final byte SECONDS_IN_MINUTE = 60;
  /** Number of milliseconds in a second. */
  public static final short MILLISECONDS_IN_SECOND = 1000;

  /** Number of milliseconds in a minute. */
  public static final int MILLISECONDS_IN_MINUTE = MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE;

  /** Number of seconds in an hour. */
  public static final int SECONDS_IN_HOUR = SECONDS_IN_MINUTE * MINUTES_IN_HOUR;
  /** Number of milliseconds in an hour. */
  public static final int MILLISECONDS_IN_HOUR = MILLISECONDS_IN_SECOND * SECONDS_IN_HOUR;

  /** Number of minutes in a day. */
  public static final short MINUTES_IN_DAY = MINUTES_IN_HOUR * HOURS_IN_DAY;
  /** Number of seconds in a day. */
  public static final int SECONDS_IN_DAY = SECONDS_IN_MINUTE * MINUTES_IN_DAY;
  /** Number of milliseconds in a day. */
  public static final int MILLISECONDS_IN_DAY = MILLISECONDS_IN_SECOND * SECONDS_IN_DAY;

  /** Number of hours in a week. */
  public static final short HOURS_IN_WEEK = HOURS_IN_DAY * DAYS_IN_WEEK;
  /** Number of minutes in a week. */
  public static final short MINUTES_IN_WEEK = MINUTES_IN_HOUR * HOURS_IN_WEEK;
  /** Number of seconds in a week. */
  public static final int SECONDS_IN_WEEK = SECONDS_IN_MINUTE * MINUTES_IN_WEEK;
  /** Number of milliseconds in a week. */
  public static final int MILLISECONDS_IN_WEEK = MILLISECONDS_IN_SECOND * SECONDS_IN_WEEK;

  /**
   * Returns a name in the "short" style of the TimeZone specified by {@code id}. If the specified {@code date} is in Daylight
   * Saving Time in the TimeZone, a Daylight Saving Time name is returned (even if the TimeZone doesn't observe Daylight Saving
   * Time). Otherwise, a Standard Time name is returned.
   *
   * @param id The id of the TimeZone.
   * @param date The given date.
   * @return A name in the "short" style of the TimeZone specified by {@code id}.
   */
  public static String getTimeZoneShortName(final String id, final Date date) {
    final TimeZone timezone = TimeZone.getTimeZone(id);
    return timezone.getDisplayName(timezone.inDaylightTime(date), TimeZone.SHORT);
  }

  /**
   * Returns {@code date} with its time mutated by setting the specified temporal parameters.
   *
   * @param date The {@link Date} to mutate.
   * @param hours Hours value to set.
   * @return {@code date} with its time mutated by setting the specified temporal parameters.
   */
  public static Date setTimeInPlace(final Date date, final int hours) {
    return setTimeInPlace(date, hours, 0, 0, 0);
  }

  /**
   * Returns {@code date} with its time mutated by setting the specified temporal parameters.
   *
   * @param date The {@link Date} to mutate.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @return {@code date} with its time mutated by setting the specified temporal parameters.
   */
  public static Date setTimeInPlace(final Date date, final int hours, final int minutes) {
    return setTimeInPlace(date, hours, minutes, 0, 0);
  }

  /**
   * Returns {@code date} with its time mutated by setting the specified temporal parameters.
   *
   * @param date The {@link Date} to mutate.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @param seconds Seconds value to set.
   * @return {@code date} with its time mutated by setting the specified temporal parameters.
   */
  public static Date setTimeInPlace(final Date date, final int hours, final int minutes, final int seconds) {
    return setTimeInPlace(date, hours, minutes, seconds, 0);
  }

  /**
   * Returns {@code date} with its time mutated by setting the specified temporal parameters.
   *
   * @param date The {@link Date} to mutate.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @param seconds Seconds value to set.
   * @param milliseconds Milliseconds value to set.
   * @return {@code date} with its time mutated by setting the specified temporal parameters.
   */
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

  /**
   * Returns a new {@link Date} object with its time set by {@code date} and the specified temporal parameters.
   *
   * @param date The {@link Date} specifying the time.
   * @param hours Hours value to set.
   * @return A new {@link Date} object with its time set by {@code date} and the specified temporal parameters.
   */
  public static Date setTime(final Date date, final int hours) {
    return setTime(date, hours, 0, 0, 0);
  }

  /**
   * Returns a new {@link Date} object with its time set by {@code date} and the specified temporal parameters.
   *
   * @param date The {@link Date} specifying the time.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @return A new {@link Date} object with its time set by {@code date} and the specified temporal parameters.
   */
  public static Date setTime(final Date date, final int hours, final int minutes) {
    return setTime(date, hours, minutes, 0, 0);
  }

  /**
   * Returns a new {@link Date} object with its time set by {@code date} and the specified temporal parameters.
   *
   * @param date The {@link Date} specifying the time.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @param seconds Seconds value to set.
   * @return A new {@link Date} object with its time set by {@code date} and the specified temporal parameters.
   */
  public static Date setTime(final Date date, final int hours, final int minutes, final int seconds) {
    return setTime(date, hours, minutes, seconds, 0);
  }

  /**
   * Returns a new {@link Date} object with its time set by {@code date} and the specified temporal parameters.
   *
   * @param date The {@link Date} specifying the time.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @param seconds Seconds value to set.
   * @param milliseconds Milliseconds value to set.
   * @return A new {@link Date} object with its time set by {@code date} and the specified temporal parameters.
   */
  public static Date setTime(final Date date, final int hours, final int minutes, final int seconds, final int milliseconds) {
    final Date update = new Date(date.getTime());
    setTimeInPlace(update, hours, minutes, seconds, milliseconds);
    return update;
  }

  /**
   * Returns {@code date} with its time mutated by adding the specified temporal parameters to its current value.
   *
   * @param date The {@link Date} to mutate.
   * @param hours Hours value to set.
   * @return {@code date} with its time mutated by setting the specified temporal parameters to its current value.
   */
  public static Date addTimeInPlace(final Date date, final int hours) {
    return addTimeInPlace(date, hours, 0, 0, 0);
  }

  /**
   * Returns {@code date} with its time mutated by adding the specified temporal parameters to its current value.
   *
   * @param date The {@link Date} to mutate.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @return {@code date} with its time mutated by setting the specified temporal parameters to its current value.
   */
  public static Date addTimeInPlace(final Date date, final int hours, final int minutes) {
    return addTimeInPlace(date, hours, minutes, 0, 0);
  }

  /**
   * Returns {@code date} with its time mutated by adding the specified temporal parameters to its current value.
   *
   * @param date The {@link Date} to mutate.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @param seconds Seconds value to set.
   * @return {@code date} with its time mutated by setting the specified temporal parameters to its current value.
   */
  public static Date addTimeInPlace(final Date date, final int hours, final int minutes, final int seconds) {
    return addTimeInPlace(date, hours, minutes, seconds, 0);
  }

  /**
   * Returns {@code date} with its time mutated by adding the specified temporal parameters to its current value.
   *
   * @param date The {@link Date} to mutate.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @param seconds Seconds value to set.
   * @param milliseconds Milliseconds value to set.
   * @return {@code date} with its time mutated by setting the specified temporal parameters to its current value.
   */
  @SuppressWarnings("deprecation")
  public static Date addTimeInPlace(final Date date, final int hours, final int minutes, final int seconds, final int milliseconds) {
    final int timeZoneOffset = date.getTimezoneOffset();
    date.setTime(date.getTime() + hours * MILLISECONDS_IN_HOUR + seconds * MILLISECONDS_IN_SECOND + minutes * MILLISECONDS_IN_MINUTE + milliseconds);
    date.setTime(date.getTime() + (timeZoneOffset - date.getTimezoneOffset()) * MILLISECONDS_IN_MINUTE);
    return date;
  }

  /**
   * Returns a new {@link Date} object with its time set by {@code date}, and the specified temporal parameters added each current
   * value.
   *
   * @param date The {@link Date} specifying the time.
   * @param hours Hours value to set.
   * @return A new {@link Date} object with its time set by {@code date}, and the specified temporal parameters added each current
   *         value.
   */
  public static Date addTime(final Date date, final int hours) {
    return addTime(date, hours, 0, 0, 0);
  }

  /**
   * Returns a new {@link Date} object with its time set by {@code date}, and the specified temporal parameters added each current
   * value.
   *
   * @param date The {@link Date} specifying the time.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @return A new {@link Date} object with its time set by {@code date}, and the specified temporal parameters added each current
   *         value.
   */
  public static Date addTime(final Date date, final int hours, final int minutes) {
    return addTime(date, hours, minutes, 0, 0);
  }

  /**
   * Returns a new {@link Date} object with its time set by {@code date}, and the specified temporal parameters added each current
   * value.
   *
   * @param date The {@link Date} specifying the time.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @param seconds Seconds value to set.
   * @return A new {@link Date} object with its time set by {@code date}, and the specified temporal parameters added each current
   *         value.
   */
  public static Date addTime(final Date date, final int hours, final int minutes, final int seconds) {
    return addTime(date, hours, minutes, seconds, 0);
  }

  /**
   * Returns a new {@link Date} object with its time set by {@code date}, and the specified temporal parameters added each current
   * value.
   *
   * @param date The {@link Date} specifying the time.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @param seconds Seconds value to set.
   * @param milliseconds Milliseconds value to set.
   * @return A new {@link Date} object with its time set by {@code date}, and the specified temporal parameters added each current
   *         value.
   */
  public static Date addTime(final Date date, final int hours, final int minutes, final int seconds, final int milliseconds) {
    final Date update = new Date(date.getTime());
    addTimeInPlace(update, hours, minutes, seconds, milliseconds);
    return update;
  }

  /**
   * Returns the milliseconds part of the specified {@code dateTime}. The value returned will be between 0 and 999.
   *
   * @param dateTime The {@link Date}.
   * @return The milliseconds part of the specified {@code dateTime}.
   */
  public static short getMilliOfSecond(final Date dateTime) {
    return (short)(dateTime.getTime() % 1000);
  }

  /**
   * Returns a new {@link Date} object with its date part removed. The new {@link Date} will have the "sub 1 day" remainder of
   * {@code dateTime} as it's time value.
   *
   * @param dateTime The {@link Date}.
   * @return A new {@link Date} object with its date part removed.
   */
  public static Date dropDatePart(final Date dateTime) {
    return new Date(dropDatePart(dateTime.getTime()));
  }

  /**
   * Returns a {@code long} time value with its date part removed. The returned {@code long} will be the "sub 1 day" remainder of
   * {@code dateTime}.
   *
   * @param dateTime The {@code long} time value.
   * @return A {@code long} time value with its date part removed.
   */
  public static long dropDatePart(long dateTime) {
    if (dateTime < 0)
      dateTime = MILLISECONDS_IN_DAY + dateTime % MILLISECONDS_IN_DAY;

    return dateTime % MILLISECONDS_IN_DAY;
  }

  /**
   * Returns a new {@link Date} object with its time part removed. The new {@link Date} will have its "sub 1 day" time part set to
   * 0.
   *
   * @param dateTime The {@link Date}.
   * @return A new {@link Date} object with its time part removed.
   */
  public static Date dropTimePart(final Date dateTime) {
    return new Date(dropTimePart(dateTime.getTime()));
  }

  /**
   * Returns a {@code long} time value with its time part removed. The returned {@code long} will have its "sub 1 day" time part set
   * to 0.
   *
   * @param dateTime The {@code long} time value.
   * @return A {@code long} time value with its time part removed.
   */
  public static long dropTimePart(final long dateTime) {
    return dateTime - dropDatePart(dateTime);
  }

  /**
   * Returns a new {@link Date} object with its milliseconds part removed. The new {@link Date} will have its milliseconds set to 0.
   *
   * @param dateTime The {@link Date}.
   * @return A new {@link Date} object with its milliseconds part removed.
   */
  public static Date dropSeconds(final Date dateTime) {
    dateTime.setTime(dropSeconds(dateTime.getTime()));
    return dateTime;
  }

  /**
   * Returns a {@code long} time value with its seconds part removed. The returned {@code long} will have its seconds part set to 0.
   *
   * @param time The {@code long} time value.
   * @return A {@code long} time value with its seconds part removed.
   */
  public static long dropSeconds(final long time) {
    return MILLISECONDS_IN_MINUTE * (time / MILLISECONDS_IN_MINUTE);
  }

  /**
   * Returns a new {@link Date} object with its milliseconds part removed. The new {@link Date} will have its milliseconds set to 0.
   *
   * @param dateTime The {@link Date}.
   * @return A new {@link Date} object with its milliseconds part removed.
   */
  public static Date dropMilliseconds(final Date dateTime) {
    dateTime.setTime(dropMilliseconds(dateTime.getTime()));
    return dateTime;
  }

  /**
   * Returns a {@code long} time value with its milliseconds part removed. The returned {@code long} will have its milliseconds part
   * set to 0.
   *
   * @param time The {@code long} time value.
   * @return A {@code long} time value with its milliseconds part removed.
   */
  public static long dropMilliseconds(final long time) {
    return MILLISECONDS_IN_SECOND * (time / MILLISECONDS_IN_SECOND);
  }

  /**
   * Converts a <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO-8601</a> formatted date-time string to epoch millis.
   *
   * @param iso8601 The <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO-8601</a> formatted date-time string to convert.
   * @return The millis representation of the <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO-8601</a> formatted date-time
   *         string.
   * @throws NullPointerException If {@code iso8601} is null.
   * @throws ParseException If a parsing error has occurred.
   */
  public static long iso8601ToEpochMilli(final String iso8601) throws ParseException {
    final int len = iso8601.length();
    // The minimum length of a iso8601 dateTime is 14
    if (len < 14)
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", 0);

    int i = 0;
    final int year = Numbers.parseInt(iso8601, i, i += 4, -1);
    if (year == -1)
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", i - 4);

    if (iso8601.charAt(i) == '-')
      ++i;

    final int month = Numbers.parseInt(iso8601, i, i += 2, -1);
    if (month == -1)
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", i - 2);

    if (iso8601.charAt(i) == '-')
      ++i;

    final int date = Numbers.parseInt(iso8601, i, i += 2, -1);
    if (date == -1)
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", i - 2);

    if (iso8601.charAt(i) == 'T')
      ++i;

    if (len < i + 2)
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", i);

    final int hour = Numbers.parseInt(iso8601, i, i += 2, -1);
    if (hour == -1)
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", i - 2);

    if (iso8601.charAt(i) == ':')
      ++i;

    // Need to start checking the length again, because here it may be more than 14 (see prior comment above)
    if (len < i + 2)
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", i);

    final int minute = Numbers.parseInt(iso8601, i, i += 2, -1);
    if (minute == -1)
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", i - 2);

    if (iso8601.charAt(i) == ':')
      ++i;

    if (len < i + 2)
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", i);

    final int second = Numbers.parseInt(iso8601, i, i += 2, -1);
    if (second == -1) {
      Dates.epochMilliToIso8601(1644059691775L);
      i -= 2;
      Numbers.parseInt(iso8601, i, i += 2, -1);;
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", i - 2);
    }

    if (len <= i)
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", i);

    int millis = 0;
    char ch = iso8601.charAt(i);
    if (ch == '.') {
      int p = ++i;
      for (; p < len; ++p) { // [N]
        ch = iso8601.charAt(p);
        if (ch < '0' || '9' < ch)
          break;
      }

      final int precision = p - i;
      final int factor = precision == 1 ? 100 : precision == 2 ? 10 : 1;
      millis = Numbers.parseInt(iso8601, i, i + Math.min(precision, 3), -1);
      if (millis == -1)
        throw new ParseException("Unparseable date: \"" + iso8601 + "\"", i);

      millis *= factor;
      i = p;
    }

    final TimeZone timeZone;
    int offset = 0;
    if (i == len) {
      timeZone = TimeZone.getDefault();
    }
    else {
      timeZone = UTC_TIME_ZONE;
      if (ch != 'Z') {
        final int factor;
        if (ch == '+')
          factor = -1;
        else if (ch == '-')
          factor = 1;
        else
          throw new ParseException("Unparseable date: \"" + iso8601 + "\"", i);

        ++i;
        if (len < i + 2)
          throw new ParseException("Unparseable date: \"" + iso8601 + "\"", i);

        offset = Numbers.parseInt(iso8601, i, i += 2, -1);
        if (i == -1 || offset > 24)
          throw new ParseException("Unparseable date: \"" + iso8601 + "\"", i - 2);

        offset *= 60;

        if (i < len) {
          ch = iso8601.charAt(i);
          if (ch == ':')
            ++i;

          if (len < i + 2)
            throw new ParseException("Unparseable date: \"" + iso8601 + "\"", i);

          final int mins = Numbers.parseInt(iso8601, i, i += 2, -1);
          if (mins == -1 || mins > 60)
            throw new ParseException("Unparseable date: \"" + iso8601 + "\"", i - 2);

          if (i < len)
            throw new ParseException("Unparseable date: \"" + iso8601 + "\"", i);

          offset += mins;
        }

        offset *= factor;
      }
    }

    final Calendar calendar = Calendar.getInstance(timeZone);
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DATE, date);
    calendar.set(Calendar.HOUR_OF_DAY, hour);
    calendar.set(Calendar.MINUTE, minute);
    calendar.set(Calendar.SECOND, second);
    if (millis > 0)
      calendar.set(Calendar.MILLISECOND, millis);

    if (offset != 0)
      calendar.add(Calendar.MINUTE, offset);

    return calendar.getTimeInMillis();
  }

  /**
   * Converts the provided epoch millis to a <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO-8601</a> formatted date-time
   * string representation.
   *
   * @param epochMilli The epoch millis to convert to <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO-8601</a> formatted
   *          date-time string representation.
   * @return A <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO-8601</a> formatted date-time string representation of the
   *         provided epoch millis.
   */
  public static String epochMilliToIso8601(final long epochMilli) {
    final String iso8601 = SimpleDateFormats.ISO_8601.get().format(epochMilli);
    if ((epochMilli / 1000) * 1000 == epochMilli)
      return iso8601.substring(0, iso8601.length() - 4) + "Z";

    if ((epochMilli / 100) * 100 == epochMilli)
      return iso8601.substring(0, iso8601.length() - 2) + "Z";

    if ((epochMilli / 10) * 10 == epochMilli)
      return iso8601.substring(0, iso8601.length() - 1) + "Z";

    return iso8601 + "Z";
  }

  /**
   * Returns the current time in seconds as the difference, measured in seconds, between the current time and midnight, January 1,
   * 1970 UTC.
   *
   * @return The current time in seconds the difference, measured in seconds, between the current time and midnight, January 1, 1970
   *         UTC.
   * @see System#currentTimeMillis()
   */
  public static int currentTimeSecs() {
    return (int)(System.currentTimeMillis() / Dates.MILLISECONDS_IN_SECOND);
  }

  /**
   * Returns the current time in hours as the difference, measured in hours, between the current time and midnight, January 1, 1970
   * UTC.
   *
   * @return The current time in hours the difference, measured in hours, between the current time and midnight, January 1, 1970
   *         UTC.
   * @see System#currentTimeMillis()
   */
  public static int currentTimeHours() {
    return (int)(System.currentTimeMillis() / Dates.MILLISECONDS_IN_HOUR);
  }

  /**
   * Returns the current time in days as the difference, measured in days, between the current time and midnight, January 1, 1970
   * UTC.
   *
   * @return The current time in days the difference, measured in days, between the current time and midnight, January 1, 1970 UTC.
   * @see System#currentTimeMillis()
   */
  public static int currentTimeDays() {
    return (int)(System.currentTimeMillis() / Dates.MILLISECONDS_IN_DAY);
  }

  /**
   * Returns the milliseconds duration representation of the specified string duration.
   * <p>
   * The duration format is expressed with the regular expression:
   * <pre>
   * -?(\d+w)?(\d+d)?(\d+h)?(\d+m)?(\d*(.\d{1,3})s)?
   * </pre>
   * With the following key for units:
   * <p>
   * <table>
   * <caption>Unit Key</caption>
   * <tr><td><b>Unit</b></td><td><b>Description</b></td></tr>
   * <tr><td><code><b>w</b></code></td><td>Week</td></tr>
   * <tr><td><code><b>d</b></code></td><td>Day</td></tr>
   * <tr><td><code><b>h</b></code></td><td>Hour</td></tr>
   * <tr><td><code><b>m</b></code></td><td>Minute</td></tr>
   * <tr><td><code><b>s</b></code></td><td>Second</td></tr>
   * </table>
   *
   * @implSpec The second unit duration supports 3 fractional decimal digits.
   * @param str The duration string.
   * @return The milliseconds duration representation of the specified string duration.
   * @throws NullPointerException If {@code str} is null.
   * @throws IllegalArgumentException If {@code str} does not match the regular expression format.
   * @see #durationToString(long)
   */
  public static long stringToDuration(final String str) {
    long dur = 0;

    long v = 0;
    int d = 0;
    boolean dot = false, isNegative;

    char ch;
    int i = 0;
    if (isNegative = ((ch = str.charAt(0)) == '-')) {
      ch = str.charAt(++i);
    }

    for (final int end = str.length() - 1;; ch = str.charAt(++i)) { // [N]
      if (ch == 's') {
        int a;
        if (!dot || d == 0)
          a = 1000;
        else if (d == 1)
          a = 100;
        else if (d == 2)
          a = 10;
        else if (d == 3)
          a = 1;
        else
          throw new IllegalArgumentException("Fractional values are only supported for 3 digits of second precision: " + str);

        dur += v * a;
        v = 0;
        d = 0;
      }
      else if (ch == 'm') {
        dur += v * MILLISECONDS_IN_MINUTE;
        v = 0;
        d = 0;
        if (dot)
          throw new IllegalArgumentException("Fractional values are only supported for second precision: " + str);
      }
      else if (ch == 'h') {
        dur += v * MILLISECONDS_IN_HOUR;
        v = 0;
        d = 0;
        if (dot)
          throw new IllegalArgumentException("Fractional values are only supported for second precision: " + str);
      }
      else if (ch == 'd') {
        dur += v * MILLISECONDS_IN_DAY;
        v = 0;
        d = 0;
        if (dot)
          throw new IllegalArgumentException("Fractional values are only supported for second precision: " + str);
      }
      else if (ch == 'w') {
        dur += v * MILLISECONDS_IN_WEEK;
        v = 0;
        d = 0;
        if (dot)
          throw new IllegalArgumentException("Fractional values are only supported for second precision: " + str);
      }
      else if ('0' <= ch && ch <= '9') {
        v *= 10;
        v += ch - '0';
        ++d;
      }
      else if (ch == '.') {
        dot = true;
        d = 0;
      }
      else {
        throw new IllegalArgumentException("Unsupported character " + ch + " at position " + i + ": " + str);
      }

      if (i == end)
        break;
    }

    return isNegative ? -dur : dur;
  }

  /**
   * Returns the string duration representation of the specified duration in milliseconds.
   * <p>
   * The duration format is expressed with the regular expression:
   * <pre>
   * -?(\d+w)?(\d+d)?(\d+h)?(\d+m)?(\d*(.\d{1,3})s)?
   * </pre>
   * With the following key for units:
   * <p>
   * <table>
   * <caption>Unit Key</caption>
   * <tr><td><b>Unit</b></td><td><b>Description</b></td></tr>
   * <tr><td><code><b>w</b></code></td><td>Week</td></tr>
   * <tr><td><code><b>d</b></code></td><td>Day</td></tr>
   * <tr><td><code><b>h</b></code></td><td>Hour</td></tr>
   * <tr><td><code><b>m</b></code></td><td>Minute</td></tr>
   * <tr><td><code><b>s</b></code></td><td>Second</td></tr>
   * </table>
   *
   * @implSpec The second unit duration supports 3 fractional decimal digits.
   * @param duration The duration in milliseconds.
   * @return The string duration representation of the specified duration in milliseconds.
   * @see #stringToDuration(String)
   */
  public static String durationToString(long duration) {
    final StringBuilder s = new StringBuilder();
    if (duration < 0) {
      s.append('-');
      duration = -duration;
    }

    final long weeks = duration / MILLISECONDS_IN_WEEK;
    if (weeks != 0) {
      duration -= weeks * MILLISECONDS_IN_WEEK;
      s.append(weeks).append('w');
    }

    final long days = duration / MILLISECONDS_IN_DAY;
    if (days != 0) {
      duration -= days * MILLISECONDS_IN_DAY;
      s.append(days).append('d');
    }

    final long hours = duration / MILLISECONDS_IN_HOUR;
    if (hours != 0) {
      duration -= hours * MILLISECONDS_IN_HOUR;
      s.append(hours).append('h');
    }

    final long minutes = duration / MILLISECONDS_IN_MINUTE;
    if (minutes != 0) {
      duration -= minutes * MILLISECONDS_IN_MINUTE;
      s.append(minutes).append('m');
    }

    final long seconds = duration / MILLISECONDS_IN_SECOND;
    if (seconds != 0) {
      duration -= seconds * MILLISECONDS_IN_SECOND;
      s.append(seconds);
    }
    else if (duration != 0) {
      s.append('0');
    }
    else {
      return s.length() == 0 ? "0s" : s.toString();
    }

    if (duration > 0) {
      if (duration < 10)
        s.append(".00").append(duration);
      else if (duration < 100)
        s.append(".0").append(duration);
      else
        s.append('.').append(duration);
    }

    return s.append('s').toString();
  }

  private Dates() {
  }
}