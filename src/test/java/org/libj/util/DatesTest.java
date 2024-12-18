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

import static org.junit.Assert.*;
import static org.libj.lang.Strings.Align.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Random;
import java.util.TimeZone;
import java.util.function.ToLongFunction;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.libj.lang.ParseException;
import org.libj.lang.Strings;

public class DatesTest {
  private static final Random r = new Random();
  private TimeZone defaultTimeZone;

  @Before
  public void before() {
    defaultTimeZone = TimeZone.getDefault();
  }

  @After
  public void after() {
    TimeZone.setDefault(defaultTimeZone);
  }

  @Test
  @Ignore
  public void testDatePart() {
    for (long i = -50; i <= 50; ++i) // [N]
      for (long j = 0; j < Dates.MILLISECONDS_IN_DAY; j += 997) // [N]
        assertEquals("Iteration: " + i + " " + j, j, Dates.dropDatePart(Dates.MILLISECONDS_IN_DAY * i + j));
  }

  private static String removeDashes(final String str) {
    final StringBuilder b = new StringBuilder(str);
    b.delete(7, 8);
    b.delete(4, 5);
    return b.toString();
  }

  private static void assertEpochEquals(final ToLongFunction<String> fn, final long expected, final String str) throws ParseException {
    final long actual = fn.applyAsLong(str);
    assertEquals(str + ": " + expected + " != " + actual + " ~ " + Math.abs(expected - actual), expected, actual);
  }

  private static void testTime3(final ToLongFunction<String> fn, final long expected, final String str) throws ParseException {
    assertEpochEquals(fn, expected, str);
    assertEpochEquals(fn, expected, removeDashes(str));
    assertEpochEquals(fn, expected, str.replace(":", ""));
    assertEpochEquals(fn, expected, str.replace("T", ""));
    assertEpochEquals(fn, expected, removeDashes(str).replace(":", ""));
    assertEpochEquals(fn, expected, removeDashes(str).replace(":", "").replace("T", ""));
  }

  private static void testTime2(final ToLongFunction<String> fn, final long expected, String str) throws ParseException {
    testTime3(fn, expected, str);
    str = str.substring(0, str.length() - 1);

    final int hourOffset = (int)(Math.random() * 18);
    final String offset = Strings.pad(String.valueOf(hourOffset), RIGHT, 2, '0');
    testTime3(fn, expected - hourOffset * 60 * 60 * 1000, str + "+" + offset);
    testTime3(fn, expected + hourOffset * 60 * 60 * 1000, str + "-" + offset);

    final int minOffset = (int)(Math.random() * 60);
    final String offset2 = offset + ":" + Strings.pad(String.valueOf(minOffset), RIGHT, 2, '0');
    testTime3(fn, expected - (hourOffset * 60 + minOffset) * 60 * 1000, str + "+" + offset2);
    testTime3(fn, expected + (hourOffset * 60 + minOffset) * 60 * 1000, str + "-" + offset2);

    final String offset3 = offset + Strings.pad(String.valueOf(minOffset), RIGHT, 2, '0');
    testTime3(fn, expected - (hourOffset * 60 + minOffset) * 60 * 1000, str + "+" + offset3);
    testTime3(fn, expected + (hourOffset * 60 + minOffset) * 60 * 1000, str + "-" + offset3);
  }

  private static void testTime1(final ToLongFunction<String> fn, long expected, String str, final char del) throws ParseException {
    str = str.replace('T', del);
    testTime2(fn, expected, str);

    final int i = str.lastIndexOf('.') + 1;
    final int end = str.length() - 1;

    final String mils = str.substring(i, i + Math.min(3, end - i));
    int millis = Integer.parseInt(mils);
    millis *= mils.length() == 1 ? 100 : mils.length() == 2 ? 10 : 1;

    str = str.substring(0, i - 1) + str.charAt(end);
    expected -= millis;

    testTime2(fn, expected, str);
  }

  private static long usingTimeApi(final String str) {
    OffsetDateTime odt = OffsetDateTime.parse(str);
    Instant instant = odt.toInstant();
    return instant.getEpochSecond() * 1000;
  }

  private static void testTime(final ToLongFunction<String> fn, final char del) throws ParseException {
    long timeMs = fn.applyAsLong("2020-05-24" + del + "09:20:55.5Z");
    testTime1(fn, timeMs, "2020-05-24T09:20:55.5Z", del);
    testTime1(fn, timeMs, "2020-05-24T09:20:55.50Z", del);
    testTime1(fn, timeMs, "2020-05-24T09:20:55.500Z", del);
    testTime1(fn, timeMs, "2020-05-24T09:20:55.5002Z", del);
    testTime1(fn, timeMs, "2020-05-24T09:20:55.50021Z", del);
    testTime1(fn, timeMs, "2020-05-24T09:20:55.500210Z", del);
    testTime1(fn, timeMs, "2020-05-24T09:20:55.5002101Z", del);
    testTime1(fn, timeMs, "2020-05-24T09:20:55.50021012Z", del);
    testTime1(fn, timeMs, "2020-05-24T09:20:55.500210123Z", del);
    testTime1(fn, timeMs, "2020-05-24T09:20:55.5002101234Z", del);
    testTime1(fn, timeMs, "2020-05-24T09:20:55.50021012345Z", del);

    for (int i = 0; i < 100; ++i) { // [N]
      timeMs = System.currentTimeMillis();
      final String str = Dates.epochMilliToIso8601(timeMs);
      testTime1(fn, timeMs, str, del);
    }
  }

  @Test
  public void testRfc3339ToEpochMilli() throws ParseException {
    testTime(Dates::rfc3339ToEpochMilli, ' ');
  }

  @Test
  public void testIso8601ToEpochMilli() throws ParseException {
    testTime(Dates::iso8601ToEpochMilli, 'T');
  }

  @Test
  public void testDur() {
    for (int i = 0; i < 1000000; ++i) { // [N]
      final long d = r.nextLong();
      final String s = Dates.durationToString(d);
      final long d2 = Dates.stringToDuration(s);
      assertEquals(d, d2);
    }
  }
}