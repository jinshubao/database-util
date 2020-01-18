package com.jean.database.client.view.treeitem;

import com.jean.database.client.view.*;
import com.jean.database.core.IMetadataProvider;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;

/**
 * @author jinshubao
 */
public abstract class AbstractTreeItem extends TreeItem<Object> implements IContextMenu, ISelecte, IRefresh, IDoubleClick, IIcon {

    private final IMetadataProvider metadataProvider;

    private final Connection connection;

    public AbstractTreeItem(Connection connection, IMetadataProvider metadataProvider, Object value) {
        super(value);
        this.connection = connection;
        this.metadataProvider = metadataProvider;
        this.setGraphic(getIcon());
    }

    @Override
    public ContextMenu getContextMenu() {
        return null;
    }

    @Override
    public void onSelected(ISelecte item) {
        
    }

    @Override
    public void refresh() {

    }

    @Override
    public void doubleClick(MouseEvent event) {

    }

    @Override
    public Node getIcon() {
        return null;
    }

    public IMetadataProvider getMetadataProvider() {
        return metadataProvider;
    }

    public Connection getConnection() {
        return connection;
    }
}
