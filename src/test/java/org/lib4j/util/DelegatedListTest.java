/* Copyright (c) 2016 lib4j
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class DelegatedListTest {
  @Test
  public void testListDelegate() {
    final List<String> values = Arrays.asList("a", "b", "c", "d", "e");

    final DelegatedList.ListDelegate<String> listDelegate = new DelegatedList.ListDelegate<String>() {
      private int i = 0;

      @Override
      public void add(final int index, final String element) {
        Assert.assertEquals(i, index);
        Assert.assertEquals(values.get(i++), element);
      }

      @Override
      public void remove(final int index) {
        Assert.assertEquals(--i, index);
      }
    };

    final DelegatedList<String> list = new DelegatedList<String>(ArrayList.class, listDelegate);
    for (final String value : values)
      list.add(value);

    list.clear();
  }
}