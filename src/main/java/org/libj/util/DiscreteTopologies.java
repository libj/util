/* Copyright (c) 2023 LibJ
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;

class LocalTimeTopology implements DiscreteTopology<LocalTime> {
  private final int precision;

  LocalTimeTopology(final int precision) {
    this.precision = precision;
  }

  @Override
  public boolean isMinValue(final LocalTime v) {
    return LocalTime.MIN.equals(v);
  }

  @Override
  public boolean isMaxValue(final LocalTime v) {
    return LocalTime.MAX.equals(v);
  }

  @Override
  public LocalTime prevValue(final LocalTime v) {
    return isMinValue(v) ? v : v.minus(precision, ChronoUnit.NANOS);
  }

  @Override
  public LocalTime nextValue(final LocalTime v) {
    return isMaxValue(v) ? v : v.plus(precision, ChronoUnit.NANOS);
  }

  @Override
  public int compare(final LocalTime o1, final LocalTime o2) {
    return o1.compareTo(o2);
  }
};

class LocalDateTimeTopology implements DiscreteTopology<LocalDateTime> {
  private final int precision;

  LocalDateTimeTopology(final int precision) {
    this.precision = precision;
  }

  @Override
  public boolean isMinValue(final LocalDateTime v) {
    return LocalDateTime.MIN.equals(v);
  }

  @Override
  public boolean isMaxValue(final LocalDateTime v) {
    return LocalDateTime.MAX.equals(v);
  }

  @Override
  public LocalDateTime prevValue(final LocalDateTime v) {
    return isMinValue(v) ? v : v.minus(precision, ChronoUnit.NANOS);
  }

  @Override
  public LocalDateTime nextValue(final LocalDateTime v) {
    return isMaxValue(v) ? v : v.plus(precision, ChronoUnit.NANOS);
  }

  @Override
  public int compare(final LocalDateTime o1, final LocalDateTime o2) {
    return o1.compareTo(o2);
  }
};

class BigDecimalTopology implements DiscreteTopology<BigDecimal> {
  private static final HashMap<Integer,BigDecimalTopology> scaleToTopology = new HashMap<>();

  static BigDecimalTopology forScale(final int scale) {
    BigDecimalTopology instance = scaleToTopology.get(scale);
    if (instance == null)
      scaleToTopology.put(scale, instance = new BigDecimalTopology(scale));

    return instance;
  }

  private BigDecimal step;

  private BigDecimalTopology(final int scale) {
    this.step = BigDecimal.valueOf(1, scale);
  }

  @Override
  public boolean isMinValue(final BigDecimal v) {
    return false;
  }

  @Override
  public boolean isMaxValue(final BigDecimal v) {
    return false;
  }

  @Override
  public BigDecimal prevValue(final BigDecimal v) {
    return v.subtract(step);
  }

  @Override
  public BigDecimal nextValue(final BigDecimal v) {
    return v.add(step);
  }

  @Override
  public int compare(final BigDecimal o1, final BigDecimal o2) {
    return o1.compareTo(o2);
  }
};

/**
 * Common {@link DiscreteTopology} implementations.
 */
public final class DiscreteTopologies {
  public static final DiscreteTopology<Boolean> BOOLEAN = new DiscreteTopology<Boolean>() {
    @Override
    public boolean isMinValue(final Boolean v) {
      return Boolean.FALSE;
    }

    @Override
    public boolean isMaxValue(final Boolean v) {
      return Boolean.TRUE;
    }

    @Override
    public Boolean prevValue(final Boolean v) {
      return Boolean.FALSE;
    }

    @Override
    public Boolean nextValue(final Boolean v) {
      return Boolean.TRUE;
    }

    @Override
    public int compare(final Boolean o1, final Boolean o2) {
      return o1.compareTo(o2);
    }
  };

  public static final DiscreteTopology<boolean[]> BOOLEANS = new DiscreteTopology<boolean[]>() {
    @Override
    public boolean isMinValue(final boolean[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != false)
          return false;

      return true;
    }

    @Override
    public boolean isMaxValue(final boolean[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != true)
          return false;

      return true;
    }

    @Override
    public boolean[] prevValue(final boolean[] v) {
      final boolean[] p = v.clone();
      int i = v.length - 1;
      for (boolean a; i >= 0; --i) { // [A]
        a = p[i];
        if (false != a) {
          p[i] = false;
          break;
        }
      }

      return p;
    }

    @Override
    public boolean[] nextValue(final boolean[] v) {
      final boolean[] n = v.clone();
      int i = v.length - 1;
      for (boolean a; i >= 0; --i) { // [A]
        a = n[i];
        if (true != a) {
          n[i] = true;
          break;
        }
      }

      return n;
    }

    @Override
    public int compare(final boolean[] o1, final boolean[] o2) {
      final int len = o1.length;
      if (len < o2.length)
        return -1;

      if (len > o2.length)
        return 1;

      for (int i = 0, c; i < len; ++i) { // [A]
        c = Boolean.compare(o1[i], o2[i]);
        if (c != 0)
          return c;
      }

      return 0;
    }
  };

  public static final DiscreteTopology<Boolean[]> BOOLEAN_OBJS = new DiscreteTopology<Boolean[]>() {
    @Override
    public boolean isMinValue(final Boolean[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != false)
          return false;

      return true;
    }

    @Override
    public boolean isMaxValue(final Boolean[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != true)
          return false;

      return true;
    }

    @Override
    public Boolean[] prevValue(final Boolean[] v) {
      final Boolean[] p = v.clone();
      int i = v.length - 1;
      for (Boolean a; i >= 0; --i) { // [A]
        a = p[i];
        if (false != a) {
          p[i] = false;
          break;
        }
      }

      return p;
    }

    @Override
    public Boolean[] nextValue(final Boolean[] v) {
      final Boolean[] n = v.clone();
      int i = v.length - 1;
      for (boolean a; i >= 0; --i) { // [A]
        a = n[i];
        if (true != a) {
          n[i] = true;
          break;
        }
      }

      return n;
    }

    @Override
    public int compare(final Boolean[] o1, final Boolean[] o2) {
      final int len = o1.length;
      if (len < o2.length)
        return -1;

      if (len > o2.length)
        return 1;

      for (int i = 0, c; i < len; ++i) { // [A]
        c = o1[i].compareTo(o2[i]);
        if (c != 0)
          return c;
      }

      return 0;
    }
  };

  public static final DiscreteTopology<Byte> BYTE = new DiscreteTopology<Byte>() {
    @Override
    public boolean isMinValue(final Byte v) {
      return Byte.MIN_VALUE == v;
    }

    @Override
    public boolean isMaxValue(final Byte v) {
      return Byte.MAX_VALUE == v;
    }

    @Override
    public Byte prevValue(Byte v) {
      return isMinValue(v) ? v : --v;
    }

    @Override
    public Byte nextValue(Byte v) {
      return isMaxValue(v) ? v : ++v;
    }

    @Override
    public int compare(final Byte o1, final Byte o2) {
      return o1.compareTo(o2);
    }
  };

  public static final DiscreteTopology<byte[]> BYTES = new DiscreteTopology<byte[]>() {
    @Override
    public boolean isMinValue(final byte[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Byte.MIN_VALUE)
          return false;

      return true;
    }

    @Override
    public boolean isMaxValue(final byte[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Byte.MAX_VALUE)
          return false;

      return true;
    }

    @Override
    public byte[] prevValue(final byte[] v) {
      final byte[] p = v.clone();
      int i = v.length - 1;
      for (byte a; i >= 0; --i) { // [A]
        a = p[i];
        if (Byte.MIN_VALUE != a) {
          p[i] = --a;
          break;
        }
      }

      return p;
    }

    @Override
    public byte[] nextValue(final byte[] v) {
      final byte[] n = v.clone();
      int i = v.length - 1;
      for (byte a; i >= 0; --i) { // [A]
        a = n[i];
        if (Byte.MAX_VALUE != a) {
          n[i] = ++a;
          break;
        }
      }

      return n;
    }

    @Override
    public int compare(final byte[] o1, final byte[] o2) {
      final int len = o1.length;
      if (len < o2.length)
        return -1;

      if (len > o2.length)
        return 1;

      for (int i = 0, c; i < len; ++i) { // [A]
        c = Byte.compare(o1[i], o2[i]);
        if (c != 0)
          return c;
      }

      return 0;
    }
  };

  public static final DiscreteTopology<Byte[]> BYTE_OBJS = new DiscreteTopology<Byte[]>() {
    @Override
    public boolean isMinValue(final Byte[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Byte.MIN_VALUE)
          return false;

      return true;
    }

    @Override
    public boolean isMaxValue(final Byte[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Byte.MAX_VALUE)
          return false;

      return true;
    }

    @Override
    public Byte[] prevValue(final Byte[] v) {
      final Byte[] p = v.clone();
      int i = v.length - 1;
      for (Byte a; i >= 0; --i) { // [A]
        a = p[i];
        if (Byte.MIN_VALUE != a) {
          p[i] = --a;
          break;
        }
      }

      return p;
    }

    @Override
    public Byte[] nextValue(final Byte[] v) {
      final Byte[] n = v.clone();
      int i = v.length - 1;
      for (Byte a; i >= 0; --i) { // [A]
        a = n[i];
        if (Byte.MAX_VALUE != a) {
          n[i] = ++a;
          break;
        }
      }

      return n;
    }

    @Override
    public int compare(final Byte[] o1, final Byte[] o2) {
      final int len = o1.length;
      if (len < o2.length)
        return -1;

      if (len > o2.length)
        return 1;

      for (int i = 0, c; i < len; ++i) { // [A]
        c = o1[i].compareTo(o2[i]);
        if (c != 0)
          return c;
      }

      return 0;
    }
  };

  public static final DiscreteTopology<Short> SHORT = new DiscreteTopology<Short>() {
    @Override
    public boolean isMinValue(final Short v) {
      return Short.MIN_VALUE == v;
    }

    @Override
    public boolean isMaxValue(final Short v) {
      return Short.MAX_VALUE == v;
    }

    @Override
    public Short prevValue(Short v) {
      return isMinValue(v) ? v : --v;
    }

    @Override
    public Short nextValue(Short v) {
      return isMaxValue(v) ? v : ++v;
    }

    @Override
    public int compare(final Short o1, final Short o2) {
      return o1.compareTo(o2);
    }
  };

  public static final DiscreteTopology<short[]> SHORTS = new DiscreteTopology<short[]>() {
    @Override
    public boolean isMinValue(final short[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Short.MIN_VALUE)
          return false;

      return true;
    }

    @Override
    public boolean isMaxValue(final short[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Short.MAX_VALUE)
          return false;

      return true;
    }

    @Override
    public short[] prevValue(final short[] v) {
      final short[] p = v.clone();
      int i = v.length - 1;
      for (short a; i >= 0; --i) { // [A]
        a = p[i];
        if (Short.MIN_VALUE != a) {
          p[i] = --a;
          break;
        }
      }

      return p;
    }

    @Override
    public short[] nextValue(final short[] v) {
      final short[] n = v.clone();
      int i = v.length - 1;
      for (short a; i >= 0; --i) { // [A]
        a = n[i];
        if (Short.MAX_VALUE != a) {
          n[i] = ++a;
          break;
        }
      }

      return n;
    }

    @Override
    public int compare(final short[] o1, final short[] o2) {
      final int len = o1.length;
      if (len < o2.length)
        return -1;

      if (len > o2.length)
        return 1;

      for (int i = 0, c; i < len; ++i) { // [A]
        c = Short.compare(o1[i], o2[i]);
        if (c != 0)
          return c;
      }

      return 0;
    }
  };

  public static final DiscreteTopology<Short[]> SHORT_OBJS = new DiscreteTopology<Short[]>() {
    @Override
    public boolean isMinValue(final Short[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Short.MIN_VALUE)
          return false;

      return true;
    }

    @Override
    public boolean isMaxValue(final Short[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Short.MAX_VALUE)
          return false;

      return true;
    }

    @Override
    public Short[] prevValue(final Short[] v) {
      final Short[] p = v.clone();
      int i = v.length - 1;
      for (Short a; i >= 0; --i) { // [A]
        a = p[i];
        if (Short.MIN_VALUE != a) {
          p[i] = --a;
          break;
        }
      }

      return p;
    }

    @Override
    public Short[] nextValue(final Short[] v) {
      final Short[] n = v.clone();
      int i = v.length - 1;
      for (Short a; i >= 0; --i) { // [A]
        a = n[i];
        if (Short.MAX_VALUE != a) {
          n[i] = ++a;
          break;
        }
      }

      return n;
    }

    @Override
    public int compare(final Short[] o1, final Short[] o2) {
      final int len = o1.length;
      if (len < o2.length)
        return -1;

      if (len > o2.length)
        return 1;

      for (int i = 0, c; i < len; ++i) { // [A]
        c = o1[i].compareTo(o2[i]);
        if (c != 0)
          return c;
      }

      return 0;
    }
  };

  public static final DiscreteTopology<Integer> INTEGER = new DiscreteTopology<Integer>() {
    @Override
    public boolean isMinValue(final Integer v) {
      return Integer.MIN_VALUE == v;
    }

    @Override
    public boolean isMaxValue(final Integer v) {
      return Integer.MAX_VALUE == v;
    }

    @Override
    public Integer prevValue(Integer v) {
      return isMinValue(v) ? v : --v;
    }

    @Override
    public Integer nextValue(Integer v) {
      return isMaxValue(v) ? v : ++v;
    }

    @Override
    public int compare(final Integer o1, final Integer o2) {
      return o1.compareTo(o2);
    }
  };

  public static final DiscreteTopology<int[]> INTS = new DiscreteTopology<int[]>() {
    @Override
    public boolean isMinValue(final int[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Integer.MIN_VALUE)
          return false;

      return true;
    }

    @Override
    public boolean isMaxValue(final int[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Integer.MAX_VALUE)
          return false;

      return true;
    }

    @Override
    public int[] prevValue(final int[] v) {
      final int[] p = v.clone();
      int i = v.length - 1;
      for (int a; i >= 0; --i) { // [A]
        a = p[i];
        if (Integer.MIN_VALUE != a) {
          p[i] = --a;
          break;
        }
      }

      return p;
    }

    @Override
    public int[] nextValue(final int[] v) {
      final int[] n = v.clone();
      int i = v.length - 1;
      for (int a; i >= 0; --i) { // [A]
        a = n[i];
        if (Integer.MAX_VALUE != a) {
          n[i] = ++a;
          break;
        }
      }

      return n;
    }

    @Override
    public int compare(final int[] o1, final int[] o2) {
      final int len = o1.length;
      if (len < o2.length)
        return -1;

      if (len > o2.length)
        return 1;

      for (int i = 0, c; i < len; ++i) { // [A]
        c = Integer.compare(o1[i], o2[i]);
        if (c != 0)
          return c;
      }

      return 0;
    }
  };

  public static final DiscreteTopology<Integer[]> INTEGERS = new DiscreteTopology<Integer[]>() {
    @Override
    public boolean isMinValue(final Integer[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Integer.MIN_VALUE)
          return false;

      return true;
    }

    @Override
    public boolean isMaxValue(final Integer[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Integer.MAX_VALUE)
          return false;

      return true;
    }

    @Override
    public Integer[] prevValue(final Integer[] v) {
      final Integer[] p = v.clone();
      int i = v.length - 1;
      for (Integer a; i >= 0; --i) { // [A]
        a = p[i];
        if (Integer.MIN_VALUE != a) {
          p[i] = --a;
          break;
        }
      }

      return p;
    }

    @Override
    public Integer[] nextValue(final Integer[] v) {
      final Integer[] n = v.clone();
      int i = v.length - 1;
      for (Integer a; i >= 0; --i) { // [A]
        a = n[i];
        if (Integer.MAX_VALUE != a) {
          n[i] = ++a;
          break;
        }
      }

      return n;
    }

    @Override
    public int compare(final Integer[] o1, final Integer[] o2) {
      final int len = o1.length;
      if (len < o2.length)
        return -1;

      if (len > o2.length)
        return 1;

      for (int i = 0, c; i < len; ++i) { // [A]
        c = o1[i].compareTo(o2[i]);
        if (c != 0)
          return c;
      }

      return 0;
    }
  };

  public static final DiscreteTopology<Long> LONG = new DiscreteTopology<Long>() {
    @Override
    public boolean isMinValue(final Long v) {
      return Long.MIN_VALUE == v;
    }

    @Override
    public boolean isMaxValue(final Long v) {
      return Long.MAX_VALUE == v;
    }

    @Override
    public Long prevValue(Long v) {
      return isMinValue(v) ? v : --v;
    }

    @Override
    public Long nextValue(Long v) {
      return isMaxValue(v) ? v : ++v;
    }

    @Override
    public int compare(final Long o1, final Long o2) {
      return o1.compareTo(o2);
    }
  };

  public static final DiscreteTopology<long[]> LONGS = new DiscreteTopology<long[]>() {
    @Override
    public boolean isMinValue(final long[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Long.MIN_VALUE)
          return false;

      return true;
    }

    @Override
    public boolean isMaxValue(final long[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Long.MAX_VALUE)
          return false;

      return true;
    }

    @Override
    public long[] prevValue(final long[] v) {
      final long[] p = v.clone();
      int i = v.length - 1;
      for (long a; i >= 0; --i) { // [A]
        a = p[i];
        if (Long.MIN_VALUE != a) {
          p[i] = --a;
          break;
        }
      }

      return p;
    }

    @Override
    public long[] nextValue(final long[] v) {
      final long[] n = v.clone();
      int i = v.length - 1;
      for (long a; i >= 0; --i) { // [A]
        a = n[i];
        if (Long.MAX_VALUE != a) {
          n[i] = ++a;
          break;
        }
      }

      return n;
    }

    @Override
    public int compare(final long[] o1, final long[] o2) {
      final int len = o1.length;
      if (len < o2.length)
        return -1;

      if (len > o2.length)
        return 1;

      for (int i = 0, c; i < len; ++i) { // [A]
        c = Long.compare(o1[i], o2[i]);
        if (c != 0)
          return c;
      }

      return 0;
    }
  };

  public static final DiscreteTopology<Long[]> LONG_OBJS = new DiscreteTopology<Long[]>() {
    @Override
    public boolean isMinValue(final Long[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Long.MIN_VALUE)
          return false;

      return true;
    }

    @Override
    public boolean isMaxValue(final Long[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Long.MAX_VALUE)
          return false;

      return true;
    }

    @Override
    public Long[] prevValue(final Long[] v) {
      final Long[] p = v.clone();
      int i = v.length - 1;
      for (Long a; i >= 0; --i) { // [A]
        a = p[i];
        if (Long.MIN_VALUE != a) {
          p[i] = --a;
          break;
        }
      }

      return p;
    }

    @Override
    public Long[] nextValue(final Long[] v) {
      final Long[] n = v.clone();
      int i = v.length - 1;
      for (Long a; i >= 0; --i) { // [A]
        a = n[i];
        if (Long.MAX_VALUE != a) {
          n[i] = ++a;
          break;
        }
      }

      return n;
    }

    @Override
    public int compare(final Long[] o1, final Long[] o2) {
      final int len = o1.length;
      if (len < o2.length)
        return -1;

      if (len > o2.length)
        return 1;

      for (int i = 0, c; i < len; ++i) { // [A]
        c = o1[i].compareTo(o2[i]);
        if (c != 0)
          return c;
      }

      return 0;
    }
  };

  public static final DiscreteTopology<Float> FLOAT = new DiscreteTopology<Float>() {
    @Override
    public boolean isMinValue(final Float v) {
      return Float.MIN_VALUE == v;
    }

    @Override
    public boolean isMaxValue(final Float v) {
      return Float.MAX_VALUE == v;
    }

    @Override
    public Float prevValue(final Float v) {
      return isMinValue(v) ? v : Math.nextDown(v);
    }

    @Override
    public Float nextValue(final Float v) {
      return isMaxValue(v) ? v : Math.nextUp(v);
    }

    @Override
    public int compare(final Float o1, final Float o2) {
      return o1.compareTo(o2);
    }
  };

  public static final DiscreteTopology<float[]> FLOATS = new DiscreteTopology<float[]>() {
    @Override
    public boolean isMinValue(final float[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Float.MIN_VALUE)
          return false;

      return true;
    }

    @Override
    public boolean isMaxValue(final float[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Float.MAX_VALUE)
          return false;

      return true;
    }

    @Override
    public float[] prevValue(final float[] v) {
      final float[] p = v.clone();
      int i = v.length - 1;
      for (float a; i >= 0; --i) { // [A]
        a = p[i];
        if (Float.MIN_VALUE != a) {
          p[i] = Math.nextDown(a);
          break;
        }
      }

      return p;
    }

    @Override
    public float[] nextValue(final float[] v) {
      final float[] n = v.clone();
      int i = v.length - 1;
      for (float a; i >= 0; --i) { // [A]
        a = n[i];
        if (Float.MAX_VALUE != a) {
          n[i] = Math.nextUp(a);
          break;
        }
      }

      return n;
    }

    @Override
    public int compare(final float[] o1, final float[] o2) {
      final int len = o1.length;
      if (len < o2.length)
        return -1;

      if (len > o2.length)
        return 1;

      for (int i = 0, c; i < len; ++i) { // [A]
        c = Float.compare(o1[i], o2[i]);
        if (c != 0)
          return c;
      }

      return 0;
    }
  };

  public static final DiscreteTopology<Float[]> FLOAT_OBJS = new DiscreteTopology<Float[]>() {
    @Override
    public boolean isMinValue(final Float[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Float.MIN_VALUE)
          return false;

      return true;
    }

    @Override
    public boolean isMaxValue(final Float[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Float.MAX_VALUE)
          return false;

      return true;
    }

    @Override
    public Float[] prevValue(final Float[] v) {
      final Float[] p = v.clone();
      int i = v.length - 1;
      for (Float a; i >= 0; --i) { // [A]
        a = p[i];
        if (Float.MIN_VALUE != a) {
          p[i] = Math.nextDown(a);
          break;
        }
      }

      return p;
    }

    @Override
    public Float[] nextValue(final Float[] v) {
      final Float[] n = v.clone();
      int i = v.length - 1;
      for (Float a; i >= 0; --i) { // [A]
        a = n[i];
        if (Float.MAX_VALUE != a) {
          n[i] = Math.nextUp(a);
          break;
        }
      }

      return n;
    }

    @Override
    public int compare(final Float[] o1, final Float[] o2) {
      final int len = o1.length;
      if (len < o2.length)
        return -1;

      if (len > o2.length)
        return 1;

      for (int i = 0, c; i < len; ++i) { // [A]
        c = o1[i].compareTo(o2[i]);
        if (c != 0)
          return c;
      }

      return 0;
    }
  };

  public static final DiscreteTopology<Double> DOUBLE = new DiscreteTopology<Double>() {
    @Override
    public boolean isMinValue(final Double v) {
      return Double.MIN_VALUE == v;
    }

    @Override
    public boolean isMaxValue(final Double v) {
      return Double.MAX_VALUE == v;
    }

    @Override
    public Double prevValue(final Double v) {
      return isMinValue(v) ? v : Math.nextDown(v);
    }

    @Override
    public Double nextValue(final Double v) {
      return isMaxValue(v) ? v : Math.nextUp(v);
    }

    @Override
    public int compare(final Double o1, final Double o2) {
      return o1.compareTo(o2);
    }
  };

  public static final DiscreteTopology<double[]> DOUBLES = new DiscreteTopology<double[]>() {
    @Override
    public boolean isMinValue(final double[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Double.MIN_VALUE)
          return false;

      return true;
    }

    @Override
    public boolean isMaxValue(final double[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Double.MAX_VALUE)
          return false;

      return true;
    }

    @Override
    public double[] prevValue(final double[] v) {
      final double[] p = v.clone();
      int i = v.length - 1;
      for (double a; i >= 0; --i) { // [A]
        a = p[i];
        if (Double.MIN_VALUE != a) {
          p[i] = Math.nextDown(a);
          break;
        }
      }

      return p;
    }

    @Override
    public double[] nextValue(final double[] v) {
      final double[] n = v.clone();
      int i = v.length - 1;
      for (double a; i >= 0; --i) { // [A]
        a = n[i];
        if (Double.MAX_VALUE != a) {
          n[i] = Math.nextUp(a);
          break;
        }
      }

      return n;
    }

    @Override
    public int compare(final double[] o1, final double[] o2) {
      final int len = o1.length;
      if (len < o2.length)
        return -1;

      if (len > o2.length)
        return 1;

      for (int i = 0, c; i < len; ++i) { // [A]
        c = Double.compare(o1[i], o2[i]);
        if (c != 0)
          return c;
      }

      return 0;
    }
  };

  public static final DiscreteTopology<Double[]> DOUBLE_OBJS = new DiscreteTopology<Double[]>() {
    @Override
    public boolean isMinValue(final Double[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Double.MIN_VALUE)
          return false;

      return true;
    }

    @Override
    public boolean isMaxValue(final Double[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Double.MAX_VALUE)
          return false;

      return true;
    }

    @Override
    public Double[] prevValue(final Double[] v) {
      final Double[] p = v.clone();
      int i = v.length - 1;
      for (Double a; i >= 0; --i) { // [A]
        a = p[i];
        if (Double.MIN_VALUE != a) {
          p[i] = Math.nextDown(a);
          break;
        }
      }

      return p;
    }

    @Override
    public Double[] nextValue(final Double[] v) {
      final Double[] n = v.clone();
      int i = v.length - 1;
      for (Double a; i >= 0; --i) { // [A]
        a = n[i];
        if (Double.MAX_VALUE != a) {
          n[i] = Math.nextUp(a);
          break;
        }
      }

      return n;
    }

    @Override
    public int compare(final Double[] o1, final Double[] o2) {
      final int len = o1.length;
      if (len < o2.length)
        return -1;

      if (len > o2.length)
        return 1;

      for (int i = 0, c; i < len; ++i) { // [A]
        c = o1[i].compareTo(o2[i]);
        if (c != 0)
          return c;
      }

      return 0;
    }
  };

  public static final DiscreteTopology<Character> CHARACTER = new DiscreteTopology<Character>() {
    @Override
    public boolean isMinValue(final Character v) {
      return Character.MIN_VALUE == v;
    }

    @Override
    public boolean isMaxValue(final Character v) {
      return Character.MAX_VALUE == v;
    }

    @Override
    public Character prevValue(Character v) {
      return isMinValue(v) ? v : --v;
    }

    @Override
    public Character nextValue(Character v) {
      return isMaxValue(v) ? v : ++v;
    }

    @Override
    public int compare(final Character o1, final Character o2) {
      return o1.compareTo(o2);
    }
  };

  public static final DiscreteTopology<char[]> CHARS = new DiscreteTopology<char[]>() {
    @Override
    public boolean isMinValue(final char[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Character.MIN_VALUE)
          return false;

      return true;
    }

    @Override
    public boolean isMaxValue(final char[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Character.MAX_VALUE)
          return false;

      return true;
    }

    @Override
    public char[] prevValue(final char[] v) {
      final char[] p = v.clone();
      int i = v.length - 1;
      for (char a; i >= 0; --i) { // [A]
        a = p[i];
        if (Character.MIN_VALUE != a) {
          p[i] = --a;
          break;
        }
      }

      return p;
    }

    @Override
    public char[] nextValue(final char[] v) {
      final char[] n = v.clone();
      int i = v.length - 1;
      for (char a; i >= 0; --i) { // [A]
        a = n[i];
        if (Character.MAX_VALUE != a) {
          n[i] = ++a;
          break;
        }
      }

      return n;
    }

    @Override
    public int compare(final char[] o1, final char[] o2) {
      final int len = o1.length;
      if (len < o2.length)
        return -1;

      if (len > o2.length)
        return 1;

      for (int i = 0, c; i < len; ++i) { // [A]
        c = Character.compare(o1[i], o2[i]);
        if (c != 0)
          return c;
      }

      return 0;
    }
  };

  public static final DiscreteTopology<Character[]> CHARACTERS = new DiscreteTopology<Character[]>() {
    @Override
    public boolean isMinValue(final Character[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Character.MIN_VALUE)
          return false;

      return true;
    }

    @Override
    public boolean isMaxValue(final Character[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Character.MAX_VALUE)
          return false;

      return true;
    }

    @Override
    public Character[] prevValue(final Character[] v) {
      final Character[] p = v.clone();
      int i = v.length - 1;
      for (Character a; i >= 0; --i) { // [A]
        a = p[i];
        if (Character.MIN_VALUE != a) {
          p[i] = --a;
          break;
        }
      }

      return p;
    }

    @Override
    public Character[] nextValue(final Character[] v) {
      final Character[] n = v.clone();
      int i = v.length - 1;
      for (Character a; i >= 0; --i) { // [A]
        a = n[i];
        if (Character.MAX_VALUE != a) {
          n[i] = ++a;
          break;
        }
      }

      return n;
    }

    @Override
    public int compare(final Character[] o1, final Character[] o2) {
      final int len = o1.length;
      if (len < o2.length)
        return -1;

      if (len > o2.length)
        return 1;

      for (int i = 0, c; i < len; ++i) { // [A]
        c = o1[i].compareTo(o2[i]);
        if (c != 0)
          return c;
      }

      return 0;
    }
  };

  // FIXME: Untested
  public static DiscreteTopology<BigDecimal> BIG_DECIMAL(final int scale) {
    return BigDecimalTopology.forScale(scale);
  }

  // FIXME: Untested
  public static final DiscreteTopology<BigInteger> BIG_INTEGER = new DiscreteTopology<BigInteger>() {
    @Override
    public boolean isMinValue(final BigInteger v) {
      return false;
    }

    @Override
    public boolean isMaxValue(final BigInteger v) {
      return false;
    }

    @Override
    public BigInteger prevValue(final BigInteger v) {
      return v.subtract(BigInteger.ONE);
    }

    @Override
    public BigInteger nextValue(final BigInteger v) {
      return v.add(BigInteger.ONE);
    }

    @Override
    public int compare(final BigInteger o1, final BigInteger o2) {
      return o1.compareTo(o2);
    }
  };

  public static final LocalTimeTopology[] LOCAL_TIME = {
    new LocalTimeTopology(1000000000),
    new LocalTimeTopology(100000000),
    new LocalTimeTopology(10000000),
    new LocalTimeTopology(1000000),
    new LocalTimeTopology(100000),
    new LocalTimeTopology(10000),
    new LocalTimeTopology(1000),
    new LocalTimeTopology(100),
    new LocalTimeTopology(10),
    new LocalTimeTopology(1)
  };

  public static final LocalDateTimeTopology[] LOCAL_DATE_TIME = {
    new LocalDateTimeTopology(1000000000),
    new LocalDateTimeTopology(100000000),
    new LocalDateTimeTopology(10000000),
    new LocalDateTimeTopology(1000000),
    new LocalDateTimeTopology(100000),
    new LocalDateTimeTopology(10000),
    new LocalDateTimeTopology(1000),
    new LocalDateTimeTopology(100),
    new LocalDateTimeTopology(10),
    new LocalDateTimeTopology(1)
  };

  public static final DiscreteTopology<LocalDate> LOCAL_DATE = new DiscreteTopology<LocalDate>() {
    @Override
    public boolean isMinValue(final LocalDate v) {
      return LocalDate.MIN.equals(v);
    }

    @Override
    public boolean isMaxValue(final LocalDate v) {
      return LocalDate.MAX.equals(v);
    }

    @Override
    public LocalDate prevValue(final LocalDate v) {
      return isMinValue(v) ? v : v.minusDays(1);
    }

    @Override
    public LocalDate nextValue(final LocalDate v) {
      return isMaxValue(v) ? v : v.plusDays(1);
    }

    @Override
    public int compare(final LocalDate o1, final LocalDate o2) {
      return o1.compareTo(o2);
    }
  };

  public static final DiscreteTopology<LocalDate[]> LOCAL_DATES = new DiscreteTopology<LocalDate[]>() {
    @Override
    public boolean isMinValue(final LocalDate[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != LocalDate.MIN)
          return false;

      return true;
    }

    @Override
    public boolean isMaxValue(final LocalDate[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != LocalDate.MAX)
          return false;

      return true;
    }

    @Override
    public LocalDate[] prevValue(final LocalDate[] v) {
      final LocalDate[] p = v.clone();
      int i = v.length - 1;
      for (LocalDate a; i >= 0; --i) { // [A]
        a = p[i];
        if (!LocalDate.MIN.equals(a)) {
          p[i] = a.minusDays(1);
          break;
        }
      }

      return p;
    }

    @Override
    public LocalDate[] nextValue(final LocalDate[] v) {
      final LocalDate[] n = v.clone();
      int i = v.length - 1;
      for (LocalDate a; i >= 0; --i) { // [A]
        a = n[i];
        if (!LocalDate.MAX.equals(a)) {
          n[i] = a.plusDays(1);
          break;
        }
      }

      return n;
    }

    @Override
    public int compare(final LocalDate[] o1, final LocalDate[] o2) {
      final int len = o1.length;
      if (len < o2.length)
        return -1;

      if (len > o2.length)
        return 1;

      for (int i = 0, c; i < len; ++i) { // [A]
        c = o1[i].compareTo(o2[i]);
        if (c != 0)
          return c;
      }

      return 0;
    }
  };

  public static final DiscreteTopology<Date> DATE = new DiscreteTopology<Date>() {
    @Override
    public boolean isMinValue(final Date v) {
      return Dates.MIN_VALUE.equals(v);
    }

    @Override
    public boolean isMaxValue(final Date v) {
      return Dates.MAX_VALUE.equals(v);
    }

    @Override
    public Date prevValue(final Date v) {
      return isMinValue(v) ? v : Dates.addTime(v, -24);
    }

    @Override
    public Date nextValue(final Date v) {
      return isMaxValue(v) ? v : Dates.addTime(v, 24);
    }

    @Override
    public int compare(final Date o1, final Date o2) {
      return o1.compareTo(o2);
    }
  };

  public static final DiscreteTopology<Date[]> DATES = new DiscreteTopology<Date[]>() {
    @Override
    public boolean isMinValue(final Date[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Dates.MIN_VALUE)
          return false;

      return true;
    }

    @Override
    public boolean isMaxValue(final Date[] v) {
      for (int i = 0, i$ = v.length; i < i$; ++i) // [A]
        if (v[i] != Dates.MAX_VALUE)
          return false;

      return true;
    }

    @Override
    public Date[] prevValue(final Date[] v) {
      final Date[] p = v.clone();
      int i = v.length - 1;
      for (Date a; i >= 0; --i) { // [A]
        a = p[i];
        if (!Dates.MIN_VALUE.equals(a)) {
          p[i] = Dates.addTime(a, -24);
          break;
        }
      }

      return p;
    }

    @Override
    public Date[] nextValue(final Date[] v) {
      final Date[] n = v.clone();
      int i = v.length - 1;
      for (Date a; i >= 0; --i) { // [A]
        a = n[i];
        if (!Dates.MAX_VALUE.equals(a)) {
          n[i] = Dates.addTime(a, 24);
          break;
        }
      }

      return n;
    }

    @Override
    public int compare(final Date[] o1, final Date[] o2) {
      final int len = o1.length;
      if (len < o2.length)
        return -1;

      if (len > o2.length)
        return 1;

      for (int i = 0, c; i < len; ++i) { // [A]
        c = o1[i].compareTo(o2[i]);
        if (c != 0)
          return c;
      }

      return 0;
    }
  };

  public static final DiscreteTopology<String> STRING = new DiscreteTopology<String>() {
    @Override
    public boolean isMinValue(final String v) {
      for (int i = 0, i$ = v.length(); i < i$; ++i) // [ST]
        if (v.charAt(i) != Character.MIN_VALUE)
          return false;

      return true;
    }

    @Override
    public boolean isMaxValue(final String v) {
      for (int i = 0, i$ = v.length(); i < i$; ++i) // [ST]
        if (v.charAt(i) != Character.MAX_VALUE)
          return false;

      return true;
    }

    @Override
    public String prevValue(final String v) {
      final int len = v.length();
      final StringBuilder n = new StringBuilder(v);
      int i = len - 1;
      for (char a; i >= 0; --i) { // [ST]
        a = v.charAt(i);
        if (Character.MIN_VALUE != a) {
          n.setCharAt(i, --a);
          return n.toString();
        }
      }

      return v;
    }

    @Override
    public String nextValue(final String v) {
      final int len = v.length();
      final StringBuilder n = new StringBuilder(v);
      int i = len - 1;
      for (char a; i >= 0; --i) { // [ST]
        a = v.charAt(i);
        if (Character.MAX_VALUE != a) {
          n.setCharAt(i, ++a);
          return n.toString();
        }
      }

      return v;
    }

    @Override
    public int compare(final String o1, final String o2) {
      return o1.compareTo(o2);
    }
  };

  // FIXME: Untested
  @SuppressWarnings("rawtypes")
  public static DiscreteTopology getTopology(Class<?> cls) {
    if (cls.isArray()) {
      cls = cls.getComponentType();
      if (cls.isPrimitive()) {
        if (int.class.equals(cls))
          return DiscreteTopologies.INTS;

        if (long.class.equals(cls))
          return DiscreteTopologies.LONGS;

        if (double.class.equals(cls))
          return DiscreteTopologies.DOUBLES;

        if (short.class.equals(cls))
          return DiscreteTopologies.SHORTS;

        if (byte.class.equals(cls))
          return DiscreteTopologies.BYTES;

        if (float.class.equals(cls))
          return DiscreteTopologies.FLOATS;

        if (char.class.equals(cls))
          return DiscreteTopologies.CHARS;

        if (boolean.class.equals(cls))
          return DiscreteTopologies.BOOLEANS;
      }
      else {
        if (Integer.class.equals(cls))
          return DiscreteTopologies.INTEGERS;

        if (Long.class.equals(cls))
          return DiscreteTopologies.LONG_OBJS;

        if (Double.class.equals(cls))
          return DiscreteTopologies.DOUBLE_OBJS;

        if (Short.class.equals(cls))
          return DiscreteTopologies.SHORT_OBJS;

        if (Byte.class.equals(cls))
          return DiscreteTopologies.BYTE_OBJS;

        if (Float.class.equals(cls))
          return DiscreteTopologies.FLOAT_OBJS;

        if (Character.class.equals(cls))
          return DiscreteTopologies.CHARACTERS;

        if (Boolean.class.equals(cls))
          return DiscreteTopologies.BOOLEAN_OBJS;

        if (LocalDate.class.equals(cls))
          return DiscreteTopologies.LOCAL_DATES;

        if (Date.class.equals(cls))
          return DiscreteTopologies.DATES;

//        if (String.class.equals(cls))
//          return DiscreteTopology.STRINGS;
      }
    }
    else {
      if (Integer.class.equals(cls))
        return DiscreteTopologies.INTEGER;

      if (Long.class.equals(cls))
        return DiscreteTopologies.LONG;

      if (Double.class.equals(cls))
        return DiscreteTopologies.DOUBLE;

      if (Short.class.equals(cls))
        return DiscreteTopologies.SHORT;

      if (Byte.class.equals(cls))
        return DiscreteTopologies.BYTE;

      if (Float.class.equals(cls))
        return DiscreteTopologies.FLOAT;

      if (Character.class.equals(cls))
        return DiscreteTopologies.CHARACTER;

      if (Boolean.class.equals(cls))
        return DiscreteTopologies.BOOLEAN;

      if (LocalDate.class.equals(cls))
        return DiscreteTopologies.LOCAL_DATE;

      if (Date.class.equals(cls))
        return DiscreteTopologies.DATE;

      if (String.class.equals(cls))
        return DiscreteTopologies.STRING;

      if (BigInteger.class.equals(cls))
        return DiscreteTopologies.BIG_INTEGER;

//      if (BigDecimal.class.equals(cls))
//        return DiscreteTopology.BIG_DECIMAL;
    }

    throw new IllegalArgumentException("Class " + cls.getName() + " is not supported");
  }
}