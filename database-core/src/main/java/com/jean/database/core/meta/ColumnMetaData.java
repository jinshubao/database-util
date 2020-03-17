package com.jean.database.core.meta;

/**
 * @author jinshubao
 * <p>
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
 * columnNoNulls - might not allow NULL values
 * columnNullable - definitely allows NULL values
 * columnNullableUnknown - nullability unknown
 * REMARKS String => comment describing column (may be null)
 * COLUMN_DEF String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)
 * SQL_DATA_TYPE int => unused
 * SQL_DATETIME_SUB int => unused
 * CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
 * ORDINAL_POSITION int => index of column in table (starting at 1)
 * IS_NULLABLE String => ISO rules are used to determine the nullability for a column.
 *      YES --- if the column can include NULLs
 *      NO --- if the column cannot include NULLs
 * empty string --- if the nullability for the column is unknown
 * SCOPE_CATALOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
 * SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
 * SCOPE_TABLE String => table name that this the scope of a reference attribute (null if the DATA_TYPE isn't REF)
 * SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
 * IS_AUTOINCREMENT String => Indicates whether this column is auto incremented
 *      YES --- if the column is auto incremented
 *      NO --- if the column is not auto incremented
 * empty string --- if it cannot be determined whether the column is auto incremented
 * IS_GENERATEDCOLUMN String => Indicates whether this is a generated column
 *      YES --- if this a generated column
 *      NO --- if this not a generated column
 * empty string --- if it cannot be determined whether this is a generated column
 */
public class ColumnMetaData extends TableMetaData {

    private String columnName;
    /**
     * 对应的java.sql.Types的SQL类型(列类型ID)
     */
    private int dataType;
    private int columnSize;
    private int bufferLength;
    private int decimalDigits;
    private int numPrecRadix;
    /**
     * 是否允许为null
     * 0 (columnNoNulls) - 该列不允许为空
     * 1 (columnNullable) - 该列允许为空
     * 2 (columnNullableUnknown) - 不确定该列是否为空
     */
    private int nullable;
    private String columnDef;
    private int sqlDataType;
    private int sqlDatetimeSub;
    private int charOctetLength;
    private int ordinalPosition;
    /**
     * ISO规则用来确定某一列的是否可为空(等同于NULLABLE的值:[ 0:'YES'; 1:'NO'; 2:''; ])
     * YES -- 该列可以有空值;
     * NO -- 该列不能为空;
     * 空字符串--- 不知道该列是否可为空
     */
    private String isNullable;
    private String scopeCatalog;
    private String scopeSchema;
    private String scopeTable;
    private short sourceDataType;
    private String isAutoincrement;
    private String isGeneratedcolumn;

    public ColumnMetaData() {
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }

    public int getBufferLength() {
        return bufferLength;
    }

    public void setBufferLength(int bufferLength) {
        this.bufferLength = bufferLength;
    }

    public int getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public int getNumPrecRadix() {
        return numPrecRadix;
    }

    public void setNumPrecRadix(int numPrecRadix) {
        this.numPrecRadix = numPrecRadix;
    }

    public int getNullable() {
        return nullable;
    }

    public void setNullable(int nullable) {
        this.nullable = nullable;
    }

    public String getColumnDef() {
        return columnDef;
    }

    public void setColumnDef(String columnDef) {
        this.columnDef = columnDef;
    }

    public int getSqlDataType() {
        return sqlDataType;
    }

    public void setSqlDataType(int sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    public int getSqlDatetimeSub() {
        return sqlDatetimeSub;
    }

    public void setSqlDatetimeSub(int sqlDatetimeSub) {
        this.sqlDatetimeSub = sqlDatetimeSub;
    }

    public int getCharOctetLength() {
        return charOctetLength;
    }

    public void setCharOctetLength(int charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getScopeCatalog() {
        return scopeCatalog;
    }

    public void setScopeCatalog(String scopeCatalog) {
        this.scopeCatalog = scopeCatalog;
    }

    public String getScopeSchema() {
        return scopeSchema;
    }

    public void setScopeSchema(String scopeSchema) {
        this.scopeSchema = scopeSchema;
    }

    public String getScopeTable() {
        return scopeTable;
    }

    public void setScopeTable(String scopeTable) {
        this.scopeTable = scopeTable;
    }

    public short getSourceDataType() {
        return sourceDataType;
    }

    public void setSourceDataType(short sourceDataType) {
        this.sourceDataType = sourceDataType;
    }

    public String getIsAutoincrement() {
        return isAutoincrement;
    }

    public void setIsAutoincrement(String isAutoincrement) {
        this.isAutoincrement = isAutoincrement;
    }

    public String getIsGeneratedcolumn() {
        return isGeneratedcolumn;
    }

    public void setIsGeneratedcolumn(String isGeneratedcolumn) {
        this.isGeneratedcolumn = isGeneratedcolumn;
    }

    @Override
    public String toString() {
        return columnName;
    }

}
