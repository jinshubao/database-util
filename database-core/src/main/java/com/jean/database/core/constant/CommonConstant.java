package com.jean.database.core.constant;

/**
 * @author jinshubao
 * @date 2017/4/8
 */
public class CommonConstant {

    /**
     * TABLE_CAT String => table catalog (may be null)
     * TABLE_SCHEM String => table schema (may be null)
     * TABLE_NAME String => table name
     * COLUMN_NAME String => column name
     * DATA_TYPE int => SQL type from java.sql.Types
     * TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
     * COLUMN_SIZE int => column size.
     * BUFFER_LENGTH is not used.
     * DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
     * NUM_PREC_RADIX int => Radix (typically either 10 or 2)
     * NULLABLE int => is NULL allowed.
     *      columnNoNulls - might not allow NULL values
     *      columnNullable - definitely allows NULL values
     *      columnNullableUnknown - nullability unknown
     * REMARKS String => comment describing column (may be null)
     * COLUMN_DEF String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)
     * SQL_DATA_TYPE int => unused
     * SQL_DATETIME_SUB int => unused
     * CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
     * ORDINAL_POSITION int => index of column in table (starting at 1)
     * IS_NULLABLE String => ISO rules are used to determine the nullability for a column.
     *      YES --- if the column can include NULLs
     *      NO --- if the column cannot include NULLs
     *      empty string --- if the nullability for the column is unknown
     * SCOPE_CATALOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
     * SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
     * SCOPE_TABLE String => table name that this the scope of a reference attribute (null if the DATA_TYPE isn't REF)
     * SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
     * IS_AUTOINCREMENT String => Indicates whether this column is auto incremented
     *      YES --- if the column is auto incremented
     *      NO --- if the column is not auto incremented
     *      empty string --- if it cannot be determined whether the column is auto incremented
     * IS_GENERATEDCOLUMN String => Indicates whether this is a generated column
     *      YES --- if this a generated column
     *      NO --- if this not a generated column
     *      empty string --- if it cannot be determined whether this is a generated column
     */
    public static class MetaDataColumnName {
        public static final String TABLE_CAT = "TABLE_CAT";
        public static final String TABLE_SCHEM = "TABLE_SCHEM";

        public static final String TABLE_NAME = "TABLE_NAME";
        public static final String TABLE_TYPE = "TABLE_TYPE";
        public static final String TYPE_CAT = "TYPE_CAT";
        public static final String TYPE_SCHEM = "TYPE_SCHEM";
        public static final String SELF_REFERENCING_COL_NAME = "SELF_REFERENCING_COL_NAME";
        public static final String REF_GENERATION = "REF_GENERATION";
        public static final String TYPE_NAME = "TYPE_NAME";
        public static final String REMARKS = "REMARKS";

        public static final String COLUMN_NAME = "COLUMN_NAME";
        public static final String DATA_TYPE = "DATA_TYPE";
        public static final String COLUMN_SIZE = "COLUMN_SIZE";
        public static final String BUFFER_LENGTH = "BUFFER_LENGTH"; //unused
        public static final String DECIMAL_DIGITS = "DECIMAL_DIGITS";
        public static final String NUM_PREC_RADIX = "NUM_PREC_RADIX";
        public static final String NULLABLE = "NULLABLE";
        public static final String COLUMN_DEF = "COLUMN_DEF";
        public static final String SQL_DATA_TYPE = "SQL_DATA_TYPE"; //unused
        public static final String SQL_DATETIME_SUB = "SQL_DATETIME_SUB"; //unused
        public static final String CHAR_OCTET_LENGTH = "CHAR_OCTET_LENGTH";
        public static final String ORDINAL_POSITION = "ORDINAL_POSITION";
        public static final String IS_NULLABLE = "IS_NULLABLE";
        public static final String SCOPE_CATALOG = "SCOPE_CATALOG";
        public static final String SCOPE_SCHEMA = "SCOPE_SCHEMA";
        public static final String SCOPE_TABLE = "SCOPE_TABLE";
        public static final String SOURCE_DATA_TYPE = "SOURCE_DATA_TYPE";
        public static final String IS_AUTOINCREMENT = "IS_AUTOINCREMENT";
        public static final String IS_GENERATEDCOLUMN = "IS_GENERATEDCOLUMN";

    }
}
