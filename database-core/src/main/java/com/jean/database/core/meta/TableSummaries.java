package com.jean.database.core.meta;

public class TableSummaries {

    private Object tableName;

    private Object autoIncrement;

    private Object modifyTime;

    private Object dataLength;

    private Object tableType;

    private Object tableRows;

    private Object comments;

    public TableSummaries(Object tableName, Object autoIncrement, Object modifyTime, Object dataLength, Object tableType, Object tableRows, Object comments) {
        this.tableName = tableName;
        this.autoIncrement = autoIncrement;
        this.modifyTime = modifyTime;
        this.dataLength = dataLength;
        this.tableType = tableType;
        this.tableRows = tableRows;
        this.comments = comments;
    }

    public Object getTableName() {
        return tableName;
    }

    public void setTableName(Object tableName) {
        this.tableName = tableName;
    }

    public Object getAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(Object autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public Object getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Object modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Object getDataLength() {
        return dataLength;
    }

    public void setDataLength(Object dataLength) {
        this.dataLength = dataLength;
    }

    public Object getTableType() {
        return tableType;
    }

    public void setTableType(Object tableType) {
        this.tableType = tableType;
    }

    public Object getTableRows() {
        return tableRows;
    }

    public void setTableRows(Object tableRows) {
        this.tableRows = tableRows;
    }

    public Object getComments() {
        return comments;
    }

    public void setComments(Object comments) {
        this.comments = comments;
    }
}
