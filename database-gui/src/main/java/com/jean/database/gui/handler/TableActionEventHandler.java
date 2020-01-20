package com.jean.database.gui.handler;

import com.jean.database.gui.view.treeitem.TableTreeItem;

public interface TableActionEventHandler extends IRefresh<TableTreeItem> {

    void openTable(TableTreeItem tableTreeItem);

    void copyTable(TableTreeItem tableTreeItem);

    void deleteTable(TableTreeItem tableTreeItem);
}
