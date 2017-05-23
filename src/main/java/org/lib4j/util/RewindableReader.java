/* Copyright (c) 2016 lib4j
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

import java.io.IOException;
import java.io.Reader;

public class RewindableReader extends Reader {
  private final Reader reader;
  private final StringBuilder builder = new StringBuilder();
  private int position = 0;
  private int length = 0;
  private int readAheadLimit = -1;
  private int mark = -1;

  public RewindableReader(final Reader reader) {
    this.reader = reader;
  }

  public String readFully() throws IOException {
    while (read() != -1);
    return builder.toString();
  }

  public int getLength() {
    return length;
  }

  @Override
  public int read() throws IOException {
    if (position < length)
      return builder.charAt(position++);

    final int ch = reader.read();
    if (ch > -1) {
      ++position;
      ++length;
      builder.append((char)ch);
    }

    return ch;
  }

  @Override
  public int read(final char[] cbuf, final int off, final int len) throws IOException {
    if (position < length - len) {
      builder.getChars(position += len, position + len, cbuf, off);
      return len;
    }

    int i = 0;
    if (position < length) {
      final int gap = length - position;
      builder.getChars(position, position + gap, cbuf, off);
      i += gap;
    }

    int ch = 0;
    for (; i < len && (ch = read()) > -1; i++)
      cbuf[off + i] = (char)ch;

    builder.append(cbuf, off, i);
    position += i;
    return i;
  }

  @Override
  public void mark(final int readAheadLimit) throws IOException {
    if (readAheadLimit <= 0)
      throw new IllegalArgumentException("readAheadLimit <= 0");

    this.readAheadLimit = readAheadLimit;
    this.mark = length;
  }

  @Override
  public void reset() throws IOException {
    if (readAheadLimit != -1 && length - mark > readAheadLimit)
      throw new IOException("The mark passed the readAheadLimit");

    this.position = mark;
  }

  @Override
  public void close() throws IOException {
    reader.close();
  }
}