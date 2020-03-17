package com.jean.database.gui.handler.impl;

import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.gui.handler.ISchemaItemActionEventHandler;
import com.jean.database.gui.view.treeitem.SchemaTreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchemaItemActionEventHandlerImpl implements ISchemaItemActionEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(SchemaItemActionEventHandlerImpl.class);

    private final IConnectionConfiguration connectionConfiguration;

    private final IMetadataProvider metadataProvider;

    public SchemaItemActionEventHandlerImpl(IConnectionConfiguration connectionConfiguration, IMetadataProvider metadataProvider) {
        this.connectionConfiguration = connectionConfiguration;
        this.metadataProvider = metadataProvider;
    }

    @Override
    public void onCreate(SchemaTreeItem schemaTreeItem) {
        //
    }

    @Override
    public void onOpen(SchemaTreeItem schemaTreeItem) {
        //
    }

    @Override
    public void onClose(SchemaTreeItem schemaTreeItem) {
        //
    }

    @Override
    public void onDelete(SchemaTreeItem schemaTreeItem) {
        //
    }

    @Override
    public void refresh(SchemaTreeItem treeItem) {
        //
    }

    @Override
    public void onDetails(SchemaTreeItem schemaTreeItem) {
        //
    }

    @Override
    public void onMouseClick(SchemaTreeItem schemaTreeItem) {
        logger.debug("click {}", schemaTreeItem.getValue());
    }

    @Override
    public void onMouseDoubleClick(SchemaTreeItem schemaTreeItem) {
        this.refresh(schemaTreeItem);
    }
}
