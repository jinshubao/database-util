package com.jean.database.sql.view.handler;

import com.jean.database.api.view.handler.ICommonActionEventHandler;
import com.jean.database.api.view.handler.IMouseEventHandler;
import com.jean.database.api.view.handler.IRefreshActionEventHandler;
import com.jean.database.sql.view.treeitem.TableTreeItem;

public interface ITableItemActionEventHandler extends
        ICommonActionEventHandler<TableTreeItem>,
        IRefreshActionEventHandler<TableTreeItem>,
        IMouseEventHandler<TableTreeItem> {

    void onCopy(TableTreeItem catalogTreeItem);

}
