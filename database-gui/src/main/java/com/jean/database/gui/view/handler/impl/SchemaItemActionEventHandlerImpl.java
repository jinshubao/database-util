package com.jean.database.gui.view.handler.impl;

import com.jean.database.gui.view.action.ICloseable;
import com.jean.database.gui.view.handler.ISchemaItemActionEventHandler;
import com.jean.database.gui.view.treeitem.SchemaTreeItem;

public class SchemaItemActionEventHandlerImpl implements ISchemaItemActionEventHandler {

    public SchemaItemActionEventHandlerImpl() {
    }

    @Override
    public void onClose(SchemaTreeItem schemaTreeItem) {
        schemaTreeItem.getChildren().forEach(item -> {
            if (item instanceof ICloseable) {
                ((ICloseable) item).close();
            }
        });
        schemaTreeItem.setExpanded(false);
        schemaTreeItem.setOpen(false);
        schemaTreeItem.getChildren().clear();
    }
}
