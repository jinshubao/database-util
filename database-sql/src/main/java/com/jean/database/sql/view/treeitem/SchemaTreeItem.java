package com.jean.database.sql.view.treeitem;

import com.jean.database.api.LoggerWrapper;
import com.jean.database.sql.SQLConnectionConfiguration;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.meta.SchemaMetaData;
import com.jean.database.sql.view.TreeItemViewContext;
import com.jean.database.sql.view.handler.ISchemaItemActionEventHandler;
import com.jean.database.sql.view.handler.impl.SchemaItemActionEventHandlerImpl;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class SchemaTreeItem extends BaseDatabaseItem<SchemaMetaData> {

    private final ContextMenu contextMenu;
    private final ISchemaItemActionEventHandler actionEventHandler;

    public SchemaTreeItem(SchemaMetaData value, TreeItemViewContext viewContext, SQLConnectionConfiguration connectionConfiguration, SQLMetadataProvider metadataProvider) {
        super(value, viewContext, connectionConfiguration, metadataProvider);
        this.actionEventHandler = LoggerWrapper.warp(new SchemaItemActionEventHandlerImpl());
        this.contextMenu = this.createContextMenu();
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

    private ContextMenu createContextMenu() {
        MenuItem open = new MenuItem("打开数据库");
        open.disableProperty().bind(this.openProperty());
        open.setOnAction(event -> this.actionEventHandler.onOpen(SchemaTreeItem.this));

        MenuItem delete = new MenuItem("删除数据库");
        delete.setOnAction(event -> this.actionEventHandler.onDelete(SchemaTreeItem.this));

        MenuItem refresh = new MenuItem("刷新");
        refresh.disableProperty().bind(this.openProperty().not());
        refresh.setOnAction(event -> this.actionEventHandler.onRefresh(SchemaTreeItem.this));

        return new ContextMenu(open, delete, refresh);
    }

}
