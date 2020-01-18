package com.jean.database.client.view.treeitem;

import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.TableMetaData;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.sql.Connection;

/**
 * @author jinshubao
 */
public class TableTreeItem extends AbstractTreeItem {

    private final TableMetaData tableMetaData;
    private final ContextMenu contextMenu;

    public TableTreeItem(Connection connection, IMetadataProvider metadataProvider, TableMetaData tableMetaData) {
        super(connection, metadataProvider, tableMetaData);
        this.tableMetaData = tableMetaData;
        this.contextMenu = this.createContextMenu();
    }


    public ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem open = new MenuItem("打开表");
        MenuItem delete = new MenuItem("删除表");
        contextMenu.getItems().addAll(open, delete);
        return contextMenu;
    }

    @Override
    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    public TableMetaData getTableMetaData() {
        return tableMetaData;
    }
}
