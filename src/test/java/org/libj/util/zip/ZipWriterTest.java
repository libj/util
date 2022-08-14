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

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.junit.Test;

public class ZipWriterTest {
  private static void assertFile(final File destDir, final String fileName, final String data) throws IOException {
    final File file = new File(destDir, fileName);
    assertTrue(file.exists());
    final String actual = new String(Files.readAllBytes(file.toPath()));
    assertEquals(data, actual);
  }

  @Test
  public void test() throws IOException {
    final File file = Files.createTempFile("test", ".jar").toFile();
    file.deleteOnExit();

    try (final ZipWriter writer = new ZipWriter(new JarOutputStream(new FileOutputStream(file)))) {
      writer.write("foo", "foo".getBytes());
      writer.write("bar", "bar".getBytes());
      try {
        writer.write("bar", "foobar".getBytes());
        fail("Expected ZipException");
      }
      catch (final ZipException e) {
      }

      writer.write("dfoo/foo", "foo/foo".getBytes());
      writer.write("dbar/bar", "dbar/dbar/bar".getBytes());
    }

    final File destDir = Files.createTempDirectory("jar-test").toFile();
    Runtime.getRuntime().addShutdownHook(new Thread(() -> deleteDir(destDir)));

    ZipFiles.extract(new ZipFile(file), destDir);
    assertFile(destDir, "foo", "foo");
    assertFile(destDir, "bar", "bar");
    assertFile(destDir, "dfoo/foo", "foo/foo");
    assertFile(destDir, "dbar/bar", "dbar/dbar/bar");
  }

  /**
   * Recursively delete a directory and its contents.
   *
   * @param dir The directory to delete.
   * @return {@code true} if {@code dir} was successfully deleted, {@code false} otherwise.
   */
  private static boolean deleteDir(final File dir) {
    final File[] files = dir.listFiles();
    if (files != null)
      for (final File file : files) // [A]
        deleteDir(file);

    return dir.delete();
  }
}