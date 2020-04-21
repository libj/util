/* Copyright (c) 2020 LibJ
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

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;

public class IterablesTest {
  @Test
  public void testSingleton() {
    final Iterator<Boolean> iterator = Iterables.singleton(Boolean.TRUE).iterator();
    assertNotNull(iterator);

    assertTrue(iterator.next());
    try {
      iterator.next();
      fail("Expected NoSuchElementException");
    }
    catch (final NoSuchElementException e) {
    }
  }
}