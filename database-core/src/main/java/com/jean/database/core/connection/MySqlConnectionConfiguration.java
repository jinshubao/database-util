package com.jean.database.core.connection;


import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.core.meta.SchemaMetaData;

import java.util.Properties;

/**
 * @author jinshubao
 */
public class MySqlConnectionConfiguration extends AbstractConnectionConfiguration {

    static final String PROPERTY_SEPARATOR = "&";

    static final String URL_TEMPLATE = "jdbc:mysql://%s:%d";

    @Override
    public String getConnectionURL() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(URL_TEMPLATE, getHost(), getPort()));
        CatalogMetaData catalogMetaData = getCatalogMetaData();
        if (catalogMetaData != null) {
            String catalog = catalogMetaData.getTableCat();
            if (catalog != null && !catalog.isEmpty()) {
                builder.append("/").append(catalog);
            }
        }
        SchemaMetaData schemaMetaData = getSchemaMetaData();
        if (schemaMetaData != null) {
            String schema = schemaMetaData.getTableSchem();
            if (schema != null && !schema.isEmpty()) {
                builder.append("/").append(schema);
            }
        }
        String properties = getStringProperties();
        if (properties != null && !properties.isEmpty()) {
            builder.append("?").append(properties);
        }
        return builder.toString();
    }

    @Override
    public Properties getProperties() {
        String propertiesStr = getStringProperties();
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
}
