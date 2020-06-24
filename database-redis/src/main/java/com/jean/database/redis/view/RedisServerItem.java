package com.jean.database.redis.view;

import com.jean.database.api.BaseTask;
import com.jean.database.api.TaskManger;
import com.jean.database.api.utils.DialogUtil;
import com.jean.database.api.utils.FxmlUtils;
import com.jean.database.api.utils.ImageUtils;
import com.jean.database.api.view.treeitem.BaseTreeItem;
import com.jean.database.redis.RedisConnectionConfiguration;
import com.jean.database.redis.RedisObjectTabController;
import com.jean.database.redis.RedisServerInfoController;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.output.ByteArrayOutput;
import io.lettuce.core.protocol.Command;
import io.lettuce.core.protocol.ProtocolKeyword;
import io.lettuce.core.protocol.RedisCommand;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jinshubao
 */
public class RedisServerItem extends BaseTreeItem<RedisConnectionConfiguration> {
    private final RedisConnectionConfiguration connectionConfiguration;
    private final ContextMenu contextMenu;
    private RedisObjectTabController objectTabController;
    private RedisServerInfoController serverInfoController;

    public RedisServerItem(RedisConnectionConfiguration connectionConfiguration) {
        super(connectionConfiguration, ImageUtils.createImageView("/image/redis/x16/server.png"));
        this.connectionConfiguration = connectionConfiguration;
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
        MenuItem openItem = new MenuItem("打开连接", ImageUtils.createImageView("/image/redis/x16/connect.png"));
        openItem.disableProperty().bind(openProperty());
        openItem.setOnAction(event -> open());

        MenuItem closeItem = new MenuItem("关闭连接", ImageUtils.createImageView("/image/redis/x16/disconnect.png"));
        closeItem.disableProperty().bind(openProperty().not());
        closeItem.setOnAction(event -> {

        });

        MenuItem commandLine = new MenuItem("命令行", ImageUtils.createImageView("/image/redis/x16/delete.png"));
        commandLine.disableProperty().bind(openProperty().not());
        commandLine.setOnAction(event -> {
            TextArea textArea = new TextArea();
            textArea.setFont(Font.font(16.0D));
            textArea.setBackground(new Background(new BackgroundFill(Color.BLANCHEDALMOND, CornerRadii.EMPTY, Insets.EMPTY)));
            textArea.setOnKeyPressed(event1 -> {
                if (event1.getCode() == KeyCode.ENTER) {
                    TaskManger.execute(new RedisCommandTask("PING"));
                }
            });
            objectTabController.addDatabaseTab(new Tab("命令行", textArea));
        });

        MenuItem propertyItem = new MenuItem("连接属性");
        propertyItem.disableProperty().bind(openProperty().not());
        propertyItem.setOnAction(event -> TaskManger.execute(new RedisServerInfoTask()));

        MenuItem deleteItem = new MenuItem("删除连接", ImageUtils.createImageView("/image/redis/x16/delete.png"));
        deleteItem.setOnAction(event -> {
        });
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(openItem, closeItem, commandLine, propertyItem, deleteItem);
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
                Callback<Class<?>, Object> factory = RedisObjectTabController.getFactory(title);
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
            String value = getValue();

            if (serverInfoController == null) {
                Callback<Class<?>, Object> factory = RedisServerInfoController.getControllerFactory(connectionConfiguration);
                try {
                    FxmlUtils.LoadFxmlResult loadFxmlResult = FxmlUtils.loadFxml("/fxml/redis-server-tab.fxml", factory);
                    serverInfoController = (RedisServerInfoController) loadFxmlResult.getController();
                    objectTabController.addDatabaseTab(serverInfoController.getServerInfoTab());
                } catch (IOException e) {
                    DialogUtil.error(e);
                }
            }
            serverInfoController.serverProperties.setText(value);
        }
    }

    private class RedisCommandTask extends BaseTask<byte[]> {

        private final String redisCommand;

        public RedisCommandTask(String redisCommand) {
            this.redisCommand = redisCommand;
        }

        @Override
        protected byte[] call() throws Exception {
            try (StatefulRedisConnection<byte[], byte[]> connection = connectionConfiguration.getConnection()) {
                Command<byte[], byte[], byte[]> command = new Command<>(new StringCommandType(redisCommand), new ByteArrayOutput<>(ByteArrayCodec.INSTANCE));
                RedisCommand<byte[], byte[], byte[]> dispatch = connection.dispatch(command);
                return dispatch.getOutput().get();
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            byte[] value = getValue();
            logger.debug("command result: {}", value);
        }
    }


    static class StringCommandType implements ProtocolKeyword {

        private final byte[] commandTypeBytes;
        private final String commandType;

        StringCommandType(String commandType) {
            this.commandType = commandType;
            this.commandTypeBytes = commandType.getBytes();
        }

        @Override
        public byte[] getBytes() {
            return commandTypeBytes;
        }

        @Override
        public String name() {
            return commandType;
        }

        @Override
        public String toString() {
            return name();
        }
    }
}
