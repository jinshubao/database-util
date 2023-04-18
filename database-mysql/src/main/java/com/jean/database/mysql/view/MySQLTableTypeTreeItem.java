package com.jean.database.mysql.view;


import com.jean.database.context.ApplicationContext;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.constant.TableType;
import com.jean.database.sql.item.SQLDatabaseItem;
import com.jean.database.sql.meta.TableSummaries;
import com.jean.database.sql.meta.TableTypeMetaData;
import com.jean.database.task.BackgroundTask;

import java.util.List;

public class MySQLTableTypeTreeItem extends SQLDatabaseItem<TableTypeMetaData> {

    private final SQLMetadataProvider metadataProvider;

    public MySQLTableTypeTreeItem(ApplicationContext context, TableTypeMetaData value, SQLMetadataProvider metadataProvider) {
        super(context, value);
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
            getContext().execute(new RefreshTableInfoTask());
        }
    }

    private class RefreshTableInfoTask extends BackgroundTask<List<TableSummaries>> {

        SQLMetadataProvider metadataProvider = MySQLTableTypeTreeItem.this.metadataProvider;
        TableTypeMetaData tableTypeMetaData = MySQLTableTypeTreeItem.this.getValue();

        public RefreshTableInfoTask() {
            super("刷新表信息");
        }

        @Override
        protected List<TableSummaries> doBackground() throws Exception {
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
