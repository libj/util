/* Copyright (c) 2006 LibJ
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
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Utility functions for operations pertaining to resources.
 */
public final class Resources {
  /**
   * Returns an {@code URL} to a resource by the specified name, or, if not
   * found, to a file by the same name (in the current working directory).
   *
   * @param name The name of the resource.
   * @return An {@code URL} to a resource by the specified name, or, if not
   *         found, to a file by the same name.
   * @throws NullPointerException If {@code name} is null.
   */
  public static URL getResourceOrFile(final String name) {
    final URL resource = Thread.currentThread().getContextClassLoader().getResource(name);
    if (resource != null)
      return resource;

    final File file = new File(name);
    if (!file.exists())
      return null;

    try {
      return file.toURI().toURL();
    }
    catch (final MalformedURLException e) {
      throw new IllegalArgumentException(name, e);
    }
  }

  /**
   * Returns an {@code URL} to a file by the specified name (in the current
   * working directory), or, if not found, to a resource by the same name.
   *
   * @param name The name of the resource.
   * @return An {@code URL} to a file by the specified name, or, if not found,
   *         to a resource by the same name.
   * @throws NullPointerException If {@code name} is null.
   */
  public static URL getFileOrResource(final String name) {
    final File file = new File(name);
    if (file.exists()) {
      try {
        return file.toURI().toURL();
      }
      catch (final MalformedURLException e) {
        throw new IllegalArgumentException(name, e);
      }
    }

    return Thread.currentThread().getContextClassLoader().getResource(name);
  }

  private Resources() {
  }
}