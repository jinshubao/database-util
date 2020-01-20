package com.jean.database.gui.handler;

import com.jean.database.gui.view.treeitem.SchemaTreeItem;

public interface SchemaActionEventHandler extends IRefresh<SchemaTreeItem> {

    void openSchema(SchemaTreeItem schemaTreeItem);

    void deleteSchema(SchemaTreeItem schemaTreeItem);
}
