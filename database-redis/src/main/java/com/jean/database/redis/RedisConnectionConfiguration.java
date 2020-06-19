package com.jean.database.redis;

import com.jean.database.api.AbstractConnectionConfiguration;

public class RedisConnectionConfiguration extends AbstractConnectionConfiguration {

    public RedisConnectionConfiguration(String connectionName, String host, Integer port, String password) {
        super(connectionName, host, port, null, password);
    }


    @Override
    public String getUrl() {
        return null;
    }
}
