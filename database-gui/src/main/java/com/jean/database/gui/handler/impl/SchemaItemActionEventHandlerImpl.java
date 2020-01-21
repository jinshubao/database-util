package com.jean.database.gui.handler.impl;

import com.jean.database.core.IMetadataProvider;
import com.jean.database.gui.handler.ISchemaItemActionEventHandler;
import com.jean.database.gui.view.treeitem.SchemaTreeItem;

import java.sql.Connection;

public class SchemaItemActionEventHandlerImpl implements ISchemaItemActionEventHandler {

    private final IMetadataProvider metadataProvider;

    private final Connection connection;

    public SchemaItemActionEventHandlerImpl(IMetadataProvider metadataProvider, Connection connection) {
        this.metadataProvider = metadataProvider;
        this.connection = connection;
    }

    @Override
    public void openSchema(SchemaTreeItem schemaTreeItem) {

    }

    @Override
    public void deleteSchema(SchemaTreeItem schemaTreeItem) {

    }

    @Override
    public void refresh(SchemaTreeItem treeItem) {

    }
}
