package com.jean.database.gui.handler;

import com.jean.database.gui.view.treeitem.CatalogTreeItem;

public interface ICatalogItemActionEventHandler extends
        ICommonActionEventHandler<CatalogTreeItem>,
        IRefreshActionEventHandler<CatalogTreeItem>,
        IMouseClickEventHandler<CatalogTreeItem>,
        ISelectedActionEventHandler<CatalogTreeItem> {

}
