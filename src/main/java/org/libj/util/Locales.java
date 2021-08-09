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

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;

import org.libj.lang.Assertions;

/**
 * Utility functions for operations pertaining to {@link Locale}.
 */
public final class Locales {
  /**
   * Returns {@code true} if the provided {@link Locale} specifies an ISO
   * country and ISO language, otherwise {@code false}.
   *
   * @param locale The {@link Locale}.
   * @return {@code true} if the provided {@link Locale} specifies an ISO
   *         country and ISO language, otherwise {@code false}.
   */
  public static boolean isIso(final Locale locale) {
    if (!isIsoCountry(locale))
      return false;

    if (!isIsoLanguage(locale))
      return false;

    return true;
  }

  /**
   * Returns {@code true} if the provided {@link Locale} specifies an ISO
   * country, otherwise {@code false}.
   *
   * @param locale The {@link Locale}.
   * @return {@code true} if the provided {@link Locale} specifies an ISO
   *         country, otherwise {@code false}.
   */
  public static boolean isIsoCountry(final Locale locale) {
    if (locale == null)
      return false;

    final String country = locale.getCountry();
    if (country == null)
      return false;

    for (final String isoCountry : Locale.getISOCountries())
      if (country.equals(isoCountry))
        return true;

    return false;
  }

  /**
   * Returns {@code true} if the provided {@link Locale} specifies an ISO
   * language, otherwise {@code false}.
   *
   * @param locale The {@link Locale}.
   * @return {@code true} if the provided {@link Locale} specifies an ISO
   *         language, otherwise {@code false}.
   */
  public static boolean isIsoLanguage(final Locale locale) {
    if (locale == null)
      return false;

    final String language = locale.getLanguage();
    if (language == null)
      return false;

    for (final String isoLanguage : Locale.getISOLanguages())
      if (language.equals(isoLanguage))
        return true;

    return false;
  }

  /**
   * Returns a {@link Locale} representation of a string based locale that is in
   * the form of RFC 1766 (i.e. {@code "{language}-{country}"}), or {@code null}
   * if the specified string is null. Examples: {@code "en"}, {@code "fr-CH"},
   * {@code "i-cherokee"}.
   *
   * @param str The string.
   * @return A {@link Locale} representation of a string based locale that has
   *         the form {@code "{language}_{country}_{variant}"}, or {@code null}
   *         if the specified string is null.
   */
  public static Locale fromRFC1766(String str) {
    if (str == null)
      return null;

    str = str.trim();
    if (str.length() == 0)
      return null;

    final int dash = str.indexOf('-');
    if (dash == 0 || dash == str.length() - 1)
      return null;

    return dash < 0 ? new Locale(str) : new Locale(str.substring(0, dash), str.substring(dash + 1));
  }

  /**
   * Returns a {@link Locale} representation of a string based locale that has
   * the form {@code "{language}_{country}_{variant}"}, or {@code null} if the
   * specified string is null. Examples: {@code "en"}, {@code "de_DE"},
   * {@code "_GB"}, {@code "en_US_WIN"}, {@code "de__POSIX"}, {@code "fr_MAC"}.
   *
   * @param str The string.
   * @return A {@link Locale} representation of a string based locale that has
   *         the form {@code "{language}_{country}_{variant}"}, or {@code null}
   *         if the specified string is null.
   */
  public static Locale parse(final String str) {
    if (str == null)
      return null;

    final String string = str.trim();
    if ("default".equals(string.toLowerCase()))
      return Locale.getDefault();

    // Extract language
    final int languageIndex = string.indexOf('_');
    if (languageIndex < 0) { // No further "_" so is "{language}" only
      string.chars().forEach(c -> {
        if (!Character.isLetterOrDigit(c))
          throw new IllegalArgumentException(string);
      });

      return new Locale(string, "");
    }

    final String language = string.substring(0, languageIndex);
    language.chars().forEach(c -> {
      if (!Character.isLetterOrDigit(c))
        throw new IllegalArgumentException(string);
    });

    // Extract country
    final int countryIndex = string.indexOf('_', languageIndex + 1);
    if (countryIndex < 0) // No further "_" so is "{language}_{country}"
      return new Locale(language, string.substring(languageIndex + 1));

    // Assume all remaining is the variant so is "{language}_{country}_{variant}"
    return new Locale(language, string.substring(languageIndex + 1, countryIndex), string.substring(countryIndex + 1));
  }

  /**
   * Returns an array of {@link Locale} objects that represent the string based
   * locale elements in {@code strings} that have the form
   * {@code "{language}_{country}_{variant}"}. Examples: {@code "en"},
   * {@code "de_DE"}, {@code "_GB"}, {@code "en_US_WIN"}, {@code "de__POSIX"},
   * {@code "fr_MAC"}.
   *
   * @param strings The {@link Collection} of strings.
   * @return An array of {@link Locale} objects that represent the string based
   *         locale elements in {@code strings} that have the form
   *         {@code "{language}_{country}_{variant}"}.
   * @throws IllegalArgumentException If {@code strings} is null.
   */
  public static Locale[] parse(final Collection<String> strings) {
    final Locale[] locales = new Locale[Assertions.assertNotNull(strings).size()];
    final Iterator<String> iterator = strings.iterator();
    for (int i = 0; iterator.hasNext(); ++i)
      locales[i] = parse(iterator.next());

    return locales;
  }

  /**
   * Returns an array of {@link Locale} objects that represent the string based
   * locale elements in {@code enumeration} that have the form
   * {@code "{language}_{country}_{variant}"}. Examples: {@code "en"},
   * {@code "de_DE"}, {@code "_GB"}, {@code "en_US_WIN"}, {@code "de__POSIX"},
   * {@code "fr_MAC"}.
   *
   * @param enumeration The {@link Enumeration} of strings.
   * @return An array of {@link Locale} objects that represent the string based
   *         locale elements in {@code strings} that have the form
   *         {@code "{language}_{country}_{variant}"}.
   * @throws IllegalArgumentException If {@code enumeration} is null.
   */
  public static Locale[] parse(final Enumeration<String> enumeration) {
    return parse(enumeration, 0);
  }

  private static Locale[] parse(final Enumeration<String> enumeration, final int depth) {
    if (!Assertions.assertNotNull(enumeration).hasMoreElements())
      return new Locale[depth];

    final Locale locale = parse(enumeration.nextElement());
    final Locale[] locales = parse(enumeration, depth + 1);
    locales[depth] = locale;
    return locales;
  }

  private Locales() {
  }
}