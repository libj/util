/* Copyright (c) 2022 JetRS
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

import static org.libj.lang.Assertions.*;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A {@link DelegateOutputStream} contains some other {@link OutputStream}, to which it delegates its method calls, possibly
 * transforming the data along the way or providing additional functionality. The class {@link DelegateOutputStream} itself simply
 * overrides all methods of {@link OutputStream} with versions that pass all requests to the target {@link OutputStream}. Subclasses
 * of {@link DelegateOutputStream} may further override some of these methods and may also provide additional methods and fields.
 */
public class DelegateOutputStream extends OutputStream {
  /** The target {@link OutputStream} to which all calls are delegated. */
  protected OutputStream target;

  /**
   * Creates a new {@link DelegateOutputStream} with the specified {@code target} as the delegate {@link OutputStream}.
   *
   * @param target The delegate {@link OutputStream}.
   * @throws IllegalArgumentException If {@code target} is null.
   */
  public DelegateOutputStream(final OutputStream target) {
    this.target = assertNotNull(target);
  }

  /**
   * Creates a new {@link DelegateOutputStream} with a {@code null} delegate {@link OutputStream}.
   */
  protected DelegateOutputStream() {
  }

  @Override
  public void write(final int b) throws IOException {
    target.write(b);
  }

  @Override
  public void write(final byte[] b) throws IOException {
    target.write(b);
  }

  @Override
  public void write(final byte[] b, final int off, final int len) throws IOException {
    target.write(b, off, len);
  }

  @Override
  public void flush() throws IOException {
    target.flush();
  }

  @Override
  public void close() throws IOException {
    target.close();
  }
}