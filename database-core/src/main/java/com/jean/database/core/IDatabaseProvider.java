package com.jean.database.core;

public interface IDatabaseProvider {

    String getIdentifier();

    String getName();

    default String getIcon() {
        return null;
    }

    default String getCatalogIcon() {
        return null;
    }

    default String getSchemaIcon() {
        return null;
    }

    default String getTableIcon() {
        return null;
    }

    IConnectionConfiguration getConfiguration();

    IMetadataProvider getMetadataProvider();

    boolean supportCatalog();

    boolean supportSchema();


}
