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

import java.util.Collection;

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;
import org.jooq.impl.DSL;

/**
 * This type is used for the {@link Select}'s DSL API when selecting generic
 * {@link Record} types.
 * <p>
 * Example: <code><pre>
 * -- get all authors' first and last names, and the number
 * -- of books they've written in German, if they have written
 * -- more than five books in German in the last three years
 * -- (from 2011), and sort those authors by last names
 * -- limiting results to the second and third row
 *
 *   SELECT T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME, COUNT(*)
 *     FROM T_AUTHOR
 *     JOIN T_BOOK ON T_AUTHOR.ID = T_BOOK.AUTHOR_ID
 *    WHERE T_BOOK.LANGUAGE = 'DE'
 *      AND T_BOOK.PUBLISHED > '2008-01-01'
 * GROUP BY T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME
 *   HAVING COUNT(*) > 5
 * ORDER BY T_AUTHOR.LAST_NAME ASC NULLS FIRST
 *    LIMIT 2
 *   OFFSET 1
 *      FOR UPDATE
 *       OF FIRST_NAME, LAST_NAME
 *       NO WAIT
 * </pre></code> Its equivalent in jOOQ <code><pre>
 * create.select(TAuthor.FIRST_NAME, TAuthor.LAST_NAME, create.count())
 *       .from(T_AUTHOR)
 *       .join(T_BOOK).on(TBook.AUTHOR_ID.equal(TAuthor.ID))
 *       .where(TBook.LANGUAGE.equal("DE"))
 *       .and(TBook.PUBLISHED.greaterThan(parseDate('2008-01-01')))
 *       .groupBy(TAuthor.FIRST_NAME, TAuthor.LAST_NAME)
 *       .having(create.count().greaterThan(5))
 *       .orderBy(TAuthor.LAST_NAME.asc().nullsFirst())
 *       .limit(2)
 *       .offset(1)
 *       .forUpdate()
 *       .of(TAuthor.FIRST_NAME, TAuthor.LAST_NAME)
 *       .noWait();
 * </pre></code> Refer to the manual for more details
 *
 * @author Lukas Eder
 */
@State
public interface SelectFromStep<R extends Record> extends SelectWhereStep<R> {

    /**
     * Add a <code>FROM</code> clause to the query
     */
    @Support
    @Transition(
        name = "FROM",
        args = "Table+"
    )
    SelectJoinStep<R> from(TableLike<?>... table);

    /**
     * Add a <code>FROM</code> clause to the query
     */
    @Support
    @Transition(
        name = "FROM",
        args = "Table+"
    )
    SelectJoinStep<R> from(Collection<? extends TableLike<?>> tables);

    /**
     * Add a <code>FROM</code> clause to the query
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     */
    @Support
    SelectJoinStep<R> from(String sql);

    /**
     * Add a <code>FROM</code> clause to the query
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     */
    @Support
    SelectJoinStep<R> from(String sql, Object... bindings);

    /**
     * Add a <code>FROM</code> clause to the query
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     */
    @Support
    SelectJoinStep<R> from(String sql, QueryPart... parts);

    /**
     * Add an Oracle-style hint to the preceding select clause
     * <p>
     * Example: <code><pre>
     * DSLContext create = DSL.using(configuration);
     *
     * create.select(field1, field2)
     *       .hint("/*+ALL_ROWS&#42;/")
     *       .from(table1)
     *       .fetch();
     * </pre></code>
     * <p>
     * You can also use this clause for any other database, that accepts hints
     * or options at the same syntactic location, e.g. for MySQL's
     * <code>SQL_CALC_FOUND_ROWS</code> option: <code><pre>
     * create.select(field1, field2)
     *       .hint("SQL_CALC_FOUND_ROWS")
     *       .from(table1)
     *       .fetch();
     * </pre></code>
     * <p>
     * The outcome of such a query is this: <code><pre>
     * SELECT [hint] field1, field2 FROM table1
     * </pre></code>
     * <p>
     * For SQL Server style table hints, see {@link Table#with(String)}
     *
     * @see Table#with(String)
     * @see SelectQuery#addHint(String)
     */
    @Support
    SelectFromStep<R> hint(String hint);
}
