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

import org.junit.Assert;
import org.junit.Test;
import org.lib4j.util.JavaIdentifiers;

public class JavaIdentifiersTest {
  @Test
  public void testIdentifier() {
    Assert.assertEquals(null, JavaIdentifiers.toIdentifier(null));
    Assert.assertEquals("", JavaIdentifiers.toIdentifier(""));
    Assert.assertEquals("foo$bar", JavaIdentifiers.toIdentifier("foo$bar"));
    Assert.assertEquals("foo_bar", JavaIdentifiers.toIdentifier("foo_bar"));
    Assert.assertEquals("foobar", JavaIdentifiers.toIdentifier("foo-bar"));
    Assert.assertEquals("foobar", JavaIdentifiers.toIdentifier("foo*bar"));
    Assert.assertEquals("foobar", JavaIdentifiers.toIdentifier("foo%bar"));
    Assert.assertEquals("foobar", JavaIdentifiers.toIdentifier("foo bar"));
    Assert.assertEquals("foobar", JavaIdentifiers.toIdentifier("foo bar"));
    Assert.assertEquals("_2foobar", JavaIdentifiers.toIdentifier("2foo bar"));
    Assert.assertEquals("_abstract", JavaIdentifiers.toIdentifier("abstract"));
    Assert.assertEquals("_do", JavaIdentifiers.toIdentifier("do"));
    Assert.assertEquals("_foo", JavaIdentifiers.toIdentifier("_foo"));
  }

  @Test
  public void testToPackageCase() {
    Assert.assertEquals(null, JavaIdentifiers.toPackageCase(null));
    Assert.assertEquals("", JavaIdentifiers.toPackageCase(""));
    Assert.assertEquals("hyphenated_name", JavaIdentifiers.toPackageCase("hyphenated-name"));
    Assert.assertEquals("int_", JavaIdentifiers.toPackageCase("int"));
    Assert.assertEquals("_123name", JavaIdentifiers.toPackageCase("123name"));
  }

  @Test
  public void testToCamelCase() {
    Assert.assertEquals(null, JavaIdentifiers.toCamelCase(null));
    Assert.assertEquals("", JavaIdentifiers.toCamelCase(""));
    Assert.assertEquals("fooBar", JavaIdentifiers.toCamelCase("_foo_bar"));
    Assert.assertEquals("fooBar", JavaIdentifiers.toCamelCase("foo_bar"));
    Assert.assertEquals("FOOBarFoO", JavaIdentifiers.toCamelCase("FOO_bar_foO"));
    Assert.assertEquals("foo", JavaIdentifiers.toCamelCase("foo_"));
    Assert.assertEquals("FOo", JavaIdentifiers.toCamelCase("FOo"));
    Assert.assertEquals("FOoBAr", JavaIdentifiers.toCamelCase("FOo_bAr"));
    Assert.assertEquals("x2FOoBAr", JavaIdentifiers.toCamelCase("2FOo_bAr"));
    Assert.assertEquals("xabstract", JavaIdentifiers.toCamelCase("abstract"));
    Assert.assertEquals("xdo", JavaIdentifiers.toCamelCase("do"));
  }

  @Test
  public void testToInstanceCase() {
    Assert.assertEquals(null, JavaIdentifiers.toInstanceCase(null));
    Assert.assertEquals("", JavaIdentifiers.toInstanceCase(""));
    Assert.assertEquals("a", JavaIdentifiers.toInstanceCase("a"));
    Assert.assertEquals("xml", JavaIdentifiers.toInstanceCase("XML"));
    Assert.assertEquals("a", JavaIdentifiers.toInstanceCase("A"));
    Assert.assertEquals("fooBar", JavaIdentifiers.toInstanceCase("_foo_bar"));
    Assert.assertEquals("fooBar", JavaIdentifiers.toInstanceCase("foo_bar"));
    Assert.assertEquals("fooBarFoO", JavaIdentifiers.toInstanceCase("FOO_bar_foO"));
    Assert.assertEquals("foo", JavaIdentifiers.toInstanceCase("foo_"));
    Assert.assertEquals("fOo", JavaIdentifiers.toInstanceCase("FOo"));
    Assert.assertEquals("fOoBAr", JavaIdentifiers.toInstanceCase("FOo_bAr"));
    Assert.assertEquals("_2FOoBAr", JavaIdentifiers.toInstanceCase("2FOo_bAr"));
    Assert.assertEquals("_abstract", JavaIdentifiers.toInstanceCase("abstract"));
    Assert.assertEquals("_do", JavaIdentifiers.toInstanceCase("do"));
  }

  @Test
  public void testToClassCase() {
    Assert.assertEquals(null, JavaIdentifiers.toClassCase(null));
    Assert.assertEquals("", JavaIdentifiers.toClassCase(""));
    Assert.assertEquals("FooBar", JavaIdentifiers.toClassCase("_foo_bar"));
    Assert.assertEquals("FooBar", JavaIdentifiers.toClassCase("foo_bar"));
    Assert.assertEquals("FOOBarFoO", JavaIdentifiers.toClassCase("FOO_bar_foO"));
    Assert.assertEquals("Foo", JavaIdentifiers.toClassCase("foo_"));
    Assert.assertEquals("FOo", JavaIdentifiers.toClassCase("FOo"));
    Assert.assertEquals("FOoBAr", JavaIdentifiers.toClassCase("FOo_bAr"));
    Assert.assertEquals("X2FOoBAr", JavaIdentifiers.toClassCase("2FOo_bAr"));
    Assert.assertEquals("Abstract", JavaIdentifiers.toClassCase("abstract"));
    Assert.assertEquals("Do", JavaIdentifiers.toClassCase("do"));
  }
}