/* Copyright (c) 2017 lib4j
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

import org.junit.Assert;
import org.junit.Test;

public class IndirectDigraphTest {
  private static class Obj {
    public final String id;
    public final String parentId;

    public Obj(final String id, final String parentId) {
      this.id = id;
      this.parentId = parentId;
    }

    @Override
    public String toString() {
      return id + "->" + parentId;
    }
  }

  @Test
  public void testUnspecifiedReference() {
    final IndirectDigraph<Obj,String> digraph = new IndirectDigraph<Obj,String>(obj -> obj.id);
    digraph.addEdgeRef(new Obj("a", "b"), "b");
    digraph.addEdgeRef(new Obj("b", "c"), "c");
    digraph.addEdgeRef(new Obj("c", "a"), "d");
    try {
      digraph.getCycle().toString();
      Assert.fail("Expected IllegalStateException");
    }
    catch (final IllegalStateException e) {
      if (!e.getMessage().endsWith(": d"))
        throw e;
    }
  }

  @Test
  public void testCycle() {
    final IndirectDigraph<Obj,String> digraph = new IndirectDigraph<Obj,String>(obj -> obj.id);
    digraph.addEdgeRef(new Obj("a", "b"), "b");
    digraph.addEdgeRef(new Obj("b", "c"), "c");
    digraph.addEdgeRef(new Obj("c", "a"), "a");
    digraph.addEdgeRef(new Obj("d", "c"), "c");
    digraph.addEdgeRef(new Obj("e", "c"), "c");
    digraph.addEdgeRef(new Obj("f", "d"), "d");
    digraph.addEdgeRef(new Obj("g", "d"), "d");
    digraph.addEdgeRef(new Obj("h", "e"), "e");
    Assert.assertEquals("[c->a, b->c, a->b, c->a]", digraph.getCycle().toString());
  }

  @Test
  public void testTopological() {
    final IndirectDigraph<Obj,String> digraph = new IndirectDigraph<Obj,String>(obj -> obj.id);
    digraph.addEdgeRef(new Obj("a", "b"), "b");
    digraph.addEdgeRef(new Obj("b", "c"), "c");
    digraph.addEdgeRef(new Obj("c", null), null);
    digraph.addEdgeRef(new Obj("d", "c"), "c");
    digraph.addEdgeRef(new Obj("e", "c"), "c");
    digraph.addEdgeRef(new Obj("f", "d"), "d");
    digraph.addEdgeRef(new Obj("g", "d"), "d");
    digraph.addEdgeRef(new Obj("h", "e"), "e");
    Assert.assertEquals("[h->e, g->d, f->d, e->c, d->c, a->b, b->c, c->null]", digraph.getTopologicalOrder().toString());
  }
}