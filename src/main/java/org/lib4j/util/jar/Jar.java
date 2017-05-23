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

package org.lib4j.util.jar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public final class Jar {
  private final File jarFile;
  private final FileOutputStream stream;
  private final JarOutputStream out;
  private final Collection<JarEntry> entries = new HashSet<JarEntry>();

  public Jar(final File file) throws IOException {
    this.jarFile = file;
    if (!file.getParentFile().exists())
      file.getParentFile().mkdirs();

    this.stream = new FileOutputStream(file);
    this.out = new JarOutputStream(stream, new Manifest());
  }

  public File getFile() {
    return jarFile;
  }

  public void addEntry(final String fileName, final byte[] bytes) {
    synchronized (out) {
      // Add archive entry
      final JarEntry jarEntry = new JarEntry(fileName);
      entries.add(jarEntry);
      jarEntry.setTime(System.currentTimeMillis());

      try {
        out.putNextEntry(jarEntry);
        out.write(bytes);
      }
      catch (final IOException e) {
        if ("no current ZIP entry".equals(e.getMessage()) || "Stream closed".equals(e.getMessage()))
          return;
      }
    }
  }

  public Collection<JarEntry> getEntries() {
    return entries;
  }

  public void close() throws IOException {
    synchronized (out) {
      out.close();
      stream.close();
    }
  }
}