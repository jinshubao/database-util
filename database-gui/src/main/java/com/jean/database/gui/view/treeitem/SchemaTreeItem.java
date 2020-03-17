package com.jean.database.gui.view.treeitem;

import com.jean.database.core.meta.SchemaMetaData;
import com.jean.database.gui.handler.ISchemaItemActionEventHandler;
import com.jean.database.gui.view.action.IContextMenu;
import com.jean.database.gui.view.action.IMouseClickAction;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;

public class SchemaTreeItem extends TreeItem<SchemaMetaData> implements IContextMenu, IMouseClickAction {

    private final BooleanProperty open = new SimpleBooleanProperty(this, "onOpen", false);

    private final ContextMenu contextMenu;

    private final SchemaMetaData schemaMetaData;

    private final ISchemaItemActionEventHandler eventHandler;

    public SchemaTreeItem(SchemaMetaData schemaMetaData, ISchemaItemActionEventHandler eventHandler) {
        super(schemaMetaData);
        this.schemaMetaData = schemaMetaData;
        this.eventHandler = eventHandler;
        this.contextMenu = this.createContextMenu();
    }


    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem open = new MenuItem("打开数据库");
        open.disableProperty().bind(this.open);
        open.setOnAction(event -> eventHandler.onOpen(SchemaTreeItem.this));

        MenuItem delete = new MenuItem("删除数据库");
        delete.setOnAction(event -> eventHandler.onDelete(SchemaTreeItem.this));


        MenuItem refresh = new MenuItem("刷新");
        refresh.setOnAction(event -> eventHandler.refresh(SchemaTreeItem.this));

        contextMenu.getItems().addAll(open, delete, refresh);
        return contextMenu;
    }

    public SchemaMetaData getSchemaMetaData() {
        return schemaMetaData;
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
    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }


    @Override
    public void click(MouseEvent event) {
        eventHandler.refresh(this);
    }
}
