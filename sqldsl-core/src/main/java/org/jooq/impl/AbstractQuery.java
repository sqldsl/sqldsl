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

package org.jooq.impl;

import static org.jooq.conf.ParamType.INDEXED;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.SettingsTools.executePreparedStatements;
import static org.jooq.conf.SettingsTools.getParamType;
import static org.jooq.impl.Utils.DATA_COUNT_BIND_VALUES;
import static org.jooq.impl.Utils.DATA_FORCE_STATIC_STATEMENT;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.jooq.AttachableInternal;
import org.jooq.Configuration;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Param;
import org.jooq.Query;
import org.jooq.RenderContext;
import org.jooq.conf.ParamType;
import org.jooq.conf.StatementType;
import org.jooq.exception.DetachedException;
import org.jooq.tools.JooqLogger;

/**
 * @author Lukas Eder
 */
abstract class AbstractQuery extends AbstractQueryPart implements Query, AttachableInternal {

    private static final long           serialVersionUID = -8046199737354507547L;
    private static final JooqLogger     log              = JooqLogger.getLogger(AbstractQuery.class);

    private Configuration               configuration;
    private int                         timeout;
    private boolean                     keepStatement;
    private transient PreparedStatement statement;
    private transient String            sql;

    AbstractQuery(Configuration configuration) {
        this.configuration = configuration;
    }

    // -------------------------------------------------------------------------
    // The Attachable and Attachable internal API
    // -------------------------------------------------------------------------

    @Override
    public final void attach(Configuration c) {
        configuration = c;
    }

    @Override
    public final Configuration configuration() {
        return configuration;
    }

    // -------------------------------------------------------------------------
    // The Query API
    // -------------------------------------------------------------------------

    @Override
    public final List<Object> getBindValues() {
        return create().extractBindValues(this);
    }

    @Override
    public final Map<String, Param<?>> getParams() {
        return create().extractParams(this);
    }

    @Override
    public final Param<?> getParam(String name) {
        return create().extractParam(this, name);
    }

    /**
     * Subclasses may override this for covariant result types
     * <p>
     * {@inheritDoc}
     */
    @Override
    public Query bind(String param, Object value) {
        try {
            int index = Integer.valueOf(param);
            return bind(index, value);
        }
        catch (NumberFormatException e) {
            Param<?> p = getParam(param);

            if (p == null) {
                throw new IllegalArgumentException("No such parameter : " + param);
            }

            p.setConverted(value);
            closeIfNecessary(p);
            return this;
        }
    }

    /**
     * Subclasses may override this for covariant result types
     * <p>
     * {@inheritDoc}
     */
    @Override
    public Query bind(int index, Object value) {
        Param<?>[] params = getParams().values().toArray(new Param[0]);

        if (index < 1 || index > params.length) {
            throw new IllegalArgumentException("Index out of range for Query parameters : " + index);
        }

        Param<?> param = params[index - 1];
        param.setConverted(value);
        closeIfNecessary(param);
        return this;
    }

    /**
     * Close the statement if necessary.
     * <p>
     * [#1886] If there is an open (cached) statement and its bind values are
     * inlined due to a {@link StatementType#STATIC_STATEMENT} setting, the
     * statement should be closed.
     *
     * @param param The param that was changed
     */
    private final void closeIfNecessary(Param<?> param) {

        // This is relevant when there is an open statement, only
        if (keepStatement() && statement != null) {

            // When an inlined param is being changed, the previous statement
            // has to be closed, regardless if variable binding is performed
            if (param.isInline()) {
                close();
            }

            // If all params are inlined, the previous statement always has to
            // be closed
            else if (getParamType(configuration().settings()) == INLINED) {
                close();
            }
        }
    }

    /**
     * Subclasses may override this for covariant result types
     * <p>
     * {@inheritDoc}
     */
    @Override
    public Query queryTimeout(int t) {
        this.timeout = t;
        return this;
    }

    /**
     * Subclasses may override this for covariant result types
     * <p>
     * {@inheritDoc}
     */
    @Override
    public Query keepStatement(boolean k) {
        this.keepStatement = k;
        return this;
    }

    protected final boolean keepStatement() {
        return keepStatement;
    }

    @Override
    public final void close() {
        if (statement != null) {
            try {
                statement.close();
                statement = null;
            }
            catch (SQLException e) {
                throw Utils.translate(sql, e);
            }
        }
    }

    @Override
    public final void cancel() {
        if (statement != null) {
            try {
                statement.cancel();
            }
            catch (SQLException e) {
                throw Utils.translate(sql, e);
            }
        }
    }

    @Override
    public final int execute() {
        if (isExecutable()) {

            // Get the attached configuration of this query
            Configuration c = configuration();

            // [#1191] The following triggers a start event on all listeners.
            //         This may be used to provide jOOQ with a JDBC connection,
            //         in case this Query / Configuration was previously
            //         deserialised
            ExecuteContext ctx = new DefaultExecuteContext(c, this);
            ExecuteListener listener = new ExecuteListeners(ctx);

            int result = 0;
            try {
                if (ctx.connection() == null) {
                    throw new DetachedException("Cannot execute query. No Connection configured");
                }

                // [#385] If a statement was previously kept open
                if (keepStatement() && statement != null) {
                    ctx.sql(sql);
                    ctx.statement(statement);
                }

                // [#385] First time statement preparing
                else {
                    listener.renderStart(ctx);
                    ctx.sql(getSQL0(ctx));
                    listener.renderEnd(ctx);

                    sql = ctx.sql();

                    listener.prepareStart(ctx);
                    prepare(ctx);
                    listener.prepareEnd(ctx);

                    statement = ctx.statement();
                }

                // [#1856] Set the query timeout onto the Statement
                if (timeout != 0) {
                    ctx.statement().setQueryTimeout(timeout);
                }

                if (

                    // [#1145] Bind variables only for true prepared statements
                    // [#2414] Even if parameters are inlined here, child
                    //         QueryParts may override this behaviour!
                    executePreparedStatements(c.settings()) &&

                    // [#1520] Renderers may enforce static statements, too
                    !Boolean.TRUE.equals(ctx.data(DATA_FORCE_STATIC_STATEMENT))) {

                    listener.bindStart(ctx);
                    create(c).bind(this, ctx.statement());
                    listener.bindEnd(ctx);
                }

                result = execute(ctx, listener);
                return result;
            }
            catch (SQLException e) {
                ctx.sqlException(e);
                listener.exception(ctx);
                throw ctx.exception();
            }
            finally {

                // [#2385] Successful fetchLazy() needs to keep open resources
                if (!keepResultSet() || ctx.exception() != null) {
                    Utils.safeClose(listener, ctx, keepStatement());
                }

                if (!keepStatement()) {
                    statement = null;
                    sql = null;
                }
            }
        }
        else {
            if (log.isDebugEnabled()) {
                log.debug("Query is not executable", this);
            }

            return 0;
        }
    }

    /**
     * Default implementation to indicate whether this query should close the
     * {@link ResultSet} after execution. Subclasses may override this method.
     */
    protected boolean keepResultSet() {
        return false;
    }

    /**
     * Default implementation for preparing a statement. Subclasses may override
     * this method.
     */
    protected void prepare(ExecuteContext ctx) throws SQLException {
        ctx.statement(ctx.connection().prepareStatement(ctx.sql()));
    }

    /**
     * Default implementation for query execution using a prepared statement.
     * Subclasses may override this method.
     */
    protected int execute(ExecuteContext ctx, ExecuteListener listener) throws SQLException {
        int result = 0;
        listener.executeStart(ctx);

        if (!ctx.statement().execute()) {
            result = ctx.statement().getUpdateCount();
        }

        listener.executeEnd(ctx);
        return result;
    }

    /**
     * Default implementation for executable check. Subclasses may override this
     * method.
     */
    @Override
    public boolean isExecutable() {
        return true;
    }

    private final String getSQL0(ExecuteContext ctx) {
        if (executePreparedStatements(configuration().settings())) {
            try {
                RenderContext render = new DefaultRenderContext(configuration);
                render.data(DATA_COUNT_BIND_VALUES, true);
                return render.render(this);
            }
            catch (DefaultRenderContext.ForceInlineSignal e) {
                ctx.data(DATA_FORCE_STATIC_STATEMENT, true);
                return getSQL(INLINED);
            }
        }
        else {
            return getSQL(INLINED);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getSQL() {
        return getSQL(getParamType(configuration().settings()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getSQL(ParamType paramType) {
        switch (paramType) {
            case INDEXED:
                return create().render(this);
            case INLINED:
                return create().renderInlined(this);
            case NAMED:
                return create().renderNamedParams(this);
        }

        throw new IllegalArgumentException("ParamType not supported: " + paramType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public final String getSQL(boolean inline) {
        return getSQL(inline ? INLINED : INDEXED);
    }
}
