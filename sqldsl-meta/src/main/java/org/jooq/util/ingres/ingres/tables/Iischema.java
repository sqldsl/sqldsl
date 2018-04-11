/**
 * This class is generated by jOOQ
 */
package org.jooq.util.ingres.ingres.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "3.0.0"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Iischema extends org.jooq.impl.TableImpl<org.jooq.Record> {

	private static final long serialVersionUID = -2048975408;

	/**
	 * The singleton instance of <code>$ingres.iischema</code>
	 */
	public static final org.jooq.util.ingres.ingres.tables.Iischema IISCHEMA = new org.jooq.util.ingres.ingres.tables.Iischema();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * The column <code>$ingres.iischema.schema_name</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> SCHEMA_NAME = createField("schema_name", org.jooq.impl.SQLDataType.CHAR.length(32), IISCHEMA);

	/**
	 * The column <code>$ingres.iischema.schema_owner</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> SCHEMA_OWNER = createField("schema_owner", org.jooq.impl.SQLDataType.CHAR.length(32), IISCHEMA);

	/**
	 * The column <code>$ingres.iischema.schema_id</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.Integer> SCHEMA_ID = createField("schema_id", org.jooq.impl.SQLDataType.INTEGER, IISCHEMA);

	/**
	 * The column <code>$ingres.iischema.schema_idx</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.Integer> SCHEMA_IDX = createField("schema_idx", org.jooq.impl.SQLDataType.INTEGER, IISCHEMA);

	/**
	 * No further instances allowed
	 */
	private Iischema() {
		super("iischema", org.jooq.util.ingres.ingres.$ingres.$INGRES);
	}
}