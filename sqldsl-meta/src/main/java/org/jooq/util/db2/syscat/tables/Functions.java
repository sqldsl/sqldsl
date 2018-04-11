/**
 * This class is generated by jOOQ
 */
package org.jooq.util.db2.syscat.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "3.0.0"},
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings("all")
public class Functions extends org.jooq.impl.TableImpl<org.jooq.Record> {

	private static final long serialVersionUID = 2135651450;

	/**
	 * The singleton instance of <code>SYSCAT.FUNCTIONS</code>
	 */
	public static final org.jooq.util.db2.syscat.tables.Functions FUNCTIONS = new org.jooq.util.db2.syscat.tables.Functions();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * The column <code>SYSCAT.FUNCTIONS.FUNCSCHEMA</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> FUNCSCHEMA = createField("FUNCSCHEMA", org.jooq.impl.SQLDataType.VARCHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.FUNCNAME</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> FUNCNAME = createField("FUNCNAME", org.jooq.impl.SQLDataType.VARCHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.SPECIFICNAME</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> SPECIFICNAME = createField("SPECIFICNAME", org.jooq.impl.SQLDataType.VARCHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.DEFINER</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> DEFINER = createField("DEFINER", org.jooq.impl.SQLDataType.VARCHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.FUNCID</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.Integer> FUNCID = createField("FUNCID", org.jooq.impl.SQLDataType.INTEGER, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.RETURN_TYPE</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.Short> RETURN_TYPE = createField("RETURN_TYPE", org.jooq.impl.SQLDataType.SMALLINT, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.ORIGIN</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> ORIGIN = createField("ORIGIN", org.jooq.impl.SQLDataType.CHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.TYPE</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> TYPE = createField("TYPE", org.jooq.impl.SQLDataType.CHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.METHOD</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> METHOD = createField("METHOD", org.jooq.impl.SQLDataType.CHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.EFFECT</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> EFFECT = createField("EFFECT", org.jooq.impl.SQLDataType.CHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.PARM_COUNT</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.Short> PARM_COUNT = createField("PARM_COUNT", org.jooq.impl.SQLDataType.SMALLINT, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.PARM_SIGNATURE</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> PARM_SIGNATURE = createField("PARM_SIGNATURE", org.jooq.impl.SQLDataType.VARCHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.CREATE_TIME</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.sql.Timestamp> CREATE_TIME = createField("CREATE_TIME", org.jooq.impl.SQLDataType.TIMESTAMP, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.QUALIFIER</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> QUALIFIER = createField("QUALIFIER", org.jooq.impl.SQLDataType.VARCHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.WITH_FUNC_ACCESS</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> WITH_FUNC_ACCESS = createField("WITH_FUNC_ACCESS", org.jooq.impl.SQLDataType.CHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.TYPE_PRESERVING</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> TYPE_PRESERVING = createField("TYPE_PRESERVING", org.jooq.impl.SQLDataType.CHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.VARIANT</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> VARIANT = createField("VARIANT", org.jooq.impl.SQLDataType.CHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.SIDE_EFFECTS</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> SIDE_EFFECTS = createField("SIDE_EFFECTS", org.jooq.impl.SQLDataType.CHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.FENCED</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> FENCED = createField("FENCED", org.jooq.impl.SQLDataType.CHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.NULLCALL</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> NULLCALL = createField("NULLCALL", org.jooq.impl.SQLDataType.CHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.CAST_FUNCTION</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> CAST_FUNCTION = createField("CAST_FUNCTION", org.jooq.impl.SQLDataType.CHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.ASSIGN_FUNCTION</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> ASSIGN_FUNCTION = createField("ASSIGN_FUNCTION", org.jooq.impl.SQLDataType.CHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.SCRATCHPAD</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> SCRATCHPAD = createField("SCRATCHPAD", org.jooq.impl.SQLDataType.CHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.FINAL_CALL</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> FINAL_CALL = createField("FINAL_CALL", org.jooq.impl.SQLDataType.CHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.PARALLELIZABLE</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> PARALLELIZABLE = createField("PARALLELIZABLE", org.jooq.impl.SQLDataType.CHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.CONTAINS_SQL</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> CONTAINS_SQL = createField("CONTAINS_SQL", org.jooq.impl.SQLDataType.CHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.DBINFO</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> DBINFO = createField("DBINFO", org.jooq.impl.SQLDataType.CHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.RESULT_COLS</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.Short> RESULT_COLS = createField("RESULT_COLS", org.jooq.impl.SQLDataType.SMALLINT, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.LANGUAGE</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> LANGUAGE = createField("LANGUAGE", org.jooq.impl.SQLDataType.CHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.IMPLEMENTATION</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> IMPLEMENTATION = createField("IMPLEMENTATION", org.jooq.impl.SQLDataType.VARCHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.CLASS</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> CLASS = createField("CLASS", org.jooq.impl.SQLDataType.VARCHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.JAR_ID</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> JAR_ID = createField("JAR_ID", org.jooq.impl.SQLDataType.VARCHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.PARM_STYLE</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> PARM_STYLE = createField("PARM_STYLE", org.jooq.impl.SQLDataType.CHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.SOURCE_SCHEMA</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> SOURCE_SCHEMA = createField("SOURCE_SCHEMA", org.jooq.impl.SQLDataType.VARCHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.SOURCE_SPECIFIC</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> SOURCE_SPECIFIC = createField("SOURCE_SPECIFIC", org.jooq.impl.SQLDataType.VARCHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.IOS_PER_INVOC</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.Double> IOS_PER_INVOC = createField("IOS_PER_INVOC", org.jooq.impl.SQLDataType.DOUBLE, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.INSTS_PER_INVOC</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.Double> INSTS_PER_INVOC = createField("INSTS_PER_INVOC", org.jooq.impl.SQLDataType.DOUBLE, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.IOS_PER_ARGBYTE</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.Double> IOS_PER_ARGBYTE = createField("IOS_PER_ARGBYTE", org.jooq.impl.SQLDataType.DOUBLE, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.INSTS_PER_ARGBYTE</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.Double> INSTS_PER_ARGBYTE = createField("INSTS_PER_ARGBYTE", org.jooq.impl.SQLDataType.DOUBLE, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.PERCENT_ARGBYTES</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.Short> PERCENT_ARGBYTES = createField("PERCENT_ARGBYTES", org.jooq.impl.SQLDataType.SMALLINT, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.INITIAL_IOS</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.Double> INITIAL_IOS = createField("INITIAL_IOS", org.jooq.impl.SQLDataType.DOUBLE, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.INITIAL_INSTS</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.Double> INITIAL_INSTS = createField("INITIAL_INSTS", org.jooq.impl.SQLDataType.DOUBLE, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.CARDINALITY</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.Long> CARDINALITY = createField("CARDINALITY", org.jooq.impl.SQLDataType.BIGINT, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.IMPLEMENTED</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> IMPLEMENTED = createField("IMPLEMENTED", org.jooq.impl.SQLDataType.CHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.SELECTIVITY</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.Double> SELECTIVITY = createField("SELECTIVITY", org.jooq.impl.SQLDataType.DOUBLE, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.OVERRIDDEN_FUNCID</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.Integer> OVERRIDDEN_FUNCID = createField("OVERRIDDEN_FUNCID", org.jooq.impl.SQLDataType.INTEGER, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.SUBJECT_TYPESCHEMA</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> SUBJECT_TYPESCHEMA = createField("SUBJECT_TYPESCHEMA", org.jooq.impl.SQLDataType.VARCHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.SUBJECT_TYPENAME</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> SUBJECT_TYPENAME = createField("SUBJECT_TYPENAME", org.jooq.impl.SQLDataType.VARCHAR, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.FUNC_PATH</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> FUNC_PATH = createField("FUNC_PATH", org.jooq.impl.SQLDataType.CLOB, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.BODY</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> BODY = createField("BODY", org.jooq.impl.SQLDataType.CLOB, FUNCTIONS);

	/**
	 * The column <code>SYSCAT.FUNCTIONS.REMARKS</code>. 
	 */
	public static final org.jooq.TableField<org.jooq.Record, java.lang.String> REMARKS = createField("REMARKS", org.jooq.impl.SQLDataType.VARCHAR, FUNCTIONS);

	/**
	 * No further instances allowed
	 */
	private Functions() {
		super("FUNCTIONS", org.jooq.util.db2.syscat.Syscat.SYSCAT);
	}
}