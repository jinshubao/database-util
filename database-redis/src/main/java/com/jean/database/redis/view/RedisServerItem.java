package com.jean.database.redis.view;

import com.jean.database.api.view.ViewContext;
import com.jean.database.api.view.treeitem.BaseTreeItem;
import com.jean.database.redis.IRedisMetadataProvider;
import com.jean.database.redis.RedisConnectionConfiguration;
import com.jean.database.redis.view.handler.IRedisServerItemActionEventHandler;
import com.jean.database.redis.view.handler.impl.RedisServerItemActionEventHandlerImpl;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * @author jinshubao
 */
public class RedisServerItem extends BaseTreeItem<String> {
    private final RedisConnectionConfiguration connectionConfiguration;
    private final IRedisMetadataProvider metadataProvider;
    private final ContextMenu contextMenu;
    private final IRedisServerItemActionEventHandler redisServerItemActionEventHandler;

    public RedisServerItem(ViewContext viewContext, RedisConnectionConfiguration connectionConfiguration, IRedisMetadataProvider metadataProvider) {
        super(connectionConfiguration.getConnectionName(), viewContext);
        this.connectionConfiguration = connectionConfiguration;
        this.metadataProvider = metadataProvider;
        this.redisServerItemActionEventHandler = new RedisServerItemActionEventHandlerImpl(connectionConfiguration, metadataProvider);
        this.contextMenu = createContextMenu();
    }

    @Override
    public void click() {
        redisServerItemActionEventHandler.onClick(this);
    }

    @Override
    public void doubleClick() {
        redisServerItemActionEventHandler.onDoubleClick(this);
    }

    @Override
    public void select() {
        redisServerItemActionEventHandler.onSelected(this);
    }

    @Override
    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }


    private ContextMenu createContextMenu() {
        MenuItem openItem = new MenuItem("打开连接");
        openItem.disableProperty().bind(openProperty());
        openItem.setOnAction(event -> this.redisServerItemActionEventHandler.open(this));

        MenuItem closeItem = new MenuItem("关闭连接");
        closeItem.disableProperty().bind(openProperty().not());
        closeItem.setOnAction(event -> this.redisServerItemActionEventHandler.close(this));

        MenuItem propertyItem = new MenuItem("连接属性");
        propertyItem.disableProperty().bind(openProperty().not());
        propertyItem.setOnAction(event -> this.redisServerItemActionEventHandler.property(this));

        MenuItem deleteItem = new MenuItem("删除连接");
        deleteItem.setOnAction(event -> this.redisServerItemActionEventHandler.delete(this));
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(openItem, closeItem, propertyItem, deleteItem);
        return contextMenu;
    }

}
