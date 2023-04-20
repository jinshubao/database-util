package com.jean.database.view;

import com.jean.database.ability.ICloseable;
import com.jean.database.ability.IRefreshable;
import com.jean.database.action.IContextMenu;
import com.jean.database.action.IMouseAction;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;


public abstract class AbstractTreeItem<T> extends TreeItem<T> implements IContextMenu, IMouseAction, IRefreshable, ICloseable {

    public AbstractTreeItem(T value) {
        super(value);
    }

    public AbstractTreeItem(T value, Node icon) {
        super(value, icon);
    }


    @Override
    public ContextMenu getContextMenu() {
        return null;
    }

    @Override
    public void click() {
    }

    @Override
    public void doubleClick() {
    }

    @Override
    public void select() {
    }

    @Override
    public void refresh() {
    }

    @Override
    public void close() {
        ObservableList<TreeItem<T>> children = getChildren();
        if (children != null) {
            children.forEach(item -> {
                if (item instanceof ICloseable) {
                    ((ICloseable) item).close();
                }
            });
            children.clear();
        }
        setExpanded(false);
    }
}

