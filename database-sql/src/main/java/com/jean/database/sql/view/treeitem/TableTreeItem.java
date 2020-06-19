package com.jean.database.sql.view.treeitem;

import com.jean.database.api.LoggerWrapper;
import com.jean.database.sql.SQLConnectionConfiguration;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.constant.Images;
import com.jean.database.sql.meta.TableMetaData;
import com.jean.database.sql.view.SQLDataTableTab;
import com.jean.database.sql.view.TreeItemViewContext;
import com.jean.database.sql.view.handler.ITableItemActionEventHandler;
import com.jean.database.sql.view.handler.impl.TableItemActionEventHandlerImpl;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.lang.ref.WeakReference;

/**
 * @author jinshubao
 */
public class TableTreeItem extends BaseDatabaseItem<TableMetaData> {

    private final ContextMenu contextMenu;
    private final ITableItemActionEventHandler actionEventHandler;


    private WeakReference<SQLDataTableTab> dataTableTab = new WeakReference<>(null);


    public TableTreeItem(TableMetaData value, TreeItemViewContext viewContext, SQLConnectionConfiguration connectionConfiguration, SQLMetadataProvider metadataProvider) {
        super(value, viewContext, connectionConfiguration, metadataProvider);
        this.contextMenu = this.createContextMenu();
        this.actionEventHandler = LoggerWrapper.warp(new TableItemActionEventHandlerImpl());
        this.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(Images.TABLE_IMAGE))));
    }

    @Override
    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }

    @Override
    public void click() {
        this.actionEventHandler.onClick(this);
    }

    @Override
    public void doubleClick() {
        this.actionEventHandler.onDoubleClick(this);
    }

    @Override
    public void select() {
        this.actionEventHandler.onSelected(this);
    }

    @Override
    public void close() {
        actionEventHandler.onClose(this);
    }

    @Override
    public void refresh() {
        actionEventHandler.onRefresh(this);
    }

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem open = new MenuItem("打开表");
        open.disableProperty().bind(openProperty());
        open.setOnAction(event -> this.actionEventHandler.onOpen(TableTreeItem.this));

        MenuItem copy = new MenuItem("复制表");
        copy.setOnAction(event -> this.actionEventHandler.onCopy(TableTreeItem.this));

        MenuItem delete = new MenuItem("删除表", new ImageView(new Image(getClass().getResourceAsStream(Images.DELETE_IMAGE))));
        delete.setOnAction(event -> this.actionEventHandler.onDelete(TableTreeItem.this));

        MenuItem refresh = new MenuItem("刷新", new ImageView(new Image(getClass().getResourceAsStream(Images.REFRESH_IMAGE))));
        refresh.disableProperty().bind(openProperty().not());
        refresh.setOnAction(event -> this.actionEventHandler.onRefresh(TableTreeItem.this));

        contextMenu.getItems().addAll(open, copy, delete, refresh);
        return contextMenu;
    }

    public SQLDataTableTab getDataTableTab() {
        return dataTableTab.get();
    }

    public void setDataTableTab(SQLDataTableTab dataTableTab) {
        this.dataTableTab = new WeakReference<>(dataTableTab);
    }
}
