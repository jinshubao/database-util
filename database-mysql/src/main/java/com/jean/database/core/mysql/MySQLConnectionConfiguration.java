package com.jean.database.core.mysql;


import com.jean.database.core.IConnectionConfiguration;

import java.util.Properties;

/**
 * @author jinshubao
 */
public class MySQLConnectionConfiguration implements IConnectionConfiguration {

    private static final String PROPERTY_SEPARATOR = "&";

    private static final String URL_TEMPLATE = "jdbc:mysql://%s:%d";

    private String connectionName;

    private String host;

    private Integer port;

    private String user;

    private String password;

    private Properties properties;

    public MySQLConnectionConfiguration(String connectionName, String host, Integer port, String user, String password, Properties properties) {
        this.connectionName = connectionName;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.properties = new Properties();
        if (properties != null) {
            this.properties.putAll(properties);
        }
        if (!this.properties.contains("user") && user != null) {
            this.properties.put("user", user);
        }
        if (!this.properties.contains("password") && password != null) {
            this.properties.put("password", password);
        }
    }

    @Override
    public String getConnectionName() {
        return connectionName;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public Properties getProperties() {
        return properties;
    }

    @Override
    public String getUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(URL_TEMPLATE, host, port));
        if (this.properties != null && !this.properties.isEmpty()) {
            builder.append("?");
            this.properties.forEach((key, value) -> builder.append(key).append("=").append(value).append(PROPERTY_SEPARATOR));
            builder.replace(builder.length() - 2, builder.length() - 1, "");
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return "MySQLConnectionConfiguration{" +
                "connectionName='" + connectionName + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", properties=" + properties +
                '}';
    }
}
