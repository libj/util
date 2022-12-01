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

import java.io.IOException;
import java.io.OutputStream;

/**
 * A {@link DelegateOutputStream} that provides callback methods to observe invocations of {@link #write(int)},
 * {@link #write(byte[])}, or {@link #afterWrite(int, byte[],int,int)}.
 */
public abstract class ObservableOutputStream extends DelegateOutputStream {
  /**
   * Callback method that is invoked immediately before the invocation of {@link #write(int)}, {@link #write(byte[])}, or
   * {@link #write(byte[],int,int)}.
   *
   * @param b The {@code int} if invoked before {@link #write(int)}, or {@code -1} if invoked before {@link #write(byte[])} and
   *          {@link #write(byte[],int,int) write(byte[],off,len)}.
   * @param bs The {@code byte[]} if invoked before {@link #write(byte[])} and {@link #write(byte[],int,int) write(byte[],off,len)},
   *          or {@code null} if invoked before {@link #write(int)}.
   * @param off The {@code off} if invoked before {@link #write(byte[],int,int) write(byte[],off,len)}, or {@code 0} if invoked
   *          before {@link #write(byte[])}, or {@code -1} if invoked before {@link #write(int)}.
   * @param len The {@code len} if invoked before {@link #write(byte[],int,int) write(byte[],off,len)}, or {@code -1} if invoked
   *          before {@link #write(int)} or {@link #write(byte[])}.
   * @return {@code true} for the data to be written to the {@link DelegateOutputStream#target} stream, and {@code false} to prevent
   *         default.
   * @throws IOException If an I/O error has occurred.
   */
  protected abstract boolean beforeWrite(int b, byte[] bs, int off, int len) throws IOException;

  /**
   * If and only if the preceding call to {@link #beforeWrite(int,byte[],int,int)} returned {@code true}, this callback method is
   * invoked immediately after the normal (i.e. non-exceptional) invocation of {@link #write(int)}, {@link #write(byte[])}, or
   * {@link #write(byte[],int,int)}.
   *
   * @param b The {@code int} if invoked after {@link #write(int)}, or {@code -1} if invoked after {@link #write(byte[])} and
   *          {@link #write(byte[],int,int) write(byte[],off,len)}.
   * @param bs The {@code byte[]} if invoked after {@link #write(byte[])} and {@link #write(byte[],int,int) write(byte[],off,len)},
   *          or {@code null} if invoked after {@link #write(int)}.
   * @param off The {@code off} if invoked after {@link #write(byte[],int,int) write(byte[],off,len)}, or {@code 0} if invoked after
   *          {@link #write(byte[])}, or {@code -1} if invoked after {@link #write(int)}.
   * @param len The {@code len} if invoked after {@link #write(byte[],int,int) write(byte[],off,len)}, or {@code -1} if invoked
   *          after {@link #write(int)} or {@link #write(byte[])}.
   * @throws IOException If an I/O error has occurred.
   */
  protected abstract void afterWrite(int b, byte[] bs, int off, int len) throws IOException;

  /**
   * Creates a new {@link ObservableOutputStream} with the specified {@code target} as the delegate {@link OutputStream}.
   *
   * @param target The delegate {@link OutputStream}.
   * @throws IllegalArgumentException If {@code target} is null.
   */
  public ObservableOutputStream(final OutputStream target) {
    super(target);
  }

  /**
   * Creates a new {@link ObservableOutputStream} with a {@code null} delegate {@link OutputStream}.
   */
  protected ObservableOutputStream() {
    super();
  }

  @Override
  public void write(final int b) throws IOException {
    if (!beforeWrite(b, null, -1, -1))
      return;

    target.write(b);
    afterWrite(b, null, -1, -1);
  }

  @Override
  public void write(final byte[] b) throws IOException {
    if (!beforeWrite(-1, b, 0, -1))
      return;

    target.write(b);
    afterWrite(-1, b, 0, -1);
  }

  @Override
  public void write(final byte[] b, final int off, final int len) throws IOException {
    if (!beforeWrite(-1, b, off, len))
      return;

    target.write(b, off, len);
    afterWrite(-1, b, off, len);
  }
}