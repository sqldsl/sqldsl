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
package org.jooq.util.sqlserver;

import static org.jooq.util.sqlserver.information_schema.Tables.PARAMETERS;
import static org.jooq.util.sqlserver.information_schema.Tables.ROUTINES;

import java.sql.SQLException;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.tools.StringUtils;
import org.jooq.util.AbstractRoutineDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultParameterDefinition;
import org.jooq.util.InOutDefinition;
import org.jooq.util.ParameterDefinition;
import org.jooq.util.SchemaDefinition;

/**
 * @author Lukas Eder
 */
public class SQLServerRoutineDefinition extends AbstractRoutineDefinition {

    /**
     * internal name for the function used by HSQLDB / SQL Server
     */
    private final String specificName;

    public SQLServerRoutineDefinition(SchemaDefinition schema, String name, String specificName, String dataType, Number length, Number precision, Number scale) {
        super(schema, null, name, null, null);

        if (!StringUtils.isBlank(dataType)) {
            DataTypeDefinition type = new DefaultDataTypeDefinition(getDatabase(), schema, dataType, length, precision, scale);
            this.returnValue = new DefaultParameterDefinition(this, "RETURN_VALUE", -1, type);
        }

        this.specificName = specificName;
    }

    @Override
    protected void init0() throws SQLException {
        Result<?> result = create().selectDistinct(
                PARAMETERS.PARAMETER_MODE,
                PARAMETERS.PARAMETER_NAME,
                PARAMETERS.DATA_TYPE,
                PARAMETERS.CHARACTER_MAXIMUM_LENGTH,
                PARAMETERS.NUMERIC_PRECISION,
                PARAMETERS.NUMERIC_SCALE,
                PARAMETERS.ORDINAL_POSITION,
                PARAMETERS.IS_RESULT)
            .from(PARAMETERS)
            .join(ROUTINES)
            .on(PARAMETERS.SPECIFIC_SCHEMA.equal(ROUTINES.SPECIFIC_SCHEMA))
            .and(PARAMETERS.SPECIFIC_NAME.equal(ROUTINES.SPECIFIC_NAME))
            .where(PARAMETERS.SPECIFIC_SCHEMA.equal(getSchema().getName()))
            .and(PARAMETERS.SPECIFIC_NAME.equal(this.specificName))
            .and(PARAMETERS.IS_RESULT.isFalse())
            .orderBy(PARAMETERS.ORDINAL_POSITION.asc()).fetch();

        for (Record record : result) {
            String inOut = record.getValue(PARAMETERS.PARAMETER_MODE);

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue(PARAMETERS.DATA_TYPE),
                record.getValue(PARAMETERS.CHARACTER_MAXIMUM_LENGTH),
                record.getValue(PARAMETERS.NUMERIC_PRECISION),
                record.getValue(PARAMETERS.NUMERIC_SCALE));

            ParameterDefinition parameter = new DefaultParameterDefinition(
                this,
                record.getValue(PARAMETERS.PARAMETER_NAME).replaceAll("@", ""),
                record.getValue(PARAMETERS.ORDINAL_POSITION, int.class),
                type);

            addParameter(InOutDefinition.getFromString(inOut), parameter);
        }
    }
}
