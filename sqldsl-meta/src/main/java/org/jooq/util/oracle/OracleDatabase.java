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

package org.jooq.util.oracle;

import static org.jooq.util.oracle.sys.Tables.ALL_COLL_TYPES;
import static org.jooq.util.oracle.sys.Tables.ALL_CONSTRAINTS;
import static org.jooq.util.oracle.sys.Tables.ALL_CONS_COLUMNS;
import static org.jooq.util.oracle.sys.Tables.ALL_MVIEW_COMMENTS;
import static org.jooq.util.oracle.sys.Tables.ALL_OBJECTS;
import static org.jooq.util.oracle.sys.Tables.ALL_PROCEDURES;
import static org.jooq.util.oracle.sys.Tables.ALL_SEQUENCES;
import static org.jooq.util.oracle.sys.Tables.ALL_TAB_COMMENTS;
import static org.jooq.util.oracle.sys.Tables.ALL_TYPES;
import static org.jooq.util.oracle.sys.Tables.ALL_USERS;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record4;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultArrayDefinition;
import org.jooq.util.DefaultCheckConstraintDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.DefaultSequenceDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
import org.jooq.util.oracle.sys.tables.AllConstraints;

/**
 * @author Lukas Eder
 */
public class OracleDatabase extends AbstractDatabase {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("P")) {
            SchemaDefinition schema = getSchema(record.getValue(ALL_CONS_COLUMNS.OWNER));
            String key = record.getValue(ALL_CONS_COLUMNS.CONSTRAINT_NAME);
            String tableName = record.getValue(ALL_CONS_COLUMNS.TABLE_NAME);
            String columnName = record.getValue(ALL_CONS_COLUMNS.COLUMN_NAME);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("U")) {
            SchemaDefinition schema = getSchema(record.getValue(ALL_CONS_COLUMNS.OWNER));
            String key = record.getValue(ALL_CONS_COLUMNS.CONSTRAINT_NAME);
            String tableName = record.getValue(ALL_CONS_COLUMNS.TABLE_NAME);
            String columnName = record.getValue(ALL_CONS_COLUMNS.COLUMN_NAME);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addUniqueKey(key, table.getColumn(columnName));
            }
        }
    }

    private Result<Record4<String, String, String, String>> fetchKeys(String constraintType) {
        return create().select(
                ALL_CONS_COLUMNS.OWNER,
                ALL_CONS_COLUMNS.CONSTRAINT_NAME,
                ALL_CONS_COLUMNS.TABLE_NAME,
                ALL_CONS_COLUMNS.COLUMN_NAME)
            .from(ALL_CONS_COLUMNS
                .join(ALL_CONSTRAINTS)
                .on(ALL_CONS_COLUMNS.OWNER.equal(ALL_CONSTRAINTS.OWNER))
                .and(ALL_CONS_COLUMNS.CONSTRAINT_NAME.equal(ALL_CONSTRAINTS.CONSTRAINT_NAME)))
            .where(ALL_CONSTRAINTS.CONSTRAINT_TYPE.equal(constraintType)
                .and(ALL_CONSTRAINTS.CONSTRAINT_NAME.notLike("BIN$%"))
                .and(ALL_CONS_COLUMNS.OWNER.upper().in(getInputSchemata())))
            .orderBy(
                ALL_CONS_COLUMNS.OWNER,
                ALL_CONS_COLUMNS.CONSTRAINT_NAME,
                ALL_CONS_COLUMNS.POSITION)
            .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        for (Record record : create().select(
                    ALL_CONS_COLUMNS.OWNER,
                    ALL_CONS_COLUMNS.CONSTRAINT_NAME,
                    ALL_CONS_COLUMNS.TABLE_NAME,
                    ALL_CONS_COLUMNS.COLUMN_NAME,
                    ALL_CONSTRAINTS.R_CONSTRAINT_NAME,
                    ALL_CONSTRAINTS.R_OWNER)
                .from(ALL_CONSTRAINTS
                    .join(ALL_CONS_COLUMNS)
                    .on(ALL_CONSTRAINTS.OWNER.equal(ALL_CONS_COLUMNS.OWNER))
                    .and(ALL_CONSTRAINTS.TABLE_NAME.equal(ALL_CONS_COLUMNS.TABLE_NAME))
                    .and(ALL_CONSTRAINTS.CONSTRAINT_NAME.equal(ALL_CONS_COLUMNS.CONSTRAINT_NAME)))
                .where(ALL_CONSTRAINTS.CONSTRAINT_TYPE.equal("R"))
                .and(ALL_CONSTRAINTS.OWNER.upper().in(getInputSchemata()))
                .orderBy(
                    ALL_CONS_COLUMNS.OWNER,
                    ALL_CONS_COLUMNS.TABLE_NAME,
                    ALL_CONS_COLUMNS.CONSTRAINT_NAME,
                    ALL_CONS_COLUMNS.POSITION)
                .fetch()) {

            SchemaDefinition foreignKeySchema = getSchema(record.getValue(ALL_CONS_COLUMNS.OWNER));
            SchemaDefinition uniqueKeySchema = getSchema(record.getValue(ALL_CONSTRAINTS.R_OWNER));

            String foreignKeyName = record.getValue(ALL_CONS_COLUMNS.CONSTRAINT_NAME);
            String foreignKeyTableName = record.getValue(ALL_CONS_COLUMNS.TABLE_NAME);
            String foreignKeyColumnName = record.getValue(ALL_CONS_COLUMNS.COLUMN_NAME);
            String uniqueKeyName = record.getValue(ALL_CONSTRAINTS.R_CONSTRAINT_NAME);

            TableDefinition referencingTable = getTable(foreignKeySchema, foreignKeyTableName);
            if (referencingTable != null) {
                ColumnDefinition column = referencingTable.getColumn(foreignKeyColumnName);
                relations.addForeignKey(foreignKeyName, uniqueKeyName, column, uniqueKeySchema);
            }
        }
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations relations) throws SQLException {
        AllConstraints ac = ALL_CONSTRAINTS.as("ac");

        for (Record record : create()
                .select(
                    ac.OWNER,
                    ac.TABLE_NAME,
                    ac.CONSTRAINT_NAME,
                    ac.SEARCH_CONDITION
                )
                .from(ac)
                .where(ac.CONSTRAINT_TYPE.eq("C"))
                .and(ac.OWNER.upper().in(getInputSchemata()))
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(ac.OWNER));
            TableDefinition table = getTable(schema, record.getValue(ac.TABLE_NAME));

            if (table != null) {
                relations.addCheckConstraint(table, new DefaultCheckConstraintDefinition(
                    schema,
                    table,
                    record.getValue(ac.CONSTRAINT_NAME),
                    record.getValue(ac.SEARCH_CONDITION)
                ));
            }
        }
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();

        for (String name : create()
                .selectDistinct(ALL_USERS.USERNAME)
                .from(ALL_USERS)
                .fetch(ALL_USERS.USERNAME)) {

            result.add(new SchemaDefinition(this, name, ""));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (Record record : create().select(
                    ALL_SEQUENCES.SEQUENCE_OWNER,
                    ALL_SEQUENCES.SEQUENCE_NAME,
                    ALL_SEQUENCES.MAX_VALUE)
                .from(ALL_SEQUENCES)
                .where(ALL_SEQUENCES.SEQUENCE_OWNER.upper().in(getInputSchemata()))
                .orderBy(
                    ALL_SEQUENCES.SEQUENCE_OWNER,
                    ALL_SEQUENCES.SEQUENCE_NAME)
                .fetch()) {


            SchemaDefinition schema = getSchema(record.getValue(ALL_SEQUENCES.SEQUENCE_OWNER));
            BigInteger value = record.getValue(ALL_SEQUENCES.MAX_VALUE, BigInteger.class, BigInteger.valueOf(Long.MAX_VALUE));
            DataTypeDefinition type = getDataTypeForMAX_VAL(schema, value);

            result.add(new DefaultSequenceDefinition(
                schema,
                record.getValue(ALL_SEQUENCES.SEQUENCE_NAME),
                type));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Record record : create()
                .select()
                .from(create()
                    .select(
                        ALL_TAB_COMMENTS.OWNER,
                        ALL_TAB_COMMENTS.TABLE_NAME,
                        ALL_TAB_COMMENTS.COMMENTS)
                    .from(ALL_TAB_COMMENTS)
                    .where(ALL_TAB_COMMENTS.OWNER.upper().in(getInputSchemata()))
                    .and(ALL_TAB_COMMENTS.TABLE_NAME.notLike("%$%"))
                .unionAll(create()
                    .select(
                        ALL_MVIEW_COMMENTS.OWNER,
                        ALL_MVIEW_COMMENTS.MVIEW_NAME,
                        ALL_MVIEW_COMMENTS.COMMENTS)
                    .from(ALL_MVIEW_COMMENTS)
                    .where(ALL_MVIEW_COMMENTS.OWNER.upper().in(getInputSchemata()))
                    .and(ALL_MVIEW_COMMENTS.MVIEW_NAME.notLike("%$%"))))
                .orderBy(1, 2)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(ALL_TAB_COMMENTS.OWNER));
            String name = record.getValue(ALL_TAB_COMMENTS.TABLE_NAME);
            String comment = record.getValue(ALL_TAB_COMMENTS.COMMENTS);

            OracleTableDefinition table = new OracleTableDefinition(schema, name, comment);
            result.add(table);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<EnumDefinition>();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<UDTDefinition> getUDTs0() throws SQLException {
        List<UDTDefinition> result = new ArrayList<UDTDefinition>();

        for (Record record : create().selectDistinct(
                ALL_TYPES.OWNER,
                ALL_TYPES.TYPE_NAME)
            .from(ALL_TYPES)
            .where(ALL_TYPES.OWNER.upper().in(getInputSchemata()))
            .and(ALL_TYPES.TYPECODE.equal("OBJECT"))
            .orderBy(
                ALL_TYPES.OWNER,
                ALL_TYPES.TYPE_NAME)
            .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(ALL_TYPES.OWNER));
            String name = record.getValue(ALL_TYPES.TYPE_NAME);

            result.add(new OracleUDTDefinition(schema, name, null));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<ArrayDefinition> getArrays0() throws SQLException {
        List<ArrayDefinition> arrays = new ArrayList<ArrayDefinition>();

        for (Record record : create().select(
                ALL_COLL_TYPES.OWNER,
                ALL_COLL_TYPES.TYPE_NAME,
                ALL_COLL_TYPES.ELEM_TYPE_NAME,
                ALL_COLL_TYPES.LENGTH,
                ALL_COLL_TYPES.PRECISION,
                ALL_COLL_TYPES.SCALE)
            .from(ALL_COLL_TYPES)
            .where(ALL_COLL_TYPES.OWNER.upper().in(getInputSchemata()))
            .and(ALL_COLL_TYPES.COLL_TYPE.in("VARYING ARRAY", "TABLE"))
            .orderBy(
                ALL_COLL_TYPES.OWNER,
                ALL_COLL_TYPES.TYPE_NAME)
            .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(ALL_COLL_TYPES.OWNER));

            String name = record.getValue(ALL_COLL_TYPES.TYPE_NAME);
            String dataType = record.getValue(ALL_COLL_TYPES.ELEM_TYPE_NAME);

            int length = record.getValue(ALL_COLL_TYPES.LENGTH, BigDecimal.ZERO).intValue();
            int precision = record.getValue(ALL_COLL_TYPES.PRECISION, BigDecimal.ZERO).intValue();
            int scale = record.getValue(ALL_COLL_TYPES.SCALE, BigDecimal.ZERO).intValue();

            DefaultDataTypeDefinition type = new DefaultDataTypeDefinition(this, schema, dataType, length, precision, scale);
            DefaultArrayDefinition array = new DefaultArrayDefinition(schema, name, type);

            arrays.add(array);
        }

        return arrays;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();

        for (Record record : create().select(
                    ALL_OBJECTS.OWNER,
                    ALL_OBJECTS.OBJECT_NAME,
                    ALL_OBJECTS.OBJECT_ID,
                    ALL_PROCEDURES.AGGREGATE)
                .from(ALL_OBJECTS)
                .leftOuterJoin(ALL_PROCEDURES)
                    .on(ALL_OBJECTS.OWNER.equal(ALL_PROCEDURES.OWNER))
                    .and(ALL_OBJECTS.OBJECT_TYPE.equal(ALL_PROCEDURES.OBJECT_TYPE))
                    .and(ALL_OBJECTS.OBJECT_NAME.equal(ALL_PROCEDURES.OBJECT_NAME))
                .where(ALL_OBJECTS.OWNER.upper().in(getInputSchemata())
                    .and(ALL_OBJECTS.OBJECT_TYPE.in("FUNCTION", "PROCEDURE")))
                .orderBy(
                    ALL_OBJECTS.OWNER,
                    ALL_OBJECTS.OBJECT_NAME,
                    ALL_OBJECTS.OBJECT_ID)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(ALL_OBJECTS.OWNER));
            String objectName = record.getValue(ALL_OBJECTS.OBJECT_NAME);
            BigDecimal objectId = record.getValue(ALL_OBJECTS.OBJECT_ID);
            boolean aggregate = record.getValue(ALL_PROCEDURES.AGGREGATE, boolean.class);

            result.add(new OracleRoutineDefinition(schema, null, objectName, "", objectId, null, aggregate));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();

        for (Record record : create().select(
                    ALL_OBJECTS.OWNER,
        		    ALL_OBJECTS.OBJECT_NAME,
        		    ALL_OBJECTS.OBJECT_ID)
                .from(ALL_OBJECTS)
                .where(ALL_OBJECTS.OWNER.upper().in(getInputSchemata())
                .and(ALL_OBJECTS.OBJECT_TYPE.equal("PACKAGE")))
                .orderBy(
                    ALL_OBJECTS.OWNER,
                    ALL_OBJECTS.OBJECT_NAME,
                    ALL_OBJECTS.OBJECT_ID)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(ALL_OBJECTS.OWNER));
            String name = record.getValue(ALL_OBJECTS.OBJECT_NAME);

            result.add(new OraclePackageDefinition(schema, name, ""));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.ORACLE);
    }
}
