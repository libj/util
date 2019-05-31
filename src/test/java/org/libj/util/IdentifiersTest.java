/* Copyright (c) 2017 LibJ
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

import java.util.Collections;
import java.util.function.Function;

import org.junit.Test;

public class IdentifiersTest {
  private static void test(final String expected, final Function<String,String> function, final String value) {
    final String identifier;
    try {
      identifier = function.apply(value);
    }
    catch (final NullPointerException e) {
      if (expected == null)
        return;

      throw e;
    }

    if (identifier != null && identifier.length() > 0)
      assertTrue(Identifiers.isValid(identifier));

    assertEquals(expected, identifier);
  }

  @Test
  public void testIsValid() {
    try {
      Identifiers.isValid(null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    assertFalse(Identifiers.isValid(""));
    assertFalse(Identifiers.isValid(".A"));
    assertTrue(Identifiers.isValid("A.A"));
    assertTrue(Identifiers.isValid(IdentifiersTest.class.getName()));
    assertFalse(Identifiers.isValid(IdentifiersTest.class.getName(), false));
    assertTrue(Identifiers.isValid(IdentifiersTest.class.getSimpleName(), false));
  }

  @Test
  public void testIdentifier() {
    test("a", Identifiers::toIdentifier, "a");
    test("XML", Identifiers::toIdentifier, "XML");
    test("A", Identifiers::toIdentifier, "A");
    test("HelloWorld", Identifiers::toIdentifier, "HelloWorld");

    try {
      Identifiers.toIdentifier("@foo", Collections.singletonMap('@', "1!"));
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }
    try {
      Identifiers.toIdentifier("@foo", '\0', c -> c == '@' ? "1!" : null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }
    test("foo", Identifiers::toIdentifier, "@fo@o");
    test("foo", Identifiers::toIdentifier, "@foo");
    assertEquals("$foo", Identifiers.toIdentifier("@foo", '_', '$'));
    assertEquals("_1$foo", Identifiers.toIdentifier("@foo", Collections.singletonMap('@', "1$")));
    assertEquals("_1$foo", Identifiers.toIdentifier("@foo", c -> c == '@' ? "1$" : null));
    assertEquals("$1foo", Identifiers.toIdentifier("@foo", Collections.singletonMap('@', "$1")));
    assertEquals("$1foo", Identifiers.toIdentifier("@foo", c -> c == '@' ? "$1" : null));

    test(null, Identifiers::toIdentifier, null);
    test("", Identifiers::toIdentifier, "");
    test("foo$bar", Identifiers::toIdentifier, "foo$bar");
    test("foo_bar", Identifiers::toIdentifier, "foo_bar");
    test("foobar", Identifiers::toIdentifier, "foo-bar");
    test("foobar", Identifiers::toIdentifier, "foo*bar");
    test("foobar", Identifiers::toIdentifier, "foo%bar");
    test("foobar", Identifiers::toIdentifier, "foo bar");
    test("foobar", Identifiers::toIdentifier, "foo bar");
    test("_2foobar", Identifiers::toIdentifier, "2foo bar");
    test("_abstract", Identifiers::toIdentifier, "abstract");
    test("_do", Identifiers::toIdentifier, "do");
    test("_foo", Identifiers::toIdentifier, "_foo");

    try {
      Identifiers.toIdentifier("@foo", '\0', c -> "1$");
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
      assertEquals("Substitution \"1$\" contains illegal start character: '1'", e.getMessage());
    }

    try {
      Identifiers.toIdentifier("class", '\0');
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
      assertTrue(e.getMessage().startsWith("Unable to transform"));
    }

    assertEquals("_class", Identifiers.toIdentifier("class", '\0', Collections.singletonMap(null, "_")));
    assertEquals("_class", Identifiers.toIdentifier("class", '\0', c -> c == null ? "_" : null));
  }

  @Test
  public void testUnicode() {
    final char prefix = '\0';
    final Function<Character,String> substitutions = c -> c == null ? "_" : c != '_' ? "_" + Integer.toHexString(c) : "__";
    assertEquals("__", Identifiers.toInstanceCase("_", prefix, substitutions));
    assertEquals("_2e_2a", Identifiers.toInstanceCase(".*", prefix, substitutions));
  }

  @Test
  public void testToPackageCase() {
    test(null, Identifiers::toPackageCase, null);
    test("", Identifiers::toPackageCase, "");
    test("hyphenated_name", Identifiers::toPackageCase, "hyphenated-name");
    test("int_", Identifiers::toPackageCase, "int");
    test("_123name", Identifiers::toPackageCase, "123name");
  }

  @Test
  public void testToCamelCase() {
    test(null, Identifiers::toCamelCase, null);
    test("", Identifiers::toCamelCase, "");
    test("a", Identifiers::toCamelCase, "a");
    test("XML", Identifiers::toCamelCase, "XML");
    test("A", Identifiers::toCamelCase, "A");
    test("HelloWorld", Identifiers::toCamelCase, "HelloWorld");
    test("fooBar", Identifiers::toCamelCase, "_foo_bar");
    test("fooBar", Identifiers::toCamelCase, "foo_bar");
    test("FooBarFoO", Identifiers::toCamelCase, "FOO_bar_foO");
    test("foo", Identifiers::toCamelCase, "foo_");
    test("FOo", Identifiers::toCamelCase, "FOo");
    test("FOoBAr", Identifiers::toCamelCase, "FOo_bAr");
    test("x2FOoBAr", Identifiers::toCamelCase, "2FOo_bAr");
    test("xabstract", Identifiers::toCamelCase, "abstract");
    test("xdo", Identifiers::toCamelCase, "do");
  }

  @Test
  public void testToInstanceCase() {
    test(null, Identifiers::toInstanceCase, null);
    test("", Identifiers::toInstanceCase, "");
    test("a", Identifiers::toInstanceCase, "a");
    test("xml", Identifiers::toInstanceCase, "XML");
    test("a", Identifiers::toInstanceCase, "A");
    test("helloWorld", Identifiers::toInstanceCase, "HelloWorld");
    test("fooBar", Identifiers::toInstanceCase, "_foo_bar");
    test("fooBar", Identifiers::toInstanceCase, "foo_bar");
    test("fooBarFoO", Identifiers::toInstanceCase, "FOO_bar_foO");
    test("foo", Identifiers::toInstanceCase, "foo_");
    test("fOo", Identifiers::toInstanceCase, "FOo");
    test("fOoBAr", Identifiers::toInstanceCase, "FOo_bAr");
    test("_2FOoBAr", Identifiers::toInstanceCase, "2FOo_bAr");
    test("_abstract", Identifiers::toInstanceCase, "abstract");
    test("_do", Identifiers::toInstanceCase, "do");
  }

  @Test
  public void testToClassCase() {
    test(null, Identifiers::toClassCase, null);
    test("", Identifiers::toClassCase, "");
    test("A", Identifiers::toClassCase, "a");
    test("Xml", Identifiers::toClassCase, "XML");
    test("A", Identifiers::toClassCase, "A");
    test("HelloWorld", Identifiers::toClassCase, "HelloWorld");
    test("FooBar", Identifiers::toClassCase, "_foo_bar");
    test("FooBar", Identifiers::toClassCase, "foo_bar");
    test("FooBarFoO", Identifiers::toClassCase, "FOO_bar_foO");
    test("Foo", Identifiers::toClassCase, "foo_");
    test("FOo", Identifiers::toClassCase, "FOo");
    test("FOoBAr", Identifiers::toClassCase, "FOo_bAr");
    test("X2FOoBAr", Identifiers::toClassCase, "2FOo_bAr");
    test("Abstract", Identifiers::toClassCase, "abstract");
    test("Do", Identifiers::toClassCase, "do");
  }
}