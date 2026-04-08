package com.jean.database.redis.view.single;

import com.jean.database.api.BaseTask;
import com.jean.database.api.BaseTreeItem;
import com.jean.database.api.ControllerContext;
import com.jean.database.api.FxmlControllerFactory;
import com.jean.database.api.TaskManger;
import com.jean.database.api.ViewContext;
import com.jean.database.api.utils.DialogUtil;
import com.jean.database.api.utils.ImageUtils;
import com.jean.database.api.utils.StringUtils;
import com.jean.database.redis.RedisConnectionConfiguration;
import com.jean.database.redis.RedisConstant;
import com.jean.database.redis.RedisDatabaseTabController;
import com.jean.database.redis.RedisObjectTabController;
import com.jean.database.redis.RedisServerInfoController;
import com.jean.database.redis.model.RedisKey;
import com.jean.database.redis.view.console.RedisConsoleController;
import com.jean.database.redis.view.dialog.RedisNewKeyDialogController;
import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author jinshubao
 */
public class RedisServerItem extends BaseTreeItem<RedisConnectionConfiguration> {
    private final RedisConnectionConfiguration connectionConfiguration;
    private final ContextMenu contextMenu;
    private RedisObjectTabController objectTabController;
    private RedisServerInfoController serverInfoController;

    public RedisServerItem(ViewContext viewContext, RedisConnectionConfiguration connectionConfiguration) {
        super(viewContext, connectionConfiguration, ImageUtils.createImageView("/redis/redis.png"));
        this.connectionConfiguration = connectionConfiguration;
        this.contextMenu = createContextMenu();
        try {
            String title = connectionConfiguration.getConnectionName();
            ControllerContext objectContext = ControllerContext.builder(getViewContext(), title).build();
            FxmlControllerFactory.LoadResult<RedisObjectTabController> objectResult =
                    FxmlControllerFactory.load("fxml/redis-object-tab.fxml", objectContext, RedisObjectTabController::new);
            objectTabController = objectResult.getController();
            getViewContext().addObjectTab(objectTabController.getObjectTab());
            objectTabController.select();
        } catch (IOException e) {
            DialogUtil.error(e);
            return;
        }

        try {
            ControllerContext serverContext = ControllerContext.builder(getViewContext(), "Server Info")
                    .attribute(RedisServerInfoController.ATTR_CONNECTION_CONFIGURATION, connectionConfiguration)
                    .build();
            FxmlControllerFactory.LoadResult<RedisServerInfoController> serverResult =
                    FxmlControllerFactory.load("fxml/redis-server-tab.fxml", serverContext, RedisServerInfoController::new);
            serverInfoController = serverResult.getController();
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

    private ContextMenu createContextMenu() {
        MenuItem openItem = new MenuItem("打开连接", ImageUtils.createImageView("/image/connect.png"));
        openItem.disableProperty().bind(openProperty());
        openItem.setOnAction(event -> open());

        MenuItem closeItem = new MenuItem("关闭连接", ImageUtils.createImageView("/image/disconnect.png"));
        closeItem.disableProperty().bind(openProperty().not());
        closeItem.setOnAction(event -> closeConnection());

        MenuItem commandLine = new MenuItem("命令行", ImageUtils.createImageView("/image/delete.png"));
        commandLine.disableProperty().bind(openProperty().not());
        commandLine.setOnAction(event -> openConsole());

        MenuItem propertyItem = new MenuItem("连接属性");
        propertyItem.disableProperty().bind(openProperty().not());
        propertyItem.setOnAction(event -> {
            objectTabController.addDatabaseTab(serverInfoController.getServerInfoTab());
            TaskManger.execute(new RedisServerInfoTask());
            serverInfoController.startTimerTask();
        });

        MenuItem deleteItem = new MenuItem("删除连接", ImageUtils.createImageView("/image/delete.png"));
        deleteItem.setOnAction(event -> deleteConnection());
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(openItem, closeItem, commandLine, propertyItem, deleteItem);
        return contextMenu;
    }

    /**
     * 关闭连接
     */
    private void closeConnection() {
        if (isOpen()) {
            // 关闭所有数据库连接
            connectionConfiguration.close();
            // 清空子节点
            getChildren().clear();
            // 设置为未打开状态
            setOpen(false);
            DialogUtil.information("成功", "连接已关闭", null);
        }
    }

    /**
     * 删除连接
     */
    private void deleteConnection() {
        var result = DialogUtil.confirmation("确认", "删除连接 [" + connectionConfiguration.getConnectionName() + "]？", null);
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // 先关闭连接
            if (isOpen()) {
                connectionConfiguration.close();
            }
            // 从树中移除
            TreeItem parent = getParent();
            if (parent != null) {
                parent.getChildren().remove(this);
            }
            DialogUtil.information("成功", "连接已删除", null);
        }
    }

    /**
     * 打开 Redis Console
     */
    private void openConsole() {
        try {
            // 默认打开 db0
            int db = 0;
            ControllerContext context = ControllerContext.builder(getViewContext(), "Console - db" + db)
                    .attribute(RedisConsoleController.ATTR_CONNECTION_CONFIGURATION, connectionConfiguration)
                    .attribute(RedisConsoleController.ATTR_DATABASE, db)
                    .build();
            FxmlControllerFactory.LoadResult<RedisConsoleController> result =
                    FxmlControllerFactory.load("fxml/redis-console-tab.fxml", context, RedisConsoleController::new);
            Tab consoleTab = new Tab("Console - db" + db);
            consoleTab.setContent(result.getParent());
            objectTabController.addDatabaseTab(consoleTab);
        } catch (IOException e) {
            DialogUtil.error(e);
        }
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
            List<Integer> number = getValue();
            for (Integer index : number) {
                TreeItem databaseItem = new RedisDatabaseItem(getViewContext(), index, connectionConfiguration, objectTabController);
                redisServerItem.getChildren().add(databaseItem);
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
            serverInfoController.serverProperties.setText(getValue());
        }
    }
}
