package com.jean.database.gui.view.handler;

import com.jean.database.gui.view.treeitem.TableTreeItem;

public interface ITableItemActionEventHandler extends
        ICommonActionEventHandler<TableTreeItem>,
        IRefreshActionEventHandler<TableTreeItem>,
        IMouseEventHandler<TableTreeItem> {

}
