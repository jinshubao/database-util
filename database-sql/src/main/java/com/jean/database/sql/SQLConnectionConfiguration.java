package com.jean.database.sql;

import com.jean.database.api.IConnectionConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;

public abstract class SQLConnectionConfiguration implements IConnectionConfiguration {

    private final String connectionId;
    private String connectionName;
    private String host;
    private Integer port;
    private String username;
    private String password;
    private Properties properties;

    public SQLConnectionConfiguration(String connectionName, String host, Integer port, String username, String password) {
        this(connectionName, host, port, username, password, new Properties());
    }

    public SQLConnectionConfiguration(String connectionName, String host, Integer port, String username, String password, Properties properties) {
        this.connectionId = UUID.randomUUID().toString();
        this.connectionName = connectionName;
        this.host = host;
        this.port = port;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
