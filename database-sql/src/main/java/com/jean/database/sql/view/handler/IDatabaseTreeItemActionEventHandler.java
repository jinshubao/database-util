package com.jean.database.sql.view.handler;

import com.jean.database.api.view.handler.ICommonActionEventHandler;
import com.jean.database.api.view.handler.IMouseEventHandler;
import com.jean.database.api.view.handler.IRefreshActionEventHandler;
import com.jean.database.sql.view.treeitem.DatabaseTreeItem;

/**
 * @author jinshubao
 */
public interface IDatabaseTreeItemActionEventHandler extends
        ICommonActionEventHandler<DatabaseTreeItem>,
        IRefreshActionEventHandler<DatabaseTreeItem>,
        IMouseEventHandler<DatabaseTreeItem> {

    /**
     * 打开命令行
     *
     * @param treeItem server节点
     */
    void onOpenCommandLine(DatabaseTreeItem treeItem);

    /**
     * 运行SQL文件
     *
     * @param treeItem server节点
     */
    void onExecuteSqlFile(DatabaseTreeItem treeItem);

    void onTransformData(DatabaseTreeItem treeItem);

    void onCopy(DatabaseTreeItem treeItem);
}
