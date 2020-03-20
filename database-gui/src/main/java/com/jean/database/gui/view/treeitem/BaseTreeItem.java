package com.jean.database.gui.view.treeitem;

import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.gui.view.action.ICloseable;
import com.jean.database.gui.view.action.IContextMenu;
import com.jean.database.gui.view.action.IMouseAction;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;


public abstract class BaseTreeItem<T> extends TreeItem<T> implements IContextMenu, IMouseAction, ICloseable {

    private final BooleanProperty open = new SimpleBooleanProperty(this, "onOpen", false);
    private final Node root;
    private final IConnectionConfiguration connectionConfiguration;
    private final IMetadataProvider metadataProvider;


    public boolean isOpen() {
        return open.get();
    }

    public BooleanProperty openProperty() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open.set(open);
    }

    public BaseTreeItem(T value, Node root, IConnectionConfiguration connectionConfiguration, IMetadataProvider metadataProvider) {
        super(value);
        this.root = root;
        this.connectionConfiguration = connectionConfiguration;
        this.metadataProvider = metadataProvider;
    }

    public IConnectionConfiguration getConnectionConfiguration() {
        return connectionConfiguration;
    }

    public IMetadataProvider getMetadataProvider() {
        return metadataProvider;
    }

    public Node getRoot() {
        return root;
    }

}
