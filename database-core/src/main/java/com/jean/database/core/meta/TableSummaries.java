package com.jean.database.core.meta;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 表信息
 *
 * @author jinshubao
 */
public class TableSummaries {

    private StringProperty tableName;

    private StringProperty autoIncrement;

    private StringProperty modifyTime;

    private StringProperty dataLength;

    private StringProperty tableType;

    private StringProperty tableRows;

    private StringProperty comments;

    public TableSummaries() {
    }

    public TableSummaries(String tableName, String autoIncrement, String modifyTime, String dataLength, String tableType, String tableRows, String comments) {
        this.tableName = new SimpleStringProperty(this, "tableName", tableName);
        this.autoIncrement = new SimpleStringProperty(this, "autoIncrement", autoIncrement);
        this.modifyTime = new SimpleStringProperty(this, "modifyTime", modifyTime);
        this.dataLength = new SimpleStringProperty(this, "dataLength", dataLength);
        this.tableType = new SimpleStringProperty(this, "tableType", tableType);
        this.tableRows = new SimpleStringProperty(this, "tableRows", tableRows);
        this.comments = new SimpleStringProperty(this, "comments", comments);
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

    public String getAutoIncrement() {
        return autoIncrement.get();
    }

    public StringProperty autoIncrementProperty() {
        return autoIncrement;
    }

    public void setAutoIncrement(String autoIncrement) {
        this.autoIncrement.set(autoIncrement);
    }

    public String getModifyTime() {
        return modifyTime.get();
    }

    public StringProperty modifyTimeProperty() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime.set(modifyTime);
    }

    public String getDataLength() {
        return dataLength.get();
    }

    public StringProperty dataLengthProperty() {
        return dataLength;
    }

    public void setDataLength(String dataLength) {
        this.dataLength.set(dataLength);
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

    public String getTableRows() {
        return tableRows.get();
    }

    public StringProperty tableRowsProperty() {
        return tableRows;
    }

    public void setTableRows(String tableRows) {
        this.tableRows.set(tableRows);
    }

    public String getComments() {
        return comments.get();
    }

    public StringProperty commentsProperty() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments.set(comments);
    }
}
