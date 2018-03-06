package com.jean.database.client.view.treeitem;

import com.jean.database.core.connection.IConnectionConfiguration;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.core.provider.IMetadataProvider;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.util.List;

/**
 * @author jinshubao
 */
public class ConnectionTreeItem extends AbstractTreeItem {

    public ConnectionTreeItem(IConnectionConfiguration value) {
        super(value, value);
    }

    @Override
    public void refreshData() {
        try {
            ObservableList children = getChildren();
            children.clear();
            IMetadataProvider provider = getMetadataProvider();
            IConnectionConfiguration configuration = getConnectionConfiguration();
            List<CatalogMetaData> catalogs = provider.getCatalogs(configuration);
            for (CatalogMetaData metaData : catalogs) {
                CatalogTreeItem item = new CatalogTreeItem(configuration, metaData);
                //noinspection unchecked
                children.add(item);
            }
            setExpanded(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem open = new MenuItem("打开连接");
        open.disableProperty().bind(expandedProperty());
        open.setOnAction(event -> refreshData());

        MenuItem close = new MenuItem("关闭连接");
        close.disableProperty().bind(expandedProperty().not());
        close.setOnAction(event -> {
            getChildren().clear();
            setExpanded(false);
        });

        MenuItem delete = new MenuItem("删除连接");
        delete.setOnAction(event -> getParent().getChildren().remove(this));

        MenuItem copy = new MenuItem("复制连接...");
        MenuItem properties = new MenuItem("连接属性...");

        contextMenu.getItems().addAll(open, close, copy, delete, properties);
        return contextMenu;
    }
}
