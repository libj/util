/* Copyright (c) 2014 LibJ
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

package org.libj.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.libj.lang.Assertions;

/**
 * An efficient stream searching class based on the Knuth-Morris-Pratt
 * algorithm.
 *
 * @see <a href=
 *      "http://www.inf.fh-flensburg.de/lang/algorithmen/pattern/kmpen.htm">Knuth-Morris-Pratt
 *      algorithm</a>
 */
public final class StreamSearcher {
  /**
   * The Knuth-Morris-Pratt algorithm applied to {@code char} streams.
   */
  public static class Char {
    protected final char[][] patterns;
    protected final int[][] borders;

    /**
     * Creates a new {@link Char} instance with the specified {@code char[]}
     * vararg array representing the search patterns.
     *
     * @param patterns The vararg array representing the search patterns.
     */
    public Char(final char[] ... patterns) {
      this.patterns = patterns;
      this.borders = new int[patterns.length][patterns[0].length + 1];
      for (int p = 0; p < patterns.length; ++p) {
        int i = 0;
        int j = -1;
        borders[p][i] = j;
        while (i < patterns[0].length) {
          while (j >= 0 && patterns[p][i] != patterns[p][j])
            j = borders[p][j];

          borders[p][++i] = ++j;
        }
      }
    }

    /**
     * Searches for the next occurrence of the pattern in the stream, starting
     * from the current stream position. Note that the position of the stream is
     * changed. If a match is found, the stream points to the end of the match
     * -- i.e. the byte AFTER the pattern. Else, the stream is entirely
     * consumed. The latter is because {@link Reader} semantics make it
     * difficult to have another reasonable default, i.e. leave the stream
     * unchanged.
     *
     * @param in The {@link Reader}.
     * @return Number of bytes the stream is advanced.
     * @throws IOException If an I/O error has occurred.
     */
    public int search(final Reader in) throws IOException {
      return search(in, null, -1);
    }

    /**
     * Searches for the next occurrence of the pattern in the stream, starting
     * from the current stream position. Note that the position of the stream is
     * changed. If a match is found, the stream points to the end of the match
     * -- i.e. the byte AFTER the pattern. Else, the stream is entirely
     * consumed. The latter is because {@link Reader} semantics make it
     * difficult to have another reasonable default, i.e. leave the stream
     * unchanged.
     * <p>
     * If {@code buffer} is not null, each {@code char} read from the specified
     * {@link Reader} is set into the provided {@code buffer}, starting at the
     * given {@code offset}.
     *
     * @param in The {@link Reader}.
     * @param buffer Buffer into which read bytes are written.
     * @param offset Offset in buffer where bytes are written.
     * @return Number of bytes the stream is advanced.
     * @throws IOException If an I/O error has occurred.
     * @throws IllegalArgumentException If the given {@code offset} is out of
     *           range.
     * @throws NullPointerException If {@code in} is null.
     */
    public int search(final Reader in, final char[] buffer, final int offset) throws IOException {
      if (buffer != null)
        Assertions.assertRangeArray(offset, buffer.length);

      final int[] j = new int[patterns.length];
      int i = 0;
      for (int b; (b = in.read()) != -1;) {
        if (buffer != null)
          buffer[offset + i] = (char)b;

        ++i;
        for (int p = 0; p < patterns.length; ++p) {
          while (j[p] >= 0 && (char)b != patterns[p][j[p]])
            j[p] = borders[p][j[p]];

          // Move to the next character in the pattern.
          ++j[p];

          // If we've matched up to the full pattern length, we found it. Return,
          // which will automatically save our position in the InputStream at the
          // point immediately following the pattern match.
          if (j[p] == patterns[p].length)
            return i;
        }
      }

      // Not found, return false. Note that the stream is now completely consumed.
      return i;
    }
  }

  /**
   * The Knuth-Morris-Pratt algorithm applied to {@code byte} streams.
   */
  public static class Byte {
    protected final byte[][] patterns;
    protected final int[][] borders;

    /**
     * Creates a new {@link Byte} instance with the specified {@code byte[]}
     * vararg array representing the search patterns.
     *
     * @param patterns The vararg array representing the search patterns.
     */
    public Byte(final byte[] ... patterns) {
      this.patterns = patterns;
      this.borders = new int[patterns.length][patterns[0].length + 1];
      for (int p = 0; p < patterns.length; ++p) {
        int i = 0;
        int j = -1;
        borders[p][i] = j;
        while (i < patterns[0].length) {
          while (j >= 0 && patterns[p][i] != patterns[p][j])
            j = borders[p][j];

          borders[p][++i] = ++j;
        }
      }
    }

    /**
     * Searches for the next occurrence of the pattern in the stream, starting
     * from the current stream position. Note that the position of the stream is
     * changed. If a match is found, the stream points to the end of the match
     * -- i.e. the byte AFTER the pattern. Else, the stream is entirely
     * consumed. The latter is because {@link InputStream} semantics make it
     * difficult to have another reasonable default, i.e. leave the stream
     * unchanged.
     *
     * @param in The {@link Reader}.
     * @return Number of bytes the stream is advanced.
     * @throws IOException If an I/O error has occurred.
     */
    public int search(final InputStream in) throws IOException {
      return search(in, null, -1);
    }

    /**
     * Searches for the next occurrence of the pattern in the stream, starting
     * from the current stream position. Note that the position of the stream is
     * changed. If a match is found, the stream points to the end of the match
     * -- i.e. the byte AFTER the pattern. Else, the stream is entirely
     * consumed. The latter is because {@link InputStream} semantics make it
     * difficult to have another reasonable default, i.e. leave the stream
     * unchanged.
     *
     * @param in The {@link InputStream}.
     * @param buffer Buffer into which read bytes are written.
     * @param offset Offset in buffer where bytes are written.
     * @return Number of bytes the stream is advanced.
     * @throws IOException If an I/O error has occurred.
     */
    public int search(final InputStream in, final byte[] buffer, final int offset) throws IOException {
      if (buffer != null)
        Assertions.assertRangeArray(offset, buffer.length);

      final int[] j = new int[patterns.length];
      int i = 0;
      for (int b; (b = in.read()) != -1;) {
        if (buffer != null)
          buffer[offset + i] = (byte)b;

        ++i;
        for (int p = 0; p < patterns.length; ++p) {
          while (j[p] >= 0 && (byte)b != patterns[p][j[p]])
            j[p] = borders[p][j[p]];

          // Move to the next character in the pattern.
          ++j[p];

          // If we've matched up to the full pattern length, we found it. Return,
          // which will automatically save our position in the InputStream at the
          // point immediately following the pattern match.
          if (j[p] == patterns[p].length)
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