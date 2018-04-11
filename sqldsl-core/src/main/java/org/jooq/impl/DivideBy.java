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
package org.jooq.impl;

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.selectDistinct;
import static org.jooq.impl.DSL.selectOne;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DivideByOnConditionStep;
import org.jooq.DivideByOnStep;
import org.jooq.Field;
import org.jooq.Operator;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
class DivideBy
implements
    DivideByOnStep,
    DivideByOnConditionStep {

    private final Table<?>                dividend;
    private final Table<?>                divisor;
    private final ConditionProviderImpl   condition;
    private final QueryPartList<Field<?>> returning;

    DivideBy(Table<?> dividend, Table<?> divisor) {
        this.dividend = dividend;
        this.divisor = divisor;

        this.condition = new ConditionProviderImpl();
        this.returning = new QueryPartList<Field<?>>();
    }

    // ------------------------------------------------------------------------
    // XXX: Table API
    // ------------------------------------------------------------------------

    /**
     * Transform the relational division operation into SQL.
     * <p>
     * Various nice examples of how this can be achieved are found here: <a
     * href=
     * "http://www.simple-talk.com/sql/t-sql-programming/divided-we-stand-the-sql-of-relational-division/"
     * >http://www.simple-talk.com/sql/t-sql-programming/divided-we-stand-the-
     * sql-of-relational-division/</a>
     */
    private final Table<Record> table() {
        ConditionProviderImpl selfJoin = new ConditionProviderImpl();
        List<Field<?>> select = new ArrayList<Field<?>>();
        Table<?> outer = dividend.as("dividend");

        for (Field<?> field : returning) {
            Field<?> outerField = outer.field(field);

            // Fields from the RETURNING clause are added AS-IS to the SELECT
            // statement, if they're not contained in the dividend table
            if (outerField == null) {
                select.add(field);
            }

            // Fields from the RETURNING clause need proper aliasing if they're
            // contained in the dividend table
            else {
                select.add(outerField);
                selfJoin(selfJoin, outer, dividend, field);
            }
        }

        // Apply relational division using double-nested NOT EXISTS clauses
        // There are more efficient ways to express division in SQL, but those
        // are hard to simulate with jOOQ
        return selectDistinct(select)
              .from(outer)
              .whereNotExists(
                   selectOne()
                  .from(divisor)
                  .whereNotExists(
                       selectOne()
                      .from(dividend)
                      .where(selfJoin)
                      .and(condition)))
              .asTable();
    }

    /**
     * Extracted method for type-safety
     */
    @SuppressWarnings("deprecation")
    private final <T> void selfJoin(org.jooq.ConditionProvider selfJoin, Table<?> outer, Table<?> inner, Field<T> field) {
        Field<T> outerField = outer.field(field);
        Field<T> innerField = inner.field(field);

        if (outerField == null || innerField == null) {
            return;
        }
        else {
            selfJoin.addConditions(outerField.equal(innerField));
        }
    }

    // ------------------------------------------------------------------------
    // XXX: DivideBy API
    // ------------------------------------------------------------------------

    @Override
    public final DivideByOnConditionStep on(Condition... conditions) {
        condition.addConditions(conditions);
        return this;
    }

    @Override
    public final DivideByOnConditionStep on(Field<Boolean> c) {
        return on(condition(c));
    }

    @Override
    public final DivideByOnConditionStep on(String sql) {
        and(sql);
        return this;
    }

    @Override
    public final DivideByOnConditionStep on(String sql, Object... bindings) {
        and(sql, bindings);
        return this;
    }

    @Override
    public final DivideByOnConditionStep on(String sql, QueryPart... parts) {
        and(sql, parts);
        return this;
    }

    @Override
    public final Table<Record> returning(Field<?>... fields) {
        return returning(Arrays.asList(fields));
    }

    @Override
    public final Table<Record> returning(Collection<? extends Field<?>> fields) {
        returning.addAll(fields);
        return table();
    }

    @Override
    public final DivideByOnConditionStep and(Condition c) {
        condition.addConditions(c);
        return this;
    }

    @Override
    public final DivideByOnConditionStep and(Field<Boolean> c) {
        return and(condition(c));
    }

    @Override
    public final DivideByOnConditionStep and(String sql) {
        return and(condition(sql));
    }

    @Override
    public final DivideByOnConditionStep and(String sql, Object... bindings) {
        return and(condition(sql, bindings));
    }

    @Override
    public final DivideByOnConditionStep and(String sql, QueryPart... parts) {
        return and(condition(sql, parts));
    }

    @Override
    public final DivideByOnConditionStep andNot(Condition c) {
        return and(c.not());
    }

    @Override
    public final DivideByOnConditionStep andNot(Field<Boolean> c) {
        return andNot(condition(c));
    }

    @Override
    public final DivideByOnConditionStep andExists(Select<?> select) {
        return and(exists(select));
    }

    @Override
    public final DivideByOnConditionStep andNotExists(Select<?> select) {
        return and(notExists(select));
    }

    @Override
    public final DivideByOnConditionStep or(Condition c) {
        condition.addConditions(Operator.OR, c);
        return this;
    }

    @Override
    public final DivideByOnConditionStep or(Field<Boolean> c) {
        return or(condition(c));
    }

    @Override
    public final DivideByOnConditionStep or(String sql) {
        return or(condition(sql));
    }

    @Override
    public final DivideByOnConditionStep or(String sql, Object... bindings) {
        return or(condition(sql, bindings));
    }

    @Override
    public final DivideByOnConditionStep or(String sql, QueryPart... parts) {
        return or(condition(sql, parts));
    }

    @Override
    public final DivideByOnConditionStep orNot(Condition c) {
        return or(c.not());
    }

    @Override
    public final DivideByOnConditionStep orNot(Field<Boolean> c) {
        return orNot(condition(c));
    }

    @Override
    public final DivideByOnConditionStep orExists(Select<?> select) {
        return or(exists(select));
    }

    @Override
    public final DivideByOnConditionStep orNotExists(Select<?> select) {
        return or(notExists(select));
    }
}
