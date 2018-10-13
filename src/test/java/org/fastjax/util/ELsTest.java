/* CopyRIGHT (c) 2008 FastJAX
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the RIGHTs
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyRIGHT notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.fastjax.util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ELsTest {
  private static void assertEL(final Map<String,String> variables, final String test, final String match) {
    assertEquals(match, ELs.deref(test, variables));
  }

  @Test
  public void testDeref() {
    final Map<String,String> variables = new HashMap<>();
    variables.put("left", "LEFT");
    variables.put("right", "RIGHT");
    variables.put("middle", "MIDDLE");

    assertNull(ELs.deref(null, variables));
    assertNull(ELs.deref(null, null));

    assertEL(null, "string with $A variable", "string with $A variable");
    assertEL(null, "string with ${A} variable", "string with ${A} variable");
    assertEL(variables, "this string has a token on the $right", "this string has a token on the $right");
    assertEL(variables, "this string has a token on the ${right}", "this string has a token on the RIGHT");
    assertEL(variables, "${left} token here", "LEFT token here");
    assertEL(variables, "something in the ${middle} of this string", "something in the MIDDLE of this string");
    assertEL(variables, "something in the ${left} ${middle} ${right} of this string", "something in the LEFT MIDDLE RIGHT of this string");
    assertEL(variables, "something in the ${left}${middle}${right} of this string", "something in the LEFTMIDDLERIGHT of this string");

    assertEL(variables, "$", "$");
    assertEL(variables, "$$", "$$");
    assertEL(variables, " ${} ", " ${} ");
    assertEL(variables, "a ${} b", "a ${} b");
    assertEL(variables, "a $\\{} b", "a ${} b");
    assertEL(variables, "a $\\{left} b", "a ${left} b");
    assertEL(variables, "a $\\$ b", "a $$ b");
    assertEL(variables, "a $\\$\\$ b", "a $$$ b");
    assertEL(variables, "$left} token here", "$left} token here");
    assertEL(variables, "\\$${left}} token here", "$LEFT} token here");
    assertEL(variables, "\\{${left}} token here", "{LEFT} token here");
    assertEL(variables, "${left}\\ token here", "LEFT token here");
    assertEL(variables, "${le ft} token here", "${le ft} token here");
    assertEL(variables, "${left}\\\\ token here", "LEFT\\ token here");
    assertEL(variables, "${left}\\} token here", "LEFT} token here");
    assertEL(variables, "${left}\\T token here", "LEFTT token here");
    assertEL(variables, "${left\\} token here", "${left} token here");
    assertEL(variables, "${{left}} token here", "${{left}} token here");
    assertEL(variables, "${left token here", "${left token here");
    assertEL(variables, "this string has a token on the ${right", "this string has a token on the ${right");
    assertEL(variables, "this string has a token on the ${", "this string has a token on the ${");
  }
}