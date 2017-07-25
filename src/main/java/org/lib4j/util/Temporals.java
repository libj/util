/* Copyright (c) 2017 lib4j
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Comparator;
import java.util.Date;

public final class Temporals {
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

  public static final int MICROS_IN_MILLI = 1000;
  public static final int NANOS_IN_MICROS = 1000;
  public static final int NANOS_IN_MILLI = NANOS_IN_MICROS * MICROS_IN_MILLI;
  public static final int NANOS_IN_SECOND = NANOS_IN_MILLI * Dates.MILLISECONDS_IN_SECOND;
  public static final long NANOS_IN_MINUTE = (long)NANOS_IN_MILLI * Dates.MILLISECONDS_IN_MINUTE;
  public static final long NANOS_IN_HOUR = (long)NANOS_IN_MILLI * Dates.MILLISECONDS_IN_HOUR;
  public static final long NANOS_IN_DAY = (long)NANOS_IN_MILLI * Dates.MILLISECONDS_IN_DAY;

  public static int compare(final Temporal o1, final Temporal o2) {
    return COMPARATOR.compare(o1, o2);
  }

  public static int compare(final LocalTime o1, final LocalTime o2) {
    return o1.compareTo(o2);
  }

  public static int compare(final LocalDate o1, final LocalDate o2) {
    return o1.compareTo(o2);
  }

  public static int compare(final LocalDateTime o1, final LocalDateTime o2) {
    return o1.compareTo(o2);
  }

  public static int compare(final LocalDate o1, final LocalDateTime o2) {
    return COMPARATOR.compare(o1, o2);
  }

  public static int compare(final LocalDateTime o1, final LocalDate o2) {
    return COMPARATOR.compare(o1, o2);
  }

  public static Date toDate(final LocalDateTime dateTime) {
    return Dates.newDate(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
  }

  public static long toEpochMilli(final LocalDateTime dateTime) {
    return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }

  public static LocalDateTime toLocalDateTime(final Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  public static LocalDateTime toLocalDateTime(final long time) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
  }

  public static LocalTime subtract(final LocalTime time1, final LocalTime time2) {
    return LocalTime.ofNanoOfDay(NANOS_IN_DAY - ChronoUnit.NANOS.between(time1, time2));
  }

  private Temporals() {
  }
}