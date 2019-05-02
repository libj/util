/* Copyright (c) 2018 OpenJAX
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

import java.util.Iterator;

import org.junit.Test;

public class ConcurrentHashSetTest {
  @Test
  public void test() {
    final ConcurrentHashSet<String> set = new ConcurrentHashSet<>();
    for (int i = 0; i < 100; ++i) {
      new Thread() {
        @Override
        public void run() {
          try {
            for (int i = 0; i < 1000; ++i) {
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
            throw new UnsupportedOperationException(e);
          }
        }
      }.start();
    }
  }
}