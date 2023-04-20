package com.jean.database.mysql.view.item;

import com.jean.database.mysql.handler.MySQLTableTreeItemActionEventHandler;
import com.jean.database.sql.meta.TableMetaData;
import com.jean.database.utils.ImageUtils;
import com.jean.database.view.AbstractTreeItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * @author jinshubao
 */
public class MySQLTableTreeItem extends AbstractTreeItem<TableMetaData> {

    private MySQLTableTreeItemActionEventHandler itemActionEventHandler;

    private final ContextMenu contextMenu;

    private MenuItem open;
    private MenuItem copy;
    private MenuItem delete;
    private MenuItem refresh;


    public MySQLTableTreeItem(TableMetaData tableMetaData) {
        super(tableMetaData);
        this.contextMenu = this.createContextMenu();
        this.setGraphic(ImageUtils.createImageView("/mysql/table.png"));
    }

    public void setItemActionEventHandler(MySQLTableTreeItemActionEventHandler itemActionEventHandler) {
        this.itemActionEventHandler = itemActionEventHandler;

        open.disableProperty().bind(getItemActionEventHandler().openMenuDisableProperty());
        open.setOnAction(event -> getItemActionEventHandler().open());

        copy.disableProperty().bind(getItemActionEventHandler().copyMenuDisableProperty());
        copy.setOnAction(event -> getItemActionEventHandler().copy());

        delete.disableProperty().bind(getItemActionEventHandler().deleteMenuDisableProperty());
        delete.setOnAction(event -> getItemActionEventHandler().delete());

        refresh.disableProperty().bind(getItemActionEventHandler().refreshMenuDisableProperty());
        refresh.setOnAction(event -> getItemActionEventHandler().refresh());

    }

    public MySQLTableTreeItemActionEventHandler getItemActionEventHandler() {
        return itemActionEventHandler;
    }

    @Override
    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }


    @Override
    public void doubleClick() {
        getItemActionEventHandler().doubleClick();
    }

    @Override
    public void select() {
        getItemActionEventHandler().click();
    }


    @Override
    public void refresh() {
        getItemActionEventHandler().refresh();
    }

    @Override
    public void close() {
        getItemActionEventHandler().close();
    }

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        open = new MenuItem("打开表", ImageUtils.createImageView("/image/connect.png"));
        copy = new MenuItem("复制表", ImageUtils.createImageView("/image/copy.png"));
        delete = new MenuItem("删除表", ImageUtils.createImageView("/image/delete.png"));
        refresh = new MenuItem("刷新", ImageUtils.createImageView("/image/refresh.png"));
        contextMenu.getItems().addAll(open, copy, delete, refresh);
        return contextMenu;
    }


}
