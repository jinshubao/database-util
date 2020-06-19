package com.jean.database.sql.view.handler.impl;

import com.jean.database.api.AbstractConnectionConfiguration;
import com.jean.database.api.BaseTask;
import com.jean.database.api.KeyValuePair;
import com.jean.database.api.TaskManger;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.meta.ColumnMetaData;
import com.jean.database.sql.meta.TableMetaData;
import com.jean.database.sql.view.SQLDataTableTab;
import com.jean.database.sql.view.SQLGeneralInfoTab;
import com.jean.database.sql.view.TreeItemViewContext;
import com.jean.database.sql.view.handler.ITableItemActionEventHandler;
import com.jean.database.sql.view.treeitem.TableTreeItem;

import java.sql.Connection;
import java.util.List;

public class TableItemActionEventHandlerImpl implements ITableItemActionEventHandler {


    public TableItemActionEventHandlerImpl() {
    }

    @Override
    public void onOpen(TableTreeItem tableTreeItem) {
        if (!tableTreeItem.isOpen()) {
            TaskManger.execute(new OpenTableTask(tableTreeItem));
        }
    }

    @Override
    public void onClose(TableTreeItem tableTreeItem) {

        SQLDataTableTab dataTableTab = tableTreeItem.getDataTableTab();
        if (dataTableTab != null) {
            tableTreeItem.setDataTableTab(null);
            tableTreeItem.close();
        }

        tableTreeItem.close();
        tableTreeItem.getChildren().clear();
        tableTreeItem.setExpanded(false);
        tableTreeItem.setOpen(false);
    }

    @Override
    public void onDelete(TableTreeItem tableTreeItem) {
        this.onClose(tableTreeItem);
        tableTreeItem.getParent().getChildren().remove(tableTreeItem);
    }

    @Override
    public void onRefresh(TableTreeItem tableTreeItem) {
//        TaskManger.execute(new RefreshTableInfoTask(tableTreeItem));
    }

    @Override
    public void onClick(TableTreeItem tableTreeItem) {

    }

    @Override
    public void onDoubleClick(TableTreeItem tableTreeItem) {
        this.onOpen(tableTreeItem);
    }

    @Override
    public void onSelected(TableTreeItem tableTreeItem) {
        this.onRefresh(tableTreeItem);
    }

    @Override
    public void onCopy(TableTreeItem catalogTreeItem) {

    }

    private static class OpenTableTask extends BaseTask<List<ColumnMetaData>> {

        private final TableTreeItem tableTreeItem;
        private final AbstractConnectionConfiguration connectionConfiguration;
        private final SQLMetadataProvider metadataProvider;
        private final TableMetaData tableMetaData;
        private List<KeyValuePair<String, Object>> tableDetails;

        private OpenTableTask(TableTreeItem tableTreeItem) {
            this.tableTreeItem = tableTreeItem;
            this.metadataProvider = tableTreeItem.getMetadataProvider();
            this.connectionConfiguration = tableTreeItem.getConnectionConfiguration();
            this.tableMetaData = tableTreeItem.getValue();
        }

        @Override
        protected List<ColumnMetaData> call() throws Exception {
            try (Connection connection = metadataProvider.getConnection(connectionConfiguration)) {
                tableDetails = metadataProvider.getTableDetails(connection, tableMetaData.getTableCat(), tableMetaData.getTableSchema(), tableMetaData.getTableName(),
                        new String[]{tableMetaData.getTableType()});
                return metadataProvider.getColumnMetaData(connection, tableMetaData.getTableCat(), tableMetaData.getTypeSchema(), tableMetaData.getTableName());
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            TreeItemViewContext viewContext = tableTreeItem.getViewContext();
            SQLGeneralInfoTab generalInfoTab = viewContext.getGeneralInfoTab();

            generalInfoTab.getItems().clear();
            generalInfoTab.getItems().addAll(tableDetails);


            SQLDataTableTab dataTableTab = tableTreeItem.getDataTableTab();
            List<ColumnMetaData> value = getValue();
            if (dataTableTab == null) {
                dataTableTab = new SQLDataTableTab(tableMetaData, connectionConfiguration, metadataProvider, value);
                dataTableTab.setOnClosed(event -> tableTreeItem.setOpen(false));
                tableTreeItem.setDataTableTab(dataTableTab);
                viewContext.getObjectTabPan().getTabs().add(dataTableTab);
                tableTreeItem.setOpen(true);
            }
            viewContext.getObjectTabPan().getSelectionModel().select(dataTableTab);
        }
    }

}
