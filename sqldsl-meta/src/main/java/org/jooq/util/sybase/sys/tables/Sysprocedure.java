/**
 * This class is generated by jOOQ
 */
package org.jooq.util.sybase.sys.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "3.0.0"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class Sysprocedure extends org.jooq.impl.TableImpl<org.jooq.Record> {

	private static final long serialVersionUID = -1494553146;

	/**
	 * The singleton instance of <code>SYS.SYSPROCEDURE</code>
	 */
	public static final org.jooq.util.sybase.sys.tables.Sysprocedure SYSPROCEDURE = new org.jooq.util.sybase.sys.tables.Sysprocedure();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * The column <code>SYS.SYSPROCEDURE.proc_id</code>. 
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Integer> PROC_ID = createField("proc_id", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * The column <code>SYS.SYSPROCEDURE.creator</code>. 
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Integer> CREATOR = createField("creator", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * The column <code>SYS.SYSPROCEDURE.object_id</code>. 
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Long> OBJECT_ID = createField("object_id", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * The column <code>SYS.SYSPROCEDURE.proc_name</code>. 
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> PROC_NAME = createField("proc_name", org.jooq.impl.SQLDataType.CHAR, this);

	/**
	 * The column <code>SYS.SYSPROCEDURE.proc_defn</code>. 
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> PROC_DEFN = createField("proc_defn", org.jooq.impl.SQLDataType.LONGVARCHAR, this);

	/**
	 * The column <code>SYS.SYSPROCEDURE.remarks</code>. 
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> REMARKS = createField("remarks", org.jooq.impl.SQLDataType.LONGVARCHAR, this);

	/**
	 * The column <code>SYS.SYSPROCEDURE.replicate</code>. 
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> REPLICATE = createField("replicate", org.jooq.impl.SQLDataType.CHAR, this);

	/**
	 * The column <code>SYS.SYSPROCEDURE.srvid</code>. 
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Integer> SRVID = createField("srvid", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * The column <code>SYS.SYSPROCEDURE.source</code>. 
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> SOURCE = createField("source", org.jooq.impl.SQLDataType.LONGVARCHAR, this);

	/**
	 * The column <code>SYS.SYSPROCEDURE.avg_num_rows</code>. 
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Double> AVG_NUM_ROWS = createField("avg_num_rows", org.jooq.impl.SQLDataType.FLOAT, this);

	/**
	 * The column <code>SYS.SYSPROCEDURE.avg_cost</code>. 
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.Double> AVG_COST = createField("avg_cost", org.jooq.impl.SQLDataType.FLOAT, this);

	/**
	 * The column <code>SYS.SYSPROCEDURE.stats</code>. 
	 */
	public final org.jooq.TableField<org.jooq.Record, byte[]> STATS = createField("stats", org.jooq.impl.SQLDataType.LONGVARBINARY, this);

	/**
	 * Create a <code>SYS.SYSPROCEDURE</code> table reference
	 */
	public Sysprocedure() {
		super("SYSPROCEDURE", org.jooq.util.sybase.sys.Sys.SYS);
	}

	/**
	 * Create an aliased <code>SYS.SYSPROCEDURE</code> table reference
	 */
	public Sysprocedure(java.lang.String alias) {
		super(alias, org.jooq.util.sybase.sys.Sys.SYS, org.jooq.util.sybase.sys.tables.Sysprocedure.SYSPROCEDURE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.util.sybase.sys.tables.Sysprocedure as(java.lang.String alias) {
		return new org.jooq.util.sybase.sys.tables.Sysprocedure(alias);
	}
}
