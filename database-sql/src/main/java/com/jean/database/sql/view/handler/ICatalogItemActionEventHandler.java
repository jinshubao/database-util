package com.jean.database.sql.view.handler;

import com.jean.database.api.view.handler.ICommonActionEventHandler;
import com.jean.database.api.view.handler.IMouseEventHandler;
import com.jean.database.api.view.handler.IRefreshActionEventHandler;
import com.jean.database.sql.view.treeitem.CatalogTreeItem;

public interface ICatalogItemActionEventHandler extends
        ICommonActionEventHandler<CatalogTreeItem>,
        IRefreshActionEventHandler<CatalogTreeItem>,
        IMouseEventHandler<CatalogTreeItem> {

    /**
     * 打开命令行
     *
     * @param serverTreeItem server节点
     */
    void onOpenCommandLine(CatalogTreeItem serverTreeItem);

    /**
     * 运行SQL文件
     *
     * @param serverTreeItem server节点
     */
    void onExecuteSqlFile(CatalogTreeItem serverTreeItem);

    /**
     * 转储SQL文件(结构和数据)
     *
     * @param serverTreeItem server节点
     */
    void onExportStructAndData(CatalogTreeItem serverTreeItem);

    /**
     * 转储SQL文件(结构)
     *
     * @param serverTreeItem server节点
     */
    void onExportStruct(CatalogTreeItem serverTreeItem);

    void onPrintDatabase(CatalogTreeItem catalogTreeItem);

    void onTransformData(CatalogTreeItem catalogTreeItem);

    void onConvertToMode(CatalogTreeItem catalogTreeItem);

    void onFind(CatalogTreeItem catalogTreeItem);
}
