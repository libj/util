/* Copyright (c) 2008 lib4j
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
import java.util.Collection;

public class IdentityArrayList<E> extends ArrayList<E> {
  private static final long serialVersionUID = -5056045452760154513L;

  public IdentityArrayList(final int initialCapacity) {
    super(initialCapacity);
  }

  public IdentityArrayList(final Collection<? extends E> c) {
    super(c);
  }

  public IdentityArrayList() {
    super();
  }

  @Override
  public boolean contains(final Object o) {
    return indexOf(o) > -1;
  }

  @Override
  public int indexOf(final Object o) {
    for (int i = 0; i < size(); i++)
      if (o == get(i))
        return i;

    return -1;
  }

  @Override
  public int lastIndexOf(final Object o) {
    for (int i = size() - 1; i >= 0; i--)
      if (o == get(i))
        return i;

    return -1;
  }

  @Override
  public boolean remove(final Object o) {
    final int index = indexOf(o);
    if (index < 0)
      return false;

    return super.remove(index) != null;
  }

  @Override
  public boolean removeAll(final Collection<?> c) {
    if (c == null)
      return false;

    final int size = size();
    for (final Object o : c)
      remove(o);

    return size != size();
  }

  @Override
  public boolean retainAll(final Collection<?> c) {
    if (c == null)
      return false;

    if (c.size() == 0 && size() != 0) {
      clear();
      return true;
    }

    boolean modified = false;
    OUT:
    for (int i = 0; i < c.size(); i++) {
      final Object o = get(i);
      for (final Object obj : c)
        if (obj == o)
          continue OUT;

      modified = remove(i) != null || modified;
    }

    return modified;
  }

  @Override
  public IdentityArrayList<E> clone() {
    return new IdentityArrayList<E>(this);
  }
}