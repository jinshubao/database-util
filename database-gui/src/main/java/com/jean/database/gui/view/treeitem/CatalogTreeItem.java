package com.jean.database.gui.view.treeitem;

import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.gui.view.action.ISelectAction;
import com.jean.database.gui.constant.Images;
import com.jean.database.gui.handler.ICatalogItemActionEventHandler;
import com.jean.database.gui.view.action.IContextMenu;
import com.jean.database.gui.view.action.IMouseClickAction;
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
public class CatalogTreeItem extends TreeItem<CatalogMetaData> implements IContextMenu, IMouseClickAction, ISelectAction {

    private final BooleanProperty open = new SimpleBooleanProperty(this, "onOpen", false);

    private final ContextMenu contextMenu;

    private final CatalogMetaData catalogMetaData;

    private final ICatalogItemActionEventHandler catalogItemActionEventHandler;


    public CatalogTreeItem(CatalogMetaData catalogMetaData, ICatalogItemActionEventHandler catalogItemActionEventHandler) {
        super(catalogMetaData);
        this.catalogMetaData = catalogMetaData;
        this.catalogItemActionEventHandler = catalogItemActionEventHandler;
        this.contextMenu = this.createContextMenu();
        setGraphic(new ImageView(new Image(getClass().getResourceAsStream(Images.DATABASE_IMAGE))));
    }

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem open = new MenuItem("打开数据库");
        open.disableProperty().bind(this.open);
        open.setOnAction(event -> catalogItemActionEventHandler.onOpen(CatalogTreeItem.this));

        MenuItem close = new MenuItem("关闭数据库");
        close.disableProperty().bind(this.open.not());
        close.setOnAction(event -> catalogItemActionEventHandler.onClose(CatalogTreeItem.this));

        MenuItem create = new MenuItem("新建数据库...");
        create.setOnAction(event -> catalogItemActionEventHandler.onCreate(CatalogTreeItem.this));

        MenuItem delete = new MenuItem("删除数据库", new ImageView(new Image(getClass().getResourceAsStream(Images.DELETE_IMAGE))));
        delete.setOnAction(event -> catalogItemActionEventHandler.onDelete(CatalogTreeItem.this));

        MenuItem properties = new MenuItem("数据库属性...");
        properties.setOnAction(event -> catalogItemActionEventHandler.onDetails(CatalogTreeItem.this));

        MenuItem refresh = new MenuItem("刷新", new ImageView(new Image(getClass().getResourceAsStream(Images.REFRESH_IMAGE))));
        refresh.setOnAction(event -> catalogItemActionEventHandler.refresh(CatalogTreeItem.this));

        contextMenu.getItems().addAll(open, close, create, delete, properties, refresh);
        return contextMenu;
    }

    @Override
    public void click(MouseEvent event) {
        if (event.getClickCount() == 1) {
            catalogItemActionEventHandler.onMouseClick(this);
        } else if (event.getClickCount() == 2) {
            catalogItemActionEventHandler.onMouseDoubleClick(this);
        }
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

    @Override
    public void selected() {
        catalogItemActionEventHandler.onSelected(this);
    }
}
