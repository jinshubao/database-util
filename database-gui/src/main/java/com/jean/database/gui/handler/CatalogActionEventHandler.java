package com.jean.database.gui.handler;

import com.jean.database.gui.view.treeitem.CatalogTreeItem;

public interface CatalogActionEventHandler extends IRefresh<CatalogTreeItem> {

    void openCatalog(CatalogTreeItem catalogTreeItem);

    void closeCatalog(CatalogTreeItem catalogTreeItem);

    void createCatalog(CatalogTreeItem catalogTreeItem);

    void deleteCatalog(CatalogTreeItem catalogTreeItem);

    void catalogProperties(CatalogTreeItem catalogTreeItem);
}
