/* Copyright (c) 2006 lib4j
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

package org.lib4j.util.zip;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class Zips {
  private static final int WRITE_BUFFER_SIZE = 2048;
  private static final int READ_BUFFER_SIZE = 65536;

  public static void unzip(final File zipFile, final File targetDir) throws FileNotFoundException, IOException {
    Zips.unzip(zipFile, targetDir, null);
  }

  public static void unzip(final File zipFile, final File targetDir, final FileFilter filter) throws FileNotFoundException, IOException {
    if (zipFile == null)
      throw new NullPointerException("zipFile == null");

    if (targetDir == null)
      throw new NullPointerException("targetDir == null");

    targetDir.mkdirs();

    // read jar-file:
    try (final ZipFile zip = new ZipFile(zipFile)) {
      final String targetPath = targetDir.getAbsolutePath() + File.separator;
      final byte[] buffer = new byte[READ_BUFFER_SIZE];
      final Enumeration<? extends ZipEntry> enumeration = zip.entries();
      while (enumeration.hasMoreElements()) {
        final ZipEntry entry = enumeration.nextElement();
        if (entry.isDirectory())
          continue;

        // do not copy anything from the package cache:
        if (entry.getName().indexOf("package cache") != -1)
          continue;

        final File file = new File(targetPath + entry.getName());
        if (filter != null && !filter.accept(file))
          continue;

        if (!file.getParentFile().exists())
          file.getParentFile().mkdirs();

        try (
          final FileOutputStream out = new FileOutputStream(file);
          final InputStream in = zip.getInputStream(entry);
        ) {
          int read;
          while ((read = in.read(buffer)) != -1)
            out.write(buffer, 0, read);
        }
      }
    }
  }

  public static String gunzip(final InputStream inputStream) throws IOException {
    if (inputStream == null)
      throw new NullPointerException("inputStream == null");

    try (final GZIPInputStream zipInputStream = new GZIPInputStream(new BufferedInputStream(inputStream))) {
      final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

      int length;
      final byte[] buffer = new byte[READ_BUFFER_SIZE];
      while ((length = zipInputStream.read(buffer, 0, READ_BUFFER_SIZE)) != -1)
        outputStream.write(buffer, 0, length);

      return new String(outputStream.toByteArray());
    }
  }

  public static String gunzip(final byte[] bytes) throws IOException {
    if (bytes == null)
      throw new NullPointerException("bytes == null");

    return Zips.gunzip(new ByteArrayInputStream(bytes));
  }

  public static boolean add(final File zipFile, final Collection<CachedFile> files) throws IOException {
    if (zipFile == null)
      throw new NullPointerException("zipFile == null");

    if (files == null)
      throw new NullPointerException("files == null");

    if (files.size() == 0)
      return false;

    // get a temp file
    final File tempFile = File.createTempFile(zipFile.getName(), null);
    // delete it, otherwise you cannot rename your existing zip to it.
    tempFile.delete();

    final File file = new File(zipFile.getName());
    if (!file.renameTo(tempFile))
      throw new IOException("could not rename the file " + file.getAbsolutePath() + " to " + tempFile.getAbsolutePath());

    final byte[] bytes = new byte[WRITE_BUFFER_SIZE];

    try (
      final ZipInputStream in = new ZipInputStream(new FileInputStream(tempFile));
      final ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
    ) {
      ZipEntry entry;
      WHILE:
      while ((entry = in.getNextEntry()) != null) {
        final String name = entry.getName();
        for (final CachedFile fileWrapper : files)
          if (fileWrapper.getPath().equals(name))
            break WHILE;

        // Add ZIP entry to output stream.
        out.putNextEntry(new ZipEntry(name));
        // Transfer bytes from the ZIP file to the output file
        int length;
        while ((length = in.read(bytes)) > 0)
          out.write(bytes, 0, length);
      }

      // Compress the files
      for (final CachedFile cachedFile : files) {
        // Add ZIP entry to output stream.
        out.putNextEntry(new ZipEntry(cachedFile.getPath()));
        // Transfer bytes from the file to the ZIP file
        out.write(cachedFile.getBytes());
        // Complete the entry
        out.closeEntry();
      }
    }

    tempFile.delete();
    return true;
  }

  private Zips() {
  }
}