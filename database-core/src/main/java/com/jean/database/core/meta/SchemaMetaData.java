package com.jean.database.core.meta;

/**
 * Schema信息
 *
 * @author jinshubao
 */
public class SchemaMetaData extends CatalogMetaData {

    public SchemaMetaData() {
    }

    private String tableSchem;

    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(String tableSchem) {
        this.tableSchem = tableSchem;
    }

    @Override
    public String toString() {
        return tableSchem;
    }
}
