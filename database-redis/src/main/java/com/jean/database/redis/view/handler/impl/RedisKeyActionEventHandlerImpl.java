package com.jean.database.redis.view.handler.impl;

import com.jean.database.api.BaseTask;
import com.jean.database.api.TaskManger;
import com.jean.database.redis.RedisConnectionConfiguration;
import com.jean.database.redis.RedisConstant;
import com.jean.database.redis.RedisObjectTabController;
import com.jean.database.redis.model.RedisKey;
import com.jean.database.redis.model.RedisValue;
import com.jean.database.redis.model.RedisValueWrapper;
import com.jean.database.redis.view.handler.IRedisKeyActionEventHandler;
import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import javafx.scene.control.TableRow;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RedisKeyActionEventHandlerImpl implements IRedisKeyActionEventHandler {

    private final RedisObjectTabController objectTabController;

    public RedisKeyActionEventHandlerImpl(RedisObjectTabController objectTabController) {
        this.objectTabController = objectTabController;
    }

    @Override
    public void copy(TableRow<RedisKey> tableRow) {
        RedisKey redisKey = tableRow.getItem();
        Clipboard clipboard = Clipboard.getSystemClipboard();
        Map<DataFormat, Object> content = new HashMap<>();
        content.put(DataFormat.PLAIN_TEXT, new String(redisKey.getKey()));
        clipboard.setContent(content);
    }

    @Override
    public void delete(TableRow<RedisKey> tableRow) {
        tableRow.getTableView().getItems().remove(tableRow.getItem());
    }

    @Override
    public void onClick(TableRow<RedisKey> tableRow) {

    }

    @Override
    public void onDoubleClick(TableRow<RedisKey> tableRow) {

    }

    @Override
    public void onSelected(TableRow<RedisKey> tableRow) {
        RedisKey item = tableRow.getItem();
        TaskManger.execute(new RedisValueTask(item, objectTabController));
    }

    private static class RedisValueTask extends BaseTask<RedisValueWrapper> {

        private final RedisObjectTabController objectTabController;
        private final RedisConnectionConfiguration configuration;
        private final int database;
        private final byte[] key;

        public RedisValueTask(RedisKey redisKey, RedisObjectTabController objectTabController) {
            this.configuration = redisKey.getConnectionConfiguration();
            this.database = redisKey.getDatabase();
            this.key = redisKey.getKey();
            this.objectTabController = objectTabController;
        }

        @Override
        protected void scheduled() {
            super.scheduled();
            updateTitle("获取value");
        }

        @Override
        protected RedisValueWrapper call() throws Exception {
            try (StatefulRedisConnection<byte[], byte[]> connection = configuration.getConnection()) {
                RedisCommands<byte[], byte[]> commands = connection.sync();
                commands.select(database);
                String type = commands.type(key);

                if (RedisConstant.KeyType.STRING.equalsIgnoreCase(type)) {
                    return this.getStringValue(commands);
                }
                if (RedisConstant.KeyType.LIST.equalsIgnoreCase(type)) {
                    return this.getListValue(commands);
                }
                if (RedisConstant.KeyType.SET.equalsIgnoreCase(type)) {
                    return this.getSetValue(commands);
                }
                if (RedisConstant.KeyType.ZSET.equalsIgnoreCase(type)) {
                    return this.getScoredSetValue(commands);
                }
                if (RedisConstant.KeyType.HASH.equalsIgnoreCase(type)) {
                    return this.getHashValue(commands);
                }
                logger.debug("类型[{}]未实现", type);
                return null;
            }
        }

        private RedisValueWrapper getStringValue(RedisCommands<byte[], byte[]> commands) {
            byte[] value = commands.get(key);
            Long ttl = commands.ttl(key);
            updateProgress(1L, 1L);
            ArrayList<RedisValue> list = new ArrayList<>();
            list.add(new RedisValue(key, value));
            return new RedisValueWrapper(configuration, key, RedisConstant.KeyType.STRING, ttl, 1L, list);
        }

        private RedisValueWrapper getHashValue(RedisCommands<byte[], byte[]> commands) {
            Long size = commands.hlen(key);
            Long ttl = commands.ttl(key);
            List<RedisValue> value = new ArrayList<>();
            if (size > 0) {
                ScanArgs scanArgs = ScanArgs.Builder.limit(RedisConstant.VALUE_SCAN_SIZE);
                ScanCursor scanCursor = ScanCursor.INITIAL;
                do {
                    MapScanCursor<byte[], byte[]> cursor = commands.hscan(key, scanCursor, scanArgs);
                    scanCursor = ScanCursor.of(cursor.getCursor());
                    scanCursor.setFinished(cursor.isFinished());
                    cursor.getMap().forEach((k, v) -> value.add(new RedisValue(k, v)));
                    updateProgress(size, value.size());
                    if (isCancelled()) {
                        break;
                    }
                } while (!scanCursor.isFinished());
            }
            return new RedisValueWrapper(configuration, key, RedisConstant.KeyType.HASH, ttl, size, value);
        }


        private RedisValueWrapper getListValue(RedisCommands<byte[], byte[]> commands) {
            Long size = commands.llen(key);
            Long ttl = commands.ttl(key);
            long scanSize = RedisConstant.VALUE_SCAN_SIZE;
            if (size <= scanSize) {
                List<RedisValue> value = commands.lrange(key, 0, -1).stream().map(item -> new RedisValue(null, item)).collect(Collectors.toList());
                return new RedisValueWrapper(configuration, key, RedisConstant.KeyType.LIST, ttl, size, value);
            }
            List<RedisValue> value = new ArrayList<>();
            long start = 0;
            long stop = start + scanSize - 1;
            List<byte[]> list;
            while (!(list = commands.lrange(key, start, stop)).isEmpty()) {
                List<RedisValue> ls = list.stream().map(item -> new RedisValue(null, item)).collect(Collectors.toList());
                value.addAll(ls);
                updateProgress(value.size(), size);
                if (isCancelled()) {
                    break;
                }
            }
            return new RedisValueWrapper(configuration, key, RedisConstant.KeyType.LIST, ttl, size, value);
        }


        private RedisValueWrapper getScoredSetValue(RedisCommands<byte[], byte[]> commands) {
            Long size = commands.llen(key);
            Long ttl = commands.ttl(key);
            List<RedisValue> value = new ArrayList<>();
            if (size > 0) {
                ScanArgs scanArgs = ScanArgs.Builder.limit(RedisConstant.VALUE_SCAN_SIZE);
                ScanCursor scanCursor = ScanCursor.INITIAL;
                do {
                    ScoredValueScanCursor<byte[]> cursor = commands.zscan(key, scanCursor, scanArgs);
                    scanCursor = ScanCursor.of(cursor.getCursor());
                    scanCursor.setFinished(cursor.isFinished());
                    List<RedisValue> ls = cursor.getValues().stream().map(item -> new RedisValue(null, item.getValue(), item.getScore())).collect(Collectors.toList());
                    value.addAll(ls);
                    updateProgress(value.size(), size);
                    if (isCancelled()) {
                        break;
                    }
                } while (!scanCursor.isFinished());
            }
            return new RedisValueWrapper(configuration, key, RedisConstant.KeyType.ZSET, ttl, size, value);
        }


        private RedisValueWrapper getSetValue(RedisCommands<byte[], byte[]> commands) {
            Long size = commands.scard(key);
            Long ttl = commands.ttl(key);
            List<RedisValue> value = new ArrayList<>();
            if (size > 0) {
                ScanArgs scanArgs = ScanArgs.Builder.limit(RedisConstant.VALUE_SCAN_SIZE);
                ScanCursor scanCursor = ScanCursor.INITIAL;
                do {
                    ValueScanCursor<byte[]> cursor = commands.sscan(key, scanCursor, scanArgs);
                    scanCursor = ScanCursor.of(cursor.getCursor());
                    scanCursor.setFinished(cursor.isFinished());
                    List<RedisValue> ls = cursor.getValues().stream().map(item -> new RedisValue(null, item)).collect(Collectors.toList());
                    value.addAll(ls);
                    updateProgress(value.size(), size);
                    if (isCancelled()) {
                        break;
                    }
                } while (!scanCursor.isFinished());
            }
            return new RedisValueWrapper(configuration, key, RedisConstant.KeyType.SET, ttl, size, value);
        }


        @Override
        protected void succeeded() {
            super.succeeded();
            RedisValueWrapper value = getValue();
            objectTabController.updateValueTableView(value.getValues());
        }

        @Override
        public String toString() {
            return "getValue-task-" + super.toString();
        }
    }

}
