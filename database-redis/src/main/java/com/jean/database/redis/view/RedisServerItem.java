package com.jean.database.redis.view;

import com.jean.database.api.BaseTask;
import com.jean.database.api.TaskManger;
import com.jean.database.api.ViewContext;
import com.jean.database.api.utils.DialogUtil;
import com.jean.database.api.utils.FxmlUtils;
import com.jean.database.api.view.treeitem.BaseTreeItem;
import com.jean.database.redis.RedisConnectionConfiguration;
import com.jean.database.redis.RedisObjectTabController;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jinshubao
 */
public class RedisServerItem extends BaseTreeItem<RedisConnectionConfiguration> {
    private final RedisConnectionConfiguration connectionConfiguration;
    private final ViewContext viewContext;
    private final ContextMenu contextMenu;
    private RedisObjectTabController objectTabController;

    public RedisServerItem(RedisConnectionConfiguration connectionConfiguration, ViewContext viewContext) {
        super(connectionConfiguration);
        this.connectionConfiguration = connectionConfiguration;
        this.viewContext = viewContext;
        this.contextMenu = createContextMenu();
    }

    @Override
    public void doubleClick() {
        open();
    }

    @Override
    public void select() {
        if (objectTabController != null) {
            objectTabController.selected();
        }
    }

    @Override
    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }


    private void open() {
        if (!isOpen()) {
            TaskManger.execute(new OpenServerTask());
        }
    }

    private ContextMenu createContextMenu() {
        MenuItem openItem = new MenuItem("打开连接");
        openItem.disableProperty().bind(openProperty());
        openItem.setOnAction(event -> open());

        MenuItem closeItem = new MenuItem("关闭连接");
        closeItem.disableProperty().bind(openProperty().not());
        closeItem.setOnAction(event -> {
        });

        MenuItem propertyItem = new MenuItem("连接属性");
        propertyItem.disableProperty().bind(openProperty().not());
        propertyItem.setOnAction(event -> TaskManger.execute(new RedisServerInfoTask()));

        MenuItem deleteItem = new MenuItem("删除连接");
        deleteItem.setOnAction(event -> {
        });
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(openItem, closeItem, propertyItem, deleteItem);
        return contextMenu;
    }


    private class OpenServerTask extends BaseTask<List<Integer>> {

        private final RedisServerItem redisServerItem = RedisServerItem.this;

        @Override
        protected List<Integer> call() throws Exception {
            try (StatefulRedisConnection<byte[], byte[]> connection = connectionConfiguration.getConnection()) {
                RedisCommands<byte[], byte[]> commands = connection.sync();
                List<Integer> list = new ArrayList<>();
                try {
                    for (int i = 0; i < Integer.MAX_VALUE; i++) {
                        commands.select(i);
                        list.add(i);
                    }
                } catch (Throwable ignored) {
                }
                return list;
            }
        }

        @Override
        @SuppressWarnings({"rawtypes", "unchecked"})
        protected void succeeded() {
            super.succeeded();
            try {
                String title = connectionConfiguration.getConnectionName();
                Callback<Class<?>, Object> factory = RedisObjectTabController.getFactory(title, viewContext);
                FxmlUtils.LoadFxmlResult loadFxmlResult = FxmlUtils.loadFxml("/fxml/redis-object-tab.fxml", factory);
                objectTabController = (RedisObjectTabController) loadFxmlResult.getController();
                List<Integer> number = getValue();
                for (Integer index : number) {
                    TreeItem databaseItem = new RedisDatabaseItem(index, connectionConfiguration, objectTabController);
                    redisServerItem.getChildren().add(databaseItem);
                }
                redisServerItem.setExpanded(true);
                redisServerItem.setOpen(true);
            } catch (Exception e) {
                DialogUtil.error(e);
            }
        }
    }


    private class RedisServerInfoTask extends BaseTask<String> {

        @Override
        protected String call() throws Exception {
            updateProgress(0, 1);
            try (StatefulRedisConnection<byte[], byte[]> connection = connectionConfiguration.getConnection()) {
                String info = connection.sync().info();
                updateProgress(1, 1);
                return info;
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            DialogUtil.information("Redis server properties", "", getValue());
        }
    }

}
