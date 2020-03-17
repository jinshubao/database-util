package com.jean.database.core.meta;

/**
 * Catalog信息
 *
 * @author jinshubao
 */
public class CatalogMetaData {

    private String tableCat;
    private String quoteString;
    private String separator;

    public CatalogMetaData() {
    }


    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(String tableCat) {
        this.tableCat = tableCat;
    }

    public String getQuoteString() {
        return quoteString;
    }

    public void setQuoteString(String quoteString) {
        this.quoteString = quoteString;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    @Override
    public String toString() {
        return tableCat;
    }
}
