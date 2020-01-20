package com.jean.database.gui.view.treeitem;

import com.jean.database.core.meta.TableMetaData;
import com.jean.database.gui.handler.TableActionEventHandler;
import com.jean.database.gui.view.IContextMenu;
import com.jean.database.gui.view.IDoubleClick;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;

/**
 * @author jinshubao
 */
public class TableTreeItem extends TreeItem<TableMetaData> implements IContextMenu, IDoubleClick {

    private final BooleanProperty open = new SimpleBooleanProperty(this, "open", false);

    private final ContextMenu contextMenu;

    private final TableMetaData tableMetaData;

    private final TableActionEventHandler eventHandler;


    public TableTreeItem(TableMetaData tableMetaData, TableActionEventHandler eventHandler) {
        super(tableMetaData);
        this.contextMenu = this.createContextMenu();
        this.tableMetaData = tableMetaData;
        this.eventHandler = eventHandler;
    }

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem open = new MenuItem("打开表");
        open.disableProperty().bind(this.open);
        open.setOnAction(event -> eventHandler.openTable(TableTreeItem.this));

        MenuItem copy = new MenuItem("复制表");
        copy.setOnAction(event -> eventHandler.copyTable(TableTreeItem.this));

        MenuItem delete = new MenuItem("删除表");
        delete.setOnAction(event -> eventHandler.deleteTable(TableTreeItem.this));

        MenuItem refresh = new MenuItem("刷新");
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
