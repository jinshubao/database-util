package com.jean.database.sql.meta;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 表信息
 *
 * @author jinshubao
 */
@SuppressWarnings("FieldMayBeFinal")
public class TableMetaData {

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

    public java.lang.String getTableCat() {
        return tableCat.get();
    }

    public StringProperty tableCatProperty() {
        return tableCat;
    }

    public void setTableCat(java.lang.String tableCat) {
        this.tableCat.set(tableCat);
    }

    public java.lang.String getQuoteString() {
        return quoteString.get();
    }

    public StringProperty quoteStringProperty() {
        return quoteString;
    }

    public void setQuoteString(java.lang.String quoteString) {
        this.quoteString.set(quoteString);
    }

    public java.lang.String getSeparator() {
        return separator.get();
    }

    public StringProperty separatorProperty() {
        return separator;
    }

    public void setSeparator(java.lang.String separator) {
        this.separator.set(separator);
    }

    public java.lang.String getTableSchema() {
        return tableSchema.get();
    }

    public StringProperty tableSchemaProperty() {
        return tableSchema;
    }

    public void setTableSchema(java.lang.String tableSchema) {
        this.tableSchema.set(tableSchema);
    }

    public java.lang.String getTableName() {
        return tableName.get();
    }

    public StringProperty tableNameProperty() {
        return tableName;
    }

    public void setTableName(java.lang.String tableName) {
        this.tableName.set(tableName);
    }

    public java.lang.String getTableType() {
        return tableType.get();
    }

    public StringProperty tableTypeProperty() {
        return tableType;
    }

    public void setTableType(java.lang.String tableType) {
        this.tableType.set(tableType);
    }

    public java.lang.String getRemarks() {
        return remarks.get();
    }

    public StringProperty remarksProperty() {
        return remarks;
    }

    public void setRemarks(java.lang.String remarks) {
        this.remarks.set(remarks);
    }

    public java.lang.String getTypeCat() {
        return typeCat.get();
    }

    public StringProperty typeCatProperty() {
        return typeCat;
    }

    public void setTypeCat(java.lang.String typeCat) {
        this.typeCat.set(typeCat);
    }

    public java.lang.String getTypeSchema() {
        return typeSchema.get();
    }

    public StringProperty typeSchemaProperty() {
        return typeSchema;
    }

    public void setTypeSchema(java.lang.String typeSchema) {
        this.typeSchema.set(typeSchema);
    }

    public java.lang.String getTypeName() {
        return typeName.get();
    }

    public StringProperty typeNameProperty() {
        return typeName;
    }

    public void setTypeName(java.lang.String typeName) {
        this.typeName.set(typeName);
    }

    public java.lang.String getSelfReferencingColName() {
        return selfReferencingColName.get();
    }

    public StringProperty selfReferencingColNameProperty() {
        return selfReferencingColName;
    }

    public void setSelfReferencingColName(java.lang.String selfReferencingColName) {
        this.selfReferencingColName.set(selfReferencingColName);
    }

    public java.lang.String getRefGeneration() {
        return refGeneration.get();
    }

    public StringProperty refGenerationProperty() {
        return refGeneration;
    }

    public void setRefGeneration(java.lang.String refGeneration) {
        this.refGeneration.set(refGeneration);
    }

    public String getFullName() {
        StringBuilder builder = new StringBuilder();
        String quoteString = getQuoteString();
        String separator = getSeparator();
        builder.append(quoteString).append(getTableCat()).append(quoteString);
        String schema = getTableSchema();
        if (schema != null && !schema.isEmpty()) {
            builder.append(separator)
                    .append(quoteString).append(schema).append(quoteString);
        }
        builder.append(separator)
                .append(quoteString).append(getTableName()).append(quoteString);
        return builder.toString();
    }

    @Override
    public String toString() {
        return tableName.get();
    }
}
