package com.jean.database.core.connection;


import com.jean.database.core.constant.DatabaseType;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.core.meta.SchemaMetaData;
import com.jean.database.core.meta.TableMetaData;

/**
 * @author jinshubao
 */
public abstract class AbstractConnectionConfiguration implements IConnectionConfiguration {

    private DatabaseType databaseType;

    private String host;

    private Integer port;

    private String user;

    private String password;

    private CatalogMetaData catalogMetaData;

    private SchemaMetaData schemaMetaData;

    private TableMetaData tableMetaData;

    private String stringProperties;

    public AbstractConnectionConfiguration() {
    }

    public AbstractConnectionConfiguration(DatabaseType databaseType, String host, Integer port, String user, String password, String stringProperties) {
        this.databaseType = databaseType;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.stringProperties = stringProperties;
    }

    @Override
    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
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

    public CatalogMetaData getCatalogMetaData() {
        return catalogMetaData;
    }

    public void setCatalogMetaData(CatalogMetaData catalogMetaData) {
        this.catalogMetaData = catalogMetaData;
    }

    public SchemaMetaData getSchemaMetaData() {
        return schemaMetaData;
    }

    public void setSchemaMetaData(SchemaMetaData schemaMetaData) {
        this.schemaMetaData = schemaMetaData;
    }

    public TableMetaData getTableMetaData() {
        return tableMetaData;
    }

    public void setTableMetaData(TableMetaData tableMetaData) {
        this.tableMetaData = tableMetaData;
    }

    public String getStringProperties() {
        return stringProperties;
    }

    public void setStringProperties(String stringProperties) {
        this.stringProperties = stringProperties;
    }

    @Override
    public String toString() {
        return databaseType.getName();
    }

}
