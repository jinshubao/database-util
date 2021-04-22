package com.jean.database.redis.view.cluster;


import com.jean.database.api.BaseTask;
import com.jean.database.api.BaseTreeItem;
import com.jean.database.api.TaskManger;
import com.jean.database.api.ViewContext;
import com.jean.database.api.utils.DialogUtil;
import com.jean.database.api.utils.FxmlUtils;
import com.jean.database.api.utils.ImageUtils;
import com.jean.database.api.utils.StringUtils;
import com.jean.database.redis.RedisConstant;
import com.jean.database.redis.RedisDatabaseTabController;
import com.jean.database.redis.RedisObjectTabController;
import com.jean.database.redis.model.RedisKey;
import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jinshubao
 */
public class RedisClusterDatabaseItem extends BaseTreeItem<String> {

    private final int database;
    private final RedisClusterClient redisClusterClient;
    private final ContextMenu contextMenu;
    private final RedisObjectTabController objectTabController;

    private RedisDatabaseTabController databaseTabController;

    public RedisClusterDatabaseItem(ViewContext viewContext, int database,
                                    RedisClusterClient redisClusterClient,
                                    RedisObjectTabController objectTabController) {
        super(viewContext, "db" + database);
        this.redisClusterClient = redisClusterClient;
        this.database = database;
        this.objectTabController = objectTabController;

        MenuItem refreshItem = new MenuItem("刷新", ImageUtils.createImageView("/image/refresh.png"));
        refreshItem.setOnAction(event -> {
        });

        MenuItem flushItem = new MenuItem("清空", ImageUtils.createImageView("/image/delete.png"));
        flushItem.setOnAction(event -> {
        });

        contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(refreshItem, flushItem);

        setGraphic(ImageUtils.createImageView("/redis/db.png"));
    }


    @Override
    public void select() {
    }

    @Override
    public void doubleClick() {
        if (!isOpen()) {
            try {
                FxmlUtils.LoadFxmlResult loadFxmlResult =
                        FxmlUtils.loadFxml("/fxml/redis-db-tab.fxml", null,
                                new RedisDatabaseTabController(getViewContext(), RedisClusterDatabaseItem.this.getValue(), objectTabController));
                databaseTabController = (RedisDatabaseTabController) loadFxmlResult.getController();
                setOpen(true);
            } catch (IOException e) {
                DialogUtil.error(e);
            }
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
            try (StatefulRedisClusterConnection<String, String> connection = redisClusterClient.connect()) {
                RedisAdvancedClusterCommands<String, String> commands = connection.sync();
                Long size = commands.dbsize();
                ScanCursor scanCursor = ScanCursor.INITIAL;
                List<RedisKey> value = new ArrayList<>();
                ScanArgs limit = ScanArgs.Builder.limit(RedisConstant.KEY_SCAN_SIZE);
                do {
                    KeyScanCursor<String> cursor = commands.scan(scanCursor, limit);
                    scanCursor = ScanCursor.of(cursor.getCursor());
                    scanCursor.setFinished(cursor.isFinished());
                    List<String> keys = cursor.getKeys();
                    List<RedisKey> collect = keys.stream().map(key -> {
                        String type = commands.type(key);
                        Long ttl = commands.ttl(key);
                        return new RedisKey(null, database, null, type, ttl, size);
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
}
