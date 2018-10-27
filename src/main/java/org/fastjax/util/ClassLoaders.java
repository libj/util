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

import java.io.File;

/**
 * Utility functions for operations pertaining to {@code ClassLoader}.
 */
public final class ClassLoaders {
  private static File[] getClassPath(final String property) {
    final String value = System.getProperty(property);
    if (value == null)
      return null;

    final String[] parts = value.split(File.pathSeparator);
    final File[] urls = new File[parts.length];
    for (int i = 0; i < parts.length; ++i)
      urls[i] = new File(parts[i]);

    return urls;
  }

  /**
   * Returns a {@link File} array representing the paths in the classpath of the
   * current Java virtual machine. The classpath is determined via the
   * {@code "java.class.path"} system property.
   *
   * @return A {@link File} array representing the paths in the classpath of the
   *         current Java virtual machine.
   */
  public static File[] getClassPath() {
    final File[] classpath = getClassPath("java.class.path");
    return classpath != null ? classpath : getClassPath("user.dir");
  }

  /**
   * Returns a {@link File} array representing the paths in the surefire test
   * classpath of the current Java virtual machine. The classpath is determined
   * via the {@code "surefire.test.class.path"} system property.
   *
   * @return A {@link File} array representing the paths in the surefire test
   *         classpath of the current Java virtual machine.
   */
  public static File[] getTestClassPath() {
    final File[] classpath = getClassPath("surefire.test.class.path");
    return classpath != null ? classpath : getClassPath("user.dir");
  }

  private ClassLoaders() {
  }
}