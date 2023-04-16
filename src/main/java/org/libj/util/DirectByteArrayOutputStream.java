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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * <b>An {@link UnsynchronizedByteArrayOutputStream} that does not automatically grow the underlying buffer.</b>
 * <p>
 * This class implements an output stream in which the data is written into a byte array. <s>The buffer automatically grows as data
 * is written to it. The data can be retrieved using {@link #toByteArray()} and {@link #toString()}.</s>
 * <p>
 * Closing a {@link UnsynchronizedByteArrayOutputStream} has no effect. The methods in this class can be called after the stream has
 * been closed without generating an {@link IOException}.
 *
 * @see ByteArrayOutputStream
 * @see UnsynchronizedByteArrayOutputStream
 */
public class DirectByteArrayOutputStream extends UnsynchronizedByteArrayOutputStream {
  /**
   * Creates a new {@link DirectByteArrayOutputStream} with a byte array capacity of the specified size.
   *
   * @param size The size of the buffer.
   * @throws NegativeArraySizeException If {@code size} is negative.
   */
  public DirectByteArrayOutputStream(final int size) {
    this.buf = new byte[size];
  }

  /**
   * Creates a new {@link DirectByteArrayOutputStream} with the provided underlying byte array.
   *
   * @param buf The buffer.
   * @throws NullPointerException If {@code buf} is null.
   */
  public DirectByteArrayOutputStream(final byte[] buf) {
    this.buf = Objects.requireNonNull(buf);
  }

  /**
   * Creates a new {@link DirectByteArrayOutputStream} with a null underlying byte array.
   */
  protected DirectByteArrayOutputStream() {
  }

  /**
   * {@inheritDoc}
   *
   * @throws IOException Is declared, but is never thrown.
   * @throws ArrayIndexOutOfBoundsException If an attempt is made to write beyond the length of the underlying byte array.
   */
  @Override
  public void write(final int b) throws IOException {
    buf[count] = (byte)b;
    ++count;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IOException Is declared, but is never thrown.
   * @throws NullPointerException If {@code b} or the underlying byte array is null.
   * @throws IndexOutOfBoundsException If the specified {@code off} and {@code len} are out of bounds for {@code b}.
   */
  @Override
  public void write(final byte[] b, final int off, final int len) throws IOException {
    System.arraycopy(b, off, buf, count, len);
    count += len;
  }
}