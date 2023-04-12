package com.jean.database.redis.view.single;

import com.jean.database.context.ApplicationContext;
import com.jean.database.task.BaseTask;
import com.jean.database.item.BaseTreeItem;
import com.jean.database.task.TaskManger;
import com.jean.database.context.ViewContext;
import com.jean.database.utils.DialogUtil;
import com.jean.database.utils.FxmlUtils;
import com.jean.database.utils.ImageUtils;
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

    public RedisServerItem(ApplicationContext context, RedisConnectionConfiguration connectionConfiguration) {
        super(context, connectionConfiguration, ImageUtils.createImageView("/redis/redis.png"));
        this.connectionConfiguration = connectionConfiguration;
        this.contextMenu = createContextMenu();
        try {
            String title = connectionConfiguration.getConnectionName();
            FxmlUtils.LoadFxmlResult loadFxmlResult = FxmlUtils.loadFxml("fxml/redis-object-tab.fxml", null,
                    new RedisObjectTabController(getContext(), title));
            objectTabController = (RedisObjectTabController) loadFxmlResult.getController();
            context.getRootContext().addObjectTab(objectTabController.getObjectTab());
        } catch (IOException e) {
            DialogUtil.error(e);
            return;
        }

        try {
            FxmlUtils.LoadFxmlResult loadFxmlResult = FxmlUtils.loadFxml("fxml/redis-server-tab.fxml", null,
                    new RedisServerInfoController(context, connectionConfiguration));
            serverInfoController = (RedisServerInfoController) loadFxmlResult.getController();
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
            getContext().getRootContext().addObjectTab(objectTabController.getObjectTab());
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
        closeItem.setOnAction(event -> {

        });

        MenuItem commandLine = new MenuItem("命令行", ImageUtils.createImageView("/image/delete.png"));
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
        propertyItem.setOnAction(event -> {
            objectTabController.addDatabaseTab(serverInfoController.getServerInfoTab());
            TaskManger.execute(new RedisServerInfoTask());
            serverInfoController.startTimerTask();
        });

        MenuItem deleteItem = new MenuItem("删除连接", ImageUtils.createImageView("/image/delete.png"));
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
            List<Integer> number = getValue();
            for (Integer index : number) {
                TreeItem databaseItem = new RedisDatabaseItem(getContext(), index, connectionConfiguration, objectTabController);
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
