package com.jean.database.mysql;

import com.jean.database.sql.SQLConnectionConfiguration;

import java.util.Properties;

public class MySQLConnectionConfiguration extends SQLConnectionConfiguration {

    private static final String PROPERTY_SEPARATOR = "&";

    private static final String URL_TEMPLATE = "jdbc:mysql://%s:%d";

    public MySQLConnectionConfiguration(String connectionName, String host, Integer port, String user, String password) {
        super(connectionName, host, port, user, password);
        this.getProperties().put("characterEncoding", "UTF-8");
        this.getProperties().put("allowMultiQueries", "true");
        this.getProperties().put("zeroDateTimeBehavior", "convertToNull");
        this.getProperties().put("serverTimezone", "UTC");
        this.getProperties().put("useSSL", "false");
        this.getProperties().put("remarks", "true");
        this.getProperties().put("useInformationSchema", "true");
        this.getProperties().put("user", user);
        this.getProperties().put("password", password);
    }

    public MySQLConnectionConfiguration(String connectionName, String host, Integer port, String user, String password, Properties properties) {
        super(connectionName, host, port, user, password, properties);
        this.getProperties().put("user", user);
        this.getProperties().put("password", password);
    }

    @Override
    public String getUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(URL_TEMPLATE, this.getHost(), this.getPort()));
        if (this.getProperties() != null && !this.getProperties().isEmpty()) {
            builder.append("?");
            this.getProperties().forEach((key, value) -> builder.append(key).append("=").append(value).append(PROPERTY_SEPARATOR));
            builder.replace(builder.length() - 2, builder.length() - 1, "");
        }
        return builder.toString();
    }

}
