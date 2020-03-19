package com.jean.database.gui.view.handler.impl;

import com.jean.database.common.utils.DialogUtil;
import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.gui.factory.ActionLoggerWrapper;
import com.jean.database.gui.view.handler.AbstractEventHandler;
import com.jean.database.gui.view.handler.ICatalogItemActionEventHandler;
import com.jean.database.gui.view.handler.IServerItemActionEventHandler;
import com.jean.database.gui.view.treeitem.CatalogTreeItem;
import com.jean.database.gui.view.treeitem.ServerTreeItem;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ServerItemActionEventHandlerImpl extends AbstractEventHandler<ServerTreeItem> implements IServerItemActionEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerItemActionEventHandlerImpl.class);

    private final IConnectionConfiguration connectionConfiguration;
    private final IMetadataProvider metadataProvider;
    private final ICatalogItemActionEventHandler catalogItemActionEventHandler;

    private final TextArea ddlTextArea;

    public ServerItemActionEventHandlerImpl(IConnectionConfiguration connectionConfiguration, IMetadataProvider metadataProvider, Node root) {
        super(root);
        this.connectionConfiguration = connectionConfiguration;
        this.metadataProvider = metadataProvider;
        this.catalogItemActionEventHandler = ActionLoggerWrapper.warp(new CatalogItemActionEventHandlerImpl(connectionConfiguration, metadataProvider, root));
        this.ddlTextArea = this.lookup("#ddlTextArea");
    }

    @Override
    public void onOpen(ServerTreeItem serverTreeItem) {
        if (!serverTreeItem.getOpen()) {
            try (Connection connection = this.metadataProvider.getConnection(this.connectionConfiguration)) {
                ObservableList children = serverTreeItem.getChildren();
                List<CatalogMetaData> catalogs = this.metadataProvider.getCatalogs(connection);
                for (CatalogMetaData metaData : catalogs) {
                    CatalogTreeItem item = new CatalogTreeItem(metaData, this.catalogItemActionEventHandler);
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
    public void onDelete(ServerTreeItem serverTreeItem) {
        serverTreeItem.getChildren().clear();
        serverTreeItem.getParent().getChildren().remove(serverTreeItem);
        serverTreeItem.setOpen(false);
    }

    @Override
    public void refresh(ServerTreeItem treeItem) {
        treeItem.getChildren().clear();
        treeItem.setOpen(false);
        this.onOpen(treeItem);
    }

    @Override
    public void onMouseDoubleClick(ServerTreeItem serverTreeItem) {
        this.refresh(serverTreeItem);
    }

    @Override
    public void onSelected(ServerTreeItem serverTreeItem) {
        this.ddlTextArea.setText(serverTreeItem.getValue());
    }

    @Override
    public void onOpenCommandLine(ServerTreeItem serverTreeItem) {

    }

    @Override
    public void onExecuteSqlFile(ServerTreeItem serverTreeItem) {

    }

    @Override
    public void onTransformData(ServerTreeItem catalogTreeItem) {

    }
}
