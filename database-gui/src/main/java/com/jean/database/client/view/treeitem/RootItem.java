package com.jean.database.client.view.treeitem;

import com.jean.database.client.view.IContextMenu;
import com.jean.database.core.IConnectionProvider;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;

/**
 * @author jinshubao
 */
public class RootItem extends TreeItem<String> implements IContextMenu {

    private final IConnectionProvider connectionProvider;
    private final ContextMenu contextMenu;

    public RootItem(IConnectionProvider connectionProvider, ContextMenu contextMenu) {
        this.connectionProvider = connectionProvider;
        this.contextMenu = contextMenu;
    }

    public IConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }

    @Override
    public ContextMenu getContextMenu() {
        return contextMenu;
    }
}
