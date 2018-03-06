package com.jean.database.core.meta;

/**
 * Catalog信息
 *
 * @author jinshubao
 */
public class CatalogMetaData {

    public CatalogMetaData() {
    }

    private String tableCat;

    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(String tableCat) {
        this.tableCat = tableCat;
    }

    @Override
    public String toString() {
        return tableCat;
    }
}
