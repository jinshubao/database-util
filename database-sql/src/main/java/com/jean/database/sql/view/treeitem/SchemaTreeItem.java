package com.jean.database.sql.view.treeitem;

import com.jean.database.sql.SQLConnectionConfiguration;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.SQLObjectTabController;
import com.jean.database.sql.meta.SchemaMetaData;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class SchemaTreeItem extends BaseDatabaseItem<SchemaMetaData> {

    private final SQLObjectTabController objectTabController;
    private final ContextMenu contextMenu;

    public SchemaTreeItem(SchemaMetaData value, SQLConnectionConfiguration connectionConfiguration, SQLMetadataProvider metadataProvider, SQLObjectTabController objectTabController) {
        super(value, connectionConfiguration, metadataProvider);
        this.objectTabController = objectTabController;
        this.contextMenu = this.createContextMenu();
    }

    @Override
    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }

    @Override
    public void click() {
    }

    @Override
    public void doubleClick() {
    }

    @Override
    public void select() {
    }

    @Override
    public void close() {
    }

    private ContextMenu createContextMenu() {
        MenuItem open = new MenuItem("打开数据库");
        open.disableProperty().bind(this.openProperty());
        open.setOnAction(event -> {
        });

        MenuItem delete = new MenuItem("删除数据库");
        delete.setOnAction(event -> {
        });

        MenuItem refresh = new MenuItem("刷新");
        refresh.disableProperty().bind(this.openProperty().not());
        refresh.setOnAction(event -> {
        });

        return new ContextMenu(open, delete, refresh);
    }

}
