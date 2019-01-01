/* Copyright (c) 2017 OpenJAX
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

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Comparator;
import java.util.Date;

/**
 * Utility functions for operations pertaining to the {@code java.time} package.
 */
public final class Temporals {
  /**
   * A general comparator that compares two {@link Temporal} objects.
   */
  private static final Comparator<Temporal> COMPARATOR = new Comparator<Temporal>() {
    @Override
    public int compare(final Temporal o1, final Temporal o2) {
      if (o1 == null)
        return o2 == null ? 0 : 1;

      if (o2 == null)
        return -1;

      if (o1 instanceof LocalTime) {
        if (o2 instanceof LocalTime)
          return ((LocalTime)o1).compareTo((LocalTime)o2);

        throw new IllegalArgumentException(o1.getClass() + " cannot be compared to " + o2.getClass());
      }

      if (o1 instanceof LocalDate) {
        if (o2 instanceof LocalDate)
          return ((LocalDate)o1).compareTo((LocalDate)o2);

        if (o2 instanceof LocalDateTime)
          return ((LocalDate)o1).atStartOfDay().compareTo((LocalDateTime)o2);

        throw new UnsupportedOperationException("Unsupported Temporal type: " + o2.getClass().getName());
      }

      if (o1 instanceof LocalDateTime) {
        if (o2 instanceof LocalDateTime)
          return ((LocalDateTime)o1).compareTo((LocalDateTime)o2);

        if (o2 instanceof LocalDate)
          return ((LocalDateTime)o1).toLocalDate().compareTo((LocalDate)o2);

        throw new UnsupportedOperationException("Unsupported Temporal type: " + o2.getClass().getName());
      }

      throw new UnsupportedOperationException("Unsupported Temporal type: " + o1.getClass().getName());
    }
  };

  /** Number of microseconds in a millisecond. */
  public static final short MICROS_IN_MILLI = 1000;

  /** Number of nanoseconds in a microsecond. */
  public static final short NANOS_IN_MICROS = 1000;

  /** Number of nanoseconds in a millisecond. */
  public static final int NANOS_IN_MILLI = NANOS_IN_MICROS * MICROS_IN_MILLI;

  /** Number of nanoseconds in a second. */
  public static final int NANOS_IN_SECOND = NANOS_IN_MILLI * Dates.MILLISECONDS_IN_SECOND;

  /** Number of nanoseconds in a minute. */
  public static final long NANOS_IN_MINUTE = (long)NANOS_IN_MILLI * Dates.MILLISECONDS_IN_MINUTE;

  /** Number of nanoseconds in an hour. */
  public static final long NANOS_IN_HOUR = (long)NANOS_IN_MILLI * Dates.MILLISECONDS_IN_HOUR;

  /** Number of nanoseconds in a day. */
  public static final long NANOS_IN_DAY = (long)NANOS_IN_MILLI * Dates.MILLISECONDS_IN_DAY;

  /**
   * Compares the specified {@link Temporal} objects for order. Returns a
   * negative integer, zero, or a positive integer as the first argument is less
   * than, equal to, or greater than the second.
   * <p>
   * This method allows {@code null} values.
   *
   * @param o1 The first argument.
   * @param o2 The second argument.
   * @return A negative integer, zero, or a positive integer as the first
   *         argument is less than, equal to, or greater than the second.
   * @throws IllegalArgumentException If the specified objects cannot be
   *           compared.
   * @throws UnsupportedOperationException If one of the specified objects is an
   *           instance of an unsupported {@link Temporal} subclass.
   */
  public static int compare(final Temporal o1, final Temporal o2) {
    return COMPARATOR.compare(o1, o2);
  }

  /**
   * Compares the specified {@link LocalTime} objects for order. Returns a
   * negative integer, zero, or a positive integer as the first argument is less
   * than, equal to, or greater than the second.
   * <p>
   * This method supports {@code null} values.
   *
   * @param o1 The first argument.
   * @param o2 The second argument.
   * @return A negative integer, zero, or a positive integer as the first
   *         argument is less than, equal to, or greater than the second.
   */
  public static int compare(final LocalTime o1, final LocalTime o2) {
    return o1 == null ? o2 == null ? 0 : 1 : o1.compareTo(o2);
  }

  /**
   * Compares the specified {@link LocalDate} objects for order. Returns a
   * negative integer, zero, or a positive integer as the first argument is less
   * than, equal to, or greater than the second.
   * <p>
   * This method supports {@code null} values.
   *
   * @param o1 The first argument.
   * @param o2 The second argument.
   * @return A negative integer, zero, or a positive integer as the first
   *         argument is less than, equal to, or greater than the second.
   */
  public static int compare(final LocalDate o1, final LocalDate o2) {
    return o1 == null ? o2 == null ? 0 : 1 : o1.compareTo(o2);
  }

  /**
   * Compares the specified {@link LocalDateTime} objects for order. Returns a
   * negative integer, zero, or a positive integer as the first argument is less
   * than, equal to, or greater than the second.
   * <p>
   * This method supports {@code null} values.
   *
   * @param o1 The first argument.
   * @param o2 The second argument.
   * @return A negative integer, zero, or a positive integer as the first
   *         argument is less than, equal to, or greater than the second.
   */
  public static int compare(final LocalDateTime o1, final LocalDateTime o2) {
    return o1 == null ? o2 == null ? 0 : 1 : o1.compareTo(o2);
  }

  /**
   * Compares the specified {@link LocalDate} and {@link LocalDateTime} objects
   * for order. Returns a negative integer, zero, or a positive integer as the
   * first argument is less than, equal to, or greater than the second.
   * <p>
   * This method supports {@code null} values.
   *
   * @param o1 The first argument.
   * @param o2 The second argument.
   * @return A negative integer, zero, or a positive integer as the first
   *         argument is less than, equal to, or greater than the second.
   */
  public static int compare(final LocalDate o1, final LocalDateTime o2) {
    return COMPARATOR.compare(o1, o2);
  }

  /**
   * Compares the specified {@link LocalDateTime} and {@link LocalDate} objects
   * for order. Returns a negative integer, zero, or a positive integer as the
   * first argument is less than, equal to, or greater than the second.
   * <p>
   * This method supports {@code null} values.
   *
   * @param o1 The first argument.
   * @param o2 The second argument.
   * @return A negative integer, zero, or a positive integer as the first
   *         argument is less than, equal to, or greater than the second.
   */
  public static int compare(final LocalDateTime o1, final LocalDate o2) {
    return COMPARATOR.compare(o1, o2);
  }

  /**
   * Returns a new {@link Date} object representing the instant time of the
   * specified {@link LocalDateTime} object in the default time-zone.
   *
   * @param dateTime The {@link LocalDateTime} object.
   * @return A new {@link Date} object representing the instant time of the
   *         specified {@link LocalDateTime} object in the default time-zone.
   * @throws NullPointerException If {@code dataTime} is null.
   */
  public static Date toDate(final LocalDateTime dateTime) {
    return Dates.newDate(toEpochMilli(dateTime));
  }

  /**
   * Returns the number of milliseconds since the epoch of the instant value of
   * the specified {@link LocalDateTime} object in the default time-zone.
   *
   * @param dateTime The {@link LocalDateTime} object.
   * @return The number of milliseconds since the epoch of the instant value of
   *         the specified {@link LocalDateTime} object in the default
   *         time-zone.
   * @throws NullPointerException If {@code dataTime} is null.
   */
  public static long toEpochMilli(final LocalDateTime dateTime) {
    return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }

  /**
   * Returns a new {@link LocalDateTime} representing the instant value of the
   * specified {@link Date} object in the default time-zone.
   *
   * @param date The {@link Date} object.
   * @return A new {@link LocalDateTime} representing the instant value of the
   *         specified {@link Date} object in the default time-zone.
   * @throws NullPointerException If {@code date} is null.
   */
  public static LocalDateTime toLocalDateTime(final Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  /**
   * Returns a new {@link LocalDateTime} of the instant specified by the
   * {@code long} value representing milliseconds from the epoch, in the default
   * time-zone.
   *
   * @param time Value representing milliseconds from the epoch.
   * @return A new {@link LocalDateTime} of the instant specified by the
   *         {@code long} value representing milliseconds from the epoch, in the
   *         default time-zone.
   * @throws DateTimeException If {@code time} exceeds the maximum or minimum
   *           values.
   */
  public static LocalDateTime toLocalDateTime(final long time) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
  }

  /**
   * Returns a new {@link LocalTime} instance representing the value of the
   * first argument {@link LocalTime} minus the second.
   * <p>
   * The calculation is performed with nanosecond precision.
   *
   * @param t1 The first argument.
   * @param t2 The second argument.
   * @return A new {@link LocalTime} instance representing the value of the
   *         first argument {@link LocalTime} minus the second.
   * @throws NullPointerException If {@code t1} or {@code t2} is null.
   */
  public static LocalTime subtract(final LocalTime t1, final LocalTime t2) {
    return LocalTime.ofNanoOfDay(NANOS_IN_DAY - ChronoUnit.NANOS.between(t1, t2));
  }

  private Temporals() {
  }
}