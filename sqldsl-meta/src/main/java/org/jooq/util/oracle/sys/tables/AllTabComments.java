/**
 * This class is generated by jOOQ
 */
package org.jooq.util.oracle.sys.tables;

/**
 * This class is generated by jOOQ.
 *
 * Comments on tables and views accessible to the user
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "3.0.0"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class AllTabComments extends org.jooq.impl.TableImpl<org.jooq.Record> {

	private static final long serialVersionUID = 310178050;

	/**
	 * The singleton instance of <code>SYS.ALL_TAB_COMMENTS</code>
	 */
	public static final org.jooq.util.oracle.sys.tables.AllTabComments ALL_TAB_COMMENTS = new org.jooq.util.oracle.sys.tables.AllTabComments();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * The column <code>SYS.ALL_TAB_COMMENTS.OWNER</code>. Owner of the object
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> OWNER = createField("OWNER", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * The column <code>SYS.ALL_TAB_COMMENTS.TABLE_NAME</code>. Name of the object
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> TABLE_NAME = createField("TABLE_NAME", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * The column <code>SYS.ALL_TAB_COMMENTS.TABLE_TYPE</code>. Type of the object
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> TABLE_TYPE = createField("TABLE_TYPE", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * The column <code>SYS.ALL_TAB_COMMENTS.COMMENTS</code>. Comment on the object
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> COMMENTS = createField("COMMENTS", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * Create a <code>SYS.ALL_TAB_COMMENTS</code> table reference
	 */
	public AllTabComments() {
		super("ALL_TAB_COMMENTS", org.jooq.util.oracle.sys.Sys.SYS);
	}

	/**
	 * Create an aliased <code>SYS.ALL_TAB_COMMENTS</code> table reference
	 */
	public AllTabComments(java.lang.String alias) {
		super(alias, org.jooq.util.oracle.sys.Sys.SYS, org.jooq.util.oracle.sys.tables.AllTabComments.ALL_TAB_COMMENTS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.util.oracle.sys.tables.AllTabComments as(java.lang.String alias) {
		return new org.jooq.util.oracle.sys.tables.AllTabComments(alias);
	}
}