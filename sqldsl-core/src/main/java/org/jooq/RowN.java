/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq;

import org.jooq.Comparator;
import org.jooq.api.annotation.State;

import java.util.Collection;

import javax.annotation.Generated;

/**
 * A model type for a row value expression with degree <code>N > 22</code>.
 * <p>
 * Note: Not all databases support row value expressions, but many row value
 * expression operations can be simulated on all databases. See relevant row
 * value expression method Javadocs for details.
 *
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
@State
public interface RowN extends Row {

    // ------------------------------------------------------------------------
    // Generic comparison predicates
    // ------------------------------------------------------------------------
    
    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all 
     * {@link Comparator} types are supported
     *
     * @see #equal(RowN)
     * @see #notEqual(RowN)
     * @see #lessThan(RowN)
     * @see #lessOrEqual(RowN)
     * @see #greaterThan(RowN)
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition compare(Comparator comparator, RowN row);

    /**
     * Compare this row value expression with a record record
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all 
     * {@link Comparator} types are supported
     *
     * @see #equal(RowN)
     * @see #notEqual(RowN)
     * @see #lessThan(RowN)
     * @see #lessOrEqual(RowN)
     * @see #greaterThan(RowN)
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition compare(Comparator comparator, Record record);

    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all 
     * {@link Comparator} types are supported
     *
     * @see #equal(RowN)
     * @see #notEqual(RowN)
     * @see #lessThan(RowN)
     * @see #lessOrEqual(RowN)
     * @see #greaterThan(RowN)
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition compare(Comparator comparator, Object... values);

    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all 
     * {@link Comparator} types are supported
     *
     * @see #equal(RowN)
     * @see #notEqual(RowN)
     * @see #lessThan(RowN)
     * @see #lessOrEqual(RowN)
     * @see #greaterThan(RowN)
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition compare(Comparator comparator, Field<?>... values);

    // ------------------------------------------------------------------------
    // Equal / Not equal comparison predicates
    // ------------------------------------------------------------------------

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     * <p>
     * Row equality comparison predicates can be simulated in those databases
     * that do not support such predicates natively:
     * <code>(A, B) = (1, 2)</code> is equivalent to
     * <code>A = 1 AND B = 2</code>
     */
    @Support
    Condition equal(RowN row);

    /**
     * Compare this row value expression with a record for equality.
     *
     * @see #equal(RowN)
     */
    @Support
    Condition equal(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(RowN)
     */
    @Support
    Condition equal(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(RowN)
     */
    @Support
    Condition equal(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #equal(RowN)
     */
    @Support
    Condition equal(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(RowN)
     */
    @Support
    Condition eq(RowN row);

    /**
     * Compare this row value expression with a record for equality.
     *
     * @see #equal(RowN)
     */
    @Support
    Condition eq(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(RowN)
     */
    @Support
    Condition eq(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(RowN)
     */
    @Support
    Condition eq(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #equal(RowN)
     */
    @Support
    Condition eq(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     * <p>
     * Row non-equality comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) <> (1, 2)</code> is equivalent to
     * <code>NOT(A = 1 AND B = 2)</code>
     */
    @Support
    Condition notEqual(RowN row);

    /**
     * Compare this row value expression with a record for non-equality
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition notEqual(Record record);

    /**
     * Compare this row value expression with another row value expression for.
     * non-equality
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition notEqual(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition notEqual(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition notEqual(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition ne(RowN row);

    /**
     * Compare this row value expression with a record for non-equality.
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition ne(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition ne(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition ne(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notEqual(RowN)
     */
    @Support
    Condition ne(Select<? extends Record> select);

    // ------------------------------------------------------------------------
    // Ordering comparison predicates
    // ------------------------------------------------------------------------

    /**
     * Compare this row value expression with another row value expression for
     * order.
     * <p>
     * Row order comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B, C) < (1, 2, 3)</code> is equivalent to
     * <code>A < 1 OR (A = 1 AND B < 2) OR (A = 1 AND B = 2 AND C < 3)</code>
     */
    @Support
    Condition lessThan(RowN row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lessThan(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lessThan(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lessThan(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lessThan(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lt(RowN row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lt(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lt(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lt(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessThan(RowN)
     */
    @Support
    Condition lt(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     * <p>
     * Row order comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) <= (1, 2)</code> is equivalent to
     * <code>A < 1 OR (A = 1 AND B < 2) OR (A = 1 AND B = 2)</code>
     */
    @Support
    Condition lessOrEqual(RowN row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition lessOrEqual(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition lessOrEqual(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition lessOrEqual(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition lessOrEqual(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition le(RowN row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition le(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition le(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition le(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessOrEqual(RowN)
     */
    @Support
    Condition le(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     * <p>
     * Row order comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B, C) > (1, 2, 3)</code> is equivalent to
     * <code>A > 1 OR (A = 1 AND B > 2) OR (A = 1 AND B = 2 AND C > 3)</code>
     */
    @Support
    Condition greaterThan(RowN row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition greaterThan(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition greaterThan(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition greaterThan(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition greaterThan(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition gt(RowN row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition gt(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition gt(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition gt(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterThan(RowN)
     */
    @Support
    Condition gt(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     * <p>
     * Row order comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) >= (1, 2)</code> is equivalent to
     * <code>A > 1 OR (A = 1 AND B > 2) OR (A = 1 AND B = 2)</code>
     */
    @Support
    Condition greaterOrEqual(RowN row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition greaterOrEqual(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition greaterOrEqual(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition greaterOrEqual(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition greaterOrEqual(Select<? extends Record> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition ge(RowN row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition ge(Record record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition ge(Object... values);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition ge(Field<?>... values);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterOrEqual(RowN)
     */
    @Support
    Condition ge(Select<? extends Record> select);

    // ------------------------------------------------------------------------
    // [NOT] BETWEEN predicates
    // ------------------------------------------------------------------------

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(RowN, RowN)
     */
    @Support
    BetweenAndStepN between(Object... minValues);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(RowN, RowN)
     */
    @Support
    BetweenAndStepN between(Field<?>... minValues);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(RowN, RowN)
     */
    @Support
    BetweenAndStepN between(RowN minValue);

    /**
     * Check if this row value expression is within a range of two records.
     *
     * @see #between(RowN, RowN)
     */
    @Support
    BetweenAndStepN between(Record minValue);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     * <p>
     * The expression <code>A BETWEEN B AND C</code> is equivalent to the
     * expression <code>A >= B AND A <= C</code> for those SQL dialects that do
     * not properly support the <code>BETWEEN</code> predicate for row value
     * expressions
     */
    @Support
    Condition between(RowN minValue,
                      RowN maxValue);

    /**
     * Check if this row value expression is within a range of two records.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     *
     * @see #between(RowN, RowN)
     */
    @Support
    Condition between(Record minValue,
                      Record maxValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(RowN, RowN)
     */
    @Support
    BetweenAndStepN betweenSymmetric(Object... minValues);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(RowN, RowN)
     */
    @Support
    BetweenAndStepN betweenSymmetric(Field<?>... minValues);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(RowN, RowN)
     */
    @Support
    BetweenAndStepN betweenSymmetric(RowN minValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * records.
     *
     * @see #betweenSymmetric(RowN, RowN)
     */
    @Support
    BetweenAndStepN betweenSymmetric(Record minValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     * <p>
     * This is the same as calling <code>betweenSymmetric(minValue).and(maxValue)</code>
     * <p>
     * The expression <code>A BETWEEN SYMMETRIC B AND C</code> is equivalent to
     * the expression <code>(A >= B AND A <= C) OR (A >= C AND A <= B)</code>
     * for those SQL dialects that do not properly support the
     * <code>BETWEEN</code> predicate for row value expressions
     */
    @Support
    Condition betweenSymmetric(RowN minValue,
                               RowN maxValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * records.
     * <p>
     * This is the same as calling <code>betweenSymmetric(minValue).and(maxValue)</code>
     *
     * @see #betweenSymmetric(RowN, RowN)
     */
    @Support
    Condition betweenSymmetric(Record minValue,
                               Record maxValue);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #between(RowN, RowN)
     */
    @Support
    BetweenAndStepN notBetween(Object... minValues);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #notBetween(RowN, RowN)
     */
    @Support
    BetweenAndStepN notBetween(Field<?>... minValues);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #notBetween(RowN, RowN)
     */
    @Support
    BetweenAndStepN notBetween(RowN minValue);

    /**
     * Check if this row value expression is within a range of two records.
     *
     * @see #notBetween(RowN, RowN)
     */
    @Support
    BetweenAndStepN notBetween(Record minValue);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     * <p>
     * This is the same as calling <code>notBetween(minValue).and(maxValue)</code>
     * <p>
     * The expression <code>A NOT BETWEEN B AND C</code> is equivalent to the
     * expression <code>A < B OR A > C</code> for those SQL dialects that do
     * not properly support the <code>BETWEEN</code> predicate for row value
     * expressions
     */
    @Support
    Condition notBetween(RowN minValue,
                         RowN maxValue);

    /**
     * Check if this row value expression is within a range of two records.
     * <p>
     * This is the same as calling <code>notBetween(minValue).and(maxValue)</code>
     *
     * @see #notBetween(RowN, RowN)
     */
    @Support
    Condition notBetween(Record minValue,
                         Record maxValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(RowN, RowN)
     */
    @Support
    BetweenAndStepN notBetweenSymmetric(Object... minValues);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(RowN, RowN)
     */
    @Support
    BetweenAndStepN notBetweenSymmetric(Field<?>... minValues);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(RowN, RowN)
     */
    @Support
    BetweenAndStepN notBetweenSymmetric(RowN minValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * records.
     *
     * @see #notBetweenSymmetric(RowN, RowN)
     */
    @Support
    BetweenAndStepN notBetweenSymmetric(Record minValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     * <p>
     * This is the same as calling <code>notBetweenSymmetric(minValue).and(maxValue)</code>
     * <p>
     * The expression <code>A NOT BETWEEN SYMMETRIC B AND C</code> is equivalent
     * to the expression <code>(A < B OR A > C) AND (A < C OR A > B)</code> for
     * those SQL dialects that do not properly support the <code>BETWEEN</code>
     * predicate for row value expressions
     */
    @Support
    Condition notBetweenSymmetric(RowN minValue,
                                  RowN maxValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * records.
     * <p>
     * This is the same as calling <code>notBetweenSymmetric(minValue).and(maxValue)</code>
     *
     * @see #notBetweenSymmetric(RowN, RowN)
     */
    @Support
    Condition notBetweenSymmetric(Record minValue,
                                  Record maxValue);

    // ------------------------------------------------------------------------
    // [NOT] DISTINCT predicates
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // [NOT] IN predicates
    // ------------------------------------------------------------------------

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     * <p>
     * Row IN predicates can be simulated in those databases that do not support
     * such predicates natively: <code>(A, B) IN ((1, 2), (3, 4))</code> is
     * equivalent to <code>((A, B) = (1, 2)) OR ((A, B) = (3, 4))</code>, which
     * is equivalent to <code>(A = 1 AND B = 2) OR (A = 3 AND B = 4)</code>
     */
    @Support
    Condition in(Collection<? extends RowN> rows);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(RowN... rows);

    /**
     * Compare this row value expression with a set of records for equality.
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(Record... record);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(Select<? extends Record> select);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     * <p>
     * Row NOT IN predicates can be simulated in those databases that do not
     * support such predicates natively:
     * <code>(A, B) NOT IN ((1, 2), (3, 4))</code> is equivalent to
     * <code>NOT(((A, B) = (1, 2)) OR ((A, B) = (3, 4)))</code>, which is
     * equivalent to <code>NOT((A = 1 AND B = 2) OR (A = 3 AND B = 4))</code>
     */
    @Support
    Condition notIn(Collection<? extends RowN> rows);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(RowN... rows);

    /**
     * Compare this row value expression with a set of records for non-equality.
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(Record... record);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(Select<? extends Record> select);

}
