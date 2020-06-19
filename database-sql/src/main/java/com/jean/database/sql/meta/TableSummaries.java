package com.jean.database.sql.meta;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 表信息
 *
 * @author jinshubao
 */
@SuppressWarnings("FieldMayBeFinal")
public class TableSummaries {

    private StringProperty tableName = new SimpleStringProperty(this, "tableName");
    private StringProperty autoIncrement = new SimpleStringProperty(this, "autoIncrement");
    private StringProperty modifyTime = new SimpleStringProperty(this, "modifyTime");
    private StringProperty dataLength = new SimpleStringProperty(this, "dataLength");
    private StringProperty tableType = new SimpleStringProperty(this, "tableType");
    private StringProperty tableRows = new SimpleStringProperty(this, "tableRows");
    private StringProperty comments = new SimpleStringProperty(this, "comments");

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
