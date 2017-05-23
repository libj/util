/* Copyright (c) 2014 lib4j
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * An efficient stream searching class based on the Knuth-Morris-Pratt
 * algorithm. For more on the algorithm works see:
 * http://www.inf.fh-flensburg.de/lang/algorithmen/pattern/kmpen.htm.
 */
public final class StreamSearcher {
  public static class Char {
    protected final char[][] pattern;
    protected final int[][] borders;

    public Char(final char[] ... pattern) {
      this.pattern = pattern;
      this.borders = new int[pattern.length][pattern[0].length + 1];
      for (int p = 0; p < pattern.length; p++) {
        int i = 0;
        int j = -1;
        borders[p][i] = j;
        while (i < pattern[0].length) {
          while (j >= 0 && pattern[p][i] != pattern[p][j])
            j = borders[p][j];

          borders[p][++i] = ++j;
        }
      }
    }

    /**
     * Searches for the next occurrence of the pattern in the stream, starting
     * from the current stream position. Note that the position of the stream is
     * changed. If a match is found, the stream points to the end of the match --
     * i.e. the byte AFTER the pattern. Else, the stream is entirely consumed.
     * The latter is because InputStream semantics make it difficult to have
     * another reasonable default, i.e. leave the stream unchanged.
     *
     * @return number of bytes the stream is advanced
     * @throws IOException
     */
    public int search(final InputStreamReader stream, final char[] buffer, final int offset) throws IOException {
      int i = 0;
      int b = 0;
      final int[] j = new int[pattern.length];

      while ((b = stream.read()) != -1) {
        buffer[offset + i++] = (char)b;
        for (int p = 0; p < pattern.length; p++) {
          while (j[p] >= 0 && (char)b != pattern[p][j[p]])
            j[p] = borders[p][j[p]];

          // Move to the next character in the pattern.
          ++j[p];

          // If we've matched up to the full pattern length, we found it. Return,
          // which will automatically save our position in the InputStream at the
          // point immediately following the pattern match.
          if (j[p] == pattern[p].length)
            return i;
        }
      }

      // Not found, return false. Note that the stream is now completely consumed.
      return i;
    }
  }

  public static class Byte {
    protected final byte[][] pattern;
    protected final int[][] borders;

    public Byte(final byte[] ... pattern) {
      this.pattern = pattern;
      this.borders = new int[pattern.length][pattern[0].length + 1];
      for (int p = 0; p < pattern.length; p++) {
        int i = 0;
        int j = -1;
        borders[p][i] = j;
        while (i < pattern[0].length) {
          while (j >= 0 && pattern[p][i] != pattern[p][j])
            j = borders[p][j];

          borders[p][++i] = ++j;
        }
      }
    }

    /**
     * Searches for the next occurrence of the pattern in the stream, starting
     * from the current stream position. Note that the position of the stream is
     * changed. If a match is found, the stream points to the end of the match --
     * i.e. the byte AFTER the pattern. Else, the stream is entirely consumed.
     * The latter is because InputStream semantics make it difficult to have
     * another reasonable default, i.e. leave the stream unchanged.
     *
     * @return number of bytes the stream is advanced
     * @throws IOException
     */
    public int search(final InputStream stream, final byte[] buffer, final int offset) throws IOException {
      int i = 0;
      int b = 0;
      final int[] j = new int[pattern.length];

      while ((b = stream.read()) != -1) {
        buffer[offset + i++] = (byte)b;
        for (int p = 0; p < pattern.length; p++) {
          while (j[p] >= 0 && (byte)b != pattern[p][j[p]])
            j[p] = borders[p][j[p]];

          // Move to the next character in the pattern.
          ++j[p];

          // If we've matched up to the full pattern length, we found it. Return,
          // which will automatically save our position in the InputStream at the
          // point immediately following the pattern match.
          if (j[p] == pattern[p].length)
            return i;
        }
      }

      // Not found, return false. Note that the stream is now completely consumed.
      return i;
    }
  }

  private StreamSearcher() {
  }
}