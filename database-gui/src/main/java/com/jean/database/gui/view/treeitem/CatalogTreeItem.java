package com.jean.database.gui.view.treeitem;

import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.gui.constant.Images;
import com.jean.database.gui.handler.ICatalogItemActionEventHandler;
import com.jean.database.gui.view.IContextMenu;
import com.jean.database.gui.view.IDoubleClick;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * @author jinshubao
 */
public class CatalogTreeItem extends TreeItem<CatalogMetaData> implements IContextMenu, IDoubleClick {

    private final BooleanProperty open = new SimpleBooleanProperty(this, "open", false);

    private final ContextMenu contextMenu;

    private final CatalogMetaData catalogMetaData;

    private final ICatalogItemActionEventHandler eventHandler;


    public CatalogTreeItem(CatalogMetaData catalogMetaData, ICatalogItemActionEventHandler eventHandler) {
        super(catalogMetaData);
        this.catalogMetaData = catalogMetaData;
        this.eventHandler = eventHandler;
        this.contextMenu = this.createContextMenu();
        setGraphic(new ImageView(new Image(getClass().getResourceAsStream(Images.DATABASE_IMAGE))));
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

        MenuItem delete = new MenuItem("删除数据库", new ImageView(new Image(getClass().getResourceAsStream(Images.DELETE_IMAGE))));
        delete.setOnAction(event -> eventHandler.deleteCatalog(CatalogTreeItem.this));

        MenuItem properties = new MenuItem("数据库属性...");
        properties.setOnAction(event -> eventHandler.catalogProperties(CatalogTreeItem.this));

        MenuItem refresh = new MenuItem("刷新", new ImageView(new Image(getClass().getResourceAsStream(Images.REFRESH_IMAGE))));
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
