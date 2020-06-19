package com.jean.database.redis.view;


import com.jean.database.api.view.ViewContext;
import com.jean.database.api.view.treeitem.BaseTreeItem;
import com.jean.database.redis.IRedisMetadataProvider;
import com.jean.database.redis.RedisConnectionConfiguration;
import com.jean.database.redis.view.handler.IRedisDatabaseItemActionEventHandler;
import com.jean.database.redis.view.handler.impl.RedisDatabaseItemActionEventHandlerImpl;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * @author jinshubao
 */
public class RedisDatabaseItem extends BaseTreeItem<String> {

    private final int database;
    private final RedisConnectionConfiguration connectionConfiguration;
    private final IRedisMetadataProvider metadataProvider;
    private final ContextMenu contextMenu;
    private final IRedisDatabaseItemActionEventHandler databaseItemActionEventHandler;

    public RedisDatabaseItem(int database, ViewContext viewContext, RedisConnectionConfiguration connectionConfiguration, IRedisMetadataProvider metadataProvider) {
        super("db" + database, viewContext);
        this.connectionConfiguration = connectionConfiguration;
        this.metadataProvider = metadataProvider;
        this.database = database;
        this.databaseItemActionEventHandler = new RedisDatabaseItemActionEventHandlerImpl(database, connectionConfiguration, metadataProvider);

        MenuItem refreshItem = new MenuItem("刷新");
        refreshItem.setOnAction(event -> databaseItemActionEventHandler.refresh(this));

        MenuItem flushItem = new MenuItem("清空");
        flushItem.setOnAction(event -> databaseItemActionEventHandler.flush(this));

        contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(refreshItem, flushItem);
    }

    @Override
    public void select() {
        databaseItemActionEventHandler.onSelected(this);
    }

    @Override
    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }

    public int getDatabase() {
        return database;
    }

}
