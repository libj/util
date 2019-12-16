/* Copyright (c) 2018 LibJ
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

import java.util.Iterator;

import org.junit.Test;
import org.libj.lang.Strings;

public class ConcurrentHashSetTest {
  @Test
  public void test() {
    final ConcurrentHashSet<String> set = new ConcurrentHashSet<>();
    for (int i = 0; i < 100; ++i) {
      new Thread(() -> {
        try {
          for (int i1 = 0; i1 < 1000; ++i1) {
            Thread.sleep((int)(Math.random() * 10));

            set.add(Strings.getRandomAlpha(3));
            final Iterator<String> it = set.iterator();
            while (it.hasNext()) {
              it.next();
              if (Math.random() < .1)
                break;
            }

            it.remove();
          }
        }
        catch (final InterruptedException e) {
          throw new IllegalStateException(e);
        }
      }).start();
    }
  }
}