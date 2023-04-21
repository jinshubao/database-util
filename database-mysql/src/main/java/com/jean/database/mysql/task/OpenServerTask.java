package com.jean.database.mysql.task;

import com.jean.database.sql.SQLMetadataFactory;
import com.jean.database.sql.meta.CatalogMetaData;
import com.jean.database.task.BackgroundTask;

import java.util.List;

public class OpenServerTask extends BackgroundTask<List<CatalogMetaData>> {

    SQLMetadataFactory metadataProvider;

    public OpenServerTask(SQLMetadataFactory metadataProvider) {
        super("打开数据库");
        this.metadataProvider = metadataProvider;
    }

    @Override
    protected List<CatalogMetaData> doBackground() throws Exception {
        updateMessage("正在获取数据库信息");
        updateProgress(0, 1);
        List<CatalogMetaData> catalogs = metadataProvider.getCatalogs();
        updateMessage("获取数据库信息完成");
        updateProgress(1, 1);
        return catalogs;
    }
}