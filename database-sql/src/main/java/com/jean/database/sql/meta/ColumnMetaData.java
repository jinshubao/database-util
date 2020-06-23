package com.jean.database.sql.meta;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

/**
 * @author jinshubao
 * <p>
 * TABLE_CAT String => table catalog (may be null)
 * TABLE_SCHEM String => table schema (may be null)
 * TABLE_NAME String => table name
 * COLUMN_NAME String => column name
 * DATA_TYPE int => SQL type from java.sql.Types 对应的java.sql.Types的SQL类型(列类型ID)
 * TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
 * COLUMN_SIZE int => column size.
 * BUFFER_LENGTH is not used.
 * DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
 * NUM_PREC_RADIX int => Radix (typically either 10 or 2)
 * NULLABLE int => is NULL allowed.
 * * 0 (columnNoNulls) - 该列不允许为空
 * * 1 (columnNullable) - 该列允许为空
 * * 2 (columnNullableUnknown) - 不确定该列是否为空
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
 * * YES --- if the column can include NULLs
 * * NO --- if the column cannot include NULLs
 * empty string --- if the nullability for the column is unknown
 * SCOPE_CATALOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
 * SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
 * SCOPE_TABLE String => table name that this the scope of a reference attribute (null if the DATA_TYPE isn't REF)
 * SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
 * IS_AUTOINCREMENT String => Indicates whether this column is auto incremented
 * * YES --- if the column is auto incremented
 * * NO --- if the column is not auto incremented
 * empty string --- if it cannot be determined whether the column is auto incremented
 * IS_GENERATEDCOLUMN String => Indicates whether this is a generated column
 * * YES --- if this a generated column
 * * NO --- if this not a generated column
 * empty string --- if it cannot be determined whether this is a generated column
 * @author jinshubao
 */
@SuppressWarnings("FieldMayBeFinal")
public class ColumnMetaData {

    private StringProperty tableCat = new SimpleStringProperty(this, "tableCat");
    private StringProperty quoteString = new SimpleStringProperty(this, "quoteString");
    private StringProperty separator = new SimpleStringProperty(this, "separator");
    private StringProperty tableSchema = new SimpleStringProperty(this, "tableSchema");
    private StringProperty tableName = new SimpleStringProperty(this, "tableName");
    private StringProperty tableType = new SimpleStringProperty(this, "tableType");
    private StringProperty remarks = new SimpleStringProperty(this, "remarks");
    private StringProperty typeCat = new SimpleStringProperty(this, "typeCat");
    private StringProperty typeSchema = new SimpleStringProperty(this, "typeSchema");
    private StringProperty typeName = new SimpleStringProperty(this, "typeName");
    private StringProperty selfReferencingColName = new SimpleStringProperty(this, "selfReferencingColName");
    private StringProperty refGeneration = new SimpleStringProperty(this, "refGeneration");
    private StringProperty columnName = new SimpleStringProperty(this, "columnName");
    private StringProperty columnDef = new SimpleStringProperty(this, "columnDef");
    private StringProperty isNullable = new SimpleStringProperty(this, "isNullable");
    private StringProperty scopeCatalog = new SimpleStringProperty(this, "scopeCatalog");
    private StringProperty scopeSchema = new SimpleStringProperty(this, "scopeSchema");
    private StringProperty scopeTable = new SimpleStringProperty(this, "scopeTable");
    private StringProperty isAutoincrement = new SimpleStringProperty(this, "isAutoincrement");
    private StringProperty isGeneratedcolumn = new SimpleStringProperty(this, "isGeneratedcolumn");
    private IntegerProperty dataType = new SimpleIntegerProperty(this, "dataType");
    private IntegerProperty columnSize = new SimpleIntegerProperty(this, "columnSize");
    private IntegerProperty bufferLength = new SimpleIntegerProperty(this, "bufferLength");
    private IntegerProperty decimalDigits = new SimpleIntegerProperty(this, "decimalDigits");
    private IntegerProperty numPrecRadix = new SimpleIntegerProperty(this, "numPrecRadix");
    private IntegerProperty nullable = new SimpleIntegerProperty(this, "nullable");
    private IntegerProperty sqlDataType = new SimpleIntegerProperty(this, "sqlDataType");
    private IntegerProperty sqlDatetimeSub = new SimpleIntegerProperty(this, "sqlDatetimeSub");
    private IntegerProperty charOctetLength = new SimpleIntegerProperty(this, "charOctetLength");
    private IntegerProperty ordinalPosition = new SimpleIntegerProperty(this, "ordinalPosition");
    private IntegerProperty sourceDataType = new SimpleIntegerProperty(this, "sourceDataType");

    public String getTableCat() {
        return tableCat.get();
    }

    public StringProperty tableCatProperty() {
        return tableCat;
    }

    public void setTableCat(String tableCat) {
        this.tableCat.set(tableCat);
    }

    public String getQuoteString() {
        return quoteString.get();
    }

    public StringProperty quoteStringProperty() {
        return quoteString;
    }

    public void setQuoteString(String quoteString) {
        this.quoteString.set(quoteString);
    }

    public String getSeparator() {
        return separator.get();
    }

    public StringProperty separatorProperty() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator.set(separator);
    }

    public String getTableSchema() {
        return tableSchema.get();
    }

    public StringProperty tableSchemaProperty() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema.set(tableSchema);
    }

    public String getTableName() {
        return tableName.get();
    }

    public StringProperty tableNameProperty() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName.set(tableName);
    }

    public String getTableType() {
        return tableType.get();
    }

    public StringProperty tableTypeProperty() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType.set(tableType);
    }

    public String getRemarks() {
        return remarks.get();
    }

    public StringProperty remarksProperty() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks.set(remarks);
    }

    public String getTypeCat() {
        return typeCat.get();
    }

    public StringProperty typeCatProperty() {
        return typeCat;
    }

    public void setTypeCat(String typeCat) {
        this.typeCat.set(typeCat);
    }

    public String getTypeSchema() {
        return typeSchema.get();
    }

    public StringProperty typeSchemaProperty() {
        return typeSchema;
    }

    public void setTypeSchema(String typeSchema) {
        this.typeSchema.set(typeSchema);
    }

    public String getTypeName() {
        return typeName.get();
    }

    public StringProperty typeNameProperty() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName.set(typeName);
    }

    public String getSelfReferencingColName() {
        return selfReferencingColName.get();
    }

    public StringProperty selfReferencingColNameProperty() {
        return selfReferencingColName;
    }

    public void setSelfReferencingColName(String selfReferencingColName) {
        this.selfReferencingColName.set(selfReferencingColName);
    }

    public String getRefGeneration() {
        return refGeneration.get();
    }

    public StringProperty refGenerationProperty() {
        return refGeneration;
    }

    public void setRefGeneration(String refGeneration) {
        this.refGeneration.set(refGeneration);
    }

    public String getColumnName() {
        return columnName.get();
    }

    public StringProperty columnNameProperty() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName.set(columnName);
    }

    public String getColumnDef() {
        return columnDef.get();
    }

    public StringProperty columnDefProperty() {
        return columnDef;
    }

    public void setColumnDef(String columnDef) {
        this.columnDef.set(columnDef);
    }

    public String getIsNullable() {
        return isNullable.get();
    }

    public StringProperty isNullableProperty() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable.set(isNullable);
    }

    public String getScopeCatalog() {
        return scopeCatalog.get();
    }

    public StringProperty scopeCatalogProperty() {
        return scopeCatalog;
    }

    public void setScopeCatalog(String scopeCatalog) {
        this.scopeCatalog.set(scopeCatalog);
    }

    public String getScopeSchema() {
        return scopeSchema.get();
    }

    public StringProperty scopeSchemaProperty() {
        return scopeSchema;
    }

    public void setScopeSchema(String scopeSchema) {
        this.scopeSchema.set(scopeSchema);
    }

    public String getScopeTable() {
        return scopeTable.get();
    }

    public StringProperty scopeTableProperty() {
        return scopeTable;
    }

    public void setScopeTable(String scopeTable) {
        this.scopeTable.set(scopeTable);
    }

    public String getIsAutoincrement() {
        return isAutoincrement.get();
    }

    public StringProperty isAutoincrementProperty() {
        return isAutoincrement;
    }

    public void setIsAutoincrement(String isAutoincrement) {
        this.isAutoincrement.set(isAutoincrement);
    }

    public String getIsGeneratedcolumn() {
        return isGeneratedcolumn.get();
    }

    public StringProperty isGeneratedcolumnProperty() {
        return isGeneratedcolumn;
    }

    public void setIsGeneratedcolumn(String isGeneratedcolumn) {
        this.isGeneratedcolumn.set(isGeneratedcolumn);
    }

    public int getDataType() {
        return dataType.get();
    }

    public IntegerProperty dataTypeProperty() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType.set(dataType);
    }

    public int getColumnSize() {
        return columnSize.get();
    }

    public IntegerProperty columnSizeProperty() {
        return columnSize;
    }

    public void setColumnSize(int columnSize) {
        this.columnSize.set(columnSize);
    }

    public int getBufferLength() {
        return bufferLength.get();
    }

    public IntegerProperty bufferLengthProperty() {
        return bufferLength;
    }

    public void setBufferLength(int bufferLength) {
        this.bufferLength.set(bufferLength);
    }

    public int getDecimalDigits() {
        return decimalDigits.get();
    }

    public IntegerProperty decimalDigitsProperty() {
        return decimalDigits;
    }

    public void setDecimalDigits(int decimalDigits) {
        this.decimalDigits.set(decimalDigits);
    }

    public int getNumPrecRadix() {
        return numPrecRadix.get();
    }

    public IntegerProperty numPrecRadixProperty() {
        return numPrecRadix;
    }

    public void setNumPrecRadix(int numPrecRadix) {
        this.numPrecRadix.set(numPrecRadix);
    }

    public int getNullable() {
        return nullable.get();
    }

    public IntegerProperty nullableProperty() {
        return nullable;
    }

    public void setNullable(int nullable) {
        this.nullable.set(nullable);
    }

    public int getSqlDataType() {
        return sqlDataType.get();
    }

    public IntegerProperty sqlDataTypeProperty() {
        return sqlDataType;
    }

    public void setSqlDataType(int sqlDataType) {
        this.sqlDataType.set(sqlDataType);
    }

    public int getSqlDatetimeSub() {
        return sqlDatetimeSub.get();
    }

    public IntegerProperty sqlDatetimeSubProperty() {
        return sqlDatetimeSub;
    }

    public void setSqlDatetimeSub(int sqlDatetimeSub) {
        this.sqlDatetimeSub.set(sqlDatetimeSub);
    }

    public int getCharOctetLength() {
        return charOctetLength.get();
    }

    public IntegerProperty charOctetLengthProperty() {
        return charOctetLength;
    }

    public void setCharOctetLength(int charOctetLength) {
        this.charOctetLength.set(charOctetLength);
    }

    public int getOrdinalPosition() {
        return ordinalPosition.get();
    }

    public IntegerProperty ordinalPositionProperty() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(int ordinalPosition) {
        this.ordinalPosition.set(ordinalPosition);
    }

    public int getSourceDataType() {
        return sourceDataType.get();
    }

    public IntegerProperty sourceDataTypeProperty() {
        return sourceDataType;
    }

    public void setSourceDataType(int sourceDataType) {
        this.sourceDataType.set(sourceDataType);
    }

    @Override
    public String toString() {
        return columnName.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColumnMetaData that = (ColumnMetaData) o;
        return Objects.equals(tableCat.get(), that.tableCat.get()) &&
                Objects.equals(quoteString.get(), that.quoteString.get()) &&
                Objects.equals(separator.get(), that.separator.get()) &&
                Objects.equals(tableSchema.get(), that.tableSchema.get()) &&
                Objects.equals(tableName.get(), that.tableName.get()) &&
                Objects.equals(tableType.get(), that.tableType.get()) &&
                Objects.equals(remarks.get(), that.remarks.get()) &&
                Objects.equals(typeCat.get(), that.typeCat.get()) &&
                Objects.equals(typeSchema.get(), that.typeSchema.get()) &&
                Objects.equals(typeName.get(), that.typeName.get()) &&
                Objects.equals(selfReferencingColName.get(), that.selfReferencingColName.get()) &&
                Objects.equals(refGeneration.get(), that.refGeneration.get()) &&
                Objects.equals(columnName.get(), that.columnName.get()) &&
                Objects.equals(columnDef.get(), that.columnDef.get()) &&
                Objects.equals(isNullable.get(), that.isNullable.get()) &&
                Objects.equals(scopeCatalog.get(), that.scopeCatalog.get()) &&
                Objects.equals(scopeSchema.get(), that.scopeSchema.get()) &&
                Objects.equals(scopeTable.get(), that.scopeTable.get()) &&
                Objects.equals(isAutoincrement.get(), that.isAutoincrement.get()) &&
                Objects.equals(isGeneratedcolumn.get(), that.isGeneratedcolumn.get()) &&
                Objects.equals(dataType.get(), that.dataType.get()) &&
                Objects.equals(columnSize.get(), that.columnSize.get()) &&
                Objects.equals(bufferLength.get(), that.bufferLength.get()) &&
                Objects.equals(decimalDigits.get(), that.decimalDigits.get()) &&
                Objects.equals(numPrecRadix.get(), that.numPrecRadix.get()) &&
                Objects.equals(nullable.get(), that.nullable.get()) &&
                Objects.equals(sqlDataType.get(), that.sqlDataType.get()) &&
                Objects.equals(sqlDatetimeSub.get(), that.sqlDatetimeSub.get()) &&
                Objects.equals(charOctetLength.get(), that.charOctetLength.get()) &&
                Objects.equals(ordinalPosition.get(), that.ordinalPosition.get()) &&
                Objects.equals(sourceDataType.get(), that.sourceDataType.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableCat, quoteString, separator, tableSchema, tableName, tableType, remarks, typeCat, typeSchema, typeName, selfReferencingColName, refGeneration, columnName, columnDef, isNullable, scopeCatalog, scopeSchema, scopeTable, isAutoincrement, isGeneratedcolumn, dataType, columnSize, bufferLength, decimalDigits, numPrecRadix, nullable, sqlDataType, sqlDatetimeSub, charOctetLength, ordinalPosition, sourceDataType);
    }
}
