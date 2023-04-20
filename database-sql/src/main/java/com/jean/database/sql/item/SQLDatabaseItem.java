package com.jean.database.sql.item;

import com.jean.database.view.AbstractTreeItem;
import javafx.scene.Node;


public abstract class SQLDatabaseItem<T> extends AbstractTreeItem<T> {


    public SQLDatabaseItem(T value) {
        super(value);
    }

    public SQLDatabaseItem(T value, Node icon) {
        super(value, icon);
    }

    @Override
    public void close() {
        super.close();

    }
}
