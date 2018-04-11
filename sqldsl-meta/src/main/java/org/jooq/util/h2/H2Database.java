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
package org.jooq.util.h2;

import static org.jooq.impl.DSL.select;
import static org.jooq.util.h2.information_schema.tables.Columns.COLUMNS;
import static org.jooq.util.h2.information_schema.tables.Constraints.CONSTRAINTS;
import static org.jooq.util.h2.information_schema.tables.CrossReferences.CROSS_REFERENCES;
import static org.jooq.util.h2.information_schema.tables.FunctionAliases.FUNCTION_ALIASES;
import static org.jooq.util.h2.information_schema.tables.Schemata.SCHEMATA;
import static org.jooq.util.h2.information_schema.tables.Sequences.SEQUENCES;
import static org.jooq.util.h2.information_schema.tables.Tables.TABLES;
import static org.jooq.util.h2.information_schema.tables.TypeInfo.TYPE_INFO;

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
import org.jooq.util.h2.information_schema.tables.Columns;
import org.jooq.util.h2.information_schema.tables.Constraints;
import org.jooq.util.h2.information_schema.tables.CrossReferences;
import org.jooq.util.h2.information_schema.tables.FunctionAliases;
import org.jooq.util.h2.information_schema.tables.Schemata;
import org.jooq.util.h2.information_schema.tables.Sequences;
import org.jooq.util.h2.information_schema.tables.Tables;
import org.jooq.util.h2.information_schema.tables.TypeInfo;

/**
 * H2 implementation of {@link AbstractDatabase}
 *
 * @author Espen Stromsnes
 */
public class H2Database extends AbstractDatabase {

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.H2);
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("PRIMARY KEY")) {
            SchemaDefinition schema = getSchema(record.getValue(Constraints.TABLE_SCHEMA));
            String tableName = record.getValue(Constraints.TABLE_NAME);
            String columnList = record.getValue(Constraints.COLUMN_LIST);
            String primaryKey = record.getValue(Constraints.CONSTRAINT_NAME);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                String[] columnNames = columnList.split("[,]+");

                for (String columnName : columnNames) {
                    relations.addPrimaryKey(primaryKey, table.getColumn(columnName));
                }
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("UNIQUE")) {
            SchemaDefinition schema = getSchema(record.getValue(Constraints.TABLE_SCHEMA));
            String tableName = record.getValue(Constraints.TABLE_NAME);
            String columnList = record.getValue(Constraints.COLUMN_LIST);
            String primaryKey = record.getValue(Constraints.CONSTRAINT_NAME);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                String[] columnNames = columnList.split("[,]+");

                for (String columnName : columnNames) {
                    relations.addUniqueKey(primaryKey, table.getColumn(columnName));
                }
            }
        }
    }

    private Result<Record4<String, String, String, String>> fetchKeys(String constraintType) {
        return create().select(
                    Constraints.TABLE_SCHEMA,
                    Constraints.TABLE_NAME,
                    Constraints.COLUMN_LIST,
                    Constraints.CONSTRAINT_NAME)
                .from(CONSTRAINTS)
                .where(Constraints.TABLE_SCHEMA.in(getInputSchemata()))
                .and(Constraints.CONSTRAINT_TYPE.equal(constraintType))
                .orderBy(
                    Constraints.TABLE_SCHEMA,
                    Constraints.CONSTRAINT_NAME,
                    Constraints.COLUMN_LIST)
                .fetch();
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        for (Record record : create().select(
                    CrossReferences.FK_NAME,
                    CrossReferences.FKTABLE_NAME,
                    CrossReferences.FKTABLE_SCHEMA,
                    CrossReferences.FKCOLUMN_NAME,
                    Constraints.CONSTRAINT_NAME,
                    Constraints.CONSTRAINT_SCHEMA)
                .from(CROSS_REFERENCES)
                .join(CONSTRAINTS)
                .on(CrossReferences.PK_NAME.equal(Constraints.UNIQUE_INDEX_NAME))
                .and(CrossReferences.PKTABLE_NAME.equal(Constraints.TABLE_NAME))
                .and(CrossReferences.PKTABLE_SCHEMA.equal(Constraints.TABLE_SCHEMA))
                .where(CrossReferences.FKTABLE_SCHEMA.in(getInputSchemata()))
                .orderBy(
                    CrossReferences.FKTABLE_SCHEMA.asc(),
                    CrossReferences.FK_NAME.asc(),
                    CrossReferences.ORDINAL_POSITION.asc())
                .fetch()) {

            SchemaDefinition foreignKeySchema = getSchema(record.getValue(CrossReferences.FKTABLE_SCHEMA));
            SchemaDefinition uniqueKeySchema = getSchema(record.getValue(Constraints.CONSTRAINT_SCHEMA));

            String foreignKeyTableName = record.getValue(CrossReferences.FKTABLE_NAME);
            String foreignKeyColumn = record.getValue(CrossReferences.FKCOLUMN_NAME);
            String foreignKey = record.getValue(CrossReferences.FK_NAME);
            String uniqueKey = record.getValue(Constraints.CONSTRAINT_NAME);

            TableDefinition foreignKeyTable = getTable(foreignKeySchema, foreignKeyTableName);
            if (foreignKeyTable != null) {
                ColumnDefinition referencingColumn = foreignKeyTable.getColumn(foreignKeyColumn);

                relations.addForeignKey(foreignKey, uniqueKey, referencingColumn, uniqueKeySchema);
            }
        }
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations relations) throws SQLException {

        // TODO: Should we really UNION INFORMATION_SCHEMA.COLUMNS.CHECK_CONSTRAINT?
        for (Record record : create()
            .select(
                Constraints.TABLE_SCHEMA,
                Constraints.TABLE_NAME,
                Constraints.CONSTRAINT_NAME,
                Constraints.CHECK_EXPRESSION
             )
            .from(CONSTRAINTS)
            .where(Constraints.CONSTRAINT_TYPE.eq("CHECK"))
            .and(Constraints.TABLE_SCHEMA.in(getInputSchemata()))
            .union(
            select(
                Columns.TABLE_SCHEMA,
                Columns.TABLE_NAME,
                Columns.CHECK_CONSTRAINT,
                Columns.CHECK_CONSTRAINT
            )
            .from(COLUMNS)
            .where(Columns.CHECK_CONSTRAINT.nvl("").ne(""))
            .and(Columns.TABLE_SCHEMA.in(getInputSchemata())))
            .fetch()) {

        SchemaDefinition schema = getSchema(record.getValue(Constraints.TABLE_SCHEMA));
        TableDefinition table = getTable(schema, record.getValue(Constraints.TABLE_NAME));

        if (table != null) {
            relations.addCheckConstraint(table, new DefaultCheckConstraintDefinition(
                schema,
                table,
                record.getValue(Constraints.CONSTRAINT_NAME),
                record.getValue(Constraints.CHECK_EXPRESSION)
            ));
        }
    }
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();

        for (Record record : create().select(
                    Schemata.SCHEMA_NAME,
                    Schemata.REMARKS)
                .from(SCHEMATA)
                .fetch()) {

            result.add(new SchemaDefinition(this,
                record.getValue(Schemata.SCHEMA_NAME),
                record.getValue(Schemata.REMARKS)));
        }

        return result;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (Record record : create().select(
                    Sequences.SEQUENCE_SCHEMA,
                    Sequences.SEQUENCE_NAME)
                .from(SEQUENCES)
                .where(Sequences.SEQUENCE_SCHEMA.in(getInputSchemata()))
                .orderBy(
                    Sequences.SEQUENCE_SCHEMA,
                    Sequences.SEQUENCE_NAME)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(Sequences.SEQUENCE_SCHEMA));
            String name = record.getValue(Sequences.SEQUENCE_NAME);

            DefaultDataTypeDefinition type = new DefaultDataTypeDefinition(
                this,
                schema,
                H2DataType.BIGINT.getTypeName(), 0, 0, 0);

            result.add(new DefaultSequenceDefinition(schema, name, type));
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Record record : create().select(
                    Tables.TABLE_SCHEMA,
                    Tables.TABLE_NAME,
                    Tables.REMARKS)
                .from(TABLES)
                .where(Tables.TABLE_SCHEMA.in(getInputSchemata()))
                .orderBy(
                    Tables.TABLE_SCHEMA,
                    Tables.ID)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(Tables.TABLE_SCHEMA));
            String name = record.getValue(Tables.TABLE_NAME);
            String comment = record.getValue(Tables.REMARKS);

            H2TableDefinition table = new H2TableDefinition(schema, name, comment);
            result.add(table);
        }

        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();

        for (Record record : create()
                .select(
                    FunctionAliases.ALIAS_SCHEMA,
                    FunctionAliases.ALIAS_NAME,
                    FunctionAliases.REMARKS,
                    FunctionAliases.DATA_TYPE,
                    FunctionAliases.RETURNS_RESULT,
                    TypeInfo.TYPE_NAME,
                    TypeInfo.PRECISION,
                    TypeInfo.MAXIMUM_SCALE)
                .from(FUNCTION_ALIASES)
                .leftOuterJoin(TYPE_INFO)
                .on(FunctionAliases.DATA_TYPE.equal(TypeInfo.DATA_TYPE))
                // [#1256] TODO implement this correctly. Not sure if POS = 0
                // is the right predicate to rule out duplicate entries in TYPE_INFO
                .and(TypeInfo.POS.equal(0))
                .where(FunctionAliases.ALIAS_SCHEMA.in(getInputSchemata()))
                .and(FunctionAliases.RETURNS_RESULT.in((short) 1, (short) 2))
                .orderBy(FunctionAliases.ALIAS_NAME).fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(FunctionAliases.ALIAS_SCHEMA));
            String name = record.getValue(FunctionAliases.ALIAS_NAME);
            String comment = record.getValue(FunctionAliases.REMARKS);
            String typeName = record.getValue(TypeInfo.TYPE_NAME);
            Integer precision = record.getValue(TypeInfo.PRECISION);
            Short scale = record.getValue(TypeInfo.MAXIMUM_SCALE);

            result.add(new H2RoutineDefinition(schema, name, comment, typeName, precision, scale));
        }

        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();
        return result;
    }

    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<EnumDefinition>();
        return result;
    }

    @Override
    protected List<UDTDefinition> getUDTs0() throws SQLException {
        List<UDTDefinition> result = new ArrayList<UDTDefinition>();
        return result;
    }

    @Override
    protected List<ArrayDefinition> getArrays0() throws SQLException {
        List<ArrayDefinition> result = new ArrayList<ArrayDefinition>();
        return result;
    }
}
