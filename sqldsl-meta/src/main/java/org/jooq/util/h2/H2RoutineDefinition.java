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


import static org.jooq.util.h2.information_schema.tables.FunctionColumns.FUNCTION_COLUMNS;

import java.sql.SQLException;

import org.jooq.Record;
import org.jooq.tools.StringUtils;
import org.jooq.util.AbstractRoutineDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.InOutDefinition;
import org.jooq.util.ParameterDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.h2.information_schema.tables.FunctionColumns;

/**
 * H2 implementation of {@link AbstractRoutineDefinition}
 *
 * @author Espen Stromsnes
 * @author Lukas Eder
 */
public class H2RoutineDefinition extends AbstractRoutineDefinition {

    public H2RoutineDefinition(SchemaDefinition schema, String name, String comment, String typeName, Number precision, Number scale) {
        super(schema, null, name, comment, null);

        if (!StringUtils.isBlank(typeName)) {
            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                schema,
                typeName,
                precision,
                precision,
                scale);

            this.returnValue = new DefaultParameterDefinition(this, "RETURN_VALUE", -1, type);
        }
    }

    @Override
    protected void init0() throws SQLException {
        for (Record record : create()
                .select(
                    FunctionColumns.COLUMN_NAME,
                    FunctionColumns.TYPE_NAME,
                    FunctionColumns.PRECISION,
                    FunctionColumns.SCALE,
                    FunctionColumns.POS)
                .from(FUNCTION_COLUMNS)
                .where(FunctionColumns.ALIAS_SCHEMA.equal(getSchema().getName()))
                .and(FunctionColumns.ALIAS_NAME.equal(getName()))
                .orderBy(FunctionColumns.POS.asc()).fetch()) {

            String paramName = record.getValue(FunctionColumns.COLUMN_NAME);
            String typeName = record.getValue(FunctionColumns.TYPE_NAME);
            Integer precision = record.getValue(FunctionColumns.PRECISION);
            Short scale = record.getValue(FunctionColumns.SCALE);
            int position = record.getValue(FunctionColumns.POS);

            // VERY special case for H2 alias/function parameters. The first parameter
            // may be a java.sql.Connection object and in such cases it should NEVER be used.
            // It is only used internally by H2 to provide a connection to the current database.
            if (position == 0 && H2DataType.OTHER.getTypeName().equalsIgnoreCase(typeName)) {
                continue;
            }

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(), typeName,
                precision,
                precision,
                scale);

            ParameterDefinition parameter = new DefaultParameterDefinition(this, paramName, position, type);

            addParameter(InOutDefinition.IN, parameter);
        }
    }
}
