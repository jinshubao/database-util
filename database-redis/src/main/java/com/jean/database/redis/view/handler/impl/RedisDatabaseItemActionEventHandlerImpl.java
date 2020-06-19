package com.jean.database.redis.view.handler.impl;

import com.jean.database.api.BaseTask;
import com.jean.database.api.TaskManger;
import com.jean.database.api.utils.StringUtils;
import com.jean.database.redis.IRedisMetadataProvider;
import com.jean.database.redis.RedisConnectionConfiguration;
import com.jean.database.redis.model.RedisKey;
import com.jean.database.redis.view.RedisDatabaseItem;
import com.jean.database.redis.view.handler.IRedisDatabaseItemActionEventHandler;
import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RedisDatabaseItemActionEventHandlerImpl implements IRedisDatabaseItemActionEventHandler {

    static final int KEY_SCAN_SIZE = 1000;


    private final int database;
    private final RedisConnectionConfiguration connectionConfiguration;
    private final IRedisMetadataProvider metadataProvider;

    public RedisDatabaseItemActionEventHandlerImpl(int database, RedisConnectionConfiguration connectionConfiguration, IRedisMetadataProvider metadataProvider) {
        this.database = database;
        this.connectionConfiguration = connectionConfiguration;
        this.metadataProvider = metadataProvider;
    }

    @Override
    public void refresh() {

    }

    @Override
    public void onClick(RedisDatabaseItem redisDatabaseItem) {

    }

    @Override
    public void onDoubleClick(RedisDatabaseItem redisDatabaseItem) {

    }

    @Override
    public void onSelected(RedisDatabaseItem redisDatabaseItem) {
        TaskManger.execute(new RedisKeysTask(redisDatabaseItem));
    }

    @Override
    public void onCreate(RedisDatabaseItem redisDatabaseItem) {

    }

    @Override
    public void onOpen(RedisDatabaseItem redisDatabaseItem) {

    }

    @Override
    public void onClose(RedisDatabaseItem redisDatabaseItem) {

    }

    @Override
    public void onDelete(RedisDatabaseItem redisDatabaseItem) {

    }

    @Override
    public void onDetails(RedisDatabaseItem redisDatabaseItem) {

    }

    @Override
    public void flush(RedisDatabaseItem redisDatabaseItem) {

    }

    @Override
    public void refresh(RedisDatabaseItem redisDatabaseItem) {

    }

    private class RedisKeysTask extends BaseTask<List<RedisKey>> {

        private final RedisDatabaseItem redisDatabaseItem;

        private RedisKeysTask(RedisDatabaseItem redisDatabaseItem) {
            this.redisDatabaseItem = redisDatabaseItem;
        }

        @Override
        protected List<RedisKey> call() throws Exception {
            try (StatefulRedisConnection<byte[], byte[]> connection = metadataProvider.getConnection(connectionConfiguration)) {
                RedisCommands<byte[], byte[]> commands = connection.sync();
                commands.select(database);
                Long size = commands.dbsize();
                ScanCursor scanCursor = ScanCursor.INITIAL;
                List<RedisKey> value = new ArrayList<>();
                ScanArgs limit = ScanArgs.Builder.limit(KEY_SCAN_SIZE);
                do {
                    KeyScanCursor<byte[]> cursor = commands.scan(scanCursor, limit);
                    scanCursor = ScanCursor.of(cursor.getCursor());
                    scanCursor.setFinished(cursor.isFinished());
                    List<byte[]> keys = cursor.getKeys();
                    List<RedisKey> collect = keys.stream().map(key -> {
                        RedisKey redisKey = new RedisKey();
                        String type = commands.type(key);
                        redisKey.setType(type);
                        Long ttl = commands.ttl(key);
                        redisKey.setTtl(ttl);
                        redisKey.setServer(connectionConfiguration);
                        redisKey.setDatabase(database);
                        redisKey.setKey(key);
                        return redisKey;
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

            List<RedisKey> value = getValue();
            logger.debug("redis keys {}", value);
            //TODO 添加到表格里

        }
    }

}
