/* Copyright (c) 2008 LibJ
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

import java.io.File;
import java.util.regex.Pattern;

import org.libj.lang.Strings;

/**
 * Utility functions that perform a variety of operations on {@link String}
 * objects representing either system paths (UNIX or Windows), or file URL paths
 * (resembling {@code file:/...}).
 */
public final class Paths {
  // https://www.oreilly.com/library/view/regular-expressions-cookbook/9781449327453/ch08s18.html
  private static final Pattern windowsPath = Pattern.compile("^(?:(?:[a-zA-Z]:|\\\\\\\\[a-z0-9_.$●-]+\\\\+[a-z0-9_.$●-]+)\\\\+|\\\\[^\\\\/:*?\"<>|\r\n]+\\\\?)(?:[^\\\\/:*?\"<>|\r\n]+\\\\+)*[^\\\\/:*?\"<>|\r\n]*$");

  private static final Pattern unixPath = Pattern.compile("^/+(?:[^\0/]+/*)*$");

  // FIXME: This can be converted to a char-by-char algorithm
  private static final Pattern urlPath = Pattern.compile("^(jar:)?file:(//(?:(?<ip>[0-9]{1,3}(\\.[0-9]{1,3}){3})|(?<host>[-0-9a-z\u00A0-\uFFFD]{1,63}(\\.[-0-9a-z\u00A0-\uFFFD]{1,63})*))?)?(?<path>/(%[0-9a-f][0-9a-f]|[-._!$&'()*+,:;=@~0-9a-zA-Z\u00A0-\uFFFD/?#])*)$");

  private static final Pattern absolute = Pattern.compile("^([a-zA-Z0-9]+:)?//.*$");

  /**
   * Tests whether the specified string is an URL that represents an absolute
   * local file path.
   * <p>
   * For a string to be considered an absolute local URL, it must:
   * <ol>
   * <li>Be a valid URL.</li>
   * <li>Specify the {@code "file"} protocol.</li>
   * <li>Specify a host that is either empty or {@code "localhost"}.</li>
   * </ol>
   *
   * @param path The path to test.
   * @return {@code true} if the specified string is an URL that represents an
   *         absolute local file path, otherwise {@code false}.
   * @throws NullPointerException If {@code path} is null.
   */
  public static boolean isAbsoluteLocalURL(final String path) {
    final int i = path.startsWith("file:/") ? 6 : path.startsWith("jar:file:/") ? 10 : -1;
    if (i == -1)
      return false;

    final int len = path.length();
    if ((len <= i || path.charAt(i) == '/') && (len <= (i + 1) || path.charAt(i + 1) != '/') && (len < (i + 11) || !"localhost/".equals(path.substring(i + 1, i + 11))))
      return false;

    return urlPath.matcher(path).matches() && (i != 10 || path.contains("!/"));
  }

  /**
   * Tests whether the specified string represents an absolute UNIX file path.
   * <p>
   * For a string to be considered an absolute UNIX file path, it must:
   * <ol>
   * <li>Start with a leading {@code '/'} character.</li>
   * <li>Not contain any {@code '\0'} characters.</li>
   * </ol>
   *
   * @param path The path to test.
   * @return {@code true} if the specified string represents an absolute UNIX
   *         file path, otherwise {@code false}.
   * @throws NullPointerException If {@code path} is null.
   */
  public static boolean isAbsoluteLocalUnix(final String path) {
    return unixPath.matcher(path).matches();
  }

  /**
   * Tests whether the specified string represents an absolute Windows file
   * path.
   * <p>
   * A valid Windows file path matches the following patterns:
   * <ol>
   * <li>Drive path:
   *
   * <pre>
   * {@code c:\folder\myfile.txt}
   * </pre>
   *
   * </li>
   * <li>UNC path (ip):
   *
   * <pre>
   * {@code \\123.123.123.123\share\folder\myfile.txt}
   * </pre>
   *
   * </li>
   * <li>UNC path (named):
   *
   * <pre>
   * {@code \\server\share\folder\myfile.txt}
   * </pre>
   *
   * </li>
   * </ol>
   *
   * @param path The path to test.
   * @return {@code true} if the specified string represents an absolute Windows
   *         file path, otherwise {@code false}.
   * @throws NullPointerException If {@code path} is null.
   */
  public static boolean isAbsoluteLocalWindows(final String path) {
    return windowsPath.matcher(path).matches();
  }

  /**
   * Tests whether the specified string represents an absolute local file path.
   * <p>
   * For a string to be considered an absolute local file path, it must:
   * <ol>
   * <li>Match a local file URL: {@link Paths#isAbsoluteLocalURL(String)}</li>
   * <li>Match a Windows path: {@link Paths#isAbsoluteLocalWindows(String)}</li>
   * <li>Match a UNIX path: {@link Paths#isAbsoluteLocalUnix(String)}</li>
   * </ol>
   *
   * @param path The path to test.
   * @return {@code true} if the specified string represents an absolute local
   *         file path, otherwise {@code false}.
   * @throws NullPointerException If {@code path} is null.
   */
  public static boolean isAbsoluteLocal(final String path) {
    return isAbsoluteLocalURL(path) || isAbsoluteLocalUnix(path) || isAbsoluteLocalWindows(path);
  }

  /**
   * Tests whether the specified string represents an absolute path.
   * <p>
   * <b>Note:</b> This method does not perform strict path validation, but
   * merely inspects its prefix to match known absolute path patterns.
   * <p>
   * This method performs the following tests to detect whether the specified
   * string is an absolute path:
   * <ol>
   * <li>If the string starts with {@code '/'} (UNIX paths).</li>
   * <li>If the string starts with {@code "?:\"}, where {@code ?} is a letter,
   * case insensitive (Windows paths).</li>
   * <li>If the string starts with {@code "file:/"}.</li>
   * <li>If the string starts with {@code "jar:file:/"}.</li>
   * <li>If the string starts with {@code "<protocol>://"}, where
   * {@code <protocol>} is any string matching {@code [a-zA-Z0-9]+}.</li>
   * </ol>
   *
   * @param path The string to test.
   * @return {@code true} if the specified string represents an absolute path,
   *         otherwise {@code false}.
   * @throws NullPointerException If {@code path} is null.
   */
  public static boolean isAbsolute(final String path) {
    if (path.charAt(0) == '/' || Character.isLetter(path.charAt(0)) && path.charAt(1) == ':' && path.charAt(2) == '\\' && Character.isLetter(path.charAt(3)))
      return true;

    if (path.startsWith("file:/") || path.startsWith("jar:file:/"))
      return true;

    return absolute.matcher(path).matches();
  }

  /**
   * Returns the protocol section of the specified path, or {@code null} if a
   * protocol section does not exist.
   *
   * @param path The path from which to get the protocol section.
   * @return The protocol section of the specified path, or {@code null} if a
   *         protocol does not exist.
   * @throws NullPointerException If {@code path} is null.
   */
  public static String getProtocol(final String path) {
    final int index = path.indexOf(":/");
    return index == -1 ? null : path.substring(0, index);
  }

  /**
   * Creates a path string from a parent pathname string and a child pathname
   * string.
   * <p>
   * This method operates with the following behavior:
   * <ol>
   * <li>If {@code child} is empty then this method returns {@code parent}.</li>
   * <li>If {@code parent} is null or empty then this method returns
   * {@code child}.</li>
   * <li>If {@code parent} does not have a trailing slash, and {@code child}
   * does not have a leading slash, one slash is added.</li>
   * <li>If {@code parent} has a trailing slash, and {@code child} has a leading
   * slash, one slash is discarded.</li>
   * <li>If {@code parent} is determined to be a Windows path, then the '\'
   * character is used as the name separator (in case a separator character must
   * be inserted); otherwise, '/' is used.
   * <p>
   * <blockquote><b>Note:</b> This method does not modify separator characters
   * in any parameter.</blockquote></li>
   * <li>If {@code child} is absolute then it is converted into a relative
   * pathname in a system-dependent way.</li>
   * <li>Otherwise {@code child} is resolved against {@code parent}.</li>
   * </ol>
   *
   * @param parent The parent pathname string.
   * @param child The child pathname string.
   * @return A path string from a parent pathname string and a child pathname
   *         string.
   * @throws NullPointerException If {@code child} is null.
   * @see Paths#isAbsoluteLocalWindows(String)
   */
  public static String newPath(final String parent, final String child) {
    if (child.length() == 0)
      return parent;

    if (parent == null || parent.length() == 0)
      return child;

    final char parentN = parent.charAt(parent.length() - 1);
    final char child0 = child.charAt(0);
    if (parentN == '/' || parentN == '\\')
      return parentN == child0 ? parent + child.substring(1) : parent + child;

    if (child0 == '/' || child0 == '\\')
      return parent + child;

    final char sep = isAbsoluteLocalWindows(parent) ? '\\' : '/';
    return parent + sep + child;
  }

  /**
   * Returns the canonical form of the specified path, where {@code ".."} and
   * {@code "."} path names are dereferenced, and redundant {@code '/'} path
   * separators are removed.
   * <p>
   * This implementation differs from {@link File#getCanonicalPath()} by only
   * canonicalizing the superficial form of the specified path. This
   * implementation does not perform filesystem interaction, for the purpose of
   * operations such as resolving symbolic links (on UNIX platforms).
   *
   * @param path The path to canonicalize.
   * @return The canonical form of the specified path, where {@code ".."} and
   *         {@code "."} path names are dereferenced, and redundant {@code '/'}
   *         path separators are removed.
   * @throws NullPointerException If {@code path} is null.
   */
  public static String canonicalize(final String path) {
    return canonicalize(new StringBuilder(path), isAbsoluteLocalWindows(path)).toString();
  }

  /**
   * Returns the canonical form of the specified path, where {@code ".."} and
   * {@code "."} path names are dereferenced, and redundant {@code '/'} path
   * separators are removed.
   * <p>
   * This implementation differs from {@link File#getCanonicalPath()} by only
   * canonicalizing the superficial form of the specified path. This
   * implementation does not perform filesystem interaction, for the purpose of
   * operations such as resolving symbolic links (on UNIX platforms).
   *
   * @param path The path to canonicalize.
   * @return The canonicalized {@code path} instance, where {@code ".."} and
   *         {@code "."} path names are dereferenced, and redundant {@code '/'}
   *         path separators are removed.
   * @throws NullPointerException If {@code path} is null.
   */
  public static StringBuilder canonicalize(final StringBuilder path) {
    return canonicalize(path, isAbsoluteLocalWindows(path.toString()));
  }

  private static final char[] windowsPrefix = {'\\', '\\'};

  private static StringBuilder canonicalize(final StringBuilder path, final boolean isWindows) {
    final int p = path.indexOf("://");
    final char[] prefix;
    if (p != -1) {
      prefix = new char[p + 3];
      path.getChars(0, p + 3, prefix, 0);
      path.delete(0, p + 3);
    }
    else if (!isWindows) {
      prefix = null;
    }
    else if (Strings.startsWith(path, "\\\\")) {
      prefix = windowsPrefix;
      path.delete(0, 2);
    }
    else if (path.charAt(1) == ':') {
      prefix = new char[] {Character.toLowerCase(path.charAt(0)), ':'};
      path.delete(0, 2);
    }
    else {
      prefix = null;
    }

    final String s = isWindows ? "\\" : "/";
    Strings.replaceAll(path, s + s, s);
    Strings.replaceAll(path, s + "." + s, s);
    if (Strings.startsWith(path, "." + s))
      path.delete(0, 2);

    if (Strings.endsWith(path, s + "."))
      path.delete(path.length() - 2, path.length());

    final String dotDot = s + ".." + s;
    for (int end, start = 0; (end = path.indexOf(dotDot, start)) != -1;) {
      start = path.lastIndexOf(s, end - 1);
      if (start != -1) {
        path.delete(start, end + 3);
      }
      else {
        if (end > 0)
          path.delete(0, end + 4);

        break;
      }
    }

    if (Strings.endsWith(path, s + "..")) {
      final int start = path.lastIndexOf(s, path.length() - 4);
      path.delete(start, path.length());
    }

    return prefix == null ? path : path.insert(0, prefix);
  }

  /**
   * Returns the pathname of the parent of {@code path}, or {@code null} if
   * {@code path} does not name a parent directory.
   *
   * @param path The path string.
   * @return The pathname of the parent of {@code path}, or {@code null} if
   *         {@code path} does not name a parent directory.
   * @throws NullPointerException If {@code path} is null.
   */
  public static String getParent(final String path) {
    final int end = path.charAt(path.length() - 1) == '/' ? path.lastIndexOf('/', path.length() - 2) : path.lastIndexOf('/');
    return end == -1 || path.charAt(end) == ':' ? null : path.substring(0, end + 1);
  }

  /**
   * Returns the canonical pathname of the parent of {@code path}, or
   * {@code null} if {@code path} does not name a parent directory. In a
   * canonical path, the {@code ".."} and {@code "."} path names are
   * dereferenced, and redundant {@code '/'} path separators are removed.
   *
   * @param path The path string.
   * @return The canonical pathname of the parent of {@code path}, or
   *         {@code null} if {@code path} does not name a parent directory.
   * @throws NullPointerException If {@code path} is null.
   */
  public static String getCanonicalParent(final String path) {
    final StringBuilder builder = canonicalize(new StringBuilder(path));
    final int index = builder.lastIndexOf("/");
    return index < 0 ? null : builder.substring(0, index);
  }

  private static String getName0(final String path) {
    final boolean end = path.charAt(path.length() - 1) == '/';
    final int start = end ? path.lastIndexOf('/', path.length() - 2) : path.lastIndexOf('/');
    return start == -1 ? (end ? path.substring(0, path.length() - 1) : path) : end ? path.substring(start + 1, path.length() - 1) : path.substring(start + 1);
  }

  /**
   * Returns the name of the file or directory denoted by the specified
   * pathname. This is just the last name in the name sequence of {@code path}.
   * If the name sequence of {@code path} is empty, then the empty string is
   * returned.
   *
   * @param path The path string.
   * @return The name of the file or directory denoted by the specified
   *         pathname, or the empty string if the name sequence of {@code path}
   *         is empty.
   * @throws IllegalArgumentException If {@code path} is an empty string.
   * @throws NullPointerException If {@code path} is null.
   */
  public static String getName(final String path) {
    if (path.length() == 0)
      throw new IllegalArgumentException("Empty path");

    return getName0(path);
  }

  /**
   * Returns the short name of the file or directory denoted by the specified
   * pathname. This is just the last name in the name sequence of {@code path},
   * with its dot-extension removed, if present. If the name sequence of
   * {@code path} is empty, then the empty string is returned.
   *
   * @param path The path string.
   * @return The short name of the file or directory denoted by the specified
   *         pathname, or the empty string if the name sequence of {@code path}
   *         is empty.
   * @throws IllegalArgumentException If {@code path} is an empty string.
   * @throws NullPointerException If {@code path} is null.
   */
  public static String getShortName(String path) {
    if (path.length() == 0)
      throw new IllegalArgumentException("Empty path");

    path = getName0(path);
    final int index = path.indexOf('.');
    return index == -1 ? path : path.substring(0, index);
  }

  private Paths() {
  }
}