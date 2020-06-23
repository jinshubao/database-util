package com.jean.database.sql.view.treeitem;


import com.jean.database.api.BaseTask;
import com.jean.database.api.TaskManger;
import com.jean.database.sql.SQLConnectionConfiguration;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.SQLObjectTabController;
import com.jean.database.sql.meta.TableSummaries;
import com.jean.database.sql.meta.TableTypeMetaData;

import java.sql.Connection;
import java.util.List;

public class TableTypeTreeItem extends BaseDatabaseItem<TableTypeMetaData> {

    private final SQLObjectTabController objectTabController;

    public TableTypeTreeItem(TableTypeMetaData value, SQLConnectionConfiguration connectionConfiguration, SQLMetadataProvider metadataProvider, SQLObjectTabController objectTabController) {
        super(value, connectionConfiguration, metadataProvider);
        this.objectTabController = objectTabController;
    }

    @Override
    public void click() {
    }

    @Override
    public void doubleClick() {
    }

    @Override
    public void select() {
        TaskManger.execute(new RefreshTableInfoTask());
    }

    @Override
    public void close() {
    }


    private class RefreshTableInfoTask extends BaseTask<List<TableSummaries>> {

        SQLMetadataProvider metadataProvider = TableTypeTreeItem.this.getMetadataProvider();
        SQLConnectionConfiguration connectionConfiguration = TableTypeTreeItem.this.getConnectionConfiguration();
        TableTypeMetaData tableTypeMetaData = TableTypeTreeItem.this.getValue();

        @Override
        protected List<TableSummaries> call() throws Exception {
            try (Connection connection = connectionConfiguration.getConnection()) {
                return metadataProvider.getTableSummaries(connection, tableTypeMetaData.getTableCat(), tableTypeMetaData.getTableSchema(), null, null);
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            List<TableSummaries> tableSummaries = getValue();
            if (tableSummaries != null && !tableSummaries.isEmpty()) {
                objectTabController.setObjectValue(tableSummaries);
            }
        }
    }

}
