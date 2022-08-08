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

  @Test @Ignore
  public void testDatePart() {
    for (long i = -50; i <= 50; ++i) // [N]
      for (long j = 0; j < Dates.MILLISECONDS_IN_DAY; j += 997) // [N]
        assertEquals("Iteration: " + i + " " + j, j, Dates.dropDatePart(Dates.MILLISECONDS_IN_DAY * i + j));
  }

  private static String removeDashes(final String iso8601) {
    final StringBuilder builder = new StringBuilder(iso8601);
    builder.delete(7, 8);
    builder.delete(4, 5);
    return builder.toString();
  }

  private static void assertEpochEquals(final long expected, final String iso8601) throws ParseException {
    final long actual = Dates.iso8601ToEpochMilli(iso8601);
    assertEquals(iso8601 + ": " + expected + " != " + expected + " ~ " + Math.abs(expected - actual), expected, actual, 1);
  }

  private static void testTime2(final long expected, final String iso8601) throws ParseException {
    assertEpochEquals(expected, iso8601);
    assertEpochEquals(expected, removeDashes(iso8601));
    assertEpochEquals(expected, iso8601.replace(":", ""));
    assertEpochEquals(expected, iso8601.replace("T", ""));
    assertEpochEquals(expected, removeDashes(iso8601).replace(":", ""));
    assertEpochEquals(expected, removeDashes(iso8601).replace(":", "").replace("T", ""));
  }

  private static void testTime(final long expected, String iso8601) throws ParseException {
    testTime2(expected, iso8601);
    iso8601 = iso8601.substring(0, iso8601.length() - 1);

    final int hourOffset = (int)(Math.random() * 18);
    final String offset = Strings.pad(String.valueOf(hourOffset), RIGHT, 2, '0');
    testTime2(expected - hourOffset * 60 * 60 * 1000, iso8601 + "+" + offset);
    testTime2(expected + hourOffset * 60 * 60 * 1000, iso8601 + "-" + offset);

    final int minOffset = (int)(Math.random() * 60);
    final String offset2 = offset + ":" + Strings.pad(String.valueOf(minOffset), RIGHT, 2, '0');
    testTime2(expected - (hourOffset * 60 + minOffset) * 60 * 1000, iso8601 + "+" + offset2);
    testTime2(expected + (hourOffset * 60 + minOffset) * 60 * 1000, iso8601 + "-" + offset2);

    final String offset3 = offset + Strings.pad(String.valueOf(minOffset), RIGHT, 2, '0');
    testTime2(expected - (hourOffset * 60 + minOffset) * 60 * 1000, iso8601 + "+" + offset3);
    testTime2(expected + (hourOffset * 60 + minOffset) * 60 * 1000, iso8601 + "-" + offset3);
  }

  private static long usingTimeApi(final String iso8601) {
    OffsetDateTime odt = OffsetDateTime.parse(iso8601);
    Instant instant = odt.toInstant();
    return instant.getEpochSecond() * 1000;
  }

  @Test @Ignore
  public void testIso8601ToEpochMilli() throws ParseException {
    long time = Dates.iso8601ToEpochMilli("2020-05-24T09:20:55.5Z");
    testTime(time, "2020-05-24T09:20:55.5Z");
    testTime(time, "2020-05-24T09:20:55.50Z");
    testTime(time, "2020-05-24T09:20:55.500Z");
    testTime(time, "2020-05-24T09:20:55.5002Z");
    testTime(time, "2020-05-24T09:20:55.50021Z");
    testTime(time, "2020-05-24T09:20:55.500210Z");
    testTime(time, "2020-05-24T09:20:55.5002101Z");
    testTime(time, "2020-05-24T09:20:55.50021012Z");
    testTime(time, "2020-05-24T09:20:55.500210123Z");
    testTime(time, "2020-05-24T09:20:55.5002101234Z");
    testTime(time, "2020-05-24T09:20:55.50021012345Z");

    for (int i = 0; i < 100; ++i) { // [N]
      time = System.currentTimeMillis();
      final String iso8601 = Dates.epochMilliToIso8601(time);
      testTime(time, iso8601);
    }
  }

  @Test
  public void testDur()  {
    for (int i = 0; i < 1000000; ++i) { // [N]
      final long d = r.nextLong();
      final String s = Dates.durationToString(d);
      final long d2 = Dates.stringToDuration(s);
      assertEquals(d, d2);
    }
  }
}