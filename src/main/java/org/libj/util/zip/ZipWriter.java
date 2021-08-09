/* Copyright (c) 2006 LibJ
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import org.libj.lang.Assertions;

/**
 * Writes ZIP content.
 */
public class ZipWriter implements AutoCloseable {
  private final ZipOutputStream out;

  /**
   * Creates a new {@link ZipWriter} with the specified {@link ZipOutputStream}.
   *
   * @param out The {@link ZipOutputStream}.
   * @throws IllegalArgumentException If {@code out} is null.
   */
  public ZipWriter(final ZipOutputStream out) {
    this.out = Assertions.assertNotNull(out);
  }

  /**
   * Write a ZIP file entry with the specified {@code name} and {@code bytes}.
   *
   * @param name The name of the ZIP file entry.
   * @param bytes The content {@code byte} array.
   * @throws IOException If an I/O error has occurred.
   * @throws ZipException If a ZIP error has occurred.
   * @throws IllegalArgumentException If {@code name} or {@code bytes} is null.
   */
  public void write(final String name, final byte[] bytes) throws IOException, ZipException {
    final ZipEntry entry = new ZipEntry(Assertions.assertNotNull(name));
    entry.setTime(System.currentTimeMillis());
    out.putNextEntry(entry);
    out.write(Assertions.assertNotNull(bytes));
  }

  /**
   * Closes the writer.
   *
   * @throws IOException If an I/O error has occurred.
   */
  @Override
  public void close() throws IOException {
    out.close();
  }
}