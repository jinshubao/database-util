package com.jean.database.mysql.task;

import com.jean.database.api.KeyValuePair;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.meta.TableMetaData;
import com.jean.database.task.BackgroundTask;

import java.util.List;

public class TableGeneralInfoTask extends BackgroundTask<List<KeyValuePair<String, Object>>> {


    SQLMetadataProvider metadataProvider;
    TableMetaData tableMetaData;

    public TableGeneralInfoTask(SQLMetadataProvider metadataProvider, TableMetaData tableMetaData) {
        super("获取表信息");
        this.metadataProvider = metadataProvider;
        this.tableMetaData = tableMetaData;
    }

    @Override
    protected List<KeyValuePair<String, Object>> doBackground() throws Exception {
        return metadataProvider.getTableDetails(tableMetaData.getTableCat(),
                tableMetaData.getTableSchema(),
                tableMetaData.getTableName(),
                new String[]{tableMetaData.getTableType()});
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        // TODO 刷新数据
//            objectTabController.setGeneralInfoValue(getValue());
//            TableMetaData tableMetaData = MySQLTableTreeItem.this.getValue();
//            objectTabController.setDdlInfo(tableMetaData.getTableName());
    }
}