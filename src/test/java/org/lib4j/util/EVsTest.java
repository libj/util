/* Copyright (c) 2018 lib4j
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

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class EVsTest {
  private static void assertEV(final Map<String,String> variables, final String test, final String match) throws ParseException {
    Assert.assertEquals(match, EVs.deref(test, variables));
  }

  @Test
  public void testDeref() throws ParseException {
    final Map<String,String> variables = new HashMap<String,String>();
    variables.put("LEFT", "left");
    variables.put("RIGHT", "right");
    variables.put("MIDDLE", "middle");

    Assert.assertNull(EVs.deref(null, variables));
    Assert.assertNull(EVs.deref(null, null));

    assertEV(null, "string with $A variable", "string with  variable");
    assertEV(null, "string with ${A} variable", "string with  variable");
    assertEV(variables, "this string has a token on the $RIGHT", "this string has a token on the right");
    assertEV(variables, "this string has a token on the ${RIGHT}", "this string has a token on the right");
    assertEV(variables, "$LEFT token here", "left token here");
    assertEV(variables, "${LEFT} token here", "left token here");
    assertEV(variables, "something in the $MIDDLE of this string", "something in the middle of this string");
    assertEV(variables, "something in the ${MIDDLE} of this string", "something in the middle of this string");
    assertEV(variables, "something in the $LEFT $MIDDLE $RIGHT of this string", "something in the left middle right of this string");
    assertEV(variables, "something in the ${LEFT} ${MIDDLE} ${RIGHT} of this string", "something in the left middle right of this string");
    assertEV(variables, "something in the ${LEFT}${MIDDLE}${RIGHT} of this string", "something in the leftmiddleright of this string");

    assertEV(variables, "$", "$");
    assertEV(variables, " $ ", " $ ");
    assertEV(variables, "a $ b", "a $ b");
    assertEV(variables, "a $\\$ b", "a $$ b");
    assertEV(variables, "a $\\$\\$ b", "a $$$ b");
    assertEV(variables, "$LEFT} token here", "left} token here");
    assertEV(variables, "\\$$LEFT} token here", "$left} token here");
    assertEV(variables, "\\{$LEFT} token here", "{left} token here");
    assertEV(variables, "$LEFT\\ token here", "left token here");
    assertEV(variables, "$LEFT\\\\ token here", "left\\ token here");
    assertEV(variables, "$LEFT\\} token here", "left} token here");
    assertEV(variables, "$LEFT\\T token here", "leftT token here");

    try {
      assertEV(variables, "${LEFT token here", "left token here");
      Assert.fail("Expected a ParseException");
    }
    catch (final ParseException e) {
      if (!"${LEFT : bad substitution".equals(e.getMessage()))
        throw e;
    }

    try {
      assertEV(variables, "this string has a token on the ${RIGHT", "left token here");
      Assert.fail("Expected a ParseException");
    }
    catch (final ParseException e) {
      if (!"${RIGHT: bad substitution".equals(e.getMessage()))
        throw e;
    }

    try {
      assertEV(variables, "this string has a token on the ${", "left token here");
      Assert.fail("Expected a ParseException");
    }
    catch (final ParseException e) {
      if (!"${: bad substitution".equals(e.getMessage()))
        throw e;
    }

    try {
      assertEV(variables, "${{LEFT}} token here", "left token here");
      Assert.fail("Expected a ParseException");
    }
    catch (final ParseException e) {
      if (!"${{: bad substitution".equals(e.getMessage()))
        throw e;
    }

    try {
      EVs.deref("expect an $$ here", variables);
      Assert.fail("Expected a ParseException");
    }
    catch (final ParseException e) {
      if (!"$$: not supported".equals(e.getMessage()))
        throw e;
    }
  }
}