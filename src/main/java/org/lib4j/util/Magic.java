/* Copyright (c) 2008 lib4j
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

package org.lib4j.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public final class Magic {
  private static final int[] validMagic = new int[] {0xca, 0xfe, 0xba, 0xbe};

  public static void main(final String[] args) throws IOException {
    for (int i = 0; i < args.length; i++)
      changeClassVersion(new File(args[i]), 47);
  }

  private static void changeClassVersion(final File file, final int version) throws IOException {
    final File inFile = file;
    final File tempFile = new File(file + ".tmp");
    try (
      final DataInputStream in = new DataInputStream(new FileInputStream(inFile));
      final DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
    ) {
      final int[] magic = new int[4];
      for (int i = 0; i < magic.length; i++) {
        magic[i] = in.read();
        if (validMagic[i] != magic[i]) {
          tempFile.deleteOnExit();
          throw new IllegalArgumentException(file.getName() + " is not a valid class!");
        }

        out.write(magic[i]);
      }

      final int[] minor = new int[2];
      for (int i = 0; i < minor.length; i++)
        minor[i] = in.read();

      out.write(0);
      out.write(0);

      final int[] major = new int[2];
      for (int i = 0; i < major.length; i++)
        major[i] = in.read();

      if ((major[0] | major[1]) == version) {
        in.close();
        out.close();
        tempFile.deleteOnExit();
        System.out.println(file.getName() + " is already version " + version);
        System.exit(1);
      }

      out.write(0);
      out.write(version);

      int ch = -1;
      while ((ch = in.read()) != -1)
        out.write(ch);
    }

    tempFile.renameTo(inFile);
  }
}