/* Copyright (c) 2018 lib4j
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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

public class MemoryURLStreamHandlerTest {
  private static byte random() {
    return (byte)(Math.random() * 255);
  }

  @Test
  public void test() throws IOException {
    for (int i = 0; i < 100; i++) {
      final byte[] data = new byte[] {random(), random(), random(), random(), random()};
      final URL url = MemoryURLStreamHandler.createURL(data);
      final byte[] actual = new BufferedInputStream(url.openStream()).readAllBytes();
      Assert.assertArrayEquals(data, actual);
    }
  }
}