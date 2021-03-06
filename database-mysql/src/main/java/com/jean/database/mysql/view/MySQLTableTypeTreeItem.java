package com.jean.database.mysql.view;


import com.jean.database.api.BaseTask;
import com.jean.database.api.TaskManger;
import com.jean.database.api.ViewContext;
import com.jean.database.mysql.MySQLObjectTabController;
import com.jean.database.sql.BaseDatabaseItem;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.constant.TableType;
import com.jean.database.sql.meta.TableSummaries;
import com.jean.database.sql.meta.TableTypeMetaData;

import java.util.List;

public class MySQLTableTypeTreeItem extends BaseDatabaseItem<TableTypeMetaData> {

    private final MySQLObjectTabController objectTabController;

    public MySQLTableTypeTreeItem(ViewContext viewContext, TableTypeMetaData value, SQLMetadataProvider metadataProvider, MySQLObjectTabController objectTabController) {
        super(viewContext, value, metadataProvider);
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
        if (TableType.TABLE.equals(getValue().getTableType())) {
            TaskManger.execute(new RefreshTableInfoTask());
        }
    }

    private class RefreshTableInfoTask extends BaseTask<List<TableSummaries>> {

        SQLMetadataProvider metadataProvider = MySQLTableTypeTreeItem.this.getMetadataProvider();
        TableTypeMetaData tableTypeMetaData = MySQLTableTypeTreeItem.this.getValue();

        @Override
        protected List<TableSummaries> call() throws Exception {
            return metadataProvider.getTableSummaries(tableTypeMetaData.getTableCat(), tableTypeMetaData.getTableSchema(), null, null);
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
