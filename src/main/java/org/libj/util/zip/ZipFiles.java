/* Copyright (c) 2017 LibJ
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

import static org.libj.lang.Assertions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Utility functions pertaining to {@link ZipFile}.
 */
public final class ZipFiles {
  /**
   * Extract a {@code zipFile} to {@code destDir}.
   *
   * @param zipFile The {@link ZipFile}.
   * @param destDir The destination directory.
   * @throws IOException If an I/O error has occurred.
   * @throws IllegalArgumentException If {@code zipFile} or {@code destDir} is
   *           null.
   */
  public static void extract(final ZipFile zipFile, final File destDir) throws IOException {
    extract(zipFile, destDir, null);
  }

  /**
   * Extract a {@code zipFile} to {@code destDir}. Only entries that pass the
   * {@code predicate} test will be extracted.
   *
   * @param zipFile The {@link ZipFile}.
   * @param destDir The destination directory.
   * @param predicate The {@link Predicate} (can be null).
   * @throws IOException If an I/O error has occurred.
   * @throws IllegalArgumentException If {@code zipFile} or {@code destDir} is
   *           null.
   */
  public static void extract(final ZipFile zipFile, final File destDir, final Predicate<? super ZipEntry> predicate) throws IOException {
    assertNotNull(zipFile);
    assertNotNull(destDir);
    final Enumeration<? extends ZipEntry> entries = zipFile.entries();
    while (entries.hasMoreElements()) {
      final ZipEntry zipEntry = entries.nextElement();
      if (predicate != null && !predicate.test(zipEntry))
        continue;

      final File file = new File(destDir, zipEntry.getName());
      if (zipEntry.isDirectory()) {
        file.mkdirs();
        continue;
      }

      file.getParentFile().mkdirs();
      try (final InputStream in = zipFile.getInputStream(zipEntry); final FileOutputStream out = new FileOutputStream(file);) {
        for (int ch; (ch = in.read()) != -1; out.write(ch));
      }
    }
  }

  private ZipFiles() {
  }
}