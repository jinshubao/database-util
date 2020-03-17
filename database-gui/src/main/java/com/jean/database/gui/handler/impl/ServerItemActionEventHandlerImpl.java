package com.jean.database.gui.handler.impl;

import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.common.utils.DialogUtil;
import com.jean.database.gui.factory.ActionLoggerWrapper;
import com.jean.database.gui.handler.ICatalogItemActionEventHandler;
import com.jean.database.gui.handler.IServerItemActionEventHandler;
import com.jean.database.gui.view.treeitem.CatalogTreeItem;
import com.jean.database.gui.view.treeitem.ServerTreeItem;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ServerItemActionEventHandlerImpl implements IServerItemActionEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerItemActionEventHandlerImpl.class);

    private final IConnectionConfiguration connectionConfiguration;
    private final IMetadataProvider metadataProvider;
    private final ICatalogItemActionEventHandler catalogItemActionEventHandler;

    public ServerItemActionEventHandlerImpl(IConnectionConfiguration connectionConfiguration, IMetadataProvider metadataProvider, Node root) {
        this.connectionConfiguration = connectionConfiguration;
        this.metadataProvider = metadataProvider;
        this.catalogItemActionEventHandler = ActionLoggerWrapper.warp(new CatalogItemActionEventHandlerImpl(connectionConfiguration, metadataProvider, root));
    }


    @Override
    public void onCreate(ServerTreeItem serverTreeItem) {
        //
    }

    @Override
    public void onOpen(ServerTreeItem serverTreeItem) {
        if (!serverTreeItem.getOpen()) {
            try (Connection connection = metadataProvider.getConnection(this.connectionConfiguration)) {
                ObservableList children = serverTreeItem.getChildren();
                List<CatalogMetaData> catalogs = metadataProvider.getCatalogs(connection);
                for (CatalogMetaData metaData : catalogs) {
                    CatalogTreeItem item = new CatalogTreeItem(metaData, catalogItemActionEventHandler);
                    //noinspection unchecked
                    children.add(item);
                }
                serverTreeItem.setExpanded(true);
                serverTreeItem.setOpen(true);
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
                DialogUtil.error(e);
            }
        }
    }

    @Override
    public void onClose(ServerTreeItem serverTreeItem) {
        serverTreeItem.getChildren().clear();
        serverTreeItem.setOpen(false);
    }

    @Override
    public void onCopy(ServerTreeItem serverTreeItem) {

    }

    @Override
    public void onDelete(ServerTreeItem serverTreeItem) {
        serverTreeItem.getChildren().clear();
        serverTreeItem.getParent().getChildren().remove(serverTreeItem);
        serverTreeItem.setOpen(false);
    }

    @Override
    public void onDetails(ServerTreeItem serverTreeItem) {
        //
    }

    @Override
    public void refresh(ServerTreeItem treeItem) {
        treeItem.getChildren().clear();
        treeItem.setOpen(false);
        this.onOpen(treeItem);
    }

    @Override
    public void onMouseClick(ServerTreeItem serverTreeItem) {
    }

    @Override
    public void onMouseDoubleClick(ServerTreeItem serverTreeItem) {
        this.refresh(serverTreeItem);
    }

    @Override
    public void onSelected(ServerTreeItem serverTreeItem) {

    }
}
