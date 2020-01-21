package com.jean.database.oracle;

import com.jean.database.core.DefaultMetaDataProvider;

public class OracleMetadataProvider extends DefaultMetaDataProvider {

    @Override
    public boolean supportCatalog() {
        return true;
    }

    @Override
    public boolean supportSchema() {
        return false;
    }
}
