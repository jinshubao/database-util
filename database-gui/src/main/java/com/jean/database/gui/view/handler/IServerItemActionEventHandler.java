package com.jean.database.gui.view.handler;

import com.jean.database.gui.view.treeitem.CatalogTreeItem;
import com.jean.database.gui.view.treeitem.ServerTreeItem;

/**
 * @author jinshubao
 */
public interface IServerItemActionEventHandler extends
        ICommonActionEventHandler<ServerTreeItem>,
        IRefreshActionEventHandler<ServerTreeItem>,
        IMouseEventHandler<ServerTreeItem> {

    /**
     * 打开命令行
     *
     * @param serverTreeItem server节点
     */
    void onOpenCommandLine(ServerTreeItem serverTreeItem);

    /**
     * 运行SQL文件
     * @param serverTreeItem server节点
     */
    void onExecuteSqlFile(ServerTreeItem serverTreeItem);

    void onTransformData(ServerTreeItem catalogTreeItem);


}
