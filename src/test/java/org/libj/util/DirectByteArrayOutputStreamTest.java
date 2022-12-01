/* Copyright (c) 2022 LibJ
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

import java.io.IOException;

import org.junit.Test;

@SuppressWarnings({"resource", "unused"})
public class DirectByteArrayOutputStreamTest {
  @Test
  public void test() throws IOException {
    try {
      new DirectByteArrayOutputStream(-1);
      fail("Expected NegativeArraySizeException");
    }
    catch (final NegativeArraySizeException e) {
    }

    final int size = 12345;
    final DirectByteArrayOutputStream out = new DirectByteArrayOutputStream(size);
    for (int i = 0; i < size; ++i) // [N]
      out.write(i);

    final byte[] b = out.toByteArray();
    for (int i = 0; i < size; ++i) // [A]
      assertEquals((byte)i, b[i]);

    try {
      out.write(0);
      fail("Expected ArrayIndexOutOfBoundsException");
    }
    catch (final ArrayIndexOutOfBoundsException e) {
    }
  }
}