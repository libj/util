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

package org.libj.util.zip;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Random;
import java.util.zip.GZIPOutputStream;

import org.junit.Test;
import org.libj.util.UnsynchronizedByteArrayOutputStream;

public class UnsynchronizedGZIPOutputStreamTest {
  private final Random r = new Random();

  @Test
  public void testParity() throws IOException {
    long expectedTime = 0;
    long actualTime = 0;
    try (
      final UnsynchronizedByteArrayOutputStream expected = new UnsynchronizedByteArrayOutputStream();
      final GZIPOutputStream expectedGzip = new GZIPOutputStream(expected);

      final UnsynchronizedByteArrayOutputStream actual = new UnsynchronizedByteArrayOutputStream();
      final UnsynchronizedGZIPOutputStream actualGzip = new UnsynchronizedGZIPOutputStream(actual);
    ) {
      long ts;
      for (int i = 0; i < 100000; ++i) { // [N]
        if (r.nextBoolean()) {
          final int b = Math.abs(r.nextInt()) & 0xff;

          ts = System.nanoTime();
          expectedGzip.write(b);
          expectedTime += System.nanoTime() - ts;

          ts = System.nanoTime();
          actualGzip.write(b);
          actualTime += System.nanoTime() - ts;
        }
        else {
          final byte[] b = new byte[Math.abs(r.nextInt() % 2345) + 1];
          r.nextBytes(b);
          if (r.nextBoolean()) {
            ts = System.nanoTime();
            expectedGzip.write(b);
            expectedTime += System.nanoTime() - ts;

            ts = System.nanoTime();
            actualGzip.write(b);
            actualTime += System.nanoTime() - ts;
          }
          else {
            final int off = Math.abs(r.nextInt() % b.length);
            final int len = Math.abs(r.nextInt() % (b.length - off));

            ts = System.nanoTime();
            expectedGzip.write(b, off, len);
            expectedTime += System.nanoTime() - ts;

            ts = System.nanoTime();
            actualGzip.write(b, off, len);
            actualTime += System.nanoTime() - ts;
          }
        }
      }

      final byte[] expectedBytes = expected.toByteArray();
      final byte[] actualBytes = actual.toByteArray();

      expectedBytes[9] = 0;
      actualBytes[9] = 0;

      assertArrayEquals(expectedBytes, actualBytes);
      System.err.println("              GZIPOutputStream: " + expectedTime + "ns");
      System.err.println("UnsynchronizedGZIPOutputStream: " + actualTime + "ns");
      System.err.println("UnsynchronizedGZIPOutputStream is " + 100 * ((double)expectedTime / actualTime - 1) + "% faster than GZIPOutputStream");
    }
  }
}