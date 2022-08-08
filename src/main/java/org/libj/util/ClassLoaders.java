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

/**
 * Utility functions for operations pertaining to {@link ClassLoader}.
 */
public final class ClassLoaders {
  private static File[] getClassPath(final String property) {
    final String value = System.getProperty(property);
    if (value == null)
      return null;

    final String[] parts = value.split(File.pathSeparator);
    final File[] urls = new File[parts.length];
    for (int i = 0; i < parts.length; ++i) // [A]
      urls[i] = new File(parts[i]);

    return urls;
  }

  /**
   * Returns a {@link File} array representing the paths in the classpath of the system {@link ClassLoader}. The classpath is
   * determined from the {@code "java.class.path"} system property.
   *
   * @return A {@link File} array representing the paths in the classpath of the system {@link ClassLoader}.
   * @see ClassLoader#getSystemClassLoader()
   */
  public static File[] getClassPath() {
    final File[] classpath = getClassPath("java.class.path");
    return classpath != null ? classpath : getClassPath("user.dir");
  }

  /**
   * Returns a {@link File} array representing the paths in the classpath of the bootstrap {@link ClassLoader}. The classpath is
   * determined from the {@code "sun.boot.class.path"} system property.
   *
   * @implNote The {@code "sun.boot.class.path"} property has been removed in Java 9+.
   * @return A {@link File} array representing the paths in the classpath of the bootstrap {@link ClassLoader}.
   */
  public static File[] getBootstrapClassPath() {
    return getClassPath("sun.boot.class.path");
  }

  /**
   * Returns a {@link File} array representing the paths in the Surefire test classpath of the current Java virtual machine. The
   * classpath is determined from the {@code "surefire.test.class.path"} system property, which is set by the Surefire runtime.
   *
   * @return A {@link File} array representing the paths in the Surefire test classpath of the current Java virtual machine.
   * @see <a href= "https://maven.apache.org/surefire/maven-surefire-plugin/">Maven Surefire Plugin</a>
   */
  public static File[] getTestClassPath() {
    return getClassPath("surefire.test.class.path");
  }

  private ClassLoaders() {
  }
}