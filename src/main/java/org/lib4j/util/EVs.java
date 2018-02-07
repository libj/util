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
import java.util.Map;

public final class EVs {
  private static void appendVariable(final Map<String,String> variables, final StringBuilder builder, final StringBuilder var) {
    if (variables != null) {
      final String variable = variables.get(var.toString());
      if (variable != null)
        builder.append(variable);
    }

    var.setLength(0);
  }

  public static String deref(final String string, final Map<String,String> variables) throws ParseException {
    if (string == null || string.length() == 0)
      return string;

    final StringBuilder builder = new StringBuilder();
    final StringBuilder var = new StringBuilder();
    boolean escape = false;
    boolean bracket = false;
    final char[] chars = string.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      char ch = chars[i];
      if (ch == '\\') {
        if (var.length() > 0)
          appendVariable(variables, builder, var);

        if (!(escape = !escape))
          builder.append(ch);
      }
      else if (!escape) {
        if (ch == '$') {
          if (var.length() > 0)
            appendVariable(variables, builder, var);

          if (++i == chars.length) {
            builder.append('$');
          }
          else {
            ch = chars[i];
            if (ch == '$')
              throw new ParseException("$$: not supported", i);

            if (ch == '{') {
              bracket = true;
              if (++i == chars.length)
                throw new ParseException("${: bad substitution", i);

              ch = chars[i];
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
            appendVariable(variables, builder, var);
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
          appendVariable(variables, builder, var);

        builder.append(ch);
        escape = false;
      }
    }

    if (var.length() > 0) {
      if (bracket)
        throw new ParseException("${" + var + ": bad substitution", string.length());

      appendVariable(variables, builder, var);
    }

    return builder.toString();
  }

  private EVs() {
  }
}