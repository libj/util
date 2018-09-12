/* Copyright (c) 2008 FastJAX
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

package org.fastjax.util;

import java.util.Map;

public final class ELs {
  private static void appendVariable(final Map<String,String> variables, final StringBuilder builder, final StringBuilder var) {
    final String name = var.toString();
    final String value = variables == null ? null : variables.get(name);
    if (value != null)
      builder.append(value);
    else
      builder.append('$').append('{').append(name).append('}');

    var.setLength(0);
  }

  private static void appendNoMatch(final StringBuilder builder, final StringBuilder var, final char close) {
    builder.append('$').append('{');
    if (var.length() > 0) {
      builder.append(var.toString());
      var.setLength(0);
    }

    if (close != '\0')
      builder.append(close);
  }

  public static String deref(final String string, final Map<String,String> variables) {
    if (string == null || string.length() == 0)
      return string;

    final StringBuilder builder = new StringBuilder();
    final StringBuilder var = new StringBuilder();
    boolean escape = false;
    final char[] chars = string.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      char ch = chars[i];
      if (ch == '\\') {
        if (var.length() > 0) {
          builder.append('$').append('{').append(var.toString());
          var.setLength(0);
        }

        if (!(escape = !escape))
          builder.append(ch);
      }
      else if (!escape) {
        if (ch == '$') {
          if (var.length() > 0) {
            appendVariable(variables, builder, var);
          }

          if (++i == chars.length) {
            builder.append('$');
          }
          else {
            ch = chars[i];
            if (ch != '{') {
              var.setLength(0);
              builder.append('$');
              if (ch != '\\')
                builder.append(ch);
            }
            else if (++i == chars.length)
              appendNoMatch(builder, var, '\0');
            else {
              ch = chars[i];
              if ('a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z' || ch == '_')
                var.append(ch);
              else
                appendNoMatch(builder, var, ch);
            }
          }
        }
        else if (var.length() > 0) {
          if ('a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z' || '0' <= ch && ch <= '9' || ch == '_') {
            var.append(ch);
          }
          else if (ch != '}') {
            appendNoMatch(builder, var, ch);
          }
          else {
            appendVariable(variables, builder, var);
            if (ch != '}')
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

    if (var.length() > 0)
      appendNoMatch(builder, var, '\0');

    return builder.toString();
  }

  private ELs() {
  }
}