package com.jean.database.core.meta;

public class TableTypeMetaData extends SchemaMetaData {

    private String tableType;

    public TableTypeMetaData(SchemaMetaData schemaMetaData, String tableType) {
        this((CatalogMetaData) schemaMetaData, tableType);
        this.setTableSchem(schemaMetaData.getTableSchem());
    }

    public TableTypeMetaData(CatalogMetaData catalogMetaData, String tableType) {
        this.setTableCat(catalogMetaData.getTableCat());
        this.setQuoteString(catalogMetaData.getQuoteString());
        this.setSeparator(catalogMetaData.getSeparator());
        this.tableType = tableType;

    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }


    @Override
    public String toString() {
        return this.getTableType();
    }
}
