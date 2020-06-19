package com.jean.database.sql.view.handler.impl;


import com.jean.database.api.BaseTask;
import com.jean.database.api.TaskManger;
import com.jean.database.api.view.action.ICloseable;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.meta.TableSummaries;
import com.jean.database.sql.meta.TableTypeMetaData;
import com.jean.database.sql.view.SQLObjectTab;
import com.jean.database.sql.view.TreeItemViewContext;
import com.jean.database.sql.view.handler.ITableTypeItemActionEventHandler;
import com.jean.database.sql.view.treeitem.TableTypeTreeItem;

import java.sql.Connection;
import java.util.List;

public class TableTypeItemActionEventHandlerImpl implements ITableTypeItemActionEventHandler {

    public TableTypeItemActionEventHandlerImpl() {
    }

    @Override
    public void onClick(TableTypeTreeItem tableTypeTreeItem) {

    }

    @Override
    public void onDoubleClick(TableTypeTreeItem tableTypeTreeItem) {

    }

    @Override
    public void onSelected(TableTypeTreeItem tableTypeTreeItem) {
        TaskManger.execute(new RefreshTableInfoTask(tableTypeTreeItem));
    }

    @Override
    public void onClose(TableTypeTreeItem tableTypeTreeItem) {
        tableTypeTreeItem.getChildren().forEach(item -> {
            if (item instanceof ICloseable) {
                ((ICloseable) item).close();
            }
        });
        tableTypeTreeItem.setExpanded(false);
        tableTypeTreeItem.getChildren().clear();
    }


    private static class RefreshTableInfoTask extends BaseTask<List<TableSummaries>> {

        private final TableTypeTreeItem tableTypeTreeItem;
        private final SQLMetadataProvider metadataProvider;
        private final TableTypeMetaData tableTypeMetaData;

        private RefreshTableInfoTask(TableTypeTreeItem tableTypeTreeItem) {
            this.tableTypeTreeItem = tableTypeTreeItem;
            this.metadataProvider = tableTypeTreeItem.getMetadataProvider();
            this.tableTypeMetaData = tableTypeTreeItem.getValue();
        }

        @Override
        protected List<TableSummaries> call() throws Exception {
            try (Connection connection = metadataProvider.getConnection(tableTypeTreeItem.getConnectionConfiguration())) {
                return metadataProvider.getTableSummaries(connection, tableTypeMetaData.getTableCat(), tableTypeMetaData.getTableSchema(), null, null);
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            TreeItemViewContext viewContext = tableTypeTreeItem.getViewContext();
            SQLObjectTab objectTab = viewContext.getObjectTab();
            viewContext.getObjectTabPan().getSelectionModel().select(objectTab);
            objectTab.getItems().clear();
            List<TableSummaries> tableSummaries = getValue();
            if (tableSummaries != null && !tableSummaries.isEmpty()) {
                objectTab.getItems().addAll(tableSummaries);
            }
        }
    }
}
