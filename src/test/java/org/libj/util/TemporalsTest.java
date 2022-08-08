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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.junit.Test;

public class TemporalsTest {
  @Test
  public void testConversion() {
    for (int year = -10000; year < 10000; year += 127) { // [N]
      for (int month = 1; month < 13; month++) { // [N]
        for (int date = 1; date < 29; date++) { // [N]
          final int hour = (int)(Math.random() * 24);
          if (hour == 2)
            continue;

          final int minute = (int)(Math.random() * 60);
          final int second = (int)(Math.random() * 60);
          final int millis = (int)(Math.random() * 1000);

          final LocalDateTime dateTime1 = LocalDateTime.of(year, month, date, hour, minute, second, millis * Temporals.NANOS_IN_MILLI);
          final Date date1 = Temporals.toDate(dateTime1);
          final LocalDateTime dateTime2 = Temporals.toLocalDateTime(date1);

          assertEquals(dateTime1, dateTime2);
          assertEquals(date1, Temporals.toDate(dateTime2));
        }
      }
    }
  }

  @Test
  public void testSubtract() {
    final LocalTime difference = Temporals.subtract(LocalTime.MIDNIGHT, LocalTime.parse("01:55:31", DateTimeFormatter.ISO_LOCAL_TIME));
    assertEquals(LocalTime.parse("22:04:29", DateTimeFormatter.ISO_LOCAL_TIME), difference);
  }
}