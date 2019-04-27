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

package org.openjax.standard.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class IdentityArrayListTest {
  @Test
  public void test() {
    final ArrayList<String> regularList = new ArrayList<>();
    final IdentityArrayList<String> identityList = new IdentityArrayList<>();

    final String a = "a";
    regularList.add(a);
    identityList.add(a);

    assertTrue(regularList.contains(new String("a")));
    assertTrue(identityList.contains(a));
    assertFalse(identityList.contains(new String("a")));

    final IdentityArrayList<String> cloneIdentityList = identityList.clone();
    assertFalse(cloneIdentityList.contains(new String("a")));
    assertTrue(cloneIdentityList.contains(a));

    List<String> subIdentityList = identityList.subList(0, 1);
    assertFalse(subIdentityList.contains(new String("a")));
    assertTrue(subIdentityList.contains(a));

    subIdentityList = subIdentityList.subList(0, 1);
    assertFalse(subIdentityList.contains(new String("a")));
    assertTrue(subIdentityList.contains(a));
  }
}