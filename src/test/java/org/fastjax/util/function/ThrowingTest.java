/* Copyright (c) 2018 FastJAX
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

package org.fastjax.util.function;

import static org.fastjax.util.function.Throwing.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class ThrowingTest {
  @Test
  public void testConsumer() {
    try {
      Arrays
        .asList(1, 2, 3)
        .forEach(rethrow(i -> {
          if (i == 3)
            throw new IOException("i=" + i);
        }));

      fail("Expected IOException");
    }
    catch (final Exception e) {
      assertEquals(IOException.class, e.getClass());
      assertEquals("i=3", e.getMessage());
    }
  }

  @Test
  public void testFunction() {
    try {
      Arrays
        .asList(1, 2, 3)
        .stream()
        .map(rethrow(i -> {
          if (i == 3)
            throw new IOException("i=" + i);

          return String.valueOf(i);
        }))
        .forEach(f -> {});

      fail("Expected IOException");
    }
    catch (final Exception e) {
      assertEquals(IOException.class, e.getClass());
      assertEquals("i=3", e.getMessage());
    }
  }
}