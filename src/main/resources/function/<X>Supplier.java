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

package org.libj.util.function;

/**
 * Represents a supplier of {@code <y>}-valued results. This is the
 * {@code <y>}-producing primitive specialization of
 * {@link java.util.function.Supplier}.
 * <p>
 * There is no requirement that a distinct result be returned each time the
 * supplier is invoked.
 * <p>
 * This is a functional interface whose functional method is
 * {@link #getAs<X>()}.
 *
 * @see java.util.function.Supplier
 */
@FunctionalInterface
public interface <X>Supplier {
  /**
   * Gets a result.
   *
   * @return A result.
   */
  <y> getAs<X>();
}