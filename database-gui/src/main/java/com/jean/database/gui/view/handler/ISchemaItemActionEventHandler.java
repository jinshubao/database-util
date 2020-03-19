package com.jean.database.gui.view.handler;

import com.jean.database.gui.view.treeitem.SchemaTreeItem;

public interface ISchemaItemActionEventHandler extends
        ICommonActionEventHandler<SchemaTreeItem>,
        IRefreshActionEventHandler<SchemaTreeItem>,
        IMouseEventHandler<SchemaTreeItem> {

}
