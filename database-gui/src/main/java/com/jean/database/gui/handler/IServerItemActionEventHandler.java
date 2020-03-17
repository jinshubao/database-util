package com.jean.database.gui.handler;

import com.jean.database.gui.view.treeitem.ServerTreeItem;

public interface IServerItemActionEventHandler extends
        ICommonActionEventHandler<ServerTreeItem>,
        IRefreshActionEventHandler<ServerTreeItem>,
        IMouseClickEventHandler<ServerTreeItem>,
        ISelectedActionEventHandler<ServerTreeItem> {

    void onCopy(ServerTreeItem serverTreeItem);

}
