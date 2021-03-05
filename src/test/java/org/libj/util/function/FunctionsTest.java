/* Copyright (c) 2021 LibJ
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

package org.libj.util.function;

import static org.junit.Assert.*;
import static org.libj.util.function.Functions.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.Test;

public class FunctionsTest {
  private static final int numTests = 1000;
  private static final Random random = new Random();

  private static class DoubleToString implements Function<Double,String> {
    @Override
    public String apply(final Double t) {
      return t.toString();
    }
  }

  private static class Negate implements Function<String,String> {
    @Override
    public String apply(final String t) {
      return t.charAt(0) != '-' ? "-" + t : t.substring(1);
    }
  }

  private static class Identity implements Function<String,String> {
    @Override
    public String apply(final String t) {
      return t;
    }
  }

  private static class StringToBigDecimal implements Function<String,BigDecimal> {
    @Override
    public BigDecimal apply(final String t) {
      return new BigDecimal(t);
    }
  }

  @Test
  public void testAndThen() throws Exception {
    final Function<Double,String> p0 = new DoubleToString();
    final Function<String,String> p1 = new Negate();
    final Function<String,BigDecimal> p2 = new StringToBigDecimal();
    for (int i = 0; i < numTests; ++i) {
      final double input = random.nextDouble();
      final BigDecimal result = p0.andThen(p1).andThen(p2).apply(input);
      assertEquals(-input, result.doubleValue(), Math.ulp(input));
    }
  }

  @Test
  public void testAnd() throws Exception {
    final Function<Double,String> p0 = new DoubleToString();
    final Function<String,String> p1 = new Negate();
    final Function<String,String> p2 = new Identity();
    for (int i = 0; i < numTests; ++i) {
      final double input = random.nextDouble();
      final Stream<String> result1 = p0.andThen(and(p1, p2)).apply(input);
      final Stream<String> result2 = p0.andThen(and(p2, p1)).apply(input);
      assertEquals(String.valueOf(-input), result1.findFirst().get());
      assertEquals(String.valueOf(input), result2.findFirst().get());
    }
  }

  @Test
  public void testOr() throws Exception {
    final Function<Double,String> p0 = new DoubleToString();
    final Function<String,String> p1 = new Negate();
    final Function<String,String> p2 = new Identity();
    for (int i = 0; i < numTests; ++i) {
      final double input = random.nextDouble();
      final String result1 = p0.andThen(or(p1, p2)).apply(input);
      final String result2 = p0.andThen(or(p2, p1)).apply(input);
      assertEquals(String.valueOf(-input), result1);
      assertEquals(String.valueOf(input), result2);
    }
  }

  @Test
  public void testAndOrThen() {
    final Function<Integer,StringBuilder> p0 = i -> new StringBuilder(String.valueOf(Math.abs(i)) + "9").reverse();
    final Function<Integer,CharSequence> p1 = i -> "9" + new StringBuilder(String.valueOf(Math.abs(i))).reverse().toString();
    final Function<Integer,Comparable<String>> p2 = i -> new StringBuilder(String.valueOf(Math.abs(i)) + "9").reverse().toString();

    final Function<Object,Number> p3 = s -> Long.parseLong(s.toString());
    final Function<Object,String> p4 = s -> "%%%";

    final Function<Serializable,String> finish = s -> new StringBuilder(s.toString()).reverse().toString();

    for (int i = 0; i < numTests; ++i) {
      final int input = random.nextInt();
      final String result = and(p0, p1, p2).andThen(s -> s.map(or(p3, p4).andThen(finish))).apply(input).findAny().get();
      assertEquals(String.valueOf(Math.abs(input)) + "9", result);
    }
  }
}