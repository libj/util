/* Copyright (c) 2017 OpenJAX
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

package org.openjax.util;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

public class IdentityHashSetTest {
  @Test
  public void test() {
    final HashSet<String> regularSet = new HashSet<>();
    final IdentityHashSet<String> identitySet = new IdentityHashSet<>();

    final String a = "a";
    regularSet.add(a);
    identitySet.add(a);

    assertTrue(regularSet.contains(new String("a")));
    assertTrue(identitySet.contains(a));
    assertFalse(identitySet.contains(new String("a")));

    final IdentityHashSet<String> cloneIdentitySet = identitySet.clone();
    assertFalse(cloneIdentitySet.contains(new String("a")));
    assertTrue(cloneIdentitySet.contains(a));
  }
}