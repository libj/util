/* Copyright (c) 2024 LibJ
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

package org.libj.util.zip;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Random;
import java.util.zip.GZIPInputStream;

import org.junit.Test;
import org.libj.lang.Strings;

public class GZIPCompressingInputStreamTest {
  private static void test(final String str) throws IOException {
    final byte[] bytes = str.getBytes();
    final InputStream in = new GZIPCompressingInputStream(new ByteArrayInputStream(bytes));
    final PipedInputStream pin = new PipedInputStream(8192);
    try (final PipedOutputStream pout = new PipedOutputStream(pin)) {
      for (int c; (c = in.read()) != -1;) // [ST]
        pout.write(c);
    }

    final GZIPInputStream zin = new GZIPInputStream(pin);
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    for (int c; (c = zin.read()) != -1;) // [ST]
      out.write(c);

    assertArrayEquals(bytes, out.toByteArray());
  }

  @Test
  public void test() throws IOException {
    final Random random = new Random();
    for (int i = 0; i < 100; ++i)
      test(Strings.getRandomAlphaNumeric(random, i * i + 1));
  }
}