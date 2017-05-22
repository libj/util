/* Copyright (c) 2008 lib4j
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

package org.safris.commons.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class ELsTest {
  @Test
  public void testDereference() {
    final Map<String,String> variables = new HashMap<String,String>();
    variables.put("right", "RIGHT");
    variables.put("left", "LEFT");
    variables.put("middle", "MIDDLE");

    final Map<String,String> map = new HashMap<String,String>();
    map.put("this string has a token on the ${right}", "this string has a token on the RIGHT");
    map.put("${left} token here", "LEFT token here");
    map.put("something in the ${middle} of this string", "something in the MIDDLE of this string");

    for (final Map.Entry<String,String> entry : map.entrySet())
      Assert.assertEquals(ELs.dereference(entry.getKey(), variables), entry.getValue());

    Assert.assertNull(ELs.dereference(null, variables));
    Assert.assertNull(ELs.dereference(null, null));
    final String same = "string with ${a} variable";
    Assert.assertEquals(ELs.dereference(same, null), same);

    try {
      ELs.dereference("expect an ${exception here", variables);
      Assert.fail("Expected a ExpressionFormatException");
    }
    catch (final ExpressionFormatException e) {
    }
  }
}