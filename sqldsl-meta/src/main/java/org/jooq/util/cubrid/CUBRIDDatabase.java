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

package org.jooq.util.cubrid;

import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.fieldByName;
import static org.jooq.impl.DSL.val;
import static org.jooq.util.cubrid.dba.Tables.DB_CLASS;
import static org.jooq.util.cubrid.dba.Tables.DB_INDEX;
import static org.jooq.util.cubrid.dba.Tables.DB_INDEX_KEY;
import static org.jooq.util.cubrid.dba.Tables.DB_SERIAL;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
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

/**
 * @author Lukas Eder
 */
public class CUBRIDDatabase extends AbstractDatabase {

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys(DB_INDEX.IS_UNIQUE.isTrue().and(DB_INDEX.IS_PRIMARY_KEY.isFalse()))) {
            String key = record.getValue("constraint_name", String.class);
            String tableName = record.getValue(DB_CLASS.CLASS_NAME);
            String columnName = record.getValue(DB_INDEX_KEY.KEY_ATTR_NAME);

            TableDefinition table = getTable(getSchemata().get(0), tableName);
            if (table != null) {
                relations.addUniqueKey(key, table.getColumn(columnName));
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys(DB_INDEX.IS_PRIMARY_KEY.isTrue())) {
            String key = record.getValue("constraint_name", String.class);
            String tableName = record.getValue(DB_CLASS.CLASS_NAME);
            String columnName = record.getValue(DB_INDEX_KEY.KEY_ATTR_NAME);

            TableDefinition table = getTable(getSchemata().get(0), tableName);
            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
        }
    }

    private Result<Record3<String, String, String>> fetchKeys(Condition condition) {
        return
        create().select(
                    concat(DB_CLASS.CLASS_NAME, val("__"), DB_INDEX.INDEX_NAME).as("constraint_name"),
                    DB_INDEX_KEY.KEY_ATTR_NAME,
                    DB_CLASS.CLASS_NAME)
                .from(DB_INDEX)
                .join(DB_CLASS).on(DB_INDEX.CLASS_NAME.equal(DB_CLASS.CLASS_NAME))
                .join(DB_INDEX_KEY).on(
                    DB_INDEX_KEY.INDEX_NAME.equal(DB_INDEX.INDEX_NAME).and(
                    DB_INDEX_KEY.CLASS_NAME.equal(DB_INDEX.CLASS_NAME)))
                .where(condition)
                .orderBy(
                    DB_INDEX.INDEX_NAME.asc())
                .fetch();
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        ConnectionProvider provider = create().configuration().connectionProvider();
        Connection connection = null;

        try {
            connection = provider.acquire();
            DatabaseMetaData meta = connection.getMetaData();

            for (String table : create()
                    .selectDistinct(DB_INDEX.CLASS_NAME)
                    .from(DB_INDEX)
                    .where(DB_INDEX.IS_FOREIGN_KEY.isTrue())
                    .fetch(DB_INDEX.CLASS_NAME)) {

                for (Record record : create().fetch(meta.getImportedKeys(null, null, table))) {
                    String foreignKeyName =
                        record.getValue("FKTABLE_NAME", String.class) +
                        "__" +
                        record.getValue("FK_NAME", String.class);
                    String foreignKeyTableName = record.getValue("FKTABLE_NAME", String.class);
                    String foreignKeyColumnName = record.getValue("FKCOLUMN_NAME", String.class);
                    String uniqueKeyName =
                        record.getValue("PKTABLE_NAME", String.class) +
                        "__" +
                        record.getValue("PK_NAME", String.class);

                    TableDefinition referencingTable = getTable(getSchemata().get(0), foreignKeyTableName);
                    if (referencingTable != null) {
                        ColumnDefinition column = referencingTable.getColumn(foreignKeyColumnName);
                        relations.addForeignKey(foreignKeyName, uniqueKeyName, column, getSchemata().get(0));
                    }
                }
            }
        }
        finally {
            if (connection != null) {
                provider.release(connection);
            }
        }
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations r) throws SQLException {
        // Currently not supported
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();
        result.add(new SchemaDefinition(this, "", ""));
        return result;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (Record record : create()
                .select(
                    DB_SERIAL.NAME,
                    DB_SERIAL.MAX_VAL)
                .from(DB_SERIAL)
                .fetch()) {

            BigInteger value = record.getValue(DB_SERIAL.MAX_VAL, BigInteger.class, BigInteger.valueOf(Long.MAX_VALUE));
            DataTypeDefinition type = getDataTypeForMAX_VAL(getSchemata().get(0), value);

            result.add(new DefaultSequenceDefinition(
                getSchemata().get(0),
                record.getValue(DB_SERIAL.NAME),
                type));
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Record record : create()
                .select(
                    DB_CLASS.CLASS_NAME)
                .from(DB_CLASS)
                .orderBy(
                    DB_CLASS.CLASS_NAME.asc())
                .fetch()) {

            String name = record.getValue(DB_CLASS.CLASS_NAME);

            CUBRIDTableDefinition table = new CUBRIDTableDefinition(getSchemata().get(0), name, null);
            result.add(table);
        }

        return result;
    }

    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<EnumDefinition>();

        for (TableDefinition tableDefinition : getTables(getSchemata().get(0))) {
            for (Record record : create().fetch("SHOW COLUMNS FROM {0} WHERE TYPE LIKE 'ENUM(%)'", fieldByName(tableDefinition.getInputName()))) {
                String table = tableDefinition.getInputName();
                String column = record.getValue("Field", String.class);
                String columnType = record.getValue("Type", String.class);
                String name = table + "_" + column;

                ColumnDefinition columnDefinition = tableDefinition.getColumn(column);

                // [#1137] Avoid generating enum classes for enum types that
                // are explicitly forced to another type
                if (getConfiguredForcedType(columnDefinition) == null) {
                    DefaultEnumDefinition definition = new DefaultEnumDefinition(getSchemata().get(0), name, "");
                    for (String string : columnType.replaceAll("ENUM\\(|\\)", "").split(",")) {
                        definition.addLiteral(string.trim().replaceAll("'", ""));
                    }

                    result.add(definition);
                }
            }
        }

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

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();
        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();
        return result;
    }

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.CUBRID);
    }
}
