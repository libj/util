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
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * <b>An unsynchronized variant of {@link java.io.ByteArrayOutputStream}.</b>
 * <p>
 * This class implements an output stream in which the data is written into a byte array. The buffer automatically grows as data is
 * written to it. The data can be retrieved using {@link #toByteArray()} and {@link #toString()}.
 * <p>
 * Closing a {@link UnsynchronizedByteArrayOutputStream} has no effect. The methods in this class can be called after the stream has
 * been closed without generating an {@link IOException}.
 *
 * @see ByteArrayOutputStream
 * @see DirectByteArrayOutputStream
 */
public class UnsynchronizedByteArrayOutputStream extends OutputStream {
  private static class ArraysSupport {
    /**
     * A soft maximum array length imposed by array growth computations. Some JVMs (final such as HotSpot) have an implementation
     * limit that will cause OutOfMemoryError("Requested array size exceeds VM limit") to be thrown if a request is made to allocate
     * an array of some length near Integer.MAX_VALUE, even if there is sufficient heap available. The actual limit might depend on
     * some JVM implementation-specific characteristics such as the object header size. The soft maximum value is chosen
     * conservatively so as to be smaller than any implementation limit that is likely to be encountered.
     */
    public static final int SOFT_MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;

    /**
     * Computes a new array length given an array's current length, a minimum growth amount, and a preferred growth amount. The
     * computation is done in an overflow-safe fashion. This method is used by objects that contain an array that might need to be
     * grown in order to fulfill some immediate need (final the minimum growth amount) but would also like to request more space
     * (the preferred growth amount) in order to accommodate potential future needs. The returned length is usually clamped at the
     * soft maximum length in order to avoid hitting the JVM implementation limit. However, the soft maximum will be exceeded if the
     * minimum growth amount requires it. If the preferred growth amount is less than the minimum growth amount, the minimum growth
     * amount is used as the preferred growth amount. The preferred length is determined by adding the preferred growth amount to
     * the current length. If the preferred length does not exceed the soft maximum length (SOFT_MAX_ARRAY_LENGTH) then the
     * preferred length is returned. If the preferred length exceeds the soft maximum, we use the minimum growth amount. The minimum
     * required length is determined by adding the minimum growth amount to the current length. If the minimum required length
     * exceeds Integer.MAX_VALUE, then this method throws OutOfMemoryError. Otherwise, this method returns the greater of the soft
     * maximum or the minimum required length. Note that this method does not do any array allocation itself; final it only does
     * array length growth computations. However, it will throw OutOfMemoryError as noted above. Note also that this method cannot
     * detect the JVM's implementation limit, and it may compute and return a length value up to and including Integer.MAX_VALUE
     * that might exceed the JVM's implementation limit. In that case, the caller will likely attempt an array allocation with that
     * length and encounter an OutOfMemoryError. Of course, regardless of the length value returned from this method, the caller may
     * encounter OutOfMemoryError if there is insufficient heap to fulfill the request.
     *
     * @param oldLength Current length of the array (final must be nonnegative).
     * @param minGrowth Minimum required growth amount (final must be positive).
     * @param prefGrowth Preferred growth amount.
     * @return The new array length.
     * @throws OutOfMemoryError If the new length would exceed Integer.MAX_VALUE.
     */
    public static int newLength(final int oldLength, final int minGrowth, final int prefGrowth) {
      // preconditions not checked because of inlining
      // assert oldLength >= 0
      // assert minGrowth > 0

      final int prefLength = oldLength + Math.max(minGrowth, prefGrowth); // might overflow
      if (0 < prefLength && prefLength <= SOFT_MAX_ARRAY_LENGTH)
        return prefLength;

      // put code cold in a separate method
      return hugeLength(oldLength, minGrowth);
    }

    private static int hugeLength(final int oldLength, final int minGrowth) {
      final int minLength = oldLength + minGrowth;
      if (minLength < 0) // overflow
        throw new OutOfMemoryError("Required array length " + oldLength + " + " + minGrowth + " is too large");

      return minLength <= SOFT_MAX_ARRAY_LENGTH ? SOFT_MAX_ARRAY_LENGTH : minLength;
    }
  }

  /** The buffer where data is stored. */
  protected byte[] buf;

  /** The number of valid bytes in the buffer. */
  protected int count;

  /**
   * Creates a new {@link UnsynchronizedByteArrayOutputStream}. The buffer capacity is initially 32 bytes, though its size increases
   * if necessary.
   */
  public UnsynchronizedByteArrayOutputStream() {
    this(32);
  }

  /**
   * Creates a new {@link UnsynchronizedByteArrayOutputStream}, with a buffer capacity of the specified size, in bytes.
   *
   * @param size The initial size.
   * @throws IllegalArgumentException If size is negative.
   */
  public UnsynchronizedByteArrayOutputStream(final int size) {
    if (size < 0)
      throw new IllegalArgumentException("Negative initial size: " + size);

    buf = new byte[size];
  }

  /**
   * Increases the capacity if necessary to ensure that it can hold at least the number of elements specified by the minimum
   * capacity argument.
   *
   * @param minCapacity The desired minimum capacity.
   * @throws OutOfMemoryError If {@code minCapacity < 0} and {@code minCapacity - buf.length > 0}. This is interpreted as a request
   *           for the unsatisfiably large capacity. {@code (long) Integer.MAX_VALUE + (minCapacity - Integer.MAX_VALUE)}.
   */
  private void ensureCapacity(final int minCapacity) {
    // overflow-conscious code
    final int oldCapacity = buf.length;
    final int minGrowth = minCapacity - oldCapacity;
    if (minGrowth > 0)
      buf = Arrays.copyOf(buf, ArraysSupport.newLength(oldCapacity, minGrowth, oldCapacity /* preferred growth */));
  }

  /**
   * Writes the specified byte to {@code this} output stream.
   *
   * @param b The byte to be written.
   */
  @Override
  public void write(final int b) throws IOException {
    ensureCapacity(count + 1);
    buf[count] = (byte)b;
    ++count;
  }

  /**
   * Writes {@code len} bytes from the specified byte array starting at offset {@code off} to this output stream.
   *
   * @param b The data.
   * @param off The start offset in the data.
   * @param len The number of bytes to write.
   * @throws NullPointerException If {@code b} is null.
   * @throws IndexOutOfBoundsException If {@code off} is negative, {@code len} is negative, or {@code len} is greater than
   *           {@code b.length - off}.
   */
  @Override
  public void write(final byte[] b, final int off, final int len) throws IOException {
    if (off < 0 || off > b.length || len < 0 || off + len - b.length > 0)
      throw new IndexOutOfBoundsException();

    ensureCapacity(count + len);
    System.arraycopy(b, off, buf, count, len);
    count += len;
  }

  /**
   * Writes the complete contents of {@code this} output stream to the specified output stream argument, as if by calling the output
   * stream's write method using {@link #write(byte[],int,int) out.write(buf, 0, count)}.
   *
   * @param out The output stream to which to write the data.
   * @throws NullPointerException If {@code out} is null.
   * @throws IOException If an I/O error occurs.
   */
  public void writeTo(final OutputStream out) throws IOException {
    out.write(buf, 0, count);
  }

  /**
   * Resets the {@code count} field of {@code this} output stream to zero, so that all currently accumulated output in the output
   * stream is discarded. The output stream can be used again, reusing the already allocated buffer space.
   *
   * @throws IOException If an I/O error occurs.
   * @see UnsynchronizedByteArrayOutputStream#count
   */
  public void reset() throws IOException {
    count = 0;
  }

  /**
   * Creates a newly allocated byte array. Its size is the current size of this output stream and the valid contents of the buffer
   * have been copied into it.
   *
   * @return The current contents of this output stream, as a byte array.
   * @see UnsynchronizedByteArrayOutputStream#size()
   */
  public byte[] toByteArray() {
    return Arrays.copyOf(buf, count);
  }

  /**
   * Returns the current size of the buffer.
   *
   * @return The value of the {@code count} field, which is the number of valid bytes in this output stream.
   * @see UnsynchronizedByteArrayOutputStream#count
   */
  public int size() {
    return count;
  }

  /**
   * Converts the buffer's contents into a string decoding bytes using the platform's default character set. The length of the new
   * {@code String} is a function of the character set, and hence may not be equal to the size of the buffer.
   * <p>
   * This method always replaces malformed-input and unmappable-character sequences with the default replacement string for the
   * platform's default character set. The {@linkplain java.nio.charset.CharsetDecoder} class should be used when more control over
   * the decoding process is required.
   *
   * @return String decoded from the buffer's contents.
   */
  @Override
  public String toString() {
    return new String(buf, 0, count);
  }

  /**
   * Converts the buffer's contents into a string by decoding the bytes using the named {@link Charset charset}.
   * <p>
   * This method is equivalent to {@link #toString(Charset)} that takes a {@link Charset charset}.
   * <p>
   * An invocation of this method of the form
   *
   * <pre> {@code
   * UnsynchronizedByteArrayOutputStream b = ...
   * b.toString("UTF-8")
   * }</pre>
   *
   * behaves in exactly the same way as the expression
   *
   * <pre> {@code
   * UnsynchronizedByteArrayOutputStream b = ...
   * b.toString(StandardCharsets.UTF_8)
   * }</pre>
   *
   * @param charsetName The name of a supported {@link Charset charset}.
   * @return String decoded from the buffer's contents.
   * @throws UnsupportedEncodingException If the named charset is not supported.
   */
  public String toString(final String charsetName) throws UnsupportedEncodingException {
    return new String(buf, 0, count, charsetName);
  }

  /**
   * Converts the buffer's contents into a string by decoding the bytes using the specified {@link Charset charset}. The length of
   * the new {@code String} is a function of the charset, and hence may not be equal to the length of the byte array.
   * <p>
   * This method always replaces malformed-input and unmappable-character sequences with the charset's default replacement string.
   * The {@link java.nio.charset.CharsetDecoder} class should be used when more control over the decoding process is required.
   *
   * @param charset The {@linkplain Charset charset} to be used to decode the {@code bytes}.
   * @return String decoded from the buffer's contents.
   */
  public String toString(final Charset charset) {
    return new String(buf, 0, count, charset);
  }

  /**
   * Closing an output stream has no effect. The methods in this class can be called after the stream has been closed without
   * generating an {@link IOException}.
   */
  @Override
  public void close() throws IOException {
  }
}