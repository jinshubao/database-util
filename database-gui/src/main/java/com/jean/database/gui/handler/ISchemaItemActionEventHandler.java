package com.jean.database.gui.handler;

import com.jean.database.gui.view.treeitem.SchemaTreeItem;

public interface ISchemaItemActionEventHandler extends
        ICommonActionEventHandler<SchemaTreeItem>,
        IRefreshActionEventHandler<SchemaTreeItem>,
        IMouseClickEventHandler<SchemaTreeItem> {

}
