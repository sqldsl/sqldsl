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

import java.util.List;

/**
 * A general interface defining any database object, such as tables, views,
 * stored procedures, etc.
 *
 * @author Lukas Eder
 */
public interface Definition {

    /**
     * @return A reference to the Database context
     */
    Database getDatabase();

    /**
     * @return The schema of this object
     */
    SchemaDefinition getSchema();

    /**
     * @return The name of this object, e.g. [my_table]. This corresponds to
     *         {@link #getInputName()}
     */
    String getName();

    /**
     * @return The name of this object, e.g. [my_table], as defined in the
     *         source database.
     */
    String getInputName();

    /**
     * @return The name of this object, e.g. [my_table], as defined for the
     *         target database. This may differ from the input name if schema /
     *         table rewriting is applied.
     */
    String getOutputName();

    /**
     * @return The comment of this object
     */
    String getComment();

    /**
     * @return A path of definitions for this definition, e.g.
     *         <code>[schema].[package].[routine].[parameter]</code>
     */
    List<Definition> getDefinitionPath();

    /**
     * @return A qualified name for this object (corresponding to
     *         {@link #getName()})
     */
    String getQualifiedName();

    /**
     * @return A qualified name for this object (corresponding to
     *         {@link #getInputName()})
     */
    String getQualifiedInputName();

    /**
     * @return A qualified name for this object (corresponding to
     *         {@link #getOutputName()})
     */
    String getQualifiedOutputName();

    /**
     * @return The overload suffix if applicable
     */
    String getOverload();
}
