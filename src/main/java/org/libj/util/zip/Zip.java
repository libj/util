/* Copyright (c) 2021 LibJ
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Utility enum pertaining to {@code java.util.zip} package.
 */
public enum Zip {
  ZIP {
    @Override
    public byte[] compress(final byte[] decompressed) throws IOException {
      try (
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(decompressed.length);
        final ZipOutputStream zos = new ZipOutputStream(baos);
      ) {
        zos.write(decompressed);
        return baos.toByteArray();
      }
    }

    @Override
    public byte[] decompress(final byte[] compressed) throws IOException {
      try (final InputStream in = new ZipInputStream(new ByteArrayInputStream(compressed))) {
        return Zip.decompress(in);
      }
    }
  },
  GZIP {
    @Override
    public byte[] compress(final byte[] decompressed) throws IOException {
      try (
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(decompressed.length);
        final GZIPOutputStream gzos = new GZIPOutputStream(baos);
      ) {
        gzos.write(decompressed);
        return baos.toByteArray();
      }
    }

    @Override
    public byte[] decompress(final byte[] compressed) throws IOException {
      try (final InputStream in = new GZIPInputStream(new ByteArrayInputStream(compressed))) {
        return Zip.decompress(in);
      }
    }
  };

  private static byte[] decompress(final InputStream in) throws IOException {
    final byte[] buffer = new byte[1024];
    try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      for (int len; (len = in.read(buffer)) > 0;)
        out.write(buffer, 0, len);

      return out.toByteArray();
    }
  }

  /**
   * Returns the compressed bytes from the provided {@code decompressed} bytes.
   *
   * @param decompressed The bytes to compress.
   * @return The decompressed bytes from the provided {@code decompressed} bytes.
   * @throws IOException If an I/O error has occurred.
   * @throws NullPointerException If {@code decompressed} is null.
   */
  public abstract byte[] compress(final byte[] decompressed) throws IOException;

  /**
   * Returns the decompressed bytes from the provided {@code compressed} bytes.
   *
   * @param compressed The bytes to decompress.
   * @return The decompressed bytes from the provided {@code compressed} bytes.
   * @throws IOException If an I/O error has occurred.
   * @throws NullPointerException If {@code compressed} is null.
   */
  public abstract byte[] decompress(final byte[] compressed) throws IOException;
}