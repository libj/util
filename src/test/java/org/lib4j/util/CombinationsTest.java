/* Copyright (c) 2014 lib4j
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

import java.util.Arrays;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CombinationsTest {
  private static final Logger logger = LoggerFactory.getLogger(CombinationsTest.class);

  @Test
  public void test() {
    final String[][] in = new String[][] {{"km", "m", "ft"}, {"sec", "min", "hr"}, {"kg", "lb"}};
    final String[][] out = Combinations.<String>combine(in);
    for (final String[] combination : out)
      logger.info(Arrays.toString(combination));
  }
}