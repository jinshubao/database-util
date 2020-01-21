package com.jean.database.gui.handler.impl;

import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.core.utils.DialogUtil;
import com.jean.database.gui.handler.ICatalogItemActionEventHandler;
import com.jean.database.gui.handler.IServerItemActionEventHandler;
import com.jean.database.gui.view.treeitem.CatalogTreeItem;
import com.jean.database.gui.view.treeitem.ServerTreeItem;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ServerItemActionEventHandlerImpl implements IServerItemActionEventHandler {

    private final IMetadataProvider metadataProvider;

    private final Connection connection;

    private final ICatalogItemActionEventHandler catalogActionEventHandler;

    public ServerItemActionEventHandlerImpl(IMetadataProvider metadataProvider, Connection connection, ICatalogItemActionEventHandler catalogActionEventHandler) {
        this.metadataProvider = metadataProvider;
        this.connection = connection;
        this.catalogActionEventHandler = catalogActionEventHandler;
    }

    @Override
    public void openServer(ServerTreeItem serverTreeItem) {
        if (!serverTreeItem.getOpen()) {
            try {
                ObservableList children = serverTreeItem.getChildren();
                List<CatalogMetaData> catalogs = metadataProvider.getCatalogs(connection);
                for (CatalogMetaData metaData : catalogs) {
                    CatalogTreeItem item = new CatalogTreeItem(metaData, catalogActionEventHandler);
                    //noinspection unchecked
                    children.add(item);
                }
                serverTreeItem.setExpanded(true);
                serverTreeItem.setOpen(true);
            } catch (SQLException e) {
                DialogUtil.error("ERROR", e.getMessage(), e);
            }
        }
    }

    @Override
    public void closeServer(ServerTreeItem serverTreeItem) {
        serverTreeItem.getChildren().clear();
        serverTreeItem.setOpen(false);
    }

    @Override
    public void copyServer(ServerTreeItem serverTreeItem) {

    }

    @Override
    public void deleteServer(ServerTreeItem serverTreeItem) {
        serverTreeItem.getChildren().clear();
        serverTreeItem.getParent().getChildren().remove(serverTreeItem);
        serverTreeItem.setOpen(false);
    }

    @Override
    public void serverProperties(ServerTreeItem serverTreeItem) {
        //TODO
    }

    @Override
    public void refresh(ServerTreeItem treeItem) {
        treeItem.getChildren().clear();
        treeItem.setOpen(false);
        this.openServer(treeItem);
    }
}
