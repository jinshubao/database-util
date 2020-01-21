package com.jean.database.gui.view.treeitem;

import com.jean.database.core.meta.TableMetaData;
import com.jean.database.gui.constant.Images;
import com.jean.database.gui.handler.ITableItemActionEventHandler;
import com.jean.database.gui.view.IContextMenu;
import com.jean.database.gui.view.IDoubleClick;
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
public class TableTreeItem extends TreeItem<TableMetaData> implements IContextMenu, IDoubleClick {

    private final BooleanProperty open = new SimpleBooleanProperty(this, "open", false);

    private final ContextMenu contextMenu;

    private final TableMetaData tableMetaData;

    private final ITableItemActionEventHandler eventHandler;


    public TableTreeItem(TableMetaData tableMetaData, ITableItemActionEventHandler eventHandler) {
        super(tableMetaData);
        this.contextMenu = this.createContextMenu();
        this.tableMetaData = tableMetaData;
        this.eventHandler = eventHandler;
        setGraphic(new ImageView(new Image(getClass().getResourceAsStream(Images.TABLE_IMAGE))));
    }

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem open = new MenuItem("打开表");
        open.disableProperty().bind(this.open);
        open.setOnAction(event -> eventHandler.openTable(TableTreeItem.this));

        MenuItem copy = new MenuItem("复制表");
        copy.setOnAction(event -> eventHandler.copyTable(TableTreeItem.this));

        MenuItem delete = new MenuItem("删除表", new ImageView(new Image(getClass().getResourceAsStream(Images.DELETE_IMAGE))));
        delete.setOnAction(event -> eventHandler.deleteTable(TableTreeItem.this));

        MenuItem refresh = new MenuItem("刷新", new ImageView(new Image(getClass().getResourceAsStream(Images.REFRESH_IMAGE))));
        refresh.setOnAction(event -> eventHandler.refresh(TableTreeItem.this));

        contextMenu.getItems().addAll(open, copy, delete, refresh);
        return contextMenu;
    }

    @Override
    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    @Override
    public void doubleClick(MouseEvent event) {
        eventHandler.openTable(this);
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
}
