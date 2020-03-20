package com.jean.database.gui.view.handler.impl;

import com.jean.database.common.utils.NodeUtils;
import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.gui.manager.TaskManger;
import com.jean.database.gui.task.BaseTask;
import com.jean.database.gui.view.action.ICloseable;
import com.jean.database.gui.view.handler.IServerItemActionEventHandler;
import com.jean.database.gui.view.treeitem.CatalogTreeItem;
import com.jean.database.gui.view.treeitem.ServerTreeItem;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextArea;

import java.sql.Connection;
import java.util.List;

public class ServerItemActionEventHandlerImpl implements IServerItemActionEventHandler {

    private final TextArea ddlTextArea;

    public ServerItemActionEventHandlerImpl(Node root) {
        this.ddlTextArea = NodeUtils.lookup(root, "#ddlTextArea");
    }

    @Override
    public void onOpen(ServerTreeItem serverTreeItem) {
        if (!serverTreeItem.isOpen()) {
            TaskManger.execute(new OpenServerTask(serverTreeItem));
        }
    }

    @Override
    public void onClose(ServerTreeItem serverTreeItem) {
        serverTreeItem.getChildren().forEach(item -> {
            if (item instanceof ICloseable) {
                ((ICloseable) item).close();
            }
        });
        serverTreeItem.setExpanded(false);
        serverTreeItem.setOpen(false);
        serverTreeItem.getChildren().clear();

    }

    @Override
    public void onDelete(ServerTreeItem serverTreeItem) {
        this.onClose(serverTreeItem);
        serverTreeItem.getParent().getChildren().remove(serverTreeItem);
    }

    @Override
    public void refresh(ServerTreeItem treeItem) {
        treeItem.getChildren().clear();
        treeItem.setOpen(false);
        this.onOpen(treeItem);
    }

    @Override
    public void onDoubleClick(ServerTreeItem serverTreeItem) {
        this.onOpen(serverTreeItem);
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

    private static class OpenServerTask extends BaseTask<List<CatalogMetaData>> {

        private final ServerTreeItem serverTreeItem;

        private OpenServerTask(ServerTreeItem serverTreeItem) {
            this.serverTreeItem = serverTreeItem;
        }

        @Override
        protected List<CatalogMetaData> call() throws Exception {
            try (Connection connection = serverTreeItem.getMetadataProvider().getConnection(serverTreeItem.getConnectionConfiguration())) {
                return this.serverTreeItem.getMetadataProvider().getCatalogs(connection);
            }
        }


        @Override
        protected void succeeded() {
            super.succeeded();
            IConnectionConfiguration connectionConfiguration = serverTreeItem.getConnectionConfiguration();
            IMetadataProvider metadataProvider = serverTreeItem.getMetadataProvider();
            ObservableList children = this.serverTreeItem.getChildren();
            children.clear();
            List<CatalogMetaData> value = getValue();
            for (CatalogMetaData metaData : value) {
                CatalogTreeItem item = new CatalogTreeItem(metaData, serverTreeItem.getRoot(), connectionConfiguration, metadataProvider);
                //noinspection unchecked
                children.add(item);
            }
            serverTreeItem.setExpanded(true);
            serverTreeItem.setOpen(true);
        }
    }
}
