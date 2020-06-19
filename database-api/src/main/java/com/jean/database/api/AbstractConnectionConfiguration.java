package com.jean.database.api;

import java.util.Properties;
import java.util.UUID;

/**
 * @author jinshubao
 */
public abstract class AbstractConnectionConfiguration implements IConnectionConfiguration {

    private final String connectionId;

    private String connectionName;
    private String host;
    private Integer port;
    private String user;
    private String password;
    private Properties properties;

    public AbstractConnectionConfiguration(String connectionName, String host, Integer port, String user, String password) {
        this(connectionName, host, port, user, password, new Properties());
    }

    public AbstractConnectionConfiguration(String connectionName, String host, Integer port, String user, String password, Properties properties) {
        this.connectionId = UUID.randomUUID().toString();
        this.connectionName = connectionName;
        this.host = host;
        this.port = port;
        this.user = user;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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
    public String toString() {
        return this.connectionName;
    }


}
