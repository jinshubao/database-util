package com.jean.database.core.mysql;

import com.jean.database.core.DefaultMetaDataProvider;

public class MySQLMetadataProvider extends DefaultMetaDataProvider {

    @Override
    public boolean supportCatalog() {
        return true;
    }

    @Override
    public boolean supportSchema() {
        return false;
    }
}
