package com.jean.database.core;

public interface IDatabaseTypeProvider {

    IDataBaseType getDataBaseType();

    IConfigurationProvider getConfigurationProvider();

    IConnectionProvider getConnectionProvider();

    IMetadataProvider getMetadataProvider();

    IDataProvider getDataProvider();

}
