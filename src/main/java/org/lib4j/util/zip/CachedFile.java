/* Copyright (c) 2006 lib4j
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

package org.lib4j.util.zip;

import java.io.File;
import java.net.URI;

// FIXME: This is not the right way to be doing this!
// FIXME: It creates unnecessary dependencies and relations.
public final class CachedFile extends File {
  private static final long serialVersionUID = 3333613083676335051L;

  private final byte[] bytes;

  public CachedFile(final String pathname, final byte[] bytes) {
    super(pathname);
    this.bytes = bytes;
  }

  public CachedFile(final String parent, final String child, final byte[] bytes) {
    super(parent, child);
    this.bytes = bytes;
  }

  public CachedFile(final File parent, final String child, final byte[] bytes) {
    super(parent, child);
    this.bytes = bytes;
  }

  public CachedFile(final URI uri, final byte[] bytes) {
    super(uri);
    this.bytes = bytes;
  }

  public byte[] getBytes() {
    return bytes;
  }
}