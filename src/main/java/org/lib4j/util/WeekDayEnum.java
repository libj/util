/* Copyright (c) 2015 lib4j
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

import java.util.Calendar;

public class WeekDayEnum extends MaskedEnum {
  public static final WeekDayEnum MONDAY = new WeekDayEnum(Calendar.MONDAY);
  public static final WeekDayEnum TUESDAY = new WeekDayEnum(Calendar.TUESDAY);
  public static final WeekDayEnum WEDNESDAY = new WeekDayEnum(Calendar.WEDNESDAY);
  public static final WeekDayEnum THURSDAY = new WeekDayEnum(Calendar.THURSDAY);
  public static final WeekDayEnum FRIDAY = new WeekDayEnum(Calendar.FRIDAY);
  public static final WeekDayEnum SATURDAY = new WeekDayEnum(Calendar.SATURDAY);
  public static final WeekDayEnum SUNDAY = new WeekDayEnum(Calendar.SUNDAY);

  public static WeekDayEnum[] toArray(final int mask) {
    return MaskedEnum.<WeekDayEnum>toArray(WeekDayEnum.class, mask);
  }
  
  public static WeekDayEnum valueOf(final int ordinal) {
    return MaskedEnum.<WeekDayEnum>valueOf(WeekDayEnum.class, ordinal);
  }

  private WeekDayEnum(final int index) {
    super((index + 5) % 7);
  }
}