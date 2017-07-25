/* Copyright (c) 2016 lib4j
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

import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;

public final class Locales {
  /**
   * Convert a string based locale into a Locale Object.
   * Assumes the string has form "{language}_{country}_{variant}".
   * Examples: "en", "de_DE", "_GB", "en_US_WIN", "de__POSIX", "fr_MAC"
   *
   * @param string
   *          The String
   * @return the Locale
   */
  public static Locale parse(String string) {
    if (string == null)
      return null;

    string = string.trim();
    if ("default".equals(string.toLowerCase()))
      return Locale.getDefault();

    // Extract language
    final int languageIndex = string.indexOf('_');
    if (languageIndex == -1) // No further "_" so is "{language}" only
      return new Locale(string, "");

    final String language = string.substring(0, languageIndex);

    // Extract country
    int countryIndex = string.indexOf('_', languageIndex + 1);
    if (countryIndex == -1) // No further "_" so is "{language}_{country}"
      return new Locale(language, string.substring(languageIndex + 1));

    // Assume all remaining is the variant so is "{language}_{country}_{variant}"
    return new Locale(language, string.substring(languageIndex + 1, countryIndex), string.substring(countryIndex + 1));
  }

  public static Locale[] parse(final Collection<String> strings) {
    if (strings == null)
      return null;

    final Locale[] locales = new Locale[strings.size()];
    int i = 0;
    for (final String string : strings)
      locales[i++] = parse(string);

    return locales;
  }

  public static Locale[] parse(final Enumeration<String> enumeration) {
    return parse(enumeration, 0);
  }

  private static Locale[] parse(final Enumeration<String> enumeration, final int depth) {
    if (!enumeration.hasMoreElements())
      return new Locale[depth];

    final Locale locale = parse(enumeration.nextElement());
    final Locale[] locales = parse(enumeration, depth + 1);
    locales[depth] = locale;
    return locales;
  }

  private Locales() {
  }
}