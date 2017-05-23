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

public class MonthEnum extends MaskedEnum {
  public static final MonthEnum JANUARY = new MonthEnum(Calendar.JANUARY);
  public static final MonthEnum FEBRUARY = new MonthEnum(Calendar.FEBRUARY);
  public static final MonthEnum MARCH = new MonthEnum(Calendar.MARCH);
  public static final MonthEnum APRIL = new MonthEnum(Calendar.APRIL);
  public static final MonthEnum MAY = new MonthEnum(Calendar.MAY);
  public static final MonthEnum JUNE = new MonthEnum(Calendar.JUNE);
  public static final MonthEnum JULY = new MonthEnum(Calendar.JULY);
  public static final MonthEnum AUGUST = new MonthEnum(Calendar.AUGUST);
  public static final MonthEnum SEPTEMBER = new MonthEnum(Calendar.SEPTEMBER);
  public static final MonthEnum OCTOBER = new MonthEnum(Calendar.OCTOBER);
  public static final MonthEnum NOVEMBER = new MonthEnum(Calendar.NOVEMBER);
  public static final MonthEnum DECEMBER = new MonthEnum(Calendar.DECEMBER);

  public static MonthEnum[] toArray(final int mask) {
    return MaskedEnum.<MonthEnum>toArray(MonthEnum.class, mask);
  }
  
  public static MonthEnum valueOf(final int ordinal) {
    return MaskedEnum.<MonthEnum>valueOf(MonthEnum.class, ordinal);
  }

  private MonthEnum(final int index) {
    super(index);
  }
}