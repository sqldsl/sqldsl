/**
 * This class is generated by jOOQ
 */
package org.jooq.util.firebird.rdb.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "3.0.0"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class Rdb$relationConstraints extends org.jooq.impl.TableImpl<org.jooq.Record> {

	private static final long serialVersionUID = -1796820184;

	/**
	 * The singleton instance of <code>RDB$RELATION_CONSTRAINTS</code>
	 */
	public static final org.jooq.util.firebird.rdb.tables.Rdb$relationConstraints RDB$RELATION_CONSTRAINTS = new org.jooq.util.firebird.rdb.tables.Rdb$relationConstraints();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * The column <code>RDB$RELATION_CONSTRAINTS.RDB$CONSTRAINT_NAME</code>. 
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> RDB$CONSTRAINT_NAME = createField("RDB$CONSTRAINT_NAME", org.jooq.impl.SQLDataType.CHAR.length(31), this);

	/**
	 * The column <code>RDB$RELATION_CONSTRAINTS.RDB$CONSTRAINT_TYPE</code>. 
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> RDB$CONSTRAINT_TYPE = createField("RDB$CONSTRAINT_TYPE", org.jooq.impl.SQLDataType.CHAR.length(11), this);

	/**
	 * The column <code>RDB$RELATION_CONSTRAINTS.RDB$RELATION_NAME</code>. 
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> RDB$RELATION_NAME = createField("RDB$RELATION_NAME", org.jooq.impl.SQLDataType.CHAR.length(31), this);

	/**
	 * The column <code>RDB$RELATION_CONSTRAINTS.RDB$DEFERRABLE</code>. 
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> RDB$DEFERRABLE = createField("RDB$DEFERRABLE", org.jooq.impl.SQLDataType.CHAR.length(3), this);

	/**
	 * The column <code>RDB$RELATION_CONSTRAINTS.RDB$INITIALLY_DEFERRED</code>. 
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> RDB$INITIALLY_DEFERRED = createField("RDB$INITIALLY_DEFERRED", org.jooq.impl.SQLDataType.CHAR.length(3), this);

	/**
	 * The column <code>RDB$RELATION_CONSTRAINTS.RDB$INDEX_NAME</code>. 
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> RDB$INDEX_NAME = createField("RDB$INDEX_NAME", org.jooq.impl.SQLDataType.CHAR.length(31), this);

	/**
	 * Create a <code>RDB$RELATION_CONSTRAINTS</code> table reference
	 */
	public Rdb$relationConstraints() {
		super("RDB$RELATION_CONSTRAINTS", org.jooq.util.firebird.rdb.DefaultSchema.DEFAULT_SCHEMA);
	}

	/**
	 * Create an aliased <code>RDB$RELATION_CONSTRAINTS</code> table reference
	 */
	public Rdb$relationConstraints(java.lang.String alias) {
		super(alias, org.jooq.util.firebird.rdb.DefaultSchema.DEFAULT_SCHEMA, org.jooq.util.firebird.rdb.tables.Rdb$relationConstraints.RDB$RELATION_CONSTRAINTS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.util.firebird.rdb.tables.Rdb$relationConstraints as(java.lang.String alias) {
		return new org.jooq.util.firebird.rdb.tables.Rdb$relationConstraints(alias);
	}
}