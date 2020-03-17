package com.jean.database.gui.handler;

import com.jean.database.gui.view.treeitem.TableTreeItem;

public interface ITableItemActionEventHandler extends
        ICommonActionEventHandler<TableTreeItem>,
        IRefreshActionEventHandler<TableTreeItem>,
        IMouseClickEventHandler<TableTreeItem>,
        ISelectedActionEventHandler<TableTreeItem> {

    void onCopy(TableTreeItem tableTreeItem);

}
