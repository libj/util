package org.libj.util.concurrent;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TimeUnitsTest {
  private static final long[] values = {Long.MIN_VALUE, Integer.MIN_VALUE, Short.MIN_VALUE, Byte.MIN_VALUE, -1, 1, Byte.MAX_VALUE, Short.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE};

  private static void test(final TimeUnit unit0, final TimeUnit unit1) {
    assertTrue(unit0.compareTo(unit1) < 0);
    final long time1 = 1;
    final long time0 = unit0.convert(time1, unit1);
    assertEquals(0, TimeUnits.compare(time0, unit0, time1, unit1));
    assertEquals(0, TimeUnits.compare(time1, unit1, time0, unit0));
    for (long value : values) {
      assertEquals(1, TimeUnits.compare(value, unit1, value, unit0));
      assertEquals(-1, TimeUnits.compare(value, unit0, value, unit1));
    }
  }

  @Test
  public void test() {
    assertEquals(0, TimeUnits.compare(Long.MAX_VALUE, TimeUnit.MILLISECONDS, Long.MAX_VALUE, TimeUnit.MILLISECONDS));
    assertEquals(0, TimeUnits.compare(Long.MAX_VALUE, TimeUnit.MICROSECONDS, Long.MAX_VALUE, TimeUnit.MICROSECONDS));
    assertEquals(0, TimeUnits.compare(Long.MAX_VALUE, TimeUnit.NANOSECONDS, Long.MAX_VALUE, TimeUnit.NANOSECONDS));

    assertEquals(0, TimeUnits.compare(Long.MAX_VALUE, TimeUnit.SECONDS, Long.MAX_VALUE, TimeUnit.SECONDS));
    assertEquals(1, TimeUnits.compare(Long.MAX_VALUE, TimeUnit.SECONDS, Long.MAX_VALUE - 1, TimeUnit.SECONDS));
    assertEquals(-1, TimeUnits.compare(Long.MAX_VALUE - 1, TimeUnit.SECONDS, Long.MAX_VALUE, TimeUnit.SECONDS));

    test(TimeUnit.HOURS, TimeUnit.DAYS);

    test(TimeUnit.MINUTES, TimeUnit.HOURS);
    test(TimeUnit.MINUTES, TimeUnit.DAYS);

    test(TimeUnit.SECONDS, TimeUnit.MINUTES);
    test(TimeUnit.SECONDS, TimeUnit.HOURS);
    test(TimeUnit.SECONDS, TimeUnit.DAYS);

    test(TimeUnit.MILLISECONDS, TimeUnit.SECONDS);
    test(TimeUnit.MILLISECONDS, TimeUnit.MINUTES);
    test(TimeUnit.MILLISECONDS, TimeUnit.HOURS);
    test(TimeUnit.MILLISECONDS, TimeUnit.DAYS);

    test(TimeUnit.MICROSECONDS, TimeUnit.MILLISECONDS);
    test(TimeUnit.MICROSECONDS, TimeUnit.SECONDS);
    test(TimeUnit.MICROSECONDS, TimeUnit.MINUTES);
    test(TimeUnit.MICROSECONDS, TimeUnit.HOURS);
    test(TimeUnit.MICROSECONDS, TimeUnit.DAYS);

    test(TimeUnit.NANOSECONDS, TimeUnit.MICROSECONDS);
    test(TimeUnit.NANOSECONDS, TimeUnit.MILLISECONDS);
    test(TimeUnit.NANOSECONDS, TimeUnit.SECONDS);
    test(TimeUnit.NANOSECONDS, TimeUnit.MINUTES);
    test(TimeUnit.NANOSECONDS, TimeUnit.HOURS);
    test(TimeUnit.NANOSECONDS, TimeUnit.DAYS);
  }
}