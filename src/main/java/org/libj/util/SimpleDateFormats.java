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

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public final class SimpleDateFormats {
  private static final String[] timeZoneIDs = TimeZone.getAvailableIDs();

  public static TimeZone fetchTimeZone(String offset) {
    if (offset.charAt(0) != '+' && offset.charAt(0) != '-')
      return TimeZone.getTimeZone(offset);

    if (offset.length() != 5)
      return null;

    final int offsetHours = Integer.parseInt(offset.substring(0, 3));
    final int offsetMinutes = Integer.parseInt(offset.substring(3));

    for (final String timeZoneID : timeZoneIDs) { // [A]
      final TimeZone timeZone = TimeZone.getTimeZone(timeZoneID);
      final long hours = TimeUnit.MILLISECONDS.toHours(timeZone.getRawOffset());
      final long minutes = Math.abs(TimeUnit.MILLISECONDS.toMinutes(timeZone.getRawOffset()) % 60);
      if (hours == offsetHours && minutes == offsetMinutes)
        return timeZone;
    }

    return null;
  }

  private static class IsoDateFormatLocal extends ThreadLocal<SimpleDateFormat> {
    private class IsoDateFormats extends SimpleDateFormat {
      private final IsoDateFormat[] formats;

      IsoDateFormats(final Locale locale, final String ... patterns) {
        this.formats = new IsoDateFormat[patterns.length];
        for (int i = 0, i$ = patterns.length; i < i$; ++i) // [A]
          this.formats[i] = locale != null ? new IsoDateFormat(patterns[i], locale, i) : new IsoDateFormat(patterns[i], i);
      }

      @Override
      public Date parse(final String text, final ParsePosition pos) {
        final String tzPart = text.substring(text.lastIndexOf(' ') + 1);
        final int i = tzPart.charAt(0) != '+' && tzPart.charAt(0) != '-' ? 1 : 0;
        // FIXME: Need to finish this...
        // for (int i = 0, i$ = formats.length; i < i$; ++i) { // [A]
        final IsoDateFormat format = formats[i];

        format.setTimeZone(fetchTimeZone(tzPart));
        final Date date = format.parse(text, pos);
        if (date != null)
          return date;
        // }

        return null;
      }

      @Override
      public StringBuffer format(final Date date, final StringBuffer toAppendTo, final FieldPosition pos) {
        final SimpleDateFormat format;
        if (date instanceof IsoDate) {
          format = ((IsoDate)date).getFormat();
        }
        else {
          format = formats[formats.length - 1];
          format.setTimeZone(Dates.UTC_TIME_ZONE);
        }

        return format.format(date, toAppendTo, pos);
      }
    }

    private class IsoDateFormat extends SimpleDateFormat {
      private final int index;

      IsoDateFormat(final String pattern, final Locale locale, final int index) {
        super(pattern, locale);
        this.index = index;
      }

      IsoDateFormat(final String pattern, final int index) {
        super(pattern);
        this.index = index;
      }

      @Override
      public IsoDate parse(final String text, final ParsePosition pos) {
        final Date date = super.parse(text, pos);
        return date == null ? null : new IsoDate(date.getTime(), getTimeZone(), IsoDateFormatLocal.this, index);
      }
    }

    private final String[] patterns;
    private final Locale locale;

    private IsoDateFormatLocal(final Locale locale, final String ... patterns) {
      this.patterns = patterns;
      this.locale = locale;
    }

    @Override
    protected SimpleDateFormat initialValue() {
      return new IsoDateFormats(locale, patterns);
    }
  }

  public static final class IsoDate extends Date {
    private final TimeZone timeZone;
    private final ThreadLocal<SimpleDateFormat> format;
    private final int index;

    private IsoDate(final long time, final TimeZone timeZone, final ThreadLocal<SimpleDateFormat> format, final int index) {
      super(time);
      this.timeZone = timeZone;
      this.format = format;
      this.index = index;
    }

    private SimpleDateFormat getFormat() {
      SimpleDateFormat format = this.format.get();
      if (format instanceof IsoDateFormatLocal.IsoDateFormats)
        format = ((IsoDateFormatLocal.IsoDateFormats)format).formats[index];

      format.setTimeZone(timeZone);
      return format;
    }

    @Override
    public int hashCode() {
      return 31 * super.hashCode() + index;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this)
        return true;

      if (!super.equals(obj))
        return false;

      if (!(obj instanceof IsoDate))
        return true;

      return index == ((IsoDate)obj).index;
    }

    @Override
    public String toString() {
      return getFormat().format(this);
    }
  }

  /**
   * Returns a new {@link Date} object representing the {@code time} with the provided {@link SimpleDateFormat} that is to be used by
   * the {@link Date#toString()} of the returned date.
   *
   * @param time The milliseconds since January 1, 1970, 00:00:00 GMT.
   * @param format The {@link SimpleDateFormat} that is to be used by the {@link Date#toString()} of the returned date.
   * @return A new {@link Date} object representing the {@code time}.
   */
  public static Date newDate(final long time, final ThreadLocal<SimpleDateFormat> format) {
    return new IsoDate(time, TimeZone.getDefault(), format, 0);
  }

  public static final ThreadLocal<SimpleDateFormat> ISO_8601 = SimpleDateFormats.newSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
  public static final ThreadLocal<SimpleDateFormat> RFC_1123 = SimpleDateFormats.newSimpleDateFormat(Locale.US, "EEE, dd MMM yyyy HH:mm:ss Z", "EEE, dd MMM yyyy HH:mm:ss zz");

  public static ThreadLocal<SimpleDateFormat> newSimpleDateFormat(final Locale locale, final String ... patterns) {
    return new IsoDateFormatLocal(locale, patterns);
  }

  public static ThreadLocal<SimpleDateFormat> newSimpleDateFormat(final String ... patterns) {
    return newSimpleDateFormat(null, patterns);
  }

  private SimpleDateFormats() {
  }
}