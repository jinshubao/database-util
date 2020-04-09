package com.jean.database.core.meta;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Catalog信息
 *
 * @author jinshubao
 */
public class CatalogMetaData {

    private StringProperty tableCat = new SimpleStringProperty(this, "tableCat");
    private StringProperty quoteString = new SimpleStringProperty(this, "quoteString");
    private StringProperty separator = new SimpleStringProperty(this, "separator");

    public CatalogMetaData() {
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

    @Override
    public String toString() {
        return tableCat.get();
    }
}
