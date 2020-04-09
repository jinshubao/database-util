package com.jean.database.gui.view.treeitem;

import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.TableMetaData;
import com.jean.database.gui.constant.Images;
import com.jean.database.gui.factory.LoggerWrapper;
import com.jean.database.gui.view.action.IContextMenu;
import com.jean.database.gui.view.action.IMouseAction;
import com.jean.database.gui.view.handler.IMouseEventHandler;
import com.jean.database.gui.view.handler.ITableItemActionEventHandler;
import com.jean.database.gui.view.handler.impl.TableItemActionEventHandlerImpl;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.lang.ref.WeakReference;

/**
 * @author jinshubao
 */
public class TableTreeItem extends BaseTreeItem<TableMetaData> implements IContextMenu, IMouseAction {

    private final ContextMenu contextMenu;
    private final ITableItemActionEventHandler tableItemActionEventHandler;

    private WeakReference<Tab> tabRef;

    public TableTreeItem(TableMetaData value, IConnectionConfiguration connectionConfiguration, IMetadataProvider metadataProvider) {
        super(value, connectionConfiguration, metadataProvider);
        this.contextMenu = this.createContextMenu();
        this.tableItemActionEventHandler = LoggerWrapper.warp(new TableItemActionEventHandlerImpl());
        this.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(Images.TABLE_IMAGE))));
    }


    @Override
    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    @Override
    public IMouseEventHandler getMouseEventHandler() {
        return this.tableItemActionEventHandler;
    }

    @Override
    public void close() {
        tableItemActionEventHandler.onClose(this);
    }

    public Tab getTabRef() {
        if (tabRef != null) {
            return tabRef.get();
        }
        return null;
    }

    public void setTabRef(Tab tabRef) {
        this.tabRef = new WeakReference<>(tabRef);
    }

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem open = new MenuItem("打开表");
        open.disableProperty().bind(openProperty());
        open.setOnAction(event -> this.tableItemActionEventHandler.onOpen(TableTreeItem.this));

        MenuItem copy = new MenuItem("复制表");
        copy.setOnAction(event -> this.tableItemActionEventHandler.onCopy(TableTreeItem.this));

        MenuItem delete = new MenuItem("删除表", new ImageView(new Image(getClass().getResourceAsStream(Images.DELETE_IMAGE))));
        delete.setOnAction(event -> this.tableItemActionEventHandler.onDelete(TableTreeItem.this));

        MenuItem refresh = new MenuItem("刷新", new ImageView(new Image(getClass().getResourceAsStream(Images.REFRESH_IMAGE))));
        refresh.disableProperty().bind(openProperty().not());
        refresh.setOnAction(event -> this.tableItemActionEventHandler.onRefresh(TableTreeItem.this));

        contextMenu.getItems().addAll(open, copy, delete, refresh);
        return contextMenu;
    }
}
