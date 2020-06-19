package com.jean.database.redis;

import com.jean.database.api.utils.StringUtils;
import com.jean.database.redis.model.RedisKey;
import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.ByteArrayCodec;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RedisMetadataProvider implements IRedisMetadataProvider {

    public StatefulRedisConnection<byte[], byte[]> getConnection(RedisConnectionConfiguration configuration) {
        RedisURI.Builder builder = RedisURI.builder()
                .withHost(configuration.getHost())
                .withPort(configuration.getPort());
        if (configuration.getPassword() != null) {
            builder.withPassword(configuration.getPassword());
        }
        RedisURI redisURI = builder.build();
        RedisClient client = RedisClient.create(redisURI);
        return client.connect(ByteArrayCodec.INSTANCE);
    }


    public List<Integer> getRedisDatabaseNumber(StatefulRedisConnection<byte[], byte[]> connection) {
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

    @Override
    public List<RedisKey> getRedisKeys(StatefulRedisConnection<byte[], byte[]> connection, int dbIndex) {
        return this.getRedisKeys(connection, dbIndex, false, false);
    }

    public List<RedisKey> getRedisKeys(StatefulRedisConnection<byte[], byte[]> connection, int dbIndex, boolean typeAction, boolean ttlAction) {
        RedisCommands<byte[], byte[]> commands = connection.sync();
        commands.select(dbIndex);
        Long size = commands.dbsize();
        ScanCursor scanCursor = ScanCursor.INITIAL;
        List<RedisKey> value = new ArrayList<>();
        ScanArgs limit = ScanArgs.Builder.limit(1000);
        do {
            KeyScanCursor<byte[]> cursor = commands.scan(scanCursor, limit);
            scanCursor = ScanCursor.of(cursor.getCursor());
            scanCursor.setFinished(cursor.isFinished());
            List<byte[]> keys = cursor.getKeys();
            List<RedisKey> collect = keys.stream().map(key -> {
                RedisKey redisKey = new RedisKey();
                if (typeAction) {
                    String type = commands.type(key);
                    redisKey.setType(type);
                }
                if (ttlAction) {
                    Long ttl = commands.ttl(key);
                    redisKey.setTtl(ttl);
                }
                redisKey.setDatabase(dbIndex);
                redisKey.setKey(key);
                return redisKey;
            }).collect(Collectors.toList());
            value.addAll(collect);
        } while (!scanCursor.isFinished());
        value.sort(Comparator.comparing(o -> StringUtils.byteArrayToString(o.getKey())));
        return value;
    }
}
