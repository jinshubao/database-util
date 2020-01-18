package com.jean.database.client.view.treeitem;

import com.jean.database.client.view.IContextMenu;
import com.jean.database.client.view.IDoubleClick;
import com.jean.database.client.view.IRefresh;
import com.jean.database.client.view.ISelecte;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.CatalogMetaData;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.util.List;

/**
 * @author jinshubao
 */
public class ServerTreeItem extends AbstractTreeItem implements IContextMenu, ISelecte, IRefresh, IDoubleClick {

    private final ContextMenu contextMenu;

    public ServerTreeItem(Connection connection, IMetadataProvider metadataProvider, String value) {
        super(connection, metadataProvider, value);
        this.contextMenu = this.createContextMenu();
    }

    @Override
    public void refresh() {
        try {
            ObservableList children = getChildren();
            IMetadataProvider metadataProvider = getMetadataProvider();
            List<CatalogMetaData> catalogs = metadataProvider.getCatalogs(getConnection());
            for (CatalogMetaData metaData : catalogs) {
                CatalogTreeItem item = new CatalogTreeItem(getConnection(), metadataProvider, metaData);
                //noinspection unchecked
                children.add(item);
            }
            setExpanded(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem open = new MenuItem("打开连接");
        open.disableProperty().bind(expandedProperty());
        open.setOnAction(event -> refresh());
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

    @Override
    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }

}
