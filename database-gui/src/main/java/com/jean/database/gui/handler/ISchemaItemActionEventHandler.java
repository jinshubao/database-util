package com.jean.database.gui.handler;

import com.jean.database.gui.view.treeitem.SchemaTreeItem;

public interface ISchemaItemActionEventHandler extends IRefreshActionEventHandler<SchemaTreeItem> {

    void openSchema(SchemaTreeItem schemaTreeItem);

    void deleteSchema(SchemaTreeItem schemaTreeItem);
}
