package com.jean.database.gui.view.treeitem;

import com.jean.database.core.meta.SchemaMetaData;
import com.jean.database.gui.view.action.IContextMenu;
import com.jean.database.gui.view.action.IMouseAction;
import com.jean.database.gui.view.handler.ISchemaItemActionEventHandler;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;

public class SchemaTreeItem extends TreeItem<SchemaMetaData> implements IContextMenu, IMouseAction {

    private final BooleanProperty open = new SimpleBooleanProperty(this, "onOpen", false);

    private final ContextMenu contextMenu;

    private final SchemaMetaData schemaMetaData;

    private final ISchemaItemActionEventHandler schemaItemActionEventHandler;

    public SchemaTreeItem(SchemaMetaData schemaMetaData, ISchemaItemActionEventHandler schemaItemActionEventHandler) {
        super(schemaMetaData);
        this.schemaMetaData = schemaMetaData;
        this.schemaItemActionEventHandler = schemaItemActionEventHandler;
        this.contextMenu = this.createContextMenu();
    }


    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem open = new MenuItem("打开数据库");
        open.disableProperty().bind(this.open);
        open.setOnAction(event -> this.schemaItemActionEventHandler.onOpen(SchemaTreeItem.this));

        MenuItem delete = new MenuItem("删除数据库");
        delete.setOnAction(event -> this.schemaItemActionEventHandler.onDelete(SchemaTreeItem.this));


        MenuItem refresh = new MenuItem("刷新");
        refresh.setOnAction(event -> this.schemaItemActionEventHandler.refresh(SchemaTreeItem.this));

        contextMenu.getItems().addAll(open, delete, refresh);
        return contextMenu;
    }

    public SchemaMetaData getSchemaMetaData() {
        return this.schemaMetaData;
    }

    public boolean isOpen() {
        return open.get();
    }

    public BooleanProperty openProperty() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open.set(open);
    }

    @Override
    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }


    @Override
    public void click(MouseEvent event) {
        this.schemaItemActionEventHandler.refresh(this);
    }

    @Override
    public void select() {
        this.schemaItemActionEventHandler.onSelected(this);
    }
}
