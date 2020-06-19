package com.jean.database.sql.view.handler;

import com.jean.database.api.view.handler.ICommonActionEventHandler;
import com.jean.database.api.view.handler.IMouseEventHandler;
import com.jean.database.sql.view.treeitem.TableTypeTreeItem;

public interface ITableTypeItemActionEventHandler extends
        IMouseEventHandler<TableTypeTreeItem>,
        ICommonActionEventHandler<TableTypeTreeItem> {


}
