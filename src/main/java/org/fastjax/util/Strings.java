/* Copyright (c) 2006 FastJAX
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

package org.fastjax.util;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public final class Strings {
  private static final char[] alphaNumeric = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
  private static final SecureRandom secureRandom = new SecureRandom();

  private static String getRandom(final int length, final int start, final int len) {
    if (length < 0)
      throw new IllegalArgumentException("Length must be non-negative: " + length);

    if (length == 0)
      return "";

    final char[] array = new char[length];
    for (int i = 0; i < length; ++i)
      array[i] = alphaNumeric[start + secureRandom.nextInt(len)];

    return new String(array);
  }

  public static String getRandomAlphaNumeric(final int len) {
    return getRandom(len, 0, alphaNumeric.length);
  }

  public static String getRandomAlpha(final int len) {
    return getRandom(len, 0, alphaNumeric.length - 10);
  }

  public static String getRandomNumeric(final int len) {
    return getRandom(len, alphaNumeric.length - 10, 10);
  }

  private static boolean interpolateShallow(final StringBuilder text, final Map<String,String> properties, final String open, final String close) throws ParseException {
    boolean changed = false;
    for (int start = text.length() - close.length() - 1; (start = text.lastIndexOf(open, start - 1)) > -1;) {
      final int end = text.indexOf(close, start + open.length());
      if (end < 0)
        throw new ParseException(text.toString(), start);

      final String key = text.substring(start + open.length(), end);
      final String value = properties.get(key);
      if (value != null) {
        text.replace(start, end + close.length(), value);
        changed = true;
      }
    }

    return changed;
  }

  private static String interpolateDeep(final StringBuilder text, final Map<String,String> properties, final String open, final String close) throws ParseException {
    final int max = properties.size() * properties.size();
    for (int i = 0; interpolateShallow(text, properties, open, close); ++i)
      if (i == max)
        throw new IllegalArgumentException("Loop detected");

    return text.toString();
  }

  public static Map<String,String> interpolate(final Map<String,String> properties, final String open, final String close) throws ParseException {
    Objects.requireNonNull(properties);
    Objects.requireNonNull(open);
    Objects.requireNonNull(close);
    for (final Map.Entry<String,String> entry : properties.entrySet())
      if (entry.getValue() != null)
        entry.setValue(interpolateDeep(new StringBuilder(entry.getValue()), properties, open, close));

    return properties;
  }

  public static String interpolate(final String text, final Map<String,String> properties, final String open, final String close) throws ParseException {
    return interpolateDeep(new StringBuilder(Objects.requireNonNull(text)), Objects.requireNonNull(properties), Objects.requireNonNull(open), Objects.requireNonNull(close));
  }

  /**
   * Replaces each substring in the specified {@code StringBuilder} that matches
   * the literal target sequence with the specified literal replacement
   * sequence. The replacement proceeds from the beginning of the string to the
   * end, for example, replacing "aa" with "b" in the string "aaa" will result
   * in "ba" rather than "ab".
   *
   * @param builder The {@link StringBuilder}.
   * @param target The sequence of char values to be replaced
   * @param replacement The replacement sequence of char values
   * @return The resulting string
   * @throws NullPointerException If {@code builder}, {@code target}, or
   *           {@code replacement} are null.
   * @see String#replace(CharSequence, CharSequence)
   */
  public static StringBuilder replace(final StringBuilder builder, final CharSequence target, final CharSequence replacement) {
    final String tgtStr = target.toString();
    final String replStr = replacement.toString();
    int j = builder.lastIndexOf(tgtStr);
    if (j < 0)
      return builder;

    final int tgtLen = tgtStr.length();
    final int tgtLen1 = Math.max(tgtLen, 1);
    final int thisLen = builder.length();

    final int newLenHint = thisLen - tgtLen + replStr.length();
    if (newLenHint < 0)
      throw new OutOfMemoryError();

    do {
      builder.replace(j, j + tgtLen1, replStr);
    }
    while ((j = builder.lastIndexOf(tgtStr, j - tgtLen1)) > -1);
    return builder;
  }

  public static String replaceDeep(String string, final CharSequence target, final CharSequence replacement) {
    String result;
    while (string.length() != (result = string.replace(target, replacement)).length())
      string = result;

    return result;
  }

  public static StringBuilder replaceDeep(final StringBuilder builder, final CharSequence target, final CharSequence replacement) {
    while (builder.length() != replace(builder, target, replacement).length());
    return builder;
  }

  /**
   * Tests if the specified {@link StringBuilder} starts with the specified
   * prefix.
   *
   * @param builder The {@link StringBuilder}.
   * @param prefix The prefix.
   * @return {@code true} if the {@code prefix} character sequence is a prefix
   *         of {@code builder}; {@code false} otherwise. Note also that
   *         {@code true} will be returned if {@code prefix} is an empty string
   *         or is equal to {@code builder}.
   * @throws NullPointerException If {@code builder} or {@code prefix} are null.
   */
  public static boolean startsWith(final StringBuilder builder, final CharSequence prefix) {
    if (prefix.length() == 0)
      return true;

    if (builder.length() < prefix.length())
      return false;

    for (int i = 0; i < prefix.length(); ++i)
      if (builder.charAt(i) != prefix.charAt(i))
        return false;

    return true;
  }

  /**
   * Tests if the specified {@link StringBuilder} ends with the specified
   * suffix.
   *
   * @param builder The {@link StringBuilder}.
   * @param suffix The suffix.
   * @return {@code true} if the {@code suffix} character sequence is a suffix
   *         of {@code builder}; {@code false} otherwise. Note also that
   *         {@code true} will be returned if {@code suffix} is an empty string
   *         or is equal to {@code builder}.
   * @throws NullPointerException If {@code builder} or {@code suffix} are null.
   */
  public static boolean endsWith(final StringBuilder builder, final CharSequence suffix) {
    if (suffix.length() == 0)
      return true;

    if (builder.length() < suffix.length())
      return false;

    final int offset = builder.length() - suffix.length();
    for (int i = suffix.length() - 1; i >= 0; --i)
      if (builder.charAt(offset + i) != suffix.charAt(i))
        return false;

    return true;
  }

  /**
   * Converts the characters in the specified {@link StringBuilder} to lowercase
   * using case mapping information from the UnicodeData file.
   *
   * @param builder The {@link StringBuilder}.
   * @return The specified {@link StringBuilder}, with its characters converted to lowercase.
   * @throws NullPointerException If {@code builder} is null.
   * @see Character#toLowerCase(char)
   */
  public static StringBuilder toLowerCase(final StringBuilder builder) {
    for (int i = 0; i < builder.length(); ++i)
      builder.setCharAt(i, Character.toLowerCase(builder.charAt(i)));

    return builder;
  }

  /**
   * Converts the characters in the specified {@link StringBuilder} to uppercase
   * using case mapping information from the UnicodeData file.
   *
   * @param builder The {@link StringBuilder}.
   * @return The specified {@link StringBuilder}, with its characters converted to uppercase.
   * @throws NullPointerException If {@code builder} is null.
   * @see Character#toUpperCase(char)
   */
  public static StringBuilder toUppereCase(final StringBuilder builder) {
    for (int i = 0; i < builder.length(); ++i)
      builder.setCharAt(i, Character.toUpperCase(builder.charAt(i)));

    return builder;
  }

  private static String changeCase(final String string, final boolean upper, final int beginIndex, final int endIndex) {
    if (string.length() == 0)
      return string;

    if (beginIndex > endIndex)
      throw new IllegalArgumentException("start {" + beginIndex + "} > end {" + endIndex + "}");

    if (string.length() < beginIndex)
      throw new StringIndexOutOfBoundsException("start index {" + beginIndex + "} > string length {" + string.length() + "}");

    if (endIndex < 0)
      throw new StringIndexOutOfBoundsException("end index {" + endIndex + "} < 0");

    if (beginIndex == endIndex)
      return string;

    if (beginIndex == 0) {
      final String caseString = string.substring(beginIndex, endIndex).toLowerCase();
      final String endString = string.substring(endIndex);
      return upper ? caseString.toUpperCase() + endString : caseString.toLowerCase() + endString;
    }

    if (endIndex == string.length()) {
      final String beginString = string.substring(0, beginIndex);
      final String caseString = string.substring(beginIndex, endIndex).toLowerCase();
      return upper ? beginString + caseString.toUpperCase() : beginString + caseString.toLowerCase();
    }

    final String beginString = string.substring(0, beginIndex);
    final String caseString = string.substring(beginIndex, endIndex).toLowerCase();
    final String endString = string.substring(endIndex);
    return upper ? beginString + caseString.toUpperCase() + endString : beginString + caseString.toLowerCase() + endString;
  }

  public static String toLowerCase(final String string, final int beginIndex, final int endIndex) {
    return changeCase(string, false, beginIndex, endIndex);
  }

  public static String toLowerCase(final String string, final int beginIndex) {
    return changeCase(string, false, beginIndex, string.length());
  }

  public static String toUpperCase(final String string, final int beginIndex, final int endIndex) {
    return changeCase(string, true, beginIndex, endIndex);
  }

  public static String toUpperCase(final String string, final int beginIndex) {
    return changeCase(string, true, beginIndex, string.length());
  }

  public static String padFixed(final String string, final int length, final boolean right) {
    if (length - string.length() < 0)
      return string;

    final char[] chars = new char[length - string.length()];
    java.util.Arrays.fill(chars, ' ');
    return right ? string + String.valueOf(chars) : String.valueOf(chars) + string;
  }

  private static String hex(long i, final int places) {
    if (i == Long.MIN_VALUE)
      return "-8000000000000000";

    boolean negative = i < 0;
    if (negative)
      i = -i;

    String result = Long.toString(i, 16).toUpperCase();
    if (result.length() < places)
      result = "0000000000000000".substring(result.length(), places) + result;

    return negative ? '-' + result : result;
  }

  public static String toUTF8Literal(final char ch) {
    return "\\x" + hex(ch, 2);
  }

  public static String toUTF8Literal(final String string) {
    final StringBuilder buffer = new StringBuilder(string.length() * 4);
    for (int i = 0; i < string.length(); ++i) {
      char ch = string.charAt(i);
      buffer.append(toUTF8Literal(ch));
    }

    return buffer.toString();
  }

  public static String getAlpha(final int number) {
    int scale;
    return number < '{' - 'a' ? String.valueOf((char)('a' + number)) : getAlpha((scale = number / ('{' - 'a')) - 1) + String.valueOf((char)('a' + number - scale * ('{' - 'a')));
  }

  @SafeVarargs
  public static String getCommonPrefix(final String ... strings) {
    if (strings == null || strings.length == 0)
      return null;

    if (strings.length == 1)
      return strings[0];

    for (int i = 0; i < strings[0].length(); ++i)
      for (int j = 1; j < strings.length; ++j)
        if (i == strings[j].length() || strings[0].charAt(i) != strings[j].charAt(i))
          return strings[0].substring(0, i);

    return strings[0];
  }

  public static String getCommonPrefix(final Collection<String> strings) {
    if (strings == null || strings.size() == 0)
      return null;

    Iterator<String> iterator = strings.iterator();
    if (strings.size() == 1)
      return iterator.next();

    final String string0 = iterator.next();
    for (int i = 0; i < string0.length(); ++i) {
      if (i > 0) {
        iterator = strings.iterator();
        iterator.next();
      }

      while (iterator.hasNext()) {
        final String next = iterator.next();
        if (i == next.length() || string0.charAt(i) != next.charAt(i))
          return string0.substring(0, i);
      }
    }

    return string0;
  }

  public static String escapeForJava(final String string) {
    return string == null ? null : string.replace("\\", "\\\\").replace("\"", "\\\"");
  }

  public static String printColumns(final String ... columns) {
    // Split input strings into columns and rows
    final String[][] strings = new String[columns.length][];
    int maxLines = 0;
    for (int i = 0; i < columns.length; ++i) {
      strings[i] = columns[i] == null ? null : columns[i].split("\n");
      if (strings[i] != null && strings[i].length > maxLines)
        maxLines = strings[i].length;
    }

    // Store an array of column widths
    final int[] widths = new int[columns.length];
    // calculate column widths
    for (int i = 0, maxWidth = 0; i < columns.length; ++i) {
      if (strings[i] != null) {
        for (int j = 0; j < strings[i].length; ++j)
          if (strings[i][j].length() > maxWidth)
            maxWidth = strings[i][j].length();
      }
      else if (maxWidth < 4) {
        maxWidth = 4;
      }

      widths[i] = maxWidth + 1;
    }

    // Print the lines
    final StringBuilder builder = new StringBuilder();
    for (int j = 0; j < maxLines; ++j) {
      if (j > 0)
        builder.append('\n');

      for (int i = 0; i < strings.length; ++i) {
        final String line = strings[i] == null ? "null" : j < strings[i].length ? strings[i][j] : "";
        builder.append(String.format("%-" + widths[i] + "s", line));
      }
    }

    return builder.length() == 0 ? "null" : builder.toString();
  }

  /**
   * Returns a string consisting of a specific number of concatenated
   * repetitions of an input string. For example,
   * {@code Strings.repeat("ha", 3)} returns the string {@code "hahaha"}.
   *
   * @param string Any non-null string.
   * @param count A nonnegative number of times to repeat the string.
   * @return A string containing {@code string} repeated {@code count} times; an
   *         empty string if {@code count == 0}; the {@code string} if
   *         {@code count == 1}
   * @throws NullPointerException If {@code string == null}
   * @throws IllegalArgumentException If {@code count &lt; 0}
   * @throws ArrayIndexOutOfBoundsException If
   *           {@code string.length() * count &gt; Integer.MAX_VALUE}
   */
  public static String repeat(final String string, final int count) {
    if (count < 0)
      throw new IllegalArgumentException("count < 0");

    if (count == 0 || string.length() == 0)
      return "";

    if (count == 1)
      return string;

    final int length = string.length();
    final long longSize = (long)length * (long)count;
    final int size = (int)longSize;
    if (size != longSize)
      throw new ArrayIndexOutOfBoundsException("Required array size too large: " + longSize);

    final char[] chars = new char[size];
    string.getChars(0, length, chars, 0);
    int n;
    for (n = length; n < size - n; n <<= 1)
      System.arraycopy(chars, 0, chars, n, n);

    System.arraycopy(chars, 0, chars, n, size - n);
    return new String(chars);
  }

  public static byte[] getBytes(final String value, final String enc) {
    try {
      return value.getBytes(enc);
    }
    catch (final UnsupportedEncodingException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  public static String trim(final String string, char ch) {
    if (string == null)
      return null;

    int i = 0;
    for (; i < string.length() && string.charAt(i) == ch; ++i);
    if (i == string.length())
      return "";

    int j = string.length() - 1;
    for (; j > i + 1 && string.charAt(j) == ch; --j);
    return i == 0 && j == string.length() - 1 ? string : string.substring(i, j + 1);
  }

  public static int indexOfUnQuoted(final String string, final char ch, final int fromIndex) {
    boolean esacped = false;
    boolean inSingleQuote = false;
    boolean inDoubleQuote = false;
    for (int i = fromIndex; i < string.length(); ++i) {
      final char c = string.charAt(i);
      if (c == '\\')
        esacped = true;
      else if (esacped)
        esacped = false;
      else if (c == ch && !inSingleQuote && !inDoubleQuote)
        return i;
      else if (c == '\'')
        inSingleQuote = !inSingleQuote;
      else if (c == '"')
        inDoubleQuote = !inDoubleQuote;
    }

    return -1;
  }

  public static int indexOfUnQuoted(final String string, final char ch) {
    return indexOfUnQuoted(string, ch, 0);
  }

  public static int lastIndexOfUnQuoted(final String string, final char ch, final int fromIndex) {
    boolean esacped = false;
    boolean inSingleQuote = false;
    boolean inDoubleQuote = false;
    char n = '\0';
    for (int i = fromIndex - 1; i >= 0; --i) {
      final char c = string.charAt(i);
      if (c == '\\')
        esacped = true;
      else if (esacped)
        esacped = false;
      else if (n == ch && !inSingleQuote && !inDoubleQuote)
        return i + 1;
      else if (n == '\'')
        inSingleQuote = !inSingleQuote;
      else if (n == '"')
        inDoubleQuote = !inDoubleQuote;

      n = c;
    }

    return n == ch ? 0 : -1;
  }

  public static int lastIndexOfUnQuoted(final String string, final char ch) {
    return lastIndexOfUnQuoted(string, ch, string.length());
  }

  public static String toTruncatedString(final Object obj, final int length) {
    if (length < 4)
      throw new IllegalArgumentException("length < 4: " + length);

    final String str = obj == null ? "null" : obj.toString();
    return str.length() > length ? str.substring(0, length - 3) + "..." : str;
  }

  private static void appendElVar(final Map<String,String> variables, final StringBuilder builder, final StringBuilder var) {
    final String name = var.toString();
    final String value = variables.get(name);
    if (value != null)
      builder.append(value);
    else
      builder.append('$').append('{').append(name).append('}');

    var.setLength(0);
  }

  private static void appendElNoMatch(final StringBuilder builder, final StringBuilder var, final char close) {
    builder.append('$').append('{');
    if (var.length() > 0) {
      builder.append(var.toString());
      var.setLength(0);
    }

    if (close != '\0')
      builder.append(close);
  }

  /**
   * Dereferences all Expression Language-encoded names, such as
   * <code>${foo}</code> or <code>${bar}</code>, in the specified string with
   * values in the specified map.
   * <p>
   * Names encoded in Expression Language follow the same rules as <a href=
   * "https://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.8">Java
   * Identifiers</a>.
   *
   * @param s The string in which EL-encoded names are to be dereferenced.
   * @param variables The map of name to value pairs.
   * @return The specified string with EL-encoded names replaced with their
   *         mapped values. If a name is missing from the specified map, or if a
   *         name does not conform to the rules of <a href=
   *         "https://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.8">Java
   *         Identifiers</a>, or if the Expression Language encoding is
   *         malformed, it will remain in the string as-is.
   * @throws NullPointerException If {@code s} is null, or if {@code s} contains
   *           an EL-encoded name and {@code variables} is null.
   */
  public static String derefEL(final String s, final Map<String,String> variables) {
    if (s.length() < 4)
      return s;

    final StringBuilder builder = new StringBuilder();
    final StringBuilder var = new StringBuilder();
    boolean escape = false;
    final int len = s.length();
    for (int i = 0; i < len; ++i) {
      char ch = s.charAt(i);
      if (ch == '\\') {
        if (var.length() > 0) {
          builder.append('$').append('{').append(var.toString());
          var.setLength(0);
        }

        if (!(escape = !escape))
          builder.append(ch);
      }
      else if (!escape) {
        if (ch == '$') {
          if (var.length() > 0) {
            appendElVar(variables, builder, var);
          }

          if (++i == len) {
            builder.append('$');
          }
          else {
            ch = s.charAt(i);
            if (ch != '{') {
              var.setLength(0);
              builder.append('$');
              if (ch != '\\')
                builder.append(ch);
            }
            else if (++i == len) {
              appendElNoMatch(builder, var, '\0');
            }
            else {
              ch = s.charAt(i);
              if ('a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z' || ch == '_')
                var.append(ch);
              else
                appendElNoMatch(builder, var, ch);
            }
          }
        }
        else if (var.length() > 0) {
          if ('a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z' || '0' <= ch && ch <= '9' || ch == '_') {
            var.append(ch);
          }
          else if (ch != '}') {
            appendElNoMatch(builder, var, ch);
          }
          else {
            appendElVar(variables, builder, var);
            if (ch != '}')
              builder.append(ch);
          }
        }
        else {
          builder.append(ch);
        }
      }
      else {
        if (var.length() > 0)
          appendElVar(variables, builder, var);

        builder.append(ch);
        escape = false;
      }
    }

    if (var.length() > 0)
      appendElNoMatch(builder, var, '\0');

    return builder.toString();
  }

  private static void appendEvVar(final Map<String,String> variables, final StringBuilder builder, final StringBuilder var) {
    final String variable = variables.get(var.toString());
    if (variable != null)
      builder.append(variable);

    var.setLength(0);
  }

  /**
   * Dereferences all POSIX-compliant Environment Variable names, such as
   * <code>$FOO</code> or <code>${BAR}</code>, in the specified string with
   * values in the specified map.
   * <p>
   * Names encoded in POSIX format follow the rules defined in the POSIX
   * standard on shells <a href=
   * "http://pubs.opengroup.org/onlinepubs/9699919799/utilities/V3_chap02.html#tag_18_10_02">IEEE
   * Std 1003.1-2017</a>.
   *
   * @param s The string in which POSIX-compliant names are to be dereferenced.
   * @param variables The map of name to value pairs.
   * @return The specified string with POSIX-compliant names replaced with their
   *         mapped values. If a name is missing from the specified map, it will
   *         remain in the string as-is.
   * @throws ParseException If the encoding of the environment variable name is
   *           malformed.
   * @throws NullPointerException If {@code s} is null, or if {@code s} contains
   *           an POSIX-compliant name and {@code variables} is null.
   */
  public static String derefEV(final String s, final Map<String,String> variables) throws ParseException {
    if (s.length() < 2)
      return s;

    final StringBuilder builder = new StringBuilder();
    final StringBuilder var = new StringBuilder();
    boolean escape = false;
    boolean bracket = false;
    final int len = s.length();
    for (int i = 0; i < len; i++) {
      char ch = s.charAt(i);
      if (ch == '\\') {
        if (var.length() > 0)
          appendEvVar(variables, builder, var);

        if (!(escape = !escape))
          builder.append(ch);
      }
      else if (!escape) {
        if (ch == '$') {
          if (var.length() > 0)
            appendEvVar(variables, builder, var);

          if (++i == len) {
            builder.append('$');
          }
          else {
            ch = s.charAt(i);
            if (ch == '$')
              throw new ParseException("$$: not supported", i);

            if (ch == '{') {
              bracket = true;
              if (++i == len)
                throw new ParseException("${: bad substitution", i);

              ch = s.charAt(i);
            }

            if ('a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z' || ch == '_') {
              var.append(ch);
            }
            else if (!bracket) {
              builder.append('$');
              if (ch != '\\')
                builder.append(ch);
            }
            else {
              throw new ParseException("${" + ch + ": bad substitution", i);
            }
          }
        }
        else if (var.length() > 0) {
          if ('a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z' || '0' <= ch && ch <= '9' || ch == '_') {
            var.append(ch);
          }
          else if (bracket && ch != '}') {
            throw new ParseException("${" + var + ch + ": bad substitution", i);
          }
          else {
            appendEvVar(variables, builder, var);
            if (!bracket || ch != '}')
              builder.append(ch);
          }
        }
        else {
          builder.append(ch);
        }
      }
      else {
        if (var.length() > 0)
          appendEvVar(variables, builder, var);

        builder.append(ch);
        escape = false;
      }
    }

    if (var.length() > 0) {
      if (bracket)
        throw new ParseException("${" + var + ": bad substitution", len);

      appendEvVar(variables, builder, var);
    }

    return builder.toString();
  }

  private Strings() {
  }
}