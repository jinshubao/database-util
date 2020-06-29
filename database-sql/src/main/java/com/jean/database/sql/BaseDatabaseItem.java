package com.jean.database.sql;

import com.jean.database.api.BaseTreeItem;
import com.jean.database.api.action.ICloseable;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;


public abstract class BaseDatabaseItem<T> extends BaseTreeItem<T> {

    private final SQLMetadataProvider metadataProvider;

    public BaseDatabaseItem(T value, SQLMetadataProvider metadataProvider) {
        super(value);
        this.metadataProvider = metadataProvider;
    }

    public SQLMetadataProvider getMetadataProvider() {
        return metadataProvider;
    }


    @Override
    public void close() {
        super.close();
        ObservableList<TreeItem<T>> children = getChildren();
        if (children != null) {
            children.forEach(item -> {
                if (item instanceof ICloseable) {
                    ((ICloseable) item).close();
                }
            });
            children.clear();
        }
    }
}
