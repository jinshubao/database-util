package com.jean.database.gui.view.treeitem;

import com.jean.database.core.meta.TableMetaData;
import com.jean.database.gui.constant.Images;
import com.jean.database.gui.handler.ITableItemActionEventHandler;
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
public class TableTreeItem extends TreeItem<TableMetaData> implements IContextMenu, IMouseClickAction, ISelectAction {

    private final BooleanProperty open = new SimpleBooleanProperty(this, "onOpen", false);

    private final ContextMenu contextMenu;

    private final TableMetaData tableMetaData;

    private final ITableItemActionEventHandler tableItemActionEventHandler;


    public TableTreeItem(TableMetaData tableMetaData, ITableItemActionEventHandler tableItemActionEventHandler) {
        super(tableMetaData);
        this.contextMenu = this.createContextMenu();
        this.tableMetaData = tableMetaData;
        this.tableItemActionEventHandler = tableItemActionEventHandler;
        setGraphic(new ImageView(new Image(getClass().getResourceAsStream(Images.TABLE_IMAGE))));
    }

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem open = new MenuItem("打开表");
        open.disableProperty().bind(this.open);
        open.setOnAction(event -> tableItemActionEventHandler.onOpen(TableTreeItem.this));

        MenuItem copy = new MenuItem("复制表");
        copy.setOnAction(event -> tableItemActionEventHandler.onCopy(TableTreeItem.this));

        MenuItem delete = new MenuItem("删除表", new ImageView(new Image(getClass().getResourceAsStream(Images.DELETE_IMAGE))));
        delete.setOnAction(event -> tableItemActionEventHandler.onDelete(TableTreeItem.this));

        MenuItem refresh = new MenuItem("刷新", new ImageView(new Image(getClass().getResourceAsStream(Images.REFRESH_IMAGE))));
        refresh.setOnAction(event -> tableItemActionEventHandler.refresh(TableTreeItem.this));

        contextMenu.getItems().addAll(open, copy, delete, refresh);
        return contextMenu;
    }

    @Override
    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    @Override
    public void click(MouseEvent event) {
        if (event.getClickCount() == 1) {
            tableItemActionEventHandler.onMouseClick(this);
        } else if (event.getClickCount() == 2) {
            tableItemActionEventHandler.onMouseDoubleClick(this);
        }
    }

    public TableMetaData getTableMetaData() {
        return tableMetaData;
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
    public void selected() {
        this.tableItemActionEventHandler.onSelected(this);
    }
}
