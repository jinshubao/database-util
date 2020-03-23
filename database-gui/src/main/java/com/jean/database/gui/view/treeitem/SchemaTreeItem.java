package com.jean.database.gui.view.treeitem;

import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.SchemaMetaData;
import com.jean.database.gui.factory.LoggerWrapper;
import com.jean.database.gui.view.handler.IMouseEventHandler;
import com.jean.database.gui.view.handler.ISchemaItemActionEventHandler;
import com.jean.database.gui.view.handler.impl.SchemaItemActionEventHandlerImpl;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class SchemaTreeItem extends BaseTreeItem<SchemaMetaData> {

    private final ContextMenu contextMenu;

    private final ISchemaItemActionEventHandler schemaItemActionEventHandler;


    public SchemaTreeItem(SchemaMetaData value, IConnectionConfiguration connectionConfiguration, IMetadataProvider metadataProvider) {
        super(value, connectionConfiguration, metadataProvider);
        this.schemaItemActionEventHandler = LoggerWrapper.warp(new SchemaItemActionEventHandlerImpl());
        this.contextMenu = this.createContextMenu();
    }

    @Override
    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }

    @Override
    public IMouseEventHandler getMouseEventHandler() {
        return this.schemaItemActionEventHandler;
    }

    @Override
    public void close() {
        schemaItemActionEventHandler.onClose(this);
    }

    private ContextMenu createContextMenu() {
        MenuItem open = new MenuItem("打开数据库");
        open.disableProperty().bind(this.openProperty());
        open.setOnAction(event -> this.schemaItemActionEventHandler.onOpen(SchemaTreeItem.this));

        MenuItem delete = new MenuItem("删除数据库");
        delete.setOnAction(event -> this.schemaItemActionEventHandler.onDelete(SchemaTreeItem.this));

        MenuItem refresh = new MenuItem("刷新");
        refresh.disableProperty().bind(this.openProperty().not());
        refresh.setOnAction(event -> this.schemaItemActionEventHandler.refresh(SchemaTreeItem.this));

        return new ContextMenu(open, delete, refresh);
    }

}
