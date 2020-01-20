package com.jean.database.core.mysql;

import com.jean.database.core.*;

public class MySQLDatabaseTypeProvider implements IDatabaseTypeProvider {

    private final IDataBaseType dataBaseType;

    private final IConfigurationProvider configurationProvider;

    private final IConnectionProvider connectionProvider;

    private final IMetadataProvider metadataProvider;

    private final IDataProvider dataProvider;


    public MySQLDatabaseTypeProvider() {
        dataBaseType = new MySQLDataBaseType("MySQL");
        this.configurationProvider = new MySQLConfigurationProvider();
        connectionProvider = new DefaultConnectionProvider();
        metadataProvider = new MySQLMetadataProvider();
        dataProvider = new MySQLDataProvider();
    }

    @Override
    public IDataBaseType getDataBaseType() {
        return dataBaseType;
    }

    @Override
    public IConfigurationProvider getConfigurationProvider() {
        return configurationProvider;
    }

    @Override
    public IConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }

    @Override
    public IMetadataProvider getMetadataProvider() {
        return metadataProvider;
    }

    @Override
    public IDataProvider getDataProvider() {
        return dataProvider;
    }

}
