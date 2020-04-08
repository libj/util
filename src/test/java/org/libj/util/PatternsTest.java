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

import static org.junit.Assert.*;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.junit.Test;

public class PatternsTest {
  @Test
  public void testGetGroupNames() {
    assertArrayEquals(null, Patterns.getGroupNames(Pattern.compile("test")));
    assertArrayEquals(new String[] {"one"}, Patterns.getGroupNames(Pattern.compile("(?<one>[a-z])")));
    assertArrayEquals(new String[] {"one", "two"}, Patterns.getGroupNames(Pattern.compile("(?<one>[a-z])(?<two>[a-z])")));
    assertArrayEquals(new String[] {"one", "two", "three"}, Patterns.getGroupNames(Pattern.compile("(?<one>[a-z])(?<two>[a-z](?<three>[a-z]))")));
  }

  private static void assertUnescape(final String expected, final String test) {
    assertEquals(expected, Patterns.unescape(test));

    final String beforeExpected;
    final String afterExpected;
    final String bothExpected;
    if (expected == null) {
      beforeExpected = null;
      afterExpected = null;
      bothExpected = null;
    }
    else {
      beforeExpected = expected.startsWith("^") ? "^ " + expected.substring(1) : " " + expected;
      afterExpected = expected.endsWith("$") ? expected.substring(0, expected.length() - 1) + " $" : expected + " ";
      bothExpected = (beforeExpected.endsWith("$") ? beforeExpected.substring(0, beforeExpected.length() - 1) : beforeExpected) + " " + (afterExpected.startsWith("^") ? afterExpected.substring(1) : afterExpected);
    }

    final String beforeTest = test.startsWith("^") ? "^ " + test.substring(1) : " " + test;
    final String afterTest = test.endsWith("$") ? test.substring(0, test.length() - 1) + " $" : test + " ";
    final String bothTest = (beforeTest.endsWith("$") ? beforeTest.substring(0, beforeTest.length() - 1) : beforeTest) + " " + (afterTest.startsWith("^") ? afterTest.substring(1) : afterTest);

    assertEquals(beforeExpected, Patterns.unescape(beforeTest));
    assertEquals(afterExpected, Patterns.unescape(afterTest));
    assertEquals(bothExpected, Patterns.unescape(bothTest));
  }

  private static void assertLastIndexOfUnEscaped(final int expected, final String str) {
    assertEquals(expected, Patterns.lastIndexOfUnEscaped(str, ')', 0, str.length()));
  }

  @Test
  public void testLastIndexOfUnEscaped() {
    assertLastIndexOfUnEscaped(-1, "");
    assertLastIndexOfUnEscaped(0, ")");
    assertLastIndexOfUnEscaped(1, " )");
    assertLastIndexOfUnEscaped(1, " ) ");
    assertLastIndexOfUnEscaped(-1, " \\) ");
    assertLastIndexOfUnEscaped(3, " \\\\) ");
    assertLastIndexOfUnEscaped(-1, " \\Q)\\E ");
    assertLastIndexOfUnEscaped(-1, " \\Q) ");
    assertLastIndexOfUnEscaped(7, " \\Q)\\E)) ");
  }

  @Test
  public void testUnescapeClass() {
    assertNull(Patterns.unescapeClass("", '\0', 0, 0));
    assertNull(Patterns.unescapeClass("\\", '\0', 0, 0));
    assertEquals("\\", new String(Patterns.unescapeClass("\\\\", '\0', 0, 0)));
    assertEquals("\\", new String(Patterns.unescapeClass("\\\\", '\0', 0, 0)));
    assertEquals("\\", new String(Patterns.unescapeClass("\\\\\\", '\0', 0, 0)));
    assertEquals("abc", new String(Patterns.unescapeClass("abc", '\0', 0, 0)));
    assertEquals("abc", new String(Patterns.unescapeClass("\\abc", '\0', 0, 0)));
    assertEquals("abc", new String(Patterns.unescapeClass("a\\bc", '\0', 0, 0)));
    assertEquals("abc", new String(Patterns.unescapeClass("ab\\c", '\0', 0, 0)));
    assertEquals("abc", new String(Patterns.unescapeClass("abc\\", '\0', 0, 0)));
    assertEquals("abc\\", new String(Patterns.unescapeClass("abc\\\\", '\0', 0, 0)));
  }

  @Test
  public void testPosixCharacterClasses() {
    assertUnescape(null, "\\p{Alnum}");
    assertUnescape(null, "\\p{Alpha}");
    assertUnescape(null, "\\p{ASCII}");
    assertUnescape(null, "\\p{Blank}");
    assertUnescape(null, "\\p{Cntrl}");
    assertUnescape(null, "\\p{Digit}");
    assertUnescape(null, "\\p{Graph}");
    assertUnescape(null, "\\p{InGreek}");
    assertUnescape(null, "\\p{IsAlphabetic}");
    assertUnescape(null, "\\p{IsLatin}");
    assertUnescape(null, "\\p{javaLowerCase}");
    assertUnescape(null, "\\p{javaMirrored}");
    assertUnescape(null, "\\p{javaUpperCase}");
    assertUnescape(null, "\\p{javaWhitespace}");
    assertUnescape(null, "\\p{Lower}");
    assertUnescape(null, "\\p{Lu}");
    assertUnescape(null, "\\p{Print}");
    assertUnescape(null, "\\p{Punct}");
    assertUnescape(null, "\\p{Sc}");
    assertUnescape(null, "\\p{Space}");
    assertUnescape(null, "\\p{Upper}");
    assertUnescape(null, "\\p{XDigit}");

    try {
      assertUnescape(null, "\\p{AlphaX}");
      fail("Expected PatternSyntaxException");
    }
    catch (final PatternSyntaxException e) {
    }
    try {
      assertUnescape(null, "\\p{Alpha");
      fail("Expected PatternSyntaxException");
    }
    catch (final PatternSyntaxException e) {
    }
  }

  @Test
  public void testQuantifier() {
    assertUnescape(null, "a?");
    assertUnescape("a?", "a\\?");

    assertUnescape(null, "a*");
    assertUnescape("a*", "a\\*");

    assertUnescape(null, "a+");
    assertUnescape("a+", "a\\+");

    assertUnescape(null, "a{2}");
    assertUnescape("a", "a{1}");

    assertUnescape("a", "a{1}+");
    assertUnescape("a", "a{1}?");
    assertUnescape("aa", "a{1}a");
    assertUnescape("aa", "a{1}+a");
    assertUnescape("aa", "a{1}?a");
    try {
      assertUnescape(null, "a{1}*");
      fail("Expected PatternSyntaxException");
    }
    catch (final PatternSyntaxException e) {
    }
    try {
      assertUnescape(null, "a{1}+*");
      fail("Expected PatternSyntaxException");
    }
    catch (final PatternSyntaxException e) {
    }

    assertUnescape(null, "a{1,}");
    assertUnescape(null, "a{2,}");
    assertUnescape(null, "a{1,2}");
    assertUnescape("a", "a{1,1}");

    assertUnescape("a{1", "a{1");
    assertUnescape("a{2", "a{2");
    assertUnescape("a{2,2", "a{2,2");
    assertUnescape("a{1,2", "a{1,2");
    assertUnescape("a{1,,}", "a{1,,}");
    assertUnescape("a{,}", "a{,}");
    assertUnescape("a{,,}", "a{,,}");

    assertUnescape("a{a}", "a{a}");
    assertUnescape("a{1{", "a{1{");
    assertUnescape("a{2{", "a{2{");
  }

  @Test
  public void testCharacterClasses() {
    assertUnescape("", "[]");

    assertUnescape(null, "[ab]");
    assertUnescape("a", "[aa]");
    assertUnescape("a", "[aaa]");

    assertUnescape(null, "[a-b]");
    assertUnescape("a", "[a-a]");

    assertUnescape(null, "[^ab]");
    assertUnescape(null, "[^a]");
    assertUnescape("a", "[a]");

    assertUnescape(null, "[a-aa-b]");
    assertUnescape("a", "[a-aa-a]");

    assertUnescape(null, "[a-z&&[def]]");
    assertUnescape("a", "[a-a&&[aaa]]");

    assertUnescape(null, "[a-a&&[aaa]&&[^a]]");
    assertUnescape("a", "[a-a&&[aaa]&&[a]]");
    assertUnescape("a", "[a-a&&[aaa]&&a]");

    assertUnescape("[]", "\\[]");
    assertUnescape("[]", "[\\]");
    assertUnescape("[]", "\\[\\]");
    assertUnescape("[a]", "\\[a]");
    assertUnescape("[ab]", "[ab\\]");
  }

  @Test
  public void testComplex() {
    assertUnescape(null, "[ab]\\p{Alpha}");
    assertUnescape("a", "^[a]$");
    assertUnescape(null, "^[ab]\\p{Alpha}$");
    try {
      assertUnescape(null, "^[ab]\\p{AlphaX}$");
      fail("Expected PatternSyntaxException");
    }
    catch (final PatternSyntaxException e) {
    }
    assertUnescape("abc", "^[aa](b|b)(c|c)$");
    assertUnescape("a\\c", "^[aa](\\\\|\\\\)(c|c)$");
    assertUnescape("a)c", "^[aa](\\)|\\))(c|c)$");
    assertUnescape("a\\c", "^[aa](\\Q\\\\E|\\Q\\\\E)(c|c)$");
    assertUnescape(".{[", ".{[");
    assertUnescape(null, "[\\p{L}&&[^\\p{Lu}]]");
    assertUnescape(null, "num.+");
  }

  @Test
  public void testEscapes() {
    assertUnescape("(", "\\(");
    assertUnescape(")", "\\)");
    assertUnescape("*", "\\*");
    assertUnescape("+", "\\+");
    assertUnescape(".", "\\.");
    assertUnescape("?", "\\?");
    assertUnescape("[", "\\[");
    assertUnescape("\\", "\\\\");
    assertUnescape("]", "\\]");
    assertUnescape("{", "\\{");
    assertUnescape("|", "\\|");
    assertUnescape("}", "\\}");
  }

  private static void assertReduceGroup(final String expected, final String test) {
    assertEquals(expected, reduceGroup(test));
    assertEquals(expected != null ? expected + " " : null, reduceGroup("(" + test + ") "));
    assertEquals(expected != null ? " " + expected : null, reduceGroup(" (" + test + ")"));
    assertEquals(expected != null ? " " + expected + " " : null, reduceGroup(" (" + test + ") "));
  }

  private static String reduceGroup(final String test) {
    final StringBuilder builder = new StringBuilder(test);
    final int end = Patterns.reduceGroups(builder, 0, builder.length());
    return end == -1 ? null : builder.toString();
  }

  @Test
  public void testReduceGroups1() {
    assertReduceGroup("y", "y");
    assertReduceGroup("\\", "\\\\");

    assertReduceGroup("y", "y|y");
    assertReduceGroup(null, "y|n");
    assertReduceGroup("y", "y|y|y");
    assertReduceGroup(null, "y|y|n");

    assertReduceGroup("\\", "\\\\|\\\\");
    assertReduceGroup("|\\", "\\|\\\\");

    assertReduceGroup("y", "y|(y)");
    assertReduceGroup(null, "y|(n)");
    assertReduceGroup("y", "y|y|(y)");
    assertReduceGroup(null, "y|y|(n)");

    assertReduceGroup("y", "(y)|y");
    assertReduceGroup(null, "(y)|n");
    assertReduceGroup("y", "(y)|(y)|y");
    assertReduceGroup(null, "(y)|(y)|n");

    assertReduceGroup("y", "(y)|(y)");
    assertReduceGroup(null, "(y)|(n)");
    assertReduceGroup("y", "(y)|(y)|(y)");
    assertReduceGroup(null, "(y)|(y)|(n)");

    assertReduceGroup("y", "(y)|(y|y)|((y)|((y)|(y)))");
    assertReduceGroup(null, "(y)|(y|y)|((n)|((y)|(y)))");
  }

  @Test
  public void testReduceGroups2() {
    assertReduceGroup("ab", "ab");

    assertReduceGroup("ab", "ab|ab");
    assertReduceGroup(null, "ab|ac");

    assertReduceGroup("ab", "ab|ab|ab");
    assertReduceGroup(null, "ab|ab|ac");

    assertReduceGroup("ab", "ab|a(b)");
    assertReduceGroup("ab", "ab|(a)b");
    assertReduceGroup("ab", "a(b)|ab");
    assertReduceGroup("ab", "(a)b|ab");

    assertReduceGroup("ab", "(ab)|ab");
    assertReduceGroup("ab", "ab|(ab)");
    assertReduceGroup("ab", "(ab)|(ab)");
    assertReduceGroup("ab", "(a(b))|(ab)");
    assertReduceGroup("ab", "(ab)|(a(b))");
    assertReduceGroup("ab", "((ab))|(a(b))");
    assertReduceGroup("ab", "((ab))|((a(b)))");
    assertReduceGroup(null, "((ac))|((a(b)))");
  }

  @Test
  public void testReduceGroups3() {
    assertReduceGroup("abc", "abc");

    assertReduceGroup("abc", "abc|a(b)c");
    assertReduceGroup("abc", "abc|(a)bc");
    assertReduceGroup("abc", "ab(c)|abc");
    assertReduceGroup("abc", "a(b)c|abc");
    assertReduceGroup("abc", "(a)bc|abc");
    assertReduceGroup(null, "(z)bc|abc");
  }

  @Test
  public void testReduceGroups4() {
    assertReduceGroup("abc", "abc");
    assertReduceGroup("abc", "abc|a(b)c");
    assertReduceGroup("abc", "a((b)c)|(a(b)c)");
    assertReduceGroup("abc", "a((b|b)c)|(a(b|b)c)");
  }

  @Test
  public void testReduceGroupsX() {
    assertReduceGroup("a", "(a|a)|(a|a)");
    assertReduceGroup("a", "(a|a)|(a)");
    assertReduceGroup("ab", "(a(b|b))|(ab)");
    assertReduceGroup("ab", "((a|a)(b|b))|(((a)(b))");
    assertReduceGroup(null, "((a|a)(b|b))|(((a)|(b))");
  }

  @Test
  public void testUnescapeGroupsError() {
    assertUnescape(null, "(|");
    assertUnescape(null, "|)");
    assertUnescape(null, "(a))");
  }

  @Test
  public void testUnescapeSpecial() {
    assertUnescape("abc", "(?<name>abc)");
    assertUnescape("abc", "(?:abc)");
    assertUnescape("abc", "(?idmsux-idmsux:abc)");
    assertUnescape("abc", "(?=abc)");
    assertUnescape("abc", "(?!abc)");
    assertUnescape("abc", "(?<=abc)");
    assertUnescape("abc", "(?<!abc)");
    assertUnescape("abc", "(?>abc)");

    try {
      assertUnescape(null, "(?<nameabc)");
      fail("Expected PatternSyntaxException");
    }
    catch (final PatternSyntaxException e) {
    }
    try {
      assertUnescape(null, "(?\\:abc)");
      fail("Expected PatternSyntaxException");
    }
    catch (final PatternSyntaxException e) {
    }
    try {
      assertUnescape(null, "(??abc)");
      fail("Expected PatternSyntaxException");
    }
    catch (final PatternSyntaxException e) {
    }

    assertUnescape("abcdef", "(?<name1>abc)((?<name2>def)|(?<name3>def))");
  }
}