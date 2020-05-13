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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utility functions for operations pertaining to {@link Date}.
 */
public final class Dates {
  public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");

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

  private static final ThreadLocal<SimpleDateFormat> iso8601 = SimpleDateFormats.newSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

  private static final class IsoDate extends Date {
    private static final long serialVersionUID = -3516661689900839721L;

    private IsoDate(final long time) {
      super(time);
    }

    @Override
    public String toString() {
      return DateTimeFormatter.ISO_INSTANT.format(toInstant());
    }

    @Override
    public IsoDate clone() {
      return (IsoDate)super.clone();
    }
  }

  /**
   * Returns a new {@link Date} object representing the {@code time}.
   * <p>
   * The class returned by this method overrides {@link Date#toString()} to
   * format the date as per the ISO-8601 instant format in UTC, such as
   * '2011-12-03T10:15:30Z'.
   *
   * @param time The milliseconds since January 1, 1970, 00:00:00 GMT.
   * @return A new {@link Date} object representing the {@code time}.
   */
  public static Date newDate(final long time) {
    return new IsoDate(time);
  }

  /**
   * Returns a name in the "short" style of the TimeZone specified by
   * {@code id}. If the specified {@code date} is in Daylight Saving Time in the
   * TimeZone, a Daylight Saving Time name is returned (even if the TimeZone
   * doesn't observe Daylight Saving Time). Otherwise, a Standard Time name is
   * returned.
   *
   * @param id The id of the TimeZone.
   * @param date The given date.
   * @return A name in the "short" style of the TimeZone specified by
   *         {@code id}.
   */
  public static String getTimeZoneShortName(final String id, final Date date) {
    final TimeZone timezone = TimeZone.getTimeZone(id);
    return timezone.getDisplayName(timezone.inDaylightTime(date), TimeZone.SHORT);
  }

  /**
   * Returns {@code date} with its time mutated by setting the specified
   * temporal parameters.
   *
   * @param date The {@link Date} to mutate.
   * @param hours Hours value to set.
   * @return {@code date} with its time mutated by setting the specified
   *         temporal parameters.
   */
  public static Date setTimeInPlace(final Date date, final int hours) {
    return setTimeInPlace(date, hours, 0, 0, 0);
  }

  /**
   * Returns {@code date} with its time mutated by setting the specified
   * temporal parameters.
   *
   * @param date The {@link Date} to mutate.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @return {@code date} with its time mutated by setting the specified
   *         temporal parameters.
   */
  public static Date setTimeInPlace(final Date date, final int hours, final int minutes) {
    return setTimeInPlace(date, hours, minutes, 0, 0);
  }

  /**
   * Returns {@code date} with its time mutated by setting the specified
   * temporal parameters.
   *
   * @param date The {@link Date} to mutate.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @param seconds Seconds value to set.
   * @return {@code date} with its time mutated by setting the specified
   *         temporal parameters.
   */
  public static Date setTimeInPlace(final Date date, final int hours, final int minutes, final int seconds) {
    return setTimeInPlace(date, hours, minutes, seconds, 0);
  }

  /**
   * Returns {@code date} with its time mutated by setting the specified
   * temporal parameters.
   *
   * @param date The {@link Date} to mutate.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @param seconds Seconds value to set.
   * @param milliseconds Milliseconds value to set.
   * @return {@code date} with its time mutated by setting the specified
   *         temporal parameters.
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
   * Returns a new {@link Date} object with its time set by {@code date} and the
   * specified temporal parameters.
   *
   * @param date The {@link Date} specifying the time.
   * @param hours Hours value to set.
   * @return A new {@link Date} object with its time set by {@code date} and the
   *         specified temporal parameters.
   */
  public static Date setTime(final Date date, final int hours) {
    return setTime(date, hours, 0, 0, 0);
  }

  /**
   * Returns a new {@link Date} object with its time set by {@code date} and the
   * specified temporal parameters.
   *
   * @param date The {@link Date} specifying the time.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @return A new {@link Date} object with its time set by {@code date} and the
   *         specified temporal parameters.
   */
  public static Date setTime(final Date date, final int hours, final int minutes) {
    return setTime(date, hours, minutes, 0, 0);
  }

  /**
   * Returns a new {@link Date} object with its time set by {@code date} and the
   * specified temporal parameters.
   *
   * @param date The {@link Date} specifying the time.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @param seconds Seconds value to set.
   * @return A new {@link Date} object with its time set by {@code date} and the
   *         specified temporal parameters.
   */
  public static Date setTime(final Date date, final int hours, final int minutes, final int seconds) {
    return setTime(date, hours, minutes, seconds, 0);
  }

  /**
   * Returns a new {@link Date} object with its time set by {@code date} and the
   * specified temporal parameters.
   *
   * @param date The {@link Date} specifying the time.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @param seconds Seconds value to set.
   * @param milliseconds Milliseconds value to set.
   * @return A new {@link Date} object with its time set by {@code date} and the
   *         specified temporal parameters.
   */
  public static Date setTime(final Date date, final int hours, final int minutes, final int seconds, final int milliseconds) {
    final Date update = new Date(date.getTime());
    setTimeInPlace(update, hours, minutes, seconds, milliseconds);
    return update;
  }

  /**
   * Returns {@code date} with its time mutated by adding the specified temporal
   * parameters to its current value.
   *
   * @param date The {@link Date} to mutate.
   * @param hours Hours value to set.
   * @return {@code date} with its time mutated by setting the specified
   *         temporal parameters to its current value.
   */
  public static Date addTimeInPlace(final Date date, final int hours) {
    return addTimeInPlace(date, hours, 0, 0, 0);
  }

  /**
   * Returns {@code date} with its time mutated by adding the specified temporal
   * parameters to its current value.
   *
   * @param date The {@link Date} to mutate.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @return {@code date} with its time mutated by setting the specified
   *         temporal parameters to its current value.
   */
  public static Date addTimeInPlace(final Date date, final int hours, final int minutes) {
    return addTimeInPlace(date, hours, minutes, 0, 0);
  }

  /**
   * Returns {@code date} with its time mutated by adding the specified temporal
   * parameters to its current value.
   *
   * @param date The {@link Date} to mutate.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @param seconds Seconds value to set.
   * @return {@code date} with its time mutated by setting the specified
   *         temporal parameters to its current value.
   */
  public static Date addTimeInPlace(final Date date, final int hours, final int minutes, final int seconds) {
    return addTimeInPlace(date, hours, minutes, seconds, 0);
  }

  /**
   * Returns {@code date} with its time mutated by adding the specified temporal
   * parameters to its current value.
   *
   * @param date The {@link Date} to mutate.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @param seconds Seconds value to set.
   * @param milliseconds Milliseconds value to set.
   * @return {@code date} with its time mutated by setting the specified
   *         temporal parameters to its current value.
   */
  @SuppressWarnings("deprecation")
  public static Date addTimeInPlace(final Date date, final int hours, final int minutes, final int seconds, final int milliseconds) {
    final int timeZoneOffset = date.getTimezoneOffset();
    date.setTime(date.getTime() + hours * MILLISECONDS_IN_HOUR + seconds * MILLISECONDS_IN_SECOND + minutes * MILLISECONDS_IN_MINUTE + milliseconds);
    date.setTime(date.getTime() + (timeZoneOffset - date.getTimezoneOffset()) * MILLISECONDS_IN_MINUTE);
    return date;
  }

  /**
   * Returns a new {@link Date} object with its time set by {@code date}, and
   * the specified temporal parameters added each current value.
   *
   * @param date The {@link Date} specifying the time.
   * @param hours Hours value to set.
   * @return A new {@link Date} object with its time set by {@code date}, and
   *         the specified temporal parameters added each current value.
   */
  public static Date addTime(final Date date, final int hours) {
    return addTime(date, hours, 0, 0, 0);
  }

  /**
   * Returns a new {@link Date} object with its time set by {@code date}, and
   * the specified temporal parameters added each current value.
   *
   * @param date The {@link Date} specifying the time.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @return A new {@link Date} object with its time set by {@code date}, and
   *         the specified temporal parameters added each current value.
   */
  public static Date addTime(final Date date, final int hours, final int minutes) {
    return addTime(date, hours, minutes, 0, 0);
  }

  /**
   * Returns a new {@link Date} object with its time set by {@code date}, and
   * the specified temporal parameters added each current value.
   *
   * @param date The {@link Date} specifying the time.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @param seconds Seconds value to set.
   * @return A new {@link Date} object with its time set by {@code date}, and
   *         the specified temporal parameters added each current value.
   */
  public static Date addTime(final Date date, final int hours, final int minutes, final int seconds) {
    return addTime(date, hours, minutes, seconds, 0);
  }

  /**
   * Returns a new {@link Date} object with its time set by {@code date}, and
   * the specified temporal parameters added each current value.
   *
   * @param date The {@link Date} specifying the time.
   * @param hours Hours value to set.
   * @param minutes Minutes value to set.
   * @param seconds Seconds value to set.
   * @param milliseconds Milliseconds value to set.
   * @return A new {@link Date} object with its time set by {@code date}, and
   *         the specified temporal parameters added each current value.
   */
  public static Date addTime(final Date date, final int hours, final int minutes, final int seconds, final int milliseconds) {
    final Date update = new Date(date.getTime());
    addTimeInPlace(update, hours, minutes, seconds, milliseconds);
    return update;
  }

  /**
   * Returns the milliseconds part of the specified {@code dateTime}. The value
   * returned will be between 0 and 999.
   *
   * @param dateTime The {@link Date}.
   * @return The milliseconds part of the specified {@code dateTime}.
   */
  public static short getMilliOfSecond(final Date dateTime) {
    return (short)(dateTime.getTime() % 1000);
  }

  /**
   * Returns a new {@link Date} object with its date part removed. The new
   * {@link Date} will have the "sub 1 day" remainder of {@code dateTime} as
   * it's time value.
   *
   * @param dateTime The {@link Date}.
   * @return A new {@link Date} object with its date part removed.
   */
  public static Date dropDatePart(final Date dateTime) {
    return new Date(dropDatePart(dateTime.getTime()));
  }

  /**
   * Returns a {@code long} time value with its date part removed. The returned
   * {@code long} will be the "sub 1 day" remainder of {@code dateTime}.
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
   * Returns a new {@link Date} object with its time part removed. The new
   * {@link Date} will have its "sub 1 day" time part set to 0.
   *
   * @param dateTime The {@link Date}.
   * @return A new {@link Date} object with its time part removed.
   */
  public static Date dropTimePart(final Date dateTime) {
    return new Date(dropTimePart(dateTime.getTime()));
  }

  /**
   * Returns a {@code long} time value with its time part removed. The returned
   * {@code long} will have its "sub 1 day" time part set to 0.
   *
   * @param dateTime The {@code long} time value.
   * @return A {@code long} time value with its time part removed.
   */
  public static long dropTimePart(final long dateTime) {
    return dateTime - dropDatePart(dateTime);
  }

  /**
   * Returns a new {@link Date} object with its milliseconds part removed. The
   * new {@link Date} will have its milliseconds set to 0.
   *
   * @param dateTime The {@link Date}.
   * @return A new {@link Date} object with its milliseconds part removed.
   */
  public static Date dropSeconds(final Date dateTime) {
    dateTime.setTime(dropSeconds(dateTime.getTime()));
    return dateTime;
  }

  /**
   * Returns a {@code long} time value with its seconds part removed. The
   * returned {@code long} will have its seconds part set to 0.
   *
   * @param time The {@code long} time value.
   * @return A {@code long} time value with its seconds part removed.
   */
  public static long dropSeconds(final long time) {
    return MILLISECONDS_IN_MINUTE * (time / MILLISECONDS_IN_MINUTE);
  }

  /**
   * Returns a new {@link Date} object with its milliseconds part removed. The
   * new {@link Date} will have its milliseconds set to 0.
   *
   * @param dateTime The {@link Date}.
   * @return A new {@link Date} object with its milliseconds part removed.
   */
  public static Date dropMilliseconds(final Date dateTime) {
    dateTime.setTime(dropMilliseconds(dateTime.getTime()));
    return dateTime;
  }

  /**
   * Returns a {@code long} time value with its milliseconds part removed. The
   * returned {@code long} will have its milliseconds part set to 0.
   *
   * @param time The {@code long} time value.
   * @return A {@code long} time value with its milliseconds part removed.
   */
  public static long dropMilliseconds(final long time) {
    return MILLISECONDS_IN_SECOND * (time / MILLISECONDS_IN_SECOND);
  }

  /**
   * Converts a <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO-8601</a>
   * formatted date-time string to epoch millis.
   *
   * @param iso8601 The
   *          <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO-8601</a>
   *          formatted date-time string to convert.
   * @return The millis representation of the
   *         <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO-8601</a>
   *         formatted date-time string.
   * @throws ParseException If a parsing error has occurred.
   */
  public static long iso8601ToEpochMilli(final String iso8601) throws ParseException {
    final int year = Numbers.parseInt(iso8601, 0, 4, -1);
    if (year == -1)
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", 0);

    if (iso8601.charAt(4) != '-')
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", 4);

    final int month = Numbers.parseInt(iso8601, 5, 7, -1);
    if (month == -1)
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", 5);

    if (iso8601.charAt(7) != '-')
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", 7);

    final int date = Numbers.parseInt(iso8601, 8, 10, -1);
    if (date == -1)
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", 8);

    if (iso8601.charAt(10) != 'T')
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", 11);

    final int hour = Numbers.parseInt(iso8601, 11, 13, -1);
    if (hour == -1)
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", 11);

    if (iso8601.charAt(13) != ':')
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", 13);

    final int minute = Numbers.parseInt(iso8601, 14, 16, -1);
    if (minute == -1)
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", 14);

    if (iso8601.charAt(16) != ':')
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", 16);

    final int second = Numbers.parseInt(iso8601, 17, 19, -1);
    if (second == -1)
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", 17);

    final int z;
    final int millis;
    if (iso8601.charAt(19) == '.') {
      millis = Numbers.parseInt(iso8601, 20, Math.min(23, iso8601.length() - 1), -1);
      if (millis == -1)
        throw new ParseException("Unparseable date: \"" + iso8601 + "\"", 20);

      z = iso8601.length() - 1;
    }
    else {
      millis = 0;
      z = 19;
    }

    if (iso8601.charAt(z) != 'Z')
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", z);

    if (iso8601.length() > z + 1)
      throw new ParseException("Unparseable date: \"" + iso8601 + "\"", z);

    final Calendar calendar = Calendar.getInstance(UTC_TIME_ZONE);
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DATE, date);
    calendar.set(Calendar.HOUR_OF_DAY, hour);
    calendar.set(Calendar.MINUTE, minute);
    calendar.set(Calendar.SECOND, second);
    if (millis > 0)
      calendar.set(Calendar.MILLISECOND, millis);

    return calendar.getTimeInMillis();
  }

  /**
   * Converts the provided epoch millis to a
   * <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO-8601</a> formatted
   * date-time string representation.
   *
   * @param epochMilli The epoch millis to convert to
   *          <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO-8601</a>
   *          formatted date-time string representation.
   * @return A <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO-8601</a>
   *         formatted date-time string representation of the provided epoch
   *         millis.
   */
  public static String epochMilliToIso8601(final long epochMilli) {
    return Dates.iso8601.get().format(epochMilli);
  }

  private Dates() {
  }
}