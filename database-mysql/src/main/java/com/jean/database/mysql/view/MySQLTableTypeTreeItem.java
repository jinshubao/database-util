package com.jean.database.mysql.view;


import com.jean.database.context.ApplicationContext;
import com.jean.database.task.BaseTask;
import com.jean.database.task.TaskManger;
import com.jean.database.mysql.controller.MySQLObjectTabController;
import com.jean.database.sql.item.BaseDatabaseItem;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.constant.TableType;
import com.jean.database.sql.meta.TableSummaries;
import com.jean.database.sql.meta.TableTypeMetaData;

import java.util.List;

public class MySQLTableTypeTreeItem extends BaseDatabaseItem<TableTypeMetaData> {



    private final SQLMetadataProvider metadataProvider;
    public MySQLTableTypeTreeItem(ApplicationContext context,  TableTypeMetaData value, SQLMetadataProvider metadataProvider) {
        super( context,value);
        this.metadataProvider = metadataProvider;
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

        SQLMetadataProvider metadataProvider = MySQLTableTypeTreeItem.this.metadataProvider;
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
                //TODO refresh table info
            }
        }
    }

}
