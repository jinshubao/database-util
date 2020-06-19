package com.jean.database.sql.view.handler.impl;

import com.jean.database.api.view.action.ICloseable;
import com.jean.database.sql.view.handler.ISchemaItemActionEventHandler;
import com.jean.database.sql.view.treeitem.SchemaTreeItem;

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

    @Override
    public void onClick(SchemaTreeItem schemaTreeItem) {

    }

    @Override
    public void onDoubleClick(SchemaTreeItem schemaTreeItem) {

    }

    @Override
    public void onSelected(SchemaTreeItem schemaTreeItem) {

    }
}
