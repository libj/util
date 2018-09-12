/* Copyright (c) 2012 Kevin L. Stern
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
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.fastjax.util;

import org.fastjax.util.DamerauLevenshteinDistance;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DamerauLevenshteinDistanceTest {
  private static final Logger logger = LoggerFactory.getLogger(DamerauLevenshteinDistanceTest.class);

  @Test
  public void testDistance() {
    Assert.assertEquals(7, new DamerauLevenshteinDistance(1, 1, 1, 1).compute("NawKtYu", ""));
    Assert.assertEquals(7, new DamerauLevenshteinDistance(1, 1, 1, 1).compute("", "NawKtYu"));
    Assert.assertEquals(0, new DamerauLevenshteinDistance(1, 1, 1, 1).compute("NawKtYu", "NawKtYu"));
    Assert.assertEquals(6, new DamerauLevenshteinDistance(1, 1, 1, 1).compute("NawKtYu", "tKNwYua"));
    Assert.assertEquals(1, new DamerauLevenshteinDistance(1, 1, 1, 1).compute("Jdc", "dJc"));
    Assert.assertEquals(5, new DamerauLevenshteinDistance(1, 1, 1, 1).compute("sUzSOwx", "zsSxUwO"));
    Assert.assertEquals(7, new DamerauLevenshteinDistance(1, 1, 1, 1).compute("eOqoHAta", "tAeaqHoO"));
    Assert.assertEquals(1, new DamerauLevenshteinDistance(1, 1, 1, 1).compute("glSbo", "lgSbo"));
    Assert.assertEquals(4, new DamerauLevenshteinDistance(1, 1, 1, 1).compute("NJtQKcJE", "cJEtQKJN"));
    Assert.assertEquals(5, new DamerauLevenshteinDistance(1, 1, 1, 1).compute("GitIEVs", "EGItVis"));
    Assert.assertEquals(4, new DamerauLevenshteinDistance(1, 1, 1, 1).compute("MiWK", "WKiM"));
  }

  @Test
  public void testCost() {
    // Test replace cost.
    Assert.assertEquals(1, new DamerauLevenshteinDistance(100, 100, 1, 100).compute("a", "b"));
    // Test swap cost.
    Assert.assertEquals(200, new DamerauLevenshteinDistance(100, 100, 100, 200).compute("ab", "ba"));
    // Test delete cost.
    Assert.assertEquals(1, new DamerauLevenshteinDistance(1, 100, 100, 100).compute("aa", "a"));
    // Test insert cost.
    Assert.assertEquals(1, new DamerauLevenshteinDistance(100, 1, 100, 100).compute("a", "aa"));
  }

  @Test
  @SuppressWarnings("unused")
  public void testInvalidCosts() {
    logger.info(String.valueOf(new DamerauLevenshteinDistance(1, 1, 1, 1).compute("Where's Your Head At", "Wheres Y")));
    try {
      new DamerauLevenshteinDistance(1, 1, 1, 0);
      Assert.fail();
    }
    catch (final IllegalArgumentException e) {
    }
  }
}