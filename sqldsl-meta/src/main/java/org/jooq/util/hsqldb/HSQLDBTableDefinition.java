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

package org.jooq.util.hsqldb;

import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.nvl2;
import static org.jooq.impl.DSL.val;
import static org.jooq.util.hsqldb.information_schema.Tables.COLUMNS;
import static org.jooq.util.hsqldb.information_schema.Tables.ELEMENT_TYPES;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.util.AbstractTableDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultColumnDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.SchemaDefinition;

/**
 * @author Lukas Eder
 */
public class HSQLDBTableDefinition extends AbstractTableDefinition {

	public HSQLDBTableDefinition(SchemaDefinition schema, String name, String comment) {
		super(schema, name, comment);
	}

	@Override
	public List<ColumnDefinition> getElements0() throws SQLException {
		List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

        for (Record record : create().select(
                COLUMNS.COLUMN_NAME,
                COLUMNS.ORDINAL_POSITION,
                nvl(ELEMENT_TYPES.COLLECTION_TYPE_IDENTIFIER,
                    nvl2(COLUMNS.INTERVAL_TYPE,
                        concat(COLUMNS.DATA_TYPE, val(" "), COLUMNS.INTERVAL_TYPE),
                        COLUMNS.DATA_TYPE)).as("datatype"),
                COLUMNS.IDENTITY_GENERATION,
                COLUMNS.IS_NULLABLE,
                COLUMNS.COLUMN_DEFAULT,
                COLUMNS.CHARACTER_MAXIMUM_LENGTH,
                COLUMNS.NUMERIC_PRECISION,
                COLUMNS.NUMERIC_SCALE,
                COLUMNS.UDT_NAME)
            .from(COLUMNS)
            .leftOuterJoin(ELEMENT_TYPES)
            .on(COLUMNS.TABLE_SCHEMA.equal(ELEMENT_TYPES.OBJECT_SCHEMA))
            .and(COLUMNS.TABLE_NAME.equal(ELEMENT_TYPES.OBJECT_NAME))
            .and(COLUMNS.DTD_IDENTIFIER.equal(ELEMENT_TYPES.COLLECTION_TYPE_IDENTIFIER))
            .where(COLUMNS.TABLE_SCHEMA.equal(getSchema().getName()))
            .and(COLUMNS.TABLE_NAME.equal(getName()))
            .orderBy(COLUMNS.ORDINAL_POSITION)
            .fetch()) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue("datatype", String.class),
                record.getValue(COLUMNS.CHARACTER_MAXIMUM_LENGTH),
                record.getValue(COLUMNS.NUMERIC_PRECISION),
                record.getValue(COLUMNS.NUMERIC_SCALE),
                record.getValue(COLUMNS.UDT_NAME));

			ColumnDefinition column = new DefaultColumnDefinition(
			    getDatabase().getTable(getSchema(), getName()),
			    record.getValue(COLUMNS.COLUMN_NAME),
			    record.getValue(COLUMNS.ORDINAL_POSITION, int.class),
			    type,
			    record.getValue(COLUMNS.IS_NULLABLE, boolean.class),
			    null != record.getValue(COLUMNS.IDENTITY_GENERATION),
			    null);
			result.add(column);
		}

		return result;
	}
}
