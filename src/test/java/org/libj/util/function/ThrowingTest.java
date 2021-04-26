/* Copyright (c) 2018 LibJ
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
import static org.libj.util.function.Throwing.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.Test;

@SuppressWarnings("unused")
public class ThrowingTest {
  @Test
  public void testRunnable() {
    try {
      final Runnable runnable = rethrow(() -> {
        if (true)
          throw new IOException();
      });

      runnable.run();
      fail("Expected IOException");
    }
    catch (final Exception e) {
      assertSame(IOException.class, e.getClass());
    }
  }

  @Test
  public void testSupplier() {
    try {
      final Supplier<String> supplier = rethrow(() -> {
        if (true)
          throw new IOException();

        return "hello world";
      });

      supplier.get();
      fail("Expected IOException");
    }
    catch (final Exception e) {
      assertSame(IOException.class, e.getClass());
    }
  }

  @Test
  public void testConsumer() {
    try {
      Arrays
        .asList(2, 1, 0)
        .forEach(rethrow(i -> {
          if (i == 0)
            throw new IOException("i=" + i);
        }));

      fail("Expected IOException");
    }
    catch (final Exception e) {
      assertSame(IOException.class, e.getClass());
      assertEquals("i=0", e.getMessage());
    }
  }

  @Test
  public void testBiConsumer() {
    try {
      final BiConsumer<Integer,Integer> consumer = rethrow((Integer i, Integer j) -> {
        if (i == 0)
          throw new IOException("i=" + i);
      });

      for (int i = 3; i >= 0; --i)
        consumer.accept(i, -i);

      fail("Expected IOException");
    }
    catch (final Exception e) {
      assertSame(IOException.class, e.getClass());
      assertEquals("i=0", e.getMessage());
    }
  }

  @Test
  public void testObjIntConsumer() {
    try {
      final ObjIntConsumer<String> consumer = rethrow((String s, int i) -> {
        if (i == 0)
          throw new IOException("i=" + i);
      });

      for (int i = 3; i >= 0; --i)
        consumer.accept(null, i);

      fail("Expected IOException");
    }
    catch (final Exception e) {
      assertSame(IOException.class, e.getClass());
      assertEquals("i=0", e.getMessage());
    }
  }

  @Test
  public void testTriConsumer() {
    try {
      final TriConsumer<Integer,Integer,Integer> consumer = rethrow((i, j, k) -> {
        if (i == 0)
          throw new IOException("i=" + i);
      });

      for (int i = 3; i >= 0; --i)
        consumer.accept(i, -i, i);

      fail("Expected IOException");
    }
    catch (final Exception e) {
      assertSame(IOException.class, e.getClass());
      assertEquals("i=0", e.getMessage());
    }
  }

  @Test
  public void testPredicate() {
    try {
      Arrays
        .asList(2, 1, 0)
        .stream()
        .filter(Throwing.<Integer>rethrow(i -> {
          if (i == 0)
            throw new IOException("i=" + i);

          return false;
        }))
        .collect(Collectors.toList());

      fail("Expected IOException");
    }
    catch (final Exception e) {
      assertSame(IOException.class, e.getClass());
      assertEquals("i=0", e.getMessage());
    }
  }

  @Test
  public void testBiPredicate() {
    try {
      final BiPredicate<Integer,Integer> predicate = Throwing.<Integer,Integer>rethrow((i, j) -> {
        if (i == 0)
          throw new IOException("i=" + i);

        return false;
      });

      for (int i = 3; i >= 0; --i)
        predicate.test(i, -i);

      fail("Expected IOException");
    }
    catch (final Exception e) {
      assertSame(IOException.class, e.getClass());
      assertEquals("i=0", e.getMessage());
    }
  }

  @Test
  public void testFunction() {
    try {
      Arrays
        .asList(2, 1, 0)
        .stream()
        .map(rethrow((Integer i) -> {
          if (i == 0)
            throw new IOException("i=" + i);

          return String.valueOf(i);
        }))
        .forEach(f -> {});

      fail("Expected IOException");
    }
    catch (final Exception e) {
      assertSame(IOException.class, e.getClass());
      assertEquals("i=0", e.getMessage());
    }
  }

  @Test
  public void testBiFunction() {
    try {
      final BiFunction<Integer,Integer,String> function = rethrow((Integer i, Integer j) -> {
        if (i == 0)
          throw new IOException("i=" + i);

        return String.valueOf(i);
      });

      for (int i = 3; i >= 0; --i)
        function.apply(i, -i);

      fail("Expected IOException");
    }
    catch (final Exception e) {
      assertSame(IOException.class, e.getClass());
      assertEquals("i=0", e.getMessage());
    }
  }
}