package com.jean.database.core.mysql;


import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.utils.StringUtil;

import java.util.Properties;

/**
 * @author jinshubao
 */
public class MySQLConnectionConfiguration implements IConnectionConfiguration {

    private static final String PROPERTY_SEPARATOR = "&";

    private static final String URL_TEMPLATE = "jdbc:mysql://%s:%d";

    private String customName;

    private String host;

    private Integer port;

    private String user;

    private String password;

    private Properties properties;

    public MySQLConnectionConfiguration(String customName, String host, Integer port, String user, String password, Properties properties) {
        this.customName = customName;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.properties = properties;
        if (this.properties == null) {
            this.properties = new Properties();
        }
        if (!this.properties.contains("serverTimezone")) {
            this.properties.put("serverTimezone", "UTC");
        }
        if (!this.properties.contains("useUnicode")) {
            this.properties.put("useUnicode", "true");
        }
        if (!this.properties.contains("characterEncoding")) {
            this.properties.put("characterEncoding", "utf8");
        }
    }

    @Override
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(URL_TEMPLATE, getHost(), getPort()));
        if (this.properties != null && !this.properties.isEmpty()) {
            builder.append("?");
            this.properties.forEach((key, value) -> builder.append(key).append("=").append(value).append(PROPERTY_SEPARATOR));
            builder.replace(builder.length() - 2, builder.length() - 1, "");
        }
        return builder.toString();
    }

    @Override
    public Properties getProperties() {
        return this.properties;
    }

    @Override
    public String toString() {
        return StringUtil.isNotBlank(this.customName) ? this.customName : (this.host + ":" + this.port);
    }
}
