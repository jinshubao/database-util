package com.jean.database.sql.meta;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * 表类型信息
 *
 * @author jinshubao
 */
@SuppressWarnings("FieldMayBeFinal")
public class TableTypeMetaData {

    private StringProperty tableCat = new SimpleStringProperty(this, "tableCat");
    private StringProperty quoteString = new SimpleStringProperty(this, "quoteString");
    private StringProperty separator = new SimpleStringProperty(this, "separator");
    private StringProperty tableSchema = new SimpleStringProperty(this, "tableSchema");
    private StringProperty tableType = new SimpleStringProperty(this, "tableType");

    private ObservableList<TableMetaData> tableMetaDataList = FXCollections.observableArrayList();

    public TableTypeMetaData() {
    }

    public TableTypeMetaData(String tableCat, String quoteString, String separator, String tableSchema, String tableType) {
        this.tableCat.set(tableCat);
        this.quoteString.set(quoteString);
        this.separator.set(separator);
        this.tableSchema.set(tableSchema);
        this.tableType.set(tableType);
    }

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

    public String getTableType() {
        return tableType.get();
    }

    public StringProperty tableTypeProperty() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType.set(tableType);
    }

    public void setTableMetaDataList(List<TableMetaData> tables) {
        this.tableMetaDataList.clear();
        this.tableMetaDataList.addAll(tables);
    }

    public ObservableList<TableMetaData> getTableMetaDataList() {
        return tableMetaDataList;
    }

    @Override
    public String toString() {
        return this.getTableType();
    }
}
