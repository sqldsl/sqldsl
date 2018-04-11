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
package org.jooq.util.ingres;

import static org.jooq.impl.DSL.trim;
import static org.jooq.util.ingres.ingres.tables.Iicolumns.IICOLUMNS;
import static org.jooq.util.ingres.ingres.tables.IidbSubcomments.IIDB_SUBCOMMENTS;

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
import org.jooq.util.ingres.ingres.tables.Iicolumns;
import org.jooq.util.ingres.ingres.tables.IidbSubcomments;

/**
 * @author Lukas Eder
 */
public class IngresTableDefinition extends AbstractTableDefinition {

    public IngresTableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    @Override
    protected List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

        for (Record record : create().select(
                    Iicolumns.COLUMN_SEQUENCE,
                    trim(Iicolumns.COLUMN_NAME),
                    trim(Iicolumns.COLUMN_DATATYPE),
                    Iicolumns.COLUMN_LENGTH,
                    Iicolumns.COLUMN_SCALE,
                    Iicolumns.COLUMN_NULLS,
                    Iicolumns.COLUMN_ALWAYS_IDENT,
                    Iicolumns.COLUMN_BYDEFAULT_IDENT,
                    trim(IidbSubcomments.LONG_REMARK))
                .from(IICOLUMNS)
                .leftOuterJoin(IIDB_SUBCOMMENTS)
                .on(IidbSubcomments.OBJECT_NAME.equal(Iicolumns.TABLE_NAME))
                .and(IidbSubcomments.OBJECT_OWNER.equal(Iicolumns.TABLE_OWNER))
                .and(IidbSubcomments.SUBOBJECT_NAME.equal(Iicolumns.COLUMN_NAME))
                .and(IidbSubcomments.SUBOBJECT_TYPE.equal("C"))
                .and(IidbSubcomments.TEXT_SEQUENCE.equal(1L))
                .where(Iicolumns.TABLE_OWNER.equal(getSchema().getName()))
                .and(trim(Iicolumns.TABLE_NAME).equal(getName()))
                .orderBy(Iicolumns.COLUMN_SEQUENCE)
                .fetch()) {

            String typeName = record.getValue(trim(Iicolumns.COLUMN_DATATYPE));

            // [#664] INTEGER types come with a COLUMN_LENGTH in bytes
            // This is important to distinguish BIGINT, INT, SMALLINT, TINYINT
            if (IngresDataType.INTEGER.getTypeName().equalsIgnoreCase(typeName)) {
                switch (record.getValue(Iicolumns.COLUMN_LENGTH)) {
                    case 8:
                        typeName = IngresDataType.BIGINT.getTypeName();
                        break;
                    case 2:
                        typeName = IngresDataType.SMALLINT.getTypeName();
                        break;
                    case 1:
                        typeName = IngresDataType.TINYINT.getTypeName();
                        break;
                }
            }

            // [#744] FLOAT types have the same issue
            else if (IngresDataType.FLOAT.getTypeName().equalsIgnoreCase(typeName)) {
                switch (record.getValue(Iicolumns.COLUMN_LENGTH)) {
                    case 8:
                        typeName = IngresDataType.FLOAT8.getTypeName();
                        break;
                    case 4:
                        typeName = IngresDataType.FLOAT4.getTypeName();
                        break;
                }
            }

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                typeName,
                record.getValue(Iicolumns.COLUMN_LENGTH),
                record.getValue(Iicolumns.COLUMN_LENGTH),
                record.getValue(Iicolumns.COLUMN_SCALE));

            ColumnDefinition column = new DefaultColumnDefinition(
                getDatabase().getTable(getSchema(), getName()),
                record.getValue(trim(Iicolumns.COLUMN_NAME)),
                record.getValue(Iicolumns.COLUMN_SEQUENCE),
                type,
                record.getValue(Iicolumns.COLUMN_NULLS, boolean.class),
                record.getValue(Iicolumns.COLUMN_ALWAYS_IDENT, boolean.class) ||
                record.getValue(Iicolumns.COLUMN_BYDEFAULT_IDENT, boolean.class),
                record.getValue(trim(IidbSubcomments.LONG_REMARK)));
            result.add(column);
        }

        return result;
    }
}
