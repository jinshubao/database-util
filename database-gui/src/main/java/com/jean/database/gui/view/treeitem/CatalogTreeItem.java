package com.jean.database.gui.view.treeitem;

import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.gui.handler.CatalogActionEventHandler;
import com.jean.database.gui.view.IContextMenu;
import com.jean.database.gui.view.IDoubleClick;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;

/**
 * @author jinshubao
 */
public class CatalogTreeItem extends TreeItem<CatalogMetaData> implements IContextMenu, IDoubleClick {

    private final BooleanProperty open = new SimpleBooleanProperty(this, "open", false);

    private final ContextMenu contextMenu;

    private final CatalogMetaData catalogMetaData;

    private final CatalogActionEventHandler eventHandler;


    public CatalogTreeItem(CatalogMetaData catalogMetaData, CatalogActionEventHandler eventHandler) {
        super(catalogMetaData);
        this.catalogMetaData = catalogMetaData;
        this.eventHandler = eventHandler;
        this.contextMenu = this.createContextMenu();
    }

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem open = new MenuItem("打开数据库");
        open.disableProperty().bind(this.open);
        open.setOnAction(event -> eventHandler.openCatalog(CatalogTreeItem.this));

        MenuItem close = new MenuItem("关闭数据库");
        close.disableProperty().bind(this.open.not());
        close.setOnAction(event -> eventHandler.closeCatalog(CatalogTreeItem.this));

        MenuItem create = new MenuItem("新建数据库...");
        create.setOnAction(event -> eventHandler.createCatalog(CatalogTreeItem.this));

        MenuItem delete = new MenuItem("删除数据库");
        delete.setOnAction(event -> eventHandler.deleteCatalog(CatalogTreeItem.this));

        MenuItem properties = new MenuItem("数据库属性...");
        properties.setOnAction(event -> eventHandler.catalogProperties(CatalogTreeItem.this));

        MenuItem refresh = new MenuItem("刷新");
        refresh.setOnAction(event -> eventHandler.refresh(CatalogTreeItem.this));

        contextMenu.getItems().addAll(open, close, create, delete, properties, refresh);
        return contextMenu;
    }

    @Override
    public void doubleClick(MouseEvent event) {
        eventHandler.openCatalog(this);
    }

    @Override
    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    public CatalogMetaData getCatalogMetaData() {
        return catalogMetaData;
    }

    public boolean getOpen() {
        return open.get();
    }

    public void setOpen(boolean open) {
        this.open.set(open);
    }
}
