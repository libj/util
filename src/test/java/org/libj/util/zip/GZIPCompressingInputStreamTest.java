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