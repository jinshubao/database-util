package com.jean.database.client.view.treeitem;

import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.core.meta.SchemaMetaData;

import java.sql.Connection;

public class SchemaTreeItem extends AbstractTreeItem {

    private final CatalogMetaData catalogMetaData;

    private final SchemaMetaData schemaMetaData;

    public SchemaTreeItem(Connection connection, IMetadataProvider metadataProvider, CatalogMetaData catalogMetaData, SchemaMetaData schemaMetaData) {
        super(connection, metadataProvider, schemaMetaData);
        this.catalogMetaData = catalogMetaData;
        this.schemaMetaData = schemaMetaData;
    }

    public CatalogMetaData getCatalogMetaData() {
        return catalogMetaData;
    }

    public SchemaMetaData getSchemaMetaData() {
        return schemaMetaData;
    }
}
