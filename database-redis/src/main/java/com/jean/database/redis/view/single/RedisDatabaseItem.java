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
import com.jean.database.redis.model.RedisKey;
import com.jean.database.redis.view.dialog.RedisNewKeyDialogController;
import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jinshubao
 */
public class RedisDatabaseItem extends BaseTreeItem<String> {

    private final int database;
    private final RedisConnectionConfiguration connectionConfiguration;
    private final ContextMenu contextMenu;
    private final RedisObjectTabController objectTabController;

    private RedisDatabaseTabController databaseTabController;

    public RedisDatabaseItem(ViewContext viewContext, int database,
                             RedisConnectionConfiguration connectionConfiguration,
                             RedisObjectTabController objectTabController) {
        super(viewContext, "db" + database);
        this.connectionConfiguration = connectionConfiguration;
        this.database = database;
        this.objectTabController = objectTabController;

        MenuItem addItem = new MenuItem("新增KEY", ImageUtils.createImageView("/image/add.png"));
        addItem.setOnAction(event -> showNewKeyDialog());

        MenuItem refreshItem = new MenuItem("刷新", ImageUtils.createImageView("/image/refresh.png"));
        refreshItem.setOnAction(event -> refresh());

        MenuItem flushItem = new MenuItem("清空数据库 (FLUSHDB)", ImageUtils.createImageView("/image/delete.png"));
        flushItem.setOnAction(event -> flushDatabase());

        contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(addItem, refreshItem, flushItem);

        setGraphic(ImageUtils.createImageView("/redis/db.png"));

        try {
            ControllerContext context = ControllerContext.builder(getViewContext(), RedisDatabaseItem.this.getValue())
                    .attribute(RedisDatabaseTabController.ATTR_OBJECT_TAB_CONTROLLER, objectTabController)
                    .attribute(RedisDatabaseTabController.ATTR_CONNECTION_CONFIGURATION, connectionConfiguration)
                    .attribute(RedisDatabaseTabController.ATTR_DATABASE, database)
                    .build();
            FxmlControllerFactory.LoadResult<RedisDatabaseTabController> result =
                    FxmlControllerFactory.load("fxml/redis-db-tab.fxml", context, RedisDatabaseTabController::new);
            databaseTabController = result.getController();
        } catch (IOException e) {
            DialogUtil.error(e);
        }
    }

    @Override
    public void select() {
    }

    @Override
    public void doubleClick() {
        if (!isOpen()) {
            // 首次打开时，将 Tab 添加到 TabPane
            objectTabController.addDatabaseTab(databaseTabController.getDatabaseTab());
            setOpen(true);
        }
        databaseTabController.selected();
        TaskManger.execute(new RedisKeysTask());
    }

    @Override
    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }

    private class RedisKeysTask extends BaseTask<List<RedisKey>> {

        @Override
        protected void scheduled() {
            super.scheduled();
        }

        @Override
        protected List<RedisKey> call() throws Exception {
            try (StatefulRedisConnection<byte[], byte[]> connection = connectionConfiguration.getConnection()) {
                RedisCommands<byte[], byte[]> commands = connection.sync();
                commands.select(database);
                Long size = commands.dbsize();
                ScanCursor scanCursor = ScanCursor.INITIAL;
                List<RedisKey> value = new ArrayList<>();
                ScanArgs limit = ScanArgs.Builder.limit(RedisConstant.KEY_SCAN_SIZE);
                do {
                    KeyScanCursor<byte[]> cursor = commands.scan(scanCursor, limit);
                    scanCursor = ScanCursor.of(cursor.getCursor());
                    scanCursor.setFinished(cursor.isFinished());
                    List<byte[]> keys = cursor.getKeys();
                    List<RedisKey> collect = keys.stream().map(key -> {
                        String type = commands.type(key);
                        Long ttl = commands.ttl(key);
                        return new RedisKey(connectionConfiguration, database, key, type, ttl, size);
                    }).collect(Collectors.toList());
                    value.addAll(collect);
                    updateProgress(value.size(), size);
                    if (isCancelled()) {
                        break;
                    }
                } while (!scanCursor.isFinished());
                value.sort(Comparator.comparing(o -> StringUtils.byteArrayToString(o.getKey())));
                return value;
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            databaseTabController.updateKeyTableView(getValue());
        }
    }

    /**
     * 显示新增 Key 对话框
     */
    private void showNewKeyDialog() {
        try {
            Callback<Boolean, Void> callback = success -> null;
            ControllerContext context = ControllerContext.builder(getViewContext(), "新增 Key")
                    .attribute(RedisNewKeyDialogController.ATTR_CALLBACK, callback)
                    .build();
            FxmlControllerFactory.LoadResult<RedisNewKeyDialogController> result =
                    FxmlControllerFactory.load("fxml/redis-new-key-dialog.fxml", context, RedisNewKeyDialogController::new);

            Callback<ButtonType, RedisNewKeyDialogController> dialogCallback = buttonType -> {
                if (buttonType == ButtonType.OK) {
                    String key = result.getController().getKey();
                    String type = result.getController().getType();
                    String value = result.getController().getValue();
                    long ttl = result.getController().getTtl();
                    if (key != null && !key.isEmpty()) {
                        TaskManger.execute(new AddKeyTask(key, type, value, ttl));
                    } else {
                        DialogUtil.warning("警告", "Key 不能为空", null);
                    }
                }
                return null;
            };

            DialogUtil.customizeDialog("新增 Key", result.getParent(), dialogCallback);
        } catch (IOException e) {
            DialogUtil.error(e);
        }
    }

    /**
     * 刷新 Key 列表
     */
    public void refresh() {
        TaskManger.execute(new RedisKeysTask());
    }

    /**
     * 清空数据库
     */
    private void flushDatabase() {
        var result = DialogUtil.confirmation("确认", "清空数据库 " + getValue() + "？", "此操作不可恢复！");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            TaskManger.execute(new FlushDbTask());
        }
    }

    /**
     * 新增 Key 任务
     */
    private class AddKeyTask extends BaseTask<Boolean> {

        private final String key;
        private final String type;
        private final String value;
        private final long ttl;

        public AddKeyTask(String key, String type, String value, long ttl) {
            this.key = key;
            this.type = type;
            this.value = value;
            this.ttl = ttl;
        }

        @Override
        protected void scheduled() {
            updateMessage("正在新增 Key...");
        }

        @Override
        protected Boolean call() throws Exception {
            try (StatefulRedisConnection<byte[], byte[]> connection = connectionConfiguration.getConnection()) {
                RedisCommands<byte[], byte[]> commands = connection.sync();
                commands.select(database);

                byte[] keyBytes = key.getBytes(RedisConstant.CHARSET_UTF8);

                switch (type) {
                    case RedisConstant.KeyType.STRING -> commands.set(keyBytes, value.getBytes(RedisConstant.CHARSET_UTF8));
                    case RedisConstant.KeyType.LIST -> commands.rpush(keyBytes, value.getBytes(RedisConstant.CHARSET_UTF8));
                    case RedisConstant.KeyType.SET -> commands.sadd(keyBytes, value.getBytes(RedisConstant.CHARSET_UTF8));
                    case RedisConstant.KeyType.ZSET -> commands.zadd(keyBytes, 0, value.getBytes(RedisConstant.CHARSET_UTF8));
                    case RedisConstant.KeyType.HASH -> commands.hset(keyBytes, "field".getBytes(RedisConstant.CHARSET_UTF8), value.getBytes(RedisConstant.CHARSET_UTF8));
                    default -> throw new RuntimeException("不支持的数据类型: " + type);
                }

                // 设置 TTL
                if (ttl >= 0) {
                    commands.expire(keyBytes, ttl);
                }

                return true;
            } catch (Exception e) {
                throw new RuntimeException("新增 Key 失败: " + e.getMessage(), e);
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            DialogUtil.information("成功", "Key [" + key + "] 创建成功！", null);
            refresh();
        }

        @Override
        protected void failed() {
            super.failed();
            Throwable ex = getException();
            if (ex != null) {
                DialogUtil.error("新增 Key 失败", ex);
            }
        }
    }

    /**
     * 清空数据库任务
     */
    private class FlushDbTask extends BaseTask<Boolean> {

        @Override
        protected void scheduled() {
            updateMessage("正在清空数据库...");
        }

        @Override
        protected Boolean call() throws Exception {
            try (StatefulRedisConnection<byte[], byte[]> connection = connectionConfiguration.getConnection()) {
                RedisCommands<byte[], byte[]> commands = connection.sync();
                commands.select(database);
                String result = commands.flushdb();
                logger.debug("FLUSHDB result: {}", result);
                return true;
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            DialogUtil.information("成功", "数据库 " + RedisDatabaseItem.this.getValue() + " 已清空！", null);
            refresh();
        }

        @Override
        protected void failed() {
            super.failed();
            Throwable ex = getException();
            if (ex != null) {
                DialogUtil.error("清空数据库失败", ex);
            }
        }
    }
}
