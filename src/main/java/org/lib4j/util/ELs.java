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

package org.lib4j.util;

import java.util.Map;

public final class ELs {
  public static String dereference(String string, final Map<String,String> variables) throws ExpressionFormatException {
    if (string == null || string.length() == 0 || variables == null)
      return string;

    final StringBuilder buffer = new StringBuilder();
    int i = -1;
    int j = -1;
    while ((i = string.indexOf("${", i)) != -1) {
      j = string.indexOf("}", i + 2);
      if (j == -1)
        throw new ExpressionFormatException("There is an error in your expression: " + string);

      final String token = string.substring(i + 2, j);
      final String variable = variables.get(token);
      buffer.append(string.substring(0, i)).append(variable);
      string = string.substring(j + 1);
    }

    return buffer.append(string).toString();
  }

  private ELs() {
  }
}