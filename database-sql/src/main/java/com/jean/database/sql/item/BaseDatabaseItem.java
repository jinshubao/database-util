package com.jean.database.sql.item;

import com.jean.database.context.ApplicationContext;
import com.jean.database.item.BaseTreeItem;
import com.jean.database.ability.ICloseable;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;


public abstract class BaseDatabaseItem<T> extends BaseTreeItem<T> {

//    private final SQLMetadataProvider metadataProvider;

    public BaseDatabaseItem(ApplicationContext context, T value) {
        super(context, value);
//        this.metadataProvider = metadataProvider;
    }

//    public SQLMetadataProvider getMetadataProvider() {
//        return metadataProvider;
//    }


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
