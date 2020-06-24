package com.jean.database.api.view.treeitem;

import com.jean.database.api.view.action.ICloseable;
import com.jean.database.api.view.action.IContextMenu;
import com.jean.database.api.view.action.IMouseAction;
import com.jean.database.api.view.action.IRefreshable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;


public abstract class BaseTreeItem<T> extends TreeItem<T> implements IContextMenu, IMouseAction, IRefreshable, ICloseable {

    private final BooleanProperty open = new SimpleBooleanProperty(this, "onOpen", false);

    public BaseTreeItem(T value) {
        super(value);
    }

    public BaseTreeItem(T value, Node graphic) {
        super(value, graphic);
    }

    public boolean isOpen() {
        return open.get();
    }

    public BooleanProperty openProperty() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open.set(open);
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

    }

}

