package com.jean.database.gui.view.treeitem;

import com.jean.database.gui.constant.Images;
import com.jean.database.gui.handler.IServerItemActionEventHandler;
import com.jean.database.gui.view.action.IContextMenu;
import com.jean.database.gui.view.action.IMouseClickAction;
import com.jean.database.gui.view.action.ISelectAction;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * @author jinshubao
 */
public class ServerTreeItem extends TreeItem<String> implements IContextMenu, IMouseClickAction, ISelectAction {

    private final ContextMenu contextMenu;

    private final IServerItemActionEventHandler serverItemActionEventHandler;

    private final BooleanProperty open = new SimpleBooleanProperty(this, "onOpen", false);

    public ServerTreeItem(String value, IServerItemActionEventHandler serverItemActionEventHandler) {
        super(value);
        this.serverItemActionEventHandler = serverItemActionEventHandler;
        this.contextMenu = this.createContextMenu();
    }

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem open = new MenuItem("打开连接");
        open.setOnAction(event -> serverItemActionEventHandler.onOpen(ServerTreeItem.this));

        MenuItem close = new MenuItem("关闭连接");
        close.setOnAction(event -> serverItemActionEventHandler.onClose(ServerTreeItem.this));

        MenuItem delete = new MenuItem("删除连接", new ImageView(new Image(getClass().getResourceAsStream(Images.DELETE_IMAGE))));
        delete.setOnAction(event -> serverItemActionEventHandler.onDelete(ServerTreeItem.this));

        MenuItem copy = new MenuItem("复制连接...");
        copy.setOnAction(event -> serverItemActionEventHandler.onCopy(ServerTreeItem.this));

        MenuItem properties = new MenuItem("连接属性...");
        properties.setOnAction(event -> serverItemActionEventHandler.onDetails(ServerTreeItem.this));

        MenuItem refresh = new MenuItem("刷新", new ImageView(new Image(getClass().getResourceAsStream(Images.REFRESH_IMAGE))));
        refresh.setOnAction(event -> serverItemActionEventHandler.refresh(ServerTreeItem.this));

        contextMenu.getItems().addAll(open, close, copy, delete, properties, refresh);
        return contextMenu;
    }

    @Override
    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }

    @Override
    public void click(MouseEvent event) {
        if (event.getClickCount() == 1) {
            this.serverItemActionEventHandler.onMouseClick(this);
        } else if (event.getClickCount() == 2) {
            this.serverItemActionEventHandler.onMouseDoubleClick(this);
        }
    }

    public boolean getOpen() {
        return open.get();
    }

    public BooleanProperty openProperty() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open.set(open);
    }

    @Override
    public void selected() {

    }
}
