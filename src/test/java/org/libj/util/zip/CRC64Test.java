/* Copyright (c) 2021 OpenJAX
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

import org.junit.Test;

public class CRC64Test {
  @Test
  public void test() {
    final CRC64 crc = new CRC64();
    crc.update("foo bar".getBytes());
    assertEquals("8d145fadb8898c9c", Long.toHexString(crc.getValue()));
  }
}