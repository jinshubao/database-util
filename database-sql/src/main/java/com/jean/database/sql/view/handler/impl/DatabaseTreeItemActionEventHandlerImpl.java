package com.jean.database.sql.view.handler.impl;

import com.jean.database.api.AbstractConnectionConfiguration;
import com.jean.database.api.BaseTask;
import com.jean.database.api.TaskManger;
import com.jean.database.api.view.ViewContext;
import com.jean.database.api.view.action.ICloseable;
import com.jean.database.sql.SQLConnectionConfiguration;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.meta.CatalogMetaData;
import com.jean.database.sql.view.TreeItemViewContext;
import com.jean.database.sql.view.handler.IDatabaseTreeItemActionEventHandler;
import com.jean.database.sql.view.treeitem.CatalogTreeItem;
import com.jean.database.sql.view.treeitem.DatabaseTreeItem;
import javafx.collections.ObservableList;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.util.List;

public class DatabaseTreeItemActionEventHandlerImpl implements IDatabaseTreeItemActionEventHandler {

    @Override
    public void onOpen(DatabaseTreeItem databaseTreeItem) {
        if (!databaseTreeItem.isOpen()) {
            TaskManger.execute(new OpenServerTask(databaseTreeItem));
        }
    }

    @Override
    public void onClose(DatabaseTreeItem databaseTreeItem) {
        databaseTreeItem.getChildren().forEach(item -> {
            if (item instanceof ICloseable) {
                ((ICloseable) item).close();
            }
        });
        databaseTreeItem.setExpanded(false);
        databaseTreeItem.setOpen(false);
        databaseTreeItem.getChildren().clear();

    }

    @Override
    public void onDelete(DatabaseTreeItem databaseTreeItem) {
        this.onClose(databaseTreeItem);
        databaseTreeItem.getParent().getChildren().remove(databaseTreeItem);
    }

    @Override
    public void onRefresh(DatabaseTreeItem treeItem) {
        this.onClose(treeItem);
        this.onOpen(treeItem);
    }

    @Override
    public void onClick(DatabaseTreeItem databaseTreeItem) {

    }

    @Override
    public void onDoubleClick(DatabaseTreeItem databaseTreeItem) {
        this.onOpen(databaseTreeItem);
    }

    @Override
    public void onSelected(DatabaseTreeItem databaseTreeItem) {
        databaseTreeItem.getViewContext().getDdlInfoTab().setText(databaseTreeItem.getValue());
    }

    @Override
    public void onOpenCommandLine(DatabaseTreeItem databaseTreeItem) {

    }

    @Override
    public void onExecuteSqlFile(DatabaseTreeItem databaseTreeItem) {

    }

    @Override
    public void onTransformData(DatabaseTreeItem catalogTreeItem) {

    }

    @Override
    public void onCopy(DatabaseTreeItem catalogTreeItem) {

    }

    private static class OpenServerTask extends BaseTask<List<CatalogMetaData>> {

        private final DatabaseTreeItem databaseTreeItem;
        private final SQLConnectionConfiguration connectionConfiguration;
        private final SQLMetadataProvider metadataProvider;

        private OpenServerTask(DatabaseTreeItem databaseTreeItem) {
            this.databaseTreeItem = databaseTreeItem;
            this.metadataProvider = databaseTreeItem.getMetadataProvider();
            this.connectionConfiguration = databaseTreeItem.getConnectionConfiguration();
        }

        @Override
        protected List<CatalogMetaData> call() throws Exception {
            try (Connection connection = metadataProvider.getConnection(connectionConfiguration)) {
                return metadataProvider.getCatalogs(connection);
            }
        }


        @Override
        protected void succeeded() {
            super.succeeded();
            ObservableList children = databaseTreeItem.getChildren();
            children.clear();
            List<CatalogMetaData> value = getValue();
            TreeItemViewContext viewContext = databaseTreeItem.getViewContext();
            for (CatalogMetaData metaData : value) {
                CatalogTreeItem item = new CatalogTreeItem(metaData, viewContext, connectionConfiguration, metadataProvider);
                //noinspection unchecked
                children.add(item);
            }
            databaseTreeItem.setExpanded(true);
            databaseTreeItem.setOpen(true);
        }
    }
}
