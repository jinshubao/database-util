package com.jean.database.redis.view.cluster;

import com.jean.database.api.BaseTask;
import com.jean.database.api.BaseTreeItem;
import com.jean.database.api.TaskManger;
import com.jean.database.api.ViewContext;
import com.jean.database.api.utils.DialogUtil;
import com.jean.database.api.utils.FxmlUtils;
import com.jean.database.api.utils.ImageUtils;
import com.jean.database.redis.RedisClusterServerInfoController;
import com.jean.database.redis.RedisObjectTabController;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RedisClusterServerItem extends BaseTreeItem<String> {

    private final RedisClusterClient redisClusterClient;
    private final ContextMenu contextMenu;

    private RedisObjectTabController objectTabController;
    private RedisClusterServerInfoController serverInfoController;


    public RedisClusterServerItem(ViewContext viewContext, String value, RedisClusterClient redisClusterClient) {
        super(viewContext, value, ImageUtils.createImageView("/redis/redis.png"));
        this.redisClusterClient = redisClusterClient;
        this.contextMenu = this.createContextMenu();
        try {
            FxmlUtils.LoadFxmlResult loadFxmlResult =
                    FxmlUtils.loadFxml("/fxml/redis-object-tab.fxml", null,
                            new RedisObjectTabController(viewContext, value));
            objectTabController = (RedisObjectTabController) loadFxmlResult.getController();
            getViewContext().addObjectTab(objectTabController.getObjectTab());
            objectTabController.select();
        } catch (IOException e) {
            DialogUtil.error(e);
            return;
        }

        try {
            FxmlUtils.LoadFxmlResult loadFxmlResult =
                    FxmlUtils.loadFxml("/fxml/redis-cluster-server-tab.fxml", null,
                            new RedisClusterServerInfoController(viewContext, redisClusterClient));
            serverInfoController = (RedisClusterServerInfoController) loadFxmlResult.getController();
        } catch (IOException e) {
            DialogUtil.error(e);
        }
    }


    @Override
    public void doubleClick() {
        open();
    }

    @Override
    public void select() {
        objectTabController.select();
    }

    @Override
    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }


    private void open() {
        if (!isOpen()) {
            setExpanded(true);
            setOpen(true);
            getViewContext().addObjectTab(objectTabController.getObjectTab());
            objectTabController.select();
            TaskManger.execute(new OpenServerTask());
        }
    }

    @Override
    public void close() {
        super.close();
        redisClusterClient.shutdown();
    }

    private ContextMenu createContextMenu() {
        MenuItem openItem = new MenuItem("打开连接", ImageUtils.createImageView("/image/connect.png"));
        openItem.disableProperty().bind(openProperty());
        openItem.setOnAction(event -> open());

        MenuItem closeItem = new MenuItem("关闭连接", ImageUtils.createImageView("/image/disconnect.png"));
        closeItem.disableProperty().bind(openProperty().not());
        closeItem.setOnAction(event -> {

        });

        MenuItem commandLine = new MenuItem("命令行", ImageUtils.createImageView("/image/delete.png"));
        commandLine.disableProperty().bind(openProperty().not());
        commandLine.setOnAction(event -> {

        });

        MenuItem propertyItem = new MenuItem("连接属性");
        propertyItem.disableProperty().bind(openProperty().not());
        propertyItem.setOnAction(event -> {

        });

        MenuItem deleteItem = new MenuItem("删除连接", ImageUtils.createImageView("/image/delete.png"));
        deleteItem.setOnAction(event -> {
        });
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(openItem, closeItem, commandLine, propertyItem, deleteItem);
        return contextMenu;
    }


    private class OpenServerTask extends BaseTask<List<Integer>> {

        private final RedisClusterServerItem redisServerItem = RedisClusterServerItem.this;

        @Override
        protected List<Integer> call() throws Exception {
            try (StatefulRedisClusterConnection<String, String> connection = redisClusterClient.connect()) {
                RedisAdvancedClusterCommands<String, String> clusterCommands = connection.sync();

                List<Object> slots = clusterCommands.clusterSlots();
                List<Integer> list = new ArrayList<>();
                return list;
            }
        }

        @Override
        @SuppressWarnings({"rawtypes", "unchecked"})
        protected void succeeded() {
            super.succeeded();
            List<Integer> number = getValue();
            for (Integer index : number) {
                TreeItem databaseItem = new RedisClusterDatabaseItem(getViewContext(), index, redisClusterClient, objectTabController);
                redisServerItem.getChildren().add(databaseItem);
            }
        }
    }
}
