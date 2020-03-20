package com.jean.database.gui.view.treeitem;

import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.TableTypeMetaData;
import com.jean.database.gui.factory.LoggerWrapper;
import com.jean.database.gui.view.handler.IMouseEventHandler;
import com.jean.database.gui.view.handler.ITableTypeItemActionEventHandler;
import com.jean.database.gui.view.handler.impl.TableTypeItemActionEventHandlerImpl;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;

public class TableTypeTreeItem extends BaseTreeItem<TableTypeMetaData> {

    private final ITableTypeItemActionEventHandler tableTypeItemActionEventHandler;


    public TableTypeTreeItem(TableTypeMetaData value, Node root, IConnectionConfiguration connectionConfiguration, IMetadataProvider metadataProvider) {
        super(value, root, connectionConfiguration, metadataProvider);
        this.tableTypeItemActionEventHandler = LoggerWrapper.warp(new TableTypeItemActionEventHandlerImpl(root));
    }

    @Override
    public IMouseEventHandler getMouseEventHandler() {
        return this.tableTypeItemActionEventHandler;
    }

    @Override
    public ContextMenu getContextMenu() {
        return null;
    }

    @Override
    public void close() {
        tableTypeItemActionEventHandler.onClose(this);
    }
}
