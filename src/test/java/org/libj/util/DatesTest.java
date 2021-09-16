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

import java.text.ParseException;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.libj.lang.Strings;

public class DatesTest {
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
  public void testDatePart() {
    for (long i = -50; i <= 50; ++i)
      for (long j = 0; j < Dates.MILLISECONDS_IN_DAY; j += 997)
        assertEquals("Iteration: " + i + " " + j, j, Dates.dropDatePart(Dates.MILLISECONDS_IN_DAY * i + j));
  }

  private static String removeDashes(final String iso8601) {
    final StringBuilder builder = new StringBuilder(iso8601);
    builder.delete(7, 8);
    builder.delete(4, 5);
    return builder.toString();
  }

  private static void testTime2(final long expected, final String iso8601) throws ParseException {
    assertEquals(expected, Dates.iso8601ToEpochMilli(iso8601), 1);
    assertEquals(expected, Dates.iso8601ToEpochMilli(removeDashes(iso8601)), 1);
    assertEquals(expected, Dates.iso8601ToEpochMilli(iso8601.replace(":", "")), 1);
    assertEquals(expected, Dates.iso8601ToEpochMilli(iso8601.replace("T", "")), 1);
    assertEquals(expected, Dates.iso8601ToEpochMilli(removeDashes(iso8601).replace(":", "")), 1);
    assertEquals(expected, Dates.iso8601ToEpochMilli(removeDashes(iso8601).replace(":", "").replace("T", "")), 1);
  }

  private static void testTime(final long expected, final String iso8601) throws ParseException {
    testTime2(expected, iso8601);
    testTime2(expected, iso8601 + "Z");
    final int hourOffset = (int)(Math.random() * 24);
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

  @Test
  public void testIso8601ToEpochMilli() throws ParseException {
    TimeZone.setDefault(Dates.UTC_TIME_ZONE);
    long time = Dates.iso8601ToEpochMilli("2020-05-24T09:20:55.5Z");
    testTime(time, "2020-05-24T09:20:55.5");
    testTime(time, "2020-05-24T09:20:55.50");
    testTime(time, "2020-05-24T09:20:55.500");
    testTime(time, "2020-05-24T09:20:55.5002");
    testTime(time, "2020-05-24T09:20:55.50021");
    testTime(time, "2020-05-24T09:20:55.500210");
    testTime(time, "2020-05-24T09:20:55.5002101");
    testTime(time, "2020-05-24T09:20:55.50021012");
    testTime(time, "2020-05-24T09:20:55.500210123");
    testTime(time, "2020-05-24T09:20:55.5002101234");
    testTime(time, "2020-05-24T09:20:55.50021012345");

    for (int i = 0; i < 100; ++i) {
      time = System.currentTimeMillis();
      final String iso8601 = Dates.epochMilliToIso8601(time);
      testTime(time, iso8601);
    }
  }
}