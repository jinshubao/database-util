package com.jean.database.mysql.view;

import com.jean.database.action.IContextMenu;
import com.jean.database.action.IMouseAction;
import com.jean.database.mysql.config.MySQLConnectionConfiguration;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;

public class MySQLServerTreeItem extends TreeItem implements IContextMenu, IMouseAction {

    private IMouseAction mouseActionHandler;

    private ContextMenu contextMenu;

    public MySQLServerTreeItem(MySQLConnectionConfiguration config, Node imageView, IMouseAction mouseActionHandler, ContextMenu contextMenu) {
        super(config, imageView);
        this.mouseActionHandler = mouseActionHandler;
        this.contextMenu = contextMenu;
    }

    @Override
    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    @Override
    public void click() {
        mouseActionHandler.click();
    }

    @Override
    public void doubleClick() {
        mouseActionHandler.doubleClick();
    }

    @Override
    public void select() {
        mouseActionHandler.select();
    }
}