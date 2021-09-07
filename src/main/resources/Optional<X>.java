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

package org.libj.util;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

import org.libj.util.function.<X>Consumer;
import org.libj.util.function.<X>Supplier;

/**
 * A container object which may or may not contain a {@code <x>} value. If a
 * value is present, {@code isPresent()} returns {@code true}. If no value is
 * present, the object is considered <i>empty</i> and {@code isPresent()}
 * returns {@code false}.
 * <p>
 * Additional methods that depend on the presence or absence of a contained
 * value are provided, such as {@link #orElse(<x>) orElse()} (returns a default
 * value if no value is present) and {@link #ifPresent(<X>Consumer) ifPresent()}
 * (performs an action if a value is present).
 * <p>
 * This is a value-based class; use of identity-sensitive operations (including
 * reference equality ({@code ==}), identity hash code, or synchronization) on
 * instances of {@code Optional<X>} may have unpredictable results and should be
 * avoided.
 *
 * @implNote {@code Optional<X>} is primarily intended for use as a
 *           method return type where there is a clear need to represent "no
 *           result." A variable whose type is {@code Optional<X>} should never
 *           itself be {@code null}; it should always point to an
 *           {@code Optional<X>} instance.
 */
public final class Optional<X> {
  /** Common instance for {@code empty()}. */
  private static final Optional<X> EMPTY = new Optional<X>();

  /**
   * If true then the value is present, otherwise indicates no value is present.
   */
  private final boolean isPresent;
  private final <x> value;

  /**
   * Construct an empty instance.
   *
   * @implNote Generally only one empty instance, {@link Optional<X>#EMPTY},
   *           should exist per VM.
   */
  private Optional<X>() {
    this.isPresent = false;
    this.value = <d>;
  }

  /**
   * Returns an empty {@code Optional<X>} instance. No value is present for this
   * {@code Optional<X>}.
   *
   * @implNote Though it may be tempting to do so, avoid testing if an object is
   *           empty by comparing with {@code ==} against instances returned by
   *           {@code Optional<X>.empty()}. There is no guarantee that it is a
   *           singleton. Instead, use {@link #isPresent()}.
   * @return An empty {@code Optional<X>}.
   */
  public static Optional<X> empty() {
    return EMPTY;
  }

  /**
   * Construct an instance with the described value.
   *
   * @param value The <x> value to describe.
   */
  private Optional<X>(final <x> value) {
    this.isPresent = true;
    this.value = value;
  }

  /**
   * Returns an {@code Optional<X>} describing the given value.
   *
   * @param value The value to describe.
   * @return An {@code Optional<X>} with the value present.
   */
  public static Optional<X> of(final <x> value) {
    return new Optional<X>(value);
  }

  /**
   * If a value is present, returns the value, otherwise throws
   * {@code NoSuchElementException}.
   *
   * @implNote The preferred alternative to this method is
   *           {@link #orElseThrow()}.
   * @return The value described by this {@code Optional<X>}.
   * @throws NoSuchElementException If no value is present.
   */
  public <x> getAs<X>() {
    if (isPresent)
      return value;

    throw new NoSuchElementException("No value present");
  }

  /**
   * If a value is present, returns {@code true}, otherwise {@code false}.
   *
   * @return {@code true} if a value is present, otherwise {@code false}.
   */
  public boolean isPresent() {
    return isPresent;
  }

  /**
   * If a value is not present, returns {@code true}, otherwise {@code false}.
   *
   * @return {@code true} if a value is not present, otherwise {@code false}.
   */
  public boolean isEmpty() {
    return !isPresent;
  }

  /**
   * If a value is present, performs the given action with the value, otherwise
   * does nothing.
   *
   * @param action The action to be performed, if a value is present.
   * @throws NullPointerException If value is present and the given action is
   *           {@code null}.
   */
  public void ifPresent(final <X>Consumer action) {
    if (isPresent)
      action.accept(value);
  }

  /**
   * If a value is present, performs the given action with the value, otherwise
   * performs the given empty-based action.
   *
   * @param action The action to be performed, if a value is present.
   * @param emptyAction The empty-based action to be performed, if no value is
   *          present.
   * @throws NullPointerException If a value is present and the given action is
   *           {@code null}, or no value is present and the given empty-based
   *           action is {@code null}.
   */
  public void ifPresentOrElse(final <X>Consumer action, final Runnable emptyAction) {
    if (isPresent)
      action.accept(value);
    else
      emptyAction.run();
  }

//  /**
//   * If a value is present, returns a sequential {@link <X>Stream} containing
//   * only that value, otherwise returns an empty {@code <X>Stream}.
//   * <p>
//   * <b>Note:</b> This method can be used to transform a {@code Stream} of
//   * optional <x>s to an {@code <X>Stream} of present <x>s:
//   *
//   * <pre>
//   * {@code
//   *     Stream<Optional<X>> os = ..
//   *     <X>Stream s = os.flatMapTo<X>(Optional<X>::stream)
//   * }
//   * </pre>
//   *
//   * @return The optional value as an {@code <X>Stream}.
//   */
//  public <X>Stream stream() {
//    return isPresent ? <X>Stream.of(value) : <X>Stream.empty();
//  }

  /**
   * If a value is present, returns the value, otherwise returns {@code other}.
   *
   * @param other The value to be returned, if no value is present.
   * @return The value, if present, otherwise {@code other}.
   */
  public <x> orElse(final <x> other) {
    return isPresent ? value : other;
  }

  /**
   * If a value is present, returns the value, otherwise returns the result
   * produced by the supplying function.
   *
   * @param supplier The supplying function that produces a value to be returned.
   * @return The value, if present, otherwise the result produced by the
   *         supplying function.
   * @throws NullPointerException If no value is present and the supplying
   *           function is {@code null}.
   */
  public <x> orElseGet(final <X>Supplier supplier) {
    return isPresent ? value : supplier.getAs<X>();
  }

  /**
   * If a value is present, returns the value, otherwise throws
   * {@code NoSuchElementException}.
   *
   * @return The value described by this {@code Optional<X>}.
   * @throws NoSuchElementException If no value is present.
   */
  public <x> orElseThrow() {
    if (isPresent)
      return value;

    throw new NoSuchElementException("No value present");
  }

  /**
   * If a value is present, returns the value, otherwise throws an exception
   * produced by the exception supplying function.
   *
   * @implNote A method reference to the exception constructor with an empty
   *           argument list can be used as the supplier. For example,
   *           {@code IllegalStateException::new}.
   * @param <T> Type of the exception to be thrown
   * @param exceptionSupplier The supplying function that produces an exception
   *          to be thrown.
   * @return The value, if present
   * @throws T If no value is present.
   * @throws NullPointerException If no value is present and the exception
   *           supplying function is {@code null}.
   */
  public <T extends Throwable> <x> orElseThrow(final Supplier<? extends T> exceptionSupplier) throws T {
    if (isPresent)
      return value;

    throw exceptionSupplier.get();
  }

  /**
   * Indicates whether some other object is "equal to" this
   * {@code Optional<X>}. The other object is considered equal if:
   * <ul>
   * <li>it is also an {@code Optional<X>} and;
   * <li>both instances have no value present or;
   * <li>the present values are "equal to" each other via {@code ==}.
   * </ul>
   *
   * @param obj An object to be tested for equality.
   * @return {@code true} if the other object is "equal to" this object
   *         otherwise {@code false}.
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof Optional<X>))
      return false;

    final Optional<X> that = (Optional<X>)obj;
    return isPresent && that.isPresent ? value == that.value : isPresent == that.isPresent;
  }

  /**
   * Returns the hash code of the value, if present, otherwise {@code 0} (zero)
   * if no value is present.
   *
   * @return Hash code value of the present value or {@code 0} if no value is
   *         present.
   */
  @Override
  public int hashCode() {
    return isPresent ? <XX>.hashCode(value) : 0;
  }

  /**
   * Returns a non-empty string representation of this {@code Optional<X>}
   * suitable for debugging. The exact presentation format is unspecified and
   * may vary between implementations and versions.
   *
   * @implNote If a value is present the result must include its string
   *           representation in the result. Empty and present
   *           {@code Optional<X>}s must be unambiguously differentiable.
   * @return The string representation of this instance.
   */
  @Override
  public String toString() {
    return isPresent ? String.format("Optional<X>[%s]", value) : "Optional<X>.empty";
  }
}