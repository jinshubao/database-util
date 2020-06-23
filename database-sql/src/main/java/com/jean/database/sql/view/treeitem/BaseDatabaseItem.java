package com.jean.database.sql.view.treeitem;

import com.jean.database.api.view.treeitem.BaseTreeItem;
import com.jean.database.sql.SQLConnectionConfiguration;
import com.jean.database.sql.SQLMetadataProvider;


public abstract class BaseDatabaseItem<T> extends BaseTreeItem<T> {

    private final SQLConnectionConfiguration connectionConfiguration;
    private final SQLMetadataProvider metadataProvider;

    public BaseDatabaseItem(T value, SQLConnectionConfiguration connectionConfiguration, SQLMetadataProvider metadataProvider) {
        super(value);
        this.connectionConfiguration = connectionConfiguration;
        this.metadataProvider = metadataProvider;
    }

    public SQLConnectionConfiguration getConnectionConfiguration() {
        return connectionConfiguration;
    }

    public SQLMetadataProvider getMetadataProvider() {
        return metadataProvider;
    }

}
