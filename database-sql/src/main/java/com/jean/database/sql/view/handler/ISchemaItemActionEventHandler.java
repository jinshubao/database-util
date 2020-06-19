package com.jean.database.sql.view.handler;

import com.jean.database.api.view.handler.ICommonActionEventHandler;
import com.jean.database.api.view.handler.IMouseEventHandler;
import com.jean.database.api.view.handler.IRefreshActionEventHandler;
import com.jean.database.sql.view.treeitem.SchemaTreeItem;

public interface ISchemaItemActionEventHandler extends
        ICommonActionEventHandler<SchemaTreeItem>,
        IRefreshActionEventHandler<SchemaTreeItem>,
        IMouseEventHandler<SchemaTreeItem> {

}
