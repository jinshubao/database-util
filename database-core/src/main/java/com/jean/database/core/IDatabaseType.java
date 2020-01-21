package com.jean.database.core;


public interface IDatabaseType {

    String getIdentifier();

    String getName();

    default String getIcon() {
        return null;
    }

    default String getCatalogIcon() {
        return null;
    }

    default String getSchema() {
        return null;
    }

    default String getTableIcon() {
        return null;
    }
}
