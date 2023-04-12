package com.jean.database.oracle.config;


import com.jean.database.sql.SQLConnectionConfiguration;

import java.util.Properties;

/**
 * @author jinshubao
 */
public class OracleConnectionConfiguration extends SQLConnectionConfiguration {

    private static final String PROPERTY_SEPARATOR = "&";

    private static final String URL_TEMPLATE = "jdbc:mysql://%s:%d";

    public OracleConnectionConfiguration(String connectionName, String host, Integer port, String user, String password) {
        this(connectionName, host, port, user, password, new Properties());
    }

    public OracleConnectionConfiguration(String connectionName, String host, Integer port, String username, String password, Properties properties) {
        super(connectionName, host, port, username, password, properties);
        if (this.getProperties() != null) {
            if (!this.getProperties().contains("user") && username != null) {
                this.getProperties().put("user", username);
            }
            if (!this.getProperties().contains("password") && password != null) {
                this.getProperties().put("password", password);
            }
        }
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
