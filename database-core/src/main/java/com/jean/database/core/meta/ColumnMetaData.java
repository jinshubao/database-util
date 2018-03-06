package com.jean.database.core.meta;

/**
 * @author jinshubao
 */
public class ColumnMetaData extends TableMetaData {

    public ColumnMetaData() {
    }

    private String columnName;

    /**
     * 对应的java.sql.Types的SQL类型(列类型ID)
     */
    private Integer dataType;

    /**
     * 列大小
     */
    private Integer columnSize;

    private Integer bufferLength;

    /**
     * 小数位数
     */
    private Integer decimalDigits;

    private Integer numPrecRadix;

    /**
     * 是否允许为null
     * 0 (columnNoNulls) - 该列不允许为空
     * 1 (columnNullable) - 该列允许为空
     * 2 (columnNullableUnknown) - 不确定该列是否为空
     */
    private Integer nullAble;

    /**
     * 对于 char 类型，该长度是列中的最大字节数
     */
    private Integer charOctetLength;

    private Integer ordinalPosition;

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

    private Integer sourceDataType;

    private String isAutoincrement;

    private String isGeneratedcolumn;

    private Integer sqlDataType;

    private Integer sqlDatetimeSub;

    /**
     * 默认值
     */
    private String columnDef;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public Integer getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(Integer columnSize) {
        this.columnSize = columnSize;
    }

    public Integer getBufferLength() {
        return bufferLength;
    }

    public void setBufferLength(Integer bufferLength) {
        this.bufferLength = bufferLength;
    }

    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public Integer getNumPrecRadix() {
        return numPrecRadix;
    }

    public void setNumPrecRadix(Integer numPrecRadix) {
        this.numPrecRadix = numPrecRadix;
    }

    public Integer getNullAble() {
        return nullAble;
    }

    public void setNullAble(Integer nullAble) {
        this.nullAble = nullAble;
    }

    public Integer getCharOctetLength() {
        return charOctetLength;
    }

    public void setCharOctetLength(Integer charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    public Integer getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(Integer ordinalPosition) {
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

    public Integer getSourceDataType() {
        return sourceDataType;
    }

    public void setSourceDataType(Integer sourceDataType) {
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

    public String getColumnDef() {
        return columnDef;
    }

    public void setColumnDef(String columnDef) {
        this.columnDef = columnDef;
    }

    public Integer getSqlDataType() {
        return sqlDataType;
    }

    public void setSqlDataType(Integer sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    public Integer getSqlDatetimeSub() {
        return sqlDatetimeSub;
    }

    public void setSqlDatetimeSub(Integer sqlDatetimeSub) {
        this.sqlDatetimeSub = sqlDatetimeSub;
    }

    @Override
    public String toString() {
        return columnName;
    }

}