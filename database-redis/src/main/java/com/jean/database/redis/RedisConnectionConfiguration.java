package com.jean.database.redis;

import com.jean.database.api.AbstractConnectionConfiguration;
import com.jean.database.api.view.action.ICloseable;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;

public class RedisConnectionConfiguration extends AbstractConnectionConfiguration implements ICloseable {

    private RedisClient client;

    public RedisConnectionConfiguration(String connectionName, String host, Integer port, String password) {
        super(connectionName, host, port, null, password);
    }

    @Override
    public String getUrl() {
        return null;
    }


    public StatefulRedisConnection<byte[], byte[]> getConnection() {
        RedisURI.Builder builder = RedisURI.builder()
                .withHost(getHost())
                .withPort(getPort());
        if (getPassword() != null) {
            builder.withPassword(getPassword());
        }

        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    RedisURI redisURI = builder.build();
                    client = RedisClient.create(redisURI);
                }
            }
        }
        return client.connect(ByteArrayCodec.INSTANCE);
    }


    @Override
    public void close() {
        if (client != null) {
            client.shutdown();
        }
    }
}
