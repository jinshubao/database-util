package com.jean.database.view;

import com.jean.database.ability.ICloseable;
import com.jean.database.action.IContextMenu;
import com.jean.database.action.IMouseAction;
import com.jean.database.ability.IRefreshable;
import com.jean.database.context.ApplicationContext;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;


public abstract class AbstractTreeItem<T> extends TreeItem<T> implements IContextMenu, IMouseAction, IRefreshable, ICloseable {

    private final BooleanProperty open = new SimpleBooleanProperty(this, "onOpen", false);


    private final ApplicationContext context;

    public AbstractTreeItem(ApplicationContext context, T value) {
        this(context, value, null);
    }

    public AbstractTreeItem(ApplicationContext context, T value, Node graphic) {
        super(value, graphic);
        this.context = context;
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

    public ApplicationContext getContext() {
        return context;
    }
}

