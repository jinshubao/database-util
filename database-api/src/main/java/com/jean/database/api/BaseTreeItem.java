package com.jean.database.api;

import com.jean.database.api.action.ICloseable;
import com.jean.database.api.action.IContextMenu;
import com.jean.database.api.action.IMouseAction;
import com.jean.database.api.action.IRefreshable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;


public abstract class BaseTreeItem<T> extends TreeItem<T> implements IContextMenu, IMouseAction, IRefreshable, ICloseable {

    private final ViewContext viewContext;

    private final BooleanProperty open = new SimpleBooleanProperty(this, "onOpen", false);

    public BaseTreeItem(ViewContext viewContext, T value) {
        super(value);
        this.viewContext = viewContext;
    }

    public BaseTreeItem(ViewContext viewContext, T value, Node graphic) {
        super(value, graphic);
        this.viewContext = viewContext;
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
        setOpen(false);
    }

    public ViewContext getViewContext() {
        return viewContext;
    }
}

