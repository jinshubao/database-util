package com.jean.database.core.constant;

import java.util.Map;
import java.util.Properties;

/**
 * @author jinshubao
 * @date 2017/4/9
 */

public enum DatabaseType {

    MySql("MySql", "com.mysql.jdbc.Driver") {

        static final String PROPERTY_SEPARATOR = "&";

        static final String URL_TEMPLATE = "jdbc:mysql://%s:%d";

        static final String DEFAULT_PROPERTIES = "serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false";

        @Override
        public String getConnectionURL(String host, Integer port, String user, String password, String catalog, String schema, String properties) {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format(URL_TEMPLATE, host, port));
            if (catalog != null && !catalog.isEmpty()) {
                builder.append("/").append(catalog);
            }
            if (schema != null && !schema.isEmpty()) {
                builder.append("/").append(schema);
            }
            if (properties != null && !properties.isEmpty()) {
                builder.append("?").append(properties);
            }
            return builder.toString();
        }

        @Override
        public Properties convertProperties(String propertiesStr) {
            if (propertiesStr == null || propertiesStr.isEmpty()) {
                return null;
            }
            Properties properties = new Properties();
            String[] props = propertiesStr.split(PROPERTY_SEPARATOR);
            for (String prop : props) {
                if (prop != null && !prop.isEmpty()) {
                    String[] split = prop.split("=");
                    if (split.length == 2) {
                        properties.put(split[0], split[1]);
                    }

                }
            }
            return properties;

        }

        @Override
        public String convertProperties(Properties properties) {
            if (properties.isEmpty()) {
                return "";
            }
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<Object, Object> objectEntry : properties.entrySet()) {
                builder.append(objectEntry.getKey()).append("=").append(objectEntry.getValue()).append(PROPERTY_SEPARATOR);
            }
            String props = builder.toString();
            if (props.endsWith(PROPERTY_SEPARATOR)) {
                props = props.substring(0, props.length() - PROPERTY_SEPARATOR.length() - 1);
            }
            return props;
        }

        @Override
        public String getDefaultProperties() {
            return DEFAULT_PROPERTIES;
        }
    },
    Oracle("Oracle", "oracle.jdbc.driver.OracleDriver") {

        static final String URL_TEMPLATE = "jdbc:oracle:thin:@//%s:%d";

        static final String DEFAULT_PROPERTIES = "";

        @Override
        public String getConnectionURL(String host, Integer port, String user, String password, String catalog, String schema, String properties) {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format(URL_TEMPLATE, host, port));
            if (catalog != null && !catalog.isEmpty()) {
                builder.append("/").append(catalog);
            }
            if (schema != null && !schema.isEmpty()) {
                builder.append("/").append(schema);
            }
            return builder.toString();
        }

        @Override
        public Properties convertProperties(String properties) {
            return null;
        }

        @Override
        public String convertProperties(Properties properties) {
            return "";
        }

        @Override
        public String getDefaultProperties() {
            return DEFAULT_PROPERTIES;
        }
    };

    private String name;
    private String driverClass;

    DatabaseType(String name, String driverClass) {
        this.driverClass = driverClass;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDriverClass() {
        return driverClass;
    }

    @Override
    public String toString() {
        return name;
    }

    public abstract String getConnectionURL(String host, Integer port, String user, String password, String catalog, String schema, String properties);

    public abstract Properties convertProperties(String properties);

    public abstract String convertProperties(Properties properties);

    public abstract String getDefaultProperties();
}