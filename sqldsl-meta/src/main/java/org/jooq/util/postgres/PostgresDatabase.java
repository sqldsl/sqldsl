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

package org.jooq.util.postgres;

import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.decode;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.upper;
import static org.jooq.impl.DSL.val;
import static org.jooq.util.postgres.information_schema.Tables.ATTRIBUTES;
import static org.jooq.util.postgres.information_schema.Tables.CHECK_CONSTRAINTS;
import static org.jooq.util.postgres.information_schema.Tables.KEY_COLUMN_USAGE;
import static org.jooq.util.postgres.information_schema.Tables.PARAMETERS;
import static org.jooq.util.postgres.information_schema.Tables.REFERENTIAL_CONSTRAINTS;
import static org.jooq.util.postgres.information_schema.Tables.ROUTINES;
import static org.jooq.util.postgres.information_schema.Tables.SEQUENCES;
import static org.jooq.util.postgres.information_schema.Tables.TABLES;
import static org.jooq.util.postgres.information_schema.Tables.TABLE_CONSTRAINTS;
import static org.jooq.util.postgres.pg_catalog.Tables.PG_ENUM;
import static org.jooq.util.postgres.pg_catalog.Tables.PG_NAMESPACE;
import static org.jooq.util.postgres.pg_catalog.Tables.PG_TYPE;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultCheckConstraintDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultEnumDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.DefaultSequenceDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
import org.jooq.util.hsqldb.HSQLDBDatabase;
import org.jooq.util.postgres.information_schema.tables.CheckConstraints;
import org.jooq.util.postgres.information_schema.tables.Routines;
import org.jooq.util.postgres.information_schema.tables.TableConstraints;

/**
 * Postgres uses the ANSI default INFORMATION_SCHEMA, but unfortunately ships
 * with a non-capitalised version of it: <code>information_schema</code>. Hence
 * the {@link HSQLDBDatabase} is not used here.
 *
 * @author Lukas Eder
 */
public class PostgresDatabase extends AbstractDatabase {

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("PRIMARY KEY")) {
            SchemaDefinition schema = getSchema(record.getValue(KEY_COLUMN_USAGE.TABLE_SCHEMA));
            String key = record.getValue(KEY_COLUMN_USAGE.CONSTRAINT_NAME);
            String tableName = record.getValue(KEY_COLUMN_USAGE.TABLE_NAME);
            String columnName = record.getValue(KEY_COLUMN_USAGE.COLUMN_NAME);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("UNIQUE")) {
            SchemaDefinition schema = getSchema(record.getValue(KEY_COLUMN_USAGE.TABLE_SCHEMA));
            String key = record.getValue(KEY_COLUMN_USAGE.CONSTRAINT_NAME);
            String tableName = record.getValue(KEY_COLUMN_USAGE.TABLE_NAME);
            String columnName = record.getValue(KEY_COLUMN_USAGE.COLUMN_NAME);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addUniqueKey(key, table.getColumn(columnName));
            }
        }
    }

    private Result<Record4<String, String, String, String>> fetchKeys(String constraintType) {
        return create()
            .select(
                KEY_COLUMN_USAGE.CONSTRAINT_NAME,
                KEY_COLUMN_USAGE.TABLE_SCHEMA,
                KEY_COLUMN_USAGE.TABLE_NAME,
                KEY_COLUMN_USAGE.COLUMN_NAME)
            .from(TABLE_CONSTRAINTS)
            .join(KEY_COLUMN_USAGE)
            .on(TABLE_CONSTRAINTS.CONSTRAINT_SCHEMA.equal(KEY_COLUMN_USAGE.CONSTRAINT_SCHEMA))
            .and(TABLE_CONSTRAINTS.CONSTRAINT_NAME.equal(KEY_COLUMN_USAGE.CONSTRAINT_NAME))
            .where(TABLE_CONSTRAINTS.CONSTRAINT_TYPE.equal(constraintType))
            .and(TABLE_CONSTRAINTS.TABLE_SCHEMA.in(getInputSchemata()))
            .orderBy(
                KEY_COLUMN_USAGE.TABLE_SCHEMA.asc(),
                KEY_COLUMN_USAGE.TABLE_NAME.asc(),
                KEY_COLUMN_USAGE.CONSTRAINT_NAME.asc(),
                KEY_COLUMN_USAGE.ORDINAL_POSITION.asc())
            .fetch();
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        Result<?> result = create()
            .select(
                REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_NAME,
                REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_SCHEMA,
                KEY_COLUMN_USAGE.CONSTRAINT_NAME,
                KEY_COLUMN_USAGE.TABLE_SCHEMA,
                KEY_COLUMN_USAGE.TABLE_NAME,
                KEY_COLUMN_USAGE.COLUMN_NAME)
            .from(REFERENTIAL_CONSTRAINTS)
            .join(KEY_COLUMN_USAGE)
            .on(KEY_COLUMN_USAGE.CONSTRAINT_SCHEMA.equal(REFERENTIAL_CONSTRAINTS.CONSTRAINT_SCHEMA))
            .and(KEY_COLUMN_USAGE.CONSTRAINT_NAME.equal(REFERENTIAL_CONSTRAINTS.CONSTRAINT_NAME))
            .where(KEY_COLUMN_USAGE.TABLE_SCHEMA.in(getInputSchemata()))
            .orderBy(
                KEY_COLUMN_USAGE.TABLE_SCHEMA.asc(),
                KEY_COLUMN_USAGE.TABLE_NAME.asc(),
                KEY_COLUMN_USAGE.CONSTRAINT_NAME.asc(),
                KEY_COLUMN_USAGE.ORDINAL_POSITION.asc())
            .fetch();

        for (Record record : result) {
            SchemaDefinition foreignKeySchema = getSchema(record.getValue(KEY_COLUMN_USAGE.TABLE_SCHEMA));
            SchemaDefinition uniqueKeySchema = getSchema(record.getValue(REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_SCHEMA));

            String foreignKey = record.getValue(KEY_COLUMN_USAGE.CONSTRAINT_NAME);
            String foreignKeyTable = record.getValue(KEY_COLUMN_USAGE.TABLE_NAME);
            String foreignKeyColumn = record.getValue(KEY_COLUMN_USAGE.COLUMN_NAME);
            String uniqueKey = record.getValue(REFERENTIAL_CONSTRAINTS.UNIQUE_CONSTRAINT_NAME);

            TableDefinition referencingTable = getTable(foreignKeySchema, foreignKeyTable);

            if (referencingTable != null) {

                // [#986] Add the table name as a namespace prefix to the key
                // name. In Postgres, foreign key names are only unique per table
                ColumnDefinition referencingColumn = referencingTable.getColumn(foreignKeyColumn);
                relations.addForeignKey(foreignKeyTable + "__" + foreignKey, uniqueKey, referencingColumn, uniqueKeySchema);
            }
        }
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations relations) throws SQLException {
        TableConstraints tc = TABLE_CONSTRAINTS.as("tc");
        CheckConstraints cc = CHECK_CONSTRAINTS.as("cc");

        for (Record record : create()
                .select(
                    tc.TABLE_SCHEMA,
                    tc.TABLE_NAME,
                    cc.CONSTRAINT_NAME,
                    cc.CHECK_CLAUSE
                 )
                .from(tc)
                .join(cc)
                .using(tc.CONSTRAINT_CATALOG, tc.CONSTRAINT_SCHEMA, tc.CONSTRAINT_NAME)
                .where(tc.TABLE_SCHEMA.in(getInputSchemata()))
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(tc.TABLE_SCHEMA));
            TableDefinition table = getTable(schema, record.getValue(tc.TABLE_NAME));

            if (table != null) {
                relations.addCheckConstraint(table, new DefaultCheckConstraintDefinition(
                    schema,
                    table,
                    record.getValue(cc.CONSTRAINT_NAME),
                    record.getValue(cc.CHECK_CLAUSE)
                ));
            }
        }
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Record record : create()
                .select(
                    TABLES.TABLE_SCHEMA,
                    TABLES.TABLE_NAME)
                .from(TABLES)
                .where(TABLES.TABLE_SCHEMA.in(getInputSchemata()))
                .orderBy(
                    TABLES.TABLE_SCHEMA,
                    TABLES.TABLE_NAME)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(TABLES.TABLE_SCHEMA));
            String name = record.getValue(TABLES.TABLE_NAME);
            String comment = "";

            result.add(new PostgresTableDefinition(schema, name, comment));
        }

        return result;
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();

        // [#1409] Shouldn't select from INFORMATION_SCHEMA.SCHEMATA, as that
        // would only return schemata of which CURRENT_USER is the owner
        for (String name : create()
                .select(PG_NAMESPACE.NSPNAME)
                .from(PG_NAMESPACE)
                .fetch(PG_NAMESPACE.NSPNAME)) {

            result.add(new SchemaDefinition(this, name, ""));
        }

        return result;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (Record record : create()
                .select(
                    SEQUENCES.SEQUENCE_SCHEMA,
                    SEQUENCES.SEQUENCE_NAME,
                    SEQUENCES.DATA_TYPE,
                    SEQUENCES.NUMERIC_PRECISION,
                    SEQUENCES.NUMERIC_SCALE)
                .from(SEQUENCES)
                .where(SEQUENCES.SEQUENCE_SCHEMA.in(getInputSchemata()))
                .orderBy(
                    SEQUENCES.SEQUENCE_SCHEMA,
                    SEQUENCES.SEQUENCE_NAME)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(SEQUENCES.SEQUENCE_SCHEMA));

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                this, schema,
                record.getValue(SEQUENCES.DATA_TYPE),
                0,
                record.getValue(SEQUENCES.NUMERIC_PRECISION),
                record.getValue(SEQUENCES.NUMERIC_SCALE));

            result.add(new DefaultSequenceDefinition(schema, record.getValue(SEQUENCES.SEQUENCE_NAME), type));
        }

        return result;
    }

    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<EnumDefinition>();

        Result<Record3<String, String, String>> records = create()
            .select(
                PG_NAMESPACE.NSPNAME,
                PG_TYPE.TYPNAME,
                PG_ENUM.ENUMLABEL)
            .from(PG_ENUM)
            .join(PG_TYPE).on("pg_enum.enumtypid = pg_type.oid")
            .join(PG_NAMESPACE).on("pg_type.typnamespace = pg_namespace.oid")
            .where(PG_NAMESPACE.NSPNAME.in(getInputSchemata()))
            .orderBy(
                PG_NAMESPACE.NSPNAME,
                PG_TYPE.TYPNAME,
                PG_ENUM.ENUMLABEL)
            .fetch();

        DefaultEnumDefinition definition = null;
        for (Record record : records) {
            SchemaDefinition schema = getSchema(record.getValue(PG_NAMESPACE.NSPNAME));
            String typeName = String.valueOf(record.getValue(PG_TYPE.TYPNAME));

            if (definition == null || !definition.getName().equals(typeName)) {
                definition = new DefaultEnumDefinition(schema, typeName, null);
                result.add(definition);
            }

            definition.addLiteral(String.valueOf(record.getValue(PG_ENUM.ENUMLABEL)));
        }

        return result;
    }

    @Override
    protected List<UDTDefinition> getUDTs0() throws SQLException {
        List<UDTDefinition> result = new ArrayList<UDTDefinition>();

        for (Record record : create()
                .selectDistinct(
                    ATTRIBUTES.UDT_SCHEMA,
                    ATTRIBUTES.UDT_NAME)
                .from(ATTRIBUTES)
                .where(ATTRIBUTES.UDT_SCHEMA.in(getInputSchemata()))
                .orderBy(
                    ATTRIBUTES.UDT_SCHEMA,
                    ATTRIBUTES.UDT_NAME)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(ATTRIBUTES.UDT_SCHEMA));
            String name = record.getValue(ATTRIBUTES.UDT_NAME);

            result.add(new PostgresUDTDefinition(schema, name, null));
        }

        return result;
    }

    @Override
    protected List<ArrayDefinition> getArrays0() throws SQLException {
        List<ArrayDefinition> result = new ArrayList<ArrayDefinition>();
        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();

        Routines r1 = ROUTINES.as("r1");
        Routines r2 = ROUTINES.as("r2");

        for (Record record : create().select(
                r1.ROUTINE_SCHEMA,
                r1.ROUTINE_NAME,
                r1.SPECIFIC_NAME,

                // Ignore the data type when there is at least one out parameter
                decode()
                    .when(exists(
                        selectOne()
                        .from(PARAMETERS)
                        .where(PARAMETERS.SPECIFIC_SCHEMA.equal(r1.SPECIFIC_SCHEMA))
                        .and(PARAMETERS.SPECIFIC_NAME.equal(r1.SPECIFIC_NAME))
                        .and(upper(PARAMETERS.PARAMETER_MODE).notEqual("IN"))),
                            val("void"))
                    .otherwise(r1.DATA_TYPE).as("data_type"),
                r1.CHARACTER_MAXIMUM_LENGTH,
                r1.NUMERIC_PRECISION,
                r1.NUMERIC_SCALE,
                r1.TYPE_UDT_NAME,

                // Calculate overload index if applicable
                decode().when(
                exists(
                    selectOne()
                    .from(r2)
                    .where(r2.ROUTINE_SCHEMA.in(getInputSchemata()))
                    .and(r2.ROUTINE_SCHEMA.equal(r1.ROUTINE_SCHEMA))
                    .and(r2.ROUTINE_NAME.equal(r1.ROUTINE_NAME))
                    .and(r2.SPECIFIC_NAME.notEqual(r1.SPECIFIC_NAME))),
                    select(count())
                    .from(r2)
                    .where(r2.ROUTINE_SCHEMA.in(getInputSchemata()))
                    .and(r2.ROUTINE_SCHEMA.equal(r1.ROUTINE_SCHEMA))
                    .and(r2.ROUTINE_NAME.equal(r1.ROUTINE_NAME))
                    .and(r2.SPECIFIC_NAME.lessOrEqual(r1.SPECIFIC_NAME)).asField())
                .as("overload"))
            .from(r1)
            .where(r1.ROUTINE_SCHEMA.in(getInputSchemata()))
            .orderBy(
                r1.ROUTINE_SCHEMA.asc(),
                r1.ROUTINE_NAME.asc())
            .fetch()) {

            result.add(new PostgresRoutineDefinition(this, record));
        }

        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();
        return result;
    }

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.POSTGRES);
    }
}
