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

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class ClassLoadersTest {
  @Test
  public void test() {
    final File[] classpath = ClassLoaders.getClassPath();
    for (final File path : classpath) // [A]
      if (path.getAbsolutePath().contains("junit"))
        return;

    fail("Expected junit in the classpath.");
  }
}