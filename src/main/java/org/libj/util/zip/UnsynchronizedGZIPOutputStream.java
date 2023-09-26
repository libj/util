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

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 * An unsynchronized version of {@link java.util.zip.GZIPOutputStream} that writes the GZIP header upon the invocation
 * {@link #write(int)}, {@link #write(byte[])}, or {@link #write(byte[],int,int)} methods instead upon instantiation as is done in
 * {@link java.util.zip.GZIPOutputStream#GZIPOutputStream(OutputStream,int,boolean)}.
 * <p>
 * This class implements a stream filter for writing compressed data in the GZIP file format.
 *
 * @implNote This class relies on the synchronized {@link DeflaterOutputStream}.
 */
public class UnsynchronizedGZIPOutputStream extends DeflaterOutputStream {
  /** RC-32 of uncompressed data. */
  protected final CRC32 crc = new CRC32();

  /* GZIP header magic number. */
  private static final int GZIP_MAGIC = 0x8b1f;

  /* Trailer size in bytes. */
  private static final int TRAILER_SIZE = 8;

  // Represents the default "unknown" value for OS header, per RFC-1952
  private static final byte OS_UNKNOWN = System.getProperty("java.version").charAt(1) >= 7 ? (byte)0 : (byte)255;

  /* Indicates that the stream has been closed. */
  private boolean closed = false;

  private boolean hasHeader;
  private boolean usesDefaultDeflater = false;

  /**
   * Creates a new output stream with the specified buffer size.
   * <p>
   * The new output stream instance is created as if by invoking the 3-argument constructor
   * {@link #UnsynchronizedGZIPOutputStream(OutputStream,int,boolean) UnsynchronizedGZIPOutputStream(out,size,false)}.
   *
   * @param out The output stream.
   * @param size The output buffer size.
   * @throws IllegalArgumentException If {@code size <= 0}.
   */
  public UnsynchronizedGZIPOutputStream(final OutputStream out, final int size) {
    this(out, size, false);
  }

  /**
   * Creates a new output stream with the specified buffer size and flush mode.
   *
   * @param out The output stream.
   * @param size The output buffer size.
   * @param syncFlush If {@code true} invocation of the inherited {@link DeflaterOutputStream#flush() flush()} method of this instance
   *          flushes the compressor with flush mode {@link Deflater#SYNC_FLUSH} before flushing the output stream, otherwise only
   *          flushes the output stream.
   * @throws IllegalArgumentException If {@code size <= 0}
   */
  public UnsynchronizedGZIPOutputStream(final OutputStream out, final int size, final boolean syncFlush) {
    super(out, out != null ? new Deflater(Deflater.DEFAULT_COMPRESSION, true) : null, size, syncFlush);
    usesDefaultDeflater = true;
  }

  /**
   * Creates a new output stream with a default buffer size.
   * <p>
   * The new output stream instance is created as if by invoking the 2-argument constructor
   * {@link #UnsynchronizedGZIPOutputStream(OutputStream,boolean) UnsynchronizedGZIPOutputStream(out,false)}.
   *
   * @param out The output stream.
   */
  public UnsynchronizedGZIPOutputStream(final OutputStream out) {
    this(out, 512, false);
  }

  /**
   * Creates a new output stream with a default buffer size and the specified flush mode.
   *
   * @param out the output stream
   * @param syncFlush if {@code true} invocation of the inherited {@link DeflaterOutputStream#flush() flush()} method of this instance
   *          flushes the compressor with flush mode {@link Deflater#SYNC_FLUSH} before flushing the output stream, otherwise only
   *          flushes the output stream.
   * @throws IOException If an I/O error has occurred.
   */
  public UnsynchronizedGZIPOutputStream(final OutputStream out, final boolean syncFlush) throws IOException {
    this(out, 512, syncFlush);
  }

  // FIXME: Rewrite this without the use of an array.
  private final byte[] buf = new byte[1];

  /**
   * Writes a byte to the compressed output stream. <s>This method will block until all the bytes are written.</s>
   *
   * @param b The byte to be written.
   * @throws IOException If an I/O error has occurred.
   */
  @Override
  public void write(final int b) throws IOException {
    buf[0] = (byte)(b & 0xff);
    write(buf, 0, 1);
  }

  /**
   * Writes array of bytes to the compressed output stream. <s>This method will block until all the bytes are written.</s>
   *
   * @param buf The data to be written.
   * @param off The start offset of the data.
   * @param len The length of the data.
   * @throws IOException If an I/O error has occurred.
   */
  @Override
  public void write(final byte[] buf, final int off, final int len) throws IOException {
    if (!hasHeader) {
      hasHeader = true;
      writeHeader();
      crc.reset();
    }

    super.write(buf, off, len);
    crc.update(buf, off, len);
  }

  /**
   * Finishes writing compressed data to the output stream without closing the underlying stream. Use this method when applying
   * multiple filters in succession to the same output stream.
   *
   * @throws IOException If an I/O error has occurred.
   */
  @Override
  public void finish() throws IOException {
    final Deflater def = this.def;
    if (!def.finished()) {
      final OutputStream out = this.out;
      try {
        def.finish();
        final byte[] buf = this.buf;
        while (!def.finished()) {
          int len = def.deflate(buf, 0, 1);
          if (def.finished() && len <= 1 - TRAILER_SIZE) {
            // last deflater buffer. Fit trailer at the end
            writeTrailer(buf, len);
            len += TRAILER_SIZE;
            out.write(buf, 0, len);
            return;
          }

          if (len > 0)
            out.write(buf, 0, len);
        }

        // if we can't fit the trailer at the end of the last
        // deflater buffer, we write it separately
        final byte[] trailer = new byte[TRAILER_SIZE];
        writeTrailer(trailer, 0);
        out.write(trailer);
      }
      catch (final IOException e) {
        if (usesDefaultDeflater)
          def.end();

        throw e;
      }
    }
  }

  /*
   * Writes GZIP member header.
   */
  private void writeHeader() throws IOException {
    out.write((byte)GZIP_MAGIC);        // Magic number (short)
    out.write((byte)(GZIP_MAGIC >> 8)); // Magic number (short)
    out.write(Deflater.DEFLATED);       // Compression method (CM)
    out.write(0);                       // Flags (FLG)
    out.write(0);                       // Modification time MTIME (int)
    out.write(0);                       // Modification time MTIME (int)
    out.write(0);                       // Modification time MTIME (int)
    out.write(0);                       // Modification time MTIME (int)
    out.write(0);                       // Extra flags (XFLG)
    out.write(OS_UNKNOWN);              // Operating system (OS)
  }

  /*
   * Writes GZIP member trailer to a byte array, starting at a given offset.
   */
  private void writeTrailer(final byte[] buf, final int offset) {
    writeInt((int)crc.getValue(), buf, offset); // CRC-32 of uncompr. data
    writeInt(def.getTotalIn(), buf, offset + 4); // Number of uncompr. bytes
  }

  /*
   * Writes integer in Intel byte order to a byte array, starting at a given offset.
   */
  private static void writeInt(final int i, final byte[] buf, final int offset) {
    writeShort(i & 0xffff, buf, offset);
    writeShort((i >> 16) & 0xffff, buf, offset + 2);
  }

  /*
   * Writes short integer in Intel byte order to a byte array, starting at a given offset
   */
  private static void writeShort(final int s, final byte[] buf, final int offset) {
    buf[offset] = (byte)(s & 0xff);
    buf[offset + 1] = (byte)((s >> 8) & 0xff);
  }

  /**
   * Writes remaining compressed data to the output stream and closes the underlying stream.
   *
   * @throws IOException If an I/O error has occurred.
   */
  @Override
  public void close() throws IOException {
    if (!closed) {
      finish();
      if (usesDefaultDeflater)
        def.end();

      out.close();
      closed = true;
    }
  }
}