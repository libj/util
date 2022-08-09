/* Copyright (c) 2016 LibJ
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

import static org.libj.lang.Assertions.*;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Utility functions for operations pertaining to {@link Pattern}.
 */
public final class Patterns {
  private static final ConcurrentHashMap<String,Pattern> propertyNameToPattern = new ConcurrentHashMap<>();
  private static final char[] escapeChars = {'!', '$', '(', ')', '*', '+', '-', '.', '<', '=', '>', '?', '[', '\\', ']', '^', '{', '|', '}'};

  /**
   * Returns {@code true} if the provided char is a Java Regular Expression metacharacter, {@code false} otherwise.
   *
   * @param ch
   * @return {@code true} if the provided char is a Java Regular Expression metacharacter, {@code false} otherwise.
   * @see <a href= "https://docs.oracle.com/javase/tutorial/essential/regex/literals.html">String Literals</a>
   */
  public static boolean isMetaCharacter(final char ch) {
    return Arrays.binarySearch(escapeChars, ch) > -1;
  }

  /**
   * Returns the provided {@code regex} with regular expression literal characters escaped.
   *
   * @param regex The string to escape.
   * @return The provided {@code regex} with regular expression literal characters escaped.
   * @see <a href= "https://docs.oracle.com/javase/tutorial/essential/regex/literals.html">String Literals</a>
   */
  public static StringBuilder escape(final StringBuilder regex) {
    for (int i = regex.length() - 1; i >= 0; --i) { // [N]
      final char ch = regex.charAt(i);
      if (isMetaCharacter(ch))
        regex.insert(i, '\\');
    }

    return regex;
  }

  /**
   * Returns the provided {@code regex} with regular expression literal characters escaped.
   *
   * @param regex The string to escape.
   * @return The provided {@code regex} with regular expression literal characters escaped.
   * @see <a href= "https://docs.oracle.com/javase/tutorial/essential/regex/literals.html">String Literals</a>
   */
  public static String escape(final String regex) {
    return escape(new StringBuilder(regex)).toString();
  }

  /**
   * Compiles the given regular expression into a pattern.
   * <p>
   * This method differentiates itself from {@link Pattern#compile(String)} by caching the result for faster subsequence execution.
   *
   * @param regex The expression to be compiled.
   * @return The given regular expression compiled into a pattern.
   * @throws PatternSyntaxException If the expression's syntax is invalid.
   * @throws IllegalArgumentException If {@code regex} is null.
   */
  public static Pattern compile(final String regex) {
    return compile(regex, 0);
  }

  /**
   * Compiles the given regular expression into a pattern with the given flags.
   * <p>
   * This method differentiates itself from {@link Pattern#compile(String,int)} by caching the result for faster subsequence
   * execution.
   *
   * @param regex The expression to be compiled
   * @param flags Match flags, a bit mask that may include {@link Pattern#CASE_INSENSITIVE}, {@link Pattern#MULTILINE},
   *          {@link Pattern#DOTALL}, {@link Pattern#UNICODE_CASE}, {@link Pattern#CANON_EQ}, {@link Pattern#UNIX_LINES},
   *          {@link Pattern#LITERAL}, {@link Pattern#UNICODE_CHARACTER_CLASS} and {@link Pattern#COMMENTS}
   * @return the given regular expression compiled into a pattern with the given flags
   * @throws IllegalArgumentException If bit values other than those corresponding to the defined match flags are set in
   *           {@code flags}
   * @throws PatternSyntaxException If the expression's syntax is invalid
   * @throws IllegalArgumentException If {@code regex} is null.
   */
  public static Pattern compile(final String regex, final int flags) {
    final String key = assertNotNull(regex) + "$" + flags;
    Pattern pattern = propertyNameToPattern.get(key);
    if (pattern == null)
      propertyNameToPattern.put(key, pattern = Pattern.compile(regex, flags));

    return pattern;
  }

  /**
   * Returns a string array of the group names of the provided {@code pattern}.
   * <p>
   * This implementation expects the group name encoding as:
   *
   * <pre>
   * {@code (?<name>regex)}
   * </pre>
   *
   * @param pattern The {@link Pattern}.
   * @return A string array of the group names of the provided {@code pattern}.
   * @throws IllegalArgumentException If {@code pattern} is null.
   */
  public static String[] getGroupNames(final Pattern pattern) {
    return getGroupNames(assertNotNull(pattern).toString(), 0, 0);
  }

  /**
   * Returns a string array of the group names of the provided {@code pattern}.
   * <p>
   * This implementation expects the group name encoding as:
   *
   * <pre>
   * {@code (?<name>regex)}
   * </pre>
   *
   * @param pattern The {@link Pattern}.
   * @return A string array of the group names of the provided {@code pattern}.
   * @throws IllegalArgumentException If {@code pattern} is null.
   */
  public static String[] getGroupNames(final String pattern) {
    return getGroupNames(assertNotNull(pattern), 0, 0);
  }

  private static String[] empty = {};

  private static String[] getGroupNames(final String regex, final int index, final int depth) {
    final int start = regex.indexOf("(?<", index);
    if (start < 0)
      return depth == 0 ? empty : new String[depth];

    final int end = regex.indexOf('>', start + 3);
    if (end < 0)
      throw new PatternSyntaxException("Malformed pattern", regex, start + 3);

    final String name = regex.substring(start + 3, end);
    final String[] names = getGroupNames(regex, end + 1, depth + 1);
    names[depth] = name;
    return names;
  }

  /**
   * Unescapes the provided regex into a form that does not contain any regular expression syntax. For a regex to be "unescapable",
   * it must satisfy the following conditions:
   * <ol>
   * <li>Have valid regular expression syntax. If this is not satisfied, a {@link PatternSyntaxException} will be thrown.</li>
   * <li>Be able to match no more than 1 string (i.e. "abc" is technically a regular expression, however it can only match a single
   * string: "abc"). If this is not satisfied, this method will return {@code null}.</li>
   * </ol>
   * If the provided regex string contains definitions for special groups constructs, these constructs will be removed.<blockquote>
   * <table>
   * <caption>Examples</caption>
   * <tr><td><b>Input</b></td><td><b>Output</b></td></tr>
   * <tr><td><code>\|</code></td><td><code>|</code></td></tr>
   * <tr><td><code>abc</code></td><td><code>abc</code></td></tr>
   * <tr><td><code>(abc)</code></td><td><code>abc</code></td></tr>
   * <tr><td><code>[aa][bb][cc]</code></td><td><code>abc</code></td></tr>
   * <tr><td><code>(a|a)(b|b)(c|c)</code></td><td><code>abc</code></td></tr>
   * <tr><td><code>(a|a)(b|b)(c|d)</code></td><td><code>null</code></td></tr>
   * </table>
   * </blockquote>
   *
   * @param regex The {@link String} to unescape.
   * @return The unescaped form of the regex if and only if the pattern is a valid regular expression, and if it <u>cannot match
   *         more than 1 string</u>, or {@code null} if the specified string cannot be parsed as a regular expression, or if it
   *         represents a regular expression that can match multiple strings.
   * @throws PatternSyntaxException If the expression's syntax is invalid.
   */
  public static String unescape(final String regex) {
    if (regex == null)
      return null;

    final int len = regex.length();
    if (len == 0)
      return regex;

    char ch = regex.charAt(0);
    if (ch == '^' && regex.length() == 1)
      return "";

    final StringBuilder builder = new StringBuilder();

    boolean multiMatch = false;

    boolean escaped = false;
    boolean blockEscaped = false;
    char last = '\0';
    OUT:
    for (int i = 0; i < len; escaped = isEscape(last = ch), ++i) { // [N]
      ch = regex.charAt(i);
      if (blockEscaped) {
        blockEscaped = !escaped || ch != 'E';
      }
      else if (blockEscaped = escaped && ch == 'Q') {
      }
      else if (escaped) {
        if (isPredefinedCharacterClass(ch)) {
          multiMatch = true;
          continue;
        }
        else if (isBoundaryMatcher(ch)) {
          continue;
        }
        else if (ch == 'p' || ch == 'P' && i + 4 < len && regex.charAt(i + 1) == '{') {
          CharacterClassEnum characterClass = null;
          final int s = i + 2;
          int j = s;
          for (; j < len; ++j) { // [N]
            ch = regex.charAt(j);
            if (ch == '}' && characterClass != null) {
              multiMatch = true;
              continue OUT;
            }

            characterClass = CharacterClassEnum.findNext(characterClass, j - s, ch);
            if (characterClass == null)
              break;
          }

          throw new PatternSyntaxException("Invalid character class", regex, i);
        }
      }
      else if (ch == '[') {
        final int start = i + 1;
        final int end = indexOfClassClose(regex, start);
        if (end < 0) {
          builder.append(ch);
        }
        else if (start == end) {
          i = end;
        }
        else {
          final char uniqueChar = uniqueCharFromClass(regex, start, end);
          if (uniqueChar == '\0') {
            multiMatch = true;
          }
          else {
            builder.append(uniqueChar);
            i = end;
          }
        }

        continue;
      }
      else {
        if (last == '(' && ch == '?') {
          int end = indexOfUnEscaped(regex, ')', i + 1, len);
          end = consumeSpecial(regex, len, i + 1, end);
          if (end < 0)
            throw new PatternSyntaxException("Invalid group", regex, i);

          i = end;
          continue;
        }

        int end = consumeQuantifier(regex, i);
        if (end == -1) {
          multiMatch = true;
          continue;
        }
        else if (end < 0) {
          throw new PatternSyntaxException("Invalid quantifier", regex, i);
        }
        else if (i != end) {
          if (end + 1 < len) {
            ch = regex.charAt(end + 1);
            if (ch == '+' || ch == '?')
              ++end;
          }

          i = end;
          continue;
        }
      }

      builder.append(ch);
    }

    if (multiMatch)
      return null;

    final int builderLen = builder.length();
    if (builderLen > 0) {
      if (builder.charAt(builderLen - 1) == '$') {
        final int escapes = countEscapes(builder, builderLen - 2);
        if (escapes % 2 == 0)
          builder.delete(builderLen - 1, builderLen);
      }

      if (builder.charAt(0) == '^')
        builder.delete(0, 1);
    }

    final int idx = reduceGroups(builder, 0, builder.length());
    return idx < 0 ? null : builder.toString();
  }

  private static final int[] escapes = {'(', ')', '*', '+', '.', '?', '[', '\\', ']', '{', '|', '}'};

  /**
   * Reduces groups and unescapes regex escaped chars in the specified {@link StringBuilder}.
   *
   * @param builder The {@link StringBuilder}.
   * @param start The starting index.
   * @param end The ending index.
   * @return The index in the {@link StringBuilder} representing the next group, or {@code -1} if the specified
   *         {@link StringBuilder} cannot be parsed.
   */
  static int reduceGroups(final StringBuilder builder, final int start, int end) {
    return reduceGroups(builder, false, start, end);
  }

  private static int reduceGroups(final StringBuilder builder, final boolean wasOpened, final int start, int end) {
    boolean escaped = false;
    boolean blockEscaped = false;
    char ch;
    for (int i = start; i < builder.length(); escaped = (blockEscaped || !escaped) && isEscape(ch), ++i) { // [N]
      ch = builder.charAt(i);
      if (blockEscaped) {
        if (escaped && ch == 'E') {
          blockEscaped = false;
          builder.delete(i - 1, i + 1);
          end -= 2;
          i -= 2;
        }

        continue;
      }
      else if (escaped) {
        if (Arrays.binarySearch(escapes, ch) > -1) {
          builder.delete(i - 1, i);
          --end;
          --i;
        }
        else if (ch == 'Q') {
          blockEscaped = true;
          builder.delete(i - 1, i + 1);
          end -= 2;
          i -= 2;
        }

        continue;
      }

      if (ch == '(') {
        // Special groups are not supported
        if (i < builder.length() - 1 && builder.charAt(i + 1) == '?')
          return -1;

        // Delete the '('
        builder.delete(i, i + 1);
        --end;
        final int len = builder.length();
        i = reduceGroups(builder, true, i, lastIndexOfUnEscaped(builder, ')', i, end) + 1);
        if (i < 0)
          return -1;

        end -= len - builder.length();
        --i;
      }
      else if (ch == '|') {
        // Delete the '|'
        builder.delete(i, i + 1);
        final String left = builder.substring(start, i);
        end = reduceGroups(builder, wasOpened, i, end - 1);
        if (end < 0)
          return -1;

        final String right = builder.substring(i, end);
        if (!left.equals(right))
          return -1;

        // Delete the `right`
        builder.delete(i, end);
        return i;
      }
      else if (ch == ')') {
        if (!wasOpened)
          return -1;

        // Delete the ')'
        builder.delete(i, i + 1);
        return i;
      }
    }

    return end;
  }

  static int countEscapes(final CharSequence str, final int fromIndex) {
    int count = 0;
    for (int j = fromIndex; j >= 0; --j) { // [N]
      if (isEscape(str.charAt(j)))
        ++count;
      else
        break;
    }

    return count;
  }

  static int lastIndexOfUnEscaped(final CharSequence str, final char ch, final int start, final int end) {
    for (int lastIndex = -1, i = start;; lastIndex = i++) {
      i = indexOfUnEscaped(str, ch, i, end);
      if (i < 0)
        return lastIndex;
    }
  }

  static int indexOfUnEscaped(final CharSequence str, final char ch, final int start, final int end) {
    boolean escaped = false;
    int blockEscaped = -1;
    for (int i = start; i < end; ++i) { // [N]
      final char c = str.charAt(i);
      if (escaped) {
        escaped = false;
        if (blockEscaped < 0) {
          if (c == 'Q') {
            blockEscaped = i;
            continue;
          }
        }
        else if (c == 'E') {
          blockEscaped = -1;
        }

        continue;
      }
      else if ((escaped = isEscape(c)) || blockEscaped > -1) {
        continue;
      }
      else if (c == ch) {
        return i;
      }
    }

    return -1;
  }

  private static int indexOfClassClose(final CharSequence str, final int fromIndex) {
    boolean escaped = false;
    for (int i = Math.max(fromIndex, 0), i$ = str.length(), and = 0, depth = 1; i < i$; ++i) { // [N]
      final char c = str.charAt(i);
      if (and == 1) {
        if (c == '&') {
          ++and;
          continue;
        }

        and = 0;
      }
      else if (and == 2) {
        if (c == '[')
          ++depth;

        and = 0;
        continue;
      }

      if (escaped)
        escaped = false;
      else if (isEscape(c))
        escaped = true;
      else if (and == 0 && c == '&')
        ++and;
      else if (c == ']' && --depth == 0)
        return i;
    }

    return -1;
  }

  static char[] unescapeClass(final String str, final char last, final int index, final int depth) {
    if (str.length() == index)
      return depth == 0 ? null : new char[depth];

    final char ch = str.charAt(index);
    final char[] ret;
    if (!isEscape(ch) || isEscape(last)) {
      ret = unescapeClass(str, ch != '\\' ? ch : '\0', index + 1, depth + 1);
      ret[depth] = ch;
    }
    else {
      ret = unescapeClass(str, ch, index + 1, depth);
    }

    return ret;
  }

  static char uniqueCharFromClass(final CharSequence str) {
    return uniqueCharFromClass(str, 0, str.length());
  }

  private static boolean isPredefinedCharacterClass(final char ch) {
    return ch == 'd' || ch == 'D' || ch == 'h' || ch == 'H' || ch == 's' || ch == 'S' || ch == 'v' || ch == 'w' || ch == 'W' || ch == 'R';
  }

  private static boolean isBoundaryMatcher(final char ch) {
    return ch == 'b' || ch == 'B' || ch == 'A' || ch == 'G' || ch == 'Z' || ch == 'z';
  }

  private static int consumeSpecial(final String str, final int len, final int start, final int end) {
    char ch, last = '\0';
    for (int i = start; i < len; ++i, last = ch) { // [N]
      ch = str.charAt(i);
      if (i == start) {
        if (ch == ':' || ch == '=' || ch == '!' || ch == '>')
          return i;
      }
      else if (i == start + 1) {
        if (last == '<') {
          if (ch == '=' || ch == '!')
            return i;

          return lastIndexOfUnEscaped(str, '>', start + 1, end);
        }
      }
      else {
        final int colon = lastIndexOfUnEscaped(str, ':', start + 1, end);
        if (colon < 0)
          return -1;

        boolean hyphenSeen = false;
        for (i = start; i < colon; ++i) { // [N]
          ch = str.charAt(i);
          if (ch == 'i' || ch == 'd' || ch == 'm' || ch == 's' || ch == 'u' || ch == 'x')
            continue;

          if (ch != '-' || hyphenSeen)
            return -1;

          hyphenSeen = true;
        }

        return colon;
      }
    }

    return -1;
  }

  private static int consumeQuantifier(final String str, final int start) {
    char ch = str.charAt(start);
    if (ch == '?' || ch == '*' || ch == '+')
      return -1;

    if (ch != '{')
      return start;

    final int close = str.indexOf('}', start + 1);
    if (close < 0 || close == start + 1)
      return start;

    ch = str.charAt(start + 1);
    if (ch < '0' || '9' < ch)
      return start;

    final boolean isOne = str.charAt(start + 1) == '1';
    if (close == start + 2)
      return isOne ? isValidQualifier(str, start + 2) : -1;

    if (close == start + 3) {
      ch = str.charAt(start + 2);
      if ('0' <= ch && ch <= '9' || ch == ',')
        return -1;

      return isValidQualifier(str, start + 3);
    }

    if (close == start + 4) {
      ch = str.charAt(start + 2);
      if ('0' <= ch && ch <= '9')
        return -1;

      if (ch != ',' || str.charAt(start + 3) == '1')
        return isValidQualifier(str, start + 4);
    }

    boolean commaSeen = false;
    for (int i = start + 1; i < close; ++i) { // [N]
      ch = str.charAt(i);
      if (ch == ',') {
        if (commaSeen)
          return start;

        commaSeen = true;
      }
      else if (ch < '0' || '9' < ch)
        return start;
    }

    return -1;
  }

  private static int isValidQualifier(final String str, final int start) {
    if (start + 1 >= str.length())
      return start;

    char ch = str.charAt(start + 1);
    if (ch == '*')
      return -start - 1;

    if (start + 2 == str.length() || ch != '+' && ch != '?')
      return start;

    ch = str.charAt(start + 2);
    if (ch == '*' || ch == '+' || ch == '?')
      return -start - 2;

    return start;
  }

  static char uniqueCharFromClass(final CharSequence str, final int fromIndex, final int toIndex) {
    int i = fromIndex;
    if (toIndex - fromIndex == 1)
      return str.charAt(i);

    char first = str.charAt(i++);
    if (isEscape(first))
      first = str.charAt(i++);

    int and = 0;
    boolean escaped = false;
    for (char ch, last = first; i < toIndex; ++i, last = ch) { // [N]
      ch = str.charAt(i);
      if (and == 1) {
        if (ch == '&') {
          ++and;
          continue;
        }

        and = 0;
      }
      else if (and == 2) {
        if (ch == '[') {
          and = 3;
          continue;
        }

        and = 0;
      }
      else if (and == 3 && ch == ']') {
        and = 0;
        continue;
      }

      if (!escaped && (escaped = isEscape(ch)))
        continue;

      if (escaped && isPredefinedCharacterClass(ch))
        return '\0';

      if (and == 0 && ch == '&' && i > fromIndex + 1) {
        ++and;
      }
      else if (ch == '-') {
        if (i == toIndex - 1 && last != '-')
          return '\0';
      }
      else if (first != ch) {
        return '\0';
      }

      last = ch;
    }

    return first;
  }

  private static boolean isEscape(final char ch) {
    return ch == '\\';
  }

  private Patterns() {
  }
}