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

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.HashMap;

public abstract class MemoryURLStreamHandler extends URLStreamHandler {
  private static final String PROPERTY = "java.protocol.handler.pkgs";

  static {
    final String pkgs = System.getProperty(PROPERTY);
    if (pkgs == null || !pkgs.contains("org.lib4j.util"))
      System.setProperty(PROPERTY, pkgs != null && pkgs.length() > 0 ? pkgs + "|" + "org.lib4j.util" : "org.lib4j.util");
  }

  public static final HashMap<String,byte[]> idToData = new HashMap<>();

  public static URL createURL(final byte[] data) {
    try {
      final String path = "/" + Integer.toHexString(System.identityHashCode(data));
      final URL url = new URL("memory", null, path);

      idToData.put(path, data);
      return url;
    }
    catch (final MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }
}