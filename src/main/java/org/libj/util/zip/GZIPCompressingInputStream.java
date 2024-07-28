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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;

/**
 * Compresses an {@link InputStream} in a memory-optimal, on-demand way only compressing enough to fill a buffer.
 */
public class GZIPCompressingInputStream extends InputStream {
  private final InputStream in;
  private final DeflaterOutputStream out;
  private final byte[] readBuf = new byte[8192];

  private byte[] buf = new byte[8192];
  private int read = 0;
  private int write = 0;

  public GZIPCompressingInputStream(final InputStream in) {
    this.in = in;
    this.out = new UnsynchronizedGZIPOutputStream(new OutputStream() {
      private void ensureCapacity(final int len) {
        final int length = buf.length;
        if (write + len >= length) {
          final byte[] newbuf = new byte[(length + len) * 2];
          System.arraycopy(buf, 0, newbuf, 0, length);
          buf = newbuf;
        }
      }

      @Override
      public void write(final byte[] b, final int off, final int len) throws IOException {
        ensureCapacity(len);
        System.arraycopy(b, off, buf, write, len);
        write += len;
      }

      @Override
      public void write(final int b) throws IOException {
        ensureCapacity(1);
        buf[write++] = (byte)b;
      }
    });
  }

  private void compressStream() throws IOException {
    // If the reader has caught up with the writer, then zero the positions out.
    if (read == write) {
      read = 0;
      write = 0;
    }

    while (write == 0) {
      // Feed the gzip stream data until it spits out a block.
      final int val = in.read(readBuf);
      if (val > 0) {
        out.write(readBuf, 0, val);
      }
      else if (val == -1) {
        // Nothing left to do, we've hit the end of the stream, so close and break out.
        out.close();
        break;
      }
    }
  }

  @Override
  public int read(final byte[] b, final int off, final int len) throws IOException {
    compressStream();
    final int noBytes = Math.min(len, write - read);
    if (noBytes > 0) {
      System.arraycopy(buf, read, b, off, noBytes);
      read += noBytes;
    }
    else if (len > 0) {
      // If bytes were requested, but we have none, then we're at the end of the stream.
      return -1;
    }

    return noBytes;
  }

  @Override
  public int read() throws IOException {
    compressStream();
    return write == 0 ? -1 : buf[read++] & 0xFF;
  }
}