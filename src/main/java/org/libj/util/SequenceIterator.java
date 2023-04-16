/* Copyright (c) 2022 LibJ
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Generate sequence of {@link BigDecimal} values.
 */
public class SequenceIterator implements Iterable<BigDecimal> {
  protected final BigDecimal start;
  protected final BigDecimal end;
  protected final BigDecimal step;
  protected BigDecimal cur;


  /**
   * Creates a new {@link SequenceIterator} with the provided {@code start}, {@code end}, and {@code step} parameters.
   *
   * @param start The {@link BigDecimal} from which to start the sequence (inclusive).
   * @param end The {@link BigDecimal} at which to end the sequence (exclusive).
   * @param step The {@link BigDecimal} representing the step of each sequence.
   * @throws IllegalArgumentException If {@code start} is less than {@code end} and {@code step} is positive, or if {@code end} is
   *           less than {@code start} and {@code step} is negative.
   * @throws IllegalArgumentException If {@code step} is zero.
   * @throws NullPointerException If any argument is null.
   */
  public SequenceIterator(final BigDecimal start, final BigDecimal end, final BigDecimal step) {
    this.start = start;
    this.end = end;
    this.step = step;
    if (step.signum() > 0) {
      if (start.compareTo(end) >= 0)
        throw new IllegalArgumentException("start (" + start + ") must be less than end (" + end + ") with a positive step (" + step + ")");
    }
    else if (step.signum() < 0) {
      if (start.compareTo(end) <= 0)
        throw new IllegalArgumentException("end (" + end + ") must be less than start (" + start + ") with a negative step (" + step + ")");
    }
    else {
      throw new IllegalArgumentException("step must not be 0");
    }

    cur = start.subtract(step);
  }

  /**
   * Returns the cardinality of sequences to be generated.
   *
   * @return The cardinality of sequences to be generated.
   */
  public BigInteger getCardinality() {
    return BigDecimal.ONE.add(end.subtract(start).divide(step)).toBigInteger();
  }

  @Override
  public Iterator<BigDecimal> iterator() {
    return new Iterator<BigDecimal>() {
      protected BigDecimal cur = SequenceIterator.this.start.subtract(step);

      @Override
      public boolean hasNext() {
        return step.signum() > 0 ? cur.compareTo(end) < 0 : end.compareTo(cur) < 0;
      }

      @Override
      public BigDecimal next() {
        if (!hasNext())
          throw new NoSuchElementException();

        return cur = cur.add(step);
      }
    };
  }
}