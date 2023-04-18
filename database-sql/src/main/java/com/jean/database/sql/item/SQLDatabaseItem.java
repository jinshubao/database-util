package com.jean.database.sql.item;

import com.jean.database.context.ApplicationContext;
import com.jean.database.view.AbstractTreeItem;
import com.jean.database.ability.ICloseable;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;


public abstract class SQLDatabaseItem<T> extends AbstractTreeItem<T> {


    public SQLDatabaseItem(ApplicationContext context, T value) {
        super(context, value);
    }

    public SQLDatabaseItem(ApplicationContext context, T value, Node graphic) {
        super(context, value, graphic);
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
