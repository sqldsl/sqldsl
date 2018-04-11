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

package org.jooq.util;

import java.sql.Connection;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;

/**
 * A base implementation for any type of definition.
 *
 * @author Lukas Eder
 */
public abstract class AbstractDefinition implements Definition {

    private final Database         database;
    private final SchemaDefinition schema;
    private final String           name;
    private final String           comment;
    private final String           overload;

    // [#2238] Some caches for strings that are heavy to calculate in large schemas
    private transient String       qualifiedInputName;
    private transient String       qualifiedOutputName;
    private transient Integer      hashCode;

    public AbstractDefinition(Database database, SchemaDefinition schema, String name) {
        this(database, schema, name, null);
    }

    public AbstractDefinition(Database database, SchemaDefinition schema, String name, String comment) {
        this(database, schema, name, comment, null);
    }

    public AbstractDefinition(Database database, SchemaDefinition schema, String name, String comment, String overload) {
        this.database = database;

        // The subclass constructor cannot pass "this" to the super constructor
        this.schema = (schema == null && this instanceof SchemaDefinition)
            ? (SchemaDefinition) this
            : schema;
        this.name = name;
        this.comment = comment;
        this.overload = overload;
    }

    @Override
    public final String getOverload() {
        return overload;
    }

    @Override
    public final SchemaDefinition getSchema() {
        return schema;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final String getInputName() {
        return name;
    }

    /**
     * Subclasses may override this method
     *
     * {@inheritDoc}
     */
    @Override
    public String getOutputName() {
        return getInputName();
    }

    @Override
    public final String getComment() {
        return comment;
    }

    @Override
    public final String getQualifiedName() {
        return getQualifiedInputName();
    }

    /**
     * Subclasses may override this method
     *
     * {@inheritDoc}
     */
    @Override
    public final String getQualifiedInputName() {
        if (qualifiedInputName == null) {
            StringBuilder sb = new StringBuilder();

            String separator = "";
            for (Definition part : getDefinitionPath()) {
                if (part instanceof SchemaDefinition && ((SchemaDefinition) part).isDefaultSchema()) {
                    continue;
                }

                sb.append(separator);
                sb.append(part.getInputName());

                separator = ".";
            }

            qualifiedInputName = sb.toString();
        }

        return qualifiedInputName;
    }

    /**
     * Subclasses may override this method
     *
     * {@inheritDoc}
     */
    @Override
    public final String getQualifiedOutputName() {
        if (qualifiedOutputName == null) {
            StringBuilder sb = new StringBuilder();

            String separator = "";
            for (Definition part : getDefinitionPath()) {
                if (part instanceof SchemaDefinition && ((SchemaDefinition) part).isDefaultSchema()) {
                    continue;
                }

                sb.append(separator);
                sb.append(part.getOutputName());

                separator = ".";
            }

            qualifiedOutputName = sb.toString();
        }

        return qualifiedOutputName;
    }

    @Override
    public final Database getDatabase() {
        return database;
    }

    protected final Connection getConnection() {
        return database.getConnection();
    }

    @Override
    public final String toString() {
        return getQualifiedName();
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof Definition) {
            Definition that = (Definition) obj;
            return that.getQualifiedName().equals(getQualifiedName());
        }

        return false;
    }

    @Override
    public final int hashCode() {
        if (hashCode == null) {
            hashCode = getQualifiedName().hashCode();
        }

        return hashCode;
    }

    protected final DSLContext create() {
        return database.create();
    }

    protected final SQLDialect getDialect() {
        return database.getDialect();
    }
}
