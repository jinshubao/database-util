package com.jean.database.oracle;

import com.jean.database.core.*;

public class OracleDatabaseTypeProvider implements IDatabaseTypeProvider {

    private final IDatabaseType dataBaseType;

    private final IConfigurationProvider configurationProvider;

    private final IConnectionProvider connectionProvider;

    private final IMetadataProvider metadataProvider;

    private final IDataProvider dataProvider;


    public OracleDatabaseTypeProvider() {
        dataBaseType = new OracleDatabaseType("Oracle");
        this.configurationProvider = new OracleConfigurationProvider();
        connectionProvider = new DefaultConnectionProvider();
        metadataProvider = new OracleMetadataProvider();
        dataProvider = new OracleDataProvider();
    }

    @Override
    public IDatabaseType getDatabaseType() {
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
