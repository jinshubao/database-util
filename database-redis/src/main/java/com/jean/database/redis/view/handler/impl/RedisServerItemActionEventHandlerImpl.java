package com.jean.database.redis.view.handler.impl;

import com.jean.database.api.BaseTask;
import com.jean.database.api.TaskManger;
import com.jean.database.api.utils.DialogUtil;
import com.jean.database.redis.IRedisMetadataProvider;
import com.jean.database.redis.RedisConnectionConfiguration;
import com.jean.database.redis.view.RedisDatabaseItem;
import com.jean.database.redis.view.RedisServerItem;
import com.jean.database.redis.view.handler.IRedisServerItemActionEventHandler;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.ArrayList;
import java.util.List;

public class RedisServerItemActionEventHandlerImpl implements IRedisServerItemActionEventHandler {

    private final RedisConnectionConfiguration connectionConfiguration;
    private final IRedisMetadataProvider metadataProvider;

    public RedisServerItemActionEventHandlerImpl(RedisConnectionConfiguration connectionConfiguration, IRedisMetadataProvider metadataProvider) {
        this.connectionConfiguration = connectionConfiguration;
        this.metadataProvider = metadataProvider;
    }

    @Override
    public void open(RedisServerItem treeItem) {
        if (!treeItem.isOpen()) {
            TaskManger.execute(new OpenRedisConnectionTask(treeItem));
        }
    }

    @Override
    public void close(RedisServerItem treeItem) {
        treeItem.getChildren().clear();
        treeItem.setOpen(false);
        treeItem.setExpanded(false);
    }

    @Override
    public void delete(RedisServerItem treeItem) {
        this.close(treeItem);
        treeItem.getParent().getChildren().remove(treeItem);

    }

    @Override
    public void property(RedisServerItem treeItem) {
        TaskManger.execute(new RedisServerInfoTask());
    }

    @Override
    public void onClick(RedisServerItem redisServerItem) {

    }

    @Override
    public void onDoubleClick(RedisServerItem redisServerItem) {
        this.open(redisServerItem);
    }

    @Override
    public void onSelected(RedisServerItem redisServerItem) {

        //TODO 切换界面展示


    }

    private class RedisServerInfoTask extends BaseTask<String> {

        @Override
        protected String call() throws Exception {
            updateProgress(0, 1);
            try (StatefulRedisConnection<byte[], byte[]> connection = metadataProvider.getConnection(connectionConfiguration)) {
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

    private class OpenRedisConnectionTask extends BaseTask<List<Integer>> {

        private final RedisServerItem redisServerItem;

        public OpenRedisConnectionTask(RedisServerItem redisServerItem) {
            this.redisServerItem = redisServerItem;
        }

        @Override
        protected List<Integer> call() throws Exception {
            try (StatefulRedisConnection<byte[], byte[]> connection = metadataProvider.getConnection(connectionConfiguration)) {
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
        protected void succeeded() {
            super.succeeded();
            List<Integer> number = getValue();
            for (Integer index : number) {
                RedisDatabaseItem databaseItem = new RedisDatabaseItem(index, redisServerItem.getViewContext(), connectionConfiguration, metadataProvider);
                redisServerItem.getChildren().add(databaseItem);
            }
            redisServerItem.setExpanded(true);
            redisServerItem.setOpen(true);
        }
    }
}
