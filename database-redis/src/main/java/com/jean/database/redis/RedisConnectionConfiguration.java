package com.jean.database.redis;

import com.jean.database.api.IConnectionConfiguration;
import com.jean.database.api.action.ICloseable;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;

import java.util.Properties;
import java.util.UUID;

public class RedisConnectionConfiguration implements IConnectionConfiguration, ICloseable {

    private final String connectionId;
    private String connectionName;
    private String host;
    private Integer port;
    private String password;
    private Properties properties;

    private RedisClient client;


    public RedisConnectionConfiguration(String connectionName, String host, Integer port, String password) {
        this(connectionName, host, port, password, null);
    }

    public RedisConnectionConfiguration(String connectionName, String host, Integer port, String password, Properties properties) {
        this.connectionId = UUID.randomUUID().toString();
        this.connectionName = connectionName;
        this.host = host;
        this.port = port;
        this.password = password;
        this.properties = properties;
    }


    @Override
    public String getConnectionId() {
        return connectionId;
    }

    @Override
    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public String getUrl() {
        return null;
    }

    public StatefulRedisConnection<byte[], byte[]> getConnection() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    RedisURI.Builder builder = RedisURI.builder()
                            .withHost(getHost())
                            .withPort(getPort());
                    if (getPassword() != null) {
                        builder.withPassword(getPassword());
                    }
                    client = RedisClient.create(builder.build());
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

    @Override
    public String toString() {
        return this.connectionName;
    }

}
