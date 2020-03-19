package com.jean.database.gui.view.handler.impl;

import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.gui.view.handler.AbstractEventHandler;
import com.jean.database.gui.view.handler.ISchemaItemActionEventHandler;
import com.jean.database.gui.view.treeitem.SchemaTreeItem;
import javafx.scene.Node;

public class SchemaItemActionEventHandlerImpl extends AbstractEventHandler<SchemaTreeItem> implements ISchemaItemActionEventHandler {

    private final IConnectionConfiguration connectionConfiguration;
    private final IMetadataProvider metadataProvider;

    public SchemaItemActionEventHandlerImpl(IConnectionConfiguration connectionConfiguration, IMetadataProvider metadataProvider, Node root) {
        super(root);
        this.connectionConfiguration = connectionConfiguration;
        this.metadataProvider = metadataProvider;
    }

}
