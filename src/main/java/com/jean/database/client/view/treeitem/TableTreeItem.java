package com.jean.database.client.view.treeitem;

import com.jean.database.client.controller.MainController;
import com.jean.database.core.connection.IConnectionConfiguration;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.core.meta.SchemaMetaData;
import com.jean.database.core.meta.TableMetaData;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

/**
 * @author jinshubao
 */
public class TableTreeItem extends AbstractTreeItem {

    public TableTreeItem(IConnectionConfiguration connectionConfiguration,
                         CatalogMetaData catalogMetaData, SchemaMetaData schemaMetaData, TableMetaData value) {
        super(connectionConfiguration, catalogMetaData, schemaMetaData, value, value);
    }

    @Override
    public void refreshData() {
    }

    @Override
    public ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem open = new MenuItem("打开表");
        open.setOnAction(event -> {
            MainController controller = getController();
            try {
                controller.openTable(getConnectionConfiguration(),
                        getCatalogMetaData(),
                        getSchemaMetaData(),
                        tableMetaData.getTableName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        MenuItem delete = new MenuItem("删除表");
        contextMenu.getItems().addAll(open, delete);
        return contextMenu;
    }

    @Override
    public void onDoubleClick(MouseEvent event) {
        try {
            getController().openTable(connectionConfiguration, catalogMetaData, schemaMetaData, tableMetaData.getTableName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
