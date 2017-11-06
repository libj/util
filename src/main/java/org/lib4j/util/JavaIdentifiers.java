/* Copyright (c) 2017 lib4j
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

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.lib4j.lang.Strings;
import org.lib4j.util.function.TriFunction;

public final class JavaIdentifiers {
  private static final String[] reservedWords = {"abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "enum", "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void", "volatile", "while"};

  // NOTE: This array is sorted
  private static final char[] discardTokens = new char[] {'!', '"', '#', '%', '&', '\'', '(', ')', '*', ',', '-', '.', '.', '/', ':', ';', '<', '>', '?', '@', '[', '\\', ']', '^', '_', '`', '{', '|', '}', '~'};

  private static final Map<Character,String> defaultPackageSub = Collections.singletonMap(null, "_");

  private static boolean isJavaReservedWord(final String word) {
    return Arrays.binarySearch(reservedWords, word) >= 0;
  }

  private static String transformNotReserved(final String string, final char head, final char tail, final Map<Character,String> sub, final TriFunction<String,Character,Map<Character,String>,StringBuilder> transformer) {
    if (string == null || string.length() == 0)
      return string;

    final StringBuilder builder = transformer.apply(string, head, sub);
    final String word = builder.toString();
    return !isJavaReservedWord(word) ? word : tail == '\0' ? builder.insert(0, head).toString() : builder.append(tail).toString();
  }

  /**
   * Transforms a string into a valid Java Identifier. Strings that start with
   * an illegal character are prepended with <code>head</code>. Strings that
   * are Java Reserved Words are prepended with <code>head</code>. All other
   * illegal characters are substituted with the string value mapped to the key
   * of the character in <code>sub</code>. If the mapping is missing, the
   * illegal character is omitted.
   *
   * @param string The input string.
   * @param head The character that will be prepended to the string if the
   *        first character is not valid.
   * @param sub The mapping of illegal characters to their substitutions.
   * @return The string transformed to a valid Java Identifier.
   *
   * @see <a href="https://docs.oracle.com/javase/specs/jls/se9/html/jls-3.html#jls-3.8">Java Identifiers</a>
   */
  public static String toIdentifier(final String string, final char head, final Map<Character,String> sub) {
    return transformNotReserved(string, head, '\0', sub, JavaIdentifiers::toIdentifier0);
  }

  /**
   * Transforms a string into a valid Java Identifier. Strings that start with
   * an illegal character are prepended with <code>head</code>. Strings that
   * are Java Reserved Words are prepended with <code>head</code>. All other
   * illegal characters are omitted.
   *
   * @param string The input string.
   * @param head The character that will be prepended to the string if the
   *        first character is not valid.
   * @return The string transformed to a valid Java Identifier.
   *
   * @see <a href="https://docs.oracle.com/javase/specs/jls/se9/html/jls-3.html#jls-3.8">Java Identifiers</a>
   */
  public static String toIdentifier(final String string, final char head) {
    return transformNotReserved(string, head, '\0', null, JavaIdentifiers::toIdentifier0);
  }

  /**
   * Transforms a string into a valid Java Identifier. Strings that start with
   * an illegal character are prepended with <code>_</code>. Strings that
   * are Java Reserved Words are prepended with <code>head</code>. All other
   * illegal characters are omitted.
   *
   * @param string The input string.
   * @param head The character that will be prepended to the string if the
   *        first character is not valid.
   * @return The string transformed to a valid Java Identifier.
   *
   * @see <a href="https://docs.oracle.com/javase/specs/jls/se9/html/jls-3.html#jls-3.8">Java Identifiers</a>
   */
  public static String toIdentifier(final String string) {
    return transformNotReserved(string, '_', '\0', null, JavaIdentifiers::toIdentifier0);
  }

  private static StringBuilder toIdentifier0(final String string, final char head, final Map<Character,String> sub) {
    final StringBuilder builder = new StringBuilder(string.length());
    final char[] chars = string.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      final char ch = chars[i];
      if (i == 0 && !Character.isJavaIdentifierStart(ch) && head != '\0')
        builder.append(head);

      if (Character.isJavaIdentifierPart(ch)) {
        builder.append(ch);
      }
      else if (sub != null) {
        String replacement = sub.get(ch);
        if (replacement == null)
          replacement = sub.get(null);

        if (replacement != null)
          builder.append(replacement);
      }
    }

    return builder;
  }

  /**
   * Transforms a string into a valid Java Identifier that meets suggested
   * package name guidelines. Strings that are Java Reserved Words are
   * prepended with <code>head</code>. Strings that start with an illegal
   * character are prepended with <code>_</code>. All other illegal characters
   * are substituted <code>_</code>.
   *
   * If the domain name contains a hyphen, or any other special character not
   * allowed in an identifier, convert it into an underscore.
   *
   * If any of the resulting package name components are keywords, append an
   * underscore to them.
   *
   * If any of the resulting package name components start with a digit, or any
   * other character that is not allowed as an initial character of an
   * identifier, have an underscore prefixed to the component.
   *
   * @param string The input string.
   * @param head The character that will be prepended to the string if the
   *        first character is not valid.
   * @param sub The mapping of illegal characters to their substitutions.
   * @return The string transformed to a valid Java Identifier that meets
   *         suggested package name guidelines.
   *
   * @see <a href="https://docs.oracle.com/javase/specs/jls/se9/html/jls-3.html#jls-3.8">Java Identifiers</a>
   * @see <a href="https://docs.oracle.com/javase/tutorial/java/package/namingpkgs.html">Package Names</a>
   * @see <a href="https://docs.oracle.com/javase/specs/jls/se9/html/jls-6.html#d5e8089">Unique Pacakge Names</a>
   */
  public static String toPackageCase(final String string) {
    return transformNotReserved(string, '_', '_', defaultPackageSub, JavaIdentifiers::toPacakgeCase0);
  }

  private static StringBuilder toPacakgeCase0(final String string, final char head, final Map<Character,String> sub) {
    return Strings.toLowerCase(toIdentifier0(string, head, sub));
  }

  /**
   * Transforms a string into a valid Java Identifier in camelCase. Strings
   * that start with an illegal character are prepended with <code>head</code>.
   * Strings that are Java Reserved Words are prepended with <code>head</code>.
   * All other illegal characters are substituted with the string value mapped
   * to the key of the character in <code>sub</code>. If the mapping is
   * missing, the illegal character is omitted.
   *
   * @param string The input string.
   * @param head The character that will be prepended to the string if the
   *        first character is not valid.
   * @param sub The mapping of illegal characters to their substitutions.
   * @return The string transformed to a valid Java Identifier in camelCase.
   *
   * @see <a href="https://docs.oracle.com/javase/specs/jls/se9/html/jls-3.html#jls-3.8">Java Identifiers</a>
   */
  public static String toCamelCase(final String string, final char head, final Map<Character,String> sub) {
    return transformNotReserved(string, head, '\0', sub, JavaIdentifiers::toCamelCase0);
  }

  /**
   * Transforms a string into a valid Java Identifier in camelCase. Strings
   * that start with an illegal character are prepended with <code>head</code>.
   * Strings that are Java Reserved Words are prepended with <code>head</code>.
   * All other illegal characters are omitted.
   *
   * @param string The input string.
   * @param head The character that will be prepended to the string if the
   *        first character is not valid.
   * @return The string transformed to a valid Java Identifier in camelCase.
   *
   * @see <a href="https://docs.oracle.com/javase/specs/jls/se9/html/jls-3.html#jls-3.8">Java Identifiers</a>
   */
  public static String toCamelCase(final String string, final char head) {
    return transformNotReserved(string, head, '\0', null, JavaIdentifiers::toCamelCase0);
  }

  /**
   * Transforms a string into a valid Java Identifier in camelCase. Strings
   * that start with an illegal character are prepended with <code>x</code>.
   * Strings that are Java Reserved Words are prepended with <code>x</code>.
   * All other illegal characters are omitted.
   *
   * @param string The input string.
   * @return The string transformed to a valid Java Identifier in camelCase.
   *
   * @see <a href="https://docs.oracle.com/javase/specs/jls/se9/html/jls-3.html#jls-3.8">Java Identifiers</a>
   */
  public static String toCamelCase(final String string) {
    return transformNotReserved(string, 'x', '\0', null, JavaIdentifiers::toCamelCase0);
  }

  private static StringBuilder toCamelCase0(final String string, final char head, final Map<Character,String> sub) {
    final StringBuilder builder = new StringBuilder(string.length());
    final char[] chars = string.toCharArray();
    boolean capNext = false;
    for (int i = 0; i < chars.length; i++) {
      if (i == 0 && !Character.isJavaIdentifierStart(chars[i]))
        builder.append(head);

      final char ch = chars[i];
      final int index = java.util.Arrays.binarySearch(discardTokens, ch);
      if (index >= 0) {
        capNext = true;
        if (sub != null) {
          String replacement = sub.get(ch);
          if (replacement == null)
            replacement = sub.get(null);

          if (replacement != null)
            builder.append(replacement);
        }
      }
      else if (capNext) {
        builder.append(Character.toUpperCase(ch));
        capNext = false;
      }
      else {
        builder.append(ch);
      }
    }

    return builder;
  }

  /**
   * Transforms a string into a valid Java Identifier in lower-camelCase.
   * Strings that start with an illegal character are prepended with
   * <code>head</code>. Strings that are Java Reserved Words are prepended with
   * <code>head</code>. All other illegal characters are substituted with the
   * string value mapped to the key of the character in <code>sub</code>. If
   * the mapping is missing, the illegal character is omitted.
   *
   * @param string The input string.
   * @param head The character that will be prepended to the string if the
   *        first character is not valid.
   * @param sub The mapping of illegal characters to their substitutions.
   * @return The string transformed to a valid Java Identifier in lower-CamelCase.
   *
   * @see <a href="https://docs.oracle.com/javase/specs/jls/se9/html/jls-3.html#jls-3.8">Java Identifiers</a>
   */
  public static String toInstanceCase(final String string, final char head, final Map<Character,String> sub) {
    return transformNotReserved(string, head, '\0', sub, JavaIdentifiers::toInstanceCase0);
  }

  /**
   * Transforms a string into a valid Java Identifier in lower-camelCase.
   * Strings that start with an illegal character are prepended with
   * <code>head</code>. Strings that are Java Reserved Words are prepended with
   * <code>head</code>. All other illegal characters are omitted.
   *
   * @param string The input string.
   * @param head The character that will be prepended to the string if the
   *        first character is not valid.
   * @return The string transformed to a valid Java Identifier in lower-CamelCase.
   *
   * @see <a href="https://docs.oracle.com/javase/specs/jls/se9/html/jls-3.html#jls-3.8">Java Identifiers</a>
   */
  public static String toInstanceCase(final String string, final char head) {
    return transformNotReserved(string, head, '\0', null, JavaIdentifiers::toInstanceCase0);
  }

  /**
   * Transforms a string into a valid Java Identifier in lower-camelCase.
   * Strings that start with an illegal character are prepended with
   * <code>_</code>. Strings that are Java Reserved Words are prepended with
   * <code>_</code>. All other illegal characters are omitted.
   *
   * @param string The input string.
   * @return The string transformed to a valid Java Identifier in lower-CamelCase.
   *
   * @see <a href="https://docs.oracle.com/javase/specs/jls/se9/html/jls-3.html#jls-3.8">Java Identifiers</a>
   */
  public static String toInstanceCase(final String string) {
    return transformNotReserved(string, '_', '\0', null, JavaIdentifiers::toInstanceCase0);
  }

  /**
   * Transforms a string into a legal Java [c]amelCase identifier, guaranteeing
   * the beginning string of upper-case characters (until the last) are changed
   * to lower case. All illegal characters are removed, and used to determine
   * the location of change of case.
   *
   * @param string The input string.
   * @return The string transformed to a legal Java [c]amelCase identifier.
   */
  private static StringBuilder toInstanceCase0(final String string, final char head, final Map<Character,String> sub) {
    final StringBuilder builder = toCamelCase0(string, head, sub);
    final int len = builder.length();
    if (len == 1) {
      if (Character.isUpperCase(builder.charAt(0)))
        builder.setCharAt(0, Character.toLowerCase(builder.charAt(0)));

      return builder;
    }

    int i;
    for (i = 0; i < len; i++) {
      final char ch = builder.charAt(i);
      if (('0' <= ch && ch <= '9') || ('a' <= ch && ch <= 'z'))
        break;
    }

    if (i <= 1)
      return builder;

    if (i == len)
      return Strings.toLowerCase(builder);

    for (int j = 0; j < i - 1; j++)
      builder.setCharAt(j, Character.toLowerCase(builder.charAt(j)));

    return builder;
  }

  private static String transform(final String string, final char head, final Map<Character,String> sub, final TriFunction<String,Character,Map<Character,String>,StringBuilder> transformer) {
    if (string == null || string.length() == 0)
      return string;

    final StringBuilder builder = transformer.apply(string, head, sub);
    if (Character.isUpperCase(string.charAt(0)))
      return builder.toString();

    builder.setCharAt(0, Character.toUpperCase(builder.charAt(0)));
    return builder.toString();
  }

  /**
   * Transforms a string into a valid Java Identifier in Title-CamelCase.
   * Strings that start with an illegal character are prepended with
   * <code>head</code>. All other illegal characters are substituted with the
   * string value mapped to the key of the character in <code>sub</code>. If
   * the mapping is missing, the illegal character is omitted.
   *
   * @param string The input string.
   * @param head The character that will be prepended to the string if the
   *        first character is not valid.
   * @param sub The mapping of illegal characters to their substitutions.
   * @return The string transformed to a valid Java Identifier in Title-CamelCase.
   *
   * @see <a href="https://docs.oracle.com/javase/specs/jls/se9/html/jls-3.html#jls-3.8">Java Identifiers</a>
   */
  public static String toClassCase(final String string, final char head, final Map<Character,String> sub) {
    return transform(string, head, sub, JavaIdentifiers::toCamelCase0);
  }

  /**
   * Transforms a string into a valid Java Identifier in Title-CamelCase.
   * Strings that start with an illegal character are prepended with
   * <code>head</code>. All other illegal characters are omitted.
   *
   * @param string The input string.
   * @param head The character that will be prepended to the string if the
   *        first character is not valid.
   * @return The string transformed to a valid Java Identifier in Title-CamelCase.
   *
   * @see <a href="https://docs.oracle.com/javase/specs/jls/se9/html/jls-3.html#jls-3.8">Java Identifiers</a>
   */
  public static String toClassCase(final String string, final char head) {
    return transform(string, head, null, JavaIdentifiers::toCamelCase0);
  }

  /**
   * Transforms a string into a valid Java Identifier in Title-CamelCase.
   * Strings that start with an illegal character are prepended with
   * <code>X</code>. All other illegal characters are omitted.
   *
   * @param string The input string.
   * @return The string transformed to a valid Java Identifier in Title-CamelCase.
   *
   * @see <a href="https://docs.oracle.com/javase/specs/jls/se9/html/jls-3.html#jls-3.8">Java Identifiers</a>
   */
  public static String toClassCase(final String string) {
    return transform(string, 'X', null, JavaIdentifiers::toCamelCase0);
  }

  private JavaIdentifiers() {
  }
}