package com.jean.database.redis;

import com.jean.database.redis.model.RedisKey;
import io.lettuce.core.api.StatefulRedisConnection;

import java.util.List;

public interface IRedisMetadataProvider {

    StatefulRedisConnection<byte[], byte[]> getConnection(RedisConnectionConfiguration configuration);

    List<Integer> getRedisDatabaseNumber(StatefulRedisConnection<byte[], byte[]> connection);

    List<RedisKey> getRedisKeys(StatefulRedisConnection<byte[], byte[]> connection, int dbIndex);
}
