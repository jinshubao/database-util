package com.jean.database.core;

public interface IDatabaseTypeProvider {

    IDatabaseType getDatabaseType();

    IConfigurationProvider getConfigurationProvider();

    IConnectionProvider getConnectionProvider();

    IMetadataProvider getMetadataProvider();

    IDataProvider getDataProvider();

}
