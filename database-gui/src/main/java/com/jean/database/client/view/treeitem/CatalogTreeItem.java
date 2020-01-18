package com.jean.database.client.view.treeitem;

import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.core.meta.SchemaMetaData;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.sql.Connection;
import java.util.List;

/**
 * @author jinshubao
 */
public class CatalogTreeItem extends AbstractTreeItem {

    private final CatalogMetaData catalogMetaData;

    private ContextMenu contextMenu;

    public CatalogTreeItem(Connection connection, IMetadataProvider metadataProvider, CatalogMetaData catalogMetaData) throws Exception {
        super(connection, metadataProvider, catalogMetaData);
        this.catalogMetaData = catalogMetaData;
        this.contextMenu = this.createContextMenu();
        this.init();
    }

    private void init() throws Exception {
        ObservableList children = getChildren();
        IMetadataProvider metadataProvider = getMetadataProvider();
        List<SchemaMetaData> schemas = metadataProvider.getSchemas(getConnection(), catalogMetaData.getTableCat(), null);
        for (SchemaMetaData schema : schemas) {
            SchemaTreeItem groupItem = new SchemaTreeItem(getConnection(), metadataProvider, catalogMetaData, schema);
            //noinspection unchecked
            children.add(groupItem);
        }
        setExpanded(true);
    }

    public ContextMenu createContextMenu() {

        ContextMenu contextMenu = new ContextMenu();

        MenuItem open = new MenuItem("打开数据库");
        open.disableProperty().bind(expandedProperty());
        open.setOnAction(event -> {
            //TODO
        });

        MenuItem close = new MenuItem("关闭数据库");
        close.disableProperty().bind(expandedProperty().not());
        close.setOnAction(event -> {
            getChildren().clear();
            setExpanded(false);
        });

        MenuItem create = new MenuItem("新建数据库...");
        MenuItem delete = new MenuItem("删除数据库");
        MenuItem properties = new MenuItem("数据库属性...");

        contextMenu.getItems().addAll(open, close, create, delete, properties);
        return contextMenu;
    }

    @Override
    public ContextMenu getContextMenu() {
        return contextMenu;
    }
}
